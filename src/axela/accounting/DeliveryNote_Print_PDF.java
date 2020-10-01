//package axela.accounting;
////JEET 26th NOV, 2014 
//
//import cloudify.connect.Connect;
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
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.List;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.rowset.CachedRowSet;
//
//public class DeliveryNote_Print_PDF extends Connect {
//
//     
//    public String voucher_id = "0";
//    public String vouchertype_id = "0";
//    public String voucherclass_id = "3";
//    public String StrSql = "";
//    public String StrHTML = "";
//    public String BranchAccess = "";
//    public String ExeAccess = "";
//    double total_taxamt = 0.00;
//    public String total_disc = "";
//    public double grand_total = 0.0;
//    PdfPTable item_table;
//    Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
//    Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
//    Font normal_font = FontFactory.getFont("Helvetica", 10);
//    Bill_Print_PDF bill_obj = new Bill_Print_PDF();
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            //CheckSession(request, response);
//            
//            CheckPerm(comp_id,  "emp_delivery_note_access", request, response);
//            BranchAccess = GetSession("BranchAccess", request);
//            ExeAccess = GetSession("ExeAccess", request);
//            voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
//            vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
//
//            if (ReturnPerm(comp_id,  "emp_delivery_note_access", request).equals("1")) {
//                DeliveryNoteDetails(request, response, voucher_id,  BranchAccess, ExeAccess, "pdf");
//            }
//        } catch (Exception ex) {
//            SOPError("Axelaauto===" + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//    }
//
//    public void DeliveryNoteDetails(HttpServletRequest request, HttpServletResponse response, String voucher_id,   String BranchAccess, String ExeAccess, String purpose) throws IOException, DocumentException {
//        try {
//
//            StrSql = "SELECT  voucher_date, voucher_id, voucher_amount, voucher_customer_id, voucher_consignee_add, voucher_billing_add, "
//                    + " voucher_narration, voucher_notes,"
//                    + " vouchertrans_cheque_bank, vouchertrans_cheque_no,"
//                    + " vouchertrans_cheque_date, vouchertrans_paymode_id, vouchertype_label,"
//                    + " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
//                    + " branch_add, branch_pin, branch_phone1, branch_mobile1, branch_email1, branch_invoice_name,"
//                    + "  comp_logo, comp_name,"
//                    + " customer_name, customer_address, customer_pin,"
//                    + " CONCAT(contact_fname,' ', contact_lname) AS contact_name, title_desc,"
//                    + " COALESCE(branchcity.city_name,'') as city_name,"
//                    + " COALESCE(branchstate.state_name,'')  as state_name,COALESCE(customercity.city_name,'')  as cust_city,"
//                    + " COALESCE( customerstate.state_name ,'') as cust_state,"
//                    + " COALESCE( branchcountry.country_name ,'') as branch_country,"
//                    + " COALESCE( customercountry.country_name ,'') as cust_country,"
//                    + " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms, jobtitle_desc"
//                    + " FROM  "+compdb(comp_id)+"axela_acc_voucher"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_branch ON branch_id = voucher_branch_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_customer ON customer_id = voucher_customer_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_customer_contact ON contact_customer_id = customer_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_title ON title_id = contact_title_id"
//                    + " LEFT JOIN axela_city branchcity  ON  branchcity.city_id = branch_city_id"
//                    + " LEFT JOIN axela_city customercity ON customercity.city_id = customer_city_id"
//                    + " LEFT JOIN axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
//                    + " LEFT JOIN axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
//                    + " LEFT JOIN axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id"
//                    + " LEFT JOIN axela_country customercountry ON customercountry.country_id = customerstate.state_country_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_emp ON emp_id = voucher_emp_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_acc_voucher_class ON voucherclass_id = "+voucherclass_id
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
//                    + " INNER JOIN  "+compdb(comp_id)+"axela_jobtitle ON jobtitle_id = emp_jobtitle_id ,"
//                    + "  axela_comp"
//                    + " WHERE voucher_id = " + voucher_id + BranchAccess + ExeAccess + ""
//                    + " AND vouchertype_id = " + vouchertype_id
//                    + " GROUP BY voucher_id"
//                    + " ORDER BY voucher_id DESC";
////            SOP("StrSql===" + StrSqlBreaker(StrSql));
//            CachedRowSet crs = processQuery(StrSql, 0);
//
//            if (crs.isBeforeFirst()) {
//                Document document = new Document();
//                if (purpose.equals("pdf")) {
//                    response.setContentType("application/pdf");
//                    PdfWriter.getInstance(document, response.getOutputStream());
//                } else if (purpose.equals("file")) {
//                    File f = new File(CachePath(comp_id));
//                    if (!f.exists()) {
//                        f.mkdirs();
//                    }
//                    PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Invoice_" + voucher_id + ".pdf"));
//                }
//                document.open();
//
//                while (crs.next()) {
//                    PdfPTable top_table = new PdfPTable(2);
//                    top_table.setWidthPercentage(100);
//                    PdfPCell cell;
//                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("comp_name"))));
//
//                    if (!crs.getString("comp_logo").equals("")) {
////                        Image comp_logo = Image.getInstance(CompLogoPath() + unescapehtml(crs.getString("comp_logo")));
////                        cell.addElement(new Chunk(comp_logo, 0, 0));
////                        cell.setFixedHeight(comp_logo.getHeight());
//                    } else {
//                        cell.addElement(new Phrase(""));
//                    }
//
//                    cell.setBorderWidth(0);
//                    cell.setPaddingLeft(0);
//                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//                    top_table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("vouchertype_label")), header_font));
//                    cell.setBorderWidth(0);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//                    top_table.addCell(cell);
//
//                    String contact = "", comp_name = "";
//                    comp_name += "M/s. " + unescapehtml(crs.getString("branch_invoice_name"));
//                    contact += unescapehtml(crs.getString("branch_add")) + ",";
//                    contact += "\n" + unescapehtml(crs.getString("city_name")) + " - " + unescapehtml(crs.getString("branch_pin")) + ",\n";
//                    contact += unescapehtml(crs.getString("state_name")) + ",\n" + unescapehtml(crs.getString("branch_country")) + ".";
//                    if (!crs.getString("branch_phone1").equals("")) {
//                        contact += "\n" + unescapehtml(crs.getString("branch_phone1"));
//                    }
//
//                    if (!crs.getString("branch_mobile1").equals("")) {
//                        contact += "\n" + unescapehtml(crs.getString("branch_mobile1"));
//                    }
//                    cell = new PdfPCell();
//                    cell.addElement(new Phrase(comp_name, bold_font));
//                    cell.addElement(new Phrase(contact, normal_font));
//                    cell.setVerticalAlignment(Element.ALIGN_TOP);
//                    cell.setRowspan(3);
//                    top_table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Date: " + strToShortDate(unescapehtml(crs.getString("voucher_date")) ), normal_font));
//                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
//                    cell.setFixedHeight(35);
//                    top_table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Voucher ID: " + unescapehtml(crs.getString("voucher_id")), normal_font));
//                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
//                    cell.setFixedHeight(35);
//                    top_table.addCell(cell);
//
//                     cell = new PdfPCell(new Phrase("Voucher No.: " +unescapehtml(crs.getString("voucher_no")), normal_font));
//                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
//                    cell.setFixedHeight(35);
//                    top_table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("Customer: ", bold_font));
//                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setNoWrap(true);
////                    cell.setBorderWidthLeft(0.25f);
//                    cell.setBorderWidthRight(0.25f);
//                    cell.setBorderWidthTop(0);
//                    cell.setBorderWidthBottom(0);
//                    if (crs.getString("voucher_consignee_add").equals("")) {
//                        cell.setColspan(2);
//                    }
//                    top_table.addCell(cell);
//
//                    //Consignee
//                    if (!crs.getString("voucher_consignee_add").equals("")) {
//                        cell = new PdfPCell(new Phrase("Consignee Address:", bold_font));
//                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                        cell.setNoWrap(true);
//                       cell.setBorderWidthLeft(0.25f);
//                       cell.setBorderWidthLeft(0.25f);
//                        cell.setBorderWidthTop(0);
//                        cell.setBorderWidthBottom(0);
//                        top_table.addCell(cell);
//                    }
//
//// Dont Touch
//                    cell = new PdfPCell();
//                    cell.addElement(new Phrase(unescapehtml(crs.getString("customer_name")), normal_font));
//                    if (!crs.getString("customer_name").equals(crs.getString("contact_name"))) {
//                        cell.addElement(new Phrase(unescapehtml(crs.getString("title_desc") + " " + crs.getString("contact_name")), normal_font));
//                    }
//                    if (!crs.getString("voucher_billing_add").equals("")) {
//                        cell.addElement(new Phrase(unescapehtml(crs.getString("voucher_billing_add")) + ".", normal_font));
//                    }
//                    if (crs.getString("voucher_consignee_add").equals("")) {
//                        cell.setColspan(2);
//                    }
//                    cell.setVerticalAlignment(Element.ALIGN_TOP);
//                    cell.setNoWrap(true);
////                    cell.setBorderWidthLeft(0.25f);
//                    cell.setBorderWidthRight(0.25f);
//                    cell.setBorderWidthTop(0);
//                    cell.setBorderWidthBottom(0);
//                    top_table.addCell(cell);
//
//                    //Consignee Address
//                    if (!crs.getString("voucher_consignee_add").equals("")) {
//                        cell = new PdfPCell();
//                        cell.addElement(new Phrase(unescapehtml(crs.getString("voucher_consignee_add")) + ".", normal_font));
//                        cell.setVerticalAlignment(Element.ALIGN_TOP);
//                        cell.setNoWrap(true);
//                        cell.setBorderWidthLeft(0.25f);
////                        cell.setBorderWidthRight(0.25f);
//                        cell.setBorderWidthTop(0);
//                        cell.setBorderWidthBottom(0);
//                        top_table.addCell(cell);
//                    }
//                    
//                    document.add(top_table);
//
//                    item_table = bill_obj.ItemDetails(voucher_id);
//                    grand_total = bill_obj.grand_total;
//                    document.add(item_table);
//
//                    PdfPTable total_table = new PdfPTable(2);
//                    total_table.setWidthPercentage(100);
//                    cell = new PdfPCell(new Phrase("Amount Chargeable (in words): "
//                    		+ "Rupees" +  "  "
//                            + toTitleCase(IndianCurrencyFormatToWord((((long) grand_total)))) + " Only/-.\n" + " ", normal_font));
//                    cell.setColspan(2);
//                    total_table.addCell(cell);
//                    if (crs.getString("voucher_terms").length() > 0) {
//                        StringReader reader = new StringReader(unescapehtml("<font size=2>" + crs.getString("voucher_terms") + "</font>"));
//                        List arr = (List) HTMLWorker.parseToList(reader, null, null);
//                        Phrase phrase = new Phrase("");
//                        for (int i = 0; i < arr.size(); i++) {
//                            Element element = (Element) arr.get(i);
//                            phrase.add(element);
//                        }
//                        cell = new PdfPCell(phrase);
//                        cell.setColspan(2);
//                        total_table.addCell(cell);
//                    }
//                    String comp = "";
//                    comp = "For M/s. " + unescapehtml(crs.getString("branch_invoice_name"));
//                    comp += "\n" + "";
//                    comp += "\n" + "";
//                    comp += "\n" + "";
//                    comp += "\n" + "";
//                    comp += "\n" + "Authorized Signatory";
//                    cell = new PdfPCell(new Phrase(comp, normal_font));
//                    total_table.addCell(cell);
//                    cell = new PdfPCell(new Phrase(""));
//                    total_table.addCell(cell);
//
//                    document.add(total_table);
//                }
//                document.close();
//            } else {
//                response.sendRedirect("../portal/error.jsp?msg=Invalid Delivery Note!");
//            }
//            crs.close();
//        } catch (Exception ex) {
//            SOPError("Axelaauto===" + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//    }
//}
