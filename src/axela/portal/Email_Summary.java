package axela.portal;

/**
 * Created on October 10, 2008, 3:29 PM
 * @author nivedhitha
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email_Summary extends Connect {

	public String StrHTML = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML1 = "";
	public String StrSql = "";
	public String email_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_email_access", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				email_id = CNumeric(PadQuotes(request.getParameter("email_id")));
				StrHTML1 = EmailListdata();
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

	public String EmailListdata() {
		StrHTML = "";
		StrSql = " Select email_id, email_from, email_to, email_subject, email_msg, "
				+ " coalesce(CONCAT(emp_name,' (',emp_ref_no,')'),'') as emp_name,"
				+ " email_emp_id "
				+ " from " + compdb(comp_id) + "axela_email "
				+ " left  join " + compdb(comp_id) + "axela_emp on emp_id=email_emp_id "
				+ " where email_id = " + email_id;
		if (!branch_id.equals("0")) {
			StrSql = StrSql + " and emp_branch_id= " + branch_id + "";
		}
		StrHTML = StrHTML + "<table class=\"table table-bordered\">";
//		StrHTML = StrHTML + "<tr>\n";
//		StrHTML = StrHTML + "<th colspan=2>Email Details</th>\n";
//		StrHTML = StrHTML + "</tr>\n";

		try {
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("email_from").equals("") && crs.getString("email_from") != null) {
						StrHTML = StrHTML + "<tr>\n"
								+ "<td width=25%> From: </td>"
								+ "<td>" + crs.getString("email_from") + "</td></tr>\n";
					}
					if (!crs.getString("email_to").equals("") && crs.getString("email_to") != null) {
						StrHTML = StrHTML + "<tr>\n"
								+ " <td>To: </td>"
								+ "<td>" + crs.getString("email_to") + "</td></tr>\n";
					}
					if (!crs.getString("emp_name").equals("") && crs.getString("emp_name") != null) {
						StrHTML = StrHTML + "<tr>\n"
								+ " <td>Executive: </td>"
								+ "<td><a href=\"executive-summary.jsp?emp_id=" + crs.getString("email_emp_id") + "\">" + crs.getString("emp_name") + "</a></td></tr>\n";
					}
					if (!crs.getString("email_subject").equals("") && crs.getString("email_subject") != null) {

						StrHTML = StrHTML + "<tr>\n"
								+ "<td><b>Subject: </b></td>"
								+ "<td><b>" + crs.getString("email_subject") + "</b></td></tr>\n";
					}
					if (!crs.getString("email_msg").equals("") && crs.getString("email_msg") != null) {
						StrHTML = StrHTML + "<tr>\n"
								+ " <td>Message: </td>"
								+ "<td>" + unescapehtml(crs.getString("email_msg")) + "</td></tr>\n";
					}
				}
				StrHTML = StrHTML + "</table>\n";
				
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return StrHTML;
	}
}
