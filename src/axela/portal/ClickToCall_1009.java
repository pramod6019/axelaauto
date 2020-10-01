package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ClickToCall_1009 {

	public String StrSql = "";
	public String StrHTML = "";
	public String callno = "";
	public String emp_clicktocall = "";
	public String emp_routeno = "";
	public String emp_callerid = "";
	public String emp_clicktocall_username = "";
	public String emp_clicktocall_password = "";
	public String emp_clicktocall_campaign = "";
	public String comp_id = "0";
	public String emp_id = "0";
	Connect ct = new Connect();
	CachedRowSet crs = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			// comp_id = CNumeric(GetSession("comp_id", request));
			if (!(ct.GetSession("emp_clicktocall", request).equals(""))) {
				comp_id = (ct.CNumeric(ct.GetSession("comp_id", request)));
				emp_id = (ct.CNumeric(ct.GetSession("emp_id", request)));
				callno = ct.PadQuotes(request.getParameter("callno"));
				callno = callno.substring(2);
				ct.SOP("callno=1234=" + callno);
				StrSql = " SELECT emp_clicktocall, emp_clicktocall_username, emp_clicktocall_password, emp_clicktocall_campaign "
						+ " FROM " + ct.compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id;
				// ct.SOP("StrSql------------" + StrSql);
				crs = ct.processQuery(StrSql, 0);
				while (crs.next()) {
					emp_clicktocall = crs.getString("emp_clicktocall");
					emp_clicktocall_username = crs.getString("emp_clicktocall_username");
					emp_clicktocall_campaign = crs.getString("emp_clicktocall_campaign");
					emp_clicktocall_password = crs.getString("emp_clicktocall_password");
				}
				crs.close();

				StrHTML = "<div class=\"modal-header\">\n";
				StrHTML += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"></button>\n";
				StrHTML += "<h4 class=\"modal-title\" style=text-align:center>Click to Call</h4>\n";
				StrHTML += "</div>\n";
				StrHTML += "<div class=\"modal-body \">\n";
				StrHTML += "<div class=\"row\">\n";
				StrHTML += "<div class=\"col-md-12\" style=text-align:center>\n";
				ct.SOP("value==" + callno.length());
				ct.SOP("value=callno=" + callno);
				if (emp_clicktocall.equals("1") && !emp_clicktocall_username.equals("")) {
					if (!callno.equals("") && (callno.length() == 10)) {
						// StrHTML = "Calling " + callno + "";
						// StrHTML += "<div style=\"visibility : hidden\">";
						// StrHTML += "<div id=emp_clicktocall_username value='" + emp_clicktocall_username + "'>" + emp_clicktocall_username + "'</div>";
						// StrHTML += "<div id=emp_clicktocall_password>" + emp_clicktocall_password + "</div>";
						// StrHTML += "<div id=callno>" + callno + "</div>";
						// StrHTML += "<div id=emp_clicktocall_campaign>" + emp_clicktocall_campaign + "</div>";
						// StrHTML += "</div>";
						// CallServiceProvider(emp_routeno, emp_callerid, callno);
						// ct.SOP("emp_clicktocall_username----------------" + emp_clicktocall_username);

						StrHTML += "<a href=javascript:Click2CallDDMotors('" + emp_clicktocall_username + "','" + emp_clicktocall_password + "','" + callno + "','" + emp_clicktocall_campaign
								+ "')><center>Click here to call " + callno + "</center></a>\n";
					} else {
						StrHTML += "<font color=red>Invalid No.!</font>";
					}
				} else {
					StrHTML += "<font color=red>Access Denied for Click to Call!</font>";
				}
				StrHTML += "</div>\n";
				StrHTML += "</div>\n";
				StrHTML += "</div>\n";
				// ct.SOP("StrHTML----------------" + StrHTML);
			}

		} catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
