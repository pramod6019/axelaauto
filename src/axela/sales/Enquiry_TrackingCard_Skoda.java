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

public class Enquiry_TrackingCard_Skoda extends Connect {
	
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
			StrSql = "SELECT enquiry_id, branch_invoice_name,"
					+ " CONCAT(branch_add, ', ', branch.city_name, '-', branch_pin) AS branch_add,"
					+ " branch_phone1, branch_phone2, branch_logo,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE (contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE (contact_email1, '') AS contact_email1,"
					+ " loc.city_name,"
					+ " COALESCE (enquiry_date, '') AS enquiry_date,"
					+ " COALESCE (soe_name, '') AS soe_name,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " COALESCE (model_name, '') AS model_name,"
					+ " COALESCE (fueltype_name, '') AS fueltype_name,"
					+ " COALESCE (testdrive_time, '') AS testdrive_time,"
					+ " enquiry_existingvehicle,"
					+ " enquiry_tradein_preownedvariant_id,"
					+ " COALESCE (na_skoda_financerequired,'') AS na_skoda_financerequired,"
					+ " contact_address,"
					+ " COALESCE(item_name, '') AS item_name,"
					+ " COALESCE (na_skoda_ownbusiness, '') AS na_skoda_ownbusiness,"
					+ " COALESCE (na_skoda_companyname, '') AS na_skoda_companyname,"
					+ " contact_jobtitle,"
					+ " COALESCE(location_name, '') AS location_name,"
					+ " COALESCE(sob_name, '') AS sob_name,"
					+ " enquiry_close_date, enquiry_buyertype_id,"
					+ " COALESCE (na_skoda_currentcarappxkmsrun , '') AS na_skoda_currentcarappxkmsrun,"
					+ " COALESCE (GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)),'') AS variant_name,"
					+ " COALESCE (na_skoda_whatareyoulookingfor,'') AS na_skoda_whatareyoulookingfor,"
					+ " COALESCE (na_skoda_numberoffamilymembers,'') AS na_skoda_numberoffamilymembers,"
					+ " COALESCE (na_skoda_whowilldrive,'') AS na_skoda_whowilldrive,"
					+ " COALESCE (na_skoda_whoareyoubuyingfor,'') AS na_skoda_whoareyoubuyingfor,"
					+ " COALESCE (na_skoda_newcarappxrun,'') AS na_skoda_newcarappxrun,"
					+ " COALESCE (na_skoda_wherewillbecardriven,'') AS na_skoda_wherewillbecardriven"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_na ON na_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city loc ON loc.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch ON branch.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = loc.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_enquiry_id = enquiry_id"
					+ " LEFT JOIN axela_preowned_variant ON variant_id = currentcars_variant_id"
					+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = enquiry_fueltype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id
					+ BranchAccess
					+ ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";
			// SOP("StrSql---------tracking card----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			
			while (crs.next()) {
				dataMap = new HashMap();
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_add", crs.getString("branch_add"));
				dataMap.put("branch_phone1", crs.getString("branch_phone1"));
				dataMap.put("branch_phone2", crs.getString("branch_phone2"));
				dataMap.put("contactname", crs.getString("contactname"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_email1", crs.getString("contact_email1"));
				dataMap.put("city_name", crs.getString("city_name"));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("soe_name", crs.getString("soe_name"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("fueltype_name", crs.getString("fueltype_name"));
				dataMap.put("testdrive_time", strToShortDate(crs.getString("testdrive_time")));
				dataMap.put("enquiry_existingvehicle", crs.getString("enquiry_existingvehicle"));
				if (!crs.getString("enquiry_tradein_preownedvariant_id").equals("0")) {
					dataMap.put("enquiry_tradein_preownedvariant_id", "Yes");
				} else {
					dataMap.put("enquiry_tradein_preownedvariant_id", "No");
				}
				dataMap.put("na_skoda_financerequired", crs.getString("na_skoda_financerequired"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("na_skoda_ownbusiness", crs.getString("na_skoda_ownbusiness"));
				dataMap.put("na_skoda_companyname", crs.getString("na_skoda_companyname"));
				dataMap.put("contact_jobtitle", crs.getString("contact_jobtitle"));
				dataMap.put("sob_name", crs.getString("sob_name"));
				dataMap.put("enquiry_close_date", strToShortDate(crs.getString("enquiry_close_date")));
				if (crs.getString("branch_logo").equals("")) {
					dataMap.put("branch_logo", "");
				} else {
					dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				}
				if (crs.getString("enquiry_buyertype_id").equals("1")) {
					dataMap.put("enquiry_buyertype_id", 1);// checkbox
				} else if (crs.getString("enquiry_buyertype_id").equals("2")) {
					dataMap.put("enquiry_buyertype_id", 2);// checkbox
				} else if (crs.getString("enquiry_buyertype_id").equals("3")) {
					dataMap.put("enquiry_buyertype_id", 3);// checkbox
				} else {
					dataMap.put("enquiry_buyertype_id", 0);// checkbox
				}
				if (unescapehtml(crs.getString("na_skoda_currentcarappxkmsrun")).equals("<10,000")) {
					dataMap.put("na_skoda_currentcarappxkmsrun", 1);// checkbox
				} else if (unescapehtml(crs.getString("na_skoda_currentcarappxkmsrun")).equals("10,000-30,000")) {
					dataMap.put("na_skoda_currentcarappxkmsrun", 2);// checkbox
				} else if (unescapehtml(crs.getString("na_skoda_currentcarappxkmsrun")).equals("30,000-60,000")) {
					dataMap.put("na_skoda_currentcarappxkmsrun", 3);// checkbox
				} else if (unescapehtml(crs.getString("na_skoda_currentcarappxkmsrun")).equals(">60,000")) {
					dataMap.put("na_skoda_currentcarappxkmsrun", 4);// checkbox
				} else {
					dataMap.put("na_skoda_currentcarappxkmsrun", 0);// checkbox
				}
				if (crs.getString("variant_name").equals("")) {
					dataMap.put("variant_name", "");
				} else {
					dataMap.put("variant_name", crs.getString("variant_name"));
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Comfort")) {
					dataMap.put("comfort", 1);// checkbox
				} else {
					dataMap.put("comfort", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Styling/Appearance")) {
					dataMap.put("styling_appearance", 1);// checkbox
				} else {
					dataMap.put("styling_appearance", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Quality/Reliability")) {
					dataMap.put("quality_reliability", 1);// checkbox
				} else {
					dataMap.put("quality_reliability", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Fuel Economy")) {
					dataMap.put("fuel_economy", 1);// checkbox
				} else {
					dataMap.put("fuel_economy", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Safety")) {
					dataMap.put("safety", 1);// checkbox
				} else {
					dataMap.put("safety", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Ease Of Use")) {
					dataMap.put("ease_of_use", 1);// checkbox
				} else {
					dataMap.put("ease_of_use", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Roominess/Storage")) {
					dataMap.put("roominess_storage", 1);// checkbox
				} else {
					dataMap.put("roominess_storage", 0);// checkbox
				}
				if (crs.getString("na_skoda_whatareyoulookingfor").contains("Driving Characteristics")) {
					dataMap.put("driving_characteristics", 1);// checkbox
				} else {
					dataMap.put("driving_characteristics", 0);// checkbox
				}
				if (crs.getString("na_skoda_numberoffamilymembers").equals("1-2")) {
					dataMap.put("na_skoda_numberoffamilymembers", 1);// checkbox
				} else if (crs.getString("na_skoda_numberoffamilymembers").equals("2-4")) {
					dataMap.put("na_skoda_numberoffamilymembers", 2);// checkbox
				} else if (crs.getString("na_skoda_numberoffamilymembers").equals("4-6")) {
					dataMap.put("na_skoda_numberoffamilymembers", 3);// checkbox
				} else if (crs.getString("na_skoda_numberoffamilymembers").equals("6 above")) {
					dataMap.put("na_skoda_numberoffamilymembers", 4);// checkbox
				} else {
					dataMap.put("na_skoda_numberoffamilymembers", 0);// checkbox
				}
				if (crs.getString("na_skoda_whowilldrive").equals("Self-driven")) {
					dataMap.put("na_skoda_whowilldrive", 1);// checkbox
				} else if (crs.getString("na_skoda_whowilldrive").equals("Chauffer driven")) {
					dataMap.put("na_skoda_whowilldrive", 2);// checkbox
				} else {
					dataMap.put("na_skoda_whowilldrive", 0);// checkbox
				}
				if (crs.getString("na_skoda_whoareyoubuyingfor").equals("Self")) {
					dataMap.put("na_skoda_whoareyoubuyingfor", 1);// checkbox
				} else if (crs.getString("na_skoda_whoareyoubuyingfor").equals("Spouse")) {
					dataMap.put("na_skoda_whoareyoubuyingfor", 2);// checkbox
				} else if (crs.getString("na_skoda_whoareyoubuyingfor").equals("Son/Daughter")) {
					dataMap.put("na_skoda_whoareyoubuyingfor", 3);// checkbox
				} else if (crs.getString("na_skoda_whoareyoubuyingfor").equals("Parents")) {
					dataMap.put("na_skoda_whoareyoubuyingfor", 4);// checkbox
				} else {
					dataMap.put("na_skoda_whoareyoubuyingfor", 0);// checkbox
				}
				if (unescapehtml(crs.getString("na_skoda_newcarappxrun")).equals("<10")) {
					dataMap.put("na_skoda_newcarappxrun", 1);// checkbox
				} else if (unescapehtml(crs.getString("na_skoda_newcarappxrun")).equals("<70")) {
					dataMap.put("na_skoda_newcarappxrun", 2);// checkbox
				} else if (unescapehtml(crs.getString("na_skoda_newcarappxrun")).equals(">70")) {
					dataMap.put("na_skoda_newcarappxrun", 3);// checkbox
				} else {
					dataMap.put("na_skoda_newcarappxrun", 0);// checkbox
				}
				if (crs.getString("na_skoda_wherewillbecardriven").equals("City")) {
					dataMap.put("na_skoda_wherewillbecardriven", 1);// checkbox
				} else if (crs.getString("na_skoda_wherewillbecardriven").equals("HighWay")) {
					dataMap.put("na_skoda_wherewillbecardriven", 2);// checkbox
				} else {
					dataMap.put("na_skoda_wherewillbecardriven", 0);// checkbox
				}
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
