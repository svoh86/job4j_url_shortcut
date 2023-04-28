package ru.job4j.urlshortcut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "site")
public class Site {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Domain cannot be empty")
    private String domain;
    private String login;
    private String password;
    private boolean registration;
    @OneToMany(mappedBy = "site")
    private List<URL> urls = new ArrayList<>();
}
