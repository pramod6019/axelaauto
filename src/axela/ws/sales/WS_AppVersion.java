package axela.ws.sales;
//Divya

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

public class WS_AppVersion extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String emp_device_id = "";
	JSONObject output = new JSONObject();

	public JSONObject appversion(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}

		if (!input.isNull("emp_id")) {
			emp_id = PadQuotes((String) input.get("emp_id"));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}

		if (!input.isNull("emp_device_id")) {
			emp_device_id = PadQuotes((String) input.get("emp_device_id"));
		}

		if (!emp_id.equals("0")) {

			StrSql = "SELECT config_app_ver, emp_device_id"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + emp_id;
			CachedRowSet crs =processQuery(StrSql, 0);
			// SOP("strsql==appver==" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					output.put("config_app_ver", crs.getString("config_app_ver"));

					if (!emp_id.equals("1")) {
						if (!emp_device_id.equals("")) {
							if (crs.getString("emp_device_id").equals("")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_emp SET"
										+ " emp_device_id = '" + emp_device_id + "'"
										+ " WHERE emp_id =" + emp_id;
								updateQuery(StrSql);
								output.put("emp_device_id", "yes");
							} else {
								if (!crs.getString("emp_device_id").equals(emp_device_id)) {
									output.put("emp_device_id", "no");
								} else {
									output.put("emp_device_id", "yes");
								}
							}
						} else {
							output.put("emp_device_id", "no");
						}
					} else {
						output.put("emp_device_id", "yes");
					}
				}
			} else {
				output.put("msg", "Invalid Request!");
			}
			crs.close();
		} else {
			output.put("msg", "Invalid Request!");
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

}
