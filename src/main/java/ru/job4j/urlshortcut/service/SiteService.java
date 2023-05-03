package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.dto.SiteDTO;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.Collections;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SiteService implements UserDetailsService {
    private final SiteRepository siteRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Site site = siteRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("Site with username: " + username + " not found!"));
        return new User(site.getLogin(), site.getPassword(), Collections.emptyList());
    }

    public SiteDTO save(Site site) {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphanumeric(10);
        site.setLogin(login);
        site.setPassword(encoder.encode(password));
        site.setRegistration(true);
        try {
            siteRepository.save(site);
        } catch (Exception e) {
            throw new IllegalArgumentException(("This site " + site.getDomain() + " is already registered!"));
        }
        SiteDTO siteDTO = modelMapper.map(site, SiteDTO.class);
        siteDTO.setPassword(password);
        return siteDTO;
    }
}
