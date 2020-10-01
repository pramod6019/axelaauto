package axela.sales;
//$at!$# 19th October, 2013
//aJIt 22nd October, 2013

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.pdf.PdfWriter;

public class Print_Booking_Form_Harley extends Connect {

	public String so_id = "0";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String reportfrom = "";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	PdfWriter writer;
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	HashMap dataMap = new HashMap();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
			ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
			// CheckPerm("emp_sales_order_access", request, response);
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			reportfrom = PadQuotes(request.getParameter("reportfrom"));
			SOP("reportfrom----------------" + reportfrom);
			if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("1")) {
				JasperReport report = new JasperReport();
				report.reportfrom = "sales/reports/" + reportfrom;
				// report.parameters = BuildParameters();
				report.dataList = SalesorderBookingDetails(so_id);
				report.doPost(request, response);

			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// public Map BuildParameters() {
	//
	// try {
	// Connection conn = null;
	// conn = connectDB();
	// parameters.put("REPORT_CONNECTION", conn);
	// conn.close();
	// } catch (Exception ex) {
	// SOPError(ClientName + "===" + this.getClass().getName());
	// System.out.println("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// }
	//
	// return parameters;
	// }
	public List<Map> SalesorderBookingDetails(String so_id) throws IOException
	{

		try {

			String color = "", interior = "";
			StrSql = " SELECT branch_invoice_name, branch_add, COALESCE (branch_city.city_name, '') AS branch_city_name, "
					+ " branch_pin, branch_mobile1, branch_email1, so_id, so_date, so_enquiry_id, so_exprice, so_insur_amount, enquiry_date, "
					+ " customer_name, contact_address, contact_pin, "
					+ " COALESCE (contact_city.city_name, '') AS contact_city_name, "
					+ " COALESCE (contact_state.state_name, '') AS contact_state_name, "
					+ " contact_mobile1, contact_phone1, "
					+ " contact_email1, model_name , COALESCE(option_name,'') as colour,"
					+ " so_delivered_date, COALESCE(vehstocklocation_name,'') AS deliverylocation,"
					+ " so_grandtotal,"
					+ " so_booking_amount"
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					// + " INNER JOIN axela_sales_quote ON quote_id = so_quote_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON branch_city.city_id = branch_city_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_city contact_city ON contact_city.city_id = contact_city_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_state contact_state ON contact_state.state_id = contact_city.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = so_din_del_location"
					+ " WHERE so_id = " + so_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("TrackingDetails====2==========" + StrSql);
			// rs.beforeFirst();
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					dataMap.put("branch_address", crs.getString("branch_invoice_name") + "<br> " + crs.getString("branch_add")
							+ ", " + crs.getString("branch_city_name") + " - " + crs.getInt("branch_pin")
							+ "<br>" + " Email - " + crs.getString("branch_email1") + ", Ph: " + crs.getString("branch_mobile1"));
					// dataMap.put("branch_add", crs.getString("branch_add"));
					// dataMap.put("branch_city_name", crs.getString("branch_city_name"));
					// dataMap.put("branch_pin", crs.getInt("branch_pin"));
					// dataMap.put("branch_mobile1", crs.getString("branch_mobile1"));
					// dataMap.put("branch_email1", crs.getString("branch_email1"));
					dataMap.put("so_id", crs.getInt("so_id"));
					dataMap.put("so_date", strToShortDate(crs.getString("so_date")));
					dataMap.put("so_enquiry_id", crs.getInt("so_enquiry_id"));
					dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
					dataMap.put("customer_name", crs.getString("customer_name"));
					dataMap.put("contact_address", crs.getString("contact_address"));
					dataMap.put("contact_pin", crs.getInt("contact_pin"));
					dataMap.put("contact_city_name", crs.getString("contact_city_name"));
					dataMap.put("contact_state_name", crs.getString("contact_state_name"));
					dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
					dataMap.put("contact_phone1", crs.getString("contact_phone1"));
					dataMap.put("contact_email1", crs.getString("contact_email1"));
					dataMap.put("model_name", crs.getString("model_name"));
					dataMap.put("colour", crs.getString("colour"));
					dataMap.put("so_exprice", IndFormat(crs.getString("so_exprice")));
					dataMap.put("so_insur_amount", IndFormat(crs.getString("so_insur_amount")));
					dataMap.put("so_grandtotal", IndFormat(crs.getString("so_grandtotal")));
					dataMap.put("so_booking_amount", IndFormat(crs.getString("so_booking_amount")));
					dataMap.put("balance_payble", IndFormat(Double.parseDouble(crs.getString("so_grandtotal")) - Double.parseDouble(crs.getString("so_booking_amount")) + ""));
					dataMap.put("deliverey_date", strToShortDate(crs.getString("so_delivered_date")));
					dataMap.put("deliverylocation", crs.getString("deliverylocation"));

				}
			}

			dataList.add(dataMap);
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return dataList;
	}
	// public void createCheckBox(PdfPTable table, Document document, PdfWriter
	// writer, int checked, PdfPCell cell, String name, ArrayList<PdfFormField>
	// list, int left, int bottom, int right, int top)
	// throws DocumentException {
	// boolean chk_box = false;
	// // checked or unchecked
	// if (checked == 1) {
	// chk_box = true;
	// }
	//
	// PdfFormField checkbox = PdfFormField.createCheckBox(writer);
	// // set the name for checkboxes
	// checkbox.setFieldName(name);
	// checkbox.setFieldFlags(PdfFormField.FF_READ_ONLY);
	//
	// // create the check box
	// cell = new PdfPCell(new Phrase(""));
	// cell.setCellEvent(new CellField(writer, checkbox, chk_box, "1", left,
	// bottom, right, top));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// table.addCell(cell);
	//
	// list.add(checkbox);
	// }
}
