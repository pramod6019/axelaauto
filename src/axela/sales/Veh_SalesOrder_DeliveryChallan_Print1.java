package axela.sales;

//Bhavya 19th Apr 2016

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
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

public class Veh_SalesOrder_DeliveryChallan_Print1 extends Connect {

	public String so_id = "0";
	public String brand_id = "0";
	public String StrSql = "", msg = "", reportfrom = "", format = "pdf";
	public String StrHTML = "";
	public String comp_id = "0", emp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String total_disc = "";
	public String grandtotal = "";
	public String quoteitem_rowcount = "0", quote_exprice = "0";
	public String item_taxcal = "0";
	public int count1 = 0;
	public String quoteitem_option_group = "";
	public String group_name = "";
	public String branch_city_id = "0";
	public String branch_id = "0";
	public double quote_grandtotal = 0.0;
	public double quote_netamt = 0.0;
	public double quoteitem_price = 0.0;
	public double quote_before_ex_disc = 0.0;
	public double quoteitem_totalprice = 0.0;
	public String BranchAccess = "";
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
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("1")) {
					JasperReport report = new JasperReport();
					if (brand_id.equals("6")) {
						reportfrom = "salesorder-deliverychallan-hyundai";
					} else if (brand_id.equals("7")) {
						reportfrom = "salesorder-deliverychallan-ford";
					} else if (brand_id.equals("9")) {
						reportfrom = "salesorder-deliverychallan-honda";
					} else if (brand_id.equals("51")) {
						reportfrom = "salesorder-deliverychallan-volvo";
					} else if (brand_id.equals("151")) {
						reportfrom = "salesorder-deliverychallan-onetriumph";
					} else {
						reportfrom = "salesorder-deliverychallan-maruti";
					}
					report.reportfrom = "/sales/reports/" + reportfrom;
					report.parameters = BuildParameters();
					report.dataList = DeliveryChallanDetails();
					report.doPost(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public Map BuildParameters() {
		Connection conn = connectDB();
		try {
			parameters.put("REPORT_CONNECTION", conn);
		}

		catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}

		return parameters;
	}
	public List<Map> DeliveryChallanDetails() throws IOException {

		HashMap dataMap;
		try {
			StrSql = "SELECT comp_name, comp_logo,"
					+ " so_no, model_name, item_name,"
					+ " COALESCE(DATE_FORMAT(so_date,'%d/%m/%Y'), '') AS so_date,"
					// + " COALESCE(invoice_id, '') as invoice_id,"
					+ " COALESCE(veh_id, 0) as veh_id,"
					+ " COALESCE(fintype_name, '') as fintype_name,"
					// + " COALESCE(invoice_date,'') as invoice_date,"
					+ " COALESCE(vehstock_id, 0) as vehstock_id, COALESCE(vehstock_chassis_no,'') as vehstock_chassis_no,"
					+ " COALESCE(vehstock_engine_no,'') as vehstock_engine_no,"
					+ " COALESCE(so_reg_no, '') as so_reg_no,"
					+ " COALESCE(DATE_FORMAT(so_delivered_date,'%d/%m/%Y'), '') AS so_delivered_date,"
					+ " COALESCE(fincomp_name, '') as fincomp_name,"
					+ " COALESCE(DATE_FORMAT(so_payment_date,'%d/%m/%Y'), '') AS so_payment_date,"
					+ " COALESCE(DATE_FORMAT(so_reg_date,'%d/%m/%Y'), '') AS so_reg_date,"
					+ " COALESCE(option_name, '') as colour,"
					+ " customer_id, customer_name, customer_address,"
					+ " contact_mobile1, contact_mobile2, branch_city_id, branch_id, branch_brand_id,"
					// +
					// " COALESCE(customer_city.city_name,'') AS acc_city, customer_pin, COALESCE(customer_state.state_name,'') AS acc_state,"
					// +
					// " quote_ship_address, quote_ship_city, quote_ship_pin, quote_ship_state, quote_terms,"
					// +
					// " quote_grandtotal, branch_invoice_name, customer_id, customer_name, exe.emp_name as emp_name,"
					+ " emp_name"
					// + " jobtitle_id,\n"
					// +
					// " COALESCE(teamlead.emp_name,'') as tl_emp_name, COALESCE(teamlead.emp_mobile1,'') as tl_emp_mobile1, COALESCE(teamlead.emp_email1,'') as tl_emp_email1\n"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_type ON fintype_id = so_fintype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_so_id = so_id,"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_invoice ON invoice_so_id = so_id,"
					// +
					// " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					// +
					// " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
					// +
					// " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					// +
					// " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = exe.emp_id\n"
					// +
					// " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id\n"
					// +
					// " LEFT JOIN " + compdb(comp_id) + "axela_emp teamlead on teamlead.emp_id = team_emp_id, " + compdb(comp_id) + "axela_comp\n"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE so_id = " + so_id + BranchAccess
					+ ExeAccess + "" + " GROUP BY so_id"
					+ " ORDER BY so_id DESC";
			// SOP("StrSql---------DeliveryChallanDetails--------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				String branch_brand_id = crs.getString("branch_brand_id");
				String city_name = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + crs.getString("branch_city_id") + "");
				String branch_invoice_name = ExecuteQuery("SELECT branch_invoice_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id=" + crs.getString("branch_id") + "");
				String principal_company = ExecuteQuery("SELECT brand_company FROM axela_brand WHERE brand_id=" + branch_brand_id + "");
				dataMap = new HashMap();
				// customer details
				dataMap.put("city_name", city_name);
				dataMap.put("branch_invoice_name", branch_invoice_name);
				dataMap.put("principal_company", principal_company);
				dataMap.put("comp_name", unescapehtml(crs.getString("comp_name")));
				dataMap.put("comp_logo",
						unescapehtml(crs.getString("comp_logo")));
				dataMap.put("so_no",
						unescapehtml(crs.getString("so_no")));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("so_date", unescapehtml(crs.getString("so_date")));
				dataMap.put("veh_id", unescapehtml(crs.getString("veh_id")));
				dataMap.put("fintype_name", unescapehtml(crs.getString("fintype_name")));
				dataMap.put("vehstock_id", unescapehtml(crs.getString("vehstock_id")));
				dataMap.put("vehstock_chassis_no", unescapehtml(crs.getString("vehstock_chassis_no")));
				dataMap.put("vehstock_engine_no", unescapehtml(crs.getString("vehstock_engine_no")));
				dataMap.put("so_reg_no", unescapehtml(crs.getString("so_reg_no")));
				dataMap.put("so_delivered_date", unescapehtml(crs.getString("so_delivered_date")));
				dataMap.put("fincomp_name", unescapehtml(crs.getString("fincomp_name")));
				dataMap.put("so_payment_date", unescapehtml(crs.getString("so_payment_date")));
				dataMap.put("so_reg_date", unescapehtml(crs.getString("so_reg_date")));
				dataMap.put("colour", unescapehtml(crs.getString("colour")));
				dataMap.put("customer_id", unescapehtml(crs.getString("customer_id")));
				dataMap.put("customer_name", unescapehtml(crs.getString("customer_name")));
				dataMap.put("customer_address", unescapehtml(crs.getString("customer_address")));
				dataMap.put("colour", unescapehtml(crs.getString("colour")));
				dataMap.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
				dataMap.put("contact_mobile2", unescapehtml(crs.getString("contact_mobile2")));
				dataMap.put("branch_city_id", unescapehtml(crs.getString("branch_city_id")));
				dataMap.put("branch_id", unescapehtml(crs.getString("branch_id")));
				dataMap.put("branch_brand_id", unescapehtml(crs.getString("branch_brand_id")));
				dataMap.put("emp_name", unescapehtml(crs.getString("emp_name")));

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
