package com.sample.inventory.controller;




import com.sample.inventory.entity.source.AddressBookEntry;
import com.sample.inventory.entity.target.TargetAddressBookEntry;
import com.sample.inventory.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/streaming")
public class StreamingController {

    @Autowired
    private StreamingService streamingService;

    @PostMapping("/start")
    public ResponseEntity<String> startStreaming() {
        streamingService.startStreaming();
        return ResponseEntity.ok("Streaming started");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopStreaming() {
        streamingService.stopStreaming();
        return ResponseEntity.ok("Streaming stopped");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStreamingStatus() {
        String status = streamingService.getStreamingStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/data")
    public ResponseEntity<AddressBookEntry> insertData(@RequestBody AddressBookEntry entry) {
        AddressBookEntry savedEntry = streamingService.insertData(entry);
        return ResponseEntity.ok(savedEntry);
    }

    @GetMapping("/data")
    public ResponseEntity<List<TargetAddressBookEntry>> retrieveDataFromTarget() {
        List<TargetAddressBookEntry> entries = streamingService.retrieveDataFromTarget();
        return ResponseEntity.ok(entries);
    }
}

