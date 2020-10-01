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

import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Book_A_Car extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String add = "", type = "";
	public String addB = "";
	public String enquiry_user_id = "0";
	public String enquiry_title_id = "1";
	public String enquiry_fname = "";
	public String enquiry_lname = "";
	public String enquiry_mobile = "91-";
	public String enquiry_email = "";
	public String comp_id = "0";
	public String modelname = "", itemname = "", titledesc = "Mr.";
	public String enquiry_testdrivetype_id = "";
	public String enquiry_model_id = "0";
	public String enquiry_item_id = "";
	public String enquiry_comments = "";
	public String enquiry_time = "";
	public String enquiry_date = "";
	public String enquiry_team_id = "0";
	public String date = "";
	public String year = "", month = "", day = "";
	public String user_id = "0";
	public String model_id = "0";
	public String validatetestdrivetime = "";
	public String ostype = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	Enquiry_Quickadd enq = new Enquiry_Quickadd();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				add = PadQuotes(request.getParameter("add"));
				type = PadQuotes(request.getParameter("type"));
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				enquiry_model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				enquiry_item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ostype = PadQuotes(request.getParameter("ostype"));
				modelname = ExecuteQuery("SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model"
						+ " WHERE model_id = " + enquiry_model_id);
				if (!enquiry_item_id.equals("0")) {
					itemname = ExecuteQuery("SELECT item_name  FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_id = " + enquiry_item_id
							+ " AND item_active = 1 AND item_type_id = 1");
				}
				// itemname = ExecuteQuery("SELECT item_name FROM " + compdb(comp_id) + "axela_inventory_item"
				// + " WHERE item_id = " + enquiry_item_id);
				if (addB.equals("yes")) {
					String addmsg = "";
					if (type.equals("testdrive")) {
						addmsg = "Test Drive";
					} else {
						addmsg = "Enquiry";
					}
					addmsg += " Added Successfully!<br>Our Sales Executive shall contact you shortly!";
					enq.comp_id = comp_id;
					enq.emp_id = "36";
					enq.enquiry_branch_id = "1";
					enq.PopulateConfigDetails();
					enquiry_team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_emp_id = " + 36));
					GetValues(request, response);
					CheckForm();
					// SOP("msg=======" + msg);
					if (msg.equals("")) {

						enq.emp_id = "36";
						enq.comp_id = comp_id;
						// -----customer n contact-------
						enq.enquiry_branch_id = "1";
						enq.customer_name = enquiry_fname + " " + enquiry_lname;
						enq.contact_mobile1 = enquiry_mobile;
						enq.contact_mobile2 = "";
						enq.contact_city_id = "6";
						enq.enquiry_soe_id = "3";
						enq.enquiry_sob_id = "43";
						enq.enquiry_emp_id = "36";
						enq.enquiry_team_id = enquiry_team_id;
						enq.contact_address = "";
						enq.contact_pin = "";
						enq.contact_email1 = enquiry_email;
						enq.contact_email2 = "";
						enq.contact_phone1 = "";
						enq.contact_id = "0";
						enq.contact_title_id = enquiry_title_id;
						enq.contact_fname = enquiry_fname;
						enq.contact_lname = enquiry_lname;
						enq.contact_jobtitle = "";

						// -----enquiry-------
						enq.enquiry_title = enquiry_title_id;
						enq.enquiry_desc = enquiry_date + " " + enquiry_time + " " + enquiry_comments;
						enq.enquiry_date = strToShortDate(ToLongDate(kknow()));
						enq.enquiry_entry_date = ToLongDate(kknow());
						enq.enquiry_model_id = enquiry_model_id;
						enq.enquiry_item_id = enquiry_item_id;
						enq.enquiry_close_date = strToShortDate(ToLongDate(kknow()));
						enq.enquiry_campaign_id = "2";
						enq.enquiry_notes = "";
						enq.enquiry_buyertype_id = "1";
						enq.enquiry_qcsno = "";
						enq.enquiry_finance = "0";
						enq.enquiry_budget = "0";
						enq.enquiry_entry_id = "36";
						enq.PopulateConfigDetails();
						enq.AddEnquiryFields();
						msg = enq.msg;
						if (msg.equals("")) {
							// response.sendRedirect(response
							// .encodeRedirectURL("../ddmotors-app/success.jsp?&msg="
							// + addmsg));
							// msg = addmsg;
							response.sendRedirect(response.encodeRedirectURL("../ddmotors-app/callurlmodel-list.jsp?msg=" + addmsg));

						} else {
							msg = "Error! <br>" + unescapehtml(msg);
						}

					} else if (!msg.equals("")) {
						msg = "Error! <br>" + unescapehtml(msg);
					}
				}
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			enquiry_title_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_title_id")));
			if (!enquiry_title_id.equals("0")) {
				titledesc = ExecuteQuery("SELECT title_desc FROM " + compdb(comp_id) + "axela_title"
						+ " WHERE title_id = " + enquiry_title_id);
			}
			enquiry_fname = PadQuotes(request.getParameter("txt_enquiry_fname"));
			enquiry_lname = PadQuotes(request.getParameter("txt_enquiry_lname"));
			enquiry_mobile = PadQuotes(request.getParameter("txt_enquiry_mobile"));
			enquiry_email = PadQuotes(request.getParameter("txt_enquiry_email"));
			enquiry_model_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_model_id")));
			SOP("enquiry_model_id=======" + enquiry_model_id);
			// enquiry_item_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_item_id")));
			if (!enquiry_model_id.equals("0")) {
				enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE model_id = " + enquiry_model_id
						+ " AND item_active = 1 AND item_type_id = 1"
						+ " LIMIT 1 "));
			}
			SOP("enquiry_item_id=======" + enquiry_item_id);
			enquiry_date = PadQuotes(request.getParameter("txt_enquiry_date"));
			enquiry_time = PadQuotes(request.getParameter("txt_enquiry_time"));
			if (!enquiry_date.equals("")) {
				year = enquiry_date.split("-")[0];
				month = enquiry_date.split("-")[1];
				day = enquiry_date.split("-")[2];
				if (!enquiry_time.equals("")) {
					date = day + "/" + month + "/" + year + " " + enquiry_time;
				} else {
					date = day + "/" + month + "/" + year + " 00:00";
				}
			}
			enquiry_comments = PadQuotes(request.getParameter("txt_enquiry_comments"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void CheckForm() {
		if (enquiry_title_id.equals("0")) {
			msg = msg + "Select Title!<br>";
		}
		if (enquiry_fname.equals("")) {
			msg = msg + "Enter Name!<br>";
		}
		// if (enquiry_lname.equals("")) {
		// msg = msg + "Enter Last Name!<br>";
		// }
		if (enquiry_mobile.equals("")) {
			msg = msg + "Enter Mobile No.!<br>";
		}
		if (enquiry_email.equals("")) {
			msg = msg + "Enter Email!<br>";
		}
		if (enquiry_model_id.equals("0")) {
			msg = msg + "Select Model!<br>";
		}
		// if (date.equals("")) {
		// msg = msg + "Select Test Drive Date!<br>";
		// }
		// if (enquiry_time.equals("")) {
		// msg = msg + "Select Test Drive Time!<br>";
		// }
		// if (!date.equals("")) {
		// date = ConvertLongDateToStr(date);
		// if (Long.parseLong(date) < Long.parseLong(ToLongDate(kknow()))) {
		// msg = msg + "Test Drive Date cannot be less than " + strToLongDate(ToLongDate(kknow())) + "!";
		// }
		// }
	}

	public String PopulateJSONTitle(String enquiry_title_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

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
			output.put("selectedtitle_id", list);
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output.toString().replace("\\", "").replace("\"", "");
	}

	public String PopulateJSONModel() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1 AND model_active = '1' AND model_sales = '1'"
					+ " ORDER BY model_name";
			// SOP("StrSql------" + StrSql);
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

	public String PopulateModel(String enquiry_model_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT model_id, model_name" + " FROM " + compdb(comp_id)
					+ "axela_inventory_item_model"
					+ " WHERE 1 = 1 AND model_active = '1'"
					+ " ORDER BY model_name";
			SOPError("PopulateModel SQL------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("model_id"),
						enquiry_model_id));
				Str.append(">").append(crs.getString("model_name"))
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

	public String PopulateItem(String enquiry_model_id, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE 1=1";
			StrSql += " AND item_model_id =" + CNumeric(enquiry_model_id);
			StrSql += " AND item_type_id = 1 AND item_active = '1' AND model_active = '1' AND model_sales = '1'"
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("PopulateItem -------------" + StrSqlBreaker(StrSql));

			map.put("item_id", "'0'");
			map.put("item_name", "'Select'");
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", ("'" + unescapehtml(crs.getString("item_id")) + "'"));
					map.put("item_name", ("'" + unescapehtml(crs.getString("item_name")) + "'"));
					list.add(gson.toJson(map));
				}
			} else {
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

	// public String PopulateItem(String enquiry_model_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id)
	// + "axela_inventory_item" + " WHERE 1=1";
	// if (!enquiry_model_id.equals("0")) {
	// StrSql += " AND item_model_id =" + enquiry_model_id;
	// }
	// StrSql += " AND item_type_id = 1 AND item_active = '1'"
	// + " ORDER BY item_name";
	// // SOP("PopulateItem -------------" + StrSqlBreaker(StrSql));
	//
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<select class=\"form-control\" name=\"dr_enquiry_item_id\" id= \"dr_enquiry_item_id\" >");
	// Str.append("<option value=0> Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("item_id"))
	// .append("");
	// Str.append(StrSelectdrop(crs.getString("item_id"),
	// enquiry_item_id));
	// Str.append(">").append(crs.getString("item_name"))
	// .append("</option>\n");
	// }
	// crs.close();
	// Str.append("</select>");
	// // Str.append("<label for=\"form_control_1\">Variant *</label></span>");
	// return Str.toString();
	//
	// } catch (Exception ex) {
	// SOPError(this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// return "";
	// }
	// }

}
