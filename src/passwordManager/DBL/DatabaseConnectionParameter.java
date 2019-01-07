/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.DBL;

import java.sql.Connection;

/**
 *
 * @author m-hassnain
 */
public class DatabaseConnectionParameter {
    
    private static DatabaseConnectionParameter connectionParameter = null;
    private String dbAdmin;
    private String dbPass;
    private DatabaseConnectionParameter(String dbAdmin,String dbPass) {
        this.dbAdmin = dbAdmin;
        this.dbPass = dbPass;
    }

    public static DatabaseConnectionParameter getConnection(String dbAdmin,String dbPass) {
        if(DatabaseConnectionParameter.connectionParameter==null){
            connectionParameter = new DatabaseConnectionParameter(dbAdmin, dbPass);
        }
        return connectionParameter;
    }
    public static void removeConnection(){
        if(DatabaseConnectionParameter.connectionParameter!=null){
            DatabaseConnectionParameter.connectionParameter = null;
        }
    }

    public String getDbAdmin() {
        return dbAdmin;
    }

    public String getDbPass() {
        return dbPass;
    }
    
    public void setDBPass(String pass){
        this.dbPass = pass;
    }
    
}
