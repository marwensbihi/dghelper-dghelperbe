package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Marwen Sbihi
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> getAll(@RequestParam(value = "level", defaultValue = "0") Integer level) {
        return categoryService.getAll(level);
    }

    @GetMapping(value = "{categoryId}", produces = "application/json")
    public ResponseEntity<Response> getById(@PathVariable("categoryId") String domainId, @RequestParam(value = "level", defaultValue = "0") Integer level) {
        return categoryService.getById(domainId, level);
    }

    @PostMapping(value = "upload", produces = "application/json")
    public ResponseEntity<Response> uploadCSV(@RequestParam("file") MultipartFile file) {
        return categoryService.uploadCSV(file);
    }
}
