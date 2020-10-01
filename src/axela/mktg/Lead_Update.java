package axela.mktg;
//Saiman 11th Feb 2013
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

public class Lead_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String lead_id = "0";
	public String branch_id = "0";
	public String branch_name = "", lead_branch_id = "0";
	public String lead_fname = "";
	public String lead_lname = "";
	public String lead_title_id = "0";
	public String lead_jobtitle = "";
	public String lead_date = "", leaddate = "";
	public String lead_mobile = "";
	public String lead_email = "";
	public String lead_website = "";
	public String lead_phone = "";
	public String lead_company = "";
	public String lead_emp_id = "0";
	public String lead_empcount_id = "0";
	public String lead_req = "";
	public String emp_name = "";
	public String lead_leadsoe_id = "0";
	public String lead_leadsob_id = "0";
	public String lead_active = "";
	public String lead_refno = "";
	public String lead_entry_id = "0";
	public String lead_entry_date = "";
	public String lead_modified_id = "0";
	public String lead_modified_date = "";
	public String config_sales_soe = "";
	public String emp_role_id = "";
	public String config_sales_sob = "";
	public String emp_id = "0";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String branch_lead_email_enable = "";
	public String branch_lead_email_sub = "";
	public String branch_lead_email_format = "";
	public String branch_lead_email_exe_sub = "";
	public String branch_lead_email_exe_format = "";
	public String branch_lead_sms_enable = "";
	public String branch_lead_sms_exe_format = "", branch_lead_sms_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sales_lead_refno = "";
	public String Enable = "";
	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String emp_email = "", emp_mobile = "";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_role_id = GetSession("emp_role_id", request);
			branch_id = GetSession("emp_branch_id", request);
			CheckPerm(comp_id, "emp_lead_access", request, response);
			QueryString = PadQuotes(request.getQueryString());
			add = PadQuotes(request.getParameter("add"));
			update = PadQuotes(request.getParameter("update"));
			addB = PadQuotes(request.getParameter("add_button"));
			updateB = PadQuotes(request.getParameter("update_button"));
			deleteB = PadQuotes(request.getParameter("delete_button"));
			msg = PadQuotes(request.getParameter("msg"));
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
			Enable = ReturnPerm(comp_id, "emp_lead_edit", request);

			if (add.equals("yes")) {
				status = "Add";
			} else if (update.equals("yes")) {
				status = "Update";
			}

			lead_emp_id = CNumeric(PadQuotes(request.getParameter("dr_lead_emp_id")));
			if (!branch_id.equals("0")) {
				lead_branch_id = branch_id;
			} else if (branch_id.equals("0")) {
				lead_branch_id = CNumeric(PadQuotes(request.getParameter("dr_lead_branch_id")));
			}
			if ("yes".equals(add)) {
				if (!"yes".equals(addB)) {
					lead_mobile = "";
					lead_phone = "";
					lead_active = "1";
					lead_emp_id = emp_id;
					leaddate = DateToShortDate(kknow());
				} else {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_lead_add", request).equals("1")) {
						lead_entry_id = emp_id;
						lead_entry_date = ToLongDate(kknow());
						lead_modified_date = "";
						lead_modified_id = "0";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("lead-list.jsp?lead_id=" + lead_id + "&msg=Lead added successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
			if ("yes".equals(update)) {
				if (!"yes".equals(updateB) && !"Delete Lead".equals(deleteB)) {
					PopulateFields(response);
				} else if ("yes".equals(updateB) && !"Delete Lead".equals(deleteB)) {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_lead_edit", request).equals("1")) {
						lead_modified_id = emp_id;
						lead_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("lead-list.jsp?lead_id=" + lead_id + "&msg=Lead details updated successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				} else if ("Delete Lead".equals(deleteB)) {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_lead_delete", request).equals("1")) {
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("lead-list.jsp?msg=Lead details deleted successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		leaddate = PadQuotes(request.getParameter("txt_lead_date"));
		lead_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		lead_fname = PadQuotes(request.getParameter("txt_lead_fname"));
		lead_lname = PadQuotes(request.getParameter("txt_lead_lname"));
		lead_jobtitle = PadQuotes(request.getParameter("txt_lead_jobtitle"));
		lead_mobile = PadQuotes(request.getParameter("txt_lead_mobile"));
		lead_phone = PadQuotes(request.getParameter("txt_lead_phone"));
		lead_email = PadQuotes(request.getParameter("txt_lead_email"));
		lead_website = PadQuotes(request.getParameter("txt_lead_website"));
		lead_company = PadQuotes(request.getParameter("txt_lead_company"));
		lead_empcount_id = CNumeric(PadQuotes(request.getParameter("dr_lead_empcount_id")));
		lead_req = PadQuotes(request.getParameter("txt_lead_req"));
		lead_leadsoe_id = CNumeric(PadQuotes(request.getParameter("dr_lead_leadsoe_id")));
		lead_refno = PadQuotes(request.getParameter("txt_lead_refno"));
		lead_leadsob_id = CNumeric(PadQuotes(request.getParameter("dr_lead_leadsob_id")));
		emp_name = PadQuotes(request.getParameter("txt_emp_name"));
		lead_active = PadQuotes(request.getParameter("chk_lead_active"));
		if (lead_active.equals("on")) {
			lead_active = "1";
		} else {
			lead_active = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (lead_branch_id.equals("0")) {
				msg = msg + "<br>Select Branch!";
			}

			if (leaddate.equals("")) {
				msg = msg + "<br>Enter Date!";
			} else if (!leaddate.equals("")) {
				if (isValidDateFormatShort(leaddate)) {
					lead_date = ConvertShortDateToStr(leaddate);
				} else {
					msg = msg + "<br>Enter Valid Date!";
				}
			}

			if (status.equals("Update")) {
				if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(leaddate))) {
					msg = msg + "<br>Date Should Be Less Than Current Date!";
				}
			}

			if (lead_title_id.equals("0") && lead_fname.equals("")) {
				msg = msg + "<br>Enter contact name!";
			} else {
				if (lead_title_id.equals("0")) {
					msg = msg + "<br>Select contact Title!";
				}
				if (lead_fname.equals("")) {
					msg = msg + "<br>Enter the contact First Name!";
				} else {
					lead_fname = toTitleCase(lead_fname);
				}

			}
			if (!lead_lname.equals("")) {
				lead_lname = toTitleCase(lead_lname);
			}

			if (lead_mobile.equals("") && lead_phone.equals("") && lead_email.equals("")) {
				msg = msg + "<br>Enter mobile or phone or email!";
			}

			if (!lead_mobile.equals("")) {
				if (!IsValidMobileNo11(lead_mobile)) {
					msg = msg + "<br>Enter valid Mobile!";
				}
			}

			if (!lead_phone.equals("")) {
				if (!IsValidPhoneNo11(lead_phone)) {
					msg = msg + "<br>Enter valid Phone!";
				}
			}

			if (!lead_email.equals("") && !IsValidEmail(lead_email)) {
				msg = msg + "<br>Email1 is invalid!";
			} else {
				lead_email = lead_email.toLowerCase();
			}

			if (!lead_website.equals("") && lead_website.contains("http://")) {
				msg = msg + "<br>Website is invalid!";
			}

			if (lead_company.equals("")) {
				msg = msg + "<br>Enter the company name!";
			}

			if (lead_empcount_id.equals("0")) {
				msg = msg + "<br>Select Employee Count!";
			}

			if (lead_req.length() > 2000) {
				lead_req = lead_req.substring(0, 1999);
			}

			if (lead_leadsoe_id.equals("0")) {
				msg = msg + "<br>Select Source of Enquiry!";
			}
			if (lead_leadsob_id.equals("0")) {
				msg = msg + "<br>Select Source of Business!";
			}
			if (lead_emp_id.equals("0")) {
				msg = msg + "<br>Select Executive!";
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		// SOP("lead_date=" + lead_date);
		ResultSet rs = null;
		if (msg.equals("")) {
			try {
				// lead_date = ToLongDate(kknow());

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT into " + compdb(comp_id) + "axela_mktg_lead"
						+ " (lead_branch_id,"
						+ " lead_title_id,"
						+ " lead_fname,"
						+ " lead_lname,"
						+ " lead_jobtitle,"
						+ " lead_date,"
						+ " lead_email,"
						+ " lead_website,"
						+ " lead_mobile,"
						+ " lead_phone,"
						+ " lead_company,"
						+ " lead_empcount_id,"
						+ " lead_req,";
				if (config_sales_lead_refno.equals("1")) {
					StrSql = StrSql + "lead_refno, ";
				}
				StrSql = StrSql + " lead_leadsoe_id,"
						+ " lead_leadsob_id,"
						+ " lead_emp_id,"
						+ " lead_active,"
						+ " lead_entry_id,"
						+ " lead_entry_date,"
						+ " lead_modified_id,"
						+ " lead_modified_date"
						+ " ) "
						+ "values"
						+ "("
						+ " " + lead_branch_id + ","
						+ " " + lead_title_id + ","
						+ " '" + lead_fname + "',"
						+ " '" + lead_lname + "',"
						+ " '" + lead_jobtitle + "',"
						+ " '" + lead_date + "',"
						+ " '" + lead_email + "',"
						+ " '" + lead_website + "',"
						+ " '" + lead_mobile + "',"
						+ " '" + lead_phone + "',"
						+ " '" + lead_company + "',"
						+ " " + lead_empcount_id + ","
						+ " '" + lead_req + "',";
				if (config_sales_lead_refno.equals("1")) {
					StrSql = StrSql + "'" + lead_refno + "',";
				}
				StrSql = StrSql + "" + lead_leadsoe_id + ","
						+ " " + lead_leadsob_id + ","
						+ " " + lead_emp_id + ","
						+ " '" + lead_active + "',"
						+ " '" + lead_entry_id + "',"
						+ " '" + lead_entry_date + "',"
						+ " 0,"
						+ " ''"
						+ ")";
				SOP("StrSql----------add---------" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					lead_id = rs.getString(1);
				}
				rs.close();

				conntx.commit();
				SOP("Transaction commit...");
			} catch (Exception e) {
				conntx.rollback();
				SOPError(ClientName + "===" + this.getClass().getName());
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
		if (msg.equals("")) {
			try {
				// if (!branch_id.equals("0")) {
				// lead_branch_id = branch_id;
				// }
				StrSql = "UPDATE " + compdb(comp_id) + "axela_mktg_lead"
						+ " SET"
						+ " lead_branch_id = '" + lead_branch_id + "',"
						+ " lead_title_id = '" + lead_title_id + "',"
						+ " lead_fname = '" + lead_fname + "',"
						+ " lead_lname = '" + lead_lname + "',"
						+ " lead_jobtitle = '" + lead_jobtitle + "',"
						+ " lead_date = '" + lead_date + "',"
						+ " lead_email = '" + lead_email + "',"
						+ " lead_website = '" + lead_website + "',"
						+ " lead_mobile = '" + lead_mobile + "',"
						+ " lead_phone = '" + lead_phone + "',"
						+ " lead_company = '" + lead_company + "',";
				if (config_sales_soe.equals("1")) {
					StrSql = StrSql + " lead_leadsoe_id = '" + lead_leadsoe_id + "',";
				}
				if (config_sales_sob.equals("1")) {
					StrSql = StrSql + " lead_leadsob_id= '" + lead_leadsob_id + "',";
				}
				StrSql = StrSql
						+ " lead_empcount_id = '" + lead_empcount_id + "',"
						+ " lead_req = '" + lead_req + "',";
				if (config_sales_lead_refno.equals("1")) {
					StrSql = StrSql + " lead_refno = '" + lead_refno + "',";
				}
				StrSql = StrSql
						+ " lead_emp_id = '" + lead_emp_id + "',"
						+ " lead_active = '" + lead_active + "',"
						+ " lead_modified_id = '" + lead_modified_id + "',"
						+ " lead_modified_date = '" + lead_modified_date + "'"
						+ " where lead_id = " + lead_id + "";

				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		msg = "";
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_mktg_lead where lead_id =" + lead_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_mktg_lead.*, branch_code, soe_name, sob_name,"
					+ " branch_name, empcount_desc, title_id, emp_id, emp_name"
					+ " from " + compdb(comp_id) + "axela_mktg_lead"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = lead_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = lead_title_id"
					+ " inner join " + compdb(comp_id) + "axela_empcount on empcount_id = lead_empcount_id"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = lead_emp_id"
					+ " left join " + compdb(comp_id) + "axela_soe on soe_id = lead_leadsoe_id"
					+ " left join " + compdb(comp_id) + "axela_sob on sob_id = lead_leadsob_id"
					+ " where lead_id = " + lead_id + "" + BranchAccess + ExeAccess;
			// SOP("1==="+StrSql);
			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lead_branch_id = crs.getString("lead_branch_id");
					lead_title_id = crs.getString("lead_title_id");
					lead_fname = crs.getString("lead_fname");
					lead_lname = crs.getString("lead_lname");
					lead_jobtitle = crs.getString("lead_jobtitle");
					lead_date = crs.getString("lead_date");
					leaddate = strToShortDate(lead_date);
					lead_email = crs.getString("lead_email");
					lead_website = crs.getString("lead_website");
					lead_mobile = crs.getString("lead_mobile");
					lead_phone = crs.getString("lead_phone");
					lead_company = crs.getString("lead_company");
					lead_empcount_id = crs.getString("lead_empcount_id");
					lead_req = crs.getString("lead_req");
					lead_refno = crs.getString("lead_refno");
					lead_leadsoe_id = crs.getString("lead_leadsoe_id");
					lead_leadsob_id = crs.getString("lead_leadsob_id");
					lead_emp_id = crs.getString("lead_emp_id");
					lead_active = crs.getString("lead_active");
					lead_entry_id = crs.getString("lead_entry_id");
					entry_by = Exename(comp_id, crs.getInt("lead_entry_id"));
					entry_date = strToLongDate(crs.getString("lead_entry_date"));
					lead_modified_id = crs.getString("lead_modified_id");
					if (!lead_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(lead_modified_id));
						modified_date = strToLongDate(crs.getString("lead_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Lead!"));
			}
			crs.close();
			SOP("rs closed.");
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTitle(String lead_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " from " + compdb(comp_id) + "axela_title"
					+ " where 1 = 1"
					+ " order by title_desc ";
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), lead_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " from " + compdb(comp_id) + "axela_soe"
					+ " where 1 = 1"
					+ " group by soe_id"
					+ " order by soe_name";
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), lead_leadsoe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT sob_id, sob_name from " + compdb(comp_id) + "axela_sob"
					+ " where 1 = 1"
					+ " group by sob_id"
					+ " order by sob_name";
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), lead_leadsob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " from " + compdb(comp_id) + "axela_branch"
					+ " where 1 = 1 " + BranchAccess
					+ " order by branch_name";
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<option value = 0> Select </option>");
			// Str.append("<option value =0 " + StrSelectdrop("0", quote_branch_id) + ">Head Office</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), lead_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateEmpCount() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT empcount_id, empcount_desc"
					+ " from " + compdb(comp_id) + "axela_empcount"
					+ " where  1 = 1"
					+ " order by empcount_id asc";
			CachedRowSet crs = processQuery(StrSql);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("empcount_id")).append("");
				Str.append(StrSelectdrop(crs.getString("empcount_id"), lead_empcount_id));
				Str.append(">").append(crs.getString("empcount_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " where emp_sales = '1'"
					+ " AND emp_active = '1'"
					+ " AND (emp_branch_id = " + lead_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + lead_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), lead_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
