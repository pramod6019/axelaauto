package axela.ws.sales;

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
	public String comp_logo = "";
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
	Executive_Univ_Check update = new Executive_Univ_Check();
	public Connection conn1 = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;

	public JSONObject signindata(JSONObject input) throws Exception {
		// if (AppRun().equals("0")) {
		SOPError("input sign======== " + input);
		// }
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
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			StrSql = "SELECT emp_id, emp_name, emp_email1, emp_upass, emp_branch_id, emp_uuid, comp_id,"
					+ " emp_role_id, emp_all_exe, emp_recperpage, emp_ip_access, emp_device_id"
					+ " FROM axela_uni_emp"
					+ " WHERE emp_sales = '1' AND emp_active = '1'"
					// + " AND (emp_branch_id !=0 OR (emp_id = 1 or emp_id = 2))"
					+ " AND emp_email1 = ? "
					+ " AND emp_upass = ? ";
			// SOPError("StrSql-----" + StrSql);

			conn1 = connectDB();
			pstmt = conn1.prepareStatement(StrSql);
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
						comp_name = ExecuteQuery("SELECT comp_name from " + compdb(comp_id) + "axela_comp where comp_id = " + rs.getString("comp_id"));
						comp_logo = ExecuteQuery("SELECT comp_logo from " + compdb(comp_id) + "axela_comp where comp_id = " + rs.getString("comp_id"));
						emp_photo = ExecuteQuery("SELECT emp_photo from " + compdb(comp_id) + "axela_emp where emp_id = " + emp_id);
						emp_status = ExecuteQuery("SELECT emp_status from " + compdb(comp_id) + "axela_emp where emp_id = " + emp_id);
						if (!comp_logo.equals("")) {
							if (AppRun().equals("1")) {
								url_image = WSUrl() + "thumbnail?image=" + comp_logo + "&path=complogo&width=500";
							} else {
								url_image = "http://192.168.0.15:8030/axelaauto/ws/" + "sales/thumbnail?image=" + comp_logo + "&path=complogo&width=500";
							}
						}

						if (!emp_photo.equals("")) {
							if (AppRun().equals("1")) {
								url_exeimage = WSUrl() + "thumbnail?image=" + emp_photo + "&path=userpath&width=500";
							} else {
								url_exeimage = "http://192.168.0.15:8030/axelaauto/ws/" + "sales/thumbnail?image=" + emp_photo + "&path=userpath&width=500";
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
			SOPError("Axelaauto =======" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			output.put("emp_id", emp_id);
			output.put("emp_name", emp_name);
			if (emp_id.equals("1") || emp_id.equals("2")) {
				emp_branch_id = "1";
			}
			output.put("emp_branch_id", emp_branch_id);
			output.put("emp_device_id", emp_device_id);
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
			if (conn1 != null && !conn1.isClosed()) {
				conn1.close();
			}
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}

		return output;
	}
}
