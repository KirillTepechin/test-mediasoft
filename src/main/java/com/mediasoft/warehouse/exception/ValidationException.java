package com.mediasoft.warehouse.exception;

import java.util.Set;

/**
 * Исключение, которое выбрасывается, когда валидация данных не прошла успешно
 */
public class ValidationException extends RuntimeException {
    /**
     * @param errors Множество строк с сообщениями об ошибках валидации.
     */
    public <T> ValidationException(Set<String> errors) {
        super(String.join("\n", errors));
    }

}

