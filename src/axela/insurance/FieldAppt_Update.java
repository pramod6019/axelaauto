package axela.insurance;
//Smitha Nag 11th Feb 2013

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

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class FieldAppt_Update extends Connect {

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
	public String fieldappt_id = "";
	public String fieldappt_emp_id = "";
	public String fieldappt_fieldappttype_id = "0";
	public String fieldappt_appttime = "", fieldappt_fromtime = "", fieldappt_totime = "";
	public String fieldappt_notes = "";
	public String fieldappt_entry_id = "";
	public String fieldappt_entry_date = "";
	public String fieldappt_insurenquiry_id = "";
	public String fieldappt_modified_id = "";
	public String fieldappt_modified_date = "";
	public String fieldapptdate = "", fieldappttypename = "";
	public String fieldappt_contact_id = "0";
	public String fieldappt_contact_name = "";
	public String fieldappt_contact_link = "";
	public String fieldappt_insurenquiry_link = "";
	// public String fieldappt_insurenquiry_date = "";

	public String insurenquiry_branch_id = "";
	public String insurenquiry_model_id = "";
	public String insurenquiry_variant_id = "";
	// public String StartHour = "", StartMin = "";
	// public String DurHour = "", DurMin = "";
	public String insurenquiry_contact_id = "";
	public String insurenquiry_contact_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
	public String insurenquiry_variant = "";
	public String executive_name = "";
	public String strHTML = "";
	public String insurenquiry_date = "", insurenquiry_name = "";
	// public String pop = "", contact_mobile1 = "";

	// public String config_sms_enable = "", branch_brand_id = "0";
	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_field_appointment_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				fieldappt_insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				fieldappt_id = CNumeric(PadQuotes(request.getParameter("fieldappt_id")));

				if (!add.equals("yes")) {
					PopulateFields(response);
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				EnquiryDetails(response);
				// PopulateConfigDetails();

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						fieldappt_notes = "";
						String str = ToLongDate(kknow());
						fieldapptdate = SplitDate(str) + "/" + SplitMonth(str) + "/" + SplitYear(str) + " " + SplitHourMin(str);
					} else {
						if (ReturnPerm(comp_id, "emp_field_appointment_add", request).equals("1")) {
							GetValues(request, response);
							fieldappt_entry_id = CNumeric(GetSession("emp_id", request));
							fieldappt_entry_date = ToLongDate(kknow());
							AddFields();
							// SOP("msg==" + msg);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								// UpdateTestDriveType(fieldappt_insurenquiry_id);
								response.sendRedirect(response.encodeRedirectURL("fieldappt-list.jsp?fieldappt_id=" + fieldappt_id + "&msg=Field Appointment Added Successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Field Appointment".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Field Appointment".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_field_appointment_edit", request).equals("1")) {
							GetValues(request, response);
							fieldappt_modified_id = CNumeric(GetSession("emp_id", request));
							fieldappt_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("fieldappt-list.jsp?fieldappt_id=" + fieldappt_id + "&msg=Field Appointment Updated Successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Field Appointment".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_field_appointment_delete", request).equals("1")) {
							// GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("fieldappt-list.jsp?msg==Field Appointment details deleted successfully."));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		fieldappt_contact_id = PadQuotes(request.getParameter("txt_fieldappt_contact_id"));
		fieldappt_contact_name = PadQuotes(request.getParameter("txt_fieldappt_contact_name"));
		insurenquiry_date = PadQuotes(request.getParameter("txt_fieldappt_insurenquiry_date"));
		fieldappt_contact_link = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + fieldappt_contact_id + "\">" + fieldappt_contact_name + "</a>";
		fieldappt_insurenquiry_link = "<a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + fieldappt_insurenquiry_id + "\">"
				+ fieldappt_insurenquiry_id + "</a>";

		fieldappt_emp_id = CNumeric(PadQuotes(request.getParameter("dr_field_executive")));
		fieldappt_fieldappttype_id = CNumeric(PadQuotes(request.getParameter("dr_fieldappt_type_id")));
		fieldapptdate = PadQuotes(request.getParameter("txt_fieldappt_date"));
		fieldappt_notes = PadQuotes(request.getParameter("txt_fieldappt_notes"));

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		fieldappt_appttime = fieldapptdate + ":00";
		fieldappt_appttime = ConvertLongDateToStr(fieldappt_appttime);
		Date start = AddHoursDate(StringToDate(fieldappt_appttime), 0, -1, 0);
		fieldappt_fromtime = ToLongDate(start);
		Date end = AddHoursDate(StringToDate(fieldappt_appttime), 0, 1, 0);
		fieldappt_totime = ToLongDate(end);

		if (fieldappt_emp_id.equals("0")) {
			msg = msg + "<br>Select Field Executive!";
		}
		if (fieldappt_fieldappttype_id.equals("0")) {
			msg = msg + "<br>Select Appointment Type!";
		}
		SOP("fieldapptdate==" + fieldapptdate);
		if (fieldapptdate.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatLong(fieldapptdate)) {
				if (Long.parseLong(ConvertLongDateToStr(fieldapptdate)) < (Long.parseLong((ToLongDate(kknow()))))) {
					msg += "<br>Field Appointment Time must be greater than Current Time!";
				}
				if (!fieldappt_fromtime.equals("") && !fieldappt_totime.equals("") && msg.equals("")) {
					if (!fieldappt_fromtime.equals("") && !fieldappt_totime.equals("")) {
						StrSql = "SELECT fieldappt_id from " + compdb(comp_id) + "axela_insurance_fieldappt "
								+ " WHERE 1=1 "
								+ " AND fieldappt_emp_id = " + fieldappt_emp_id
								+ " AND fieldappt_status_taken = 0"
								+ " AND "
								+ " ((fieldappt_fromtime >= " + fieldappt_fromtime + " and fieldappt_fromtime < " + fieldappt_totime + ")"
								+ " or (fieldappt_totime > " + fieldappt_fromtime + " and fieldappt_totime <= " + fieldappt_totime + ") "
								+ " or (fieldappt_fromtime >= " + fieldappt_fromtime + " and fieldappt_totime <= " + fieldappt_totime + ") "
								+ " or (fieldappt_fromtime <= " + fieldappt_fromtime + " and fieldappt_totime >= " + fieldappt_totime + "))";
						if (!update.equals("")) {
							StrSql = StrSql + " AND fieldappt_id!=" + fieldappt_id;
						}
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Executive has another Appointment!";
						}
					}
				}
			} else {
				msg = msg + "<br>Enter Valid Field Appointment Date!";
			}
		}

		if (fieldappt_notes.length() > 8000) {
			fieldappt_notes = fieldappt_notes.substring(0, 7999);
		}

		// SOP("msg=11=" + msg);
	}

	protected void AddFields() throws SQLException {
		CheckForm();

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_fieldappt"
						+ "("
						+ "fieldappt_insurenquiry_id,"
						+ "fieldappt_emp_id, "
						+ "fieldappt_fieldappttype_id,"
						+ "fieldappt_appttime,"
						+ "fieldappt_fromtime,"
						+ "fieldappt_totime,"
						+ "fieldappt_notes,"
						+ "fieldappt_entry_id,"
						+ "fieldappt_entry_date,"
						+ "fieldappt_modified_id,"
						+ "fieldappt_modified_date"
						+ ")"
						+ " VALUES "
						+ "("
						+ "" + fieldappt_insurenquiry_id + ","
						+ "" + fieldappt_emp_id + ","
						+ "'" + fieldappt_fieldappttype_id + "',"
						+ "'" + fieldappt_appttime + "',"
						+ "'" + fieldappt_fromtime + "',"
						+ "'" + fieldappt_totime + "',"
						+ "'" + fieldappt_notes + "',"
						+ "'" + fieldappt_entry_id + "',"
						+ "'" + fieldappt_entry_date + "',"
						+ "0,"
						+ "''"
						+ ")";
				// SOP("StrSql = " + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					fieldappt_id = rs.getString(1);
				}
				rs.close();
				// StrSql = "SELECT enquiry_stage_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
				// + " WHERE enquiry_id =" + fieldappt_insurenquiry_id;
				// if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) <= 3) {
				// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
				// + " SET enquiry_stage_id = 3"
				// + " WHERE enquiry_id = " + fieldappt_insurenquiry_id;
				// stmttx.execute(StrSql);
				// }

				// if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && branch_testdrive_sms_enable.equals("1")) {
				// if (!branch_testdrive_sms_format.equals("") && !contact_mobile1.equals("")) {
				// SendSMS();
				// }
				// }

				conntx.commit();
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_fieldappt"
						+ " SET "
						+ "fieldappt_emp_id = '" + fieldappt_emp_id + "', "
						+ "fieldappt_appttime = '" + fieldappt_appttime + "', "
						+ "fieldappt_fromtime = '" + fieldappt_fromtime + "', "
						+ "fieldappt_totime = '" + fieldappt_totime + "', "
						+ "fieldappt_fieldappttype_id = '" + fieldappt_fieldappttype_id + "', "
						+ "fieldappt_notes = '" + fieldappt_notes + "', "
						+ "fieldappt_modified_id = '" + fieldappt_modified_id + "', "
						+ "fieldappt_modified_date = '" + fieldappt_modified_date + "' "
						+ "WHERE fieldappt_id = " + fieldappt_id + " ";
				// SOP("StrSql===" + StrSql);
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
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_fieldappt WHERE fieldappt_id =" + fieldappt_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT fieldappt_emp_id, fieldappt_appttime, fieldappt_fromtime, fieldappt_fieldappttype_id,"
					+ " fieldappt_totime, fieldappt_notes, fieldappt_entry_id, insurenquiry_variant_id,"
					+ " fieldappt_entry_date, fieldappt_modified_id, fieldappt_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = fieldappt_insurenquiry_id"
					+ " WHERE fieldappt_id = " + fieldappt_id + "";
			// SOP("StrSql==PopulateFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					fieldappt_emp_id = crs.getString("fieldappt_emp_id");
					// SOP("fieldappt_emp_id==" + fieldappt_emp_id);
					fieldappt_appttime = crs.getString("fieldappt_appttime");
					fieldappt_fromtime = crs.getString("fieldappt_fromtime");
					fieldappt_totime = crs.getString("fieldappt_totime");
					fieldapptdate = SplitDate(fieldappt_appttime) + "/" + SplitMonth(fieldappt_appttime) + "/" + SplitYear(fieldappt_appttime) + " " + SplitHourMin(fieldappt_appttime);
					fieldappt_fieldappttype_id = crs.getString("fieldappt_fieldappttype_id");
					fieldappt_notes = crs.getString("fieldappt_notes");
					fieldappt_entry_id = crs.getString("fieldappt_entry_id");
					entry_by = Exename(comp_id, crs.getInt("fieldappt_entry_id"));
					entry_date = strToLongDate(crs.getString("fieldappt_entry_date"));
					fieldappt_modified_id = crs.getString("fieldappt_modified_id");
					if (!fieldappt_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(fieldappt_modified_id));
						modified_date = strToLongDate(crs.getString("fieldappt_modified_date"));
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

	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			if (!fieldappt_insurenquiry_id.equals("")) {
				StrSql = "SELECT customer_id, customer_name, insurenquiry_branch_id, contact_mobile1,"
						+ " customer_email1, customer_address, customer_pin,"
						+ " branch_code, insurenquiry_date,"
						+ " insurenquiry_variant_id, COALESCE(insurenquiry_variant, '') AS insurenquiry_variant,"
						+ " insurenquiry_emp_id, CONCAT(emp_name, '(', emp_ref_no, ')') AS emp_name,"
						+ " branch_brand_id "
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_customer_id = customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = insurenquiry_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
						+ " WHERE insurenquiry_id = " + CNumeric(fieldappt_insurenquiry_id)
						+ BranchAccess + " ";
				// SOP("StrSql--EnquiryDetails----" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				// if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurenquiry_contact_id = crs.getString("customer_id");
					insurenquiry_contact_name = crs.getString("customer_name");
					fieldappt_contact_link = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + insurenquiry_contact_id + "\">" + insurenquiry_contact_name + "</a>";
					fieldappt_insurenquiry_link = "<a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + fieldappt_insurenquiry_id + "\">"
							+ fieldappt_insurenquiry_id + "</a>";
					customer_email1 = crs.getString("customer_email1");
					customer_address = crs.getString("customer_address");
					customer_pin = crs.getString("customer_pin");
					insurenquiry_variant_id = crs.getString("insurenquiry_variant_id");
					insurenquiry_branch_id = crs.getString("insurenquiry_branch_id");
					insurenquiry_date = crs.getString("insurenquiry_date");
					fieldappt_emp_id = crs.getString("insurenquiry_emp_id");
					executive_name = crs.getString("emp_name");
					// branch_brand_id = crs.getString("branch_brand_id");
				}
				// msg = "";
				// if (insurenquiry_model_id.equals("0")) {
				// msg = msg + "<br>Select Model for the Enquiry!";
				// }
				// if (customer_address.equals("")) {
				// msg = msg + "<br>Full Address not updated for this Enquiry!";
				// }
				// if (customer_pin.equals("")) {
				// msg = msg + "<br>Address Pin Code not updated for this Enquiry!";
				// }
				// if (fieldappt_emp_id.equals("0")) {
				// msg = msg + "<br>Select Execuitve for the Enquiry!";
				// }
				// msg = msg + new Enquiry_Dash_Methods().CheckEnquiryFields(fieldappt_insurenquiry_id, branch_brand_id, comp_id);
				// if (!msg.equals("")) {
				// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg));
				// }
				// }
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFieldExecutive(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_fieldinsur = 1"
					+ " AND emp_active = 1";
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// ////SOP("PopulateInsurExecutive-==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), fieldappt_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateApptType(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT fieldappttype_id, fieldappttype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt_type "
					+ " WHERE 1=1";

			StrSql += " GROUP BY fieldappttype_id "
					+ " ORDER BY fieldappttype_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Appointment Type</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fieldappttype_id")).append("");
				Str.append(Selectdrop(crs.getInt("fieldappttype_id"), fieldappt_fieldappttype_id));
				Str.append(">").append(crs.getString("fieldappttype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
