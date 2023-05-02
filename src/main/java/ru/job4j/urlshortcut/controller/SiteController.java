package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.dto.SiteDTO;
import ru.job4j.urlshortcut.dto.SiteDomainDTO;
import ru.job4j.urlshortcut.service.SiteService;

import javax.validation.Valid;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@RestController
@AllArgsConstructor
public class SiteController {
    private final SiteService siteService;

    @PostMapping("/registration")
    public ResponseEntity<SiteDTO> registration(@RequestBody @Valid SiteDomainDTO siteDomainDTO) {
        Site site = new Site();
        site.setDomain(siteDomainDTO.getDomain());
        return new ResponseEntity<>(
                siteService.save(site),
                HttpStatus.OK
        );
    }
}
