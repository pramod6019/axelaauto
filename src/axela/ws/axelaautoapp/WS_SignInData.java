package axela.ws.axelaautoapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import cloudify.connect.Connect;

public class WS_SignInData extends Connect {

	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String comp_logo = "", branch_logo = "";
	public String emp_branch_id = "0";
	public String emp_uuid = "0";
	public String emp_device_id = "";
	public String emp_name = "", emp_photo = "", emp_status = "";
	public int TotalRecords = 0;
	public String signinid = "";
	public String password = "";
	public String comp_name = "";
	public String url_image = "";
	public String url_exeimage = "";
	public String emp_exeaccess = "";
	public String emp_branchaccess = "";
	public Connection conn = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;
	Executive_Univ_Check update = new Executive_Univ_Check();
	public String comp_module_preowned = "0";
	private String comp_module_service = "0";
	private String comp_module_sales = "0";

	public JSONObject signindata(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input signin for TESTING ======== " + input);
		}
		JSONObject output = new JSONObject();
		try {
			if (!input.isNull("signinid")) {
				signinid = PadQuotes((String) input.get("signinid"));
			}
			if (!input.isNull("password")) {
				password = PadQuotes((String) input.get("password"));
			}
			if (!input.isNull("emp_device_id")) {
				emp_device_id = PadQuotes((String) input.get("emp_device_id"));
			}
			StrSql = "SELECT emp_id, emp_name, emp_email1, emp_upass, emp_branch_id, emp_uuid, comp_id,"
					+ " emp_role_id, emp_all_exe, emp_recperpage, emp_ip_access, emp_device_id"
					+ " FROM axela_uni_emp "
					+ " WHERE 1=1"
					+ " AND emp_active = 1"
					+ " AND emp_email1 = ? "
					+ " AND emp_upass = ? ";
			conn = connectDB();
			pstmt = conn.prepareStatement(StrSql);
			pstmt.setString(1, signinid);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					if (!password.equals(rs.getString("emp_upass"))) {
						emp_id = "0";
					} else {
						emp_id = rs.getString("emp_id");
						emp_name = rs.getString("emp_name");
						emp_branch_id = rs.getString("emp_branch_id");
						emp_uuid = rs.getString("emp_uuid");
						comp_id = rs.getString("comp_id");
						comp_name = ExecuteQuery("SELECT comp_name"
								+ " FROM " + compdb(comp_id) + "axela_comp"
								+ " WHERE comp_id = " + rs.getString("comp_id"));
						comp_module_preowned = CNumeric(ExecuteQuery("SELECT comp_module_preowned"
								+ " FROM " + compdb(comp_id) + "axela_comp"
								+ " WHERE comp_id = " + rs.getString("comp_id")));
						comp_module_sales = CNumeric(ExecuteQuery("SELECT comp_module_sales"
								+ " FROM " + compdb(comp_id) + "axela_comp"
								+ " WHERE comp_id = " + rs.getString("comp_id")));
						comp_module_service = CNumeric(ExecuteQuery("SELECT comp_module_service"
								+ " FROM " + compdb(comp_id) + "axela_comp"
								+ " WHERE comp_id = " + rs.getString("comp_id")));
						branch_logo = ExecuteQuery("SELECT COALESCE(branch_logo, '') AS branch_logo"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_id = " + rs.getString("emp_branch_id"));
						comp_logo = ExecuteQuery("SELECT comp_logo"
								+ " FROM " + compdb(comp_id) + "axela_comp"
								+ " WHERE comp_id = " + rs.getString("comp_id"));
						// if (!branch_logo.equals("")) {
						// comp_logo = branch_logo;
						// }
						emp_photo = ExecuteQuery("SELECT emp_photo"
								+ " FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_id);
						emp_status = unescapehtml(ExecuteQuery("SELECT emp_status"
								+ " FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_id));
						emp_status = emp_status.replace("\"", "");
						if (!branch_logo.equals("")) {
							url_image = WSUrl() + "thumbnail?image=" + branch_logo + "&path=branchlogo&width=500";

						} else {
							url_image = WSUrl() + "thumbnail?image=" + comp_logo + "&path=complogo&width=500";
						}

						if (!emp_photo.equals("")) {
							if (AppRun().equals("1")) {
								url_exeimage = WSUrl() + "thumbnail?image=" + emp_photo + "&path=userpath&width=500";
							} else {
								url_exeimage = WSUrl() + "thumbnail?image=" + emp_photo + "&path=userpath&width=500";
							}
						}
						if (!emp_id.equals("1")) {
							if (!emp_device_id.equals("")) {
								if (rs.getString("emp_device_id").equals("")) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_emp SET"
											+ " emp_device_id = '" + emp_device_id + "'"
											+ " WHERE emp_id =" + emp_id;
									updateQuery(StrSql);
									update.UpdateUniversalEmp(emp_id, comp_id);
									emp_device_id = "yes";
								} else {
									if (!rs.getString("emp_device_id").equals(emp_device_id)) {
										emp_device_id = "no";
									} else {
										emp_device_id = "yes";
									}
								}
								// updating the log for emp
								UpdateAppLog();
							} else {
								emp_device_id = "no";
							}
						} else {

							emp_device_id = "yes";

						}
					}
				}
			} else {
				emp_id = "0";
			}
			rs.close();
			/* End of Signin Block */
		} catch (Exception ex) {
			SOPError("Axelaauto-App ======" + this.getClass().getName());
			SOPError("Axelaauto-App===== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			output.put("emp_id", emp_id);
			output.put("emp_name", emp_name);
			if (emp_id.equals("1") || emp_id.equals("2")) {
				emp_branch_id = "1";
			}
			output.put("comp_module_sales", comp_module_sales);
			output.put("emp_branch_id", emp_branch_id);
			output.put("emp_device_id", emp_device_id);
			output.put("comp_module_service", comp_module_service);
			output.put("comp_module_preowned", comp_module_preowned);
			output.put("emp_uuid", emp_uuid);
			output.put("comp_id", comp_id);
			output.put("comp_name", comp_name);
			output.put("complogo", url_image);
			output.put("url_exeimage", url_exeimage);
			output.put("emp_photo", emp_photo);
			output.put("emp_status", emp_status);
			input = null;
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		if (AppRun().equals("0")) {
			SOP("output ==signin== " + output);
		}
		return output;
	}

	public void UpdateAppLog() {

		StrSql = "UPDATE " + compdb(comp_id) + "axela_emp_log"
				+ " SET log_signout_time = '" + ToLongDate(kknow()) + "'"
				+ " WHERE 1=1"
				+ " AND log_emp_app = 1"
				+ " AND log_signout_time = ''"
				+ " AND log_emp_id = " + emp_id;
		// SOP("StrSql====" + StrSql);
		updateQuery(StrSql);

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_log"
				+ " (log_emp_id,"
				+ " log_session_id,"
				+ " log_comp_id,"
				+ " log_emp_app,"
				+ " log_remote_host,"
				+ " log_remote_agent,"
				+ " log_attemptcount,"
				+ " log_signin_time, "
				+ " log_signout_time)"
				+ " VALUES"
				+ " (" + emp_id + ","
				+ " '',"
				+ " " + comp_id + ","
				+ "'1',"
				+ " '',"
				+ " '',"
				+ " '0',"
				+ " '" + ToLongDate(kknow()) + "',"
				+ " '')";
		updateQuery(StrSql);
	}

}
