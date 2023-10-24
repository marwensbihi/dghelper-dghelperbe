package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.Information;
import com.majesteye.dghelper.models.Paragraph;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Marwen Sbihi
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/information")
public class InformationController {
  @Autowired
  InformationService informationService;
  private final InformationService paragraphService;

  @Autowired
  public InformationController(InformationService paragraphService) {
    this.paragraphService = paragraphService;
  }
  @GetMapping(produces = "application/json")
  public ResponseEntity<Response> getAll(@RequestParam(value = "level", defaultValue = "0") Integer level) {
    return informationService.getAll();
  }

  @GetMapping(value = "{informationId}", produces = "application/json")
  public ResponseEntity<Response> getById(@PathVariable("informationId") Long informationId) {
    return informationService.getById(informationId);
  }

  @PostMapping(value = "create", produces = "application/json")
  public ResponseEntity<Response> createInformation(@RequestBody Information information) {
    return informationService.save(information);
  }

  @PutMapping(value = "{paragraphId}/update", produces = "application/json")
  public ResponseEntity<Response> updateParagraph(@PathVariable Long paragraphId, @RequestBody Paragraph paragraph) {
    return informationService.updateParagraph(paragraphId, paragraph);
  }

/*
  @PostMapping(value = "update/{informationId}", produces = "application/json")
  public ResponseEntity<Response> updateParagraph(@PathVariable("informationId") Long informationId, @RequestBody String paragraph) {
    return informationService.updateParagraph(informationId, paragraph);
  }
*/
  @DeleteMapping("/{informationId}")
  public ResponseEntity<Response> deleteInformation(@PathVariable Long informationId) {
    return informationService.deleteByID(informationId);
  }

}
