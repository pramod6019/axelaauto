// 5 march 1985
/*======Saiman 10th May 2013=====*/
package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Priority_Enquiry_Followup extends Connect {

	public String StrSql = "";
	public String endtime = "";
	double diff = 0;
	public String StrHTML = "";
	public String msg = "";
	public String title = "";
	public String comp_id = "0";
	public String branch_name = "", branch_email1 = "";
	public String stage_name = "";
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
	public String model_name = "";
	public String item_name = "";
	public String enquiry_title = "";
	public String followuptype_name = "";
	public String followup_followup_time = "";
	public String branch_id = "0";
	String appurl = "";
	int triggercount = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				PriorityEnquiryFollowup();
			} else {
				comp_id = "1000";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1009";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1011";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1015";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1017";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1018";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1019";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				comp_id = "1020";
				PriorityEnquiryFollowup();
				Thread.sleep(100);

				// comp_id = "1022";
				// PriorityEnquiryFollowup();
				// Thread.sleep(100);

				comp_id = "1023";
				PriorityEnquiryFollowup();

				Thread.sleep(100);
				comp_id = "1024";
				PriorityEnquiryFollowup();

				Thread.sleep(100);
				comp_id = "1026";
				PriorityEnquiryFollowup();

			}

			StrHTML = "Enquiry Followup Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void PriorityEnquiryFollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT enquiry_id, enquiry_branch_id, followup_id, followup_trigger, followup_followup_time, enquiry_title,"
					+ " priorityenquiry_trigger1_hrs, priorityenquiry_trigger2_hrs, priorityenquiry_trigger3_hrs, "
					+ " priorityenquiry_trigger4_hrs, priorityenquiry_trigger5_hrs, stage_name,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS executive, emp_mobile1,"
					+ " COALESCE(emp_device_fcmtoken,'') AS emp_device_fcmtoken,"
					+ " COALESCE(emp_device_os,'') AS emp_device_os,"
					+ " model_name, item_name,"
					+ " followuptype_name, branch_email1, customer_id, customer_name, branch_name,"
					+ " comp_subdomain"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_status_id = 1"
					+ " AND followup_desc = ''"
					+ " AND followup_trigger < 5"
					+ " AND (priorityenquiry_trigger1_hrs != '0:00'"
					+ " OR priorityenquiry_trigger2_hrs != '0:00'"
					+ " OR priorityenquiry_trigger3_hrs != '0:00'"
					+ " OR priorityenquiry_trigger4_hrs != '0:00'"
					+ " OR priorityenquiry_trigger5_hrs != '0:00') AND followup_followup_time <=" + ToLongDate(kknow())
					+ " AND branch_esc_enquiry_followup = 1"
					+ " GROUP BY enquiry_id"
					+ " ORDER BY followup_trigger, followup_followup_time";

//			if (comp_id.equals("1023")) {
//				SOPInfo("jobs escalation===" + StrSql);
//			}

//			SOP("dc enquiry followup cron----------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				endtime = crs.getString("followup_followup_time");
				diff = getMinutesBetween(endtime, ToLongDate(kknow()));
				branch_id = crs.getString("enquiry_branch_id");
				branch_name = crs.getString("branch_name");
				branch_email1 = crs.getString("branch_email1");
				stage_name = crs.getString("stage_name");
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
				enquiry_title = crs.getString("enquiry_title");
				model_name = crs.getString("model_name");
				item_name = crs.getString("item_name");
				followuptype_name = crs.getString("followuptype_name");
				followup_followup_time = crs.getString("followup_followup_time");

				if (crs.getInt("followup_trigger") == 0 && !crs.getString("priorityenquiry_trigger1_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityenquiry_trigger1_hrs")))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("followup_id"), "1");

				}
				if (crs.getInt("followup_trigger") == 1 && !crs.getString("priorityenquiry_trigger2_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityenquiry_trigger2_hrs")))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("followup_id"), "2");
				}
				if (crs.getInt("followup_trigger") == 2 && !crs.getString("priorityenquiry_trigger3_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityenquiry_trigger3_hrs")))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("followup_id"), "3");
				}
				if (crs.getInt("followup_trigger") == 3 && !crs.getString("priorityenquiry_trigger4_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityenquiry_trigger4_hrs")))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("followup_id"), "4");
				}
				if (crs.getInt("followup_trigger") == 4 && !crs.getString("priorityenquiry_trigger5_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityenquiry_trigger5_hrs")))) {
					UpdateTrigger(crs.getString("enquiry_id"), crs.getString("followup_id"), "5");
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
	protected void UpdateTrigger(String enquiry_id, String followup_id, String TriggerLevel) {
		triggercount++;
		StringBuilder Str = new StringBuilder();
		String subject = "Follow-up escalated to Level " + TriggerLevel + " for Enquiry ID " + enquiry_id;
		StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup "
				+ " SET followup_trigger = " + TriggerLevel + ""
				+ " , followup_notify = 0"
				+ " WHERE followup_id = " + followup_id + "";
		updateQuery(StrSql);
//		 if (comp_id.equals("1023")) {
//				SOPInfo("jobs escalation==update===" + StrSql);
//			}
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
			Str.append("<td align=left>Enquiry ID:</td>\n");
			Str.append("<td align=left><a href=" + appurl + "sales/enquiry-list.jsp?enquiry_id=" + enquiry_id + ">" + enquiry_id + "</a></td>\n");
			Str.append("</tr>\n");
			if (!model_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Model:</td>\n");
				Str.append("<td align=left> " + model_name + "</td>\n");
				Str.append("</tr>\n");
			}
			if (!item_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Item:</td>\n");
				Str.append("<td align=left>" + item_name + "</td>\n");
				Str.append("</tr>\n");
			}
			Str.append("<tr>\n");
			Str.append("<td align=left>Follow-up Type:</td>\n");
			Str.append("<td align=left>" + followuptype_name + "</td>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n");
			Str.append("<td align=left>Follow-up Time:</td>\n");
			Str.append("<td align=left><a href=" + appurl + "sales/enquiry-dash.jsp?enquiry_id=" + enquiry_id + "#tabs-2>" + strToLongDate(followup_followup_time) + "</a></td>\n");
			Str.append("</tr>\n");
			if (!stage_name.equals("")) {
				Str.append("<tr>\n");
				Str.append("<td align=left>Stage:</td>\n");
				Str.append("<td align=left>" + stage_name + "</td>\n");
				Str.append("</tr>\n");
			}
			Str.append("<tr>\n");
			Str.append("<td align=left>Escalation Level:</td>\n");
			Str.append("<td align=left>" + TriggerLevel + "</td>\n");
			Str.append("</tr>\n");
			Str.append("</table>\n");

			// if (!emp_email1.equals("") && emp_active.equals("1")) {
			// if (isNotPublicEmail(emp_email1).equals("1") && AppRun().equals("1")) {
			// postMail(emp_email1, GetEmps(TriggerLevel, "priorityenquiryfollowup",
			// emp_id, branch_id, comp_id), "", branch_email1,
			// subject, Str.toString(), "", comp_id);
			// }
			// }
			// SOP("Mail===" + Str.toString());
			if (!emp_device_os.equals("") && !emp_device_fcmtoken.equals("")) {
				msg = "Enquiry ID: " + enquiry_id + "\n"
						+ "Follow-up Time: " + strToLongDate(followup_followup_time) + "\n"
						+ "Contact: " + contact_name + "\n"
						+ "Mobile: " + contact_mobile1 + "\n";
				title = "Enquiry Follow-up Level " + TriggerLevel + " Escalation\n";
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
