// smitha nag 28, 29 march 2013
package axela.insurance;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Target_Update extends Connect {
	public String msg = "";
	public String update = "";
	public String updateB = "";
	public String comp_id = "0";
	// public String branch_id = "0", dr_branch_id = "0";
	// public String branch_name = "";
	public String year = "";
	// public String target_id = "0";
	public String insurance_target_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String insurance_target_enquiry_count = "0", insurance_target_policy_count = "0";
	public String insurance_target_startdate = "";
	public String insurance_target_enddate = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String insurance_target_entry_id = "";
	public String insurance_target_entry_date = "";
	public String insurance_target_modified_id = "";
	public String insurance_target_modified_date = "";
	public String emp_id = "0";
	public String insurance_target_exe_id = "0";
	public String insurance_target_exe_name = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "service_target_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				insurance_target_exe_id = CNumeric(PadQuotes(request.getParameter("insurance_target_exe_id")));
				msg = PadQuotes(request.getParameter("msg"));
				insurance_target_exe_name = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id =" + insurance_target_exe_id + "");
				insurance_target_id = CNumeric(PadQuotes(request.getParameter("insurance_target_id")));
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
						insurance_target_id = CNumeric(ExecuteQuery("SELECT insurance_target_id"
								+ " FROM " + compdb(comp_id) + "axela_insurance_target"
								+ " WHERE  SUBSTR(insurance_target_startdate, 1, 6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
								+ " AND SUBSTR(insurance_target_enddate, 1, 6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
								+ " AND insurance_target_emp_id = " + insurance_target_exe_id + ""));
						if (insurance_target_id.equals("0")) {
							AddTargetFields();
						} else {
							UpdateTargetFields();
						}
						response.sendRedirect(response.encodeRedirectURL("insurance-target-list.jsp?dr_executives="
								+ insurance_target_exe_id + "&dr_year=" + year + "&msg=" + "Target Updated Successfully!"));
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	private void GetValues(HttpServletRequest request, HttpServletResponse response) {

		insurance_target_enquiry_count = CNumeric(PadQuotes(request.getParameter("txt_insurance_target_enquiry_count")));
		insurance_target_policy_count = CNumeric(PadQuotes(request.getParameter("txt_insurance_target_policy_count")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (insurance_target_enquiry_count.equals("0")) {
			msg = msg + "<br>Enter Enquiry Target!";
		}
		if (insurance_target_policy_count.equals("0")) {
			msg = msg + "<br>Enter Policy Target!";
		}
	}

	private void PopulateFields(HttpServletResponse response) {
		if (!insurance_target_id.equals("0")) {
			// SOP("service==" + service_target_id);
			StrSql = "SELECT insurance_target_id, insurance_target_emp_id,"
					+ " COALESCE(insurance_target_startdate, '') AS insurance_target_startdate,"
					+ " COALESCE(insurance_target_enddate, '') AS insurance_target_enddate,"
					+ " COALESCE(insurance_target_enquiry_count, 0) AS insurance_target_enquiry_count,"
					+ " COALESCE(insurance_target_policy_count, 0) AS insurance_target_policy_count,"
					+ " COALESCE(insurance_target_entry_date, '') AS insurance_target_entry_date,"
					+ " COALESCE(insurance_target_entry_id, '0') AS insurance_target_entry_id,"
					+ " COALESCE(insurance_target_modified_date, '') AS insurance_target_modified_date,"
					+ " COALESCE(insurance_target_modified_id, '0') AS insurance_target_modified_id";
			SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_target"
					+ " WHERE insurance_target_emp_id=" + insurance_target_exe_id + ""
					+ " AND SUBSTR(insurance_target_startdate, 1, 6) >= " + year + doublenum(Integer.parseInt(month))
					+ " AND SUBSTR(insurance_target_enddate, 1, 6)<=" + year + doublenum(Integer.parseInt(month));
			StrSql = StrSql + SqlJoin;
			// SOP("StrSql===pop=" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				while (crs.next()) {
					insurance_target_id = crs.getString("insurance_target_id");
					insurance_target_enquiry_count = crs.getString("insurance_target_enquiry_count");
					insurance_target_policy_count = crs.getString("insurance_target_policy_count");
					insurance_target_entry_id = crs.getString("insurance_target_entry_id");
					entry_by = Exename(comp_id, crs.getInt("insurance_target_entry_id"));
					entry_date = strToLongDate(crs.getString("insurance_target_entry_date"));
					insurance_target_modified_id = crs.getString("insurance_target_modified_id");
					if (!insurance_target_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(insurance_target_modified_id));
						modified_date = strToLongDate(crs.getString("insurance_target_modified_date"));
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
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_target"
					+ " ( "
					+ " insurance_target_emp_id,  insurance_target_startdate, insurance_target_enddate,"
					+ "	insurance_target_enquiry_count, insurance_target_policy_count, "
					+ " insurance_target_entry_id, insurance_target_entry_date ) "
					+ " VALUES "
					+ " (" + insurance_target_exe_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + days + "000000" + ","
					+ " " + insurance_target_enquiry_count + ","
					+ " " + insurance_target_policy_count + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			updateQuery(StrSql);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void UpdateTargetFields() throws SQLException {
		try {
			// SOP("Coming update....");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_target "
					+ " SET"
					+ " insurance_target_enquiry_count= " + insurance_target_enquiry_count + ", "
					+ " insurance_target_policy_count= " + insurance_target_policy_count + ", "
					+ " insurance_target_modified_id = " + emp_id + ", "
					+ " insurance_target_modified_date=" + ToLongDate(kknow()) + " "
					+ " WHERE insurance_target_id=" + insurance_target_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
