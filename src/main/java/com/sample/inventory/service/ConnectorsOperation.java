package com.sample.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConnectorsOperation {

    @Value("${kafka.connect.url}")
    private String kafkaConnectUrl;

    @Value("${jdbc.sink.url}")
    private String jdbcConnectorUrl;

    @Value("${debezium.connector.name}")
    private String debeziumConnectorName;

    @Value("${debezium.database.hostname}")
    private String debeziumDatabaseHostname;

    @Value("${debezium.database.port}")
    private String debeziumDatabasePort;

    @Value("${debezium.database.user}")
    private String debeziumDatabaseUser;

    @Value("${debezium.database.password}")
    private String debeziumDatabasePassword;

    @Value("${debezium.database.dbname}")
    private String debeziumDatabaseName;

    @Value("${debezium.table.whitelist}")
    private String debeziumTableWhitelist;

    @Value("${debezium.topic.prefix}")
    private String debeziumTopicPrefix;

    @Value("${jdbc.connector.name}")
    private String jdbcConnectorName;

    @Value("${jdbc.connection.url}")
    private String jdbcConnectionUrl;

    @Value("${jdbc.connection.user}")
    private String jdbcConnectionUser;

    @Value("${jdbc.connection.password}")
    private String jdbcConnectionPassword;

    public void startDebeziumConnector() {
        RestTemplate restTemplate = new RestTemplate();
        String sourceConnectorPayload = String.format("{\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"config\": {\n" +
                        "    \"connector.class\": \"io.debezium.connector.postgresql.PostgresConnector\",\n" +
                        "    \"tasks.max\": \"1\",\n" +
                        "    \"database.hostname\": \"%s\",\n" +
                        "    \"database.port\": \"%s\",\n" +
                        "    \"database.user\": \"%s\",\n" +
                        "    \"database.password\": \"%s\",\n" +
                        "    \"database.dbname\": \"%s\",\n" +
                        "    \"plugin.name\": \"pgoutput\",\n" +
                        "    \"table.whitelist\": \"%s\",\n" +
                        "    \"tombstones.on.delete\": \"false\",\n" +
                        "    \"topic.prefix\": \"%s\"\n" +
                        "  }\n" +
                        "}",
                debeziumConnectorName,
                debeziumDatabaseHostname,
                debeziumDatabasePort,
                debeziumDatabaseUser,
                debeziumDatabasePassword,
                debeziumDatabaseName,
                debeziumTableWhitelist,
                debeziumTopicPrefix);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(sourceConnectorPayload, headers);
        restTemplate.postForEntity(kafkaConnectUrl, request, String.class);
    }

    public void startJdbcSinkConnector() {
        RestTemplate restTemplate = new RestTemplate();

        String sinkConnectorPayload = String.format("{\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"config\": {\n" +
                        "    \"connector.class\": \"io.confluent.connect.jdbc.JdbcSinkConnector\",\n" +
                        "    \"tasks.max\": \"1\",\n" +
                        "    \"topics\": \"%s\",\n" +
                        "    \"connection.url\": \"%s\",\n" +
                        "    \"connection.user\": \"%s\",\n" +
                        "    \"connection.password\": \"%s\",\n" +
                        "    \"auto.create\": \"true\",\n" +
                        "    \"auto.evolve\": \"true\",\n" +
                        "    \"pk.mode\": \"record_value\",\n" +
                        "    \"pk.fields\": \"id\",\n" +
                        "    \"insert.mode\": \"upsert\",\n" +
                        "    \"transforms\": \"Flatten\",\n" +
                        "    \"transforms.Flatten.type\": \"org.apache.kafka.connect.transforms.Flatten$Value\",\n" +
                        "    \"transforms.Flatten.delimiter\": \"_\",\n" +
                        "    \"key.converter\": \"io.confluent.connect.avro.AvroConverter\",\n" +
                        "    \"key.converter.schema.registry.url\": \"http://schema-registry:8081\",\n" +
                        "    \"value.converter\": \"io.confluent.connect.avro.AvroConverter\",\n" +
                        "    \"value.converter.schema.registry.url\": \"http://schema-registry:8081\"\n" +
                        "  }\n" +
                        "}",
                jdbcConnectorName,
                debeziumTopicPrefix + ".public.address_book",
                jdbcConnectionUrl,
                jdbcConnectionUser,
                jdbcConnectionPassword);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(sinkConnectorPayload, headers);
        restTemplate.postForEntity(jdbcConnectorUrl, request, String.class);
    }


}
