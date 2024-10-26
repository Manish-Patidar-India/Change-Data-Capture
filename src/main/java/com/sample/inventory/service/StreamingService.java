package com.sample.inventory.service;
import com.sample.inventory.entity.source.AddressBookEntry;
import com.sample.inventory.entity.target.TargetAddressBookEntry;
import com.sample.inventory.repository.source.AddressBookRepository;
import com.sample.inventory.repository.target.TargetAddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StreamingService {

    @Value("${kafka.connect.url}")
    public String kafkaConnectUrl;

    @Value("${jdbc.sink.url}")
    public String jdbcConnectorUrl;

    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private TargetAddressBookRepository targetAddressBookRepository;

    @Autowired
    private ConnectorsOperation connectorsOperation;



    public void startStreaming() {
        connectorsOperation.startDebeziumConnector();
       connectorsOperation. startJdbcSinkConnector();
    }

    public void stopStreaming() {
        // Delete connectors using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(kafkaConnectUrl + "/debezium-source");
        restTemplate.delete(jdbcConnectorUrl + "/jdbc-sink");
    }

    public String getStreamingStatus() {

        RestTemplate restTemplate = new RestTemplate();
        String statusSource = restTemplate.getForObject(kafkaConnectUrl + "/debezium-source/status", String.class);
        String statusSink = restTemplate.getForObject(kafkaConnectUrl + "/jdbc-sink/status", String.class);
        return "Source: " + statusSource + ", Sink: " + statusSink;
    }

    public AddressBookEntry insertData(AddressBookEntry entry) {
        return repository.save(entry);
    }

    public List<TargetAddressBookEntry> retrieveDataFromTarget() {
        return targetAddressBookRepository.findAll();
    }
}

