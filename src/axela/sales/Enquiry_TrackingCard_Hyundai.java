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

public class Enquiry_TrackingCard_Hyundai extends Connect {

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
	Connection conn = null;
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

	public List<Map> EnquiryDetails() throws IOException {

		HashMap dataMap;
		String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_familymember_count = "", enquiry_priorityenquiry_id = "";
		String enquiry_income_id = "", testdrive_fb_taken = "", enquiry_hyundai_topexpectation = "", enquiry_hyundai_modeofpurchase = "";
		try {
			StrSql = "SELECT customer_id, customer_name,"
					+ " contact_mobile1, contact_mobile2, contact_address, contact_email1, contact_pin,"
					+ " city_name, state_name,"
					+ " IF(title_gender = 1, 'Male',"
					+ " IF(title_gender = 2, 'Female', '')) AS title_gender,"
					+ " enquiry_id, enquiry_date, "
					// + " enquiry_hyundai_ex_manuf, "
					+ " enquiry_hyundai_chooseoneoption, enquiry_hyundai_topexpectation, enquiry_hyundai_membersinthefamily, enquiry_hyundai_kmsinamonth,"
					+ " enquiry_hyundai_finalizenewcar, enquiry_hyundai_modeofpurchase, enquiry_hyundai_annualincome, enquiry_hyundai_othercars,"
					// +
					// " enquiry_purchasemonth, enquiry_loancompletionmonth, enquiry_kms,"
					+ " enquirytype_name,"
					+ " ABS(CAST(enquiry_expectedprice AS CHAR) - CAST(enquiry_quotedprice AS CHAR)) AS gap,"
					+ " enquiry_familymember_count, enquiry_priorityenquiry_id,"
					+ " enquiry_purchasemode_id, enquiry_income_id,"
					+ " enquiry_monthkms_id, enquiry_othercar,"
					+ " COALESCE (testdrive_time, '') AS testdrive_time,"
					+ " COALESCE (testdrive_time_from, '') AS testdrive_time_from,"
					+ " COALESCE (testdrive_time_to, '') AS testdrive_time_to,"
					+ " COALESCE (testdrive_fb_taken, '') AS testdrive_fb_taken,"
					+ " COALESCE (testdrive_location_id, '') AS testdrive_location_id,"
					+ " COALESCE (location_name, '') AS location_name,"
					+ " COALESCE (testdrive_notes, '') AS testdrive_notes,"
					+ " COALESCE (ownership_name, '') AS ownership_name,"
					+ " COALESCE (age_name,'') AS age_name,"
					+ " COALESCE (occ_name, '') AS occ_name,"
					+ " COALESCE(model_name, 0) AS model_name,"
					+ " COALESCE (monthkms_id,'') AS monthkms_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_ownership ON ownership_id = enquiry_ownership_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_age ON age_id = enquiry_age_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms ON monthkms_id = enquiry_monthkms_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("TrackingDetails====2==========" + StrSqlBreaker(StrSql));
			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("enquiry_id", enquiry_id);
				dataMap.put("customer_id", crs.getString("customer_id"));
				// dataMap.put("enquiry_date",
				// strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("sc_name", "");
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_mobile", crs.getString("contact_mobile1"));
				dataMap.put("customer_alternate_mobile", crs.getString("contact_mobile2"));
				dataMap.put("address", crs.getString("contact_address"));
				dataMap.put("city", crs.getString("city_name"));
				dataMap.put("state", crs.getString("state_name"));
				dataMap.put("pincode", crs.getString("contact_pin"));
				dataMap.put("email", crs.getString("contact_email1"));
				dataMap.put("occupation", crs.getString("occ_name"));
				dataMap.put("age", crs.getString("age_name"));
				dataMap.put("gender", crs.getString("title_gender"));
				dataMap.put("city1", "");
				dataMap.put("official_address", "");
				dataMap.put("type_enquiry", "");
				dataMap.put("official_email", "");
				// // please choose one option details
				enquiry_buyertype_id = crs.getString("enquiry_hyundai_chooseoneoption");
				// SOP("enquiry_buyertype_id----" + enquiry_buyertype_id);
				if (enquiry_buyertype_id.equals("First Time Buyer")) {
					dataMap.put("chk_1a", "X");
				} else if (enquiry_buyertype_id.equals("Exchange Buyer")) {
					dataMap.put("chk_1b", "X");
				} else if (enquiry_buyertype_id.equals("Additional Car Buyer")) {
					dataMap.put("chk_1c", "X");
				}
				// // How many kilometers you drive in a month details
				enquiry_monthkms_id = crs.getString("enquiry_hyundai_kmsinamonth");
				// SOP("enquiry_monthkms_id==========" + enquiry_monthkms_id);
				if (enquiry_monthkms_id.equals("500 to 1000")) {
					dataMap.put("chk_2a", "X");
				} else if (enquiry_monthkms_id.equals("1000 to 1500")) {
					dataMap.put("chk_2b", "X");
				} else if (enquiry_monthkms_id.equals("1500 to 2000")) {
					dataMap.put("chk_2c", "X");
				} else if (enquiry_monthkms_id.equals(">2000")) {
					dataMap.put("chk_2d", "X");
				}
				// // How many members are there in your family details
				enquiry_familymember_count = crs.getString("enquiry_hyundai_membersinthefamily");
				// SOP("enquiry_familymember_count=========" + enquiry_familymember_count);
				if (enquiry_familymember_count.equals("2")) {
					dataMap.put("chk_3a", "X");
				} else if (enquiry_familymember_count.equals("3 to 5")) {
					dataMap.put("chk_3b", "X");
				} else if (enquiry_familymember_count.equals("6 to 7")) {
					dataMap.put("chk_3c", "X");
				} else if (enquiry_familymember_count.equals("More Than 7")) {
					dataMap.put("chk_3d", "X");
				}
				// // what is your top most expectation from the car details
				enquiry_hyundai_topexpectation = crs.getString("enquiry_hyundai_topexpectation");
				if (enquiry_hyundai_topexpectation.equals("Features")) {
					dataMap.put("chk_4a", "X");
				} else if (enquiry_hyundai_topexpectation.equals("Performance")) {
					dataMap.put("chk_4b", "X");
				} else if (enquiry_hyundai_topexpectation.equals("Looks")) {
					dataMap.put("chk_4c", "X");
				} else if (enquiry_hyundai_topexpectation.equals("Value for Money")) {
					dataMap.put("chk_4d", "X");
				}

				// // By when, are you expecting to finalize your new car
				// details
				enquiry_priorityenquiry_id =
						unescapehtml(crs.getString("enquiry_hyundai_finalizenewcar"));
				// SOP("enquiry_priorityenquiry_id----" + enquiry_priorityenquiry_id);
				enquiry_priorityenquiry_id = enquiry_priorityenquiry_id;
				if (enquiry_priorityenquiry_id.equals("<15 Days (Hot)")) {
					dataMap.put("chk_5a", "X");
				} else if (enquiry_priorityenquiry_id.equals("16 to 30 Days (Warm)")) {
					dataMap.put("chk_5b", "X");
				} else if (enquiry_priorityenquiry_id.equals(">30 Days (Cold)")) {
					dataMap.put("chk_5c", "X");
				}

				// purchase mode
				enquiry_hyundai_modeofpurchase = crs.getString("enquiry_hyundai_modeofpurchase");
				// SOP("enquiry_hyundai_modeofpurchase---------" + enquiry_hyundai_modeofpurchase);
				if (enquiry_hyundai_modeofpurchase.equals("Cash")) {
					dataMap.put("chk_6a", "X");
				} else if (enquiry_hyundai_modeofpurchase.equals("Finance")) {
					dataMap.put("chk_6b", "X");
				} else if (enquiry_hyundai_modeofpurchase.equals("Self Arranged Finance")) {
					dataMap.put("chk_6c", "X");
				} else if (enquiry_hyundai_modeofpurchase.equals("Company Finance")) {
					dataMap.put("chk_6d", "X");
				}
				// // what is your approximate annual household income details
				enquiry_income_id = crs.getString("enquiry_hyundai_annualincome");
				if (enquiry_income_id.equals("<2.5 Lakhs")) {
					dataMap.put("chk_7a", "X");
				} else if (enquiry_income_id.equals("2.5 Lakhs to 5 Lakhs")) {
					dataMap.put("chk_7b", "X");
				} else if (enquiry_income_id.equals("5 Lakhs to 10 Lakhs")) {
					dataMap.put("chk_7c", "X");
				} else if (enquiry_income_id.equals("10 Lakhs")) {
					dataMap.put("chk_7d", "X");
				}
				// // Which model are you interested in details
				dataMap.put("model_name", crs.getString("model_name"));
				// // which others are you considering
				dataMap.put("enquiry_othercar",
						crs.getString("enquiry_hyundai_othercars"));
				// // test drive details
				dataMap.put("yes1", "");
				dataMap.put("yes2", "");
				dataMap.put("date_time", crs.getString("testdrive_time"));
				// dataMap.put("date_time",
				// strToLongDate(crs.getString("testdrive_time")));
				// // PeriodTime =
				// PeriodTime(crs.getString("testdrive_time_from"),
				// // crs.getString("testdrive_time_to"), "1");
				dataMap.put("duration", "");
				dataMap.put("location", crs.getString("location_name"));
				dataMap.put("model", crs.getString("model_name"));
				testdrive_fb_taken = crs.getString("testdrive_fb_taken");
				if (testdrive_fb_taken.equals("1")) {
					dataMap.put("feedback_taken", "YES");
				} else if (testdrive_fb_taken.equals("0")) {
					dataMap.put("feedback_taken", "NO");
				}
				dataMap.put("model", crs.getString("model_name"));
				dataMap.put("enterd_cdms", "");
				dataMap.put("reasons_testdrive",
						crs.getString("testdrive_notes"));
				//
				// // exchange details
				dataMap.put("gap", crs.getString("gap") + "/-");
				// // summary
				dataMap.put("status", "");
				dataMap.put("model_varient_enquired", "");
				dataMap.put("model_varient_booked", "");
				dataMap.put("date_booking", "");

				dataList.add(dataMap);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return dataList;
	}
}
