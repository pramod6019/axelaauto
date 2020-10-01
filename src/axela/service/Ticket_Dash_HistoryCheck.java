package axela.service;

/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Dash_HistoryCheck extends Connect {

	public String name = "";
	public String value = "";
	public String ticket_id = "0";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String ticket_emp_id = "0";
	public String ticket_ticketstatus_id = "0";
	public String ticket_ticket_dept_id = "0";
	public String ticket_report_time = "";
	public String ticket_closed_time = "";
	public String ticket_reopened_emp_id = "0";
	public String ticket_entry_date = "";
	public String comment = "";
	Date closedDate, entryDate, reportDate;
	public String status = "";
	public String StrHTML = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String contact_id = "0";
	public String contact_name = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String send_contact_email = "";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_email_formail = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String ticket_cc = "";
	public String ticket_entry_id = "0";
	public String entryemp_email = "";
	public String emp_email = "";
	public String config_ticket_closed_email_enable = "";
	public String config_ticket_closed_email_sub = "";
	public String config_ticket_closed_email_format = "";
	// public String config_ticket_closed_email_exe_sub = "";
	// public String config_ticket_closed_email_exe_format = "";
	public String config_ticket_closed_sms_enable = "";
	public String config_ticket_closed_sms_format = "";
	// public String config_ticket_closed_sms_exe_format = "";
	public String config_ticket_new_email_enable = "";
	public String config_ticket_new_email_exe_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String comp_email_enable = "", comp_sms_enable = "";
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
	public String customer_branch_id = "";
	public String ExeAccess = "";
	public String customer_details = "";
	public String doc_details = "";
	public String customer_id = "";
	public int recperpage = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String history = "";
	public String summary = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			ExeAccess = GetSession("ExeAccess", request);
			emp_id = CNumeric(GetSession("emp_id", request));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
			comment = PadQuotes(request.getParameter("comment"));
			status = PadQuotes(request.getParameter("status"));
			// SOP("status====" + status);
			customer_details = PadQuotes(request.getParameter("customer_details"));
			doc_details = PadQuotes(request.getParameter("doc_details"));
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			history = PadQuotes(request.getParameter("history"));
			summary = PadQuotes(request.getParameter("summary"));

			try {
				if (!ticket_id.equals("0")) {
					StrSql = "Select ticket_emp_id, ticket_ticketstatus_id, ticket_ticket_dept_id, ticket_entry_date,ticket_entry_id,"
							+ " ticket_closed_time, ticket_report_time, ticket_reopened_time,"
							+ " COALESCE(customer_branch_id,0) as customer_branch_id, ticket_cc, "
							+ " COALESCE(emp_email1,'') as entryemp_email"
							+ " from " + compdb(comp_id) + "axela_service_ticket"
							+ " inner join " + compdb(comp_id) + "axela_service_ticket_dept on  ticket_dept_id = ticket_ticket_dept_id"
							+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_entry_id"
							+ " left join " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id"
							+ " left join " + compdb(comp_id) + "axela_customer on customer_id  = contact_customer_id"
							+ " where ticket_id = " + ticket_id
							+ ExeAccess.replace("emp_id", "ticket_emp_id")
							+ " limit 1";
					// SOP("StrSql = " + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						customer_branch_id = crs.getString("customer_branch_id");
						ticket_ticketstatus_id = crs
								.getString("ticket_ticketstatus_id");
						ticket_emp_id = crs.getString("ticket_emp_id");
						ticket_ticket_dept_id = crs.getString("ticket_ticket_dept_id");
						ticket_closed_time = crs.getString("ticket_closed_time");
						ticket_report_time = crs.getString("ticket_report_time");
						ticket_cc = crs.getString("ticket_cc");
						ticket_entry_date = crs.getString("ticket_entry_date");
						if (!ticket_closed_time.equals("")) {
							closedDate = StringToDate(ticket_closed_time);
						}
						reportDate = StringToDate(ticket_report_time);
						entryDate = StringToDate(ticket_entry_date);
						ticket_entry_id = crs.getString("ticket_entry_id");
						entryemp_email = crs.getString("entryemp_email");
					}
					crs.close();
				} else {
					StrHTML = "Update Permission Denied!!!</font>";
					return;
				}
				// SOP("ticket_emp_id = " + ticket_emp_id);
				if ((!ticket_emp_id.equals("0") || emp_id.equals("1"))
						|| ticket_closed_time.equals("")) {

					if (name.equals("txt_ticket_subject")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (value.length() < 5) {
								StrHTML = "Subject Should be Atleast five Digits!</font>";
							} else {
								String history_oldvalue = ExecuteQuery("Select ticket_subject from "
										+ compdb(comp_id)
										+ "axela_service_ticket where ticket_id="
										+ ticket_id + " ");
								StrSql = "Update "
										+ compdb(comp_id)
										+ "axela_service_ticket set ticket_subject='"
										+ value + "' where ticket_id=" + ticket_id
										+ "";
								updateQuery(StrSql);
								String history_actiontype = "TICKET SUBJECT";
								StrSql = "insert into "
										+ compdb(comp_id)
										+ "axela_service_ticket_history (history_ticket_id,history_emp_id,history_datetime,history_actiontype,history_oldvalue,history_newvalue) values ('"
										+ ticket_id + "','" + emp_id + "','"
										+ ToLongDate(kknow()) + "','"
										+ history_actiontype + "','"
										+ history_oldvalue + "','" + value + "') ";
								updateQuery(StrSql);
								StrHTML = "Subject updated!</font>";
							}
						} else {
							StrHTML = "Enter Subject!</font>";
						}
					}

					if (name.equals("txt_ticket_desc")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (value.length() < 5) {
								StrHTML = "Description Should be Atleast five Digits!</font>";
							} else {
								String history_oldvalue = ExecuteQuery("Select ticket_desc from "
										+ compdb(comp_id)
										+ "axela_service_ticket where ticket_id="
										+ ticket_id + " ");
								StrSql = "Update " + compdb(comp_id)
										+ "axela_service_ticket set ticket_desc='"
										+ value + "' where ticket_id=" + ticket_id
										+ "";
								updateQuery(StrSql);
								String history_actiontype = "TICKET DESCRIPTION";
								StrSql = "insert into "
										+ compdb(comp_id)
										+ "axela_service_ticket_history (history_ticket_id,history_emp_id,history_datetime,history_actiontype,history_oldvalue,history_newvalue) values ('"
										+ ticket_id + "','" + emp_id + "','"
										+ ToLongDate(kknow()) + "','"
										+ history_actiontype + "','"
										+ history_oldvalue + "','" + value + "') ";
								updateQuery(StrSql);
								StrHTML = "Description updated!</font>";
							}
						} else {
							StrHTML = "Enter Description!</font>";
						}
					}

					if (name.equals("txt_report_time")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (isValidDateFormatLong(value)) {
								value = ConvertLongDateToStr(value);
							} else {
								StrHTML = "Enter Valid Report time!</font>";
								return;
							}
							if (Long.parseLong(value) > Long
									.parseLong(ToLongDate(kknow()))) {
								StrHTML = "Report time should be less than or equal to Today's time!</font>";
								return;
							}
							String history_oldvalue = strToLongDate(ExecuteQuery("Select ticket_report_time from "
									+ compdb(comp_id)
									+ "axela_service_ticket where ticket_id="
									+ ticket_id + " "));
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_report_time = '" + value + "'"
									+ " where ticket_id = " + ticket_id + "";
							updateQuery(StrSql);
							String history_actiontype = "TICKET_REPORT_TIME";
							String history_newvalue = strToLongDate(value);
							StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue)"
									+ " values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " " + ToLongDate(kknow())
									+ "," + " '" + history_actiontype + "'," + " '"
									+ history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Report Time updated!</font>";

							history_oldvalue = strToLongDate(ExecuteQuery("Select ticket_due_time from "
									+ compdb(comp_id)
									+ "axela_service_ticket where ticket_id="
									+ ticket_id + " "));

							// /////due time
							String duehrs = "", business_hrs = "", starttime = "", endtime = "", sun = "", mon = "", tue = "", wed = "", thu = "", fri = "", sat = "";
							String ticket_due_time = "";
							StrSql = " select ticket_dept_duehrs, ticket_dept_business_hrs, ticket_dept_starttime, ticket_dept_endtime,"
									+ " ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu, ticket_dept_fri, ticket_dept_sat,"
									+ " ticket_dept_trigger1_hrs, ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
									+ " ticket_dept_trigger5_hrs"
									+ " from "
									+ compdb(comp_id)
									+ "axela_service_ticket_dept"
									+ " where ticket_dept_id=" + ticket_ticket_dept_id + "";
							// SOP("StrSql = " + StrSql);
							CachedRowSet crs1 = processQuery(StrSql, 0);
							if (crs1.isBeforeFirst()) {
								while (crs1.next()) {
									duehrs = crs1.getString("ticket_dept_duehrs");
									business_hrs = crs1
											.getString("ticket_dept_business_hrs");
									starttime = crs1.getString("ticket_dept_starttime");
									endtime = crs1.getString("ticket_dept_endtime");
									sun = crs1.getString("ticket_dept_sun");
									mon = crs1.getString("ticket_dept_mon");
									tue = crs1.getString("ticket_dept_tue");
									wed = crs1.getString("ticket_dept_wed");
									thu = crs1.getString("ticket_dept_thu");
									fri = crs1.getString("ticket_dept_fri");
									sat = crs1.getString("ticket_dept_sat");
									ticket_dept_trigger1_hrs = crs1
											.getString("ticket_dept_trigger1_hrs");
									ticket_dept_trigger2_hrs = crs1
											.getString("ticket_dept_trigger2_hrs");
									ticket_dept_trigger3_hrs = crs1
											.getString("ticket_dept_trigger3_hrs");
									ticket_dept_trigger4_hrs = crs1
											.getString("ticket_dept_trigger4_hrs");
									ticket_dept_trigger5_hrs = crs1
											.getString("ticket_dept_trigger5_hrs");
								}
								crs1.close();
							}
							if (business_hrs.equals("1")) {
								ArrayList public_holidate = publicHolidays(value,
										customer_branch_id, comp_id);
								if (!duehrs.equals("0")) {
									ticket_due_time = DueTime((value), duehrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_due_time = value;
								}
								// *** start- ticket_trigger_hrs
								// ---------------1--------------------
								if (!ticket_dept_trigger1_hrs.equals("0")) {
									ticket_trigger1_hrs = DueTime(value,
											ticket_dept_trigger1_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger1_hrs = "";
								}
								// ---------------2--------------------
								if (!ticket_dept_trigger2_hrs.equals("0")) {
									ticket_trigger2_hrs = DueTime(value,
											ticket_dept_trigger2_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger2_hrs = "";
								}
								// ---------------3--------------------
								if (!ticket_dept_trigger3_hrs.equals("0")) {
									ticket_trigger3_hrs = DueTime(value,
											ticket_dept_trigger3_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger3_hrs = "";
								}
								// ---------------4--------------------
								if (!ticket_dept_trigger4_hrs.equals("0")) {
									ticket_trigger4_hrs = DueTime(value,
											ticket_dept_trigger4_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger4_hrs = "";
								}
								// ---------------5--------------------
								if (!ticket_dept_trigger5_hrs.equals("0")) {
									ticket_trigger5_hrs = DueTime(value,
											ticket_dept_trigger5_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger5_hrs = "";
								}
								// *** eof- ticket_trigger_hrs
							} else {
								// SOP("elsee==");
								if (!duehrs.equals("0")) {
									ticket_due_time = ToLongDate(AddHoursDate(
											StringToDate((value)), 0,
											Double.parseDouble(duehrs), 0));
								} else {
									ticket_due_time = value;
								}
								// ---------------1--------------------
								if (!ticket_dept_trigger1_hrs.equals("0")) {
									ticket_trigger1_hrs = ToLongDate(AddHoursDate(
											StringToDate(value), 0,
											Double.parseDouble(ticket_dept_trigger1_hrs),
											0));
								} else {
									ticket_trigger1_hrs = "";
								}
								// ---------------2--------------------
								if (!ticket_dept_trigger2_hrs.equals("0")) {
									ticket_trigger2_hrs = ToLongDate(AddHoursDate(
											StringToDate(value), 0,
											Double.parseDouble(ticket_dept_trigger2_hrs),
											0));
								} else {
									ticket_trigger2_hrs = "";
								}
								// ---------------3--------------------
								if (!ticket_dept_trigger3_hrs.equals("0")) {
									ticket_trigger3_hrs = ToLongDate(AddHoursDate(
											StringToDate(value), 0,
											Double.parseDouble(ticket_dept_trigger3_hrs),
											0));
								} else {
									ticket_trigger3_hrs = "";
								}
								// ---------------4--------------------
								if (!ticket_dept_trigger4_hrs.equals("0")) {
									ticket_trigger4_hrs = ToLongDate(AddHoursDate(
											StringToDate(value), 0,
											Double.parseDouble(ticket_dept_trigger4_hrs),
											0));
								} else {
									ticket_trigger4_hrs = "";
								}
								// ---------------5--------------------
								if (!ticket_dept_trigger5_hrs.equals("0")) {
									ticket_trigger5_hrs = ToLongDate(AddHoursDate(
											StringToDate(value), 0,
											Double.parseDouble(ticket_dept_trigger5_hrs),
											0));
								} else {
									ticket_trigger5_hrs = "";
								}
							}

							// SOP("ticket_due_time==" + ticket_due_time);
							// //////end due time
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_due_time = '" + ticket_due_time
									+ "'," + " ticket_trigger1_hrs = '"
									+ ticket_trigger1_hrs + "',"
									+ " ticket_trigger2_hrs = '"
									+ ticket_trigger2_hrs + "',"
									+ " ticket_trigger3_hrs = '"
									+ ticket_trigger3_hrs + "',"
									+ " ticket_trigger4_hrs = '"
									+ ticket_trigger4_hrs + "',"
									+ " ticket_trigger5_hrs = '"
									+ ticket_trigger5_hrs + "'"
									+ " where ticket_id = " + ticket_id + "";
							updateQuery(StrSql);
							history_actiontype = "TICKET_DUE_DATE";
							history_newvalue = strToLongDate(ticket_due_time);
							StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue)"
									+ " values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " " + ToLongDate(kknow())
									+ "," + " '" + history_actiontype + "'," + " '"
									+ history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("ticket_due_time=---=" + ticket_due_time);
							StrHTML = StrHTML
									+ "<br>Due Date updated!</font>"
									+ "&nbsp<input type=hidden name=duedate id=duedate value= '"
									+ strToLongDate(ticket_due_time) + "'>";
						} else {
							StrHTML = "Enter Report Time!</font>";
						}
					}

					if (name.equals("dr_ticket_ticketsource_id")) {
						if (!value.equals("0") && !value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select coalesce(ticketsource_name,'') as ticketsource_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket left join "
									+ compdb(comp_id)
									+ "axela_service_ticket_source on ticketsource_id = ticket_ticketsource_id where ticket_id="
									+ ticket_id + " ");
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_ticketsource_id = '" + value + "'"
									+ " where ticket_id = " + ticket_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("Select ticketsource_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket_source where ticketsource_id="
									+ value + " ");
							String history_actiontype = "TICKET SOURCE";
							StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue)"
									+ " values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " '" + ToLongDate(kknow())
									+ "'," + " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Ticket Source updated!</font>";
						} else {
							StrHTML = "Select Ticket Source!</font>";
						}
					}

					if (name.equals("dr_ticket_priorityticket_id")) {
						if (!value.equals("0") && !value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select priorityticket_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket left join "
									+ compdb(comp_id)
									+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id where ticket_id="
									+ ticket_id + " ");
							if (history_oldvalue == null) {
								history_oldvalue = "";
							}
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_priorityticket_id = '" + value + "'"
									+ " where ticket_id = " + ticket_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("Select priorityticket_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket_priority where priorityticket_id="
									+ value + " ");
							String history_actiontype = "TICKET PRIORITY";
							StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue) "
									+ "values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " '" + ToLongDate(kknow())
									+ "'," + " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Ticket Priority updated!</font>";
						} else {
							StrHTML = "Select Ticket Priority!</font>";
						}
					}

					if (name.equals("dr_ticket_emp_id")) {
						if (!value.equals("0") && !value.equals("")) {
							// String ticket_close =
							// ExecuteQuery("Select emp_ticket_close from " +
							// compdb(comp_id) + "axela_emp where emp_id=" +
							// emp_id
							// + " ");
							// if (ticket_close.equals("0")) {
							// StrHTML = "Update Permission Denied!</font>";
							// return;
							// }
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select ticket_emp_id from "
									+ compdb(comp_id)
									+ "axela_service_ticket where ticket_id="
									+ ticket_id + " ");
							if (!history_oldvalue.equals("")) {
								history_oldvalue = ExecuteQuery("Select coalesce(concat(emp_name,' (',emp_ref_no,')'),'') as emp_name"
										+ " from "
										+ compdb(comp_id)
										+ "axela_service_ticket"
										+ " inner join "
										+ compdb(comp_id)
										+ "axela_emp on emp_id = ticket_emp_id"
										+ " where ticket_id = " + ticket_id + "");
							} else {
								history_oldvalue = "No Executives Found";
							}
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket set ticket_emp_id='"
									+ value + "' where ticket_id=" + ticket_id + "";
							updateQuery(StrSql);

							PopulateConfigDetails();
							if (comp_email_enable.equals("1")
									&& config_email_enable.equals("1")
									&& !config_admin_email.equals("")
									&& config_ticket_new_email_enable.equals("1")) {
								// SOP("config_ticket_new_email_exe_format = " +
								// config_ticket_new_email_exe_format);
								// SOP("config_ticket_new_email_exe_sub = " +
								// config_ticket_new_email_exe_sub);
								// SOP("emp_email_formail = " +
								// emp_email_formail);
								if (!config_ticket_new_email_exe_format.equals("")) {
									emp_email_formail = "";
									emp_email_formail = ExecuteQuery("Select if(emp_email2!='', concat(emp_email1, ', ', emp_email2), emp_email1) as emp_email"
											+ " from "
											+ compdb(comp_id)
											+ "axela_emp where emp_id = "
											+ value
											+ " limit 1");
									if (!value.equals(ticket_entry_id)
											&& !entryemp_email.equals("")) {
										emp_email_formail = emp_email_formail + ","
												+ entryemp_email;
									}
									// SOP("1111111------------"+emp_email_formail);
									SendEmailToExecutive1();
								}
							}
							String history_newvalue = ExecuteQuery("Select emp_id from "
									+ compdb(comp_id)
									+ "axela_emp where emp_id="
									+ value + " ");
							if (!history_newvalue.equals("")) {
								history_newvalue = ExecuteQuery("Select coalesce(concat(emp_name,' (',emp_ref_no,')'),'') as emp_name"
										+ " from "
										+ compdb(comp_id)
										+ "axela_service_ticket"
										+ " inner join "
										+ compdb(comp_id)
										+ "axela_emp on emp_id = ticket_emp_id"
										+ " where ticket_id = " + ticket_id + " ");
							} else {
								history_newvalue = "No Executives Found";
							}
							String history_actiontype = "TICKET EXECUTIVE";
							StrSql = "insert into " + compdb(comp_id)
									+ "axela_service_ticket_history "
									+ "(history_ticket_id," + "history_emp_id,"
									+ "history_datetime," + "history_actiontype,"
									+ "history_oldvalue," + "history_newvalue) "
									+ "values " + "('" + ticket_id + "','" + ""
									+ emp_id + "','" + "" + ToLongDate(kknow())
									+ "','" + "" + history_actiontype + "','" + ""
									+ history_oldvalue + "','" + ""
									+ history_newvalue + "') ";
							updateQuery(StrSql);
							StrHTML = "Ticket Executive updated!</font>";
						} else {
							StrHTML = "Select Ticket Executive!</font>";
						}
					}
					if (name.equals("txt_ticket_cc")) {
						// if (!value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						String cc[] = value.split(",");
						int i = 0;
						String ccmsg = "";
						for (i = 0; i < cc.length; i++) {
							cc[i] = cc[i].replace(" ", "");
							if (!IsValidEmail(cc[i])) {
								ccmsg = "Enter Valid Email CC!";
							}
						}
						if (ccmsg.equals("")) {
							String history_oldvalue = ExecuteQuery("Select ticket_cc from "
									+ compdb(comp_id)
									+ "axela_service_ticket where ticket_id="
									+ ticket_id + " ");
							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket set ticket_cc='"
									+ value + "' where ticket_id=" + ticket_id + "";
							updateQuery(StrSql);
							String history_actiontype = "EMAIL CC";
							StrSql = "insert into "
									+ compdb(comp_id)
									+ "axela_service_ticket_history (history_ticket_id,history_emp_id,history_datetime,history_actiontype,history_oldvalue,history_newvalue) values ('"
									+ ticket_id + "','" + emp_id + "','"
									+ ToLongDate(kknow()) + "','"
									+ history_actiontype + "','" + history_oldvalue
									+ "','" + value + "') ";
							updateQuery(StrSql);
							StrHTML = "EMAIL CC updated!</font>";
						} else {
							StrHTML = ccmsg + "</font>";
						}
					}

					if (name.equals("dr_ticket_dept_id")) {
						if (!value.equals("0") && !value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select ticket_dept_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket"
									+ " inner join "
									+ compdb(comp_id)
									+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
									+ " where ticket_id = " + ticket_id + "");

							StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_ticket_dept_id = '" + value + "'"
									+ " where ticket_id = " + ticket_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("Select ticket_dept_name from "
									+ compdb(comp_id)
									+ "axela_service_ticket_dept where ticket_dept_id="
									+ value + " ");
							String history_actiontype = "TICKET DEPARTMENT";
							StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue) "
									+ "values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " '" + ToLongDate(kknow())
									+ "'," + " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Ticket Department updated!</font>";

							history_oldvalue = strToLongDate(ExecuteQuery("Select ticket_due_time from "
									+ compdb(comp_id)
									+ "axela_service_ticket where ticket_id="
									+ ticket_id + " "));
							// String duehrs =
							// ExecuteQuery("Select ticket_dept_duehrs from " +
							// compdb(comp_id) +
							// "axela_service_ticket_dept where ticket_dept_id = " +
							// value + " ");
							// String ticket_due_time =
							// ToLongDate(AddHoursDate(StringToDate(ticket_report_time),
							// 0, Double.parseDouble(duehrs), 0));

							// /////due time
							String duehrs = "", business_hrs = "", starttime = "", endtime = "", sun = "", mon = "", tue = "", wed = "", thu = "", fri = "", sat = "";
							String ticket_due_time = "";
							StrSql = " select ticket_dept_duehrs, ticket_dept_business_hrs, ticket_dept_starttime, ticket_dept_endtime,"
									+ " ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu, ticket_dept_fri, ticket_dept_sat,"
									+ " ticket_dept_trigger1_hrs, ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
									+ " ticket_dept_trigger5_hrs"
									+ " from "
									+ compdb(comp_id)
									+ "axela_service_ticket_dept"
									+ " where ticket_dept_id=" + value + "";
							// SOP("Duerel SQL----" + StrSql);
							CachedRowSet crs1 = processQuery(StrSql, 0);
							if (crs1.isBeforeFirst()) {
								while (crs1.next()) {
									duehrs = crs1.getString("ticket_dept_duehrs");
									business_hrs = crs1
											.getString("ticket_dept_business_hrs");
									starttime = crs1.getString("ticket_dept_starttime");
									endtime = crs1.getString("ticket_dept_endtime");
									sun = crs1.getString("ticket_dept_sun");
									mon = crs1.getString("ticket_dept_mon");
									tue = crs1.getString("ticket_dept_tue");
									wed = crs1.getString("ticket_dept_wed");
									thu = crs1.getString("ticket_dept_thu");
									fri = crs1.getString("ticket_dept_fri");
									sat = crs1.getString("ticket_dept_sat");
									ticket_dept_trigger1_hrs = crs1
											.getString("ticket_dept_trigger1_hrs");
									ticket_dept_trigger2_hrs = crs1
											.getString("ticket_dept_trigger2_hrs");
									ticket_dept_trigger3_hrs = crs1
											.getString("ticket_dept_trigger3_hrs");
									ticket_dept_trigger4_hrs = crs1
											.getString("ticket_dept_trigger4_hrs");
									ticket_dept_trigger5_hrs = crs1
											.getString("ticket_dept_trigger5_hrs");
								}
							}
							crs1.close();
							if (business_hrs.equals("1")) {
								ArrayList public_holidate = publicHolidays(
										ticket_report_time, customer_branch_id, comp_id);
								if (!duehrs.equals("0")) {
									ticket_due_time = DueTime(ticket_report_time,
											duehrs, Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_due_time = ticket_report_time;
								}
								// *** start- ticket_trigger_hrs
								// ---------------1--------------------
								if (!ticket_dept_trigger1_hrs.equals("0")) {
									ticket_trigger1_hrs = DueTime(
											ticket_report_time, ticket_dept_trigger1_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger1_hrs = "";
								}
								// ---------------2--------------------
								if (!ticket_dept_trigger2_hrs.equals("0")) {
									ticket_trigger2_hrs = DueTime(
											ticket_report_time, ticket_dept_trigger2_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger2_hrs = "";
								}
								// ---------------3--------------------
								if (!ticket_dept_trigger3_hrs.equals("0")) {
									ticket_trigger3_hrs = DueTime(
											ticket_report_time, ticket_dept_trigger3_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger3_hrs = "";
								}
								// ---------------4--------------------
								if (!ticket_dept_trigger4_hrs.equals("0")) {
									ticket_trigger4_hrs = DueTime(
											ticket_report_time, ticket_dept_trigger4_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger4_hrs = "";
								}
								// ---------------5--------------------
								if (!ticket_dept_trigger5_hrs.equals("0")) {
									ticket_trigger5_hrs = DueTime(
											ticket_report_time, ticket_dept_trigger5_hrs,
											Double.parseDouble(starttime),
											Double.parseDouble(endtime), sun, mon,
											tue, wed, thu, fri, sat,
											public_holidate);
								} else {
									ticket_trigger5_hrs = "";
								}
								// *** eof- ticket_trigger_hrs
							} else {
								if (!duehrs.equals("0")) {
									ticket_due_time = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(duehrs), 0));
								} else {
									ticket_due_time = ticket_report_time;
								}
								// ---------------1--------------------
								if (!ticket_dept_trigger1_hrs.equals("0")) {
									ticket_trigger1_hrs = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(ticket_dept_trigger1_hrs),
											0));
								} else {
									ticket_trigger1_hrs = "";
								}

								// ---------------2--------------------
								if (!ticket_dept_trigger2_hrs.equals("0")) {
									ticket_trigger2_hrs = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(ticket_dept_trigger2_hrs),
											0));
								} else {
									ticket_trigger2_hrs = "";
								}

								// ---------------3--------------------
								if (!ticket_dept_trigger3_hrs.equals("0")) {
									ticket_trigger3_hrs = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(ticket_dept_trigger3_hrs),
											0));
								} else {
									ticket_trigger3_hrs = "";
								}

								// ---------------4--------------------
								if (!ticket_dept_trigger4_hrs.equals("0")) {
									ticket_trigger4_hrs = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(ticket_dept_trigger4_hrs),
											0));
								} else {
									ticket_trigger4_hrs = "";
								}

								// ---------------5--------------------
								if (!ticket_dept_trigger5_hrs.equals("0")) {
									ticket_trigger5_hrs = ToLongDate(AddHoursDate(
											StringToDate(ticket_report_time), 0,
											Double.parseDouble(ticket_dept_trigger5_hrs),
											0));
								} else {
									ticket_trigger5_hrs = "";
								}

							}

							// //////end due time
							// SOP("ticket_due_time = " + ticket_due_time);
							// SOP("ticket_trigger1_hrs = " +
							// ticket_trigger1_hrs);
							// SOP("ticket_trigger2_hrs = " +
							// ticket_trigger2_hrs);
							// SOP("ticket_trigger3_hrs = " +
							// ticket_trigger3_hrs);
							// SOP("ticket_trigger4_hrs = " +
							// ticket_trigger4_hrs);
							// SOP("ticket_trigger5_hrs = " +
							// ticket_trigger5_hrs);
							this.StrSql = "Update " + compdb(comp_id)
									+ "axela_service_ticket" + " SET"
									+ " ticket_due_time = '" + ticket_due_time
									+ "'," + " ticket_trigger1_hrs = '"
									+ ticket_trigger1_hrs + "',"
									+ " ticket_trigger2_hrs = '"
									+ ticket_trigger2_hrs + "',"
									+ " ticket_trigger3_hrs = '"
									+ ticket_trigger3_hrs + "',"
									+ " ticket_trigger4_hrs = '"
									+ ticket_trigger4_hrs + "',"
									+ " ticket_trigger5_hrs = '"
									+ ticket_trigger5_hrs + "'"
									+ " where ticket_id = " + ticket_id + "";
							// SOP("StrSql = " + StrSql);
							updateQuery(this.StrSql);
							history_actiontype = "TICKET DUE DATE";
							history_newvalue = strToLongDate(ticket_due_time);
							this.StrSql = "INSERT into " + compdb(comp_id)
									+ "axela_service_ticket_history"
									+ " (history_ticket_id," + " history_emp_id,"
									+ " history_datetime," + " history_actiontype,"
									+ " history_oldvalue," + " history_newvalue)"
									+ " values (" + " '" + ticket_id + "'," + " '"
									+ emp_id + "'," + " '" + ToLongDate(kknow())
									+ "'," + " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(this.StrSql);
							StrHTML = StrHTML
									+ "<br>Due Date updated!</font>"
									+ "&nbsp<input type=hidden name=duedate id=duedate value= '"
									+ strToLongDate(ticket_due_time) + "'>";
						} else {
							StrHTML = "Select Ticket Department!</font>";
						}
					}

					if (name.equals("dr_ticket_ticketstatus_id")
							|| name.equals("txt_comments")) {
						value = status;
						value = value.replaceAll("nbsp", "&");
						if (!value.equals("0")) {
							if (status.equals("3") || status.equals("5")) {
								String history_oldvalue = ExecuteQuery("Select ticketstatus_name from "
										+ compdb(comp_id)
										+ "axela_service_ticket inner join "
										+ compdb(comp_id)
										+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id where ticket_id="
										+ ticket_id + " ");
								String emp_ticket_close = ExecuteQuery("Select emp_ticket_close from "
										+ compdb(comp_id)
										+ "axela_emp where emp_id = "
										+ emp_id);
								// SOP("emp_ticket_close = " +
								// emp_ticket_close);
								if (emp_ticket_close.equals("1")) {
									if (!comment.equals("")) {
										if (comment.length() < 5) {
											StrHTML = "Close Summary Should be Atleast five Digits!</font>";
										} else {
											// StrSql = "Update " +
											// compdb(comp_id)
											// + "axela_service_ticket"
											// + " SET"
											// + " ticket_ticketstatus_id = '" +
											// value + "'"
											// + " where ticket_id = " +
											// ticket_id +
											// "";
											StrSql = "Update " + compdb(comp_id)
													+ "axela_service_ticket"
													+ " SET"
													+ " ticket_ticketstatus_id = '"
													+ value + "',"
													+ " ticket_closed_emp_id = "
													+ emp_id + ","
													+ " ticket_closed_time = '"
													+ ToLongDate(kknow()) + "'"
													+ " where ticket_id = "
													+ ticket_id + "";
											// SOP("StrSql=="+StrSql);
											updateQuery(StrSql);
											String history_newvalue = ExecuteQuery("Select ticketstatus_name from "
													+ compdb(comp_id)
													+ "axela_service_ticket_status where ticketstatus_id="
													+ value + " ");
											String history_actiontype = "TICKET STATUS";
											StrSql = "INSERT into "
													+ compdb(comp_id)
													+ "axela_service_ticket_history"
													+ " (history_ticket_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " values (" + " '"
													+ ticket_id + "'," + " '"
													+ emp_id + "'," + " '"
													+ ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype
													+ "'," + " '"
													+ history_oldvalue + "',"
													+ " '" + history_newvalue
													+ "')";
											updateQuery(StrSql);
											StrHTML = "Ticket Status updated!</font><br>";

											value = comment;

											value = value.replaceAll("nbsp", "&");
											history_oldvalue = ExecuteQuery("Select ticket_closed_comments from "
													+ compdb(comp_id)
													+ "axela_service_ticket where ticket_id="
													+ ticket_id + " ");

											StrSql = "Update " + compdb(comp_id)
													+ "axela_service_ticket"
													+ " SET"
													+ " ticket_closed_comments = '"
													+ value + "'"
													+ " where ticket_id = "
													+ ticket_id + "";
											// StrSql = "Update " +
											// compdb(comp_id)
											// + "axela_service_ticket"
											// + " SET"
											// + " ticket_closed_comments = '" +
											// value + "',"
											// + " ticket_closed_emp_id = " +
											// emp_id
											// + ","
											// + " ticket_closed_time = '" +
											// ToLongDate(kknow()) + "'"
											// + " where ticket_id = " +
											// ticket_id +
											// "";
											updateQuery(StrSql);

											history_actiontype = "CLOSED SUMMARY";
											StrSql = "INSERT into "
													+ compdb(comp_id)
													+ "axela_service_ticket_history"
													+ " (history_ticket_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " values (" + " '"
													+ ticket_id + "'," + " '"
													+ emp_id + "'," + " '"
													+ ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype
													+ "'," + " '"
													+ history_oldvalue + "',"
													+ " '" + value + "')";
											updateQuery(StrSql);
											StrHTML = StrHTML
													+ "Close Summary updated!</font>";
											PopulateConfigDetails();
											ContactInfo();
											if (!contact_email2.equals("")
													&& !contact_email1.equals("")) {
												send_contact_email = contact_email1
														+ "," + contact_email2;
											} else if (!contact_email1.equals("")) {
												send_contact_email = contact_email1;
											}
											if (comp_email_enable.equals("1")
													&& config_email_enable
															.equals("1")
													&& !config_admin_email
															.equals("")
													&& config_ticket_closed_email_enable
															.equals("1")) {
												// SOP("contact_email1=="+contact_email1+"   contact_mobile1==="+contact_mobile1);
												if (!send_contact_email.equals("")
														&& !config_ticket_closed_email_format
																.equals("")
														&& !config_ticket_closed_email_sub
																.equals("")) {
													// SendEmail(); //important
													// not
													// enable
												}
												// SOP("config_ticket_new_email_exe_format = "
												// +
												// config_ticket_new_email_exe_format);
												// SOP("config_ticket_new_email_exe_sub = "
												// +
												// config_ticket_new_email_exe_sub);
												// SOP("emp_email_formail = " +
												// emp_email_formail);
												// if (!config_ticket_closed_email_exe_format
												// .equals("")
												// && !config_ticket_closed_email_exe_sub
												// .equals("")
												// && !emp_email_formail
												// .equals("")) {
												// SOP("contact exe");
												// SendEmailToExecutive();
												// }
											}
											if (comp_sms_enable.equals("1")
													&& config_sms_enable
															.equals("1")
													&& config_ticket_closed_sms_enable
															.equals("1")) {
												// SOP("send sms  config_ticket_new_sms_format=="
												// +
												// config_ticket_new_sms_format);
												if (!config_ticket_closed_sms_format
														.equals("")) {
													// SOP("send smsss==" +
													// comp_sms_enable);
													if (!contact_mobile1.equals("")) {
														// SendSMS(contact_mobile1);
													}
													if (!contact_mobile2.equals("")) {
														// SendSMS(contact_mobile2);
													}
												}
												// SOP("config_ticket_new_sms_exe_format = "
												// +
												// config_ticket_new_sms_exe_format);
												// if (!config_ticket_closed_sms_exe_format
												// .equals("")) {
												// SOP("exeeeeeee");
												// if (!emp_mobile1.equals("")) {
												// SendSMSToExecutive(emp_mobile1);
												// }
												// if (!emp_mobile2.equals("")) {
												// SendSMSToExecutive(emp_mobile2);
												// }
												// }
											}
										}
									} else {
										StrHTML = "Enter Close Summary!</font>";
									}
								} else {
									StrHTML = "Update Permission Denied!";
								}
							} else if (!status.equals("3") && !status.equals("5") && !status.equals("")
									&& name.equals("dr_ticket_ticketstatus_id")) {
								status = status.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("Select ticketstatus_name from "
										+ compdb(comp_id)
										+ "axela_service_ticket inner join "
										+ compdb(comp_id)
										+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id where ticket_id="
										+ ticket_id + " ");

								if (status.equals("5")) {
									StrHTML = "Update Permission Denied!</font>";
									return;
								}
								StrSql = "Update " + compdb(comp_id)
										+ "axela_service_ticket" + " SET"
										+ " ticket_ticketstatus_id = '" + status
										+ "'" + " where ticket_id = " + ticket_id
										+ "";
								// StrSql = "Update " + compdb(comp_id) +
								// "axela_service_ticket"
								// + " SET"
								// + " ticket_ticketstatus_id = '" + status +
								// "',"
								// + " ticket_closed_emp_id = " + emp_id + ","
								// + " ticket_closed_time = '" +
								// ToLongDate(kknow())
								// + "'"
								// + " where ticket_id = " + ticket_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("Select ticketstatus_name from "
										+ compdb(comp_id)
										+ "axela_service_ticket_status where ticketstatus_id="
										+ status + " ");
								String history_actiontype = "TICKET STATUS";

								StrSql = "INSERT into " + compdb(comp_id)
										+ "axela_service_ticket_history"
										+ " (history_ticket_id,"
										+ " history_emp_id," + " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " values (" + " '"
										+ ticket_id + "'," + " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Ticket Status updated!</font>";
							} else if (!status.equals("3")
									&& name.equals("txt_comments")) {
								value = comment;
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("Select ticket_closed_comments from "
										+ compdb(comp_id)
										+ "axela_service_ticket where ticket_id="
										+ ticket_id + " ");

								StrSql = "Update " + compdb(comp_id)
										+ "axela_service_ticket" + " SET"
										+ " ticket_closed_comments = '" + value
										+ "'" + " where ticket_id = " + ticket_id
										+ "";
								// StrSql = "Update " + compdb(comp_id) +
								// "axela_service_ticket"
								// + " SET"
								// + " ticket_closed_comments = '" + value +
								// "',"
								// + " ticket_closed_emp_id = " + emp_id + ","
								// + " ticket_closed_time = '" +
								// ToLongDate(kknow())
								// + "'"
								// + " where ticket_id = " + ticket_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CLOSED SUMMARY";
								StrSql = "INSERT into " + compdb(comp_id)
										+ "axela_service_ticket_history"
										+ " (history_ticket_id,"
										+ " history_emp_id," + " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " values (" + " '"
										+ ticket_id + "'," + " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Close Summary updated!</font>";
							}
						} else {
							StrHTML = "Select Status!</font>";
						}
					}

					if (name.equals("dr_ticket_tickettype_id")) {
						// if (!value.equals("0") && !value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						String history_oldvalue = ExecuteQuery("Select coalesce(tickettype_name,'') as tickettype_name from "
								+ compdb(comp_id)
								+ "axela_service_ticket left join "
								+ compdb(comp_id)
								+ "axela_service_ticket_type on tickettype_id = ticket_tickettype_id where ticket_id="
								+ ticket_id + " ");

						StrSql = "Update " + compdb(comp_id)
								+ "axela_service_ticket" + " SET"
								+ " ticket_tickettype_id = '" + value + "'"
								+ " where ticket_id = " + ticket_id + "";
						updateQuery(StrSql);
						String history_newvalue = ExecuteQuery("Select tickettype_name from "
								+ compdb(comp_id)
								+ "axela_service_ticket_type where tickettype_id="
								+ value + " ");
						String history_actiontype = "TICKET TYPE";
						StrSql = "INSERT into " + compdb(comp_id)
								+ "axela_service_ticket_history"
								+ " (history_ticket_id," + " history_emp_id,"
								+ " history_datetime," + " history_actiontype,"
								+ " history_oldvalue," + " history_newvalue)"
								+ " values (" + " " + ticket_id + "," + " "
								+ emp_id + "," + " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "'," + " '"
								+ history_oldvalue + "'," + " '" + history_newvalue
								+ "')";
						// SOP("StrSql--"+StrSql);
						updateQuery(StrSql);
						StrHTML = "Ticket Type updated!</font>";
						// } else {
						// StrHTML = "Select Ticket Type!</font>";
						// }
					}

					if (name.equals("dr_ticket_ticketcat_id")) {
						// if (!value.equals("0") && !value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						String history_oldvalue = ExecuteQuery("Select coalesce(ticketcat_name,'') as ticketcat_name from "
								+ compdb(comp_id)
								+ "axela_service_ticket left join "
								+ compdb(comp_id)
								+ "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id where ticket_id="
								+ ticket_id + " ");
						StrSql = "Update " + compdb(comp_id)
								+ "axela_service_ticket" + " SET"
								+ " ticket_ticketcat_id = '" + value + "'"
								+ " where ticket_id = " + ticket_id + "";
						updateQuery(StrSql);
						String history_newvalue = ExecuteQuery("Select ticketcat_name from "
								+ compdb(comp_id)
								+ "axela_service_ticket_cat where ticketcat_id="
								+ value + " ");
						String history_actiontype = "TICKET CATEGORY";
						StrSql = "INSERT into " + compdb(comp_id)
								+ "axela_service_ticket_history"
								+ " (history_ticket_id," + " history_emp_id,"
								+ " history_datetime," + " history_actiontype,"
								+ " history_oldvalue," + " history_newvalue)"
								+ " values (" + " '" + ticket_id + "'," + " '"
								+ emp_id + "'," + " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "'," + " '"
								+ history_oldvalue + "'," + " '" + history_newvalue
								+ "')";
						updateQuery(StrSql);
						StrHTML = "Ticket Category updated!</font>";
						// } else {
						// StrHTML = "Select Ticket Category!</font>";
						// }
					}
				} else {
					StrHTML = "Update Permission Denied!</font>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			}
		}
	}

	public void ContactInfo() {
		StrSql = "Select contact_id, concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
				+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2"
				+ " from "
				+ compdb(comp_id)
				+ "axela_customer_contact"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_title on title_id = contact_title_id"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_service_ticket on ticket_contact_id = contact_id"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_service_ticket_dept on ticket_ticket_dept_id = ticket_dept_id"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_service_ticket_status on ticket_ticketstatus_id = ticketstatus_id"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_service_ticket_priority on ticket_priorityticket_id = priorityticket_id"
				+ " inner join "
				+ compdb(comp_id)
				+ "axela_emp on ticket_emp_id = emp_id "
				+ " where ticket_id = " + ticket_id;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_mobile2 = crs.getString("contact_mobile2");
				contact_name = crs.getString("contact_name");
				contact_id = crs.getString("contact_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "Select config_ticket_closed_email_enable, config_ticket_closed_email_sub,"
				+ " config_ticket_closed_email_format, "
				// + " config_ticket_closed_email_exe_sub, "
				// + " config_ticket_closed_email_exe_format,"
				+ " config_ticket_closed_sms_enable,"
				+ " config_ticket_closed_sms_format,"
				// + " config_ticket_closed_sms_exe_format, "
				+ " config_ticket_new_email_enable, config_ticket_new_email_exe_format, "
				+ " config_admin_email, config_email_enable, config_sms_enable, "
				+ " comp_email_enable, comp_sms_enable,"
				+ " coalesce(ticketemp.emp_email1,'') as emp_email1, coalesce(ticketemp.emp_email2,'') as emp_email2,"
				+ " coalesce(ticketemp.emp_name,'') as emp_name,"
				+ " coalesce(ticketemp.emp_mobile1,'') as emp_mobile1 , coalesce(ticketemp.emp_mobile2,'') as emp_mobile2,"
				+ " coalesce(admin.emp_email1,'') as emp_email"
				// + " coalesce(ticketnewemp.emp_email1, '') as newemp_email1,"
				// + " coalesce(ticketnewemp.emp_email2, '') as newemp_email2"
				+ " from "
				+ compdb(comp_id)
				+ "axela_config, "
				+ compdb(comp_id)
				+ "axela_comp, "
				+ compdb(comp_id)
				+ "axela_emp admin"
				+ " left join "
				+ compdb(comp_id)
				+ "axela_emp ticketemp on ticketemp.emp_id = "
				+ ticket_emp_id
				+ "" + " where 1=1 and admin.emp_id = " + emp_id + " limit 1";
		// if (name.equals("dr_ticket_emp_id")) {
		// StrSql += " left join " + compdb(comp_id) +
		// "axela_emp ticketnewemp on ticketnewemp.emp_id = " + value + "";
		// }
		try {
			SOP("StrSql==123==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_ticket_closed_email_enable = crs
						.getString("config_ticket_closed_email_enable");
				config_ticket_closed_email_sub = crs
						.getString("config_ticket_closed_email_sub");
				config_ticket_closed_email_format = crs
						.getString("config_ticket_closed_email_format");
				// config_ticket_closed_email_exe_sub = crs .getString("config_ticket_closed_email_exe_sub");
				// config_ticket_closed_email_exe_format = crs .getString("config_ticket_closed_email_exe_format");
				config_ticket_closed_sms_enable = crs
						.getString("config_ticket_closed_sms_enable");
				config_ticket_closed_sms_format = crs
						.getString("config_ticket_closed_sms_format");
				// config_ticket_closed_sms_exe_format = crs .getString("config_ticket_closed_sms_exe_format");

				config_ticket_new_email_enable = crs
						.getString("config_ticket_new_email_enable");
				config_ticket_new_email_exe_format = crs
						.getString("config_ticket_new_email_exe_format");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");

				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
				emp_email = crs.getString("emp_email");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	public void SendEmail() {
		String sub = (config_ticket_closed_email_sub);
		String msg = (config_ticket_closed_email_format);
		try {
			msg = "replace('" + msg + "','[TICKETID]',ticket_id)";
			msg = "replace(" + msg + ",'[TICKETSUBJECT]',ticket_subject)";
			msg = "replace(" + msg + ",'[TICKETSTATUS]',ticketstatus_name)";
			msg = "replace(" + msg + ",'[TICKETTIME]',ticket_report_time)";
			msg = "replace(" + msg + ",'[DUETIME]',ticket_due_time)";
			msg = "replace(" + msg + ",'[EXECUTIVENAME]',emp_name)";
			msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
			msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
			msg = "replace(" + msg + ",'[EXEMOBILE2]',emp_mobile2)";
			msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
			msg = "replace(" + msg + ",'[EXEEMAIL2]',emp_email2)";
			msg = "replace(" + msg + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
			msg = "replace(" + msg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
			msg = "replace(" + msg + ",'[DEPARTMENT]',ticket_dept_name)";
			msg = "replace(" + msg + ",'[PRIORITY]',priorityticket_name)";
			msg = "replace(" + msg + ",'[CONTACTID]',ticket_contact_id)";
			msg = "replace("
					+ msg
					+ ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
			msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
			msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
			msg = "replace(" + msg + ",'[CUSTOMERID]',ticket_customer_id)";
			msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
			msg = "replace(" + msg
					+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

			sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
			sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
			sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
			sub = "replace(" + sub + ",'[TICKETTIME]',ticket_report_time)";
			sub = "replace(" + sub + ",'[DUETIME]',ticket_due_time)";
			sub = "replace(" + sub + ",'[EXECUTIVENAME]',emp_name)";
			sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
			sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
			sub = "replace(" + sub + ",'[EXEMOBILE2]',emp_mobile2)";
			sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
			sub = "replace(" + sub + ",'[EXEEMAIL2]',emp_email2)";
			sub = "replace(" + sub + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
			sub = "replace(" + sub + ",'[REGNO]',coalesce(veh_reg_no, ''))";
			sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
			sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
			sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
			sub = "replace("
					+ sub
					+ ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
			sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
			sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
			sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
			sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
			sub = "replace(" + sub
					+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

			try {
				StrSql = "SELECT" + " "
						+ contact_id
						+ ","
						+ " '"
						+ contact_name
						+ "',"
						+ " '"
						+ emp_email1
						+ "',"
						+ " '"
						+ send_contact_email
						+ "',"
						+ " '"
						+ ticket_cc
						+ "',"
						+ " "
						+ (sub)
						+ ","
						+ " "
						+ (msg)
						+ ","
						+ " '"
						+ ToLongDate(kknow())
						+ "',"
						+ " "
						+ emp_id
						+ ","
						+ " 0"
						+ " from "
						+ compdb(comp_id)
						+ "axela_service_ticket"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_emp on emp_id = ticket_emp_id "
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_customer on customer_id = ticket_customer_id"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_customer_contact on contact_id = ticket_contact_id"
						// + " inner join "
						// + compdb(comp_id)
						// + "axela_title on title_id = contact_title_id"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id"
						+ " inner join "
						+ compdb(comp_id)
						+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
						+ " left join "
						+ compdb(comp_id)
						+ "axela_service_veh on veh_id = ticket_veh_id"

						+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " left join "
						+ compdb(comp_id)
						+ "axela_service_ticket_trans on tickettrans_ticket_id = ticket_id"
						+ " where ticket_id = " + ticket_id + " limit 1";
				StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
						+ "(email_contact_id," + " email_contact,"
						+ " email_from," + " email_to," + " email_cc,"
						+ " email_subject," + " email_msg," + " email_date,"
						+ " email_entry_id," + " email_sent)" + " " + StrSql
						+ "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void SendEmailToExecutive() throws SQLException {
		String emailmsg, sub;
		emailmsg = "";// (config_ticket_closed_email_exe_format);

		sub = "";// (config_ticket_closed_email_exe_sub);

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg
				+ ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg
				+ ",'[TICKETTIME]',ticket_report_time)";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',ticket_due_time)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETOWNER]',o.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXECUTIVENAME]',s.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',s.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',s.emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',s.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',s.emp_email2)";
		emailmsg = "replace(" + emailmsg
				+ ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		emailmsg = "replace(" + emailmsg
				+ ",'[REGNO]',coalesce(veh_reg_no, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace("
				+ emailmsg
				+ ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg
				+ ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg
				+ ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg
				+ ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg
				+ ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		emailmsg = "replace(" + emailmsg
				+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',ticket_report_time)";
		sub = "replace(" + sub + ",'[DUETIME]',ticket_due_time)";
		sub = "replace(" + sub + ",'[TICKETOWNER]',o.emp_name)";
		sub = "replace(" + sub + ",'[EXECUTIVENAME]',s.emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',s.emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEMOBILE2]',s.emp_mobile2)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',s.emp_email1)";
		sub = "replace(" + sub + ",'[EXEEMAIL2]',s.emp_email2)";
		sub = "replace(" + sub + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		sub = "replace(" + sub + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace("
				+ sub
				+ ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub
				+ ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		sub = "replace(" + sub
				+ ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub
				+ ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		sub = "replace(" + sub
				+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT" + " "
					+ emp_id
					+ ","
					+ " s.emp_name,"
					+ " '"
					+ emp_email
					+ "',"
					+ " '"
					+ emp_email_formail
					+ "',"
					+ " '"
					+ ticket_cc
					+ "',"
					+ " "
					+ unescapehtml(sub)
					+ ","
					+ " "
					+ unescapehtml(emailmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "', "
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " from "
					+ compdb(comp_id)
					+ "axela_service_ticket"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp o on o.emp_id = ticket_emp_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp s on s.emp_id = "
					+ emp_id
					+ ""
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_jobtitle on jobtitle_id = s.emp_jobtitle_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer on customer_id = ticket_customer_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer_contact on contact_id = ticket_contact_id "
					// + " left join "
					// + compdb(comp_id)
					// + "axela_title on title_id = contact_title_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_ticket_trans on tickettrans_ticket_id = ticket_id"
					+ " where ticket_id=" + ticket_id
					+ " order by tickettrans_id desc" + " limit 1";
			// SOP("StrSqlselect-emaile-"+StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_emp_id," + " email_emp," + " email_from,"
					+ " email_to," + " email_cc," + " email_subject,"
					+ " email_msg," + " email_date," + " email_sent,"
					+ " email_entry_id)" + " " + StrSql + "";
			// SOP("StrSqlinsert-emaile-" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void SendSMS(String contact_mobile) {
		String smsmsg = config_ticket_closed_sms_format;

		smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',ticket_report_time)";
		smsmsg = "replace(" + smsmsg + ",'[DUETIME]',ticket_due_time)";
		smsmsg = "replace(" + smsmsg + ",'[EXECUTIVENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',emp_mobile2)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',emp_email2)";
		smsmsg = "replace(" + smsmsg
				+ ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		smsmsg = "replace(" + smsmsg + ",'[PRIORITY]',priorityticket_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)";
		smsmsg = "replace("
				+ smsmsg
				+ ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg
				+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT " + ""
					+ contact_id
					+ ", "
					+ " '"
					+ contact_name
					+ "', "
					+ " '"
					+ contact_mobile
					+ "', "
					+ " "
					+ (smsmsg)
					+ ", "
					+ " '"
					+ ToLongDate(kknow())
					+ "', "
					+ " 0, "
					+ " "
					+ emp_id
					+ " "
					+ " from "
					+ compdb(comp_id)
					+ "axela_service_ticket "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp on emp_id = ticket_emp_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_customer on customer_id = ticket_customer_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_customer_contact on contact_id = ticket_contact_id "
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_title on title_id = contact_title_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id" + " left join "
					+ compdb(comp_id)
					+ "axela_service_ticket_trans on tickettrans_ticket_id = ticket_id"
					+ " where ticket_id=" + ticket_id + " limit 1";
			// SOP("StrSql--"+StrSql);
			StrSql = "insert into " + compdb(comp_id) + "axela_sms" + "("
					+ "sms_contact_id," + "sms_contact," + "sms_mobileno,"
					+ "sms_msg," + "sms_date ," + "sms_sent ,"
					+ "sms_entry_id)" + " " + StrSql + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void SendSMSToExecutive(String emp_mobile) throws SQLException {
		String smsmsg = "";// config_ticket_closed_sms_exe_format;
		smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',ticket_report_time)";
		smsmsg = "replace(" + smsmsg + ",'[DUETIME]',ticket_due_time)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETOWNER]',o.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXECUTIVENAME]',s.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',s.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',s.emp_mobile2)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',s.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',s.emp_email2)";
		smsmsg = "replace(" + smsmsg
				+ ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		smsmsg = "replace(" + smsmsg + ",'[PRIORITY]',priorityticket_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)";
		smsmsg = "replace("
				+ smsmsg
				+ ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		smsmsg = "replace(" + smsmsg
				+ ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		smsmsg = "replace(" + smsmsg
				+ ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		smsmsg = "replace(" + smsmsg
				+ ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		smsmsg = "replace(" + smsmsg
				+ ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT" + " "
					+ emp_id
					+ ","
					+ " s.emp_name,"
					+ " '"
					+ emp_mobile
					+ "',"
					+ " "
					+ (smsmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "',"
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " from "
					+ compdb(comp_id)
					+ "axela_service_ticket "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp o on o.emp_id = ticket_emp_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp s on s.emp_id = "
					+ emp_id
					+ ""
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_jobtitle on jobtitle_id = s.emp_jobtitle_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer on customer_id = ticket_customer_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer_contact on contact_id = ticket_contact_id "
					// + " left join "
					// + compdb(comp_id)
					// + "axela_title on title_id = contact_title_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_ticket_trans on tickettrans_ticket_id = ticket_id"
					+ " where ticket_id=" + ticket_id
					+ " order by tickettrans_id desc" + " limit 1";
			// SOP("StrSql-smse-"+StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_emp_id," + " sms_emp," + " sms_mobileno,"
					+ " sms_msg," + " sms_date," + " sms_sent,"
					+ " sms_entry_id)" + " " + StrSql + "";
			// SOP("StrSql-smse-"+StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	// ////////////onchange of executive///////////////////////
	protected void SendEmailToExecutive1() throws SQLException {
		String emailmsg;
		emailmsg = config_ticket_new_email_exe_format;
		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',ticket_report_time)";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',ticket_due_time)";
		emailmsg = "replace(" + emailmsg + ",'[EXECUTIVENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',emp_email2)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT" + " "
					+ emp_id
					+ ","
					+ " '"
					+ emp_name
					+ "',"
					+ " '"
					+ emp_email1
					+ "',"
					+ " '"
					+ emp_email_formail
					+ "',"
					+ " '"
					+ ticket_cc
					+ "',"
					+ " 'Ticket ID: "
					+ ticket_id
					+ " Re-Assigned',"
					+ " "
					+ unescapehtml(emailmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "', "
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " from "
					+ compdb(comp_id)
					+ "axela_service_ticket"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_emp on emp_id = ticket_emp_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer on customer_id = ticket_customer_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_customer_contact on contact_id = ticket_contact_id "
					// + " left join "
					// + compdb(comp_id)
					// + "axela_title on title_id = contact_title_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " left join " + compdb(comp_id) + "axela_service_ticket_trans on tickettrans_ticket_id = ticket_id"
					+ " where ticket_id=" + ticket_id + " limit 1";
			// SOP("StrSqlselect-emaile-"+StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_emp_id," + " email_emp," + " email_from,"
					+ " email_to," + " email_cc," + " email_subject,"
					+ " email_msg," + " email_date," + " email_sent,"
					+ " email_entry_id)" + " " + StrSql + "";
			// SOP("StrSqlinsert-emaile----------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
