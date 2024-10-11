package com.herman.herman.controller;

import com.herman.herman.dto.FormRequest;
import com.herman.herman.service.PdfService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/pdf")
@RequiredArgsConstructor
public class PdfController {
  private final PdfService pdfService;

  @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Tag(name = "Add image into pdf.")
  public ResponseEntity<Resource> imageToPdf(@Valid @RequestPart("file") MultipartFile file) {
    final var res = this.pdfService.imageToPdf(file);
    return new ResponseEntity<>(new ByteArrayResource(res.getResource()), res.getResourceHeader(), HttpStatus.OK);
  }

  @PostMapping("/radio-btn")
  @Tag(name = "Create pdf with form.")
  public ResponseEntity<Resource> createPdfRadioBtn(@RequestBody FormRequest formRequest) {
    final var res = this.pdfService.generatePdf(formRequest);
    return new ResponseEntity<>(new ByteArrayResource(res.getResource()), res.getResourceHeader(), HttpStatus.OK);
  }
}
