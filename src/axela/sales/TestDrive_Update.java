package axela.sales;
//Smitha Nag 11th Feb 2013

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String BranchAccess = "", branch_id = "";
	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String testdrive_id = "";
	public String testdrive_testdriveveh_id = "", model_id = "";
	public String testdrive_emp_id = "";
	public String testdrive_location_id = "";
	public String testdrive_type = "1";
	public String testdrive_time = "", testdrive_time_from = "", testdrive_time_to = "";
	public String testdrive_confirmed = "", unconfirm = "";
	public String testdrive_notes = "";
	public String testdrive_entry_id = "";
	public String testdrive_entry_date = "";
	public String testdrive_enquiry_id = "";
	public String testdrive_modified_id = "";
	public String testdrive_modified_date = "";
	public String enquiry_branch_id = "";
	public String enquiry_model_id = "";
	// public String StartHour = "", StartMin = "";
	// public String DurHour = "", DurMin = "";
	public String testdrive_customer_id = "";
	public String customer_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
	public String model_name = "";
	public String executive_name = "";
	public String strHTML = "";
	public String enquiry_no = "", enquiry_date = "", enquiry_name = "";
	public String pop = "", contact_mobile1 = "";
	public String testdrivedate = "", testdrivetypename = "";
	// public String ddate = "", dMin = "", dHour = "";

	public String brandconfig_testdrive_email_enable = "";
	public String brandconfig_testdrive_email_sub = "";
	public String brandconfig_testdrive_email_format = "";
	public String brandconfig_testdrive_email_exe_enable = "";
	public String brandconfig_testdrive_email_exe_sub = "";
	public String brandconfig_testdrive_email_exe_format = "";
	public String brandconfig_testdrive_sms_enable = "";
	public String brandconfig_testdrive_sms_format = "";
	public String brandconfig_testdrive_sms_exe_enable = "";
	public String brandconfig_testdrive_sms_exe_format = "";

	public String comp_sms_enable = "";
	public String config_sms_enable = "", branch_brand_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				pop = PadQuotes(request.getParameter("pop"));
				add = PadQuotes(request.getParameter("add"));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdrive_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				// SOP("testdrive_id===" + testdrive_id);
				unconfirm = PadQuotes(request.getParameter("unconfirm"));

				EnquiryDetails(response);

				if (!add.equals("yes")) {
					PopulateFields(response);
				}

				if (unconfirm.equals("yes")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "SELECT emp_id, testdrive_id, "
								+ "(select concat(emp_name,' (',emp_ref_no,')') "
								+ " from " + compdb(comp_id) + "axela_emp where emp_id = " + emp_id + ") as updateemp "
								+ " from " + compdb(comp_id) + "axela_emp"
								+ " inner join " + compdb(comp_id) + "axela_sales_testdrive on testdrive_emp_id = emp_id "
								+ " WHERE emp_active ='1' and testdrive_id = " + testdrive_id + "";
						//
						StrSql = StrSql + " ORDER BY emp_name";
						// SOP("testdrive_confirmed===" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						String testdrive_notes = "";
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								testdrive_notes = "Test Drive unconfirmed by " + crs.getString("updateemp") + " at " + strToLongDate(ToLongDate(kknow())) + ", ";
							}
							// if (ReturnPerm(comp_id, "emp_testdrive_access",
							// request, response).equals("1")) {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
									+ " SET "
									+ " testdrive_confirmed = '0', "
									+ " testdrive_notes = concat('" + testdrive_notes + "',testdrive_notes)"
									+ " where testdrive_id =" + testdrive_id + "";
							updateQuery(StrSql);
							// // SOP("AFTER UPDATING UNCONFIRMED==========");
							response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));
							// } else {
							// response.sendRedirect(AccessDenied());
							// }
						} else {
							response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Permission denied!"));
						}
						crs.close();
					} else {
						response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));
					}
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				// if (!"yes".equals(updateB) || !"yes".equals(addB)) {
				// PopulateTestDriveType();
				// }

				// // SOP("testdriv/e_type = " + testdrive_type);

				PopulateConfigDetails();

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						testdrive_testdriveveh_id = "";
						testdrive_location_id = "";
						testdrive_type = "1";
						testdrive_confirmed = "1";
						testdrive_notes = "";
						String str = ToLongDate(kknow());
						testdrivedate = SplitDate(str) + "/" + SplitMonth(str) + "/" + SplitYear(str) + " " + SplitHourMin(str);
					} else {
						if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
							GetValues(request, response);
							testdrive_entry_id = CNumeric(GetSession("emp_id", request));
							testdrive_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive added successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
						// PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							GetValues(request, response);
							testdrive_modified_id = CNumeric(GetSession("emp_id", request));
							testdrive_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive details updated successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Test Drive".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?msg=Test Drive details deleted successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		testdrive_id = PadQuotes(request.getParameter("testdrive_id"));
		model_id = PadQuotes(request.getParameter("dr_model"));
		testdrive_testdriveveh_id = PadQuotes(request.getParameter("dr_vehicle"));
		testdrive_location_id = PadQuotes(request.getParameter("dr_location"));
		testdrivedate = PadQuotes(request.getParameter("txt_testdrive_date"));
		// StartHour = PadQuotes(request.getParameter("drop_StartHour"));
		// StartMin = PadQuotes(request.getParameter("drop_StartMin"));
		// DurHour = PadQuotes(request.getParameter("drop_DurHour"));
		// DurMin = PadQuotes(request.getParameter("drop_DurMin"));
		testdrive_location_id = PadQuotes(request.getParameter("dr_location"));
		testdrive_confirmed = PadQuotes(request.getParameter("chk_testdrive_confirmed"));
		if (testdrive_confirmed.equals("on")) {
			testdrive_confirmed = "1";
		} else {
			testdrive_confirmed = "0";
		}
		// if (!emp_testdrive_edit.equals("1"))
		testdrive_confirmed = "1";
		testdrive_notes = PadQuotes(request.getParameter("txt_testdrive_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		testdrive_time = testdrivedate + ":00";
		testdrive_time = ConvertLongDateToStr(testdrive_time);
		StrSql = "select location_leadtime, location_testdrive_dur "
				+ " from " + compdb(comp_id) + "axela_sales_testdrive_location "
				+ " where location_id=" + testdrive_location_id;
		// SOP("check===" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Date start = AddHoursDate(StringToDate(testdrive_time), 0, 0, -crs.getDouble("location_leadtime"));
				testdrive_time_from = ToLongDate(start);
				Date end = AddHoursDate(StringToDate(testdrive_time), 0, 0, (crs.getDouble("location_testdrive_dur") + crs.getDouble("location_leadtime")));
				testdrive_time_to = ToLongDate(end);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("error in cal of start and end time---" + ex);
		}
		if (model_name.equals("")) {
			msg = msg + "<br>Select Model!";
		}
		if (testdrive_testdriveveh_id.equals("0")) {
			msg = msg + "<br>Select Vehicle!";
		}
		if (testdrive_emp_id.equals("0")) {
			msg = msg + "<br>Select Sales Consultant!";
		}
		if (testdrivedate.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatLong(testdrivedate)) {
				if (!testdrive_time_from.equals("") && !testdrive_time_to.equals("")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "select testdrive_id from " + compdb(comp_id) + "axela_sales_testdrive "
								+ " WHERE testdrive_confirmed='1' "
								+ " AND testdrive_testdriveveh_id = " + testdrive_testdriveveh_id
								+ " AND testdrive_fb_taken = 0"
								+ " AND "
								+ " ((testdrive_time_from >= " + testdrive_time_from + " and testdrive_time_from < " + testdrive_time_to + ")"
								+ " or (testdrive_time_to > " + testdrive_time_from + " and testdrive_time_to <= " + testdrive_time_to + ") "
								+ " or (testdrive_time_from >= " + testdrive_time_from + " and testdrive_time_to <= " + testdrive_time_to + ") "
								+ " or (testdrive_time_from <= " + testdrive_time_from + " and testdrive_time_to >= " + testdrive_time_to + "))";
						if (!update.equals("")) {
							StrSql = StrSql + " and testdrive_id !=" + testdrive_id;
						}
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Vehicle is occupied by other Test Drives!";
						}
						StrSql = "SELECT salesgatepass_testdriveveh_id, salesgatepass_fromtime, salesgatepass_totime FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
								+ " where salesgatepass_testdriveveh_id=" + testdrive_testdriveveh_id + " and "
								+ "((salesgatepass_fromtime >= " + testdrive_time_from + " and salesgatepass_fromtime < " + testdrive_time_to + ") "
								+ " or (salesgatepass_totime > " + testdrive_time_from + " and salesgatepass_totime <= " + testdrive_time_to + ") "
								+ " or (salesgatepass_fromtime < " + testdrive_time_from + " and salesgatepass_totime > " + testdrive_time_to + "))";
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Vehicle outage!";
						}

					}
				}
			} else {
				msg = msg + "<br>Enter Valid Test Drive Date!";
			}
		}
		if (testdrive_location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		// if (testdrive_type.equals("0")) {
		// msg = msg + "<br>Select Test Drive Type!";
		// }
		// if (StartHour.equals("00") && StartMin.equals("00")) {
		// msg = msg + "Select Valid Test Drive Time!<br>";
		// }
		if (testdrive_notes.length() > 8000) {
			testdrive_notes = testdrive_notes.substring(0, 7999);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				// testdrive_id =
				// ExecuteQuery("Select coalesce(max(testdrive_id),0)+1 as testdrive_id from "
				// + compdb(comp_id) + "axela_sales_testdrive");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_testdrive"
						+ "("
						// + "testdrive_id,"
						+ "testdrive_enquiry_id,"
						+ "testdrive_testdriveveh_id, "
						+ "testdrive_emp_id, "
						+ "testdrive_location_id,"
						+ "testdrive_type,"
						+ "testdrive_time,"
						+ "testdrive_time_from,"
						+ "testdrive_time_to,"
						+ "testdrive_confirmed,"
						+ "testdrive_notes,"
						+ "testdrive_entry_id,"
						+ "testdrive_entry_date,"
						+ "testdrive_modified_id,"
						+ "testdrive_modified_date"
						+ ") "
						+ "values	"
						+ "("
						// + "" + testdrive_id + ","
						+ "" + testdrive_enquiry_id + ","
						+ "'" + testdrive_testdriveveh_id + "',"
						+ "" + testdrive_emp_id + ","
						+ "'" + testdrive_location_id + "',"
						+ "'" + testdrive_type + "',"
						+ "'" + testdrive_time + "',"
						+ "'" + testdrive_time_from + "',"
						+ "'" + testdrive_time_to + "',"
						+ "'" + testdrive_confirmed + "',"
						+ "'" + testdrive_notes + "',"
						+ "'" + testdrive_entry_id + "',"
						+ "'" + testdrive_entry_date + "',"
						+ "0,"
						+ "''"
						+ ")";
				// // SOP("StrSql = " + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					testdrive_id = rs.getString(1);
					new Enquiry_Dash_Check().LastTimeUpdate(testdrive_enquiry_id, comp_id);
				}
				rs.close();
				StrSql = "SELECT enquiry_stage_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id =" + testdrive_enquiry_id;
				if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) <= 3) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET enquiry_stage_id = 3"
							+ " WHERE enquiry_id = " + testdrive_enquiry_id;
					stmttx.execute(StrSql);
				}

				// // SOP("comp_sms_enable = " + comp_sms_enable);
				// // SOP("config_sms_enable = " + config_sms_enable);
				// // SOP("branch_testdrive_sms_enable = " +
				// branch_testdrive_sms_enable);
				// // SOP("branch_testdrive_sms_format = " +
				// branch_testdrive_sms_format);
				// // SOP("contact_mobile1 = " + contact_mobile1);
				// // SOP("testdrive_confirmed = " + testdrive_confirmed);
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && brandconfig_testdrive_sms_enable.equals("1")) {
					if (!brandconfig_testdrive_sms_format.equals("") && !contact_mobile1.equals("") && testdrive_confirmed.equals("1")) {
						SendSMS();
					}
				}

				conntx.commit();
				// for setting enquiry priority
				EnquiryPriorityUpdate(comp_id, testdrive_enquiry_id);
				// // SOP("Transaction commit...");
			} catch (Exception e) {
				conntx.rollback();
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
						+ " SET "
						+ "testdrive_testdriveveh_id= '" + testdrive_testdriveveh_id + "', "
						+ "testdrive_emp_id = '" + testdrive_emp_id + "', "
						+ "testdrive_location_id = '" + testdrive_location_id + "', "
						+ "testdrive_time = '" + testdrive_time + "', "
						+ "testdrive_time_from = '" + testdrive_time_from + "', "
						+ "testdrive_time_to = '" + testdrive_time_to + "', "
						+ "testdrive_type = '" + testdrive_type + "', "
						+ "testdrive_confirmed = '" + testdrive_confirmed + "', "
						+ "testdrive_notes = '" + testdrive_notes + "', "
						+ "testdrive_modified_id = '" + testdrive_modified_id + "', "
						+ "testdrive_modified_date = '" + testdrive_modified_date + "' "
						+ "where testdrive_id = " + testdrive_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "Select testdrive_doc_value from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(TestDriveDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_colour_trans where trans_testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT testdrive_testdriveveh_id, testdrive_emp_id,testdrive_time, testdrive_time_from, testdrive_type,"
					+ " testdrive_time_to, testdrive_location_id, testdrive_confirmed, testdrive_notes, testdrive_entry_id,"
					+ " testdrive_entry_date, testdrive_modified_id, testdrive_modified_date, item_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = testdriveveh_item_id "
					+ " where testdrive_id = " + testdrive_id + "";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_testdriveveh_id = crs.getString("testdrive_testdriveveh_id");
					model_id = crs.getString("item_model_id");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = crs.getString("testdrive_time_from");
					testdrive_time_to = crs.getString("testdrive_time_to");
					testdrivedate = SplitDate(testdrive_time) + "/" + SplitMonth(testdrive_time) + "/" + SplitYear(testdrive_time) + " " + SplitHourMin(testdrive_time);
					Date stdate = StringToDate(testdrive_time_from);
					Date eddate = StringToDate(testdrive_time_to);
					// DurHour = "" + (int) getHoursBetween(stdate, eddate);
					// DurMin = "" + (int) getMinBetween(stdate, eddate);
					testdrive_location_id = crs.getString("testdrive_location_id");
					testdrive_type = crs.getString("testdrive_type");
					testdrive_confirmed = crs.getString("testdrive_confirmed");
					// testdrive_notes=
					// crs.getString("testdrive_notes").substring(0,
					// crs.getString("testdrive_notes").lastIndexOf(","));
					testdrive_notes = crs.getString("testdrive_notes");
					testdrive_entry_id = crs.getString("testdrive_entry_id");
					entry_by = Exename(comp_id, crs.getInt("testdrive_entry_id"));
					entry_date = strToLongDate(crs.getString("testdrive_entry_date"));
					testdrive_modified_id = crs.getString("testdrive_modified_id");
					if (!testdrive_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(testdrive_modified_id));
						modified_date = strToLongDate(crs.getString("testdrive_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateVehicle(String comp_id, String model_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT "
					+ " testdriveveh_id,"
					+ " testdriveveh_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id "
					+ " WHERE 1 = 1"
					+ " AND item_model_id = " + model_id + ""
					+ " AND testdriveveh_branch_id = " + branch_id
					+ " AND testdriveveh_active = 1 "
					+ " ORDER BY  testdriveveh_name";
			// SOP("StrSql ----PopulateVehicle-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_vehicle\" class=\"form-control\" id=\"dr_vehicle\" onChange=\"TestDriveCheck()\">");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("testdriveveh_id")).append("");
				Str.append(StrSelectdrop(crs.getString("testdriveveh_id"), testdrive_testdriveveh_id));
				Str.append(">").append(crs.getString("testdriveveh_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String comp_id, String model_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT "
					+ " model_id,"
					+ " model_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + " axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_item_id = item_id"
					+ " WHERE	1 = 1"
					// + " AND testdriveveh_branch_id= " + CNumeric(branch_id) + ""
					+ " AND enquiry_id =" + testdrive_enquiry_id
					+ " AND testdriveveh_active = 1 "
					+ " GROUP BY model_id"
					+ " ORDER BY  model_name";
			// SOP("StrSql ----Populate-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_model' class='form-control' id='dr_model' onChange='populateVehicle();'>");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLocation(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT location_name, location_id"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_location "
					+ " where location_active='1' "
					+ " and location_branch_id= " + CNumeric(enquiry_branch_id) + " "
					+ " order by location_name ";
			// SOP("location===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(Selectdrop(crs.getInt("location_id"), testdrive_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public void PopulateTestDriveType() {
	// String result = "";
	// try {
	// if (status.equals("Add")) {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + testdrive_enquiry_id + "\n"
	// + "AND testdrive_fb_taken = '1'";
	// // // SOP("StrSql = " + StrSql);
	// result = ExecuteQuery(StrSql);
	// if (result.equals("")) {
	// testdrivetypename = "Main Test Drive";
	// testdrive_type = "1";
	// } else {
	// testdrivetypename = "Alternate Test Drive";
	// testdrive_type = "2";
	// }
	// } else {
	// StrSql = "SELECT testdrive_type\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_id = " + testdrive_id + "";
	// result = ExecuteQuery(StrSql);
	// if (result.equals("1")) {
	// testdrivetypename = "Main Test Drive";
	// testdrive_type = "1";
	// } else {
	// testdrivetypename = "Alternate Test Drive";
	// testdrive_type = "2";
	// }
	// }
	// // // SOP("result = " + result);
	// // // SOP("testdrivetypename = " + testdrivetypename);
	//
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// }
	// }
	// public String PopulateTestDriveType() {
	// String type = "<option value = 0>Select</option>\n";
	// type = type + "<option value = 1" + Selectdrop(1, testdrive_type) +
	// ">Main Test Drive</option>\n";
	// type = type + "<option value = 2" + Selectdrop(2, testdrive_type) +
	// ">Alternate Test Drive</option>\n";
	// return type;
	// }
	// public String PopulateStartHour() {
	// String year = "";
	// if (add.equals("yes")) {
	// // if (StartHour.equals("")) {
	// // StartHour = "10";
	// // }
	// }
	// for (int i = 0; i <= 23; i++) {
	// year = year + "<option value = " + doublenum(i) + "" + Selectdrop(i,
	// StartHour) + ">" + doublenum(i) + "</option>\n";
	// }
	// return year;
	// }
	//
	// public String PopulateStartMin() {
	// String stringval = "";
	// for (int i = 0; i <= 55;) {
	// stringval = stringval + "<option value = " + doublenum(i) + "" +
	// Selectdrop(i, StartMin) + ">" + doublenum(i) + "</option>\n";
	// i = i + 5;
	// }
	// return stringval;
	// }
	// public String PopulateDurHour() {
	// String hour = "";
	// for (int i = 0; i <= 23; i++) {
	// hour = hour + "<option value = " + doublenum(i) + "" + Selectdrop(i,
	// DurHour) + ">" + doublenum(i) + "</option>\n";
	// }
	// return hour;
	// }
	//
	// public String PopulateDurMin() {
	// String stringval = "";
	// for (int i = 0; i <= 55;) {
	// stringval = stringval + "<option value = " + doublenum(i) + "" +
	// Selectdrop(i, DurMin) + ">" + doublenum(i) + "</option>\n";
	// i = i + 5;
	// }
	// return stringval;
	// }
	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			if (!testdrive_enquiry_id.equals("")) {
				StrSql = "Select customer_id, customer_name, enquiry_branch_id, contact_mobile1,"
						+ " customer_email1, customer_address, customer_pin, enquiry_date,"
						+ " concat('ENQ',branch_code,enquiry_no) as enquiry_no, branch_code,"
						+ " enquiry_model_id, coalesce(model_name, '') as model_name,"
						+ " enquiry_emp_id, concat(emp_name, '(', emp_ref_no, ')') as emp_name,"
						+ " branch_brand_id"
						+ " from " + compdb(comp_id) + "axela_customer"
						+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id=customer_id"
						+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id=enquiry_contact_id"
						+ " left join " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
						+ " left join " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id"
						+ " where enquiry_id = " + CNumeric(testdrive_enquiry_id) + BranchAccess + " ";
				// SOP("StrSql(11)===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						contact_mobile1 = crs.getString("contact_mobile1");
						testdrive_customer_id = crs.getString("customer_id");
						customer_name = crs.getString("customer_name");
						customer_email1 = crs.getString("customer_email1");
						customer_address = crs.getString("customer_address");
						customer_pin = crs.getString("customer_pin");
						model_name = crs.getString("model_name");
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						model_id = crs.getString("enquiry_model_id");
						enquiry_date = crs.getString("enquiry_date");
						enquiry_no = crs.getString("enquiry_no");
						testdrive_emp_id = crs.getString("enquiry_emp_id");
						executive_name = crs.getString("emp_name");
						branch_brand_id = crs.getString("branch_brand_id");
					}
					msg = "";

					if (model_id.equals("0")) {
						msg = msg + "<br>Select Model for the Enquiry!";
					}
					if (customer_address.equals("") && !branch_brand_id.equals("60")) {
						msg = msg + "<br>Full Address not updated for this Enquiry!";
					}
					if (customer_pin.equals("") && !branch_brand_id.equals("60")) {
						msg = msg + "<br>Address Pin Code not updated for this Enquiry!";
					}
					if (testdrive_emp_id.equals("0")) {
						msg = msg + "<br>Select Execuitve for the Enquiry!";
					}
					if (!branch_brand_id.equals("60")) {
						msg = msg + new Enquiry_Dash_Methods().CheckEnquiryFields(testdrive_enquiry_id, branch_brand_id, comp_id);
					}
					if (!msg.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg));
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void UpdateTestDriveType(String enquiry_id) {
		String testdriveid = "0";
		StrSql = "SELECT testdrive_id FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " WHERE testdrive_enquiry_id = " + enquiry_id + ""
				+ " AND testdrive_fb_taken = 1 LIMIT 1";
		testdriveid = ExecuteQuery(StrSql);
		if (!testdriveid.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
					+ " SET testdrive_type = CASE"
					+ " WHEN testdrive_id = " + testdriveid + " THEN 1"
					+ " WHEN testdrive_id != " + testdriveid + " THEN 2"
					+ " END"
					+ " WHERE testdrive_enquiry_id = " + enquiry_id + "";
			// // SOP("StrSql = " + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(brandconfig_testdrive_email_enable, '') AS brandconfig_testdrive_email_enable,"
					+ " COALESCE(brandconfig_testdrive_email_sub, '') AS brandconfig_testdrive_email_sub,"
					+ " COALESCE(brandconfig_testdrive_email_format, '') AS brandconfig_testdrive_email_format,"
					+ " COALESCE(brandconfig_testdrive_email_exe_enable, '') AS brandconfig_testdrive_email_exe_enable,"
					+ " COALESCE(brandconfig_testdrive_email_exe_sub, '') AS brandconfig_testdrive_email_exe_sub,"
					+ " COALESCE(brandconfig_testdrive_email_exe_format, '') AS brandconfig_testdrive_email_exe_format,"
					+ " COALESCE(brandconfig_testdrive_sms_enable, '') AS brandconfig_testdrive_sms_enable,"
					+ " COALESCE(brandconfig_testdrive_sms_format, '') AS brandconfig_testdrive_sms_format,"
					+ " COALESCE(brandconfig_testdrive_sms_exe_enable, '') AS brandconfig_testdrive_sms_exe_enable,"
					+ " COALESCE(brandconfig_testdrive_sms_exe_format, '') AS  brandconfig_testdrive_sms_exe_format,"
					+ " config_sms_enable,"
					+ " comp_sms_enable"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + enquiry_branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + "";
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				brandconfig_testdrive_email_enable = crs.getString("brandconfig_testdrive_email_enable");
				brandconfig_testdrive_email_sub = crs.getString("brandconfig_testdrive_email_sub");
				brandconfig_testdrive_email_format = crs.getString("brandconfig_testdrive_email_format");
				brandconfig_testdrive_email_exe_enable = crs.getString("brandconfig_testdrive_email_exe_enable");
				brandconfig_testdrive_email_exe_sub = crs.getString("brandconfig_testdrive_email_exe_sub");
				brandconfig_testdrive_email_exe_format = crs.getString("brandconfig_testdrive_email_exe_format");
				brandconfig_testdrive_sms_enable = crs.getString("brandconfig_testdrive_sms_enable");
				brandconfig_testdrive_sms_format = crs.getString("brandconfig_testdrive_sms_format");
				brandconfig_testdrive_sms_exe_enable = crs.getString("brandconfig_testdrive_sms_exe_enable");
				brandconfig_testdrive_sms_exe_format = crs.getString("brandconfig_testdrive_sms_exe_format");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS() throws SQLException {
		// String smsmsg =
		// "Dear [CONTACTNAME], As confirmed by you, your Test Drive for the Maruti Suzuki [ITEM] has been scheduled for [DATE] at [TIME].";
		String smsmsg = (brandconfig_testdrive_sms_format);
		smsmsg = "replace('" + smsmsg + "','[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',testdriveveh_name)";
		smsmsg = "replace(" + smsmsg + ",'[DATE]',DATE_FORMAT('" + testdrive_time + "', '%d/%m/%Y'))";
		smsmsg = "replace(" + smsmsg + ",'[TIME]',DATE_FORMAT('" + testdrive_time + "', '%H:%i %p'))";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";
		smsmsg = "REPLACE(" + smsmsg + ", '[LOCATION]',location_name)";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_branch_id + ","
					+ " contact_id,"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " contact_mobile1,"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive on testdrive_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_testdriveveh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id"
					+ " where testdrive_id = " + testdrive_id + " LIMIT 1";
			// SOP("select-sms-" + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// // SOP("INSERT-sms-" + StrSql);
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
