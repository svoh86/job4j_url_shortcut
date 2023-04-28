package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.URL;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
public interface URLRepository extends CrudRepository<URL, Integer> {
}