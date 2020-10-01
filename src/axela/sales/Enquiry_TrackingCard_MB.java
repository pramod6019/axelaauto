package axela.sales;

//Bhavya 19th Apr 2016

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

public class Enquiry_TrackingCard_MB extends Connect {
	public static String ClientName = "Joshi Auto Zone Pvt.Ltd";
	public String enquiry_id = "0", brand_id = "0", emp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String comp_id = "0", reportfrom;
	public String ExeAccess = "";
	public List dataList = new ArrayList();
	public HashMap parameters = new HashMap();
	public String FollowupDetails = "", enquiry_mb_currentcars = "", all[] = {};
	public String checkboxval_audi = "", checkboxval_bmw = "", checkboxval_others = "", checkboxval_vw = "";
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
			SOPError("Axelaauto====" + this.getClass().getName());
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

			StrSql = "SELECT enquiry_id,enquiry_title,status_name,"
					+ " COALESCE(enquiry_mb_type, 0) AS enquiry_mb_type, enquiry_date, enquiry_close_date,"
					+ " enquiry_status_desc,enquiry_status_id, branch_add,"
					+ " COALESCE (testdrive_confirmed, 0) AS testdrive_confirmed,"
					+ " enquiry_status_id, enquiry_priorityenquiry_id, enquirytype_name, "
					+ " COALESCE(enquiry_enquirytype_id, 0) AS enquiry_enquirytype_id,"
					+ " COALESCE(enquiry_mb_occupation, 0) AS enquiry_mb_occupation, "
					+ " COALESCE(enquiry_mb_carusage, 0) AS enquiry_mb_carusage, enquiry_monthkms_id, enquiry_custtype_id, customer_id,"
					+ " customer_name, contact_id, income_name,"
					+ " COALESCE (company_name, '') AS company_name, contact_mobile1, contact_mobile2,"
					+ " CONCAT( title_desc, ' ', contact_fname, ' ', contact_lname ) AS contactname,"
					+ " contact_phone1, contact_email1, contact_email2, contact_city_id, city_name, contact_pin,"
					+ " contact_address, contact_dob,"
					+ " COALESCE (model_name, '') AS model_name, priorityenquiry_name,"
					+ " COALESCE(priorityenquiry_id,0) AS priorityenquiry_id,"
					+ " COALESCE (testdrive_time_from, '') AS testdrive_time_from,"
					+ " COALESCE (testdrive_time_to, '') AS testdrive_time_to,"
					+ " COALESCE (testdrive_out_kms, '') AS testdrive_out_kms,"
					+ " COALESCE (testdrive_in_kms, '') AS testdrive_in_kms,"
					+ " COALESCE ( DATE_FORMAT( testdrive_time, '%d/%m/%Y %H:%i' ), '' ) AS testdrive_time,"
					+ " IF ( enquiry_mb_income = 1, '<50 lakhs', IF ( enquiry_mb_income = 2, '50-70 lakhs',"
					+ " IF ( enquiry_mb_income = 3, '>75 lakhs', '' ) ) ) AS 'Income Name', item_name,"
					+ " COALESCE (monthkms_name, '') AS monthkms_name,"
					+ " COALESCE (enquiry_mb_currentcars, '') AS brand,"
					+ " COALESCE (enquiry_mb_currentcars, '') AS enquiry_mb_currentcars,"
					+ " followup_desc,"
					+ " COALESCE(enquiry_mb_drivingpattern, 0) AS enquiry_mb_drivingpattern,"
					+ " COALESCE (custtype_name, '') AS custtype_name,"
					+ " COALESCE(soe_name, '') AS soe_name,"
					+ " COALESCE(sob_name,'') AS sob_name, emp_id,"
					+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
					+ " branch_id, branch_code,"
					+ " CONCAT( branch_name, ' (', branch_code, ')' ) AS branchname,"
					+ " branch_logo, COALESCE ( ( SELECT followup_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " WHERE followup_desc = ''"
					+ " AND followup_enquiry_id =  " + enquiry_id + ""
					+ " ORDER BY followup_id DESC LIMIT 1 ), '' ) AS followup_followup_time,"
					+ " COALESCE ( ( SELECT lostcase1_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = enquiry_lostcase1_id "
					+ " WHERE enquiry_id = " + enquiry_id + "), '' ) AS lost1,"
					+ " COALESCE ( ( SELECT lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = enquiry_lostcase2_id "
					+ " WHERE enquiry_id = " + enquiry_id + "), '' ) AS lost2,"
					+ " COALESCE ( ( SELECT lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id = enquiry_lostcase3_id"
					+ " WHERE enquiry_id = " + enquiry_id + "), '' ) AS lost3,"
					+ " COALESCE ( ( SELECT DATE_FORMAT( followupnext.followup_followup_time, '%d/%m/%Y %H:%i' )"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup AS followupnext "
					+ " WHERE followupnext.followup_enquiry_id = enquiry_id "
					+ " AND followupnext.followup_followup_time > axela_sales_enquiry_followup.followup_followup_time"
					+ " LIMIT 1 ), '' ) AS 'Next_Follow-up', "
					+ " COALESCE (followuptype_name, '') AS 'Follow-up Type' "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_company ON company_id = contact_company_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms ON monthkms_id = enquiry_monthkms_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_income ON income_id = enquiry_income_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";

			// SOP("StrSql=enq==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				dataMap.put("dealership_name", ClientName);
				dataMap.put("location", crs.getString("branch_add")); // location of joshi
				dataMap.put("consult_name", crs.getString("emp_name"));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));

				if (crs.getString("enquiry_enquirytype_id").equals("New")) {
					dataMap.put("enquiry_enquirytype_id", 1); // Enquiry Type checkbox
				}
				else {
					dataMap.put("enquiry_enquirytype_id", 2);// Enquiry Type checkbox
				}

				if (crs.getString("priorityenquiry_id").equals("Hot")) {
					dataMap.put("priorityenquiry_id", 1); // Enquiry Status checkbox
				} else if (crs.getString("priorityenquiry_id").equals("Warm")) {
					dataMap.put("priorityenquiry_id", 2); // Enquiry Status checkbox
				} else if (crs.getString("priorityenquiry_id").equals("Cold")) {
					dataMap.put("priorityenquiry_id", 3); // Enquiry Status checkbox
				}

				if (crs.getString("enquiry_mb_occupation").equals("Business")) {
					dataMap.put("enquiry_mb_occupation", 1); // Occupation checkbox
				}
				else if (crs.getString("enquiry_mb_occupation").equals("Salaried")) {
					dataMap.put("enquiry_mb_occupation", 9); // Occupation checkbox
				}
				else if (crs.getString("enquiry_mb_occupation").equals("Self Employed")) {
					dataMap.put("enquiry_mb_occupation", 2); // Occupation checkbox
				} else {
					dataMap.put("enquiry_mb_occupation", 7); // Occupation checkbox
				}

				if (crs.getString("enquiry_mb_type").equals("Corp.")) {
					dataMap.put("enquiry_mb_type", 1); // Type checkbox
				} else if (crs.getString("enquiry_mb_type").equals("Fleet")) {
					dataMap.put("enquiry_mb_type", 3); // Type checkbox
				}

				if (crs.getString("enquiry_mb_drivingpattern").equals("Chauffeur")) {
					dataMap.put("enquiry_mb_drivingpattern", 1);// Driving Pattern checkbox
				} else if (crs.getString("enquiry_mb_drivingpattern").equals("Self")) {
					dataMap.put("enquiry_mb_drivingpattern", 2);// Driving Pattern checkbox
				}

				enquiry_mb_currentcars = crs.getString("enquiry_mb_currentcars");
				all = enquiry_mb_currentcars.split(",");
				for (int i = 0; i < all.length; i++) {
					enquiry_mb_currentcars = all[i];
					if (enquiry_mb_currentcars.equals("Audi")) {
						dataMap.put("enquiry_mb_currentcars_audi", 1);// checkbox
					}
					if (enquiry_mb_currentcars.equals("BMW")) {
						dataMap.put("enquiry_mb_currentcars_bmw", 2);// checkbox
					}
					if (enquiry_mb_currentcars.equals("Others")) {
						dataMap.put("enquiry_mb_currentcars_other", 3);// checkbox
					}
					if (enquiry_mb_currentcars.equals("VW")) {
						dataMap.put("enquiry_mb_currentcars_vw", 4);// checkbox
					}
				}

				dataMap.put("contact_name", crs.getString("contactname"));
				dataMap.put("contact_email", crs.getString("contact_email1"));
				dataMap.put("contact_mobile", crs.getString("contact_mobile1"));
				dataMap.put("contact_area", crs.getString("contact_address"));
				dataMap.put("contact_dob", crs.getString("contact_dob"));
				dataMap.put("company_name", crs.getString("company_name"));
				dataMap.put("address", crs.getString("contact_address"));
				dataMap.put("contact_city", crs.getString("city_name"));
				dataMap.put("contact_pin", crs.getString("contact_pin"));
				dataMap.put("contact_phone", crs.getString("contact_phone1"));
				dataMap.put("res", "");
				dataMap.put("engine_option", crs.getString("item_name"));
				if (crs.getString("enquiry_mb_carusage").equals("Fleet")) {
					dataMap.put("enquiry_mb_carusage", 1); // Car Usage checkbox
				} else if (crs.getString("enquiry_mb_carusage").equals("Gift")) {
					dataMap.put("enquiry_mb_carusage", 2); // Car Usage checkbox
				} else if (crs.getString("enquiry_mb_carusage").equals("Official")) {
					dataMap.put("enquiry_mb_carusage", 3); // Car Usage checkbox
				} else if (crs.getString("enquiry_mb_carusage").equals("Others")) {
					dataMap.put("enquiry_mb_carusage", 4); // Car Usage checkbox
				} else if (crs.getString("enquiry_mb_carusage").equals("Personal")) {
					dataMap.put("enquiry_mb_carusage", 5); // Car Usage checkbox
				}

				dataMap.put("brand", crs.getString("brand"));
				dataMap.put("model", "xxx");
				dataMap.put("grade", "xxx");
				dataMap.put("soe_name", crs.getString("soe_name")); // lead source
				dataMap.put("sob_name", crs.getString("sob_name")); // lead mode
				dataMap.put("testdrive_time", crs.getString("testdrive_time"));
				dataMap.put("emp_name", crs.getString("emp_name")); // given by
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("grade", "");
				if (!crs.getString("testdrive_time_from").equals("") && !crs.getString("testdrive_time_to").equals("")) {
					dataMap.put("date_time", PeriodTime(crs.getString("demo_time_from"), crs.getString("demo_time_to"), "1"));
				} else {
					dataMap.put("date_time", ""); // duration
				}
				dataMap.put("testdrive_out_kms", crs.getString("testdrive_out_kms")); // meter reading start kms
				dataMap.put("testdrive_in_kms", crs.getString("testdrive_in_kms")); // end kms
				dataMap.put("scheduled_date", "");
				dataMap.put("time", "");
				dataMap.put("finance_ten1", "3 Years");
				dataMap.put("finance_ten2", "5 Years");
				dataMap.put("finance_ten3", "7 Years");
				dataMap.put("enquiry_close_date", strToShortDate(crs.getString("enquiry_close_date"))); // expected date of purchase
				dataMap.put("nextfoll_date_time", crs.getString("Next_Follow-up"));
				dataMap.put("how", crs.getString("Follow-up Type")); // how
				dataMap.put("status_name", crs.getString("status_name")); // status
				dataMap.put("enquiry_status_desc", crs.getString("enquiry_status_desc")); // status comments
				dataMap.put("lost1", crs.getString("lost1"));
				dataMap.put("lost2", crs.getString("lost2"));
				dataMap.put("lost3", crs.getString("lost3"));
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
