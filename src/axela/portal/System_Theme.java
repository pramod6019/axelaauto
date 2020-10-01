package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class System_Theme extends Connect {

    public String msg = "";
    public String StrSql = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String theme = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	 emp_id = CNumeric(GetSession("emp_id", request));
                 msg = PadQuotes(request.getParameter("msg"));
                 GetValues(request, response);
                 if (!theme.equals("0")) {
                     UpdateFields();
                     Cookie cookie = new Cookie("axelatheme", theme);
                     cookie.setMaxAge(60 * 60 * 24 * 30 * 60);
                     cookie.setPath(request.getContextPath());
                     response.addCookie(cookie);
                     response.sendRedirect(response.encodeRedirectURL("system-theme.jsp?msg=Theme has been updated succesfully!"));
                 } else {
                     theme = ExecuteQuery("Select emp_theme_id from " + compdb(comp_id) + "axela_emp"
                             + " where emp_id = " + emp_id);
                 }
            }
           
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        theme = CNumeric(PadQuotes(request.getParameter("txt_emp_theme_id")));
//        SOP("theme==" + theme);

    }

    protected void UpdateFields() {
        try {
            StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
                    + " Set"
                    + " emp_theme_id = " + theme + ""
                    + " where emp_id = " + emp_id + "";
            updateQuery(StrSql);

        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
