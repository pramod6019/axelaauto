package axela.preowned;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.service.Ticket_Add;
import cloudify.connect.Connect;

public class Preowned_CRMFollowup_Update extends Connect {

	public String update = "";
	public String add = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String preowned_id = "0";
	public String preowned_no = "";
	public String precrmfollowup_id = "";
	public String precrmfollowup_mobile1 = "";
	public String precrmfollowup_mobile2 = "";
	public String precrmfollowup_email1 = "";
	public String precrmfollowup_email2 = "";
	public String precrmfollowup_lostcase1_id = "0";
	public String precrmfollowup_lostcase2_id = "0";
	public String precrmfollowup_lostcase3_id = "0";
	public String precrmfollowup_desc = "";
	public String precrmfollowup_precrmfeedbacktype_id = "0";
	public String precrmfollowup_cancelreason_id = "0";
	public String precrmfollowup_entry_id = "0";
	// public String crm_entry_date = "";
	public String precrmfollowup_modified_id = "0";
	public String precrmfollowup_modified_time = "";
	// public String precrmtype_id = "0";
	public String precrmfollowup_precrmconcern_id = "0";
	public String precrmtype_name = "";
	public String return_perm = "0";
	public String precrmfollowupdays_lostfollowup = "", precrmfollowupdays = "";
	public String precrmfollowupdays_so_inactive = "";
	public String precrmfollowup_precrmfollowupdays_id = "";
	public String crmexeid = "0";
	public String crmexename = "";
	public String preowned_date, preowned_close_date = "";
	public String soe_name = "";
	public String sob_name = "";
	public String preownedlostcase1_name = "";
	public String preownedlostcase2_name = "";
	public String preownedlostcase3_name = "";
	public String exe_name = "", preownedexe_name = "";
	public String exe_email1 = "";
	public String manager_email1 = "", crmemp_email1 = "", pbfemp_email1 = "", psfemp_email1 = "", preownedexe_email1 = "";
	public String manager_name = "", crm_name = "";
	public String manager_mobile1 = "", crmemp_mobile1 = "";
	public String manager_jobtitle = "", crm_jobtitle = "";
	public String preownedstatus_name = "";
	// public String stage_name = "";
	public String so_id = "0";
	public String so_no = "";
	public String so_date = "";
	public String so_delivered_date = "";
	public String so_promise_date = "";
	public String so_payment_date = "";
	public String so_booking_amount = "", so_ew_amount = "";
	public String so_grandtotal = "";
	public String preownedmodel_name = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String contact_id = "0";
	public String contact_name = "";
	public String contactname = "";
	public String contact_mobile1 = "";
	public String contactmobile1 = "";
	public String contactmobile2 = "";
	public String contact_mobile2 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String variant_name = "";
	public String precrmfollowup_crm_emp_id = "";
	public String BranchAccess = "", ExeAccess = "", StrScript = "";
	public String precrmfollowup_ticket_emp_id = "0";
	public String crm_tickettype_id = "0";
	public String preowned_branch_id = "";
	public String preowned_emp_id = "";
	public String precrmfollowupdays_precrmtype_id = "";
	public String precrmfollowupdays_desc = "";
	public String precrmfollowup_satisfied = "";
	public String curryear = "", year1 = "", branch_ticket_email = "";
	public String branch_name = "", option_name = "", sooptionname = "";
	public String fincomp_name = "", engine_no = "", chassis_no = "", reg_no = "", occ_name = "";
	public String branch_brand_id = "0";
	// crm mail and msg

	public String precrmfollowupdays_satisfied_email_enable = "";
	public String precrmfollowupdays_satisfied_email_sub = "";
	public String precrmfollowupdays_satisfied_email_format = "";
	public String precrmfollowupdays_satisfied_email_exe_sub = "";
	public String precrmfollowupdays_satisfied_email_exe_format = "";
	public String precrmfollowupdays_satisfied_sms_enable = "";
	public String precrmfollowupdays_satisfied_sms_format = "";
	public String precrmfollowupdays_satisfied_sms_exe_format = "";

	public String precrmfollowupdays_dissatisfied_email_enable = "";
	public String precrmfollowupdays_dissatisfied_email_sub = "";
	public String precrmfollowupdays_dissatisfied_email_format = "";
	public String precrmfollowupdays_dissatisfied_email_exe_sub = "";
	public String precrmfollowupdays_dissatisfied_email_exe_format = "";
	public String precrmfollowupdays_dissatisfied_sms_enable = "";
	public String precrmfollowupdays_dissatisfied_sms_format = "";
	public String precrmfollowupdays_dissatisfied_sms_exe_format = "";
	// contact able
	public String precrmfollowupdays_contactable_email_enable = "";
	public String precrmfollowupdays_contactable_email_sub = "";
	public String precrmfollowupdays_contactable_email_format = "";
	public String precrmfollowupdays_contactable_email_exe_sub = "";
	public String precrmfollowupdays_contactable_email_exe_format = "";
	public String precrmfollowupdays_contactable_sms_enable = "";
	public String precrmfollowupdays_contactable_sms_format = "";
	public String precrmfollowupdays_contactable_sms_exe_format = "";
	// non contact able
	public String precrmfollowupdays_noncontactable_email_enable = "";
	public String precrmfollowupdays_noncontactable_email_sub = "";
	public String precrmfollowupdays_noncontactable_email_format = "";
	public String precrmfollowupdays_noncontactable_email_exe_sub = "";
	public String precrmfollowupdays_noncontactable_email_exe_format = "";
	public String precrmfollowupdays_noncontactable_sms_enable = "";
	public String precrmfollowupdays_noncontactable_sms_format = "";
	public String precrmfollowupdays_noncontactable_sms_exe_format = "";
	public String emailmsg = "", sub = "";
	public String send_contact_email = "";

	// public String config_admin_email = "";
	public String branch_email1 = "";
	public String branch_crm_mobile = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";

	// ticket executive

	public String config_ticket_preowned_crm_email_sub = "";
	public String config_ticket_preowned_crm_email_format = "";

	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String emp_name = "";
	public String emp_email_formail = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String ticket_id = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_preowned_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				precrmfollowup_id = CNumeric(PadQuotes(request.getParameter("precrmfollowup_id")));
				curryear = SplitYear(ToLongDate(kknow()));
				year1 = (Integer.parseInt(curryear) + 10) + "";
				PopulateFields(response);
				PopulateConfigDetails(comp_id);
				if ("Update Follow-up".equals(updateB)) {
					GetValues(request);
					if (add.equals("add")) {
						return_perm = ReturnPerm(comp_id, "emp_preowned_add", request);
					} else {
						return_perm = ReturnPerm(comp_id, "emp_preowned_edit", request);
					}
					if (return_perm.equals("1")) {
						CheckForm(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateCRMFollowup();
							PreownedCRMCustomFieldUpdate(comp_id, precrmfollowup_id, "yes", request);
							// /// Add Ticket if dissatisfied
							SOP("precrmfollowup_entry_id-----11------" + precrmfollowup_entry_id);
							if (precrmfollowup_entry_id.equals("0"))
							{
								conntx = connectDB();
								conntx.setAutoCommit(false);
								stmttx = conntx.createStatement();

								SOP("contact_email1--------" + contact_email1);
								SOP("contact_email2--------" + contact_email2);
								SOP("precrmfollowup_satisfied--------" + precrmfollowup_satisfied);
								SOP("precrmfollowupdays_satisfied_email_enable--------" + precrmfollowupdays_satisfied_email_enable);
								SOP("precrmfollowupdays_satisfied_email_format--------" + precrmfollowupdays_satisfied_email_format);
								SOP("precrmfollowupdays_satisfied_email_sub--------" + precrmfollowupdays_satisfied_email_sub);
								SOP("precrmfollowupdays_satisfied_email_exe_format--------" + precrmfollowupdays_satisfied_email_exe_format);
								SOP("precrmfollowupdays_satisfied_email_exe_sub--------" + precrmfollowupdays_satisfied_email_exe_sub);
								//
								// SOP("comp_email_enable--------" + comp_email_enable);
								// SOP("config_email_enable--------" + config_email_enable);
								// SOP("branch_email1--------" + branch_email1);
								// SOP("precrmfollowupdays_contactable_email_enable--------" + precrmfollowupdays_contactable_email_enable);
								// SOP("contact_email1--------" + contact_email1);
								// SOP("precrmfollowupdays_contactable_email_format--------" + precrmfollowupdays_contactable_email_format);
								// SOP("precrmfollowupdays_contactable_email_sub--------" + precrmfollowupdays_contactable_email_sub);
								// SOP("precrmfollowupdays_contactable_email_exe_format--------" + precrmfollowupdays_contactable_email_exe_format);
								// SOP("precrmfollowupdays_contactable_email_exe_sub--------" + precrmfollowupdays_contactable_email_exe_sub);
								// SOP("config_sms_enable--------" + config_sms_enable);
								// SOP("precrmfollowupdays_contactable_sms_enable--------" + precrmfollowupdays_contactable_sms_enable);
								// SOP("precrmfollowupdays_contactable_sms_format--------" + precrmfollowupdays_contactable_sms_format);
								// SOP("precrmfollowup_mobile1--------" + precrmfollowup_mobile1);
								// SOP("precrmfollowup_mobile2--------" + precrmfollowup_mobile2);
								// SOP("precrmfollowupdays_contactable_sms_exe_format--------" + precrmfollowupdays_contactable_sms_exe_format);
								// SOP("precrmfollowup_precrmfeedbacktype_id--------" + precrmfollowup_precrmfeedbacktype_id);
								// SOP("precrmfollowupdays_noncontactable_email_enable--------" + precrmfollowupdays_noncontactable_email_enable);
								// SOP("precrmfollowupdays_noncontactable_email_format--------" + precrmfollowupdays_noncontactable_email_format);
								// SOP("precrmfollowupdays_noncontactable_email_sub--------" + precrmfollowupdays_noncontactable_email_sub);
								// SOP("precrmfollowupdays_noncontactable_email_exe_format--------" + precrmfollowupdays_noncontactable_email_exe_format);
								// SOP("precrmfollowupdays_noncontactable_email_exe_sub--------" + precrmfollowupdays_noncontactable_email_exe_sub);
								// SOP("precrmfollowupdays_noncontactable_sms_enable--------" + precrmfollowupdays_noncontactable_sms_enable);
								// SOP("precrmfollowupdays_noncontactable_sms_format--------" + precrmfollowupdays_noncontactable_sms_format);
								// SOP("precrmfollowupdays_noncontactable_sms_exe_format--------" + precrmfollowupdays_noncontactable_sms_exe_format);

								if (!contact_email2.equals("") && !contact_email1.equals("")) {
									send_contact_email = contact_email1 + "," + contact_email2;
								} else if (!contact_email1.equals("")) {
									send_contact_email = contact_email1;
								} else if (!contact_email2.equals("")) {
									send_contact_email = contact_email2;
								}
								if (precrmfollowup_precrmfeedbacktype_id.equals("1")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& precrmfollowupdays_contactable_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !precrmfollowupdays_contactable_email_format.equals("")
												&& !precrmfollowupdays_contactable_email_sub.equals("")) {
											SendEmail("customer", precrmfollowup_precrmfeedbacktype_id, "0", comp_id);
										}

										if (!branch_email1.equals("")
												&& !precrmfollowupdays_contactable_email_exe_format.equals("")
												&& !precrmfollowupdays_contactable_email_exe_sub.equals("")) {
											SendEmail("executive", precrmfollowup_precrmfeedbacktype_id, "0", comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& precrmfollowupdays_contactable_sms_enable.equals("1")) {
										if (!precrmfollowupdays_contactable_sms_format.equals("")) {
											if (!precrmfollowup_mobile1.equals("")) {
												SendSMS("customer", precrmfollowup_precrmfeedbacktype_id, "0", precrmfollowup_mobile1);
											}
											if (!precrmfollowup_mobile2.equals("")) {
												SendSMS("customer", precrmfollowup_precrmfeedbacktype_id, "0", precrmfollowup_mobile2);
											}
										}
										if (!precrmfollowupdays_contactable_sms_exe_format.equals("")) {
											SendSMS("executive", precrmfollowup_precrmfeedbacktype_id, "0", branch_crm_mobile);
										}
									}
								}

								if (precrmfollowup_precrmfeedbacktype_id.equals("2")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& precrmfollowupdays_noncontactable_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !precrmfollowupdays_noncontactable_email_format.equals("")
												&& !precrmfollowupdays_noncontactable_email_sub.equals("")) {
											SendEmail("customer", precrmfollowup_precrmfeedbacktype_id, "0", comp_id);
										}

										if (!branch_email1.equals("")
												&& !precrmfollowupdays_noncontactable_email_exe_format.equals("")
												&& !precrmfollowupdays_noncontactable_email_exe_sub.equals("")) {
											SendEmail("executive", precrmfollowup_precrmfeedbacktype_id, "0", comp_id);
										}
									}
									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& precrmfollowupdays_noncontactable_sms_enable.equals("1")) {
										if (!precrmfollowupdays_noncontactable_sms_format.equals("")) {
											if (!precrmfollowup_mobile1.equals("")) {
												SendSMS("customer", precrmfollowup_precrmfeedbacktype_id, "0", precrmfollowup_mobile1);
											}
											if (!precrmfollowup_mobile2.equals("")) {
												SendSMS("customer", precrmfollowup_precrmfeedbacktype_id, "0", precrmfollowup_mobile2);
											}
										}
										if (!precrmfollowupdays_noncontactable_sms_exe_format.equals("")) {
											SendSMS("executive", precrmfollowup_precrmfeedbacktype_id, "0", branch_crm_mobile);
										}
									}
								}

								if (precrmfollowup_satisfied.equals("1")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& precrmfollowupdays_satisfied_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !precrmfollowupdays_satisfied_email_format.equals("")
												&& !precrmfollowupdays_satisfied_email_sub.equals("")) {
											SendEmail("customer", "0", precrmfollowup_satisfied, comp_id);
										}

										if (!branch_email1.equals("")
												&& !precrmfollowupdays_satisfied_email_exe_format.equals("")
												&& !precrmfollowupdays_satisfied_email_exe_sub.equals("")) {
											SendEmail("executive", "0", precrmfollowup_satisfied, comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& precrmfollowupdays_satisfied_sms_enable.equals("1")) {
										if (!precrmfollowupdays_satisfied_sms_format.equals("")) {
											if (!precrmfollowup_mobile1.equals("")) {
												SendSMS("customer", "0", precrmfollowup_satisfied, precrmfollowup_mobile1);
											}
											if (!precrmfollowup_mobile2.equals("")) {
												SendSMS("customer", "0", precrmfollowup_satisfied, precrmfollowup_mobile2);
											}
										}
										if (!precrmfollowupdays_satisfied_sms_exe_format.equals("")) {
											SendSMS("executive", "0", precrmfollowup_satisfied, branch_crm_mobile);
										}
									}

								}

								if (precrmfollowup_satisfied.equals("2")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& precrmfollowupdays_dissatisfied_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !precrmfollowupdays_dissatisfied_email_format.equals("")
												&& !precrmfollowupdays_dissatisfied_email_sub.equals("")) {
											SendEmail("customer", "0", precrmfollowup_satisfied, comp_id);
										}

										if (!branch_email1.equals("")
												&& !precrmfollowupdays_dissatisfied_email_exe_format.equals("")
												&& !precrmfollowupdays_dissatisfied_email_exe_sub.equals("")) {
											SendEmail("executive", "0", precrmfollowup_satisfied, comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& precrmfollowupdays_dissatisfied_sms_enable.equals("1")) {
										if (!precrmfollowupdays_dissatisfied_sms_format.equals("")) {
											if (!precrmfollowup_mobile1.equals("")) {
												SendSMS("customer", "0", precrmfollowup_satisfied, precrmfollowup_mobile1);
											}
											if (!precrmfollowup_mobile2.equals("")) {
												SendSMS("customer", "0", precrmfollowup_satisfied, precrmfollowup_mobile2);
											}
										}
										if (!precrmfollowupdays_dissatisfied_sms_exe_format.equals("") && !branch_crm_mobile.equals("")) {
											SendSMS("executive", "0", precrmfollowup_satisfied, branch_crm_mobile);
										}
									}
								}
								conntx.commit();
								// / SOP("Transaction commit...");
								// SOP("precrmfollowup_satisfied--------------before ticket-------" + precrmfollowup_satisfied);
								if (precrmfollowup_satisfied.equals("2")) {
									if (!precrmfollowup_ticket_emp_id.equals("0")) {
										Ticket_Add tkt = new Ticket_Add();
										tkt.comp_id = comp_id;
										tkt.emp_id = emp_id;
										tkt.ticket_branch_id = preowned_branch_id;
										tkt.ticket_customer_id = customer_id;
										tkt.ticket_contact_id = contact_id;
										tkt.veh_id = "0";
										tkt.jc_id = "0";
										if (!preownedexe_email1.equals("")) {
											tkt.ticket_cc += "" + preownedexe_email1 + "";
										}
										if (!crmemp_email1.equals("")) {
											tkt.ticket_cc += "," + crmemp_email1 + "";
										}
										if (!manager_email1.equals("")) {
											tkt.ticket_cc += "," + manager_email1 + "";
										}
										if (!branch_ticket_email.equals("")) {
											tkt.ticket_cc += "," + branch_ticket_email + "";
										}
										if (!precrmfollowup_mobile1.equals("")) {
											tkt.contact_mobile1 = precrmfollowup_mobile1;
										}
										if (!precrmfollowup_mobile2.equals("")) {
											tkt.contact_mobile2 = precrmfollowup_mobile2;
										}
										tkt.ticket_emp_id = precrmfollowup_ticket_emp_id;
										tkt.ticket_ticketsource_id = "1";
										tkt.ticket_report_time = ToLongDate(kknow());
										tkt.ticket_ticketstatus_id = "1";
										tkt.ticket_priorityticket_id = "1";
										tkt.ticket_ticket_dept_id = "1";
										tkt.ticket_subject = "Dis-satisfied Pre-Owned CRM Call";
										tkt.ticket_desc = precrmfollowup_desc;
										tkt.customer_branch_id = preowned_branch_id;
										tkt.ticket_preowned_id = preowned_id;
										tkt.ticket_so_id = so_id;
										tkt.ticket_preowned_crm_id = precrmfollowup_id;
										tkt.ticket_tickettype_id = "1";
										tkt.branch_brand_id = branch_brand_id;
										tkt.ticket_entry_id = emp_id;
										tkt.ticket_entry_date = ToLongDate(kknow());
										tkt.PopulateConfigDetails(comp_id);
										tkt.AddFields(comp_id);
										ticket_id = tkt.ticket_id;
										// SOP("ticket_id-----------crm-----" + ticket_id);
										// SOP("config_ticket_preowned_crm_email_sub-----------crm-----" + config_ticket_preowned_crm_email_sub);
										// / SOP("config_ticket_preowned_crm_email_format-----------crm-----" + config_ticket_preowned_crm_email_format);

										if (!ticket_id.equals("0")) {
											if (!config_ticket_preowned_crm_email_sub.equals("")
													&& !config_ticket_preowned_crm_email_format.equals("")) {
												SendCRMTicketEmail("1");
											}
										}
									}
								}
								conntx.commit();
							}
							response.sendRedirect(response.encodeRedirectURL("preowned-dash-crmfollowup.jsp?preowned_id=" + preowned_id + "&msg=CRM Follow-up updated successfully!#tabs-3"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	protected void PopulateFields(HttpServletResponse response)
			throws SQLException, IOException {
		String script = "";

		script = "replace(precrmfollowupdays_script, '[SALUTATION]'," + "'" + GetSalutation(ToLongDate(kknow())) + "')";
		script = "replace(" + script + ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
		script = "replace(" + script + ",'[EXENAME]',session.emp_name) as precrmfollowupdays_script";

		StrSql = "SELECT " + script + " ,preowned_branch_id, COALESCE(branch_ticket_email, '') AS branch_ticket_email,"
				+ " preowned_close_date, branch_brand_id, "
				+ " preowned_preownedlostcase1_id, preowned_preownedlostcase2_id, preowned_preownedlostcase3_id,"
				+ " precrmfollowup_desc, precrmfollowupdays_precrmtype_id, precrmfollowupdays_daycount, "
				+ " precrmfollowupdays_desc, precrmfollowupdays_lostfollowup, precrmfollowupdays_so_inactive,"
				+ " precrmfollowup_cancelreason_id, precrmfollowup_precrmfeedbacktype_id, precrmfollowup_satisfied,"
				+ " COALESCE(entryemp.emp_id,0) AS crmentryid, "
				+ " COALESCE(entryemp.emp_name,'') AS crmentryname, "
				+ " COALESCE(precrmfollowup_entry_time,'') AS precrmfollowup_entry_time,"
				+ " COALESCE(modifiedemp.emp_id,0) AS crmmodifiedid,"
				+ " COALESCE(modifiedemp.emp_name,'') AS modifiedname, "
				+ " COALESCE(precrmfollowup_modified_time,'') AS precrmfollowup_modified_time, "
				+ " COALESCE(preownedexe.emp_name, '') as preownedexe_name,"
				+ " COALESCE(preownedexe.emp_email1, '') as preownedexe_email1,"
				+ " COALESCE(manager.emp_name,'') as manager_name,"
				+ " COALESCE(manager.emp_email1, '') as manager_email1,"
				+ " COALESCE(crm.emp_name,'') AS crm_name, COALESCE(crm.emp_mobile1,'') AS crmemp_mobile1,"
				+ " COALESCE(crm.emp_email1,'') AS crmemp_email1,"
				+ " COALESCE(preownedcrmexe.emp_id,'') AS crmexeid,"
				+ " COALESCE(preownedcrmexe.emp_name,'') AS crmexename,"
				+ " precrmfollowup_lostcase1_id, precrmfollowup_lostcase2_id, precrmfollowup_lostcase3_id,"
				+ " COALESCE(crmjobtitle.jobtitle_desc,'') AS crm_jobtitle,"
				+ " preowned_id, preowned_no,"
				+ " precrmfollowup_precrmfollowupdays_id,"
				+ " preownedstatus_name, "
				+ " COALESCE(soe_name,'') soe_name,"
				+ " COALESCE(sob_name,'') sob_name,"
				+ " customer_id, customer_name,"
				+ " contact_id, COALESCE(concat(title_desc,' ', contact_fname,' ', contact_lname),'') AS contact_name, contact_address, title_gender,"
				+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_phone1, contact_phone2,"
				+ " COALESCE(session.emp_name, '') AS exe_name, "
				+ " COALESCE(session.emp_email1, '') AS exe_email1, preowned_date, variant_name,"
				+ " COALESCE (so_id, 0) AS so_id, "
				+ " COALESCE (so_no, '') AS so_no,"
				+ " COALESCE (so_date, '') AS so_date,"
				+ " COALESCE (so_promise_date, '') AS so_promise_date,"
				+ " COALESCE (so_delivered_date, '') AS so_delivered_date,"
				+ " COALESCE (so_payment_date, '') AS so_payment_date,"
				+ " COALESCE (so_booking_amount, '') AS so_booking_amount,"
				+ " COALESCE(so_ew_amount, '') AS so_ew_amount,"
				+ " COALESCE (so_grandtotal, '') AS so_grandtotal,"
				+ " COALESCE (preownedmodel_name, '') AS preownedmodel_name, "
				+ " precrmfollowup_precrmconcern_id, "
				+ " branch_name, "
				+ " COALESCE (soopt.option_name, '') AS sooptionname,"
				+ " COALESCE (fincomp_name, '') AS fincomp_name,"
				+ " COALESCE (stock_engine_no, '') AS engine_no,"
				+ " COALESCE (stock_chassis_no, '') AS chassis_no,"
				+ " COALESCE (so_reg_no, '') AS reg_no,"
				+ " precrmfollowup_ticket_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays on precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
				+ " INNER JOIN axela_brand on brand_id = precrmfollowupdays_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_status ON preownedstatus_id = preowned_preownedstatus_id"
				+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
				+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"

				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp session on session.emp_id = " + emp_id + " "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp preownedexe on preownedexe.emp_id = preowned_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = preowned_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager on manager.emp_id = team_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm on crm.emp_id = team_crm_emp_id " // team_crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp preownedcrmexe on preownedcrmexe.emp_id = precrmfollowup_crm_emp_id " // precrmfollowup_crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp on entryemp.emp_id = precrmfollowup_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp on modifiedemp.emp_id = precrmfollowup_modified_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmjobtitle on crmjobtitle.jobtitle_id = crm.emp_jobtitle_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = precrmfollowup_so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_stock ON stock_id = so_preownedstock_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_stock_option soopt ON soopt.option_id = so_option_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
				+ " WHERE precrmfollowup_id = " + precrmfollowup_id + ""
				+ BranchAccess.replace("branch_id", "preowned_branch_id")
				+ ExeAccess.replace("emp_id", "precrmfollowup_crm_emp_id")
				+ " GROUP BY preowned_id";
		SOP("StrSql--CRM-------888888--------" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);
		if (crs1.isBeforeFirst()) {
			while (crs1.next()) {
				StrScript = crs1.getString("precrmfollowupdays_script");
				preowned_branch_id = crs1.getString("preowned_branch_id");
				branch_name = crs1.getString("branch_name");
				sooptionname = crs1.getString("sooptionname");
				fincomp_name = crs1.getString("fincomp_name");
				engine_no = crs1.getString("engine_no");
				chassis_no = crs1.getString("chassis_no");
				reg_no = crs1.getString("reg_no");
				branch_brand_id = crs1.getString("branch_brand_id");
				branch_ticket_email = crs1.getString("branch_ticket_email");
				preowned_close_date = strToShortDate(crs1.getString("preowned_close_date"));
				precrmfollowup_desc = crs1.getString("precrmfollowup_desc");
				precrmfollowupdays_precrmtype_id = crs1.getString("precrmfollowupdays_precrmtype_id");
				precrmfollowupdays_desc = crs1.getString("precrmfollowupdays_daycount") + crs1.getString("precrmfollowupdays_desc");
				precrmfollowupdays_lostfollowup = crs1.getString("precrmfollowupdays_lostfollowup");
				precrmfollowupdays_so_inactive = crs1.getString("precrmfollowupdays_so_inactive");
				precrmfollowup_precrmfeedbacktype_id = crs1.getString("precrmfollowup_precrmfeedbacktype_id");
				precrmfollowup_cancelreason_id = crs1.getString("precrmfollowup_cancelreason_id");
				precrmfollowup_satisfied = crs1.getString("precrmfollowup_satisfied");
				precrmfollowup_entry_id = crs1.getString("crmentryid");

				if (!precrmfollowup_entry_id.equals("0")) {
					entry_by = "<a href=../portal/executive-summary.jsp?emp_id=" + crs1.getInt("crmentryid") + ">" + crs1.getString("crmentryname") + "</a>";
					entry_date = strToLongDate(crs1.getString("precrmfollowup_entry_time"));
				}
				precrmfollowup_modified_id = crs1.getString("crmmodifiedid");
				if (!precrmfollowup_modified_id.equals("0")) {
					modified_by = "<a href=../portal/executive-summary.jsp?emp_id=" + crs1.getInt("crmmodifiedid") + ">" + crs1.getString("modifiedname") + "</a>";
					modified_date = strToLongDate(crs1.getString("precrmfollowup_modified_time"));
				}
				preownedexe_email1 = crs1.getString("preownedexe_email1");
				manager_name = crs1.getString("manager_name");
				manager_email1 = crs1.getString("manager_email1");
				crm_name = crs1.getString("crm_name");
				crmemp_mobile1 = crs1.getString("crmemp_mobile1");
				crmemp_email1 = crs1.getString("crmemp_email1");
				if (precrmfollowup_entry_id.equals("0")) {
					precrmfollowup_lostcase1_id = crs1.getString("preowned_preownedlostcase1_id");
					precrmfollowup_lostcase2_id = crs1.getString("preowned_preownedlostcase2_id");
					precrmfollowup_lostcase3_id = crs1.getString("preowned_preownedlostcase3_id");
				} else {
					precrmfollowup_lostcase1_id = crs1.getString("precrmfollowup_lostcase1_id");
					precrmfollowup_lostcase2_id = crs1.getString("precrmfollowup_lostcase2_id");
					precrmfollowup_lostcase3_id = crs1.getString("precrmfollowup_lostcase3_id");
				}
				// SOP("precrmfollowup_lostcase1_id---------------" + precrmfollowup_lostcase1_id);
				// SOP("precrmfollowup_lostcase2_id---------------" + precrmfollowup_lostcase2_id);
				// SOP("precrmfollowup_lostcase3_id---------------" + precrmfollowup_lostcase3_id);

				preowned_id = crs1.getString("preowned_id");
				preowned_no = crs1.getString("preowned_no");
				if (crs1.getString("precrmfollowupdays_lostfollowup").equals("1")) {
					GetEnquiryLostCase(preowned_id);
				}
				crm_jobtitle = crs1.getString("crm_jobtitle");
				precrmfollowup_precrmfollowupdays_id = crs1.getString("precrmfollowup_precrmfollowupdays_id");
				crmexeid = crs1.getString("crmexeid");
				crmexename = crs1.getString("crmexename");
				soe_name = crs1.getString("soe_name");
				sob_name = crs1.getString("sob_name");
				customer_id = crs1.getString("customer_id");
				customer_name = crs1.getString("customer_name");
				contact_id = crs1.getString("contact_id");
				contact_name = crs1.getString("contact_name");
				contactname = crs1.getString("contact_name");
				contact_email1 = crs1.getString("contact_email1");
				contact_email2 = crs1.getString("contact_email2");
				contact_phone1 = crs1.getString("contact_phone1");
				contact_phone2 = crs1.getString("contact_phone2");
				contact_address = crs1.getString("contact_address");
				exe_name = crs1.getString("exe_name");
				exe_email1 = crs1.getString("exe_email1");
				preowned_date = strToShortDate(crs1.getString("preowned_date"));
				variant_name = crs1.getString("variant_name");
				so_id = crs1.getString("so_id");
				so_no = crs1.getString("so_no");
				if (!so_id.equals("0")) {
					so_date = strToShortDate(crs1.getString("so_date"));
					so_delivered_date = strToShortDate(crs1.getString("so_delivered_date"));
					so_promise_date = strToShortDate(crs1.getString("so_promise_date"));
					so_payment_date = strToShortDate(crs1.getString("so_payment_date"));
					so_booking_amount = IndFormat(crs1.getString("so_booking_amount"));
					so_ew_amount = IndFormat(crs1.getString("so_ew_amount"));
					so_grandtotal = IndFormat(crs1.getString("so_grandtotal"));
				}
				preownedmodel_name = crs1.getString("preownedmodel_name");
				precrmfollowup_precrmconcern_id = crs1.getString("precrmfollowup_precrmconcern_id");
				precrmfollowup_mobile1 = crs1.getString("contact_mobile1");
				precrmfollowup_mobile2 = crs1.getString("contact_mobile2");
				precrmfollowup_email1 = crs1.getString("contact_email1");
				precrmfollowup_email2 = crs1.getString("contact_email2");
				precrmfollowup_desc = crs1.getString("precrmfollowup_desc");
				preownedexe_name = crs1.getString("preownedexe_name");
				precrmfollowup_ticket_emp_id = crs1.getString("precrmfollowup_ticket_emp_id");
			}
		} else {
			msg += "Pre-Owned Consultant is not allocated to the Team";
			response.sendRedirect(response
					.encodeRedirectURL("../portal/error.jsp?msg=" + msg));
		}
		crs1.close();
	}
	/**
	 * @param comp_id
	 */
	public void PopulateConfigDetails(String comp_id) {
		StrSql = "SELECT  branch_email1,"
				+ " config_email_enable, branch_crm_mobile, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_ticket_preowned_crm_email_sub, config_ticket_preowned_crm_email_format,"
				+ " precrmfollowupdays_contactable_email_enable, precrmfollowupdays_contactable_email_sub, precrmfollowupdays_contactable_email_format,"
				+ " precrmfollowupdays_contactable_email_exe_sub, precrmfollowupdays_contactable_email_exe_format,"
				+ " precrmfollowupdays_contactable_sms_enable, precrmfollowupdays_contactable_sms_format,"
				+ " precrmfollowupdays_contactable_sms_exe_format , precrmfollowupdays_noncontactable_email_enable,"
				+ " precrmfollowupdays_noncontactable_email_sub , precrmfollowupdays_noncontactable_email_format,"
				+ " precrmfollowupdays_noncontactable_email_exe_sub , precrmfollowupdays_noncontactable_email_exe_format,"
				+ " precrmfollowupdays_noncontactable_sms_enable , precrmfollowupdays_noncontactable_sms_format,"
				+ " precrmfollowupdays_noncontactable_sms_exe_format ,"
				+ " COALESCE(precrmfollowupdays_satisfied_email_enable,'') AS precrmfollowupdays_satisfied_email_enable,"
				+ " COALESCE(precrmfollowupdays_satisfied_email_sub,'') AS precrmfollowupdays_satisfied_email_sub,"
				+ " COALESCE(precrmfollowupdays_satisfied_email_format,'') AS precrmfollowupdays_satisfied_email_format,"
				+ " COALESCE(precrmfollowupdays_satisfied_email_exe_sub,'') AS precrmfollowupdays_satisfied_email_exe_sub,"
				+ " COALESCE(precrmfollowupdays_satisfied_email_exe_format,'') AS precrmfollowupdays_satisfied_email_exe_format,"
				+ " COALESCE(precrmfollowupdays_satisfied_sms_enable,'') AS precrmfollowupdays_satisfied_sms_enable,"
				+ " COALESCE(precrmfollowupdays_satisfied_sms_format,'') AS precrmfollowupdays_satisfied_sms_format,"
				+ " COALESCE(precrmfollowupdays_satisfied_sms_exe_format,'') AS precrmfollowupdays_satisfied_sms_exe_format,"
				+ " COALESCE(precrmfollowupdays_dissatisfied_email_enable,'') AS precrmfollowupdays_dissatisfied_email_enable, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_email_sub,'') AS precrmfollowupdays_dissatisfied_email_sub, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_email_format,'') AS precrmfollowupdays_dissatisfied_email_format, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_email_exe_sub,'') AS precrmfollowupdays_dissatisfied_email_exe_sub, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_email_exe_format,'') AS precrmfollowupdays_dissatisfied_email_exe_format, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_sms_enable,'') AS precrmfollowupdays_dissatisfied_sms_enable, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_sms_format,'') AS precrmfollowupdays_dissatisfied_sms_format, "
				+ " COALESCE(precrmfollowupdays_dissatisfied_sms_exe_format,'') AS precrmfollowupdays_dissatisfied_sms_exe_format "
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = precrmfollowup_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id,"
				+ compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1 "
				+ " and precrmfollowup_id = " + precrmfollowup_id;
		// SOP("StrSql---PopulateConfigDetails-------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				// ticket
				config_ticket_preowned_crm_email_sub = crs.getString("config_ticket_preowned_crm_email_sub");
				config_ticket_preowned_crm_email_format = crs.getString("config_ticket_preowned_crm_email_format");

				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				branch_crm_mobile = crs.getString("branch_crm_mobile");
				precrmfollowupdays_contactable_email_enable = crs.getString("precrmfollowupdays_contactable_email_enable");
				precrmfollowupdays_contactable_email_sub = crs.getString("precrmfollowupdays_contactable_email_sub");
				precrmfollowupdays_contactable_email_format = crs.getString("precrmfollowupdays_contactable_email_format");
				precrmfollowupdays_contactable_email_exe_sub = crs.getString("precrmfollowupdays_contactable_email_exe_sub");
				precrmfollowupdays_contactable_email_exe_format = crs.getString("precrmfollowupdays_contactable_email_exe_format");
				precrmfollowupdays_contactable_sms_enable = crs.getString("precrmfollowupdays_contactable_sms_enable");
				precrmfollowupdays_contactable_sms_format = crs.getString("precrmfollowupdays_contactable_sms_format");
				precrmfollowupdays_contactable_sms_exe_format = crs.getString("precrmfollowupdays_contactable_sms_exe_format");
				precrmfollowupdays_noncontactable_email_enable = crs.getString("precrmfollowupdays_noncontactable_email_enable");
				precrmfollowupdays_noncontactable_email_sub = crs.getString("precrmfollowupdays_noncontactable_email_sub");
				precrmfollowupdays_noncontactable_email_format = crs.getString("precrmfollowupdays_noncontactable_email_format");
				precrmfollowupdays_noncontactable_email_exe_sub = crs.getString("precrmfollowupdays_noncontactable_email_exe_sub");
				precrmfollowupdays_noncontactable_email_exe_format = crs.getString("precrmfollowupdays_noncontactable_email_exe_format");
				precrmfollowupdays_noncontactable_sms_enable = crs.getString("precrmfollowupdays_noncontactable_sms_enable");
				precrmfollowupdays_noncontactable_sms_format = crs.getString("precrmfollowupdays_noncontactable_sms_format");
				precrmfollowupdays_noncontactable_sms_exe_format = crs.getString("precrmfollowupdays_noncontactable_sms_exe_format");
				precrmfollowupdays_satisfied_email_enable = crs.getString("precrmfollowupdays_satisfied_email_enable");
				precrmfollowupdays_satisfied_email_sub = crs.getString("precrmfollowupdays_satisfied_email_sub");
				precrmfollowupdays_satisfied_email_format = crs.getString("precrmfollowupdays_satisfied_email_format");
				precrmfollowupdays_satisfied_email_exe_sub = crs.getString("precrmfollowupdays_satisfied_email_exe_sub");
				precrmfollowupdays_satisfied_email_exe_format = crs.getString("precrmfollowupdays_satisfied_email_exe_format");
				precrmfollowupdays_satisfied_sms_enable = crs.getString("precrmfollowupdays_satisfied_sms_enable");
				precrmfollowupdays_satisfied_sms_format = crs.getString("precrmfollowupdays_satisfied_sms_format");
				precrmfollowupdays_satisfied_sms_exe_format = crs.getString("precrmfollowupdays_satisfied_sms_exe_format");
				precrmfollowupdays_dissatisfied_email_enable = crs.getString("precrmfollowupdays_dissatisfied_email_enable");
				precrmfollowupdays_dissatisfied_email_sub = crs.getString("precrmfollowupdays_dissatisfied_email_sub");
				precrmfollowupdays_dissatisfied_email_format = crs.getString("precrmfollowupdays_dissatisfied_email_format");
				precrmfollowupdays_dissatisfied_email_exe_sub = crs.getString("precrmfollowupdays_dissatisfied_email_exe_sub");
				precrmfollowupdays_dissatisfied_email_exe_format = crs.getString("precrmfollowupdays_dissatisfied_email_exe_format");
				precrmfollowupdays_dissatisfied_sms_enable = crs.getString("precrmfollowupdays_dissatisfied_sms_enable");
				precrmfollowupdays_dissatisfied_sms_format = crs.getString("precrmfollowupdays_dissatisfied_sms_format");
				precrmfollowupdays_dissatisfied_sms_exe_format = crs.getString("precrmfollowupdays_dissatisfied_sms_exe_format");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request) {
		add = PadQuotes(request.getParameter("txt_add"));
		precrmfollowup_lostcase1_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowup_lostcase1_id")));
		precrmfollowup_lostcase2_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowup_lostcase2_id")));
		precrmfollowup_lostcase3_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowup_lostcase3_id")));
		precrmfollowup_cancelreason_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowup_cancelreason_id")));
		precrmfollowup_desc = PadQuotes(request.getParameter("txt_precrmfollowup_desc"));
		precrmfollowup_precrmfeedbacktype_id = PadQuotes(request.getParameter("dr_feedbacktype"));
		StrScript = unescapehtml(PadQuotes(request.getParameter("txt_StrScript")));
		precrmfollowup_mobile1 = PadQuotes(request.getParameter("txt_precrmfollowup_mobile1"));
		precrmfollowup_mobile2 = PadQuotes(request.getParameter("txt_precrmfollowup_mobile2"));
		precrmfollowup_email1 = PadQuotes(request.getParameter("txt_precrmfollowup_email1"));
		precrmfollowup_email2 = PadQuotes(request.getParameter("txt_precrmfollowup_email2"));
		precrmfollowup_satisfied = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowup_satisfied")));
		precrmfollowup_precrmconcern_id = PadQuotes(request.getParameter("dr_precrmfollowup_crmconcern_id"));
		precrmfollowup_ticket_emp_id = PadQuotes(request.getParameter("dr_ticketowner_id"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		// precrmfollowup_entry_id = PadQuotes(request.getParameter("precrmfollowup_entry_id"));
		precrmfollowup_modified_id = PadQuotes(request.getParameter("precrmfollowup_modified_id"));
	}

	public void UpdateCRMFollowup() {
		if (msg.equals("")) {
			try {
				String tagname = "";
				if (precrmfollowup_satisfied.equals("1")) {
					tagname = "Satisfied";
				} else if (precrmfollowup_satisfied.equals("2")) {
					tagname = "Dis-Satisfied";
				}

				StrSql = "INSERT into " + compdb(comp_id) + "axela_preowned_history"
						+ " (preownedhistory_preowned_id,"
						+ " preownedhistory_emp_id,"
						+ " preownedhistory_datetime,"
						+ " preownedhistory_actiontype,"
						+ " preownedhistory_newvalue,"
						+ " preownedhistory_oldvalue)"
						+ " values ("
						+ " '" + preowned_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 'Tag Added',"
						+ " '" + tagname + "',"
						+ "'')";// preownedhistory_oldvalue

				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crmfollowup"
						+ " SET" + " precrmfollowup_mobile1 = '" + precrmfollowup_mobile1 + "',"
						+ " precrmfollowup_mobile2 = '" + precrmfollowup_mobile2 + "',"
						+ " precrmfollowup_email1 = '" + precrmfollowup_email1 + "',"
						+ " precrmfollowup_email2 = '" + precrmfollowup_email2 + "',"
						+ " precrmfollowup_lostcase1_id = " + precrmfollowup_lostcase1_id + ","
						+ " precrmfollowup_lostcase2_id = " + precrmfollowup_lostcase2_id + ","
						+ " precrmfollowup_lostcase3_id = " + precrmfollowup_lostcase3_id + ","
						+ " precrmfollowup_cancelreason_id = " + precrmfollowup_cancelreason_id + ","
						+ " precrmfollowup_desc = '" + precrmfollowup_desc + "',"
						+ " precrmfollowup_precrmfeedbacktype_id = " + precrmfollowup_precrmfeedbacktype_id + ","
						+ " precrmfollowup_satisfied = " + precrmfollowup_satisfied + ","
						+ " precrmfollowup_precrmconcern_id = " + precrmfollowup_precrmconcern_id + ","
						+ " precrmfollowup_ticket_emp_id = " + precrmfollowup_ticket_emp_id + ",";

				if (precrmfollowup_entry_id.equals("0")) {
					StrSql = StrSql + " precrmfollowup_entry_id = " + emp_id + ","
							+ " precrmfollowup_entry_time = '" + ToLongDate(kknow()) + "'";
				} else {
					StrSql = StrSql + " precrmfollowup_modified_id = " + emp_id + ","
							+ " precrmfollowup_modified_time = '" + ToLongDate(kknow()) + "'";
				}
				StrSql = StrSql + " WHERE precrmfollowup_id = " + precrmfollowup_id + ""
						+ " and precrmfollowup_preowned_id = " + preowned_id;
				// SOP("STrSql=====" + StrSql);
				updateQuery(StrSql);

				// SOP("StrSql======updatecrmfollowup====" + StrSql);
				if (!contact_mobile1.equals(precrmfollowup_mobile1)) {
					StrSql = "Insert into " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " values" + " (" + preowned_id + ", "
							+ emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 1',"
							+ " '" + contact_mobile1 + "',"
							+ " '" + precrmfollowup_mobile1 + "')";
					updateQuery(StrSql);
					// SOP("strsqlll======22222===="+StrSql);
				}
				if (!contact_mobile2.equals(precrmfollowup_mobile2)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_preowned_history"
							+ " (preownedhistory_preowned_id," + " preownedhistory_emp_id,"
							+ " preownedhistory_datetime," + " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue," + " preownedhistory_newvalue)"
							+ " values" + " (" + preowned_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 2'," + " '" + contact_mobile2
							+ "'," + " '" + precrmfollowup_mobile2 + "')";
					updateQuery(StrSql);
				}
				if (!contact_email1.equals(precrmfollowup_email1)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_preowned_history"
							+ " (preownedhistory_preowned_id," + " preownedhistory_emp_id,"
							+ " preownedhistory_datetime," + " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue," + " preownedhistory_newvalue)"
							+ " values" + " (" + preowned_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 1'," + " '" + contact_email1
							+ "'," + " '" + precrmfollowup_email1 + "')";
					updateQuery(StrSql);
				}
				if (!contact_email2.equals(precrmfollowup_email2)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_preowned_history"
							+ " (preownedhistory_preowned_id," + " preownedhistory_emp_id,"
							+ " preownedhistory_datetime," + " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue," + " preownedhistory_newvalue)"
							+ " values" + " (" + preowned_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 2'," + " '" + contact_email2
							+ "'," + " '" + precrmfollowup_email2 + "')";
					updateQuery(StrSql);
				}

				// //update email for contact/////
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET" + " contact_mobile1 = '" + precrmfollowup_mobile1 + "',"
						+ " contact_mobile2 = '" + precrmfollowup_mobile2 + "',"
						+ " contact_email1 = '" + precrmfollowup_email1 + "',"
						+ " contact_email2 = '" + precrmfollowup_email2 + "'"
						+ " WHERE contact_id = " + contact_id + "";
				// SOP("StrSql = " + StrSqlBreaker(StrSql));
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		msg += CRMCustomFieldValidate(comp_id, precrmfollowup_id, "2", request);
		if (precrmfollowup_precrmfeedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Feedback Type!";
		}
		if (!precrmfollowup_mobile1.equals("")) {
			if (!IsValidMobileNo11(precrmfollowup_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			}
		}
		if (!precrmfollowup_mobile2.equals("")) {
			if (!IsValidMobileNo11(precrmfollowup_mobile2)) {
				msg = msg + "<br>Enter Valid Contact Mobile 2!";
			}
		}
		if (!precrmfollowup_email1.equals("")) {
			if (!IsValidEmail(precrmfollowup_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1 !";
			}
		}
		if (!precrmfollowup_email2.equals("")) {
			if (!IsValidEmail(precrmfollowup_email2)) {
				msg = msg + "<br>Enter Valid Contact Email 2!";
			}
		}
		if (precrmfollowupdays_lostfollowup.equals("1") && precrmfollowup_precrmfeedbacktype_id.equals("1")) {

			if (precrmfollowup_lostcase1_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 1!";
			}
			if (precrmfollowup_lostcase2_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 2!";
			}
			if (precrmfollowup_lostcase3_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 3!";
			}
		} else {
			precrmfollowup_lostcase1_id = "0";
			precrmfollowup_lostcase2_id = "0";
			precrmfollowup_lostcase3_id = "0";
		}
		if (precrmfollowup_desc.equals("")) {
			msg = msg + "<br>Enter Feedback!";
		} else if (precrmfollowup_satisfied.equals("2")) {
			if (precrmfollowup_desc.length() < 5) {
				msg = msg + "<br>Feedback should have atleast five characters!";
			}
			if (precrmfollowup_desc.length() > 999) {
				precrmfollowup_desc = precrmfollowup_desc.substring(0, 999);
			}
		}

		if (precrmfollowup_precrmfeedbacktype_id.equals("1")) {
			if (precrmfollowup_satisfied.equals("0")) {
				msg = msg + "<br>Select Overall Experience!";
			}
		}

		if (precrmfollowup_satisfied.equals("2")) {
			if (precrmfollowup_precrmconcern_id.equals("0")) {
				msg = msg + "<br>Select CRM Concern!";
			}
		}

		if (precrmfollowup_satisfied.equals("2")) {
			if (precrmfollowup_ticket_emp_id.equals("0")) {
				msg = msg + "<br>Select Ticket Owner!";
			}
		}
		if (precrmfollowup_precrmfeedbacktype_id.equals("2")) {
			precrmfollowup_satisfied = "0";
			precrmfollowup_precrmconcern_id = "0";
			precrmfollowup_ticket_emp_id = "0";
		}
		if (precrmfollowup_satisfied.equals("1")) {
			precrmfollowup_precrmconcern_id = "0";
			precrmfollowup_ticket_emp_id = "0";
		}

	}
	public String PopulateCRMFeedbackType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT precrmfeedbacktype_id, precrmfeedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfeedbacktype" + " WHERE 1=1"
					+ " ORDER BY precrmfeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("precrmfeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmfeedbacktype_id"), precrmfollowup_precrmfeedbacktype_id));
				Str.append(">").append(crs.getString("precrmfeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLostCase1(String comp_id, String precrmfollowup_lostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase1_id, preownedlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY preownedlostcase1_name";
			// SOP("strsql=------------PopulateLostCase1---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase1_id"), precrmfollowup_lostcase1_id));
				Str.append(">").append(crs.getString("preownedlostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateLostCase2(String comp_id, String precrmfollowup_lostcase2_id, String preownedlostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase2_id, preownedlostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " WHERE preownedlostcase2_lostcase1_id = " + CNumeric(preownedlostcase1_id)
					+ " ORDER BY preownedlostcase2_name";
			// SOP("strsql=------------PopulateLostCase2---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_precrmfollowup_lostcase2_id\" id=\"dr_precrmfollowup_lostcase2_id\" class=\"form-control\" onchange=\"populateLostCase3()\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase2_id"), precrmfollowup_lostcase2_id));
				Str.append(">").append(crs.getString("preownedlostcase2_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateLostCase3(String comp_id, String precrmfollowup_lostcase3_id, String preownedlostcase2_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase3_id, preownedlostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
					+ " WHERE preownedlostcase3_lostcase2_id = " + CNumeric(preownedlostcase2_id)
					+ " ORDER BY preownedlostcase3_name";
			// SOP("StrSql-PopulateLostCase3------------s--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_precrmfollowup_lostcase3_id\" id=\"dr_precrmfollowup_lostcase3_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase3_id"), precrmfollowup_lostcase3_id));
				Str.append(">").append(crs.getString("preownedlostcase3_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateCRMSatisfied(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", precrmfollowup_satisfied)).append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", precrmfollowup_satisfied)).append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	public String PopulateCancelReason(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// SOP("cancelreason==pop===" + precrmfollowup_cancelreason_id);
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id")).append("");
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), precrmfollowup_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
	public String PopulateConcern(String comp_id, String precrmfollowup_precrmconcern_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT precrmconcern_id,"
					+ "	precrmconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_concern "
					+ " WHERE precrmconcern_precrmtype_id = 1"
					+ " GROUP BY precrmconcern_id"
					+ " ORDER BY precrmconcern_desc";
			// SOP("StrSql-1-" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmconcern_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmconcern_id"), precrmfollowup_precrmconcern_id));
				Str.append(">").append(crs.getString("precrmconcern_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
		return Str.toString();
	}
	public String PopulateExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 and emp_ticket_owner='1' AND emp_active='1' AND (emp_branch_id = " + preowned_branch_id
					+ " or emp_id = 1" + " or emp_id in (SELECT empbr.emp_id "
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id)
					+ " axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + preowned_branch_id + ")"
					+ " OR emp_all_branches = 1)"
					// + ExeAccess
					// + " and emp_id = " + preowned_emp_id
					+ " group by emp_id " + " ORDER BY emp_name ";
			SOP("StrSql------PopulateExecutive-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), precrmfollowup_ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
		return Str.toString();
	}

	public void GetEnquiryLostCase(String preowned_id) {
		try {
			StrSql = "SELECT "
					+ " preownedlostcase1_id, COALESCE (concat('(',preownedlostcase1_name,')'), '') preownedlostcase1_name,"
					+ " preownedlostcase2_id, COALESCE (concat('(',preownedlostcase2_name,')'), '') preownedlostcase2_name,"
					+ " preownedlostcase3_id, COALESCE (concat('(',preownedlostcase3_name,')'), '') preownedlostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_lostcase1 ON preownedlostcase1_id = preowned_preownedlostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_lostcase2 ON preownedlostcase2_id = preowned_preownedlostcase2_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_lostcase3 ON preownedlostcase3_id = preowned_preownedlostcase3_id"
					+ " WHERE 1=1"
					+ " AND preowned_id = " + preowned_id
					+ " GROUP BY preowned_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedlostcase1_name = crs.getString("preownedlostcase1_name");
					preownedlostcase2_name = crs.getString("preownedlostcase2_name");
					preownedlostcase3_name = crs.getString("preownedlostcase3_name");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
	}

	public String GetSalutation(String date) {
		String salutation = "";
		int time = 0;
		time = Integer.parseInt(date.substring(8, 10));
		if (time < 12 && time >= 0) {
			salutation = "Good Morning";
		} else if (time < 16 && time >= 12) {
			salutation = "Good Afternoon";
		} else if (time < 24 && time >= 16) {
			salutation = "Good Evening";
		}
		return salutation;
	}

	protected void SendEmail(String type, String contactable, String satisfied, String comp_id) throws SQLException {
		String emailmsg = "", sub = "", email_from = "", email_to = "", email_contact_id = "", email_contact_name = "";
		String email_cc = "";
		// SOP("type-------" + type);
		// SOP("contactable--------" + contactable);
		// SOP("statisfied--------" + statisfied);

		if (type.equals("customer")) {
			email_from = branch_email1;
			email_to = send_contact_email;
			if (!preownedexe_email1.equals("")) {
				email_cc += "" + preownedexe_email1 + "";
			}
			if (!crmemp_email1.equals("")) {
				email_cc += "," + crmemp_email1 + "";
			}
			if (!manager_email1.equals("")) {
				email_cc += "," + manager_email1 + "";
			}

			email_contact_id = contact_id;
			email_contact_name = contact_name;
			if (contactable.equals("1")) {
				sub = precrmfollowupdays_contactable_email_sub;
				emailmsg = precrmfollowupdays_contactable_email_format;

			}
			else if (contactable.equals("2")) {
				sub = precrmfollowupdays_noncontactable_email_sub;
				emailmsg = precrmfollowupdays_noncontactable_email_format;
			}

			if (satisfied.equals("1")) {
				sub = precrmfollowupdays_satisfied_email_sub;
				emailmsg = precrmfollowupdays_satisfied_email_format;
			}
			else if (satisfied.equals("2")) {
				sub = precrmfollowupdays_dissatisfied_email_sub;
				emailmsg = precrmfollowupdays_dissatisfied_email_format;
			}

		}

		else if (type.equals("executive")) {
			email_from = branch_email1;
			email_to = "";
			email_contact_id = "0";
			// email_contact_name = "";

			if (!preownedexe_email1.equals("")) {
				email_to += preownedexe_email1 + "";
			}
			if (!crmemp_email1.equals("")) {
				email_to += "," + crmemp_email1 + "";
			}
			if (!manager_email1.equals("")) {
				email_to += "," + manager_email1 + "";
			}

			if (contactable.equals("1")) {
				sub = precrmfollowupdays_contactable_email_exe_sub;
				emailmsg = precrmfollowupdays_contactable_email_exe_format;

			}
			else if (contactable.equals("2")) {
				sub = precrmfollowupdays_noncontactable_email_exe_sub;
				emailmsg = precrmfollowupdays_noncontactable_email_exe_format;
			}

			if (satisfied.equals("1")) {
				sub = precrmfollowupdays_satisfied_email_exe_sub;
				emailmsg = precrmfollowupdays_satisfied_email_exe_format;

			}
			else if (satisfied.equals("2")) {
				sub = precrmfollowupdays_dissatisfied_email_exe_sub;
				emailmsg = precrmfollowupdays_dissatisfied_email_exe_format;

			}

		}
		//
		// SOP("satisfied-----email--" + satisfied);
		// SOP("contactable----email---" + contactable);
		// SOP("type-----email--" + type);
		// SOP("email_to---email----------" + email_to);
		// SOP("email_cc-----email-------------" + email_cc);

		sub = "replace('" + sub + "','[PREOWNEDID]', COALESCE(preowned_id, ''))";
		sub = "replace(" + sub + ",'[PREOWNEDCRMDAY]',concat(precrmfollowupdays_daycount, ' ', precrmfollowupdays_desc ))";
		sub = "replace(" + sub + ",'[ENTRYDATE]', DATE_FORMAT(precrmfollowup_entry_time, '%d/%m/%Y'))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[CRMEXE]',preownedcrmexe.emp_name)";
		sub = "replace(" + sub + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		sub = "replace(" + sub + ",'[CRMEXEMOBILE1]',preownedcrmexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[PREOWNEDEXE]', COALESCE(preownedexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[PREOWNEDEXEJOBTITLE]',preownedexejobtitle.jobtitle_desc)";
		sub = "replace(" + sub + ",'[PREOWNEDEXEMOBILE1]',preownedexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[PREOWNEDEXEEMAIL1]',preownedexe.emp_email1)";
		sub = "replace(" + sub + ",'[VOC]',precrmfollowup_desc)";
		sub = "replace(" + sub + ",'[CONCERN]',COALESCE(precrmconcern_desc,''))";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		sub = "replace(" + sub + ",'[VARIANTNAME]',COALESCE(variant_name, ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";

		emailmsg = "replace('" + emailmsg + "','[PREOWNEDID]', COALESCE(preowned_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDCRMDAY]',concat( precrmfollowupdays_daycount, ' ', precrmfollowupdays_desc ))";
		emailmsg = "replace(" + emailmsg + ",'[ENTRYDATE]', DATE_FORMAT(precrmfollowup_entry_time, '%d/%m/%Y'))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXE]',preownedcrmexe.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEMOBILE1]',preownedcrmexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDEXE]', COALESCE(preownedexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDEXEJOBTITLE]',preownedexejobtitle.jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDEXEMOBILE1]',preownedexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDEXEEMAIL1]',preownedexe.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[VOC]',precrmfollowup_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',COALESCE(precrmconcern_desc,''))";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[VARIANTNAME]',COALESCE(variant_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";

		try {
			StrSql = "SELECT "
					+ "	branch_id,"
					+ " " + email_contact_id + ", "
					+ " '" + email_contact_name + "', "
					+ " '" + email_from + "', "
					+ " '" + email_to + "', "
					+ " '" + email_cc + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = precrmfollowup_crm_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preownedcrmexe ON preownedcrmexe.emp_id = precrmfollowup_crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = preownedcrmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preownedexe ON preownedexe.emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle preownedexejobtitle ON preownedexejobtitle.jobtitle_id = preownedexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_concern on precrmconcern_id = precrmfollowup_precrmconcern_id "
					+ " WHERE precrmfollowup_id = " + precrmfollowup_id
					+ " LIMIT 1";
			// SOP("StrSql--crm -------mail--" + StrSql);
			StrSql = "insert into " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql------email insert-----" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String type, String contactable, String satisfied, String contact_mobile) throws SQLException {
		String smsmsg = "", sms_contact_id = "", sms_contact_name = "", sms_to = "";

		if (type.equals("customer")) {
			sms_contact_id = contact_id;
			sms_contact_name = contact_name;
			sms_to = contact_mobile;
			if (contactable.equals("1")) {
				smsmsg = precrmfollowupdays_contactable_sms_format;
			}
			else if (contactable.equals("2")) {
				smsmsg = precrmfollowupdays_noncontactable_sms_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = precrmfollowupdays_satisfied_sms_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = precrmfollowupdays_dissatisfied_sms_format;
			}

		}

		if (type.equals("executive")) {
			sms_to = branch_crm_mobile;

			SOP("branch_crm_mobile=====" + branch_crm_mobile);
			sms_contact_id = "0";// crmexeid;
			// sms_contact_name = crmexename;
			if (contactable.equals("1")) {
				smsmsg = precrmfollowupdays_contactable_sms_exe_format;
			}
			else if (contactable.equals("2")) {
				smsmsg = precrmfollowupdays_noncontactable_sms_exe_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = precrmfollowupdays_satisfied_sms_exe_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = precrmfollowupdays_dissatisfied_sms_exe_format;
			}
		}
		// SOP("satisfied-----SMS--" + satisfied);
		// SOP("contactable----SMS---" + contactable);
		// SOP("type-----SMS--" + type);
		// SOP("sms_to---SMS----------" + sms_to);
		// SOP("sms_contact_id-----SMS-------------" + sms_contact_id);

		smsmsg = "replace('" + smsmsg + "','[PREOWNEDID]', COALESCE(preowned_id, ''))";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDCRMDAY]',concat( precrmfollowupdays_daycount, ' ', precrmfollowupdays_desc ))";
		smsmsg = "replace(" + smsmsg + ",'[ENTRYDATE]', DATE_FORMAT(precrmfollowup_entry_time, '%d/%m/%Y'))";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',contact_id)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXE]',preownedcrmexe.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXEMOBILE1]',preownedcrmexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDEXE]', COALESCE(preownedexe.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDEXEJOBTITLE]',preownedexejobtitle.jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDEXEMOBILE1]',preownedexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDEXEEMAIL1]',preownedexe.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[VOC]',precrmfollowup_desc)";
		smsmsg = "replace(" + smsmsg + ",'[CONCERN]',COALESCE(precrmconcern_desc,''))";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[VARIANTNAME]',COALESCE(variant_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";

		try {
			StrSql = "SELECT"
					+ " " + preowned_branch_id + ","
					+ " " + sms_contact_id + ","
					+ " '" + sms_contact_name + "',"
					+ " '" + sms_to + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '" + emp_id + "',"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "

					+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preownedcrmexe ON preownedcrmexe.emp_id = precrmfollowup_crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = preownedcrmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preownedexe ON preownedexe.emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle preownedexejobtitle ON preownedexejobtitle.jobtitle_id = preownedexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_concern on precrmconcern_id = precrmfollowup_precrmconcern_id "
					+ " WHERE precrmfollowup_id = " + precrmfollowup_id + ""
					+ " LIMIT 1";
			// SOP("StrSql--------SMS--" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_emp_id,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("Insert-------Sendsms-----------" + StrSqlBreaker(StrSql));
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

	protected void SendCRMTicketEmail(String type) throws SQLException {
		String emailmsg = "", sub = "", email_from = "", email_to = "";
		email_from = branch_email1;
		if (!manager_email1.equals("")) {
			email_to += manager_email1 + ",";
		}
		email_to += crmemp_email1;
		sub = config_ticket_preowned_crm_email_sub;
		emailmsg = config_ticket_preowned_crm_email_format;
		// SOP("sub--------" + sub);
		// SOP("emailmsg-----------" + emailmsg);
		// SOP("email_to----------" + email_to);
		sub = "replace('" + sub + "','[TICKETID]',COALESCE(ticket_id, 0))";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',COALESCE(ticket_subject, ''))";
		sub = "replace(" + sub + ",'[VOC]',COALESCE(ticket_desc,''))";
		sub = "replace(" + sub + ",'[CONCERN]',COALESCE(precrmconcern_desc,''))";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',COALESCE(ticketstatus_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTIME]', IF(ticket_report_time = '','',COALESCE (DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'),'')))";
		sub = "replace(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',IF(ticket_due_time = '','',COALESCE (DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'),'')))";
		sub = "replace(" + sub + ",'[TICKETOWNER]',COALESCE(owner.emp_name, ''))";

		sub = "replace(" + sub + ",'[CRMDAY]',COALESCE(CONCAT(precrmfollowupdays_daycount, ' ',precrmfollowupdays_desc),''))";
		sub = "replace(" + sub + ",'[CRMEXE]',COALESCE(preownedcrmexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[CRMEXEJOBTITLE]',COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		sub = "replace(" + sub + ",'[CRMEXEMOBILE1]',COALESCE(preownedcrmexe.emp_mobile1, ''))";
		sub = "replace(" + sub + ",'[CRMEXEEMAIL1]',COALESCE(preownedcrmexe.emp_email1, ''))";
		sub = "replace(" + sub + ",'[PREOWNEDID]', COALESCE(preowned_id, '0'))";
		sub = "replace(" + sub + ",'[PREOWNEDDATE]', IF(preowned_date = '','',COALESCE(DATE_FORMAT(preowned_date,'%d/%m/%Y'),'')))";
		sub = "replace(" + sub + ",'[PREOWNEDEXE]', COALESCE(preownedexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		sub = "replace(" + sub + ",'[VARIANT]',COALESCE(variant_name, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',COALESCE(ticket_dept_name, ''))";
		sub = "replace(" + sub + ",'[PRIORITY]',COALESCE(priorityticket_name, ''))";
		sub = "replace(" + sub + ",'[CONTACTID]',COALESCE(ticket_contact_id, 0))";
		sub = "replace(" + sub + ",'[CONTACTNAME]',COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',COALESCE(ticket_customer_id, 0))";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',COALESCE(ticket_id, 0))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',COALESCE(ticket_subject,''))";
		emailmsg = "replace(" + emailmsg + ",'[VOC]',COALESCE(ticket_desc, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',COALESCE(precrmconcern_desc,''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',COALESCE(ticketstatus_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',IF(ticket_report_time = '','',COALESCE (DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',IF(ticket_due_time = '','',COALESCE (DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETOWNER]',COALESCE(owner.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMDAY]',COALESCE(CONCAT(precrmfollowupdays_daycount, ' ',precrmfollowupdays_desc),''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXE]',COALESCE(preownedcrmexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEJOBTITLE]',COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEMOBILE1]',COALESCE(preownedcrmexe.emp_mobile1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEEMAIL1]',COALESCE(preownedcrmexe.emp_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDID]', COALESCE(preowned_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDDATE]', IF(preowned_date = '','',COALESCE(DATE_FORMAT(preowned_date,'%d/%m/%Y'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDEXE]', COALESCE(preownedexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";

		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[VARIANT]',COALESCE(variant_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',COALESCE(ticket_dept_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',COALESCE(priorityticket_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',COALESCE(ticket_contact_id, 0))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',COALESCE(ticket_customer_id, 0))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		try {
			StrSql = "SELECT"
					+ " " + preowned_branch_id + ","
					+ " 0 , "
					+ " '', "
					+ " '" + email_from + "', "
					+ " '" + email_to + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner on owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type on tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup on precrmfollowup_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_concern on precrmconcern_id = precrmfollowup_precrmconcern_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id =  preowned_branch_id "// + " preowned_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp preownedcrmexe on preownedcrmexe.emp_id = precrmfollowup_crm_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = preownedcrmexe.emp_jobtitle_id";
			StrSql += "  LEFT JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " LEFT JOIN axela_preowned_model on preownedmodel_id = preowned_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp preownedexe ON preownedexe.emp_id = preowned_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle salesexejobtitle ON salesexejobtitle.jobtitle_id = preownedexe.emp_jobtitle_id";

			// StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle managerjobtitle ON managerjobtitle.jobtitle_id = manager.emp_jobtitle_id"
			StrSql += " where ticket_id = " + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			// SOP("SendCRMTicketEmail---emaile-------------------" + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ "("
					+ " email_branch_id, "
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("SendCRMTicketEmail----------------" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
