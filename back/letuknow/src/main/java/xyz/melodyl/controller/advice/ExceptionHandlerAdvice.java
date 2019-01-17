package xyz.melodyl.controller.advice;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@RestController
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(value = {SQLException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void sqlException(Exception exception, HttpServletResponse response) {
        LOGGER.error(exception.getMessage());
    }

    @ExceptionHandler(value = {IOException.class, TooManyResultsException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void sqlSyntaxErrorException(Exception exception, HttpServletResponse response) {
        LOGGER.error(exception.getMessage());
    }
}
