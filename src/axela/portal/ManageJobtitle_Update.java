package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJobtitle_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String jobtitle_id = "0";
	public String jobtitle_report_id = "0";
	public String QueryString = "";
	public String jobtitle_desc;
	public String emp_id = "0";
	public String comp_id = "0";
	public String jobtitle_entry_id = "0";
	public String jobtitle_entry_date = "";
	public String jobtitle_modified_id = "0";
	public String jobtitle_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				jobtitle_id = CNumeric(PadQuotes(request.getParameter("jobtitle_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						jobtitle_desc = "";
					} else {
						GetValues(request, response);
						jobtitle_entry_id = emp_id;
						jobtitle_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejobtitle.jsp?jobtitle_id=" + jobtitle_id + "&msg=Job Title Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Job Title".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Job Title".equals(deleteB)) {
						GetValues(request, response);
						jobtitle_modified_id = emp_id;
						jobtitle_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejobtitle.jsp?jobtitle_id=" + jobtitle_id + "&msg=Job Title Updated Successfully!"));
						}
					} else if ("Delete Job Title".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejobtitle.jsp?msg=Job Title Deleted Successfully!"));
						}
					}
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
		jobtitle_desc = PadQuotes(request.getParameter("txt_jobtitle_desc"));
		jobtitle_report_id = CNumeric(PadQuotes(request.getParameter("dr_jobtitle_report_id")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (jobtitle_desc.equals("")) {
			msg = "<br>Enter Job Title!";
		}
		try {
			if (!jobtitle_desc.equals("")) {
				if (update.equals("yes")) {
					StrSql = "Select jobtitle_desc from " + compdb(comp_id) + "axela_jobtitle"
							+ " where jobtitle_desc = '" + jobtitle_desc + "'"
							+ " and jobtitle_id != " + jobtitle_id + "";
				} else if (add.equals("yes")) {
					StrSql = "Select jobtitle_desc from " + compdb(comp_id) + "axela_jobtitle"
							+ " where jobtitle_desc = '" + jobtitle_desc + "'";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Job Title Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				jobtitle_id = ExecuteQuery("Select (coalesce(max(jobtitle_id),0)+1) from " + compdb(comp_id) + "axela_jobtitle");
				StrSql = "Insert into " + compdb(comp_id) + "axela_jobtitle"
						+ " (jobtitle_id,"
						+ " jobtitle_desc,"
						+ " jobtitle_entry_id,"
						+ " jobtitle_entry_date)"
						+ " values"
						+ " (" + jobtitle_id + ","
						+ " '" + jobtitle_desc + "',"
						+ " " + jobtitle_entry_id + ","
						+ " '" + jobtitle_entry_date + "')";
				// SOP("SqlStr" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_jobtitle"
					+ " WHERE jobtitle_id = " + jobtitle_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					jobtitle_id = crs.getString("jobtitle_id");
					jobtitle_desc = crs.getString("jobtitle_desc");
					jobtitle_report_id = crs.getString("jobtitle_report_id");
					jobtitle_entry_id = crs.getString("jobtitle_entry_id");
					if (!jobtitle_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(jobtitle_entry_id));
						entry_date = strToLongDate(crs.getString("jobtitle_entry_date"));
					}
					jobtitle_modified_id = crs.getString("jobtitle_modified_id");
					if (!jobtitle_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(jobtitle_modified_id));
						modified_date = strToLongDate(crs.getString("jobtitle_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Title!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_jobtitle"
						+ " SET"
						+ " jobtitle_desc = '" + jobtitle_desc + "',"
						+ " jobtitle_report_id = " + jobtitle_report_id + ","
						+ " jobtitle_modified_id = " + jobtitle_modified_id + ","
						+ " jobtitle_modified_date = '" + jobtitle_modified_date + "'"
						+ " where jobtitle_id = " + jobtitle_id + "";
				// SOP("SqlStr=== " + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT emp_jobtitle_id FROM " + compdb(comp_id) + "axela_emp"
				+ " where emp_jobtitle_id = " + jobtitle_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Job Title is Associated with Executive!";
		}

		StrSql = "SELECT jobtitle_id FROM " + compdb(comp_id) + "axela_jobtitle"
				+ " WHERE jobtitle_report_id = " + jobtitle_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Job Title is Associated with Reporting Job Title!";
		}

		StrSql = "SELECT discount_id FROM " + compdb(comp_id) + "axela_sales_discount"
				+ " WHERE discount_jobtitle_id = " + jobtitle_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Job Title is Associated with Discount!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_jobtitle where jobtitle_id = " + jobtitle_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateReportJobTitle() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=0>Select</option>\n");
			StrSql = "SELECT jobtitle_id, jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_jobtitle"
					+ " WHERE jobtitle_id !=" + jobtitle_id
					+ " ORDER BY jobtitle_desc";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jobtitle_id"));
				Str.append(StrSelectdrop(crs.getString("jobtitle_id"), jobtitle_report_id));
				Str.append(">").append(crs.getString("jobtitle_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
