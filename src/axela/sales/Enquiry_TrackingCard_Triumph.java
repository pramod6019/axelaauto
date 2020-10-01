package axela.sales;

// 19th Apr 2016

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_TrackingCard_Triumph extends Connect {

	public String enquiry_id = "0", brand_id = "0", emp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String comp_id = "0", reportfrom;
	public String ExeAccess = "";
	public List dataList = new ArrayList();
	public HashMap parameters = new HashMap();
	public String FollowupDetails = "";
	Connection conn = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("comp_id------------" + comp_id);
			emp_id = CNumeric(GetSession("emp_id", request));
			reportfrom = PadQuotes(request.getParameter("dr_report"));
			// SOP("reportfrom------------" + reportfrom);
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				SetSession("enquiry_id", enquiry_id, request);
				SetSession("brand_id", brand_id, request);

				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = EnquiryDetails();
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

	public List<Map> EnquiryDetails() {
		HashMap dataMap;
		try {
			StrSql = "SELECT enquiry_id,"
					+ " COALESCE(enquiry_date,'') AS enquiry_date,"
					+ " COALESCE(enquiry_no,'') AS enquiry_no, emp_name, customer_name, customer_address,"
					+ " city_name, customer_pin, state_name, "
					+ " COALESCE(customer_mobile1,'') AS customer_mobile1,"
					+ " COALESCE(customer_phone1,'') AS  customer_phone1,"
					+ " COALESCE(customer_email1,'') AS customer_email1,"
					+ " customer_id, customer_name, model_name,item_id, enquiry_finance,"
					+ " soe_name, enquiry_item_id,"
					+ " COALESCE(soe_id,'') AS soe_id,"
					+ " COALESCE(sob_id,'') AS sob_id,"
					+ " COALESCE(model_name,'') AS model_name,"
					+ " COALESCE(option_name, '') AS option_name,"
					+ " COALESCE(testdrive_time, '') AS testdrive_time,"
					+ " COALESCE(testdrive_fb_taken,0) AS testdrive_fb_taken,"
					+ " COALESCE(testdrive_fb_entry_id, 0) AS testdrive_fb_entry_id,"
					+ " COALESCE(testdrive_enquiry_id,0) AS testdrive_enquiry_id,"
					+ " COALESCE(enquiry_occ_id, 0) AS enquiry_occ_id,"
					+ " COALESCE(enquiry_age_id, 0) AS enquiry_age_id,"
					+ " COALESCE(enquiry_soe_id, 0) AS enquiry_soe_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_custtype ON custtype_id = enquiry_custtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";
			// SOP("StrSql---------tracking card----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("enquiry_no", crs.getString("enquiry_no"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", crs.getString("customer_address"));
				dataMap.put("city_name", crs.getString("city_name"));
				dataMap.put("customer_pin", crs.getString("customer_pin"));
				dataMap.put("state_name", crs.getString("state_name"));
				dataMap.put("customer_mobile", crs.getString("customer_mobile1"));
				SOP("customer_phone0----------" + crs.getString("customer_phone1"));
				dataMap.put("customer_phone", crs.getString("customer_phone1"));
				dataMap.put("customer_email", crs.getString("customer_email1"));
				dataMap.put("enquiry_item_id", crs.getInt("enquiry_item_id"));
				dataMap.put("option_name", crs.getString("option_name"));
				dataMap.put("testdrive_time", strToShortDate(crs.getString("testdrive_time")));
				if (crs.getString("testdrive_fb_taken").equals("1")) {
					dataMap.put("testdrive_fb_taken", "Taken");
				} else if (crs.getString("testdrive_fb_taken").equals("2")) {
					dataMap.put("testdrive_fb_taken", "Not Taken");
				} else {
					dataMap.put("testdrive_fb_taken", "");
				}
				if (!crs.getString("testdrive_fb_entry_id").equals("0")) {
					dataMap.put("testdrive_fb_form", "Yes");
				} else {
					dataMap.put("testdrive_fb_form", "No");
				}
				dataMap.put("enquiry_occ_id", crs.getInt("enquiry_occ_id"));
				dataMap.put("enquiry_age_id", crs.getInt("enquiry_age_id"));
				dataMap.put("enquiry_soe_id", crs.getInt("enquiry_soe_id"));
				dataMap.put("esc_dateof_purchase", (""));
				dataList.add(dataMap);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return dataList;
	}

}
