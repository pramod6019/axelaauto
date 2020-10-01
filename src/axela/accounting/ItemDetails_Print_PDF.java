package axela.accounting;

//JEET 9th DEC, 2014  

import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ItemDetails_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String voucherclass_id = "0";
	public String vouchertype_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	// public String formatdigit_id = "0";
	// public String config_format_decimal = "0";
	public String BranchAccess = "";

	public String ExeAccess = "";
	double total_taxamt = 0.00;
	double total_bilsundry = 0.00;
	public String total_disc = "";
	public double grand_total = 0.0;
	DecimalFormat df = new DecimalFormat("0.00");
	PdfPTable item_table;
	PdfPTable table_temp;
	Paragraph paragraph = null;
	Font header_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

	}

	public void PaymodeDetails(PdfPTable paymode_table, String voucher_id) {
		PdfPCell cell = new PdfPCell();
		item_table.setWidthPercentage(100);
		double vouchertrans_amount = 0.00;

		String paymode_name = "";
		StrSql = " SELECT paymode_name, vouchertrans_amount"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " LEFT JOIN axela_acc_paymode on paymode_id = vouchertrans_paymode_id"
				+ " WHERE vouchertrans_voucher_id = " + voucher_id
				+ " AND vouchertrans_paymode_id != 0" + " GROUP BY paymode_id "
				+ " ORDER BY paymode_name";

		// SOP("\n StrSql=pay=" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			cell = new PdfPCell(new Phrase("Received By: ", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setNoWrap(true);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthBottom(0);
			paymode_table.addCell(cell);

			// EmptyCell
			cell = new PdfPCell(new Phrase("", normal_font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setNoWrap(true);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthBottom(0);
			paymode_table.addCell(cell);
			// End Empty cell

			while (crs.next()) {
				vouchertrans_amount += crs.getDouble("vouchertrans_amount");
				paymode_name = crs.getString("paymode_name");
				cell = new PdfPCell(new Phrase(paymode_name, normal_font));
				cell.setNoWrap(true);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				paymode_table.addCell(cell);

				paymode_name = crs.getString("vouchertrans_amount");
				cell = new PdfPCell(new Phrase(paymode_name, normal_font));
				cell.setNoWrap(true);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				paymode_table.addCell(cell);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void TaxBreakUp(PdfPTable item_table, int colspan, String voucher_id) {
		double taxamt = 0.00;
		try {
			StrSql = "SELECT CONCAT(customer_name) AS tax_name,"
					+ " COALESCE(sum(vouchertrans_amount), 0) as taxamount "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = vouchertrans_tax_id "
					+ " WHERE vouchertrans_voucher_id = " + voucher_id
					+ " AND vouchertrans_tax = 1 "
					+ " AND vouchertrans_item_id != 0"
					+ " GROUP BY customer_id "
					+ " ORDER BY customer_name";
			// SOP("StrSql=TaxDetails===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				PdfPCell cell;
				while (crs.next()) {

					if (crs.getDouble("taxamount") > 0) {
						taxamt += crs.getDouble("taxamount");
						// SOP("taxamt----" + taxamt);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("tax_name")), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setColspan(colspan);
						item_table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(IndDecimalFormat(df.format((crs.getDouble("taxamount"))))), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						item_table.addCell(cell);
					}
				}
			}

			total_taxamt = taxamt;
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	public PdfPCell BillSundryForBill(String comp_id, PdfPCell cell, String voucher_id) {
		double taxamt = 0.00;
		try {
			StrSql = "SELECT CONCAT(customer_name) AS tax_name , COALESCE(sum(vouchertrans_amount), 0) as taxamount "
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_trans "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_custyomer on customer_id = vouchertrans_tax_id "
					+ " WHERE vouchertrans_voucher_id = "
					+ voucher_id
					+ " AND vouchertrans_tax_id !=0  "
					+ " AND vouchertrans_item_id = 0"
					+ " GROUP BY customer_id "
					+ " ORDER BY customer_name";
			// SOP("StrSql=TaxDetails===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			paragraph = new Paragraph();
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getDouble("taxamount") != 0.0) {
						taxamt += crs.getDouble("taxamount");
						paragraph.add(new Phrase(unescapehtml(crs.getString("tax_name")), normal_font));
						paragraph.add(new Phrase(unescapehtml(crs.getString("taxamount")) + "\n", normal_font));
					}
				}
			}
			cell = new PdfPCell(new Phrase(paragraph));
			total_bilsundry = taxamt;
			crs.close();
			return cell;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return cell = new PdfPCell();
	}
	public void BillSundry(PdfPTable item_table, int colspan, String voucher_id) {
		double taxamt = 0.00;
		try {
			StrSql = "SELECT CONCAT(customer_name) AS tax_name,"
					+ " COALESCE(sum(vouchertrans_amount), 0) as taxamount "
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_trans "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = vouchertrans_tax_id "
					+ " WHERE vouchertrans_voucher_id = " + voucher_id
					+ " AND vouchertrans_tax_id !=0  "
					+ " AND vouchertrans_item_id = 0"
					+ " GROUP BY customer_id "
					+ " ORDER BY customer_name";
			// SOP("StrSql=TaxDetails===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				PdfPCell cell;
				while (crs.next()) {
					if (crs.getDouble("taxamount") != 0.0) {
						taxamt += crs.getDouble("taxamount");
						// SOP("BillSundrey  taxamt---"+taxamt);

						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("tax_name")),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setColspan(colspan);
						item_table.addCell(cell);

						cell = new PdfPCell(
								new Phrase(unescapehtml(IndDecimalFormat(df
										.format((crs.getDouble("taxamount"))))), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						item_table.addCell(cell);
					}
				}
			}

			total_bilsundry = taxamt;
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public PdfPTable ItemDetails(String voucher_id, String vouchertype_id, HttpServletRequest request)
			throws DocumentException, SQLException {
		double config_total = 0.0, total = 0.00;
		double discamt = 0.00, totaldisc = 0.00;
		double qty = 0;
		String item_name = "";
		String total_tax = "0.0";
		String serial_no = "";
		double totaltruckspace = 0.0;

		int cnt = 0;
		int count = 0;
		int colspan = 0;
		int sundrycolspan = 0;
		double totaldiscount = 0.00;
		String comp_id = GetSession("comp_id", request);
		PdfPCell cell;
		try {
			StrSql = "SELECT";
			// PO No. for Git
			if (vouchertype_id.equals("10")) {
				StrSql += " @voucher_po_id :=(select voucher_po_id from " + compdb(comp_id) + "axela_acc_voucher"
						+ " where voucher_id =trans.vouchertrans_voucher_id) as voucher_po_id,"
						+ " coalesce(if(@voucher_po_id !=0,"
						+ "  (select concat(vouchertype_prefix,voucher_no,vouchertype_suffix)"
						+ " from " + compdb(comp_id) + "axela_acc_voucher "
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " where voucher_id=@voucher_po_id),0),0) as voucher_po_no,";
			}
			// GIT No. for GRN
			if (vouchertype_id.equals("20")) {
				StrSql += " @voucher_git_id :=(select voucher_git_id from " + compdb(comp_id) + "axela_acc_voucher"
						+ " where voucher_id =trans.vouchertrans_voucher_id) as voucher_git_id,"
						+ " coalesce(if(@voucher_git_id !=0,"
						+ "  (select concat(vouchertype_prefix,voucher_no,vouchertype_suffix)"
						+ " from " + compdb(comp_id) + "axela_acc_voucher "
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " where voucher_id=@voucher_git_id),0),0) as voucher_git_no,";
			}
			StrSql += " item_id, item_name, item_code, item_serial, vouchertrans_alt_qty, vouchertrans_qty,"
					+ " vouchertrans_price, vouchertrans_amount,"
					+ " @discountper:=coalesce((select disc.vouchertrans_discount_perc"
					+ " FROM  " + compdb(comp_id) + " axela_acc_voucher_trans disc where disc.vouchertrans_option_id = trans.vouchertrans_rowcount"
					+ " and disc.vouchertrans_discount = 1"
					+ " and disc.vouchertrans_voucher_id = trans.vouchertrans_voucher_id ), 0) as discountper,"
					+ " uom_name,"
					+ " @discount:=coalesce((select disc.vouchertrans_amount"
					+ " FROM  " + compdb(comp_id) + " axela_acc_voucher_trans disc where disc.vouchertrans_option_id = trans.vouchertrans_rowcount"
					+ " and disc.vouchertrans_discount = 1"
					+ " and disc.vouchertrans_voucher_id = trans.vouchertrans_voucher_id ), 0) as discount,"
					+ " @netrate:=(vouchertrans_price-(@discount/vouchertrans_qty)) as netrate,"

					+ " @taxrate:=coalesce((select customer_rate"
					+ " FROM  " + compdb(comp_id) + " axela_acc_voucher_trans taxtable"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = taxtable.vouchertrans_tax_id"
					+ " where taxtable.vouchertrans_option_id = trans.vouchertrans_rowcount"
					+ " and taxtable.vouchertrans_tax = 1"
					+ " and taxtable.vouchertrans_voucher_id = trans.vouchertrans_voucher_id limit 1 ), 0) as tax,"
					+ " (@taxrate/100*(vouchertrans_amount-@discount)) AS taxpaid,"
					+ " vouchertrans_truckspace,'' AS purchasebillno"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans trans ON trans.vouchertrans_item_id = item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher on voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_uom on uom_id = vouchertrans_alt_uom_id"
					+ " WHERE 1=1 "
					+ " AND vouchertrans_discount = 0"
					+ " AND vouchertrans_tax = 0"
					+ " AND vouchertrans_voucher_id =" + voucher_id
					+ " GROUP BY item_id"
					+ " ORDER BY item_id ";

			// SOP("\n StrSql=print-ItemDetails=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("111");
			if (vouchertype_id.equals("102")) {
				item_table = new PdfPTable(10);
				item_table.setWidths(new int[]{1, 5, 15, 2, 2, 3, 4, 3, 4, 5});
			} else if (vouchertype_id.equals("21") || vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
				SOP("22222");
				item_table = new PdfPTable(10);
				item_table.setWidths(new int[]{1, 5, 15, 2, 2, 4, 4, 4, 4, 5});
				SOP("333");
			} else if (vouchertype_id.equals("12") || vouchertype_id.equals("20")) {
				item_table = new PdfPTable(7);
				item_table.setWidths(new int[]{1, 4, 17, 2, 4, 4, 5});

			} else if (vouchertype_id.equals("24")) {
				item_table = new PdfPTable(8);
				item_table.setWidths(new int[]{1, 4, 17, 2, 2, 3, 3, 4});
			}
			else if (vouchertype_id.equals("5")) {
				item_table = new PdfPTable(9);
				item_table.setWidths(new int[]{1, 4, 17, 2, 3, 3, 3, 3, 4});
			}
			SOP("222");
			item_table.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("#", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);
			if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
				cell = new PdfPCell(new Phrase("Part Number", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("Product", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}
			SOP("333");
			cell = new PdfPCell(new Phrase("Description", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);
			if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
				cell = new PdfPCell(new Phrase("Tax", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}
			SOP("444");
			cell = new PdfPCell(new Phrase("Qty", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("UOM", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);
			SOP("555");
			if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
				cell = new PdfPCell(new Phrase("Rate", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Disc.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Taxable Amount", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Tax", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}

			if (vouchertype_id.equals("10")) {
				cell = new PdfPCell(new Phrase("Order No.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}
			if (vouchertype_id.equals("21") || vouchertype_id.equals("12")
					|| vouchertype_id.equals("20") || vouchertype_id.equals("2")
					|| vouchertype_id.equals("114")
					|| vouchertype_id.equals("102")
					|| vouchertype_id.equals("4") || vouchertype_id.equals("3")) {
				cell = new PdfPCell(new Phrase("Rate", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}

			if (vouchertype_id.equals("2") || vouchertype_id.equals("4")) {
				cell = new PdfPCell(new Phrase("GRN No.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}

			if (vouchertype_id.equals("20")) {
				cell = new PdfPCell(new Phrase("Order No.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}
			if (vouchertype_id.equals("10") || vouchertype_id.equals("12")) {
				cell = new PdfPCell(new Phrase("Truck Space", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);
			}
			if (vouchertype_id.equals("21") || vouchertype_id.equals("114")
					|| vouchertype_id.equals("102")
					|| vouchertype_id.equals("117")
					|| vouchertype_id.equals("3")) {
				if (vouchertype_id.equals("21")
						|| vouchertype_id.equals("114")
						|| vouchertype_id.equals("102")
						|| vouchertype_id.equals("3")) {
					cell = new PdfPCell(new Phrase("Disc.", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);
				}

				cell = new PdfPCell(new Phrase("Net. Rate", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Amount", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				item_table.addCell(cell);

				if (vouchertype_id.equals("117")) {
					cell = new PdfPCell(new Phrase("Purchase Bill No.",
							bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);
				}
			}
			while (crs.next()) {
				count++;
				cnt++;
				// SOP("S/L No----" + count);
				// item_small_desc
				item_name = unescapehtml(crs.getString("item_name"));

				serial_no = CNumeric(crs.getString("item_serial"));
				if (!serial_no.equals("0")) {
					item_name += ("\n" + crs.getString("item_serial"));
				}
				// item_name = item_name.trim();
				totaltruckspace += crs.getDouble("vouchertrans_truckspace");

				total = crs.getDouble("vouchertrans_amount");
				grand_total += total;
				qty += crs.getDouble("vouchertrans_qty");
				totaldiscount += crs.getDouble("discount");
				// S/L No.
				// SOP("S/L No----" + count);
				if (cnt == 1) {
					cell = new PdfPCell(new Phrase(count + ".", normal_font));
				} else {
					cell = new PdfPCell(new Phrase(count + ".", normal_font));
				}
				cell.setNoWrap(true);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidthLeft(0.50f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);
				// Product
				// SOP("Product----" + crs.getString("item_code"));
				if (cnt == 1) {
					cell = new PdfPCell(new Phrase(" " + crs.getString("item_code"),
							normal_font));
				} else {
					cell = new PdfPCell(new Phrase(" " + crs.getString("item_code"),
							normal_font));
				}
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);
				// Description
				// SOP("Description----" + item_name);
				if (cnt == 1) {
					cell = new PdfPCell(new Phrase(" " + item_name, normal_font));
				} else {
					cell = new PdfPCell(new Phrase(" " + item_name, normal_font));
				}
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

				// Tax
				// SOP("TAX----" + crs.getString("tax"));
				if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {

					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("tax")), normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("tax")), normal_font));
					}

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}

				// Quantity
				// SOP("Quantity----" + crs.getDouble("vouchertrans_qty"));

				if (cnt == 1) {
					cell = new PdfPCell(new Phrase(
							(int) crs.getDouble("vouchertrans_qty") + "",
							normal_font));
				} else {
					cell = new PdfPCell(new Phrase(
							(int) crs.getDouble("vouchertrans_qty") + "",
							normal_font));
				}

				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

				// UOM
				// SOP("UOM----" + crs.getString("uom_name"));
				if (cnt == 1) {
					cell = new PdfPCell(
							new Phrase(unescapehtml(crs.getString("uom_name")),
									normal_font));
				} else {
					cell = new PdfPCell(
							new Phrase(unescapehtml(crs.getString("uom_name")),
									normal_font));
				}

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(true);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

				// Rate
				// SOP("Rate----" + crs.getDouble("vouchertrans_price"));

				if (vouchertype_id.equals("20") || vouchertype_id.equals("2")
						|| vouchertype_id.equals("21")
						|| vouchertype_id.equals("12")
						|| vouchertype_id.equals("114")
						|| vouchertype_id.equals("6")
						|| vouchertype_id.equals("3")
						|| vouchertype_id.equals("4")
						|| vouchertype_id.equals("7")) {
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_price")))),
								normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_price")))),
								normal_font));
					}

					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}
				if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("discountper")) + "%",
								normal_font));
					}
					else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("discountper")) + "%",
								normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}

				if (vouchertype_id.equals("2") || vouchertype_id.equals("4")) {
					// GRN NO.
					// SOP("GRN NO.----"+crs.getString("voucher_grn_no"));
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_grn_no")),
								normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_grn_no")),
								normal_font));
					}

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0);
					item_table.addCell(cell);
				}

				if (vouchertype_id.equals("10")) {
					// PO No.
					// SOP("PO NO.----"+crs.getString("voucher_po_no"));
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_po_no")),
								normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_po_no")),
								normal_font));
					}

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}

				if (vouchertype_id.equals("20")) {
					// SOP("GIT----"+crs.getString("voucher_git_no"));
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_git_no")
										+ ""), normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(crs.getString("voucher_git_no")
										+ ""), normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}
				if (vouchertype_id.equals("12") || vouchertype_id.equals("10")) {
					// Truck Space
					// SOP("Truck Space----"+crs.getDouble("vouchertrans_truckspace"));

					if (cnt == 1) {
						cell = new PdfPCell(
								new Phrase(unescapehtml(df.format(crs
										.getDouble("vouchertrans_truckspace"))
										+ ""), normal_font));
					} else {
						cell = new PdfPCell(
								new Phrase(unescapehtml(df.format(crs
										.getDouble("vouchertrans_truckspace"))
										+ ""), normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.50f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
				}

				// Bill
				if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
					// Taxable Amount
					if (cnt == 1) {
						cell = new PdfPCell(
								new Phrase(unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_amount"))
										+ "")), normal_font));
					} else {
						cell = new PdfPCell(
								new Phrase(unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_amount")))
										+ ""), normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.50f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);

					// Tax
					if (cnt == 1) {
						cell = new PdfPCell(
								new Phrase(unescapehtml(df.format(crs
										.getDouble("taxpaid"))
										+ ""), normal_font));
					} else {
						cell = new PdfPCell(
								new Phrase(unescapehtml(df.format(crs
										.getDouble("taxpaid"))
										+ ""), normal_font));
					}
					total_tax = "" + (Double.parseDouble(total_tax) + crs.getDouble("taxpaid"));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.50f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);

				}

				if (vouchertype_id.equals("21")
						|| vouchertype_id.equals("114")
						|| vouchertype_id.equals("102")
						|| vouchertype_id.equals("117")
						|| vouchertype_id.equals("3")) {
					// Discount
					// SOP("Discount----" + crs.getDouble("discount"));
					if (vouchertype_id.equals("21")
							|| vouchertype_id.equals("114")
							|| vouchertype_id.equals("102")
							|| vouchertype_id.equals("3")) {
						if (cnt == 1) {
							cell = new PdfPCell(new Phrase(
									unescapehtml(df.format(crs
											.getDouble("discountper")) + "%"),
									normal_font));
						} else {
							cell = new PdfPCell(new Phrase(
									unescapehtml(df.format(crs
											.getDouble("discountper")) + "%"),
									normal_font));
						}

						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(false);
						cell.setBorderWidthLeft(0.25f);
						cell.setBorderWidthRight(0.25f);
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.25f);
						item_table.addCell(cell);
					}
					// }
					// Net. Rate
					// SOP("Net. Rate----"+crs.getDouble("netrate"));
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("netrate")))), normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("netrate")))), normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.25f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);

					// Amount
					// SOP("Amount----"+total);
					if (cnt == 1) {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_amount")))),
								normal_font));
					} else {
						cell = new PdfPCell(new Phrase(
								unescapehtml(IndDecimalFormat(df.format(crs
										.getDouble("vouchertrans_amount")))),
								normal_font));
					}

					total = 0.0;
					config_total = 0.0;
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					cell.setBorderWidthLeft(0.25f);
					cell.setBorderWidthRight(0.50f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.25f);
					item_table.addCell(cell);
					if (vouchertype_id.equals("117")) {
						// Purchase Bill No.
						// SOP("Purchase Bill No.----"+purchasebillno);
						if (cnt == 1) {
							cell = new PdfPCell(
									new Phrase(unescapehtml(""),
											normal_font));
						} else {
							cell = new PdfPCell(
									new Phrase(unescapehtml(""),
											normal_font));
						}
						total = 0.0;
						config_total = 0.0;
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						cell.setBorderWidthLeft(0.25f);
						cell.setBorderWidthRight(0.50f);
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.25f);
						item_table.addCell(cell);
					}
				}

			}
			// Item List end
			// For Bill

			if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
				table_temp = new PdfPTable(3);
				table_temp.setWidths(new float[]{(float) 6.5, (float) 2.5, (float) 2.5});

				paragraph = new Paragraph();
				paragraph.add(new Phrase("Remarks: " + "\n", normal_font));
				paragraph.add(new Phrase("No. of Items: " + count + "\n", normal_font));
				paragraph.add(new Phrase("Total Qty: " + qty + "\n", normal_font));

				cell = new PdfPCell(new Phrase(paragraph));
				cell.setNoWrap(true);
				cell.setColspan(4);
				cell.setRowspan(2);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.50f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);
				// Temp table data
				paragraph = new Paragraph();
				paragraph.add(new Phrase("\n" + "Part Sub Total Amount: ", normal_font));
				paragraph.add(new Phrase("\n" + "Part Total Taxable Amount: ", normal_font));
				//
				cell = new PdfPCell(new Phrase(paragraph));
				// cell.setBorderWidthLeft(0.25f);
				// cell.setBorderWidthRight(0.50f);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidth(0);
				// cell.setBorderWidthBottom(0.25f);
				table_temp.addCell(cell);
				//
				paragraph = new Paragraph();
				paragraph.add(new Phrase("\n" + IndDecimalFormat(df.format((grand_total))), normal_font));
				paragraph.add(new Phrase("\n" + IndDecimalFormat(df.format((grand_total))), normal_font));
				//
				cell = new PdfPCell(new Phrase(paragraph));
				// // cell.setNoWrap(true);
				// cell.setBorderWidthLeft(0.25f);
				// cell.setBorderWidthRight(0.50f);
				cell.setBorderWidth(0.25f);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				// cell.setBorderWidthBottom(0.25f);
				table_temp.addCell(cell);
				//
				paragraph = new Paragraph();

				paragraph.add(new Phrase("\n" + IndDecimalFormat(df.format(Double.parseDouble(total_tax))), normal_font));
				paragraph.add(new Phrase("\n" + IndDecimalFormat(df.format(Double.parseDouble(total_tax))), normal_font));

				cell = new PdfPCell(new Phrase(paragraph));
				// // cell.setNoWrap(true);
				// cell.setBorderWidthLeft(0.25f);
				// cell.setBorderWidthRight(0.50f);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorderWidth(0);
				// cell.setBorderWidthBottom(0.25f);
				table_temp.addCell(cell);

				cell = new PdfPCell(table_temp);
				cell.setColspan(6);
				// cell.setRowspan(2);
				// cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.50f);

				// cell.setBorderWidth(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

				// Temp table data end

				// paragraph = new Paragraph();

				// paragraph.add(new Phrase("Remarks: ", normal_font));

				cell = BillSundryForBill(comp_id, cell, voucher_id);
				cell.setNoWrap(true);
				cell.setColspan(6);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.50f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

				paragraph = new Paragraph();
				paragraph.add(new Phrase(" \n", normal_font));
				paragraph.add(new Phrase("Customer Signature" + "", normal_font));
				cell = new PdfPCell(new Phrase(paragraph));
				cell.setNoWrap(true);
				cell.setColspan(4);
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
				cell.setVerticalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.50f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);
				grand_total = (grand_total - totaldiscount) + Double.parseDouble(total_tax);
				paragraph = new Paragraph();
				paragraph.add(new Phrase("Sub Total Amount: ", normal_font));
				grand_total = Math.round(Double.parseDouble(df.format(grand_total)));
				paragraph.add(new Phrase("" + IndDecimalFormat(grand_total + ""), normal_font));
				cell = new PdfPCell(new Phrase(paragraph));
				cell.setNoWrap(true);
				cell.setColspan(6);
				cell.setRowspan(2);
				cell.setBorderWidthLeft(0);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.25f);
				item_table.addCell(cell);

			}

			if (vouchertype_id.equals("102")) {
				colspan = 9;
				sundrycolspan = 4;
			} else if (vouchertype_id.equals("21")
					|| vouchertype_id.equals("114")
					|| vouchertype_id.equals("102")
					|| vouchertype_id.equals("3")) {
				colspan = 8;
				sundrycolspan = 4;

			} else if (vouchertype_id.equals("12")
					|| vouchertype_id.equals("10")
					|| vouchertype_id.equals("20") || vouchertype_id.equals("2")
					|| vouchertype_id.equals("4")) {
				colspan = 6;
				sundrycolspan = 2;
			} else if (vouchertype_id.equals("117")) {
				colspan = 7;
				sundrycolspan = 3;
			}
			// if (vouchertype_id.equals("102")) {
			// cell = new PdfPCell(new Phrase("Total:", bold_font));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// cell.setNoWrap(true);
			// cell.setColspan(4);
			// item_table.addCell(cell);
			// } else {
			// cell = new PdfPCell(new Phrase("Total:", bold_font));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// cell.setNoWrap(true);
			// cell.setColspan(2);
			// item_table.addCell(cell);
			// }

			// Total Qty
			// cell = new PdfPCell(new Phrase((int) qty + "", bold_font));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// cell.setNoWrap(true);
			// item_table.addCell(cell);
			//
			// cell = new PdfPCell(new Phrase("", normal_font));
			// cell.setColspan(sundrycolspan);
			// item_table.addCell(cell);

			if (vouchertype_id.equals("12") || vouchertype_id.equals("10")) {

				cell = new PdfPCell(new Phrase(unescapehtml(""
						+ df.format(totaltruckspace)), bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);
			}
			if (vouchertype_id.equals("21") || vouchertype_id.equals("102")
					|| vouchertype_id.equals("114")
					|| vouchertype_id.equals("3")) {
				cell = new PdfPCell(
						new Phrase(unescapehtml(IndDecimalFormat(df
								.format((grand_total)))), bold_font));
			}

			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

			if (vouchertype_id.equals("21") || vouchertype_id.equals("102")
					|| vouchertype_id.equals("114")
					|| vouchertype_id.equals("3")) {
				// Discount
				if (totaldiscount != 0.0) {
					cell = new PdfPCell(new Phrase(unescapehtml("Discount"),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setColspan(colspan);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(unescapehtml("-"
							+ IndDecimalFormat(df.format((totaldiscount)))),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				}
				// Tax
				TaxBreakUp(item_table, colspan, voucher_id);
				// BillSundry
				BillSundry(item_table, colspan, voucher_id);
			}

			// Final total
			if (vouchertype_id.equals("21") || vouchertype_id.equals("102")
					|| vouchertype_id.equals("114")
					|| vouchertype_id.equals("3")) {
				cell = new PdfPCell(new Phrase(unescapehtml("Total: "),
						bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setColspan(colspan);
				item_table.addCell(cell);
				grand_total = ((grand_total + total_taxamt) - totaldiscount);
				if (total_bilsundry != 0.0) {
					grand_total += total_bilsundry;
				}
				cell = new PdfPCell(
						new Phrase(unescapehtml(IndDecimalFormat(df
								.format((grand_total)))), bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setNoWrap(true);
				item_table.addCell(cell);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return item_table;
	}
	public String formatqty(String n) {
		String nn = "";
		if (!n.equals("")) {
			nn = n.replace(".00", "");
		}
		return nn;
	}
}
