package axela.axelaauto_app;
//aJIt 11th March, 2013

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class App_Veh_Quote_Item_Add_Check extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String addB = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String emp_id = "0";
	public String itemmaster_id = "0";
	public String group_id = "0";
	public String item_code = "", item_name = "", aftertax = "0";
	public String itemdetails = "", configItems = "";
	StringBuffer configItemsBuffer = new StringBuffer();
	public String line = "";
	public String itemadd = "";

	public String group_name = "";
	public String item_id = "0";
	public String item_qty = "";
	public String option_id = "0";
	public List<Map> itemList = new ArrayList<Map>();

	Gson gson = new Gson();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (ReturnPerm(comp_id, "emp_sales_order_cancel", request).equals("1")) {
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					addB = PadQuotes(request.getParameter("add_button"));
					emp_id = CNumeric(GetSession("emp_id", request));
					itemdetails = PadQuotes(request.getParameter("itemdetails"));
					itemadd = PadQuotes(request.getParameter("itemadd"));
					itemmaster_id = PadQuotes(request.getParameter("itemmaster_id"));
					group_id = PadQuotes(request.getParameter("group_id"));

					item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
					item_code = PadQuotes(request.getParameter("item_code"));
					aftertax = CNumeric(PadQuotes(request.getParameter("aftertax")));
					item_name = unescapehtml(JSONPadQuotes(request.getParameter("item_name")));
					item_qty = PadQuotes(request.getParameter("item_qty"));
					group_name = PadQuotes(request.getParameter("group_name"));
					option_id = CNumeric(PadQuotes(request.getParameter("option_id")));

					BufferedReader reader = request.getReader();
					while ((line = reader.readLine()) != null) {
						configItemsBuffer.append(line);
					}

					if (configItemsBuffer != null && configItemsBuffer.length() > 1) {
						SOP("configItemsBuffer=================" + configItemsBuffer);
						configItems = JSONPadQuotes(PadQuotes(configItemsBuffer.toString()));
						SOP("configItems==111==" + configItems);
					}

					if (itemdetails.equals("yes")) {
						CheckForm();
						if (msg.equals("")) {
							StrHTML = SearchItems();
						} else {
							StrHTML = msg;
						}
					}
					if (itemadd.equals("yes")) {
						StrHTML = "";
						StrHTML = CheckItemConfig();
						if (StrHTML.equals("")) {
							StrHTML = JSONPadQuotes(GetItemDetails());
						}
					}

				} else {
					StrHTML = "Access Denied!";
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	private String GetItemDetails() {
		List<Map> newConfigureList = new ArrayList<Map>();
		Map<String, String> newConfigureItem = new HashMap<String, String>();

		if (aftertax.equals("0")) {
			newConfigureItem.put("bt_group_name", unescapehtml(group_name));
			newConfigureItem.put("bt_groupadded", "new");
			newConfigureItem.put("bt_group_id", group_id);
			newConfigureItem.put("bt_item_code", unescapehtml(item_code));
			newConfigureItem.put("bt_item_name", item_name);
			newConfigureItem.put("bt_item_id", item_id);
			newConfigureItem.put("bt_item_qty", item_qty);
			newConfigureItem.put("bt_option_id", option_id);
		} else if (aftertax.equals("1")) {
			newConfigureItem.put("at_group_name", unescapehtml(group_name));
			newConfigureItem.put("at_groupadded", "new");
			newConfigureItem.put("at_group_id", group_id);
			newConfigureItem.put("at_item_code", unescapehtml(item_code));
			newConfigureItem.put("at_item_name", item_name);
			newConfigureItem.put("at_item_id", item_id);
			newConfigureItem.put("at_item_qty", item_qty);
			newConfigureItem.put("at_option_id", option_id);
		}

		newConfigureList.add(newConfigureItem);
		// SOP("newConfigureList==222==" + gson.toJson(newConfigureList));
		return gson.toJson(newConfigureList);
	}

	private String CheckItemConfig() throws JSONException {

		// This logic checks whether the selected Option is already added or not.
		String error_msg = "";
		String itemname = "";
		JSONArray jsonArray;
		jsonArray = new JSONArray(configItems);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject.get("group_name").equals(group_name)) {
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					itemname = String.valueOf(iterator.next());
					if (jsonObject.getString(itemname).equals(item_name)) {
						error_msg = "<font color=\"red\">Error!<br>Item is already added!</font>";
						break;
					}

				}
			}
		}

		// SOP("error_msg==CheckItemConfig==" + error_msg);
		return error_msg;
	}

	protected void CheckForm() {
		msg = "";
		if (group_id.equals("0")) {
			msg += "<center><b><font color=#ff0000>Select Group!</font></b></center>";
		}
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " AND (item_id LIKE '%" + item_name + "%'"
					+ " OR item_name LIKE '%" + item_name + "%'"
					+ " OR item_code LIKE '%" + item_name + "%' )";

			StrSql = "SELECT option_id, item_id, REPLACE(item_name, '+', '%2B') AS item_name, item_code, option_itemmaster_id, option_group_id, group_name, group_aftertax"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " WHERE option_itemmaster_id = " + itemmaster_id
					+ " AND option_group_id = " + group_id
					+ StrSearch
					+ " ORDER BY item_name"
					+ " LIMIT 5";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				// Str.append("<th data-toggle=\"true\">Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th data-hide=\"phone\">Qty</th>\n");
				Str.append("<th data-hide=\"phone\">Action</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {

					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					Str.append("\n<tr valign=top>");
					// Str.append("<td valign=top align=center>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td valign=top align=left>").append(item_name.replace("%2B", "+")).append("</td>");
					Str.append("<td valign=top align=left>");
					if (crs.getString("option_group_id").equals("13")) {
						Str.append("<input class='form-control' type='text' id='item_qty_" + crs.getString("item_id") + "' value='1'/>");
					} else {
						Str.append("<input class='form-control' type='text' id='item_qty_" + crs.getString("item_id") + "' value='1' disabled/>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=center><span class='btn1' onclick=\"AddItem(").append(crs.getString("item_id") + ",");
					Str.append("'" + crs.getString("group_aftertax") + "','" + crs.getString("item_name") + "','" + crs.getString("item_name") + "'," + crs.getString("option_id") + ",'"
							+ crs.getString("group_name")
							+ "');\">Add Item</span></td>");
					Str.append("</tr>");
					Str.append("</tbody>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs.close();
			} else {
				Str.append("<b><font color=#ff0000>No Items Found!</font></b>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void AddItems() {
	}
}
