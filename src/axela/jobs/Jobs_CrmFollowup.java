package axela.jobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_CrmFollowup extends Connect {
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String branch_id = "0";
	String appurl = "";
	int triggercount = 0;
	double diff = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				CRMfollowup();
			} else {
				comp_id = "1009";
				CRMfollowup();
				Thread.sleep(100);
				comp_id = "1011";
				CRMfollowup();
				Thread.sleep(100);
				comp_id = "1017";
				CRMfollowup();
				Thread.sleep(100);
				comp_id = "1019";
				CRMfollowup();
				// Thread.sleep(100);
				// comp_id = "1022";
				// CRMfollowup();
			}
			StrHTML = "CRM Followup Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void CRMfollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "Select enquiry_id, crm_id, crm_trigger, enquiry_branch_id, "
					+ " crm_followup_time, comp_subdomain "
					+ " from " + compdb(comp_id) + "axela_sales_crm"
					+ " inner join " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_status_id=1 AND crm_desc = '' AND crmdays_active = 1"
					+ " AND crm_trigger < 5"
					+ " AND branch_esc_crm_followup = 1"
					+ " AND crm_followup_time <= " + ToLongDate(kknow())
					+ " group by crm_id order by crm_trigger, crm_followup_time";
			// SOP("CRM select trigger query......" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				String endtime = crs.getString("crm_followup_time");
				diff = getMinutesBetween(endtime, ToLongDate(kknow()));

				if (crs.getInt("crm_trigger") == 0 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("crm_id"), "1");
				}
				if (crs.getInt("crm_trigger") == 1 && (diff >= ConvertHoursToMins("24:30"))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("crm_id"), "2");
				}
				if (crs.getInt("crm_trigger") == 2 && (diff >= ConvertHoursToMins("25:00"))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("crm_id"), "3");
				}
				if (crs.getInt("crm_trigger") == 3 && (diff >= ConvertHoursToMins("25:30"))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("crm_id"), "4");
				}
				if (crs.getInt("crm_trigger") == 4 && (diff >= ConvertHoursToMins("26:00"))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("crm_id"), "5");
				}
				appurl = AppURL().replace("demo", crs.getString("comp_subdomain"));
				if (triggercount >= 500) {
					return;
				}
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			crs.close();
		}
	}

	void UpdateTrigger(String enquiry_id, String crmfollowup_id, String TriggerLevel) throws SQLException {
		// SOP("triggercount===" + triggercount);
		triggercount++;
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();

		StrSql = "Update " + compdb(comp_id) + "axela_sales_crm "
				+ " set crm_trigger=" + TriggerLevel + " "
				+ " where crm_id=" + crmfollowup_id + " ";
		// SOP("StrSql = " + StrSql);
		updateQuery(StrSql);
		try {
			StrSql = "Select enquiry_id, enquiry_branch_id, stage_name, customer_id, customer_name, "
					+ " concat(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name, "
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active,  "
					+ " branch_name, branch_email1, concat(emp_name,' (',emp_ref_no,')') as Executive, emp_mobile1, "
					+ " model_name, item_name, crm_so_id, crm_trigger, crm_followup_time, "
					+ " crm_desc,"
					+ " (CASE WHEN crmdays_crmtype_id=1 THEN 'CRM'"
					+ " WHEN crmdays_crmtype_id=2 THEN 'PBF'"
					+ " WHEN crmdays_crmtype_id=3 THEN 'PSF'"
					+ " ELSE ''"
					+ " END) AS crmtype"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry "
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ " inner join " + compdb(comp_id) + "axela_sales_crm on crm_enquiry_id=enquiry_id "
					+ " inner join " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id=crm_emp_id "
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id=enquiry_customer_id "
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id=enquiry_contact_id "
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id "
					+ " WHERE crm_id=" + crmfollowup_id + "";
			// SOP("select CRM mail query......" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<head><style>table {background-color:#FFF;border-style:solid;border-color:#1b62a9;border-collapse:collapse;}");
				Str.append("td {padding:3px;line-height: 150%;FONT-SIZE: 12px;FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;}");
				Str.append("</style></head>");
				Str.append("\n <table border=1 width=600 cellspacing=0 cellpadding=0 >");
				Str.append("<tr>\n");
				Str.append("<td align=left>Branch:</td>\n");
				Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Executive:</td>\n");
				Str.append("<td align=left>").append(crs.getString("Executive")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("emp_mobile1").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>Executive Mobile:</td>\n");
					Str.append("<td align=left>").append("+").append(crs.getString("emp_mobile1")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>Customer:</td>\n");
				Str.append("<td align=left>").append(crs.getString("customer_name")).append(" (");
				Str.append(crs.getString("customer_id")).append(")</td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Contact:</td>\n");
				Str.append("<td align=left>").append(crs.getString("contact_name")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("contact_mobile1").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>Mobile:</td>\n");
					Str.append("<td align=left>").append("+").append(crs.getString("contact_mobile1")).append("</td>\n");
					Str.append("</tr>\n");
				}
				if (!crs.getString("contact_email1").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>Email:</td>\n");
					Str.append("<td align=left><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>Enquiry ID:</td>\n");
				Str.append("<td align=left><a href=").append(appurl).append("sales/enquiry-list.jsp?enquiry_id=");
				Str.append(crs.getString("enquiry_id")).append(">").append(crs.getString("enquiry_id")).append("</a></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Model:</td>\n");
				Str.append("<td align=left> ").append(crs.getString("model_name")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("item_name").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>Item:</td>\n");
					Str.append("<td align=left>").append(crs.getString("item_name")).append("</td>\n");
					Str.append("</tr>\n");
				}
				if (!crs.getString("crm_so_id").equals("0")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>SO ID:</td>\n");
					Str.append("<td align=left>").append(crs.getString("crm_so_id")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>").append(crs.getString("crmtype")).append(" Follow-up Time:</td>\n");
				Str.append("<td align=left><a href=").append(appurl);
				Str.append("sales/enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("#tabs-3>");
				Str.append(strToLongDate(crs.getString("crm_followup_time"))).append("</a></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Stage:</td>\n");
				Str.append("<td align=left>").append(crs.getString("stage_name")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("crm_desc").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>").append(crs.getString("crmtype")).append(" Follow-up Description:</td>\n");
					Str.append("<td align=left>").append(crs.getString("crm_desc")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>Escalation Level:</td>\n");
				Str.append("<td align=left>").append(crs.getString("crm_trigger")).append("</td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				String subject = crs.getString("crmtype") + " Follow-up escalated to Level " + TriggerLevel + " for Enquiry ID " + enquiry_id;
				// if (!crs.getString("emp_email1").equals("") && crs.getString("emp_active").equals("1")) {
				// if (isNotPublicEmail(crs.getString("emp_email1")).equals("1") && AppRun().equals("1")) {
				// postMail(crs.getString("emp_email1"), GetEmps(TriggerLevel, "prioritycrmfollowup",
				// crs.getString("emp_id"), crs.getString("enquiry_branch_id"), comp_id), "", crs.getString("branch_email1"),
				// subject, Str.toString(), "", comp_id);
				// }
				// }
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			crs.close();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
