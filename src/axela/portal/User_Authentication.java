package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class User_Authentication extends Connect {

	public String StrSql = "";
	public String data = "";
	public String emp_uuid = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_email1 = "";
	public String emp_upass = "";
	public String timeout = "";
	public String redirect = "";
	Encrypt encrypt = new Encrypt();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			getcompsession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				data = PadQuotes(request.getParameter("data"));
				data = encrypt.decrypt(data);
				String[] token = data.split("&");

				timeout = token[0].substring((token[0].lastIndexOf("=") + 1));
				emp_uuid = token[1].substring((token[1].lastIndexOf("=") + 1));
				redirect = token[2].substring((token[2].lastIndexOf("=") + 1));
				redirect = redirect.replace("@#$", "=");

				SOP("timeout = " + timeout);
				SOP("emp_uuid = " + emp_uuid);
				SOP("emp_uuid length = " + emp_uuid.length());
				SOP("redirect = " + redirect);

				if (getHoursBetween(StringToDate(timeout), kknow()) <= 48.00) {
					if (!emp_uuid.equals("") && (emp_uuid.length() == 36) && !redirect.equals("")) {
						StrSql = "SELECT emp_id, emp_branch_id, emp_role_id, emp_all_exe, emp_recperpage,"
								+ " emp_email1, emp_upass"
								+ " FROM " + compdb(comp_id) + "axela_emp "
								+ " WHERE emp_uuid ='" + emp_uuid + "'"
								+ " and emp_active=1";
						// SOP("StrSql=========================== = " + StrSql);
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
						user = index.Signin(emp_email1, emp_upass, "", comp_id, request, response);
						if (user.equals("Valid")) {
							// / Start redirection
							response.sendRedirect(response.encodeRedirectURL("../" + redirect + ""));
						} else {
							response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=" + user + ""));
						}

					} else {
						response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=Invalid User"));
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("index.jsp?msg=Your token has expired!"));
				}

			}

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
