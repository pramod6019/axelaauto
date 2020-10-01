package axela.ddmotors_app;
//Shilpa 14 dec 2015

import java.io.IOException;
import java.sql.SQLException;
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

public class Book_A_Service extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String user_id = "0";
	public String comp_id = "0";
	public String service_user_id = "0";
	public String user_name = "";
	public String service_title_id = "1";
	public String service_fname = "";
	public String service_lname = "";
	public String service_mobile = "91-";
	public String service_email = "";
	public String service_reg_no = "";
	public String service_slot = "";
	public String service_time = "";
	public String time[];
	public String service_comments = "";
	public String service_entry_time = "";
	public String validateservicetime = "";
	public String year = "", month = "", day = "";
	public String ostype = "";
	public String titledesc = "Mr.";
	public String service_id = "0";
	public String branch_jc_new_email_enable = "0";
	public String branch_jc_new_email_format = "";
	public String branch_jc_new_email_sub = "";
	public String branch_jc_new_sms_enable = "0";
	public String branch_jc_new_sms_format = "";
	public String branch_jc_new_email_exe_sub = "";
	public String branch_jc_new_email_exe_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				add = PadQuotes(request.getParameter("add"));
				service_id = CNumeric(PadQuotes(request.getParameter("service_id")));
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ostype = PadQuotes(request.getParameter("ostype"));
				// first time
				if (!addB.equals("yes")) {
					StrSql = "SELECT user_title_id, user_fname, user_lname, user_email, user_mobile\n"
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_id = " + user_id;
					// SOP("strsql===============" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						service_title_id = crs.getString("user_title_id");
						service_fname = crs.getString("user_fname");
						service_lname = crs.getString("user_lname");
						service_email = crs.getString("user_email");
						service_mobile = crs.getString("user_mobile");
					}
					crs.close();
					// PopulateSlot();
					// PopulateTitle();
				} else if (addB.equals("yes")) {
					GetValues(request, response);
					// CheckForm();
					if (msg.equals("")) {
						String addmsg = "Service request added successfully,"
								+ " our Service Manager will be in touch with you shortly.<br>"
								+ " Have a nice day!";
						StrSql = "SELECT service_id"
								+ " FROM " + compdb(comp_id) + "axela_app_service"
								+ " WHERE SUBSTR(service_time,1,8) = SUBSTR('" + ToLongDate(kknow()) + "',1,8)"
								+ " LIMIT 1";
						validateservicetime = ExecuteQuery(StrSql);
						if (validateservicetime.equals("")) {
							// if (msg.equals("")) {
							AddServiceFields();
							msg = addmsg;
							response.sendRedirect(response.encodeRedirectURL("../ddmotors-app/success.jsp?&msg=" + addmsg));
							// } else if (!msg.equals("")) {
							// msg = "Error!" + unescapehtml(msg);
							// }
						} else {
							msg = addmsg;
							response.sendRedirect(response.encodeRedirectURL("../ddmotors-app/success.jsp?&msg=" + addmsg));
						}
					}
				} else {
					msg = "Error!" + unescapehtml(msg);
				}
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
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
					+ " ('91-" + service_mobile + "',"
					+ " " + service_title_id + ","
					+ " '" + service_fname + "',"
					+ " '" + service_lname + "',"
					+ " '',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("strsql====" + StrSql);
			user_id = UpdateQueryReturnID(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			service_title_id = CNumeric(PadQuotes(request.getParameter("txt_service_title_id")));
			if (!service_title_id.equals("0")) {
				titledesc = ExecuteQuery("SELECT title_desc FROM " + compdb(comp_id) + "axela_title"
						+ " WHERE title_id = " + service_title_id);
			}
			service_fname = PadQuotes(request.getParameter("txt_service_fname"));
			service_lname = PadQuotes(request.getParameter("txt_service_lname"));
			service_mobile = PadQuotes(request.getParameter("txt_service_mobile"));
			service_email = PadQuotes(request.getParameter("txt_service_email"));
			service_reg_no = PadQuotes(request.getParameter("txt_service_reg_no"));
			// service_time = PadQuotes(request.getParameter("txt_service_time"));
			// if (!service_time.equals("")) {
			// year = service_time.split("-")[0];
			// month = service_time.split("-")[1];
			// day = service_time.split("-")[2];
			// service_time = day + "/" + month + "/" + year;
			// }

			service_slot = CNumeric(PadQuotes(request.getParameter("txt_service_slot_id")));
			service_comments = PadQuotes(request.getParameter("txt_service_comments"));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// public void CheckForm() {
	// if (service_title_id.equals("0")) {
	// msg = msg + "Select Title!<br>";
	// }
	// if (service_fname.equals("")) {
	// msg = msg + "Enter Name!<br>";
	// }
	// // if (service_lname.equals("")) {
	// // msg = msg + "Enter Last Name!<br>";
	// // }
	// if (service_mobile.equals("")) {
	// msg = msg + "Enter Mobile No.!<br>";
	// }
	// if (service_email.equals("")) {
	// msg = msg + "Enter Email!<br>";
	// }
	// if (service_reg_no.equals("")) {
	// msg = msg + "Enter Reg No.!<br>";
	// }
	// // if (service_time.equals("")) {
	// // msg = msg + "Enter Date!<br>";
	// // }
	// // if (service_slot.equals("")) {
	// // msg = msg + "Select Slot!";
	// // }
	// }

	public void AddServiceFields() {
		try {
			service_id = ExecuteQuery("SELECT COALESCE(MAX(service_id),0+1) + 1  FROM " + compdb(comp_id) + "axela_app_service");
			// SOP("service_id----1-----" + service_id);
			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_app_service "
					+ " ("
					+ "service_user_id,"
					+ " service_title_id,"
					+ " service_fname,"
					+ " service_lname,"
					+ " service_mobile,"
					+ " service_email,"
					+ " service_reg_no,"
					+ " service_time,"
					+ " service_slot,"
					+ " service_comments,"
					+ " service_entry_time)"
					+ " VALUES"
					+ " ("
					+ service_user_id + ","
					+ " " + service_title_id + ","
					+ " '" + service_fname + "',"
					+ " '" + service_lname + "',"
					+ " '" + service_mobile + "',"
					+ " '" + service_email + "',"
					+ " '" + service_reg_no + "',"
					+ " '" + ConvertShortDateToStr(service_time) + "',"
					+ " '" + service_slot + "',"
					+ " '" + service_comments + "',"
					+ " '" + ToLongDate(kknow()) + "')";
			SOPError("StrSql Service add-------------" + StrSql);
			updateQuery(StrSql);
			// stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			// CachedRowSet crs =stmttx.getGeneratedKeys();
			// SOP("aaa");
			// while (crs.next()) {
			// SOP("bbb");
			// service_id = crs.getString(1);
			// SOP("service_id---------" + service_id);
			// }
			// crs.close();
			if (!service_id.equals("0"))
			{
				SendEmail();
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateJSONSlot() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		CachedRowSet crs = null;
		try {
			map.put("slot_id", "'0'");
			map.put("slot_name", "'Select'");
			list.add(gson.toJson(map));
			map.put("slot_id", ("'1'"));
			map.put("slot_name", ("'8am - 1pm'"));
			list.add(gson.toJson(map));
			map.put("slot_id", ("'2'"));
			map.put("slot_name", ("'1pm - 3pm'"));
			list.add(gson.toJson(map));
			map.put("slot_id", ("'3'"));
			map.put("slot_name", ("'3pm - 6pm'"));
			list.add(gson.toJson(map));
			map.clear();
			output.put("populateslot", list);

			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-app ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

	public String PopulateSlot() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", service_slot))
				.append(">8am - 1pm</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", service_slot))
				.append(">1pm - 3pm</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", service_slot))
				.append(">3pm - 6pm</option>\n");
		return Str.toString();
	}

	public String PopulateJSONTitle(String testdrive_title_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("title_id", "'0'");
				map.put("title_desc", "'Select'");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("title_id", "'" + crs.getString("title_id") + "'");
					map.put("title_desc", "'" + crs.getString("title_desc") + "'");
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
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

	public String PopulateTitle(String testdrive_title_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id)
					+ "axela_title" + " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("title_id"),
						testdrive_title_id));
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

	protected void SendEmail() throws SQLException {

		try {

			String email_from = "info@ddmotocrs.in";
			String email_to = "rupika.behl@ddmotocrs.net,corp.smr@ddmotocrs.net";
			String email_cc = "info@ddmotocrs.in,sujay@emax.in";
			String email_subject = "New Message From DD Motors Services ";
			StringBuilder email_msg = new StringBuilder();
			StrSql = " SELECT service_fname, service_lname, service_mobile, service_email, service_reg_no, service_comments "
					+ " FROM " + compdb(comp_id) + "axela_app_service"
					+ " WHERE service_id = " + service_id + "";
			// SOP("StrSql----qqq-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (!crs.getString("service_fname").equals(""))
				{
					email_msg.append("<p>").append("First Name:").append(crs.getString("service_fname")).append("</p>").append("<br>");
				}
				if (!crs.getString("service_lname").equals(""))
				{
					email_msg.append("<p>").append("Last Name:").append(crs.getString("service_lname")).append("</p>").append("<br>");
				}

				if (!crs.getString("service_mobile").equals(""))
				{
					email_msg.append("<p>").append("Mobile:").append(crs.getString("service_mobile")).append("</p>");
				}
				if (!crs.getString("service_email").equals(""))
				{
					email_msg.append("<p>").append("Email:").append(crs.getString("service_email")).append("</p>");
				}
				if (!crs.getString("service_reg_no").equals(""))
				{
					email_msg.append("<p>").append("Vehicle No:").append(crs.getString("service_reg_no")).append("</p>");
				}
				if (!crs.getString("service_comments").equals(""))
				{
					email_msg.append("<p>").append("Comments:").append(crs.getString("service_comments")).append("</p>");
				}

			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " VALUES("
					+ " '" + email_from + "',"
					+ " '" + email_to + "',"
					+ " '" + email_cc + "',"
					+ " '" + email_subject + "',"
					+ " '" + email_msg + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + 1 + ","
					+ " 0 )";
			// SOP("StrSql-------SendEmail------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

}
