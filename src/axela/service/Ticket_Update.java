package axela.service;
//@author divya, 23 march 2013

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Update extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String status = "";
	public String msg = "";
	public String ticket_id = "0";
	public String ticket_ticketcat_id = "0";
	public String ticket_tickettype_id = "0";
	public String ticket_parent_id = "0", ticket_branch_id = "0";
	public String ticket_customer_id = "0";
	public String customer_name = "";
	public String veh_id = "";
	public String veh_reg_no = "";
	public String jc_id = "";
	public String ticket_ticketstatus_id = "0";
	public String ticket_subject = "";
	public String ticket_desc = "";
	public String ticket_read = "";
	public String ticket_ticketsource_id = "0";
	public String ticket_priorityticket_id = "0";
	public String ticket_ticket_dept_id = "0";
	public String ticket_emp_id = "0";
	public String ticket_due_time = "";
	public String duedate = "";
	public String ticket_report_time = "";
	public String reporttime = "";
	public String ticket_contact_id = "0";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String customer_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String ticket_entry_id = "0";
	public String ticket_entry_date = "";
	public String ticket_modified_id = "0";
	public String ticket_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public DecimalFormat deci = new DecimalFormat("0.00");
	public String contact_name = "";
	public String StrSql = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String QueryString = "";
	public String ticket_trigger1_hrs = "";
	public String ticket_trigger2_hrs = "";
	public String ticket_trigger3_hrs = "";
	public String ticket_trigger4_hrs = "";
	public String ticket_trigger5_hrs = "";
	public String ticket_dept_trigger1_hrs = "";
	public String ticket_dept_trigger2_hrs = "";
	public String ticket_dept_trigger3_hrs = "";
	public String ticket_dept_trigger4_hrs = "";
	public String ticket_dept_trigger5_hrs = "";
	public String ticket_cc = "";
	public String branch_id = "", param = "", branch_type = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// CheckPerm(comp_id, "emp_ticket_access", request, response);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				// ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
				// SOP("ticket_id===" + ticket_id );

				PopulateConfigDetails();

				if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Ticket".equals(deleteB)) {
						PopulateFields(request, response);
					} else if ("yes".equals(updateB) && !"Delete Ticket".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_ticket_edit", request).equals("1")) {
							CheckForm();
							ticket_modified_id = emp_id;
							ticket_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-list.jsp?ticket_id=" + ticket_id + "&msg=Ticket updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
					if ("Delete Ticket".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_ticket_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-list.jsp?msg=Ticket deleted successfully!"));
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

	public void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ticket_branch_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_branch_id")));;
		ticket_subject = PadQuotes(request.getParameter("txt_ticket_subject"));
		ticket_desc = PadQuotes(request.getParameter("txt_ticket_desc"));
		ticket_report_time = PadQuotes(request.getParameter("txt_ticket_report_time"));
		// SOP("ticket_report_time========" + ticket_report_time);
		ticket_ticketcat_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketcat_id")));
		ticket_tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_tickettype_id")));
		ticket_customer_id = CNumeric(PadQuotes(request.getParameter("ticket_customer_id")));
		ticket_contact_id = CNumeric(PadQuotes(request.getParameter("ticket_contact_id")));
		ticket_ticketsource_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketsource_id")));
		ticket_ticketstatus_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketstatus_id")));
		ticket_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept_id")));
		ticket_priorityticket_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_priorityticket_id")));
		ticket_emp_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_emp_id")));
		customer_branch_id = CNumeric(PadQuotes(request.getParameter("txt_customer_branch_id")));
		ticket_cc = PadQuotes(request.getParameter("txt_ticket_cc"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		try {
			StrSql = " SELECT customer_name, customer_id, contact_id,"
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id =contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id =contact_title_id"
					+ " WHERE contact_id = " + ticket_contact_id + "";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				customer_name = crs1.getString("customer_name");
				contact_name = crs1.getString("contact_name");
			}
			crs1.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (ticket_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (ticket_subject.equals("")) {
			msg = msg + "<br>Enter Subject!";
		} else if (ticket_subject.length() < 5) {
			msg = msg + "<br>Subject should have atleast five characters!";
		}
		if (ticket_desc.equals("")) {
			msg = msg + "<br>Enter Brief Description!";
		} else if (ticket_desc.length() < 5) {
			msg = msg + "<br>Description should have atleast five characters!";
		}
		if (ticket_ticketsource_id.equals("0")) {
			msg = msg + "<br>Select Source!";
		}
		if (ticket_ticketstatus_id.equals("0")) {
			msg = msg + "<br>Select Status!";
		}
		if (ticket_priorityticket_id.equals("0")) {
			msg = msg + "<br>Select Ticket Priority!";
		}
		if (ticket_ticket_dept_id.equals("0")) {
			msg = msg + "<br>Select Department!";
		}
		if (ticket_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		if (!ticket_report_time.equals("")) {
			if (Long.parseLong(ConvertShortDateToStr(ticket_report_time)) > Long.parseLong(ToShortDate(kknow()))) {
				msg = msg + "<br>Report Time Should be less than or equal to Today's Time!";
			}
		}
		if (!ticket_ticket_dept_id.equals("") && !ticket_ticket_dept_id.equals("0")) {
			String duehrs = "", business_hrs = "", starttime = "", endtime = "", sun = "", mon = "", tue = "", wed = "", thu = "", fri = "", sat = "";
			try {
				StrSql = " SELECT ticket_dept_duehrs, ticket_dept_business_hrs, ticket_dept_starttime, ticket_dept_endtime,"
						+ " ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu, ticket_dept_fri, ticket_dept_sat,"
						+ " ticket_dept_trigger1_hrs, ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
						+ " ticket_dept_trigger5_hrs"
						+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept"
						+ " WHERE ticket_dept_id = " + ticket_ticket_dept_id + "";
				// SOP("StrSql=========" + StrSql);
				// SOPInfo("Department--ticket--" + StrSql);
				CachedRowSet crs1 = processQuery(StrSql, 0);

				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						duehrs = crs1.getString("ticket_dept_duehrs");
						business_hrs = crs1.getString("ticket_dept_business_hrs");
						starttime = crs1.getString("ticket_dept_starttime");
						endtime = crs1.getString("ticket_dept_endtime");
						sun = crs1.getString("ticket_dept_sun");
						mon = crs1.getString("ticket_dept_mon");
						tue = crs1.getString("ticket_dept_tue");
						wed = crs1.getString("ticket_dept_wed");
						thu = crs1.getString("ticket_dept_thu");
						fri = crs1.getString("ticket_dept_fri");
						sat = crs1.getString("ticket_dept_sat");
						ticket_dept_trigger1_hrs = crs1.getString("ticket_dept_trigger1_hrs");
						ticket_dept_trigger2_hrs = crs1.getString("ticket_dept_trigger2_hrs");
						ticket_dept_trigger3_hrs = crs1.getString("ticket_dept_trigger3_hrs");
						ticket_dept_trigger4_hrs = crs1.getString("ticket_dept_trigger4_hrs");
						ticket_dept_trigger5_hrs = crs1.getString("ticket_dept_trigger5_hrs");
					}
				}
				crs1.close();
			} catch (SQLException ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			if (business_hrs.equals("1")) {
				// SOP("comp_id---up--" + comp_id + "\t" + "ticket_report_time===" + ticket_report_time);
				ArrayList public_holidate = publicHolidays(ConvertLongDateToStr(ticket_report_time), customer_branch_id, comp_id);
				if (!duehrs.equals("0")) {
					ticket_due_time = DueTime(ConvertLongDateToStr(ticket_report_time), duehrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_due_time = ticket_report_time;
				}
				// SOP("ticket_due_time====2===" + ticket_due_time);
				// *** start- ticket_trigger_hrs
				// ---------------1--------------------
				if (!ticket_dept_trigger1_hrs.equals("0")) {
					ticket_trigger1_hrs = DueTime(ConvertLongDateToStr(ticket_report_time), ticket_dept_trigger1_hrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_trigger1_hrs = "";
				}
				// ---------------2--------------------
				if (!ticket_dept_trigger2_hrs.equals("0")) {
					ticket_trigger2_hrs = DueTime(ConvertLongDateToStr(ticket_report_time), ticket_dept_trigger2_hrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_trigger2_hrs = "";
				}
				// ---------------3--------------------
				if (!ticket_dept_trigger3_hrs.equals("0")) {
					ticket_trigger3_hrs = DueTime(ConvertLongDateToStr(ticket_report_time), ticket_dept_trigger3_hrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_trigger3_hrs = "";
				}
				// ---------------4--------------------
				if (!ticket_dept_trigger4_hrs.equals("0")) {
					ticket_trigger4_hrs = DueTime(ConvertLongDateToStr(ticket_report_time), ticket_dept_trigger4_hrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_trigger4_hrs = "";
				}
				// ---------------5--------------------
				if (!ticket_dept_trigger5_hrs.equals("0")) {
					ticket_trigger5_hrs = DueTime(ConvertLongDateToStr(ticket_report_time), ticket_dept_trigger5_hrs,
							Double.parseDouble(starttime), Double.parseDouble(endtime),
							sun, mon, tue, wed, thu, fri, sat, public_holidate);
				} else {
					ticket_trigger5_hrs = "";
				}
				// *** eof- ticket_trigger_hrs

			} else {
				if (!duehrs.equals("0")) {
					ticket_due_time = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(duehrs), 0));
				} else {
					ticket_trigger1_hrs = "";
				}
				// ---------------1--------------------
				if (!ticket_dept_trigger1_hrs.equals("0")) {
					ticket_trigger1_hrs = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(ticket_dept_trigger1_hrs), 0));
				} else {
					ticket_trigger1_hrs = "";
				}
				// ---------------2--------------------
				if (!ticket_dept_trigger2_hrs.equals("0")) {
					ticket_trigger2_hrs = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(ticket_dept_trigger2_hrs), 0));
				} else {
					ticket_trigger2_hrs = "";
				}
				// ---------------3--------------------
				if (!ticket_dept_trigger3_hrs.equals("0")) {
					ticket_trigger3_hrs = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(ticket_dept_trigger3_hrs), 0));
				} else {
					ticket_trigger3_hrs = "";
				}
				// ---------------4--------------------
				if (!ticket_dept_trigger4_hrs.equals("0")) {
					ticket_trigger4_hrs = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(ticket_dept_trigger4_hrs), 0));
				} else {
					ticket_trigger4_hrs = "";
				}
				// ---------------5--------------------
				if (!ticket_dept_trigger5_hrs.equals("0")) {
					ticket_trigger5_hrs = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(ticket_report_time)), 0, Double.parseDouble(ticket_dept_trigger5_hrs), 0));
				} else {
					ticket_trigger5_hrs = "";
				}
			}
			// SOP("ticket_due_time =up======= " + ticket_due_time);
			// SOP("ticket_trigger1_hrs = " + ticket_trigger1_hrs);
			// SOP("ticket_trigger2_hrs = " + ticket_trigger2_hrs);
			// SOP("ticket_trigger3_hrs = " + ticket_trigger3_hrs);
			// SOP("ticket_trigger4_hrs = " + ticket_trigger4_hrs);
			// SOP("ticket_trigger5_hrs = " + ticket_trigger5_hrs);
		}
		String ccmsg = "";
		if (!ticket_cc.equals("")) {
			String cc[] = ticket_cc.split(",");
			for (int i = 0; i < cc.length; i++) {
				// SOP("cc["+i+"] = " + cc[i]);
				cc[i] = cc[i].replace(" ", "");
				if (!IsValidEmail(cc[i])) {
					ccmsg = "<br>Enter Valid Email CC!";
				}
			}
			msg = msg + ccmsg;
		}
	}

	public void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_ticket.*, "
					+ " COALESCE(CONCAT(title_desc,' ',contact_fname,' ',contact_lname),'') AS contact_name, "
					+ " COALESCE(customer_name,'') AS customer_name, COALESCE(customer_branch_id, 0) AS customer_branch_id,"
					+ " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id =contact_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id =contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " WHERE ticket_id = " + ticket_id + BranchAccess + ExeAccess;
			// SOP("StrSQL==pop==" + StrSql);
			// SOPInfo("PopulateFields--ticket--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_id = crs.getString("ticket_id");
					customer_branch_id = crs.getString("customer_branch_id");
					ticket_contact_id = crs.getString("ticket_contact_id");
					ticket_customer_id = crs.getString("ticket_customer_id");
					customer_name = crs.getString("customer_name");
					contact_name = crs.getString("contact_name");
					veh_id = crs.getString("veh_id");
					if (!crs.getString("veh_reg_no").equals("")) {
						veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 4);
					}
					jc_id = crs.getString("ticket_jc_id");
					ticket_parent_id = crs.getString("ticket_parent_id");
					ticket_branch_id = crs.getString("ticket_branch_id");
					ticket_ticketstatus_id = crs.getString("ticket_ticketstatus_id");
					ticket_ticketcat_id = crs.getString("ticket_ticketcat_id");
					ticket_tickettype_id = crs.getString("ticket_tickettype_id");
					ticket_subject = crs.getString("ticket_subject");
					// SOP("ticket_subject=="+ticket_subject);
					ticket_desc = crs.getString("ticket_desc");
					ticket_emp_id = crs.getString("ticket_emp_id");
					ticket_read = crs.getString("ticket_read");
					if (ticket_read.equals("0") && emp_id.equals(ticket_emp_id)) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket SET ticket_read = '1' WHERE ticket_id = " + ticket_id + "";
						updateQuery(StrSql);
					}
					ticket_ticketsource_id = crs.getString("ticket_ticketsource_id");
					ticket_report_time = strToLongDate(crs.getString("ticket_report_time"));
					// reporttime = strToLongDate(ticket_report_time);
					ticket_ticket_dept_id = crs.getString("ticket_ticket_dept_id");
					ticket_priorityticket_id = crs.getString("ticket_priorityticket_id");
					ticket_due_time = crs.getString("ticket_due_time");
					duedate = strToLongDate(ticket_due_time);
					// SOP("duedate----------" + ticket_due_time);
					ticket_cc = crs.getString("ticket_cc");
					ticket_entry_id = crs.getString("ticket_entry_id");
					if (!ticket_entry_id.equals("") && !ticket_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(ticket_entry_id));
					}
					entry_date = strToLongDate(crs.getString("ticket_entry_date"));

					ticket_modified_id = crs.getString("ticket_modified_id");
					if (!ticket_modified_id.equals("") && !ticket_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(ticket_modified_id));
					}
					modified_date = strToLongDate(crs.getString("ticket_modified_date"));
				}
			} else {
				msg = "msg=Access denied. Please contact system administrator!";
				response.sendRedirect("../portal/error.jsp?" + msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (msg.equals("")) {
			try {
				// SOP("report=========" + ticket_report_time);
				// SOP("=======update===duetime=====" + strToLongDate(ticket_due_time));
				ticket_due_time = strToLongDate(ticket_due_time);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket"
						+ " SET"
						+ " ticket_branch_id = '" + ticket_branch_id + "',"
						+ " ticket_subject = '" + ticket_subject + "',"
						+ " ticket_desc = '" + ticket_desc + "',"
						+ " ticket_ticketcat_id =" + ticket_ticketcat_id + ","
						+ " ticket_tickettype_id = " + ticket_tickettype_id + ","
						+ " ticket_ticketstatus_id = " + ticket_ticketstatus_id + ","
						+ " ticket_ticketsource_id = " + ticket_ticketsource_id + ","
						+ " ticket_ticket_dept_id  = " + ticket_ticket_dept_id + ","
						+ " ticket_priorityticket_id = " + ticket_priorityticket_id + ","
						+ " ticket_emp_id = " + ticket_emp_id + ","
						+ " ticket_report_time = '" + ConvertLongDateToStr(ticket_report_time) + "',"
						+ " ticket_due_time = '" + ConvertLongDateToStr(ticket_due_time) + "',"
						+ " ticket_trigger1_hrs = '" + ticket_trigger1_hrs + "',"
						+ " ticket_trigger2_hrs = '" + ticket_trigger2_hrs + "',"
						+ " ticket_trigger3_hrs = '" + ticket_trigger3_hrs + "',"
						+ " ticket_trigger4_hrs = '" + ticket_trigger4_hrs + "',"
						+ " ticket_trigger5_hrs = '" + ticket_trigger5_hrs + "',"
						+ " ticket_cc = '" + ticket_cc + "',"
						+ " ticket_modified_id = " + ticket_modified_id + ","
						+ " ticket_modified_date = " + ticket_modified_date + ""
						+ " WHERE ticket_id = " + ticket_id + "";
				// SOP("StrSql=up=" + StrSql);
				// SOPInfo("UpdateFields--ticket--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void DeleteFields(HttpServletResponse response) {

		if (msg.equals("")) {
			try {
				StrSql = "SELECT doc_value FROM " + compdb(comp_id) + "axela_service_ticket_docs"
						+ " WHERE doc_ticket_id = " + ticket_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(TicketDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_ticket_docs WHERE doc_ticket_id = " + ticket_id + "";
				updateQuery(StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_ticket_trans WHERE tickettrans_ticket_id = " + ticket_id + "";
				updateQuery(StrSql);
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + ticket_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateSourceType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketsource_id, ticketsource_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_source "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticketsource_id"
					+ " ORDER BY ticketsource_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketsource_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketsource_id"), ticket_ticketsource_id));
				Str.append(">").append(crs.getString("ticketsource_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketstatus_id, ticketstatus_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_status "
					+ " WHERE 1 = 1 ";
			if (!emp_id.equals("1")) {
				StrSql += " AND ticketstatus_id NOT IN(3)";
			}
			StrSql += " GROUP BY ticketstatus_id"
					+ " ORDER BY ticketstatus_id";
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketstatus_id"), ticket_ticketstatus_id));
				Str.append(">").append(crs.getString("ticketstatus_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketPrioirty() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY priorityticket_id"
					+ " ORDER BY priorityticket_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityticket_id"), ticket_priorityticket_id));
				Str.append(">").append(crs.getString("priorityticket_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDepartment() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticket_dept_id"
					+ " ORDER BY ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_ticket_dept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String PopulateExecutive() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0> Select </option>");
	// try {
	// StrSql = "select emp_name, emp_ref_no, " + compdb(comp_id) +
	// "axela_emp.emp_id as emp_id "
	// + " from " + compdb(comp_id) + "axela_emp "
	// + " left join " + compdb(comp_id) + "axela_emp_branch on " +
	// compdb(comp_id) + "axela_emp.emp_id = " + compdb(comp_id) +
	// "axela_emp_branch.emp_id "
	// + " left join " + compdb(comp_id) +
	// "axela_service_ticket on ticket_emp_id = " + compdb(comp_id) +
	// "axela_emp.emp_id "
	// + " and ticket_ticketstatus_id!=3 "
	// + " where emp_active = '1' and " + compdb(comp_id) +
	// "axela_emp.emp_id!=1 "
	// + " and  (" + compdb(comp_id) + "axela_emp_branch.emp_branch_id=" +
	// customer_branch_id + " or " + compdb(comp_id) +
	// "axela_emp.emp_branch_id = " + customer_branch_id + ") "
	// + " group by " + compdb(comp_id) + "axela_emp.emp_id  "
	// + " order by emp_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// // SOP("StrSql-eeell-"+StrSql);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
	// Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }
	public String PopulateAllExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT emp_name, emp_ref_no, emp_id "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					// + " left join " + compdb(comp_id) +
					// "axela_service_ticket on ticket_emp_id = " +
					// compdb(comp_id) + "axela_emp.emp_id "
					// + " and ticket_ticketstatus_id!=3 "
					+ " WHERE 1 = 1 "
					+ " AND emp_ticket_owner = '1' "
					// + ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql====" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketCategory(String depertment_id, String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT ticketcat_id, ticketcat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " WHERE 1 = 1 ";
			if (!depertment_id.equals("0")) {
				StrSql += " AND ticketcat_ticketdept_id = " + depertment_id;
			}
			StrSql += " GROUP BY ticketcat_id "
					+ " ORDER BY ticketcat_name";

			// SOP("StrSqll== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_ticket_ticketcat_id\" class=\"form-control col-md-6\">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketcat_id"), ticket_ticketcat_id));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			Str.append("</select>");

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type  "
					+ " ORDER BY tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), ticket_tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// protected void GetContractDetails() {
	// try {
	// StrSql =
	// "Select customer_name,customer_id, customer_branch_id, contact_id,"
	// +
	// " concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name "
	// + " from " + compdb(comp_id) + "axela_service_contract "
	// + " inner join " + compdb(comp_id) +
	// "axela_customer on customer_id =contact_customer_id"
	// + " inner join " + compdb(comp_id) +
	// "axela_title on title_id =contact_title_id"
	// + " where contact_id=" + CNumeric(ticket_contact_id) + ""
	// + " where 1=1 ";
	//
	//
	// if (!contact_id.equals("0")) {
	// StrSql = StrSql + " and contact_id = " + contact_id + "";
	// } else if (!ticket_contact_id.equals("0")) {
	// StrSql = StrSql + " and contact_id = " + ticket_contact_id + "";
	// }
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// ticket_contact_id = crs.getString("ticket_contact_id");
	// ticket_customer_id = crs.getString("ticket_customer_id");
	// customer_name = crs.getString("customer_name");
	// contact_name = crs.getString("contact_name");
	// }
	// crs.close();
	//
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in: " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
	public void PopulateConfigDetails() {

		StrSql = " SELECT config_service_ticket_cat, config_service_ticket_type, "
				+ " emp_role_id "
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1"
				+ " AND emp_id = " + emp_id;

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
				emp_role_id = crs.getString("emp_role_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
