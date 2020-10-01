package axela.ws.sales;

//divya 26th march 2014

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Veh_Quote_List extends ConnectWS {
	public int i = 0;
	public static int total = 0;
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String keyword_search = "", keyword = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String quote_id = "0";
	public String enquiry_id = "0";
	public String config_sales_enquiry_refno = "";
	public String populate = "";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchvalue = "";
	public String StrSearch = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject QuoteList(JSONObject input) throws Exception {

		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = PadQuotes((Integer) input.get("pagecurrent") + "");
		}

		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("quote_id")) {
				quote_id = PadQuotes((String) input.get("quote_id"));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = PadQuotes((String) input.get("enquiry_id"));
			}
			if (!input.isNull("populate")) {
				populate = PadQuotes((String) input.get("populate"));
			}

			if (populate.equals("yes") && !emp_id.equals("0")) {
				PopulateSmartSearch();
			}

			if (!input.isNull("arr_keywords")) {
				arr_keywords = input.getJSONArray("arr_keywords");
				for (int i = 0; i < arr_keywords.length(); i++) {
					JSONObject jo = arr_keywords.getJSONObject(i);
					searchkeyname = jo.getString("searchkeyname");
					searchtype = jo.getString("searchtype");
					if (searchtype.equals("date")) {
						searchvalue = ConvertShortDateToStr(jo
								.getString("searchvalue"));
					} else {
						searchvalue = jo.getString("searchvalue");
					}

					if (!searchvalue.equals("")
							&& (searchtype.equals("int")
									|| searchtype.equals("date") || searchtype
										.equals("boolean"))) {
						if (searchkeyname.contains("(")
								&& !searchkeyname.contains(")")) {
							SOP("1");
							StrSearch += " AND " + searchkeyname + " = '"
									+ searchvalue + "')";
						} else {
							StrSearch += " AND " + searchkeyname + " = '"
									+ searchvalue + "'";
						}
					} else if (!searchvalue.equals("")
							&& searchtype.equals("text")) {
						if (searchkeyname.contains("(")
								&& !searchkeyname.contains(")")) {
							StrSearch += " AND " + searchkeyname + " LIKE '%"
									+ searchvalue + "%')";
						} else {
							StrSearch += " AND " + searchkeyname + " LIKE '%"
									+ searchvalue + "%'";
						}
					}
				}
			}
			if (!emp_id.equals("0")) {
				if (!input.isNull("keyword")) {
					keyword = PadQuotes((String) input.get("keyword"));
					if (!keyword.equals("")) {
						keyword_search = " and (quote_id like '%"
								+ keyword
								+ "%' or quote_no like '%"
								+ keyword
								+ "%'"
								+ " or title_id like '%"
								+ keyword
								+ "%' or title_desc like '%"
								+ keyword
								+ "%'"
								// + " or enquiry_refno like '%" + keyword +
								// "%'"
								+ " or quote_date like '%"
								+ keyword
								+ "%'"
								// + " or enquiry_close_date like '%" + keyword
								// + "%' or enquiry_lead_id like '%" + keyword +
								// "%'"
								+ " or customer_id like '%"
								+ keyword
								+ "%' or customer_name like '%"
								+ keyword
								+ "%'"
								+ " or contact_id like '%"
								+ keyword
								+ "%' or contact_fname like '%"
								+ keyword
								+ "%'"
								+ " or contact_lname like '%"
								+ keyword
								+ "%'"
								+ " or contact_mobile1 like '%"
								+ keyword
								+ "%' or contact_mobile2 like '%"
								+ keyword
								+ "%'"
								+ " or contact_email1 like '%"
								+ keyword
								+ "%'"
								// + " or so_dob like '%" + keyword + "%'"
								// + " or soe_name like '%" + keyword
								// + " or so_pan like '%" + keyword + "%'"
								+ " or quote_lead_id like '%"
								+ keyword
								+ "%'"
								+ " or quote_enquiry_id like '%"
								+ keyword
								+ "%'"
								// + " or so_quote_id like '%" + keyword + "%'"
								+ " or quote_netamt like '%"
								+ keyword
								+ "%'"
								+ " or quote_discamt like '%"
								+ keyword
								+ "%' or quote_totaltax like '%"
								+ keyword
								+ "%'"
								+ " or quote_grandtotal like '%"
								+ keyword
								+ "%' "
								+ " or quote_bill_address like '%"
								+ keyword
								+ "%' or quote_ship_address like '%"
								+ keyword
								+ "%'"
								+ " or quote_desc like '%"
								+ keyword
								+ "%'"
								+ " or quote_terms like '%"
								+ keyword
								+ "%'"
								+ " or quote_auth_id like '%"
								+ keyword
								+ "%' or quote_auth_date like '%"
								+ keyword
								+ "%'"
								// + " or so_mga_amount like '%" + keyword +
								// "%'"
								// + " or so_po like '%" + keyword + "%'"
								+ " or quote_refno like '%"
								+ keyword
								+ "%'"
								+ " or quote_notes like '%"
								+ keyword
								+ "%'"
								// + " or so_promise_date like '%" + keyword +
								// "%'"
								+ " or quote_entry_date like '%"
								+ keyword
								+ "%' or quote_modified_date like '%"
								+ keyword
								+ "%'"
								// + " or delstatus_name like '%" + keyword +
								// "%' or so_open like '%" + keyword + "%'"
								// + " or so_active like '%" + keyword +
								// "%' or so_notes like '%" + keyword + "%'"
								// + " or so_entry_date like '%" + keyword +
								// "%' or so_modified_date like '%" + keyword +
								// "%'"
								// + " or sob_name like '%" + keyword +
								// "%' or campaign_id like '%" + keyword + "%'"
								// + " or campaign_name like '%" + keyword +
								// "%'"
								+ " or emp_id like '%"
								+ keyword
								+ "%' or emp_name like '%"
								+ keyword
								+ "%'"
								// + " or stage_name like '%" + keyword + "%' "
								// + " or status_name like '%" + keyword + "%'"
								+ " or branch_id like '%" + keyword
								+ "%' or branch_code like '%" + keyword + "%'"
								+ " or branch_name like '%" + keyword + "%')"; // or
																				// followup_enquiry_id
																				// like
																				// '%"
																				// +
																				// keyword
																				// +
																				// "%'"
						// + " or item_name like '%" + keyword +
						// "%' or enquiry_priorityenquiry_id like '%" + keyword
						// + "%')";
					} else {
						keyword_search = "";
					}
					if (keyword.equals("") && !quote_id.equals("0")) {
						keyword_search += " AND quote_id =" + quote_id;
					}
					SOP("keyword==" + keyword);
				}
				PopulateConfigDetails();
				output.put("config_sales_enquiry_refno",
						config_sales_enquiry_refno);
				if (!populate.equals("yes")) {
					try {
						StrSql = "SELECT quote_id, CONCAT('QT', branch_code, quote_no) AS quote_no,"
								+ " quote_netamt, quote_totaltax, quote_grandtotal, quote_refno, quote_auth, quote_active,"
								+ " customer_name, COALESCE(so_id, 0) AS so_id, quote_enquiry_id,"
								+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
								+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, quote_discamt,"
								+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, quote_date,"
								+ " CONCAT(exeemp.emp_name, ' (', exeemp.emp_ref_no, ')') AS emp_name, exeemp.emp_id";

						CountSql = "SELECT COUNT(DISTINCT(quote_id))";

						SqlJoin = " FROM "
								+ compdb(comp_id)
								+ "axela_sales_quote"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_customer ON customer_id = quote_customer_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_customer_contact ON contact_id = quote_contact_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_branch ON branch_id = quote_branch_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_emp exeemp ON exeemp.emp_id = quote_emp_id"
								+ " LEFT JOIN "
								+ compdb(comp_id)
								+ "axela_sales_so ON so_quote_id = quote_id,"
								+ compdb(comp_id) + "axela_emp emp"
								+ " WHERE quote_enquiry_id != 0"
								+ " AND emp.emp_id = " + emp_id
								+ " AND IF (emp.emp_exeaccess != '', FIND_IN_SET(quote_emp_id, emp.emp_exeaccess), 1=1)";
						if (!enquiry_id.equals("0")) {
							SqlJoin += " AND quote_enquiry_id = " + enquiry_id;
						}
						SqlJoin += keyword_search + StrSearch;
						// + WSCheckExeAccess(emp_id) ;
						StrSql += SqlJoin;

						StrSql = StrSql + " GROUP BY quote_id"
								+ " ORDER BY quote_id DESC "
								+ LimitRecords(TotalRecords, pagecurrent);
						CountSql += SqlJoin;
						CachedRowSet crs = processQuery(StrSql, 0);
						TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
						// SOP("DDMOTORS========== quote query === " + StrSqlBreaker(StrSql));
						output.put("totalrecords", TotalRecords);

						// + "" + BranchAccess + ""
						// + WSCheckExeAccess(emp_id)
						// + " GROUP BY quote_id"
						// + " ORDER BY quote_id DESC";
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								i++;
								map.put("quote_id", crs.getString("quote_id"));
								map.put("quote_no", crs.getString("quote_no"));
								if (config_sales_enquiry_refno.equals("1")) {
									map.put("quote_refno",
											crs.getString("quote_refno"));
								} else {
									map.put("quote_refno", "");
								}
								map.put("quote_enquiry_id",
										crs.getString("quote_enquiry_id"));
								map.put("quote_active",
										crs.getString("quote_active"));
								map.put("quote_auth",
										crs.getString("quote_auth"));
								map.put("so_id", crs.getString("so_id"));
								map.put("customer_name",
										crs.getString("customer_name"));
								map.put("contactname",
										crs.getString("contact_name"));
								map.put("contact_mobile1",
										crs.getString("contact_mobile1"));
								map.put("contact_mobile2",
										crs.getString("contact_mobile2"));
								map.put("contact_email1",
										crs.getString("contact_email1"));
								map.put("contact_email2",
										crs.getString("contact_email2"));
								map.put("quote_date", strToShortDate(crs.getString("quote_date")));
								map.put("quote_netamt", crs.getString("quote_netamt"));
								map.put("quote_totaltax", IndDecimalFormat(df.format(crs.getDouble("quote_totaltax"))));
								map.put("quote_discamt", IndDecimalFormat(df
										.format(crs.getDouble("quote_discamt"))));
								map.put("quote_grandtotal",
										IndDecimalFormat(df.format(crs
												.getDouble("quote_grandtotal"))));
								map.put("emp_name", crs.getString("emp_name"));
								map.put("branch_name",
										crs.getString("branch_name"));

								list.add(gson.toJson(map)); // Converting String
															// to
															// Json
							}
							map.clear();
							output.put("listdata", list);
							list.clear();
						} else {
							output.put("msg", "No Records Found!");
						}
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto =="
								+ this.getClass().getName());
						SOPError("Error in "
								+ new Exception().getStackTrace()[0]
										.getMethodName() + ": " + ex);
					}
				}
			}
		}

		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	public JSONObject PopulateSmartSearch() {

		try {

			map.put("quote_id_type", "int");
			map.put("quote_id", "quote_id");

			map.put("quote_no_type", "text");
			map.put("quote_no", "CONCAT(quote_prefix, quote_no)");

			map.put("branch_id_type", "int");
			map.put("branch_id", "branch_id");

			map.put("branch_name_type", "text");
			map.put("branch_name", "branch_name");

			map.put("customer_id_type", "int");
			map.put("customer_id", "customer_id");

			map.put("contact_id_type", "int");
			map.put("contact_id", "contact_id");

			map.put("customer_name_type", "text");
			map.put("customer_name", "customer_name");

			map.put("contactname_type", "text");
			map.put("contactname",
					"CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)");

			map.put("contactmobile_type", "text");
			map.put("contactmobile",
					"CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))");

			map.put("contactemail_type", "text");
			map.put("contactemail", "CONCAT(contact_email1, contact_email2)");

			map.put("quote_lead_id_type", "int");
			map.put("quote_lead_id", "quote_lead_id");

			map.put("quote_enquiry_id_type", "int");
			map.put("quote_enquiry_id", "quote_enquiry_id");

			map.put("quote_date_type", "date");
			map.put("quote_date", "quote_date");

			map.put("quote_netamt_type", "int");
			map.put("quote_netamt", "quote_netamt");

			map.put("quote_discamt_type", "int");
			map.put("quote_discamt", "quote_discamt");

			map.put("quote_totaltax_type", "int");
			map.put("quote_totaltax", "quote_totaltax");

			map.put("quote_grandtotal_type", "int");
			map.put("quote_grandtotal", "quote_grandtotal");

			map.put("quote_bill_address_type", "text");
			map.put("quote_bill_address",
					"CONCAT(quote_bill_address, quote_bill_city, quote_bill_pin, quote_bill_state)");

			map.put("quote_ship_address_type", "text");
			map.put("quote_ship_address",
					"CONCAT(quote_ship_address, quote_ship_city, quote_ship_pin, quote_ship_state)");

			map.put("quote_desc_type", "text");
			map.put("quote_desc", "quote_desc");

			map.put("quote_terms_type", "text");
			map.put("quote_terms", "quote_terms");

			map.put("quote_refno_type", "text");
			map.put("quote_refno", "quote_refno");

			map.put("quote_auth_type", "boolean");
			map.put("quote_auth", "quote_auth");

			map.put("quote_auth_id_type", "text");
			map.put("quote_auth_id", "quote_auth_id IN (SELECT emp_id FROM "
					+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("quote_auth_date_type", "date");
			map.put("quote_auth_date", "quote_auth_date");

			map.put("emp_name_type", "text");
			map.put("emp_name", "CONCAT(emp_name, emp_ref_no)");

			map.put("quote_active_type", "boolean");
			map.put("quote_active", "quote_active");

			map.put("quote_notes_type", "text");
			map.put("quote_notes", "quote_notes");

			map.put("quote_entry_id_type", "text");
			map.put("quote_entry_id", "quote_entry_id IN (SELECT emp_id FROM "
					+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("quote_entry_date_type", "date");
			map.put("quote_entry_date", "quote_entry_date");

			map.put("quote_modified_id_type", "text");
			map.put("quote_modified_id",
					"quote_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("quote_modified_date_type", "date");
			map.put("quote_modified_date", "quote_modified_date");
			list.add(gson.toJson(map));
			map.clear();
			output.put("populatesmartsearch", list);
			list.clear();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		} finally {
			// crs.close();
		}
		return output;
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_sales_campaign, config_sales_enquiry_refno"
				+ " from " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_emp" + " where emp_id = " + emp_id
				+ "";
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("strsql==config=="+StrSql);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs
						.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
