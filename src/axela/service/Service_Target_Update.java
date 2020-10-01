// smitha nag 28, 29 march 2013
package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Target_Update extends Connect {
	public String msg = "";
	public String update = "";
	public String updateB = "";
	public String comp_id = "0";
	// public String branch_id = "0", dr_branch_id = "0";
	// public String branch_name = "";
	public String year = "";
	// public String target_id = "0";
	public String service_target_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String service_target_jc_count = "0", service_target_pms_count = "0";
	public String service_target_labour_amount = "0";
	public String service_target_parts_amount = "0";
	public String service_target_oil_amount = "0";
	public String service_target_tyre_amount = "0";
	public String service_target_break_count = "0";
	public String service_target_break_amount = "0";
	public String service_target_battery_amount = "0";
	public String service_target_accessories_amount = "0";
	public String service_target_vas_amount = "0", service_target_extwarranty_amount = "0";
	public String service_target_battery_count = "0", service_target_extwarranty_count = "0", service_target_tyre_count = "0";
	public String service_target_wheelalignment_amount = "0", service_target_cng_amount = "0";
	public String service_target_startdate = "";
	public String service_target_enddate = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String StrSearch = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String service_target_entry_id = "";
	public String service_target_entry_date = "";
	public String service_target_modified_id = "";
	public String service_target_modified_date = "";
	public String emp_id = "";
	public String Service_target_emp_id = "";
	public String emp_name = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "service_target_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				Service_target_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				msg = PadQuotes(request.getParameter("msg"));
				emp_name = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id=" + Service_target_emp_id + "");
				service_target_id = CNumeric(PadQuotes(request.getParameter("service_target_id")));
				month = CNumeric(PadQuotes(request.getParameter("month")));
				month_name = TextMonth(Integer.parseInt(month) - 1);
				month_name = month_name.substring(0, 3);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				year = CNumeric(PadQuotes(request.getParameter("year")));

				if (update.equals("yes")) {
					// SOP("yes");
					status = "Update";
					PopulateFields(response);

				}
				if (updateB.equals("Update")) {
					GetValues(request, response);

					service_target_modified_id = CNumeric(GetSession("emp_id", request));
					service_target_modified_date = ToLongDate(kknow());
					UpdateFields(request);

					response.sendRedirect(response.encodeRedirectURL("service-target-list.jsp?dr_executives=" + Service_target_emp_id + "&dr_year=" + year + "&msg=" + "Target Updated Successfully!"));
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	private void GetValues(HttpServletRequest request,
			HttpServletResponse response) {
		service_target_jc_count = CNumeric(PadQuotes(request.getParameter("txt_jc_count")));
		service_target_pms_count = CNumeric(PadQuotes(request.getParameter("txt_pms_count")));
		service_target_labour_amount = CNumeric(PadQuotes(request.getParameter("txt_labour_amt")));
		service_target_parts_amount = CNumeric(PadQuotes(request.getParameter("txt_parts_amt")));
		service_target_oil_amount = CNumeric(PadQuotes(request.getParameter("txt_oil_amt")));
		service_target_tyre_count = CNumeric(PadQuotes(request.getParameter("txt_tyre_count")));
		service_target_tyre_amount = CNumeric(PadQuotes(request.getParameter("txt_tyre_amt")));
		service_target_break_count = CNumeric(PadQuotes(request.getParameter("txt_break_count")));
		service_target_break_amount = CNumeric(PadQuotes(request.getParameter("txt_break_amt")));
		service_target_battery_count = CNumeric(PadQuotes(request.getParameter("txt_battery_count")));
		service_target_battery_amount = CNumeric(PadQuotes(request.getParameter("txt_battery_amt")));
		service_target_accessories_amount = CNumeric(PadQuotes(request.getParameter("txt_accessories_amt")));
		service_target_vas_amount = CNumeric(PadQuotes(request.getParameter("txt_vas_amt")));
		service_target_extwarranty_count = CNumeric(PadQuotes(request.getParameter("txt_extwarranty_count")));
		service_target_extwarranty_amount = CNumeric(PadQuotes(request.getParameter("txt_extwarranty_amt")));
		service_target_wheelalignment_amount = CNumeric(PadQuotes(request.getParameter("txt_wheelalignment_amt")));
		service_target_cng_amount = CNumeric(PadQuotes(request.getParameter("txt_cng_amt")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	private void PopulateFields(HttpServletResponse response) {
		if (!service_target_id.equals("0")) {
			// SOP("service==" + service_target_id);
			StrSql = "SELECT service_target_id, service_target_emp_id,"
					+ " COALESCE(service_target_startdate, '') AS service_target_startdate,"
					+ " COALESCE(service_target_enddate, '') AS service_target_enddate,"
					+ " COALESCE(service_target_jc_count, 0) AS service_target_jc_count,"
					+ " COALESCE(service_target_pms_count, 0) AS service_target_pms_count,"
					+ " COALESCE(service_target_labour_amount, 0) AS service_target_labour_amount,"
					+ " COALESCE(service_target_parts_amount, 0) AS service_target_parts_amount,"
					+ " COALESCE(service_target_oil_amount, 0) AS service_target_oil_amount,"
					+ " COALESCE(service_target_tyre_count, 0) AS service_target_tyre_count,"
					+ " COALESCE(service_target_tyre_amount, 0) AS service_target_tyre_amount,"
					+ " COALESCE(service_target_break_count, 0) AS service_target_break_count,"
					+ " COALESCE(service_target_break_amount, 0) AS service_target_break_amount,"
					+ " COALESCE(service_target_battery_count, 0) AS service_target_battery_count,"
					+ " COALESCE(service_target_battery_amount, 0) AS service_target_battery_amount,"
					+ " COALESCE(service_target_accessories_amount, 0) AS service_target_accessories_amount,"
					+ " COALESCE(service_target_vas_amount, 0) AS service_target_vas_amount,"
					+ " COALESCE(service_target_extwarranty_count, 0) AS service_target_extwarranty_count,"
					+ " COALESCE(service_target_extwarranty_amount, 0) AS service_target_extwarranty_amount,"
					+ " COALESCE(service_target_wheelalign_amount, 0) AS service_target_wheelalign_amount,"
					+ " COALESCE(service_target_cng_amount, 0) AS service_target_cng_amount,"
					+ " COALESCE(service_target_entry_date, '') AS service_target_entry_date,"
					+ " COALESCE(service_target_entry_id, '0') AS service_target_entry_id,"
					+ " COALESCE(service_target_modified_date, '') AS service_target_modified_date,"
					+ " COALESCE(service_target_modified_id, '0') AS service_target_modified_id";
			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_target"
					+ " WHERE service_target_emp_id=" + Service_target_emp_id + ""
					+ " AND SUBSTR(service_target_startdate, 1, 6) >= " + year + doublenum(Integer.parseInt(month))
					+ " AND SUBSTR(service_target_enddate, 1, 6)<=" + year + doublenum(Integer.parseInt(month));
			StrSql = StrSql + SqlJoin;
			// SOP("StrSql===pop=" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				while (crs.next()) {
					service_target_id = crs.getString("service_target_id");
					service_target_jc_count = crs.getString("service_target_jc_count");
					service_target_pms_count = crs.getString("service_target_pms_count");
					service_target_labour_amount = crs.getString("service_target_labour_amount");
					service_target_parts_amount = crs.getString("service_target_parts_amount");
					service_target_oil_amount = crs.getString("service_target_oil_amount");
					service_target_tyre_count = crs.getString("service_target_tyre_count");
					service_target_tyre_amount = crs.getString("service_target_tyre_amount");
					service_target_break_count = crs.getString("service_target_break_count");
					service_target_break_amount = crs.getString("service_target_break_amount");
					service_target_battery_count = crs.getString("service_target_battery_count");
					service_target_battery_amount = crs.getString("service_target_battery_amount");
					service_target_accessories_amount = crs.getString("service_target_accessories_amount");
					service_target_vas_amount = crs.getString("service_target_vas_amount");
					service_target_extwarranty_count = crs.getString("service_target_extwarranty_count");
					service_target_extwarranty_amount = crs.getString("service_target_extwarranty_amount");
					service_target_wheelalignment_amount = crs.getString("service_target_wheelalign_amount");
					service_target_cng_amount = crs.getString("service_target_cng_amount");
					service_target_entry_id = crs.getString("service_target_entry_id");
					entry_by = Exename(comp_id, crs.getInt("service_target_entry_id"));
					entry_date = strToLongDate(crs.getString("service_target_entry_date"));
					service_target_modified_id = crs.getString("service_target_modified_id");
					if (!service_target_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(service_target_modified_id));
						modified_date = strToLongDate(crs.getString("service_target_modified_date"));
					}
				}

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void UpdateFields(HttpServletRequest request) throws SQLException {
		try {

			// SOP("update fields");
			service_target_id = CNumeric(ExecuteQuery("SELECT service_target_id"
					+ " FROM " + compdb(comp_id) + "axela_service_target"
					+ " WHERE  SUBSTR(service_target_startdate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
					+ " AND SUBSTR(service_target_enddate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
					+ " AND service_target_emp_id = " + Service_target_emp_id + ""));
			// SOP("updatefield target_id===== " + target_id);

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			if (service_target_id.equals("0")) {
				AddTargetFields();

			}

			UpdateTargetFields();
			conntx.commit();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void AddTargetFields() throws SQLException {
		try {
			int days = DaysInMonth(Integer.parseInt(year), Integer.parseInt(month));
			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_service_target (  "
					+ " service_target_emp_id,  service_target_startdate,service_target_enddate, "
					+ " service_target_entry_id, service_target_entry_date ) "
					+ " VALUES "
					+ " (" + Service_target_emp_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + days + "000000" + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			// SOP("add==" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();

			while (rs.next()) {
				service_target_id = rs.getString(1);
				// SOP("inside add targets===targetid===" + target_id);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
	}
	public void UpdateTargetFields() throws SQLException {
		try {

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_target "
					+ " SET"
					+ " service_target_jc_count= " + service_target_jc_count + ", "
					+ " service_target_pms_count= " + service_target_pms_count + ", "
					+ " service_target_labour_amount= " + service_target_labour_amount + ", "
					+ " service_target_parts_amount= " + service_target_parts_amount + ", "
					+ " service_target_oil_amount= " + service_target_oil_amount + ", "
					+ " service_target_tyre_count= " + service_target_tyre_count + ", "
					+ " service_target_tyre_amount= " + service_target_tyre_amount + ", "
					+ " service_target_break_count= " + service_target_break_count + ", "
					+ " service_target_break_amount= " + service_target_break_amount + ", "
					+ " service_target_battery_count= " + service_target_battery_count + ", "
					+ " service_target_battery_amount= " + service_target_battery_amount + ", "
					+ " service_target_accessories_amount= " + service_target_accessories_amount + ", "
					+ " service_target_vas_amount= " + service_target_vas_amount + ", "
					+ " service_target_extwarranty_count= " + service_target_extwarranty_count + ", "
					+ " service_target_extwarranty_amount= " + service_target_extwarranty_amount + ", "
					+ " service_target_wheelalign_amount= " + service_target_wheelalignment_amount + ", "
					+ " service_target_cng_amount= " + service_target_cng_amount + ", "
					+ " service_target_modified_id = " + service_target_modified_id + ", "
					+ "service_target_modified_date=" + service_target_modified_date + " "
					+ " WHERE service_target_id=" + service_target_id + "";

			// SOP("Strsql update targets===" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
	}
}
