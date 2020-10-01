package axela.sales;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCampaignType_Update extends Connect {

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
	public String branch_id = "0";
	public String camptype_id = "0";
	public String camptype_desc = "";
	public String stage_probability = "";
	public String stage_rank = "";
	public String QueryString = "";
	public String camptype_entry_id = "0";
	public String camptype_entry_date = "";
	public String camptype_modified_id = "0";
	public String camptype_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				camptype_id = PadQuotes(request.getParameter("camptype_id"));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (camptype_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("index.jsp"));
					} else if (!camptype_id.equals("") && CNumeric(camptype_id).equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Campaign Type!"));
					}
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						camptype_desc = "";
					} else {
						GetValues(request, response);
						camptype_entry_id = emp_id;
						camptype_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecampaigntype.jsp?camptype_id=" + camptype_id + "&msg=Campaign Type Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Campaign Type".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Campaign Type".equals(deleteB)) {
						GetValues(request, response);
						camptype_modified_id = emp_id;
						camptype_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecampaigntype.jsp?camptype_id=" + camptype_id + "&msg=Campaign Type Updated Successfully!"));
						}
					} else if ("Delete Campaign Type".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecampaigntype.jsp?msg=Campaign Type Deleted Successfully!"));
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
		camptype_id = CNumeric(PadQuotes(request.getParameter("camptype_id")));
		camptype_desc = PadQuotes(request.getParameter("txt_camptype_desc"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (camptype_desc.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		try {
			if (!camptype_desc.equals("")) {
				StrSql = "Select camptype_desc from " + compdb(comp_id) + "axela_sales_campaign_type where camptype_desc = '" + camptype_desc + "'";
				if (update.equals("yes") && !camptype_desc.equals("")) {
					StrSql = StrSql + " and camptype_id != " + camptype_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Campaign Type Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				camptype_id = ExecuteQuery("Select (coalesce(max(camptype_id),0)+1) from " + compdb(comp_id) + "axela_sales_campaign_type");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_campaign_type"
						+ " (camptype_id,"
						+ " camptype_desc,"
						+ " camptype_entry_id,"
						+ " camptype_entry_date)"
						+ " values"
						+ " (" + camptype_id + ","
						+ " '" + camptype_desc + "',"
						+ " " + camptype_entry_id + ","
						+ " '" + camptype_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_sales_campaign_type"
					+ " where camptype_id = " + CNumeric(camptype_id) + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					camptype_desc = crs.getString("camptype_desc");
					camptype_entry_id = crs.getString("camptype_entry_id");
					if (!camptype_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(camptype_entry_id));
						entry_date = strToLongDate(crs.getString("camptype_entry_date"));
					}
					camptype_modified_id = crs.getString("camptype_modified_id");
					if (!camptype_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(camptype_modified_id));
						modified_date = strToLongDate(crs.getString("camptype_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Campaign Type!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_campaign_type"
						+ " SET"
						+ " camptype_desc = '" + camptype_desc + "',"
						+ " camptype_modified_id = " + camptype_modified_id + ","
						+ " camptype_modified_date = '" + camptype_modified_date + "'"
						+ " where camptype_id = " + camptype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {

				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT campaign_campaigntype_id from " + compdb(comp_id) + "axela_sales_campaign where campaign_campaigntype_id = " + camptype_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Campaign Type is Associated with Campaign!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_campaign_type where camptype_id =" + camptype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
