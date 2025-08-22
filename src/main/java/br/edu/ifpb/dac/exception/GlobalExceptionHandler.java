package br.edu.ifpb.dac.exception;

import br.edu.ifpb.dac.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProductPersistenceException.class)
    public ResponseEntity<ApiError> handleProductPersistence(ProductPersistenceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(LastAdminDeletionException.class)
    public ResponseEntity<ApiError> handleLastAdminDeletion(LastAdminDeletionException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UnauthorizedUserEditException.class)
    public ResponseEntity<ApiError> handleUnauthorizedUserEdit(UnauthorizedUserEditException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AdvertisementNotFoundException.class)
    public ResponseEntity<ApiError> handleAdvertisementNotFound(AdvertisementNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AdvertisementPersistenceException.class)
    public ResponseEntity<ApiError> handleAdvertisementPersistence(AdvertisementPersistenceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UnauthorizedAdvertisementEditException.class)
    public ResponseEntity<ApiError> handleUnauthorizedAdvertisementEdit(UnauthorizedAdvertisementEditException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(InvalidAdvertisementException.class)
    public ResponseEntity<ApiError> handleInvalidAdvertisement(InvalidAdvertisementException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ProductHasAdvertisementException.class)
    public ResponseEntity<ApiError> handleProductHasAdvertisement(ProductHasAdvertisementException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CategoryPersistenceException.class)
    public ResponseEntity<ApiError> handleCategoryPersistence(CategoryPersistenceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(RuleNotFoundException.class)
    public ResponseEntity<ApiError> handleRuleNotFound(RuleNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RulePersistenceException.class)
    public ResponseEntity<ApiError> handleRulePersistence(RulePersistenceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedRuleEditException.class)
    public ResponseEntity<ApiError> handleUnauthorizedRuleEdit(UnauthorizedRuleEditException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    private ResponseEntity<ApiError> buildErrorResponse(RuntimeException ex, HttpStatus status, HttpServletRequest request) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}