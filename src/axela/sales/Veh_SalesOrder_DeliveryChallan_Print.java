package axela.sales;

//aJIt 11th January, 2013
//SATISH 23-FEB-2013

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Veh_SalesOrder_DeliveryChallan_Print extends Connect {

	public String so_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";
	public String grandtotal = "";
	PdfPTable item_table;
	PdfPTable config_table;
	PdfPTable disc_table;
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 9, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 7);
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

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("1")) {
					DeliveryChallanDetails(request, response, so_id, BranchAccess,
							ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void DeliveryChallanDetails(HttpServletRequest request,
			HttpServletResponse response, String so_id, String BranchAccess,
			String ExeAccess, String purpose) throws IOException,
			DocumentException {
		try {
			StrSql = "SELECT comp_name, comp_logo,"
					+ " so_no, model_name, item_name, so_date,"
					// + " COALESCE(invoice_id, '') as invoice_id,"
					+ " COALESCE(veh_id, 0) as veh_id,"
					+ " COALESCE(fintype_name, '') as fintype_name,"
					// + " COALESCE(invoice_date,'') as invoice_date,"
					+ " COALESCE(vehstock_id, 0) as vehstock_id, COALESCE(vehstock_chassis_no,'') as vehstock_chassis_no,"
					+ " COALESCE(vehstock_engine_no,'') as vehstock_engine_no,"
					+ " COALESCE(so_reg_no, '') as so_reg_no,"
					+ " COALESCE(so_delivered_date, '') as so_delivered_date, COALESCE(fincomp_name, '') as fincomp_name,"
					+ " COALESCE(so_payment_date,'') as so_payment_date, COALESCE(so_reg_date, '') as so_reg_date,"
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

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				document.setMargins(20, 20, 20, 20);
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					PdfWriter.getInstance(document, new FileOutputStream(
							CachePath(comp_id) + "Quote_" + so_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					branch_city_id = crs.getString("branch_city_id");
					branch_id = crs.getString("branch_id");
					String branch_brand_id = crs.getString("branch_brand_id");
					String city_name = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + branch_city_id + "");
					String branch_invoice_name = ExecuteQuery("SELECT branch_invoice_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id=" + branch_id + "");
					String principal_company = ExecuteQuery("SELECT principal_company FROM axela_brand WHERE brand_id=" + branch_brand_id + "");
					PdfPTable top_table = new PdfPTable(1);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);

					PdfPCell cell;
					// if (!crs.getString("comp_logo").equals("")) {
					// Image comp_logo = Image.getInstance(CompLogoPath());
					// cell.addElement(new Chunk(comp_logo, 0, 0));
					// cell.setFixedHeight(comp_logo.getHeight());
					// } else {
					// cell.addElement(new Phrase(""));
					// }
					// cell.setPaddingLeft(0);
					// cell.setBorderWidthLeft(0);
					// cell.setBorderWidthRight(0);
					// cell.setBorderWidthTop(0);
					// cell.setBorderWidthBottom(0.1f);
					// cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					// top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("DELIVERY CHALLAN",
							header_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(70);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.1f);
					top_table.addCell(cell);

					document.add(top_table);

					PdfPTable table = new PdfPTable(5);
					table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setFixedHeight(10);
					cell.setColspan(5);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("so_no"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Model: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("model_name"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Variant: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							unescapehtml(crs.getString("item_name")),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Date: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							strToShortDate(crs.getString("so_date")),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Color: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("colour"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					// cell = new PdfPCell(new Phrase("Invoice ID: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("",
					// normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);

					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);

					cell = new PdfPCell(new Phrase("Vehicle ID: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("vehstock_id"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					// cell = new PdfPCell(new Phrase("",
					// normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// cell.setBorder(0);
					// table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("Chassis No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("vehstock_chassis_no"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Order No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Engine No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("vehstock_engine_no"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Order Date: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Key No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// ///////////////////////////////
					cell = new PdfPCell(
							new Phrase("Customer ID: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("customer_id"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Registration No.: ",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("so_reg_no"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					// ////////////////////////////
					cell = new PdfPCell(new Phrase("Name: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("customer_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Registration Date: ",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("so_reg_date"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// /////////////////////////////
					cell = new PdfPCell(new Phrase("Address: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("customer_address"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Hypothecated to: ",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("fincomp_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// //////////////////////////////////
					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Sales Consultant: ",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("emp_name"),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// ////////////////////////////////////
					cell = new PdfPCell(new Phrase("Phone (O):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("Phone (M): ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile2"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// //////////////////////////////////////////

					cell = new PdfPCell(new Phrase("             (R)",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Invoice: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// //////////////////////////
					cell = new PdfPCell(new Phrase("             (M)",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Delivery: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							strToShortDate(crs.getString("so_delivered_date")),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					// /////////////////////
					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Gate Pass: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);

					item_table = new PdfPTable(3);
					item_table.setWidthPercentage(100);
					item_table.setWidths(new int[]{30, 15, 50});

					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setColspan(3);
					cell.setFixedHeight(20);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Standard Delivery items",
							bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Tools And Accessories",
							bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("1. Duplicate Key",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("1. CMVR Kit", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("2. Key", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							"2. Insurance Cover Note/Policy", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("3. Manual Issued",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("3. PDI Copy", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("4. R.C. Book", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("4. POP Survey Form",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("5. RTO Challan",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("5. Type of Registration",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("6. Sales Invoice",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("6. SSI Feedback card",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("7. Sales Letter",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("7. Taxi/Ambulance/Normal",
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					// ///////////////////////////
					cell = new PdfPCell(new Phrase("8. Tool Kit", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					item_table.addCell(cell);
					document.add(item_table);

					PdfPTable terms_table = new PdfPTable(1);
					terms_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setFixedHeight(20);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Terms and Conditions:",
							bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"1. The sale of the vehicle is governed by the Warranty as quoted in 'Warranty & Service Booklet' supplied with each vehicle.",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"2. No credit for tax paid is available for this Retail Invoice under " + city_name + " VAT Act.",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"3. Any dispute or claims out of the invoice will be subjected to jurisdiction of court in " + city_name + ".",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"4. Force Majeure clause will be applicable to all deliveries.",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"5. No claim will be entertained after the delivery of goods.",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"6. Make DD/Cheque/favouring " + branch_invoice_name + ".",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase(
									"7. The price is governed by the " + principal_company + ". is prevailing Prices and Local Taxes as applicable at the time of delivery.",
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setBorder(0);
					terms_table.addCell(cell);

					document.add(terms_table);

					PdfPTable sign_table = new PdfPTable(2);
					sign_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setFixedHeight(30);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							crs.getString("customer_name"), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("For " + branch_invoice_name, bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							"(Customer Name & Signature)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(60);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("(Authorized Signatory)",
							bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(60);
					cell.setBorder(0);
					sign_table.addCell(cell);
					document.add(sign_table);

				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Quote!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void ItemDetails(String quote_id) throws DocumentException {
		int count = 0;
		int colspan = 0;
		String configured_item = "";
		String main_item_disc = "0";

		if (total_disc.equals("0.00")) {
			item_table = new PdfPTable(3);
			item_table.setWidths(new int[]{2, 50, 6});
		} else {
			item_table = new PdfPTable(3);
			item_table.setWidths(new int[]{2, 50, 6});
		}

		if (total_disc.equals("0.00")) {
			config_table = new PdfPTable(2);
			config_table.setWidths(new int[]{50, 6});

			disc_table = new PdfPTable(2);
			disc_table.setWidths(new int[]{50, 6});
		} else {
			config_table = new PdfPTable(2);
			config_table.setWidths(new int[]{50, 6});

			disc_table = new PdfPTable(2);
			disc_table.setWidths(new int[]{50, 6});
		}

		item_table.setWidthPercentage(100);
		config_table.setWidthPercentage(100);
		disc_table.setWidthPercentage(100);

		PdfPCell cell;
		StrSql = "SELECT item_name, item_code, item_small_desc, item_aftertaxcal, quoteitem_rowcount,"
				+ " quoteitem_qty, uom_shortname, quoteitem_price, quoteitem_disc, quoteitem_total,"
				+ " quoteitem_option_group, quoteitem_option_id, quoteitem_tax, quoteitem_option_group_tax,"
				+ " COALESCE((SELECT GROUP_CONCAT(CONCAT(opt.quoteitem_option_group, ': ', optitem.item_name,"
				+ " IF(optitem.item_code != '', CONCAT(' (', optitem.item_code, ')'), ''),"
				+ " CONCAT(' Qty: ', opt.quoteitem_qty),"
				+ " IF(optitem.item_small_desc != '', CONCAT('\n', optitem.item_small_desc), ''))"
				+ " ORDER BY opt.quoteitem_id ASC SEPARATOR '\n\n')"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_item opt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item optitem ON optitem.item_id = opt.quoteitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_item_id = opt.quoteitem_item_id"
				+ " WHERE opt.quoteitem_quote_id = invitem.quoteitem_quote_id"
				+ " AND opt.quoteitem_option_id = invitem.quoteitem_rowcount), '') AS optionitems"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.quoteitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
				+ " WHERE invitem.quoteitem_quote_id = "
				+ quote_id
				+ ""
				+ " GROUP BY quoteitem_id"
				+ " ORDER BY quoteitem_option_group_tax, quoteitem_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			count++;
			int cnt = 0;
			double value = 0.00;

			cell = new PdfPCell(new Phrase("#", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" ", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Amount", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(count + "", normal_font));
			cell.setNoWrap(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);
			while (crs.next()) {
				cnt++;
				if (cnt == 1) {
					value = crs.getDouble("quoteitem_price");
				}
				quoteitem_rowcount = crs.getString("quoteitem_rowcount");
				item_taxcal = crs.getString("quoteitem_option_group_tax");
				quoteitem_option_group = crs.getString("quoteitem_option_group");
				quoteitem_totalprice = crs.getDouble("quoteitem_total");
				String item_name = crs.getString("item_name");

				if (!quoteitem_rowcount.equals("0")) {
					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ")";
					}
				}
				item_name = item_name.replace("<br>", "\n");

				// MAIN ITEM
				if (!quoteitem_rowcount.equals("0")) {
					if (!crs.getString("quoteitem_disc").equals("0")) {
						main_item_disc = crs.getString("quoteitem_disc");
					}
					if (!item_name.equals("")) {
						cell = new PdfPCell(new Phrase(unescapehtml(item_name),
								normal_font));
					} else {
						cell = new PdfPCell(new Phrase(" ", normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					if (!item_name.equals("")) {
						cell = new PdfPCell(new Phrase("", normal_font));
					} else {
						cell = new PdfPCell(new Phrase(" ", normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				} else {
					// GROUP_NAME
					configured_item = quoteitem_option_group;
					if (item_taxcal.equals("1")) {
						if (!crs.getString("quoteitem_option_id").equals("0")
								&& !group_name.equals(quoteitem_option_group)) {
							group_name = quoteitem_option_group;
							if (group_name.equals("Additional Discounts")) {
								cell = new PdfPCell(new Phrase(
										"Consumer Offer", bold_font));
								cell.setColspan(2);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								disc_table.addCell(cell);
							} else {
								cell = new PdfPCell(new Phrase(
										crs.getString("quoteitem_option_group"),
										bold_font));
								cell.setColspan(2);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								config_table.addCell(cell);
							}
						}

						if (quoteitem_option_group
								.equals("Additional Discounts")
								&& crs.getDouble("quoteitem_disc") > 0) {
							if (Double.parseDouble(CNumeric(main_item_disc)) > 0) {
								quote_before_ex_disc = Double
										.parseDouble(CNumeric(main_item_disc));
								cell = new PdfPCell(new Phrase("Cash Discount",
										normal_font));
								disc_table.addCell(cell);

								cell = new PdfPCell(
										new Phrase(
												IndFormat(String
														.valueOf(df.format(Double
																.parseDouble(CNumeric(main_item_disc))))),
												normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								disc_table.addCell(cell);
								main_item_disc = "";
							}

							// ITEM_NAME
							if (!item_name.equals("")) {
								quote_before_ex_disc += crs
										.getDouble("quoteitem_disc");
								// SOP("quote_before_ex_disc===" +
								// quote_before_ex_disc);
								if (!item_name.equals("")) {
									cell = new PdfPCell(new Phrase(
											unescapehtml(item_name),
											normal_font));
								} else {
									cell = new PdfPCell(new Phrase(" ",
											normal_font));
								}
								disc_table.addCell(cell);

								// SOP("quoteitem_disc===" +
								// crs.getDouble("quoteitem_disc"));
								cell = new PdfPCell(
										new Phrase(
												IndFormat(String
														.valueOf(df.format(crs
																.getDouble("quoteitem_disc")))),
												normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								disc_table.addCell(cell);
							} else {
								if (!item_name.equals("")) {
									cell = new PdfPCell(new Phrase(
											unescapehtml(item_name),
											normal_font));
								} else {
									cell = new PdfPCell(new Phrase(" ",
											normal_font));
								}
								config_table.addCell(cell);

								cell = new PdfPCell(
										new Phrase(" ", normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								config_table.addCell(cell);
							}
						} else {
							// ITEM_NAME
							if (!item_name.equals("")) {
								cell = new PdfPCell(new Phrase(
										unescapehtml(item_name), normal_font));
							} else {
								cell = new PdfPCell(
										new Phrase(" ", normal_font));
							}
							config_table.addCell(cell);

							cell = new PdfPCell(new Phrase(" ", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							config_table.addCell(cell);
						}
					}

					if (total_disc.equals("0.00")) {
						colspan = 2;
					} else {
						colspan = 2;
					}

					if (item_taxcal.equals("2") && count1 == 0) {
						if (Double.parseDouble(CNumeric(main_item_disc)) > 0) {
							quote_before_ex_disc = Double
									.parseDouble(CNumeric(main_item_disc));

							cell = new PdfPCell(new Phrase("Consumer Offer",
									bold_font));
							cell.setColspan(2);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							disc_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Cash Discount",
									normal_font));
							disc_table.addCell(cell);

							cell = new PdfPCell(
									new Phrase(
											IndFormat(String
													.valueOf(df.format(Double
															.parseDouble(CNumeric(main_item_disc))))),
											normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							disc_table.addCell(cell);
							main_item_disc = "";
						}
						cell = new PdfPCell(new Phrase("Ex-Showroom Price: ",
								bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						if (!item_name.equals("")) {
							cell.setColspan(colspan - 1);
						} else {
							cell.setColspan(colspan - 2);
						}
						config_table.addCell(cell);
						cell = new PdfPCell(new Phrase(value + "", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						config_table.addCell(cell);

						cell = new PdfPCell(disc_table);
						cell.setColspan(2);
						config_table.addCell(cell);

						if (quote_before_ex_disc > 0) {
							cell = new PdfPCell(new Phrase(
									"Ex-Showroom Price after Consumer Offer: ",
									bold_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							if (!item_name.equals("")) {
								cell.setColspan(colspan - 1);
							} else {
								cell.setColspan(colspan - 2);
							}
							config_table.addCell(cell);

							// SOP("quote_exprice==item=" + quote_exprice);
							// SOP("new==" + (value - quote_before_ex_disc));
							cell = new PdfPCell(
									new Phrase(IndFormat(String.valueOf(value
											- quote_before_ex_disc)), bold_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							config_table.addCell(cell);
						}
						count++;
					}

					// AFTER TAX
					if (item_taxcal.equals("2")) {
						count1++;
						cell = new PdfPCell(new Phrase(unescapehtml(item_name),
								normal_font));
						cell.setNoWrap(true);
						config_table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								IndFormat(String.valueOf(df.format(crs
										.getDouble("quoteitem_total")))),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						config_table.addCell(cell);
					}
				}
			}

			crs.close();

			if (count1 == 0) {
				cell = new PdfPCell(new Phrase(""));
				item_table.addCell(cell);

				cell = new PdfPCell(
						new Phrase("Ex-Showroom Price: ", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(value + "", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				if (quote_before_ex_disc > 0) {
					cell = new PdfPCell(new Phrase(""));
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							"Ex-Showroom Price after Consumer Offer: ",
							bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(IndFormat(quote_exprice),
							bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				}
			}

			if (!configured_item.equals("")) {
				cell = new PdfPCell(new Phrase(""));
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setPadding(0);
				cell.addElement(config_table);
				cell.setColspan(colspan);
				item_table.addCell(cell);
			}

			if (total_disc.equals("0.00")) {
				colspan = 2;
			} else {
				colspan = 2;
			}

			cell = new PdfPCell(new Phrase(""));
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Grand Total:", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df
					.format(quote_grandtotal))), bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

}
