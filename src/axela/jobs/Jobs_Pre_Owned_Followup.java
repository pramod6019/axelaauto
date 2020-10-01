// 5 march 1985
/*======Saiman 10th May 2013=====*/
package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Pre_Owned_Followup extends Connect {

	public String StrSql = "";
	public String endtime = "";
	double diff = 0;
	public String StrHTML = "";
	public String msg = "";
	public String title = "";
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
	public String emp_device_fcmtoken = "";
	public String emp_device_os = "";
	public String preownedmodel_name = "";
	public String variant_name = "";
	public String preowned_title = "";
	public String preownedfollowuptype_name = "";
	public String preownedfollowup_followup_time = "";
	public String branch_id = "0";
	String appurl = "";
	int triggercount = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				PreownedFollowup();
			} else {
				comp_id = "1000"; // Demo
				PreownedFollowup();
				Thread.sleep(100);
				comp_id = "1009"; // DD Motors
				PreownedFollowup();
				Thread.sleep(100);
				comp_id = "1011"; // Indel
				PreownedFollowup();
				Thread.sleep(100);
				comp_id = "1015"; // BBT
				PreownedFollowup();
				comp_id = "1023"; // AMP
				PreownedFollowup();

			}
			StrHTML = "Pre-Owned Follow-up Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void PreownedFollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT preowned_id, preowned_branch_id, preownedfollowup_id, preownedfollowup_trigger, preownedfollowup_followup_time, preowned_title,"
					+ " prioritypreowned_trigger1_hrs, prioritypreowned_trigger2_hrs, prioritypreowned_trigger3_hrs, "
					+ " prioritypreowned_trigger4_hrs, prioritypreowned_trigger5_hrs,"
					+ " COALESCE(concat(title_desc, ' ', contact_fname,' ', contact_lname), '') as contact_name,"
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active,"
					+ " COALESCE(concat(emp_name,' (',emp_ref_no,')'), '') as executive, emp_mobile1,"
					+ " COALESCE(emp_device_fcmtoken,'') AS emp_device_fcmtoken,"
					+ " COALESCE(emp_device_os,'') AS emp_device_os,"
					+ " preownedmodel_name, variant_name,"
					+ " preownedfollowuptype_name, branch_email1, customer_id, customer_name, branch_name,"
					+ " comp_subdomain"
					+ " FROM " + compdb(comp_id) + "axela_preowned_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id=preownedfollowup_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_priority on prioritypreowned_id=preowned_prioritypreowned_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_preowned_stage on stage_id=preowned_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_followup_type on preownedfollowuptype_id =preownedfollowup_preownedfollowuptype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id=preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id=preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE preowned_preownedstatus_id=1"
					+ " AND preownedfollowup_desc=''"
					+ " AND preownedfollowup_trigger < 5"
					+ " AND (prioritypreowned_trigger1_hrs != '0:00'"
					+ " OR prioritypreowned_trigger2_hrs != '0:00'"
					+ " OR prioritypreowned_trigger3_hrs != '0:00'"
					+ " OR prioritypreowned_trigger4_hrs != '0:00'"
					+ " OR prioritypreowned_trigger5_hrs != '0:00')"
					+ " AND preownedfollowup_followup_time <=" + ToLongDate(kknow())
					+ " AND branch_esc_preowned_followup = 1"
					+ " group by preowned_id order by preownedfollowup_trigger, preownedfollowup_followup_time";
			// SOP("preowned followup cron---------------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				endtime = crs.getString("preownedfollowup_followup_time");
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
				emp_device_fcmtoken = crs.getString("emp_device_fcmtoken");
				emp_device_os = crs.getString("emp_device_os");
				preowned_title = crs.getString("preowned_title");
				preownedmodel_name = crs.getString("preownedmodel_name");
				variant_name = crs.getString("variant_name");
				preownedfollowuptype_name = crs.getString("preownedfollowuptype_name");
				preownedfollowup_followup_time = crs.getString("preownedfollowup_followup_time");

				if (crs.getInt("preownedfollowup_trigger") == 0 && !crs.getString("prioritypreowned_trigger1_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger1_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), crs.getString("preownedfollowup_id"), "1");
				}
				if (crs.getInt("preownedfollowup_trigger") == 1 && !crs.getString("prioritypreowned_trigger2_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger2_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), crs.getString("preownedfollowup_id"), "2");
				}
				if (crs.getInt("preownedfollowup_trigger") == 2 && !crs.getString("prioritypreowned_trigger3_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger3_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), crs.getString("preownedfollowup_id"), "3");
				}
				if (crs.getInt("preownedfollowup_trigger") == 3 && !crs.getString("prioritypreowned_trigger4_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger4_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), crs.getString("preownedfollowup_id"), "4");
				}
				if (crs.getInt("preownedfollowup_trigger") == 4 && !crs.getString("prioritypreowned_trigger5_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("prioritypreowned_trigger5_hrs")))) {
					UpdateTrigger(crs.getString("preowned_id"), crs.getString("preownedfollowup_id"), "5");
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

	protected void UpdateTrigger(String preowned_id, String preownedfollowup_id, String TriggerLevel) {
		triggercount++;
		StringBuilder Str = new StringBuilder();
		String subject = "Follow-up escalated to Level " + TriggerLevel + " For preowned ID " + preowned_id;
		StrSql = "Update " + compdb(comp_id) + "axela_preowned_followup "
				+ " set preownedfollowup_trigger=" + TriggerLevel + ""
				+ " , preownedfollowup_notify = 0"
				+ " WHERE preownedfollowup_id = " + preownedfollowup_id + "";
		// SOPError("UpdateTrigger query-------------" + StrSql);
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
			Str.append("<td align=leaft>" + executive + "</td>\n");
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
			Str.append("<td align=left>Follow-up Type:</td>\n");
			Str.append("<td align=left>" + preownedfollowuptype_name + "</td>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n");
			Str.append("<td align=left>Follow-up Time:</td>\n");
			Str.append("<td align=left><a href=" + appurl + "preowned/preowned-dash.jsp?preowned_id=" + preowned_id + "#tabs-2>" + strToLongDate(preownedfollowup_followup_time) + "</a></td>\n");
			Str.append("</tr>\n");
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
			// SOP("Mail===" + Str.toString());
			if (!emp_device_os.equals("") && !emp_device_fcmtoken.equals("")) {
				msg = "Pre-Owned ID: " + preowned_id + "\n"
						+ "Follow-up Time: " + strToLongDate(preownedfollowup_followup_time) + "\n"
						+ "Contact: " + contact_name + "\n"
						+ "Mobile: " + contact_mobile1 + "\n";
				title = "Pre-Owned Follow-up Level " + TriggerLevel + " Escalation\n";
				new FCM().SendPushNotification(emp_device_fcmtoken, emp_device_os, title, msg);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			// crs.close();
		}

	}
}
