package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Marwen Sbihi
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/domains")
public class DomainController {
    @Autowired
    DomainService domainService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> getAll(@RequestParam(value = "level", defaultValue = "0") Integer level) {
        return domainService.getAll(level);
    }

    @GetMapping(value = "{domainId}", produces = "application/json")
    public ResponseEntity<Response> getById(@PathVariable("domainId") String domainId, @RequestParam(value = "level", defaultValue = "0") Integer level) {
        return domainService.getById(domainId, level);
    }

    @PostMapping(value = "upload", produces = "application/json")
    public ResponseEntity<Response> uploadCSV(@RequestParam("file") MultipartFile file) {
        return domainService.uploadCSV(file);
    }
}
