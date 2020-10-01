//package axela.sales;
////aJIt 8th February, 2013
//// satish 26-feb-2013
//
//import axela.invoice.Invoice_Print_PDF;
//
//import javax.servlet.http.HttpSession;
//
//import cloudify.connect.Connect;
//
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.html.simpleparser.HTMLWorker;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.StringReader;
//
//import javax.servlet.http.HttpServletResponse;
//
//import java.sql.ResultSet;
//import java.text.DecimalFormat;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class Veh_Salesorder_Print_PDF extends Connect {
//
//	public String so_id = "0";
//	public String StrSql = "";
//	public String comp_id = "0";
//	public String StrHTML = "";
//	DecimalFormat df = new DecimalFormat("0.00");
//	public String BranchAccess = "";
//	public String ExeAccess = "";
//	public String total_disc = "";
//	PdfPTable item_table;
//	PdfPTable config_table;
//	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
//	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
//	Font normal_font = FontFactory.getFont("Helvetica", 10);
//	public double so_grandtotal = 0.00;
//	public String so_exprice = "";
//	public int count1;
//	public String soitem_rowcount = "";
//	public String soitem_option_group = "";
//	public String soitem_option_group_tax = "";
//	public double soitem_price = 0.00;
//	private String group_name = "";
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			CheckSession(request, response);
//			HttpSession session = request.getSession(true);
//			comp_id = CNumeric(GetSession("comp_id", request));
//				CheckPerm(comp_id, "emp_sales_order_access", request, response);
//			if (!comp_id.equals("0")) {
//				BranchAccess = GetSession("BranchAccess", request);
//				ExeAccess = GetSession("ExeAccess", request);
//				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
//				if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("1")) {
//					SalesorderDetails(request, response, so_id, BranchAccess, ExeAccess, "pdf");
//				}
//			}
//		} catch (Exception ex) {
//			SOPError("Axelaauto== " + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//		}
//	}
//
//	public void SalesorderDetails(HttpServletRequest request, HttpServletResponse response, String so_id, String BranchAccess, String ExeAccess, String purpose) throws IOException, DocumentException {
//		try {
//			StrSql = "SELECT so_date, so_exprice, so_id, CONCAT('SO', branch_code, so_no) AS so_no,"
//					+ " comp_name,branch_add, branch_pin, branch_city.city_name AS city_name,"
//					+ " branch_state.state_name AS state_name, branch_phone1, branch_mobile1,"
//					+ " branch_email1, branch_logo, comp_name, so_desc, so_terms, so_customer_id,"
//					+ " so_grandtotal, branch_invoice_name, quote_id, so_discamt, emp_name,"
//					+ " CONCAT('QT', branch_code, quote_no) AS quote_no, emp_phone1, jobtitle_desc,"
//					+ " customer_name,customer_address,  customer_pin, emp_mobile1, emp_email1,"
//					+ " customer_city.city_name AS acc_city, customer_state.state_name AS acc_state"
//					+ " FROM " + compdb(comp_id) + "axela_sales_so"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id "
//					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON  branch_city.city_id = branch_city_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_city customer_city ON customer_city.city_id = customer_city_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id,"
//					+ " " + compdb(comp_id) + "axela_comp"
//					+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + ""
//					+ " GROUP BY so_id"
//					+ " ORDER BY so_id DESC";
//			CachedRowSet crs =processQuery(StrSql, 0);
//
//			if (crs.isBeforeFirst()) {
//				Document document = new Document();
//				if (purpose.equals("pdf")) {
//					response.setContentType("application/pdf");
//					PdfWriter.getInstance(document, response.getOutputStream());
//				} else if (purpose.equals("file")) {
//					File f = new File(CachePath(comp_id));
//					if (!f.exists()) {
//						f.mkdirs();
//					}
//					PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Salesorder_" + so_id + ".pdf"));
//				}
//				document.open();
//
//				while (crs.next()) {
//					so_exprice = crs.getString("so_exprice");
//					so_grandtotal = crs.getDouble("so_grandtotal");
//					total_disc = crs.getString("so_discamt");
//
//					PdfPTable top_table = new PdfPTable(2);
//					top_table.setWidthPercentage(100);
//					top_table.setKeepTogether(true);
//					PdfPCell cell;
//
//					cell = new PdfPCell();
//					if (!crs.getString("branch_logo").equals("")) {
//						Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) +
//								unescapehtml(crs.getString("branch_logo")));
//						cell.addElement(new Chunk(branch_logo, 0, 0));
//						cell.setFixedHeight(branch_logo.getHeight());
//					} else {
//						cell.addElement(new Phrase(""));
//					}
//					cell.setBorderWidth(0);
//					cell.setPaddingLeft(0);
//					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//					top_table.addCell(cell);
//
//					cell = new PdfPCell(new Phrase("PROFORMA INVOICE", header_font));
//					cell.setBorderWidth(0);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//					top_table.addCell(cell);
//
//					String contact = "", comp_name = "";
//					comp_name += "M/s. " + crs.getString("branch_invoice_name");
//					contact += crs.getString("branch_add") + ",";
//					contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
//					contact += crs.getString("state_name") + ".";
//					if (!crs.getString("branch_phone1").equals("")) {
//						contact += "\n" + crs.getString("branch_phone1");
//					}
//
//					if (!crs.getString("branch_mobile1").equals("")) {
//						contact += "\n" + crs.getString("branch_mobile1");
//					}
//
//					cell = new PdfPCell();
//					cell.addElement(new Phrase(unescapehtml(comp_name), bold_font));
//					cell.addElement(new Phrase(unescapehtml(contact), normal_font));
//					cell.setVerticalAlignment(Element.ALIGN_TOP);
//					cell.setRowspan(3);
//					top_table.addCell(cell);
//
//					cell = new PdfPCell(new Phrase("Date: " + strToShortDate(crs.getString("so_date")), normal_font));
//					cell.setVerticalAlignment(Element.ALIGN_TOP);
//					cell.setFixedHeight(35);
//					top_table.addCell(cell);
//
//					cell = new PdfPCell();
//					cell.addElement(new Phrase("Sales Order ID:" + crs.getString("so_id"), normal_font));
//					cell.setVerticalAlignment(Element.ALIGN_TOP);
//					cell.addElement(new Chunk("Sales Order No.: " + crs.getString("so_no"), normal_font));
//					cell.setFixedHeight(40);
//					top_table.addCell(cell);
//
//					cell = new PdfPCell();
//					cell.addElement(new Phrase("Quote ID: " + crs.getString("quote_id"), normal_font));
//					cell.setVerticalAlignment(Element.ALIGN_TOP);
//					cell.addElement(new Chunk("Quote No.: " + crs.getString("quote_no"), normal_font));
//					cell.setFixedHeight(40);
//					top_table.addCell(cell);
//
//					cell = new PdfPCell();
//					cell.addElement(new Phrase(unescapehtml(crs.getString("customer_name")), normal_font));
//					cell.addElement(new Phrase(unescapehtml(crs.getString("customer_address")
//							+ ", " + crs.getString("acc_city")
//							+ "-" + crs.getString("customer_pin")
//							+ ", " + crs.getString("acc_state")
//							+ "."), normal_font));
//					top_table.addCell(cell);
//
//					cell = new PdfPCell();
//					cell.addElement(new Phrase("Customer ID: " + crs.getString("so_customer_id"), normal_font));
//					cell.setRowspan(2);
//					top_table.addCell(cell);
//
//					document.add(top_table);
//
//					ItemDetails(so_id);
//					document.add(item_table);
//
//					PdfPTable total_table = new PdfPTable(2);
//					total_table.setWidthPercentage(100);
//
//					cell = new PdfPCell(new Phrase(unescapehtml("Amount Chargeable (in words):\n"
//							+ "Rupees " + toTitleCase(NumberToWordFormat(crs.getInt("so_grandtotal")))
//							+ "Only/-.\n" + " "), normal_font));
//					cell.setColspan(2);
//					total_table.addCell(cell);
//
//					if (crs.getString("so_desc").length() > 5) {
//						StringReader reader = new StringReader(unescapehtml("<font size=2>" + crs.getString("so_desc") + "</font>"));
//						List arr = (List) HTMLWorker.parseToList(reader, null, null);
//						Phrase phrase = new Phrase("Description: ");
//						for (int i = 0; i < arr.size(); i++) {
//							Element element = (Element) arr.get(i);
//							phrase.add(element);
//						}
//						cell = new PdfPCell(phrase);
//						cell.setColspan(2);
//						total_table.addCell(cell);
//					}
//
//					if (crs.getString("so_terms").length() > 5) {
//						StringReader reader = new StringReader(unescapehtml("<font size=2>" + crs.getString("so_terms") + "</font>"));
//						List arr = (List) HTMLWorker.parseToList(reader, null, null);
//						Phrase phrase = new Phrase("Terms: ");
//						for (int i = 0; i < arr.size(); i++) {
//							Element element = (Element) arr.get(i);
//							phrase.add(element);
//						}
//						cell = new PdfPCell(phrase);
//						cell.setColspan(2);
//						total_table.addCell(cell);
//					}
//
//					cell = new PdfPCell(new Phrase(""));
//					total_table.addCell(cell);
//
//					String comp = "";
//					comp = "For M/s. " + crs.getString("branch_invoice_name");
//					comp += "\n" + crs.getString("emp_name");
//					comp += "\n";
//					comp += "\n";
//					comp += "\n";
//					comp += "\n" + crs.getString("jobtitle_desc");
//					if (!crs.getString("emp_phone1").equals("")) {
//						comp += "\n" + crs.getString("emp_phone1");
//					}
//
//					if (!crs.getString("emp_mobile1").equals("")) {
//						comp += "\n" + crs.getString("emp_mobile1");
//					}
//					cell = new PdfPCell(new Phrase(unescapehtml(comp), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					total_table.addCell(cell);
//					document.add(total_table);
//				}
//				document.close();
//			} else {
//				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order!"));
//			}
//			crs.close();
//		} catch (Exception ex) {
//			SOPError("Axelaauto== " + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//		}
//	}
//
//	public void ItemDetails(String so_id) throws DocumentException {
//		int count = 0;
//		int colspan = 0;
//		String configured_item = "";
//		try {
//			if (total_disc.equals("0.00")) {
//				item_table = new PdfPTable(6);
//				item_table.setWidths(new int[]{2, 14, 5, 5, 5, 6});
//			} else {
//				item_table = new PdfPTable(7);
//				item_table.setWidths(new int[]{2, 24, 5, 5, 5, 5, 6});
//			}
//			item_table.setWidthPercentage(100);
//
//			if (total_disc.equals("0.00")) {
//				config_table = new PdfPTable(5);
//				config_table.setWidths(new int[]{14, 5, 5, 5, 6});
//			} else {
//				config_table = new PdfPTable(6);
//				config_table.setWidths(new int[]{24, 5, 5, 5, 5, 6});
//			}
//			config_table.setWidthPercentage(100);
//
//			PdfPCell cell;
//			StrSql = "SELECT item_name, item_code, item_small_desc, soitem_rowcount, soitem_option_group, soitem_option_id,"
//					+ " soitem_total, soitem_option_group_tax, soitem_qty, uom_shortname, soitem_price,"
//					+ " soitem_disc, soitem_tax, COALESCE((SELECT GROUP_CONCAT(CONCAT(opt.soitem_option_group, ': ', optitem.item_name,"
//					+ " IF(optitem.item_code != '', CONCAT(' (', optitem.item_code, ')'), ''),"
//					+ " CONCAT(' Qty: ', opt.soitem_qty), IF(optitem.item_small_desc != '', CONCAT('\n', optitem.item_small_desc), ''))"
//					+ " ORDER BY opt.soitem_id ASC SEPARATOR '\n\n')"
//					+ " FROM " + compdb(comp_id) + "axela_sales_so_item opt"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item optitem ON optitem.item_id = opt.soitem_item_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_item_id = opt.soitem_item_id"
//					+ " WHERE opt.soitem_so_id = invitem.soitem_so_id"
//					+ " AND opt.soitem_option_id = invitem.soitem_rowcount), '') AS optionitems"
//					+ " FROM " + compdb(comp_id) + "axela_sales_so_item invitem"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.soitem_item_id"
//					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
//					+ " WHERE invitem.soitem_so_id = " + so_id + ""
//					+ " GROUP BY soitem_id"
//					+ " ORDER BY soitem_option_group_tax, soitem_id";
//			CachedRowSet crs =processQuery(StrSql, 0);
//
//			cell = new PdfPCell(new Phrase("#", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Item", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Qty", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Price", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			if (!total_disc.equals("0.00")) {
//				cell = new PdfPCell(new Phrase("Discount", bold_font));
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				item_table.addCell(cell);
//			}
//
//			cell = new PdfPCell(new Phrase("Tax", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase("Amount", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			count++;
//			cell = new PdfPCell(new Phrase(count + "", normal_font));
//			cell.setNoWrap(true);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			item_table.addCell(cell);
//
//			while (crs.next()) {
//				soitem_rowcount = crs.getString("soitem_rowcount");
//				soitem_option_group_tax = crs.getString("soitem_option_group_tax");
//				soitem_option_group = crs.getString("soitem_option_group");
//				soitem_price = crs.getDouble("soitem_price");
//
//				String item_name = crs.getString("item_name");
//				if (!crs.getString("item_code").equals("")) {
//					item_name += " (" + crs.getString("item_code") + ")";
//				}
//				if (!crs.getString("item_small_desc").equals("")) {
//					item_name += "\n" + crs.getString("item_small_desc");
//				}
//				item_name += "\n\n";
//
//				if (!soitem_rowcount.equals("0")) {
//					cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					item_table.addCell(cell);
//
//					cell = new PdfPCell(new Phrase(new Invoice_Print_PDF().formatqty(crs.getString("soitem_qty")) + " " + crs.getString("uom_shortname"), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					cell.setNoWrap(true);
//					item_table.addCell(cell);
//
//					cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_price")))), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setNoWrap(true);
//					item_table.addCell(cell);
//
//					if (!total_disc.equals("0.00")) {
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_disc")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						item_table.addCell(cell);
//					}
//
//					cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_tax")))), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setNoWrap(true);
//					item_table.addCell(cell);
//
//					cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_total")))), normal_font));
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setNoWrap(true);
//					item_table.addCell(cell);
//				} else {
//					// GROUP_NAME
//					configured_item = soitem_option_group;
//					if (soitem_option_group_tax.equals("1")) {
//						if (!crs.getString("soitem_option_id").equals("0") && !group_name.equals(soitem_option_group)) {
//							group_name = soitem_option_group;
//							cell = new PdfPCell(new Phrase(crs.getString("soitem_option_group"), bold_font));
//							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							if (!total_disc.equals("0.00")) {
//								cell = new PdfPCell(new Phrase(""));
//								config_table.addCell(cell);
//							}
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//						}
//						// ITEM_NAME
//						cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
//						// cell.setColspan(2);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(new Invoice_Print_PDF().formatqty(crs.getString("soitem_qty")) + " " + crs.getString("uom_shortname"), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_price")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						if (!total_disc.equals("0.00")) {
//							cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_disc")))), normal_font));
//							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//							cell.setNoWrap(true);
//							config_table.addCell(cell);
//						}
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_tax")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_total")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//					}
//
//					if (total_disc.equals("0.00")) {
//						colspan = 5;
//					} else {
//						colspan = 6;
//					}
//
//					if (!soitem_option_group_tax.equals("1") && count == 1) {
//						count1 = count;
//						cell = new PdfPCell(new Phrase("Ex-Showroom Price: ", bold_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						cell.setColspan(colspan - 1);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(IndFormat(so_exprice), bold_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//						count++;
//					}
//					// AFTER TAX
//					if (!soitem_option_group_tax.equals("1")) {
//						// group_name
//						if (!crs.getString("soitem_option_id").equals("0") && !group_name.equals(soitem_option_group)) {
//							group_name = soitem_option_group;
//							cell = new PdfPCell(new Phrase(crs.getString("soitem_option_group"), bold_font));
//							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							if (!total_disc.equals("0.00")) {
//								cell = new PdfPCell(new Phrase(""));
//								config_table.addCell(cell);
//							}
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//
//							cell = new PdfPCell(new Phrase(""));
//							config_table.addCell(cell);
//						}
//						// ITEM_NAME
//						cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(new Invoice_Print_PDF().formatqty(crs.getString("soitem_qty")) + " " + crs.getString("uom_shortname"), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_price")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						if (!total_disc.equals("0.00")) {
//							cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_disc")))), normal_font));
//							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//							cell.setNoWrap(true);
//							config_table.addCell(cell);
//						}
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_tax")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//
//						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("soitem_total")))), normal_font));
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//						cell.setNoWrap(true);
//						config_table.addCell(cell);
//					}
//				}
//			}
//
//			if (!configured_item.equals("")) {
//				cell = new PdfPCell(new Phrase(""));
//				item_table.addCell(cell);
//
//				cell = new PdfPCell(new Phrase(""));
//				cell.setPadding(0);
//				cell.addElement(config_table);
//				cell.setColspan(colspan);
//				item_table.addCell(cell);
//			}
//
//			if (total_disc.equals("0.00")) {
//				colspan = 5;
//			} else {
//				colspan = 6;
//			}
//			cell = new PdfPCell(new Phrase("Grand Total:", bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			cell.setNoWrap(true);
//			cell.setColspan(colspan);
//			item_table.addCell(cell);
//
//			cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(so_grandtotal))), bold_font));
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			cell.setNoWrap(true);
//			item_table.addCell(cell);
//
//			if (!total_disc.equals("0.00")) {
//				cell = new PdfPCell(new Phrase("Total Savings:", normal_font));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				cell.setColspan(colspan);
//				item_table.addCell(cell);
//
//				cell = new PdfPCell(new Phrase(IndFormat(total_disc), normal_font));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				cell.setNoWrap(true);
//				item_table.addCell(cell);
//			}
//			crs.close();
//
//			cell = new PdfPCell(new Phrase(count1 + "", normal_font));
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			cell.setNoWrap(true);
//			item_table.addCell(cell);
//		} catch (Exception ex) {
//			SOPError("Axelaauto== " + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//		}
//	}
// }
