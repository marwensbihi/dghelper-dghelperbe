package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.Information;
import com.majesteye.dghelper.models.Paragraph;
import com.majesteye.dghelper.models.Specification;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.InformationRepository;
import com.majesteye.dghelper.repository.ParagraphRepository;
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

import javax.transaction.Transactional;

import static java.time.LocalDateTime.now;

/**
 * @author Marwen sbihi
 */

@Service
public class InformationService {
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  ParagraphRepository paragraphRepository;

  @Autowired
  SpecificationRepository specificationRepository;
  @Autowired
  InformationRepository informationRepository;

  @Autowired
  Utils utils;


  public ResponseEntity<Response> getAll() {
    try {
      List<Information> informationList = informationRepository.findAll();

      for (Information information : informationList) {
        information.setSpecification(null);
        for (Paragraph paragraph : information.getParagraphs()) {
          paragraph.setInformation(null);
        }
      }

      HashMap<String, List<Information>> data = new HashMap<>();
      data.put("information", informationList);

      return utils.handleResponse("All Information retrieved successfully", HttpStatus.OK, data);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Response> getById(Long informationId) {
    try {
      Information information = informationRepository.findById(informationId).orElseThrow(
        () -> new NoSuchElementException("Information: '" + informationId + "' does not exist"));

      information.setSpecification(null);

      for (Paragraph paragraph : information.getParagraphs()) {
        paragraph.setInformation(null);
      }

      HashMap<String, Information> data = new HashMap<>();
      data.put("information", information);

      return utils.handleResponse("Information: '" + informationId + "' retrieved successfully", HttpStatus.OK,
        data);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Response> save(Information information) {
    try {
      Specification specification = specificationRepository.findById(information.getSpecificationId()).orElseThrow(
        () -> new NoSuchElementException("Specification: '" + information.getSpecificationId() + "' does not exist"));
      information.setCreatedAt(now());
      information.setSpecification(specification);
      specification.getInformations().add(information);

      List<Paragraph> paragraphs = information.getParagraphs();

      for (Paragraph paragraph : paragraphs) {
        paragraph.setInformation(information);
        paragraph.setUpdatedAt(now());
      }

      //paragraphRepository.saveAll(paragraphs);
      informationRepository.save(information);
      specificationRepository.save(specification);


      HashMap<String, Boolean> data = new HashMap<>();
      data.put("information", true);

      return utils.handleResponse("Information created successfully", HttpStatus.OK, data);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.BAD_REQUEST);
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
        jdbcTemplate.update(
          "INSERT INTO informations(information_id, information, specification_id) VALUES(?, ?, ?)",
          fields[0], fields[1], fields[2]);
      }
      reader.close();

      return utils.handleResponse("Finished loading data", HttpStatus.OK, null);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.BAD_REQUEST);
    }
  }



  public ResponseEntity<Response> deleteByID(Long informationId) {
    try {
      Information information = informationRepository.findById(informationId).orElseThrow(
        () -> new NoSuchElementException("Information: '" + informationId + "' does not exist"));

      informationRepository.deleteById(informationId);
      String message = "Information:  deleted successfully";

      return utils.handleResponse(message, HttpStatus.OK, null);
    } catch (Exception e) {

      return utils.handleException(e, HttpStatus.NOT_FOUND);
    }
  }
/*
  @Transactional
  public ResponseEntity<Response> updateParagraph(Long informationId, Information updatedInformation) {
    try {
      Information information = informationRepository.findById(informationId)
        .orElseThrow(() -> new NoSuchElementException("Information: " + informationId + " does not exist"));

      if (updatedInformation.getInformation() != null && !updatedInformation.getInformation().isEmpty()) {
        information.setInformation(updatedInformation.getInformation());
        information.setUpdatedAt(now());
      }

      return utils.handleResponse("Information updated successfully", HttpStatus.OK, null);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.NOT_FOUND);
    }
  }
*/
@Transactional
public ResponseEntity<Response> updateParagraph(Long id, Paragraph updatedInformation) {
  try {
    Paragraph paragraph = paragraphRepository.findById(id)
      .orElseThrow(() -> new NoSuchElementException("Information: " + id + " does not exist"));

    if (updatedInformation.getParagraph() != null && !updatedInformation.getParagraph().isEmpty()) {
      paragraph.setParagraph(updatedInformation.getParagraph());
      paragraph.setUpdatedAt(now());
    }

    return utils.handleResponse("Information updated successfully", HttpStatus.OK, null);
  } catch (Exception e) {
    return utils.handleException(e, HttpStatus.NOT_FOUND);
  }
}
  // ? ------------- PARAGRAPHS -------------

  public ResponseEntity<Response> getAllParagraphs() {
    try {
      List<Paragraph> paragraphs = paragraphRepository.findAll();

      for (Paragraph paragraph : paragraphs) {
        paragraph.setInformation(null);
      }

      HashMap<String, List<Paragraph>> data = new HashMap<>();
      data.put("paragraphs", paragraphs);

      return utils.handleResponse("All paragraphs retrieved successfully", HttpStatus.OK, data);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
/*
  @Transactional
  public ResponseEntity<Response> updateParagraph(Long  informationId, String updatedInformation) {

    try {

      Information information = informationRepository.findById(informationId)
        .orElseThrow(() -> new NoSuchElementException("Paragraph: " + informationId + " does not exist"));

      information.setInformation(updatedInformation);
      information.setUpdatedAt(now());

      information.setSpecification(null);
      information.setParagraphs(null);

      informationRepository.save(information);

      HashMap<String, Object> data = new HashMap<>();
      data.put("information", information);


      return utils.handleResponse("Parahraph updated successfully", HttpStatus.OK, data);
    } catch (NoSuchElementException e) {
      return utils.handleException(e, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.BAD_REQUEST);
    }
  }
  */

/*
  public ResponseEntity<Response> deleteParagraph(String paragraphId) {
    try {
      if (!paragraphRepository.existsById(paragraphId)) {
        throw new NoSuchElementException("Paragraph: " + paragraphId + " does not exist");
      }
      paragraphRepository.deleteById(paragraphId);

      return utils.handleResponse("Paragraph deleted successfully", HttpStatus.OK, null);
    } catch (NoSuchElementException e) {
      return utils.handleException(e, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
*/

}
