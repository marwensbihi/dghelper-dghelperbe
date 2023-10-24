package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.*;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.DomainRepository;
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
public class DomainService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> getAll(Integer level) {
        try {
            utils.checkDataLevel(level, 3);

            List<Domain> domains = domainRepository.findAll();

            for (Domain domain : domains) {
                utils.processDomainDataLevel(domain, level + 1);
            }

            HashMap<String, List<Domain>> data = new HashMap<>();
            data.put("domains", domains);

            return utils.handleResponse("All domains retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response> getById(String domainId, Integer level) {
        try {
            utils.checkDataLevel(level, 3);

            Domain domain = domainRepository.findById(domainId).orElseThrow(() ->
                    new NoSuchElementException("Domain: '" + domainId + "' does not exist"));

            utils.processDomainDataLevel(domain, level + 1);

            HashMap<String, Domain> data = new HashMap<>();
            data.put("domain", domain);

            return utils.handleResponse("Domain: '" + domainId + "' retrieved successfully", HttpStatus.OK, data);
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
                jdbcTemplate.update("INSERT INTO domains(domain_id, domain, compliance_percentage, category_id) VALUES(?, ?, ?, ?)",
                        fields[0], fields[1], fields[2], fields[3]);
            }
            reader.close();

            return utils.handleResponse("Finished loading data", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
