package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJCPSFConcern_Update extends Connect {

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
	public String jcpsfconcern_id = "0";
	public String jcpsfconcern_desc = "";
	public String QueryString = "";
	public String jcpsfconcern_entry_id = "0";
	public String jcpsfconcern_entry_date = "";
	public String jcpsfconcern_modified_id = "0";
	public String jcpsfconcern_modified_date = "";
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
				jcpsfconcern_id = CNumeric(PadQuotes(request.getParameter("jcpsfconcern_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						jcpsfconcern_desc = "";
					} else {
						GetValues(request, response);
						jcpsfconcern_entry_id = emp_id;
						jcpsfconcern_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejcpsfconcern.jsp?jcpsfconcern_id=" + jcpsfconcern_id + "&msg=JC PSF Concern Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete JC PSF Concern".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete JC PSF Concern".equals(deleteB)) {
						GetValues(request, response);
						jcpsfconcern_modified_id = emp_id;
						jcpsfconcern_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejcpsfconcern.jsp?jcpsfconcern_id=" + jcpsfconcern_id + "&msg=JC PSF Concern Updated Successfully!"));
						}
					} else if ("Delete JC PSF Concern".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managejcpsfconcern.jsp?msg=JC PSF Concern Deleted Successfully!"));
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
		jcpsfconcern_id = CNumeric(PadQuotes(request.getParameter("jcpsfconcern_id")));
		jcpsfconcern_desc = PadQuotes(request.getParameter("txt_jcpsfconcern_desc"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (jcpsfconcern_desc.equals("")) {
				msg = msg + "<br>Enter the JC PSF Concern Description!";
			}
			if (!jcpsfconcern_desc.equals("")) {
				StrSql = "SELECT jcpsfconcern_desc FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
						+ " WHERE jcpsfconcern_desc = '" + jcpsfconcern_desc + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND jcpsfconcern_id != " + jcpsfconcern_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar JC PSF Concern Description Found! ";
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
				jcpsfconcern_id = ExecuteQuery("SELECT (COALESCE(MAX(jcpsfconcern_id),0)+1) FROM " + compdb(comp_id) + "axela_service_jc_psf_concern");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf_concern"
						+ " (jcpsfconcern_id,"
						+ " jcpsfconcern_desc,"
						+ " jcpsfconcern_entry_id,"
						+ " jcpsfconcern_entry_date)"
						+ " VALUES"
						+ " (" + jcpsfconcern_id + ","
						+ " '" + jcpsfconcern_desc + "',"
						+ " " + jcpsfconcern_entry_id + ","
						+ " '" + jcpsfconcern_entry_date + "')";
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
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
					+ " WHERE jcpsfconcern_id = " + jcpsfconcern_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					jcpsfconcern_desc = crs.getString("jcpsfconcern_desc");
					jcpsfconcern_entry_id = crs.getString("jcpsfconcern_entry_id");
					if (!jcpsfconcern_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(jcpsfconcern_entry_id));
						entry_date = strToLongDate(crs.getString("jcpsfconcern_entry_date"));
					}
					jcpsfconcern_modified_id = crs.getString("jcpsfconcern_modified_id");
					if (!jcpsfconcern_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(jcpsfconcern_modified_id));
						modified_date = strToLongDate(crs.getString("jcpsfconcern_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid JC PSF Concern!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_concern"
						+ " SET"
						+ " jcpsfconcern_desc = '" + jcpsfconcern_desc + "',"
						+ " jcpsfconcern_modified_id = " + jcpsfconcern_modified_id + ","
						+ " jcpsfconcern_modified_date = '" + jcpsfconcern_modified_date + "'"
						+ " WHERE jcpsfconcern_id = " + jcpsfconcern_id;
				// SOP("SqlStr===" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf_concern WHERE jcpsfconcern_id = " + jcpsfconcern_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
