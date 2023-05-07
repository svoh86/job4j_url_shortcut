package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.domain.URL;
import ru.job4j.urlshortcut.dto.URLAddressDTO;
import ru.job4j.urlshortcut.dto.URLCodeDTO;
import ru.job4j.urlshortcut.dto.URLStatisticDTO;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.repository.URLRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class URLService {
    private final URLRepository urlRepository;
    private final SiteRepository siteRepository;
    private final ModelMapper modelMapper;

    public URLCodeDTO save(URLAddressDTO urlAddressDTO) {
        URL url = new URL();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Site site = siteRepository.findByLogin(login).orElseThrow(
                () -> new NoSuchElementException("This site did not found"));
        url.setAddress(urlAddressDTO.getAddress());
        url.setSite(site);
        try {
            urlRepository.save(url);
            String code = generateCode(url.getId());
            url.setCode(code);
            urlRepository.save(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("This url " + url.getAddress() + " is already registered!");
        }
        return modelMapper.map(url, URLCodeDTO.class);
    }

    private String generateCode(int seed) {
        return DigestUtils.md5DigestAsHex(("code" + seed).getBytes()).substring(0, 10);
    }

    @Transactional
    public String redirect(String code) {
        Optional<URL> urlDB = urlRepository.findByCode(code);
        if (urlDB.isEmpty()) {
            throw new NoSuchElementException("Code did not found");
        }
        urlRepository.incrementCount(code);
        return urlDB.get().getAddress();
    }

    public List<URLStatisticDTO> statistic() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Site site = siteRepository.findByLogin(login).orElseThrow(
                () -> new NoSuchElementException("This site did not found"));
        List<URL> urls = site.getUrls();
        return urls.stream().
                map(u -> modelMapper.map(u, URLStatisticDTO.class))
                .collect(Collectors.toList());
    }
}
