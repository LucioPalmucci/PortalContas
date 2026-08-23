package edu.usal.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static volatile SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    sessionFactory = construirSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    private static SessionFactory construirSessionFactory() {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .configure();

        aplicarSiExiste(builder, "hibernate.connection.url", "DB_URL");
        aplicarSiExiste(builder, "hibernate.connection.username", "DB_USER");
        aplicarSiExiste(builder, "hibernate.connection.password", "DB_PASSWORD");

        StandardServiceRegistry registry = builder.build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    private static void aplicarSiExiste(StandardServiceRegistryBuilder builder, String propiedad, String variableDeEntorno) {
        String valor = System.getenv(variableDeEntorno);
        if (valor != null && !valor.isBlank()) {
            builder.applySetting(propiedad, valor);
        }
    }

}
