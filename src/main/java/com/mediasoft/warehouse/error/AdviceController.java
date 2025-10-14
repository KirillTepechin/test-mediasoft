package com.mediasoft.warehouse.error;

import com.mediasoft.warehouse.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    @ExceptionHandler({ProductNotFoundException.class, CustomerNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleNotFoundException(Throwable e) {
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                List.of(e.getMessage()), LocalDateTime.now(), className);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(ArticleAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleArticleAlreadyExistsException(Throwable e) {
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                List.of(e.getMessage()), LocalDateTime.now(), className);
        return ResponseEntity.badRequest().body(errorDetails);
    }
    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleOrderAccessDeniedException(Throwable e) {
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                List.of(e.getMessage()), LocalDateTime.now(), className);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(OrderUnsupportedStatusException.class)
    public ResponseEntity<ErrorDetails> handleOrderUnsupportedStatusException(Throwable e) {
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                List.of(e.getMessage()), LocalDateTime.now(), className);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    /**
     * Обработчик {@link MethodArgumentNotValidException}.
     * Обрабатывает все ошибки валидации.
     * @param e исключение
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException e) {
        final List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(fm->fm.getField() + ": " + fm.getDefaultMessage()).collect(Collectors.toList());
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                errorMessages, LocalDateTime.now(), className);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(HandlerMethodValidationException e) {
        final List<String> errorMessages = e.getAllErrors().stream()
                .map(fm->((FieldError)fm).getField() + ": " + fm.getDefaultMessage()).collect(Collectors.toList());
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                errorMessages, LocalDateTime.now(), className);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    /**
     * Обработчик неизвестных исключений.
     * @param e исключение
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleUnknownException(Throwable e) {
        final String className = Arrays.stream(e.getStackTrace()).findFirst().orElseThrow().getClassName();
        final ErrorDetails errorDetails = new ErrorDetails(e.getClass().getSimpleName(),
                List.of(e.getMessage()), LocalDateTime.now(), className);
        return ResponseEntity.internalServerError().body(errorDetails);
    }
}

