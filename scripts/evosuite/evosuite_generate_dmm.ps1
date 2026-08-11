param(
    [int]$BudgetSeconds = 180,
    [string]$AsmVersion = "9.9"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common_dmm.ps1")

Write-Section "EvoSuite GENERATE - DefaultMappingManager - REAL Java 21"
Show-Configuration

$expectedTargetRelative = "org\apache\syncope\core\provisioning\java\DefaultMappingManager"

if ($script:TargetClassRelative -ne $expectedTargetRelative) {
    throw @"
Configurazione CUT DMM incoerente.

TargetClassRelative trovato:
    $script:TargetClassRelative

TargetClassRelative atteso:
    $expectedTargetRelative

Interrompo PRIMA di Maven/EvoSuite.
"@
}

if (-not (Test-Path -LiteralPath $script:TargetSource -PathType Leaf)) {
    throw "Sorgente della CUT non trovato: $script:TargetSource"
}

Write-Host "Preflight path CUT: OK"
Write-Host "  $script:TargetSource"

if (-not (Test-Path -LiteralPath $script:EvoSuiteJar -PathType Leaf)) {
    throw "JAR EvoSuite non trovato: $script:EvoSuiteJar"
}

function Ensure-Directory {
    param([Parameter(Mandatory=$true)][string]$Path)
    [void][System.IO.Directory]::CreateDirectory($Path)
}

function Get-FreshReactorClassDirectories {
    param([Parameter(Mandatory=$true)][datetime]$ModifiedSince)

    $result = New-Object System.Collections.Generic.List[string]

    $dirs = @(
    Get-ChildItem `
            -Path $script:RepoRoot `
            -Directory `
            -Recurse `
            -Filter "classes" `
            -ErrorAction SilentlyContinue |
            Where-Object {
                $_.Parent.Name -eq "target" -and
                        -not $_.FullName.StartsWith(
                                $script:DmmGenerationRoot,
                                [System.StringComparison]::OrdinalIgnoreCase
                        ) -and
                        -not $_.FullName.StartsWith(
                                $script:AsmOverlayRoot,
                                [System.StringComparison]::OrdinalIgnoreCase
                        )
            }
    )

    foreach ($dir in $dirs) {
        $freshClass = Get-ChildItem `
            -LiteralPath $dir.FullName `
            -File `
            -Recurse `
            -Filter "*.class" `
            -ErrorAction SilentlyContinue |
                Where-Object { $_.LastWriteTime -ge $ModifiedSince } |
                Select-Object -First 1

        if ($null -ne $freshClass) {
            [void]$result.Add($dir.FullName)
        }
    }

    return @($result | Select-Object -Unique)
}

function Build-EvoSuiteAsmOverlay {
    param([Parameter(Mandatory=$true)][string]$Version)

    Write-Section "3. Preparazione ASM moderno per EvoSuite 1.2.0"

    if (Test-Path -LiteralPath $script:AsmOverlayRoot) {
        Remove-Item -LiteralPath $script:AsmOverlayRoot -Recurse -Force
    }
    Ensure-Directory $script:AsmOverlayRoot

    # EvoSuite 1.2.0 usa i package ASM rilocati sotto:
    #   org.evosuite.shaded.org.objectweb.asm
    #
    # Costruiamo un piccolo JAR overlay con ASM moderno nello STESSO package.
    # Mettendolo PRIMA di evosuite-1.2.0.jar, il classloader usa queste classi
    # al posto dell'ASM 9.2 incorporato da EvoSuite.
    $pom = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>local.evosuite</groupId>
  <artifactId>evosuite-asm-java21-overlay</artifactId>
  <version>1.0</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.ow2.asm</groupId>
      <artifactId>asm</artifactId>
      <version>$Version</version>
    </dependency>
    <dependency>
      <groupId>org.ow2.asm</groupId>
      <artifactId>asm-commons</artifactId>
      <version>$Version</version>
    </dependency>
    <dependency>
      <groupId>org.ow2.asm</groupId>
      <artifactId>asm-tree</artifactId>
      <version>$Version</version>
    </dependency>
    <dependency>
      <groupId>org.ow2.asm</groupId>
      <artifactId>asm-analysis</artifactId>
      <version>$Version</version>
    </dependency>
    <dependency>
      <groupId>org.ow2.asm</groupId>
      <artifactId>asm-util</artifactId>
      <version>$Version</version>
    </dependency>
  </dependencies>

  <build>
    <finalName>evosuite-asm-java21-overlay</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.6.1</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <createDependencyReducedPom>false</createDependencyReducedPom>
              <minimizeJar>false</minimizeJar>
              <relocations>
                <relocation>
                  <pattern>org.objectweb.asm</pattern>
                  <shadedPattern>org.evosuite.shaded.org.objectweb.asm</shadedPattern>
                </relocation>
              </relocations>
              <filters>
                <filter>
                  <artifact>*:*</artifact>
                  <excludes>
                    <exclude>module-info.class</exclude>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                  </excludes>
                </filter>
              </filters>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
"@

    # Windows PowerShell 5.1 usa BOM con -Encoding UTF8.
    # Scriviamo esplicitamente UTF-8 senza BOM.
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText(
            $script:AsmOverlayPom,
            $pom,
            $utf8NoBom
    )

    # IMPORTANTE: Invoke-Maven inoltra l'output Maven nello Success Stream.
    # Se lo lasciassimo libero, la funzione restituirebbe un array composto da
    # tutte le righe Maven + il path dell'overlay. Il chiamante si aspetta invece
    # UNA SOLA stringa. Out-Host mantiene i log visibili ma li rimuove dal valore
    # di ritorno della funzione.
    Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:AsmOverlayRoot `
        -Arguments @(
        "-f", $script:AsmOverlayPom,
        "-DskipTests=true",
        "package"
    ) | Out-Host

    $overlay = Join-Path `
        $script:AsmOverlayTarget `
        "evosuite-asm-java21-overlay.jar"

    if (-not (Test-Path -LiteralPath $overlay -PathType Leaf)) {
        throw "ASM overlay non prodotto: $overlay"
    }

    Add-Type -AssemblyName System.IO.Compression.FileSystem

    $zip = [System.IO.Compression.ZipFile]::OpenRead($overlay)
    try {
        $classReader = $zip.Entries | Where-Object {
            $_.FullName -eq "org/evosuite/shaded/org/objectweb/asm/ClassReader.class"
        } | Select-Object -First 1

        if ($null -eq $classReader) {
            throw @"
L'overlay ASM è stato creato ma non contiene:
    org/evosuite/shaded/org/objectweb/asm/ClassReader.class
"@
        }
    }
    finally {
        $zip.Dispose()
    }

    Write-Host "ASM overlay creato:"
    Write-Host "  $overlay"
    Write-Host "ASM version: $Version"

    return $overlay
}

function Run-GeneratorPreflight {
    param(
        [Parameter(Mandatory=$true)][string]$OverlayJar,
        [Parameter(Mandatory=$true)][string]$ProjectCp
    )

    Write-Section "4. Preflight ASM / classpath PRIMA di EvoSuite"

    $checkDir = Join-Path $script:DmmGenerationRoot "preflight"
    if (Test-Path -LiteralPath $checkDir) {
        Remove-Item -LiteralPath $checkDir -Recurse -Force
    }
    Ensure-Directory $checkDir

    $source = Join-Path $checkDir "EvoSuiteDmmPreflight.java"

    $javaSource = @'
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.evosuite.shaded.org.objectweb.asm.ClassReader;

public final class EvoSuiteDmmPreflight {

    public static void main(String[] args) throws Exception {
        URL asmSource = ClassReader.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        System.out.println("ASM_SOURCE=" + asmSource);

        if (asmSource == null ||
                !asmSource.toString().contains("evosuite-asm-java21-overlay")) {
            throw new IllegalStateException(
                    "ASM non caricato dall'overlay: " + asmSource);
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL membership = cl.getResource(
                "org/apache/syncope/common/lib/to/MembershipTO.class");

        System.out.println("MEMBERSHIP_RESOURCE=" + membership);

        if (membership == null) {
            throw new IllegalStateException(
                    "MembershipTO non visibile al context classloader");
        }

        byte[] cut = Files.readAllBytes(Path.of(args[0]));
        ClassReader reader = new ClassReader(cut);

        System.out.println("CUT_ASM_NAME=" + reader.getClassName());

        if (!"org/apache/syncope/core/provisioning/java/DefaultMappingManager"
                .equals(reader.getClassName())) {
            throw new IllegalStateException(
                    "ASM non ha letto la CUT attesa");
        }
    }
}
'@

    # CRITICO SU WINDOWS POWERSHELL 5.1:
    # Set-Content -Encoding UTF8 aggiunge BOM (EF BB BF).
    # javac vede U+FEFF prima del primo import e fallisce con:
    #   illegal character: '\ufeff'
    #
    # Scriviamo quindi il sorgente Java in UTF-8 SENZA BOM.
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText(
            $source,
            $javaSource,
            $utf8NoBom
    )

    # Preflight aggiuntivo: i primi tre byte NON devono essere EF BB BF.
    $sourceBytes = [System.IO.File]::ReadAllBytes($source)
    if ($sourceBytes.Length -ge 3 -and
            $sourceBytes[0] -eq 0xEF -and
            $sourceBytes[1] -eq 0xBB -and
            $sourceBytes[2] -eq 0xBF) {
        throw "Il sorgente Java del preflight contiene ancora UTF-8 BOM: $source"
    }

    Write-Host "Encoding preflight Java: UTF-8 senza BOM -> OK"

    $javac21 = Join-Path $script:Jdk21Home "bin\javac.exe"
    $java21 = Join-Path $script:Jdk21Home "bin\java.exe"

    $runtimeCp = "$OverlayJar;$($script:EvoSuiteJar);$ProjectCp"

    & $javac21 `
        -encoding UTF-8 `
        -cp $runtimeCp `
        -d $checkDir `
        $source

    if ($LASTEXITCODE -ne 0) {
        throw "Compilazione del preflight ASM fallita."
    }

    & $java21 `
        -cp "$checkDir;$runtimeCp" `
        "EvoSuiteDmmPreflight" `
        $script:RealClassFile

    if ($LASTEXITCODE -ne 0) {
        throw "Preflight ASM/classpath fallito."
    }

    Write-Host ""
    Write-Host "PRECHECK COMPLETATO:"
    Write-Host "  ASM overlay caricato       -> OK"
    Write-Host "  MembershipTO visibile      -> OK"
    Write-Host "  CUT major 65 letta da ASM  -> OK"
}



function Build-SystemClassBridge {
    param([Parameter(Mandatory=$true)][string]$ProjectCp)

    Write-Section "5. Costruzione class-only bridge JAR per i thread EvoSuite"

    $bridgeDir = Join-Path $script:DmmGenerationRoot "system-class-bridge"
    $bridgeJar = Join-Path $bridgeDir "evosuite-dmm-system-class-bridge.jar"

    if (Test-Path -LiteralPath $bridgeDir) {
        Remove-Item -LiteralPath $bridgeDir -Recurse -Force
    }

    Ensure-Directory $bridgeDir

    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem

    $entries = @(
    $ProjectCp -split [regex]::Escape([System.IO.Path]::PathSeparator) |
            Where-Object { -not [string]::IsNullOrWhiteSpace($_) }
    )

    $jarEntries = @(
    $entries |
            Where-Object {
                (Test-Path -LiteralPath $_ -PathType Leaf) -and
                        ([System.IO.Path]::GetExtension($_) -ieq ".jar")
            }
    )

    $dirEntries = @(
    $entries |
            Where-Object {
                Test-Path -LiteralPath $_ -PathType Container
            }
    )

    $added = New-Object 'System.Collections.Generic.HashSet[string]' (
    [System.StringComparer]::OrdinalIgnoreCase
    )

    $output = [System.IO.File]::Open(
            $bridgeJar,
            [System.IO.FileMode]::Create,
            [System.IO.FileAccess]::ReadWrite,
            [System.IO.FileShare]::None
    )

    $zipOut = New-Object System.IO.Compression.ZipArchive(
    $output,
    [System.IO.Compression.ZipArchiveMode]::Create,
    $false
    )

    $dirClassCount = 0
    $jarClassCount = 0

    try {
        # Prima le classi appena compilate dal reactor:
        # devono avere precedenza su eventuali copie SNAPSHOT nelle JAR Maven.
        foreach ($dir in $dirEntries) {
            $dirFull = (Resolve-Path $dir).Path

            $classFiles = @(
                Get-ChildItem `
                    -LiteralPath $dirFull `
                    -File `
                    -Recurse `
                    -Filter "*.class"
            )

            foreach ($classFile in $classFiles) {
                $relative = $classFile.FullName.Substring(
                        $dirFull.Length
                ).TrimStart('\').Replace('\', '/')

                if ($relative.StartsWith(
                        "META-INF/",
                        [System.StringComparison]::OrdinalIgnoreCase
                )) {
                    continue
                }

                if ($relative.StartsWith(
                        "org/evosuite/",
                        [System.StringComparison]::OrdinalIgnoreCase
                )) {
                    continue
                }

                if (-not $added.Add($relative)) {
                    continue
                }

                $entryOut = $zipOut.CreateEntry(
                        $relative,
                        [System.IO.Compression.CompressionLevel]::Fastest
                )

                $sourceStream = [System.IO.File]::OpenRead(
                        $classFile.FullName
                )

                try {
                    $targetStream = $entryOut.Open()

                    try {
                        $sourceStream.CopyTo($targetStream)
                    }
                    finally {
                        $targetStream.Dispose()
                    }
                }
                finally {
                    $sourceStream.Dispose()
                }

                $dirClassCount++
            }
        }

        # Poi le dipendenze JAR. Copiamo solo .class base:
        # niente META-INF/services, niente configurazioni Logback,
        # niente risorse applicative.
        foreach ($jar in $jarEntries) {
            $zipIn = [System.IO.Compression.ZipFile]::OpenRead($jar)

            try {
                foreach ($entry in $zipIn.Entries) {
                    if ([string]::IsNullOrWhiteSpace($entry.Name)) {
                        continue
                    }

                    if (-not $entry.FullName.EndsWith(
                            ".class",
                            [System.StringComparison]::OrdinalIgnoreCase
                    )) {
                        continue
                    }

                    if ($entry.FullName.StartsWith(
                            "META-INF/",
                            [System.StringComparison]::OrdinalIgnoreCase
                    )) {
                        continue
                    }

                    if ($entry.FullName.StartsWith(
                            "org/evosuite/",
                            [System.StringComparison]::OrdinalIgnoreCase
                    )) {
                        continue
                    }

                    $relative = $entry.FullName.Replace('\', '/')

                    if (-not $added.Add($relative)) {
                        continue
                    }

                    $entryOut = $zipOut.CreateEntry(
                            $relative,
                            [System.IO.Compression.CompressionLevel]::Fastest
                    )

                    $sourceStream = $entry.Open()

                    try {
                        $targetStream = $entryOut.Open()

                        try {
                            $sourceStream.CopyTo($targetStream)
                        }
                        finally {
                            $targetStream.Dispose()
                        }
                    }
                    finally {
                        $sourceStream.Dispose()
                    }

                    $jarClassCount++
                }
            }
            finally {
                $zipIn.Dispose()
            }
        }
    }
    finally {
        $zipOut.Dispose()
        $output.Dispose()
    }

    if (-not (Test-Path -LiteralPath $bridgeJar -PathType Leaf)) {
        throw "Class-only bridge JAR non prodotto: $bridgeJar"
    }

    # Verifica contenuto direttamente nel JAR: nessuna estrazione.
    $verifyZip = [System.IO.Compression.ZipFile]::OpenRead($bridgeJar)

    try {
        $membershipEntry = $verifyZip.GetEntry(
                "org/apache/syncope/common/lib/to/MembershipTO.class"
        )

        $dmmEntry = $verifyZip.GetEntry(
                "org/apache/syncope/core/provisioning/java/DefaultMappingManager.class"
        )

        if ($null -eq $membershipEntry) {
            throw "MembershipTO assente dal class-only bridge JAR."
        }

        if ($null -eq $dmmEntry) {
            throw "DefaultMappingManager assente dal class-only bridge JAR."
        }
    }
    finally {
        $verifyZip.Dispose()
    }

    Write-Host "Class-only bridge JAR creato:"
    Write-Host "  $bridgeJar"
    Write-Host "Classi reactor aggiunte : $dirClassCount"
    Write-Host "Classi JAR aggiunte     : $jarClassCount"
    Write-Host "Entry totali uniche     : $($added.Count)"
    Write-Host "MembershipTO nel bridge : OK"
    Write-Host "DMM nel bridge          : OK"
    Write-Host ""
    Write-Host "File estratti su filesystem: ZERO"
    Write-Host "META-INF/services copiati   : ZERO"
    Write-Host "Configurazioni Logback      : ZERO"

    return $bridgeJar
}

function Build-IsolatedEvoSuiteLauncher {
    param(
        [Parameter(Mandatory=$true)][string]$OverlayJar,
        [Parameter(Mandatory=$true)][string]$ProjectCpFile
    )

    Write-Section "5. Preparazione launcher isolato EvoSuite"

    $launcherDir = Join-Path $script:DmmGenerationRoot "isolated-launcher"

    if (Test-Path -LiteralPath $launcherDir) {
        Remove-Item -LiteralPath $launcherDir -Recurse -Force
    }

    Ensure-Directory $launcherDir

    $launcherSource = Join-Path $launcherDir "EvoSuiteIsolatedLauncher.java"

    $launcherJava = @'
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.evosuite.EvoSuite;
import org.slf4j.LoggerFactory;

public final class EvoSuiteIsolatedLauncher {

    private static final class ProjectContextClassLoader extends URLClassLoader {

        ProjectContextClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        private static boolean blockedResource(String name) {
            if (name == null) {
                return false;
            }

            // Non permettiamo alle dipendenze applicative di registrare provider
            // nel runtime del tool (es. Spring Boot RootLogLevelConfigurator).
            if (name.startsWith("META-INF/services/")) {
                return true;
            }

            // Evita anche che configurazioni Logback dell'applicazione
            // interferiscano con il logging interno di EvoSuite.
            return name.equals("logback.xml")
                    || name.equals("logback-test.xml")
                    || name.equals("logback.groovy")
                    || name.equals("logback-test.groovy");
        }

        @Override
        public URL getResource(String name) {
            if (blockedResource(name)) {
                ClassLoader parent = getParent();
                return parent == null ? null : parent.getResource(name);
            }
            return super.getResource(name);
        }

        @Override
        public Enumeration<URL> getResources(String name) throws IOException {
            if (blockedResource(name)) {
                ClassLoader parent = getParent();
                return parent == null
                        ? Collections.emptyEnumeration()
                        : parent.getResources(name);
            }
            return super.getResources(name);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "Manca il file contenente il project classpath");
        }

        /*
         * ComputeClassWriter di EvoSuite usa il context loader del thread.
         * I thread interni possono ereditare/riusare il system loader invece
         * del project context loader. Il class-only bridge deve quindi rendere
         * almeno le classi del SUT visibili anche da qui.
         */
        URL systemMembership = ClassLoader.getSystemClassLoader().getResource(
                "org/apache/syncope/common/lib/to/MembershipTO.class");

        System.out.println(
                "SYSTEM_MEMBERSHIP_RESOURCE=" + systemMembership);

        if (systemMembership == null) {
            throw new IllegalStateException(
                    "MembershipTO non visibile dal system classloader");
        }

        /*
         * Inizializza SLF4J/Logback PRIMA di installare il context loader
         * applicativo. Il bridge contiene SOLO .class: nessun
         * META-INF/services e nessuna configurazione Logback del progetto.
         */
        LoggerFactory.getILoggerFactory();

        String projectCp = Files.readString(
                Path.of(args[0]),
                StandardCharsets.UTF_8).trim();

        if (projectCp.isEmpty()) {
            throw new IllegalStateException("Project classpath vuoto");
        }

        String[] entries = projectCp.split(
                Pattern.quote(File.pathSeparator));

        List<URL> urls = new ArrayList<>();

        for (String entry : entries) {
            if (!entry.isBlank()) {
                urls.add(Path.of(entry).toUri().toURL());
            }
        }

        ClassLoader original =
                Thread.currentThread().getContextClassLoader();

        try (ProjectContextClassLoader projectLoader =
                     new ProjectContextClassLoader(
                             urls.toArray(URL[]::new),
                             original)) {

            Thread.currentThread().setContextClassLoader(projectLoader);

            List<String> evoArgs = new ArrayList<>();

            for (int i = 1; i < args.length; i++) {
                evoArgs.add(args[i]);
            }

            /*
             * EvoSuite continua a ricevere il classpath completo del SUT.
             * Lo aggiungiamo qui, in memoria, evitando di duplicare una lunga
             * stringa nella command line Windows.
             */
            evoArgs.add("-projectCP");
            evoArgs.add(projectCp);

            EvoSuite.main(evoArgs.toArray(String[]::new));
        }
        finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
'@

    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)

    [System.IO.File]::WriteAllText(
            $launcherSource,
            $launcherJava,
            $utf8NoBom
    )

    $javac21 = Join-Path $script:Jdk21Home "bin\javac.exe"

    # Il launcher deve vedere solo EvoSuite + ASM overlay.
    # NON deve vedere il projectCP Syncope durante la compilazione/runtime base.
    $launcherCompileCp = "$OverlayJar;$($script:EvoSuiteJar)"

    & $javac21 `
        -encoding UTF-8 `
        -cp $launcherCompileCp `
        -d $launcherDir `
        $launcherSource

    if ($LASTEXITCODE -ne 0) {
        throw "Compilazione del launcher isolato EvoSuite fallita."
    }

    $launcherClass = Join-Path $launcherDir "EvoSuiteIsolatedLauncher.class"

    if (-not (Test-Path -LiteralPath $launcherClass -PathType Leaf)) {
        throw "Launcher isolato non prodotto: $launcherClass"
    }

    if (-not (Test-Path -LiteralPath $ProjectCpFile -PathType Leaf)) {
        throw "File project classpath assente: $ProjectCpFile"
    }

    Write-Host "Launcher isolato compilato:"
    Write-Host "  $launcherClass"
    Write-Host ""
    Write-Host "Separazione classloader:"
    Write-Host "  System CP  = launcher + ASM overlay + EvoSuite"
    Write-Host "  Context CP = Syncope + dipendenze"
    Write-Host "  META-INF/services applicativi filtrati dal context loader"

    return $launcherDir
}

# ============================================================================
# 1. BUILD REALE
# ============================================================================

Write-Section "1. Build REALE DefaultMappingManager con JDK 21"

$buildStart = (Get-Date).AddSeconds(-2)

Invoke-Maven `
    -JdkHome $script:Jdk21Home `
    -WorkingDirectory $script:RepoRoot `
    -Arguments @(
    "-pl", "core/provisioning-java",
    "-am",
    "-DtargetJdk=21",
    "-Dmaven.build.cache.skipCache=true",
    "-Dmaven.test.skip=true",
    "-Drat.skip=true",
    "-Dspotless.check.skip=true",
    "-Dcheckstyle.skip=true",
    "clean",
    "compile"
)

if (-not (Test-Path -LiteralPath $script:RealClassFile -PathType Leaf)) {
    throw "CUT reale non trovata: $script:RealClassFile"
}

$major = Get-ClassMajorVersion -ClassFile $script:RealClassFile
Write-Host "CUT reale classfile major version: $major"

if ($major -ne 65) {
    throw @"
La CUT reale deve essere Java 21 (major 65).
Major trovata: $major
"@
}

# ============================================================================
# 2. CLASSPATH REALE
# ============================================================================

Write-Section "2. Costruzione classpath REALE Java 21"

if (Test-Path -LiteralPath $script:DmmGenerationRoot) {
    Remove-Item -LiteralPath $script:DmmGenerationRoot -Recurse -Force
}
Ensure-Directory $script:DmmGenerationRoot

$reactorDirs = @(Get-FreshReactorClassDirectories -ModifiedSince $buildStart)

if ($reactorDirs.Count -eq 0) {
    throw "Nessuna target\classes fresca trovata dopo la build."
}

# Diamo sempre priorità alla directory target/classes della CUT.
$orderedDirs = New-Object System.Collections.Generic.List[string]
[void]$orderedDirs.Add((Join-Path $script:ModuleDir "target\classes"))

foreach ($dir in $reactorDirs) {
    if ($dir -ne (Join-Path $script:ModuleDir "target\classes")) {
        [void]$orderedDirs.Add($dir)
    }
}

Invoke-Maven `
    -JdkHome $script:Jdk21Home `
    -WorkingDirectory $script:ModuleDir `
    -Arguments @(
    "-DtargetJdk=21",
    "-Dmaven.test.skip=true",
    "-Drat.skip=true",
    "-Dspotless.check.skip=true",
    "-Dcheckstyle.skip=true",
    "org.apache.maven.plugins:maven-dependency-plugin:3.8.1:build-classpath",
    "-DincludeScope=runtime",
    "-Dmdep.outputFile=$($script:DmmDependencyCpFile)"
)

if (-not (Test-Path -LiteralPath $script:DmmDependencyCpFile -PathType Leaf)) {
    throw "Classpath Maven non prodotto: $script:DmmDependencyCpFile"
}

$rawMavenCp = (Get-Content -Raw $script:DmmDependencyCpFile).Trim()
$mavenJars = @()

if (-not [string]::IsNullOrWhiteSpace($rawMavenCp)) {
    $mavenJars = @(
    $rawMavenCp -split ";" |
            Where-Object {
                -not [string]::IsNullOrWhiteSpace($_) -and
                        (Test-Path -LiteralPath $_ -PathType Leaf) -and
                        [System.IO.Path]::GetExtension($_) -ieq ".jar"
            } |
    # Le classi Syncope SNAPSHOT fresche sono già fornite dalle
    # target\classes del reactor; evitiamo copie stale dal repository locale.
    Where-Object {
        [System.IO.Path]::GetFileName($_) -notmatch
                '^syncope-.*-5\.0\.0-SNAPSHOT\.jar$'
    } |
            Select-Object -Unique
    )
}

$cpParts = New-Object System.Collections.Generic.List[string]

foreach ($dir in ($orderedDirs | Select-Object -Unique)) {
    [void]$cpParts.Add($dir)
}

foreach ($jar in $mavenJars) {
    [void]$cpParts.Add($jar)
}

$projectCp = $cpParts -join ";"

if ([string]::IsNullOrWhiteSpace($projectCp)) {
    throw "Classpath di generazione vuoto."
}

$membershipRelative = "org\apache\syncope\common\lib\to\MembershipTO.class"
$membershipFound = $false

foreach ($dir in $orderedDirs) {
    if (Test-Path -LiteralPath (Join-Path $dir $membershipRelative) -PathType Leaf) {
        $membershipFound = $true
        Write-Host "MembershipTO trovata in:"
        Write-Host "  $dir"
        break
    }
}

if (-not $membershipFound) {
    throw @"
MembershipTO non trovata nelle target\classes fresche del reactor.
La generazione viene fermata prima di EvoSuite.
"@
}

Write-Host "Directory reactor nel CP : $($orderedDirs.Count)"
Write-Host "JAR esterni nel CP       : $($mavenJars.Count)"
Write-Host "Lunghezza projectCP      : $($projectCp.Length) caratteri"

if ($projectCp.Length -gt 24000) {
    Write-Warning @"
Il projectCP è molto lungo. Windows supporta command line più grandi di cmd.exe
quando l'eseguibile viene invocato direttamente da PowerShell, ma siamo vicini
a una dimensione che può diventare fragile.
"@
}

# ============================================================================
# 3-4. ASM OVERLAY + PREFLIGHT
# ============================================================================

$asmOverlayJar = Build-EvoSuiteAsmOverlay -Version $AsmVersion

if ($asmOverlayJar -is [System.Array]) {
    throw @"
Build-EvoSuiteAsmOverlay ha restituito piu valori invece di un solo path:
$($asmOverlayJar | Out-String)
"@
}

$asmOverlayJar = [string]$asmOverlayJar

if ([string]::IsNullOrWhiteSpace($asmOverlayJar) -or
        -not (Test-Path -LiteralPath $asmOverlayJar -PathType Leaf)) {
    throw "Path ASM overlay non valido restituito dalla funzione: '$asmOverlayJar'"
}

Write-Host "ASM overlay path verificato come stringa singola:"
Write-Host "  $asmOverlayJar"

Run-GeneratorPreflight `
    -OverlayJar $asmOverlayJar `
    -ProjectCp $projectCp

# ============================================================================
# 5-6. LAUNCHER ISOLATO + EVOsuite GENERATION
# ============================================================================

# Il projectCP è già stato scritto qui da maven-dependency-plugin; lo
# sovrascriviamo con il classpath COMPLETO effettivamente costruito sopra,
# così il launcher può leggerlo senza passarlo due volte sulla command line.
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)

[System.IO.File]::WriteAllText(
        $script:DmmDependencyCpFile,
        $projectCp,
        $utf8NoBom
)

$systemClassBridge = Build-SystemClassBridge -ProjectCp $projectCp

if ($systemClassBridge -is [System.Array]) {
    throw "Build-SystemClassBridge ha restituito piu valori."
}

$systemClassBridge = [string]$systemClassBridge

if ([string]::IsNullOrWhiteSpace($systemClassBridge) -or
        -not (Test-Path -LiteralPath $systemClassBridge -PathType Leaf)) {
    throw "Class-only bridge JAR non valido: '$systemClassBridge'"
}

$launcherDir = Build-IsolatedEvoSuiteLauncher `
    -OverlayJar $asmOverlayJar `
    -ProjectCpFile $script:DmmDependencyCpFile

if ($launcherDir -is [System.Array]) {
    throw "Build-IsolatedEvoSuiteLauncher ha restituito piu valori."
}

$launcherDir = [string]$launcherDir

Write-Section "7. Generazione EvoSuite sulla CUT REALE Java 21"

if (Test-Path -LiteralPath $script:EvoGeneratedRoot) {
    Remove-Item -LiteralPath $script:EvoGeneratedRoot -Recurse -Force
}

Ensure-Directory $script:EvoGeneratedTests
Ensure-Directory $script:EvoReportDir

$java21 = Join-Path $script:Jdk21Home "bin\java.exe"

# CRITICO:
# il projectCP NON entra piu nel system classpath di EvoSuite.
#
# System/runtime CP:
#   launcher -> ASM overlay -> EvoSuite
#
# Project/SUT CP:
#   installato dal launcher come context classloader isolato
#   e passato internamente a EvoSuite come -projectCP.
$runtimeCp = "$launcherDir;$asmOverlayJar;$($script:EvoSuiteJar);$systemClassBridge"

$evoArgs = @(
    "-Xmx3500m",
    "-cp", $runtimeCp,
    "EvoSuiteIsolatedLauncher",
    $script:DmmDependencyCpFile,
    "-generateMOSuite",
    "-class", $script:TargetClass,
    "-Dtest_dir=$($script:EvoGeneratedTests)",
    "-Dreport_dir=$($script:EvoReportDir)",
    "-Dcriterion=LINE:BRANCH",
    "-Dsearch_budget=$BudgetSeconds",
    "-Dstopping_condition=MaxTime",
    "-Dassertions=true",
    "-Dminimize=true",
    "-Dsandbox=false",
    "-Dclient_on_thread=true"
)

$oldJdkJavaOptions = $env:JDK_JAVA_OPTIONS

try {
    $requiredOptions = @(
        "-Djava.security.manager=allow",
        "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.net=ALL-UNNAMED"
    )

    $effectiveOptions = $oldJdkJavaOptions

    foreach ($required in $requiredOptions) {
        if ([string]::IsNullOrWhiteSpace($effectiveOptions)) {
            $effectiveOptions = $required
        }
        elseif ($effectiveOptions -notmatch [regex]::Escape($required)) {
            $effectiveOptions = "$effectiveOptions $required"
        }
    }

    $env:JDK_JAVA_OPTIONS = $effectiveOptions

    Write-Host "Budget EvoSuite: $BudgetSeconds secondi"
    Write-Host "JDK generazione: Java 21"
    Write-Host "CUT usata: REALE, nessuna modifica al bytecode"
    Write-Host "ASM overlay: $AsmVersion"
    Write-Host "Runtime EvoSuite isolato dalle RISORSE Spring Boot/Syncope: SI"
    Write-Host "Class-only bridge JAR visibile al system classloader: SI"
    Write-Host "JDK_JAVA_OPTIONS:"
    Write-Host "  $env:JDK_JAVA_OPTIONS"

    & $java21 @evoArgs

    if ($LASTEXITCODE -ne 0) {
        throw "EvoSuite terminato con exit code $LASTEXITCODE."
    }
}
finally {
    $env:JDK_JAVA_OPTIONS = $oldJdkJavaOptions
}

# ============================================================================
# 8. COPY GENERATED TESTS
# ============================================================================

$evoSuiteRawPackageDir = Join-Path `
    $script:EvoGeneratedTests `
    "org\apache\syncope\core\provisioning\java"

$rawTest = Join-Path `
    $evoSuiteRawPackageDir `
    "DefaultMappingManager_ESTest.java"

$rawScaffolding = Join-Path `
    $evoSuiteRawPackageDir `
    "DefaultMappingManager_ESTest_scaffolding.java"

if (-not (Test-Path -LiteralPath $rawTest -PathType Leaf)) {
    throw "EvoSuite non ha prodotto il test atteso: $rawTest"
}

if (-not (Test-Path -LiteralPath $rawScaffolding -PathType Leaf)) {
    throw "EvoSuite non ha prodotto lo scaffolding atteso: $rawScaffolding"
}

Write-Section "8. Copia test generati in src\test\evosuite"

Ensure-Directory $script:GeneratedPackageDir

# PowerShell tratta i nomi delle variabili senza distinzione tra maiuscole e minuscole.
# La directory RAW di EvoSuite deve quindi avere un nome completamente diverso
# da $script:GeneratedPackageDir.
$rawTestFull = [System.IO.Path]::GetFullPath($rawTest)
$destTestFull = [System.IO.Path]::GetFullPath($script:GeneratedTestFile)

$rawScaffoldingFull = [System.IO.Path]::GetFullPath($rawScaffolding)
$destScaffoldingFull = [System.IO.Path]::GetFullPath($script:GeneratedScaffoldingFile)

if ($rawTestFull -ieq $destTestFull) {
    throw "Collisione path: sorgente e destinazione del test coincidono: $rawTestFull"
}

if ($rawScaffoldingFull -ieq $destScaffoldingFull) {
    throw "Collisione path: sorgente e destinazione dello scaffolding coincidono: $rawScaffoldingFull"
}

Write-Host "Sorgente test generato:"
Write-Host "  $rawTestFull"
Write-Host "Destinazione test:"
Write-Host "  $destTestFull"

Copy-Item `
    -LiteralPath $rawTest `
    -Destination $script:GeneratedPackageDir `
    -Force

Copy-Item `
    -LiteralPath $rawScaffolding `
    -Destination $script:GeneratedPackageDir `
    -Force

if (-not (Test-Path -LiteralPath $script:GeneratedTestFile -PathType Leaf)) {
    throw "Test non presente dopo la copia: $script:GeneratedTestFile"
}

if (-not (Test-Path -LiteralPath $script:GeneratedScaffoldingFile -PathType Leaf)) {
    throw "Scaffolding non presente dopo la copia: $script:GeneratedScaffoldingFile"
}

Write-Host ""
Write-Host "GENERAZIONE COMPLETATA"
Write-Host "Test:"
Write-Host "  $script:GeneratedTestFile"
Write-Host "Scaffolding:"
Write-Host "  $script:GeneratedScaffoldingFile"
Write-Host ""
Write-Host "Garanzie della pipeline:"
Write-Host "  CUT sorgente modificata       : NO"
Write-Host "  CUT bytecode modificato       : NO"
Write-Host "  dipendenze Syncope modificate : NO"
Write-Host "  ASM interno EvoSuite sostituito a runtime tramite overlay: SI"
Write-Host ""
Write-Host "Prossimo comando:"
Write-Host "  .\scripts\evosuite\evosuite_run_dmm.ps1 test"