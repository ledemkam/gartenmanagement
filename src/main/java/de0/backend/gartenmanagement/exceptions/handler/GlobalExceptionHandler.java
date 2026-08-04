package de0.backend.gartenmanagement.exceptions.handler;

import de0.backend.gartenmanagement.dtos.ErrorDto;
import de0.backend.gartenmanagement.exceptions.DuplicateProductException;
import de0.backend.gartenmanagement.exceptions.InsufficientStockException;
import de0.backend.gartenmanagement.exceptions.InvalidPriceException;
import de0.backend.gartenmanagement.exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {





    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handlePostNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request) {
        ErrorDto error = new ErrorDto(
                ex.getMessage(),
                request.getRequestURI(),   // → path
                404,                       // → status
                LocalDateTime.now()        // → timestamp
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorDto> handleDuplicateProduct(
            DuplicateProductException ex,
            HttpServletRequest request) {
        ErrorDto error = new ErrorDto(
                ex.getMessage(),
                request.getRequestURI(),
                409,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorDto> handleInsufficientStock(
            InsufficientStockException ex,
            HttpServletRequest request) {
        ErrorDto error = new ErrorDto(
                ex.getMessage(),
                request.getRequestURI(),
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<ErrorDto> handleInvalidPrice(
            InvalidPriceException ex,
            HttpServletRequest request) {
        ErrorDto error = new ErrorDto(
                ex.getMessage(),
                request.getRequestURI(),
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        log.error("Caught MethodArgumentNotValidException", ex);
        ErrorDto errorDto = new ErrorDto();

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation error occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        log.error("Caught ConstraintViolationException", ex);
        ErrorDto errorDto = new ErrorDto();

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + ": " + violation.getMessage()
                ).orElse("Constraint violation occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        log.debug("No static resource found: {}", request.getRequestURI());
        ErrorDto error = new ErrorDto(
                "Resource not found: " + ex.getResourcePath(),
                request.getRequestURI(),
                404,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex, HttpServletRequest request) {
        log.error("Caught exception", ex);
        ErrorDto errorDto = new ErrorDto(
                "An unknown error occurred",
                request.getRequestURI(),
                500,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
