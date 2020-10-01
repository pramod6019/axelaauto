// smitha nag 13 feb 2013
// saiman 2nd may 2013
package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Feedback extends Connect {

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
	public String contact_id = "";
	public String enquiry_branch_id = "";
	public String enquiry_date = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String vehicle_name = "", testdriveveh_id = "";
	public String model_name = "";
	public String location_name = "";
	public String testdrive_time = "";
	public String testdrive_time_from = "";
	public String executive_name = "";
	public String testdrive_fb_taken = "";
	public String testdrive_doc_value = "";
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
	public String brand_id = "";
	// public String colHTML = "";
	public String config_sms_enable = "";
	public String comp_sms_enable = "";
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public int colcount = 0;
	public String item_id = "0";
	public String QueryString = "";
	public TestDrive_Update type = new TestDrive_Update();
	public String testdrive_license_no = "";
	public String testdrive_license_valid = "";

	public String branch_email = "";
	public String emp_email1 = "", emp_email2 = "";
	public String emp_mobile1 = "", emp_mobile2 = "";
	public String brandconfig_testdrive_feedback_email_enable = "";
	public String brandconfig_testdrive_feedback_email_sub = "";
	public String brandconfig_testdrive_feedback_email_format = "";
	public String brandconfig_testdrive_feedback_email_exe_enable = "";
	public String brandconfig_testdrive_feedback_email_exe_sub = "";
	public String brandconfig_testdrive_feedback_email_exe_format = "";
	public String brandconfig_testdrive_feedback_sms_enable = "";
	public String brandconfig_testdrive_feedback_sms_format = "";
	public String brandconfig_testdrive_feedback_sms_exe_enable = "";
	public String brandconfig_testdrive_feedback_sms_exe_format = "";
	public Connection conntx = null;
	Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				feedbackB = PadQuotes(request.getParameter("update_button"));
				// SOP("feedbackB===" + feedbackB);
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
							// SOP("entry_by====" + testdrive_fb_entry_id);
							testdrive_fb_entry_date = ToLongDate(kknow());
							UpdateFields(request);
							if (testdrive_fb_taken.equals("1") && msg.equals("")) {
								String date = ToLongDate(kknow());
								new Enquiry_Quickadd().AddCustomCRMFields(enquiry_id, date, "testdrive", comp_id);
							}
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								type.UpdateTestDriveType(testdrive_enquiry_id);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
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
		testdrive_fb_taken = PadQuotes(request.getParameter("dr_testdrivetaken"));
		testdrive_fb_status_id = PadQuotes(request.getParameter("dr_status"));

		testdrive_license_no = PadQuotes(request.getParameter("txt_testdrive_license_no"));
		testdrive_license_valid = PadQuotes(request.getParameter("txt_testdrive_license_valid"));

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
		// entry_date = PadQuotes(request.getParameter("entry_date"));
		// modified_by = PadQuotes(request.getParameter("modified_by"));
		// modified_date = PadQuotes(request.getParameter("modified_date"));
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
		// SOP("testdrive_time====" + Long.parseLong(testdrive_time));
		// SOP("curtime====" + Long.parseLong(ToLongDate(kknow())));
		if (Long.parseLong(testdrive_time) > Long.parseLong(ToLongDate(kknow()))) {
			msg = msg + "<br>Test Drive feedback can be given after " + strToLongDate(testdrive_time) + "!";
		}
		if (testdrive_fb_taken.equals("0")) {
			msg = msg + "<br>Select Test Drive Taken or Not Taken!";
		}
		if (testdrive_fb_status_id.equals("0") && testdrive_fb_taken.equals("1")) {
			msg = msg + "<br>Select Status!";
		}
		if (testdrive_fb_taken.equals("1")) {
			if (!testdrive_fb_status_id.equals("0") && testdrive_fb_status_comments.equals("")) {
				msg = msg + "<br>Enter Status Comments!";
			}

			// for JLR not mandatory
			if (!brand_id.equals("60")) {
				testdrive_doc_value = ExecuteQuery("SELECT testdrive_doc_value FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " WHERE testdrive_id = " + testdrive_id + "");
				if (testdrive_doc_value.equals("")) {
					msg = msg + "<br>Upload Driving Licence for Test Drive!";
				}
			}

			if (brand_id.equals("60") && testdrive_fb_taken.equals("1")) {
				if (testdrive_license_no.equals("0") || testdrive_license_no.equals("")) {
					msg = msg + "<br>Enter DL No!";
				}
				if (testdrive_license_valid.equals("")) {
					msg = msg + "<br>Enter License Valid Till!";
				} else {
					if (Long.parseLong(ConvertShortDateToStr(testdrive_license_valid))
					< Long.parseLong(ToShortDate(kknow()))) {
						msg = msg + "<br>License is already Expired!";
					}
				}

			}

			if (testdrive_fb_status_id.equals("0") && testdrive_type.equals("1")) {
				// if (colcount < 1) {
				// msg = msg + "<br>Select atleast 3 Colours!";
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
				// msg = msg + "<br>Select Finance Required or not!";
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
				// msg = msg + "<br>Select Finance as Required!";
				// }
				// if (testdrive_fb_finance.equals("0") &&
				// testdrive_fb_finance_comments.equals("")) {
				// msg = msg + "<br>Enter Finance Comments!";
				// }
				// if (testdrive_fb_insurance.equals("-1")) {
				// msg = msg + "<br>Select Insurance Required or not!";
				// }
				// if (testdrive_fb_insurance.equals("0") &&
				// testdrive_fb_insurance_comments.equals("")) {
				// msg = msg + "<br>Enter Insurance Comments!";
				// }
			}
		}
		if (testdrive_fb_taken.equals("2")) {
			if (testdrive_fb_notes.equals("")) {
				msg = msg + "<br>Enter reason for testdrive not taken in notes!";
			}
		}
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
		if (brand_id.equals("55") && !entry_by.equals("") && !emp_id.equals("1")) {
			// SOP("now====" + Long.parseLong(ToShortDate(kknow())));
			// SOP("test time====" + Long.parseLong(testdrive_time.substring(0, 8)));
			if (Long.parseLong(ToShortDate(kknow())) > Long.parseLong(testdrive_time.substring(0, 8))) {
				msg = msg + "<br>Test Drive feedback has to be given on Test Drive Date Only!";
			}
		}

	}

	protected void UpdateFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
						+ " SET "
						+ "testdrive_fb_taken = " + testdrive_fb_taken + ", "
						+ "testdrive_fb_status_id = " + testdrive_fb_status_id + ", "
						+ "testdrive_fb_status_comments = '" + testdrive_fb_status_comments + "', "
						+ "testdrive_license_no = '" + testdrive_license_no + "', "
						+ "testdrive_license_valid = '" + ConvertShortDateToStr(testdrive_license_valid) + "', "
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
						+ "WHERE testdrive_id = " + testdrive_id + " ";
				// SOP("StrSql query of updatefields---" + StrSql);

				updateQuery(StrSql);
				new Enquiry_Dash_Check().LastTimeUpdate(enquiry_id, comp_id);
				PopulateConfigDetails();

				if (comp_email_enable.equals("1") && config_email_enable.equals("1") && !branch_email.equals("")) {
					if (brandconfig_testdrive_feedback_email_enable.equals("1")) {
						if (!brandconfig_testdrive_feedback_email_sub.equals("")
								&& !brandconfig_testdrive_feedback_email_format.equals("")) {
							if (!branch_email.equals("") && !contact_email1.equals(""))
								SendFeedbackEmail(comp_id, "customer", contact_email1);
						}
					}

					if (brandconfig_testdrive_feedback_email_exe_enable.equals("1")) {
						if (!brandconfig_testdrive_feedback_email_exe_sub.equals("")
								&& !brandconfig_testdrive_feedback_email_exe_format.equals("")) {
							if (!branch_email.equals("") && !emp_email1.equals(""))
								SendFeedbackEmail(comp_id, "executive", emp_email1);
						}
					}
				}

				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
					if (brandconfig_testdrive_feedback_sms_enable.equals("1")
							&& !brandconfig_testdrive_feedback_sms_format.equals("")) {
						if (!contact_mobile1.equals("")) {
							SendFeedbackSMS(comp_id, "customer", contact_mobile1);
						}
						if (!contact_mobile2.equals("")) {
							SendFeedbackSMS(comp_id, "customer", contact_mobile2);
						}
					}

					if (brandconfig_testdrive_feedback_sms_exe_enable.equals("1")
							&& !brandconfig_testdrive_feedback_sms_exe_format.equals("")) {
						if (!emp_mobile1.equals("")) {
							SendFeedbackSMS(comp_id, "executive", emp_mobile1);
						}
						if (!emp_mobile2.equals("")) {
							SendFeedbackSMS(comp_id, "executive", emp_mobile2);
						}
					}
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive_colour_trans WHERE trans_testdrive_id = " + testdrive_id;
				updateQuery(StrSql);

				// type
				UpdateTestdriveType();
				// SOP("testdrive_fb_taken = " + testdrive_fb_taken);

				// }
				// type
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " enquiry_contact_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " testdrive_fb_taken,"
					+ " testdrive_fb_status_id,"
					+ " testdrive_license_no,"
					+ " testdrive_license_valid,"
					+ " testdrive_fb_status_comments ,"
					+ " testdrive_fb_budget, "
					+ " testdrive_fb_delexp_date,"
					+ " testdrive_fb_finance,"
					+ " testdrive_fb_finance_amount,"
					+ " testdrive_fb_finance_comments,  "
					+ " testdrive_fb_insurance,"
					+ " testdrive_fb_insurance_comments,"
					+ " testdrive_fb_notes,"
					+ " testdrive_fb_entry_id, "
					+ " testdrive_fb_entry_date,"
					+ " testdrive_fb_modified_id,"
					+ " testdrive_fb_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " WHERE testdrive_id = " + testdrive_id + "";
			// SOP("Populate Fields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				testdrive_fb_taken = crs.getString("testdrive_fb_taken");
				testdrive_fb_status_id = crs.getString("testdrive_fb_status_id");
				testdrive_license_no = crs.getString("testdrive_license_no");
				testdrive_license_valid = strToShortDate(crs.getString("testdrive_license_valid"));
				testdrive_fb_status_comments = crs.getString("testdrive_fb_status_comments");
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
				// SOP("testdrive_fb_modified_id===" + testdrive_fb_modified_id);
				if (!testdrive_fb_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(testdrive_fb_modified_id));
					modified_date = strToLongDate(crs.getString("testdrive_fb_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			StrSql = "select * from " + compdb(comp_id) + "axela_sales_testdrive_status order by status_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				project = project + "<option value=" + crs.getString("status_id") + "";
				project = project + StrSelectdrop(crs.getString("status_id"), testdrive_fb_status_id);
				project = project + ">" + crs.getString("status_name") + "</option>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return project;
	}

	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " enquiry_id,"
					+ " customer_id,"
					+ " customer_name,"
					+ " contact_id,"
					+ " enquiry_item_id,"
					+ " enquiry_branch_id,"
					+ " location_name, "
					+ " testdrive_emp_id,"
					+ " concat(emp_name,' ',emp_mobile1) AS emp_name, "
					+ " enquiry_date,"
					+ " testdriveveh_name,"
					+ " testdrive_type,"
					+ " testdrive_time, "
					+ " testdrive_time_from,"
					+ " testdrive_time_to,"
					+ " testdrive_enquiry_id,"
					+ " branch_code, "
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " CONCAT('ENQ',branch_code,enquiry_no) AS enquiry_no,"
					+ " testdriveveh_id,"
					+ " model_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id = testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_testdriveveh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
					+ " WHERE testdrive_id = " + testdrive_id + BranchAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_id = crs.getString("enquiry_id");
					testdrive_customer_id = crs.getString("customer_id");
					contact_id = crs.getString("contact_id");
					testdrive_enquiry_id = crs.getString("testdrive_enquiry_id");
					testdrive_type = crs.getString("testdrive_type");
					customer_name = crs.getString("customer_name");
					enquiry_no = crs.getString("enquiry_no");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					enquiry_date = crs.getString("enquiry_date");
					location_name = crs.getString("location_name");
					// model_id = crs.getString("model_id");
					brand_id = crs.getString("model_brand_id");
					item_id = crs.getString("enquiry_item_id");
					testdriveveh_id = crs.getString("testdriveveh_id");
					vehicle_name = crs.getString("testdriveveh_name");
					// model_name = crs.getString("model_name");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = strToLongDate(crs.getString("testdrive_time"));
					if (!crs.getString("testdrive_time_from").equals("")) {
						testdrive_time_from = PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1");
					}
					executive_name = crs.getString("emp_name");
					contact_name = crs.getString("contact_name");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");

					if (!contact_email2.equals("")) {
						contact_email1 = contact_email1 + "," + contact_email2;
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	/*
	 * protected void SendSMS() { String msg = ""; if (config_sms_enable.equals("1") && !contact_mobile1.equals("") && testdrive_fb_taken.equals("1")) { msg = "Greetings from DD Motors! Dear " +
	 * contact_name + " Hope you had a wonderful Maruti Drive Experience, For any further assistance please contact Sales Manager " + executive_name + " .\nWarm Regards,\nDD Motocrs."; try { String
	 * Sql = "INSERT INTO " + compdb(comp_id) + "axela_sms" + "(sms_branch_id," + "sms_mobileno," + "sms_msg," + "sms_date ," + "sms_sent ," + "sms_contact_id, " // + "sms_customer_id ," +
	 * "sms_entry_id)" + "VALUES" + "(" + branch_id + "," + "'" + contact_mobile1 + "'," + "'" + msg + "'," + "'" + ToLongDate(kknow()) + "'," + "'0'," + "'" + contact_id + "'," // + "'" +
	 * testdrive_customer_id + "'," + "'" + emp_id + "')"; updateQuery(Sql); } catch (Exception ex) { SOPError("Axelaauto== " + this.getClass().getName()); SOPError("Error in " + new
	 * Exception().getStackTrace()[0].getMethodName() + ": " + ex); } } }
	 */

	public void getConfigFields() {
		StrSql = "SELECT config_sms_enable FROM " + compdb(comp_id) + "axela_config";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_sms_enable = crs.getString("config_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(brandconfig_testdrive_feedback_email_enable, '') AS brandconfig_testdrive_feedback_email_enable,"
					+ " COALESCE(brandconfig_testdrive_feedback_email_sub, '') AS brandconfig_testdrive_feedback_email_sub,"
					+ " COALESCE(brandconfig_testdrive_feedback_email_format, '') AS brandconfig_testdrive_feedback_email_format,"
					+ " COALESCE(brandconfig_testdrive_feedback_email_exe_enable, '') AS brandconfig_testdrive_feedback_email_exe_enable,"
					+ " COALESCE(brandconfig_testdrive_feedback_email_exe_sub, '') AS brandconfig_testdrive_feedback_email_exe_sub,"
					+ " COALESCE(brandconfig_testdrive_feedback_email_exe_format, '') AS brandconfig_testdrive_feedback_email_exe_format,"
					+ " COALESCE(brandconfig_testdrive_feedback_sms_enable, '') AS brandconfig_testdrive_feedback_sms_enable,"
					+ " COALESCE(brandconfig_testdrive_feedback_sms_format, '') AS brandconfig_testdrive_feedback_sms_format,"
					+ " COALESCE(brandconfig_testdrive_feedback_sms_exe_enable, '') AS brandconfig_testdrive_feedback_sms_exe_enable,"
					+ " COALESCE(brandconfig_testdrive_feedback_sms_exe_format, '') AS  brandconfig_testdrive_feedback_sms_exe_format,"
					+ " config_sms_enable,"
					+ " comp_sms_enable,"
					+ " config_email_enable,"
					+ " comp_email_enable,"
					+ " branch_email1,"
					+ " COALESCE(emp.emp_email1,'') AS emp_email1,"
					+ " COALESCE(emp.emp_email2,'') AS emp_email2,"
					+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1,"
					+ " COALESCE(emp.emp_mobile2,'') AS emp_mobile2"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + enquiry_branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + "";
			// SOP("PopulateConfigDetails = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				branch_email = crs.getString("branch_email1");

				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");

				if (!emp_email2.equals("")) {
					emp_email1 = emp_email1 + "," + emp_email2;
				}

				brandconfig_testdrive_feedback_email_enable = crs.getString("brandconfig_testdrive_feedback_email_enable");
				brandconfig_testdrive_feedback_email_sub = crs.getString("brandconfig_testdrive_feedback_email_sub");
				brandconfig_testdrive_feedback_email_format = crs.getString("brandconfig_testdrive_feedback_email_format");
				brandconfig_testdrive_feedback_email_exe_enable = crs.getString("brandconfig_testdrive_feedback_email_exe_enable");
				brandconfig_testdrive_feedback_email_exe_sub = crs.getString("brandconfig_testdrive_feedback_email_exe_sub");
				brandconfig_testdrive_feedback_email_exe_format = crs.getString("brandconfig_testdrive_feedback_email_exe_format");
				brandconfig_testdrive_feedback_sms_enable = crs.getString("brandconfig_testdrive_feedback_sms_enable");
				brandconfig_testdrive_feedback_sms_format = crs.getString("brandconfig_testdrive_feedback_sms_format");
				brandconfig_testdrive_feedback_sms_exe_enable = crs.getString("brandconfig_testdrive_feedback_sms_exe_enable");
				brandconfig_testdrive_feedback_sms_exe_format = crs.getString("brandconfig_testdrive_feedback_sms_exe_format");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				// SOP("branch_email==" + branch_email);
				// SOP("emp_email1==" + emp_email1);
				// SOP("emp_email2==" + emp_email2);
				// SOP("emp_mobile1==" + emp_mobile1);
				// SOP("emp_mobile2==" + emp_mobile2);
				// SOP("brandconfig_testdrive_feedback_email_enable==" + brandconfig_testdrive_feedback_email_enable);
				// SOP("brandconfig_testdrive_feedback_email_sub==" + brandconfig_testdrive_feedback_email_sub);
				// SOP("brandconfig_testdrive_feedback_email_format==" + brandconfig_testdrive_feedback_email_format);
				// SOP("brandconfig_testdrive_feedback_email_exe_enable==" + brandconfig_testdrive_feedback_email_exe_enable);
				// SOP("brandconfig_testdrive_feedback_email_exe_sub==" + brandconfig_testdrive_feedback_email_exe_sub);
				// SOP("brandconfig_testdrive_feedback_email_exe_format==" + brandconfig_testdrive_feedback_email_exe_format);
				// SOP("brandconfig_testdrive_feedback_sms_enable==" + brandconfig_testdrive_feedback_sms_enable);
				// SOP("brandconfig_testdrive_feedback_sms_format==" + brandconfig_testdrive_feedback_sms_format);
				// SOP("brandconfig_testdrive_feedback_sms_exe_enable==" + brandconfig_testdrive_feedback_sms_exe_enable);
				// SOP("brandconfig_testdrive_feedback_sms_exe_format==" + brandconfig_testdrive_feedback_sms_exe_format);
				// SOP("config_sms_enable==" + config_sms_enable);
				// SOP("comp_sms_enable==" + comp_sms_enable);
				// SOP("config_email_enable==" + config_email_enable);
				// SOP("comp_email_enable==" + comp_email_enable);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendFeedbackEmail(String comp_id, String type, String email_to) throws SQLException {
		String emailmsg = "", sub = "", email_contact_id = "", email_contact_name = "";
		if (type.equals("customer")) {
			email_contact_id = contact_id;
			email_contact_name = contact_name;
			sub = brandconfig_testdrive_feedback_email_sub;
			emailmsg = brandconfig_testdrive_feedback_email_format;
		}
		else if (type.equals("executive")) {
			email_contact_id = "0";
			email_contact_name = "";
			sub = brandconfig_testdrive_feedback_email_exe_sub;
			emailmsg = brandconfig_testdrive_feedback_email_exe_format;
		}

		sub = "replace('" + sub + "','[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',testdriveveh_name)";
		sub = "replace(" + sub + ",'[DATE]',DATE_FORMAT('" + testdrive_time + "', '%d/%m/%Y'))";
		sub = "replace(" + sub + ",'[TIME]',DATE_FORMAT('" + testdrive_time + "', '%H:%i %p'))";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "REPLACE(" + sub + ", '[BRANCHADDRESS]',branch_add)";
		sub = "REPLACE(" + sub + ", '[LOCATION]',location_name)";

		emailmsg = "replace('" + emailmsg + "','[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',testdriveveh_name)";
		emailmsg = "replace(" + emailmsg + ",'[DATE]',DATE_FORMAT('" + testdrive_time + "', '%d/%m/%Y'))";
		emailmsg = "replace(" + emailmsg + ",'[TIME]',DATE_FORMAT('" + testdrive_time + "', '%H:%i %p'))";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";
		emailmsg = "REPLACE(" + emailmsg + ", '[LOCATION]',location_name)";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_branch_id + ","
					+ " contact_id,"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + branch_email + "', "
					+ " '" + email_to + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ","
					+ " 0"
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
					+ " WHERE testdrive_id = " + testdrive_id
					+ " LIMIT 1";
			// SOP("select-sms-" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					// + " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql--Feedback-------mail--" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendFeedbackSMS(String comp_id, String type, String sms_to) throws SQLException {
		String smsmsg = "", sms_contact_id = "", sms_contact_name = "";
		if (type.equals("customer")) {
			sms_contact_id = contact_id;
			sms_contact_name = contact_name;
			smsmsg = brandconfig_testdrive_feedback_sms_format;

		}
		else if (type.equals("executive")) {
			sms_contact_id = "0";
			sms_contact_name = "";
			smsmsg = brandconfig_testdrive_feedback_sms_exe_format;

		}
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
			// SOP("INSERT-sms-" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public void UpdateTestdriveType() {
	// try {
	// String count = "";
	// StrSql = "SELECT count(testdrive_id)\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "and testdrive_fb_taken = 1";
	// count = ExecuteQuery(StrSql);
	//
	// if (count.equals("1")) {
	// if (testdrive_fb_taken.equals("1")) {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "and testdrive_id != " + testdrive_id + "\n"
	// + "and testdrive_type = 1";
	// // SOP("StrSql select = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 2"
	// + " where testdrive_id = " + crs1.getString("testdrive_id") + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// }
	// crs1.close();
	// } else {
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "and testdrive_fb_taken = 1";
	// String newtestdriveid = ExecuteQuery(StrSql);
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " where testdrive_id = " + newtestdriveid + " ";
	// // SOP("StrSql update = " + StrSql);
	// updateQuery(StrSql);
	// StrSql = "SELECT testdrive_id\n"
	// + "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
	// + "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
	// + "and testdrive_id != " + newtestdriveid + "\n"
	// + "and testdrive_type = 1";
	// // SOP("StrSql select = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 2"
	// + " where testdrive_id = " + crs1.getString("testdrive_id") + " ";
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
	// // SOP("StrSql select = " + StrSql);
	// CachedRowSet crs1 =processQuery(StrSql, 0);
	// while (crs1.next()) {
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
	// + " SET"
	// + " testdrive_type = 1"
	// + " where testdrive_id = " + crs1.getString("testdrive_id") + " ";
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
	public void UpdateTestdriveType() {
		try {
			String newtestdriveid = "";
			String count = "";
			StrSql = "SELECT count(testdrive_id)\n"
					+ "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
					+ "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
					+ "AND testdrive_fb_taken = 1";
			count = ExecuteQuery(StrSql);

			if (count.equals("0")) {
				StrSql = "SELECT testdrive_id\n"
						+ "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
						+ "WHERE testdrive_enquiry_id = " + enquiry_id + "";
				// SOP("StrSql select = " + StrSql);
				CachedRowSet crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
							+ " SET"
							+ " testdrive_type = 1"
							+ " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
					// SOP("StrSql update = " + StrSql);
					updateQuery(StrSql);
				}
				crs1.close();

			} else {
				// if only 1 testdrive/demo is taken
				if (count.equals("1")) {
					if (testdrive_fb_taken.equals("1")) {
						// says current testdrive/demo is taken
						newtestdriveid = testdrive_id;
					} else {
						// get the testdrive_id where testdrive/demo is taken
						StrSql = "SELECT testdrive_id\n"
								+ "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
								+ "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
								+ "AND testdrive_fb_taken = 1";
						newtestdriveid = ExecuteQuery(StrSql);
					}

					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
							+ " SET"
							+ " testdrive_type = 1"
							+ " WHERE testdrive_id = " + newtestdriveid + " ";
					updateQuery(StrSql);

					StrSql = "SELECT testdrive_id\n"
							+ "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
							+ "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
							+ "AND testdrive_id != " + newtestdriveid + "\n"
							+ "AND testdrive_type = 1";
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
								+ " SET"
								+ " testdrive_type = 2"
								+ " WHERE testdrive_id = " + crs1.getString("testdrive_id") + " ";
						// SOP("StrSql update = " + StrSql);
						updateQuery(StrSql);
					}
					crs1.close();

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
