package ru.job4j.urlshortcut.domain;

import lombok.*;

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

    @Override
    public String toString() {
        return "Site{" + "id=" + id + ", domain='" + domain + '\''
                + ", login='" + login + '\'' + ", password='" + password + '\''
                + ", registration=" + registration + ", urls=" + urls + '}';
    }
}
