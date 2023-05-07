package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.dto.SiteDTO;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.Collections;
import java.util.List;

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

    /**
     * Метод пытается сохранить в БД сайт.
     * При удачном сохранении генерируется логин и пароль на основе ID сайта.
     *
     * @param site site
     * @return siteDTO
     */
    public SiteDTO save(Site site) {
        SiteDTO siteDTO;
        try {
            siteRepository.save(site);
            site.setRegistration(true);
            List<String> loginAndPassword = generateLoginAndPassword(site.getId());
            site.setLogin(loginAndPassword.get(0));
            site.setPassword(encoder.encode(loginAndPassword.get(1)));
            siteRepository.save(site);
            siteDTO = modelMapper.map(site, SiteDTO.class);
            siteDTO.setPassword(loginAndPassword.get(1));
        } catch (Exception e) {
            throw new IllegalArgumentException(("This site " + site.getDomain() + " is already registered!"));
        }
        return siteDTO;
    }

    private List<String> generateLoginAndPassword(int seed) {
        String login = DigestUtils.md5DigestAsHex(("login" + seed).getBytes()).substring(0, 10);
        String password = DigestUtils.md5DigestAsHex(("password" + seed).getBytes()).substring(0, 10);
        return List.of(login, password);
    }
}
