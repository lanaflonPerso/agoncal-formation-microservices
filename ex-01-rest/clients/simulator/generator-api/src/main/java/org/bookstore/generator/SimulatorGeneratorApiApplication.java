package org.bookstore.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@ConfigurationProperties
public class SimulatorGeneratorApiApplication implements CommandLineRunner {

	private final Logger log = LoggerFactory.getLogger(SimulatorGeneratorApiApplication.class);

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SimulatorGeneratorApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int nbLoops = Integer.parseInt(env.getProperty("simulator.loop"));
		long sleep = Long.parseLong(env.getProperty("simulator.sleep"));

        getGeneratorApiHeaders();
        Thread.sleep(sleep);

        for (int i = 0; i < nbLoops; i++) {
            try {
                getGeneratorApi();
                Thread.sleep(sleep);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getGeneratorApiHeaders() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/generator/api/numbers";
        log.info("HTTP Headers on " + url);
        HttpHeaders headers = restTemplate.headForHeaders(url, String.class);
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String header : entry.getValue()) {
                log.info("\t{} : \t{}", entry, header);
            }

        }
    }

    private void getGeneratorApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/generator/api/numbers";
        log.info("HTTP GET on " + url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("\tResponse code {} - body {}", response.getStatusCode(), response.getBody());
    }
}
