package edu.usal.jdbc.excepciones;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
