package acbManagement.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

final class OperationServiceSupport {
    private OperationServiceSupport() {}

    static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    static ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    static ResponseStatusException forbidden(String message) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }

    static ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}
