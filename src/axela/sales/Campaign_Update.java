package axela.sales;
//@Bhagwan Singh 11 feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String campaign_id = "0";
	public String campaign_name = "";
	public String campaign_campaigntype_id = "0";
	public String campaign_desc = "";
	public String campaign_startdate = "";
	public String campaign_enddate = "";
	public String campaign_budget = "";
	public String campaign_active = "";
	public String campaign_notes = "";
	public String campaign_entry_id = "0";
	public String campaign_entry_date = "";
	public String campaign_modified_id = "0";
	public String campaign_modified_date = "";
	public String checkperm = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String[] list_branch_trans = new String[10];
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button1"));
				updateB = PadQuotes(request.getParameter("update_button1"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						campaign_id = "";
						campaign_name = "";
						campaign_campaigntype_id = "";
						campaign_desc = "";
						campaign_startdate = "";
						campaign_enddate = "";
						campaign_budget = "";
						campaign_active = "1";
						campaign_notes = "";
						campaign_entry_id = "";
						campaign_entry_date = "";
						campaign_modified_id = "";
						campaign_modified_date = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_campaign_add", request).equals("1")) {
							campaign_entry_id = emp_id;
							campaign_entry_date = ToLongDate(kknow());
							if (msg.equals("")) {
								AddFields(request);
							}
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?campaign_id=" + campaign_id + "&msg=Campaign added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!("yes").equals(updateB) && !"Delete Campaign".equals(deleteB)) {
						PopulateFields(response);
					} else if (("yes").equals(updateB) && !"Delete Campaign".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_campaign_edit", request).equals("1")) {
							campaign_modified_id = emp_id;
							campaign_modified_date = ToLongDate(kknow());
							if (msg.equals("")) {
								UpdateFields(request);
							}
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Campaign Updated Successfully!";
								response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?campaign_id=" + campaign_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Campaign".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_campaign_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?msg=Campaign deleted successfully!"));
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
		campaign_campaigntype_id = CNumeric(PadQuotes(request.getParameter("dr_camptype_id")));
		campaign_name = PadQuotes(request.getParameter("txt_campaign_name"));
		campaign_desc = PadQuotes(request.getParameter("txt_campaign_desc"));
		campaign_startdate = PadQuotes(request.getParameter("txt_campaign_startdate"));
		campaign_enddate = PadQuotes(request.getParameter("txt_campaign_enddate"));
		campaign_budget = CNumeric(PadQuotes(request.getParameter("txt_campaign_budget")));
		campaign_active = PadQuotes(request.getParameter("chk_campaign_active"));
		list_branch_trans = request.getParameterValues("list_branch_trans");
		if (campaign_active.equals("on")) {
			campaign_active = "1";
		} else {
			campaign_active = "0";
		}
		campaign_notes = PadQuotes(request.getParameter("txt_campaign_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (campaign_name.equals("")) {
			msg = msg + "<br>Enter Campaign Name!";
		} else// similar Name found
		{
			try {
				// if (update.equals("yes")) {
				// StrSql = "SELECT campaign_name FROM " + compdb(comp_id) + "axela_sales_campaign"
				// + " WHERE campaign_name = '" + campaign_name + "' AND campaign_id!=" + campaign_id + ""; // and campaign_campaigncat_id=0
				// } else if (add.equals("yes")) {
				// StrSql = "SELECT campaign_name FROM " + compdb(comp_id) + "axela_sales_campaign"
				// + " WHERE campaign_name = '" + campaign_name + "'"; // AND campaign_campaigncat_id="+campaign_campaigncat_id
				// }
				// CachedRowSet crs1 =processQuery(StrSql, 0);
				// while (crs1.next()) {
				// msg = msg + "<br>Similar Campaign Found! ";
				// }
				// crs1.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (campaign_campaigntype_id.equals("0")) {
			msg = msg + "<br>Please select a campaign Type!";
		}
		if (campaign_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		} else if (campaign_desc.length() > 1000) {
			campaign_desc = campaign_desc.substring(0, 1000);
		}
		if (campaign_startdate.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!campaign_startdate.equals("") && !isValidDateFormatShort(campaign_startdate)) {
			msg = msg + "<br>Enter valid Start Date!";
		}
		if (campaign_enddate.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!campaign_enddate.equals("") && !isValidDateFormatShort(campaign_enddate)) {
			msg = msg + "<br>Enter valid End Date!";
		}
		if (!campaign_startdate.equals("") && isValidDateFormatShort(campaign_startdate) && !campaign_enddate.equals("") && isValidDateFormatShort(campaign_enddate)) {
			long campaign_sdate = Long.parseLong(ConvertShortDateToStr(campaign_startdate));
			long campaign_edate = Long.parseLong(ConvertShortDateToStr(campaign_enddate));
			if (campaign_sdate > campaign_edate) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
		if (campaign_budget.equals("")) {
			msg = msg + "<br>Enter Budget!";
		}
		else if (!campaign_budget.equals("0"))
		{
			if (isNumeric(campaign_budget) && campaign_budget.contains("."))
			{
				msg = msg + "<br>Budget should be Numeric!";
			}
			// else if (!isNumeric(campaign_budget)) {
			// SOP("campaign_budget---555-" + campaign_budget);
			// msg = msg + "<br>Entered Budget should be Numeric!";
			// }
		}
		if (list_branch_trans == null) {
			msg = msg + "<br>Select Branch!";
		}
		if (campaign_notes.length() > 8000) {
			campaign_notes = campaign_notes.substring(0, 7999);
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				campaign_id = ExecuteQuery("SELECT (coalesce(max(campaign_id),0)+1) FROM " + compdb(comp_id) + "axela_sales_campaign");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_campaign"
						+ "(campaign_id,"
						+ " campaign_campaigntype_id,"
						+ " campaign_name,"
						+ " campaign_desc,"
						+ " campaign_startdate,"
						+ " campaign_enddate,"
						+ " campaign_budget,"
						+ " campaign_active,"
						+ " campaign_notes,"
						+ " campaign_entry_id,"
						+ " campaign_entry_date,"
						+ " campaign_modified_id,"
						+ " campaign_modified_date)"
						+ " VALUES"
						+ "(" + campaign_id + ","
						+ " " + campaign_campaigntype_id + ","
						+ " '" + campaign_name + "',"
						+ " '" + campaign_desc + "',"
						+ " '" + ConvertShortDateToStr(campaign_startdate) + "',"
						+ " '" + ConvertShortDateToStr(campaign_enddate) + "',"
						+ "'" + campaign_budget + "',"
						+ " '" + campaign_active + "',"
						+ " '" + campaign_notes + "',"
						+ " " + campaign_entry_id + ","
						+ " '" + campaign_entry_date + "',"
						+ " 0,"
						+ " '')";
				updateQuery(StrSql);
				UpdateList();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT campaign_id, campaign_campaigntype_id, campaign_name, campaign_desc,"
					+ " campaign_startdate, campaign_enddate, campaign_budget,"
					+ " campaign_active, campaign_notes, campaign_entry_id,"
					+ " campaign_entry_date, campaign_modified_id, campaign_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " WHERE campaign_id = " + campaign_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					campaign_id = crs.getString("campaign_id");
					campaign_campaigntype_id = crs.getString("campaign_campaigntype_id");
					campaign_name = crs.getString("campaign_name");
					campaign_desc = crs.getString("campaign_desc");
					campaign_startdate = strToShortDate(crs.getString("campaign_startdate"));
					campaign_enddate = strToShortDate(crs.getString("campaign_enddate"));
					campaign_budget = crs.getString("campaign_budget");
					campaign_active = crs.getString("campaign_active");
					campaign_notes = crs.getString("campaign_notes");
					campaign_entry_id = crs.getString("campaign_entry_id");
					campaign_entry_date = crs.getString("campaign_entry_date");
					campaign_modified_id = crs.getString("campaign_modified_id");
					if (!crs.getString("campaign_entry_id").equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("campaign_entry_id"));
						entry_date = strToLongDate(crs.getString("campaign_entry_date"));
					}
					campaign_modified_date = crs.getString("campaign_modified_date");
					if (!crs.getString("campaign_modified_id").equals("0")) {
						modified_by = Exename(comp_id, crs.getInt("campaign_modified_id"));
						modified_date = strToLongDate(crs.getString("campaign_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?&msg=Invalid Campaign!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UpdateList() {
		try {
			if (msg.equals("")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_campaign_branch"
						+ " WHERE camptrans_campaign_id =" + campaign_id + "";
				updateQuery(StrSql);

				if (list_branch_trans != null) {
					for (int i = 0; i < list_branch_trans.length; i++) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_campaign_branch (camptrans_campaign_id, camptrans_branch_id)"
								+ " VALUES ( " + campaign_id + ", " + list_branch_trans[i] + ")";
						updateQuery(StrSql);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_campaign"
						+ " SET"
						+ " campaign_campaigntype_id = " + campaign_campaigntype_id + ","
						+ " campaign_name = '" + campaign_name + "',"
						+ " campaign_desc = '" + campaign_desc + "',"
						+ " campaign_startdate = '" + ConvertShortDateToStr(campaign_startdate) + "',"
						+ " campaign_enddate = '" + ConvertShortDateToStr(campaign_enddate) + "',"
						+ " campaign_budget = " + campaign_budget + ","
						+ " campaign_active = '" + campaign_active + "',"
						+ " campaign_notes = '" + campaign_notes + "',"
						+ " campaign_modified_id = " + campaign_modified_id + ","
						+ " campaign_modified_date = '" + campaign_modified_date + "'"
						+ " WHERE campaign_id = " + campaign_id + " ";
				updateQuery(StrSql);

				// Add the branch transaction
				UpdateList();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_campaign_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_campaign_id = " + campaign_id + " ";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Campaign is associated with Enquiry!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_campaign"
						+ " WHERE campaign_id = " + campaign_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateCampType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT camptype_id, camptype_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign_type"
					+ " ORDER BY camptype_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("camptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("camptype_id"), campaign_campaigntype_id));
				Str.append(">").append(crs.getString("camptype_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String Exec = " ";
			while (crs.next()) {
				Exec = Exec + "<option value=" + crs.getString("branch_id") + ">" + crs.getString("branch_name") + "</option> \n";
			}
			crs.close();
			return Exec;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchTrans(HttpServletRequest request) {
		campaign_id = PadQuotes(request.getParameter("campaign_id"));
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id,branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON branch_id = camptrans_branch_id"
					+ " WHERE camptrans_campaign_id = '" + campaign_id + "'"
					+ " ORDER BY branch_name";
			if (add.equals("yes") && list_branch_trans != null || (updateB.equals("Update Campaign"))) {
				StrSql = "SELECT branch_id, branch_name"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " ORDER BY branch_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("Update Campaign")) && list_branch_trans != null) {
					// SOP(list_branch_trans.length + ".....");
					for (int i = 0; i < list_branch_trans.length; i++) {
						if (crs.getString("branch_id").equals(list_branch_trans[i])) {
							Str.append("<<option value=").append(crs.getString("branch_id")).append(" selected>").append(crs.getString("branch_name")).append("</option> \n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("Update Campaign")) {
					Str.append("<option value=").append(crs.getString("branch_id")).append(" selected>");
					Str.append(crs.getString("branch_name")).append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
