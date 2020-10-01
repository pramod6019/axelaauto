// Ved Prakash (16 Feb 2013)
// modified by Sangita , 7th may 2013
package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Gatepass_Update extends Connect {
	public String BranchAccess = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String salesgatepass_id = "0";
	public String salesgatepass_gatepasstype_id = "0";
	public String salesgatepass_testdriveveh_id = "0";
	public String salesgatepass_fromtime = "", salesgatepass_out_kms = "0";
	public String salesgatepass_totime = "";
	public String salesgatepassfromtime = "";
	public String salesgatepasstotime = "";
	public String salesgatepass_from_branch_id = "0";
	public String salesgatepass_from_branch = "";
	public String salesgatepass_to_branch_id = "0";
	public String salesgatepass_driver_id = "0";
	public String salesgatepass_notes = "";
	public String salesgatepass_entry_id = "0";
	public String salesgatepass_entry_date = "";
	public String salesgatepass_modified_id = "0";
	public String salesgatepass_modified_date = "";

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;

	public Executive_Check execheck = new Executive_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				salesgatepass_id = CNumeric(PadQuotes(request.getParameter("salesgatepass_id")));
				salesgatepass_testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));

				StrSql = "SELECT"
						+ " branch_id,"
						+ " CONCAT(branch_name, ' (',branch_code,')') AS branchname"
						+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = testdriveveh_branch_id"
						+ " WHERE testdriveveh_id =" + salesgatepass_testdriveveh_id;
				CachedRowSet crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					salesgatepass_from_branch_id = crs1.getString("branch_id");
					salesgatepass_from_branch = crs1.getString("branchname");
				}
				crs1.close();
				salesgatepassfromtime = strToLongDate(ToLongDate(kknow()));
				salesgatepasstotime = strToLongDate(ToLongDate(kknow()));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				try {
					if ("yes".equals(add)) {
						if (!"yes".equals(addB)) {
						} else {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
								salesgatepass_entry_id = emp_id;
								salesgatepass_entry_date = ToLongDate(kknow());
								AddFields(comp_id);
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("testdrive-gatepass-list.jsp?testdriveveh_id=" + salesgatepass_testdriveveh_id +
											"&msg=Test Drive Gate Pass Added Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
					if ("yes".equals(update)) {
						if (!"yes".equals(updateB) && !"Delete Gate Pass".equals(deleteB)) {
							PopulateFields(response);
						} else if ("yes".equals(updateB) && !"Delete Gate Pass".equals(deleteB)) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
								salesgatepass_modified_id = emp_id;
								salesgatepass_modified_date = ToLongDate(kknow());
								UpdateFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("testdrive-gatepass-list.jsp?testdriveveh_id=" + salesgatepass_testdriveveh_id +
											"&msg=Test Drive Gate Pass Added Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else if ("Delete Gate Pass".equals(deleteB)) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_testdrive_delete", request).equals("1")) {
								DeleteFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("testdrive-gatepass-list.jsp?testdriveveh_id=" + salesgatepass_testdriveveh_id +
											"&msg=Test Drive Gate Pass Deleted Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}

				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						SOP("Transaction Error==");
						conntx.rollback();
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
					}
					msg = "<br>Transaction Error!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		salesgatepass_to_branch_id = CNumeric(PadQuotes(request.getParameter("dr_salesgatepass_to_branch_id")));
		salesgatepass_gatepasstype_id = CNumeric(PadQuotes(request.getParameter("dr_salesgatepass_gatepasstype_id")));
		salesgatepass_fromtime = PadQuotes(request.getParameter("txt_salesgatepass_fromtime"));
		salesgatepass_totime = PadQuotes(request.getParameter("txt_salesgatepass_totime"));
		salesgatepass_out_kms = PadQuotes(request.getParameter("txt_salesgatepass_out_kms"));
		salesgatepass_driver_id = CNumeric(PadQuotes(request.getParameter("allexecutives")));
		salesgatepass_notes = PadQuotes(request.getParameter("txt_salesgatepass_notes"));
	}

	protected void CheckForm() {
		if (salesgatepass_to_branch_id.equals("0")) {
			msg += "<br>Select To Location!";
		}
		if (salesgatepass_gatepasstype_id.equals("0")) {
			msg += "<br>Select Gate Pass Type!";
		}
		if (!salesgatepass_fromtime.equals("")) {
			if (isValidDateFormatLong(salesgatepass_fromtime)) {
				salesgatepass_fromtime = ConvertLongDateToStr(salesgatepass_fromtime);
				if (Long.parseLong(salesgatepass_fromtime.substring(0, 8) + "000000") < Long.parseLong(ToShortDate(kknow()))) {
					msg = msg + "<br>From Time cannot be less than Current Date!";
				}
				salesgatepassfromtime = strToLongDate(salesgatepass_fromtime);
			} else {
				msg = msg + "<br>Enter Valid From Time!";
				salesgatepass_fromtime = "";
			}
		}

		if (!salesgatepass_totime.equals("")) {
			if (isValidDateFormatLong(salesgatepass_totime)) {
				salesgatepass_totime = ConvertLongDateToStr(salesgatepass_totime);
				if (!salesgatepass_fromtime.equals("") && !salesgatepass_totime.equals("") && Long.parseLong(salesgatepass_fromtime) > Long.parseLong(salesgatepass_totime)) {
					msg = msg + "<br>From Time should be less than To Time!";
				}
				salesgatepasstotime = strToLongDate(salesgatepass_totime);

			} else {
				msg = msg + "<br>Enter Valid To Time!";
				salesgatepass_totime = "";
			}
		}
		// check if any test drive is there at that time
		if (!salesgatepass_fromtime.equals("") && !salesgatepass_totime.equals(""))
			StrSql = "SELECT testdrive_testdriveveh_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " WHERE testdrive_testdriveveh_id = " + salesgatepass_testdriveveh_id
					+ " AND ((testdrive_time_from >= " + salesgatepass_fromtime
					+ " AND testdrive_time_to < " + salesgatepass_totime + ") "
					+ " OR (testdrive_time_from > " + salesgatepass_fromtime
					+ " AND testdrive_time_to <= " + salesgatepass_totime + ") "
					+ " OR (testdrive_time_from < " + salesgatepass_fromtime
					+ " AND testdrive_time_to > " + salesgatepass_totime + "))";
		// SOP("Execute==1===" + StrSql);
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Vehicle has Test Drive(s) at this time!";
		}

		// check if any other gate pass is there for the vehicle at that time
		if (!salesgatepassfromtime.equals("") && !salesgatepasstotime.equals(""))
			StrSql = "SELECT salesgatepass_testdriveveh_id, salesgatepass_fromtime, salesgatepass_totime"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " WHERE salesgatepass_testdriveveh_id = " + salesgatepass_testdriveveh_id
					+ " AND ((salesgatepass_fromtime >= " + salesgatepass_fromtime
					+ " AND salesgatepass_fromtime < " + salesgatepass_totime + ") "
					+ " OR (salesgatepass_totime > " + salesgatepass_fromtime
					+ " AND salesgatepass_totime <= " + salesgatepass_totime + ") "
					+ " OR (salesgatepass_fromtime < " + salesgatepass_fromtime
					+ " AND salesgatepass_totime > " + salesgatepass_totime + "))";
		// SOP("Execute==2===" + StrSql);
		if ("yes".equals(updateB)) {
			StrSql += "AND salesgatepass_id != " + salesgatepass_id;
		}
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Vehicle Outage present during this time!";
		}

		if (salesgatepass_out_kms.equals("0")) {
			msg += "<br>Enter Out KMS!";
		}
	}
	protected void AddFields(String comp_id) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
						+ " (salesgatepass_gatepasstype_id,"
						+ " salesgatepass_testdriveveh_id,"
						+ " salesgatepass_fromtime,"
						+ " salesgatepass_totime,"
						+ " salesgatepass_out_kms,"
						+ " salesgatepass_from_branch_id,"
						+ " salesgatepass_to_branch_id,"
						+ " salesgatepass_driver_id,"
						+ " salesgatepass_notes,"
						+ " salesgatepass_entry_id,"
						+ " salesgatepass_entry_date)"
						+ " VALUES"
						+ " (" + salesgatepass_gatepasstype_id + ","
						+ " " + salesgatepass_testdriveveh_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + salesgatepass_totime + "',"
						+ " '" + salesgatepass_out_kms + "',"
						+ " " + salesgatepass_from_branch_id + ","
						+ " " + salesgatepass_to_branch_id + ","
						+ " " + salesgatepass_driver_id + ","
						+ " '" + salesgatepass_notes + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "')";
				// SOP("StrSql-----insert---" + StrSql);
				stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					salesgatepass_id = rs.getString(1);
				}
				rs.close();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " SET "
						+ " testdriveveh_branch_id= " + salesgatepass_to_branch_id + ""
						+ " WHERE testdriveveh_id = " + salesgatepass_testdriveveh_id + "";

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " salesgatepass_id,"
					+ " salesgatepass_gatepasstype_id,"
					+ " salesgatepass_testdriveveh_id,"
					+ " salesgatepass_fromtime,"
					+ " salesgatepass_totime,"
					+ " salesgatepass_out_kms,"
					+ " salesgatepass_from_branch_id,"
					+ " salesgatepass_to_branch_id,"
					+ " salesgatepass_driver_id,"
					+ " salesgatepass_notes,"
					+ " salesgatepass_entry_id,"
					+ " salesgatepass_entry_date,"
					+ " salesgatepass_modified_id,"
					+ " salesgatepass_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " WHERE 1 = 1"
					+ " AND salesgatepass_id = " + salesgatepass_id;
			// SOP("StrSql===pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					salesgatepass_id = crs.getString("salesgatepass_id");
					salesgatepass_gatepasstype_id = crs.getString("salesgatepass_gatepasstype_id");
					salesgatepassfromtime = strToLongDate(crs.getString("salesgatepass_fromtime"));
					salesgatepasstotime = strToLongDate(crs.getString("salesgatepass_totime"));
					salesgatepass_out_kms = crs.getString("salesgatepass_out_kms");
					SOP("salesgatepass_out_kms==" + salesgatepass_out_kms);
					salesgatepass_to_branch_id = crs.getString("salesgatepass_to_branch_id");
					salesgatepass_driver_id = crs.getString("salesgatepass_driver_id");
					salesgatepass_notes = crs.getString("salesgatepass_notes");
					salesgatepass_entry_id = crs.getString("salesgatepass_entry_id");
					salesgatepass_entry_date = crs.getString("salesgatepass_entry_date");
					salesgatepass_modified_id = crs.getString("salesgatepass_modified_id");
					salesgatepass_modified_date = crs.getString("salesgatepass_modified_date");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Gate Pass!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
						+ " SET"
						+ " salesgatepass_gatepasstype_id = '" + salesgatepass_gatepasstype_id + "',"
						// + " salesgatepass_fromtime = '" + salesgatepass_fromtime + "',"
						+ " salesgatepass_totime = '" + salesgatepass_totime + "',"
						+ " salesgatepass_out_kms = '" + salesgatepass_out_kms + "',"
						+ " salesgatepass_to_branch_id = " + salesgatepass_to_branch_id + ","
						+ " salesgatepass_driver_id = " + salesgatepass_driver_id + ","
						+ " salesgatepass_notes = '" + salesgatepass_notes + "',"
						+ " salesgatepass_modified_id = " + emp_id + ","
						+ " salesgatepass_modified_date = '" + ToLongDate(kknow()) + "'"
						+ " WHERE salesgatepass_id = " + salesgatepass_id + "";
				// SOP("update==" + StrSql);
				stmttx.execute(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " SET "
						+ " testdriveveh_branch_id= " + salesgatepass_to_branch_id + ""
						+ " WHERE testdriveveh_id = " + salesgatepass_testdriveveh_id + "";
				stmttx.execute(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
	}

	protected void DeleteFields() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " WHERE salesgatepass_id = " + salesgatepass_id + "";
			// SOP("Delete==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch(String branch_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					// + " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_salesgatepass_to_branch_id\" name=\"dr_salesgatepass_to_branch_id\" class=\"form-control\">");
			Str.append("<option value=0>Select To Location</option>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDriver(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales = 1"
					+ " AND emp_active = 1";
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + ""
					+ " AND leave_active = 1)";

			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("PopulateInsurExecutive-==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_salesgatepass_driver_id\" id=\"dr_salesgatepass_driver_id\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), salesgatepass_driver_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateGatepassType(String gatepassType_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT"
					+ " salesgatepasstype_id,"
					+ " salesgatepasstype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass_type"
					+ " WHERE 1 = 1";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_salesgatepass_gatepasstype_id\" id=\"dr_salesgatepass_gatepasstype_id\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select Gate Pass</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("salesgatepasstype_id")).append("");
				Str.append(Selectdrop(crs.getInt("salesgatepasstype_id"), gatepassType_id));
				Str.append(">").append(crs.getString("salesgatepasstype_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
