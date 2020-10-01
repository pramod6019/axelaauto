// 17 Jan 2017
package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Pre_Owned_Evaluation extends Connect {

	public String StrSql = "";
	public String endtime = "";
	double diff = 0;
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String branch_name = "", branch_email1 = "";
	public String customer_id = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String emp_email1 = "";
	public String emp_id = "";
	public String emp_active = "";
	public String executive = "";
	public String emp_mobile1 = "";
	public String preownedmodel_name = "";
	public String variant_name = "";
	public String preowned_title = "";
	public String preownedfollowuptype_name = "";
	public String eval_date = "";
	public String branch_id = "0", eval_id = "0";
	String appurl = "";
	int triggercount = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				PreownedEvaluation();
			} else {
				comp_id = "1009"; // DD Motors
				PreownedEvaluation();
				Thread.sleep(100);
				comp_id = "1011"; // Indel
				PreownedEvaluation();
				Thread.sleep(100);
				comp_id = "1015"; // BBT
				PreownedEvaluation();
				comp_id = "1023"; // AMP
				PreownedEvaluation();
			}
			StrHTML = "Pre-Owned Evaluation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void PreownedEvaluation() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT preowned_id, preowned_branch_id, preowned_date, preowned_eval_trigger,  preowned_title,"
					+ " prioritypreowned_trigger1_hrs, prioritypreowned_trigger2_hrs, prioritypreowned_trigger3_hrs, "
					+ " prioritypreowned_trigger4_hrs, prioritypreowned_trigger5_hrs,"
					+ " COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname), '') as contact_name,"
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active,"
					+ " COALESCE(concat(emp_name,' (',emp_ref_no,')'), '') as executive, emp_mobile1, preownedmodel_name, variant_name,"
					+ " branch_email1, customer_id, customer_name, branch_name,"
					+ " comp_subdomain"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_priority on prioritypreowned_id = preowned_prioritypreowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id=preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id=preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval on eval_preowned_id = preowned_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE preowned_preownedstatus_id = 1"
					+ " AND preowned_eval_trigger < 5"
					+ " AND (prioritypreowned_trigger1_hrs != '0:00'"
					+ " OR prioritypreowned_trigger2_hrs != '0:00'"
					+ " OR prioritypreowned_trigger3_hrs != '0:00'"
					+ " OR prioritypreowned_trigger4_hrs != '0:00'"
					+ " OR prioritypreowned_trigger5_hrs != '0:00') "
					+ " AND eval_date IS NULL"
					+ " AND branch_esc_preowned_eval_followup = 1"
					+ " GROUP BY preowned_id "
					+ " ORDER BY preowned_eval_trigger";
			// SOPInfo("preowned evaluation cron-----------------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				endtime = crs.getString("preowned_date");
				diff = getMinutesBetween(endtime, ToLongDate(kknow()));
				branch_id = crs.getString("preowned_branch_id");
				branch_name = crs.getString("branch_name");
				branch_email1 = crs.getString("branch_email1");
				customer_id = crs.getString("customer_id");
				customer_name = crs.getString("customer_name");
				contact_name = crs.getString("contact_name");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_email1 = crs.getString("contact_email1");
				emp_email1 = crs.getString("emp_email1");
				emp_id = crs.getString("emp_id");
				emp_active = crs.getString("emp_active");
				executive = crs.getString("executive");
				emp_mobile1 = crs.getString("emp_mobile1");
				preowned_title = crs.getString("preowned_title");
				preownedmodel_name = crs.getString("preownedmodel_name");
				variant_name = crs.getString("variant_name");

				if (crs.getInt("preowned_eval_trigger") == 0 && !crs.getString("prioritypreowned_trigger1_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger1_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), "1");
				}
				if (crs.getInt("preowned_eval_trigger") == 1 && !crs.getString("prioritypreowned_trigger2_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger2_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), "2");
				}
				if (crs.getInt("preowned_eval_trigger") == 2 && !crs.getString("prioritypreowned_trigger3_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger3_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), "3");
				}
				if (crs.getInt("preowned_eval_trigger") == 3 && !crs.getString("prioritypreowned_trigger4_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger4_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), "4");
				}
				if (crs.getInt("preowned_eval_trigger") == 4 && !crs.getString("prioritypreowned_trigger5_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger5_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), "5");
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

	protected void UpdateTrigger(String preowned_id, String TriggerLevel) {
		triggercount++;
		StringBuilder Str = new StringBuilder();
		String subject = "Evaluation escalated to Level " + TriggerLevel + " For Pre-Owned ID " + preowned_id;
		StrSql = "Update " + compdb(comp_id) + "axela_preowned"
				+ " SET preowned_eval_trigger=" + TriggerLevel + " , "
				+ " preowned_eval_notify = 0"
				+ " WHERE preowned_id = " + preowned_id + "";
		// SOPInfo("UpdateTrigger query-------------" + StrSql);
		updateQuery(StrSql);
		try {
			Str.append("<head><style>table {background-color:#FFF;border-style:solid;border-color:#1b62a9;border-collapse:collapse;}");
			Str.append("td {padding:3px;line-height: 150%;FONT-SIZE: 12px;FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;}");
			Str.append("</style></head>");
			Str.append("\n <table border=1 width=600 cellspacing=0 cellpadding=0 >");
			Str.append("<tr>\n");
			Str.append("<td align=left>Branch:</td>\n");
			Str.append("<td align=left>" + branch_name + "</td>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n");
			Str.append("<td align=left>Executive:</td>\n");
			Str.append("<td align=left>" + executive + "</td>\n");
			Str.append("</tr>\n");
			if (!emp_mobile1.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Executive Mobile:</td>\n");
				Str.append("<td align=left>+").append(emp_mobile1).append("</td>\n");
				Str.append("</tr>\n");
			}
			if (!customer_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Customer:</td>\n");
				Str.append("<td align=left>" + customer_name + " (" + customer_id + ")</td>\n");
				Str.append("</tr>\n");
			}
			if (!contact_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Contact:</td>\n");
				Str.append("<td align=left>" + contact_name + "</td>\n");
				Str.append("</tr>\n");
			}
			if (!contact_mobile1.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Contact Mobile:</td>\n");
				Str.append("<td align=left>+").append(contact_mobile1).append("</td>\n");
				Str.append("</tr>\n");
			}
			if (!contact_email1.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Contact Email:</td>\n");
				Str.append("<td align=left><a href=mailto:" + contact_email1 + ">" + contact_email1 + "</a></td>\n");
				Str.append("</tr>\n");
			}
			Str.append("<tr>\n");
			Str.append("<td align=left>Preowned ID:</td>\n");
			Str.append("<td align=left><a href=" + appurl + "preowned/preowned-list.jsp?preowned_id=" + preowned_id + ">" + preowned_id + "</a></td>\n");
			Str.append("</tr>\n");
			if (!preownedmodel_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Model:</td>\n");
				Str.append("<td align=left> " + preownedmodel_name + "</td>\n");
				Str.append("</tr>\n");
			}
			if (!variant_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Item:</td>\n");
				Str.append("<td align=left>" + variant_name + "</td>\n");
				Str.append("</tr>\n");
			}
			Str.append("<tr>\n");
			Str.append("<td align=left>Escalation Level:</td>\n");
			Str.append("<td align=left>" + TriggerLevel + "</td>\n");
			Str.append("</tr>\n");
			Str.append("</table>\n");

			// if (!emp_email1.equals("") && emp_active.equals("1")) {
			// if (isNotPublicEmail(emp_email1).equals("1") && AppRun().equals("1")) {
			// postMail(emp_email1, GetEmps(TriggerLevel, "prioritypreownedfollowup",
			// emp_id, branch_id, comp_id), "", branch_email1,
			// subject, Str.toString(), "", comp_id);
			// }
			// }
			// SOP("Str===" + Str.toString());
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			// crs.close();
		}

	}
}
