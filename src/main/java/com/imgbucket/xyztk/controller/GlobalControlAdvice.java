package com.imgbucket.xyztk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@Slf4j
@ControllerAdvice
@Component
public class GlobalControlAdvice {
    private final MessageSource messageSource;

    public GlobalControlAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {
        model.addAttribute("error", messageSource.getMessage("error.internalDB",null, Locale.ENGLISH));
        model.addAttribute("message", messageSource.getMessage("error.exception",null, Locale.ENGLISH));
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(messageSource.getMessage("log.error",null, Locale.ENGLISH) + e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        model.addAttribute("error", messageSource.getMessage("error.internal",null, Locale.ENGLISH));
        model.addAttribute("message", messageSource.getMessage("error.exception",null, Locale.ENGLISH));
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(messageSource.getMessage("log.error",null, Locale.ENGLISH) + e.getMessage());
        return "error";
    }
}
