package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class Spawn_PM extends Connect {

	public String StrSql = "";
	public String data = "";
	public String emp_uuid = "";
	public String emp_id = "";
	public String server_name = "";
	public String comp_id = "0";
	public String emp_email1 = "";
	public String emp_upass = "";
	public String timeout = "";
	public String redirect = "";
	public String check = "";
	Encrypt encrypt = new Encrypt();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			check = request.getHeader("referer");
			// if (check.contains("https://pm.emax.in/pm") || check.contains("http://139.162.48.130/pm")) {
			data = PadQuotes(request.getParameter("data"));
			data = encrypt.decrypt(data);
			String[] token = data.split("&");

			comp_id = token[0].substring((token[0].lastIndexOf("=") + 1));
			emp_id = token[1].substring((token[1].lastIndexOf("=") + 1));

			redirect = redirect.replace("@#$", "=");

			getcompsession(request, response);
			HttpSession session = request.getSession(true);

			if (!comp_id.equals("0"))
			{
				StrSql = "SELECT emp_id, emp_email1, emp_upass"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id ='" + emp_id + "'"
						+ " AND emp_active=1";

				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						emp_id = crs.getString("emp_id");
						emp_email1 = crs.getString("emp_email1");
						emp_upass = crs.getString("emp_upass");
					}
				}
				crs.close();

				Index index = new Index();
				String user = "";
				index.user_jsessionid = session.getId();
				index.signincount = 1;
				index.signinid = emp_email1;
				index.password = emp_upass;

				user = index.Signin(emp_email1, emp_upass, "yes", comp_id, request, response);

				if (user.equals("Valid")) {
					response.sendRedirect(response.encodeRedirectURL("../portal/home.jsp"));
				} else {
					response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=" + user + ""));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=Access Denied!"));
			}
			// } else {
			// response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=Access Denied!"));
			// }
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
