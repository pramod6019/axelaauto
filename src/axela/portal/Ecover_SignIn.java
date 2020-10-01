package axela.portal;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class Ecover_SignIn extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String url = "";
	public String comp_id = "";
	public String project_name = "";
	public String domain = "";
	public String emp_id = "0";
	public String parameters = "";
	public String enquiry_add = "", policy_add = "", policydetails = "";
	Encrypt encrypt = new Encrypt();
	public String StrHTML = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
			enquiry_add = PadQuotes(request.getParameter("enquiry_add"));
			policy_add = PadQuotes(request.getParameter("policy_add"));
			policydetails = PadQuotes(request.getParameter("policydetails"));
			if (!emp_id.equals("0")) {
				comp_id = GetSession("comp_id", request);
				emp_id = GetSession("emp_id", request);
				StrSql = "SELECT emp_insur"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1"
						+ " AND emp_active = 1"
						+ " AND emp_id = " + emp_id;
				if (comp_id.equals("0") || (CNumeric(ExecuteQuery(StrSql)).equals("0") && !emp_id.equals("1")
						&& !enquiry_add.equals("yes") && !policy_add.equals("yes"))) {
					msg = "msg=Access denied. Please contact system administrator!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				} else {
					if (AppRun().equals("1")) {
						if (comp_id.equals("1000")) {
							url = "http://" + "demo.ecover.in";
						} else if (comp_id.equals("1009")) {
							url = "http://" + "ddmotors.ecover.in";
						} else if (comp_id.equals("1011")) {
							url = "http://" + "indel.ecover.in";
						}
					} else {
						url = "http://localhost:7090";
					}
					url += "/ecover/portal";

					url += "/spawn-pm.jsp?data=" + URLEncoder.encode(encrypt.encrypt("comp_id=" + comp_id + "&emp_id=" + emp_id), "UTF-8");
				}
				// SOP("url===" + url);
				if (enquiry_add.equals("yes")) {
					url += "&enquiry_add=yes";
				}
				if (policy_add.equals("yes")) {
					url += "&policy_add=yes&policydetails=" + policydetails;
				}
				if (!url.equals("")) {
					response.sendRedirect(url);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
