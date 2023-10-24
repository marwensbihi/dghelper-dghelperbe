package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.Control;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.ControlRepository;
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
public class ControlService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ControlRepository controlRepository;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> getAll(Integer level) {
        try {
            utils.checkDataLevel(level, 2);

            List<Control> controls = controlRepository.findAll();

            for (Control control : controls) {
                utils.processControlDataLevel(control, level + 2);
            }

            HashMap<String, List<Control>> data = new HashMap<>();
            data.put("controls", controls);

            return utils.handleResponse("All controls retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response> getById(String controlId, Integer level) {
        try {
            utils.checkDataLevel(level, 2);

            Control control = controlRepository.findById(controlId).orElseThrow(() ->
                    new NoSuchElementException("Control: '" + controlId + "' does not exist"));

            utils.processControlDataLevel(control, level + 2);

            HashMap<String, Control> data = new HashMap<>();
            data.put("control", control);

            return utils.handleResponse("Control: '" + controlId + "' retrieved successfully", HttpStatus.OK, data);
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
                if(skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split("\t");
                jdbcTemplate.update("INSERT INTO controls(control_id, control, dimension, dependency, compliance_percentage, domain_id) VALUES(?, ?, ?, ?, ?, ?)"
                        , fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
            }
            reader.close();

            return utils.handleResponse("Finished loading data", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
