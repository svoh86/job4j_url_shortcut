package ru.job4j.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDomainDTO {
    @NotBlank(message = "Domain cannot be empty")
    private String domain;
}
