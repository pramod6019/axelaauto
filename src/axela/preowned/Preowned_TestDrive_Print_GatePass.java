package axela.preowned;
//Dilip Kumar 06 Jul 2013

import java.io.IOException;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Preowned_TestDrive_Print_GatePass extends Connect {

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
//    private String no_rec;
//    private int totalcount;
    public String StrSearch = "";
    public String testdrive_id = "";
//    private int Rectangle;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	BranchAccess = GetSession("BranchAccess", request);
                ExeAccess = GetSession("ExeAccess", request);
                CheckPerm(comp_id, "emp_preowned_testdrive_access", request, response);
                invoice_id = CNumeric(PadQuotes(request.getParameter("invoice_id")));
                testdrive_id = PadQuotes(request.getParameter("testdrive_id"));
                StrSearch = " and testdrive_id=" + testdrive_id + "";
                InvoiceDetails(request, response, "pdf");
            }
            
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void InvoiceDetails(HttpServletRequest request, HttpServletResponse response, String purpose) throws IOException, DocumentException {
//        StringBuilder Str = new StringBuilder();
        Document document = new Document();
        if (purpose.equals("pdf")) {
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
        }
        document.open();
//        int count = 0;
//        int colspan = 0;

        PdfPCell cell;
        StrSql = "SELECT testdrive_id, variant_id, CONCAT(variant_name, '-', preowned_regno) AS variant_name,"
                + " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
                + " COALESCE(CONCAT(customer_address1, ', ', customer_address2, ', ', customer_address3, ', ', city_name, ' - ', customer_pin, '.'), '') AS customer_address1,"
                + " contact_mobile1, contact_mobile2, contact_email1, contact_email2, preowned_regno, location_name,"
                + " testdrive_time_from, testdrive_in_time, testdrive_out_time, testdrive_out_kms, testdrive_in_kms, testdrive_license_no,"
                + " testdrive_license_address, testdrive_license_issued_by, testdrive_license_valid,"
                + " CONCAT(testdriveemp.emp_name, ' (', testdriveemp.emp_ref_no, ')') AS executive,"
                //                + " driveremp.emp_name as driver, veh_regno , comp_logo  "
                + " driver_name AS driver, branch_logo"
                + " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
                + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id   "
                + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
                + " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_emp as testdriveemp on testdriveemp.emp_id = testdrive_emp_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver  ON driver_id = testdrive_out_driver_id"
                + " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
                //                + " inner join " + compdb(comp_id) + "axela_emp as driveremp on driveremp.emp_id= testdrive_out_driver_id"
                + ", " + compdb(comp_id) + "axela_comp"
                + " WHERE  testdrive_out_time!='' " + StrSearch + " " + BranchAccess + ""
                + " GROUP BY testdrive_id"
                + " ORDER BY branch_name, customer_name";
        //SOP("StrSql");
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    header_table = new PdfPTable(2);
                    header_table.setWidthPercentage(100);
                    header_table.setKeepTogether(true);

                    cell = new PdfPCell();
                    if (!crs.getString("branch_logo").equals("")) {
                        Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
//                        Image compo_logo = Image.getInstance(CompLogoPath());
                        cell.addElement(new Chunk(branch_logo, 0, 0));
                        cell.setFixedHeight(branch_logo.getHeight());
                    } else {
                        cell.addElement(new Phrase(""));
                    }
                    cell.setBorderWidth(0);
                    cell.setPaddingLeft(0);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    header_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("PREOWNED CAR AGREEMENT", header_font));
                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    header_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
//                    header_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
//                    header_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
//                    header_table.addCell(cell);
                    document.add(header_table);

                    item_table = new PdfPTable(2);
                    item_table.setWidths(new int[]{4, 12});
                    item_table.setWidthPercentage(100);
                    item_table.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Name of the Customer:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("contactname")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Address:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setBorderWidth(0);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("customer_address1")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Mobile:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setBorderWidth(0);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("contact_mobile1")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Tel:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setBorderWidth(0);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("contact_mobile2")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Test Drive Vehicle Requested:", bold_font));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setBorderWidth(0);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("variant_name")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Registration Number:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("preowned_regno")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    item_table.addCell(cell);
                    document.add(item_table);

                    two_col = new PdfPTable(4);
//                    two_col.setWidths(new int[]{ 8, 12});
                    two_col.setWidthPercentage(100);
                    two_col.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Date:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    // padding done here.......................................................
//                    cell.setPadding(10);
//                    cell.setPaddingBottom(5);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_time_from")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Place of Test Drive:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("location_name")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Time (Commence):", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_out_time")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Time (End):", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_in_time")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    two_col.addCell(cell);
                    document.add(two_col);

                    top_table = new PdfPTable(1);
                    top_table.setWidthPercentage(100);
                    top_table.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Terms and Conditions:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    top_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml("The customer is responsible for the testdrivenstrator car at all times while the car is being driven.\n"
                            + "The customer will provide a copy for valid Driving license to Axelaauto cars India Pvt. Ltd. for driving the demoenstrator car "
                            + "OR not having a valid driving License are not permitted to drive the demonstrator car.\n"
                            + "The customer, agree to report any damage caused due to an accident to the police and to Axelaauto cars India Pvt. Ltd. as"
                            + "soon as possible and to supply the relevant police report immediately to Axelaauto cars India Pvt. Ltd.\n"
                            + "The customer agree to pay for any damages(To the Vehicle/Third Party Damages) if the same occur while he is Driving the"
                            + "Test Drive Vehicle.\n"
                            + "The customer is responsible for all relevant excess insurance payments if the same are required in case the vehicle required"
                            + "any Insurance claims.\n"
                            + "The customer is responsible for any parking fines or for any monitoring offences committed while in possession of the"
                            + "demonstrator car.\n"
                            + "The customer agree not to carry any animals or smoke in the demonstrator car.\n"
                            + "The customer agree that Axelaauto cars India Pvt. Ltd. are not responsible for goods left in the demonstrator car after the Test"
                            + "Drive.\n"), normal_font));
//                    cell.setBorderWidth(0);
//                    cell.setNoWrap(true);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    top_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
////                    cell.setBorderWidth(0);
//                    top_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
////                    cell.setBorderWidth(0);
//                    top_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase("", bold_font));
////                    cell.setBorderWidth(0);
//                    top_table.addCell(cell);
                    document.add(top_table);

                    four_col = new PdfPTable(4);
//                    body_table.setWidths(new int[]{2,2,2,2,3,3});
                    four_col.setWidthPercentage(100);
                    four_col.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Agreed and Signed:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setNoWrap(true);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    four_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("\n ", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    four_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Date:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    four_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(strToLongDate(ToLongDate(kknow())), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    four_col.addCell(cell);
                    document.add(four_col);

                    six_col = new PdfPTable(6);
//                    body_table.setWidths(new int[]{2,2,2,2,3,3});
                    six_col.setWidthPercentage(100);
                    six_col.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Driving License#:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setNoWrap(true);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(crs.getString("testdrive_license_no"), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Issued by:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_license_issued_by")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Valid till:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_license_valid")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("Copy of Driving License Enclose:", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setNoWrap(true);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
                    six_col.addCell(cell);
                    cell = new PdfPCell(new Phrase("", bold_font));
//                    cell.setBorderWidth(0);
                    six_col.addCell(cell);
                    document.add(six_col);

                    body_table = new PdfPTable(4);
//                    body_table.setWidths(new int[]{ 8, 12});
                    body_table.setWidthPercentage(100);
                    body_table.setKeepTogether(true);
                    cell = new PdfPCell(new Phrase("Sales Manager:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("executive")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Driver's Name:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("driver")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Approved by:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Purpose of use:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Test Drive", bold_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Starting Kilometers:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_out_kms")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Closing Kilometers:", normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_in_kms")), normal_font));
//                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    body_table.addCell(cell);
                    document.add(body_table);
                }
            } else {
                response.sendRedirect("../portal/error.jsp?msg=Invalid Gate Pass!");
//                Str.append("Invalid Gate Pass!");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        document.close();
    }
}
