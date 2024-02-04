/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.csdbapp;

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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Norly
 */
public class Client {

    public static final int SERVICE_PORT = 8081; //port location
    public static final String HOSTNAME = "localhost"; //host name

    public static void main(String args[]) throws FileNotFoundException {
       
        try (Socket s = new Socket(HOSTNAME, SERVICE_PORT)) {
            
            File selectedFile = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an image");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            // reading the image data and displaying it.
            }
            File imageFile = new File(selectedFile.getAbsolutePath());
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

    public static void sendImageDataString(Socket s, String imageString) throws IOException{ //base 64 is function to sent data over the network
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeBytes(imageString);
        System.out.println("Image file Sent!");
    }
    
}
// Client <-> Server <-> Database
