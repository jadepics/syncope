import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Costruisce l'intero dataset della Milestone 1 di Apache Syncope con un solo comando
 * e produce un unico workbook Excel:
 *
 *     milestone_1/output/milestone_1_dataset.xlsx
 *
 * Il workbook contiene:
 * - Dataset: una riga per release selezionata x file Java di produzione;
 * - Tickets: ciclo IV/AF/OV/FV, Proportion e consistenza;
 * - TicketCommits: commit Git candidati e controlli temporali;
 * - Releases: catalogo JIRA/Git, date grezze/effettive e selezione del primo 33%;
 * - ReleaseAnomalies: conflitti e inversioni nelle date delle release;
 * - ExcludedTickets: ticket non utilizzati nel labeling;
 * - Metadata: regole, formule, soglie e conteggi.
 *
 * Non vengono prodotti CSV o JSON intermedi permanenti. Le directory temporanee
 * utilizzate per gli snapshot dei tag vengono eliminate al termine dell'analisi.
 */
public final class BuildMilestone1Dataset {

    private static final String PROJECT_KEY = "SYNCOPE";
    private static final String PROJECT_NAME = "Apache Syncope";
    private static final String JIRA_BASE_URL = "https://issues.apache.org/jira";
    private static final String JIRA_VERSIONS_ENDPOINT =
            JIRA_BASE_URL + "/rest/api/2/project/" + PROJECT_KEY + "/versions";
    private static final String JIRA_SEARCH_ENDPOINT =
            JIRA_BASE_URL + "/rest/api/2/search";
    private static final String JQL =
            "project = " + PROJECT_KEY + " AND issuetype = Bug ORDER BY created ASC";
    private static final String ISSUE_FIELDS = String.join(",",
            "summary",
            "status",
            "resolution",
            "priority",
            "created",
            "resolutiondate",
            "versions",
            "fixVersions");

    private static final int PAGE_SIZE = 100;
    private static final int MAX_HTTP_ATTEMPTS = 3;
    private static final double DEFAULT_RELEASE_FRACTION = 0.33d;
    private static final ObjectMapper JSON = new ObjectMapper();
    /*
     * Esclude i tag/versioni come "syncope-1.0.0" dal riconoscimento dei ticket.
     * Sono accettate chiavi come SYNCOPE-836, anche seguite da punteggiatura.
     */
    private static final Pattern ISSUE_KEY_PATTERN =
            Pattern.compile("(?i)(?<![A-Z0-9])SYNCOPE-\\d+(?![A-Z0-9]|\\.\\d)");

    private static final String OFFICIAL_RELEASE_HISTORY_URL =
            "https://cwiki.apache.org/confluence/display/SYNCOPE/Espressivo";

    /*
     * Correzioni applicate solo quando una fonte ufficiale Apache documenta
     * esplicitamente un conflitto con la data JIRA.
     *
     * La data JIRA originale viene comunque conservata nel workbook.
     */
    private static final Map<String, ReleaseDateOverride> RELEASE_DATE_OVERRIDES =
            Map.of(
                    "1.0.3-incubating",
                    new ReleaseDateOverride(
                            LocalDate.of(2012, 10, 30),
                            OFFICIAL_RELEASE_HISTORY_URL,
                            "La cronologia ufficiale Apache indica 30/10/2012; "
                                    + "JIRA riporta 30/09/2012."));

    private static final Pattern VERSION_NUMBERS_PATTERN =
            Pattern.compile("^(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?(.*)$");

    private static final List<DateTimeFormatter> JIRA_DATE_FORMATTERS = List.of(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    );

    private static final Set<String> EXCLUDED_PATH_SEGMENTS = Set.of(
            "test",
            "tests",
            "testfixtures",
            "integration-test",
            "integration-tests",
            "target",
            "build",
            "generated-sources",
            "generated-test-sources",
            "examples",
            "archetype-resources"
    );

    private static final Set<String> JAVA_LANG_TYPES = Set.of(
            "String", "Object", "Integer", "Long", "Short", "Byte", "Double", "Float",
            "Boolean", "Character", "Number", "Class", "Enum", "Throwable", "Exception",
            "RuntimeException", "Error", "Iterable", "Comparable", "AutoCloseable", "Void"
    );

    private BuildMilestone1Dataset() {
        // Utility class.
    }

    public static void main(final String[] args) {
        Instant startedAt = Instant.now();

        try {
            Path repositoryRoot = findRepositoryRoot();
            Path outputDirectory = repositoryRoot.resolve("milestone_1").resolve("output");
            Path outputWorkbook = outputDirectory.resolve("milestone_1_dataset.xlsx");
            Files.createDirectories(outputDirectory);

            double releaseFraction = readReleaseFraction();
            Integer maxSelectedReleases = readOptionalPositiveInt("milestone.maxReleases");

            System.out.println("=== Apache Syncope - Milestone 1 dataset builder ===");
            System.out.println("Repository: " + repositoryRoot);
            System.out.println("Output: " + outputWorkbook);

            GitClient git = new GitClient(repositoryRoot);
            git.assertRepositoryAvailable();

            JiraClient jira = new JiraClient();

            System.out.println("[1/9] Recupero versioni JIRA e tag Git...");
            List<JiraVersion> jiraVersions = jira.fetchVersions();
            Map<String, String> gitTagsByCanonicalVersion = git.collectTagsByCanonicalVersion();
            ReleaseCatalog releaseCatalog = ReleaseCatalog.build(
                    jiraVersions,
                    gitTagsByCanonicalVersion,
                    git);

            System.out.println("Release JIRA con data: " + releaseCatalog.allReleases().size());
            System.out.println("Release utilizzabili con tag Git: "
                    + releaseCatalog.taggedReleases().size());
            System.out.println("Anomalie metadati/date release rilevate: "
                    + releaseCatalog.releaseAnomalies().size());

            System.out.println("[2/9] Recupero ticket Bug JIRA...");
            List<IssueRaw> rawIssues = jira.fetchBugIssues();
            System.out.println("Ticket Bug recuperati: " + rawIssues.size());

            System.out.println("[3/9] Calcolo preliminare OV, AF e FV...");
            List<Ticket> tickets = rawIssues.stream()
                    .map(issue -> TicketLifecycleBuilder.buildPreliminary(issue, releaseCatalog))
                    .collect(Collectors.toCollection(ArrayList::new));

            System.out.println("[4/9] Ricerca dei fix commit nella cronologia Git...");
            Map<String, List<GitCommit>> commitsByIssue = git.collectIssueCommits();
            for (Ticket ticket : tickets) {
                ticket.commitCandidates.addAll(
                        commitsByIssue.getOrDefault(ticket.issue.key, List.of()));
                ticket.evaluateCommitCandidates();
            }

            System.out.println("[5/9] Stima delle IV mancanti con Incremental Proportion...");
            TicketLifecycleBuilder.estimateMissingInjectedVersions(tickets, releaseCatalog);

            System.out.println("[6/9] Controllo completo della consistenza temporale...");
            for (Ticket ticket : tickets) {
                ticket.validateLifecycle();
            }

            List<Release> selectedReleases = releaseCatalog.selectFirstFraction(
                    releaseFraction,
                    maxSelectedReleases);
            if (selectedReleases.isEmpty()) {
                throw new IllegalStateException(
                        "Nessuna release con tag Git è stata selezionata per il dataset.");
            }

            System.out.println("[7/9] Analisi del primo "
                    + String.format(Locale.ROOT, "%.2f", releaseFraction * 100.0)
                    + "% delle release (" + selectedReleases.size() + ")...");

            List<ClassMetrics> datasetRows = new ArrayList<>();
            SourceArchiveManager archiveManager = new SourceArchiveManager(git);
            SourceMetricsAnalyzer metricsAnalyzer = new SourceMetricsAnalyzer();

            int releaseCounter = 0;
            for (Release release : selectedReleases) {
                releaseCounter++;
                System.out.println("  Release " + releaseCounter + "/"
                        + selectedReleases.size() + ": " + release.version);

                Path snapshotDirectory = archiveManager.createSnapshot(release);
                try {
                    List<ClassMetrics> releaseMetrics = metricsAnalyzer.analyze(
                            snapshotDirectory,
                            release);
                    datasetRows.addAll(releaseMetrics);
                    release.productionJavaFileCount = releaseMetrics.size();
                } finally {
                    deleteRecursively(snapshotDirectory);
                }
            }

            System.out.println("Classi/file Java analizzati: " + datasetRows.size());

            System.out.println("[8/9] Propagazione della bugginess e labeling Yes/No...");
            BugLabeler labeler = new BugLabeler(selectedReleases, datasetRows);
            List<ExcludedTicket> excludedTickets = labeler.apply(tickets);

            System.out.println("[9/9] Generazione del workbook Excel unico...");
            WorkbookWriter.write(
                    outputWorkbook,
                    releaseCatalog,
                    selectedReleases,
                    tickets,
                    datasetRows,
                    excludedTickets,
                    releaseFraction,
                    startedAt,
                    Instant.now(),
                    git.currentHead());

            long buggyRows = datasetRows.stream().filter(row -> row.buggy).count();
            long consistentTickets = tickets.stream()
                    .filter(ticket -> ticket.consistencyStatus == ConsistencyStatus.CONSISTENT)
                    .count();

            System.out.println();
            System.out.println("Dataset completato.");
            System.out.println("Righe Dataset: " + datasetRows.size());
            System.out.println("Righe buggy=Yes: " + buggyRows);
            System.out.println("Ticket consistenti: " + consistentTickets + "/" + tickets.size());
            System.out.println("Ticket esclusi dal labeling: " + excludedTickets.size());
            System.out.println("File generato: " + outputWorkbook);

        } catch (Exception exception) {
            System.err.println("Errore durante la costruzione della Milestone 1:");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            System.exit(1);
        }
    }

    private static double readReleaseFraction() {
        String raw = System.getProperty("milestone.releaseFraction", "0.33").trim();
        try {
            double value = Double.parseDouble(raw);
            if (value <= 0.0 || value > 1.0) {
                throw new IllegalArgumentException(
                        "milestone.releaseFraction deve essere nell'intervallo (0, 1].");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Valore non valido per milestone.releaseFraction: " + raw,
                    exception);
        }
    }

    private static Integer readOptionalPositiveInt(final String propertyName) {
        String raw = System.getProperty(propertyName);
        if (raw == null || raw.isBlank()) {
            return null;
        }

        try {
            int value = Integer.parseInt(raw.trim());
            if (value <= 0) {
                throw new IllegalArgumentException(propertyName + " deve essere positivo.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Valore non valido per " + propertyName + ": " + raw,
                    exception);
        }
    }

    private static Path findRepositoryRoot() {
        Path current = Paths.get("").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isDirectory(current.resolve(".git"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException(
                "Repository Git non trovato dalla directory corrente.");
    }

    private static String canonicalVersion(final String rawValue) {
        if (rawValue == null) {
            return "";
        }

        String value = rawValue.trim().toLowerCase(Locale.ROOT);
        value = value.replaceFirst("^apache\\s+syncope[\\s_-]*", "");
        value = value.replaceFirst("^syncope[\\s_-]*", "");
        value = value.replaceFirst("^release[\\s_-]*", "");
        value = value.replace('_', '-').replace(' ', '-');
        value = value.replaceAll("(?<=\\d)\\.(?=(m|rc|alpha|beta)\\d*$)", "-");
        value = value.replaceAll("-+", "-");
        value = value.replaceAll("^-|-$", "");
        return value;
    }

    private static int compareVersionNames(final String first, final String second) {
        VersionParts a = VersionParts.parse(first);
        VersionParts b = VersionParts.parse(second);
        int comparison = Integer.compare(a.major, b.major);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(a.minor, b.minor);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(a.patch, b.patch);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(a.qualifierRank, b.qualifierRank);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(a.qualifierNumber, b.qualifierNumber);
        if (comparison != 0) {
            return comparison;
        }
        return a.normalized.compareTo(b.normalized);
    }

    private static boolean isProductionJavaPath(final String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return false;
        }

        String normalized = rawPath.replace('\\', '/');
        if (!normalized.toLowerCase(Locale.ROOT).endsWith(".java")) {
            return false;
        }

        String lower = normalized.toLowerCase(Locale.ROOT);
        if (lower.contains("/src/test/")
                || lower.contains("/src/test-")
                || lower.contains("/src/it/")
                || lower.contains("/src/integration-test/")
                || lower.contains("/target/")
                || lower.contains("/generated-sources/")
                || lower.contains("/generated-test-sources/")) {
            return false;
        }

        String[] segments = lower.split("/");
        for (String segment : segments) {
            if (EXCLUDED_PATH_SEGMENTS.contains(segment)) {
                return false;
            }
        }

        String filename = segments.length == 0 ? lower : segments[segments.length - 1];
        return !filename.endsWith("test.java")
                && !filename.endsWith("tests.java")
                && !filename.endsWith("itcase.java");
    }

    private static void deleteRecursively(final Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }

        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(
                    final Path file,
                    final BasicFileAttributes attributes) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(
                    final Path directory,
                    final IOException exception) throws IOException {
                if (exception != null) {
                    throw exception;
                }
                Files.deleteIfExists(directory);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static String joinVersions(final Collection<Release> releases) {
        return releases.stream()
                .sorted(Comparator.comparingInt(release -> release.sequence))
                .map(release -> release.version)
                .collect(Collectors.joining(" | "));
    }

    private static String joinStrings(final Collection<String> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .collect(Collectors.joining(" | "));
    }

    private static LocalDate dateOf(final OffsetDateTime value) {
        return value == null ? null : value.toLocalDate();
    }

    private static LocalDate minDate(final Collection<GitCommit> commits) {
        return commits.stream()
                .map(commit -> commit.committerDate)
                .filter(Objects::nonNull)
                .map(OffsetDateTime::toLocalDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    private static final class JiraClient {

        private final HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        List<JiraVersion> fetchVersions() throws IOException, InterruptedException {
            JsonNode root = fetchJson(URI.create(JIRA_VERSIONS_ENDPOINT));
            JsonNode values = root.isArray() ? root : root.path("values");
            if (!values.isArray()) {
                throw new IllegalStateException(
                        "Risposta JIRA versioni non riconosciuta: " + root);
            }

            List<JiraVersion> versions = new ArrayList<>();
            for (JsonNode node : values) {
                String name = node.path("name").asText("").trim();
                if (name.isBlank()) {
                    continue;
                }

                String releaseDateText = node.path("releaseDate").asText("").trim();
                LocalDate releaseDate = null;
                if (!releaseDateText.isBlank()) {
                    try {
                        releaseDate = LocalDate.parse(releaseDateText);
                    } catch (DateTimeParseException ignored) {
                        // La versione rimane disponibile ma senza data utilizzabile.
                    }
                }

                versions.add(new JiraVersion(
                        node.path("id").asText(""),
                        name,
                        canonicalVersion(name),
                        node.path("released").asBoolean(false),
                        node.path("archived").asBoolean(false),
                        releaseDate,
                        node.path("description").asText("")));
            }
            return versions;
        }

        List<IssueRaw> fetchBugIssues() throws IOException, InterruptedException {
            List<IssueRaw> issues = new ArrayList<>();
            int startAt = 0;
            int total = Integer.MAX_VALUE;

            while (startAt < total) {
                String query = "jql=" + urlEncode(JQL)
                        + "&startAt=" + startAt
                        + "&maxResults=" + PAGE_SIZE
                        + "&fields=" + urlEncode(ISSUE_FIELDS);

                JsonNode page = fetchJson(URI.create(JIRA_SEARCH_ENDPOINT + "?" + query));
                total = page.path("total").asInt(0);
                JsonNode issueNodes = page.path("issues");
                if (!issueNodes.isArray() || issueNodes.isEmpty()) {
                    break;
                }

                for (JsonNode issueNode : issueNodes) {
                    issues.add(parseIssue(issueNode));
                }
                startAt += issueNodes.size();
                System.out.println("  Ticket scaricati: " + issues.size() + " / " + total);
            }
            return issues;
        }

        private IssueRaw parseIssue(final JsonNode issueNode) {
            JsonNode fields = issueNode.path("fields");
            String createdText = fields.path("created").asText("");
            String closedText = fields.path("resolutiondate").asText("");

            return new IssueRaw(
                    issueNode.path("id").asText(""),
                    issueNode.path("key").asText(""),
                    fields.path("summary").asText(""),
                    fields.path("status").path("name").asText(""),
                    fields.path("resolution").path("name").asText(""),
                    fields.path("priority").path("name").asText(""),
                    parseJiraDate(createdText).orElse(null),
                    parseJiraDate(closedText).orElse(null),
                    extractVersionNames(fields.path("versions")),
                    extractVersionNames(fields.path("fixVersions")));
        }

        private JsonNode fetchJson(final URI uri) throws IOException, InterruptedException {
            IOException lastIOException = null;

            for (int attempt = 1; attempt <= MAX_HTTP_ATTEMPTS; attempt++) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(uri)
                            .timeout(Duration.ofSeconds(90))
                            .header("Accept", "application/json")
                            .header("User-Agent", "Syncope-Milestone-1-Dataset-Builder")
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                    int statusCode = response.statusCode();
                    if (statusCode >= 200 && statusCode < 300) {
                        return JSON.readTree(response.body());
                    }

                    if ((statusCode == 429 || statusCode >= 500)
                            && attempt < MAX_HTTP_ATTEMPTS) {
                        Thread.sleep(Duration.ofSeconds(attempt * 2L).toMillis());
                        continue;
                    }

                    throw new IllegalStateException(
                            "JIRA ha restituito HTTP " + statusCode + " per " + uri
                                    + System.lineSeparator() + response.body());
                } catch (IOException exception) {
                    lastIOException = exception;
                    if (attempt < MAX_HTTP_ATTEMPTS) {
                        Thread.sleep(Duration.ofSeconds(attempt * 2L).toMillis());
                    }
                }
            }

            throw lastIOException == null
                    ? new IOException("Impossibile interrogare JIRA: " + uri)
                    : lastIOException;
        }

        private static List<String> extractVersionNames(final JsonNode versionsNode) {
            List<String> result = new ArrayList<>();
            if (!versionsNode.isArray()) {
                return result;
            }
            for (JsonNode versionNode : versionsNode) {
                String name = versionNode.path("name").asText("").trim();
                if (!name.isBlank()) {
                    result.add(name);
                }
            }
            return result;
        }

        private static Optional<OffsetDateTime> parseJiraDate(final String value) {
            if (value == null || value.isBlank()) {
                return Optional.empty();
            }
            for (DateTimeFormatter formatter : JIRA_DATE_FORMATTERS) {
                try {
                    return Optional.of(OffsetDateTime.parse(value, formatter));
                } catch (DateTimeParseException ignored) {
                    // Prova il formato successivo.
                }
            }
            return Optional.empty();
        }

        private static String urlEncode(final String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
    }

    private static final class GitClient {

        private final Path repositoryRoot;

        GitClient(final Path repositoryRoot) {
            this.repositoryRoot = repositoryRoot;
        }

        void assertRepositoryAvailable() throws IOException, InterruptedException {
            String inside = run(List.of("rev-parse", "--is-inside-work-tree"), true).trim();
            if (!"true".equalsIgnoreCase(inside)) {
                throw new IllegalStateException("La directory non è un repository Git valido.");
            }
        }

        String currentHead() throws IOException, InterruptedException {
            return run(List.of("rev-parse", "HEAD"), true).trim();
        }

        Map<String, String> collectTagsByCanonicalVersion()
                throws IOException, InterruptedException {
            String output = run(List.of("tag", "--list"), true);
            Map<String, List<String>> candidates = new HashMap<>();

            for (String line : output.split("\\R")) {
                String tag = line.trim();
                if (tag.isBlank()) {
                    continue;
                }
                String canonical = canonicalVersion(tag);
                if (canonical.isBlank()) {
                    continue;
                }
                candidates.computeIfAbsent(canonical, ignored -> new ArrayList<>()).add(tag);
            }

            Map<String, String> selected = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : candidates.entrySet()) {
                List<String> tags = entry.getValue();
                tags.sort(Comparator
                        .comparingInt((String tag) -> tag.toLowerCase(Locale.ROOT)
                                .startsWith("syncope-") ? 0 : 1)
                        .thenComparingInt(String::length)
                        .thenComparing(Function.identity()));
                selected.put(entry.getKey(), tags.getFirst());
            }
            return selected;
        }

        String commitForTag(final String tag) throws IOException, InterruptedException {
            return run(List.of("rev-list", "-n", "1", tag), true).trim();
        }

        LocalDate commitDateForTag(final String tag)
                throws IOException, InterruptedException {
            String value = run(List.of(
                    "show",
                    "-s",
                    "--format=%cI",
                    tag), true).trim();
            OffsetDateTime date = parseGitDate(value);
            return date == null ? null : date.toLocalDate();
        }

        Map<String, List<GitCommit>> collectIssueCommits()
                throws IOException, InterruptedException {
            String output = run(List.of(
                    "log",
                    "--all",
                    "--format=%H%x1f%aI%x1f%cI%x1f%s"), true);

            Map<String, List<GitCommit>> result = new HashMap<>();
            Map<String, GitCommit> commitsByHash = new LinkedHashMap<>();
            Map<String, Set<String>> issueKeysByHash = new LinkedHashMap<>();

            for (String line : output.split("\\R")) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\u001f", 4);
                if (parts.length < 4) {
                    continue;
                }

                Matcher matcher = ISSUE_KEY_PATTERN.matcher(parts[3]);
                Set<String> keys = new LinkedHashSet<>();
                while (matcher.find()) {
                    keys.add(matcher.group().toUpperCase(Locale.ROOT));
                }
                if (keys.isEmpty()) {
                    continue;
                }

                GitCommit commit = new GitCommit(
                        parts[0],
                        parseGitDate(parts[1]),
                        parseGitDate(parts[2]),
                        parts[3]);
                commitsByHash.putIfAbsent(commit.hash, commit);
                issueKeysByHash.computeIfAbsent(commit.hash, ignored -> new LinkedHashSet<>())
                        .addAll(keys);
            }

            int counter = 0;
            for (GitCommit commit : commitsByHash.values()) {
                counter++;
                commit.fileChanges.addAll(changedFiles(commit.hash));
                for (String issueKey : issueKeysByHash.getOrDefault(commit.hash, Set.of())) {
                    result.computeIfAbsent(issueKey, ignored -> new ArrayList<>())
                            .add(commit.copy());
                }
                if (counter % 100 == 0) {
                    System.out.println("  Commit con ticket analizzati: " + counter
                            + " / " + commitsByHash.size());
                }
            }

            for (List<GitCommit> commits : result.values()) {
                commits.sort(Comparator
                        .comparing((GitCommit commit) -> commit.committerDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(commit -> commit.hash));
            }
            return result;
        }

        private List<FileChange> changedFiles(final String commitHash)
                throws IOException, InterruptedException {
            String output = run(List.of(
                    "show",
                    "--format=",
                    "--name-status",
                    "--find-renames",
                    "--find-copies",
                    commitHash), true);

            List<FileChange> changes = new ArrayList<>();
            for (String line : output.split("\\R")) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\t");
                if (parts.length < 2) {
                    continue;
                }
                String status = parts[0].trim();
                if ((status.startsWith("R") || status.startsWith("C"))
                        && parts.length >= 3) {
                    changes.add(new FileChange(status, normalizeGitPath(parts[1]),
                            normalizeGitPath(parts[2])));
                } else {
                    changes.add(new FileChange(status, null, normalizeGitPath(parts[1])));
                }
            }
            return changes;
        }

        void archiveTag(final String tag, final Path zipFile)
                throws IOException, InterruptedException {
            run(List.of(
                    "archive",
                    "--format=zip",
                    "--output=" + zipFile.toAbsolutePath(),
                    tag), true);
        }

        private String run(final List<String> arguments, final boolean failOnError)
                throws IOException, InterruptedException {
            List<String> command = new ArrayList<>();
            command.add("git");
            command.addAll(arguments);

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(repositoryRoot.toFile());
            builder.redirectErrorStream(false);
            Process process = builder.start();

            String stdout;
            String stderr;
            try (InputStream out = process.getInputStream();
                 InputStream err = process.getErrorStream()) {
                stdout = new String(out.readAllBytes(), StandardCharsets.UTF_8);
                stderr = new String(err.readAllBytes(), StandardCharsets.UTF_8);
            }

            boolean completed = process.waitFor(10, TimeUnit.MINUTES);
            if (!completed) {
                process.destroyForcibly();
                throw new IllegalStateException("Comando Git scaduto: " + String.join(" ", command));
            }

            if (failOnError && process.exitValue() != 0) {
                throw new IllegalStateException(
                        "Comando Git fallito (exit " + process.exitValue() + "): "
                                + String.join(" ", command)
                                + System.lineSeparator() + stderr);
            }
            return stdout;
        }

        private static OffsetDateTime parseGitDate(final String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            try {
                return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (DateTimeParseException exception) {
                return null;
            }
        }

        private static String normalizeGitPath(final String path) {
            return path == null ? null : path.trim().replace('\\', '/');
        }
    }

    private static final class ReleaseCatalog {

        private final List<Release> allReleases;
        private final List<Release> taggedReleases;
        private final Map<String, Release> byCanonicalVersion;
        private final List<ReleaseAnomaly> releaseAnomalies;

        private ReleaseCatalog(
                final List<Release> allReleases,
                final List<Release> taggedReleases,
                final Map<String, Release> byCanonicalVersion,
                final List<ReleaseAnomaly> releaseAnomalies) {
            this.allReleases = allReleases;
            this.taggedReleases = taggedReleases;
            this.byCanonicalVersion = byCanonicalVersion;
            this.releaseAnomalies = releaseAnomalies;
        }

        static ReleaseCatalog build(
                final List<JiraVersion> jiraVersions,
                final Map<String, String> tagsByCanonical,
                final GitClient git) throws IOException, InterruptedException {

            Map<String, JiraVersion> selectedJiraByCanonical = new LinkedHashMap<>();
            for (JiraVersion version : jiraVersions) {
                if (!version.released || version.releaseDate == null || version.canonical.isBlank()) {
                    continue;
                }

                JiraVersion previous = selectedJiraByCanonical.get(version.canonical);
                if (previous == null
                        || (previous.archived && !version.archived)
                        || (previous.releaseDate != null
                        && version.releaseDate.isAfter(previous.releaseDate))) {
                    selectedJiraByCanonical.put(version.canonical, version);
                }
            }

            List<Release> releases = new ArrayList<>();
            List<ReleaseAnomaly> anomalies = new ArrayList<>();

            for (JiraVersion jiraVersion : selectedJiraByCanonical.values()) {
                String tag = tagsByCanonical.get(jiraVersion.canonical);
                String commitHash = null;
                LocalDate tagCommitDate = null;

                if (tag != null) {
                    commitHash = git.commitForTag(tag);
                    if (commitHash.isBlank()) {
                        tag = null;
                        commitHash = null;
                    } else {
                        tagCommitDate = git.commitDateForTag(tag);
                    }
                }

                LocalDate effectiveReleaseDate = jiraVersion.releaseDate;
                String releaseDateSource = "JIRA";
                String correctionReason = "";
                String evidenceUrl = "";

                ReleaseDateOverride override =
                        RELEASE_DATE_OVERRIDES.get(jiraVersion.canonical);
                if (override != null
                        && !override.effectiveDate().equals(jiraVersion.releaseDate)) {
                    effectiveReleaseDate = override.effectiveDate();
                    releaseDateSource = "OFFICIAL_APACHE_OVERRIDE";
                    correctionReason = override.reason();
                    evidenceUrl = override.evidenceUrl();

                    anomalies.add(new ReleaseAnomaly(
                            "JIRA_OFFICIAL_DATE_CONFLICT",
                            "CORRECTED",
                            jiraVersion.name,
                            jiraVersion.canonical,
                            "",
                            jiraVersion.releaseDate,
                            effectiveReleaseDate,
                            tagCommitDate,
                            correctionReason,
                            evidenceUrl));
                }

                releases.add(new Release(
                        jiraVersion.id,
                        jiraVersion.name,
                        jiraVersion.canonical,
                        jiraVersion.releaseDate,
                        effectiveReleaseDate,
                        releaseDateSource,
                        correctionReason,
                        evidenceUrl,
                        tag,
                        commitHash,
                        tagCommitDate,
                        jiraVersion.archived));
            }

            /*
             * La sequenza utilizzata da OV, IV, FV, Proportion e labeling
             * usa la data effettiva, cioè JIRA salvo correzioni ufficiali
             * esplicitamente documentate.
             */
            releases.sort(Comparator
                    .comparing((Release release) -> release.releaseDate)
                    .thenComparing(release -> release.version,
                            BuildMilestone1Dataset::compareVersionNames));

            Map<String, Release> byCanonical = new LinkedHashMap<>();
            for (int index = 0; index < releases.size(); index++) {
                Release release = releases.get(index);
                release.sequence = index;
                byCanonical.put(release.canonicalVersion, release);
            }

            detectSemanticDateInversions(releases, anomalies);
            detectTagDateWarnings(releases, anomalies);

            List<Release> tagged = releases.stream()
                    .filter(release -> release.gitTag != null)
                    .collect(Collectors.toCollection(ArrayList::new));

            return new ReleaseCatalog(releases, tagged, byCanonical, anomalies);
        }

        private static void detectSemanticDateInversions(
                final List<Release> releases,
                final List<ReleaseAnomaly> anomalies) {

            Map<String, List<Release>> byReleaseLine = new TreeMap<>();
            for (Release release : releases) {
                VersionParts parts = VersionParts.parse(release.canonicalVersion);
                String line = parts.major() + "." + parts.minor();
                byReleaseLine
                        .computeIfAbsent(line, ignored -> new ArrayList<>())
                        .add(release);
            }

            for (Map.Entry<String, List<Release>> entry : byReleaseLine.entrySet()) {
                List<Release> semanticOrder = new ArrayList<>(entry.getValue());
                semanticOrder.sort((first, second) ->
                        compareVersionNames(first.version, second.version));

                Release previous = null;
                for (Release current : semanticOrder) {
                    if (previous != null) {
                        if (current.jiraReleaseDate.isBefore(previous.jiraReleaseDate)) {
                            anomalies.add(new ReleaseAnomaly(
                                    "JIRA_SEMANTIC_DATE_INVERSION",
                                    "WARNING",
                                    current.version,
                                    current.canonicalVersion,
                                    previous.version,
                                    current.jiraReleaseDate,
                                    current.releaseDate,
                                    current.tagCommitDate,
                                    "Nella linea " + entry.getKey()
                                            + " la versione semanticamente successiva "
                                            + current.version
                                            + " ha data JIRA precedente a "
                                            + previous.version + ".",
                                    current.releaseDateEvidenceUrl));
                        }

                        if (current.releaseDate.isBefore(previous.releaseDate)) {
                            anomalies.add(new ReleaseAnomaly(
                                    "EFFECTIVE_SEMANTIC_DATE_INVERSION",
                                    "BLOCKING",
                                    current.version,
                                    current.canonicalVersion,
                                    previous.version,
                                    current.jiraReleaseDate,
                                    current.releaseDate,
                                    current.tagCommitDate,
                                    "Anche dopo le correzioni documentate, "
                                            + current.version
                                            + " risulta precedente a "
                                            + previous.version + ".",
                                    current.releaseDateEvidenceUrl));
                        }
                    }
                    previous = current;
                }
            }
        }

        private static void detectTagDateWarnings(
                final List<Release> releases,
                final List<ReleaseAnomaly> anomalies) {
            for (Release release : releases) {
                if (release.tagCommitDate != null
                        && release.tagCommitDate.isAfter(release.releaseDate)) {
                    anomalies.add(new ReleaseAnomaly(
                            "TAG_COMMIT_AFTER_EFFECTIVE_RELEASE_DATE",
                            "WARNING",
                            release.version,
                            release.canonicalVersion,
                            "",
                            release.jiraReleaseDate,
                            release.releaseDate,
                            release.tagCommitDate,
                            "Il commit puntato dal tag è datato "
                                    + release.tagCommitDate
                                    + ", dopo la data effettiva di release "
                                    + release.releaseDate
                                    + ". Verificare timezone o metadati storici.",
                            release.releaseDateEvidenceUrl));
                }
            }
        }

        List<Release> allReleases() {
            return Collections.unmodifiableList(allReleases);
        }

        List<Release> taggedReleases() {
            return Collections.unmodifiableList(taggedReleases);
        }

        List<ReleaseAnomaly> releaseAnomalies() {
            return Collections.unmodifiableList(releaseAnomalies);
        }

        Release recognize(final String rawName) {
            return byCanonicalVersion.get(canonicalVersion(rawName));
        }

        Release openingVersion(final LocalDate createDate) {
            if (createDate == null) {
                return null;
            }

            Release result = null;
            for (Release release : allReleases) {
                if (!release.releaseDate.isAfter(createDate)) {
                    result = release;
                } else {
                    break;
                }
            }
            return result;
        }

        List<Release> selectFirstFraction(
                final double fraction,
                final Integer optionalMaximum) {
            int count = (int) Math.ceil(taggedReleases.size() * fraction);
            count = Math.max(1, Math.min(count, taggedReleases.size()));
            if (optionalMaximum != null) {
                count = Math.min(count, optionalMaximum);
            }

            List<Release> result = new ArrayList<>(taggedReleases.subList(0, count));
            Set<Release> selected = new HashSet<>(result);
            for (Release release : allReleases) {
                release.selectedForDataset = selected.contains(release);
            }
            return result;
        }

        Release bySequence(final int sequence) {
            if (sequence < 0 || sequence >= allReleases.size()) {
                return null;
            }
            return allReleases.get(sequence);
        }
    }

    private static final class TicketLifecycleBuilder {

        private TicketLifecycleBuilder() {
        }

        static Ticket buildPreliminary(
                final IssueRaw issue,
                final ReleaseCatalog catalog) {
            Ticket ticket = new Ticket(issue, catalog);
            ticket.openingVersion = catalog.openingVersion(dateOf(issue.createDate));

            if (ticket.openingVersion == null) {
                ticket.dataGaps.add("OPENING_VERSION_NOT_FOUND");
            }

            ticket.rawAffectedRecognized.addAll(recognizeVersions(
                    issue.affectedVersionsRaw,
                    catalog,
                    ticket.unrecognizedAffectedVersions));

            ticket.rawFixedRecognized.addAll(recognizeVersions(
                    issue.fixedVersionsRaw,
                    catalog,
                    ticket.unrecognizedFixedVersions));

            if (!ticket.unrecognizedAffectedVersions.isEmpty()) {
                ticket.dataGaps.add("UNRECOGNIZED_AFFECTED_VERSION");
            }
            if (!ticket.unrecognizedFixedVersions.isEmpty()) {
                ticket.dataGaps.add("UNRECOGNIZED_FIXED_VERSION");
            }

            LinkedHashSet<Release> effectiveAffected = new LinkedHashSet<>();
            effectiveAffected.addAll(ticket.rawAffectedRecognized);

            if (ticket.rawFixedRecognized.isEmpty()) {
                ticket.affectedVersions.addAll(effectiveAffected.stream()
                        .sorted(Comparator.comparingInt(release -> release.sequence))
                        .toList());
                if (!ticket.affectedVersions.isEmpty()) {
                    ticket.injectedVersion = ticket.affectedVersions.getFirst();
                    ticket.injectedVersionSource = "AFFECTED_VERSION";
                }
                ticket.dataGaps.add("FIXED_VERSION_NOT_FOUND");
                return ticket;
            }

            ticket.rawFixedRecognized.sort(Comparator.comparingInt(release -> release.sequence));
            ticket.fixedVersion = ticket.rawFixedRecognized.getLast();

            if (ticket.rawFixedRecognized.size() > 1) {
                effectiveAffected.addAll(
                        ticket.rawFixedRecognized.subList(
                                0,
                                ticket.rawFixedRecognized.size() - 1));
            }

            ticket.affectedVersions.addAll(effectiveAffected.stream()
                    .sorted(Comparator.comparingInt(release -> release.sequence))
                    .toList());

            if (!ticket.affectedVersions.isEmpty()) {
                ticket.injectedVersion = ticket.affectedVersions.getFirst();
                ticket.injectedVersionSource = "AFFECTED_VERSION";
            }

            return ticket;
        }

        static void estimateMissingInjectedVersions(
                final List<Ticket> tickets,
                final ReleaseCatalog catalog) {

            List<Ticket> ordered = new ArrayList<>(tickets);
            ordered.sort(Comparator
                    .comparing(Ticket::fixOrderingDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(ticket -> ticket.issue.key));

            List<Double> previousObservedProportions = new ArrayList<>();

            for (Ticket ticket : ordered) {
                if (ticket.injectedVersion != null) {
                    Double observed = observedProportion(ticket);
                    if (observed != null) {
                        ticket.proportionUsed = observed;
                        ticket.priorProportionObservationCount = previousObservedProportions.size();
                        previousObservedProportions.add(observed);
                    }
                    continue;
                }

                ticket.priorProportionObservationCount = previousObservedProportions.size();
                if (ticket.openingVersion == null || ticket.fixedVersion == null) {
                    ticket.injectedVersionSource = "NOT_ESTIMABLE";
                    continue;
                }

                if (ticket.fixedVersion.sequence < ticket.openingVersion.sequence) {
                    ticket.injectedVersionSource = "NOT_ESTIMABLE";
                    continue;
                }

                if (ticket.fixedVersion.sequence == ticket.openingVersion.sequence) {
                    ticket.injectedVersion = ticket.openingVersion;
                    ticket.injectedVersionSource = "SAME_AS_OPENING_VERSION";
                    ticket.proportionUsed = 1.0d;
                    continue;
                }

                if (previousObservedProportions.isEmpty()) {
                    ticket.injectedVersion = ticket.openingVersion;
                    ticket.injectedVersionSource = "SIMPLE_COLD_START";
                    ticket.proportionUsed = 1.0d;
                    continue;
                }

                double mean = previousObservedProportions.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(1.0d);

                int predictedSequence = (int) Math.round(
                        ticket.fixedVersion.sequence
                                - (ticket.fixedVersion.sequence
                                - ticket.openingVersion.sequence) * mean);

                predictedSequence = Math.max(0, predictedSequence);
                predictedSequence = Math.min(predictedSequence,
                        ticket.openingVersion.sequence);

                ticket.injectedVersion = catalog.bySequence(predictedSequence);
                ticket.injectedVersionSource = "PROPORTION_INCREMENTAL";
                ticket.proportionUsed = mean;
            }
        }

        private static List<Release> recognizeVersions(
                final List<String> rawVersions,
                final ReleaseCatalog catalog,
                final List<String> unrecognized) {
            LinkedHashSet<Release> result = new LinkedHashSet<>();
            for (String rawVersion : rawVersions) {
                Release release = catalog.recognize(rawVersion);
                if (release == null) {
                    unrecognized.add(rawVersion);
                } else {
                    result.add(release);
                }
            }
            return result.stream()
                    .sorted(Comparator.comparingInt(release -> release.sequence))
                    .toList();
        }

        private static Double observedProportion(final Ticket ticket) {
            if (ticket.injectedVersion == null
                    || ticket.openingVersion == null
                    || ticket.fixedVersion == null) {
                return null;
            }

            int denominator = ticket.fixedVersion.sequence - ticket.openingVersion.sequence;
            if (denominator <= 0) {
                return null;
            }

            if (ticket.injectedVersion.sequence > ticket.openingVersion.sequence) {
                return null;
            }

            return (double) (ticket.fixedVersion.sequence - ticket.injectedVersion.sequence)
                    / denominator;
        }
    }

    private static final class SourceArchiveManager {

        private final GitClient git;

        SourceArchiveManager(final GitClient git) {
            this.git = git;
        }

        Path createSnapshot(final Release release)
                throws IOException, InterruptedException {
            if (release.gitTag == null) {
                throw new IllegalArgumentException(
                        "La release non possiede un tag Git: " + release.version);
            }

            Path temporaryRoot = Files.createTempDirectory(
                    "syncope-m1-" + release.canonicalVersion.replaceAll("[^a-zA-Z0-9._-]", "_"));
            Path zipFile = temporaryRoot.resolve("source.zip");
            Path sourceDirectory = temporaryRoot.resolve("source");
            Files.createDirectories(sourceDirectory);

            git.archiveTag(release.gitTag, zipFile);
            unzip(zipFile, sourceDirectory);
            Files.deleteIfExists(zipFile);
            return temporaryRoot;
        }

        private static void unzip(final Path zipFile, final Path destination)
                throws IOException {
            try (InputStream input = Files.newInputStream(zipFile);
                 ZipInputStream zipInput = new ZipInputStream(input, StandardCharsets.UTF_8)) {
                ZipEntry entry;
                while ((entry = zipInput.getNextEntry()) != null) {
                    Path target = destination.resolve(entry.getName()).normalize();
                    if (!target.startsWith(destination.normalize())) {
                        throw new IOException("Archivio ZIP non sicuro: " + entry.getName());
                    }

                    if (entry.isDirectory()) {
                        Files.createDirectories(target);
                    } else {
                        Files.createDirectories(target.getParent());
                        try (OutputStream output = Files.newOutputStream(target)) {
                            zipInput.transferTo(output);
                        }
                    }
                    zipInput.closeEntry();
                }
            }
        }
    }

    private static final class SourceMetricsAnalyzer {

        private final JavaParser parser;

        SourceMetricsAnalyzer() {
            ParserConfiguration configuration = new ParserConfiguration()
                    .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE)
                    .setCharacterEncoding(StandardCharsets.UTF_8);
            parser = new JavaParser(configuration);
        }

        List<ClassMetrics> analyze(final Path snapshotRoot, final Release release)
                throws IOException {
            Path sourceRoot = snapshotRoot.resolve("source");
            List<Path> javaFiles;
            try (var stream = Files.walk(sourceRoot)) {
                javaFiles = stream
                        .filter(Files::isRegularFile)
                        .filter(path -> isProductionJavaPath(
                                sourceRoot.relativize(path).toString()))
                        .sorted()
                        .toList();
            }

            List<FileModel> models = new ArrayList<>();
            for (Path javaFile : javaFiles) {
                models.add(parseFile(sourceRoot, javaFile));
            }

            ProjectTypeIndex typeIndex = new ProjectTypeIndex(models);
            for (FileModel model : models) {
                model.resolveProjectReferences(typeIndex);
            }
            for (FileModel model : models) {
                model.computeProjectMetrics(typeIndex);
            }

            return models.stream()
                    .map(model -> model.toClassMetrics(release))
                    .sorted(Comparator.comparing(metrics -> metrics.classPath))
                    .toList();
        }

        private FileModel parseFile(final Path sourceRoot, final Path javaFile)
                throws IOException {
            String relativePath = sourceRoot.relativize(javaFile)
                    .toString().replace('\\', '/');
            String source = Files.readString(javaFile, StandardCharsets.UTF_8);
            LineCounts lineCounts = LineCounts.count(source);

            ParseResult<CompilationUnit> parseResult = parser.parse(source);
            CompilationUnit unit = parseResult.getResult().orElse(null);

            if (unit == null) {
                return FileModel.unparsed(
                        relativePath,
                        stripJavaExtension(javaFile.getFileName().toString()),
                        lineCounts,
                        parseResult.getProblems().stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(" | ")));
            }

            String packageName = unit.getPackageDeclaration()
                    .map(declaration -> declaration.getNameAsString())
                    .orElse("");

            List<TypeDeclaration<?>> topLevelTypes = unit.getTypes();
            String primaryType = topLevelTypes.isEmpty()
                    ? stripJavaExtension(javaFile.getFileName().toString())
                    : topLevelTypes.getFirst().getNameAsString();
            String qualifiedName = packageName.isBlank()
                    ? primaryType
                    : packageName + "." + primaryType;

            FileModel model = new FileModel(
                    relativePath,
                    primaryType,
                    qualifiedName,
                    packageName,
                    lineCounts,
                    unit,
                    "");
            model.computeLocalMetrics();
            return model;
        }

        private static String stripJavaExtension(final String filename) {
            return filename.endsWith(".java")
                    ? filename.substring(0, filename.length() - 5)
                    : filename;
        }
    }

    private static final class FileModel {

        final String path;
        final String className;
        final String qualifiedName;
        final String packageName;
        final LineCounts lineCounts;
        final CompilationUnit compilationUnit;
        final String parseWarning;

        final Set<String> declaredTypeSimpleNames = new LinkedHashSet<>();
        final Set<String> referencedTypeSimpleNames = new LinkedHashSet<>();
        final Set<String> projectReferences = new LinkedHashSet<>();
        final List<String> directParentNames = new ArrayList<>();
        final Map<String, Integer> declaredMethodsByName = new LinkedHashMap<>();
        final List<MethodModel> methods = new ArrayList<>();

        int fields;
        int privateOrProtectedFields;
        int publicMethods;
        int methodCount;
        int wmc;
        int rfc;
        int lcom;
        int dit = 1;
        int noc;
        int ca;
        int ce;
        int moa;
        int inheritedMethods;
        int superMethodCalls;
        int overrideMethods;
        double dam;
        double mfa;
        double cam;
        double amc;
        double tcc;

        boolean godClass;
        boolean longMethod;
        boolean longParameterList;
        boolean dataClass;
        boolean featureEnvy;
        boolean largeClass;
        int nSmells;

        private FileModel(
                final String path,
                final String className,
                final String qualifiedName,
                final String packageName,
                final LineCounts lineCounts,
                final CompilationUnit compilationUnit,
                final String parseWarning) {
            this.path = path;
            this.className = className;
            this.qualifiedName = qualifiedName;
            this.packageName = packageName;
            this.lineCounts = lineCounts;
            this.compilationUnit = compilationUnit;
            this.parseWarning = parseWarning;
        }

        static FileModel unparsed(
                final String path,
                final String className,
                final LineCounts lineCounts,
                final String parseWarning) {
            return new FileModel(
                    path,
                    className,
                    className,
                    "",
                    lineCounts,
                    null,
                    parseWarning);
        }

        void computeLocalMetrics() {
            if (compilationUnit == null) {
                return;
            }

            for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                declaredTypeSimpleNames.add(type.getNameAsString());
            }

            for (ClassOrInterfaceType type : compilationUnit.findAll(ClassOrInterfaceType.class)) {
                referencedTypeSimpleNames.add(simpleTypeName(type.getNameAsString()));
            }
            referencedTypeSimpleNames.removeAll(declaredTypeSimpleNames);
            referencedTypeSimpleNames.removeAll(JAVA_LANG_TYPES);

            for (ClassOrInterfaceDeclaration declaration
                    : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
                declaration.getExtendedTypes().forEach(type ->
                        directParentNames.add(simpleTypeName(type.getNameAsString())));
            }

            List<FieldDeclaration> fieldDeclarations =
                    compilationUnit.findAll(FieldDeclaration.class);
            for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                fields += fieldDeclaration.getVariables().size();
                if (fieldDeclaration.isPrivate() || fieldDeclaration.isProtected()) {
                    privateOrProtectedFields += fieldDeclaration.getVariables().size();
                }
            }
            dam = fields == 0 ? 0.0d : (double) privateOrProtectedFields / fields;

            List<CallableDeclaration<?>> callables = new ArrayList<>();
            callables.addAll(compilationUnit.findAll(MethodDeclaration.class));
            callables.addAll(compilationUnit.findAll(ConstructorDeclaration.class));
            methodCount = callables.size();

            Set<String> invokedMethods = new LinkedHashSet<>();
            for (CallableDeclaration<?> callable : callables) {
                MethodModel methodModel = MethodModel.from(callable);
                methods.add(methodModel);
                wmc += methodModel.complexity;
                longMethod |= methodModel.loc >= 80 || methodModel.complexity >= 15;
                longParameterList |= callable.getParameters().size() >= 5;
                featureEnvy |= methodModel.externalCalls >= 5
                        && methodModel.externalCalls > methodModel.localCalls;
                invokedMethods.addAll(methodModel.invokedMethodNames);

                declaredMethodsByName.merge(callable.getNameAsString(), 1, Integer::sum);
                if (callable instanceof MethodDeclaration method && method.isPublic()) {
                    publicMethods++;
                }
                if (callable instanceof MethodDeclaration method
                        && method.getAnnotations().stream()
                        .anyMatch(annotation -> "Override".equals(annotation.getNameAsString()))) {
                    overrideMethods++;
                }
            }

            rfc = methodCount + invokedMethods.size();
            amc = methodCount == 0 ? 0.0d : (double) wmc / methodCount;
            superMethodCalls = countSuperCalls(compilationUnit);
            lcom = calculateLcom(methods);
            tcc = calculateTcc(methods);
            cam = calculateCam(methods);

            largeClass = lineCounts.loc >= 500 || methodCount >= 40;
            godClass = wmc >= 47 && referencedTypeSimpleNames.size() >= 5 && tcc < 0.33d;
            dataClass = fields >= 3
                    && methodCount > 0
                    && accessorRatio(callables) >= 0.80d
                    && wmc <= methodCount + 2;

            nSmells = bool(godClass)
                    + bool(longMethod)
                    + bool(longParameterList)
                    + bool(dataClass)
                    + bool(featureEnvy)
                    + bool(largeClass);
        }

        void resolveProjectReferences(final ProjectTypeIndex index) {
            projectReferences.clear();
            if (compilationUnit == null) {
                ce = 0;
                return;
            }

            for (String reference : referencedTypeSimpleNames) {
                FileModel target = index.bySimpleName.get(reference);
                if (target != null && target != this) {
                    projectReferences.add(target.qualifiedName);
                }
            }
            ce = projectReferences.size();
        }

        void computeProjectMetrics(final ProjectTypeIndex index) {
            if (compilationUnit == null) {
                return;
            }

            ca = (int) index.models.stream()
                    .filter(other -> other != this)
                    .filter(other -> other.projectReferences.contains(qualifiedName))
                    .count();

            FileModel parent = directParentNames.stream()
                    .map(index.bySimpleName::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            dit = calculateDit(parent, index, new HashSet<>());

            noc = (int) index.models.stream()
                    .filter(other -> other.directParentNames.stream()
                            .anyMatch(name -> name.equals(className)))
                    .count();

            inheritedMethods = countInheritedMethods(parent, index, new HashSet<>());
            int totalAvailableMethods = inheritedMethods + methodCount;
            mfa = totalAvailableMethods == 0
                    ? 0.0d
                    : (double) inheritedMethods / totalAvailableMethods;

            Set<String> projectSimpleNames = index.bySimpleName.keySet();
            for (FieldDeclaration field : compilationUnit.findAll(FieldDeclaration.class)) {
                for (VariableDeclarator variable : field.getVariables()) {
                    String typeName = simpleTypeName(variable.getType().asString());
                    if (projectSimpleNames.contains(typeName)) {
                        moa++;
                    }
                }
            }
        }

        ClassMetrics toClassMetrics(final Release release) {
            ClassMetrics metrics = new ClassMetrics();
            metrics.project = PROJECT_NAME;
            metrics.release = release;
            metrics.className = className;
            metrics.classPath = path;
            metrics.loc = lineCounts.loc;
            metrics.cloc = lineCounts.cloc;
            metrics.wmc = wmc;
            metrics.dit = dit;
            metrics.noc = noc;
            metrics.cbo = ce;
            metrics.rfc = rfc;
            metrics.lcom = lcom;
            metrics.ca = ca;
            metrics.ce = ce;
            metrics.npm = publicMethods;
            metrics.dam = dam;
            metrics.moa = moa;
            metrics.mfa = mfa;
            metrics.cam = cam;
            metrics.ic = superMethodCalls;
            metrics.cbm = overrideMethods + superMethodCalls;
            metrics.amc = amc;
            metrics.tcc = tcc;
            metrics.nSmells = nSmells;
            metrics.godClass = godClass;
            metrics.longMethod = longMethod;
            metrics.longParameterList = longParameterList;
            metrics.dataClass = dataClass;
            metrics.featureEnvy = featureEnvy;
            metrics.largeClass = largeClass;
            metrics.analysisWarning = parseWarning;
            return metrics;
        }

        private static int calculateDit(
                final FileModel parent,
                final ProjectTypeIndex index,
                final Set<FileModel> visited) {
            if (parent == null || !visited.add(parent)) {
                return 1;
            }
            FileModel grandParent = parent.directParentNames.stream()
                    .map(index.bySimpleName::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            return 1 + calculateDit(grandParent, index, visited);
        }

        private static int countInheritedMethods(
                final FileModel parent,
                final ProjectTypeIndex index,
                final Set<FileModel> visited) {
            if (parent == null || !visited.add(parent)) {
                return 0;
            }
            int result = parent.methodCount;
            FileModel grandParent = parent.directParentNames.stream()
                    .map(index.bySimpleName::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            return result + countInheritedMethods(grandParent, index, visited);
        }

        private static int calculateLcom(final List<MethodModel> methods) {
            int disjointPairs = 0;
            int sharedPairs = 0;
            for (int i = 0; i < methods.size(); i++) {
                for (int j = i + 1; j < methods.size(); j++) {
                    Set<String> intersection = new HashSet<>(methods.get(i).fieldNames);
                    intersection.retainAll(methods.get(j).fieldNames);
                    if (intersection.isEmpty()) {
                        disjointPairs++;
                    } else {
                        sharedPairs++;
                    }
                }
            }
            return Math.max(disjointPairs - sharedPairs, 0);
        }

        private static double calculateTcc(final List<MethodModel> methods) {
            if (methods.size() < 2) {
                return 1.0d;
            }
            int connected = 0;
            int possible = methods.size() * (methods.size() - 1) / 2;
            for (int i = 0; i < methods.size(); i++) {
                for (int j = i + 1; j < methods.size(); j++) {
                    Set<String> intersection = new HashSet<>(methods.get(i).fieldNames);
                    intersection.retainAll(methods.get(j).fieldNames);
                    if (!intersection.isEmpty()) {
                        connected++;
                    }
                }
            }
            return possible == 0 ? 1.0d : (double) connected / possible;
        }

        private static double calculateCam(final List<MethodModel> methods) {
            if (methods.isEmpty()) {
                return 0.0d;
            }
            Set<String> allParameterTypes = new LinkedHashSet<>();
            int totalMethodTypeMembership = 0;
            for (MethodModel method : methods) {
                allParameterTypes.addAll(method.parameterTypes);
                totalMethodTypeMembership += method.parameterTypes.size();
            }
            if (allParameterTypes.isEmpty()) {
                return 1.0d;
            }
            return (double) totalMethodTypeMembership
                    / (methods.size() * allParameterTypes.size());
        }

        private static int countSuperCalls(final CompilationUnit unit) {
            int count = unit.findAll(SuperExpr.class).size();
            for (MethodCallExpr call : unit.findAll(MethodCallExpr.class)) {
                if (call.getScope().isPresent()
                        && "super".equals(call.getScope().get().toString())) {
                    count++;
                }
            }
            return count;
        }

        private static double accessorRatio(final List<CallableDeclaration<?>> callables) {
            if (callables.isEmpty()) {
                return 0.0d;
            }
            long accessors = callables.stream()
                    .map(CallableDeclaration::getNameAsString)
                    .filter(name -> name.startsWith("get")
                            || name.startsWith("set")
                            || name.startsWith("is"))
                    .count();
            return (double) accessors / callables.size();
        }

        private static int bool(final boolean value) {
            return value ? 1 : 0;
        }

        private static String simpleTypeName(final String rawType) {
            if (rawType == null || rawType.isBlank()) {
                return "";
            }
            String value = rawType.replaceAll("<.*>", "")
                    .replace("[]", "")
                    .replace("? extends ", "")
                    .replace("? super ", "")
                    .trim();
            int dot = value.lastIndexOf('.');
            if (dot >= 0) {
                value = value.substring(dot + 1);
            }
            return value;
        }
    }

    private static final class MethodModel {

        final String name;
        final int loc;
        final int complexity;
        final int externalCalls;
        final int localCalls;
        final Set<String> invokedMethodNames;
        final Set<String> fieldNames;
        final Set<String> parameterTypes;

        private MethodModel(
                final String name,
                final int loc,
                final int complexity,
                final int externalCalls,
                final int localCalls,
                final Set<String> invokedMethodNames,
                final Set<String> fieldNames,
                final Set<String> parameterTypes) {
            this.name = name;
            this.loc = loc;
            this.complexity = complexity;
            this.externalCalls = externalCalls;
            this.localCalls = localCalls;
            this.invokedMethodNames = invokedMethodNames;
            this.fieldNames = fieldNames;
            this.parameterTypes = parameterTypes;
        }

        static MethodModel from(final CallableDeclaration<?> callable) {
            int loc = callable.getRange()
                    .map(range -> range.end.line - range.begin.line + 1)
                    .orElse(0);

            int complexity = 1
                    + callable.findAll(IfStmt.class).size()
                    + callable.findAll(ForStmt.class).size()
                    + callable.findAll(ForEachStmt.class).size()
                    + callable.findAll(WhileStmt.class).size()
                    + callable.findAll(DoStmt.class).size()
                    + callable.findAll(CatchClause.class).size()
                    + callable.findAll(ConditionalExpr.class).size()
                    + callable.findAll(SwitchEntry.class).size()
                    + (int) callable.findAll(BinaryExpr.class).stream()
                    .filter(expression -> expression.getOperator()
                            == BinaryExpr.Operator.AND
                            || expression.getOperator() == BinaryExpr.Operator.OR)
                    .count();

            Set<String> invokedNames = new LinkedHashSet<>();
            int externalCalls = 0;
            int localCalls = 0;
            for (MethodCallExpr call : callable.findAll(MethodCallExpr.class)) {
                invokedNames.add(call.getNameAsString());
                if (call.getScope().isEmpty()) {
                    localCalls++;
                } else {
                    String scope = call.getScope().get().toString();
                    if ("this".equals(scope) || "super".equals(scope)) {
                        localCalls++;
                    } else {
                        externalCalls++;
                    }
                }
            }

            Set<String> fieldNames = new LinkedHashSet<>();
            callable.findAll(FieldAccessExpr.class)
                    .forEach(expression -> fieldNames.add(expression.getNameAsString()));
            callable.findAll(NameExpr.class)
                    .forEach(expression -> fieldNames.add(expression.getNameAsString()));

            Set<String> parameterTypes = callable.getParameters().stream()
                    .map(parameter -> FileModel.simpleTypeName(parameter.getType().asString()))
                    .filter(value -> !value.isBlank())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            return new MethodModel(
                    callable.getNameAsString(),
                    loc,
                    complexity,
                    externalCalls,
                    localCalls,
                    invokedNames,
                    fieldNames,
                    parameterTypes);
        }
    }

    private static final class ProjectTypeIndex {

        final List<FileModel> models;
        final Map<String, FileModel> bySimpleName = new LinkedHashMap<>();

        ProjectTypeIndex(final List<FileModel> models) {
            this.models = models;
            for (FileModel model : models) {
                bySimpleName.putIfAbsent(model.className, model);
                for (String declared : model.declaredTypeSimpleNames) {
                    bySimpleName.putIfAbsent(declared, model);
                }
            }
        }
    }

    private static final class BugLabeler {

        private final List<Release> selectedReleases;
        private final List<ClassMetrics> rows;
        private final Map<Integer, Map<String, ClassMetrics>> rowsByReleaseAndPath;

        BugLabeler(
                final List<Release> selectedReleases,
                final List<ClassMetrics> rows) {
            this.selectedReleases = selectedReleases;
            this.rows = rows;
            this.rowsByReleaseAndPath = new HashMap<>();

            for (ClassMetrics row : rows) {
                rowsByReleaseAndPath
                        .computeIfAbsent(row.release.sequence, ignored -> new HashMap<>())
                        .put(row.classPath, row);
            }
        }

        List<ExcludedTicket> apply(final List<Ticket> tickets) {
            List<ExcludedTicket> excluded = new ArrayList<>();
            int lastSelectedSequence = selectedReleases.getLast().sequence;

            for (Ticket ticket : tickets) {
                String blockingReason = ticket.blockingReasonForLabeling();
                if (blockingReason != null) {
                    excluded.add(ExcludedTicket.from(ticket, blockingReason));
                    ticket.labelingStatus = "EXCLUDED";
                    continue;
                }

                if (ticket.injectedVersion.sequence > lastSelectedSequence) {
                    ticket.labelingStatus = "OUTSIDE_SELECTED_RELEASE_WINDOW";
                    continue;
                }

                Set<String> changedJavaPaths = ticket.validCommits.stream()
                        .flatMap(commit -> commit.productionJavaPaths().stream())
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                if (changedJavaPaths.isEmpty()) {
                    excluded.add(ExcludedTicket.from(
                            ticket,
                            "NO_PRODUCTION_JAVA_FILES_IN_VALID_COMMITS"));
                    ticket.labelingStatus = "EXCLUDED";
                    continue;
                }

                int matchedRows = 0;
                for (Release release : selectedReleases) {
                    if (release.sequence < ticket.injectedVersion.sequence
                            || release.sequence >= ticket.fixedVersion.sequence) {
                        continue;
                    }

                    Map<String, ClassMetrics> metricsByPath = rowsByReleaseAndPath
                            .getOrDefault(release.sequence, Map.of());

                    for (String path : changedJavaPaths) {
                        ClassMetrics row = metricsByPath.get(path);
                        if (row != null) {
                            row.buggy = true;
                            row.bugTickets.add(ticket.issue.key);
                            ticket.validCommits.stream()
                                    .filter(commit -> commit.productionJavaPaths().contains(path))
                                    .forEach(commit -> row.fixCommits.add(commit.hash));
                            matchedRows++;
                        }
                    }
                }

                if (matchedRows == 0) {
                    excluded.add(ExcludedTicket.from(
                            ticket,
                            "NO_MATCHING_CLASS_IN_SELECTED_RELEASES"));
                    ticket.labelingStatus = "EXCLUDED";
                } else {
                    ticket.labelingStatus = "USED";
                    ticket.labeledClassReleaseRows = matchedRows;
                }
            }

            for (ClassMetrics row : rows) {
                row.consistencyStatus = row.buggy ? "CONSISTENT_BUG_LINK" : "NO_BUG_LINK";
            }
            return excluded;
        }
    }

    private static final class WorkbookWriter {

        private static final int EXCEL_MAX_CELL_TEXT_LENGTH = 32_767;
        private static final String TRUNCATION_SUFFIX =
                "\n[TRUNCATED: Excel cell limit reached; see normalized workbook sheets]";

        private WorkbookWriter() {
        }

        static void write(
                final Path outputFile,
                final ReleaseCatalog releaseCatalog,
                final List<Release> selectedReleases,
                final List<Ticket> tickets,
                final List<ClassMetrics> datasetRows,
                final List<ExcludedTicket> excludedTickets,
                final double releaseFraction,
                final Instant startedAt,
                final Instant completedAt,
                final String repositoryHead) throws IOException {

            try (Workbook workbook = new XSSFWorkbook()) {
                Styles styles = new Styles(workbook);
                writeDatasetSheet(workbook, styles, datasetRows);
                writeTicketsSheet(workbook, styles, tickets);
                writeTicketCommitsSheet(workbook, styles, tickets);
                writeCommitFilesSheet(workbook, styles, tickets);
                writeReleasesSheet(workbook, styles, releaseCatalog.allReleases());
                writeReleaseAnomaliesSheet(
                        workbook,
                        styles,
                        releaseCatalog.releaseAnomalies());
                writeExcludedTicketsSheet(workbook, styles, excludedTickets);
                writeMetadataSheet(
                        workbook,
                        styles,
                        releaseCatalog,
                        selectedReleases,
                        tickets,
                        datasetRows,
                        excludedTickets,
                        releaseFraction,
                        startedAt,
                        completedAt,
                        repositoryHead);

                try (OutputStream output = Files.newOutputStream(outputFile)) {
                    workbook.write(output);
                } catch (java.nio.file.FileSystemException exception) {
                    throw new IOException(
                            "Impossibile scrivere " + outputFile
                                    + ". Chiudere il file in Excel e rieseguire.",
                            exception);
                }
            }
        }

        private static void writeDatasetSheet(
                final Workbook workbook,
                final Styles styles,
                final List<ClassMetrics> rows) {
            Sheet sheet = workbook.createSheet("Dataset");
            String[] headers = {
                    "Project", "Release", "ReleaseDate", "ClassName", "ClassPath",
                    "LOC", "CLOC", "WMC", "DIT", "NOC", "CBO", "RFC", "LCOM",
                    "Ca", "Ce", "NPM", "DAM", "MOA", "MFA", "CAM", "IC", "CBM",
                    "AMC", "TCC", "NSmells", "GodClass", "LongMethod",
                    "LongParameterList", "DataClass", "FeatureEnvy", "LargeClass",
                    "Buggy", "BugTicketCount", "BugTickets", "FixCommits",
                    "ConsistencyStatus", "AnalysisWarning"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (ClassMetrics value : rows) {
                Row row = sheet.createRow(rowIndex++);
                int column = 0;
                setString(row, column++, value.project, styles.text);
                setString(row, column++, value.release.version, styles.text);
                setDate(row, column++, value.release.releaseDate, styles.date);
                setString(row, column++, value.className, styles.text);
                setString(row, column++, value.classPath, styles.text);
                setInteger(row, column++, value.loc, styles.integer);
                setInteger(row, column++, value.cloc, styles.integer);
                setInteger(row, column++, value.wmc, styles.integer);
                setInteger(row, column++, value.dit, styles.integer);
                setInteger(row, column++, value.noc, styles.integer);
                setInteger(row, column++, value.cbo, styles.integer);
                setInteger(row, column++, value.rfc, styles.integer);
                setInteger(row, column++, value.lcom, styles.integer);
                setInteger(row, column++, value.ca, styles.integer);
                setInteger(row, column++, value.ce, styles.integer);
                setInteger(row, column++, value.npm, styles.integer);
                setDouble(row, column++, value.dam, styles.decimal);
                setInteger(row, column++, value.moa, styles.integer);
                setDouble(row, column++, value.mfa, styles.decimal);
                setDouble(row, column++, value.cam, styles.decimal);
                setInteger(row, column++, value.ic, styles.integer);
                setInteger(row, column++, value.cbm, styles.integer);
                setDouble(row, column++, value.amc, styles.decimal);
                setDouble(row, column++, value.tcc, styles.decimal);
                setInteger(row, column++, value.nSmells, styles.integer);
                setBoolean(row, column++, value.godClass, styles.booleanStyle);
                setBoolean(row, column++, value.longMethod, styles.booleanStyle);
                setBoolean(row, column++, value.longParameterList, styles.booleanStyle);
                setBoolean(row, column++, value.dataClass, styles.booleanStyle);
                setBoolean(row, column++, value.featureEnvy, styles.booleanStyle);
                setBoolean(row, column++, value.largeClass, styles.booleanStyle);
                setString(row, column++, value.buggy ? "Yes" : "No",
                        value.buggy ? styles.buggyYes : styles.buggyNo);
                setInteger(row, column++, value.bugTickets.size(), styles.integer);
                setString(row, column++, joinStrings(value.bugTickets), styles.wrapText);
                setString(row, column++, joinStrings(value.fixCommits), styles.wrapText);
                setString(row, column++, value.consistencyStatus, styles.text);
                setString(row, column, value.analysisWarning, styles.wrapText);
            }

            finishSheet(sheet, headers.length, rowIndex, datasetColumnWidths());
        }

        private static void writeTicketsSheet(
                final Workbook workbook,
                final Styles styles,
                final List<Ticket> tickets) {
            Sheet sheet = workbook.createSheet("Tickets");
            String[] headers = {
                    "ticketID", "summary", "status", "resolution", "priority",
                    "createDate", "closedDate",
                    "openingVersion", "openingVersionDate",
                    "affectedVersionCountRaw", "affectedVersionsRaw",
                    "affectedVersionCount", "affectedVersions",
                    "fixedVersionCountRaw", "fixedVersionsRaw",
                    "fixedVersion", "fixedVersionDate",
                    "injectedVersion", "injectedVersionDate", "injectedVersionSource",
                    "proportionUsed", "priorProportionObservations",
                    "commitCandidateCount", "validFixCommitCount", "selectedFixCommits",
                    "consistencyStatus", "violations", "dataGaps", "warnings",
                    "labelingStatus", "labeledClassReleaseRows"
            };
            writeHeader(sheet, headers, styles);

            List<Ticket> ordered = tickets.stream()
                    .sorted(Comparator
                            .comparing((Ticket ticket) -> ticket.issue.createDate,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(ticket -> ticket.issue.key))
                    .toList();

            int rowIndex = 1;
            for (Ticket ticket : ordered) {
                Row row = sheet.createRow(rowIndex++);
                int column = 0;
                setString(row, column++, ticket.issue.key, styles.text);
                setString(row, column++, ticket.issue.summary, styles.wrapText);
                setString(row, column++, ticket.issue.status, styles.text);
                setString(row, column++, ticket.issue.resolution, styles.text);
                setString(row, column++, ticket.issue.priority, styles.text);
                setDateTime(row, column++, ticket.issue.createDate, styles.dateTime);
                setDateTime(row, column++, ticket.issue.closedDate, styles.dateTime);
                setString(row, column++, version(ticket.openingVersion), styles.text);
                setDate(row, column++, releaseDate(ticket.openingVersion), styles.date);
                setInteger(row, column++, ticket.issue.affectedVersionsRaw.size(), styles.integer);
                setString(row, column++, joinStrings(ticket.issue.affectedVersionsRaw), styles.wrapText);
                setInteger(row, column++, ticket.affectedVersions.size(), styles.integer);
                setString(row, column++, joinVersions(ticket.affectedVersions), styles.wrapText);
                setInteger(row, column++, ticket.issue.fixedVersionsRaw.size(), styles.integer);
                setString(row, column++, joinStrings(ticket.issue.fixedVersionsRaw), styles.wrapText);
                setString(row, column++, version(ticket.fixedVersion), styles.text);
                setDate(row, column++, releaseDate(ticket.fixedVersion), styles.date);
                setString(row, column++, version(ticket.injectedVersion), styles.text);
                setDate(row, column++, releaseDate(ticket.injectedVersion), styles.date);
                setString(row, column++, ticket.injectedVersionSource, styles.text);
                setNullableDouble(row, column++, ticket.proportionUsed, styles.decimal);
                setInteger(row, column++, ticket.priorProportionObservationCount, styles.integer);
                setInteger(row, column++, ticket.commitCandidates.size(), styles.integer);
                setInteger(row, column++, ticket.validCommits.size(), styles.integer);
                setString(row, column++, ticket.validCommits.stream()
                        .map(commit -> commit.hash)
                        .collect(Collectors.joining(" | ")), styles.wrapText);
                setString(row, column++, ticket.consistencyStatus.name(),
                        ticket.consistencyStatus == ConsistencyStatus.INCONSISTENT
                                ? styles.error
                                : ticket.consistencyStatus == ConsistencyStatus.NOT_FULLY_CHECKABLE
                                  ? styles.warning
                                  : styles.ok);
                setString(row, column++, joinStrings(ticket.violations), styles.wrapText);
                setString(row, column++, joinStrings(ticket.dataGaps), styles.wrapText);
                setString(row, column++, joinStrings(ticket.warnings), styles.wrapText);
                setString(row, column++, ticket.labelingStatus, styles.text);
                setInteger(row, column, ticket.labeledClassReleaseRows, styles.integer);
            }

            finishSheet(sheet, headers.length, rowIndex, ticketColumnWidths());
        }

        private static void writeTicketCommitsSheet(
                final Workbook workbook,
                final Styles styles,
                final List<Ticket> tickets) {
            Sheet sheet = workbook.createSheet("TicketCommits");
            String[] headers = {
                    "ticketID", "commitHash", "commitSubject", "authorDate", "committerDate",
                    "openingVersion", "openingVersionDate", "createDate",
                    "fixedVersion", "fixedVersionDate",
                    "changedFileCount", "changedJavaFileCount", "changedJavaFiles",
                    "temporalStatus", "violations", "warnings"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (Ticket ticket : tickets) {
                for (GitCommit commit : ticket.commitCandidates) {
                    Row row = sheet.createRow(rowIndex++);
                    int column = 0;
                    setString(row, column++, ticket.issue.key, styles.text);
                    setString(row, column++, commit.hash, styles.text);
                    setString(row, column++, commit.subject, styles.wrapText);
                    setDateTime(row, column++, commit.authorDate, styles.dateTime);
                    setDateTime(row, column++, commit.committerDate, styles.dateTime);
                    setString(row, column++, version(ticket.openingVersion), styles.text);
                    setDate(row, column++, releaseDate(ticket.openingVersion), styles.date);
                    setDateTime(row, column++, ticket.issue.createDate, styles.dateTime);
                    setString(row, column++, version(ticket.fixedVersion), styles.text);
                    setDate(row, column++, releaseDate(ticket.fixedVersion), styles.date);
                    setInteger(row, column++, commit.fileChanges.size(), styles.integer);
                    Set<String> productionJavaPaths = commit.productionJavaPaths();
                    setInteger(row, column++, productionJavaPaths.size(), styles.integer);
                    setString(
                            row,
                            column++,
                            summarizeCommitJavaPaths(productionJavaPaths),
                            styles.wrapText);
                    setString(row, column++, commit.temporalValid ? "VALID_FIX_COMMIT" : "INVALID",
                            commit.temporalValid ? styles.ok : styles.error);
                    setString(row, column++, joinStrings(commit.violations), styles.wrapText);
                    setString(row, column, joinStrings(commit.warnings), styles.wrapText);
                }
            }

            finishSheet(sheet, headers.length, rowIndex, commitColumnWidths());
        }

        /**
         * Normalizza i file modificati: una riga per ticket, commit e file.
         * In questo modo nessun elenco di path deve superare il limite Excel
         * di 32.767 caratteri per singola cella.
         */
        private static void writeCommitFilesSheet(
                final Workbook workbook,
                final Styles styles,
                final List<Ticket> tickets) {
            Sheet sheet = workbook.createSheet("CommitFiles");
            String[] headers = {
                    "ticketID", "commitHash", "commitSubject", "fileStatus",
                    "oldPath", "newPath", "oldPathIsProductionJava",
                    "newPathIsProductionJava"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (Ticket ticket : tickets) {
                for (GitCommit commit : ticket.commitCandidates) {
                    for (FileChange change : commit.fileChanges) {
                        Row row = sheet.createRow(rowIndex++);
                        int column = 0;
                        setString(row, column++, ticket.issue.key, styles.text);
                        setString(row, column++, commit.hash, styles.text);
                        setString(row, column++, commit.subject, styles.wrapText);
                        setString(row, column++, change.status(), styles.text);
                        setString(row, column++, change.oldPath(), styles.wrapText);
                        setString(row, column++, change.newPath(), styles.wrapText);
                        setBoolean(
                                row,
                                column++,
                                isProductionJavaPath(change.oldPath()),
                                styles.booleanStyle);
                        setBoolean(
                                row,
                                column,
                                isProductionJavaPath(change.newPath()),
                                styles.booleanStyle);
                    }
                }
            }

            finishSheet(
                    sheet,
                    headers.length,
                    rowIndex,
                    new int[]{18, 42, 70, 14, 90, 90, 24, 24});
        }

        private static String summarizeCommitJavaPaths(
                final Collection<String> paths) {
            String complete = joinStrings(paths);
            if (complete.length() <= EXCEL_MAX_CELL_TEXT_LENGTH) {
                return complete;
            }
            return paths.size()
                    + " production Java files; full list available in CommitFiles sheet";
        }

        private static void writeReleasesSheet(
                final Workbook workbook,
                final Styles styles,
                final List<Release> releases) {
            Sheet sheet = workbook.createSheet("Releases");
            String[] headers = {
                    "sequence",
                    "jiraVersionID",
                    "version",
                    "canonicalVersion",
                    "jiraReleaseDate",
                    "effectiveReleaseDate",
                    "releaseDateSource",
                    "releaseDateCorrected",
                    "releaseDateCorrectionReason",
                    "releaseDateEvidenceURL",
                    "gitTag",
                    "tagCommitHash",
                    "tagCommitDate",
                    "archived",
                    "selectedFirst33Percent",
                    "productionJavaFileCount"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (Release release : releases) {
                Row row = sheet.createRow(rowIndex++);
                int column = 0;
                setInteger(row, column++, release.sequence, styles.integer);
                setString(row, column++, release.jiraId, styles.text);
                setString(row, column++, release.version, styles.text);
                setString(row, column++, release.canonicalVersion, styles.text);
                setDate(row, column++, release.jiraReleaseDate, styles.date);
                setDate(row, column++, release.releaseDate, styles.date);
                setString(row, column++, release.releaseDateSource, styles.text);
                setBoolean(row, column++, release.releaseDateCorrected(), styles.booleanStyle);
                setString(row, column++, release.releaseDateCorrectionReason, styles.wrapText);
                setString(row, column++, release.releaseDateEvidenceUrl, styles.wrapText);
                setString(row, column++, release.gitTag, styles.text);
                setString(row, column++, release.tagCommitHash, styles.text);
                setDate(row, column++, release.tagCommitDate, styles.date);
                setBoolean(row, column++, release.archived, styles.booleanStyle);
                setBoolean(row, column++, release.selectedForDataset, styles.booleanStyle);
                setInteger(row, column, release.productionJavaFileCount, styles.integer);
            }

            finishSheet(sheet, headers.length, rowIndex,
                    new int[]{10, 16, 22, 24, 15, 17, 25, 20, 65, 65,
                            28, 42, 15, 12, 22, 24});
        }

        private static void writeReleaseAnomaliesSheet(
                final Workbook workbook,
                final Styles styles,
                final List<ReleaseAnomaly> anomalies) {
            Sheet sheet = workbook.createSheet("ReleaseAnomalies");
            String[] headers = {
                    "anomalyType",
                    "severity",
                    "version",
                    "canonicalVersion",
                    "relatedVersion",
                    "jiraReleaseDate",
                    "effectiveReleaseDate",
                    "gitTagCommitDate",
                    "details",
                    "evidenceURL"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (ReleaseAnomaly anomaly : anomalies) {
                Row row = sheet.createRow(rowIndex++);
                int column = 0;
                setString(row, column++, anomaly.type(), styles.text);
                setString(row, column++, anomaly.severity(), styles.text);
                setString(row, column++, anomaly.version(), styles.text);
                setString(row, column++, anomaly.canonicalVersion(), styles.text);
                setString(row, column++, anomaly.relatedVersion(), styles.text);
                setDate(row, column++, anomaly.jiraReleaseDate(), styles.date);
                setDate(row, column++, anomaly.effectiveReleaseDate(), styles.date);
                setDate(row, column++, anomaly.tagCommitDate(), styles.date);
                setString(row, column++, anomaly.details(), styles.wrapText);
                setString(row, column, anomaly.evidenceUrl(), styles.wrapText);
            }

            finishSheet(sheet, headers.length, rowIndex,
                    new int[]{38, 16, 24, 26, 24, 16, 18, 18, 80, 70});
        }

        private static void writeExcludedTicketsSheet(
                final Workbook workbook,
                final Styles styles,
                final List<ExcludedTicket> excludedTickets) {
            Sheet sheet = workbook.createSheet("ExcludedTickets");
            String[] headers = {
                    "ticketID", "exclusionReason", "consistencyStatus",
                    "violations", "dataGaps", "warnings"
            };
            writeHeader(sheet, headers, styles);

            int rowIndex = 1;
            for (ExcludedTicket excluded : excludedTickets) {
                Row row = sheet.createRow(rowIndex++);
                setString(row, 0, excluded.ticketId, styles.text);
                setString(row, 1, excluded.reason, styles.error);
                setString(row, 2, excluded.consistencyStatus, styles.text);
                setString(row, 3, excluded.violations, styles.wrapText);
                setString(row, 4, excluded.dataGaps, styles.wrapText);
                setString(row, 5, excluded.warnings, styles.wrapText);
            }

            finishSheet(sheet, headers.length, rowIndex,
                    new int[]{18, 42, 24, 65, 55, 55});
        }

        private static void writeMetadataSheet(
                final Workbook workbook,
                final Styles styles,
                final ReleaseCatalog releaseCatalog,
                final List<Release> selectedReleases,
                final List<Ticket> tickets,
                final List<ClassMetrics> datasetRows,
                final List<ExcludedTicket> excludedTickets,
                final double releaseFraction,
                final Instant startedAt,
                final Instant completedAt,
                final String repositoryHead) {
            Sheet sheet = workbook.createSheet("Metadata");
            writeHeader(sheet, new String[]{"Key", "Value"}, styles);

            Map<String, String> metadata = new LinkedHashMap<>();
            metadata.put("Project", PROJECT_NAME);
            metadata.put("JIRA project", PROJECT_KEY);
            metadata.put("JQL", JQL);
            metadata.put("Repository HEAD", repositoryHead);
            metadata.put("Started UTC", startedAt.toString());
            metadata.put("Completed UTC", completedAt.toString());
            metadata.put("Release fraction", Double.toString(releaseFraction));
            metadata.put("All released JIRA versions with date",
                    Integer.toString(releaseCatalog.allReleases().size()));
            metadata.put("Versions with Git tag",
                    Integer.toString(releaseCatalog.taggedReleases().size()));
            metadata.put("Release date anomalies",
                    Integer.toString(releaseCatalog.releaseAnomalies().size()));
            metadata.put("Selected versions", Integer.toString(selectedReleases.size()));
            metadata.put("Bug tickets", Integer.toString(tickets.size()));
            metadata.put("Dataset rows", Integer.toString(datasetRows.size()));
            metadata.put("Buggy rows", Long.toString(
                    datasetRows.stream().filter(row -> row.buggy).count()));
            metadata.put("Excluded tickets", Integer.toString(excludedTickets.size()));
            metadata.put("OV rule",
                    "Latest release with releaseDate <= ticket createDate; equality allowed.");
            metadata.put("AF construction rule",
                    "AF_raw union all recognized FV except the most recent; duplicates removed.");
            metadata.put("AF consistency rule",
                    "Every effective AF must satisfy IV<=AF<=FV. "
                            + "AF may be later than OV and ticket creation.");
            metadata.put("FV rule",
                    "Most recent recognized Fixed Version by release chronology.");
            metadata.put("IV rule",
                    "Oldest effective AF; if AF is absent, Incremental Proportion; cold start IV=OV.");
            metadata.put("Proportion formula",
                    "P=(FVindex-IVindex)/(FVindex-OVindex); previous direct observations only.");
            metadata.put("Temporal consistency",
                    "IV<=AF<=FV; IV<=OV<=FV; releaseDate(OV)<=createDate"
                            + "<=fixCommitDate<=releaseDate(FV); equal dates are valid.");
            metadata.put("Release date policy",
                    "The workbook preserves the raw JIRA date and uses an effective date. "
                            + "An override is applied only when an official Apache source "
                            + "documents the conflict; Git tag commit dates are evidence, "
                            + "not substitutes for release dates.");
            metadata.put("Known official date correction",
                    "1.0.3-incubating: JIRA 2012-09-30 -> effective 2012-10-30.");
            metadata.put("Known correction source", OFFICIAL_RELEASE_HISTORY_URL);
            metadata.put("Bug propagation",
                    "Buggy from IV inclusive to the release immediately before FV: [IV,FV).");
            metadata.put("Feature extraction window",
                    "Only the first ceil(N*releaseFraction) tagged releases are analyzed.");
            metadata.put("Labeling window",
                    "100% of releases/tickets are used; a ticket is used when IV is in the selected window.");
            metadata.put("Source snapshot",
                    "Temporary ZIP generated locally with git archive from the official release tag.");
            metadata.put("Production Java filtering",
                    "Excludes tests, integration tests, generated sources, target/build and test-like filenames.");
            metadata.put("Dataset granularity",
                    "One row per release and production Java source file; top-level types are aggregated per file.");
            metadata.put("Commit ticket-key matching",
                    "Exact SYNCOPE-<digits> matching; Maven release strings such as "
                            + "syncope-1.0.0 are explicitly excluded.");
            metadata.put("Labeling status",
                    "USED=contributes to labels; EXCLUDED=blocked or has no matching class; "
                            + "OUTSIDE_SELECTED_RELEASE_WINDOW=valid ticket with IV outside the first 33%.");
            metadata.put("Commit file normalization",
                    "The CommitFiles sheet stores one row per ticket, commit and changed file. "
                            + "TicketCommits contains only a compact path summary when an Excel cell would exceed 32767 characters.");
            metadata.put("Excel text limit",
                    "Text cells are defensively truncated at 32767 characters; normalized details remain in dedicated sheets where available.");
            metadata.put("Metrics",
                    "LOC,CLOC,WMC,DIT,NOC,CBO,RFC,LCOM,Ca,Ce,NPM,DAM,MOA,MFA,CAM,IC,CBM,AMC,TCC.");
            metadata.put("Metric approximation note",
                    "Metrics are source-level AST metrics. IC=super calls; CBM=override methods+super calls; CBO=Ce.");
            metadata.put("Smells",
                    "GodClass,LongMethod,LongParameterList,DataClass,FeatureEnvy,LargeClass; NSmells is their count.");
            metadata.put("GodClass threshold", "WMC>=47 AND Ce>=5 AND TCC<0.33.");
            metadata.put("LongMethod threshold", "At least one method LOC>=80 OR complexity>=15.");
            metadata.put("LongParameterList threshold", "At least one callable with >=5 parameters.");
            metadata.put("DataClass threshold", "fields>=3, accessor ratio>=0.80 and WMC<=methodCount+2.");
            metadata.put("FeatureEnvy threshold", "external calls>=5 and external calls>local calls.");
            metadata.put("LargeClass threshold", "LOC>=500 OR methodCount>=40.");

            int rowIndex = 1;
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                setString(row, 0, entry.getKey(), styles.metadataKey);
                setString(row, 1, entry.getValue(), styles.wrapText);
            }

            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, rowIndex - 1, 0, 1));
            sheet.setColumnWidth(0, 34 * 256);
            sheet.setColumnWidth(1, 110 * 256);
        }

        private static void writeHeader(
                final Sheet sheet,
                final String[] headers,
                final Styles styles) {
            Row header = sheet.createRow(0);
            header.setHeightInPoints(28);
            for (int column = 0; column < headers.length; column++) {
                Cell cell = header.createCell(column);
                cell.setCellValue(headers[column]);
                cell.setCellStyle(styles.header);
            }
        }

        private static void finishSheet(
                final Sheet sheet,
                final int columnCount,
                final int rowCount,
                final int[] widths) {
            sheet.createFreezePane(0, 1);
            if (rowCount > 1) {
                sheet.setAutoFilter(new CellRangeAddress(
                        0,
                        rowCount - 1,
                        0,
                        columnCount - 1));
            }
            for (int column = 0; column < columnCount; column++) {
                int width = column < widths.length ? widths[column] : 16;
                width = Math.max(8, Math.min(width, 120));
                sheet.setColumnWidth(column, width * 256);
            }
        }

        private static int[] datasetColumnWidths() {
            int[] widths = new int[37];
            Arrays.fill(widths, 12);
            widths[0] = 20;
            widths[1] = 18;
            widths[2] = 14;
            widths[3] = 30;
            widths[4] = 75;
            widths[33] = 45;
            widths[34] = 45;
            widths[35] = 24;
            widths[36] = 60;
            return widths;
        }

        private static int[] ticketColumnWidths() {
            return new int[]{
                    18, 65, 16, 18, 14, 22, 22, 20, 16,
                    18, 55, 18, 55, 18, 55, 20, 16, 20, 16, 26,
                    18, 20, 18, 18, 55, 22, 70, 55, 55, 26, 22
            };
        }

        private static int[] commitColumnWidths() {
            return new int[]{
                    18, 42, 70, 22, 22, 20, 16, 22, 20, 16,
                    18, 20, 85, 24, 60, 50
            };
        }

        private static void setString(
                final Row row,
                final int column,
                final String value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            cell.setCellValue(excelSafeText(value));
            cell.setCellStyle(style);
        }

        private static String excelSafeText(final String value) {
            if (value == null) {
                return "";
            }
            if (value.length() <= EXCEL_MAX_CELL_TEXT_LENGTH) {
                return value;
            }

            int maximumPrefixLength =
                    EXCEL_MAX_CELL_TEXT_LENGTH - TRUNCATION_SUFFIX.length();
            return value.substring(0, Math.max(0, maximumPrefixLength))
                    + TRUNCATION_SUFFIX;
        }

        private static void setInteger(
                final Row row,
                final int column,
                final int value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }

        private static void setDouble(
                final Row row,
                final int column,
                final double value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }

        private static void setNullableDouble(
                final Row row,
                final int column,
                final Double value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            if (value != null) {
                cell.setCellValue(value);
            }
            cell.setCellStyle(style);
        }

        private static void setBoolean(
                final Row row,
                final int column,
                final boolean value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }

        private static void setDate(
                final Row row,
                final int column,
                final LocalDate value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            if (value != null) {
                cell.setCellValue(value);
            }
            cell.setCellStyle(style);
        }

        private static void setDateTime(
                final Row row,
                final int column,
                final OffsetDateTime value,
                final CellStyle style) {
            Cell cell = row.createCell(column);
            if (value != null) {
                cell.setCellValue(value.toLocalDateTime());
            }
            cell.setCellStyle(style);
        }

        private static String version(final Release release) {
            return release == null ? "" : release.version;
        }

        private static LocalDate releaseDate(final Release release) {
            return release == null ? null : release.releaseDate;
        }
    }

    private static final class Styles {

        final CellStyle header;
        final CellStyle text;
        final CellStyle wrapText;
        final CellStyle integer;
        final CellStyle decimal;
        final CellStyle date;
        final CellStyle dateTime;
        final CellStyle booleanStyle;
        final CellStyle buggyYes;
        final CellStyle buggyNo;
        final CellStyle error;
        final CellStyle warning;
        final CellStyle ok;
        final CellStyle metadataKey;

        Styles(final Workbook workbook) {
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            header = workbook.createCellStyle();
            header.setFont(headerFont);
            header.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setAlignment(HorizontalAlignment.CENTER);
            header.setVerticalAlignment(VerticalAlignment.CENTER);
            header.setWrapText(true);
            addBorders(header);

            text = workbook.createCellStyle();
            text.setVerticalAlignment(VerticalAlignment.TOP);
            addBorders(text);

            wrapText = workbook.createCellStyle();
            wrapText.cloneStyleFrom(text);
            wrapText.setWrapText(true);

            integer = workbook.createCellStyle();
            integer.cloneStyleFrom(text);
            integer.setDataFormat(workbook.createDataFormat().getFormat("0"));

            decimal = workbook.createCellStyle();
            decimal.cloneStyleFrom(text);
            decimal.setDataFormat(workbook.createDataFormat().getFormat("0.000000"));

            date = workbook.createCellStyle();
            date.cloneStyleFrom(text);
            date.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));

            dateTime = workbook.createCellStyle();
            dateTime.cloneStyleFrom(text);
            dateTime.setDataFormat(workbook.createDataFormat()
                    .getFormat("yyyy-mm-dd hh:mm:ss"));

            booleanStyle = workbook.createCellStyle();
            booleanStyle.cloneStyleFrom(text);
            booleanStyle.setAlignment(HorizontalAlignment.CENTER);

            buggyYes = coloredStyle(workbook, IndexedColors.CORAL);
            buggyNo = coloredStyle(workbook, IndexedColors.LIGHT_GREEN);
            error = coloredStyle(workbook, IndexedColors.ROSE);
            warning = coloredStyle(workbook, IndexedColors.LIGHT_YELLOW);
            ok = coloredStyle(workbook, IndexedColors.LIGHT_GREEN);

            Font keyFont = workbook.createFont();
            keyFont.setBold(true);
            metadataKey = workbook.createCellStyle();
            metadataKey.cloneStyleFrom(wrapText);
            metadataKey.setFont(keyFont);
            metadataKey.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            metadataKey.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        private static CellStyle coloredStyle(
                final Workbook workbook,
                final IndexedColors color) {
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setVerticalAlignment(VerticalAlignment.TOP);
            style.setWrapText(true);
            addBorders(style);
            return style;
        }

        private static void addBorders(final CellStyle style) {
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
    }

    private static final class Ticket {

        final IssueRaw issue;
        final ReleaseCatalog releaseCatalog;
        final List<Release> rawAffectedRecognized = new ArrayList<>();
        final List<Release> rawFixedRecognized = new ArrayList<>();
        final List<Release> affectedVersions = new ArrayList<>();
        final List<String> unrecognizedAffectedVersions = new ArrayList<>();
        final List<String> unrecognizedFixedVersions = new ArrayList<>();
        final List<GitCommit> commitCandidates = new ArrayList<>();
        final List<GitCommit> validCommits = new ArrayList<>();
        final List<String> violations = new ArrayList<>();
        final List<String> dataGaps = new ArrayList<>();
        final List<String> warnings = new ArrayList<>();

        Release openingVersion;
        Release fixedVersion;
        Release injectedVersion;
        String injectedVersionSource = "";
        Double proportionUsed;
        int priorProportionObservationCount;
        ConsistencyStatus consistencyStatus = ConsistencyStatus.NOT_FULLY_CHECKABLE;
        String labelingStatus = "NOT_PROCESSED";
        int labeledClassReleaseRows;

        Ticket(final IssueRaw issue, final ReleaseCatalog releaseCatalog) {
            this.issue = issue;
            this.releaseCatalog = releaseCatalog;
        }

        void evaluateCommitCandidates() {
            validCommits.clear();
            for (GitCommit commit : commitCandidates) {
                commit.evaluateFor(this);
                if (commit.temporalValid) {
                    validCommits.add(commit);
                }
            }
        }

        void validateLifecycle() {
            violations.clear();

            LocalDate createDate = dateOf(issue.createDate);
            LocalDate closedDate = dateOf(issue.closedDate);

            if (issue.createDate == null) {
                addGap("MISSING_CREATE_DATE");
            }
            if (issue.createDate != null
                    && issue.closedDate != null
                    && issue.createDate.isAfter(issue.closedDate)) {
                addViolation("CREATE_DATE_AFTER_CLOSED_DATE");
            }

            if (openingVersion == null) {
                addGap("MISSING_OPENING_VERSION");
            } else if (createDate != null
                    && openingVersion.releaseDate.isAfter(createDate)) {
                addViolation("OPENING_VERSION_AFTER_TICKET_CREATION");
            }

            if (fixedVersion == null) {
                addGap("MISSING_FIXED_VERSION");
            }

            if (injectedVersion == null) {
                addGap("MISSING_INJECTED_VERSION");
            }

            if (injectedVersion != null && openingVersion != null
                    && injectedVersion.sequence > openingVersion.sequence) {
                addViolation("INJECTED_VERSION_AFTER_OPENING_VERSION");
            }

            if (openingVersion != null && fixedVersion != null
                    && openingVersion.sequence > fixedVersion.sequence) {
                addViolation("OPENING_VERSION_AFTER_FIXED_VERSION");
            }

            if (injectedVersion != null && fixedVersion != null
                    && injectedVersion.sequence > fixedVersion.sequence) {
                addViolation("INJECTED_VERSION_AFTER_FIXED_VERSION");
            }

            /*
             * Regola definitiva per le Affected Version:
             *
             *     IV <= AF <= FV
             *
             * Le AF possono essere successive alla OV e alla data di apertura
             * del ticket, perché il difetto può restare presente in release
             * pubblicate mentre il ticket è ancora aperto.
             */
            for (Release affected : affectedVersions) {
                if (injectedVersion != null
                        && affected.sequence < injectedVersion.sequence) {
                    addViolation("AFFECTED_VERSION_BEFORE_INJECTED_VERSION:"
                            + affected.version);
                }
                if (fixedVersion != null
                        && affected.sequence > fixedVersion.sequence) {
                    addViolation("AFFECTED_VERSION_AFTER_FIXED_VERSION:"
                            + affected.version);
                }
            }

            if (closedDate != null && fixedVersion != null
                    && closedDate.isAfter(fixedVersion.releaseDate)) {
                addWarning("TICKET_CLOSED_AFTER_FIXED_VERSION_RELEASE");
            }

            if (commitCandidates.isEmpty()) {
                addGap("NO_FIX_COMMIT_FOUND");
            } else if (validCommits.isEmpty()) {
                addViolation("NO_TEMPORALLY_VALID_FIX_COMMIT");
            } else if (validCommits.stream()
                    .flatMap(commit -> commit.productionJavaPaths().stream())
                    .findAny().isEmpty()) {
                addGap("NO_PRODUCTION_JAVA_FILES_IN_VALID_COMMITS");
            }

            if (!violations.isEmpty()) {
                consistencyStatus = ConsistencyStatus.INCONSISTENT;
            } else if (!dataGaps.isEmpty()) {
                consistencyStatus = ConsistencyStatus.NOT_FULLY_CHECKABLE;
            } else {
                consistencyStatus = ConsistencyStatus.CONSISTENT;
            }
        }

        LocalDate fixOrderingDate() {
            LocalDate commitDate = minDate(validCommits);
            if (commitDate != null) {
                return commitDate;
            }
            if (issue.closedDate != null) {
                return issue.closedDate.toLocalDate();
            }
            return fixedVersion == null ? null : fixedVersion.releaseDate;
        }

        String blockingReasonForLabeling() {
            if (consistencyStatus == ConsistencyStatus.INCONSISTENT) {
                return "INCONSISTENT_LIFECYCLE";
            }
            if (openingVersion == null) {
                return "NO_OPENING_VERSION";
            }
            if (fixedVersion == null) {
                return "NO_FIXED_VERSION";
            }
            if (injectedVersion == null) {
                return "IV_NOT_ESTIMABLE";
            }
            if (validCommits.isEmpty()) {
                return commitCandidates.isEmpty()
                        ? "NO_FIX_COMMIT"
                        : "FIX_COMMIT_OUTSIDE_VALID_INTERVAL";
            }
            return null;
        }

        private void addViolation(final String value) {
            if (!violations.contains(value)) {
                violations.add(value);
            }
        }

        private void addGap(final String value) {
            if (!dataGaps.contains(value)) {
                dataGaps.add(value);
            }
        }

        private void addWarning(final String value) {
            if (!warnings.contains(value)) {
                warnings.add(value);
            }
        }
    }

    private static final class GitCommit {

        final String hash;
        final OffsetDateTime authorDate;
        final OffsetDateTime committerDate;
        final String subject;
        final List<FileChange> fileChanges = new ArrayList<>();
        final List<String> violations = new ArrayList<>();
        final List<String> warnings = new ArrayList<>();
        boolean temporalValid;

        GitCommit(
                final String hash,
                final OffsetDateTime authorDate,
                final OffsetDateTime committerDate,
                final String subject) {
            this.hash = hash;
            this.authorDate = authorDate;
            this.committerDate = committerDate;
            this.subject = subject;
        }

        GitCommit copy() {
            GitCommit copy = new GitCommit(hash, authorDate, committerDate, subject);
            copy.fileChanges.addAll(fileChanges);
            return copy;
        }

        void evaluateFor(final Ticket ticket) {
            violations.clear();
            warnings.clear();
            LocalDate commitDate = dateOf(committerDate);
            LocalDate createDate = dateOf(ticket.issue.createDate);

            if (commitDate == null) {
                violations.add("MISSING_COMMITTER_DATE");
            }
            if (createDate != null && commitDate != null
                    && commitDate.isBefore(createDate)) {
                violations.add("FIX_COMMIT_BEFORE_TICKET_CREATION");
            }
            if (ticket.openingVersion != null && commitDate != null
                    && commitDate.isBefore(ticket.openingVersion.releaseDate)) {
                violations.add("FIX_COMMIT_BEFORE_OPENING_VERSION");
            }
            if (ticket.fixedVersion != null && commitDate != null
                    && commitDate.isAfter(ticket.fixedVersion.releaseDate)) {
                violations.add("FIX_COMMIT_AFTER_FIXED_VERSION");
            }
            if (productionJavaPaths().isEmpty()) {
                warnings.add("NO_PRODUCTION_JAVA_FILES");
            }

            temporalValid = violations.isEmpty();
        }

        Set<String> productionJavaPaths() {
            LinkedHashSet<String> paths = new LinkedHashSet<>();
            for (FileChange change : fileChanges) {
                if (isProductionJavaPath(change.oldPath)) {
                    paths.add(change.oldPath);
                }
                if (isProductionJavaPath(change.newPath)) {
                    paths.add(change.newPath);
                }
            }
            return paths;
        }
    }

    private static final class Release {

        final String jiraId;
        final String version;
        final String canonicalVersion;
        final LocalDate jiraReleaseDate;
        final LocalDate releaseDate;
        final String releaseDateSource;
        final String releaseDateCorrectionReason;
        final String releaseDateEvidenceUrl;
        final String gitTag;
        final String tagCommitHash;
        final LocalDate tagCommitDate;
        final boolean archived;
        int sequence;
        boolean selectedForDataset;
        int productionJavaFileCount;

        Release(
                final String jiraId,
                final String version,
                final String canonicalVersion,
                final LocalDate jiraReleaseDate,
                final LocalDate releaseDate,
                final String releaseDateSource,
                final String releaseDateCorrectionReason,
                final String releaseDateEvidenceUrl,
                final String gitTag,
                final String tagCommitHash,
                final LocalDate tagCommitDate,
                final boolean archived) {
            this.jiraId = jiraId;
            this.version = version;
            this.canonicalVersion = canonicalVersion;
            this.jiraReleaseDate = jiraReleaseDate;
            this.releaseDate = releaseDate;
            this.releaseDateSource = releaseDateSource;
            this.releaseDateCorrectionReason = releaseDateCorrectionReason;
            this.releaseDateEvidenceUrl = releaseDateEvidenceUrl;
            this.gitTag = gitTag;
            this.tagCommitHash = tagCommitHash;
            this.tagCommitDate = tagCommitDate;
            this.archived = archived;
        }

        boolean releaseDateCorrected() {
            return !Objects.equals(jiraReleaseDate, releaseDate);
        }
    }

    private static final class ClassMetrics {

        String project;
        Release release;
        String className;
        String classPath;
        int loc;
        int cloc;
        int wmc;
        int dit;
        int noc;
        int cbo;
        int rfc;
        int lcom;
        int ca;
        int ce;
        int npm;
        double dam;
        int moa;
        double mfa;
        double cam;
        int ic;
        int cbm;
        double amc;
        double tcc;
        int nSmells;
        boolean godClass;
        boolean longMethod;
        boolean longParameterList;
        boolean dataClass;
        boolean featureEnvy;
        boolean largeClass;
        boolean buggy;
        final Set<String> bugTickets = new TreeSet<>();
        final Set<String> fixCommits = new TreeSet<>();
        String consistencyStatus = "NO_BUG_LINK";
        String analysisWarning = "";
    }

    private static final class LineCounts {

        final int loc;
        final int cloc;

        LineCounts(final int loc, final int cloc) {
            this.loc = loc;
            this.cloc = cloc;
        }

        static LineCounts count(final String source) {
            int loc = 0;
            int cloc = 0;
            boolean insideBlockComment = false;

            for (String rawLine : source.split("\\R", -1)) {
                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }

                boolean containsCode = false;
                boolean containsComment = false;
                int index = 0;

                while (index < line.length()) {
                    if (insideBlockComment) {
                        containsComment = true;
                        int end = line.indexOf("*/", index);
                        if (end < 0) {
                            index = line.length();
                        } else {
                            insideBlockComment = false;
                            index = end + 2;
                        }
                        continue;
                    }

                    int lineComment = line.indexOf("//", index);
                    int blockComment = line.indexOf("/*", index);
                    int nextComment;
                    boolean block;
                    if (lineComment < 0) {
                        nextComment = blockComment;
                        block = true;
                    } else if (blockComment < 0 || lineComment < blockComment) {
                        nextComment = lineComment;
                        block = false;
                    } else {
                        nextComment = blockComment;
                        block = true;
                    }

                    if (nextComment < 0) {
                        if (!line.substring(index).isBlank()) {
                            containsCode = true;
                        }
                        break;
                    }

                    if (!line.substring(index, nextComment).isBlank()) {
                        containsCode = true;
                    }
                    containsComment = true;

                    if (!block) {
                        break;
                    }
                    insideBlockComment = true;
                    index = nextComment + 2;
                }

                if (containsCode) {
                    loc++;
                }
                if (containsComment) {
                    cloc++;
                }
            }
            return new LineCounts(loc, cloc);
        }
    }

    private record ReleaseDateOverride(
            LocalDate effectiveDate,
            String evidenceUrl,
            String reason) {
    }

    private record ReleaseAnomaly(
            String type,
            String severity,
            String version,
            String canonicalVersion,
            String relatedVersion,
            LocalDate jiraReleaseDate,
            LocalDate effectiveReleaseDate,
            LocalDate tagCommitDate,
            String details,
            String evidenceUrl) {
    }

    private record JiraVersion(
            String id,
            String name,
            String canonical,
            boolean released,
            boolean archived,
            LocalDate releaseDate,
            String description) {
    }

    private record IssueRaw(
            String id,
            String key,
            String summary,
            String status,
            String resolution,
            String priority,
            OffsetDateTime createDate,
            OffsetDateTime closedDate,
            List<String> affectedVersionsRaw,
            List<String> fixedVersionsRaw) {
    }

    private record FileChange(
            String status,
            String oldPath,
            String newPath) {
    }

    private record ExcludedTicket(
            String ticketId,
            String reason,
            String consistencyStatus,
            String violations,
            String dataGaps,
            String warnings) {

        static ExcludedTicket from(final Ticket ticket, final String reason) {
            return new ExcludedTicket(
                    ticket.issue.key,
                    reason,
                    ticket.consistencyStatus.name(),
                    joinStrings(ticket.violations),
                    joinStrings(ticket.dataGaps),
                    joinStrings(ticket.warnings));
        }
    }

    private record VersionParts(
            int major,
            int minor,
            int patch,
            int qualifierRank,
            int qualifierNumber,
            String normalized) {

        static VersionParts parse(final String rawVersion) {
            String normalized = canonicalVersion(rawVersion);
            Matcher matcher = VERSION_NUMBERS_PATTERN.matcher(normalized);
            if (!matcher.matches()) {
                return new VersionParts(0, 0, 0, 0, 0, normalized);
            }

            int major = parseInt(matcher.group(1));
            int minor = parseInt(matcher.group(2));
            int patch = parseInt(matcher.group(3));
            String qualifier = Optional.ofNullable(matcher.group(4)).orElse("")
                    .replaceFirst("^-", "");

            /*
             * "incubating" descrive lo stato del progetto Apache, non rende
             * una release precedente a milestone o release candidate.
             * Lo rimuoviamo prima di classificare il vero qualificatore.
             */
            String lifecycleQualifier = qualifier
                    .replaceAll("(?i)(^|-)incubating($|-)", "-")
                    .replaceAll("-+", "-")
                    .replaceAll("^-|-$", "");

            int rank = 100;
            int number = 0;
            if (!lifecycleQualifier.isBlank()) {
                if (lifecycleQualifier.contains("snapshot")) {
                    rank = 0;
                } else if (lifecycleQualifier.startsWith("alpha")) {
                    rank = 20;
                } else if (lifecycleQualifier.startsWith("beta")) {
                    rank = 30;
                } else if (lifecycleQualifier.startsWith("m")) {
                    rank = 40;
                } else if (lifecycleQualifier.startsWith("rc")) {
                    rank = 50;
                } else {
                    rank = 60;
                }

                Matcher digits = Pattern.compile("(\\d+)")
                        .matcher(lifecycleQualifier);
                if (digits.find()) {
                    number = parseInt(digits.group(1));
                }
            }

            return new VersionParts(major, minor, patch, rank, number, normalized);
        }

        private static int parseInt(final String value) {
            if (value == null || value.isBlank()) {
                return 0;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                return 0;
            }
        }
    }

    private enum ConsistencyStatus {
        CONSISTENT,
        INCONSISTENT,
        NOT_FULLY_CHECKABLE
    }
}
