// smitha nag 28, 29 march 2013
package axela.sales;

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

public class Target_Branch_Update extends Connect {
	public String msg = "";
	public String update = "";
	public String updateB = "";
	public String comp_id = "0";
	public String branch_id = "0", dr_branch_id = "0";
	public String branch_name = "";
	public String year = "";
	public String target_id = "0";
	public String branchtarget_id = "0";
	public String branchtarget_enquiry_count = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String branchtarget_homevisit_count = "0";
	public String branchtarget_testdrives_count = "0";
	public String branchtarget_bookings_count = "0";
	public String branchtarget_delivery_count = "0";
	public String branchtarget_insurance_count = "0";
	public String branchtarget_ew_count = "0";
	public String branchtarget_fincases_count = "0";
	public String branchtarget_exchange_count = "0";
	public String branchtarget_evaluation_count = "0";
	public String branchtarget_accessories_amount = "0";
	public String branchtarget_startdate = "";
	public String branchtarget_enddate = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String StrSearch = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String target_branch_id = "";
	public String targetbranchid = "";
	public String branchtarget_entry_id = "";
	public String branchtarget_entry_date = "";
	public String branchtarget_modified_id = "";
	public String branchtarget_modified_date = "";
	public String emp_id = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_target_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				branch_name = ExecuteQuery("select branch_name from "
						+ compdb(comp_id) + "axela_branch where branch_id="
						+ branch_id + "");
				target_id = CNumeric(PadQuotes(request.getParameter("target_id")));
				month = CNumeric(PadQuotes(request.getParameter("month")));
				month_name = TextMonth(Integer.parseInt(month) - 1);
				month_name = month_name.substring(0, 3);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				year = CNumeric(PadQuotes(request.getParameter("year")));

				if (update.equals("yes")) {

					status = "Update";
					// SOP("status====" + status);
					PopulateFields(response);
					// branchtarget_entry_id = CNumeric(GetSession("emp_id",
					// request));
					// branchtarget_entry_date = ToLongDate(kknow());
				}
				if (updateB.equals("Update")) {
					GetValues(request, response);

					branchtarget_modified_id = CNumeric(GetSession(
							"emp_id", request));
					branchtarget_modified_date = ToLongDate(kknow());
					UpdateFields(request);

					response.sendRedirect(response
							.encodeRedirectURL("target-branch-list.jsp?dr_branch="
									+ branch_id
									+ "&dr_year="
									+ year
									+ "&msg="
									+ "Target Updated Successfully!"));
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

		branchtarget_enquiry_count = CNumeric(PadQuotes(request.getParameter("txt_Enquiry_count")));
		branchtarget_homevisit_count = CNumeric(PadQuotes(request.getParameter("txt_home_count")));
		branchtarget_testdrives_count = CNumeric(PadQuotes(request.getParameter("txt_drive_count")));
		branchtarget_bookings_count = CNumeric(PadQuotes(request.getParameter("txt_book_count")));
		branchtarget_delivery_count = CNumeric(PadQuotes(request.getParameter("txt_delivery_count")));
		branchtarget_accessories_amount = CNumeric(PadQuotes(request.getParameter("txt_accessories_count")));
		branchtarget_insurance_count = CNumeric(PadQuotes(request.getParameter("txt_insurance_count")));
		branchtarget_ew_count = CNumeric(PadQuotes(request.getParameter("txt_ew_count")));
		branchtarget_fincases_count = CNumeric(PadQuotes(request.getParameter("txt_fincases_count")));
		branchtarget_exchange_count = CNumeric(PadQuotes(request.getParameter("txt_exchange_count")));
		branchtarget_evaluation_count = CNumeric(PadQuotes(request.getParameter("txt_evaluation_count")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	private void PopulateFields(HttpServletResponse response) {
		if (!branch_id.equals("0")) {

			StrSql = "SELECT branchtarget_id, branchtarget_branch_id,"
					+ " coalesce(branchtarget_month, '') as branchtarget_month,"
					+ " coalesce(branchtarget_entry_date, '') as branchtarget_entry_date,"
					+ " coalesce(branchtarget_modified_date, '') as branchtarget_modified_date,"
					+ " coalesce(branchtarget_entry_id, '0') branchtarget_entry_id,"
					+ " coalesce(branchtarget_modified_id, '0') as branchtarget_modified_id,"
					+ " coalesce(branchtarget_enquiry_count, 0) as branchtarget_enquiry_count,"
					+ " coalesce(branchtarget_homevisit_count, 0) as branchtarget_homevisit_count,"
					+ " coalesce(branchtarget_testdrives_count, 0) as branchtarget_testdrives_count,"
					+ " coalesce(branchtarget_bookings_count, 0) as branchtarget_bookings_count,"
					+ " coalesce(branchtarget_delivery_count, 0) as branchtarget_delivery_count,"
					+ " coalesce(branchtarget_accessories_amount, 0) as branchtarget_accessories_amount,"
					+ " coalesce(branchtarget_insurance_count, 0) as branchtarget_insurance_count,"
					+ " coalesce(branchtarget_ew_count, 0) as branchtarget_ew_count,"
					+ " coalesce(branchtarget_fincases_count, 0) as branchtarget_fincases_count,"
					+ " coalesce(branchtarget_exchange_count, 0) as branchtarget_exchange_count,"
					+ " coalesce(branchtarget_evaluation_count, 0) as branchtarget_evaluation_count";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_target_branch"
					+ " WHERE branchtarget_branch_id=" + branch_id + ""
					+ " AND SUBSTR(branchtarget_month, 1, 6) = " + year + doublenum(Integer.parseInt(month));

			StrSql = StrSql + SqlJoin;
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				while (crs.next()) {
					branchtarget_id = crs.getString("branchtarget_id");
					branchtarget_enquiry_count = crs.getString("branchtarget_enquiry_count");
					branchtarget_homevisit_count = crs.getString("branchtarget_homevisit_count");
					branchtarget_testdrives_count = crs.getString("branchtarget_testdrives_count");
					branchtarget_bookings_count = crs.getString("branchtarget_bookings_count");
					branchtarget_delivery_count = crs.getString("branchtarget_delivery_count");
					branchtarget_accessories_amount = crs.getString("branchtarget_accessories_amount");
					branchtarget_insurance_count = crs.getString("branchtarget_insurance_count");
					branchtarget_ew_count = crs.getString("branchtarget_ew_count");
					branchtarget_fincases_count = crs.getString("branchtarget_fincases_count");
					branchtarget_exchange_count = crs.getString("branchtarget_exchange_count");
					branchtarget_evaluation_count = crs.getString("branchtarget_evaluation_count");
					branchtarget_entry_id = crs.getString("branchtarget_entry_id");
					entry_by = Exename(comp_id, crs.getInt("branchtarget_entry_id"));
					entry_date = strToLongDate(crs
							.getString("branchtarget_entry_date"));
					branchtarget_modified_id = crs.getString("branchtarget_modified_id");
					if (!branchtarget_modified_id.equals("0")) {
						modified_by = Exename(comp_id,
								Integer.parseInt(branchtarget_modified_id));
						modified_date = strToLongDate(crs
								.getString("branchtarget_modified_date"));
					}

				}

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);

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
			target_id = CNumeric(ExecuteQuery("SELECT branchtarget_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_target_branch"
					+ " WHERE substr(branchtarget_month,1,6) = " + year + doublenum(Integer.parseInt(month))
					+ " AND branchtarget_branch_id = " + branch_id + ""));
			// SOP("updatefield target_id===== " + target_id);

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			if (target_id.equals("0")) {
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
			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_sales_target_branch (  "
					+ " branchtarget_branch_id, " + " branchtarget_month, "
					+ " branchtarget_entry_id, branchtarget_entry_date ) "
					+ " values  "
					+ " (" + branch_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + emp_id + "," + " "
					+ ToLongDate(kknow()) + ")";

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();

			while (rs.next()) {
				target_id = rs.getString(1);
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

			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_target_branch "
					+ " SET"
					+ " branchtarget_enquiry_count= " + branchtarget_enquiry_count + ", "
					+ " branchtarget_homevisit_count= " + branchtarget_homevisit_count + ", "
					+ " branchtarget_testdrives_count= " + branchtarget_testdrives_count + ", "
					+ " branchtarget_bookings_count= " + branchtarget_bookings_count + ", "
					+ " branchtarget_delivery_count= " + branchtarget_delivery_count + ", "
					+ " branchtarget_accessories_amount= " + branchtarget_accessories_amount + ", "
					+ " branchtarget_insurance_count= " + branchtarget_insurance_count + ", "
					+ " branchtarget_ew_count= " + branchtarget_ew_count + ", "
					+ " branchtarget_fincases_count= " + branchtarget_fincases_count + ", "
					+ " branchtarget_exchange_count= " + branchtarget_exchange_count + ", "
					+ " branchtarget_evaluation_count= " + branchtarget_evaluation_count + ", "
					+ " branchtarget_evaluation_count= " + branchtarget_evaluation_count + ", "
					+ " branchtarget_modified_id = " + branchtarget_modified_id + ", "
					+ " branchtarget_modified_date=" + branchtarget_modified_date + " "
					+ " WHERE branchtarget_id=" + target_id + "";

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
