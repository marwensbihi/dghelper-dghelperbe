package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Marwen Sbihi
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/specifications")
public class SpecificationController {
    @Autowired
    SpecificationService specificationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> getAll(@RequestParam(value = "level", defaultValue = "0") Integer level) {
        return specificationService.getAll(level);
    }

    @GetMapping(value = "{specificationId}", produces = "application/json")
    public ResponseEntity<Response> getById(@PathVariable("specificationId") String specificationId, @RequestParam(value = "level", defaultValue = "0") Integer level) {
        return specificationService.getById(specificationId, level);
    }

    @PostMapping(value = "upload", produces = "application/json")
    public ResponseEntity<Response> uploadCSV(@RequestParam("file") MultipartFile file) {
        return specificationService.uploadCSV(file);
    }
}
