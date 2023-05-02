package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.Site;

import java.util.Optional;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
public interface SiteRepository extends CrudRepository<Site, Integer> {
    Optional<Site> findByLogin(String username);
}
