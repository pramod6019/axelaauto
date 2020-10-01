package axela.service;
//satish 13-March-2013

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class JobCard_Print_Pdf extends Connect {

	public String jc_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String total_disc = "";
	public String jc_netamt = "";
	public String comp_id = "0";
	public String jc_fuel_guage = "";
	public String item_name = "";
	public String type_name = "";
	public double jc_grandtotal = 0.0;
	public double total = 0.0;
	PdfPTable item_table;
	DecimalFormat df = new DecimalFormat("0.00");
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	PdfWriter writer;
	Image img;
	public int fguage = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				if (ReturnPerm(comp_id, "emp_service_jobcard_access", request).equals("1")) {
					JobCardDetails(request, response, jc_id, BranchAccess, ExeAccess, "pdf", "estimate");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void JobCardDetails(HttpServletRequest request, HttpServletResponse response, String jc_id, String BranchAccess, String ExeAccess, String purpose, String stage) throws IOException,
			DocumentException {
		try {

			StrSql = "SELECT jc_time_in, jc_id, CONCAT('JC', branch_code, jc_no) AS jc_no, jc_discamt,"
					+ " jc_netamt, comp_name, branch_add, branch_pin, branch_phone1, branch_invoice_name,"
					+ " branch_mobile1, branch_email1, branch_logo, comp_name, jc_notes, jc_bill_address,"
					+ " jc_bill_city, jc_bill_pin, jc_bill_state, jc_del_address, jc_del_city, jc_del_pin,"
					+ " jc_del_state, jc_terms, jc_grandtotal, jc_cust_voice, jc_customer_id, emp_name,"
					+ " jc_fuel_guage, jc_veh_id, veh_reg_no, veh_chassis_no, veh_kms, customer_name,"
					+ " customer_address, customer_pin,"
					+ " COALESCE(branch_city.city_name, '') AS city_name, jobtitle_desc,"
					+ " COALESCE(branch_state.state_name, '')  AS state_name,"
					+ " COALESCE(customer_city.city_name, '') AS acc_city,"
					+ " COALESCE(customer_state.state_name, '') AS acc_state, emp_phone1, emp_mobile1, emp_email1"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON  branch_city.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city customer_city ON customer_city.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE jc_id = " + jc_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY jc_id"
					+ " ORDER BY jc_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "JobCard_" + jc_id + ".pdf"));
				}

				document.open();
				PdfContentByte cb = writer.getDirectContent();
				Barcode128 code128 = new Barcode128();
				code128.setCodeType(Barcode128.CODE128);
				code128.setCode(jc_id);
				img = code128.createImageWithBarcode(cb, null, null);

				while (crs.next()) {
					jc_netamt = crs.getString("jc_netamt");
					jc_fuel_guage = crs.getString("jc_fuel_guage");
					fguage = Integer.parseInt(jc_fuel_guage);
					jc_grandtotal = crs.getDouble("jc_grandtotal");
					total_disc = crs.getString("jc_discamt");
					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					PdfPCell cell;

					cell = new PdfPCell();
					if (!crs.getString("branch_logo").equals("")) {
						Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
						cell.addElement(new Chunk(branch_logo, 0, 0));
						cell.setFixedHeight(branch_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}

					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					if (stage.equals("estimate")) {
						cell = new PdfPCell(new Phrase("Job Card Estimate", header_font));
					} else {
						cell = new PdfPCell(new Phrase("Job Card", header_font));
					}
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					String contact = "", comp_name = "";
					comp_name += "M/s. " + crs.getString("branch_invoice_name");
					contact += crs.getString("branch_add") + ",";
					contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
					contact += crs.getString("state_name") + ".";
					if (!crs.getString("branch_phone1").equals("")) {
						contact += "\n" + crs.getString("branch_phone1");
					}

					if (!crs.getString("branch_mobile1").equals("")) {
						contact += "\n" + crs.getString("branch_mobile1");
					}

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(comp_name), bold_font));
					cell.addElement(new Phrase(unescapehtml(contact), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setRowspan(3);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Date:  " + strToShortDate(crs.getString("jc_time_in")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setFixedHeight(20);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Chunk(img, 0, -20));
					cell.addElement(new Phrase(unescapehtml("\nJob Card ID:  " + crs.getString("jc_id")), normal_font));
					cell.addElement(new Chunk(unescapehtml("Reg. No.:  " + crs.getString("veh_reg_no")), normal_font));
					cell.addElement(new Phrase(unescapehtml("Chassis Number:  " + crs.getString("veh_chassis_no")), normal_font));
					cell.addElement(new Chunk(unescapehtml("Vehicle ID:  " + crs.getString("jc_veh_id")), normal_font));
					cell.addElement(new Phrase(unescapehtml("Kms:  " + crs.getString("veh_kms")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					top_table.addCell(cell);

					PdfPTable fuelGuage_table = new PdfPTable(2);
					fuelGuage_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase(unescapehtml("Fuel Gauge: " + crs.getString("jc_fuel_guage") + "%"), normal_font));
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					fuelGuage_table.addCell(cell);

					cell = new PdfPCell(new Phrase(" "));
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);

					String fpath = JobCardFuelGuagePath(comp_id) + "jcfg_" + jc_id + ".jpg";
					File f1 = new File(fpath);
					if (f1.exists()) {
						Image fuelguage = Image.getInstance(fpath);
						fuelguage.setAlignment(Image.TEXTWRAP);
						fuelguage.setAlignment(Image.ALIGN_RIGHT);
						cell.addElement(new Chunk(fuelguage, 0, -4));
						cell.setFixedHeight(27);
					}

					cell.setVerticalAlignment(Element.ALIGN_TOP);
					fuelGuage_table.addCell(cell);
					top_table.addCell(fuelGuage_table);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(crs.getString("customer_name")), normal_font));
					cell.addElement(new Phrase(unescapehtml(crs.getString("customer_address")
							+ ", " + crs.getString("acc_city")
							+ "-" + crs.getString("customer_pin")
							+ ", " + crs.getString("acc_state")
							+ "."), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml("Customer ID:  " + crs.getString("jc_customer_id")), normal_font));
					cell.setRowspan(2);
					top_table.addCell(cell);
					document.add(top_table);

					ItemDetails(jc_id);
					document.add(item_table);

					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase(unescapehtml("Amount Chargeable (in words):\n"
							+ "Rupees " + toTitleCase(NumberToWordFormat((int) crs.getDouble("jc_grandtotal")))
							+ "Only/-.\n" + " "), normal_font));
					cell.setColspan(2);
					total_table.addCell(cell);

					if (crs.getString("jc_terms").length() > 5) {
						cell = new PdfPCell(new Phrase(unescapehtml("Terms:  \n" + crs.getString("jc_terms")), normal_font));
						cell.setColspan(2);
						total_table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase(""));
					total_table.addCell(cell);
					String comp = "";
					comp = "For M/s. " + crs.getString("branch_invoice_name");
					comp += "\n";
					comp += "\n";
					comp += "\n";
					comp += "\n" + crs.getString("emp_name");
					comp += "\n" + crs.getString("jobtitle_desc");

					if (!crs.getString("emp_phone1").equals("")) {
						comp += "\n" + crs.getString("emp_phone1");
					}

					if (!crs.getString("emp_mobile1").equals("")) {
						comp += "\n" + crs.getString("emp_mobile1");
					}

					if (!crs.getString("emp_email1").equals("")) {
						comp += "\n" + crs.getString("emp_email1");
					}

					cell = new PdfPCell(new Phrase(unescapehtml(comp), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					total_table.addCell(cell);
					document.add(total_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Quote!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void ItemDetails(String jc_id) throws DocumentException, SQLException {

		if (total_disc.equals("0.00")) {
			item_table = new PdfPTable(7);
			item_table.setWidths(new int[]{2, 14, 5, 5, 5, 5, 6});
		} else {
			item_table = new PdfPTable(8);
			item_table.setWidths(new int[]{2, 19, 5, 5, 5, 5, 5, 6});
		}

		item_table.setWidthPercentage(100);
		try {
			int count = 0;
			int colspan = 0;
			PdfPCell cell;
			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_small_desc, item_type_id, type_name, jcitem_total, jcitem_rowcount,"
					+ " jcitem_qty, uom_shortname, jcitem_price, type_id, COALESCE(customer_name, '') AS tax_name,"
					+ " jcitem_disc, jcitem_tax, jcitem_option_id, jcitem_option_group, jcitem_tax_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_item invitem"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.jcitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jcitem_tax_id AND customer_tax = 1"
					+ " WHERE invitem.jcitem_jc_id = " + jc_id + ""
					+ " GROUP BY jcitem_id"
					+ " ORDER BY item_type_id, jcitem_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if (!type_name.equals(crs.getString("type_name")) && !type_name.equals("")) {
					cell = new PdfPCell(new Phrase("Total:  ", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setColspan(colspan);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(Math.ceil(total)))), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
					total = 0.0;
				}

				if (!type_name.equals(crs.getString("type_name"))) {
					count = 0;
					type_name = crs.getString("type_name");
					cell = new PdfPCell(new Phrase("#", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(type_name, bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Qty", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Price", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);

					if (!total_disc.equals("0.00")) {
						cell = new PdfPCell(new Phrase("Discount", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						item_table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("Tax", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Amount", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);
					type_name = crs.getString("type_name");
				}

				count++;
				item_name = crs.getString("item_name");
				if (!crs.getString("item_small_desc").equals("")) {
					item_name += "\n" + crs.getString("item_small_desc");
				}

				if (!crs.getString("tax_name").equals("")) {
					item_name += "\n" + crs.getString("tax_name");
				}
				item_name += "\n\n";

				cell = new PdfPCell(new Phrase(count + "", normal_font));
				cell.setNoWrap(true);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
				cell.setColspan(2);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(formatqty(crs.getString("jcitem_qty")) + " " + crs.getString("uom_shortname"), normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("jcitem_price")))), normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				if (!total_disc.equals("0.00")) {
					cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("jcitem_disc")))), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				}

				cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("jcitem_tax")))), normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("jcitem_total")))), normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				total += crs.getDouble("jcitem_total");
				if (total_disc.equals("0.00")) {
					colspan = 6;
				} else {
					colspan = 7;
				}
			}

			cell = new PdfPCell(new Phrase("Total:  ", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			cell.setColspan(colspan);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(Math.ceil(total)))), bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Grand Total:  ", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			cell.setColspan(colspan);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(Math.ceil(jc_grandtotal)))), bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

			if (!total_disc.equals("0.00")) {
				cell = new PdfPCell(new Phrase("Total Savings:  ", normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setColspan(colspan);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(IndFormat(total_disc), normal_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);
			}
			crs.close();

			// to dispaly the tax components
			TaxBreakUp(item_table, cell, colspan);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void TaxBreakUp(PdfPTable table, PdfPCell cell, int colspan) {
		try {
			StrSql = "SELECT SUM(jcitem_tax) AS tax, COALESCE(customer_name, '') AS tax_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_item invitem"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.jcitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON tax_id = jcitem_tax_id AND customer_tax = 1"
					+ " WHERE invitem.jcitem_jc_id = " + jc_id + ""
					+ " GROUP BY jcitem_tax_id"
					+ " ORDER BY jcitem_tax_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if (!crs.getString("tax_name").equals("") && (int) crs.getDouble("tax") != 0) {
					cell = new PdfPCell(new Phrase(crs.getString("tax_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setColspan(colspan);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(IndFormat(crs.getString("tax")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					table.addCell(cell);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String formatqty(String n) {
		String nn = "";
		if (!n.equals("")) {
			nn = n.replace(".00", "");
		}
		return nn;
	}
}
