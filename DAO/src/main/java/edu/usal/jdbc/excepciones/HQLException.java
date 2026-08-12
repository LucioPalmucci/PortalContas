package edu.usal.jdbc.excepciones;

import org.hibernate.HibernateException;

public class HQLException extends HibernateException {
    public HQLException(String message) {
        super(message);
    }
}
