package axela.axelaauto_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.service.Ticket_Add;
import cloudify.connect.Connect;

public class App_CRM_Update extends Connect {

	public String update = "";
	public String add = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String entry_by = "", entry_date = "";
	public String modified_date = "", modified_by = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "0";
	public String enquiry_no = "";
	public String enquiry_dmsno = "";
	public String crm_id = "";
	public String crm_mobile1 = "";
	public String crm_mobile2 = "";
	public String crm_email1 = "";
	public String crm_email2 = "";
	public String crm_lostcase1_id = "0";
	public String crm_lostcase2_id = "0";
	public String crm_lostcase3_id = "0";
	public String crm_desc = "";
	public String crm_crmfeedbacktype_id = "0";
	public String crm_cancelreason_id = "0";
	public String crm_entry_id = "0";
	public String crm_entry_date = "";
	public String crm_modified_id = "0";
	public String crm_modified_time = "";
	public String crmtype_id = "0";
	public String crm_crmconcern_id = "0";
	public String crmtype_name = "";
	public String return_perm = "0";
	public String crmdays_lostfollowup = "", crmdays = "";
	public String crmdays_so_inactive = "";
	public String crm_crmdays_id = "";
	public String crmexeid = "0";
	public String crmexename = "";
	public String enquiry_date, enquiry_close_date = "";
	public String soe_name = "";
	public String sob_name = "";
	public String lostcase1_name = "";
	public String lostcase2_name = "";
	public String lostcase3_name = "";
	public String exe_name = "", enquiryexe_name = "";
	public String exe_email1 = "";
	public String manager_email1 = "", crmemp_email1 = "", bfemp_email1 = "", pbfemp_email1, psfemp_email1 = "", enquiryexe_email1 = "", custtype_name = "";
	public String manager_name = "", crm_name = "";
	public String manager_mobile1 = "", crmemp_mobile1 = "";
	public String manager_jobtitle = "", crm_jobtitle = "";
	public String status_name = "";
	public String stage_name = "";
	public String so_id = "0";
	public String so_no = "";
	public String so_date = "";
	public String so_delivered_date = "";
	public String so_promise_date = "";
	public String so_payment_date = "";
	public String so_booking_amount = "", so_ew_amount = "";
	public String so_grandtotal = "";
	public String model_name = "";
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
	public String item_name = "";
	public String crm_emp_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "", StrScript = "";
	public String crm_ticket_emp_id = "0";
	public String crm_tickettype_id = "0";
	public String enquiry_branch_id = "";
	public String enquiry_emp_id = "";
	public String crmdays_crmtype_id = "";
	public String crmdays_desc = "";
	public String crm_satisfied = "";
	public String emp_uuid = "";
	public String curryear = "", year1 = "", branch_ticket_email = "";
	public String branch_name = "", option_name = "", sooptiondesc = "";
	public String fincomp_name = "", engine_no = "", chassis_no = "", reg_no = "", occ_name = "";

	// crm mail and msg

	public String crmdays_satisfied_email_enable = "";
	public String crmdays_satisfied_email_sub = "";
	public String crmdays_satisfied_email_format = "";
	public String crmdays_satisfied_email_exe_sub = "";
	public String crmdays_satisfied_email_exe_format = "";
	public String crmdays_satisfied_sms_enable = "";
	public String crmdays_satisfied_sms_format = "";
	public String crmdays_satisfied_sms_exe_format = "";

	public String crmdays_dissatisfied_email_enable = "";
	public String crmdays_dissatisfied_email_sub = "";
	public String crmdays_dissatisfied_email_format = "";
	public String crmdays_dissatisfied_email_exe_sub = "";
	public String crmdays_dissatisfied_email_exe_format = "";
	public String crmdays_dissatisfied_sms_enable = "";
	public String crmdays_dissatisfied_sms_format = "";
	public String crmdays_dissatisfied_sms_exe_format = "";
	// contact able
	public String crmdays_contactable_email_enable = "";
	public String crmdays_contactable_email_sub = "";
	public String crmdays_contactable_email_format = "";
	public String crmdays_contactable_email_exe_sub = "";
	public String crmdays_contactable_email_exe_format = "";
	public String crmdays_contactable_sms_enable = "";
	public String crmdays_contactable_sms_format = "";
	public String crmdays_contactable_sms_exe_format = "";
	// non contact able
	public String crmdays_noncontactable_email_enable = "";
	public String crmdays_noncontactable_email_sub = "";
	public String crmdays_noncontactable_email_format = "";
	public String crmdays_noncontactable_email_exe_sub = "";
	public String crmdays_noncontactable_email_exe_format = "";
	public String crmdays_noncontactable_sms_enable = "";
	public String crmdays_noncontactable_sms_format = "";
	public String crmdays_noncontactable_sms_exe_format = "";
	public String emailmsg = "", sub = "";
	public String send_contact_email = "";

	// public String config_admin_email = "";
	public String branch_email1 = "";
	public String branch_crm_email = "";
	public String branch_crm_mobile = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";

	// ticket executive

	public String config_ticket_crm_email_sub = "";
	public String config_ticket_crm_email_format = "";
	public String config_ticket_pbf_email_sub = "";
	public String config_ticket_pbf_email_format = "";
	public String config_ticket_psf_email_sub = "";
	public String config_ticket_psf_email_format = "";

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
			SOP("inside crm update....");
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_enquiry_add", request, response);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				crm_id = CNumeric(PadQuotes(request.getParameter("crm_id")));
				curryear = SplitYear(ToLongDate(kknow()));
				year1 = (Integer.parseInt(curryear) + 10) + "";
				crm_emp_id = ExecuteQuery("SELECT crm_emp_id FROM " + compdb(comp_id) + "axela_sales_crm "
						+ " WHERE crm_id=" + crm_id + "");
				if (!emp_id.equals("1") && !emp_id.equals(crm_emp_id))
				{
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				PopulateFields(response);
				PopulateConfigDetails(comp_id);
				if ("Update Follow-up".equals(updateB)) {
					GetValues(request);
					if (add.equals("add")) {
						return_perm = ReturnPerm(comp_id, "emp_enquiry_add",
								request);
					} else {
						return_perm = ReturnPerm(comp_id, "emp_enquiry_edit",
								request);
					}
					if (return_perm.equals("1")) {
						CheckForm(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateCRMFollowup();
							CRMCustomFieldUpdate(comp_id, crm_id, "yes", request);
							// /// Add Ticket if dissatisfied
							if (crm_entry_id.equals("0"))
							{
								conntx = connectDB();
								conntx.setAutoCommit(false);
								stmttx = conntx.createStatement();

								// SOP("insi-------de");
								// SOP("contact_email1--------" + contact_email1);
								// SOP("contact_email2--------" + contact_email2);
								// SOP("crm_satisfied--------" + crm_satisfied);
								// SOP("comp_email_enable--------" + comp_email_enable);
								// SOP("config_email_enable--------" + config_email_enable);
								// SOP("branch_email1--------" + branch_email1);
								// SOP("crmdays_satisfied_email_enable--------" + crmdays_satisfied_email_enable);
								// SOP("contact_email1--------" + contact_email1);
								// SOP("crmdays_satisfied_email_format--------" + crmdays_satisfied_email_format);
								// SOP("crmdays_satisfied_email_sub--------" + crmdays_satisfied_email_sub);
								// SOP("branch_crm_email--------" + branch_crm_email);
								// SOP("branch_email1--------" + branch_email1);
								// SOP("crmdays_satisfied_email_exe_format--------" + crmdays_satisfied_email_exe_format);
								// SOP("crmdays_satisfied_email_exe_sub--------" + crmdays_satisfied_email_exe_sub);
								//
								// SOP("crmdays_contactable_sms_format--------" + crmdays_contactable_sms_format);
								// SOP("crm_mobile1--------" + crm_mobile1);
								// SOP("crm_mobile2--------" + crm_mobile2);
								// SOP("crmdays_satisfied_email_exe_sub--------" + crmdays_satisfied_email_exe_sub);

								if (!contact_email2.equals("") && !contact_email1.equals("")) {
									send_contact_email = contact_email1 + "," + contact_email2;
								} else if (!contact_email1.equals("")) {
									send_contact_email = contact_email1;
								} else if (!contact_email2.equals("")) {
									send_contact_email = contact_email2;
								}
								// SOP("crm_crmfeedbacktype_id--------" + crm_crmfeedbacktype_id);
								// SOP("comp_email_enable--------" + comp_email_enable);
								// SOP("config_email_enable--------" + config_email_enable);
								// SOP("branch_email1--------" + branch_email1);
								// SOP("crmdays_contactable_email_enable--------" + crmdays_contactable_email_enable);
								// SOP("contact_email1--------" + contact_email1);
								// SOP("crmdays_contactable_email_format--------" + crmdays_contactable_email_format);
								// SOP("crmdays_contactable_email_sub--------" + crmdays_contactable_email_sub);

								if (crm_crmfeedbacktype_id.equals("1")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& crmdays_contactable_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !crmdays_contactable_email_format.equals("")
												&& !crmdays_contactable_email_sub.equals("")) {
											SendEmail("customer", crm_crmfeedbacktype_id, "0", comp_id);
										}

										if (!branch_email1.equals("")
												&& !crmdays_contactable_email_exe_format.equals("")
												&& !crmdays_contactable_email_exe_sub.equals("")) {
											SendEmail("executive", crm_crmfeedbacktype_id, "0", comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& crmdays_contactable_sms_enable.equals("1")) {
										if (!crmdays_contactable_sms_format.equals("")) {
											if (!crm_mobile1.equals("")) {
												SendSMS("customer", crm_crmfeedbacktype_id, "0", crm_mobile1);
											}
											if (!crm_mobile2.equals("")) {
												SendSMS("customer", crm_crmfeedbacktype_id, "0", crm_mobile2);
											}
										}
										if (!crmdays_contactable_sms_exe_format.equals("")) {
											if (!branch_crm_mobile.equals("")) {
												SendSMS("executive", crm_crmfeedbacktype_id, "0", branch_crm_mobile);
											}
										}
									}
								}

								if (crm_crmfeedbacktype_id.equals("2")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& crmdays_noncontactable_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !crmdays_noncontactable_email_format.equals("")
												&& !crmdays_noncontactable_email_sub.equals("")) {
											SendEmail("customer", crm_crmfeedbacktype_id, "0", comp_id);
										}

										if (!branch_email1.equals("")
												&& !crmdays_noncontactable_email_exe_format.equals("")
												&& !crmdays_noncontactable_email_exe_sub.equals("")) {
											SendEmail("executive", crm_crmfeedbacktype_id, "0", comp_id);
										}
									}
									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& crmdays_noncontactable_sms_enable.equals("1")) {
										if (!crmdays_noncontactable_sms_format.equals("")) {
											if (!crm_mobile1.equals("")) {
												SendSMS("customer", crm_crmfeedbacktype_id, "0", crm_mobile1);
											}
											if (!crm_mobile2.equals("")) {
												SendSMS("customer", crm_crmfeedbacktype_id, "0", crm_mobile2);
											}
										}
										if (!crmdays_noncontactable_sms_exe_format.equals("")) {
											if (!branch_crm_mobile.equals("")) {
												SendSMS("executive", crm_crmfeedbacktype_id, "0", branch_crm_mobile);
											}
										}
									}
								}

								if (crm_satisfied.equals("1")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& crmdays_satisfied_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !crmdays_satisfied_email_format.equals("")
												&& !crmdays_satisfied_email_sub.equals("")) {
											SendEmail("customer", "0", crm_satisfied, comp_id);
										}

										if (!branch_email1.equals("")
												&& !crmdays_satisfied_email_exe_format.equals("")
												&& !crmdays_satisfied_email_exe_sub.equals("")) {
											SendEmail("executive", "0", crm_satisfied, comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& crmdays_satisfied_sms_enable.equals("1")) {
										if (!crmdays_satisfied_sms_format.equals("")) {
											if (!crm_mobile1.equals("")) {
												SendSMS("customer", "0", crm_satisfied, crm_mobile1);
											}
											if (!crm_mobile2.equals("")) {
												SendSMS("customer", "0", crm_satisfied, crm_mobile2);
											}
										}
										if (!crmdays_satisfied_sms_exe_format.equals("")) {
											if (!branch_crm_mobile.equals("")) {
												SendSMS("executive", "0", crm_satisfied, branch_crm_mobile);
											}
										}
									}

								}

								if (crm_satisfied.equals("2")) {
									if (comp_email_enable.equals("1")
											&& config_email_enable.equals("1")
											&& !branch_email1.equals("")
											&& crmdays_dissatisfied_email_enable.equals("1")) {
										if (!contact_email1.equals("")
												&& !crmdays_dissatisfied_email_format.equals("")
												&& !crmdays_dissatisfied_email_sub.equals("")) {
											SendEmail("customer", "0", crm_satisfied, comp_id);
										}

										if (!branch_crm_email.equals("")
												&& !branch_email1.equals("")
												&& !crmdays_dissatisfied_email_exe_format.equals("")
												&& !crmdays_dissatisfied_email_exe_sub.equals("")) {
											SendEmail("executive", "0", crm_satisfied, comp_id);
										}
									}

									if (comp_sms_enable.equals("1")
											&& config_sms_enable.equals("1")
											&& crmdays_dissatisfied_sms_enable.equals("1")) {
										if (!crmdays_dissatisfied_sms_format.equals("")) {
											if (!crm_mobile1.equals("")) {
												SendSMS("customer", "0", crm_satisfied, crm_mobile1);
											}
											if (!crm_mobile2.equals("")) {
												SendSMS("customer", "0", crm_satisfied, crm_mobile2);
											}
										}
										if (!crmdays_dissatisfied_sms_exe_format.equals("")) {
											if (!branch_crm_mobile.equals("")) {
												SendSMS("executive", "0", crm_satisfied, branch_crm_mobile);
											}
										}
									}
								}
								conntx.commit();
								// SOP("Transaction commit...");
								if (crm_satisfied.equals("2")) {
									if (!crm_ticket_emp_id.equals("0")) {
										Ticket_Add tkt = new Ticket_Add();
										tkt.comp_id = comp_id;
										tkt.emp_id = emp_id;
										tkt.ticket_branch_id = enquiry_branch_id;
										tkt.ticket_customer_id = customer_id;
										tkt.ticket_contact_id = contact_id;
										tkt.veh_id = "0";
										tkt.jc_id = "0";
										if (!enquiryexe_email1.equals("")) {
											tkt.ticket_cc += "" + enquiryexe_email1 + "";
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
										// SOP("tkt.ticket_cc======"+tkt.ticket_cc);
										tkt.ticket_emp_id = crm_ticket_emp_id;
										tkt.ticket_ticketsource_id = "1";
										tkt.ticket_report_time = ToLongDate(kknow());
										tkt.ticket_ticketstatus_id = "1";
										tkt.ticket_priorityticket_id = "1";
										tkt.ticket_ticket_dept_id = "1";
										tkt.ticket_subject = "Dis-satisfied CRM Call";
										tkt.ticket_desc = crm_desc;
										tkt.customer_branch_id = enquiry_branch_id;
										tkt.ticket_enquiry_id = enquiry_id;
										tkt.ticket_so_id = so_id;
										tkt.ticket_crm_id = crm_id;
										if (crmtype_id.equals("1")) {
											tkt.ticket_tickettype_id = "1";
										}
										if (crmtype_id.equals("2")) {
											tkt.ticket_tickettype_id = "2";
										}
										if (crmtype_id.equals("3")) {
											tkt.ticket_tickettype_id = "3";
										}
										tkt.ticket_entry_id = emp_id;
										tkt.ticket_entry_date = ToLongDate(kknow());
										tkt.PopulateConfigDetails(comp_id);
										tkt.AddFields(comp_id);
										ticket_id = tkt.ticket_id;
										if (!ticket_id.equals("0")) {
											if (crmtype_id.equals("1") &&
													!config_ticket_crm_email_sub.equals("")
													&& !config_ticket_crm_email_format.equals("")) {
												SendCRMTicketEmail(crmtype_id);
											}
											if (crmtype_id.equals("2") &&
													!config_ticket_pbf_email_sub.equals("")
													&& !config_ticket_pbf_email_format.equals("")) {
												SendCRMTicketEmail(crmtype_id);
											}
											if (crmtype_id.equals("3") &&
													!config_ticket_psf_email_sub.equals("")
													&& !config_ticket_psf_email_format.equals("")) {
												SendCRMTicketEmail(crmtype_id);
											}

										}
									}
								}
							}
							response.sendRedirect("callurlapp-enquiry-dash-crm-followup.jsp?enquiry_id=" + enquiry_id + "&crmtab=active&msg=CRM Follow-up updated successfully!");
						}
					} else {
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void PopulateFields(HttpServletResponse response)
			throws SQLException, IOException {
		String script = "";

		script = "replace(crmdays_script, '[SALUTATION]'," + "'" + GetSalutation(ToLongDate(kknow())) + "')";
		script = "replace(" + script + ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
		script = "replace(" + script + ",'[EXENAME]',session.emp_name) as crmdays_script";

		StrSql = "SELECT " + script + " ,enquiry_branch_id, COALESCE(branch_ticket_email, '') AS branch_ticket_email,"
				+ " enquiry_close_date, branch_brand_id, "
				+ " enquiry_buyertype_id, enquiry_existingvehicle,"
				// " enquiry_purchasemonth, enquiry_priorityenquiry_id, enquiry_occ_id,"
				+ " enquiry_lostcase1_id, enquiry_lostcase2_id, enquiry_lostcase3_id,"
				+ " crm_desc, crmdays_crmtype_id, crmdays_daycount, crmdays_desc, crmdays_lostfollowup, crmdays_so_inactive,"
				+ " crm_cancelreason_id, crm_crmfeedbacktype_id, crm_satisfied,"
				+ " COALESCE(entryemp.emp_id,0) AS crmentryid, "
				+ " COALESCE(entryemp.emp_name,'') AS crmentryname, "
				+ " COALESCE(crm_entry_time,'') AS crm_entry_time,"
				+ " COALESCE(modifiedemp.emp_id,0) AS crmmodifiedid,"
				+ " COALESCE(modifiedemp.emp_name,'') AS modifiedname, "
				+ " COALESCE(crm_modified_time,'') AS crm_modified_time, "
				+ " COALESCE(enquiryexe.emp_name, '') as enquiryexe_name,"
				+ " COALESCE(enquiryexe.emp_email1, '') as enquiryexe_email1,"
				+ " COALESCE(manager.emp_name,'') as manager_name,"
				+ " COALESCE(manager.emp_email1, '') as manager_email1,"
				+ " COALESCE(crm.emp_name,'') AS crm_name, COALESCE(crm.emp_mobile1,'') AS crmemp_mobile1,"
				+ " COALESCE(crm.emp_email1,'') AS crmemp_email1,"
				+ " COALESCE(pbf.emp_email1,'') AS pbfemp_email1,"
				+ " COALESCE(psf.emp_email1,'') AS psfemp_email1,"
				+ " COALESCE(crmexe.emp_id,'') AS crmexeid,"
				+ " COALESCE(crmexe.emp_name,'') AS crmexename,"
				+ " crm_lostcase1_id, crm_lostcase2_id, crm_lostcase3_id,"
				+ " COALESCE(crmjobtitle.jobtitle_desc,'') AS crm_jobtitle,"
				+ " enquiry_id, enquiry_no, enquiry_dmsno, crm_crmdays_id,"
				+ " stage_name, status_name, "
				+ " COALESCE(soe_name,'') soe_name,"
				+ " COALESCE(sob_name,'') sob_name,"
				+ " customer_id, customer_name,"
				+ " contact_id, COALESCE(concat(title_desc,' ', contact_fname,' ', contact_lname),'') AS contact_name, contact_address, title_gender,"
				+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_phone1, contact_phone2,"
				+ " COALESCE(session.emp_name, '') AS exe_name, "
				+ " COALESCE(session.emp_email1, '') AS exe_email1, enquiry_date, item_name,"
				+ " COALESCE (so_id, 0) AS so_id, "
				+ " COALESCE(custtype_name,'') AS custtype_name, "
				+ " COALESCE (so_no, '') AS so_no,"
				+ " COALESCE (so_date, '') AS so_date,"
				+ " COALESCE (so_promise_date, '') AS so_promise_date,"
				+ " COALESCE (so_delivered_date, '') AS so_delivered_date,"
				+ " COALESCE (so_payment_date, '') AS so_payment_date,"
				+ " COALESCE (so_booking_amount, '') AS so_booking_amount,"
				+ " COALESCE(so_ew_amount, '') AS so_ew_amount,"
				+ " COALESCE (so_grandtotal, '') AS so_grandtotal,"
				+ " COALESCE (model_name, '') AS model_name, crmtype_id,crm_crmconcern_id, crmtype_name, branch_name, "
				+ " COALESCE (enopt.option_name, '') AS option_name,"
				+ " COALESCE (soopt.option_name, '') AS sooptionname,"
				+ " COALESCE (fincomp_name, '') AS fincomp_name,"
				+ " COALESCE (vehstock_engine_no, '') AS engine_no,"
				+ " COALESCE (vehstock_chassis_no, '') AS chassis_no,"
				+ " COALESCE (so_reg_no, '') AS reg_no,"
				+ " COALESCE (occ_name, '') AS occ_name, crm_ticket_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
				+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
				+ " INNER JOIN axela_brand on brand_id = crmdays_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp session on session.emp_id = " + emp_id + " "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp enquiryexe on enquiryexe.emp_id = enquiry_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager on manager.emp_id = team_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm on crm.emp_id = team_crm_emp_id " // team_crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp pbf on pbf.emp_id = team_pbf_emp_id " // team_pbf_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp psf on psf.emp_id = team_psf_emp_id " // team_psf_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crmexe on crmexe.emp_id = crm_emp_id " // crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp on entryemp.emp_id = crm_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp on modifiedemp.emp_id = crm_modified_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmjobtitle on crmjobtitle.jobtitle_id = crm.emp_jobtitle_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option enopt ON enopt.option_id = enquiry_option_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option soopt ON soopt.option_id = so_option_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id"
				+ " WHERE crm_id = " + crm_id + ""
				+ BranchAccess.replace("branch_id", "enquiry_branch_id")
				+ ExeAccess.replace("emp_id", "crm_emp_id")
				+ " GROUP BY enquiry_id";
		SOP("StrSql=========================" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);
		if (crs1.isBeforeFirst()) {
			while (crs1.next()) {
				StrScript = crs1.getString("crmdays_script");
				enquiry_branch_id = crs1.getString("enquiry_branch_id");
				branch_name = crs1.getString("branch_name");
				option_name = crs1.getString("option_name");
				// sooptionname = crs1.getString("sooptionname");
				occ_name = crs1.getString("occ_name");
				fincomp_name = crs1.getString("fincomp_name");
				engine_no = crs1.getString("engine_no");
				chassis_no = crs1.getString("chassis_no");
				reg_no = crs1.getString("reg_no");
				// branch_brand_id = crs1.getString("branch_brand_id");
				branch_ticket_email = crs1.getString("branch_ticket_email");
				enquiry_close_date = strToShortDate(crs1
						.getString("enquiry_close_date"));
				crm_desc = crs1.getString("crm_desc");
				crmdays_crmtype_id = crs1.getString("crmdays_crmtype_id");
				crmdays_desc = crs1.getString("crmdays_daycount")
						+ crs1.getString("crmdays_desc");
				crmdays_lostfollowup = crs1.getString("crmdays_lostfollowup");
				crmdays_so_inactive = crs1.getString("crmdays_so_inactive");
				crm_crmfeedbacktype_id = crs1.getString("crm_crmfeedbacktype_id");
				crm_cancelreason_id = crs1.getString("crm_cancelreason_id");
				crm_satisfied = crs1.getString("crm_satisfied");
				crm_entry_id = crs1.getString("crmentryid");
				// SOP("crm_entry_id====" + crm_entry_id);

				if (!crm_entry_id.equals("0")) {
					entry_by = "<a href=../portal/executive-summary.jsp?emp_id=" + crs1.getInt("crmentryid") + ">" + crs1.getString("crmentryname") + "</a>";
					entry_date = strToLongDate(crs1.getString("crm_entry_time"));
				}
				crm_modified_id = crs1.getString("crmmodifiedid");
				if (!crm_modified_id.equals("0")) {
					modified_by = "<a href=../portal/executive-summary.jsp?emp_id=" + crs1.getInt("crmmodifiedid") + ">" + crs1.getString("modifiedname") + "</a>";
					modified_date = strToLongDate(crs1.getString("crm_modified_time"));
				}
				enquiryexe_email1 = crs1.getString("enquiryexe_email1");
				manager_name = crs1.getString("manager_name");
				manager_email1 = crs1.getString("manager_email1");
				crm_name = crs1.getString("crm_name");
				crmemp_mobile1 = crs1.getString("crmemp_mobile1");
				crmemp_email1 = crs1.getString("crmemp_email1");
				pbfemp_email1 = crs1.getString("pbfemp_email1");
				psfemp_email1 = crs1.getString("psfemp_email1");
				custtype_name = crs1.getString("custtype_name");
				if (crm_entry_id.equals("0")) {
					crm_lostcase1_id = crs1.getString("enquiry_lostcase1_id");
					crm_lostcase2_id = crs1.getString("enquiry_lostcase2_id");
					crm_lostcase3_id = crs1.getString("enquiry_lostcase3_id");
				} else {
					crm_lostcase1_id = crs1.getString("crm_lostcase1_id");
					crm_lostcase2_id = crs1.getString("crm_lostcase2_id");
					crm_lostcase3_id = crs1.getString("crm_lostcase3_id");
				}
				enquiry_id = crs1.getString("enquiry_id");
				enquiry_no = crs1.getString("enquiry_no");
				if (crs1.getString("crmdays_lostfollowup").equals("1")) {
					GetEnquiryLostCase(enquiry_id);
				}
				crm_jobtitle = crs1.getString("crm_jobtitle");
				enquiry_dmsno = crs1.getString("enquiry_dmsno");
				crm_crmdays_id = crs1.getString("crm_crmdays_id");
				crmexeid = crs1.getString("crmexeid");
				crmexename = crs1.getString("crmexename");
				stage_name = crs1.getString("stage_name");
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
				enquiry_date = strToShortDate(crs1.getString("enquiry_date"));
				item_name = crs1.getString("item_name");
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
				model_name = crs1.getString("model_name");
				crmtype_id = crs1.getString("crmtype_id");
				crm_crmconcern_id = crs1.getString("crm_crmconcern_id");
				crmtype_name = crs1.getString("crmtype_name");

				crm_mobile1 = crs1.getString("contact_mobile1");
				crm_mobile2 = crs1.getString("contact_mobile2");
				crm_email1 = crs1.getString("contact_email1");
				crm_email2 = crs1.getString("contact_email2");
				crm_desc = crs1.getString("crm_desc");
				enquiryexe_name = crs1.getString("enquiryexe_name");
				crm_ticket_emp_id = crs1.getString("crm_ticket_emp_id");
			}
		} else {
			msg += "Sales Consultant is not allocated to the Team";
			response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=" + msg));
		}
		crs1.close();
	}

	public void PopulateConfigDetails(String comp_id) {
		StrSql = "SELECT  branch_email1,"
				+ " config_email_enable, branch_crm_email, branch_crm_mobile, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_ticket_crm_email_sub, config_ticket_crm_email_format,"
				+ " config_ticket_pbf_email_sub, config_ticket_pbf_email_format,"
				+ " config_ticket_psf_email_sub, config_ticket_psf_email_format,"
				+ " crmdays_contactable_email_enable, crmdays_contactable_email_sub, crmdays_contactable_email_format,"
				+ " crmdays_contactable_email_exe_sub, crmdays_contactable_email_exe_format,"
				+ " crmdays_contactable_sms_enable, crmdays_contactable_sms_format,"
				+ " crmdays_contactable_sms_exe_format , crmdays_noncontactable_email_enable,"
				+ " crmdays_noncontactable_email_sub , crmdays_noncontactable_email_format,"
				+ " crmdays_noncontactable_email_exe_sub , crmdays_noncontactable_email_exe_format,"
				+ " crmdays_noncontactable_sms_enable , crmdays_noncontactable_sms_format,"
				+ " crmdays_noncontactable_sms_exe_format ,"
				+ " COALESCE(crmdays_satisfied_email_enable,'') AS crmdays_satisfied_email_enable,"
				+ " COALESCE(crmdays_satisfied_email_sub,'') AS crmdays_satisfied_email_sub,"
				+ " COALESCE(crmdays_satisfied_email_format,'') AS crmdays_satisfied_email_format,"
				+ " COALESCE(crmdays_satisfied_email_exe_sub,'') AS crmdays_satisfied_email_exe_sub,"
				+ " COALESCE(crmdays_satisfied_email_exe_format,'') AS crmdays_satisfied_email_exe_format,"
				+ " COALESCE(crmdays_satisfied_sms_enable,'') AS crmdays_satisfied_sms_enable,"
				+ " COALESCE(crmdays_satisfied_sms_format,'') AS crmdays_satisfied_sms_format,"
				+ " COALESCE(crmdays_satisfied_sms_exe_format,'') AS crmdays_satisfied_sms_exe_format,"
				+ " COALESCE(crmdays_dissatisfied_email_enable,'') AS crmdays_dissatisfied_email_enable, "
				+ " COALESCE(crmdays_dissatisfied_email_sub,'') AS crmdays_dissatisfied_email_sub, "
				+ " COALESCE(crmdays_dissatisfied_email_format,'') AS crmdays_dissatisfied_email_format, "
				+ " COALESCE(crmdays_dissatisfied_email_exe_sub,'') AS crmdays_dissatisfied_email_exe_sub, "
				+ " COALESCE(crmdays_dissatisfied_email_exe_format,'') AS crmdays_dissatisfied_email_exe_format, "
				+ " COALESCE(crmdays_dissatisfied_sms_enable,'') AS crmdays_dissatisfied_sms_enable, "
				+ " COALESCE(crmdays_dissatisfied_sms_format,'') AS crmdays_dissatisfied_sms_format, "
				+ " COALESCE(crmdays_dissatisfied_sms_exe_format,'') AS crmdays_dissatisfied_sms_exe_format "
				+ " FROM " + compdb(comp_id) + "axela_sales_crmdays "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_crmdays_id = crmdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id,"
				+ compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1 "
				+ " and crm_id = " + crm_id;
		// SOP("StrSql---PopulateConfigDetails-------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				// ticket
				config_ticket_crm_email_sub = crs.getString("config_ticket_crm_email_sub");
				config_ticket_crm_email_format = crs.getString("config_ticket_crm_email_format");
				config_ticket_pbf_email_sub = crs.getString("config_ticket_pbf_email_sub");
				config_ticket_pbf_email_format = crs.getString("config_ticket_pbf_email_format");
				config_ticket_psf_email_sub = crs.getString("config_ticket_psf_email_sub");
				config_ticket_psf_email_format = crs.getString("config_ticket_psf_email_format");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				branch_crm_email = crs.getString("branch_crm_email");
				branch_crm_mobile = crs.getString("branch_crm_mobile");
				crmdays_contactable_email_enable = crs.getString("crmdays_contactable_email_enable");
				crmdays_contactable_email_sub = crs.getString("crmdays_contactable_email_sub");
				crmdays_contactable_email_format = crs.getString("crmdays_contactable_email_format");
				crmdays_contactable_email_exe_sub = crs.getString("crmdays_contactable_email_exe_sub");
				crmdays_contactable_email_exe_format = crs.getString("crmdays_contactable_email_exe_format");
				crmdays_contactable_sms_enable = crs.getString("crmdays_contactable_sms_enable");
				crmdays_contactable_sms_format = crs.getString("crmdays_contactable_sms_format");
				crmdays_contactable_sms_exe_format = crs.getString("crmdays_contactable_sms_exe_format");
				crmdays_noncontactable_email_enable = crs.getString("crmdays_noncontactable_email_enable");
				crmdays_noncontactable_email_sub = crs.getString("crmdays_noncontactable_email_sub");
				crmdays_noncontactable_email_format = crs.getString("crmdays_noncontactable_email_format");
				crmdays_noncontactable_email_exe_sub = crs.getString("crmdays_noncontactable_email_exe_sub");
				crmdays_noncontactable_email_exe_format = crs.getString("crmdays_noncontactable_email_exe_format");
				crmdays_noncontactable_sms_enable = crs.getString("crmdays_noncontactable_sms_enable");
				crmdays_noncontactable_sms_format = crs.getString("crmdays_noncontactable_sms_format");
				crmdays_noncontactable_sms_exe_format = crs.getString("crmdays_noncontactable_sms_exe_format");
				crmdays_satisfied_email_enable = crs.getString("crmdays_satisfied_email_enable");
				crmdays_satisfied_email_sub = crs.getString("crmdays_satisfied_email_sub");
				crmdays_satisfied_email_format = crs.getString("crmdays_satisfied_email_format");
				crmdays_satisfied_email_exe_sub = crs.getString("crmdays_satisfied_email_exe_sub");
				crmdays_satisfied_email_exe_format = crs.getString("crmdays_satisfied_email_exe_format");
				crmdays_satisfied_sms_enable = crs.getString("crmdays_satisfied_sms_enable");
				crmdays_satisfied_sms_format = crs.getString("crmdays_satisfied_sms_format");
				crmdays_satisfied_sms_exe_format = crs.getString("crmdays_satisfied_sms_exe_format");
				crmdays_dissatisfied_email_enable = crs.getString("crmdays_dissatisfied_email_enable");
				crmdays_dissatisfied_email_sub = crs.getString("crmdays_dissatisfied_email_sub");
				crmdays_dissatisfied_email_format = crs.getString("crmdays_dissatisfied_email_format");
				crmdays_dissatisfied_email_exe_sub = crs.getString("crmdays_dissatisfied_email_exe_sub");
				crmdays_dissatisfied_email_exe_format = crs.getString("crmdays_dissatisfied_email_exe_format");
				crmdays_dissatisfied_sms_enable = crs.getString("crmdays_dissatisfied_sms_enable");
				crmdays_dissatisfied_sms_format = crs.getString("crmdays_dissatisfied_sms_format");
				crmdays_dissatisfied_sms_exe_format = crs.getString("crmdays_dissatisfied_sms_exe_format");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request) {
		add = PadQuotes(request.getParameter("txt_add"));
		crm_lostcase1_id = CNumeric(PadQuotes(request.getParameter("dr_crm_lostcase1_id")));
		crm_lostcase2_id = CNumeric(PadQuotes(request.getParameter("dr_crm_lostcase2_id")));
		crm_lostcase3_id = CNumeric(PadQuotes(request.getParameter("dr_crm_lostcase3_id")));
		crm_cancelreason_id = CNumeric(PadQuotes(request.getParameter("dr_crm_cancelreason_id")));
		crm_desc = PadQuotes(request.getParameter("txt_crmfollowup_desc"));
		crm_crmfeedbacktype_id = PadQuotes(request.getParameter("dr_feedbacktype"));
		StrScript = unescapehtml(PadQuotes(request.getParameter("txt_StrScript")));
		crm_mobile1 = PadQuotes(request.getParameter("txt_crm_mobile1"));
		crm_mobile2 = PadQuotes(request.getParameter("txt_crm_mobile2"));
		crm_email1 = PadQuotes(request.getParameter("txt_crm_email1"));
		crm_email2 = PadQuotes(request.getParameter("txt_crm_email2"));
		crm_satisfied = CNumeric(PadQuotes(request.getParameter("dr_crm_satisfied")));
		crm_crmconcern_id = PadQuotes(request.getParameter("dr_crm_concern_id"));
		crm_ticket_emp_id = PadQuotes(request.getParameter("dr_ticketowner_id"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		// crm_entry_id = PadQuotes(request.getParameter("crm_entry_id"));
		crm_modified_id = PadQuotes(request.getParameter("crm_modified_id"));
	}

	public void UpdateCRMFollowup() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm"
						+ " SET" + " crm_mobile1 = '" + crm_mobile1 + "',"
						+ " crm_mobile2 = '" + crm_mobile2 + "',"
						+ " crm_email1 = '" + crm_email1 + "',"
						+ " crm_email2 = '" + crm_email2 + "',"
						+ " crm_lostcase1_id = " + crm_lostcase1_id + ","
						+ " crm_lostcase2_id = " + crm_lostcase2_id + ","
						+ " crm_lostcase3_id = " + crm_lostcase3_id + ","
						+ " crm_cancelreason_id = " + crm_cancelreason_id + ","
						+ " crm_desc = '" + crm_desc + "',"
						+ " crm_crmfeedbacktype_id = " + crm_crmfeedbacktype_id + ","
						+ " crm_satisfied = " + crm_satisfied + ","
						+ " crm_crmconcern_id = " + crm_crmconcern_id + ","
						+ " crm_ticket_emp_id = " + crm_ticket_emp_id + ",";
				if (crm_entry_id.equals("0")) {
					StrSql = StrSql + " crm_entry_id = " + emp_id + ","
							+ " crm_entry_time = '" + ToLongDate(kknow()) + "'";
				} else {
					StrSql = StrSql + " crm_modified_id = " + emp_id + ","
							+ " crm_modified_time = '" + ToLongDate(kknow()) + "'";
				}
				StrSql = StrSql + " WHERE crm_id = " + crm_id + ""
						+ " and crm_enquiry_id = " + enquiry_id;
				// SOP("STrSql=====" + StrSql);
				updateQuery(StrSql);

				if (!contact_mobile1.equals(crm_mobile1)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 1'," + " '" + contact_mobile1
							+ "'," + " '" + crm_mobile1 + "')";
					updateQuery(StrSql);
					// SOP("strsqlll======22222===="+StrSql);
				}
				if (!contact_mobile2.equals(crm_mobile2)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 2'," + " '" + contact_mobile2
							+ "'," + " '" + crm_mobile2 + "')";
					updateQuery(StrSql);
				}
				if (!contact_email1.equals(crm_email1)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 1'," + " '" + contact_email1
							+ "'," + " '" + crm_email1 + "')";
					updateQuery(StrSql);
				}
				if (!contact_email2.equals(crm_email2)) {
					StrSql = "Insert into " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 2'," + " '" + contact_email2
							+ "'," + " '" + crm_email2 + "')";
					updateQuery(StrSql);
				}

				// //update email for contact/////
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET" + " contact_mobile1 = '" + crm_mobile1 + "',"
						+ " contact_mobile2 = '" + crm_mobile2 + "',"
						+ " contact_email1 = '" + crm_email1 + "',"
						+ " contact_email2 = '" + crm_email2 + "'"
						+ " WHERE contact_id = " + contact_id + "";
				// SOP("StrSql = " + StrSqlBreaker(StrSql));
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto-App=== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		msg += CRMCustomFieldValidate(comp_id, crm_id, "2", request);

		if (crm_crmfeedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Feedback Type!";
		}

		if (crm_mobile1.equals("91-"))
		{
			crm_mobile1 = "";
		}
		if (crm_mobile2.equals("91-"))
		{
			crm_mobile2 = "";
		}

		if (!crm_mobile1.equals("")) {
			if (!IsValidMobileNo11(crm_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			}
		}
		if (!crm_mobile2.equals("")) {
			if (!IsValidMobileNo11(crm_mobile2)) {
				msg = msg + "<br>Enter Valid Contact Mobile 2!";
			}
		}
		if (!crm_email1.equals("")) {
			if (!IsValidEmail(crm_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1 !";
			}
		}
		if (!crm_email2.equals("")) {
			if (!IsValidEmail(crm_email2)) {
				msg = msg + "<br>Enter Valid Contact Email 2!";
			}
		}
		if (crmdays_lostfollowup.equals("1") && crm_crmfeedbacktype_id.equals("1")) {

			if (crm_lostcase1_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 1!";
			}
			if (crm_lostcase2_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 2!";
			}
			if (crm_lostcase3_id.equals("0")) {
				msg = msg + "<br>Select Lost Case 3!";
			}
		} else {
			crm_lostcase1_id = "0";
			crm_lostcase2_id = "0";
			crm_lostcase3_id = "0";
		}
		if (crm_desc.equals("")) {
			msg = msg + "<br>Enter Feedback!";
		} else if (crm_satisfied.equals("2")) {
			if (crm_desc.length() < 5) {
				msg = msg + "<br>Feedback should have atleast five characters!";
			}
			if (crm_desc.length() > 7999) {
				crm_desc = crm_desc.substring(0, 7999);
			}
		}
		if (crm_satisfied.equals("2")) {
			if (crm_crmconcern_id.equals("0")) {
				msg = msg + "<br>Select CRM Concern!";
			}
		}
		if (crm_satisfied.equals("2")) {
			if (crm_ticket_emp_id.equals("0")) {
				msg = msg + "<br>Select Ticket Owner!";
			}
		}
		if (crm_crmfeedbacktype_id.equals("2")) {
			crm_satisfied = "0";
			crm_crmconcern_id = "0";
			crm_ticket_emp_id = "0";
		}
		if (crm_satisfied.equals("1")) {
			crm_crmconcern_id = "0";
			crm_ticket_emp_id = "0";
		}

	}

	public String PopulateCRMFeedbackType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT crmfeedbacktype_id, crmfeedbacktype_name"
					+ " FROM axela_sales_crm_feedbacktype" + " WHERE 1=1"
					+ " ORDER BY crmfeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=")
						.append(crs.getString("crmfeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmfeedbacktype_id"),
						crm_crmfeedbacktype_id));
				Str.append(">").append(crs.getString("crmfeedbacktype_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLostCase1(String comp_id, String lostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// SOP("lostcase1_id====" + lostcase1_id);
			StrSql = "SELECT lostcase1_id, lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY lostcase1_name";
			// SOP("sTRsQL==1===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2(String comp_id, String lostcase2_id, String lostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// SOP("lostcase2_id====" + lostcase2_id);
			StrSql = "SELECT lostcase2_id, lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_lostcase1_id = " + CNumeric(lostcase1_id)
					+ " ORDER BY lostcase2_name";
			// SOP("strsql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_crm_lostcase2_id\" id=\"dr_crm_lostcase2_id\" class=\"form-control\" onchange=\"populateLostCase3()\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3(String comp_id, String lostcase3_id, String lostcase2_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_lostcase2_id = " + CNumeric(lostcase2_id)
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_crm_lostcase3_id\" id=\"dr_crm_lostcase3_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		// Str.append("<option value=0").append(StrSelectdrop("0",
		// crmfollowup_satisfied)).append(">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", crm_satisfied))
				.append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", crm_satisfied))
				.append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	public String PopulateCancelReason(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// SOP("cancelreason==pop===" + crm_cancelreason_id);
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id")).append("");
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), crm_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateConcern(String comp_id, String crm_crmconcern_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT crmconcern_id,"
					+ "	crmconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_concern "
					+ " WHERE crmconcern_crmtype_id=" + crmtype_id
					+ " GROUP BY crmconcern_id"
					+ " ORDER BY crmconcern_desc";
			// SOP("StrSql-1-" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmconcern_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmconcern_id"), crm_crmconcern_id));
				Str.append(">").append(crs.getString("crmconcern_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
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
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_emp "
					+ " WHERE 1=1 and emp_ticket_owner='1' AND emp_active='1' AND (emp_branch_id = "
					+ enquiry_branch_id
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM "
					+ compdb(comp_id)
					+ "axela_emp_branch empbr "
					+ " WHERE "
					+ compdb(comp_id)
					+ "axela_emp.emp_id=empbr.emp_id AND empbr.emp_branch_id=" + enquiry_branch_id + "))"
					// + ExeAccess
					// + " and emp_id = " + enquiry_emp_id
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), crm_ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public void GetEnquiryLostCase(String enquiry_id) {
		try {
			StrSql = "SELECT "
					+ " lostcase1_id, COALESCE (concat('(',lostcase1_name,')'), '') lostcase1_name,"
					+ " lostcase2_id, COALESCE (concat('(',lostcase2_name,')'), '') lostcase2_name,"
					+ " lostcase3_id, COALESCE (concat('(',lostcase3_name,')'), '') lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = enquiry_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = enquiry_lostcase2_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id = enquiry_lostcase3_id"
					+ " WHERE 1=1"
					+ " AND enquiry_id = " + enquiry_id
					+ " GROUP BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lostcase1_name = crs.getString("lostcase1_name");
					lostcase2_name = crs.getString("lostcase2_name");
					lostcase3_name = crs.getString("lostcase3_name");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

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
			if (!enquiryexe_email1.equals("")) {
				email_cc += "" + enquiryexe_email1 + "";
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
				sub = crmdays_contactable_email_sub;
				emailmsg = crmdays_contactable_email_format;

			}
			else if (contactable.equals("2")) {
				sub = crmdays_noncontactable_email_sub;
				emailmsg = crmdays_noncontactable_email_format;
			}

			if (satisfied.equals("1")) {
				sub = crmdays_satisfied_email_sub;
				emailmsg = crmdays_satisfied_email_format;
			}
			else if (satisfied.equals("2")) {
				if (!branch_crm_email.equals("")) {
					email_cc += "," + branch_crm_email + "";
				}
				sub = crmdays_dissatisfied_email_sub;
				emailmsg = crmdays_dissatisfied_email_format;
			}

		}

		else if (type.equals("executive")) {
			email_from = branch_email1;
			email_to = "";
			email_contact_id = "0";
			// email_contact_name = "";

			if (!enquiryexe_email1.equals("")) {
				email_to += enquiryexe_email1 + "";
			}
			if (!crmemp_email1.equals("")) {
				email_to += "," + crmemp_email1 + "";
			}
			if (!manager_email1.equals("")) {
				email_to += "," + manager_email1 + "";
			}

			if (contactable.equals("1")) {
				sub = crmdays_contactable_email_exe_sub;
				emailmsg = crmdays_contactable_email_exe_format;

			}
			else if (contactable.equals("2")) {
				sub = crmdays_noncontactable_email_exe_sub;
				emailmsg = crmdays_noncontactable_email_exe_format;
			}

			if (satisfied.equals("1")) {
				sub = crmdays_satisfied_email_exe_sub;
				emailmsg = crmdays_satisfied_email_exe_format;

			}
			else if (satisfied.equals("2")) {
				if (!branch_crm_email.equals("")) {
					email_to += "," + branch_crm_email;
				}
				sub = crmdays_dissatisfied_email_exe_sub;
				emailmsg = crmdays_dissatisfied_email_exe_format;

			}

		}
		//
		SOP("satisfied-------" + satisfied);
		SOP("contactable-------" + contactable);
		SOP("type-------" + type);
		SOP("email_to===" + email_to);
		SOP("email_cc===" + email_cc);

		sub = "replace('" + sub + "','[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		sub = "replace(" + sub + ",'[CRMDAY]',concat(crmdays_daycount, ' ', crmdays_desc ))";
		sub = "replace(" + sub + ",'[ENTRYDATE]', DATE_FORMAT(crm_entry_time, '%d/%m/%Y'))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[CRMEXE]',crmexe.emp_name)";
		sub = "replace(" + sub + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		sub = "replace(" + sub + ",'[CRMEXEMOBILE1]',crmexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[SALESEXEJOBTITLE]',selesexejobtitle.jobtitle_desc)";
		sub = "replace(" + sub + ",'[SALESEXEMOBILE1]',selesexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[SALESEXEEMAIL1]',selesexe.emp_email1)";
		sub = "replace(" + sub + ",'[VOC]',crm_desc)";
		sub = "replace(" + sub + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "replace(" + sub + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";

		emailmsg = "replace('" + emailmsg + "','[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMDAY]',concat( crmdays_daycount, ' ', crmdays_desc ))";
		emailmsg = "replace(" + emailmsg + ",'[ENTRYDATE]', DATE_FORMAT(crm_entry_time, '%d/%m/%Y'))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXE]',crmexe.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEMOBILE1]',crmexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXEJOBTITLE]',selesexejobtitle.jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXEMOBILE1]',selesexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXEEMAIL1]',selesexe.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[VOC]',crm_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
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
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_crmdays_id = crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = crm_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle selesexejobtitle ON selesexejobtitle.jobtitle_id = selesexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern on crmconcern_id = crm_crmconcern_id "
					+ " WHERE crm_id = " + crm_id
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
				smsmsg = crmdays_contactable_sms_format;
			}
			else if (contactable.equals("2")) {
				smsmsg = crmdays_noncontactable_sms_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = crmdays_satisfied_sms_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = crmdays_dissatisfied_sms_format;
			}

		}

		if (type.equals("executive")) {
			sms_to = branch_crm_mobile;
			sms_contact_id = "0";// crmexeid;
			// sms_contact_name = crmexename;
			if (contactable.equals("1")) {
				smsmsg = crmdays_contactable_sms_exe_format;
			}
			else if (contactable.equals("2")) {
				smsmsg = crmdays_noncontactable_sms_exe_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = crmdays_satisfied_sms_exe_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = crmdays_dissatisfied_sms_exe_format;
			}
		}
		smsmsg = "replace('" + smsmsg + "','[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		smsmsg = "replace(" + smsmsg + ",'[CRMDAY]',concat( crmdays_daycount, ' ', crmdays_desc ))";
		smsmsg = "replace(" + smsmsg + ",'[ENTRYDATE]', DATE_FORMAT(crm_entry_time, '%d/%m/%Y'))";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',contact_id)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXE]',crmexe.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXEMOBILE1]',crmexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXEJOBTITLE]',selesexejobtitle.jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXEMOBILE1]',selesexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXEEMAIL1]',selesexe.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[VOC]',crm_desc)";
		smsmsg = "replace(" + smsmsg + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_branch_id + ","
					+ " " + sms_contact_id + ","
					+ " '" + sms_contact_name + "',"
					+ " '" + sms_to + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '" + emp_id + "',"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_crmdays_id = crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle selesexejobtitle ON selesexejobtitle.jobtitle_id = selesexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern on crmconcern_id = crm_crmconcern_id "
					+ " WHERE crm_id = " + crm_id + ""
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
			// SOP("insert-------Sendsms---customer--------" + StrSqlBreaker(StrSql));
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
		if (!branch_crm_email.equals("")) {
			email_to = branch_crm_email + ",";
		}
		if (!manager_email1.equals("")) {
			email_to += manager_email1 + ",";
		}
		if (type.equals("1")) {
			email_to += crmemp_email1;
			sub = config_ticket_crm_email_sub;
			emailmsg = config_ticket_crm_email_format;
		}
		else if (type.equals("2")) {
			email_to += pbfemp_email1;
			sub = config_ticket_pbf_email_sub;
			emailmsg = config_ticket_pbf_email_format;
		}
		else if (type.equals("3")) {
			email_to += psfemp_email1;
			sub = config_ticket_psf_email_sub;
			emailmsg = config_ticket_psf_email_format;
		}
		// SOP("branch_crm_email==" + branch_crm_email);
		// SOP("manager_email1==" + manager_email1);
		// SOP("email_to===" + email_to);

		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[VOC]',ticket_desc)";
		sub = "replace(" + sub + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[TICKETOWNER]',owner.emp_name)";

		sub = "replace(" + sub + ",'[CRMDAY]',COALESCE(CONCAT(crmdays_daycount, ' ',crmdays_desc),''))";
		sub = "replace(" + sub + ",'[CRMEXE]',crmexe.emp_name)";
		sub = "replace(" + sub + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		sub = "replace(" + sub + ",'[CRMEXEMOBILE1]',crmexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[CRMEXEEMAIL1]',crmexe.emp_email1)";

		if (type.equals("1")) {
			sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";

		}
		else {
			sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			sub = "replace(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
			sub = "replace(" + sub + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
			sub = "replace(" + sub + ",'[COLOUR]',COALESCE(option_name, ''))";
			sub = "replace(" + sub + ",'[SONO]',COALESCE(so_no, ''))";
		}

		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(salesexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "replace(" + sub + ",'[VARIANT]',COALESCE(item_name, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[VOC]',ticket_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETOWNER]',owner.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[CRMDAY]',COALESCE(CONCAT(crmdays_daycount, ' ',crmdays_desc),''))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXE]',crmexe.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEJOBTITLE]',crmexejobtitle.jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEMOBILE1]',crmexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEEMAIL1]',crmexe.emp_email1)";
		if (type.equals("1")) {
			emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";

		}
		else {
			emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
			emailmsg = "replace(" + emailmsg + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
			emailmsg = "replace(" + emailmsg + ",'[COLOUR]',COALESCE(option_name, ''))";
			emailmsg = "replace(" + emailmsg + ",'[SONO]',COALESCE(so_no, ''))";
		}

		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(salesexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";

		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[VARIANT]',COALESCE(item_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		try {
			StrSql = "SELECT"
					+ " branch_id , "
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
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm on crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern on crmconcern_id = crm_crmconcern_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crmexe on crmexe.emp_id = crm_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id";
			if (type.equals("1")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = enquiry_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle salesexejobtitle ON salesexejobtitle.jobtitle_id = salesexe.emp_jobtitle_id";
			}
			else {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = so_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = so_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle salesexejobtitle ON salesexejobtitle.jobtitle_id = salesexe.emp_jobtitle_id";
			}

			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = enquiry_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle managerjobtitle ON managerjobtitle.jobtitle_id = manager.emp_jobtitle_id"
					+ " where ticket_id=" + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			// SOP("SendCRMTicketEmail---emaile---" + StrSql);
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
			// SOP("SendCRMTicketEmail------" + StrSql);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cloudify.connect.Connect#CRMCustomFieldView(java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String CRMCustomFieldView(String comp_id, String crm_id, String colspan, HttpServletRequest request) {
		SOP("=====CRMCustomFieldView========");
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id," + " crmcf_numeric, crmcf_length_min, crmcf_length_max,"
					+ " crmcf_option, crmcf_unique, crmcf_mandatory, crmcf_instruction, crmcf_voc,"
					+ " crmcf_fieldref, COALESCE(crmcftrans_value,'') AS crmcftransvalue, COALESCE(crmcftrans_value, '') AS crmcftrans_value," + " COALESCE(crmcftrans_voc, '') AS crmcftrans_voc,"
					+ " crm_enquiry_id, crm_so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_trans ON crmcftrans_crmcf_id = crmcf_id"
					+ " AND crmcftrans_crm_id = " + crm_id + ""
					+ " WHERE crmcf_active = 1 "
					+ " AND crm_id = " + crm_id + ""
					+ " GROUP BY crmcf_id"
					+ " ORDER BY crmcf_rank";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						}
					} else {
						if (crs.getString("crmcftransvalue") == null && !crs.getString("crmcf_fieldref").equals(""))
							fieldvalue = ReturnFieldRef(comp_id, crs.getString("crmcf_fieldref"), crs.getString("crm_enquiry_id"), crs.getString("crm_so_id"));
						else
							fieldvalue = crs.getString("crmcftrans_value");
						if (crs.getString("crmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToShortDate(fieldvalue);

							}
						} else if (crs.getString("crmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								SOP("fieldvalue---1111----" + fieldvalue);
								fieldvalue = strToLongDate(fieldvalue);
								SOP("fieldvalue----2222---" + fieldvalue);
							}
						}
					}

					Str.append("<div class=\"form-group\" style=\"position:relative; top: 20px;\">")
							.append("<label class=\"control-label col-md-6\">")
							.append(crs.getString("crmcf_title"));
					if (crs.getString("crmcf_mandatory").equals("1")) {
						Str.append("<span> *").append("</span>");

					}
					Str.append("</label>")

							.append("<div class=\"col-md-6\">");
					// start of text field
					if (crs.getString("crmcf_cftype_id").equals("1")) {
						Str.append("<input name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append(" id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append("type=\"text\" class=\"form-control\" size=50 maxlength=").append(crs.getString("crmcf_length_max"));
						Str.append(" value=\"").append(fieldvalue).append("\"");
						if (crs.getString("crmcf_numeric").equals("1")) {
							Str.append(" onKeyUp=\"toInteger('crmcf_id-").append(crs.getString("crmcf_id")).append("','custom')\"");
						}
						Str.append("/>");
					}
					// end text fields
					// start text area
					else if (crs.getString("crmcf_cftype_id").equals("2")) {
						Str.append("<textarea name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append(" id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append("', 'span_crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',").append(crs.getString("crmcf_length_max"));
						Str.append(")\">").append(fieldvalue).append("</textarea>");
						Str.append(" <span id=\"span_crmcf_id-").append(crs.getString("crmcf_id")).append("\"> (").append(crs.getString("crmcf_length_max")).append(" Characters)</span>");

					}
					// ends textarea
					// start checkbox
					else if (crs.getString("crmcf_cftype_id").equals("3")) {
						Str.append("<input id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append(" type=\"checkbox\"");
						Str.append(" name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append("style=position:absolute; top: 5px").append(" ").append(PopulateCheck(fieldvalue))
								.append("/><br>");
					}
					// end checkbox
					// start dropdown
					else if (crs.getString("crmcf_cftype_id").equals("4")) {
						Str.append("<select name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append("id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append("class=\"form-control\">\n");
						String[] option = crs.getString("crmcf_option").split("\\r\\n");
						Str.append("<option value=\"\">Select</option>\n");
						for (int i = 1; i <= option.length; i++) {
							Str.append("<option" + " value=\"").append(option[i - 1]).append("\" ");
							Str.append(StrSelectdrop(option[i - 1], fieldvalue));
							Str.append(">").append(option[i - 1]).append("</option>\n");
						}
						Str.append("</select>");
					} // / End Drop Down
						// start date fields
					else if (crs.getString("crmcf_cftype_id").equals("5")) {
						Str.append("<input name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append("id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append("type=\"text\" onclick=\"datePicker('crmcf_id-").append(crs.getString("crmcf_id")).append("')\" class=\"form-control\"");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append("readonly/>\n");

					}
					// end date fields
					// start datetime
					else if (crs.getString("crmcf_cftype_id").equals("6")) {
						String date = "";
						String time = "";
						SOP("fieldvalue==============================" + fieldvalue);
						if (!fieldvalue.equals("")) {
							date = fieldvalue.substring(0, 10);
							time = fieldvalue.substring(11, 16);
						}
						Str.append("<input name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append("id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append("type=\"text\" onclick=\"datePicker('crmcf_id-").append(crs.getString("crmcf_id")).append("')\" class=\"form-control\" ");
						Str.append("value=\"").append(date).append("\"");
						Str.append("readonly/><br>");
						Str.append("<span style=\"color:black\">").append("Time").append("<span>*</span>:").append("</span>");

						Str.append("<input name=\"crmcftime_id-").append(crs.getString("crmcf_id")).append("\"").append("id=\"crmcftime_id-").append(crs.getString("crmcf_id")).append("\"");
						Str.append("type=\"text\" onclick=\"timePicker('crmcftime_id-").append(crs.getString("crmcf_id")).append("')\" class=\"form-control\" ");
						Str.append("value=\"").append(time).append("\"");
						Str.append("readonly/>\n");

					} // / End DateTime Text Box

					// start time text box
					else if (crs.getString("crmcf_cftype_id").equals("7")) {
						Str.append("<input name=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"").append("id=\"crmcf_id-").append(crs.getString("crmcf_id")).append("\"");
						String text_field = "crmcf_id-" + crs.getString("crmcf_id");
						Str.append(" type=\"text\" class=\"form-control\"").append("value=\"").append(fieldvalue).append("\"").append("readonly=\"readonly\"");
						Str.append("onclick=\"timePicker('" + text_field + "')\"").append(" style=\"background-color:white\"");
						Str.append("/>\n");
					}
					// end of time ttext box
					if (!crs.getString("crmcf_instruction").equals("")) {
						Str.append("<br>").append(crs.getString("crmcf_instruction"));
					}
					// // Display Voc
					if (crs.getString("crmcf_voc").equals("1")) {
						Str.append("<br>VOC:<textarea name=crmcftrans_voc-").append(crs.getString("crmcf_id")).append(" id=crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append("', 'span_crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',8000)\">").append(crs.getString("crmcftrans_voc")).append("</textarea>");
						Str.append(" <span id=\"span_crmcftrans_voc-").append(crs.getString("crmcf_id")).append("\"> (8000 Characters)</span>");
					}

					Str.append("</div>");
					Str.append("</div>");
					Str.append("<input type=hidden name=customfieldsubmit value=yes>\n");
				}

				// Str.append("</table>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String CRMCustomFieldValidate(String comp_id, String crm_id, String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		String fieldvoc = "";
		crm_id = CNumeric(crm_id);
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id," + " crmcf_numeric, crmcf_length_min, crmcf_length_max, crmcf_voc," + " crmcf_option, crmcf_unique, crmcf_mandatory"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id" + " INNER JOIN " + compdb(comp_id)
					+ "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_trans ON crmcftrans_crmcf_id = crmcf_id" + " AND crmcftrans_crm_id = "
					+ crm_id + ""
					+ " WHERE crmcf_active = 1"
					+ " AND crm_id = " + crm_id + ""
					+ " GROUP BY crmcf_id "
					+ " ORDER BY crmcf_rank";
			// SOP("StrSql===cf==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					String datetime = "";
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
						datetime = PadQuotes(request.getParameter("crmcftime_id-" + (crs.getString("crmcf_id"))));
						fieldvoc = PadQuotes(request.getParameter("crmcftrans_voc-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						}
						// SOPError("fieldvalue = " + fieldvalue);
						if (fieldvalue.equals("") && crs.getString("crmcf_mandatory").equals("1")) {
							// / For Text Field AND Text Area
							if (crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) {
								Str.append("<br>").append(crs.getString("crmcf_title")).append(" is blank!");
							}
							// / For Drop Down AND Date Time
							if (crs.getString("crmcf_cftype_id").equals("4") || crs.getString("crmcf_cftype_id").equals("5") || crs.getString("crmcf_cftype_id").equals("6")
									|| crs.getString("crmcf_cftype_id").equals("7")) {
								Str.append("<br>Select ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / For Date
						if (!fieldvalue.equals("") && crs.getString("crmcf_cftype_id").equals("5")) {
							if (!isValidDateFormatShort(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / For Date Time
						if (!fieldvalue.equals("") && crs.getString("crmcf_cftype_id").equals("6")) {
							fieldvalue = fieldvalue + " " + datetime;
							if (!isValidDateFormatLong(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / Minimum Length
						// / For Text Field AND Text Area
						if (crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) {
							if (crs.getInt("crmcf_length_min") > 0 && fieldvalue.length() < crs.getInt("crmcf_length_min")) {
								Str.append("<br>Enter a minimum of ").append(crs.getString("crmcf_length_min")).append(" Characters for ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// For voc text field
						if ((crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) && crs.getString("crmcf_voc").equals("1")) {
							if (crs.getInt("crmcf_length_min") > 0 && fieldvoc.length() < crs.getInt("crmcf_length_min")) {
								Str.append("<br>Enter a minimum of voc ").append(crs.getString("crmcf_length_min")).append(" Characters for ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / Maximum Length
						if (fieldvalue.length() > crs.getInt("crmcf_length_max")) {
							fieldvalue = fieldvalue.substring(0, crs.getInt("crmcf_length_max") - 1);
						}
						// / Check Unique Field
						if (!fieldvalue.equals("") && crs.getString("crmcf_unique").equals("1")) {
							StrSql = "SELECT cftrans_id" + " FROM " + "" + compdb(comp_id) + "axela_sales_crm_trans" + " WHERE cftrans_crmcf_id = " + crs.getString("crmcf_id")
									+ " AND cftrans_value = '" + fieldvalue + "'";
							if (!ExecuteQuery(StrSql).equals("")) {
								Str.append("<br>").append(crs.getString("crmcf_title")).append(" is not unique!");
							}
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void CRMCustomFieldUpdate(String comp_id, String crm_id, String update, HttpServletRequest request) throws Exception {
		String fieldvalue = "";
		String fieldvoc = "";
		// String emp_formatdate = GetSession("formatdate_name", request);
		// String emp_formattime = GetSession("formattime_name", request);
		crm_id = CNumeric(crm_id);
		String StrSql = "";
		Connection conntx = null;
		Statement stmttx = null;
		CachedRowSet crs = null;
		if (!crm_id.equals("0")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm_trans" + " WHERE crmcftrans_crm_id = " + crm_id + "";
			updateQuery(StrSql);

			if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes") && update.equals("yes")) {

				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();

					StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id, crmcf_numeric, crmcf_length_min, crmcf_length_max,"
							+ " crmcf_option, crmcf_unique, crmcf_mandatory, crmcf_voc, crmcf_instruction, crm_enquiry_id "
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							+ " WHERE crmcf_active = 1"
							+ " AND crm_id = " + crm_id + ""
							+ " GROUP BY crmcf_id"
							+ " ORDER BY crmcf_rank";
					// SOPError("CustomFieldUpdate StrSql===" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					while (crs.next()) {
						// Get Field Value
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
						fieldvoc = PadQuotes(request.getParameter("crmcftrans_voc-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						} else if (crs.getString("crmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertShortDateToStr(fieldvalue);
							}
						} else if (crs.getString("crmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								String datetime = PadQuotes(request.getParameter("crmcftime_id-" + (crs.getString("crmcf_id"))));
								fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
								fieldvalue = fieldvalue + " " + datetime;
								fieldvalue = ConvertLongDateToStr(fieldvalue);
							}
						}

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crm_trans" + " (crmcftrans_crmcf_id," + " crmcftrans_crm_id," + " crmcftrans_value," + " crmcftrans_voc)" + " VALUES"
								+ " (" + crs.getString("crmcf_id") + "," + " " + crm_id + "," + " '" + fieldvalue + "', " + " '" + fieldvoc + "')";
						// SOP("StrSql======ssss = " + StrSql);
						stmttx.addBatch(StrSql);
					}
					crs.close();
					stmttx.executeBatch();
					conntx.commit();

				} catch (Exception ex) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("Axelaauto-App===" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
					// msg = "<br>Transaction Error!";
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
	}

}
