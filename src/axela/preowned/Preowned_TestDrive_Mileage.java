//Saiman 6th July 2013
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive_Mileage extends Connect {

	public String UpdateB = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String branch_id;
	public String comp_id = "0";
	public String BranchAccess;
	public String emp_id;
	public String emp_role_id = "";
	public String testdrive_id;
	public String testdrive_enquiry_id = "";
	public String enquiry_no = "";
	public String testdrive_customer_id;
	public String enquiry_branch_id;
	public String customer_name;
	public String variant_name, variant_id;
	public String location_name = "";
	public String testdrive_time_from = "";
	public String testdrive_time = "", testdrivetime;
	public String preownedmodel_name = "";
	public String testdrive_out_time = "";
	public String testdriveouttime = "", testdrivetimefrom;
	public String executive_name = "";
	public String testdrive_out_driver_id = "";
	public String testdrive_license_no = "";
	public String testdrive_license_address = "";
	public String testdrive_license_issued_by = "";
	public String testdrive_license_valid = "", testdrivelicensevalid = "";
	public String config_sms_enable = "";
	public String contact_id = "";
	public String contact_mobile1 = "";
	public String testdrive_out_kms = "";
	public String testdriveintime = "";
	public String testdrive_in_time = "";
	public String testdrive_in_kms = "";
	public String testdrive_mileage_notes = "";
	public String testdrive_mileage_entry_id = "0";
	public String testdrive_mileage_entry_date = "";
	public String testdrive_mileage_modified_id = "0";
	public String testdrive_mileage_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String empEditPerm = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_preowned_testdrive_access", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				UpdateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				empEditPerm = ReturnPerm(comp_id, "emp_preowned_testdrive_edit", request);

				OpportunityDetails(response);
				getConfigFields();
				if (UpdateB.equals("")) {
					PopulateFields();
					if (entry_by.equals("")) {
						testdriveouttime = ToLongDate(kknow());
						testdriveouttime = strToLongDate(testdriveouttime);
					}
				} else if ("yes".equals(UpdateB)) {
					GetValues(request, response);
					if (entry_by.equals("")) {
						if (ReturnPerm(comp_id, "emp_preowned_testdrive_add", request).equals("1")) {
							testdrive_mileage_entry_id = CNumeric(GetSession("emp_id", request));
							testdrive_mileage_entry_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else {
						if (ReturnPerm(comp_id, "emp_preowned_testdrive_edit", request).equals("1")) {
							testdrive_mileage_modified_id = CNumeric(GetSession("emp_id", request));
							testdrive_mileage_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}

				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
		testdrive_out_driver_id = PadQuotes(request.getParameter("dr_driver"));
		testdrive_license_no = PadQuotes(request.getParameter("txt_testdrive_license_no"));
		testdrive_license_address = PadQuotes(request.getParameter("txt_testdrive_license_address"));
		testdrive_license_issued_by = PadQuotes(request.getParameter("txt_testdrive_license_issued_by"));
		testdrivelicensevalid = PadQuotes(request.getParameter("txt_testdrive_license_valid"));
		testdrive_license_valid = ConvertShortDateToStr(testdrivelicensevalid);
		testdriveouttime = PadQuotes(request.getParameter("txt_testdrive_out_time"));
		testdrive_out_time = ConvertLongDateToStr(testdriveouttime);
		testdrive_out_kms = PadQuotes(request.getParameter("txt_testdrive_out_kms"));
		if (testdrive_out_kms.equals("") || !isNumeric(testdrive_out_kms)) {
			testdrive_out_kms = "0";
		}
		testdriveintime = PadQuotes(request.getParameter("txt_testdrive_in_time"));
		testdrive_in_time = ConvertLongDateToStr(testdriveintime);
		testdrive_in_kms = PadQuotes(request.getParameter("txt_testdrive_in_kms"));
		if (testdrive_in_kms.equals("") || !isNumeric(testdrive_in_kms)) {
			testdrive_in_kms = "0";
		}
		testdrive_mileage_notes = PadQuotes(request.getParameter("txt_testdrive_mileage_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (testdrive_out_driver_id.equals("0")) {
			msg = msg + "<br>Select Driver!";
		}
		if (!testdrivelicensevalid.equals("")) {
			if (!isValidDateFormatShort(testdrivelicensevalid)) {
				msg = msg + "<br>Select valid License Date!";
			} else {
				testdrive_license_valid = ConvertShortDateToStr(testdrivelicensevalid);
			}
		}
		if (testdriveouttime.equals("")) {
			msg = msg + "<br>Select Out Time!";
		}
		if (!testdriveouttime.equals("")) {
			if (!isValidDateFormatLong(testdriveouttime)) {
				msg = msg + "<br>Select valid Out Time!";
			} else {
				testdrive_out_time = ConvertLongDateToStr(testdriveouttime);
			}
		}
		if (!testdriveintime.equals("")) {
			if (!isValidDateFormatLong(testdriveintime)) {
				msg = msg + "<br>Select valid In Time!";
			} else {
				testdrive_in_time = ConvertLongDateToStr(testdriveintime);
			}
			if (Long.parseLong(testdrive_in_time) <= Long.parseLong(testdrive_out_time)) {
				msg = msg + "<br>In Time must be greater than " + strToLongDate(testdrive_out_time) + "!";
			}
		}
		if (testdrive_in_kms.equals("0") && !testdriveintime.equals("")) {
			msg = msg + "<br>Enter In Kms!";
		} else if (!testdrive_in_kms.equals("0") && Double.parseDouble(testdrive_in_kms) < Double.parseDouble(testdrive_out_kms)) {
			msg = msg + "<br>In Kms must be greater than " + testdrive_out_kms + " kms!";
		}
		if (testdrive_mileage_notes.length() > 8000) {
			testdrive_mileage_notes = testdrive_mileage_notes.substring(0, 7999);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " SET "
						+ "testdrive_out_driver_id = " + testdrive_out_driver_id + ", "
						+ "testdrive_license_no = '" + testdrive_license_no + "', "
						+ "testdrive_license_address = '" + testdrive_license_address + "', "
						+ "testdrive_license_issued_by = '" + testdrive_license_issued_by + "', "
						+ "testdrive_license_valid = '" + testdrive_license_valid + "', "
						+ "testdrive_out_kms = " + testdrive_out_kms + ", "
						+ "testdrive_out_time = '" + testdrive_out_time + "', "
						+ "testdrive_in_kms = " + testdrive_in_kms + ", "
						+ "testdrive_in_time = '" + testdrive_in_time + "',"
						+ "testdrive_mileage_notes = '" + testdrive_mileage_notes + "',";
				if (entry_by.equals("")) {
					StrSql = StrSql + "testdrive_mileage_entry_id = '" + emp_id + "', "
							+ "testdrive_mileage_entry_date = '" + ToLongDate(kknow()) + "', ";
					// SendSMS(contact_id, contact_mobile1);
				}
				StrSql = StrSql + "testdrive_mileage_modified_id = " + testdrive_mileage_modified_id + ", "
						+ "testdrive_mileage_modified_date = '" + testdrive_mileage_modified_date + "' "
						+ "where testdrive_id = " + testdrive_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void SendSMS(String contact_id, String mobile) {
		String msg = "";

		if (config_sms_enable.equals("1") && !mobile.equals("")) {
			msg = "Audi " + preownedmodel_name + " is on its way for you to have a test drive.\nHappy Cruising.\nTeam Audi.";
			try {
				String Sql = "insert into " + compdb(comp_id) + "axela_sms"
						+ "(sms_mobileno,"
						+ "sms_msg,"
						+ "sms_date ,"
						+ "sms_sent ,"
						+ "sms_contact_id, "
						+ "sms_customer_id ,"
						+ "sms_entry_id)"
						+ "values"
						+ "('" + mobile + "',"
						+ "'" + msg + "',"
						+ "'" + ToLongDate(kknow()) + "',"
						+ "'0',"
						+ "'" + contact_id + "',"
						+ "'" + testdrive_customer_id + "',"
						+ "'" + emp_id + "')";
				updateQuery(Sql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT testdrive_out_driver_id, testdrive_license_no, testdrive_license_address,  "
					+ " testdrive_license_issued_by, testdrive_license_valid, testdrive_out_time, testdrive_out_kms,  "
					+ " testdrive_in_time, testdrive_in_kms, testdrive_mileage_notes, testdrive_mileage_entry_id,  "
					+ " testdrive_mileage_entry_date, testdrive_mileage_modified_id, testdrive_mileage_modified_date "
					+ " from " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " where testdrive_id=" + testdrive_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				testdrive_out_driver_id = crs.getString("testdrive_out_driver_id");
				testdrive_license_no = crs.getString("testdrive_license_no");
				testdrive_license_address = crs.getString("testdrive_license_address");
				testdrive_license_issued_by = crs.getString("testdrive_license_issued_by");
				testdrive_license_valid = crs.getString("testdrive_license_valid");
				testdrivelicensevalid = strToShortDate(testdrive_license_valid);
				testdrive_out_time = crs.getString("testdrive_out_time");
				testdriveouttime = strToLongDate(testdrive_out_time);
				testdrive_out_kms = crs.getString("testdrive_out_kms");
				testdrive_in_time = crs.getString("testdrive_in_time");
				testdriveintime = strToLongDate(testdrive_in_time);
				testdrive_in_kms = crs.getString("testdrive_in_kms");
				testdrive_mileage_notes = crs.getString("testdrive_mileage_notes");
				testdrive_mileage_entry_id = crs.getString("testdrive_mileage_entry_id");
				if (!testdrive_mileage_entry_id.equals("0")) {
					entry_by = Exename(comp_id, crs.getInt("testdrive_mileage_entry_id"));
					entry_date = strToLongDate(crs.getString("testdrive_mileage_entry_date"));
				}
				testdrive_mileage_modified_id = crs.getString("testdrive_mileage_modified_id");
				if (!testdrive_mileage_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(testdrive_mileage_modified_id));
					modified_date = strToLongDate(crs.getString("testdrive_mileage_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDriver() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT driver_id, concat(driver_name,' (', driver_id , ')') as driver_name "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_driver  "
					+ " where driver_active=1";
			// SOP("PopulateDriver StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("driver_id") + "");
				Str.append(Selectdrop(crs.getInt("driver_id"), testdrive_out_driver_id));
				Str.append(">" + crs.getString("driver_name") + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void OpportunityDetails(HttpServletResponse response) {
		try {
			StrSql = "Select customer_id, customer_name,enquiry_branch_id,location_name,testdrive_emp_id, preownedmodel_name, "
					+ " variant_id, variant_name, preowned_regno, testdrive_time,testdrive_time_to, coalesce(contact_id,'') as contact_id, coalesce(contact_mobile1,'') as contact_mobile1, "
					+ " testdrive_time_from, testdrive_enquiry_id, concat('OPR',branch_code,enquiry_no) as enquiry_no ,branch_code "
					+ " from " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id=testdrive_enquiry_id "
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id=enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id "
					+ " left join " + compdb(comp_id) + "axela_customer_contact on contact_id=enquiry_contact_id "
					+ " where testdrive_id = " + testdrive_id + BranchAccess + "";
			// SOP("StrSql in ==========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_customer_id = crs.getString("customer_id");
					testdrive_enquiry_id = crs.getString("testdrive_enquiry_id");
					customer_name = crs.getString("customer_name");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_id = crs.getString("contact_id");
					enquiry_no = crs.getString("enquiry_no");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					location_name = crs.getString("location_name");
					variant_id = crs.getString("variant_id");
					variant_name = crs.getString("variant_name");
					if (!crs.getString("preowned_regno").equals("")) {
						variant_name = variant_name + " - " + crs.getString("preowned_regno");
					}
					preownedmodel_name = crs.getString("preownedmodel_name");
					testdrive_time = crs.getString("testdrive_time");
					testdrivetime = strToLongDate(crs.getString("testdrive_time"));
					if (!crs.getString("testdrive_time_from").equals("")) {
						testdrive_time_from = crs.getString("testdrive_time_from");
						testdrivetime = PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1");
					}
					executive_name = Exename(comp_id, crs.getInt("testdrive_emp_id"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void getConfigFields() {
		StrSql = "Select config_sms_enable from " + compdb(comp_id) + "axela_config";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_sms_enable = crs.getString("config_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
