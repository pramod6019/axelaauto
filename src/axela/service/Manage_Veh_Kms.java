//$atiSh 24-Oct-2013
package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Manage_Veh_Kms extends Connect {

	public String add = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess = "";
	public String update = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String empEditperm = "";
	public String chkPermMsg = "";
	public String QueryString = "";
	public String config_veh_kms_date = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			// CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// SOP("emp_id====" + emp_id);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				update = PadQuotes(request.getParameter("Update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = unescapehtml(PadQuotes(request.getParameter("msg")));
				QueryString = PadQuotes(request.getQueryString());
				// empEditperm = ExecuteQuery("Select emp_service_vehicle_edit from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
				if (update.equals("yes")) {
					status = "Update";
				}
				config_veh_kms_date = strToLongDate(ExecuteQuery("SELECT config_veh_kms_date FROM " + compdb(comp_id) + "axela_config"));

				if ("Update Vehicle Kms".equals(updateB)) {
					msg = "";
					VehKmsUpdate("0", comp_id);
					if (msg.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("manage-veh-kms.jsp?msg=Vehicle Kms Updated Successfully!"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("manage-veh-kms.jsp?msg=Error!" + msg));
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
	}

	public void VehKmsUpdate(String veh_id, String comp_id) throws SQLException {
		try {
			String default_mileage = "30";

			StrSql = "UPDATE "
					+ compdb(comp_id)
					+ "axela_service_veh v1"
					+ " INNER JOIN (SELECT veh_id, calkms"
					+ " FROM (SELECT veh_id, @default_mileage:="
					+ default_mileage
					+ ","
					+ " @kmscount:=COALESCE((SELECT COUNT(vehkms_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " WHERE vehkms_veh_id = veh_id), '0') AS kmscount,"
					+ " @date1:=COALESCE((SELECT vehkms_entry_date"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " WHERE vehkms_veh_id = veh_id"
					+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS date1,"
					+ " @kms1:=COALESCE((SELECT vehkms_kms"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " WHERE vehkms_veh_id = veh_id"
					+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS kms1,"
					+ " @date2:=COALESCE((SELECT vehkms_entry_date"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " WHERE vehkms_veh_id = veh_id"
					+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS date2,"
					+ " @kms2:=COALESCE((SELECT vehkms_kms"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " WHERE vehkms_veh_id = veh_id"
					+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS kms2,"
					+ " IF(@kmscount>1, COALESCE((veh_kms + ((@kms1 - @kms2)/DATEDIFF(@date1, @date2))*DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms + (@kms1-@kms2))),"
					+ " (IF(@kmscount=1, COALESCE((veh_kms + @default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms+@default_mileage)),"
					+ " COALESCE(@default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', veh_sale_date),@default_mileage))))"
					+ " AS calkms,"
					+ " IF (@kmscount > 1,"
					+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / ((@kms1 - @kms2) / DATEDIFF(@date1, @date2)) ,0),"
					+ "	(IF(@kmscount = 1,"
					+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0),"
					+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0)))"
					+ " ) AS calservicedate,"
					+ " IF(@kmscount > 1, COALESCE(@date1, ''), (IF(@kmscount = 1, COALESCE(@date1, ''), veh_sale_date )))"
					+ " AS lastservice_date"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE 1=1";
			// + " AND veh_vehsource_id = 2";
			if (!veh_id.equals("0"))
				StrSql += " AND veh_id = " + veh_id;
			StrSql += " GROUP BY veh_id"
					+ " ORDER BY veh_id) Sat) v2"
					+ " SET"
					+ " v1.veh_cal_kms = v2.calkms,"
					+ " v1.veh_calservicedate = DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL IF(@calserviceday< 365, @calserviceday, 365) DAY),'%Y%m%d%h%i%s')"
					+ " WHERE v1.veh_id = v2.veh_id";
			// SOP("StrSql----vehicle kms update-----------" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
					+ " (vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_followup_time)"
					+ " SELECT veh_id, veh_crmemp_id,"
					+ " CONCAT(substr(" + ToShortDate(kknow()) + ",1,8), 100000)"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE 1=1"
					+ " AND veh_calservicedate <= DATE_FORMAT(DATE_ADD('" + ToLongDate(kknow()) + "', INTERVAL 1 MONTH),'%Y%m%d%h%i%s') "
					+ " AND veh_id NOT IN (SELECT vehfollowup_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup "
					+ " WHERE 1=1"
					+ " AND vehfollowup_veh_id = veh_id AND vehfollowup_desc = '')"
					+ " AND veh_id NOT IN (SELECT booking_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " WHERE 1 = 1"
					+ " AND booking_veh_id = veh_id"
					+ " AND booking_bookingstatus_id = 1"
					+ " AND booking_time > DATE_FORMAT(DATE_SUB('" + ToLongDate(kknow()) + "', INTERVAL 15 DAY),'%Y%m%d%h%i%s'))";
			if (!veh_id.equals("0")) {
				StrSql += " AND veh_id = " + veh_id;
			}
			StrSql += " ORDER BY veh_calservicedate limit 3000";
			// SOP("strsql====add veh followup=" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);

			// updating veh kms date
			if (veh_id.equals("0")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_config"
						+ " SET"
						+ " config_veh_kms_date = '" + ToLongDate(kknow()) + "'";
				updateQuery(StrSql);
			}
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
