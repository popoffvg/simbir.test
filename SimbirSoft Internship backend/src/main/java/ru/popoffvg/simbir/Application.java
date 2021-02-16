package ru.popoffvg.simbir;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

public class Application implements Callable<Integer> {

    static private final Logger LOGGER = LogManager.getLogger(Application.class);

    @Parameters(index = "0", description = "Web page address.")
    private String url;

    @Option(names = "--database-connection", defaultValue = "jdbc:h2:mem:")
    private String dataBaseUrl;

    @Option(names = "--database-user", defaultValue = "sa")
    private String dataBaseUser;

    @Option(names = "--database-password")
    private String dataBasPassword;

    @Option(names = "--buffer-size", defaultValue = "1024")
    private int bufferSize;
    private final StatisticViewer statisticViewer = new StatisticViewer();
    private StatisticStore ds;

    public static void main(String[] args) {

        // отключаем проверку SSL сертификатов
        final Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

        Application app = new Application();

        LOGGER.info("START");
        int exitCode = 0;
        try {
            exitCode = new CommandLine(app).execute(args);
        } catch (Exception e) {
            LOGGER.error("Execution error",e);
        }
        LOGGER.info("END");
        System.exit(exitCode);
    }

    public Integer call() {

        Map<String, String> dsSettings = new HashMap<>();
        dsSettings.put("javax.persistence.jdbc.url", dataBaseUrl);
        dsSettings.put("javax.persistence.jdbc.user", dataBaseUser);
        dsSettings.put("javax.persistence.jdbc.password", dataBasPassword);
        ds = new StatisticStore(dsSettings);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        TextParser parser = new TextParser(bufferSize);

        client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(Paths.get("tmp.html")))
                .thenApply(response -> {
                    try (BufferedReader input = Files.newBufferedReader(response.body(), StandardCharsets.UTF_8)) {
                        return parser.parse(input);
                    } catch (Exception e) {
                        LOGGER.error("Request error",e);
                        return null;
                    }
                })
                .thenApply(statistic -> ds.store(statistic))
                .thenAccept(statisticViewer::print).join();

        return 0;
    }
}
