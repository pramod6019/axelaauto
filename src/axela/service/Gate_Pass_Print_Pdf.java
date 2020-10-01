package axela.service;

//SATISH 12-MARCH-2013
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class Gate_Pass_Print_Pdf extends Connect {

	public String jc_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD | Font.UNDERLINE);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font underline_font = FontFactory.getFont("Helvetica", 10, Font.UNDERLINE);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	public String veh_chassis_no = "0";
	public String veh_engine_no = "0";
	public String comp_id = "0";
	public String veh_reg_no = "0";
	public String veh_sale_date = "";
	public String delivery_time = "";
	public String veh_kms = "0";
	public String customer_name = "";
	public String address = "";
	public String city = "";
	public String emp_name = "";
	public String model_name = "";

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
					GatePassDetails(request, response, jc_id, BranchAccess, ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void GatePassDetails(HttpServletRequest request, HttpServletResponse response, String jc_id, String BranchAccess, String ExeAccess, String purpose) throws IOException, DocumentException {
		// StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jc_time_in, jc_id, jc_kms, jc_title, jc_time_out,"
					+ " CONCAT(contact_fname, '/', contact_lname) AS contact_name,"
					+ " jc_emp_id, jc_location_id, CONCAT(emp_name, '-', jobtitle_desc) AS emp_name,"
					+ " branch_logo, comp_name, customer_name, model_name,"
					+ " veh_chassis_no, veh_engine_no, veh_reg_no,"
					+ " veh_sale_date, city_name, customer_address, veh_kms"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE jc_id = " + jc_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY jc_id"
					+ " ORDER BY jc_id desc";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "JobCard_" + jc_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_engine_no = crs.getString("veh_engine_no");
					veh_reg_no = crs.getString("veh_reg_no");
					if (!crs.getString("jc_time_out").equals("")) {
						veh_sale_date = strToShortDate(crs.getString("jc_time_out"));
						delivery_time = SplitHourMin(crs.getString("jc_time_out"));
					}
					veh_kms = crs.getString("veh_kms");
					customer_name = crs.getString("customer_name");
					address = crs.getString("customer_address");
					city = crs.getString("city_name");
					emp_name = crs.getString("emp_name");
					model_name = crs.getString("model_name");
					String s = delivery_time;
					Date d = null;
					SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
					if (!crs.getString("jc_time_out").equals("")) {
						try {
							d = sdf.parse(s);
							SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
							delivery_time = sdf1.format(d).toUpperCase();
						} catch (Exception ex) {
							SOPError("Axelaauto== " + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						}
					}

					PdfPTable top_table = new PdfPTable(4);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);

					PdfPCell cell;

					cell = new PdfPCell();
					cell.setColspan(2);
					if (!crs.getString("branch_logo").equals("")) {
						Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + unescapehtml(crs.getString("branch_logo")));
						cell.addElement(new Chunk(branch_logo, 0, 0));
						cell.setFixedHeight(branch_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}

					cell.setPaddingLeft(0);
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Gate Pass", header_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setColspan(2);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Sale Date:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorder(0);
					top_table.addCell(cell);
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(veh_sale_date), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("           Delivery Time:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorder(0);
					top_table.addCell(cell);
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(delivery_time), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Customer Name:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(customer_name), normal_font));
					cell.setColspan(3);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);

					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Address:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(address), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(3);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("City :", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(city), normal_font));
					cell.setColspan(3);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Kms:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(veh_kms), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Model:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(model_name), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Reg. No.:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(veh_reg_no), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Chassis Number:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(veh_chassis_no), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Engine No.:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(veh_engine_no), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Sales Manager:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(emp_name), normal_font));
					cell.setColspan(3);
					cell.setFixedHeight(40);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Customer Signature:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("          Prepared by:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(40);
					cell.setBorder(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(35);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					top_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Customer Name:", normal_font));
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
					cell.addElement(new Phrase("          Customer Signature:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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

					cell = new PdfPCell(new Phrase("  ", normal_font));
					cell.setBorder(0);
					cell.setColspan(4);
					top_table.addCell(cell);

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
					cell.addElement(new Phrase("          Authorised Person\n          Signature:", normal_font));
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
					cell.addElement(new Phrase("          Security Person\n          Signature:", normal_font));
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

					document.add(top_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Gate Pass!");
				// Str.append("Invalid Gatepass!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
