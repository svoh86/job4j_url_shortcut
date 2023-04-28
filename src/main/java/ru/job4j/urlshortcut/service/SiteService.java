package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.Optional;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SiteService {
    private final SiteRepository siteRepository;
//    private final BCryptPasswordEncoder encoder;

    public Optional<Site> findByDomain(String domain) {
        return siteRepository.findByDomain(domain);
    }

    @Transactional
    public Site save(Site site) {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphanumeric(10);
        site.setLogin(login);
        site.setPassword(password);
//        site.setPassword(encoder.encode(password)); // TODO add security
        site.setRegistration(true);
        return siteRepository.save(site);
    }

}
