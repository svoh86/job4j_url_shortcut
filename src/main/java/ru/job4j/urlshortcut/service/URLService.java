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
import ru.job4j.urlshortcut.dto.URLStatisticDTO;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.repository.URLRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public URLCodeDTO save(URLAddressDTO urlAddressDTO) {
        URL url = new URL();
        String code = RandomStringUtils.randomAlphanumeric(8);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Site site = siteRepository.findByLogin(login).orElseThrow(
                () -> new IllegalArgumentException("This site did not found"));
        url.setAddress(urlAddressDTO.getAddress());
        url.setSite(site);
        url.setCode(code);
        try {
            urlRepository.save(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("This url " + url.getAddress() + " is already registered!");
        }
        return modelMapper.map(url, URLCodeDTO.class);
    }

    @Transactional
    public String redirect(String code) {
        Optional<URL> urlDB = urlRepository.findByCode(code);
        if (urlDB.isEmpty()) {
            throw new IllegalArgumentException("Code did not found");
        }
        urlRepository.incrementCount(code);
        return urlDB.get().getAddress();
    }

    public List<URLStatisticDTO> statistic() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Site site = siteRepository.findByLogin(login).orElseThrow(
                () -> new IllegalArgumentException("This site did not found"));
        List<URL> urls = site.getUrls();
        return urls.stream().
                map(u -> modelMapper.map(u, URLStatisticDTO.class))
                .collect(Collectors.toList());
    }
}
