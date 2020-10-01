package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCrmConcern_Update extends Connect {

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
	public String branch_id = "0";
	public String crmtype_id = "0", crmconcern_id = "0";
	public String crmconcern_desc = "";
	public String crmconcern_crmtype_id = "0";
	public String QueryString = "";
	public String crmconcern_entry_id = "0";
	public String crmconcern_entry_date = "";
	public String crmconcern_modified_id = "0";
	public String crmconcern_modified_date = "";
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
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				crmconcern_id = CNumeric(PadQuotes(request.getParameter("crmconcern_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						crmconcern_desc = "";
					} else {
						GetValues(request, response);
						crmconcern_entry_id = emp_id;
						crmconcern_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecrmconcern.jsp?crmconcern_id=" + crmconcern_id + "&msg=CRM Concern Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete CRM Concern".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete CRM Concern".equals(deleteB)) {
						GetValues(request, response);
						crmconcern_modified_id = emp_id;
						crmconcern_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecrmconcern.jsp?crmconcern_id=" + crmconcern_id + "&msg=CRM Concern Updated Successfully!"));
						}
					} else if ("Delete CRM Concern".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecrmconcern.jsp?msg=CRM Concern Deleted Successfully!"));
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
		crmconcern_id = CNumeric(PadQuotes(request.getParameter("crmconcern_id")));
		crmconcern_desc = PadQuotes(request.getParameter("txt_crmconcern_desc"));
		crmconcern_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmtype")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (crmconcern_crmtype_id.equals("0")) {
				msg = msg + "<br>Select the CRM Type!";
			}
			if (crmconcern_desc.equals("")) {
				msg = msg + "<br>Enter the CRM Concern Description!";
			}
			if (!crmconcern_desc.equals("")) {
				StrSql = "SELECT crmconcern_desc from " + compdb(comp_id) + "axela_sales_crm_concern"
						+ " WHERE crmconcern_desc = '" + crmconcern_desc + "'"
						+ " AND crmconcern_crmtype_id = " + crmconcern_crmtype_id;
				if (update.equals("yes")) {
					StrSql = StrSql + " AND crmconcern_id != " + crmconcern_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar CRM Concern Description Found! ";
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
				crmconcern_id = ExecuteQuery("SELECT (COALESCE(MAX(crmconcern_id),0)+1) FROM " + compdb(comp_id) + "axela_sales_crm_concern");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crm_concern"
						+ " (crmconcern_id,"
						+ " crmconcern_crmtype_id,"
						+ " crmconcern_desc,"
						+ " crmconcern_entry_id,"
						+ " crmconcern_entry_date)"
						+ " VALUES"
						+ " (" + crmconcern_id + ","
						+ " " + crmconcern_crmtype_id + ","
						+ " '" + crmconcern_desc + "',"
						+ " " + crmconcern_entry_id + ","
						+ " '" + crmconcern_entry_date + "')";
				// SOP("StrSql=add=" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_sales_crm_concern"
					+ " WHERE crmconcern_id = " + crmconcern_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					crmconcern_crmtype_id = crs.getString("crmconcern_crmtype_id");
					crmconcern_desc = crs.getString("crmconcern_desc");
					crmconcern_entry_id = crs.getString("crmconcern_entry_id");
					if (!crmconcern_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(crmconcern_entry_id));
						entry_date = strToLongDate(crs.getString("crmconcern_entry_date"));
					}
					crmconcern_modified_id = crs.getString("crmconcern_modified_id");
					if (!crmconcern_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(crmconcern_modified_id));
						modified_date = strToLongDate(crs.getString("crmconcern_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid CRM Concern!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_concern"
						+ " SET"
						+ " crmconcern_crmtype_id = " + crmconcern_crmtype_id + ","
						+ " crmconcern_desc = '" + crmconcern_desc + "',"
						+ " crmconcern_modified_id = " + crmconcern_modified_id + ","
						+ " crmconcern_modified_date = '" + crmconcern_modified_date + "'"
						+ " WHERE crmconcern_id = " + crmconcern_id;
				SOP("SqlStr===" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "Delete from " + compdb(comp_id) + "axela_sales_crm_concern WHERE crmconcern_id =" + crmconcern_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCRMType() {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT crmtype_name, crmtype_id"
					+ " FROM axela_sales_crm_type";
			SqlStr += " ORDER BY crmtype_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select CRM Type</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("crmtype_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("crmtype_id"), crmconcern_crmtype_id));
				stringval.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
}
