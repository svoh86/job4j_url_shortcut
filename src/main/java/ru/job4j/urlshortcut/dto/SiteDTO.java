package ru.job4j.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDTO {
    private String login;
    private String password;
    private boolean registration;
}
