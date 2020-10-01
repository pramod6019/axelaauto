package axela.jobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_ServicePSF_Followup extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	String appurl = "";
	int triggercount = 0;
	double diff = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
			} else {
				comp_id = "1009";
			}
			ServicePSFFollowup();
			StrHTML = "Service PSF Followup Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}
	// for psf branch to band
	public void ServicePSFFollowup() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT jcpsf_jc_id, jcpsf_id, jcpsf_trigger, branch_id, "
					+ " jcpsf_followup_time, comp_subdomain "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand on brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE 1 = 1 "
					// + " AND enquiry_status_id=1 "
					+ " AND jcpsf_desc = ''"
					+ " AND psfdays_active = 1"
					+ " AND jcpsf_trigger < 5"
					+ " AND branch_esc_servicepsf_followup = 1"
					+ " AND jcpsf_followup_time <= " + ToLongDate(kknow())
					+ " GROUP BY jcpsf_id"
					+ " ORDER BY jcpsf_trigger, jcpsf_followup_time";
			// SOP("PSF select trigger query......" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				String endtime = crs.getString("jcpsf_followup_time");
				if (crs.getInt("jcpsf_trigger") == 0 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("jcpsf_jc_id"), crs.getString("jcpsf_id"), "1");
				}
				if (crs.getInt("jcpsf_trigger") == 1 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("jcpsf_jc_id"), crs.getString("jcpsf_id"), "2");
				}
				if (crs.getInt("jcpsf_trigger") == 2 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("jcpsf_jc_id"), crs.getString("jcpsf_id"), "3");
				}
				if (crs.getInt("jcpsf_trigger") == 3 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("jcpsf_jc_id"), crs.getString("jcpsf_id"), "4");
				}
				if (crs.getInt("jcpsf_trigger") == 4 && (diff >= ConvertHoursToMins("24:00"))) {
					UpdateTrigger(crs.getString("jcpsf_jc_id"), crs.getString("jcpsf_id"), "5");
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
	void UpdateTrigger(String jc_id, String psffollowup_id, String TriggerLevel) throws SQLException {
		// SOP("triggercount===" + triggercount);
		triggercount++;
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		// SOP("psffollowup_id===" + psffollowup_id);
		StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf "
				+ " SET jcpsf_trigger=" + TriggerLevel + " "
				+ " WHERE jcpsf_id=" + psffollowup_id + " ";
		// SOP("StrSql = " + StrSql);
		updateQuery(StrSql);
		try {
			StrSql = "SELECT jc_id, branch_id, jcstage_name, customer_id, customer_name, "
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, "
					+ " contact_mobile1, contact_email1, emp_email1, emp_id, emp_active, "
					+ " branch_name, branch_email1, CONCAT(emp_name,' (',emp_ref_no,')') AS Executive, emp_mobile1, "
					+ " preownedmodel_name, variant_name,"
					+ " jcpsf_trigger, jcpsf_followup_time,"
					+ " jcpsf_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " LEFT JOIN axela_brand on brand_id = psfdays_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jcpsf_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id "
					+ " LEFT JOIN axelaauto.axela_preowned_model  ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id "
					+ " WHERE jcpsf_id = " + psffollowup_id + "";
			// SOP("select PSF mail query---------" + StrSql);
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
				Str.append("<td align=left>Job Card ID:</td>\n");
				Str.append("<td align=left><a href=").append(appurl).append("service/jobcard-list.jsp?jc_id=");
				Str.append(crs.getString("jc_id")).append(">").append(crs.getString("jc_id")).append("</a></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Model:</td>\n");
				Str.append("<td align=left> ").append(crs.getString("preownedmodel_name")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("variant_name").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>Item:</td>\n");
					Str.append("<td align=left>").append(crs.getString("variant_name")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>").append("Service PSF Follow-up Time:</td>\n");
				Str.append("<td align=left><a href=").append(appurl);
				Str.append("service/jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("#tabs-3>");
				Str.append(strToLongDate(crs.getString("jcpsf_followup_time"))).append("</a></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=left>Stage:</td>\n");
				Str.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>\n");
				Str.append("</tr>\n");
				if (!crs.getString("jcpsf_desc").equals("")) {
					Str.append("<tr>\n");
					Str.append("<td align=left>").append("Service PSF Follow-up Description:</td>\n");
					Str.append("<td align=left>").append(crs.getString("jcpsf_desc")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left>Escalation Level:</td>\n");
				Str.append("<td align=left>").append(crs.getString("jcpsf_trigger")).append("</td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				String subject = "Service PSF Follow-up escalated to Level " + TriggerLevel + " for Enquiry ID " + jc_id;
				if (!crs.getString("emp_email1").equals("") && crs.getString("emp_active").equals("1")) {
					if (isNotPublicEmail(crs.getString("emp_email1")).equals("1") && AppRun().equals("1")) {
						postMail(crs.getString("emp_email1"), GetEmps(TriggerLevel, "prioritycrmfollowup",
								crs.getString("emp_id"), crs.getString("branch_id"), comp_id), "", crs.getString("branch_email1"),
								subject, Str.toString(), "", comp_id);
					}
				}
				// SOP("Mail===" + Str.toString());
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
