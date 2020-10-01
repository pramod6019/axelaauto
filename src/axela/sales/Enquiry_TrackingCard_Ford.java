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

public class Enquiry_TrackingCard_Ford extends Connect {

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
			StrSql = "SELECT customer_id, customer_name,"
					+ " contact_mobile1, contact_address, contact_email1, contact_dob,"
					+ " enquiry_id, enquiry_entry_date,  enquiry_ford_ex_fueltype, enquiry_ford_kmsdriven, enquiry_ford_newvehfor,"
					+ " enquiry_ford_investment, enquiry_ford_colourofchoice, enquiry_ford_cashorfinance,"
					+ " enquiry_ford_currentcar, enquiry_ford_exchangeoldcar, enquiry_ford_othercarconcideration,"
					+ " enquiry_ford_ex_make, enquiry_ford_ex_model, enquiry_ford_ex_derivative, enquiry_ford_ex_year,"
					+ " enquiry_ford_ex_odoreading, enquiry_ford_ex_doors, enquiry_ford_ex_bodystyle, enquiry_ford_ex_enginesize,"
					+ " enquiry_ford_ex_fueltype, enquiry_ford_ex_drive, enquiry_ford_ex_transmission, enquiry_ford_ex_colour,"
					+ " enquiry_ford_ex_priceoffered, enquiry_ford_ex_estmtprice,"
					+ " priorityenquiry_id, custtype_id,"
					+ " COALESCE(soe_id,'') AS soe_id,"
					+ " COALESCE(sob_id,'') AS sob_id,"
					+ " COALESCE(model_name,'') AS model_name,"
					+ " COALESCE(occ_name,'') AS occ_name,"
					+ " COALESCE(testdrive_enquiry_id,0) AS testdrive_enquiry_id,"
					+ " COALESCE(so_enquiry_id,0) AS  so_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_custtype ON custtype_id = enquiry_custtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				dataMap.put("enquiry_id", enquiry_id);
				dataMap.put("enquiry_entry_date", strToShortDate(crs.getString("enquiry_entry_date")));
				dataMap.put("customer_id", crs.getString("customer_id"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("contact_dob", crs.getString("contact_dob"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("model_name", crs.getString("model_name"));
				if (crs.getString("enquiry_ford_ex_fueltype").equals("Diesel")) {
					dataMap.put("diesel", "X");
				} else if (crs.getString("enquiry_ford_ex_fueltype").equals("Petrol")) {
					dataMap.put("petrol", "X");
				}
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("contact_email1", crs.getString("contact_email1"));
				dataMap.put("occ_name", crs.getString("occ_name"));
				// soe details
				if (crs.getString("soe_id").equals("1")) {
					dataMap.put("walk-in", "X");
				} else if (crs.getString("soe_id").equals("2")) {
					dataMap.put("tele-in", "X");
				} else if (crs.getString("soe_id").equals("3")) {
					dataMap.put("referral", "X");
				} else if (crs.getString("soe_id").equals("4")) {
					dataMap.put("dsa", "X");
				} else if (crs.getString("soe_id").equals("6")) {
					dataMap.put("event", "X");
				}
				// sob details
				if (crs.getString("sob_id").equals("2")) {
					dataMap.put("unknown", "X");
				} else if (crs.getString("soe_id").equals("3")) {
					dataMap.put("tv", "X");
				} else if (crs.getString("soe_id").equals("4")) {
					dataMap.put("event", "X");
				} else if (crs.getString("soe_id").equals("5")) {
					dataMap.put("direct-mail", "X");
				} else if (crs.getString("soe_id").equals("6")) {
					dataMap.put("internet", "X");
				} else if (crs.getString("soe_id").equals("7")) {
					dataMap.put("press", "X");
				} else if (crs.getString("soe_id").equals("8")) {
					dataMap.put("prior-customer", "X");
				}
				// customer type
				if (crs.getString("custtype_id").equals("1")) {
					dataMap.put("business", "X");
				} else if (crs.getString("custtype_id").equals("2")) {
					dataMap.put("federal-govt", "X");
				} else if (crs.getString("custtype_id").equals("3")) {
					dataMap.put("fleet", "X");
				} else if (crs.getString("custtype_id").equals("4")) {
					dataMap.put("local-govt", "X");
				} else if (crs.getString("custtype_id").equals("5")) {
					dataMap.put("not-for-profit", "X");
				} else if (crs.getString("custtype_id").equals("6")) {
					dataMap.put("private", "X");
				} else if (crs.getString("custtype_id").equals("7")) {
					dataMap.put("rental", "X");
				} else if (crs.getString("custtype_id").equals("2")) {
					dataMap.put("state-govt", "X");
				}
				// priority
				if (crs.getString("priorityenquiry_id").equals("1")) {
					dataMap.put("hot", "X");
				} else if (crs.getString("priorityenquiry_id").equals("2")) {
					dataMap.put("warm", "X");
				} else if (crs.getString("priorityenquiry_id").equals("3")) {
					dataMap.put("cold", "X");
				}
				// other comments
				dataMap.put("enquiry_ford_kmsdriven", crs.getString("enquiry_ford_kmsdriven"));
				dataMap.put("enquiry_ford_newvehfor", crs.getString("enquiry_ford_newvehfor"));
				dataMap.put("enquiry_ford_investment", crs.getString("enquiry_ford_investment"));
				dataMap.put("enquiry_ford_colourofchoice", crs.getString("enquiry_ford_colourofchoice"));
				dataMap.put("enquiry_ford_cashorfinance", crs.getString("enquiry_ford_cashorfinance"));
				dataMap.put("enquiry_ford_currentcar", crs.getString("enquiry_ford_currentcar"));
				dataMap.put("enquiry_ford_exchangeoldcar", crs.getString("enquiry_ford_exchangeoldcar"));
				dataMap.put("enquiry_ford_othercarconcideration", crs.getString("enquiry_ford_othercarconcideration"));

				// trade in details
				dataMap.put("enquiry_ford_ex_make", crs.getString("enquiry_ford_ex_make"));
				dataMap.put("enquiry_ford_ex_model", crs.getString("enquiry_ford_ex_model"));
				dataMap.put("enquiry_ford_ex_derivative", crs.getString("enquiry_ford_ex_derivative"));
				dataMap.put("enquiry_ford_ex_year", crs.getString("enquiry_ford_ex_year"));
				dataMap.put("enquiry_ford_ex_odoreading", crs.getString("enquiry_ford_ex_odoreading"));
				dataMap.put("enquiry_ford_ex_doors", crs.getString("enquiry_ford_ex_doors"));
				dataMap.put("enquiry_ford_ex_bodystyle", crs.getString("enquiry_ford_ex_bodystyle"));
				dataMap.put("enquiry_ford_ex_enginesize", crs.getString("enquiry_ford_ex_enginesize"));
				dataMap.put("enquiry_ford_ex_fueltype", crs.getString("enquiry_ford_ex_fueltype"));
				dataMap.put("enquiry_ford_ex_drive", crs.getString("enquiry_ford_ex_drive"));
				dataMap.put("enquiry_ford_ex_transmission", crs.getString("enquiry_ford_ex_transmission"));
				dataMap.put("enquiry_ford_ex_colour", crs.getString("enquiry_ford_ex_colour"));
				dataMap.put("enquiry_ford_ex_priceoffered", crs.getString("enquiry_ford_ex_priceoffered"));
				dataMap.put("enquiry_ford_ex_estmtprice", crs.getString("enquiry_ford_ex_estmtprice"));
				if (!crs.getString("model_name").equals("")) {
					dataMap.put("vehicle_selection", "X");
				}
				if (!crs.getString("testdrive_enquiry_id").equals("0")) {
					dataMap.put("testdrive", "X");
				}
				if (!crs.getString("so_enquiry_id").equals("0")) {
					dataMap.put("so_enquiry_id", "X");
				}
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
