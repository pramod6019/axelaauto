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

import axela.service.Ticket_Add;
import cloudify.connect.Connect;

public class CRM_Update extends Connect {
	
	public String update = "";
	public String add = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "", ticketmsg = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
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
	public String manager_email1 = "", crmemp_email1 = "", pbfemp_email1 = "", psfemp_email1 = "", enquiryexe_email1 = "", custtype_name = "";
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
	public String item_name = "", so_item_name = "";
	public String crm_emp_id = "";
	public String BranchAccess = "", ExeAccess = "", StrScript = "";
	public String crm_ticket_emp_id = "0";
	public String crm_tickettype_id = "0";
	public String enquiry_branch_id = "";
	public String enquiry_emp_id = "";
	public String crmdays_crmtype_id = "";
	public String crmdays_desc = "";
	public String crm_satisfied = "";
	public String curryear = "", year1 = "", branch_ticket_email = "";
	public String branch_name = "", option_name = "", sooptionname = "";
	public String fincomp_name = "", engine_no = "", chassis_no = "", reg_no = "", occ_name = "";
	public String branch_brand_id = "0";
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
	
	public String ticket_emp_email1 = "";
	public String ticket_emp_email2 = "";
	public String ticket_emp_mobile1 = "";
	public String ticket_emp_mobile2 = "";
	
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String emp_name = "";
	public String emp_email_formail = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String preious_ticket_id = "0";
	public String ticket_id = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String enquirytype_id = "1";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_add", request, response);
			if (!comp_id.equals("0")) {
				// SOP(".....1");
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				crm_id = CNumeric(PadQuotes(request.getParameter("crm_id")));
				curryear = SplitYear(ToLongDate(kknow()));
				year1 = (Integer.parseInt(curryear) + 10) + "";
				// geeting enquiry type for item join
				StrSql = "SELECT enquiry_enquirytype_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id"
						+ " WHERE 1=1"
						+ " AND crm_id = " + crm_id;
				enquirytype_id = CNumeric(ExecuteQuery(StrSql));
				
				PopulateFields(response);
				// SOP(".....2.1");
				if ("Update Follow-up".equals(updateB)) {
					GetValues(request);
					PopulateConfigDetails(comp_id);
					if (add.equals("add")) {
						return_perm = ReturnPerm(comp_id, "emp_enquiry_add", request);
					} else {
						return_perm = ReturnPerm(comp_id, "emp_enquiry_edit", request);
					}
					if (return_perm.equals("1")) {
						CheckForm(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateCRMFollowup();
							CRMCustomFieldUpdate(comp_id, crm_id, "yes", request);
							
							if (crm_entry_id.equals("0")) {
								SendCommunications();
							}
							
							if (crm_satisfied.equals("2") && preious_ticket_id.equals("0")) {
								AddTicketDetails();
							}
							if (!ticket_id.equals("")) {
								ticketmsg = "Ticket ID: " + ticket_id + " is Raised!";
							}
							response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id
									+ "&crmmsg=CRM Follow-up updated successfully!&crmticketmsg=" + ticketmsg
									+ "#tabs-3"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void SendCommunications() throws Exception {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			
			if (!contact_email2.equals("") && !contact_email1.equals("")) {
				send_contact_email = contact_email1 + "," + contact_email2;
			} else if (!contact_email1.equals("")) {
				send_contact_email = contact_email1;
			} else if (!contact_email2.equals("")) {
				send_contact_email = contact_email2;
			}
			// contacatable and non contactable msg and emails
			if (crm_crmfeedbacktype_id.equals("1")) {
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && crmdays_contactable_email_enable.equals("1")) {
					if (!contact_email1.equals("") && !crmdays_contactable_email_format.equals("")
							&& !crmdays_contactable_email_sub.equals("")) {
						SendEmail("customer", crm_crmfeedbacktype_id, "0", comp_id);
					}
					
					if (!branch_email1.equals("") && !crmdays_contactable_email_exe_format.equals("")
							&& !crmdays_contactable_email_exe_sub.equals("")) {
						SendEmail("executive", crm_crmfeedbacktype_id, "0", comp_id);
					}
				}
				
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
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
			
			// contacatable and non contactable msg and emails
			if (crm_crmfeedbacktype_id.equals("2")) {
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && crmdays_noncontactable_email_enable.equals("1")) {
					if (!contact_email1.equals("") && !crmdays_noncontactable_email_format.equals("")
							&& !crmdays_noncontactable_email_sub.equals("")) {
						SendEmail("customer", crm_crmfeedbacktype_id, "0", comp_id);
					}
					
					if (!branch_email1.equals("") && !crmdays_noncontactable_email_exe_format.equals("")
							&& !crmdays_noncontactable_email_exe_sub.equals("")) {
						SendEmail("executive", crm_crmfeedbacktype_id, "0", comp_id);
					}
				}
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
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
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && crmdays_satisfied_email_enable.equals("1")) {
					if (!contact_email1.equals("") && !crmdays_satisfied_email_format.equals("")
							&& !crmdays_satisfied_email_sub.equals("")) {
						SendEmail("customer", "0", crm_satisfied, comp_id);
					}
					
					if (!branch_email1.equals("") && !crmdays_satisfied_email_exe_format.equals("")
							&& !crmdays_satisfied_email_exe_sub.equals("")) {
						SendEmail("executive", "0", crm_satisfied, comp_id);
					}
				}
				
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
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
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && crmdays_dissatisfied_email_enable.equals("1")) {
					if (!contact_email1.equals("") && !crmdays_dissatisfied_email_format.equals("")
							&& !crmdays_dissatisfied_email_sub.equals("")) {
						SendEmail("customer", "0", crm_satisfied, comp_id);
					}
					
					if (!branch_crm_email.equals("") && !branch_email1.equals("")
							&& !crmdays_dissatisfied_email_exe_format.equals("")
							&& !crmdays_dissatisfied_email_exe_sub.equals("")) {
						SendEmail("executive", "0", crm_satisfied, comp_id);
					}
				}
				
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
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
			// SOP("Transaction commit...");
			conntx.commit();
			
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("AxelaAuto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// msg = "<br>Transaction Error!";
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}
	
	public void AddTicketDetails() throws Exception {
		try {
			Ticket_Add tkt = new Ticket_Add();
			tkt.comp_id = comp_id;
			tkt.emp_id = emp_id;
			tkt.ticket_branch_id = enquiry_branch_id;
			tkt.ticket_customer_id = customer_id;
			tkt.ticket_contact_id = contact_id;
			tkt.veh_id = "0";
			tkt.jc_id = "0";
			
			if (!crm_email1.equals("")) {
				tkt.contact_email1 = crm_email1;
			}
			if (!crm_email2.equals("")) {
				tkt.contact_email2 = crm_email2;
			}
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
			if (!crm_mobile1.equals("")) {
				tkt.contact_mobile1 = crm_mobile1;
			}
			if (!crm_mobile2.equals("")) {
				tkt.contact_mobile2 = crm_mobile2;
			}
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
			tkt.branch_brand_id = branch_brand_id;
			tkt.ticket_entry_id = emp_id;
			tkt.ticket_entry_date = ToLongDate(kknow());
			tkt.PopulateConfigDetails(comp_id);
			tkt.AddFields(comp_id);
			ticket_id = tkt.ticket_id;
			// creating the connection for emails of ticket
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			
			if (!ticket_id.equals("0")) {
				if (crmtype_id.equals("1") && !config_ticket_crm_email_sub.equals("")
						&& !config_ticket_crm_email_format.equals("")) {
					SendCRMTicketEmail(crmtype_id);
				}
				if (crmtype_id.equals("2") && !config_ticket_pbf_email_sub.equals("")
						&& !config_ticket_pbf_email_format.equals("")) {
					SendCRMTicketEmail(crmtype_id);
				}
				if (crmtype_id.equals("3") && !config_ticket_psf_email_sub.equals("")
						&& !config_ticket_psf_email_format.equals("")) {
					SendCRMTicketEmail(crmtype_id);
				}
				
			}
			
			// commit for ticket transaction
			conntx.commit();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("AxelaAuto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// msg = "<br>Transaction Error!";
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}
	
	protected void PopulateFields(HttpServletResponse response) throws SQLException, IOException {
		String script = "";
		
		script = "REPLACE(crmdays_script, '[SALUTATION]'," + "'" + GetSalutation(ToLongDate(kknow())) + "')";
		script = "REPLACE(" + script + ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
		script = "REPLACE(" + script + ",'[EXENAME]',session.emp_name) AS crmdays_script";
		
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
				+ " COALESCE(enquiryexe.emp_name, '') AS enquiryexe_name,"
				+ " COALESCE(enquiryexe.emp_email1, '') AS enquiryexe_email1,"
				+ " COALESCE(manager.emp_name,'') AS manager_name,"
				+ " COALESCE(manager.emp_email1, '') AS manager_email1,"
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
				+ " COALESCE(session.emp_email1, '') AS exe_email1, enquiry_date,";
		if (enquirytype_id.equals("2")) {
			StrSql += " COALESCE(preownedvariant.variant_name,'') AS item_name,";
		} else {
			StrSql += " COALESCE(enqitem.item_name,'') AS item_name,";
		}
		
		StrSql += " COALESCE (so_id, 0) AS so_id, "
				+ " COALESCE(custtype_name,'') AS custtype_name, "
				+ " COALESCE (so_no, '') AS so_no,"
				+ " COALESCE(soitem.item_name,'') AS soitemname,"
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
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
				+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
				+ " INNER JOIN axela_brand ON brand_id = crmdays_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		if (enquirytype_id.equals("2")) {
			StrSql += " INNER JOIN axela_preowned_variant preownedvariant ON preownedvariant.variant_id = enquiry_preownedvariant_id";
		} else {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item enqitem ON enqitem.item_id = enquiry_item_id";
		}
		
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp session ON session.emp_id = " + emp_id + " "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = team_crm_emp_id " // team_crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp pbf ON pbf.emp_id = team_pbf_emp_id " // team_pbf_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp psf ON psf.emp_id = team_psf_emp_id " // team_psf_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id " // crm_emp_id
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = crm_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = crm_modified_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmjobtitle ON crmjobtitle.jobtitle_id = crm.emp_jobtitle_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item soitem ON soitem.item_id = so_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option enopt ON enopt.option_id = enquiry_option_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option soopt ON soopt.option_id = so_option_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id"
				+ " WHERE crm_id = " + crm_id + ""
				+ BranchAccess.replace("branch_id", "enquiry_branch_id")
				+ ExeAccess.replace("emp_id", "crm_emp_id")
				+ " GROUP BY enquiry_id";
		// SOPInfo("StrSql--CRM-------888888--------" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);
		if (crs1.isBeforeFirst()) {
			while (crs1.next()) {
				StrScript = crs1.getString("crmdays_script");
				enquiry_branch_id = crs1.getString("enquiry_branch_id");
				branch_name = crs1.getString("branch_name");
				option_name = crs1.getString("option_name");
				sooptionname = crs1.getString("sooptionname");
				occ_name = crs1.getString("occ_name");
				fincomp_name = crs1.getString("fincomp_name");
				engine_no = crs1.getString("engine_no");
				chassis_no = crs1.getString("chassis_no");
				reg_no = crs1.getString("reg_no");
				branch_brand_id = crs1.getString("branch_brand_id");
				branch_ticket_email = crs1.getString("branch_ticket_email");
				enquiry_close_date = strToShortDate(crs1.getString("enquiry_close_date"));
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
				so_item_name = crs1.getString("soitemname");
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
				+ " config_email_enable, branch_crm_email, branch_crm_mobile,"
				+ " COALESCE(emp_email1,'') AS ticket_emp_email1,"
				+ " COALESCE(emp_email2,'') AS ticket_emp_email2,"
				+ " COALESCE(emp_mobile1,'') AS ticket_emp_mobile1,"
				+ " COALESCE(emp_mobile2,'') AS ticket_emp_mobile2,"
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
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				// to get the ticket owner email id and phone number
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + crm_ticket_emp_id + ","
				+ compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1 "
				+ " and crm_id = " + crm_id;
		// SOP("StrSql---PopulateConfigDetails-------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				ticket_emp_email1 = crs.getString("ticket_emp_email1");
				ticket_emp_email2 = crs.getString("ticket_emp_email2");
				ticket_emp_mobile1 = crs.getString("ticket_emp_mobile1");
				ticket_emp_mobile2 = crs.getString("ticket_emp_mobile2");
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
	
	public void UpdateCRMFollowup() throws SQLException {
		if (msg.equals("")) {
			try {
				// creating the connection for crm details update
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " WHERE tagtrans_customer_id = " + customer_id
						+ " AND tagtrans_tag_id IN (1, 2)";
				
				stmttx.addBatch(StrSql);
				// updateQuery(StrSql);
				
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " (tagtrans_tag_id,tagtrans_customer_id)"
						+ " VALUES(" + crm_satisfied + "," + customer_id + ")";
				stmttx.addBatch(StrSql);
				// updateQuery(StrSql);
				
				String tagname = "";
				if (crm_satisfied.equals("1")) {
					tagname = "Satisfied";
				} else if (crm_satisfied.equals("2")) {
					tagname = "Dis-Satisfied";
				}
				
				StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_newvalue,"
						+ " history_oldvalue)"
						+ " values ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 'Tag Added',"
						+ " '" + tagname + "',"
						+ "'')";// history_oldvalue
				stmttx.addBatch(StrSql);
				// updateQuery(StrSql);
				
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
						+ " AND crm_enquiry_id = " + enquiry_id;
				// SOP("STrSql=====" + StrSql);
				stmttx.addBatch(StrSql);
				// updateQuery(StrSql);
				
				// SOP("StrSql======updatecrmfollowup====" + StrSql);
				if (!crm_mobile1.equals(crm_mobile1)) {
					StrSql = "INSERT INTO " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 1'," + " '" + contact_mobile1
							+ "'," + " '" + crm_mobile1 + "')";
					stmttx.addBatch(StrSql);
					// updateQuery(StrSql);
					// SOP("strsqlll======22222===="+StrSql);
				}
				if (!crm_mobile2.equals(crm_mobile2)) {
					StrSql = "INSERT INTO " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Mobile 2'," + " '" + contact_mobile2
							+ "'," + " '" + crm_mobile2 + "')";
					stmttx.addBatch(StrSql);
					// updateQuery(StrSql);
				}
				if (!crm_email1.equals(crm_email1)) {
					StrSql = "INSERT INTO " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 1'," + " '" + contact_email1
							+ "'," + " '" + crm_email1 + "')";
					stmttx.addBatch(StrSql);
					// updateQuery(StrSql);
				}
				if (!crm_email2.equals(crm_email2)) {
					StrSql = "INSERT INTO " + compdb(comp_id)
							+ "axela_sales_enquiry_history"
							+ " (history_enquiry_id," + " history_emp_id,"
							+ " history_datetime," + " history_actiontype,"
							+ " history_oldvalue," + " history_newvalue)"
							+ " values" + " (" + enquiry_id + ", " + emp_id
							+ ", '" + ToLongDate(kknow())
							+ "', 'Contact Email 2'," + " '" + contact_email2
							+ "'," + " '" + crm_email2 + "')";
					stmttx.addBatch(StrSql);
					// updateQuery(StrSql);
				}
				
				// //update email for contact/////
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET" + " contact_mobile1 = '" + crm_mobile1 + "',"
						+ " contact_mobile2 = '" + crm_mobile2 + "',"
						+ " contact_email1 = '" + crm_email1 + "',"
						+ " contact_email2 = '" + crm_email2 + "'"
						+ " WHERE contact_id = " + contact_id + "";
				// SOP("StrSql = " + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
				// updateQuery(StrSql);
				
				stmttx.executeBatch();
				conntx.commit();
				
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
				}
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
	
	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		msg += CRMCustomFieldValidate(comp_id, crm_id, "2", request);
		// SOP("msg==" + msg);
		if (crm_crmfeedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Feedback Type!";
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
		
		if (crm_crmfeedbacktype_id.equals("1")) {
			if (crm_satisfied.equals("0")) {
				msg = msg + "<br>Select Overall Experience!";
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
		
		StrSql = "SELECT ticket_id FROM " + compdb(comp_id) + "axela_service_ticket"
				+ " WHERE 1 = 1"
				+ " AND ticket_crm_id = " + crm_id
				+ " AND ticket_ticketstatus_id IN (1, 2, 5)";
		// SOP("Str====" + StrSql);
		preious_ticket_id = CNumeric(ExecuteQuery(StrSql));
		
		if (crm_satisfied.equals("1")) {
			crm_crmconcern_id = "0";
			crm_ticket_emp_id = "0";
			if (!preious_ticket_id.equals("0")) {
				msg += "<br>Ticket ID " + preious_ticket_id + " is Already Open!";
			}
		}
		
	}
	
	public String PopulateCRMFeedbackType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT crmfeedbacktype_id, crmfeedbacktype_name"
					+ " FROM axela_sales_crm_feedbacktype"
					+ " WHERE 1 = 1"
					+ " ORDER BY crmfeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				
				Str.append("<option value=").append(crs.getString("crmfeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmfeedbacktype_id"), crm_crmfeedbacktype_id));
				Str.append(">").append(crs.getString("crmfeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	
	public String PopulateLostCase1(String comp_id, String crm_lostcase1_id) {
		
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
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), crm_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateLostCase2(String comp_id, String crm_lostcase2_id, String lostcase1_id) {
		
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("lostcase2_id====" + lostcase2_id);
			StrSql = "SELECT lostcase2_id, lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_lostcase1_id = " + CNumeric(lostcase1_id)
					+ " ORDER BY lostcase2_name";
			// SOP("strsql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_crm_lostcase2_id\" id=\"dr_crm_lostcase2_id\" class=\"form-control\" onchange=\"populateLostCase3()\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), crm_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateLostCase3(String comp_id, String crm_lostcase3_id, String lostcase2_id) {
		
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_lostcase2_id = " + CNumeric(lostcase2_id)
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_crm_lostcase3_id\" id=\"dr_crm_lostcase3_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), crm_lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
					+ " WHERE 1=1"
					+ " AND emp_ticket_owner='1'"
					+ " AND emp_active='1'"
					+ " AND (emp_branch_id = " + enquiry_branch_id
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr " + " WHERE " + compdb(comp_id)
					+ "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id = " + enquiry_branch_id + ")"
					+ " OR emp_all_branches = 1)"
					// + ExeAccess
					// + " and emp_id = " + enquiry_emp_id
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql------PopulateExecutive-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), crm_ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			
		}
		return Str.toString();
	}
	
	public void GetEnquiryLostCase(String enquiry_id) {
		try {
			StrSql = "SELECT "
					+ " lostcase1_id, COALESCE (CONCAT('(',lostcase1_name,')'), '') lostcase1_name,"
					+ " lostcase2_id, COALESCE (CONCAT('(',lostcase2_name,')'), '') lostcase2_name,"
					+ " lostcase3_id, COALESCE (CONCAT('(',lostcase3_name,')'), '') lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = enquiry_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = enquiry_lostcase2_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id = enquiry_lostcase3_id"
					+ " WHERE 1 = 1"
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			if (!enquiryexe_email1.equals("") && !email_cc.contains(enquiryexe_email1)) {
				email_cc += "" + enquiryexe_email1 + "";
			}
			if (!crmemp_email1.equals("") && !email_cc.contains(crmemp_email1)) {
				email_cc += "," + crmemp_email1 + "";
			}
			if (!manager_email1.equals("") && !email_cc.contains(manager_email1)) {
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
			
			if (!enquiryexe_email1.equals("") && !email_to.contains(enquiryexe_email1)) {
				email_to += enquiryexe_email1 + "";
			}
			if (!crmemp_email1.equals("") && !email_to.contains(crmemp_email1)) {
				email_to += "," + crmemp_email1 + "";
			}
			if (!manager_email1.equals("") && !email_to.contains(manager_email1)) {
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
				if (!branch_crm_email.equals("") && !email_to.contains(branch_crm_email)) {
					email_to += "," + branch_crm_email;
				}
				sub = crmdays_dissatisfied_email_exe_sub;
				emailmsg = crmdays_dissatisfied_email_exe_format;
				
			}
			
		}
		
		email_to = RemoveDuplicateEmails(email_to);
		email_cc = RemoveDuplicateEmails(email_cc);
		//
		// SOP("satisfied-------" + satisfied);
		// SOP("contactable-------" + contactable);
		// SOP("type-------" + type);
		// SOP("email_to===" + email_to);
		// SOP("email_cc===" + email_cc);
		
		sub = "REPLACE('" + sub + "','[ENQUIRYID]', COALESCE(enquiry_id, '0'))";
		sub = "REPLACE(" + sub + ",'[CRMDAY]', COALESCE(CONCAT(crmdays_daycount, ' ', crmdays_desc ), ''))";
		sub = "REPLACE(" + sub + ",'[ENTRYDATE]', COALESCE(DATE_FORMAT(crm_entry_time, '%d/%m/%Y'), ''))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]', COALESCE(customer_id, '0'))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]', COALESCE(customer_name, ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTID]', COALESCE(contact_id, '0'))";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname), ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1, ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]', COALESCE(contact_email1, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXE]', COALESCE(crmexe.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXEJOBTITLE]', COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXEMOBILE1]', COALESCE(crmexe.emp_mobile1, ''))";
		sub = "REPLACE(" + sub + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[SALESEXEJOBTITLE]', COALESCE(selesexejobtitle.jobtitle_desc, ''))";
		sub = "REPLACE(" + sub + ",'[SALESEXEMOBILE1]', COALESCE(selesexe.emp_mobile1, ''))";
		sub = "REPLACE(" + sub + ",'[SALESEXEEMAIL1]', COALESCE(selesexe.emp_email1, ''))";
		sub = "REPLACE(" + sub + ",'[VOC]', COALESCE(crm_desc, ''))";
		sub = "REPLACE(" + sub + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		sub = "REPLACE(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "REPLACE(" + sub + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";
		
		emailmsg = "REPLACE('" + emailmsg + "','[ENQUIRYID]', COALESCE(enquiry_id, '0'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMDAY]', COALESCE(CONCAT( crmdays_daycount, ' ', crmdays_desc ), ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[ENTRYDATE]', COALESCE(DATE_FORMAT(crm_entry_time, '%d/%m/%Y'), ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]', COALESCE(customer_id, '0'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTID]', COALESCE(contact_id, '0'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname), ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXE]', COALESCE(crmexe.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEJOBTITLE]', COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEMOBILE1]', COALESCE(crmexe.emp_mobile1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SALESEXEJOBTITLE]', COALESCE(selesexejobtitle.jobtitle_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SALESEXEMOBILE1]', COALESCE(selesexe.emp_mobile1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SALESEXEEMAIL1]', COALESCE(selesexe.emp_email1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VOC]', COALESCE(crm_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";
		
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
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id ";
			if (crmtype_id.equals("1")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = enquiry_emp_id";
			}
			else if (crmtype_id.equals("2") || crmtype_id.equals("3")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = so_emp_id";
			}
			
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle selesexejobtitle ON selesexejobtitle.jobtitle_id = selesexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id "
					+ " WHERE crm_id = " + crm_id
					+ " LIMIT 1";
			// SOP("StrSql--crm -------mail--" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
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
			// SOP("StrSql------email-insert---SendEmail--" + type + "=====" + StrSql);
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
		smsmsg = "REPLACE('" + smsmsg + "','[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CRMDAY]', COALESCE(CONCAT( crmdays_daycount, ' ', crmdays_desc ), ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[ENTRYDATE]', COALESCE(DATE_FORMAT(crm_entry_time, '%d/%m/%Y'), ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERID]', COALESCE(customer_id, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTID]', COALESCE(contact_id, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname), ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CRMEXE]', COALESCE(crmexe.emp_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CRMEXEJOBTITLE]', COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CRMEXEMOBILE1]', COALESCE(crmexe.emp_mobile1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SALESEXE]', COALESCE(selesexe.emp_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SALESEXEJOBTITLE]', COALESCE(selesexejobtitle.jobtitle_desc, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SALESEXEMOBILE1]', COALESCE(selesexe.emp_mobile1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SALESEXEEMAIL1]', COALESCE(selesexe.emp_email1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[VOC]', COALESCE(crm_desc, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONCERN]',COALESCE(crmconcern_desc,''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[VARIANTNAME]',COALESCE(item_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCEMAIL]', COALESCE(branch_email1, ''))";
		
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
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id ";
			if (crmtype_id.equals("1")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = enquiry_emp_id";
			}
			else if (crmtype_id.equals("2") || crmtype_id.equals("3")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp selesexe ON selesexe.emp_id = so_emp_id";
			}
			
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle selesexejobtitle ON selesexejobtitle.jobtitle_id = selesexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id "
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
			email_to = branch_crm_email;
		}
		if (!ticket_emp_email1.equals("") && !email_to.contains(ticket_emp_email1)) {
			email_to += "," + ticket_emp_email1;
		}
		if (!ticket_emp_email2.equals("") && !email_to.contains(ticket_emp_email2)) {
			email_to += "," + ticket_emp_email2;
		}
		if (!manager_email1.equals("") && !email_to.contains(manager_email1)) {
			email_to += "," + manager_email1;
		}
		
		if (type.equals("1")) {
			if (!email_to.contains(crmemp_email1)) {
				email_to += "," + crmemp_email1;
			}
			sub = config_ticket_crm_email_sub;
			emailmsg = config_ticket_crm_email_format;
		}
		else if (type.equals("2")) {
			if (!email_to.contains(pbfemp_email1)) {
				email_to += "," + pbfemp_email1;
			}
			sub = config_ticket_pbf_email_sub;
			emailmsg = config_ticket_pbf_email_format;
		}
		else if (type.equals("3")) {
			if (!email_to.contains(psfemp_email1)) {
				email_to += "," + psfemp_email1;
			}
			sub = config_ticket_psf_email_sub;
			emailmsg = config_ticket_psf_email_format;
		}
		
		email_to = RemoveDuplicateEmails(email_to);
		
		// SOP("branch_crm_email==" + branch_crm_email);
		// SOP("manager_email1==" + manager_email1);
		// SOP("email_to===" + email_to);
		
		sub = "REPLACE('" + sub + "','[TICKETID]', COALESCE(ticket_id, '0'))";
		sub = "REPLACE(" + sub + ",'[TICKETSUBJECT]', COALESCE(ticket_subject, ''))";
		sub = "REPLACE(" + sub + ",'[VOC]', COALESCE(ticket_desc, ''))";
		sub = "REPLACE(" + sub + ",'[CONCERN]', COALESCE(crmconcern_desc, ''))";
		sub = "REPLACE(" + sub + ",'[TICKETSTATUS]', COALESCE(ticketstatus_name, ''))";
		sub = "REPLACE(" + sub + ",'[TICKETTIME]', COALESCE(DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'), ''))";
		sub = "REPLACE(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "REPLACE(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "REPLACE(" + sub + ",'[DUETIME]', COALESCE(DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'), ''))";
		sub = "REPLACE(" + sub + ",'[TICKETOWNER]', COALESCE(owner.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[CRMDAY]', COALESCE(CONCAT(crmdays_daycount, ' ',crmdays_desc), ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXE]', COALESCE(crmexe.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXEJOBTITLE]', COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXEMOBILE1]', COALESCE(crmexe.emp_mobile1, ''))";
		sub = "REPLACE(" + sub + ",'[CRMEXEEMAIL1]', COALESCE(crmexe.emp_email1, ''))";
		
		if (type.equals("1")) {
			sub = "REPLACE(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			sub = "REPLACE(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			
		}
		else {
			sub = "REPLACE(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
			sub = "REPLACE(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			sub = "REPLACE(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
			sub = "REPLACE(" + sub + ",'[DELIVERYDATE]', COALESCE( IF(so_delivered_date = '', '',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'), '')), ''))";
			sub = "REPLACE(" + sub + ",'[COLOUR]',COALESCE(option_name, ''))";
			sub = "REPLACE(" + sub + ",'[SONO]',COALESCE(so_no, ''))";
		}
		sub = "REPLACE(" + sub + ",'[SOE]', COALESCE(soe_name, ''))";
		sub = "REPLACE(" + sub + ",'[SALESEXE]', COALESCE(salesexe.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "REPLACE(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "REPLACE(" + sub + ",'[VARIANT]',COALESCE(item_name, ''))";
		sub = "REPLACE(" + sub + ",'[DEPARTMENT]', COALESCE(ticket_dept_name, ''))";
		sub = "REPLACE(" + sub + ",'[PRIORITY]', COALESCE(priorityticket_name, ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTID]', COALESCE(ticket_contact_id, '0'))";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname), ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]', COALESCE(ticket_customer_id, ''))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";
		
		emailmsg = "REPLACE('" + emailmsg + "','[TICKETID]', COALESCE(ticket_id, '0'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETSUBJECT]', COALESCE(ticket_subject, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VOC]', COALESCE(ticket_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONCERN]',COALESCE(crmconcern_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETSTATUS]', COALESCE(ticketstatus_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETTIME]', COALESCE(DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'), ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[DUETIME]', COALESCE(DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'), ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETOWNER]', COALESCE(owner.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMDAY]',COALESCE(CONCAT(crmdays_daycount, ' ',crmdays_desc),''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXE]', COALESCE(crmexe.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEJOBTITLE]', COALESCE(crmexejobtitle.jobtitle_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEMOBILE1]', COALESCE(crmexe.emp_mobile1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEEMAIL1]', COALESCE(crmexe.emp_email1, ''))";
		if (type.equals("1")) {
			emailmsg = "REPLACE(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, '0'))";
			emailmsg = "REPLACE(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			
		}
		else {
			emailmsg = "REPLACE(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, '0'))";
			emailmsg = "REPLACE(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[DELIVERYDATE]', COALESCE(IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')), ''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[COLOUR]',COALESCE(option_name, ''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[SONO]',COALESCE(so_no, ''))";
		}
		
		emailmsg = "REPLACE(" + emailmsg + ",'[SOE]', COALESCE(soe_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SALESEXE]', COALESCE(salesexe.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		
		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VARIANT]',COALESCE(item_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[DEPARTMENT]', COALESCE(ticket_dept_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[PRIORITY]', COALESCE(priorityticket_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTID]', COALESCE(ticket_contact_id, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]',COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]', COALESCE(ticket_customer_id, '0'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";
		
		try {
			StrSql = "SELECT"
					+ " branch_id , " + " 0 , " + " '', "
					+ " '" + email_from + "', " + " '" + email_to + "', " + " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", " + " " + emp_id + ", " + " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner ON owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crmexe ON crmexe.emp_id = crm_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmexejobtitle ON crmexejobtitle.jobtitle_id = crmexe.emp_jobtitle_id";
			if (type.equals("1")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = enquiry_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle salesexejobtitle ON salesexejobtitle.jobtitle_id = salesexe.emp_jobtitle_id";
			}
			else {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = ticket_so_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = so_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle salesexejobtitle ON salesexejobtitle.jobtitle_id = salesexe.emp_jobtitle_id";
			}
			
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = enquiry_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle managerjobtitle ON managerjobtitle.jobtitle_id = manager.emp_jobtitle_id"
					+ " where ticket_id=" + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			// SOP("SendCRMTicketEmail---email---" + sub);
			// SOP("StrSql---StrSql---" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
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
			SOPError("Axelaauto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
