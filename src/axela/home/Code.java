package axela.home;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Code extends Connect {
    
    public String StrHTML = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CheckSession(request, response);
        HttpSession session = request.getSession(true);
//            comp_id = CNumeric(GetSession("comp_id", request));
        StrHTML = (String) request.getSession().getAttribute("captcha");
        SOP("StrHTML = " + StrHTML);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
