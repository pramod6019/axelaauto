/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

/**
 *
 * @author Satish
 */
public class Capture_Webcam_Img extends Connect {

    private String fileStoreURL = "";
    public String comp_id = "0";
    public Capture_Webcam_Img() {
        super();
    }

//    public void init(ServletConfig config) throws ServletException {
//        fileStoreURL = config.getServletContext().getRealPath("") + "/upload";
//        SOP("path==== " + fileStoreURL);
//        try {
//            File f = new File(fileStoreURL);
//            if (!f.exists()) {
//                f.mkdir();
//            }
//        } catch (Exception e) {
//            SOPError("Error 404: " + e.getMessage());
//        }
//
//    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            fileStoreURL = JobCardImgPath(comp_id) + "upload";
         
            File f = new File(fileStoreURL);
            if (!f.exists()) {
                f.mkdir();
            }

            long time = new Date().getTime();
            SOP("fileStoreURL -- " + fileStoreURL);
            FileOutputStream fileOutputStream = new FileOutputStream(fileStoreURL + "/" + time + ".jpg");
            int res;
            SOP("res = " + request.getInputStream().read());
            while ((res = request.getInputStream().read()) != -1) {
                fileOutputStream.write(res);
            }
            fileOutputStream.close();
            response.getWriter().append(JobCardImgPath(comp_id) + time + ".jpg");
        } catch (Exception e) {
            SOPError("Error : " + e.getMessage());
        } finally {
            out.close();
        }
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
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
