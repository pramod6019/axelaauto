package axela.axelaauto_app;

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

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Preowned_Testdrive_Add extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "", msgerror = "", followup_msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String BranchAccess = "", branch_id = "";
	public String ExeAccess = "";
	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String testdrive_id = "";
	public String testdrive_preownedstock_id = "";
	public String testdrive_veh_id = "0";
	public String testdrive_emp_id = "";
	public String testdrive_location_id = "0", testdrivezone_id = "0";
	public String testdrive_type = "";
	public String testdrive_time = "", testdrive_time_from = "", testdrive_time_to = "";
	public String testdrive_confirmed = "", unconfirm = "";
	public String testdrive_notes = "", testdrive_sendemail = "";
	public String testdrive_entry_id = "";
	public String testdrive_entry_date = "";
	public String testdrive_enquiry_id = "";
	public String testdrive_modified_id = "";
	public String testdrive_modified_date = "";
	public String enquiry_branch_id = "0";
	public String enquiry_model_id = "";
	public String testdrive_customer_id = "";
	public String customer_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
	public String model_name = "";
	public String executive_name = "";
	public String strHTML = "";
	public String enquiry_no = "", enquiry_date = "", enquiry_name = "";
	public String testdrivedate = "";
	public String testdrivedate1 = "";
	public String testdrivetime = "";
	public String testdrive_contact_id = "";
	public String contact_name = "";
	public String item_name = "";
	public String emp_email1;
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String branch_testdrive_email_enable = "";
	public String branch_testdrive_email_sub = "";
	public String branch_testdrive_email_format = "";
	public String branch_testdrive_sms_enable = "";
	public String branch_testdrive_sms_format = "";
	public String contact_email1 = "", contact_mobile1 = "";
	public String contact_email2 = "", contact_mobile2 = "";
	public String comp_id = "0";
	public String send_contact_email = "", followup_time = "", testdrive_fb_taken = "";
	public String day = "", month = "", year = "", branch_brand_id = "0";
	public String emp_uuid = "", access = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				access = ReturnPerm(comp_id, "emp_testdrive_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				msgerror = unescapehtml(PadQuotes(request.getParameter("msgerror")));
				testdrive_enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				unconfirm = PadQuotes(request.getParameter("unconfirm"));
				followup_time = PadQuotes(request.getParameter("followup_time"));
				followup_time = strToLongDate(followup_time);
				if (!testdrive_enquiry_id.equals(""))
				{
					if (!add.equals("yes")) {
						PopulateFields(response);
					}
					if (add.equals("yes")) {
						status = "Add";
					} else if (update.equals("yes")) {
						status = "Update";
					}
					followup_msg = msg;
					PreownedEnquiryDetails(comp_id, response);
					// PopulateConfigDetails(comp_id);
					if ("yes".equals(add)) {
						if (!"yes".equals(addB)) {
							msg = followup_msg;
							testdrive_preownedstock_id = "";
							testdrive_location_id = "";
							testdrive_type = "1";
							testdrive_confirmed = "1";
							testdrive_notes = "";
							String str = ToLongDate(kknow());
							if (followup_time.equals("")) {
								testdrivedate = SplitDate(str) + "/" + SplitMonth(str) + "/" + SplitYear(str) + " " + SplitHourMin(str);
								if (!testdrivedate.equals(""))
								{
									testdrivedate = testdrivedate.substring(0, 10);
									day = testdrivedate.substring(0, 2);
									month = testdrivedate.substring(3, 5);
									year = testdrivedate.substring(6);
									testdrivedate = day + "/" + month + "/" + year;
								}
							} else {
								testdrivedate = followup_time;
							}
						} else {
							GetValues(request, response);
							testdrive_entry_id = session.getAttribute("emp_id").toString();
							testdrive_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect("callurlapp-preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id
										+ "&msg=Test Drive added successfully.");
							}

						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		testdrive_id = PadQuotes(request.getParameter("testdrive_id"));
		testdrive_type = PadQuotes(request.getParameter("dr_testdrivetype"));
		testdrive_preownedstock_id = PadQuotes(request.getParameter("dr_preownedstock_id"));
		testdrive_location_id = PadQuotes(request.getParameter("dr_location"));
		testdrivedate = PadQuotes(request.getParameter("txt_testdrive_date"));
		testdrivedate = PadQuotes(request.getParameter("txt_testdrive_date"));
		testdrivetime = PadQuotes(request.getParameter("txt_testdrive_time"));
		if (!testdrivedate.equals("")) {
			testdrivedate = testdrivedate + " " + testdrivetime;
		}
		testdrive_confirmed = PadQuotes(request.getParameter("chk_testdrive_confirmed"));
		if (testdrive_confirmed.equals("on")) {
			testdrive_confirmed = "1";
		} else {
			testdrive_confirmed = "0";
		}
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
		StrSql = "SELECT location_leadtime, location_testdrive_dur "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_location "
				+ " WHERE location_id=" + testdrive_location_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Date start = AddHoursDate(StringToDate(testdrive_time), 0, 0, -crs.getDouble("location_leadtime"));
				testdrive_time_from = ToLongDate(start);
				Date end = AddHoursDate(StringToDate(testdrive_time), 0, 0, crs.getDouble("location_testdrive_dur"));
				testdrive_time_to = ToLongDate(end);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// if (model_name.equals("")) {
		// msg = msg + "<br>Select Model!";
		// }
		// if (testdrive_veh_id.equals("0")) {
		// msg = msg + "<br>Select Vehicleee!";
		// }
		// if (testdrive_emp_id.equals("0")) {
		// msg = msg + "<br>Select Sales Consultant!";
		// }
		if (testdrivedate.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatLong(testdrivedate)) {
				if (!testdrive_time_from.equals("") && !testdrive_time_to.equals("")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "SELECT testdrive_id FROM " + compdb(comp_id) + "axela_preowned_testdrive"
								+ " WHERE testdrive_confirmed='1'"
								+ " AND testdrive_preownedstock_id=" + testdrive_preownedstock_id
								+ " AND "
								+ " ((testdrive_time_from >= " + testdrive_time_from + " AND testdrive_time_from < " + testdrive_time_to + ")"
								+ " OR (testdrive_time_to > " + testdrive_time_from + " AND testdrive_time_to <= " + testdrive_time_to + ") "
								+ " OR (testdrive_time_from >= " + testdrive_time_from + " AND testdrive_time_to <= " + testdrive_time_to + ") "
								+ " OR (testdrive_time_from <= " + testdrive_time_from + " AND testdrive_time_to >= " + testdrive_time_to + "))";
						if (!update.equals("")) {
							StrSql = StrSql + " AND testdrive_id!=" + testdrive_id;
						}
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Vehicle is occupied by other Test Drives!";
						}
					}
				}
			} else {
				msg = msg + "<br>Enter Valid testdrive Date!";
			}
		}

		if (testdrive_notes.length() > 8000) {
			testdrive_notes = testdrive_notes.substring(0, 7999);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		ResultSet rs = null;
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_testdrive"
						+ "("
						+ "testdrive_enquiry_id,"
						+ "testdrive_preownedstock_id, "
						+ "testdrive_emp_id, "
						+ "testdrive_location_id,"
						+ "testdrive_type,"
						+ "testdrive_time,"
						+ "testdrive_time_from,"
						+ "testdrive_time_to,"
						+ "testdrive_confirmed,"
						+ "testdrive_notes,"
						+ "testdrive_entry_id,"
						+ "testdrive_entry_date"
						+ ") "
						+ "values	"
						+ "("
						+ "" + testdrive_enquiry_id + ","
						+ "'" + testdrive_preownedstock_id + "',"
						+ "" + testdrive_emp_id + ","
						+ "'" + testdrive_location_id + "',"
						+ "" + testdrive_type + ","
						+ "'" + testdrive_time + "',"
						+ "'" + testdrive_time_from + "',"
						+ "'" + testdrive_time_to + "',"
						+ "'" + testdrive_confirmed + "',"
						+ "'" + testdrive_notes + "',"
						+ "'" + testdrive_entry_id + "',"
						+ "'" + testdrive_entry_date + "'"
						+ ")";
				// SOP("Trsql===" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					testdrive_id = rs.getString(1);
				}
				rs.close();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_stage_id=4"
						+ " WHERE enquiry_id=" + testdrive_enquiry_id;
				stmttx.execute(StrSql);

				// start - sending email n sms
				// if (!contact_email2.equals("") && !contact_email1.equals(""))
				// {
				// send_contact_email = contact_email1 + "," + contact_email2;
				// } else if (!contact_email1.equals("")) {
				// send_contact_email = contact_email1;
				// }

				// if (testdrive_sendemail.equals("1")) {
				// // **** email
				// if (comp_email_enable.equals("1")
				// && config_email_enable.equals("1")
				// && !emp_email1.equals("")
				// && branch_testdrive_email_enable.equals("1")) {
				//
				// if (!send_contact_email.equals("")
				// && !branch_testdrive_email_format.equals("")
				// && !branch_testdrive_email_sub.equals("")) {
				// SendEmail();
				// }
				// }
				//
				// // **** sms
				// // if (comp_sms_enable.equals("1") &&
				// config_sms_enable.equals("1") &&
				// branch_testdrive_sms_enable.equals("1")) {
				// // if (!branch_testdrive_sms_format.equals("")) {
				// // if (!contact_mobile1.equals("")) {
				// // SendSMS(contact_mobile1);
				// // }
				// // if (!contact_mobile2.equals("")) {
				// // SendSMS(contact_mobile2);
				// // }
				// // }
				// // }
				// }
				// eof - sending email n sms

				// stmttx.execute(StrSql);

				// if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && branch_testdrive_sms_enable.equals("1")) {
				// if (!branch_testdrive_sms_format.equals("") && !contact_mobile1.equals("") && testdrive_confirmed.equals("1")) {
				// SendSMS(contact_mobile1);
				// }
				// }
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Axelaauto-App===" + this.getClass().getName());
					SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
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

	// protected void SendEmail() throws SQLException {
	// String emailmsg, sub;
	// sub = "replace(branch_testdrive_email_sub, '[TESTDRIVEID]',testdrive_id)";
	// sub = "replace(" + sub + ",'[TESTDRIVETIME]',DATE_FORMAT(testdrive_time, '%d/%m/%Y at %h:%i %p'))";
	// sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
	// sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
	// sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
	// sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
	// sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
	// sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
	// sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
	// sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
	// sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
	// sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";
	// sub = "replace(" + sub + ",'[DAY]',DAYNAME(testdrive_time))";
	// sub = "replace(" + sub + ",'[DATE]',DATE_FORMAT(testdrive_time, '%d/%m/%Y'))";
	// sub = "replace(" + sub + ",'[TIME]',DATE_FORMAT(testdrive_time, '%h:%i'))";
	// sub = "replace(" + sub + ",'[LOCATION]',location_name)";
	//
	// emailmsg = "replace(branch_testdrive_email_format, '[TESTDRIVEID]',testdrive_id)";
	// emailmsg = "replace(" + emailmsg + ",'[TESTDRIVETIME]',DATE_FORMAT(testdrive_time, '%d/%m/%Y at %h:%i %p'))";
	// emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
	// emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
	// emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
	// emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
	// emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
	// emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
	// emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
	// emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
	// emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
	// emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";
	// emailmsg = "replace(" + emailmsg + ",'[DAY]',DAYNAME(testdrive_time))";
	// emailmsg = "replace(" + emailmsg + ",'[DATE]',DATE_FORMAT(testdrive_time, '%d/%m/%Y'))";
	// emailmsg = "replace(" + emailmsg + ",'[TIME]',DATE_FORMAT(testdrive_time, '%h:%i'))";
	// emailmsg = "replace(" + emailmsg + ",'[LOCATION]',location_name)";
	//
	// try {
	// StrSql = "SELECT"
	// + " '" + testdrive_contact_id + "',"
	// + " '" + contact_name + "',"
	// + " '" + emp_email1 + "',"
	// + " '" + send_contact_email + "',"
	// + " " + (sub) + ","
	// + " " + (emailmsg) + ","
	// + " '" + ToLongDate(kknow()) + "',"
	// + " " + emp_id + ","
	// + " 0"
	// + " FROM " + compdb(comp_id) + "axela_sales_testdrive "
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_veh_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
	// + " WHERE testdrive_id = " + testdrive_id + ""
	// + " GROUP BY testdrive_id LIMIT 1";
	//
	// StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
	// + " (email_contact_id,"
	// + " email_contact,"
	// + " email_from,"
	// + " email_to,"
	// + " email_subject,"
	// + " email_msg,"
	// + " email_date,"
	// + " email_entry_id,"
	// + " email_sent)"
	// + " " + StrSql + "";
	// // SOP("StrSql-------axela_email---" + StrSqlBreaker(StrSql));
	// stmttx.execute(StrSql);
	// } catch (Exception ex) {
	// if (conntx.isClosed()) {
	// msg = "<br>Transaction Error!";
	// SOPError("conn is closed.....");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// msg = "<br>Transaction Error!";
	// SOPError("Connection rollback...");
	// }
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	// protected void SendSMS(String contact_mobile) throws SQLException {
	// String smsmsg = "";
	// smsmsg = "replace(branch_testdrive_sms_format, '[TESTDRIVEID]',testdrive_id)";
	// smsmsg = "replace(" + smsmsg + ",'[TESTDRIVETIME]',DATE_FORMAT(testdrive_time, '%d/%m/%Y at %h:%i %p'))";
	// smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
	// smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
	// smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
	// smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
	// smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
	// smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
	// smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
	// smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
	// smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
	// smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";
	// smsmsg = "replace(" + smsmsg + ",'[LOCATION]',location_name)";
	// smsmsg = "replace(" + smsmsg + ",'[DAY]',DAYNAME(testdrive_time))";
	// smsmsg = "replace(" + smsmsg + ",'[DATE]',DATE_FORMAT(testdrive_time, '%d/%m/%Y'))";
	// smsmsg = "replace(" + smsmsg + ",'[TIME]',DATE_FORMAT(testdrive_time, '%h:%i'))";
	// smsmsg = "replace(" + smsmsg + ",'[LOCATION]',location_name)";
	// try {
	// StrSql = "SELECT"
	// + " " + testdrive_contact_id + ","
	// + " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
	// + " '" + contact_mobile + "',"
	// + " " + (smsmsg) + ","
	// + " '" + ToLongDate(kknow()) + "',"
	// + " 0,"
	// + " " + emp_id + ""
	// + " FROM " + compdb(comp_id) + "axela_sales_testdrive "
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_veh_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
	// + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
	// + " WHERE testdrive_id = " + testdrive_id + ""
	// + " GROUP BY testdrive_id LIMIT 1";
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
	// + " (sms_contact_id,"
	// + " sms_contact,"
	// + " sms_mobileno,"
	// + " sms_msg,"
	// + " sms_date,"
	// + " sms_sent,"
	// + " sms_entry_id)"
	// + " " + StrSql + "";
	// // SOP("StrSql-------axela_sms---" + StrSqlBreaker(StrSql));
	// stmttx.execute(StrSql);
	// } catch (Exception ex) {
	// if (conntx.isClosed()) {
	// msg = "<br>Transaction Error!";
	// SOPError("conn is closed.....");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// msg = "<br>Transaction Error!";
	// }
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
						+ " SET "
						+ " testdrive_veh_id= '" + testdrive_veh_id + "', "
						+ " testdrive_emp_id = '" + testdrive_emp_id + "', "
						+ " testdrive_location_id = '" + testdrive_location_id + "', "
						+ " testdrive_time = '" + testdrive_time + "', "
						+ " testdrive_time_from = '" + testdrive_time_from + "', "
						+ " testdrive_time_to = '" + testdrive_time_to + "', "
						+ " testdrive_type = " + testdrive_type + ", "
						+ " testdrive_sendemail = '" + testdrive_sendemail + "', "
						+ " testdrive_confirmed = '" + testdrive_confirmed + "', "
						+ " testdrive_notes = '" + testdrive_notes + "', "
						+ " testdrive_modified_id = '" + testdrive_modified_id + "', "
						+ " testdrive_modified_date = '" + testdrive_modified_date + "' "
						+ " WHERE testdrive_id = " + testdrive_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "SELECT testdrive_doc_value FROM " + compdb(comp_id) + "axela_sales_testdrive WHERE testdrive_id = " + testdrive_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(TestDriveDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive_colour_trans WHERE trans_colour_id =" + testdrive_id + "";
				updateQuery(StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive WHERE testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT testdrive_preownedstock_id, testdrive_emp_id,testdrive_time, testdrive_time_from, testdrive_type,"
					+ " testdrive_time_to, testdrive_location_id, testdrive_confirmed, testdrive_notes, testdrive_entry_id,"
					+ " testdrive_entry_date, testdrive_modified_id, testdrive_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " WHERE testdrive_id=" + testdrive_id + "";
			// SOP("StrSql------PopulateFields-------" +
			// StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_preownedstock_id = crs.getString("testdrive_preownedstock_id");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = crs.getString("testdrive_time_from");
					testdrive_time_to = crs.getString("testdrive_time_to");
					testdrivedate = SplitDate(testdrive_time) + "/" + SplitMonth(testdrive_time) + "/" + SplitYear(testdrive_time) + " " + SplitHourMin(testdrive_time);
					testdrive_location_id = crs.getString("testdrive_location_id");
					testdrive_type = crs.getString("testdrive_type");
					testdrive_confirmed = crs.getString("testdrive_confirmed");
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
				response.sendRedirect(response.encodeRedirectURL("../portal/app-error.jsp?msg=Invalid Test drive"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	/*
	 * public String PopulateZone() { try { StringBuilder Str = new StringBuilder(); StrSql = " SELECT testdrivezone_name, testdrivezone_id" + " FROM axela_sales_testdrive_zone " +
	 * " WHERE testdrivezone_active='1' " + " ORDER BY testdrivezone_name "; CachedRowSet crs =processQuery(StrSql, 0); Str.append("<option value=0>Select</option>");
	 * 
	 * while (crs.next()) { Str.append("<option value=").append(crs.getString("testdrivezone_id")); Str.append(Selectdrop(crs.getInt("testdrivezone_id"), testdrivezone_id)); Str
	 * .append(">").append(crs.getString("testdrivezone_name")).append("</option> \n" ); } crs.close(); return Str.toString(); } catch (Exception ex) { SOPError("Axelaauto-App===" +
	 * this.getClass().getName()); SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex); return ""; } }
	 */

	public String PopulateVehicle(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT veh_id, veh_name"
					// + " ,model_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id=veh_item_id "
					+ " WHERE veh_branch_id= " + CNumeric(enquiry_branch_id) + ""
					+ " AND item_model_id=" + CNumeric(enquiry_model_id) + ""
					+ " AND veh_active=1 "
					+ " order by  veh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_vehicle\" class=form-control id=\"dr_vehicle\" onChange=\"testdriveCheck()\">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("veh_id")).append("");
				Str.append(StrSelectdrop(crs.getString("veh_id"), testdrive_veh_id));
				Str.append(">").append(crs.getString("veh_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVariant() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedstock_id, carmanuf_name, preownedmodel_name, variant_name, preowned_regno "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id  = preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1"
					// + " AND preowned_branch_id = " + CNumeric(enquiry_branch_id) + ""
					// + " AND preowned_preownedmodel_id = " + CNumeric(enquiry_model_id) + ""
					+ " and preownedstock_id not in"
					+ " (SELECT so_preownedstock_id from " + compdb(comp_id) + "axela_sales_so where so_active=1)";
			if (add.equals("yes")) {
				StrSql = StrSql + " AND preownedstock_blocked=0";
			}

			StrSql = StrSql + " ORDER BY variant_name";
			// SOP("vehicle====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstock_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedstock_id"), testdrive_preownedstock_id)).append(">");
				if (!crs.getString("preowned_regno").equals("")) {
					Str.append(crs.getString("carmanuf_name")).append("-").append(crs.getString("preownedmodel_name")).append("-").
							append(crs.getString("variant_name")).append(" (").append(crs.getString("preowned_regno")).append(")");
				} else {
					Str.append(crs.getString("variant_name"));
				}
				Str.append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLocation(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_location "
					+ " WHERE location_active='1' "
					+ " AND location_branch_id= " + CNumeric(enquiry_branch_id) + " "
					+ " ORDER BY location_name ";
			SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_location class=form-control id=dr_location >");
			Str.append("<option value=0>Select Location</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(Selectdrop(crs.getInt("location_id"), testdrive_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTestDriveType() {
		String type = "<option value = 0>Select</option>\n";
		type = type + "<option value = 1" + Selectdrop(1, testdrive_type) + ">Main Test Drive</option>\n";
		type = type + "<option value = 2" + Selectdrop(2, testdrive_type) + ">Alternate Test Drive</option>\n";
		return type;
	}

	protected void PreownedEnquiryDetails(String comp_id, HttpServletResponse response) {
		try {
			if (!testdrive_enquiry_id.equals("")) {
				StrSql = "SELECT customer_id, customer_name, customer_email1, customer_address,"
						+ " customer_pin, enquiry_date, enquiry_branch_id, branch_code,"
						+ " CONCAT('OPR',branch_code,enquiry_no) as enquiry_no, enquiry_emp_id,"
						+ " preownedmodel_name, fueltype_name, contact_id,"
						+ " CONCAT(title_desc,' ',contact_fname,' ', contact_lname) as contactname,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id=customer_id"
						+ " INNER join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
						+ " INNER join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = enquiry_fueltype_id"
						+ " INNER join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id"
						+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id"
						+ " LEFT join " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
						+ " WHERE enquiry_id =" + testdrive_enquiry_id + "";
				// SOP("PreownedEnquiryDetails StrSql=======" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				// SOP("StrSql-----enquiryDetails-----" +
				// StrSqlBreaker(StrSql));
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						testdrive_customer_id = crs.getString("customer_id");
						customer_name = crs.getString("customer_name");
						// contact_name = crs.getString("contact_name");
						// testdrive_contact_id = crs.getString("contact_id");
						contact_name = crs.getString("contactname");
						customer_email1 = crs.getString("customer_email1");
						customer_address = crs.getString("customer_address");
						customer_pin = crs.getString("customer_pin");
						model_name = crs.getString("preownedmodel_name");
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						// enquiry_model_id = crs.getString("enquiry_preownedmodel_id");
						enquiry_date = crs.getString("enquiry_date");
						enquiry_no = crs.getString("enquiry_no");
						testdrive_emp_id = crs.getString("enquiry_emp_id");
						executive_name = crs.getString("emp_name");
						// fuel_name = crs.getString("fueltype_name");

						if (!msg.equals("")) {
							response.sendRedirect("callurlapp-error.jsp?msg=" + msg);
						}
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("callurlapp-error.jsp?msg=Invalid Enquiry!"));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// public void PopulateConfigDetails(String comp_id) {
	// // SOP("comp_id----22---" + comp_id);
	// StrSql = "SELECT config_email_enable, config_sms_enable, comp_email_enable,"
	// + " comp_sms_enable, COALESCE(branch_email1,'') AS branch_email1,"
	// + " COALESCE(branch_testdrive_email_enable,'') AS branch_testdrive_email_enable,"
	// + " COALESCE(branch_testdrive_email_format,'') AS branch_testdrive_email_format,"
	// + " COALESCE(branch_testdrive_email_sub,'') AS branch_testdrive_email_sub,"
	// + " COALESCE(branch_testdrive_sms_enable,'') AS branch_testdrive_sms_enable,"
	// + " COALESCE(branch_testdrive_sms_format,'') AS branch_testdrive_sms_format"
	// + " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
	// + " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + enquiry_branch_id + ""
	// + " WHERE 1 = 1 ";
	// // SOP("StrSql-----PopulateConfigDetails-----" + StrSqlBreaker(StrSql));
	// CachedRowSet crs = processQuery(StrSql, 0);
	// try {
	// while (crs.next()) {
	// comp_email_enable = crs.getString("comp_email_enable");
	// comp_sms_enable = crs.getString("comp_sms_enable");
	// branch_email1 = crs.getString("branch_email1");
	// config_email_enable = crs.getString("config_email_enable");
	// config_sms_enable = crs.getString("config_sms_enable");
	// branch_testdrive_email_enable = crs.getString("branch_testdrive_email_enable");
	// branch_testdrive_email_format = crs.getString("branch_testdrive_email_format");
	// branch_testdrive_email_sub = crs.getString("branch_testdrive_email_sub");
	// branch_testdrive_sms_enable = crs.getString("branch_testdrive_sms_enable");
	// branch_testdrive_sms_format = crs.getString("branch_testdrive_sms_format");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
}
