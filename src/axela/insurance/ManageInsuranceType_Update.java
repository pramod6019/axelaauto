// Dilip Kumar (11 May 2013)
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInsuranceType_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String insurtype_id = "0";
	public String insurtype_name = "";
	public String insurtype_entry_id = "0";
	public String type_entry_by = "";
	public String insurtype_entry_date = "";
	public String insurtype_modified_id = "0";
	public String type_modified_by = "";
	public String insurtype_modified_date = "";

	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String QueryString = "";

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
				insurtype_id = CNumeric(PadQuotes(request.getParameter("insurtype_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (insurtype_id.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("index.jsp"));
					} else if (insurtype_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Type!"));
					}
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						insurtype_entry_id = emp_id;
						insurtype_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancetype.jsp?insurtype_id=" + insurtype_id + "&msg=Insurance Type added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Insurance Source".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Insurance Source".equals(deleteB)) {
						GetValues(request, response);
						insurtype_modified_id = emp_id;
						insurtype_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancetype.jsp?insurtype_id=" + insurtype_id + "&msg=Insurance Type updated Successfully!"));
						}
					}
					if ("Delete Insurance Source".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancetype.jsp?msg=Insurance Type deleted Successfully!"));
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
		insurtype_name = PadQuotes(request.getParameter("txt_insurtype_name"));
		type_entry_by = PadQuotes(request.getParameter("entry_by"));
		type_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (insurtype_name.equals("")) {
			msg = "<br>Enter Insurance Type Name!";
		}
		try {
			if (!insurtype_name.equals("")) {
				StrSql = "Select insurtype_name from " + compdb(comp_id) + "axela_insurance_type where insurtype_name = '" + insurtype_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and insurtype_id != " + insurtype_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Insurance Type Found! ";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				insurtype_id = ExecuteQuery("Select max(insurtype_id) as insurtype_id from " + compdb(comp_id) + "axela_insurance_type");
				if (insurtype_id == null || insurtype_id.equals("")) {
					insurtype_id = "0";
				}
				int insurtype_idi = Integer.parseInt(insurtype_id) + 1;
				insurtype_id = "" + insurtype_idi;

				StrSql = "Insert into " + compdb(comp_id) + "axela_insurance_type"
						+ " (insurtype_id,"
						+ " insurtype_name,"
						+ " insurtype_entry_id,"
						+ " insurtype_entry_date)"
						+ " values"
						+ " (" + insurtype_id + ","
						+ " '" + insurtype_name + "',"
						+ " " + insurtype_entry_id + ","
						+ " '" + insurtype_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select *"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " where insurtype_id = " + insurtype_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurtype_name = crs.getString("insurtype_name");
					insurtype_entry_id = crs.getString("insurtype_entry_id");
					if (!insurtype_entry_id.equals("")) {
						type_entry_by = Exename(comp_id, Integer.parseInt(insurtype_entry_id));
					}
					entry_date = strToLongDate(crs.getString("insurtype_entry_date"));
					insurtype_modified_id = crs.getString("insurtype_modified_id");
					if (!insurtype_modified_id.equals("")) {
						type_modified_by = Exename(comp_id, Integer.parseInt(insurtype_modified_id));
					}
					modified_date = strToLongDate(crs.getString("insurtype_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Type!"));
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
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_type"
						+ " SET"
						+ " insurtype_name = '" + insurtype_name + "',"
						+ " insurtype_modified_id = " + insurtype_modified_id + ","
						+ " insurtype_modified_date  = '" + insurtype_modified_date + "'"
						+ " where insurtype_id = " + insurtype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		// StrSql = "SELECT Insurance_insurtype_id FROM " + compdb(comp_id) + "axela_insurance_insurance where Insurance_insurtype_id = " + insurtype_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Type is Associated with Insurance!";
		// }
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_insurance_type where insurtype_id = " + insurtype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
