package com.mediasoft.warehouse.error;

import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.exception.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений.
 */
@ControllerAdvice(annotations = RestController.class)
public class AdviceController {

    /**
     * Обработчик {@link ProductNotFoundException}.
     *
     * @param e исключение
     * @return response entity
     */
    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик {@link MethodArgumentNotValidException}.
     * Обрабатывает все ошибки валидации.
     * @param e исключение
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
        final ValidationException validationException = new ValidationException(
                e.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toSet()));
        return new ResponseEntity<>(validationException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик неизвестных исключений.
     * @param e исключение
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

