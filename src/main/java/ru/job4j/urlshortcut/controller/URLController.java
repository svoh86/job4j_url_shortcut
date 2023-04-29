package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.dto.URLAddressDTO;
import ru.job4j.urlshortcut.dto.URLCodeDTO;
import ru.job4j.urlshortcut.service.URLService;

import javax.validation.Valid;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@RestController
@AllArgsConstructor
public class URLController {
    private final URLService urlService;

    @PostMapping("/convert")
    public ResponseEntity<URLCodeDTO> convert(@RequestBody @Valid URLAddressDTO urlAddressDTO) {
        return new ResponseEntity<>(
                urlService.save(urlAddressDTO),
                HttpStatus.OK
        );
    }
}
