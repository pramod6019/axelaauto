// 5 march 1985
/*======Saiman 10th May 2013=====*/
package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Ticket_Escalation extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";

	String appurl = "";
	int triggercount = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				TicketEscalation();
			} else {
				comp_id = "1000";
				TicketEscalation();// demo
				Thread.sleep(100);
				comp_id = "1011"; // Indel
				TicketEscalation();
			}

			StrHTML = "Ticket Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void TicketEscalation() throws SQLException {
		CachedRowSet crs = null;
		try {
			triggercount = 0;
			StrSql = "SELECT ticket_id, ticket_trigger, ticket_due_time,"
					+ " ticket_trigger1_hrs, ticket_trigger2_hrs, ticket_trigger3_hrs, "
					+ " ticket_trigger4_hrs, ticket_trigger5_hrs,"
					+ " comp_subdomain"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ "," + compdb(comp_id) + "axela_comp"
					+ " WHERE ticket_ticketstatus_id  NOT IN (3, 6, 7)"
					+ " AND ticket_trigger < 5"
					+ " AND (ticket_trigger1_hrs != '0.0'"
					+ " OR ticket_trigger2_hrs != '0.0'"
					+ " OR ticket_trigger3_hrs != '0.0'"
					+ " OR ticket_trigger4_hrs != '0.0'"
					+ " OR ticket_trigger5_hrs != '0.0')"
					+ " AND ticket_due_time <=" + ToLongDate(kknow())
					+ " GROUP BY ticket_id"
					+ " ORDER BY ticket_trigger, ticket_due_time";
			// SOPInfo("ticket esclation----------" + kknow() + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {

				if (crs.getInt("ticket_trigger") == 0 && !crs.getString("ticket_trigger1_hrs").equals("0.0")
						&& !crs.getString("ticket_trigger1_hrs").equals("")
						&& (Long.parseLong(crs.getString("ticket_trigger1_hrs")) < Long.parseLong(ToLongDate(kknow())))) {
					UpdateTrigger(crs.getString("ticket_id"), "1");
				}
				if (crs.getInt("ticket_trigger") == 1 && !crs.getString("ticket_trigger2_hrs").equals("0.0")
						&& !crs.getString("ticket_trigger2_hrs").equals("")
						&& (Long.parseLong(crs.getString("ticket_trigger2_hrs")) < Long.parseLong(ToLongDate(kknow())))) {
					UpdateTrigger(crs.getString("ticket_id"), "2");
				}
				if (crs.getInt("ticket_trigger") == 2 && !crs.getString("ticket_trigger3_hrs").equals("0.0")
						&& !crs.getString("ticket_trigger3_hrs").equals("")
						&& (Long.parseLong(crs.getString("ticket_trigger3_hrs")) < Long.parseLong(ToLongDate(kknow())))) {
					UpdateTrigger(crs.getString("ticket_id"), "3");
				}
				if (crs.getInt("ticket_trigger") == 3 && !crs.getString("ticket_trigger4_hrs").equals("0.0")
						&& !crs.getString("ticket_trigger4_hrs").equals("")
						&& (Long.parseLong(crs.getString("ticket_trigger4_hrs")) < Long.parseLong(ToLongDate(kknow())))) {
					UpdateTrigger(crs.getString("ticket_id"), "4");
				}
				if (crs.getInt("ticket_trigger") == 4 && !crs.getString("ticket_trigger5_hrs").equals("0.0")
						&& !crs.getString("ticket_trigger5_hrs").equals("")
						&& (Long.parseLong(crs.getString("ticket_trigger5_hrs")) < Long.parseLong(ToLongDate(kknow())))) {
					UpdateTrigger(crs.getString("ticket_id"), "5");
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
	protected void UpdateTrigger(String ticket_id, String TriggerLevel) {
		triggercount++;
		StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket"
				+ " SET ticket_trigger = " + TriggerLevel + ""
				+ " WHERE ticket_id = " + ticket_id + "";
		// SOP("UpdateTrigger-----" + StrSql);
		updateQuery(StrSql);
	}
}
