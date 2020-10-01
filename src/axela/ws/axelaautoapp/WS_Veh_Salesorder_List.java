package axela.ws.axelaautoapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Veh_Salesorder_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String enquiry_id = "0";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String StrSearch = "";
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
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public String branch_id = "0";
	public String config_sales_enquiry_refno = "";
	public String strsearch = "";
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String executive_id = "0";
	public String strSearch2 = "", StrSearch1 = "", filter = "";
	public String brand_ids = "0";
	public String region_ids = "0";
	public String branch_ids = "0";
	public String executive_ids = "0";
	public String model_ids = "0";
	public String filterso_contact_id = "0";
	public String filterso_contact_name = "";
	public String filterso_contact_mobile = "";
	public String filterso_contact_email = "";
	public String filterso_quote_id = "0";
	public String filterso_pan_no = "";
	public String filterso_active = "";
	public String filterso_vehstock_id = "0";
	public String filterso_from_payment_date = "";
	public String filterso_from_promise_date = "";
	public String filterso_to_payment_date = "";
	public String filterso_from_delivery_date = "";
	public String filterso_to_promise_date = "";
	public String filterso_to_delivery_date = "";
	public String filterso_from_cancel_date = "";
	public String filterso_to_cancel_date = "";
	public String filterso_dob = "";
	public String filterso_from_date = "";
	public String filterso_to_date = "";
	public String filterso_id = "0";
	public String filterso_no = "0";
	public String sostatus = "";
	public String filterso_customer_id = "0";
	public String filterso_customer_name = "";
	public String filterso_branch_id = "0";
	public String filterso_enquiry_id = "0";
	public String filterso_branch_name = "";
	public String filterpreownedstock_id = "0", filterpreownedstock_id1 = "0";
	public String preownedstock_id = "0";
	public String filterquery = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject SalesorderList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input =====so list=== " + input);
		}
		HttpSession session = request.getSession(true);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		if (!input.isNull("pagecurrent")) {
			// pagecurrent = PadQuotes((Integer) input.get("pagecurrent") + "");
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}
		if (!input.isNull("so_id")) {
			so_id = CNumeric(PadQuotes((String) input.get("so_id")));
		}
		if (!input.isNull("enquiry_id")) {
			enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
		}
		if (!input.isNull("quote_id")) {
			quote_id = CNumeric(PadQuotes((String) input.get("quote_id")));
		}
		if (!input.isNull("filter")) {
			filter = PadQuotes((String) input.get("filter"));
		}
		if (filter.equals("yes")) {
			strSearch2 = ProcessFilter(request, input);
		}
		if (!input.isNull("brand_id")) {
			brand_id = CNumeric((String) input.get("brand_id"));
		}
		if (!input.isNull("region_id")) {
			region_id = CNumeric((String) input.get("region_id"));
		}
		if (!input.isNull("branch_id")) {
			branch_id = (CNumeric((String) input.get("branch_id")));
		}
		if (!input.isNull("team_id")) {
			team_id = (CNumeric((String) input.get("team_id")));
		}
		if (!input.isNull("executive_id")) {
			executive_id = CNumeric((String) input.get("executive_id"));
		}
		// if (!input.isNull("preownedvehstock_id")) {
		// preownedvehstock_id = CNumeric((String) input.get("preownedvehstock_id"));
		// }
		if (!input.isNull("sostatus")) {
			sostatus = PadQuotes((String) input.get("sostatus"));
		}
		// SOP("sostatus===============" + sostatus);
		if (!emp_id.equals("0")) {

			if (sostatus.equals("monthbookings")) {
				StrSearch = " AND so_active = '1' AND SUBSTR(so_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
			}
			else if (sostatus.equals("monthretails")) {
				StrSearch = " AND so_active = '1' AND SUBSTR(so_retail_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";

			} else if (sostatus.equals("monthcancellations") || sostatus.equals("mothcancellations")) {
				StrSearch = " AND so_active = '0' AND so_delivered_date = '' AND so_retail_date = ''  AND SUBSTR(so_cancel_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
			} else if (sostatus.equals("todaybookings")) {
				StrSearch = " AND so_active = '1' AND SUBSTR(so_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";

			} else if (sostatus.equals("todayretails")) {
				StrSearch = " AND so_active = '1' AND SUBSTR(so_retail_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";

			} else if (sostatus.equals("totalbookings")) {
				StrSearch = " AND so_delivered_date=''"
						+ " AND so_cancel_date=''";

			}
			if (!brand_id.equals("0")) {
				StrSearch1 += " AND branch_brand_id = " + brand_id + "";
			}
			if (!region_id.equals("0")) {
				StrSearch1 += " AND branch_region_id = " + region_id + "";
			}
			if (!branch_id.equals("0")) {
				StrSearch1 += " AND branch_id = " + branch_id + "";
			}
			if (!team_id.equals("0")) {
				StrSearch1 += " AND teamtrans_team_id = " + team_id + "";
			}
			if (!executive_id.equals("0")) {
				StrSearch1 += " AND teamtrans_emp_id = " + executive_id + "";
			}
			if (!input.isNull("filterquery")) {
				filterquery = JSONPadQuotes((String) input.get("filterquery"));
				if (!filterquery.equals("")) {
					filterquery = new String(Base64.decodeBase64(filterquery.getBytes("ISO-8859-1")));
				}
			}
			// if (preownedvehstock_id.equals("0")) {
			// StrSearch1 += " AND preownedvehstock_id = " + preownedvehstock_id + "";;
			// }
			// if (!emp_id.equals("0")) {
			// PopulateConfigDetails();
			// output.put("config_sales_enquiry_refno", config_sales_enquiry_refno);
			// if (!populate.equals("yes")) {
			try {
				StrSql = "SELECT so_id, so_pan, so_netamt, so_branch_id, so_netamt, CONCAT(so_prefix, so_no) AS so_no,"
						+ " so_date,so_dob, so_enquiry_id, so_quote_id, so_delivered_date,"
						+ " so_grandtotal, so_discamt, so_refno, so_active, so_promise_date,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " contact_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name," + " contact_mobile1, contact_mobile2, contact_email1 , contact_email2,"
						+ " customer_curr_bal, customer_id, so_auth, customer_name, emp_id," + " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, so_totaltax,"
						+ " COALESCE(item_name, '') AS item_name, so_preownedstock_id," + " COALESCE(vehstock_delstatus_id, 0) AS vehstock_delstatus_id,"
						+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no,"
						+ " COALESCE(delstatus_name, '') AS delstatus_name,"
						// + " COALESCE(stock_comm_no, '') AS vehstock_comm_no,"
						// + " COALESCE(stock_vin_no, '') AS vehstock_vin_no,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 1"
						+ " AND trans_vehstock_id = vehstock_id), '') AS 'Paintwork',"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 2"
						+ " AND trans_vehstock_id = vehstock_id), '') AS 'Upholstery',"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 4"
						+ " AND trans_vehstock_id = vehstock_id), '') AS 'Package',"
						+ " COALESCE(vehstock_id, 0) AS vehstock_id, voucher_vouchertype_id";

				CountSql = "SELECT COUNT(DISTINCT(so_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_sales_so_payment_track track ON track_so_id = so_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
						// + " AND soitem_rowcount != 0"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
						// + " LEFT JOIN " + compdb(comp_id) + "axela_invoice ON invoice_so_id = so_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_so_id = so_id and voucher_vouchertype_id = 6"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id";
				if (!executive_id.equals("0") || !executive_ids.equals("0") || !team_id.equals("0")) {
					SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id";
					SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
				}
				SqlJoin += " WHERE 1=1";
				StrSql += SqlJoin;
				CountSql += SqlJoin;
				StrSql += StrSearch
						+ StrSearch1
						+ strSearch2
						+ filterquery
						+ ExeAccess.replace("emp_id", "so_emp_id");
				if (!BranchAccess.equals("")) {
					StrSql += BranchAccess.replace("branch_id", "so_branch_id");
				}
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// SOP("TotalRecords----" + TotalRecords);
				// totalcount = TotalRecords / 25;
				StrSql += StrSearch + " GROUP BY so_id" + " ORDER BY so_id DESC";
				CountSql += StrSearch;
				StrSql += LimitRecords(TotalRecords, pagecurrent);
				SOP("StrSql===SO List===" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						i++;
						map.put("so_id", crs.getString("so_id"));
						map.put("so_branch_id", crs.getString("so_branch_id"));
						map.put("so_no", crs.getString("so_no"));
						map.put("so_date", strToShortDate(crs.getString("so_date")));
						map.put("so_delivered_date", strToShortDate(crs.getString("so_delivered_date")));
						map.put("so_dob", strToShortDate(crs.getString("so_dob")));
						map.put("so_pan", crs.getString("so_pan"));
						map.put("so_netamt", IndDecimalFormat(df.format(crs.getDouble("so_netamt"))));
						map.put("so_totaltax", IndDecimalFormat(df.format(crs.getDouble("so_totaltax"))));
						map.put("so_grandtotal", IndDecimalFormat(df.format(crs.getDouble("so_grandtotal"))));
						map.put("so_quote_id", crs.getString("so_quote_id"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_id", crs.getString("contact_id"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("customer_id", crs.getString("customer_id"));
						map.put("ledger", crs.getString("customer_id"));
						map.put("item_name", unescapehtml(crs.getString("item_name")));
						map.put("emp_name", crs.getString("emp_name"));
						map.put("so_enquiry_id", crs.getString("so_enquiry_id"));
						map.put("so_preownedstock_id", crs.getString("so_preownedstock_id"));
						map.put("voucher_vouchertype_id", crs.getString("voucher_vouchertype_id"));
						// map.put("vouchertype_voucherclass_id", crs.getString("vouchertype_voucherclass_id"));
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

					// SOP("output = " + output);
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto =="
						+ this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0]
								.getMethodName() + ": " + ex);
			}
			// }
			// }

		}
		SOP("output---------WS_salesorder----" + output);
		return output;
	}
	private String ProcessFilter(HttpServletRequest request, JSONObject input) {
		try {
			if (!input.isNull("model_ids")) {
				model_ids = PadQuotes((String) input.get("model_ids"));
			}
			if (!input.isNull("brand_ids")) {
				brand_ids = PadQuotes((String) input.get("brand_ids"));
			}
			if (!input.isNull("region_ids")) {
				region_ids = PadQuotes((String) input.get("region_ids"));
			}
			if (!input.isNull("branch_ids")) {
				branch_ids = PadQuotes((String) input.get("branch_ids"));
			}
			if (!input.isNull("model_ids")) {
				model_ids = PadQuotes((String) input.get("model_ids"));
			}
			if (!input.isNull("executive_ids")) {
				executive_ids = PadQuotes((String) input.get("executive_ids"));
			}
			if (!input.isNull("txt_so_id")) {
				filterso_id = CNumeric(PadQuotes((String) input.get("txt_so_id")));
			}
			if (!input.isNull("txt_so_no")) {
				filterso_no = CNumeric(PadQuotes((String) input.get("txt_so_no")));
			}
			if (!input.isNull("txt_from_date")) {
				filterso_from_date = PadQuotes((String) input.get("txt_from_date"));
			}
			if (!input.isNull("txt_to_date")) {
				filterso_to_date = PadQuotes((String) input.get("txt_to_date"));
			}
			if (!filterso_from_date.equals(""))
			{
				// filterso_from_date = FormatDateStr(filterso_from_date);
				filterso_from_date = ConvertShortDateToStr(filterso_from_date);
			}
			if (!filterso_to_date.equals(""))
			{
				// filterso_to_date = FormatDateStr(filterso_to_date);
				filterso_to_date = ConvertShortDateToStr(filterso_to_date);

			}

			// if (!input.isNull("dr_branch_name")) {
			// filterso_branch_name = CNumeric(PadQuotes((String) input.get("dr_branch_name")));
			// }
			// session1.setAttribute("filterso_branch_id1", filterso_branch_id);
			if (!input.isNull("txt_customer_id")) {
				filterso_customer_id = CNumeric(PadQuotes((String) input.get("txt_customer_id")));
			}
			if (!input.isNull("txt_customer_name")) {
				filterso_customer_name = PadQuotes((String) input.get("txt_customer_name"));
			}
			if (!input.isNull("txt_so_dob")) {
				filterso_dob = PadQuotes((String) input.get("txt_so_dob"));
			}
			if (!filterso_dob.equals(""))
			{
				// if (!isValidDateFormatShort(filterso_dob))
				// {
				// filterso_dob = "";
				// }
				filterso_dob = ConvertShortDateToStr(filterso_dob);

			}
			if (!input.isNull("txt_contact_id")) {
				filterso_contact_id = CNumeric(PadQuotes((String) input.get("txt_contact_id")));
			}
			if (!input.isNull("txt_contact_name")) {
				filterso_contact_name = PadQuotes((String) input.get("txt_contact_name"));
			}
			if (!input.isNull("txt_contact_mobile")) {
				filterso_contact_mobile = PadQuotes((String) input.get("txt_contact_mobile"));
			}
			if (!input.isNull("txt_contact_email")) {
				filterso_contact_email = PadQuotes((String) input.get("txt_contact_email"));
			}
			if (!input.isNull("txt_pan_no")) {
				filterso_pan_no = PadQuotes((String) input.get("txt_pan_no"));
			}
			if (!input.isNull("txt_enquiry_id")) {
				filterso_enquiry_id = CNumeric(PadQuotes((String) input.get("txt_enquiry_id")));
				SOP("filterso_enquiry_id--------" + filterso_enquiry_id);
			}
			if (!input.isNull("txt_quote_id")) {
				filterso_quote_id = CNumeric(PadQuotes((String) input.get("txt_quote_id")));
			}
			if (!input.isNull("txt_stock_id")) {
				filterso_vehstock_id = CNumeric(PadQuotes((String) input.get("txt_stock_id")));
			}
			if (!input.isNull("dr_action")) {
				filterso_active = PadQuotes((String) input.get("dr_action"));
			}
			if (!input.isNull("txt_so_from_payment_date")) {
				filterso_from_payment_date = PadQuotes((String) input.get("txt_so_from_payment_date"));
			}
			if (!input.isNull("txt_so_to_payment_date")) {
				filterso_to_payment_date = PadQuotes((String) input.get("txt_so_to_payment_date"));
			}
			if (!input.isNull("txt_so_from_promise_date")) {
				filterso_from_promise_date = PadQuotes((String) input.get("txt_so_from_promise_date"));
			}
			if (!input.isNull("txt_so_to_promise_date")) {
				filterso_to_promise_date = PadQuotes((String) input.get("txt_so_to_promise_date"));
			}
			if (!input.isNull("txt_so_from_delivery_date")) {
				filterso_from_delivery_date = PadQuotes((String) input.get("txt_so_from_delivery_date"));
			}
			if (!input.isNull("txt_so_to_delivery_date")) {
				filterso_to_delivery_date = PadQuotes((String) input.get("txt_so_to_delivery_date"));
			}
			if (!input.isNull("txt_so_from_cancel_date")) {
				filterso_from_cancel_date = PadQuotes((String) input.get("txt_so_from_cancel_date"));
			}
			if (!input.isNull("txt_so_to_cancel_date")) {
				filterso_to_cancel_date = PadQuotes((String) input.get("txt_so_to_cancel_date"));
			}
			// from stock list

			if (!input.isNull("so_preownedstock_id")) {
				filterpreownedstock_id = CNumeric((String) input.get("so_preownedstock_id"));
			}

			// // from preowned-list
			// if (!input.isNull("preownedstock_so_id")) {
			// filterpreownedvehstock_id1 = CNumeric((String) input.get("preownedstock_so_id"));
			// }

			if (!filterso_from_payment_date.equals(""))
			{
				filterso_from_payment_date = ConvertShortDateToStr(filterso_from_payment_date);
			}
			if (!filterso_to_payment_date.equals(""))
			{
				filterso_to_payment_date = ConvertShortDateToStr(filterso_to_payment_date);

			}
			if (!filterso_from_promise_date.equals(""))
			{
				filterso_from_promise_date = ConvertShortDateToStr(filterso_from_promise_date);

			}
			if (!filterso_to_promise_date.equals(""))
			{
				filterso_to_promise_date = ConvertShortDateToStr(filterso_to_promise_date);

			}
			if (!filterso_from_delivery_date.equals(""))
			{
				filterso_from_delivery_date = ConvertShortDateToStr(filterso_from_delivery_date);

			}
			if (!filterso_to_delivery_date.equals(""))
			{
				filterso_to_delivery_date = ConvertShortDateToStr(filterso_to_delivery_date);

			}
			if (!filterso_from_cancel_date.equals(""))
			{
				filterso_from_cancel_date = ConvertShortDateToStr(filterso_from_cancel_date);

			}
			if (!filterso_to_cancel_date.equals(""))
			{
				filterso_to_cancel_date = ConvertShortDateToStr(filterso_to_cancel_date);

			}

			if (!model_ids.equals("0"))
			{
				strSearch2 = " AND model_id IN(" + model_ids + ")";
			}

			if (!brand_ids.equals("0"))
			{
				strSearch2 += " AND branch_brand_id IN(" + brand_ids + ")";
			}
			if (!region_ids.equals("0"))
			{
				strSearch2 += " AND branch_region_id IN(" + region_ids + ")";
			}

			if (!branch_ids.equals("0"))
			{
				strSearch2 += " AND so_branch_id IN(" + branch_ids + ")";
			}

			if (!executive_ids.equals("0"))
			{
				strSearch2 += " AND so_emp_id IN(" + executive_ids + ")";
			}

			if (!filterso_id.equals("0"))
			{
				strSearch2 += " AND so_id=" + filterso_id;
			}

			if (!filterso_no.equals("0"))
			{
				strSearch2 += " AND so_no=" + filterso_no;
			}

			if (!filterso_from_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_date,1,8) >=SUBSTR('" + filterso_from_date + "',1,8)";

			}

			if (!filterso_to_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_date,1,8) <=SUBSTR('" + filterso_to_date + "',1,8)";
			}

			// if (!filterso_branch_name.equals(""))
			// {
			// strSearch2 += " AND branch_name='" + filterso_branch_name + "'";
			// }

			if (!filterso_customer_id.equals("0"))
			{
				strSearch2 += " AND so_customer_id=" + filterso_customer_id;
			}

			if (!filterso_customer_name.equals(""))
			{
				strSearch2 += " AND customer_name='" + filterso_customer_name + "'";
			}

			if (!filterso_dob.equals(""))
			{
				strSearch2 += " AND so_dob='" + filterso_dob + "'";
			}

			if (!filterso_contact_id.equals("0"))
			{
				strSearch2 += " AND so_contact_id=" + filterso_contact_id;
			}

			if (!filterso_contact_name.equals(""))
			{
				strSearch2 += " AND (contact_fname LIKE " + filterso_contact_name + " OR contact_lname LIKE + filterso_contact_name +";
			}
			if (!filterso_contact_mobile.contains("-") && !filterso_contact_mobile.equals("")) {
				filterso_contact_mobile = "91-" + filterso_contact_mobile;
			}
			if (!filterso_contact_mobile.equals(""))
			{
				strSearch2 += " AND (contact_mobile1='" + filterso_contact_mobile + "'"
						+ " OR contact_mobile2='" + filterso_contact_mobile + "'" + ")";
			}

			if (!filterso_contact_email.equals(""))
			{
				strSearch2 += " AND (contact_email1='" + filterso_contact_email + "'"
						+ " OR contact_email1='" + filterso_contact_email + "'" + ")";
			}

			if (!filterso_pan_no.equals(""))
			{
				strSearch2 += " AND so_pan='" + filterso_pan_no + "'";
			}

			// if (!filterso_quote_id.equals("0"))
			// {
			// strSearch2 += " AND quote_branch_id=" + filterso_quote_id;
			// }

			if (!filterso_quote_id.equals("0"))
			{
				strSearch2 += " AND so_quote_id=" + filterso_quote_id;
			}

			if (!filterso_enquiry_id.equals("0"))
			{
				SOP("11111");
				strSearch2 += " AND so_enquiry_id=" + filterso_enquiry_id;
			}

			if (!filterso_vehstock_id.equals("0"))
			{
				strSearch2 += " AND so_vehstock_id=" + filterso_vehstock_id;
			}

			if (!filterso_active.equals(""))
			{
				strSearch2 += " AND so_active=" + filterso_active;
			}

			if (!filterso_from_payment_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_payment_date,1,8) >=SUBSTR('" + filterso_from_payment_date + "',1,8)";
			}

			if (!filterso_to_payment_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_payment_date,1,8) <=SUBSTR('" + filterso_to_payment_date + "',1,8)";
			}

			if (!filterso_from_promise_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_promise_date,1,8) >=SUBSTR('" + filterso_from_promise_date + "',1,8)";
			}

			if (!filterso_to_promise_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_promise_date,1,8) <=SUBSTR('" + filterso_to_promise_date + "',1,8)";
			}

			if (!filterso_from_delivery_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_delivered_date,1,8) >=SUBSTR('" + filterso_from_delivery_date + "',1,8)";
			}
			if (!filterso_to_delivery_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_delivered_date,1,8) <=SUBSTR('" + filterso_to_delivery_date + "',1,8)";
			}

			if (!filterso_from_cancel_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_cancel_date,1,8) >=SUBSTR('" + filterso_from_cancel_date + "',1,8)";
			}

			if (!filterso_to_cancel_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(so_cancel_date,1,8) >=SUBSTR('" + filterso_to_cancel_date + "',1,8)";
			}

			if (!filterpreownedstock_id.equals("0")) {
				strSearch2 = " AND so_preownedstock_id=" + filterpreownedstock_id;
			}

			// if (!filterpreownedvehstock_id1.equals("0")) {
			// strSearch2 = " AND so_id=" + filterpreownedvehstock_id1;
			// }

			// return strsearch;

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		SOP("strSearch2--------" + strSearch2);
		return strSearch2;
	}
	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_enquiry_refno"
				+ " FROM " + compdb(comp_id) + "axela_config";
		config_sales_enquiry_refno = ExecuteQuery(StrSql);
	}
}
