package axela.ws.axelaautoapp;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class WS_GPSData extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String emp_uuid = "0";
	public String gps_emp_id = "";
	public String gps_latitude = "";
	public String gps_longitude = "";
	public String active = "";
	public String comp_id = "";
	public double lat = 0, lan = 0;
	JSONObject output = new JSONObject();

	public JSONObject gps(JSONObject input) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOP("input gps = " + input);
			}

			if (!input.isNull("gps_comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("gps_comp_id")));
			}

			if (!input.isNull("gps_emp_id")) {
				gps_emp_id = CNumeric(PadQuotes((String) input.get("gps_emp_id")));
			}
			if (!input.isNull("gps_latitude")) {
				gps_latitude = PadQuotes((String) input.get("gps_latitude"));

				if (gps_latitude.length() > 15) {
					gps_latitude = gps_latitude.substring(0, 15);
				}
				lat = Double.parseDouble((String) input.get("gps_latitude"));
			}
			if (!input.isNull("gps_longitude")) {
				gps_longitude = PadQuotes((String) input.get("gps_longitude"));

				if (gps_longitude.length() > 15) {
					gps_longitude = gps_longitude.substring(0, 15);
				}
				lan = Double.parseDouble((String) input.get("gps_longitude"));
			}

			// if (!input.isNull("gps_comp_id")) {
			// gps_comp_id = PadQuotes((String) input.get("gps_comp_id"));
			// }
			active = ExecuteQuery("SELECT emp_active FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + gps_emp_id + "");
			// if (isNumeric(gps_latitude) && !gps_latitude.equals("") && isNumeric(gps_longitude) && !gps_longitude.equals("") && active.equals("1")) {
			if (!gps_latitude.equals("") && !gps_longitude.equals("") && active.equals("1")) {
				if (lat != 0 && lan != 0) {
					AddFields();
				} else {
					output.put("msg", "Location Not Found!");
				}
			}

		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Axelaauto_app===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			input = null;
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	public void CheckForm() {
		msg = "";
		if (gps_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		if (gps_latitude.equals("")) {
			msg = msg + "<br>Enter latitude!";
		}
		if (gps_longitude.equals("")) {
			msg = msg + "<br>Enter longitude!";
		}
	}

	public void AddFields() {
		try {
			CheckForm();

			if (msg.equals("")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_gps"
						+ " (gps_emp_id,"
						+ " gps_latitude,"
						+ " gps_longitude,"
						+ " gps_time)"
						+ " VALUES"
						+ " (" + gps_emp_id + ","
						+ "" + lat + ","
						+ "" + lan + ","
						+ " " + ToLongDate(kknow()) + ")";
				// SOP("StrSql---INSERT-----" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
				output.put("msg", "GPS added successfully!");
			} else {
				output.put("msg", "Error!" + msg);
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Axelaauto_app===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
