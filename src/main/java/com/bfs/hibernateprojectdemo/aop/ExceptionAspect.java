package com.bfs.hibernateprojectdemo.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;
import com.bfs.hibernateprojectdemo.exception.InvalidTokenException;
import com.bfs.hibernateprojectdemo.exception.ValidationException;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class ExceptionAspect {

    
    @Pointcut("execution(* com.bfs.hibernateprojectdemo..*(..))")
    public void applicationPackagePointcut() {}

    
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "ex")
    public ResponseEntity<DataResponse> handleExceptions(Exception ex) {
        DataResponse response;

        if (ex instanceof ResourceNotFoundException) {
            response = DataResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (ex instanceof InvalidTokenException) {
            response = DataResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof ValidationException) {
            response = DataResponse.builder()
                    .success(false)
                    .message("Validation failed: " + ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof AccessDeniedException) {
            response = DataResponse.builder()
                    .success(false)
                    .message("Access denied: " + ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else {
            response = DataResponse.builder()
                    .success(false)
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
