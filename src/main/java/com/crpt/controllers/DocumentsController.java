package com.crpt.controllers;

import com.crpt.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/documents")
@RestController
@Slf4j
public class DocumentsController {
    @PostMapping(
            value = "/validation",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> validateDocument(@Valid @RequestBody Document document){
        log.info("Received document: " + document);
        return ResponseEntity.ok().build();
    }
}
