package axela.inventory;
//smitha nag 14 feb 2013- 17feb 2013(edited)

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Stock_Gatepass_Print extends Connect {

	public String comp_id = "0";
	public String invoice_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";
	public String comp_name = "";
	public String contact = "";
	PdfPTable item_table;
	PdfPTable top_table;
	PdfPTable body_table;
	PdfPTable two_col;
	PdfPTable header_table;
	PdfPTable six_col;
	PdfPTable four_col;
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	public String StrSearch = "";
	public String vehstockgatepass_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				vehstockgatepass_id = PadQuotes(request.getParameter("vehstockgatepass_id"));
				StrSearch = " AND vehstockgatepass_id = " + vehstockgatepass_id + "";
				if (ReturnPerm(comp_id, "emp_stock_access", request).equals("1")) {
					GatepassDetails(request, response, "pdf");
				} else {
					response.sendRedirect(AccessDenied());
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void GatepassDetails(HttpServletRequest request, HttpServletResponse response, String purpose) throws IOException, DocumentException {

		PdfPCell cell;
		StrSql = "SELECT vehstock_id, "
				// + "COALESCE(vehstock_comm_no, '') AS vehstock_comm_no,"
				+ " vehstock_chassis_no, "
				+ "vehstock_engine_no, "
				// + "vehstock_comm_no,"
				+ " vehstockgatepass_id, vehstockgatepass_time,"
				+ " vehstockdriver_name,"
				+ " branch_name, branch_logo, "
				+ " COALESCE(model_name,'') AS model_name, COALESCE(item_name,'') AS item_name,"
				+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
				+ " WHERE option_optiontype_id = 1"
				+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
				+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
				+ " WHERE option_optiontype_id = 2"
				+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
				// + " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no,"
				+ " CONCAT(fromloc.vehstocklocation_name, ' (', fromloc.vehstocklocation_id, ')') AS from_location_name,"
				+ " CONCAT(toloc.vehstocklocation_name, ' (', toloc.vehstocklocation_id, ')') AS to_location_name,"
				+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock on vehstock_id = vehstockgatepass_vehstock_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_driver ON vehstockdriver_id = vehstockgatepass_stockdriver_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location fromloc ON fromloc.vehstocklocation_id = vehstockgatepass_from_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location toloc ON toloc.vehstocklocation_id = vehstockgatepass_to_location_id"
				+ " WHERE 1 = 1 " + StrSearch + " " + BranchAccess;
		// SOP("StrSql===" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					PdfWriter.getInstance(document, response.getOutputStream());
				}
				document.open();
				while (crs.next()) {
					header_table = new PdfPTable(2);
					header_table.setWidthPercentage(100);
					header_table.setKeepTogether(true);

					cell = new PdfPCell();
					if (!crs.getString("branch_logo").equals("")) {
						Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
						cell.addElement(new Chunk(branch_logo, 0, 0));
						cell.setFixedHeight(branch_logo.getHeight());
					}

					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					header_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Stock Movement Gate Pass", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					header_table.addCell(cell);
					document.add(header_table);

					item_table = new PdfPTable(2);
					item_table.setWidths(new int[]{4, 12});
					item_table.setWidthPercentage(100);
					item_table.setKeepTogether(true);
					cell = new PdfPCell(new Phrase("Name of the Company:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("branch_name")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Gate Pass Time:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("vehstockgatepass_time")), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					PdfPTable movement_table = new PdfPTable(4);
					movement_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("Stock ID:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("vehstock_id"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Chassis No.:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);
					//
					cell = new PdfPCell(new Phrase(crs.getString("vehstock_chassis_no")
							, normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Model:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("model_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Item:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("item_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Engine No.:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("vehstock_engine_no"), normal_font));
					cell.setColspan(3);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					// cell = new PdfPCell(new Phrase("Comm No.:", bold_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// movement_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(""
					// // crs.getString("vehstock_comm_no")
					// , normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Paint Work:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("paintwork"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Upholstery:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("upholstery"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Vehicle Movement", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					cell.setBackgroundColor(BaseColor.GRAY);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("From :", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("from_location_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(new Phrase("To :", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("to_location_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					movement_table.addCell(cell);

					cell = new PdfPCell(movement_table);
					cell.setColspan(2);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Agreed and Signed:", bold_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("\n ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Driver's Name:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("vehstockdriver_name"))
							, normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Approved by:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Purpose of use:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Stock Movement", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					PdfPTable top_table = new PdfPTable(4);
					top_table.setWidthPercentage(100);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Authorised Person Name:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(45);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Authorised Person's\n          Signature:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					// cell.setNoWrap(true);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(45);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("     ", normal_font));
					cell.setBorder(0);
					cell.setColspan(4);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Security Person Name:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(45);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Security Person's\n          Signature:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(45);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setColspan(4);
					cell.setFixedHeight(45);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					// cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell(top_table);
					cell.setColspan(2);
					item_table.addCell(cell);

					document.add(item_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Gate Pass!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
