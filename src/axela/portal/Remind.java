package axela.portal;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class Remind extends Connect {

	public String submitB = "";
	public String msg = "";
	public String comp_id = "0";
	public String emp_id = "0";
	public String StrSql = "", timeout = "";
	public String email_id = "", emp_uuid = "";
	public String signinid = "", emp_name = "";
	public String desc = "", from_address = "";
	public String password = "";
	public String to_address1 = "", to_address2 = "";
	public String AppUrl = "";
	Encrypt encrypt = new Encrypt();
	StringBuilder Str = new StringBuilder();
	Executives_Update update = new Executives_Update();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			getcompsession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				submitB = PadQuotes(request.getParameter("submit_button"));

				if (submitB.equals("Submit")) {
					email_id = PadQuotes(request.getParameter("emailid"));
					CheckFields();
					if (msg.equals("")) {
						timeout = ToLongDate(AddHoursDate(kknow(), 0, 48, 0));
						StrSql = "SELECT emp_id, emp_email1, emp_email2, emp_name, emp_uuid, comp_subdomain, config_admin_email"
								+ " FROM " + compdb(comp_id) + "axela_emp "
								+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id + ","
								+ compdb(comp_id) + "axela_config"
								+ " WHERE comp_active = 1"
								+ " AND emp_email1 != ''"
								+ " AND emp_active = '1'"
								+ " AND (emp_email1 = '" + email_id + "'"
								+ " OR emp_email2 = '" + email_id + "')";
						CachedRowSet crs =processQuery(StrSql, 0);

						try {
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									signinid = crs.getString("emp_email1");
									emp_id = crs.getString("emp_id");
									emp_name = crs.getString("emp_name");
									from_address = "AxelaAuto " + "<" + crs.getString("config_admin_email") + ">";
									to_address1 = emp_name + " <" + email_id + ">";
									emp_uuid = crs.getString("emp_uuid");
									password = GenPass(12);
									AppUrl = AppURL().replace("demo", crs.getString("comp_subdomain"));
									if (!crs.getString("emp_email2").equals("")) {
										to_address2 = emp_name + " <" + crs.getString("emp_email2") + ">";
									}
								}

								updateQuery("UPDATE " + compdb(comp_id) + "axela_emp"
										+ " SET"
										+ " emp_upass = '" + password + "'"
										+ " WHERE emp_email1 = '" + email_id + "'");

								new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);

								Str.append("<font face=\"Arial\" size=\"2\">Hi ").append(emp_name).append(",");
								Str.append("<br/><br/>You have requested to reset your password on ").append(strToLongDate(ToLongDate(kknow()))).append(", ");
								Str.append("please follow the link below and you will be able to personally reset your password.");
								Str.append("<br/><br/><a href=\"").append(AppUrl).append("portal/user-authentication.jsp?data=");
								Str.append(URLEncoder.encode(encrypt.encrypt("timeout=" + timeout + "&user=" + emp_uuid + "&redirect=portal/reassign-password.jsp"), "UTF-8"));
								Str.append("\">Click here to reset your password.</b></a>");
								Str.append("<br/><br/>This password reset request is valid for the next 48 hours.");
								Str.append("<br/><br/>Best Regards,");
								Str.append("<br/><b>Team ").append(AppName).append("</b></font>");
								// SOP("str===" + Str);
								postMail(to_address1, to_address2, "", from_address, "Forgot Password", Str.toString(), "", comp_id);
								msg = "Password reset link has been sent to your Email ID!";
							} else {
								msg = "Invalid Email ID!";
							}
							crs.close();
						} catch (Exception ex) {// }
							SOPError("Axelaauto===" + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckFields() {
		msg = "";
		if (email_id.equals("")) {
			msg = "Enter Email ID!<br>";
		} else if (!IsValidEmail(email_id)) {
			msg = "Enter valid Email ID!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
