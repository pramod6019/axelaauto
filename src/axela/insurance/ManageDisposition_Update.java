package axela.insurance;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageDisposition_Update extends Connect {

	public String add = "", emp_id = "", branch_id = "", BranchAccess = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String comp_id = "0";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_role_id = "0";
	public String rank = "", insudisposition_parent_id = "";
	public String insurdisposition_id = "";
	public String disposition_id = "", disposition_name = "", disposition_instruction = "";

	public String mandatory = "";
	public String comments = "";
	public String followup = "";
	public String active = "1";

	public String entry_id = "", entry_by = "";
	public String entry_date = "";
	public String modified_id = "", modified_by = "";
	public String modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_disposition_configurator_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				// // SOP("updateB===" + updateB);
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				insurdisposition_id = PadQuotes(request.getParameter("insurdisposition_id"));
				insudisposition_parent_id = PadQuotes(request.getParameter("insurdisposition_parent_id"));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Disposition".equals(addB)) {
						disposition_name = "";

					} else {
						CheckPerm(comp_id, "emp_insurance_disposition_configurator_access", request, response);
						GetValues(request, response);
						entry_id = emp_id;
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-disposition.jsp?insurdisposition_parent_id=" + insudisposition_parent_id + "&insurdisposition_id="
									+ CNumeric(disposition_id) + "&msg=Disposition added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Disposition".equals(updateB) && !"Delete Disposition".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Disposition".equals(updateB) && !"Delete Disposition".equals(deleteB)) {
						CheckPerm(comp_id, "emp_insurance_disposition_configurator_access", request, response);
						GetValues(request, response);
						modified_id = emp_id;
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-disposition.jsp?insurdisposition_parent_id=" + insudisposition_parent_id + "&insurdisposition_id="
									+ CNumeric(insurdisposition_id) + "&msg=Disposition updated successfully!" + msg + ""));
						}
					} else if ("Delete Disposition".equals(deleteB)) {

						CheckPerm(comp_id, "emp_insurance_disposition_configurator_access", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-disposition.jsp?insurdisposition_parent_id=" + insudisposition_parent_id + "&insurdisposition_id="
									+ CNumeric(insurdisposition_id) + "&msg=Disposition deleted successfully!" + msg + ""));
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
		disposition_name = PadQuotes(request.getParameter("txt_disp_name"));// name
		insudisposition_parent_id = PadQuotes(request.getParameter("txt_disp_parent"));// it is parent_disposition_id
		// type = PadQuotes(request.getParameter("dr_type"));
		mandatory = PadQuotes(request.getParameter("chk_mandatory"));
		comments = PadQuotes(request.getParameter("chk_comments"));
		followup = PadQuotes(request.getParameter("chk_followup"));
		active = PadQuotes(request.getParameter("chk_active"));
		disposition_instruction = PadQuotes(request.getParameter("txt_instruction"));

		if (mandatory.equals("on")) {
			mandatory = "1";
		} else {
			mandatory = "0";
		}

		if (comments.equals("on")) {
			comments = "1";
		} else {
			comments = "0";
		}

		if (followup.equals("on")) {
			followup = "1";
		} else {
			followup = "0";
		}

		if (active.equals("on")) {
			active = "1";
		} else {
			active = "0";
		}

	}

	protected void CheckForm() {
		msg = "";
		if (disposition_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}

		try {
			StrSql = "SELECT insurdisposition_name from " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_name = '" + disposition_name + "'"
					+ " AND insurdisposition_parent_id = " + insudisposition_parent_id + "";
			if (update.equals("yes")) {
				StrSql = StrSql + " and insurdisposition_id != " + CNumeric(insurdisposition_id) + "";
			}
			ResultSet rsuname = processQuery(StrSql, 0);
			if (rsuname.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
			rsuname.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				rank = CNumeric(PadQuotes(ExecuteQuery("SELECT MAX(insurdisposition_rank+1) "
						+ " FROM " + compdb(comp_id) + "axela_insurance_disposition AS disposition_rank"
						+ " WHERE insurdisposition_parent_id = " + insudisposition_parent_id + "")));
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_disposition"
						+ " ( "
						+ " insurdisposition_name, "
						+ " insurdisposition_parent_id, "
						+ " insurdisposition_mandatory, "
						+ " insurdisposition_comments, "
						+ " insurdisposition_instructions, "
						+ " insurdisposition_nextfollowup, "
						+ " insurdisposition_active,"
						+ " insurdisposition_rank,"
						+ " insurdisposition_entry_id,"
						+ " insurdisposition_entry_date)"
						+ " VALUES"
						+ " ('" + disposition_name + "',"
						+ " " + insudisposition_parent_id + ","
						+ " '" + mandatory + "',"
						+ " '" + comments + "',"
						+ " '" + disposition_instruction + "',"
						+ " '" + followup + "',"
						+ " '" + active + "',"
						+ " '" + rank + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "')";

				// SOP("Add===" + StrSql);
				disposition_id = UpdateQueryReturnID(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "SELECT insurdisposition_name, insurdisposition_parent_id, "// parent_id is changing to 0 on update
					+ " insurdisposition_mandatory, insurdisposition_comments, insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup, insurdisposition_active,"
					+ " insurdisposition_entry_id, insurdisposition_entry_date,"
					+ " insurdisposition_modified_id, insurdisposition_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition "
					+ " WHERE insurdisposition_id = " + CNumeric(insurdisposition_id);

			// // SOP("PopulateFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					disposition_name = crs.getString("insurdisposition_name");
					insudisposition_parent_id = crs.getString("insurdisposition_parent_id");
					mandatory = crs.getString("insurdisposition_mandatory");
					comments = crs.getString("insurdisposition_comments");
					disposition_instruction = crs.getString("insurdisposition_instructions");
					followup = crs.getString("insurdisposition_nextfollowup");
					active = crs.getString("insurdisposition_active");
					entry_id = crs.getString("insurdisposition_entry_id");
					entry_date = crs.getString("insurdisposition_entry_date");
					modified_id = crs.getString("insurdisposition_modified_id");
					modified_date = crs.getString("insurdisposition_modified_date");
					if (!entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(entry_id));
						entry_date = strToLongDate(crs.getString("insurdisposition_entry_date"));
					}
					if (!modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(modified_id));
						modified_date = strToLongDate(crs.getString("insurdisposition_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Disposition!"));

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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_disposition"
						+ " SET"
						+ " insurdisposition_name = '" + disposition_name + "',"
						+ " insurdisposition_parent_id = " + insudisposition_parent_id + ","
						+ " insurdisposition_mandatory = '" + mandatory + "',"
						+ " insurdisposition_comments = '" + comments + "',"
						+ " insurdisposition_instructions = '" + disposition_instruction + "',"
						+ " insurdisposition_nextfollowup = '" + followup + "',"
						+ " insurdisposition_active = '" + active + "',"
						+ " insurdisposition_modified_id = '" + emp_id + "',"
						+ " insurdisposition_modified_date = '" + ToLongDate(kknow()) + "'"
						+ " WHERE insurdisposition_id = " + CNumeric(insurdisposition_id) + "";

				// // SOP("Update==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (disposition_id.equals("1")) {
			msg = "<br>Cannot Delete First Record!";
			return;
		}

		// Category association
		StrSql = "SELECT insurdisposition_id FROM " + compdb(comp_id) + "axela_insurance_disposition"
				+ " WHERE insurdisposition_parent_id = " + CNumeric(insurdisposition_id) + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category has Sub Category(s)!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_disposition"
						+ " WHERE insurdisposition_id = " + CNumeric(insurdisposition_id) + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateParentDisposition(String disposition_parentid, String active) {// could not write a query properly
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			StrSql = "SELECT insurdisposition_id, insurdisposition_name "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1=1";

			if (!active.equals("")) {
				StrSql = StrSql + " AND insurdisposition_active = " + active + "";
			}
			StrSql = StrSql + " ORDER BY insurdisposition_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurdisposition_id"));
				Str.append(StrSelectdrop(crs.getString("insurdisposition_id"), insudisposition_parent_id));
				Str.append(">").append(crs.getString("insurdisposition_name"));
				Str.append(":</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
}