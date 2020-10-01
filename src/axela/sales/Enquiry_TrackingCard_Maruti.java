package axela.sales;

//Bhavya 19th Apr 2016

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import cloudify.connect.Connect;

public class Enquiry_TrackingCard_Maruti extends Connect {
	
	public Connection conn = null;
	public String enquiry_id = "0", brand_id = "0";
	public String StrSql = "", msg = "", reportfrom = "", format = "pdf";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String comp_id = "0", emp_id = "0";
	public String ExeAccess = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			reportfrom = PadQuotes(request.getParameter("dr_report"));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = CheckNull(GetSession("BranchAccess", request));
				ExeAccess = CheckNull(GetSession("ExeAccess", request));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				
				// SOP("brand_id--------------" + brand_id);
				
				if (brand_id.equals("1") || brand_id.equals("2")) {
					reportfrom = "tracking-card-maruti";
				}
				// SOP("reportfrom--------------" + reportfrom);
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				report.parameters.put("REPORT_CONNECTION", conn);
				EnquiryDetails();
				report.dataList = dataList;
				report.doPost(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}
	
	public void EnquiryDetails() throws IOException {
		
		HashMap dataMap;
		String enquiry_buyertype_id = "", enquiry_custtype_id = "", enquiry_age_id = "", enquiry_gender_id = "", enquiry_soe_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		String enquiry_purchasemode_id = "", enquiry_income_id = "", enquiry_ownership_id = "";
		int enquiry_familymember_count = 0;
		try {
			StrSql = "SELECT enquiry_id, enquiry_title, enquiry_desc, enquiry_dmsno,"
					+ " COALESCE(occ_name,'') AS occ_name,"
					+ " coalesce(contact_jobtitle,'') AS contact_jobtitle,"
					+ " enquiry_qcsno, enquiry_date, enquiry_close_date, enquiry_monthkms_id, enquiry_item_id,"
					+ " enquiry_purchasemode_id,  enquiry_income_id,"
					+ " enquiry_familymember_count, enquiry_expectation_id, "
					+ " enquiry_othercar, enquiry_buyertype_id, enquiry_ownership_id, "
					+ " enquiry_existingvehicle, enquiry_purchasemonth, enquiry_loancompletionmonth, enquiry_currentemi,"
					+ " enquiry_kms, enquiry_loanfinancer, enquiry_expectedprice, enquiry_quotedprice,"
					+ " enquiry_age_id, enquiry_occ_id, enquiry_fuelallowance,"
					+ " ABS(CAST(enquiry_expectedprice AS CHAR) - CAST(enquiry_quotedprice AS CHAR)) AS gap,"
					+ " customer_id, customer_name, contact_id,"
					+ " CONCAT(title_desc,' ',contact_fname,' '," + " contact_lname) AS contactname,"
					+ " contact_phone1, contact_mobile1, contact_mobile2, contact_email1,"
					+ " contact_email2, contact_city_id, city_name, contact_pin, contact_address,"
					+ " coalesce(enquiry_model_id, 0) AS enquiry_model_id, coalesce(model_name, 0) AS model_name, enquiry_custtype_id, "
					+ " enquiry_soe_id, COALESCE(option_name,'') AS option_name, enquiry_priorityenquiry_id,"
					+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') AS emp_name, title_gender,"
					+ " branch_id, branch_code, concat(branch_name,' (', branch_code, ')') AS branchname, branch_logo, comp_logo"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_ownership ON ownership_id = enquiry_ownership_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_age ON age_id = enquiry_age_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_ownership ON status_id = enquiry_status_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_expectation ON expectation_id = enquiry_expectation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_income ON income_id = enquiry_income_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms ON monthkms_id = enquiry_monthkms_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype ON buyertype_id = enquiry_buyertype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = enquiry_option_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode ON purchasemode_id = enquiry_purchasemode_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";
			
			// SOP("StrSql======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			
			StrSql = "SELECT COALESCE (model_name, '') AS model_name, "
					+ " DATE_FORMAT(testdrive_time, '%d/%m/%Y') AS testdrive_time, "
					+ " DATEDIFF(testdrive_time_from, testdrive_time_to ) AS diff,"
					+ " CASE WHEN testdrive_location_id = 1 THEN 'X' ELSE '' END AS testdrive_location_id1, "
					+ " CASE WHEN testdrive_location_id = 2 THEN 'X' ELSE '' END AS testdrive_location_id2, "
					+ " CASE WHEN testdrive_fb_taken = 2 THEN testdrive_fb_notes ELSE '' END AS testdrive_notes "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
					+ " WHERE 1 = 1 "
					+ " AND testdrive_enquiry_id = " + enquiry_id + ""
					+ " GROUP BY testdrive_id "
					+ " ORDER BY testdrive_id DESC ";
			// SOP("StrSql---------------" + StrSql);
			String tdmodel_name = ExecuteQuery(StrSql);
			
			StrSql = "SELECT DATE_FORMAT(followup_followup_time, '%d/%m/%Y' ) AS followup_followup_time, "
					+ " followup_desc, COALESCE (DATEDIFF(enquiry_close_date, enquiry_date ), '' ) AS diff, "
					+ " COALESCE (followuptype_name, '') AS followuptype_name, "
					+ " COALESCE (DATE_FORMAT((SELECT followup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " WHERE followup_enquiry_id = " + enquiry_id
					+ " AND followup_id > f.followup_id "
					+ " ORDER BY followup_followup_time LIMIT 1), '%d/%m/%Y' ), '') AS nextfollowup "
					+ " FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup f "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = f.followup_enquiry_id "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = f.followup_followuptype_id "
					+ " WHERE 1 = 1 "
					+ " AND f.followup_enquiry_id = " + enquiry_id
					+ " ORDER BY f.followup_followup_time ";
			String followup_followup_time = ExecuteQuery(StrSql);
			
			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				dataMap.put("comp_id", comp_id);
				dataMap.put("enquiry_id", enquiry_id);
				dataMap.put("enquiry_dmsno", crs.getString("enquiry_dmsno"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("contactname", crs.getString("contactname"));
				dataMap.put("occupation", crs.getString("occ_name"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_mobile2", crs.getString("contact_mobile2"));
				dataMap.put("contact_phone1", crs.getString("contact_phone1"));
				dataMap.put("contact_jobtitle", crs.getString("contact_jobtitle"));
				dataMap.put("contact_email1", crs.getString("contact_email1"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("city_name", crs.getString("city_name"));
				dataMap.put("contact_pin", crs.getString("contact_pin"));
				dataMap.put("chk_1a", Checkmark("Omni", crs.getString("model_name")));
				dataMap.put("chk_1b", Checkmark("Alto", crs.getString("model_name")));
				dataMap.put("chk_1c", Checkmark("WagonR", crs.getString("model_name")));
				dataMap.put("chk_1d", Checkmark("Swift Dzire", crs.getString("model_name")));
				dataMap.put("chk_1e", Checkmark("SX4", crs.getString("model_name")));
				dataMap.put("chk_1f", Checkmark("Versa", crs.getString("model_name")));
				dataMap.put("chk_1g", Checkmark("Swift", crs.getString("model_name")));
				dataMap.put("chk_1h", Checkmark("Ritz", crs.getString("model_name")));
				dataMap.put("chk_1i", Checkmark("Celerio", crs.getString("model_name")));
				dataMap.put("chk_1j", Checkmark("Ertiga", crs.getString("model_name")));
				dataMap.put("chk_1k", Checkmark("EECO", crs.getString("model_name")));
				dataMap.put("chk_1l", Checkmark("Ciaz", crs.getString("model_name")));
				dataMap.put("chk_2a", Checkmark("1", crs.getString("enquiry_custtype_id")));
				dataMap.put("chk_2b", Checkmark("2", crs.getString("enquiry_custtype_id")));
				dataMap.put("chk_2c", Checkmark("3", crs.getString("enquiry_custtype_id")));
				
				dataMap.put("chk_3a", Checkmark("1", crs.getString("enquiry_age_id")));
				dataMap.put("chk_3b", Checkmark("2", crs.getString("enquiry_age_id")));
				dataMap.put("chk_3c", Checkmark("3", crs.getString("enquiry_age_id")));
				dataMap.put("chk_3d", Checkmark("4", crs.getString("enquiry_age_id")));
				
				dataMap.put("chk_4a", Checkmark("1", crs.getString("title_gender")));
				dataMap.put("chk_4b", Checkmark("2", crs.getString("title_gender")));
				
				if (CNumeric(crs.getString("enquiry_fuelallowance")).equals("0")) {
					dataMap.put("chk_5a", "");
				} else {
					dataMap.put("chk_5a", "X");
				}
				
				dataMap.put("enquiry_fuelallowance", crs.getString("enquiry_fuelallowance"));
				
				dataMap.put("chk_6a", Checkmark("1", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6b", Checkmark("2", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6c", Checkmark("3", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6g", Checkmark("3", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6d", Checkmark("6", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6e", Checkmark("7", crs.getString("enquiry_soe_id")));
				dataMap.put("chk_6f", Checkmark("8", crs.getString("enquiry_soe_id")));
				
				dataMap.put("chk_7a", Checkmark("1", crs.getString("enquiry_monthkms_id")));
				dataMap.put("chk_7b", Checkmark("2", crs.getString("enquiry_monthkms_id")));
				dataMap.put("chk_7c", Checkmark("3", crs.getString("enquiry_monthkms_id")));
				dataMap.put("chk_7d", Checkmark("4", crs.getString("enquiry_monthkms_id")));
				
				dataMap.put("chk_8a", Checkmark("1", crs.getString("enquiry_purchasemode_id")));
				dataMap.put("chk_8b", Checkmark("2", crs.getString("enquiry_purchasemode_id")));
				dataMap.put("chk_8c", Checkmark("3", crs.getString("enquiry_purchasemode_id")));
				dataMap.put("chk_8d", Checkmark("4", crs.getString("enquiry_purchasemode_id")));
				
				dataMap.put("chk_9a", Checkmark("1", crs.getString("enquiry_income_id")));
				dataMap.put("chk_9b", Checkmark("2", crs.getString("enquiry_income_id")));
				dataMap.put("chk_9c", Checkmark("3", crs.getString("enquiry_income_id")));
				dataMap.put("chk_9d", Checkmark("4", crs.getString("enquiry_income_id")));
				
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("colour", crs.getString("option_name"));
				dataMap.put("enquiry_familymember_count", crs.getString("enquiry_familymember_count"));
				
				dataMap.put("chk_10a", Checkmark("1", crs.getString("enquiry_priorityenquiry_id")));
				dataMap.put("chk_10b", Checkmark("2", crs.getString("enquiry_priorityenquiry_id")));
				dataMap.put("chk_10c", Checkmark("3", crs.getString("enquiry_priorityenquiry_id")));
				
				dataMap.put("enquiry_othercar", crs.getString("enquiry_othercar"));
				
				dataMap.put("chk_11a", Checkmark("1", crs.getString("enquiry_buyertype_id")));
				dataMap.put("chk_11b", Checkmark("2", crs.getString("enquiry_buyertype_id")));
				dataMap.put("chk_11c", Checkmark("3", crs.getString("enquiry_buyertype_id")));
				
				dataMap.put("enquiry_existingvehicle", crs.getString("enquiry_existingvehicle"));
				dataMap.put("enquiry_purchasemonth", crs.getString("enquiry_purchasemonth"));
				
				dataMap.put("chk_12a", Checkmark("1", crs.getString("enquiry_ownership_id")));
				dataMap.put("chk_12b", Checkmark("2", crs.getString("enquiry_ownership_id")));
				dataMap.put("chk_12c", Checkmark("3", crs.getString("enquiry_ownership_id")));
				
				dataMap.put("enquiry_loancompletionmonth", crs.getString("enquiry_loancompletionmonth"));
				dataMap.put("enquiry_currentemi", crs.getString("enquiry_currentemi"));
				dataMap.put("enquiry_kms", crs.getString("enquiry_kms"));
				dataMap.put("enquiry_loanfinancer", crs.getString("enquiry_loanfinancer"));
				dataMap.put("enquiry_expectedprice", IndDecimalFormat(crs.getString("enquiry_expectedprice")) + " /-");
				dataMap.put("enquiry_quotedprice", IndDecimalFormat(crs.getString("enquiry_quotedprice")) + " /-");
				dataMap.put("gap", IndDecimalFormat(crs.getString("gap")) + " /-");
				dataMap.put("tdmodel_name", tdmodel_name);
				dataMap.put("followup_followup_time", followup_followup_time);
				dataList.add(dataMap);
			}
			crs.close();
			
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected String Checkmark(String name, String value) {
		String checked = "";
		if (value.contains(name) && !value.contains("Swift")) {
			checked = "X";
		}
		if (value.equals("Swift") && name.equals("Swift")) {
			checked = "X";
		} else if (value.equals("Swift Dzire") && name.equals("Swift Dzire")) {
			checked = "X";
		}
		return checked;
	}
}
