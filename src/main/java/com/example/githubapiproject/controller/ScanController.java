package com.example.githubapiproject.controller;


import com.example.githubapiproject.model.Scan;
import com.example.githubapiproject.service.ScanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping
    public List<Scan> getAllScans() {
        return scanService.getAllScans();
    }

    @GetMapping("/{id}")
    public Scan getScanById(
            @PathVariable String id) {

        return scanService.getScanById(id);
    }

    @PostMapping
    public Scan startScan() {
        return scanService.startScan();
    }

    @PostMapping("/run")
    public Scan runScan(){
        return scanService.runScan();
    }
}