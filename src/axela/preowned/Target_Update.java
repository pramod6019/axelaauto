// smitha nag 28, 29 march 2013
package axela.preowned;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Update extends Connect {
	public String msg = "";
	public String update = "";
	public String updateB = "";
	public String comp_id = "0";
	// public String branch_id = "0", dr_branch_id = "0";
	// public String branch_name = "";
	public String year = "";
	// public String target_id = "0";
	public String preownedtarget_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String preownedtarget_enquiry_count = "0", preownedtarget_enquiry_eval_count = "0", preownedtarget_purchase_count = "0";
	public String preownedtarget_startdate = "";
	public String preownedtarget_enddate = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String preownedtarget_entry_id = "";
	public String preownedtarget_entry_date = "";
	public String preownedtarget_modified_id = "";
	public String preownedtarget_modified_date = "";
	public String emp_id = "0";
	public String preownedtarget_exe_id = "0";
	public String preownedtarget_exe_name = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_preowned_target_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				preownedtarget_exe_id = CNumeric(PadQuotes(request.getParameter("preownedtarget_exe_id")));
				msg = PadQuotes(request.getParameter("msg"));
				preownedtarget_exe_name = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id =" + preownedtarget_exe_id + "");
				preownedtarget_id = CNumeric(PadQuotes(request.getParameter("preownedtarget_id")));
				month = CNumeric(PadQuotes(request.getParameter("month")));
				month_name = TextMonth(Integer.parseInt(month) - 1);
				month_name = month_name.substring(0, 3);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				year = CNumeric(PadQuotes(request.getParameter("year")));

				if (update.equals("yes")) {
					status = "Update";
					PopulateFields(response);
				}
				if (updateB.equals("Update")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						preownedtarget_id = CNumeric(ExecuteQuery("SELECT preownedtarget_id"
								+ " FROM " + compdb(comp_id) + "axela_preowned_target"
								+ " WHERE  SUBSTR(preownedtarget_startdate, 1, 6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
								+ " AND SUBSTR(preownedtarget_enddate, 1, 6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
								+ " AND preownedtarget_emp_id = " + preownedtarget_exe_id + ""));
						if (preownedtarget_id.equals("0")) {
							AddTargetFields();
						} else {
							UpdateTargetFields();
						}
						response.sendRedirect(response.encodeRedirectURL("target-list.jsp?dr_executives=" + preownedtarget_exe_id
								+ "&dr_year=" + year + "&msg=" + "Target Updated Successfully!"));
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void GetValues(HttpServletRequest request, HttpServletResponse response) {
		preownedtarget_enquiry_count = CNumeric(PadQuotes(request.getParameter("txt_preownedtarget_enquiry_count")));
		preownedtarget_enquiry_eval_count = CNumeric(PadQuotes(request.getParameter("txt_preownedtarget_eval_count")));
		preownedtarget_purchase_count = CNumeric(PadQuotes(request.getParameter("txt_preownedtarget_purchase_count")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (preownedtarget_enquiry_count.equals("0")) {
			msg = msg + "<br>Enter Pre-Owned Enquiry Target!";
		}
		if (preownedtarget_enquiry_eval_count.equals("0")) {
			msg = msg + "<br>Enter Evaluation Target!";
		}
		if (preownedtarget_purchase_count.equals("0")) {
			msg = msg + "<br>Enter Purchase Target!";
		}
	}

	private void PopulateFields(HttpServletResponse response) {
		if (!preownedtarget_id.equals("0")) {
			// SOP("service==" + service_target_id);
			StrSql = "SELECT preownedtarget_id,"
					+ " preownedtarget_emp_id,"
					+ " COALESCE(preownedtarget_startdate, '') AS preownedtarget_startdate,"
					+ " COALESCE(preownedtarget_enddate, '') AS preownedtarget_enddate,"
					+ " COALESCE(preownedtarget_enquiry_count, 0) AS preownedtarget_enquiry_count,"
					+ " COALESCE(preownedtarget_enquiry_eval_count, 0) AS preownedtarget_enquiry_eval_count,"
					+ " COALESCE(preownedtarget_purchase_count, 0) AS preownedtarget_purchase_count,"
					+ " COALESCE(preownedtarget_entry_id, '0') AS preownedtarget_entry_id,"
					+ " COALESCE(preownedtarget_entry_date, '') AS preownedtarget_entry_date,"
					+ " COALESCE(preownedtarget_modified_id, '0') AS preownedtarget_modified_id,"
					+ " COALESCE(preownedtarget_modified_date, '') AS preownedtarget_modified_date";
			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_target"
					+ " WHERE preownedtarget_emp_id = " + preownedtarget_exe_id + ""
					+ " AND SUBSTR(preownedtarget_startdate, 1, 6) >= " + year + doublenum(Integer.parseInt(month))
					+ " AND SUBSTR(preownedtarget_enddate, 1, 6) <= " + year + doublenum(Integer.parseInt(month));
			StrSql = StrSql + SqlJoin;
			// SOP("StrSql===pop=" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				while (crs.next()) {
					preownedtarget_id = crs.getString("preownedtarget_id");
					preownedtarget_enquiry_count = crs.getString("preownedtarget_enquiry_count");
					preownedtarget_enquiry_eval_count = crs.getString("preownedtarget_enquiry_eval_count");
					preownedtarget_purchase_count = crs.getString("preownedtarget_purchase_count");
					preownedtarget_entry_id = crs.getString("preownedtarget_entry_id");
					entry_by = Exename(comp_id, crs.getInt("preownedtarget_entry_id"));
					entry_date = strToLongDate(crs.getString("preownedtarget_entry_date"));
					preownedtarget_modified_id = crs.getString("preownedtarget_modified_id");
					if (!preownedtarget_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(preownedtarget_modified_id));
						modified_date = strToLongDate(crs.getString("preownedtarget_modified_date"));
					}
				}

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void AddTargetFields() throws SQLException {
		try {
			// SOP("Coming....");
			int days = DaysInMonth(Integer.parseInt(year), Integer.parseInt(month));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_target"
					+ " ( "
					+ " preownedtarget_emp_id,"
					+ " preownedtarget_startdate,"
					+ " preownedtarget_enddate,"
					+ "	preownedtarget_enquiry_count,"
					+ " preownedtarget_enquiry_eval_count, "
					+ " preownedtarget_purchase_count, "
					+ " preownedtarget_entry_id,"
					+ " preownedtarget_entry_date"
					+ " ) "
					+ " VALUES "
					+ " ("
					+ preownedtarget_exe_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + days + "000000" + ","
					+ " " + preownedtarget_enquiry_count + ","
					+ " " + preownedtarget_enquiry_eval_count + ","
					+ " " + preownedtarget_purchase_count + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow())
					+ " )";
			updateQuery(StrSql);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UpdateTargetFields() throws SQLException {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_target "
					+ " SET"
					+ " preownedtarget_enquiry_count = " + preownedtarget_enquiry_count + ", "
					+ " preownedtarget_enquiry_eval_count = " + preownedtarget_enquiry_eval_count + ", "
					+ " preownedtarget_purchase_count = " + preownedtarget_purchase_count + ", "
					+ " preownedtarget_modified_id = " + emp_id + ", "
					+ " preownedtarget_modified_date =" + ToLongDate(kknow()) + " "
					+ " WHERE preownedtarget_id = " + preownedtarget_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
