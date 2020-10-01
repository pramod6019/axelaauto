//Bhagwan Singh 09/01/2013
//Ved Prakash 13 March 2013
package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTicketDepartment_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String emp_id = "0";
	public String ticket_dept_id = "0";
	public String ticket_dept_name = "";
	public String ticket_dept_desc = "";
	public String ticket_dept_duehrs = "";
	public String ticket_dept_rank = "";
	public String ticket_dept_business_hrs = "0";
	public String ticket_dept_trigger1_hrs = "0";
	public String ticket_dept_trigger2_hrs = "0";
	public String ticket_dept_trigger3_hrs = "0";
	public String ticket_dept_trigger4_hrs = "0";
	public String ticket_dept_trigger5_hrs = "0";
	public String start_time = "00:00";
	public String end_time = "00:00";
	public String starttime = "";
	public String endtime = "";
	public String start = "";
	public String end = "";
	public String ticket_dept_sun = "0";
	public String ticket_dept_mon = "0";
	public String ticket_dept_tue = "0";
	public String ticket_dept_wed = "0";
	public String ticket_dept_thu = "0";
	public String ticket_dept_fri = "0";
	public String ticket_dept_sat = "0";
	public String ticket_dept_entry_id = "0";
	public String ticket_dept_entry_date = "";
	public String ticket_dept_modified_id = "0";
	public String ticket_dept_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String QueryString = "";
	public int count = 0;
	public DecimalFormat deci = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ticket_dept_id = CNumeric(PadQuotes(request.getParameter("ticket_dept_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						ticket_dept_entry_id = emp_id;
						ticket_dept_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketdept.jsp?msg=Department Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Department".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Department".equals(deleteB)) {
						GetValues(request, response);
						ticket_dept_modified_id = emp_id;
						ticket_dept_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketdept.jsp?msg=Department Updated Successfully!" + msg + ""));
						}
					}
					if ("Delete Department".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketdept.jsp?msg=Department Deleted Successfully!"));
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ticket_dept_id = CNumeric(PadQuotes(request.getParameter("ticket_dept_id")));
		ticket_dept_name = PadQuotes(request.getParameter("txt_ticket_dept_name"));
		ticket_dept_desc = PadQuotes(request.getParameter("txt_ticket_dept_desc"));
		ticket_dept_duehrs = PadQuotes(request.getParameter("txt_ticket_dept_duehrs"));

		ticket_dept_trigger1_hrs = PadQuotes(request.getParameter("txt_ticket_dept_trigger1_hrs"));
		ticket_dept_trigger2_hrs = PadQuotes(request.getParameter("txt_ticket_dept_trigger2_hrs"));
		ticket_dept_trigger3_hrs = PadQuotes(request.getParameter("txt_ticket_dept_trigger3_hrs"));
		ticket_dept_trigger4_hrs = PadQuotes(request.getParameter("txt_ticket_dept_trigger4_hrs"));
		ticket_dept_trigger5_hrs = PadQuotes(request.getParameter("txt_ticket_dept_trigger5_hrs"));
		if (ticket_dept_trigger1_hrs.equals("")) {
			ticket_dept_trigger1_hrs = "0";
		}
		if (ticket_dept_trigger2_hrs.equals("")) {
			ticket_dept_trigger2_hrs = "0";
		}
		if (ticket_dept_trigger3_hrs.equals("")) {
			ticket_dept_trigger3_hrs = "0";
		}
		if (ticket_dept_trigger4_hrs.equals("")) {
			ticket_dept_trigger4_hrs = "0";
		}
		if (ticket_dept_trigger5_hrs.equals("")) {
			ticket_dept_trigger5_hrs = "0";
		}

		ticket_dept_business_hrs = PadQuotes(request.getParameter("chk_ticket_dept_businesshrs"));
		if (ticket_dept_business_hrs.equals("on")) {
			start_time = PadQuotes(request.getParameter("txt_start_time"));
			if (start_time.contains(":") && !start_time.equals("")) {
				starttime = start_time.replace(':', '.');
			}

			end_time = PadQuotes(request.getParameter("txt_end_time"));
			if (end_time.contains(":") && !end_time.equals("")) {
				endtime = end_time.replace(':', '.');
			}
			ticket_dept_sun = PadQuotes(request.getParameter("chk_ticket_dept1"));
			ticket_dept_mon = PadQuotes(request.getParameter("chk_ticket_dept2"));
			ticket_dept_tue = PadQuotes(request.getParameter("chk_ticket_dept3"));
			ticket_dept_wed = PadQuotes(request.getParameter("chk_ticket_dept4"));
			ticket_dept_thu = PadQuotes(request.getParameter("chk_ticket_dept5"));
			ticket_dept_fri = PadQuotes(request.getParameter("chk_ticket_dept6"));
			ticket_dept_sat = PadQuotes(request.getParameter("chk_ticket_dept7"));
		} else {
			starttime = "00.00";
			endtime = "00.00";
			ticket_dept_sun = "0";
			ticket_dept_mon = "0";
			ticket_dept_tue = "0";
			ticket_dept_wed = "0";
			ticket_dept_thu = "0";
			ticket_dept_fri = "0";
			ticket_dept_sat = "0";
		}
		if (ticket_dept_business_hrs.equals("on")) {
			ticket_dept_business_hrs = "1";
		} else {
			ticket_dept_business_hrs = "0";
		}
		if (ticket_dept_sun.equals("on")) {
			ticket_dept_sun = "1";
		} else {
			ticket_dept_sun = "0";
		}
		if (ticket_dept_mon.equals("on")) {
			ticket_dept_mon = "1";
		} else {
			ticket_dept_mon = "0";
		}
		if (ticket_dept_tue.equals("on")) {
			ticket_dept_tue = "1";
		} else {
			ticket_dept_tue = "0";
		}
		if (ticket_dept_wed.equals("on")) {
			ticket_dept_wed = "1";
		} else {
			ticket_dept_wed = "0";
		}
		if (ticket_dept_thu.equals("on")) {
			ticket_dept_thu = "1";
		} else {
			ticket_dept_thu = "0";
		}
		if (ticket_dept_fri.equals("on")) {
			ticket_dept_fri = "1";
		} else {
			ticket_dept_fri = "0";
		}
		if (ticket_dept_sat.equals("on")) {
			ticket_dept_sat = "1";
		} else {
			ticket_dept_sat = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (ticket_dept_name.equals("")) {
			msg = "<br>Enter Department Name!";
		} else {
			try {
				if (!ticket_dept_name.equals("")) {
					StrSql = "select ticket_dept_name from " + compdb(comp_id) + "axela_service_ticket_dept"
							+ " where ticket_dept_name = '" + ticket_dept_name + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " and ticket_dept_id != " + ticket_dept_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Department Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (ticket_dept_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (ticket_dept_duehrs.equals("")) {
			msg = msg + "<br>Enter Due Hours!";
		} else {
			String hrs = null;
			if (!isNumeric(ticket_dept_duehrs)) {
				msg = msg + "<br>Due Hours must be Numeric!";
			} else {
				ticket_dept_duehrs = deci.format(Double.parseDouble(ticket_dept_duehrs));
				// SOP("ticket_dept_duehrs = " + ticket_dept_duehrs);
				if (ticket_dept_duehrs.equals("0.00")) {
					msg = msg + "<br>Due Hours should be greater than zero!";
				}
			}
		}
		// if (ticket_dept_duehrs.equals("")) {
		// msg = msg + "<br>Enter Due Hours!";
		// } else if (!isNumeric(ticket_dept_duehrs)) {
		// msg = msg + "<br>Due Hours must be Numeric!";
		// } else if (ticket_dept_duehrs.equals("0")) {
		// msg = msg + "<br>Due Hours should be greater than zero!";
		// }

		if ((!ticket_dept_trigger5_hrs.equals("0"))) {
			if (Double.parseDouble(ticket_dept_trigger5_hrs) < Double.parseDouble(ticket_dept_trigger4_hrs)
					|| Double.parseDouble(ticket_dept_trigger5_hrs) < Double.parseDouble(ticket_dept_trigger3_hrs)
					|| Double.parseDouble(ticket_dept_trigger5_hrs) < Double.parseDouble(ticket_dept_trigger2_hrs)
					|| Double.parseDouble(ticket_dept_trigger5_hrs) < Double.parseDouble(ticket_dept_trigger1_hrs)) {
				msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
			}
		}
		if (!ticket_dept_trigger4_hrs.equals("0")) {
			if (Double.parseDouble(ticket_dept_trigger4_hrs) < Double.parseDouble(ticket_dept_trigger3_hrs)
					|| Double.parseDouble(ticket_dept_trigger4_hrs) < Double.parseDouble(ticket_dept_trigger2_hrs)
					|| Double.parseDouble(ticket_dept_trigger4_hrs) < Double.parseDouble(ticket_dept_trigger1_hrs)) {
				msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
			}
		}
		if (!ticket_dept_trigger3_hrs.equals("0")) {
			if (Double.parseDouble(ticket_dept_trigger3_hrs) < Double.parseDouble(ticket_dept_trigger2_hrs)
					|| Double.parseDouble(ticket_dept_trigger3_hrs) < Double.parseDouble(ticket_dept_trigger1_hrs)) {
				msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
			}
		}
		if (!ticket_dept_trigger2_hrs.equals("0")) {
			if (Double.parseDouble(ticket_dept_trigger2_hrs) < Double.parseDouble(ticket_dept_trigger1_hrs)) {
				msg = msg + "<br> Level-2 hours should be more than previous Level hours";
			}
		}
		if (ticket_dept_business_hrs.equals("1")) {
			if (starttime.equals("") && start_time.equals("")) {
				msg = msg + "<br>Enter Start Time!";
			} else if (start_time.equals("00:00") || start_time.equals("0:00") || start_time.equals("0")) {
				msg = msg + "<br>Start Time should be greater than Zero!";
			} else if (isNumeric(start_time)) {
				if (Integer.parseInt(start_time) == 0) {
					msg = msg + "<br>Start Time should be greater than Zero!";
				}
			}

			if (endtime.equals("") && end_time.equals("")) {
				msg = msg + "<br>Enter End Time!";
			} else if (end_time.equals("00:00") || end_time.equals("0:00") || end_time.equals("0")) {
				msg = msg + "<br>End Time should be greater than Zero!";
			} else if (isNumeric(end_time)) {
				if (Integer.parseInt(end_time) == 0) {
					msg = msg + "<br>End Time should be greater than Zero!";
				}
			}
			// SOP("starttime = " + starttime);
			// SOP("start_time = " + start_time);
			// SOP("endtime = " + endtime);
			// SOP("end_time = " + end_time);
			if (!starttime.equals("") && !start_time.equals("") && !endtime.equals("") && !end_time.equals("")) {
				if (Double.parseDouble(endtime) <= Double.parseDouble(starttime)) {
					msg = msg + "<br>End Time Should be greater than Start Time!";
				}
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				ticket_dept_id = ExecuteQuery("Select (coalesce(max(ticket_dept_id),0)+1) from " + compdb(comp_id) + "axela_service_ticket_dept");
				StrSql = "Insert into " + compdb(comp_id) + "axela_service_ticket_dept"
						+ " (ticket_dept_id,"
						+ " ticket_dept_name,"
						+ " ticket_dept_desc,"
						+ " ticket_dept_duehrs,"
						+ " ticket_dept_rank,"
						+ " ticket_dept_trigger1_hrs,"
						+ " ticket_dept_trigger2_hrs,"
						+ " ticket_dept_trigger3_hrs,"
						+ " ticket_dept_trigger4_hrs,"
						+ " ticket_dept_trigger5_hrs,";
				if (ticket_dept_business_hrs.equals("1")) {
					StrSql = StrSql + "ticket_dept_business_hrs,"
							+ " ticket_dept_starttime,"
							+ " ticket_dept_endtime,"
							+ " ticket_dept_sun,"
							+ " ticket_dept_mon,"
							+ " ticket_dept_tue,"
							+ " ticket_dept_wed,"
							+ " ticket_dept_thu,"
							+ " ticket_dept_fri,"
							+ " ticket_dept_sat,";
				}
				StrSql = StrSql + " ticket_dept_entry_id,"
						+ " ticket_dept_entry_date)"
						+ " values"
						+ " (" + ticket_dept_id + ","
						+ " '" + ticket_dept_name + "',"
						+ " '" + ticket_dept_desc + "',"
						+ " '" + ticket_dept_duehrs + "',"
						+ " (Select (coalesce(max(ticket_dept_rank),0)+1) from " + compdb(comp_id) + "axela_service_ticket_dept as Rank where 1=1),"
						+ " '" + ticket_dept_trigger1_hrs + "',"
						+ " '" + ticket_dept_trigger2_hrs + "',"
						+ " '" + ticket_dept_trigger3_hrs + "',"
						+ " '" + ticket_dept_trigger4_hrs + "',"
						+ " '" + ticket_dept_trigger5_hrs + "',";
				if (ticket_dept_business_hrs.equals("1")) {
					StrSql = StrSql
							+ " '" + ticket_dept_business_hrs + "',"
							+ " '" + starttime + "',"
							+ " '" + endtime + "',"
							+ " '" + ticket_dept_sun + "',"
							+ " '" + ticket_dept_mon + "',"
							+ " '" + ticket_dept_tue + "',"
							+ " '" + ticket_dept_wed + "',"
							+ " '" + ticket_dept_thu + "',"
							+ " '" + ticket_dept_fri + "',"
							+ " '" + ticket_dept_sat + "',";
				}
				StrSql = StrSql
						+ " " + ticket_dept_entry_id + ","
						+ " '" + ticket_dept_entry_date + "')";
				// SOP(StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select ticket_dept_name, ticket_dept_desc, ticket_dept_duehrs, ticket_dept_trigger1_hrs,"
					+ " ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
					+ " ticket_dept_trigger5_hrs, ticket_dept_business_hrs, ticket_dept_starttime,"
					+ " ticket_dept_endtime, ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu,"
					+ " ticket_dept_fri, ticket_dept_sat, ticket_dept_entry_id, ticket_dept_entry_date,"
					+ " ticket_dept_modified_id, ticket_dept_modified_date"
					+ " from " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " where  ticket_dept_id = " + ticket_dept_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_dept_name = crs.getString("ticket_dept_name");
					ticket_dept_desc = crs.getString("ticket_dept_desc");
					ticket_dept_duehrs = crs.getString("ticket_dept_duehrs");
					ticket_dept_trigger1_hrs = crs.getString("ticket_dept_trigger1_hrs");
					ticket_dept_trigger2_hrs = crs.getString("ticket_dept_trigger2_hrs");
					ticket_dept_trigger3_hrs = crs.getString("ticket_dept_trigger3_hrs");
					ticket_dept_trigger4_hrs = crs.getString("ticket_dept_trigger4_hrs");
					ticket_dept_trigger5_hrs = crs.getString("ticket_dept_trigger5_hrs");
					ticket_dept_business_hrs = crs.getString("ticket_dept_business_hrs");
					starttime = crs.getString("ticket_dept_starttime");
					start_time = starttime.replace('.', ':');
					endtime = crs.getString("ticket_dept_endtime");
					end_time = endtime.replace('.', ':');
					ticket_dept_sun = crs.getString("ticket_dept_sun");
					ticket_dept_mon = crs.getString("ticket_dept_mon");
					ticket_dept_tue = crs.getString("ticket_dept_tue");
					ticket_dept_wed = crs.getString("ticket_dept_wed");
					ticket_dept_thu = crs.getString("ticket_dept_thu");
					ticket_dept_fri = crs.getString("ticket_dept_fri");
					ticket_dept_sat = crs.getString("ticket_dept_sat");
					ticket_dept_entry_id = crs.getString("ticket_dept_entry_id");
					if (!ticket_dept_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(ticket_dept_entry_id));
						entry_date = strToLongDate(crs.getString("ticket_dept_entry_date"));
					}
					ticket_dept_modified_id = crs.getString("ticket_dept_modified_id");
					if (!ticket_dept_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(ticket_dept_modified_id));
						modified_date = strToLongDate(crs.getString("ticket_dept_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Department!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_dept"
						+ " SET"
						+ " ticket_dept_name = '" + ticket_dept_name + "',"
						+ " ticket_dept_desc = '" + ticket_dept_desc + "',"
						+ " ticket_dept_duehrs = '" + ticket_dept_duehrs + "',"
						+ " ticket_dept_trigger1_hrs = '" + ticket_dept_trigger1_hrs + "',"
						+ " ticket_dept_trigger2_hrs = '" + ticket_dept_trigger2_hrs + "',"
						+ " ticket_dept_trigger3_hrs = '" + ticket_dept_trigger3_hrs + "',"
						+ " ticket_dept_trigger4_hrs = '" + ticket_dept_trigger4_hrs + "',"
						+ " ticket_dept_trigger5_hrs = '" + ticket_dept_trigger5_hrs + "',"
						+ " ticket_dept_business_hrs = '" + ticket_dept_business_hrs + "',"
						+ " ticket_dept_starttime = '" + starttime + "',"
						+ " ticket_dept_endtime = '" + endtime + "',"
						+ " ticket_dept_sun = '" + ticket_dept_sun + "',"
						+ " ticket_dept_mon = '" + ticket_dept_mon + "',"
						+ " ticket_dept_tue = '" + ticket_dept_tue + "',"
						+ " ticket_dept_wed = '" + ticket_dept_wed + "',"
						+ " ticket_dept_thu = '" + ticket_dept_thu + "',"
						+ " ticket_dept_fri = '" + ticket_dept_fri + "',"
						+ " ticket_dept_sat = '" + ticket_dept_sat + "',"
						+ " ticket_dept_modified_id = " + ticket_dept_modified_id + ","
						+ " ticket_dept_modified_date = '" + ticket_dept_modified_date + "'"
						+ " where ticket_dept_id = " + ticket_dept_id + "";
				// SOP(StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT ticket_ticket_dept_id FROM " + compdb(comp_id) + "axela_service_ticket where ticket_ticket_dept_id = " + ticket_dept_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Department is Associated with Ticket!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_dept where ticket_dept_id = " + ticket_dept_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
