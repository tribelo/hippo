package uk.nhs.digital.apispecs;

import org.springframework.http.HttpStatus;

public class ApigeeServiceException extends RuntimeException {

    private final HttpStatus code;

    public ApigeeServiceException(HttpStatus code) {
        super();
        this.code = code;
    }

    public ApigeeServiceException(String message, Throwable cause, HttpStatus code) {
        super(message, cause);
        this.code = code;
    }

    public ApigeeServiceException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public ApigeeServiceException(Throwable cause, HttpStatus code) {
        super(cause);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
