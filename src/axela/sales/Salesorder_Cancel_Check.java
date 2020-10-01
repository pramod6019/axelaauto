package axela.sales;
//aJIt 11th March, 2013

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.service.Ticket_Add;
import cloudify.connect.Connect;

public class Salesorder_Cancel_Check extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String addB = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String so_id = "0";
	public String so_contact_id = "0";
	public double so_booking_amount = 0;
	public String so_date = "";
	public String so_active = "0";
	public String contact_email1 = "", contact_mobile1 = "";
	public String cancel_date = "", canceldate = "";
	public String cancel_reason = "0", cancel_reason_name = "", cin_status = "0";
	public String so_cancelreason_id = "0", so_cinstatus_id = "0";
	public String so_retail_date = "";
	public String cancelreason = "", cinstatus = "";
	public String emp_id = "0";
	public String history_actiontype = "";
	public String emailmsg = "", emailsub = "";
	public String exeemailmsg = "", exeemailsub = "";
	public String so_notes = "";
	public String[] socin_mobile = null;

	public String brandconfig_socin_email_enable = "0";
	public String brandconfig_socin_email_sub = "";
	public String brandconfig_socin_email_format = "";
	public String brandconfig_socin_sms_enable = "0";
	public String brandconfig_socin_sms_format = "";

	public String brandconfig_socin_exe_email_enable = "0";
	public String brandconfig_socin_exe_email_sub = "";
	public String brandconfig_socin_exe_email_format = "";
	public String brandconfig_socin_exe_sms_enable = "0";
	public String brandconfig_socin_exe_sms_format = "";

	public String branch_socin_email = "";
	public String branch_socin_mobile = "";

	public String emp_email = "", emp_mobile = "", manager_email = "";
	public String branch_name = "", branch_email1 = "";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String ticket_enquiry_id = "", ticket_customer_id = "", ticket_contact_id = "", ticket_team_emp_id = "";
	public String ticket_customer_branch_id = "", ticket_enquiry_branch_id = "0", ticket_branch_brand_id = "", ticket_manager_email = "", ticket_branch_email = "", ticket_id = "";

	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (ReturnPerm(comp_id, "emp_sales_order_cancel", request).equals("1")) {
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					addB = PadQuotes(request.getParameter("add_button"));
					emp_id = CNumeric(GetSession("emp_id", request));
					cancelreason = PadQuotes(request.getParameter("cancelreason"));
					// cinstatus = PadQuotes(request.getParameter("cinstatus"));
					GetValues(request, response);
					if (!so_id.equals("0")) {
						StrHTML = GetSODetails();
					}
					PopulateConfigDetails();
					// SOP("emp_email==" + emp_email);
					if (addB.equals("yes") && so_active.equals("1")) {
						if (!so_retail_date.equals("") && ReturnPerm(comp_id, "emp_acc_receipt_edit", request).equals("0")) {
							StrHTML = "Chief Accountant's permission is required for SO Cancellation.";
						}
						else {
							StrHTML = CancelSalesOrder();
							GetTicketDetails(so_id);
							AddticketDetails();

							if (comp_email_enable.equals("1") && config_email_enable.equals("1")
									&& !branch_email1.equals("")) {
								if (!contact_email1.equals("") && !emp_email.equals("")
										&& brandconfig_socin_email_enable.equals("1")
										&& !brandconfig_socin_email_format.equals("")
										&& !brandconfig_socin_email_sub.equals("")) {
									SendEmail();
								}

								if (!emp_email.equals("")
										&& brandconfig_socin_exe_email_enable.equals("1")
										&& !brandconfig_socin_exe_email_format.equals("")
										&& !brandconfig_socin_exe_email_sub.equals("")) {
									SendEmailToExecutive();
								}
							}
							if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
								if (brandconfig_socin_sms_enable.equals("1") && !brandconfig_socin_sms_format.equals("") && !contact_mobile1.equals("")) {
									SendSMS();
								}
								if (brandconfig_socin_exe_sms_enable.equals("1") && !brandconfig_socin_exe_sms_format.equals("") && !emp_mobile.equals("")) {
									SendSMSToExecutive(emp_mobile);
								}
								if (!brandconfig_socin_exe_sms_format.equals("") && !branch_socin_mobile.equals("")) {
									socin_mobile = branch_socin_mobile.split(",");
									for (int i = 0; i < socin_mobile.length; i++) {
										socin_mobile[i] = socin_mobile[i].replace(" ", "");
										// SOP("socin_mobile[i]-----------------" + socin_mobile[i]);
										SendSMSToExecutive(socin_mobile[i]);
									}

								}
							}
						}

					} else if (addB.equals("yes") && so_active.equals("0")) {
						StrHTML = ActivateSalesOrder();
					}
				} else {
					StrHTML = "Access Denied!";
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
		canceldate = PadQuotes(request.getParameter("cancel_date"));
		cancel_reason = CNumeric(PadQuotes(request.getParameter("cancel_reason")));
		cin_status = CNumeric(PadQuotes(request.getParameter("cin_status")));
		so_date = PadQuotes(ExecuteQuery("SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id));
		so_active = CNumeric(PadQuotes(ExecuteQuery("SELECT so_active FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id)));
		so_notes = PadQuotes(request.getParameter("so_notes"));
	}

	protected void CheckForm() {
		msg = "";

		if (so_active.equals("1")) {
			if (canceldate.equals("")) {
				msg += "<br>Enter Sales Order Cancel Date!";
			} else {
				if (isValidDateFormatShort(canceldate)) {
					cancel_date = ConvertShortDateToStr(canceldate);
					if (!(Long.parseLong(cancel_date) >= Long.parseLong(so_date))) {
						msg += "<br>Sales Order Cancel Date must be greater than or equal to Sales Order Date!";
					}
					if ((Long.parseLong(cancel_date.substring(0, 8)) != Long.parseLong(ToShortDate(kknow()).substring(0, 8)))) {
						msg += "<br>Sales Order Cancel Date must be equal to Current Date!";
					}
				} else {
					msg += "<br>Enter valid Sales Order Cancel Date!";
				}
			}

			if (cancel_reason.equals("0")) {
				msg += "<br>Enter Cancel Reason!";
			}
			// if (cin_status.equals("0")) {
			// msg += "<br>Enter CIN status!";
			// }

			if (so_notes.equals("")) {
				msg += "<br>Enter Notes!";
			}
		} else if (so_active.equals("0")) {
			StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_active = '1'"
					+ "	AND so_id != " + so_id
					+ " AND so_enquiry_id = ( SELECT so_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id + " LIMIT 1)";
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br>Already Sales Order is active for this Enquiry!";
			}
		}
	}
	public String CancelSalesOrder() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				String history_oldvalue = "Active";

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ "	SET so_cancel_date = " + cancel_date + ","
						+ "	so_cancelreason_id = " + cancel_reason + ","
						+ "	so_cinstatus_id = " + 4 + ","
						+ " so_active = 0"
						+ " WHERE so_id = " + so_id;

				updateQuery(StrSql);

				msg = "Sales Order Cancelled Successfully!";

				String history_newvalue = "In-Active";
				history_actiontype = "Sales Order Cancel";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ("
						+ " " + so_id + ","
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue
						+ "')";
				updateQuery(StrSql);

				history_actiontype = "Cancel Reason";
				history_oldvalue = "";
				history_newvalue = ExecuteQuery("SELECT cancelreason_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
						+ " WHERE cancelreason_id = " + cancel_reason);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ("
						+ " " + so_id + ","
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);

				history_actiontype = "Cancel Date";
				history_oldvalue = "";
				history_newvalue = strToShortDate(ExecuteQuery("SELECT so_cancel_date FROM " + compdb(comp_id) + "axela_sales_so" + " WHERE so_id = " + so_id));

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ("
						+ " " + so_id + ","
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);

				history_actiontype = "CIN Status";
				history_oldvalue = "";
				history_newvalue = "Location Accountant";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ("
						+ " " + so_id + ","
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);

				// Notes

				history_actiontype = "Notes";
				history_oldvalue = "";
				history_oldvalue = ExecuteQuery("SELECT so_notes"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_id = " + so_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ " SET" + " so_notes = '" + so_notes + "'"
						+ " WHERE so_id = " + so_id + "";
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id," + " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES" + " ("
						+ so_id + "," + " "
						+ emp_id + "," + " '"
						+ ToLongDate(kknow()) + "'," + " '"
						+ history_actiontype + "'," + " '"
						+ history_oldvalue + "'," + " '"
						+ so_notes
						+ "')";
				updateQuery(StrSql);

				return msg;
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				return msg;
			}
		} else {
			return msg;
		}

	}

	public String ActivateSalesOrder() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				String history_oldvalue = "In-Active";

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ "	SET so_cancel_date = '',"
						+ "	so_cancelreason_id = 0,"
						+ "	so_cinstatus_id = 0,"
						+ " so_active = 1"
						+ " WHERE so_id = " + so_id;
				updateQuery(StrSql);
				msg = "Sales Order Activated Successfully!";

				String history_newvalue = "Active";

				history_actiontype = "Sales Order Active";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
						+ " (history_so_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ("
						+ " " + so_id + ","
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				return msg;
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				return msg;
			}
		} else {
			return msg;
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(brandconfig_socin_email_enable, '') AS brandconfig_socin_email_enable,"
					+ " COALESCE(brandconfig_socin_email_sub, '') AS brandconfig_socin_email_sub,"
					+ " COALESCE(brandconfig_socin_email_format, '') AS brandconfig_socin_email_format,"
					+ " COALESCE(brandconfig_socin_sms_enable, '') AS brandconfig_socin_sms_enable,"
					+ " COALESCE(brandconfig_socin_sms_format, '') AS brandconfig_socin_sms_format,"
					+ " COALESCE(brandconfig_socin_exe_email_enable, '') AS brandconfig_socin_exe_email_enable,"
					+ " COALESCE(brandconfig_socin_exe_email_sub, '') AS brandconfig_socin_exe_email_sub,"
					+ " COALESCE(brandconfig_socin_exe_email_format, '') AS brandconfig_socin_exe_email_format,"
					+ " COALESCE(brandconfig_socin_exe_sms_enable, '') AS brandconfig_socin_exe_sms_enable,"
					+ " COALESCE(brandconfig_socin_exe_sms_format, '') AS brandconfig_socin_exe_sms_format,"
					+ " COALESCE(branch_socin_email, '') AS branch_socin_email,"
					+ " COALESCE(branch_socin_mobile, '') AS branch_socin_mobile,"
					+ " COALESCE(IF(emp.emp_email1 != '', emp.emp_email1, emp.emp_email2), '') AS emp_email,"
					+ " COALESCE(IF(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2), '') AS emp_mobile,"
					+ " COALESCE(branch_name, '') AS branch_name,"
					+ " COALESCE(branch_email1, '') AS branch_email1,"
					+ " COALESCE(config_email_enable, '') AS config_email_enable,"
					+ " COALESCE(config_sms_enable, '') AS config_sms_enable,"
					+ " COALESCE(comp_email_enable, '') AS comp_email_enable,"
					+ " COALESCE(comp_sms_enable, '') AS comp_sms_enable"
					+ " FROM " + compdb(comp_id) + " axela_brand_config"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brandconfig_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + ","
					+ compdb(comp_id) + "axela_comp,"
					+ compdb(comp_id) + "axela_config"
					+ " WHERE branch_id = " + branch_id;
			// SOP("Strsql==PopulateConfigDetails==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				brandconfig_socin_email_enable = crs.getString("brandconfig_socin_email_enable");
				brandconfig_socin_email_sub = crs.getString("brandconfig_socin_email_sub");
				brandconfig_socin_email_format = crs.getString("brandconfig_socin_email_format");
				brandconfig_socin_sms_enable = crs.getString("brandconfig_socin_sms_enable");
				brandconfig_socin_sms_format = crs.getString("brandconfig_socin_sms_format");

				brandconfig_socin_exe_email_enable = crs.getString("brandconfig_socin_exe_email_enable");
				brandconfig_socin_exe_email_sub = crs.getString("brandconfig_socin_exe_email_sub");
				brandconfig_socin_exe_email_format = crs.getString("brandconfig_socin_exe_email_format");
				brandconfig_socin_exe_sms_enable = crs.getString("brandconfig_socin_exe_sms_enable");
				brandconfig_socin_exe_sms_format = crs.getString("brandconfig_socin_exe_sms_format");

				emp_email = crs.getString("emp_email");
				emp_mobile = crs.getString("emp_mobile");

				branch_socin_email = crs.getString("branch_socin_email");
				branch_socin_mobile = crs.getString("branch_socin_mobile");
				branch_name = crs.getString("branch_name");
				branch_email1 = crs.getString("branch_email1");

				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected String GetSODetails() {
		try {
			StrSql = "SELECT so_contact_id, so_branch_id,"
					+ " so_cancelreason_id, so_cinstatus_id, so_retail_date,"
					+ " so_booking_amount,"
					+ " contact_email1, contact_mobile1,"
					+ " COALESCE(IF(manager.emp_email1 != '', manager.emp_email1, manager.emp_email2), '') AS manager_email"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " WHERE so_id = " + so_id
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id");

			// SOP("Strsql==GetSODetails==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_contact_id = crs.getString("so_contact_id");
					branch_id = crs.getString("so_branch_id");
					so_cancelreason_id = crs.getString("so_cancelreason_id");
					so_retail_date = crs.getString("so_retail_date");
					so_cinstatus_id = crs.getString("so_cinstatus_id");
					contact_email1 = crs.getString("contact_email1");
					contact_mobile1 = crs.getString("contact_mobile1");
					manager_email = crs.getString("manager_email");
					so_booking_amount = crs.getDouble("so_booking_amount");
				}
			} else {
				msg = "Access Denied";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return msg;
	}

	protected void SendEmail() throws SQLException {
		emailmsg = (brandconfig_socin_email_format);
		emailsub = (brandconfig_socin_email_sub);

		emailsub = "REPLACE('" + emailsub + "', '[SOID]',so_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]',customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]',customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXENAME]',emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEMOBILE1]',emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEPHONE1]',emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEEMAIL1]',emp_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHNAME]',branch_name)";
		emailsub = "REPLACE(" + emailsub + ", '[MODELNAME]',model_name)";
		emailsub = "REPLACE(" + emailsub + ", '[ITEMNAME]',item_name)";

		emailmsg = "REPLACE('" + emailmsg + "', '[SOID]',so_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXENAME]',emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEPHONE1]',emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEEMAIL1]',emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHNAME]',branch_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[MODELNAME]',model_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[ITEMNAME]',item_name)";

		try {
			String email_cc = "";
			if (!branch_socin_email.equals("")) {
				email_cc += branch_socin_email + ",";
			}
			if (!branch_email1.equals("")) {
				email_cc += branch_email1 + ",";
			}
			if (!manager_email.equals("")) {
				email_cc += manager_email;
			}
			if (email_cc.charAt(email_cc.length() - 1) == ',') {
				email_cc = email_cc.substring(0, email_cc.length() - 1);
			}

			email_cc = RemoveDuplicateEmails(email_cc);
			contact_email1 = RemoveDuplicateEmails(contact_email1);

			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " '" + so_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " COALESCE(IF(emp_email1 != '', emp_email1, emp_email2), '') AS emp_email,"
					+ " '" + contact_email1 + "',"
					+ " '" + email_cc + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE so_id = " + so_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ "	email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==SendEmail==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendEmailToExecutive() throws SQLException {
		exeemailmsg = (brandconfig_socin_exe_email_format);
		exeemailsub = (brandconfig_socin_exe_email_sub);

		exeemailsub = "REPLACE('" + exeemailsub + "', '[SOID]',so_id)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CUSTOMERID]',customer_id)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CUSTOMERNAME]',customer_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXENAME]',emp_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEMOBILE1]',emp_mobile1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEPHONE1]',emp_phone1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEEMAIL1]',emp_email1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[BRANCHNAME]',branch_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[MODELNAME]',model_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[ITEMNAME]',item_name)";

		exeemailmsg = "REPLACE('" + exeemailmsg + "', '[SOID]',so_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERID]',customer_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERNAME]',customer_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXENAME]',emp_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEPHONE1]',emp_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEEMAIL1]',emp_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[BRANCHNAME]',branch_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[MODELNAME]',model_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[ITEMNAME]',item_name)";

		try {

			String email_cc = "";
			if (!branch_socin_email.equals("")) {
				email_cc += branch_socin_email + ",";
			}
			if (!branch_email1.equals("")) {
				email_cc += branch_email1 + ",";
			}
			if (!manager_email.equals("")) {
				email_cc += manager_email;
			}
			if (email_cc.charAt(email_cc.length() - 1) == ',') {
				email_cc = email_cc.substring(0, email_cc.length() - 1);
			}

			email_cc = RemoveDuplicateEmails(email_cc);

			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + emp_id + ","
					+ " emp_name,"
					+ " COALESCE(IF(emp_email1 != '', emp_email1, emp_email2), '') AS emp_email,"
					+ " COALESCE(IF(emp_email1 != '', emp_email1, emp_email2), '') AS emp_email,"
					+ " '" + email_cc + "',"
					+ " " + exeemailsub + ","
					+ " " + exeemailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0," + " "
					+ emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE so_id = " + so_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ "	email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==SendEmailToExecutive==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS() throws SQLException {
		emailmsg = (brandconfig_socin_sms_format);

		emailmsg = " REPLACE('" + emailmsg + "', '[SOID]',so_id)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CUSTOMERID]',customer_id)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CUSTOMERNAME]',customer_name)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = " REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[EXENAME]',emp_name)";
		emailmsg = " REPLACE(" + emailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = " REPLACE(" + emailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[EXEPHONE1]',emp_phone1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[EXEEMAIL1]',emp_email1)";
		emailmsg = " REPLACE(" + emailmsg + ", '[BRANCHNAME]',branch_name)";
		emailmsg = " REPLACE(" + emailmsg + ", '[MODELNAME]',model_name)";
		emailmsg = " REPLACE(" + emailmsg + ", '[ITEMNAME]',item_name)";
		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + so_contact_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + contact_mobile1 + "',"
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE so_id = " + so_id + "";
			// SOP("StrSql--------se----" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==SendSMS==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMSToExecutive(String mobile) throws SQLException {
		exeemailmsg = (brandconfig_socin_exe_sms_format);

		exeemailmsg = "REPLACE('" + exeemailmsg + "', '[SOID]',so_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERID]',customer_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERNAME]',customer_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXENAME]',emp_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEPHONE1]',emp_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEEMAIL1]',emp_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[BRANCHNAME]',branch_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[MODELNAME]',model_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[ITEMNAME]',item_name)";

		// SOP("mobile--------------" + mobile);
		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + emp_id + ","
					+ " emp_name,"
					+ " '" + mobile + "',"
					+ " " + unescapehtml(exeemailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";
			// SOP("StrSql--------se-----" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_emp_id,"
					+ " sms_emp,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==SendSMSToExecutive==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void GetTicketDetails(String so_id) {
		try {
			StrSql = "SELECT so_enquiry_id, enquiry_branch_id, so_customer_id, so_branch_id,"
					+ " so_contact_id, team_emp_id, branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_team ON team_id = teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " WHERE so_id =" + so_id;
			// SOP("StrSql====enq===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.next()) {
				ticket_enquiry_id = crs.getString("so_enquiry_id");
				ticket_customer_id = crs.getString("so_customer_id");
				ticket_contact_id = crs.getString("so_contact_id");
				ticket_team_emp_id = crs.getString("team_emp_id");
				ticket_customer_branch_id = crs.getString("so_branch_id");
				ticket_branch_brand_id = crs.getString("branch_brand_id");
				ticket_enquiry_branch_id = crs.getString("enquiry_branch_id");
				// ticket_manager_email = crs.getString("emp_email1");
				// ticket_branch_email = crs.getString("branch_email1");
			}
			crs.close();

			cancel_reason_name = PadQuotes(ExecuteQuery("SELECT cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " WHERE cancelreason_id = " + cancel_reason));

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddticketDetails() throws Exception {
		String ticket_owner_id = "", StrSql = "";
		StrSql = "SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE 1=1"
				+ " AND emp_branch_id = " + ticket_customer_branch_id
				+ " AND emp_ticket_owner = 1"
				+ " AND emp_active = 1 "
				+ " ORDER BY RAND()"
				+ " LIMIT 1";
		SOP("StrSql==123=" + StrSql);
		ticket_owner_id = CNumeric(ExecuteQuery(StrSql));

		if (ticket_owner_id.equals("0")) {
			StrSql = "SELECT emp.emp_id FROM " + compdb(comp_id) + "axela_emp_branch bran"
					+ " INNER JOIN " + compdb(comp_id) + " axela_emp emp ON emp.emp_id = bran.emp_id"
					+ " WHERE bran.emp_branch_id = " + ticket_customer_branch_id
					+ " AND emp.emp_ticket_owner = 1"
					+ " AND emp_active = 1 "
					+ " ORDER BY RAND()"
					+ " LIMIT 1";
			// SOP("1===" + StrSql);
			ticket_owner_id = CNumeric(ExecuteQuery(StrSql));
		}
		if (ticket_owner_id.equals("0")) {
			ticket_owner_id = ticket_team_emp_id;
		}

		Ticket_Add tkt = new Ticket_Add();
		tkt.comp_id = comp_id;
		tkt.emp_id = emp_id;
		tkt.ticket_branch_id = ticket_enquiry_branch_id;
		tkt.ticket_customer_id = ticket_customer_id;
		tkt.ticket_contact_id = ticket_contact_id;
		tkt.veh_id = "0";
		tkt.jc_id = "0";
		tkt.ticket_emp_id = ticket_owner_id;
		tkt.ticket_ticketsource_id = "1";
		tkt.ticket_report_time = ToLongDate(kknow());
		tkt.ticket_ticketstatus_id = "1";
		tkt.ticket_ticketcat_id = "1";
		tkt.ticket_priorityticket_id = "1";
		tkt.ticket_ticket_dept_id = "1";
		tkt.ticket_subject = "SO Cancellation So ID: " + so_id;
		tkt.ticket_desc = "SO Cancellation So ID: " + so_id + " \\r\\n Reason: " + cancel_reason_name
				+ " \\r\\n Booking Amount: " + so_booking_amount;
		tkt.customer_branch_id = ticket_customer_branch_id;
		tkt.ticket_enquiry_id = ticket_enquiry_id;
		tkt.ticket_so_id = so_id;
		// tkt.ticket_crm_id = crm_id;
		tkt.ticket_tickettype_id = "2";
		tkt.branch_brand_id = ticket_branch_brand_id;
		tkt.ticket_entry_id = emp_id;
		tkt.ticket_entry_date = ToLongDate(kknow());
		// tkt.PopulateConfigDetails(comp_id);
		tkt.AddFields(comp_id);
	}
}
