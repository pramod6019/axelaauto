package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
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

public class Delivery_Receipt_Print extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String tax_check = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	public String emp_id = "0";
	public String enquiry_id = "0", total_amt = "", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "";
	public String msg = "";
	public String msg1 = "";
	public long voucher_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucher_authorize = CNumeric(ExecuteQuery("SELECT voucher_authorize"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE 1=1"
						+ " AND voucher_id =" + voucher_id));

				if (voucher_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}

				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				reportfrom = "delivery-receipt";
				report.reportfrom = "/accounting/reports/" + reportfrom;
				SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = SalesOrderDetails();
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
	public List<Map> SalesOrderDetails() throws IOException {

		HashMap dataMap;
		String custPh = "";
		String cusMblel = "", emailID = "";
		try {
			StrSql = "SELECT"
					// + " voucher_date, "
					+ " DATE_FORMAT(voucher_date,'%d-%b-%Y') as voucher_date,"
					+ " COALESCE(veh_chassis_no,'') AS veh_chassis_no,"
					+ " COALESCE(veh_engine_no,'') AS veh_engine_no,"
					+ " COALESCE(veh_reg_no,'') AS veh_reg_no,"
					+ " COALESCE(model_name, '')AS  model_name,"
					+ " COALESCE(item_name, '') AS  variant_name,"
					+ " COALESCE (option_name,'') AS option_name,"
					+ " customer_id,"
					+ " customer_name,"
					+ " customer_address,"
					+ " customer_code,"
					+ " customer_landmark,"
					+ " customer_pin,"
					+ " COALESCE(city_name, '') AS cust_city,"
					+ " COALESCE(state_name, '' ) AS cust_state,"
					+ " voucher_amount, voucher_id, voucher_no, voucher_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_so_id = so_id"
					// + " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
					// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_so_id = so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " WHERE"
					+ " voucher_id = " + voucher_id
					// + " AND vouchertype_id = " + vouchertype_id
					+ " GROUP BY"
					+ " voucher_id"
					+ " ORDER BY"
					+ " voucher_id DESC";
			// SOP("StrSql=====Delivery_Receipt_Print====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap = new HashMap();
				String customer_address = "";
				customer_address = crs.getString("customer_address");
				if (!customer_address.equals("")) {
					customer_address = crs.getString("customer_address");
					if (!crs.getString("cust_city").equals("")) {
						customer_address = customer_address + ", " + crs.getString("cust_city");
					}
					customer_address = customer_address + " - " + crs.getString("customer_pin");
					if (!crs.getString("cust_state").equals("")) {
						customer_address = customer_address + ", " + crs.getString("cust_state") + ".";
					}
					if (!crs.getString("customer_landmark").equals("")) {
						customer_address = customer_address + "<br>Landmark: " + crs.getString("customer_landmark");
					}
				}
				dataMap.put("customer_address", customer_address);
				dataMap.put("customer_id", crs.getString("customer_id"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("variant_name", crs.getString("variant_name"));
				dataMap.put("option_name", crs.getString("option_name"));
				dataMap.put("voucher_no", crs.getString("voucher_no"));
				dataMap.put("voucher_date", crs.getString("voucher_date"));
				dataMap.put("comp_ID", Integer.parseInt(comp_id));
				dataMap.put("veh_chassis_no", crs.getString("veh_chassis_no"));
				dataMap.put("veh_engine_no", crs.getString("veh_engine_no"));
				dataMap.put("veh_reg_no", crs.getString("veh_reg_no"));
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
