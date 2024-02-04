/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.coffeedbapp;

import java.sql.*;
import java.util.Properties;
import javax.swing.*;

/**
 *
 * @author norly
 */
public class CoffeeApp {
    
    //static final String driver = "org.apache.derby.jdbc.ClientDriver";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(int x=0;x<6;x++){
            final String query1 = "INSERT INTO TN3223.COFFEE VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP,CURRENT_TIME)";
            final String query2 = "SELECT * FROM TN3223.COFFEE";
        Connection theConnection = null;    // TODO code application logic here
        PreparedStatement theStatement1 = null;
        ResultSet theResult = null;
       	Statement theStatement2 = null;

        String name = JOptionPane.showInputDialog("Brand name : ");
        String sup_id = JOptionPane.showInputDialog("Supplier id : ");
	String price = JOptionPane.showInputDialog("price/kg : ");
        String sales = JOptionPane.showInputDialog("sales : ");
	//String total = JOptionPane.showInputDialog("total : ");
        String dbURL = "jdbc:derby://localhost:1527/TN3223";
        Properties properties = new Properties();
        properties.put("create", "true");
        properties.put("user", "TN3223");
        properties.put("password", "abc1234");
 
        try {
            //Class.forName(driver); No longer required 
            
            theConnection = DriverManager.getConnection(dbURL, properties);
            theStatement1 = theConnection.prepareStatement(query1);
    //Set parameters for INSERT statement and execute it
            theStatement1.setString(1, name);
            theStatement1.setString(2, sup_id);
            theStatement1.setDouble(3, Double.parseDouble(price));
            theStatement1.setInt(4, Integer.parseInt(sales));
            double totalSales = Double.parseDouble(price)*Integer.parseInt(sales);
            theStatement1.setDouble(5, totalSales);
            theStatement1.executeUpdate();

    //Get result
            theStatement2 = theConnection.createStatement();
            theResult = theStatement2.executeQuery(query2);
            while(theResult.next()) { //Fetch all the records and print in table
                for (int i=1; i<=5; i++)
                    System.out.println(theResult.getObject(i));
                System.out.println();
            }

        }
        catch (SQLException se){
            se.printStackTrace();
        }
        finally {
            try {
                theStatement1.close();//Close statement
                theStatement2.close();
                theResult.close();
                theConnection.close(); //Close database Connection
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        // TODO code application logic here
    }
    }

}

