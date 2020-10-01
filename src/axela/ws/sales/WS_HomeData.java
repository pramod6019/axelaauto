/* Ved Prakash (11th Sept 2013) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_HomeData extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String emp_active = "";
	public String ExeAccess = "";
	public CachedRowSet crs = null;

	public JSONObject home(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input homedata ========= " + input);

		}
		JSONObject output = new JSONObject();
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				SOP("comp_id=========" + comp_id);
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!emp_id.equals("0")) {
				String StrSql1 = "SELECT emp_active"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id;
				CachedRowSet crs1 = processQuery(StrSql1, 0);
				while (crs1.next()) {
					output.put("emp_active", emp_active);
					emp_active = crs1.getString("emp_active");
				}
				crs1.close();
				if (emp_active.equals("1")) {
					try {
						// ExeAccess = WSCheckExeAccess(emp_id, comp_id);
						StrSql = "SELECT"
								+ " (SELECT COALESCE(COUNT(enquiry_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE 1=1"
								+ " and substr(enquiry_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ " ) AS ENQUIRY,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 1"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ " ) AS BOOKING,"
								//
								// + " (SELECT COALESCE(COUNT(invoice_id), 0)"
								// + " FROM " + compdb(comp_id) + "axela_invoice"
								// + " WHERE 1=1"
								// + " and substr(invoice_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								// + " and invoice_active = 1"
								// // + ExeAccess.replace("emp_id", "invoice_emp_id")
								// + " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								// + emp_id
								// + ")=0, (invoice_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								// + emp_id + ") OR invoice_emp_id =" + emp_id
								// + " ),invoice_id > 0)"
								// + " ) "
								+ " 0 AS RETAIL,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_delivered_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 1"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ " ) AS DELIVERED,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_cancel_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 0"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ ") AS CANCELLED,"
								//
								+ " (SELECT COALESCE(COUNT(enquiry_id), 0) as enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE 1=1"
								+ " and substr(enquiry_entry_date,1,8) = substr(" + ToLongDate(kknow()) + ",1,8) " + " "
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS TODAY,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
								+ " WHERE enquiry_status_id = 1 AND enquiry_priorityenquiry_id = 1"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS HOT,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE enquiry_status_id = 1"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS OPEN,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 1 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 1',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 2 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 2',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 3 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 3',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 4 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 4',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 5 AND followup_desc = ''"
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 5',"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")"SELECT"
								+ " (SELECT COALESCE(COUNT(enquiry_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE 1=1"
								+ " and substr(enquiry_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ " ) AS ENQUIRY,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 1"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ " ) AS BOOKING,"
								//
								// + " (SELECT COALESCE(COUNT(invoice_id), 0)"
								// + " FROM " + compdb(comp_id) + "axela_invoice"
								// + " WHERE 1=1"
								// + " and substr(invoice_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								// + " and invoice_active = 1"
								// // + ExeAccess.replace("emp_id", "invoice_emp_id")
								// + " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								// + emp_id
								// + ")=0, (invoice_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								// + emp_id + ") OR invoice_emp_id =" + emp_id
								// + " ),invoice_id > 0)"
								// + " ) "
								+ " 0 AS RETAIL,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_delivered_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 1"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ " ) AS DELIVERED,"
								//
								+ " (SELECT COALESCE(COUNT(so_id), 0)"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE 1=1"
								+ " and substr(so_cancel_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
								+ " and so_active = 0"
								// + ExeAccess.replace("emp_id", "so_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (so_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR so_emp_id =" + emp_id
								+ " ),so_id > 0)"
								+ ") AS CANCELLED,"
								//
								+ " (SELECT COALESCE(COUNT(enquiry_id), 0) as enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE 1=1"
								+ " and substr(enquiry_entry_date,1,8) = substr(" + ToLongDate(kknow()) + ",1,8) " + " "
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS TODAY,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
								+ " WHERE enquiry_status_id = 1 AND enquiry_priorityenquiry_id = 1"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS HOT,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " WHERE enquiry_status_id = 1"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS OPEN,"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 1 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 1',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 2 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 2',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 3 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 3',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 4 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 4',"
								//
								+ " (SELECT COUNT(enquiry_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id"
								+ " WHERE enquiry_status_id = 1"
								+ " AND followup_trigger = 5 AND followup_desc = ''"
								// + " and enquiry_emp_id = " + emp_id
								// + WSCheckBranchAccess(emp_id, branch_id, role_id).replace("branch_id", "enquiry_branch_id")
								// + ExeAccess.replace("emp_id", "enquiry_emp_id")
								+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
								+ emp_id
								+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
								+ emp_id + ") OR enquiry_emp_id =" + emp_id
								+ " ),enquiry_id > 0)"
								+ ") AS 'LEVEL 5'";
						// SOP("StrSql===homequery==== = " + StrSql);
						crs = processQuery(StrSql, 0);

						Gson gson = new Gson();
						ArrayList<String> list = new ArrayList<String>();
						Map<String, String> map = new HashMap<String, String>();

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								map.put("enquiry", crs.getString("ENQUIRY"));
								map.put("booking", crs.getString("BOOKING"));
								map.put("retail", crs.getString("RETAIL"));
								map.put("delivered", crs.getString("DELIVERED"));
								map.put("cancellation", crs.getString("CANCELLED"));

								map.put("today", crs.getString("TODAY"));
								map.put("hot", crs.getString("HOT"));
								map.put("open", crs.getString("OPEN"));
								map.put("level1", crs.getString("LEVEL 1"));
								map.put("level2", crs.getString("LEVEL 2"));
								map.put("level3", crs.getString("LEVEL 3"));
								map.put("level4", crs.getString("LEVEL 4"));
								map.put("level5", crs.getString("LEVEL 5"));
								list.add(gson.toJson(map)); // Converting String to Json
							}
							map = null;
							output.put("homedata", list);
						} else {
							output.put("msg", "No Records Found!");
						}
						crs.close();
						if (AppRun().equals("0")) {
							SOP("output = " + output);
						}
					} catch (Exception ex) {
						SOPError("Axelaauto ==" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return output;
					}
				}
			}
		}
		return output;
	}

}
