package ru.job4j.urlshortcut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "url")
public class URL {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;
    private String code;
    private long count;

}
