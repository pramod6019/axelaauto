// 5 march 1985
/*======Saiman 10th May 2013=====*/
package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_ServiceFollowup_Esc extends Connect {
	
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
	public String model_name = "";
	public String item_name = "";
	public String vehfollowup_followup_time = "";
	public String branch_id = "0";
	String appurl = "";
	int triggercount = 0;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1009";
				PriorityVehicleFollowup();
			} else {
				comp_id = "1000";
				PriorityVehicleFollowup();
				Thread.sleep(100);
				
				comp_id = "1009";
				PriorityVehicleFollowup();
				Thread.sleep(100);
				
				// comp_id = "1011";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				//
				// comp_id = "1015";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				//
				// comp_id = "1017";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				//
				// comp_id = "1018";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				//
				// comp_id = "1019";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				//
				// comp_id = "1020";
				// PriorityVehicleFollowup();
				// Thread.sleep(100);
				
				// comp_id = "1022";
				// PriorityEnquiryFollowup();
				// Thread.sleep(100);
				
				// comp_id = "1023";
				// PriorityVehicleFollowup();
				//
				// Thread.sleep(100);
				// comp_id = "1024";
				// PriorityVehicleFollowup();
				//
				// Thread.sleep(100);
				// comp_id = "1026";
				// PriorityVehicleFollowup();
				
			}
			
			StrHTML = "Vehicle Followup Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}
	
	public void PriorityVehicleFollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT veh_id, veh_branch_id, vehfollowup_id, vehfollowup_trigger, vehfollowup_followup_time,"
					+ " priorityvehfollwup_trigger1_hrs, priorityvehfollwup_trigger2_hrs, priorityvehfollwup_trigger3_hrs,"
					+ " priorityvehfollwup_trigger4_hrs, priorityvehfollwup_trigger5_hrs,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS executive, emp_mobile1,"
					+ " COALESCE(emp_device_fcmtoken,'') AS emp_device_fcmtoken,"
					+ " COALESCE(emp_device_os,'') AS emp_device_os,"
					+ " model_name, item_name,"
					+ " branch_email1, customer_id, customer_name, branch_name,"
					+ " comp_subdomain"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ "," + compdb(comp_id) + "axela_service_vehfollowup_priority"
					+ " WHERE vehfollowup_desc = ''"
					+ " AND vehfollowup_trigger < 5"
					+ " AND vehfollowup_followup_time <=" + ToLongDate(kknow())
					+ " AND branch_esc_serviceveh_followup = 1"
					+ " GROUP BY veh_id"
					+ " ORDER BY vehfollowup_trigger, vehfollowup_followup_time";
			// SOPInfo("dc vehicle followup cron----------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				endtime = crs.getString("vehfollowup_followup_time");
				diff = getMinutesBetween(endtime, ToLongDate(kknow()));
				
				branch_id = crs.getString("veh_branch_id");
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
				model_name = crs.getString("model_name");
				item_name = crs.getString("item_name");
				vehfollowup_followup_time = crs.getString("vehfollowup_followup_time");
				
				if (crs.getInt("vehfollowup_trigger") == 0 && !crs.getString("priorityvehfollwup_trigger1_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger1_hrs")))) {
					UpdateTrigger(crs.getString("veh_id"), crs.getString("vehfollowup_id"), "1");
					
				}
				if (crs.getInt("vehfollowup_trigger") == 1 && !crs.getString("priorityvehfollwup_trigger2_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger2_hrs")))) {
					UpdateTrigger(crs.getString("veh_id"), crs.getString("vehfollowup_id"), "2");
				}
				if (crs.getInt("vehfollowup_trigger") == 2 && !crs.getString("priorityvehfollwup_trigger3_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger3_hrs")))) {
					UpdateTrigger(crs.getString("veh_id"), crs.getString("vehfollowup_id"), "3");
				}
				if (crs.getInt("vehfollowup_trigger") == 3 && !crs.getString("priorityvehfollwup_trigger4_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger4_hrs")))) {
					UpdateTrigger(crs.getString("veh_id"), crs.getString("vehfollowup_id"), "4");
				}
				if (crs.getInt("vehfollowup_trigger") == 4 && !crs.getString("priorityvehfollwup_trigger5_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger5_hrs")))) {
					UpdateTrigger(crs.getString("veh_id"), crs.getString("vehfollowup_id"), "5");
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
	protected void UpdateTrigger(String veh_id, String vehfollowup_id, String TriggerLevel) {
		triggercount++;
		// SOP("triggercount==========" + triggercount);
		StringBuilder Str = new StringBuilder();
		String subject = "Vehicle Follow-up escalated to Level " + TriggerLevel + " for Vehicle ID " + veh_id;
		StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
				+ " SET vehfollowup_trigger = " + TriggerLevel + ""
				+ " WHERE vehfollowup_id = " + vehfollowup_id + "";
		// SOP("UpdateTrigger query......" + StrSql);
		updateQuery(StrSql);
		// try {
		// Str.append("<head><style>table {background-color:#FFF;border-style:solid;border-color:#1b62a9;border-collapse:collapse;}");
		// Str.append("td {padding:3px;line-height: 150%;FONT-SIZE: 12px;FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;}");
		// Str.append("</style></head>");
		// Str.append("\n <table border=1 width=600 cellspacing=0 cellpadding=0 >");
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Branch:</td>\n");
		// Str.append("<td align=left>" + branch_name + "</td>\n");
		// Str.append("</tr>\n");
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Executive:</td>\n");
		// Str.append("<td align=left>" + executive + "</td>\n");
		// Str.append("</tr>\n");
		// if (!emp_mobile1.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Executive Mobile:</td>\n");
		// Str.append("<td align=left>+").append(emp_mobile1).append("</td>\n");
		// Str.append("</tr>\n");
		// }
		// if (!customer_name.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Customer:</td>\n");
		// Str.append("<td align=left>" + customer_name + " (" + customer_id + ")</td>\n");
		// Str.append("</tr>\n");
		// }
		// if (!contact_name.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Contact:</td>\n");
		// Str.append("<td align=left>" + contact_name + "</td>\n");
		// Str.append("</tr>\n");
		// }
		// if (!contact_mobile1.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Contact Mobile:</td>\n");
		// Str.append("<td align=left>+").append(contact_mobile1).append("</td>\n");
		// Str.append("</tr>\n");
		// }
		// if (!contact_email1.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Contact Email:</td>\n");
		// Str.append("<td align=left><a href=mailto:" + contact_email1 + ">" + contact_email1 + "</a></td>\n");
		// Str.append("</tr>\n");
		// }
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Enquiry ID:</td>\n");
		// Str.append("<td align=left><a href=" + appurl + "service/vehicle-list.jsp?veh_id=" + veh_id + ">" + veh_id + "</a></td>\n");
		// Str.append("</tr>\n");
		// if (!model_name.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Model:</td>\n");
		// Str.append("<td align=left> " + model_name + "</td>\n");
		// Str.append("</tr>\n");
		// }
		// if (!item_name.equals("")) {
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Item:</td>\n");
		// Str.append("<td align=left>" + item_name + "</td>\n");
		// Str.append("</tr>\n");
		// }
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Follow-up Time:</td>\n");
		// Str.append("<td align=left><a href=" + appurl + "service/vehicle-dash.jsp?veh_id=" + veh_id + "#tabs-4>" + strToLongDate(vehfollowup_followup_time) + "</a></td>\n");
		// Str.append("</tr>\n");
		// Str.append("<tr>\n");
		// Str.append("<td align=left>Escalation Level:</td>\n");
		// Str.append("<td align=left>" + TriggerLevel + "</td>\n");
		// Str.append("</tr>\n");
		// Str.append("</table>\n");
		//
		// // if (!emp_email1.equals("") && emp_active.equals("1")) {
		// // if (isNotPublicEmail(emp_email1).equals("1") && AppRun().equals("1")) {
		// // postMail(emp_email1, GetEmps(TriggerLevel, "priorityenquiryfollowup",
		// // emp_id, branch_id, comp_id), "", branch_email1,
		// // subject, Str.toString(), "", comp_id);
		// // }
		// // }
		// // SOP("Mail===" + Str.toString());
		// // if (!emp_device_os.equals("") && !emp_device_fcmtoken.equals("")) {
		// // msg = "Vehicle ID: " + veh_id + "\n"
		// // + "Follow-up Time: " + strToLongDate(vehfollowup_followup_time) + "\n"
		// // + "Contact: " + contact_name + "\n"
		// // + "Mobile: " + contact_mobile1 + "\n";
		// // title = "Vehicle Follow-up Level " + TriggerLevel + " Escalation\n";
		// // // new FCM().SendPushNotification(emp_device_fcmtoken, emp_device_os, title, msg);
		// // }
		// } catch (Exception ex) {
		// SOPError(this.getClass().getName());
		// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// } finally {
		// crs.close();
		// }
		
	}
}
