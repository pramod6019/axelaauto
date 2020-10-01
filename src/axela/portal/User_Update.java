package axela.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class User_Update extends Connect {

	public String update = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String user_id = "0";
	public String user_title_id = "0";
	public String user_name = "";
	public String user_fname = "";
	public String user_lname = "";
	public String user_mobile = "";
	public String user_email = "";
	public String user_active = "0";

	public String emp_role_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String QueryString = "";
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_contact_access", request, response);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				user_id = CNumeric(request.getParameter("user_id"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());

				// SOP("user_id===" + user_id);
				if (update.equals("yes")) {
				}
				if (!user_id.equals("0")) {
					StrSql = "SELECT COALESCE(CONCAT(title_desc, ' ', user_fname, ' ', user_lname), '') AS user_name"
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = user_title_id"
							+ " WHERE user_id = " + user_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							user_name = crs.getString("user_name");
						}
					} else {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid User!"));
					}
					crs.close();
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!("yes").equals(updateB)) {
						if (!user_name.equals("")) {
							PopulateFields(response);
						}
					} else if (("yes").equals(updateB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_contact_edit", request).equals("1")) {
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("user-list.jsp?user_id=" + user_id + "&msg=Service User details updated successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		user_id = PadQuotes(request.getParameter("user_id"));
		user_title_id = PadQuotes(request.getParameter("dr_title"));
		user_fname = PadQuotes(request.getParameter("txt_user_fname"));
		user_lname = PadQuotes(request.getParameter("txt_user_lname"));
		user_mobile = PadQuotes(request.getParameter("txt_user_mobile"));
		user_email = PadQuotes(request.getParameter("txt_user_email"));
		user_active = CheckBoxValue(PadQuotes(request.getParameter("chk_user_active")));
	}

	protected void CheckForm() {
		msg = "";

		if (user_title_id.equals("0")) {
			msg += "<br>Select Title!";
		}

		if (user_fname.equals("")) {
			msg += "<br>Enter the Contact Person First Name!";
		} else {
			user_fname = toTitleCase(user_fname);
		}

		if (!user_lname.equals("")) {
			user_lname = toTitleCase(user_lname);
		}

		if (user_mobile.equals("91-")) {
			user_mobile = "";
		}

		if (!user_email.equals("") && IsValidEmail(user_email) != true) {
			msg += "<br>Enter valid Email!";
		} else {
			user_email = user_email.toLowerCase();
		}

	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_user"
					+ " SET"
					+ " user_title_id = " + user_title_id + ","
					+ " user_fname = '" + user_fname + "',"
					+ " user_lname = '" + user_lname + "',"
					+ " user_mobile = '" + user_mobile + "',"
					+ " user_email = '" + user_email + "',"
					+ " user_active = '" + user_active + "'"
					+ " WHERE user_id = " + user_id + " ";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT user_id, user_title_id, user_fname, user_lname,"
					+ " user_mobile, user_email, user_active"
					+ " FROM " + compdb(comp_id) + "axela_app_user"
					+ " WHERE 1=1"
					+ " AND user_id = " + user_id + BranchAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					user_id = crs.getString("user_id");
					user_title_id = crs.getString("user_title_id");
					user_fname = crs.getString("user_fname");
					user_lname = crs.getString("user_lname");
					user_mobile = crs.getString("user_mobile");
					user_email = crs.getString("user_email");
					user_active = crs.getString("user_active");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid User!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"), user_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
