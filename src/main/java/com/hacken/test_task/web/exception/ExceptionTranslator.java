package com.hacken.test_task.web.exception;

import com.hacken.test_task.dto.HttpErrorResponseDTO;
import com.hacken.test_task.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    @ResponseBody
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpErrorResponseDTO handleSystemException(SystemException e) {
        log.error("Handled exception: ", e);
        return new HttpErrorResponseDTO(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpErrorResponseDTO handleRuntimeException(RuntimeException e) {
        log.error("Handled exception: ", e);
        return new HttpErrorResponseDTO(e.getMessage());
    }

}
