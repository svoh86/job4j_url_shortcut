package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.URL;

import java.util.Optional;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
public interface URLRepository extends CrudRepository<URL, Integer> {
    Optional<URL> findByCode(String code);

    @Modifying
    @Query("UPDATE URL u SET u.count = (u.count + 1) WHERE u.code = ?1")
    void incrementCount(String code);
}
