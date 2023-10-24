package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.*;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.CategoryRepository;
import com.majesteye.dghelper.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marwen Sbihi
 */
@Service
public class CategoryService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> getAll(Integer level) {
        try {
            utils.checkDataLevel(level, 4);

            List<Category> categories = categoryRepository.findAll();

            for (Category category : categories) {
                utils.processCategoryDataLevel(category, level);
            }

            HashMap<String, List<Category>> data = new HashMap<>();
            data.put("categories", categories);

            return utils.handleResponse("All categories retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response> getById(String categoryId, Integer level) {
        try {
            utils.checkDataLevel(level, 4);

            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NoSuchElementException("Category: '" + categoryId + "' does not exist"));

            utils.processCategoryDataLevel(category, level);

            HashMap<String, Category> data = new HashMap<>();
            data.put("category", category);

            return utils.handleResponse("Category: '" + categoryId + "' retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Response> uploadCSV(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split("\t");
                jdbcTemplate.update("INSERT INTO categories(category_id, category) VALUES(?, ?)",
                        fields[0], fields[1]);
            }
            reader.close();

            return utils.handleResponse("Finished loading data", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
