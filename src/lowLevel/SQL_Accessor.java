/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lowLevel;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alex
 */
public class SQL_Accessor {
 private String driverName;
    private String url;
    private String userName;
    private String password;
    private Connection conn;

    public SQL_Accessor() {
    }

    public SQL_Accessor(String driverName, String url, String userName, String password) {
        setDriverName(driverName);
        setUrl(url);
        setUserName(userName);
        setPassword(password);
    }

    public String getDriverName() {
        return driverName;
    }

    private void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void openDatabaseConnection()
            throws IllegalArgumentException, ClassNotFoundException, SQLException {
        try{
        Class.forName(driverName);       
        conn = DriverManager.getConnection(url, userName, password);
        System.out.println("Connection Is Open");
        } catch(ClassNotFoundException cnfe){
            cnfe.getLocalizedMessage();
        } catch (SQLException se){
            se.getLocalizedMessage();
        }
    }

    public void closeDatabaseConnection() throws SQLException {
        conn.close();
        System.out.println("Connection Is Closed");
    }

    public List getRecords(String table) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM " + table;
        List<Map<String, Object>> list = null;
        Map map = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData md = null;

        try {
            openDatabaseConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            list = new ArrayList();

            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    try {
                        map.put(md.getColumnName(i), rs.getObject(i));
                    } catch (NullPointerException npe) {

                    }
                }
                list.add(map);
            }

        } catch (SQLException | ClassNotFoundException se) {
            se.getLocalizedMessage();
        } finally {
            stmt.close();
            closeDatabaseConnection();
        }
        return list;
    }

    public void createRecord(String tableName, List<Object> columns, List<Object> values) throws ClassNotFoundException, SQLException {

        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + " (");
        try{
            openDatabaseConnection();
        for (Object col : columns) {
            sb.append(col).append(",");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES (");
        for (Object val : values) {
            sb.append("?,");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        PreparedStatement pStmt = conn.prepareStatement(sb.toString());

        for (int i = 0; i < values.size(); i++) {
            pStmt.setObject(i + 1, values.get(i));
        }

        pStmt.executeUpdate();
        }
        catch(SQLException se){
            se.getLocalizedMessage();
        } catch(ClassNotFoundException cnfe){
            cnfe.getLocalizedMessage();
        }finally{
        closeDatabaseConnection();
        }
    }

    public Map<String,Object> retrieveRecordById(Object tableName, 
            Object column, Object primaryKey) 
            throws ClassNotFoundException, SQLException {
        
        Map<String,Object> record = null;
        PreparedStatement pStmt = null;
        ResultSetMetaData metaData = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + tableName + " WHERE " + column + "=?";
        
            try{
                openDatabaseConnection();
                pStmt = conn.prepareStatement(query);
                pStmt.setObject(1, primaryKey);
                pStmt.executeQuery();
                rs = pStmt.executeQuery();
                metaData = rs.getMetaData();
                int colCount = metaData.getColumnCount();
                
                if(rs.next()) {
                    record = new HashMap<>();
                    for(int i=1; i < colCount; i++) {
                        record.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                }
                
            } catch(IllegalArgumentException | ClassNotFoundException | SQLException e){
                e.getLocalizedMessage();
            }finally{
                pStmt.close();
                closeDatabaseConnection();
            }
            
        return record;
    }

    public void updateRecord(Object tableName, List<Object> columns, 
            List<Object> values, Object primaryKeyColumn, Object primaryKey) throws ClassNotFoundException, SQLException{
        
        PreparedStatement pstmt = null;
        StringBuilder sb = new StringBuilder("UPDATE " + tableName + " SET ");
        
        try{
            openDatabaseConnection();
            
            for(Object col : columns){
                sb.append(col).append("=?, ");
            }

            sb =  sb.deleteCharAt(sb.length()-2); // deletes final extra white space (" ") and comma
            sb.append("WHERE ").append(primaryKeyColumn).append(" = ").append(primaryKey);
            
            pstmt = conn.prepareStatement(sb.toString());
            
            for(int i = 0; i < values.size(); i++){
                pstmt.setObject(i+1, values.get(i));
            }
            
            pstmt.executeUpdate();
            
        } catch (ClassNotFoundException | SQLException e) {
            e.getLocalizedMessage();
            System.out.println("Failed In 'upadteRecords' method");
        } finally{
            pstmt.close();
            closeDatabaseConnection();
        }
        
    }

    public void deleteRecords(Object tableName, Object primaryKeyColumn, List<Object> primaryKeys)
                        throws SQLException {
        
        PreparedStatement pstmt = null;
        String query = "DELETE FROM " + tableName + " WHERE " + primaryKeyColumn + " = ?"; 
        
        try{
            openDatabaseConnection();
        for(Object key : primaryKeys){
            pstmt = conn.prepareStatement(query);
            pstmt.setObject(1, key);
            pstmt.executeUpdate();
        }
        } catch (Exception se){
            se.getLocalizedMessage();
        } finally{
            pstmt.close();
            closeDatabaseConnection();
        }
    }
    
    public static void main(String[] args) throws Exception {
        SQL_Accessor accessor = new SQL_Accessor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/Client?zeroDateTimeBehavior=convertToNull",
                                "root","root");

        System.out.println(accessor.retrieveRecordById("actorDB.actor_table", "id", "1"));

    }
            
    
}
