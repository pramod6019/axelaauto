// smitha nag 13 feb 2013
// saiman 2nd may 2013
package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.sales.TestDrive_Update;
import cloudify.connect.Connect;

public class App_PreownedTestDrive_Feedback extends Connect {

	public String feedbackB = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String testdrive_id = "0";
	public String testdrive_type = "";
	public String testdrive_enquiry_id = "";
	public String enquiry_no = "";
	public String testdrive_customer_id = "";
	public String testdrive_emp_id = "0";
	public String contact_id = "";
	public String enquiry_branch_id = "";
	public String enquiry_date = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String vehicle_name = "", veh_id = "";
	public String model_name = "";
	public String location_name = "";
	public String preownedmodel_id = "0";
	public String variant_id = "0";
	public String variant_name = "";
	public String preownedmodel_name = "";
	public String testdrive_time = "";
	public String testdrive_time_from = "";
	public String executive_name = "";
	public String testdrive_fb_taken = "";
	public String testdrive_fb_status_id = "";
	public String testdrive_fb_status_comments = "";
	// public String testdrive_fb_finance = "1";
	// public String testdrive_fb_finance_amount = "";
	// public String testdrive_fb_finance_comments = "";
	// public String testdrive_fb_insurance = "1";
	// public String testdrive_fb_insurance_comments = "";
	public String testdrive_fb_notes = "";
	// public String testdrive_fb_budget = "";
	// public String testdrive_fb_delexp_date = "", delexp_date = "";
	public String testdrive_fb_entry_id = "0";
	public String testdrive_fb_entry_date = "";
	public String testdrive_fb_modified_id = "0";
	public String testdrive_fb_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String enquiry_id = "0";
	// public String colHTML = "";
	public String config_sms_enable = "";
	public int colcount = 0;
	public String emp_uuid = "";
	public String item_id = "0";
	public String QueryString = "", access = "";
	public TestDrive_Update type = new TestDrive_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				access = ReturnPerm(comp_id, "emp_testdrive_access", request);
				if (access.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				feedbackB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				// enqpop = PadQuotes(request.getParameter("pop"));
				EnquiryDetails(response);
				getConfigFields();
				if (feedbackB.equals("")) {
					PopulateFields(response);
					// colHTML = ListColoursIntersted(request);
				} else if ("Update Feedback".equals(feedbackB)) {
					GetValues(request, response);
					// colHTML = ListColoursIntersted(request);
					if (entry_by.equals("")) {
						if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
							testdrive_fb_entry_id = CNumeric(GetSession("emp_id", request));
							testdrive_fb_entry_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								type.comp_id = comp_id;
								type.UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect("callurlapp-preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Feedback updated successfully");
							}
						} else {
							response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
						}
					} else {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							testdrive_fb_modified_id = CNumeric(GetSession("emp_id", request));
							testdrive_fb_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								type.UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect("callurlapp-preownedtestdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Feedback updated successfully");
							}
						} else {
							response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
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
		testdrive_fb_taken = PadQuotes(request.getParameter("dr_testdrivetaken"));
		// testdrive_fb_status_id = PadQuotes(request.getParameter("dr_status"));
		testdrive_fb_status_comments = PadQuotes(request.getParameter("txt_testdrive_fb_status_comments"));
		// testdrive_fb_budget =
		// PadQuotes(request.getParameter("txt_testdrive_fb_budget"));
		// if (testdrive_fb_budget.equals("")) {
		// testdrive_fb_budget = "0";
		// }
		// testdrive_fb_budget = testdrive_fb_budget.replace(",", "");
		// delexp_date =
		// PadQuotes(request.getParameter("txt_testdrive_fb_delexp_date"));
		// testdrive_fb_finance = PadQuotes(request.getParameter("dr_finance"));
		// testdrive_fb_finance_amount =
		// PadQuotes(request.getParameter("txt_testdrive_fb_finance_amount"));
		// testdrive_fb_finance_comments =
		// PadQuotes(request.getParameter("txt_testdrive_fb_finance_comments"));
		// if (!testdrive_fb_finance.equals("0")) {
		// testdrive_fb_finance_comments = "";
		// }
		// testdrive_fb_insurance =
		// PadQuotes(request.getParameter("dr_insurance"));
		// testdrive_fb_insurance_comments =
		// PadQuotes(request.getParameter("txt_testdrive_fb_insurance_comments"));
		// if (!testdrive_fb_insurance.equals("0")) {
		// testdrive_fb_insurance_comments = "";
		// }
		testdrive_fb_notes = PadQuotes(request.getParameter("txt_testdrive_fb_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		// if (testdrive_fb_taken.equals("2") ||
		// !testdrive_fb_status_id.equals("0")) {
		// testdrive_fb_budget = "0";
		// testdrive_fb_finance = "0";
		// testdrive_fb_finance_amount = "0";
		// testdrive_fb_finance_comments = "";
		// testdrive_fb_insurance = "0";
		// testdrive_fb_insurance_comments = "";
		if (testdrive_fb_taken.equals("2") || testdrive_fb_status_id.equals("0")) {
			testdrive_fb_status_id = "0";
			testdrive_fb_status_comments = "";
		}
		// }
	}

	protected void CheckForm() {
		msg = "";
		if (Long.parseLong(testdrive_time) > Long.parseLong(ToLongDate(kknow()))) {
			msg = msg + "<br>Test Drive feedback can be given after " + (strToLongDate(testdrive_time)).replace("/", "-") + "!";
		}
		if (testdrive_fb_taken.equals("0")) {
			msg = msg + "<br>SELECT Test Drive Taken OR Not!";
		}
		// if (testdrive_fb_status_id.equals("0") && testdrive_fb_taken.equals("1")) {
		// msg = msg + "<br>SELECT Status!";
		// }
		// if (testdrive_fb_taken.equals("1")) {
		// if (!testdrive_fb_status_id.equals("0") && testdrive_fb_status_comments.equals("")) {
		// msg = msg + "<br>Enter Status Comments!";
		// }
		// if (testdrive_fb_status_id.equals("0") && testdrive_type.equals("1")) {
		// if (colcount < 1) {
		// msg = msg + "<br>SELECT atleast 3 Colours!";
		// }
		// if (delexp_date.equals("")) {
		// msg = msg + "<br>Enter Delivery expected Date!";
		// } else if (!isValidDateFormatShort(delexp_date)) {
		// msg = msg + "<br>Enter valid Delivery expected Date!";
		// } else {
		// testdrive_fb_delexp_date =
		// ConvertShortDateToStr(delexp_date);
		// }
		// if (!isNumeric(testdrive_fb_budget)) {
		// msg = msg + "<br>Budget is not numeric!";
		// }
		// if (testdrive_fb_finance.equals("-1")) {
		// msg = msg + "<br>SELECT Finance Required OR not!";
		// }
		// if (testdrive_fb_finance.equals("1")) {
		// if (testdrive_fb_finance_amount.equals("")) {
		// msg = msg + "<br>Enter Finance %age!";
		// }
		// if (Integer.parseInt(testdrive_fb_finance_amount) > 100) {
		// msg = msg + "<br>Finance %age cannot be greater than 100!";
		// }
		// }
		// if (testdrive_fb_finance.equals("0") &&
		// !testdrive_fb_finance_amount.equals("")) {
		// msg = msg + "<br>SELECT Finance as Required!";
		// }
		// if (testdrive_fb_finance.equals("0") &&
		// testdrive_fb_finance_comments.equals("")) {
		// msg = msg + "<br>Enter Finance Comments!";
		// }
		// if (testdrive_fb_insurance.equals("-1")) {
		// msg = msg + "<br>SELECT Insurance Required OR not!";
		// }
		// if (testdrive_fb_insurance.equals("0") &&
		// testdrive_fb_insurance_comments.equals("")) {
		// msg = msg + "<br>Enter Insurance Comments!";
		// }
		// }
		// }
		// if (testdrive_fb_taken.equals("2")) {
		// if (testdrive_fb_notes.equals("")) {
		// msg = msg + "<br>Enter reason for testdrive not taken in notes!";
		// }
		// }
		if (testdrive_fb_status_comments.length() > 1000) {
			testdrive_fb_status_comments = testdrive_fb_status_comments.substring(0, 999);
		}
		// if (testdrive_fb_insurance_comments.length() > 1000) {
		// testdrive_fb_insurance_comments =
		// testdrive_fb_insurance_comments.substring(0, 999);
		// }
		// if (testdrive_fb_finance_comments.length() > 1000) {
		// testdrive_fb_finance_comments =
		// testdrive_fb_finance_comments.substring(0, 999);
		// }
		if (testdrive_fb_notes.length() > 1000) {
			testdrive_fb_notes = testdrive_fb_notes.substring(0, 999);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " SET "
						+ "testdrive_fb_taken = " + testdrive_fb_taken + ", "
						// + "testdrive_fb_status_id = " + testdrive_fb_status_id + ", "
						// + "testdrive_fb_status_comments = '" + testdrive_fb_status_comments + "', "
						// + "testdrive_fb_budget = " + testdrive_fb_budget +
						// ", "
						// + "testdrive_fb_delexp_date = '" +
						// testdrive_fb_delexp_date + "', "
						// + "testdrive_fb_finance = '" + testdrive_fb_finance +
						// "',"
						// + "testdrive_fb_finance_amount = " +
						// CNumeric(testdrive_fb_finance_amount) + ", "
						// + "testdrive_fb_finance_comments = '" +
						// testdrive_fb_finance_comments + "', "
						// + "testdrive_fb_insurance = " +
						// testdrive_fb_insurance + ", "
						// + "testdrive_fb_insurance_comments = '" +
						// testdrive_fb_insurance_comments + "', "
						+ "testdrive_fb_notes = '" + testdrive_fb_notes + "', ";
				if (entry_by.equals("")) {
					StrSql = StrSql + "testdrive_fb_entry_id = '" + emp_id + "', "
							+ "testdrive_fb_entry_date = '" + ToLongDate(kknow()) + "', ";
				}
				StrSql = StrSql + "testdrive_fb_modified_id = " + testdrive_fb_modified_id + ", "
						+ "testdrive_fb_modified_date = '" + testdrive_fb_modified_date + "' "
						+ " WHERE testdrive_id = " + testdrive_id + " ";
				// SOP("StrSql=========" + StrSql);
				updateQuery(StrSql);
				SendSMS();
				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive_colour_trans"
				// + " WHERE trans_testdrive_id=" + testdrive_id;
				// updateQuery(StrSql);

				// type
				// UpdateTestdriveType();
				// SOP("testdrive_fb_taken = " + testdrive_fb_taken);

				// }
				// type
			} catch (Exception ex) {
				SOPError("Axelaauto-App== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT testdrive_fb_taken, testdrive_fb_budget, testdrive_fb_delexp_date, testdrive_fb_finance,"
					+ " testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
					+ " testdrive_fb_insurance, testdrive_fb_insurance_comments, testdrive_fb_notes, testdrive_fb_entry_id, "
					+ " testdrive_fb_entry_date, testdrive_fb_modified_id, testdrive_fb_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive "
					+ " WHERE testdrive_id=" + testdrive_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				testdrive_fb_taken = crs.getString("testdrive_fb_taken");
				// testdrive_fb_status_id = crs.getString("testdrive_fb_status_id");
				// testdrive_fb_status_comments = crs.getString("testdrive_fb_status_comments");
				// testdrive_fb_budget = crs.getString("testdrive_fb_budget");
				// delexp_date =
				// strToShortDate(crs.getString("testdrive_fb_delexp_date"));
				// testdrive_fb_finance = crs.getString("testdrive_fb_finance");
				// testdrive_fb_finance_amount =
				// crs.getString("testdrive_fb_finance_amount");
				// testdrive_fb_finance_comments =
				// crs.getString("testdrive_fb_finance_comments");
				// testdrive_fb_insurance =
				// crs.getString("testdrive_fb_insurance");
				// testdrive_fb_insurance_comments =
				// crs.getString("testdrive_fb_insurance_comments");
				testdrive_fb_notes = crs.getString("testdrive_fb_notes");
				testdrive_fb_entry_id = crs.getString("testdrive_fb_entry_id");
				if (!testdrive_fb_entry_id.equals("0")) {
					entry_by = Exename(comp_id, crs.getInt("testdrive_fb_entry_id"));
					entry_date = strToLongDate(crs.getString("testdrive_fb_entry_date"));
				}
				testdrive_fb_modified_id = crs.getString("testdrive_fb_modified_id");
				if (!testdrive_fb_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(testdrive_fb_modified_id));
					modified_date = strToLongDate(crs.getString("testdrive_fb_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTestDriveTaken() {
		String status = "";
		status = status + "<option value = 1" + Selectdrop(1, testdrive_fb_taken) + ">Taken</option>\n";
		status = status + "<option value = 2" + Selectdrop(2, testdrive_fb_taken) + ">Not Taken</option>\n";
		return status;
	}

	public String PopulateStatus() {
		String project = "";
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_sales_testdrive_status"
					+ " ORDER BY status_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				project = project + "<option value=" + crs.getString("status_id") + "";
				project = project + StrSelectdrop(crs.getString("status_id"), testdrive_fb_status_id);
				project = project + ">" + crs.getString("status_name") + "</option>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return project;
	}

	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT customer_id, customer_name, contact_id, enquiry_branch_id, location_name,"
					+ " testdrive_emp_id, concat(emp_name,' ',emp_mobile1) as emp_name,  enquiry_date,"
					+ " variant_id, variant_name, testdrive_type, testdrive_time,  testdrive_time_from, testdrive_time_to,"
					+ " testdrive_enquiry_id, branch_code, preownedmodel_id, preownedmodel_name, preowned_regno,"
					+ " concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
					+ " contact_mobile1,  concat('OPR',branch_code,enquiry_no) as enquiry_no"
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id=testdrive_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id=enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id=enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id=contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
					+ " WHERE testdrive_id = " + testdrive_id + BranchAccess + "";
			SOP("StrSql====feedback=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_customer_id = crs.getString("customer_id");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					contact_id = crs.getString("contact_id");
					testdrive_enquiry_id = crs.getString("testdrive_enquiry_id");
					testdrive_type = crs.getString("testdrive_type");
					customer_name = crs.getString("customer_name");
					enquiry_no = crs.getString("enquiry_no");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					enquiry_date = crs.getString("enquiry_date");
					location_name = crs.getString("location_name");
					preownedmodel_id = crs.getString("preownedmodel_id");
					variant_id = crs.getString("variant_id");
					variant_name = crs.getString("variant_name") + "-" + crs.getString("preowned_regno") + "";
					preownedmodel_name = crs.getString("preownedmodel_name");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = strToLongDate(crs.getString("testdrive_time"));
					if (!crs.getString("testdrive_time_from").equals("")) {
						testdrive_time_from = PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1");
					}
					executive_name = crs.getString("emp_name");
					contact_name = crs.getString("contact_name");
					contact_mobile1 = crs.getString("contact_mobile1");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("callurlapp-error.jsp?msg=Invalid Test Drive!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS() {
		String msg = "";
		if (config_sms_enable.equals("1") && !contact_mobile1.equals("") && testdrive_fb_taken.equals("1")) {
			msg = "Greetings from Audi Gurgaon! Dear " + contact_name + " Hope you had a wonderful Audi Drive Experience, For any further assistance please contact Sales Manager " + executive_name
					+ " .\nWarm Regards,\nAudi Gurgaon.";
			try {
				String Sql = "insert into " + compdb(comp_id) + "axela_sms"
						+ "(sms_mobileno,"
						+ "sms_msg,"
						+ "sms_date ,"
						+ "sms_sent ,"
						+ "sms_contact_id, "
						+ "sms_entry_id)"
						+ "values"
						+ "('" + contact_mobile1 + "',"
						+ "'" + msg + "',"
						+ "'" + ToLongDate(kknow()) + "',"
						+ "'0',"
						+ "'" + contact_id + "',"
						+ "'" + emp_id + "')";
				updateQuery(Sql);
			} catch (Exception ex) {
				SOPError("Axelaauto-App== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void getConfigFields() {
		StrSql = "SELECT config_sms_enable FROM " + compdb(comp_id) + "axela_config";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_sms_enable = crs.getString("config_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public void UpdateTestdriveType() {
	// try {
	// String count = "";
	// StrSql = "SELECT count(testdrive_id)\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_fb_taken = 1";
	// count = ExecuteQuery(StrSql);
	//
	// if (count.equals("1")) {
	// if (testdrive_fb_taken.equals("1")) {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_id != " + testdrive_id + "\n"
	// + "AND testdrive_type = 1";
	// // SOP("StrSql SELECT = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 2"
	// + " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	// } else {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_fb_taken = 1";
	// String newtestdriveid = ExecuteQuery(StrSql);
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " WHERE testdrive_id = " + newtestdriveid + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_id != " + newtestdriveid + "\n"
	// + "AND testdrive_type = 1";
	// // SOP("StrSql SELECT = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 2"
	// + " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	//
	// }
	// }
	// if (count.equals("0")) {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "";
	// // SOP("StrSql SELECT = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	//
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// }
	// }
	// public void UpdateTestdriveType() {
	// try {
	// String newtestdriveid = "";
	// String count = "";
	// StrSql = "SELECT count(testdrive_id)\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_fb_taken = 1";
	// count = ExecuteQuery(StrSql);
	//
	// if (count.equals("0")) {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "";
	// // SOP("StrSql select = " + StrSql);
	// CachedRowSet crs1 = processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	//
	// } else {
	// // if only 1 testdrive/demo is taken
	// if (count.equals("1")) {
	// if (testdrive_fb_taken.equals("1")) {
	// // says current testdrive/demo is taken
	// newtestdriveid = testdrive_id;
	// } else {
	// // get the testdrive_id WHERE testdrive/demo is taken
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_fb_taken = 1";
	// newtestdriveid = ExecuteQuery(StrSql);
	// }
	//
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " WHERE testdrive_id = " + newtestdriveid + " ";
	// updateQuery(StrSql);
	//
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "AND testdrive_id != " + newtestdriveid + "\n"
	// + "AND testdrive_type = 1";
	// CachedRowSet crs1 = processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 2"
	// + " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	//
	// }
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

}
