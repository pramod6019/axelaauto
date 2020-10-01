package axela.preowned;
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

public class Preowned_TestDrive_Update extends Connect {

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
	public String ExeAccess = "";
	public String QueryString = "";
	public String testdrive_id = "";
	public String testdrive_preownedstock_id = "";
	public String testdrive_emp_id = "";
	public String testdrive_location_id = "";
	public String testdrive_type = "";
	public String testdrive_time = "", testdrive_time_from = "", testdrive_time_to = "";
	public String testdrive_confirmed = "", unconfirm = "";
	public String testdrive_notes = "";
	public String testdrive_entry_id = "";
	public String testdrive_entry_date = "";
	public String testdrive_enquiry_id = "";
	public String testdrive_modified_id = "";
	public String testdrive_modified_date = "";
	public String enquiry_branch_id = "";
	public String location_branch_id = "";
	public String enquiry_model_id = "";
	public String testdrive_customer_id = "";
	public String customer_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
	public String model_name = "";
	public String executive_name = "";
	public String strHTML = "";
	public String enquiry_no = "", enquiry_date = "";
	public String pop = "", testdriveunconfirm = "";
	public String testdrivedate = "";
	public String fuel_name = "";
	public String testdrive_contact_id = "";
	public String contact_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				pop = PadQuotes(request.getParameter("pop"));
				add = PadQuotes(request.getParameter("add"));
				CheckPerm(comp_id, "emp_testdrive_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdrive_enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				testdriveunconfirm = ReturnPerm(comp_id, "emp_testdrive_access", request);
				unconfirm = PadQuotes(request.getParameter("unconfirm"));

				if (!add.equals("yes")) {
					PopulateFields(response);
				}

				if (unconfirm.equals("yes")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "SELECT emp_id, testdrive_id, "
								+ "(SELECT concat(emp_name,' (',emp_ref_no,')') "
								+ " FROM " + compdb(comp_id) + "axela_emp where emp_id = " + emp_id + ") as updateemp "
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " inner join " + compdb(comp_id) + "axela_preowned_testdrive on testdrive_emp_id = emp_id "
								+ " WHERE emp_active ='1' AND testdrive_id = " + testdrive_id + "";
						StrSql = StrSql + " ORDER BY emp_name";
						CachedRowSet crs = processQuery(StrSql, 0);
						String testdrive_notes = "";
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								testdrive_notes = "Test Drive unconfirmed by " + crs.getString("updateemp") + " at " + strToLongDate(ToLongDate(kknow())) + ", ";
							}
							if (testdriveunconfirm.equals("1")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_testdrive"
										+ " SET"
										+ " testdrive_confirmed = '0',"
										+ " testdrive_notes = concat('" + testdrive_notes + "',testdrive_notes)"
										+ " where testdrive_id =" + testdrive_id + "";
								updateQuery(StrSql);
								response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Permission denied!"));
						}
						crs.close();
					} else {
						response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));
					}
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				PreownedEnquiryDetails(response);

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						testdrive_preownedstock_id = "";
						testdrive_location_id = "";
						testdrive_type = "1";
						testdrive_confirmed = "1";
						testdrive_notes = "";
						String str = ToLongDate(kknow());
						testdrivedate = SplitDate(str) + "/" + SplitMonth(str) + "/" + SplitYear(str) + " " + SplitHourMin(str);
						//
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
							testdrive_entry_id = CNumeric(GetSession("emp_id", request));
							testdrive_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive added successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
					} else if ("yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							testdrive_modified_id = CNumeric(GetSession("emp_id", request));
							testdrive_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?testdrive_id=" + testdrive_id
										+ "&msg=Test Drive details updated successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Test Drive".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_testdrive_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?msg=Test Drive details deleted successfully!"));
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
		testdrive_type = PadQuotes(request.getParameter("dr_testdrivetype"));
		testdrive_preownedstock_id = PadQuotes(request.getParameter("dr_preownedstock_id"));
		testdrive_location_id = PadQuotes(request.getParameter("dr_location"));
		testdrivedate = PadQuotes(request.getParameter("txt_testdrive_date"));
		testdrive_location_id = PadQuotes(request.getParameter("dr_location"));
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
		if (!testdrivedate.equals("")) {
			testdrive_time = testdrivedate + ":00";
			testdrive_time = ConvertLongDateToStr(testdrive_time);
		}
		StrSql = "SELECT location_leadtime, location_testdrive_dur"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_location"
				+ " where location_id=" + testdrive_location_id;
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
			SOPError("error in cal of start AND end time---" + ex);
		}
		// if (model_name.equals("")) {
		// msg = msg + "<br>Select Model!";
		// }
		if (testdrive_preownedstock_id.equals("0")) {
			msg = msg + "<br>Select Variant!";
		}
		if (testdrive_emp_id.equals("0")) {
			msg = msg + "<br>Select Pre-Owned Consultant!";
		}
		if (testdrive_location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		if (testdrive_type.equals("0")) {
			msg = msg + "<br>Select Test Drive Type!";
		}
		if (testdrivedate.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatLong(testdrivedate)) {
				if (!testdrive_time_from.equals("") && !testdrive_time_to.equals("")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "SELECT testdrive_id FROM " + compdb(comp_id) + "axela_preowned_testdrive where testdrive_confirmed='1' AND testdrive_preownedstock_id=" + testdrive_preownedstock_id
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
				msg = msg + "<br>Enter Valid Test Drive Date!";
			}
		}
		if (testdrive_notes.length() > 8000) {
			testdrive_notes = testdrive_notes.substring(0, 7999);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		ResultSet rs = null;
		Connection conntx = connectDB();
		Statement stmttx = null;
		if (msg.equals("")) {
			try {

				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "insert into " + compdb(comp_id) + "axela_preowned_testdrive"
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
				StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry set enquiry_stage_id=4 where enquiry_id=" + testdrive_enquiry_id;
				conntx.commit();
			} catch (Exception e) {
				conntx.rollback();
				SOPError("Axelaauto===" + this.getClass().getName());
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " SET"
						+ " testdrive_preownedstock_id= '" + testdrive_preownedstock_id + "',"
						+ " testdrive_emp_id = '" + testdrive_emp_id + "', "
						+ " testdrive_location_id = '" + testdrive_location_id + "',"
						+ " testdrive_time = '" + testdrive_time + "', "
						+ " testdrive_time_from = '" + testdrive_time_from + "',"
						+ " testdrive_time_to = '" + testdrive_time_to + "',"
						+ " testdrive_type = " + testdrive_type + ", "
						+ " testdrive_confirmed = '" + testdrive_confirmed + "',"
						+ " testdrive_notes = '" + testdrive_notes + "', "
						+ " testdrive_modified_id = '" + testdrive_modified_id + "',"
						+ " testdrive_modified_date = '" + testdrive_modified_date + "'"
						+ " where testdrive_id = " + testdrive_id + " ";
				// SOP("StrSql===="+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "Select testdrive_doc_value FROM " + compdb(comp_id) + "axela_preowned_testdrive where testdrive_id = " + testdrive_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(PreownedDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_preowned_testdrive where testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select testdrive_preownedstock_id, testdrive_emp_id,testdrive_time, testdrive_time_from, testdrive_type,"
					+ " testdrive_time_to, testdrive_location_id, testdrive_confirmed, testdrive_notes, testdrive_entry_id,"
					+ " testdrive_entry_date, testdrive_modified_id, testdrive_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " where testdrive_id=" + testdrive_id + "";
			// SOP("StrSql=== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_preownedstock_id = crs.getString("testdrive_preownedstock_id");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = crs.getString("testdrive_time_from");
					testdrive_time_to = crs.getString("testdrive_time_to");
					testdrivedate = SplitDate(testdrive_time) + "/" + SplitMonth(testdrive_time) + "/" + SplitYear(testdrive_time) + " " + SplitHourMin(testdrive_time);
					Date stdate = StringToDate(testdrive_time_from);
					Date eddate = StringToDate(testdrive_time_to);
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
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
					+ " AND preownedstock_id not in"
					+ " (SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so where so_active=1)";
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

	public String PopulateLocation() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_location"
					+ " WHERE location_active = '1'"
					+ " AND location_branch_id = " + CNumeric(enquiry_branch_id) + ""
					+ " ORDER BY location_name";
			// SOP("====="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(Selectdrop(crs.getInt("location_id"), testdrive_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTestDriveType() {
		String type = "<option value = 0>Select</option>\n";
		type = type + "<option value = 1" + Selectdrop(1, testdrive_type) + ">Main Test Drive</option>\n";
		type = type + "<option value = 2" + Selectdrop(2, testdrive_type) + ">Alternate Test Drive</option>\n";
		return type;
	}

	protected void PreownedEnquiryDetails(HttpServletResponse response) {
		try {
			if (!testdrive_enquiry_id.equals("")) {
				StrSql = "Select customer_id, customer_name, customer_email1, customer_address,"
						+ " customer_pin, enquiry_date, enquiry_branch_id, branch_code,"
						+ " concat('OPR',branch_code,enquiry_no) as enquiry_no, enquiry_emp_id,"
						+ " preownedmodel_name, fueltype_name, contact_id,"
						+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contactname,"
						+ " concat(emp_name, ' (', emp_ref_no, ')') as emp_name"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id=customer_id"
						+ " INNER join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
						+ " INNER join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = enquiry_fueltype_id"
						+ " INNER join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id"
						+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id"
						+ " LEFT join " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
						+ " WHERE enquiry_id =" + testdrive_enquiry_id + "";
				// SOP("PreownedEnquiryDetails StrSql=======" + StrSql);

				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {

						testdrive_customer_id = crs.getString("customer_id");
						customer_name = crs.getString("customer_name");
						testdrive_contact_id = crs.getString("contact_id");
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
					}
					msg = "";
					if (enquiry_model_id.equals("0")) {
						msg = msg + "<br>Select Model for the Opportunity!";
					}
					if (customer_email1.equals("")) {
						msg = msg + "<br>Email not updated for this Opportunity!";
					}
					if (customer_address.equals("")) {
						msg = msg + "<br>Full Address not updated for this Opportunity!";
					}
					if (customer_pin.equals("")) {
						msg = msg + "<br>Address Pin Code not updated for this Opportunity!";
					}
					if (testdrive_emp_id.equals("0")) {
						msg = msg + "<br>Select Execuitve for the Opportunity!";
					}
					if (!msg.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg));
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Opportunity!"));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
