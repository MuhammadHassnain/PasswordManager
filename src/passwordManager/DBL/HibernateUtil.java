/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.DBL;

import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author m-hassnain
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = null;
    private static ServiceRegistry serviceRegistry = null;

    public static synchronized SessionFactory getSessionFactory(String username, String password) {

        try {
            Configuration config = new Configuration().configure();
            config.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
            config.setProperty("hibernate.connection.username", username);
            config.setProperty("hibernate.connection.password", password);
            config.addAnnotatedClass(Account.class);
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            sessionFactory = config.buildSessionFactory(serviceRegistry);
        } catch (JDBCConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("User name or password is incorrect");
            alert.show();
            return null;
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            return null;
        }
        return sessionFactory;
    }
}
