package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Marwen Sbihi
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/controls")
public class ControlController {
    @Autowired
    ControlService controlService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> getAll(@RequestParam(value = "level", defaultValue = "0") Integer level) {
        return controlService.getAll(level);
    }

    @GetMapping(value = "{controlId}", produces = "application/json")
    public ResponseEntity<Response> getById(@PathVariable("controlId") String controlId, @RequestParam(value = "level", defaultValue = "0") Integer level) {
        return controlService.getById(controlId, level);
    }

    @PostMapping(value = "upload", produces = "application/json")
    public ResponseEntity<Response> uploadCSV(@RequestParam("file") MultipartFile file) {
        return controlService.uploadCSV(file);
    }
}
