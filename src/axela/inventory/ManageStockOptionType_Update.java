// Bhagwan Singh (10 july 2013)
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageStockOptionType_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String optiontype_id = "0";
	public String optiontype_name = "";
	public String checkperm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";

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
				optiontype_id = CNumeric(PadQuotes(request.getParameter("optiontype_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestockoptiontype.jsp?optiontype_id=" + optiontype_id + "&msg=Stock Option Type Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete OptionType".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete OptionType".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestockoptiontype.jsp?optiontype_id=" + optiontype_id + "&msg=Stock Option Type Updated Successfully!"));
						}
					} else if ("Delete OptionType".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestockoptiontype.jsp?msg=Stock Option Type Deleted Successfully!"));
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
		optiontype_id = PadQuotes(request.getParameter("optiontype_id"));
		optiontype_name = PadQuotes(request.getParameter("txt_optiontype_name"));
	}

	protected void CheckForm() {

		if (optiontype_name.equals("")) {
			msg = msg + "<br>Enter Stock Option Type!";
		}
		try {
			if (!optiontype_name.equals("")) {
				StrSql = "SELECT optiontype_name"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option_type"
						+ " WHERE optiontype_name = '" + optiontype_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND optiontype_id != " + optiontype_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Stock Option Type Found";
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
				optiontype_id = ExecuteQuery("SELECT (COALESCE(MAX(optiontype_id), 0) +1) FROM " + compdb(comp_id) + "axela_vehstock_option_type");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_type"
						+ " (optiontype_id,"
						+ " optiontype_name)"
						+ " VALUES"
						+ " (" + optiontype_id + ","
						+ " '" + optiontype_name + "')";
				updateQuery(StrSql);
				// SOP(StrSqlBreaker(StrSql));
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT optiontype_id, optiontype_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option_type"
					+ " WHERE optiontype_id = " + optiontype_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					optiontype_name = crs.getString("optiontype_name");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?&msg=Invalid Stock Option Type!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_option_type"
						+ " SET"
						+ " optiontype_name = '" + optiontype_name + "'"
						+ " WHERE optiontype_id = " + optiontype_id + "";
				SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void DeleteFields() {
		StrSql = "SELECT option_optiontype_id"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " WHERE option_optiontype_id = " + optiontype_id + "";
		SOP("StrSql===" + StrSql);
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Type is Associated with Stock Option!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_type "
						+ " WHERE optiontype_id = " + optiontype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
