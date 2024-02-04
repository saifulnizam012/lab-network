/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csdb;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
/**
 *
 * @author HP-PC
 */
public class Client {
     public static final int SERVICE_PORT = 8081;
    public static final String HOSTNAME = "localhost";

    public static void main(String args[]) throws FileNotFoundException {
       
        try (Socket s = new Socket(HOSTNAME, SERVICE_PORT)) {
            
            File imageFile = new File("stag.png");
            //Image conversion to byte array
            FileInputStream fis = new FileInputStream(imageFile);
            byte imageData[] = new byte[(int) imageFile.length()];
            fis.read(imageData);
            //Image conversion byte array in Base64 String
            String imageDataString = encodeImage(imageData);
            //Send image data as string
            sendImageDataString(s, imageDataString);
            
            FileReader in = new FileReader(new File("user.txt"));
            //Send user data
            sendUserData(s, in);
            s.close();
        } catch (IOException e) {
            System.out.println(e);
        } 
        
    }
    
    public static void sendUserData(Socket s, FileReader in) throws IOException{
        BufferedReader reader = new BufferedReader(in);
          
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        String line;
        //Reading from file and send data to server
        while ((line = reader.readLine()) != null) {
            pw.flush();
            pw.println(line);
            pw.flush();
            System.out.println(line);
        }

        System.out.println("User data sent");
    }

    public static String encodeImage(byte[] imageByteArray) {
        String encoded = Base64.getEncoder().encodeToString(imageByteArray);// Java and above
        //DatatypeConverter.printBase64Binary(imageByteArray);Java 7
        System.out.println("Encoded image" + encoded);

        return encoded;
    }

    public static void sendImageDataString(Socket s, String imageString) throws IOException{
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeBytes(imageString);
        System.out.println("Image file Sent!");
    }
}

