package axela.ddmotors_app;
//Shilpa 14 dec 2015
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Book_A_TestDrive extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String testdrive_user_id = "0";
	public String testdrive_title_id = "0";
	public String testdrive_fname = "";
	public String testdrive_lname = "";
	public String testdrive_mobile = "";
	public String testdrive_email = "";
	public String comp_id = "0";
	public String testdrive_testdrivetype_id = "";
	public String testdrive_model_id = "0";
	public String testdrive_item_id = "0";
	public String testdrive_comments = "";
	public String time = "", date = "";
	public String testdrive_time = "";
	public String testdrive_date = "";
	public String user_id = "0";
	public String model_id = "0";
	public String validatetestdrivetime = "";
	public String ostype = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				add = PadQuotes(request.getParameter("add"));
				testdrive_user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				testdrive_model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ostype = PadQuotes(request.getParameter("ostype"));
				time = ToLongDate(kknow());

				// first time
				if (!addB.equals("yes")) {
					StrSql = "SELECT user_title_id, user_fname, user_lname, user_email, user_mobile\n"
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_id = " + testdrive_user_id;
					// SOP("strsql========" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						testdrive_title_id = crs.getString("user_title_id");
						testdrive_fname = crs.getString("user_fname");
						testdrive_lname = crs.getString("user_lname");
						testdrive_email = crs.getString("user_email");
						testdrive_mobile = crs.getString("user_mobile");
					}
					crs.close();

				} else if (addB.equals("yes")) {
					String addmsg = "Test Drive request added successfully,"
							+ " our sales consultant will be in touch with you shortly.<br>"
							+ " Have a nice day!";
					StrSql = "SELECT testdrive_id"
							+ " FROM " + compdb(comp_id) + "axela_app_testdrive"
							+ " WHERE SUBSTR(testdrive_time,1,8) = SUBSTR('" + ToLongDate(kknow()) + "',1,8)"
							+ " LIMIT 1";
					validatetestdrivetime = ExecuteQuery(StrSql);
					if (validatetestdrivetime.equals("")) {
						GetValues(request, response);
						msg = "";
						if (msg.equals("")) {
							AddTestDrive();
							msg = addmsg;
							response.sendRedirect(response.encodeRedirectURL("../ddmotors-app/success.jsp?&msg=" + addmsg));
						} else if (!msg.equals("")) {
							msg = "Error!" + unescapehtml(msg);
						}
					}
				}
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			testdrive_title_id = CNumeric(PadQuotes(request.getParameter("txt_testdrive_title_id")));
			testdrive_fname = PadQuotes(request.getParameter("txt_testdrive_fname"));
			testdrive_lname = PadQuotes(request.getParameter("txt_testdrive_lname"));
			testdrive_mobile = PadQuotes(request.getParameter("txt_testdrive_mobile"));
			testdrive_email = PadQuotes(request.getParameter("txt_testdrive_email"));
			testdrive_model_id = CNumeric(PadQuotes(request.getParameter("txt_testdrive_model_id")));
			testdrive_item_id = CNumeric(PadQuotes(request.getParameter("txt_testdrive_item_id")));
			time = PadQuotes(request.getParameter("txt_testdrive_time"));
			date = PadQuotes(request.getParameter("txt_testdrive_date"));
			testdrive_comments = PadQuotes(request.getParameter("txt_testdrive_comments"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public void CheckForm() {
	// if (testdrive_title_id.equals("0")) {
	// msg = msg + "Select Title!<br>";
	// }
	// if (testdrive_fname.equals("")) {
	// msg = msg + "Enter First Name!<br>";
	// }
	// if (testdrive_lname.equals("")) {
	// msg = msg + "Enter Last Name!<br>";
	// }
	// if (testdrive_mobile.equals("")) {
	// msg = msg + "Enter Mobile No.!<br>";
	// }
	// if (testdrive_email.equals("")) {
	// msg = msg + "Enter Email!<br>";
	// }
	// if (testdrive_model_id.equals("0")) {
	// msg = msg + "Select Model!<br>";
	// }
	// if (date.equals("")) {
	// msg = msg + "Select Test Drive Date!<br>";
	// }
	// if (time.equals("")) {
	// msg = msg + "Select Test Drive Time!<br>";
	// }
	// if (!time.equals("") && !date.equals("")) {
	// testdrive_time = date + " " + time;
	// testdrive_time = ConvertLongDateToStr(testdrive_time);
	// if (Long.parseLong(testdrive_time) < Long.parseLong(ToLongDate(kknow()))) {
	// msg = msg + "Test Drive Date cannot be less than " + strToLongDate(ToLongDate(kknow())) + "!";
	// }
	// }
	// }
	public void AddTestDrive() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_testdrive"
					+ " (testdrive_user_id,"
					+ " testdrive_title_id,"
					+ " testdrive_fname,"
					+ " testdrive_lname,"
					+ " testdrive_mobile,"
					+ " testdrive_email,"
					+ " testdrive_testdrivetype_id,"
					+ " testdrive_model_id,"
					+ " testdrive_comments,"
					+ " testdrive_time,"
					+ " testdrive_entry_time)"
					+ " VALUES"
					+ " (" + user_id + ","
					+ " " + testdrive_title_id + ","
					+ " '" + testdrive_fname + "',"
					+ " '" + testdrive_lname + "',"
					+ " '" + testdrive_mobile + "',"
					+ " '" + testdrive_email + "',"
					+ " '1',"
					+ " '" + testdrive_model_id + "',"
					+ " '" + testdrive_comments + "',"
					+ " '" + testdrive_time + "',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOPError("StrSql testdrive add = " + StrSqlBreaker(StrSql));
			updateQuery(StrSql);

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateJSONTitle(String testdrive_title_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		CachedRowSet crs = null;
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			// SOP("STrSql===" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("title_id", "'0'");
				map.put("title_desc", "'Select'");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("title_id", ("'" + crs.getString("title_id") + "'"));
					map.put("title_desc", ("'" + crs.getString("title_desc") + "'"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("title_id", "'0'");
				map.put("title_desc", "'Select'");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatetitle", list);

			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

	public String PopulateTitle(String enquiry_title_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id)
					+ "axela_title" + " ORDER BY title_desc";
			// SOP("STrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("title_id"),
						enquiry_title_id));
				Str.append(">").append(crs.getString("title_desc"))
						.append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public void AddFields() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_user"
					+ " (user_mobile,"
					+ " user_title_id,"
					+ " user_fname,"
					+ " user_lname,"
					+ " user_pass,"
					+ " user_otp_time)"
					+ " VALUES"
					+ " ('91-" + testdrive_mobile + "',"
					+ " " + testdrive_title_id + ","
					+ " '" + testdrive_fname + "',"
					+ " '" + testdrive_lname + "',"
					+ " '',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("strsql====" + StrSql);
			user_id = UpdateQueryReturnID(StrSql);

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1 AND model_active = '1'"
					+ " ORDER BY model_name";
			SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "'0'");
				map.put("model_name", "'Select'");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("model_id", ("'" + crs.getString("model_id") + "'"));
					map.put("model_name", ("'" + crs.getString("model_name") + "'"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("model_id", "'0'");
				map.put("model_name", "'Select'");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemodel", list);

			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

	public String PopulateItem(String testdrive_model_id, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE 1=1";
			StrSql += " AND item_model_id =" + CNumeric(testdrive_model_id);
			StrSql += " AND item_type_id = 1 AND item_active = '1' AND model_active = '1' AND model_sales = '1'"
					+ " ORDER BY item_name";
			// SOP("PopulateItem -------------" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "'0'");
				map.put("item_name", "'Select'");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", ("'" + unescapehtml(crs.getString("item_id")) + "'"));
					map.put("item_name", ("'" + unescapehtml(crs.getString("item_name")) + "'"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("item_id", "'0'");
				map.put("item_name", "'Select'");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateitem", list);

			list.clear();
			crs.close();

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

}
