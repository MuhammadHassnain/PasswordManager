/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.DBL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author m-hassnain
 */
public class AccountManager {

    private DatabaseConnectionParameter connectionParameter;

    public AccountManager() {

    }

    public void setConnectionParameter(DatabaseConnectionParameter connectionParameter) {
        this.connectionParameter = connectionParameter;
    }

    public DatabaseConnectionParameter getConnectionParameter() {
        return connectionParameter;
    }

    public Integer addAccount(Account account) {
        SessionFactory factory = HibernateUtil.getSessionFactory(connectionParameter.getDbAdmin(), connectionParameter.getDbPass());
        Session session = factory.openSession();
        Integer id = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            id = (Integer) session.save(account);
            transaction.commit();
        } catch (ConstraintViolationException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return -1;
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return -2;
        } finally {
            factory.close();
        }
        return id;
    }

    public Integer deleteAccount(Account account) {
        SessionFactory factory = HibernateUtil.getSessionFactory(connectionParameter.getDbAdmin(), connectionParameter.getDbPass());
        Session session = factory.openSession();
        Integer id = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(account);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            session.close();
            factory.close();
            return 0;
        } finally {
            session.close();
            factory.close();
        }
        return 1;
    }

    public Integer updateAccount(Account account) {
        SessionFactory factory = HibernateUtil.getSessionFactory(connectionParameter.getDbAdmin(), connectionParameter.getDbPass());
        Session session = factory.openSession();
        Integer id = 0;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Account accountOld = (Account) session.get(Account.class, account.getId());
            accountOld.setUrl(account.getUrl());
            accountOld.setPassword(account.getPassword());
            accountOld.setUserName(account.getUserName());
            session.update(accountOld);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            session.close();
            factory.close();
            return 0;
        } finally {
            session.close();
            factory.close();
        }
        return 1;
    }

    public List<Account> getAllAccount() {
        SessionFactory factory = HibernateUtil.getSessionFactory(connectionParameter.getDbAdmin(), connectionParameter.getDbPass());
        Session session = factory.openSession();

        String hql = "Select a FROM Account a";
        Query query = session.createQuery(hql);

        List<Account> result = query.list();
        System.out.println("passwordManager.DBL.AccountManager.getAllAccount()" + result.toString());
        session.close();
        factory.close();
        return result;
    }

    public int changeDatabasePassword(String oldPass, String newPass) {
        /**
         * -1 if oldPass didn't match current password 1 for successfully change
         * password
         */
        if (!oldPass.equals(connectionParameter.getDbPass())) {
            return -1;
        } else {
            SessionFactory factory = HibernateUtil.getSessionFactory(connectionParameter.getDbAdmin(), connectionParameter.getDbPass());
            Session session = factory.openSession();
            String hbl = "alter user admin set password '" + newPass + "'";
            SQLQuery query = session.createSQLQuery(hbl);
            query.executeUpdate();
            connectionParameter.setDBPass(newPass);
            session.close();
            factory.close();
            return 1;
        }
    }
}
