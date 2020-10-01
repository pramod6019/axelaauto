package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageEnquiryPriority_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String priorityenquiry_id = "0";
	public String priorityenquiry_name = "";
	public String priorityenquiry_desc = "";
	public String priorityenquiry_color = "";
	public String priorityenquiry_metcount = "0";
	public String priorityenquiry_testdrivecount = "0";
	public String priorityenquiry_quotecount = "0";
	public String priorityenquiry_homevisitcount = "0";
	public String priorityenquiry_closedays = "0";
	public String priorityenquiry_optioncount = "0";
	public String priorityenquiry_duehrs = "";
	public String priorityenquiry_rank = "";
	public String duehrs = "";
	public String trigger1_hrs = "0";
	public String trigger2_hrs = "0";
	public String trigger3_hrs = "0";
	public String trigger4_hrs = "0";
	public String trigger5_hrs = "0";
	public String priorityenquiry_trigger1_hrs = "0:00";
	public String priorityenquiry_trigger2_hrs = "0:00";
	public String priorityenquiry_trigger3_hrs = "0:00";
	public String priorityenquiry_trigger4_hrs = "0:00";
	public String priorityenquiry_trigger5_hrs = "0:00";
	public String priorityenquiry_entry_id = "0";
	public String priorityenquiry_entry_date = "";
	public String priorityenquiry_modified_id = "0";
	public String priorityenquiry_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				priorityenquiry_id = CNumeric(PadQuotes(request.getParameter("priorityenquiry_id")));

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						priorityenquiry_name = "";
						priorityenquiry_desc = "";
						priorityenquiry_trigger1_hrs = "";
						priorityenquiry_trigger2_hrs = "";
						priorityenquiry_trigger3_hrs = "";
						priorityenquiry_trigger4_hrs = "";
						priorityenquiry_trigger5_hrs = "";
						priorityenquiry_duehrs = "";
					} else {
						GetValues(request, response);
						priorityenquiry_entry_id = emp_id;
						priorityenquiry_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageenquirypriority.jsp?priorityenquiry_id=" + priorityenquiry_id + "&msg=Enquiry Priority Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Priority".equals(deleteB)) {
						PopulateFields(response);

					} else if ("yes".equals(updateB) && !"Delete Priority".equals(deleteB)) {
						GetValues(request, response);
						priorityenquiry_modified_id = emp_id;
						priorityenquiry_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageenquirypriority.jsp?msg=Priority Updated Successfully!"));
						}
					} else if ("Delete Priority".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageenquirypriority.jsp?msg=Priority Deleted Successfully!"));
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
		priorityenquiry_name = PadQuotes(request.getParameter("txt_priorityenquiry_name"));
		priorityenquiry_desc = PadQuotes(request.getParameter("txt_priorityenquiry_desc"));
		priorityenquiry_color = PadQuotes(request.getParameter("txt_priorityenquiry_color"));
		priorityenquiry_metcount = CNumeric(PadQuotes(request.getParameter("txt_priorityenquiry_metcount")));
		priorityenquiry_testdrivecount = CNumeric(PadQuotes(request.getParameter("txt_priorityenquiry_testdrivecount")));
		priorityenquiry_quotecount = CNumeric(PadQuotes(request.getParameter("txt_priorityenquiry_quotecount")));
		priorityenquiry_homevisitcount = CNumeric(PadQuotes(request.getParameter("txt_priorityenquiry_homevisitcount")));
		priorityenquiry_closedays = CNumeric(PadQuotes(request.getParameter("txt_priorityenquiry_closedays")));
		priorityenquiry_optioncount = CNumeric(PadQuotes(request.getParameter("dr_priorityenquiry_optioncount")));
		priorityenquiry_duehrs = PadQuotes(request.getParameter("txt_priorityenquiry_duehrs"));
		priorityenquiry_trigger1_hrs = PadQuotes(request.getParameter("txt_priorityenquiry_trigger1_hrs"));
		priorityenquiry_trigger2_hrs = PadQuotes(request.getParameter("txt_priorityenquiry_trigger2_hrs"));
		priorityenquiry_trigger3_hrs = PadQuotes(request.getParameter("txt_priorityenquiry_trigger3_hrs"));
		// SOP("Trigger 3---check--" + trigger3_hrs);
		priorityenquiry_trigger4_hrs = PadQuotes(request.getParameter("txt_priorityenquiry_trigger4_hrs"));
		priorityenquiry_trigger5_hrs = PadQuotes(request.getParameter("txt_priorityenquiry_trigger5_hrs"));
		if (priorityenquiry_trigger1_hrs.equals("")) {
			priorityenquiry_trigger1_hrs = "0:00";
		}
		if (priorityenquiry_trigger2_hrs.equals("")) {
			priorityenquiry_trigger2_hrs = "0:00";
		}
		if (priorityenquiry_trigger3_hrs.equals("")) {
			priorityenquiry_trigger3_hrs = "0:00";
		}
		if (priorityenquiry_trigger4_hrs.equals("")) {
			priorityenquiry_trigger4_hrs = "0:00";
		}
		if (priorityenquiry_trigger5_hrs.equals("")) {
			priorityenquiry_trigger5_hrs = "0:00";
		}
		duehrs = priorityenquiry_duehrs;
		trigger1_hrs = priorityenquiry_trigger1_hrs;
		trigger2_hrs = priorityenquiry_trigger2_hrs;
		// SOP("Trigger 3---check1--" + trigger3_hrs);
		trigger3_hrs = priorityenquiry_trigger3_hrs;
		// SOP("Trigger 3---check2--" + trigger3_hrs);
		trigger4_hrs = priorityenquiry_trigger4_hrs;
		trigger5_hrs = priorityenquiry_trigger5_hrs;
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {

		msg = "";
		if (priorityenquiry_name.equals("")) {
			msg = "<br>Enter Priority!";
		} else {
			try {
				if (!priorityenquiry_name.equals("")) {
					StrSql = "SELECT priorityenquiry_name FROM " + compdb(comp_id) + "axela_sales_enquiry_priority WHERE priorityenquiry_name = '" + priorityenquiry_name + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND priorityenquiry_id != " + priorityenquiry_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Priority Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (priorityenquiry_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (priorityenquiry_color.equals(""))
		{
			msg = msg + "<br>Enter Color!";
		}
		if (priorityenquiry_duehrs.equals("")) {
			msg = msg + "<br>Enter Due Hours!";
		}
		// } else if (!isNumeric(priorityenquiry_duehrs)) {
		// msg = msg + "<br>Due Hours must be Numeric!";
		// }
		if (!priorityenquiry_trigger5_hrs.equals("0") && !priorityenquiry_trigger5_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityenquiry_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger4_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
			}
		}
		if (!priorityenquiry_trigger4_hrs.equals("0") && !priorityenquiry_trigger4_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityenquiry_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
			}
		}
		if (!priorityenquiry_trigger3_hrs.equals("0") && !priorityenquiry_trigger3_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityenquiry_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityenquiry_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
			}
		}
		if (!priorityenquiry_trigger2_hrs.equals("0") && !priorityenquiry_trigger2_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityenquiry_trigger2_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityenquiry_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-2 hours should be more than previous Level hours";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				priorityenquiry_id = ExecuteQuery("SELECT (COALESCE(MAX(priorityenquiry_id),0)+1) FROM " + compdb(comp_id) + "axela_sales_enquiry_priority");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_priority"
						+ " (priorityenquiry_id,"
						+ " priorityenquiry_name,"
						+ " priorityenquiry_desc,"
						+ " priorityenquiry_color,"
						+ " priorityenquiry_metcount,"
						+ " priorityenquiry_testdrivecount,"
						+ " priorityenquiry_quotecount,"
						+ " priorityenquiry_homevisitcount,"
						+ " priorityenquiry_closedays,"
						+ " priorityenquiry_optioncount,"
						+ " priorityenquiry_duehrs,"
						+ " priorityenquiry_rank,"
						+ " priorityenquiry_trigger1_hrs,"
						+ " priorityenquiry_trigger2_hrs,"
						+ " priorityenquiry_trigger3_hrs,"
						+ " priorityenquiry_trigger4_hrs,"
						+ " priorityenquiry_trigger5_hrs,"
						+ " priorityenquiry_entry_id,"
						+ " priorityenquiry_entry_date)"
						+ " VALUES"
						+ " (" + priorityenquiry_id + ","
						+ " '" + priorityenquiry_name + "',"
						+ " '" + priorityenquiry_desc + "',"
						+ " '" + priorityenquiry_color + "',"
						+ " " + priorityenquiry_metcount + ","
						+ " " + priorityenquiry_testdrivecount + ","
						+ " " + priorityenquiry_quotecount + ","
						+ " " + priorityenquiry_homevisitcount + ","
						+ " " + priorityenquiry_closedays + ","
						+ " " + priorityenquiry_optioncount + ","
						+ " '" + duehrs + "',"
						+ " (SELECT (COALESCE(MAX(priorityenquiry_rank),0)+1) FROM " + compdb(comp_id) + "axela_sales_enquiry_priority AS Rank WHERE 1 = 1 ),"
						+ " '" + trigger1_hrs + "',"
						+ " '" + trigger2_hrs + "',"
						+ " '" + trigger3_hrs + "',"
						+ " '" + trigger4_hrs + "',"
						+ " '" + trigger5_hrs + "',"
						+ " " + priorityenquiry_entry_id + ","
						+ " '" + priorityenquiry_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT priorityenquiry_name, priorityenquiry_desc, priorityenquiry_color, priorityenquiry_metcount,"
					+ " priorityenquiry_homevisitcount, priorityenquiry_closedays, priorityenquiry_optioncount,"
					+ " priorityenquiry_testdrivecount, priorityenquiry_quotecount, priorityenquiry_duehrs, priorityenquiry_trigger1_hrs,"
					+ " priorityenquiry_trigger2_hrs, priorityenquiry_trigger3_hrs, priorityenquiry_trigger4_hrs, priorityenquiry_trigger5_hrs,"
					+ " priorityenquiry_entry_id, priorityenquiry_modified_id, priorityenquiry_entry_date, priorityenquiry_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " WHERE priorityenquiry_id = " + priorityenquiry_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					priorityenquiry_name = crs.getString("priorityenquiry_name");
					priorityenquiry_desc = crs.getString("priorityenquiry_desc");
					priorityenquiry_color = crs.getString("priorityenquiry_color");
					priorityenquiry_metcount = crs.getString("priorityenquiry_metcount");
					priorityenquiry_testdrivecount = crs.getString("priorityenquiry_testdrivecount");
					priorityenquiry_quotecount = crs.getString("priorityenquiry_quotecount");
					priorityenquiry_homevisitcount = crs.getString("priorityenquiry_homevisitcount");
					priorityenquiry_closedays = crs.getString("priorityenquiry_closedays");
					priorityenquiry_optioncount = crs.getString("priorityenquiry_optioncount");
					priorityenquiry_duehrs = crs.getString("priorityenquiry_duehrs");
					priorityenquiry_trigger1_hrs = crs.getString("priorityenquiry_trigger1_hrs");
					priorityenquiry_trigger2_hrs = crs.getString("priorityenquiry_trigger2_hrs");
					priorityenquiry_trigger3_hrs = crs.getString("priorityenquiry_trigger3_hrs");
					priorityenquiry_trigger4_hrs = crs.getString("priorityenquiry_trigger4_hrs");
					priorityenquiry_trigger5_hrs = crs.getString("priorityenquiry_trigger5_hrs");
					priorityenquiry_entry_id = crs.getString("priorityenquiry_entry_id");
					if (!priorityenquiry_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(priorityenquiry_entry_id));
						entry_date = strToLongDate(crs.getString("priorityenquiry_entry_date"));
					}
					priorityenquiry_modified_id = crs.getString("priorityenquiry_modified_id");
					if (!priorityenquiry_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(priorityenquiry_modified_id));
						modified_date = strToLongDate(crs.getString("priorityenquiry_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry Priority!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_priority"
						+ " SET"
						+ " priorityenquiry_name = '" + priorityenquiry_name + "',"
						+ " priorityenquiry_desc = '" + priorityenquiry_desc + "',"
						+ " priorityenquiry_color ='" + priorityenquiry_color + "',"
						+ " priorityenquiry_metcount = " + priorityenquiry_metcount + ","
						+ " priorityenquiry_testdrivecount = " + priorityenquiry_testdrivecount + ","
						+ " priorityenquiry_quotecount = " + priorityenquiry_quotecount + ","
						+ " priorityenquiry_homevisitcount = " + priorityenquiry_homevisitcount + ","
						+ " priorityenquiry_closedays = " + priorityenquiry_closedays + ","
						+ " priorityenquiry_optioncount = " + priorityenquiry_optioncount + ","
						+ " priorityenquiry_duehrs = '" + duehrs + "',"
						+ " priorityenquiry_trigger1_hrs = '" + trigger1_hrs + "',"
						+ " priorityenquiry_trigger2_hrs = '" + trigger2_hrs + "',"
						+ " priorityenquiry_trigger3_hrs = '" + trigger3_hrs + "',"
						+ " priorityenquiry_trigger4_hrs = '" + trigger4_hrs + "',"
						+ " priorityenquiry_trigger5_hrs = '" + trigger5_hrs + "',"
						+ " priorityenquiry_modified_id = " + priorityenquiry_modified_id + ","
						+ " priorityenquiry_modified_date = '" + priorityenquiry_modified_date + "'"
						+ " WHERE priorityenquiry_id = " + priorityenquiry_id + " ";
				// SOP("UpdateFields----" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (priorityenquiry_id.equals("1")) {
			msg = "<br>First record cannot be Deleted ";
			return;
		}
		StrSql = "SELECT enquiry_priorityenquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_priorityenquiry_id = " + priorityenquiry_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Priority is Associated with Enquiry!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_priority WHERE priorityenquiry_id =" + priorityenquiry_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateOptionCount(String priorityenquiry_optioncount) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Any</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(priorityenquiry_optioncount, "1") + ">Any One</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(priorityenquiry_optioncount, "2") + ">Any Two</option>");
			Str.append("<option value=\"3\" " + StrSelectdrop(priorityenquiry_optioncount, "3") + ">Any Three</option>");
			Str.append("<option value=\"4\" " + StrSelectdrop(priorityenquiry_optioncount, "4") + ">Any Four</option>");
			Str.append("<option value=\"5\" " + StrSelectdrop(priorityenquiry_optioncount, "5") + ">Any Five</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
