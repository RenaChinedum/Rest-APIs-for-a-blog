package com.task10.task10.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<ExceptionResponse> alreadyExist(ResourceAlreadyException e, HttpServletRequest request){
        ExceptionResponse er = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.BAD_REQUEST.value())
                .errorTime(LocalDate.now())
                .build();
        return new ResponseEntity<>(er,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ExceptionResponse> notFound(ResourceNotFoundException e, HttpServletRequest request){
        ExceptionResponse er = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.NOT_FOUND.value())
                .errorTime(LocalDate.now())
                .build();
        return new ResponseEntity<>(er,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ExceptionResponse> unauthorised(UnauthorisedException e, HttpServletRequest request){
        ExceptionResponse er = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.UNAUTHORIZED.value())
                .errorTime(LocalDate.now())
                .build();
        return new ResponseEntity<>(er,HttpStatus.UNAUTHORIZED);
    }
}
