/* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Receipt_List extends Connect {
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String enqstatus = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String role_id = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile = "";
	public String contact_mobile1_temp = "";
	public String contact_mobile2_temp = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String customer_id = "0";
	public String voucher_id = "0";
	public String status_id = "0";
	public String priority_id = "0";
	public String model_ids = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public CachedRowSet crs = null;
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String strsearch = "";
	public String voucher_so_id = "0";
	public String voucher_vouchertype_id = "0";
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;
	
	public JSONObject ReceiptList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_receipt_list===" + input);
		}
		
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric((String) input.get("pagecurrent"));
		}
		if (!input.isNull("so_id")) {
			voucher_so_id = CNumeric((String) input.get("so_id"));
			strsearch = " AND voucher_so_id =" + voucher_so_id;
		}
		if (!input.isNull("voucher_vouchertype_id")) {
			voucher_vouchertype_id = PadQuotes((String) input.get("voucher_vouchertype_id"));
			strsearch = " AND voucher_vouchertype_id =" + voucher_vouchertype_id;
		}
		if (!input.isNull("voucher_id")) {
			voucher_id = PadQuotes((String) input.get("voucher_id"));
			strsearch = " AND voucher_id =" + voucher_id;
		}
		HttpSession session = request.getSession(true);
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
		if (ReturnPerm(comp_id, "emp_acc_receipt_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		// emp_all_exe = GetSession("emp_all_exe", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		if (!emp_id.equals("0")) {
			try {
				StrSql = "SELECT voucher_id, vouchertype_name, customer_ledgertype, voucher_so_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, customer_name,"
						+ " voucher_date, voucher_amount, paymode_name,"
						+ " COALESCE(contact_mobile1,'') AS contact_mobile1, COALESCE(contact_mobile2,'') AS contact_mobile2,"
						+ " COALESCE(contact_email1,'') AS contact_email1, COALESCE(contact_email2,'') AS contact_email2,"
						+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')')) AS emp_name,"
						+ " COALESCE(branch_name, '') AS branch_name,"
						+ " COALESCE(if(vouchertype_id = 9,(SELECT customer_ledgertype"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
						+ " WHERE  vouchertrans_voucher_id = voucher_id"
						+ " AND vouchertrans_dc = 1 limit 1),"
						+ " if(vouchertype_id = 15,(SELECT customer_ledgertype"
						+ " FROM " + compdb(comp_id) + " axela_customer"
						+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
						+ " WHERE  vouchertrans_voucher_id = voucher_id"
						+ " AND vouchertrans_dc = 0 limit 1),0)), 0) AS paymodeid, voucher_authorize";
				SqlJoin = " FROM " + compdb(comp_id) + " axela_acc_voucher"
						
						+ " INNER JOIN 	" + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
						
						+ " INNER JOIN 	" + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN  " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " LEFT JOIN  " + compdb(comp_id) + " axela_customer ON customer_id = voucher_customer_id"
						+ " LEFT JOIN  " + compdb(comp_id) + " axela_customer_contact ON contact_id = voucher_contact_id"
						+ " LEFT JOIN  " + compdb(comp_id) + " axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN  " + compdb(comp_id) + " axela_emp ON emp_id = voucher_emp_id"
						+ " LEFT JOIN  " + compdb(comp_id) + " axela_branch ON branch_id = voucher_branch_id"
						+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
						+ " WHERE 1 = 1 "
						+ BranchAccess + ExeAccess
						+ " AND voucher_vouchertype_id = " + 9;
				
				StrSql = StrSql + SqlJoin;
				if (!strsearch.equals("")) {
					StrSql += strsearch;
				}
				StrSql += ExeAccess.replace("emp_id", "voucher_emp_id");
				if (!BranchAccess.equals("")) {
					StrSql += BranchAccess;
				}
				CountSql += SqlJoin;
				// TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				StrSql = StrSql + " GROUP BY voucher_id"
						+ " ORDER BY voucher_id DESC "
						+ LimitRecords(TotalRecords, pagecurrent);
				// TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// SOP("StrSql==========WS_receipt_list==========" + StrSql);
//				if (emp_id.equals("2517")) {
//					SOPInfo("StrSql==========StrSql==========" + StrSql);
//				}
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("voucher_id", crs.getString("voucher_id"));
						map.put("vouchertype_name", crs.getString("vouchertype_name"));
						map.put("voucher_so_id", crs.getString("voucher_so_id"));
						map.put("customer_ledgertype", crs.getString("customer_ledgertype"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("ledger", crs.getString("customer_name"));
						map.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
						map.put("voucher_amount", crs.getString("voucher_amount"));
						map.put("paymode_name", crs.getString("paymode_name"));
						map.put("emp_name", crs.getString("emp_name"));
						map.put("branch_name", crs.getString("branch_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("emp_name", crs.getString("emp_name"));
						map.put("voucher_authorize", crs.getString("voucher_authorize"));
						list.add(gson.toJson(map));
					}
					map = null;
					output.put("listdata", list);
					obj = new JSONObject();
					obj.put("totalrecords", TotalRecords);
					ArrayList list1 = new ArrayList();
					list1.add(obj);
					obj = null;
					output.put("totalrecords", TotalRecords);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					// SOP("output = " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		if (AppRun().equals("0")) {
			SOP("output-------WS_receipt_list--------" + output);
		}
		return output;
	}
}
