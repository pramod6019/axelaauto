package axela.ws.sales;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Add_Quote extends Connect {

	public String StrSql = "";
	public String enquiry_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String model = "";
	public String model_id = "0";
	public String customer_address = "";
	public String customer_pin = "";
	public String quote_enquiry_id = "0";
	public String item_id = "0";
	public String item_model_id = "0";
	public String msg = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject AddQuote(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		Connection conn1 = null;
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!input.isNull("model")) {
				model = PadQuotes((String) input.get("model"));
			}
			if (!input.isNull("model_id")) {
				item_model_id = CNumeric(PadQuotes((String) input.get("model_id")));
			}
			if (item_model_id.equals("0")) {
				StrSql = "SELECT customer_address, customer_pin"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_customer_id = customer_id "
						+ " WHERE enquiry_id = " + enquiry_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					customer_address = crs.getString("customer_address");
					customer_pin = crs.getString("customer_pin");
				}
				if (customer_address.equals("") || customer_pin.equals("")) {
					if (customer_address.equals("")) {
						msg = "Update Customer Address!";
					}
					if (customer_pin.equals("")) {
						msg = msg + " <br> Update Customer Pin! ";
					}
				}
				crs.close();
			}
			if (msg.equals("")) {
				SOP("in");
				if (!emp_id.equals("0") && !enquiry_id.equals("")) {
					EnquiryDetails();
				}
				PopulateItem();

				if (!emp_id.equals("0") && !enquiry_id.equals("")) {
					StrSql = "SELECT model_id, model_name"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
							+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
							+ " WHERE model_active = 1"
							+ " AND model_sales = '1'"
							+ " GROUP BY model_id"
							+ " ORDER BY model_name";
					// SOP("StrSql-------" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						map.put("model_id", "0");
						map.put("model_name", "Select");
						list.add(gson.toJson(map));
						while (crs.next()) {
							map.put("model_id", crs.getString("model_id"));
							map.put("model_name", crs.getString("model_name"));
							list.add(gson.toJson(map));
						}
						map.clear();
						output.put("populatemodel", list);
						list.clear();
					} else {
						map.put("model_id", "0");
						map.put("model_name", "Select");
						list.add(gson.toJson(map));
						output.put("msg", msg);
					}
					crs.close();
				} else {
					output.put("populatemodel", "");
				}
			} else {
				output.put("msg", msg);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// finally {
		// input = null;
		// }
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_model_id = " + item_model_id + ""
					+ " AND item_model_id != 0"
					+ " AND item_type_id = 1"
					+ " AND item_active = 1"
					+ " ORDER BY item_name";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", crs.getString("item_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateitem", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
	public JSONObject EnquiryDetails() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT enquiry_model_id, enquiry_item_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + "";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					output.put("item_id", crs.getString("enquiry_item_id"));
					output.put("model_id", crs.getString("enquiry_model_id"));
					item_id = crs.getString("enquiry_item_id");
					item_model_id = crs.getString("enquiry_model_id");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

}
