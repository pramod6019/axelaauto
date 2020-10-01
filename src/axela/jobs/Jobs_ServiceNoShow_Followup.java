package axela.jobs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Jobs_ServiceNoShow_Followup extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	String appurl = "";
	int triggercount = 0;
	double diff = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				UpdateNoShowServiceFollowup();
			} else {
				comp_id = "1009";
				UpdateNoShowServiceFollowup();
			}

			StrHTML = "Service No Show Follow-up Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	void UpdateNoShowServiceFollowup()
			throws SQLException {
		triggercount++;
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = " UPDATE " + compdb(comp_id) + "axela_service_followup"
					+ " SET vehfollowup_desc ='No Show',"
					+ " vehfollowup_contactable_id = 2," // notcontactable
					+ " vehfollowup_modified_id = 1,"
					+ " vehfollowup_modified_time  = " + ToLongDate(kknow())
					+ " WHERE vehfollowup_id IN ( SELECT * FROM  ( "
					+ " SELECT vehfollowup_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = model_brand_id"
					+ " WHERE vehfollowup_desc = ''"
					+ " AND brandconfig_vehfollowup_noshow = 1"
					+ " AND brandconfig_vehfollowup_noshow_days != 0"
					+ " AND brandconfig_vehfollowup_noshow_futuredays != 0"
					// + " AND vehfollowup_veh_id IN ( SELECT vehfollowup_veh_id "
					// + " FROM " + compdb(comp_id) + "axela_service_followup "
					// + " WHERE vehfollowup_followup_main = 1"
					// + " AND SUBSTR(vehfollowup_followup_time,1,8) < SUBSTR('" + ToShortDate(kknow()) + "',1,8)"
					// + " AND DATEDIFF(SUBSTR('" + ToShortDate(kknow()) + "',1,8), SUBSTR(vehfollowup_followup_time,1,8)) >= brandconfig_vehfollowup_noshow_days)"
					+ " AND vehfollowup_veh_id NOT IN ( SELECT vehfollowup_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_followup_main = 1"
					+ " AND SUBSTR(vehfollowup_followup_time, 1, 8) >="
					+ " SUBSTR(DATE_FORMAT(DATE_SUB('" + ToLongDate(kknow()) + "', INTERVAL  brandconfig_vehfollowup_noshow_days DAY),'%Y%m%d%h%i%s'),1,8) )"
					+ " GROUP BY vehfollowup_veh_id"
					+ "  ) AS t)";

			// SOPInfo("StrSql===Update=" + StrSql);
			// adding for update follwup
			stmttx.addBatch(StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup "
					+ " (vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_vehcalltype_id,"
					+ " vehfollowup_contactable_id,"
					+ " vehfollowup_desc,"
					+ " vehfollowup_followup_main,"
					+ " vehfollowup_followup_time,"
					+ " vehfollowup_entry_id,"
					+ " vehfollowup_entry_time)"

					+ " SELECT vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " 96 ," // vehfollowup_vehcalltype_id
					+ " 0 ," // vehfollowup_contactable_id
					+ " ''," // vehfollowup_desc
					+ " 1, "// vehfollowup_followup_main
					+ " DATE_FORMAT(DATE_ADD(" + ToShortDate(kknow()) + ", INTERVAL  brandconfig_vehfollowup_noshow_futuredays DAY),'%Y%m%d%100000'),"// vehfollowup_followup_time
					+ " 1," // vehfollowup_entry_id
					+ "'" + ToLongDate(kknow()) + "'"// vehfollowup_entry_time
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = model_brand_id"
					+ " WHERE vehfollowup_desc = 'No Show'"
					+ " AND vehfollowup_modified_id = 1 "
					+ " AND SUBSTR(vehfollowup_modified_time,1,8) = SUBSTR('" + ToLongDate(kknow()) + "',1,8)"
					+ " AND vehfollowup_veh_id NOT IN ( SELECT dup.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup dup"
					+ " WHERE dup.vehfollowup_followup_main = 1"
					+ " AND dup.vehfollowup_vehcalltype_id = 96"
					+ " AND DATEDIFF(SUBSTR(dup.vehfollowup_followup_time,1,8), SUBSTR('" + ToShortDate(kknow()) + "',1,8)) = brandconfig_vehfollowup_noshow_futuredays)"
					+ " GROUP BY vehfollowup_veh_id";

			// adding for insert follwup
			// SOPInfo("StrSql===INSERT=" + StrSql);

			stmttx.addBatch(StrSql);
			stmttx.executeBatch();
			conntx.commit();

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			conntx.setAutoCommit(true);// Enables auto commit
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
