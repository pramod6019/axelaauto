package axela.sales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Veh_Quote_New_Check extends Connect {

	public String cat_id = "0";
	public String enquiry_id = "0";
	public String branch_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String q = "";
	public String StrHTML = "", StrSearch = "";
	public String status = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	public String get_config = "";
	public String changeStockID = "", changeStockComm = "";

	public String quote_id = "0", vehstock_id = "0", vehstock_comm_no = "";
	public String quote_date = "";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df1 = new DecimalFormat("0");

	public Connection conntx = null;
	public Statement stmttx = null;
	CachedRowSet crs = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String item_id = "0", item_name = "", model_id = "0", model_name = "";

	public String newItemDetails = "", configItems = "", line = "";
	StringBuffer configItemsBuffer = new StringBuffer();
	public Map<String, String> newConfiguredbtItem;
	public ArrayList<Map> newItemList;
	public ArrayList<Map> configuredItemList = new ArrayList<Map>();

	// JSONObject
	Gson gson = new Gson();
	JSONObject input = new JSONObject();

	// Script
	private ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine engine = manager.getEngineByName("JavaScript");

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				vehstock_comm_no = PadQuotes(request.getParameter("vehstock_comm_no"));
				changeStockID = PadQuotes(request.getParameter("changeStockID"));
				changeStockComm = PadQuotes(request.getParameter("changeStockComm"));

				get_config = PadQuotes(request.getParameter("get_config"));
				status = PadQuotes(request.getParameter("status"));
				quote_date = PadQuotes(request.getParameter("quote_date"));
				newItemDetails = PadQuotes(request.getParameter("newItemDetails"));

				CheckVehStock();
				if (StrHTML.equals("")) {
					if (!newItemDetails.equals("")) {
						JsonReader reader = new JsonReader(new StringReader(newItemDetails));
						reader.setLenient(true);
						newItemList = gson.fromJson(reader, ArrayList.class);
					}
					BufferedReader reader = request.getReader();
					while ((line = reader.readLine()) != null) {
						configItemsBuffer.append(line);
					}

					if (configItemsBuffer != null && configItemsBuffer.length() > 1) {
						configItems = PadQuotes(configItemsBuffer.toString());
						if (!configItems.equals("") && !configItems.equals("\"\"")) {
							configItems = JSONPadQuotes(configItems);
							getListFromJSON(configItems);
							// SOP("configuredItemList==full data==111==" + configuredItemList.toString());
						}

					}

					cat_id = cat_id.replaceAll("nbsp", "&");
					q = PadQuotes(request.getParameter("q"));
					q = q.replaceAll("nbsp", "&");
					if (get_config.equals("yes") && !branch_id.equals("0")) {// calling from quote update
						if (quote_date.equals("")) {
							StrHTML = "<font color=\"red\"><b>Enter Quote Date!</b></font>";
						} else if (!isValidDateFormatShort(quote_date)) {
							StrHTML = "<font color=\"red\"><b>Enter valid Quote Date!</b></font>";
						} else {
							if (changeStockID.equals("yes") || changeStockComm.equals("yes")) {
								StrHTML = new Veh_Quote_Update_New()
										.GetConfigurationDetails(request, quote_id, item_id, branch_id, vehstock_id, vehstock_comm_no, "0",
												ConvertShortDateToStr(quote_date), enquiry_id, null, null,
												comp_id);
							} else {
								StrHTML = new Veh_Quote_Update_New()
										.GetConfigurationDetails(request, quote_id, item_id, branch_id, vehstock_id, vehstock_comm_no, "0",
												ConvertShortDateToStr(quote_date), enquiry_id,
												configuredItemList,
												newItemList,
												comp_id);
							}

						}
					} else {
						StrHTML = "Session Expired!";
					}
				} else {
					SOP("item_name==" + item_name);
					StrHTML = "____" + StrHTML + "__" + vehstock_comm_no + "," + vehstock_id + "," + item_name + "," + item_id + "," + model_name + "," + model_id;
				}
			}
		}
	}
	protected void CheckVehStock() {
		try {
			if ((changeStockID.equals("yes") && !vehstock_id.equals("0")) || (changeStockComm.equals("yes") && !vehstock_comm_no.equals(""))) {
				StrSql = "SELECT vehstock_id, vehstock_comm_no, item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
						+ "	COALESCE(model_id, '0') AS model_id, COALESCE(model_name, '') AS model_name"
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
				if (changeStockID.equals("yes") && !vehstock_id.equals("0")) {
					StrSql += " WHERE vehstock_id = " + vehstock_id;
				} else if (changeStockComm.equals("yes") && !vehstock_comm_no.equals("")) {
					StrSql += " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'";
				}
				SOP("StrSql==ID==" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						vehstock_id = crs.getString("vehstock_id");
						vehstock_comm_no = crs.getString("vehstock_comm_no");
						item_id = crs.getString("item_id");
						item_name = unescapehtml(crs.getString("item_name"));
						model_id = crs.getString("model_id");
						model_name = crs.getString("model_name");
					}
				} else {
					StrHTML = "<font color=\"red\"><b>Invalid Stock!</b></font>";
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void getListFromJSON(String configitems) {
		String mapName = "";
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(configItems);
			HashMap<String, String> configuredItem = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				configuredItem = new HashMap<String, String>();
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					mapName = String.valueOf(iterator.next());
					configuredItem.put(mapName, jsonObject.getString(mapName));
				}
				configuredItemList.add(configuredItem);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
