package axela.ws.sales;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Veh_Salesorder_List extends ConnectWS {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String enquiry_id = "0";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String keyword_search = "", keyword = "", StrSearch = "";
	public String so_id = "0";
	public String quote_id = "0";
	public String so_quote_id = "0", so_customer_id = "0";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchparam = "";
	public String searchvalue = "";
	public String populate = "";
	public String config_sales_enquiry_refno = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject SalesorderList(JSONObject input) throws Exception {
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
			if (!input.isNull("so_id")) {
				so_id = CNumeric(PadQuotes((String) input.get("so_id")));
			}

			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input
						.get("enquiry_id")));
			}
			if (!input.isNull("quote_id")) {
				quote_id = CNumeric(PadQuotes((String) input.get("quote_id")));
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
					searchvalue = jo.getString("searchvalue");

					if (!searchvalue.equals("") && searchtype.equals("int") || searchtype.equals("date") || searchtype.equals("boolean")) {
						StrSearch += " AND " + searchkeyname + " = "
								+ searchvalue;
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
						keyword_search = " and (so_id like '%"
								+ keyword
								+ "%' or so_no like '%"
								+ keyword
								+ "%'"
								+ " or title_id like '%"
								+ keyword
								+ "%' or title_desc like '%"
								+ keyword
								+ "%'"
								// + " or enquiry_refno like '%" + keyword +
								// "%'"
								+ " or so_date like '%"
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
								+ "%' or so_dob like '%"
								+ keyword
								+ "%'"
								// + " or soe_name like '%" + keyword
								+ " or so_pan like '%" + keyword + "%'"
								+ " or so_lead_id like '%" + keyword
								+ "%' or so_enquiry_id like '%" + keyword
								+ "%'" + " or so_quote_id like '%" + keyword
								+ "%' or so_netamt like '%" + keyword + "%'"
								+ " or so_discamt like '%" + keyword
								+ "%' or so_totaltax like '%" + keyword + "%'"
								+ " or so_grandtotal like '%" + keyword
								+ "%' or so_desc like '%" + keyword + "%'"
								+ " or so_terms like '%" + keyword
								+ "%' or so_mga_amount like '%" + keyword
								+ "%'" + " or so_po like '%" + keyword
								+ "%' or so_refno like '%" + keyword + "%'"
								+ " or so_payment_date like '%" + keyword
								+ "%' or so_promise_date like '%" + keyword
								+ "%'" + " or so_retail_date like '%" + keyword
								+ "%' or so_delivered_date like '%" + keyword
								+ "%'" + " or delstatus_name like '%" + keyword
								+ "%' or so_open like '%" + keyword + "%'"
								+ " or so_active like '%"
								+ keyword
								+ "%' or so_notes like '%"
								+ keyword
								+ "%'"
								+ " or so_entry_date like '%"
								+ keyword
								+ "%' or so_modified_date like '%"
								+ keyword
								+ "%'"
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

					if (keyword.equals("") && !so_id.equals("0")) {
						keyword_search += " AND so_id =" + so_id;
					}
					if (!enquiry_id.equals("0")) {
						keyword_search += " AND enquiry_id =" + enquiry_id;
					}

				}
				PopulateConfigDetails();
				output.put("config_sales_enquiry_refno",
						config_sales_enquiry_refno);
				if (!populate.equals("yes")) {
					try {
						StrSql = "SELECT so_id, so_branch_id, CONCAT(so_prefix, so_no) AS so_no, so_date,"
								+ " so_delivered_date, so_dob, so_pan, so_netamt, so_totaltax, so_grandtotal, so_refno, so_active,"
								+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, so_auth,"
								+ " contact_id,"
								// + " COALESCE(invoice_id, 0) AS invoice_id, "
								+ " so_quote_id, so_promise_date,"
								+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
								+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, customer_id,"
								+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
								+ " customer_name, exeemp.emp_id, concat(exeemp.emp_name, ' (', exeemp.emp_ref_no, ')') AS emp_name,"
								+ " COALESCE(stock_delstatus_id, 0) AS vehstock_delstatus_id, so_enquiry_id,"
								+ " COALESCE(delstatus_name, '') AS delstatus_name, customer_curr_bal, so_retail_date";

						CountSql = "SELECT COUNT(DISTINCT(so_id))";

						SqlJoin = " FROM "
								+ compdb(comp_id)
								+ "axela_sales_so"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_sales_so_item on soitem_so_id = so_id"
								+ " AND soitem_rowcount != 0"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_inventory_item ON item_id = soitem_item_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_customer on customer_id = so_customer_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_customer_contact on contact_id = so_contact_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_title on title_id = contact_title_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_branch on branch_id = so_branch_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_emp exeemp on exeemp.emp_id = so_emp_id"
								+ " INNER JOIN "
								+ compdb(comp_id)
								+ "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
								+ " LEFT JOIN "
								+ compdb(comp_id)
								+ "axela_vehstock on vehstock_id = so_vehstock_id"
								// + " LEFT JOIN "
								// + compdb(comp_id)
								// + "axela_invoice on invoice_so_id = so_id"
								+ " LEFT JOIN "
								+ compdb(comp_id)
								+ "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id, "
								+ compdb(comp_id) + "axela_emp emp"
								+ " WHERE 1 = 1"
								+ " AND emp.emp_id = " + emp_id
								+ " AND IF (emp.emp_exeaccess != '', FIND_IN_SET(so_emp_id, emp.emp_exeaccess), 1=1)";

						if (!quote_id.equals("0")) {
							SqlJoin += " AND so_quote_id = " + quote_id;
						}
						SqlJoin += keyword_search + StrSearch;

						StrSql += SqlJoin;
						StrSql = StrSql + " GROUP BY so_id"
								+ " ORDER BY so_id DESC "
								+ LimitRecords(TotalRecords, pagecurrent);
						// SOP("records=========="+LimitRecords(TotalRecords, pagecurrent));
						CountSql += SqlJoin;
						TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
						// SOP("DDMOTORS========== SO query === " + StrSqlBreaker(StrSql));
						CachedRowSet crs = processQuery(StrSql, 0);

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								i++;
								map.put("so_id", crs.getString("so_id"));
								map.put("so_branch_id",
										crs.getString("so_branch_id"));
								map.put("so_no", crs.getString("so_no"));
								map.put("so_date",
										strToShortDate(crs.getString("so_date")));
								map.put("so_delivered_date", strToShortDate(crs.getString("so_delivered_date")));
								map.put("so_dob", strToShortDate(crs.getString("so_dob")));
								map.put("so_pan", crs.getString("so_pan"));
								map.put("so_netamt", crs.getString("so_netamt"));
								map.put("so_totaltax",
										crs.getString("so_totaltax"));
								map.put("so_grandtotal", IndDecimalFormat(df
										.format(crs.getDouble("so_grandtotal"))));
								if (config_sales_enquiry_refno.equals("1")) {
									map.put("so_refno",
											crs.getString("so_refno"));
								} else {
									map.put("so_refno", "");
								}
								map.put("so_active", crs.getString("so_active"));
								map.put("branch_id", crs.getString("branch_id"));
								map.put("branch_name",
										crs.getString("branch_name"));
								map.put("so_auth", crs.getString("so_auth"));
								map.put("contact_id",
										crs.getString("contact_id"));
								// map.put("invoice_id",
								// crs.getString("invoice_id"));
								map.put("so_quote_id",
										crs.getString("so_quote_id"));
								map.put("so_promise_date", strToShortDate(crs
										.getString("so_promise_date")));
								map.put("customer_name",
										crs.getString("customer_name"));
								map.put("contact_name",
										crs.getString("contact_name"));
								map.put("contact_mobile1",
										crs.getString("contact_mobile1"));
								map.put("contact_mobile2",
										crs.getString("contact_mobile2"));
								map.put("contact_email1",
										crs.getString("contact_email1"));
								map.put("contact_email2",
										crs.getString("contact_email2"));
								map.put("customer_id",
										crs.getString("customer_id"));
								map.put("item_name", crs.getString("item_name"));
								map.put("emp_name", crs.getString("emp_name"));
								map.put("vehstock_delstatus_id",
										crs.getString("vehstock_delstatus_id"));
								map.put("so_enquiry_id",
										crs.getString("so_enquiry_id"));
								map.put("delstatus_name",
										crs.getString("delstatus_name"));
								map.put("customer_curr_bal",
										crs.getString("customer_curr_bal"));
								map.put("so_retail_date", strToShortDate(crs
										.getString("so_retail_date")));

								list.add(gson.toJson(map)); // Converting String
															// to Json
							}
							map.clear();
							output.put("totalrecords", TotalRecords);
							output.put("listdata", list);
							list.clear();
						} else {
							output.put("msg", "No Records Found!");
						}
						if (AppRun().equals("0")) {

							SOP("output = " + output);
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
		return output;
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_enquiry_refno" + " FROM "
				+ compdb(comp_id) + "axela_config";
		config_sales_enquiry_refno = ExecuteQuery(StrSql);
	}

	public JSONObject PopulateSmartSearch() {

		try {

			map.put("so_id_type", "int");
			map.put("so_id", "so_id");

			map.put("so_no_type", "text");
			map.put("so_no", "CONCAT('ENQ', so_no)");

			map.put("so_date_type", "date");
			map.put("so_date", "so_date");

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

			map.put("so_dob_type", "date");
			map.put("so_dob", "so_dob");

			map.put("so_pan_type", "text");
			map.put("so_pan", "so_pan");

			map.put("contactname_type", "text");
			map.put("contactname",
					"CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)");

			map.put("contactmobile_type", "text");
			map.put("contactmobile",
					"CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))");

			map.put("contactemail_type", "text");
			map.put("contactemail", "CONCAT(contact_email1, contact_email2)");

			map.put("so_lead_id_type", "int");
			map.put("so_lead_id", "so_lead_id");

			map.put("so_enquiry_id_type", "int");
			map.put("so_enquiry_id", "so_enquiry_id");

			map.put("so_quote_id_type", "int");
			map.put("so_quote_id", "so_quote_id");

			map.put("vehstock_id_type", "int");
			map.put("vehstock_id", "vehstock_id");

			map.put("so_netamt_type", "int");
			map.put("so_netamt", "so_netamt");

			map.put("so_discamt_type", "int");
			map.put("so_discamt", "so_discamt");

			map.put("so_totaltax_type", "int");
			map.put("so_totaltax", "so_totaltax");

			map.put("so_grandtotal_type", "int");
			map.put("so_grandtotal", "so_grandtotal");

			map.put("so_desc_type", "text");
			map.put("so_desc", "so_desc");

			map.put("so_terms_type", "text");
			map.put("so_terms", "so_terms");

			map.put("so_mga_amount_type", "int");
			map.put("so_mga_amount", "so_mga_amount");

			map.put("so_po_type", "text");
			map.put("so_po", "so_po");

			map.put("so_refno_type", "text");
			map.put("so_refno", "so_refno");

			map.put("so_payment_date_type", "date");
			map.put("so_payment_date", "so_payment_date");

			map.put("so_promise_date_type", "date");
			map.put("so_promise_date", "so_promise_date");

			map.put("so_retail_date_type", "date");
			map.put("so_retail_date", "so_retail_date");

			map.put("so_delivered_date_type", "date");
			map.put("so_delivered_date", "so_delivered_date");

			map.put("delstatus_name_type", "text");
			map.put("delstatus_name", "delstatus_name");

			map.put("so_open_type", "boolean");
			map.put("so_open", "so_open");

			map.put("emp_name_type", "text");
			map.put("emp_name", "CONCAT(emp_name, emp_ref_no)");

			map.put("so_active_type", "boolean");
			map.put("so_active", "so_active");

			map.put("so_notes_type", "text");
			map.put("so_notes", "so_notes");

			map.put("so_entry_id_type", "text");
			map.put("so_entry_id", "so_entry_id IN (SELECT emp_id FROM "
					+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("so_entry_date_type", "date");
			map.put("so_entry_date", "so_entry_date");

			map.put("so_modified_id_type", "text");
			map.put("so_modified_id", "so_modified_id IN (SELECT emp_id FROM "
					+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("so_modified_date_type", "date");
			map.put("so_modified_date", "so_modified_date");

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
		}
		return output;
	}
}
