package ru.job4j.urlshortcut.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());
    private final ObjectMapper objectMapper;

    @ExceptionHandler(IllegalArgumentException.class)
    public void handler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", "Check the entered data");
            put("details", e.getMessage());
        }}));
        LOGGER.error(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handler(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
