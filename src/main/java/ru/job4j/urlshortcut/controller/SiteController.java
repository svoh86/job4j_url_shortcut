package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.Optional;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@RestController
@AllArgsConstructor
public class SiteController {
    private final SiteService siteService;
    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public ResponseEntity<SiteDTO> registration(@RequestBody @Valid SiteDomainDTO siteDomainDTO) {
        Optional<Site> siteDb = siteService.findByDomain(siteDomainDTO.getDomain());
        if (siteDb.isPresent()) {
            throw new IllegalArgumentException("This site " + siteDomainDTO.getDomain() + " is already registered!");
        }

        Site site = new Site();
        site.setDomain(siteDomainDTO.getDomain());
        siteService.save(site);
        return new ResponseEntity<>(
                convertToSiteDTO(site),
                HttpStatus.OK
        );
    }

    private SiteDTO convertToSiteDTO(Site site) {
        return modelMapper.map(site, SiteDTO.class);
    }
}
