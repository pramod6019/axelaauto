package axela.service;

/**
 * @author Gurumurthy TS 1 APRIL 2013
 */
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Man_Hours extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String addB = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String bay_name = "";
	public String bay_branch_id = "0";
	public String baytrans_start_entry_id = "0";
	public String baytrans_start_time = "";
	public String baytrans_bay_id = "0";
	public String baytrans_jc_id = "0";
	public String baytrans_emp_id = "0";
	public String baytrans_end_time = "";
	public String baytrans_end_entry_id = "0";
	public String start = "";
	String jc_branch_id = "", jc_time_ready = "", jc_emp_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				addB = PadQuotes(request.getParameter("start_button"));
				updateB = PadQuotes(request.getParameter("Stop_button"));
				bay_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));

				if (addB.equals("START")) {
					GetValues(request, response);
					baytrans_start_entry_id = emp_id;
					baytrans_start_time = ToLongDate(kknow());
					CheckForm();
					if (msg.equals("")) {
						StrSql = "Update " + compdb(comp_id) + "axela_service_jc_bay_trans "
								+ " SET baytrans_end_time = '" + ToLongDate(kknow()) + "' "
								+ " where baytrans_end_time = '' and baytrans_emp_id = " + baytrans_emp_id + "";
						updateQuery(StrSql);
						AddFields();
					}
					if (!msg.equals("") && !addB.equals("START")) {
						msg = "Error!" + msg;
					}
				}
				if (updateB.equals("STOP")) {
					GetValues(request, response);
					baytrans_jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
					baytrans_end_entry_id = emp_id;
					baytrans_end_time = ToLongDate(kknow());
					UpdateFields();
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

	public void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		baytrans_bay_id = CNumeric(PadQuotes(request.getParameter("dr_baytrans_bay_id")));
		baytrans_jc_id = CNumeric(PadQuotes(request.getParameter("txt_baytrans_jc_id")));
		baytrans_emp_id = CNumeric(request.getParameter("txt_baytrans_emp_id"));
	}

	public void CheckForm() {
		if (bay_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
		if (baytrans_bay_id.equals("0")) {
			msg = msg + "<br>Select Bay!";
		}
		if (baytrans_jc_id.equals("0") && addB.equals("START")) {
			msg = msg + "<br>Enter Job Card ID!";
		}
		if (baytrans_emp_id.equals("0")) {
			msg = msg + "<br>Enter User ID!";
		}
		if (!baytrans_jc_id.equals("0")) {
			try {
				StrSql = "Select jc_branch_id, jc_time_ready, "
						+ " COALESCE((select emp_id from " + compdb(comp_id) + "axela_emp where emp_id=" + baytrans_emp_id + "),0) as emp_id  "
						+ " from " + compdb(comp_id) + "axela_service_jc "
						+ " where jc_id = " + baytrans_jc_id;
				// SOP("StrSql===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					jc_branch_id = crs.getString("jc_branch_id");
					jc_time_ready = crs.getString("jc_time_ready");
					jc_emp_id = crs.getString("emp_id");
				}
				// SOP("jc_branch_id=="+jc_branch_id);
				// SOP("jc_emp_id=="+jc_emp_id);
				// SOP("jc_emp_id=="+jc_emp_id);
				crs.close();
			} catch (SQLException ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

			if (!bay_branch_id.equals(jc_branch_id)) {
				msg = msg + "<br>Invalid Branch for Job Card!";
			}
			if (addB.equals("START")) {
				if (!jc_time_ready.equals("")) {
					msg = msg + "<br>Job Card is already Ready!";
				}
			}
			if (jc_emp_id.equals("0")) {
				msg = msg + "<br>Invalid User for Job Card!";
			}
		}
	}

	public void AddFields() {
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " (baytrans_bay_id,"
					+ " baytrans_jc_id,"
					+ " baytrans_emp_id,"
					+ " baytrans_start_time,"
					+ " baytrans_end_time,"
					+ " baytrans_start_entry_id,"
					+ " baytrans_end_entry_id)"
					+ " values "
					+ " (" + baytrans_bay_id + ","
					+ " " + baytrans_jc_id + ","
					+ " " + baytrans_emp_id + ","
					+ " '" + baytrans_start_time + "',"
					+ " '',"
					+ " " + baytrans_start_entry_id + ","
					+ " 0)";
			updateQuery(StrSql);
			msg = "";
			msg = "<br>Man Hours Stamped!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_bay_trans"
						+ " SET "
						+ " baytrans_end_entry_id = " + baytrans_end_entry_id + ","
						+ " baytrans_end_time = '" + baytrans_end_time + "'"
						+ " WHERE baytrans_end_time = ''"
						+ " AND baytrans_emp_id = " + baytrans_emp_id + "";
				if (!baytrans_jc_id.equals("0")) {
					StrSql = StrSql + " and baytrans_jc_id = " + baytrans_jc_id + "";
				}
				updateQuery(StrSql);
				msg = msg + "<br>Man Hours Stamped!";
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_bay ON bay_branch_id = branch_id"
					+ " WHERE branch_active = '1' and bay_active = '1'"
					+ " GROUP BY  branch_id"
					+ " ORDER BY  branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), bay_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBay() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bay_id, bay_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay "
					+ " WHERE bay_active = '1'"
					+ " AND bay_branch_id = " + bay_branch_id
					+ " GROUP BY bay_id "
					+ " ORDER BY bay_rank ";
			// SOP("StrSql main = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bay_id")).append("");
				Str.append(StrSelectdrop(crs.getString("bay_id"), baytrans_bay_id));
				Str.append(">").append(crs.getString("bay_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
