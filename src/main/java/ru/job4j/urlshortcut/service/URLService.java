package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.domain.URL;
import ru.job4j.urlshortcut.dto.URLAddressDTO;
import ru.job4j.urlshortcut.dto.URLCodeDTO;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.repository.URLRepository;

import java.util.Optional;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class URLService {
    private final URLRepository urlRepository;
    private final SiteRepository siteRepository;
    private final ModelMapper modelMapper;

    public Optional<URL> findByAddress(String address) {
        return urlRepository.findByAddress(address);
    }

    @Transactional
    public URLCodeDTO save(URLAddressDTO urlAddressDTO) {
        URLCodeDTO urlCodeDTO;
        Optional<URL> urlDB = findByAddress(urlAddressDTO.getAddress());
        if (urlDB.isPresent()) {
            urlCodeDTO = modelMapper.map(urlDB, URLCodeDTO.class);
        } else {
            URL url = new URL();
            String code = RandomStringUtils.randomAlphanumeric(8);
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            Site site = siteRepository.findByLogin(login).orElseThrow(
                    () -> new IllegalArgumentException("This site did not found"));
            url.setAddress(urlAddressDTO.getAddress());
            url.setSite(site);
            url.setCode(code);
            urlRepository.save(url);
            urlCodeDTO = modelMapper.map(url, URLCodeDTO.class);
        }
        return urlCodeDTO;
    }

    public String redirect(String code) {
        Optional<URL> urlDB = urlRepository.findByCode(code);
        if (urlDB.isEmpty()) {
            throw new IllegalArgumentException("Code did not found");
        }
        return urlDB.get().getAddress();
    }
}
