package axela.ws.sales;
//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Salesorder_Dash_Customer extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String so_id = "0";
	public String emp_uuid = "0";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_enquirytype_id = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_communication = "";
	public String customer_address = "";
	public String customer_landmark = "";
	public String customer_notes = "";
	public String customer_active = "";
	public String listitems = "";
	public String startdate = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject SalesOrder_Dash_Customer(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}
		if (!input.isNull("customer_id")) {
			customer_id = CNumeric(PadQuotes((String) input.get("customer_id")));
		}

		try {
			StrSql = "SELECT customer_name, customer_address, customer_landmark,"
					+ " customer_phone1, customer_phone2, customer_phone3, customer_phone4,"
					+ " customer_mobile1, customer_mobile2, customer_fax1, customer_fax2,"
					+ " customer_email1, customer_email2, customer_emp_id,"
					+ " customer_website1, customer_website2, customer_pin, customer_notes,"
					+ " customer_active, COALESCE(city_name, '') AS city_name,"
					+ " concat(emp_name,' (', emp_ref_no, ')') AS customer_exe"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = customer_emp_id"
					+ " WHERE customer_id = " + customer_id + ""
					+ " GROUP BY customer_id";
			// SOP("cust === " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("customer_name", crs.getString("customer_name"));
					if (!crs.getString("customer_phone1").equals("")) {
						map.put("customer_phone1", crs.getString("customer_phone1"));
					}
					if (!crs.getString("customer_phone2").equals("")) {
						map.put("customer_phone2", crs.getString("customer_phone"));
					}
					if (!crs.getString("customer_phone3").equals("")) {
						map.put("customer_phone3", crs.getString("customer_phone3"));
					}
					if (!crs.getString("customer_phone4").equals("")) {
						map.put("customer_phone4", crs.getString("customer_phone4"));
					}
					if (!crs.getString("customer_mobile1").equals("")) {
						map.put("customer_mobile1", crs.getString("customer_mobile1"));
					}
					if (!crs.getString("customer_mobile2").equals("")) {
						map.put("customer_mobile2", crs.getString("customer_mobile2"));
					}
					if (!crs.getString("customer_fax1").equals("")) {
						map.put("customer_fax1", crs.getString("customer_fax1"));
					}
					if (!crs.getString("customer_fax2").equals("")) {
						map.put("customer_fax2", crs.getString("customer_fax2"));
					}
					if (!crs.getString("customer_email1").equals("")) {
						map.put("customer_email1", crs.getString("customer_email1"));
					}
					if (!crs.getString("customer_email2").equals("")) {
						map.put("customer_email2", crs.getString("customer_email2"));
					}
					if (!crs.getString("customer_website1").equals("")) {
						map.put("customer_website1", crs.getString("customer_website1"));
					}
					if (!crs.getString("customer_website2").equals("")) {
						map.put("customer_website2", crs.getString("customer_website2"));
					}
					map.put("customer_pin", crs.getString("customer_pin"));
					map.put("customer_exe", crs.getString("customer_exe"));
					map.put("customer_address", crs.getString("customer_address"));
					map.put("customer_landmark", crs.getString("customer_landmark"));
					map.put("customer_notes", crs.getString("customer_notes"));
					map.put("customer_active", crs.getString("customer_active"));
					map.put("city_name", crs.getString("city_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("customerdetails", list);
				list.clear();
				// if(listitems.equals("yes")){
				ListContact();
				// }
			} else {
				output.put("msg", "No Records Found!");
			}
			if (AppRun().equals("0")) {
				SOP("output = " + output);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		// }
		// }
		return output;
	}

	public JSONObject ListContact() {
		// int count = 0;
		// String active = "";
		// String address = "", img = "";
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,"
					+ " contact_jobtitle, contact_phone1,"
					+ " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
					+ " contact_email1, contact_email2, contact_yahoo, contact_msn, contact_aol,"
					+ " contact_address, contact_pin, contact_landmark, contact_dob, contact_active,"
					+ " COALESCE(city_name,'') AS city_name, customer_id, customer_name, coalesce(branch_name,'')as branch,"
					+ " COALESCE(branch_code,'')AS branch_code"
					// + "contact_photo,  contact_company, contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " WHERE contact_customer_id = " + customer_id;
			// SOP("StrSql---listcontact---" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					map.put("contact_id", crs.getString("contact_id"));
					map.put("contact_customer_id", crs.getString("contact_customer_id"));
					map.put("contact_name", crs.getString("contact_name"));
					map.put("contact_jobtitle", crs.getString("contact_jobtitle"));
					map.put("contact_phone1", crs.getString("contact_phone1"));
					map.put("contact_phone2", crs.getString("contact_phone2"));
					map.put("contact_mobile1", crs.getString("contact_mobile1"));
					map.put("contact_mobile2", crs.getString("contact_mobile2"));
					map.put("contact_anniversary", crs.getString("contact_anniversary"));
					map.put("contact_email1", crs.getString("contact_email1"));
					map.put("contact_email2", crs.getString("contact_email2"));
					map.put("contact_yahoo", crs.getString("contact_yahoo"));
					map.put("contact_msn", crs.getString("contact_msn"));
					map.put("contact_aol", crs.getString("contact_aol"));
					map.put("contact_address", crs.getString("contact_address"));
					map.put("contact_pin", crs.getString("contact_pin"));
					map.put("contact_landmark", crs.getString("contact_landmark"));
					map.put("contact_dob", crs.getString("contact_dob"));
					map.put("contact_active", crs.getString("contact_active"));
					map.put("city_name", crs.getString("city_name"));
					map.put("customer_id", crs.getString("customer_id"));
					map.put("customer_name", crs.getString("customer_name"));
					map.put("branch", crs.getString("branch"));
					map.put("branch_code", crs.getString("branch_code"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			}

			map.clear();
			output.put("listcontact", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
