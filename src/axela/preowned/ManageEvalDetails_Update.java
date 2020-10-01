// Bhagwan Singh (10th July 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageEvalDetails_Update extends Connect {

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
	public String evaldetails_id = "0";
	public String evaldetails_name = "";
	public String evaldetails_rank = "";
	public String evaldetails_active = "";
	public String evaldetails_evalsubhead_id = "0";
	public String evaldetails_entry_id = "0";
	public String evaldetails_entry_by = "";
	public String evaldetails_entry_date = "";
	public String evaldetails_modified_id = "0";
	public String evaldetails_modified_by = "";
	public String evaldetails_modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				SOP("coming===");
				SOP("comp_id========" + comp_id);
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				evaldetails_id = CNumeric(PadQuotes(request.getParameter("evaldetails_id")));
				evaldetails_evalsubhead_id = CNumeric(PadQuotes(request.getParameter("evalsubhead_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						evaldetails_active = "1";
					} else {
						GetValues(request, response);
						evaldetails_entry_id = emp_id;
						evaldetails_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageevaldetails.jsp?msg=Evaluation Details Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Evaluation Detail".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Evaluation Detail".equals(deleteB)) {
						GetValues(request, response);
						evaldetails_modified_id = emp_id;
						evaldetails_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageevaldetails.jsp?msg=Evaluation Details Updated Successfully!"));
						}
					}
					if ("Delete Evaluation Detail".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageevaldetails.jsp?msg=Evaluation Details Deleted Successfully!"));
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
		SOP("GetValues=======");
		evaldetails_name = PadQuotes(request.getParameter("txt_evaldetails_name"));
		evaldetails_evalsubhead_id = PadQuotes(request.getParameter("dr_evaldetails_evalsubhead_id"));
		evaldetails_active = PadQuotes(request.getParameter("ch_evaldetails_active"));
		if (evaldetails_active.equals("on")) {
			evaldetails_active = "1";
		} else {
			evaldetails_active = "0";
		}
		evaldetails_entry_by = PadQuotes(request.getParameter("entry_by"));
		evaldetails_modified_by = PadQuotes(request.getParameter("modified_by"));
		evaldetails_entry_date = PadQuotes(request.getParameter("entry_date"));
		evaldetails_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (evaldetails_evalsubhead_id.equals("0")) {
			msg = "<br>Select Evaluation Sub Head!";
		}
		if (evaldetails_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		try {
			if (!evaldetails_name.equals("")) {
				StrSql = "SELECT evaldetails_name"
						+ " FROM " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " WHERE evaldetails_name = '" + evaldetails_name + "'"
						+ " AND evaldetails_evalsubhead_id =" + evaldetails_evalsubhead_id + ""
						+ " AND evaldetails_id != " + evaldetails_id + "";

				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Name Found! ";
				}
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
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " (evaldetails_name,"
						+ " evaldetails_evalsubhead_id,"
						+ " evaldetails_active,"
						+ " evaldetails_rank,"
						+ " evaldetails_entry_id,"
						+ " evaldetails_entry_date)"
						+ " VALUES("
						+ " '" + evaldetails_name + "',"
						+ " " + evaldetails_evalsubhead_id + ","
						+ " '" + evaldetails_active + "',"
						+ " (SELECT (COALESCE(MAX(evaldetails_rank), '0')+1) FROM " + compdb(comp_id) + "axela_preowned_eval_details AS Rank WHERE evaldetails_evalsubhead_id = "
						+ evaldetails_evalsubhead_id + "),"
						+ " " + evaldetails_entry_id + ","
						+ " '" + evaldetails_entry_date + "')";
				evaldetails_id = UpdateQueryReturnID(StrSql);
				// updateQuery(StrSql);
				SOP("AddFields======" + StrSqlBreaker(StrSql));
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT evaldetails_name, evaldetails_active, evaldetails_evalsubhead_id, evaldetails_entry_id,"
					+ " evaldetails_entry_date, evaldetails_modified_id, evaldetails_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval_details"
					+ " WHERE evaldetails_id = " + evaldetails_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					evaldetails_name = crs.getString("evaldetails_name");
					evaldetails_active = crs.getString("evaldetails_active");
					evaldetails_evalsubhead_id = crs.getString("evaldetails_evalsubhead_id");
					evaldetails_entry_id = crs.getString("evaldetails_entry_id");
					if (!evaldetails_entry_id.equals("")) {
						evaldetails_entry_by = Exename(comp_id, Integer.parseInt(evaldetails_entry_id));
					}
					evaldetails_entry_date = strToLongDate(crs.getString("evaldetails_entry_date"));
					evaldetails_modified_id = crs.getString("evaldetails_modified_id");
					if (!evaldetails_modified_id.equals("")) {
						evaldetails_modified_by = Exename(comp_id, Integer.parseInt(evaldetails_modified_id));
					}
					evaldetails_modified_date = strToLongDate(crs.getString("evaldetails_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Evaluation Details!"));
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
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " SET"
						+ " evaldetails_name = '" + evaldetails_name + "',"
						+ " evaldetails_active = '" + evaldetails_active + "',"
						+ " evaldetails_evalsubhead_id = " + evaldetails_evalsubhead_id + ","
						+ " evaldetails_modified_id = " + evaldetails_modified_id + ","
						+ " evaldetails_modified_date  = '" + evaldetails_modified_date + "'"
						+ " WHERE evaldetails_id = " + evaldetails_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " WHERE evaldetails_id = " + evaldetails_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateEvalSubHead() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT evalsubhead_id,  CONCAT(evalhead_name, ' - ', evalsubhead_name) AS evalsubhead_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_head ON evalhead_id = evalsubhead_evalhead_id"
					+ " ORDER BY evalsubhead_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("evalsubhead_id"));
				Str.append(StrSelectdrop(crs.getString("evalsubhead_id"), evaldetails_evalsubhead_id));
				Str.append(">").append(crs.getString("evalsubhead_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
