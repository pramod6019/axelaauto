package axela.preowned;
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

public class Preowned_Stock_Gatepass_Print extends Connect {

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
    public String preownedstockgatepass_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	BranchAccess = GetSession("BranchAccess", request);
                ExeAccess = GetSession("ExeAccess", request);
                CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
                preownedstockgatepass_id = PadQuotes(request.getParameter("preownedstockgatepass_id"));
                StrSearch = " AND preownedstockgatepass_id = " + preownedstockgatepass_id + "";
                if (ReturnPerm(comp_id, "emp_preowned_stock_access", request).equals("1")) {
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
        StrSql = "SELECT COALESCE(preownedstock_comm_no, '') AS preownedstock_comm_no,"
                + " preownedstockgatepass_id, preownedstockgatepass_time, driver_name, branch_name,"
                + " COALESCE(preownedmodel_name,'') AS model_name, COALESCE(variant_name, '') AS variant_name,"
                + " COALESCE(preownedstock_chassis_no, '') AS preownedstock_chassis_no,"
                + " COALESCE(preownedstock_comm_no, '') AS preownedstock_comm_no,"
                + " CONCAT(fromloc.preownedlocation_name, ' (', fromloc.preownedlocation_id, ')') AS from_location_name,"
                + " CONCAT(toloc.preownedlocation_name, ' (', toloc.preownedlocation_id, ')') AS to_location_name,"
                + " COALESCE(preownedstock_engine_no, '') AS preownedstock_engine_no,"
                + " intcolour_name, extcolour_name,"
                + " preownedstockgatepass_notes, preowned_regno"
                + " FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = preownedstockgatepass_preownedstock_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver ON driver_id = preownedstockgatepass_driver_id"
                + " INNER JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
                + " INNER JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
                + " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
                + " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned_location fromloc ON fromloc.preownedlocation_id = preownedstockgatepass_from_location_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned_location toloc ON toloc.preownedlocation_id = preownedstockgatepass_to_location_id"
                + " WHERE 1 = 1" + StrSearch + BranchAccess + "";
//        SOP("StrSql = " + StrSql); 
        CachedRowSet crs =processQuery(StrSql, 0);

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
                    Image comp_logo = Image.getInstance(CompLogoPath());
                    cell.addElement(new Chunk(comp_logo, 0, 0));
                    cell.setFixedHeight(comp_logo.getHeight());
                    cell.setBorderWidth(0);
                    cell.setPaddingLeft(0);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    header_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Pre Owned Stock Movement Gate Pass", header_font));
                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    header_table.addCell(cell);
                    document.add(header_table);

                    item_table = new PdfPTable(4);
                    item_table.setWidths(new int[]{25, 25, 25, 25});
                    item_table.setWidthPercentage(100);
                    item_table.setKeepTogether(true);

                    cell = new PdfPCell(new Phrase("Name of the Company:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("branch_name")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(3);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Gate Pass Time:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("preownedstockgatepass_time")), bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(3);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Model:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("model_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Variant:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("variant_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Engine Number:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("preownedstock_engine_no"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Chassis Number:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("preownedstock_chassis_no"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Commission Number:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("preownedstock_comm_no"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Reg. No.:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("preowned_regno"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Exterior:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("extcolour_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Interior:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("intcolour_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);

                    PdfPTable movement_table = new PdfPTable(4);
                    movement_table.setWidthPercentage(100);
                    cell = new PdfPCell(new Phrase("Vehicle Movement", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(4);
                    cell.setBackgroundColor(BaseColor.GRAY);
                    movement_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("From :", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    movement_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("from_location_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    movement_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("To :", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    movement_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(crs.getString("to_location_name"), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    movement_table.addCell(cell);

                    cell = new PdfPCell(movement_table);
                    cell.setColspan(4);
                    item_table.addCell(cell);

                    document.add(item_table);

                    PdfPTable sign_table = new PdfPTable(2);
                    sign_table.setWidthPercentage(100);
                    cell = new PdfPCell(new Phrase("Agreed and Signed:", bold_font));
                    cell.setNoWrap(true);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("\n ", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Driver's Name:", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("driver_name")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Remarks:", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("preownedstockgatepass_notes")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Approved by:", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Purpose of use:", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Pre Owned Stock Movement", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    sign_table.addCell(cell);

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
                    top_table.addCell(cell);

                    cell = new PdfPCell(top_table);
                    cell.setColspan(2);
                    sign_table.addCell(cell);

                    document.add(sign_table);
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
