/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.csdbapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Properties;

/**
 *
 * @author Norly
 */
public class Server { //declaration variable
    public static final int SERVICE_PORT = 8081; //port location
    public static final String HOSTNAME = "localhost";  //
    
    static final String DBURI = "jdbc:derby://localhost:1527/TN3223";
    //static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static final String QUERY1 = "INSERT INTO TN3223.USERDATA VALUES(?, ?, ?, ?)"; //modify
    static final String QUERY2 = "SELECT * FROM TN3223.USERDATA";
    static String userId = "";
    static String password = "";
    
    public static void main(String args[]) throws IOException { //Server start
        
        ServerSocket server;
        
        try {
            server = new ServerSocket(SERVICE_PORT);//initialize server socket
           
            System.out.println("Server started");
            
            while (true) {
                try (Socket s = server.accept()) { //accept connection
                    
                    receiveImage(s);
                    readUserData(s);
                    connectToDatabase(); //connect and save in database
                   
                }
            }
        } catch (IOException e) {
            System.err.println("I/O exception: " + e);
        }
       
    }
    
    public static void receiveImage(Socket s) throws IOException{  //Server receive image data from client 
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String imageString;
        while ((imageString = in.readLine()) != null) {//check image data is not null
            break;
        }
        System.out.println(imageString); 
        //convert received data
        
        System.out.println("Image file Received!");
        byte[] imageByteArray = decodeImage(imageString);
        File fileName = new File("image/stag_r.png");
        
        //convert byte array to a file image
        FileOutputStream imageOutFile = new FileOutputStream(fileName);
        imageOutFile.write(imageByteArray);
        imageOutFile.close();
        System.out.println("Successfully create received image file!");
        
    }
    
    public static byte[] decodeImage(String imageDataString) {   //convert from base 64 to byte array
        //byte[] decoded = DatatypeConverter.parseBase64Binary(imageDataString);
        byte[] decodedBytes = Base64.getDecoder().decode(imageDataString);
        String decodedString = new String(decodedBytes);
        return decodedBytes;
    }
    
    public static void readUserData(Socket s) throws IOException{//client read data (user.txt) and sent to server
    
        System.out.println("Finish");

        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String line;
        System.out.println("Reading user data");
        int count = 0;
        line = br.readLine();
        while (line != null) {
            count++;
            if (count == 1) 
                userId = line;
            if (count == 2) 
                password = line;
                
            System.out.println("received " + line);
            line = br.readLine();

        }
    }
    public static void connectToDatabase() throws IOException {
        Connection theConnection = null;
        PreparedStatement theStatement1 = null;
        ResultSet theResult = null;
        Statement theStatement2 = null;
        //Create timestamp for column 4
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        File image = new File("image/stag_r");
        FileInputStream fis = new FileInputStream(image);
       
        try {
            //Class.forName(DRIVER);
            Properties properties = new Properties();
            properties.put("create", "true");
            properties.put("user", "TN3223");
            properties.put("password", "abc1234");
            theConnection = DriverManager.getConnection(DBURI, properties);
            System.out.println("Connected to database");
            theStatement1 = theConnection.prepareStatement(QUERY1);
            
            //Set parameters for INSERT statement and execute it
            theStatement1.setString(1, userId);
            theStatement1.setString(2, password);
            theStatement1.setBinaryStream(3, fis);
         
            // add statement to add timestamp
            theStatement1.setTimestamp(4,ts);
            theStatement1.executeUpdate();

            theStatement2 = theConnection.createStatement();
            theResult = theStatement2.executeQuery(QUERY2);
            System.out.println("Retrieved data from table TN3223.USERDATA");
            while (theResult.next()) {
                System.out.println("User Id: " + theResult.getString(1));
                System.out.println("Password: " + theResult.getString(2));
                System.out.println("Image: " + theResult.getBlob(3));
                //Add statement to display timestamp fetched from table
                System.out.println("Time: " + theResult.getTimestamp(4));
                
            }
        } 
        catch (SQLException se) {
            System.out.println(se.toString());
        } 
        
        finally {
            try {
                theStatement1.close();//Close statement
                theStatement2.close();
                theResult.close();
                theConnection.close(); //Close database Connection
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

    }
    
}
