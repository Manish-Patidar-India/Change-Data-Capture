package com.sample.inventory;

import com.sample.inventory.entity.source.AddressBookEntry;
import com.sample.inventory.entity.target.TargetAddressBookEntry;
import com.sample.inventory.repository.source.AddressBookRepository;
import com.sample.inventory.repository.target.TargetAddressBookRepository;
import com.sample.inventory.service.ConnectorsOperation;
import com.sample.inventory.service.StreamingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StreamingServiceTest {

    @Mock
    private AddressBookRepository repository;

    @Mock
    private TargetAddressBookRepository targetAddressBookRepository;

    @Mock
    private ConnectorsOperation connectorsOperation;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StreamingService streamingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        streamingService.kafkaConnectUrl = "http://localhost:8083";
        streamingService.jdbcConnectorUrl = "http://localhost:8084";
    }

    @Test
    void testStartStreaming() {
        streamingService.startStreaming();
        verify(connectorsOperation, times(1)).startDebeziumConnector();
        verify(connectorsOperation, times(1)).startJdbcSinkConnector();
    }


    @Test
    void testInsertData() {
        AddressBookEntry entry = new AddressBookEntry();
        when(repository.save(any(AddressBookEntry.class))).thenReturn(entry);

        AddressBookEntry result = streamingService.insertData(entry);
        assertEquals(entry, result);
    }

    @Test
    void testRetrieveDataFromTarget() {
        List<TargetAddressBookEntry> entries = List.of(new TargetAddressBookEntry());
        when(targetAddressBookRepository.findAll()).thenReturn(entries);

        List<TargetAddressBookEntry> result = streamingService.retrieveDataFromTarget();
        assertEquals(entries, result);
    }
}
