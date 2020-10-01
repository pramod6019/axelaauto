package axela.jobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Insurance_Followup extends Connect {

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
				ServiceInsuranceFollowup();
			} else {
				comp_id = "1000";
				ServiceInsuranceFollowup();
			}
			StrHTML = "Insurance Followup Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void ServiceInsuranceFollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT insurenquiry_id, insurenquiryfollowup_id, insurenquiryfollowup_trigger, insurenquiry_branch_id,"
					+ " insurenquiryfollowup_followup_time, comp_subdomain,"
					+ " priorityinsurfollowup_trigger1_hrs,"
					+ " priorityinsurfollowup_trigger2_hrs,"
					+ " priorityinsurfollowup_trigger3_hrs,"
					+ " priorityinsurfollowup_trigger4_hrs,"
					+ " priorityinsurfollowup_trigger5_hrs"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
					+ "," + compdb(comp_id) + "axela_insurance_followup_priority"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE 1 = 1 "
					+ " AND insurenquiryfollowup_disp1 = '' "
					+ " AND insurenquiryfollowup_trigger < 5"
					+ " AND insurenquiryfollowup_followup_time <= " + ToLongDate(kknow())
					+ " GROUP BY insurenquiryfollowup_id "
					+ " ORDER BY insurenquiryfollowup_trigger DESC,"
					+ " insurenquiryfollowup_followup_time";
			// SOP("StrSql-----ServiceInsuranceFollowup---" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				String endtime = crs.getString("insurenquiryfollowup_followup_time");
				diff = getMinutesBetween(endtime, ToLongDate(kknow()));
				if (crs.getInt("insurenquiryfollowup_trigger") == 0 && !crs.getString("priorityinsurfollowup_trigger1_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityinsurfollowup_trigger1_hrs")))) {
					UpdateTrigger(crs.getString("insurenquiry_id"), crs.getString("insurenquiryfollowup_id"), "1");
				}
				if (crs.getInt("insurenquiryfollowup_trigger") == 1 && !crs.getString("priorityinsurfollowup_trigger2_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityinsurfollowup_trigger2_hrs")))) {
					UpdateTrigger(crs.getString("insurenquiry_id"), crs.getString("insurenquiryfollowup_id"), "2");
				}
				if (crs.getInt("insurenquiryfollowup_trigger") == 2 && !crs.getString("priorityinsurfollowup_trigger3_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityinsurfollowup_trigger3_hrs")))) {
					UpdateTrigger(crs.getString("insurenquiry_id"), crs.getString("insurenquiryfollowup_id"), "3");
				}
				if (crs.getInt("insurenquiryfollowup_trigger") == 3 && !crs.getString("priorityinsurfollowup_trigger4_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityinsurfollowup_trigger4_hrs")))) {
					UpdateTrigger(crs.getString("insurenquiry_id"), crs.getString("insurenquiryfollowup_id"), "4");

				}
				if (crs.getInt("insurenquiryfollowup_trigger") == 4 && !crs.getString("priorityinsurfollowup_trigger5_hrs").equals("0:00")
						&& (diff >= ConvertHoursToMins(crs.getString("priorityinsurfollowup_trigger5_hrs")))) {
					UpdateTrigger(crs.getString("insurenquiry_id"), crs.getString("insurenquiryfollowup_id"), "5");
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
	void UpdateTrigger(String insur_enquiry_id, String followup_id, String TriggerLevel) throws SQLException {
		triggercount++;
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();

		StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry_followup "
				+ " SET insurenquiryfollowup_trigger =" + TriggerLevel + " "
				+ " WHERE insurenquiryfollowup_id =" + followup_id + " ";
		// SOP("StrSql--------" + StrSqlBreaker(StrSql));
		updateQuery(StrSql);
		try {
			// StrSql = "Select veh_id , veh_branch_id, insurenquiryfollowup_id, customer_id, customer_name, "
			// + " concat(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name, "
			// + " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active, "
			// + " branch_name, branch_email1, concat(emp_name,' (',emp_ref_no,')') as Executive, emp_mobile1, "
			// + " model_name, item_name,"
			// + " insurenquiryfollowup_trigger, insurfollowup_followup_time,"
			// + " insurenquiryfollowup_comments"
			// + " FROM " + compdb(comp_id) + "axela_insurance_followup "
			// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry on veh_id = insurenquiry_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = veh_contact_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
			// + " WHERE insurenquiryfollowup_id = " + followup_id;
			//
			// // SOP("StrSql------2--" + StrSqlBreaker(StrSql));
			// crs = processQuery(StrSql, 0);
			// while (crs.next()) {
			// Str.append("<head><style>table {background-color:#FFF;border-style:solid;border-color:#1b62a9;border-collapse:collapse;}");
			// Str.append("td {padding:3px;line-height: 150%;FONT-SIZE: 12px;FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;}");
			// Str.append("</style></head>");
			// Str.append("\n <table border=1 width=600 cellspacing=0 cellpadding=0 >");
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Branch:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
			// Str.append("</tr>\n");
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Executive:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("Executive")).append("</td>\n");
			// Str.append("</tr>\n");
			// if (!crs.getString("emp_mobile1").equals("")) {
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Executive Mobile:</td>\n");
			// Str.append("<td align=left>").append("+").append(crs.getString("emp_mobile1")).append("</td>\n");
			// Str.append("</tr>\n");
			// }
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Customer:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("customer_name")).append(" (");
			// Str.append(crs.getString("customer_id")).append(")</td>\n");
			// Str.append("</tr>\n");
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Contact:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("contact_name")).append("</td>\n");
			// Str.append("</tr>\n");
			// if (!crs.getString("contact_mobile1").equals("")) {
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Mobile:</td>\n");
			// Str.append("<td align=left>").append("+").append(crs.getString("contact_mobile1")).append("</td>\n");
			// Str.append("</tr>\n");
			// }
			// if (!crs.getString("contact_email1").equals("")) {
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Email:</td>\n");
			// Str.append("<td align=left><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a></td>\n");
			// Str.append("</tr>\n");
			// }
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Vehicle ID:</td>\n");
			// Str.append("<td align=left><a href=").append(appurl).append("service/vehicle-list.jsp?veh_id=");
			// Str.append(crs.getString("veh_id")).append(">").append(crs.getString("veh_id")).append("</a></td>\n");
			// Str.append("</tr>\n");
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Model:</td>\n");
			// Str.append("<td align=left> ").append(crs.getString("model_name")).append("</td>\n");
			// Str.append("</tr>\n");
			// if (!crs.getString("item_name").equals("")) {
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Item:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("item_name")).append("</td>\n");
			// Str.append("</tr>\n");
			// }
			// Str.append("<tr>\n");
			// Str.append("<td align=left>").append("Insurance Follow-up Time:</td>\n");
			// Str.append("<td align=left><a href=").append(appurl);
			// Str.append("service/vehicel-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("#tabs-10>");
			// Str.append(strToLongDate(crs.getString("insurfollowup_followup_time"))).append("</a></td>\n");
			// Str.append("</tr>\n");
			// if (!crs.getString("insurenquiryfollowup_comments").equals("")) {
			// Str.append("<tr>\n");
			// Str.append("<td align=left>").append("Insurance Follow-up Description:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("insurenquiryfollowup_comments")).append("</td>\n");
			// Str.append("</tr>\n");
			// }
			// Str.append("<tr>\n");
			// Str.append("<td align=left>Escalation Level:</td>\n");
			// Str.append("<td align=left>").append(crs.getString("insurenquiryfollowup_trigger")).append("</td>\n");
			// Str.append("</tr>\n");
			// Str.append("</table>\n");
			// String subject = "Insurance Follow-up escalated to Level " + TriggerLevel + " for Vehicle ID " + insur_enquiry_id;
			// if (!crs.getString("emp_email1").equals("") && crs.getString("emp_active").equals("1")) {
			// if (isNotPublicEmail(crs.getString("emp_email1")).equals("1") && AppRun().equals("1")) {
			// postMail(crs.getString("emp_email1"), GetEmps(TriggerLevel, "prioritycrmfollowup",
			// crs.getString("emp_id"), crs.getString("veh_branch_id"), comp_id), "", crs.getString("branch_email1"),
			// subject, Str.toString(), "", comp_id);
			// }
			// }
			// }
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			// crs.close();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
