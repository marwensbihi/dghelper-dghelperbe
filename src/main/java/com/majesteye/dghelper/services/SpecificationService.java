package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.Specification;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.SpecificationRepository;
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
public class SpecificationService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SpecificationRepository specificationRepository;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> getAll(Integer level) {
        try {
            utils.checkDataLevel(level, 1);

            List<Specification> specifications = specificationRepository.findAll();

            for (Specification specification : specifications) {
                utils.processSpecificationDataLevel(specification, level + 3);
            }

            HashMap<String, List<Specification>> data = new HashMap<>();
            data.put("specifications", specifications);

            return utils.handleResponse("All specifications retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Response> getById(String specificationId, Integer level) {
        try {
            utils.checkDataLevel(level, 1);

            Specification specification = specificationRepository.findById(specificationId).orElseThrow(() ->
                    new NoSuchElementException("Specification: '" + specificationId + "' does not exist"));

            utils.processSpecificationDataLevel(specification, level + 3);

            HashMap<String, Specification> data = new HashMap<>();
            data.put("specification", specification);

            return utils.handleResponse("Specification: '" + specificationId + "' retrieved successfully", HttpStatus.OK, data);
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
                jdbcTemplate.update("INSERT INTO specifications(specification_id, specification, priority, compliance_percentage, control_id) VALUES(?, ?, ?, ?, ?)"
                        , fields[0], fields[1], fields[2], fields[3], fields[4]);
            }
            reader.close();

            return utils.handleResponse("Finished loading data", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
