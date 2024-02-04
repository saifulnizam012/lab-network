/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContactWebApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class DisplayContactsServlet extends HttpServlet {

    Connection theConnection;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<HTML><HEAD><TITLE>Contact List.</TITLE>");
            out.println("</HEAD>");
            out.println("<BODY bgColor=blanchedalmond text=#008000 topMargin=0>");
            out.println("<P align=center><FONT face=Helvetica><FONT color=fuchsia style=\"BACKGROUND-COLOR: white\"><BIG><BIG>List of Contacts.</BIG></BIG></FONT></P>");
            out.println("<P align=center>");
            out.println("<TABLE align=center border=1 cellPadding=1 cellSpacing=1 width=\"85%\">");
            out.println("<TR>");
            out.println("<TD>Name</TD>");
            out.println("<TD>E-mail</TD>");
            out.println("<TD>Phone No</TD></TR>");
            try{
                String dbURL = "jdbc:derby://localhost:1527/TN3223";
                String driver = "org.apache.derby.jdbc.ClientDriver";
                Properties properties = new Properties();
                properties.put("create", "true");
                properties.put("user", "TN3223");
                properties.put("password", "TN3223");
                Class.forName(driver);
                theConnection = DriverManager.getConnection(dbURL, properties);
                try (Statement theStatement = theConnection.createStatement(); ResultSet theResult = theStatement.executeQuery("SELECT * FROM TN3223.MYCONTACTS")) {
                    //Select all records from CONTACTS table.
                    while(theResult.next()) { //Fetch all the records and print in table
                        out.println();
                        out.println("<TR>");
                        out.println("<TD>" + theResult.getString(1) + "</TD>");
                        out.println("<TD>" + theResult.getString(2) + "</TD>");
                        out.println("<TD>" + theResult.getString(3) + "</TD>");
                        //String s=theResult.getString(3);
                        //out.println("<TD><a href=" + s + ">" + s + "</a></TD>");
                        out.println("</TR>");
                    }
                    //Close the result set
                    //Close statement
                                      }
                theConnection.close(); //Close database Connection
            }catch(SQLException e){
                out.println(e.getMessage());//Print trapped error.
            }
        } finally { 
            out.close();
        }
    } 

   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DisplayContactsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DisplayContactsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}