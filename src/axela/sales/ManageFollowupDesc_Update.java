package axela.sales;
//Bhagwan Singh 07/08/2013  

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageFollowupDesc_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String followupdesc_id = "0";
	public String followupdesc_name = "";
	public String QueryString = "";
	public String followupdesc_entry_id = "0";
	public String followupdesc_entry_date = "";
	public String followupdesc_modified_id = "0";
	public String followupdesc_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				followupdesc_id = CNumeric(PadQuotes(request.getParameter("followupdesc_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						followupdesc_name = "";
					} else {
						GetValues(request, response);
						followupdesc_entry_id = emp_id;
						followupdesc_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefollowupdesc.jsp?followupdesc_id=" + followupdesc_id + "&msg=Follow-up Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Follow-up".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Follow-up".equals(deleteB)) {
						GetValues(request, response);
						followupdesc_modified_id = emp_id;
						followupdesc_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefollowupdesc.jsp?followupdesc_id=" + followupdesc_id + "&msg=Follow-up Updated Successfully!"));
						}
					} else if ("Delete Follow-up".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefollowupdesc.jsp?msg=Follow-up Deleted Successfully!"));
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
		// followupdesc_id = CNumeric(PadQuotes(request.getParameter("followupdesc_id")));
		followupdesc_name = PadQuotes(request.getParameter("txt_followupdesc_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (followupdesc_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			try {
				StrSql = "SELECT followupdesc_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
						+ " WHERE followupdesc_name = '" + followupdesc_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND followupdesc_id != " + followupdesc_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Follow-up Description Found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				followupdesc_id = ExecuteQuery("SELECT (COALESCE(max(followupdesc_id), 0) +1) FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_desc");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
						+ " (followupdesc_id,"
						+ " followupdesc_name,"
						+ " followupdesc_entry_id,"
						+ " followupdesc_entry_date)"
						+ " values"
						+ " (" + followupdesc_id + ","
						+ " '" + followupdesc_name + "',"
						+ " " + followupdesc_entry_id + ","
						+ " '" + followupdesc_entry_date + "')";
				// SOP("11=" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
					+ " WHERE followupdesc_id = " + followupdesc_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					followupdesc_name = crs.getString("followupdesc_name");
					followupdesc_entry_id = crs.getString("followupdesc_entry_id");
					if (!followupdesc_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(followupdesc_entry_id));
						entry_date = strToLongDate(crs.getString("followupdesc_entry_date"));
					}
					followupdesc_modified_id = crs.getString("followupdesc_modified_id");
					if (!followupdesc_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(followupdesc_modified_id));
						modified_date = strToLongDate(crs.getString("followupdesc_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Follow-up Description Found!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
						+ " SET"
						+ " followupdesc_name = '" + followupdesc_name + "',"
						+ " followupdesc_modified_id = " + followupdesc_modified_id + ","
						+ " followupdesc_modified_date = '" + followupdesc_modified_date + "'"
						+ " WHERE followupdesc_id = " + followupdesc_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
						+ " WHERE followupdesc_id =" + followupdesc_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
