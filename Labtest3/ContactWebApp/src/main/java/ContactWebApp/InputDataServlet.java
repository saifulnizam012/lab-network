/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContactWebApp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author norleyza
 */
public class InputDataServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    String query = "INSERT INTO TN3223.MYCONTACTS VALUES(?, ?, ?)";
    Connection theConnection;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException, ClassNotFoundException {
        String nameS = request.getParameter("name");
 	String emailS = request.getParameter("email");
	String phoneNoS = request.getParameter("phoneNo");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>InputDataServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InputDataServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            out.println("<P align=center>");
            //this servlet will call another servlet
            out.println("<form name=form1 action=\"http://localhost:8080/ContactWebApp/DisplayContactsServlet\">");
            out.println("<p>");
            out.println("<input type=submit name=\"submit\" value=\"Display My Contacts\">");
            out.println("</p>");
            out.println("</form>");

            //Connect to emaildb Data source
            String dbURL = "jdbc:derby://localhost:1527/TN3223";
            String driver = "org.apache.derby.jdbc.ClientDriver";
            Properties properties = new Properties();
            properties.put("create", "true");
            properties.put("user", "TN3223");
            properties.put("password", "TN3223");
            // prepare statement for inserting data into table
            try {
                Class.forName(driver); 
                theConnection = DriverManager.getConnection(dbURL, properties);

                PreparedStatement theStatement = theConnection.prepareStatement(query);

                //Set parameters for INSERT statement and execute it
                theStatement.setString(1, nameS);
                theStatement.setString(2, emailS);
                theStatement.setString(3, phoneNoS);
                theStatement.executeUpdate();
            } catch (SQLException sqle) {
                System.out.println(sqle);
            }
	} finally {
            //if (theConnection != null)
           //     theConnection.close(); //Close database Connection
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InputDataServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InputDataServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InputDataServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InputDataServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
