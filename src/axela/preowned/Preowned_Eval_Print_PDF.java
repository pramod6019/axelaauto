package axela.preowned;
//Sangita 19th july 2013

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
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

public class Preowned_Eval_Print_PDF extends Connect {

	public String eval_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess = "";
	public String ExeAccess = "";
	PdfPTable vehicle_table;
	PdfPTable accessory_table;
	PdfPTable evalhead_table;
	PdfPTable signature_table;
	PdfWriter writer;
	Font header_font = FontFactory.getFont("Helvetica", 11, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font strong_font = FontFactory.getFont("Helvetica", 7, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);
	Font abnormal_font = FontFactory.getFont("Helvetica", 7);

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			SOP("ist bdhsbh");
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_eval_access", request, response);
				eval_id = CNumeric(PadQuotes(request.getParameter("eval_id")));
				if (ReturnPerm(comp_id, "emp_preowned_eval_access", request).equals("1")) {
					EvalDetails(request, response, eval_id, BranchAccess, ExeAccess, "pdf");
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void EvalDetails(HttpServletRequest request, HttpServletResponse response, String eval_id, String BranchAccess, String ExeAccess, String purpose) throws IOException, DocumentException {
		try {
			StrSql = "SELECT eval_date, eval_id, "
					+ " COALESCE(p.emp_name,'')AS emp_name,"
					+ " COALESCE(e.emp_name,'')AS emp_name1,"
					+ " COALESCE(branch_logo,'')AS branch_logo,"
					+ " preownedmodel_name, variant_name, preowned_manufyear,"
					+ " preowned_regdyear, preowned_regno, fueltype_name,"
					+ " preowned_sub_variant, variant_name, ownership_name,"
					+ " IF(eval_acc_stereo = 1, 'Yes', 'No') AS eval_acc_stereo,"
					+ " IF(eval_acc_powersteering = 1, 'Yes', 'No') AS eval_acc_powersteering,"
					+ " IF(eval_acc_powerwindows = 1, 'Yes', 'No') AS eval_acc_powerwindows,"
					+ " IF(eval_acc_centrallocking = 1, 'Yes', 'No') AS eval_acc_centrallocking,"
					+ " IF(eval_acc_alloywheels = 1, 'Yes', 'No') AS eval_acc_alloywheels,"
					+ " IF(eval_acc_keys = 1, 'Yes', 'No') AS eval_acc_keys,"
					+ " IF(eval_acc_toolkit = 1, 'Yes', 'No') AS eval_acc_toolkit,"
					+ " IF(eval_acc_parkingsensor = 1, 'Yes', 'No') AS eval_acc_parkingsensor,"
					+ " IF(eval_acc_others = 1, 'Yes', 'No') AS eval_acc_others,"
					+ " IF(eval_acc_stereo_make != '', CONCAT(' (', eval_acc_stereo_make, ')'), eval_acc_stereo_make) AS eval_acc_stereo_make,"
					+ " IF(eval_acc_powersteering_make != '', CONCAT(' (', eval_acc_powersteering_make, ')'), eval_acc_powersteering_make) AS eval_acc_powersteering_make,"
					+ " IF(eval_acc_powerwindows_make != '', CONCAT(' (', eval_acc_powerwindows_make, ')'), eval_acc_powerwindows_make) AS eval_acc_powerwindows_make,"
					+ " IF(eval_acc_centrallocking_make != '', CONCAT(' (', eval_acc_centrallocking_make, ')'), eval_acc_centrallocking_make) AS eval_acc_centrallocking_make,"
					+ " IF(eval_acc_alloywheels_make != '', CONCAT(' (', eval_acc_alloywheels_make, ')'), eval_acc_alloywheels_make) AS eval_acc_alloywheels_make,"
					+ " IF(eval_acc_keys_make != '', CONCAT(' (', eval_acc_keys_make, ')'), eval_acc_keys_make) AS eval_acc_keys_make,"
					+ " IF(eval_acc_toolkit_make != '', CONCAT(' (', eval_acc_toolkit_make, ')'), eval_acc_toolkit_make) AS eval_acc_toolkit_make,"
					+ " IF(eval_acc_parkingsensor_make != '', CONCAT(' (', eval_acc_parkingsensor_make, ')'), eval_acc_parkingsensor_make) AS eval_acc_parkingsensor_make,"
					+ " IF(eval_acc_others_make != '', CONCAT(' (', eval_acc_others_make, ')'), eval_acc_others_make) AS eval_acc_others_make,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, contact_phone1,"
					+ " contact_mobile1, contact_email1, contact_address, contact_pin, city_name, eval_rf_total,"
					+ " eval_offered_price, COALESCE((SELECT img_value FROM " + compdb(comp_id) + "axela_preowned_img"
					+ " WHERE img_preowned_id = preowned_id"
					+ " ORDER BY img_id LIMIT 1), '') AS img_value"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = preowned_fueltype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership ON ownership_id = preowned_ownership_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = eval_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp p ON p.emp_id = preowned_emp_id"
					+ " WHERE eval_id = " + eval_id + BranchAccess + ExeAccess.replace("emp_id", "eval_emp_id") + ""
					+ " GROUP BY eval_id"
					+ " ORDER BY eval_id DESC";
			// SOP("StrSql----=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				document.setMargins(25, 25, 25, 25);
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Eval_" + eval_id + ".pdf"));
				}

				document.open();
				SOP("1111111");
				while (crs.next()) {
					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);
					PdfPCell cell;

					cell = new PdfPCell();
					/*
					 * if (!crs.getString("branch_logo").equals("")) { Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo")); cell.addElement(new
					 * Chunk(branch_logo, 0, 0)); cell.setFixedHeight(branch_logo.getHeight()); } else { cell.addElement(new Phrase("")); }
					 */
					SOP("aaaaaaa");
					cell.setPaddingLeft(0);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.1f);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Vehicle Evaluation Sheet", header_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.1f);
					top_table.addCell(cell);

					document.add(top_table);

					PdfPTable table = new PdfPTable(2);
					table.setWidthPercentage(100);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Evaluator Name: " + crs.getString("emp_name1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);
					SOP("bbbbb");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Date: " + strToShortDate(crs.getString("eval_date")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);
					SOP("ccccccc");
					cell = new PdfPCell();
					cell.addElement(new Phrase("New Car Sales Consultant Name: " + crs.getString("emp_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);
					SOP("dddddd");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Evaluation ID: " + crs.getString("eval_id"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);
					SOP("eeeeee");
					document.add(table);

					vehicle_table = new PdfPTable(4);
					vehicle_table.setWidthPercentage(100);
					SOP("22222");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Owner Details", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					vehicle_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Vehicle Details", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					vehicle_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Name: " + crs.getString("contact_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setColspan(2);
					vehicle_table.addCell(cell);
					SOP("33333");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Make/ Model: " + crs.getString("preownedmodel_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("44444");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Variant: " + crs.getString("variant_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("5555555");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Address: " + crs.getString("contact_address")
							+ ", " + crs.getString("city_name") + " - "
							+ crs.getString("contact_pin"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setColspan(2);
					cell.setRowspan(2);
					vehicle_table.addCell(cell);
					SOP("66666666");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Reg. No.: " + crs.getString("preowned_regno"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("7777777");
					cell = new PdfPCell();
					cell.addElement(new Phrase(crs.getString("preowned_sub_variant"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("88888");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Year of Manuf.: " + crs.getString("preowned_manufyear"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("99999");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Year of Reg.: " + crs.getString("preowned_regdyear"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("ten");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Mobile No.: " + crs.getString("contact_mobile1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setColspan(2);
					vehicle_table.addCell(cell);
					SOP("eleven");
					cell = new PdfPCell();
					cell.addElement(new Phrase("No. of Ownecrs.: " + crs.getString("ownership_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("twele");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Fuel Type: " + crs.getString("fueltype_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);
					SOP("131333");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Phone No.: " + crs.getString("contact_phone1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setColspan(2);
					vehicle_table.addCell(cell);
					SOP("1414");
					cell = new PdfPCell();
					cell.addElement(new Phrase("Engine no.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Chassis no. (Match with RC).: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					vehicle_table.addCell(cell);

					document.add(vehicle_table);

					accessory_table = new PdfPTable(2);
					accessory_table.setWidthPercentage(100);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Accessories/ Standard Feature Available", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					accessory_table.addCell(cell);
					SOP("1515");
					cell = new PdfPCell(new Phrase(""));
					cell.setRowspan(10);

					if (!crs.getString("img_value").equals("")) {
						java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(PreownedImgPath(comp_id) + crs.getString("img_value"));
						Image image = Image.getInstance(writer, awtImage, 0.5f);

						int targetHeight = (int) image.getHeight();
						int targetWidth = (int) image.getWidth();

						// cell height 153
						if (targetHeight > 153) {
							targetWidth = 153 * targetWidth / targetHeight;
							targetHeight = 153;
						}
						image.scaleAbsolute(targetWidth, targetHeight);
						cell.addElement(new Chunk(image, 00, -137));
					} else {
						cell.addElement(new Phrase(""));
					}
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Stereo / radio: " + crs.getString("eval_acc_stereo")
							+ crs.getString("eval_acc_stereo_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Power Steering: " + crs.getString("eval_acc_powersteering")
							+ crs.getString("eval_acc_powersteering_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Power Windows: " + crs.getString("eval_acc_powerwindows")
							+ crs.getString("eval_acc_powerwindows_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Central Locking (Auto/Manual): " + crs.getString("eval_acc_centrallocking")
							+ crs.getString("eval_acc_centrallocking_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Alloy Wheels: " + crs.getString("eval_acc_alloywheels")
							+ crs.getString("eval_acc_alloywheels_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("No. of Keys (Original): " + crs.getString("eval_acc_keys")
							+ crs.getString("eval_acc_keys_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Tool Kit/ Jack: " + crs.getString("eval_acc_toolkit")
							+ crs.getString("eval_acc_toolkit_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Reverse Parking Sensor: " + crs.getString("eval_acc_parkingsensor")
							+ crs.getString("eval_acc_parkingsensor_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Others (If any): " + crs.getString("eval_acc_others")
							+ crs.getString("eval_acc_others_make"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					accessory_table.addCell(cell);

					document.add(accessory_table);
					SOP("1616");
					EvalHead();
					document.add(evalhead_table);

					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Price Offered: " + (IndFormat(crs.getString("eval_offered_price"))) + "/-", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(30);
					total_table.addCell(cell);
					cell = new PdfPCell();
					cell.addElement(new Phrase("Total RF Cost (including General Service & Cleaning): "
							+ (IndFormat(crs.getString("eval_rf_total"))) + "/-", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(30);
					total_table.addCell(cell);
					SOP("174");
					cell = new PdfPCell();
					cell.setColspan(2);
					cell.addElement(new Phrase("Terms & Conditions:", strong_font));
					cell.addElement(new Phrase("1. The given quote stands valid only for 24 hours from the date of issue.", abnormal_font));
					cell.addElement(new Phrase("2. This is an approximate quote; the final value of the car may change depending on the physical condition of the car.", abnormal_font));
					cell.addElement(new Phrase("3. This quote of the car is given assuming the car is non-accidental.", abnormal_font));
					cell.addElement(new Phrase("4. A final inspection of the car would be done before trading the car at proposed value.", abnormal_font));
					total_table.addCell(cell);

					document.add(total_table);

					signature_table = new PdfPTable(3);
					signature_table.setWidthPercentage(100);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Evaluator Name / Sign", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(70);
					signature_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Used Car Head Name / Sign", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(70);
					signature_table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Sales Manager Name / Sign", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(70);
					signature_table.addCell(cell);
					SOP("1717");
					document.add(signature_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Evaluation!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void EvalHead() {
		String head = "", subhead = "", status = "";
		StringBuilder Str = new StringBuilder();
		String evaltrans_rf_amt = "0", evaltrans_observation = "";
		StringBuilder Stramt = new StringBuilder();
		int countamt = 0, countsubhead = 0, i = 0, j = 1;
		PdfPCell cell;
		try {
			StrSql = "SELECT evalhead_name, evalsubhead_name,evaldetails_name, evaldetails_active,"
					+ " COALESCE(evaltrans_observation, '') AS evaltrans_observation,"
					+ " COALESCE(evaltrans_rf_amt, '') AS evaltrans_rf_amt,"
					+ " (SELECT COUNT(evaldetails_id) FROM " + compdb(comp_id) + "axela_preowned_eval_details detail"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_subhead sub ON sub.evalsubhead_id = detail.evaldetails_evalsubhead_id"
					+ " WHERE sub.evalsubhead_evalhead_id = evalhead_id"
					+ " LIMIT 1) AS headcount,"
					+ " (SELECT COUNT(evaldetails_id) FROM " + compdb(comp_id) + "axela_preowned_eval_details detail"
					+ " WHERE detail.evaldetails_evalsubhead_id = evalsubhead_id"
					+ " LIMIT 1) AS subheadcount,"
					+ " COALESCE(evaldetailstrans_status, 0) AS evaldetailstrans_status"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval_head"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_subhead ON evalsubhead_evalhead_id = evalhead_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_details ON evaldetails_evalsubhead_id = evalsubhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_trans ON evaltrans_evalhead_id = evalhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_id = evaltrans_eval_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_details_trans ON evaldetailstrans_evaldetails_id = evaldetails_id"
					+ " AND evaldetailstrans_eval_id = eval_id"
					+ " WHERE evaldetails_active = '1'"
					+ " AND eval_id = " + eval_id + ""
					+ " GROUP BY evalhead_id, evalsubhead_id, evaldetails_id"
					+ " ORDER BY evalhead_rank, evalsubhead_rank, evaldetails_rank";
			SOP("EvalHead==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				evalhead_table = new PdfPTable(5);
				evalhead_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				evalhead_table.setWidthPercentage(100);
				evalhead_table.setWidths(new int[]{20, 57, 5, 10, 8});

				while (crs.next()) {
					SOP("inside 13333");
					if (!subhead.equals(crs.getString("evalsubhead_name"))) {
						String str = Stramt.toString().replace("[SHROWSPAN]", countsubhead + "");
						Stramt.setLength(0);
						Stramt.append(str);
						countsubhead = 0;
					}
					SOP("inside sdd");
					if (!head.equals(crs.getString("evalhead_name"))) {
						if (Stramt.length() > 0) {
							Str.append(Stramt.toString().replace("[ROWSPAN]", countamt + ""));
							Stramt.setLength(0);
							countamt = 0;
						}

						if (i == 0) {
							cell = new PdfPCell(new Phrase(crs.getString("evalhead_name"), strong_font));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
							cell.setColspan(2);
							cell.setNoWrap(true);
							evalhead_table.addCell(cell);

							cell = new PdfPCell();
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.addElement(new Phrase("Status", strong_font));
							evalhead_table.addCell(cell);

							cell = new PdfPCell();
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.addElement(new Phrase("Observations", strong_font));
							evalhead_table.addCell(cell);

							cell = new PdfPCell();
							cell.addElement(new Phrase("RF Estimate", strong_font));
							cell.setNoWrap(true);
							evalhead_table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase(crs.getString("evalhead_name"), strong_font));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setColspan(5);
							cell.setNoWrap(true);
							evalhead_table.addCell(cell);
						}
						i++;
						j = 1;
						head = crs.getString("evalhead_name");
					}

					if (!subhead.equals(crs.getString("evalsubhead_name"))) {
						Stramt.append("<td rowspan=[SHROWSPAN]><b>").append(crs.getString("evalsubhead_name")).append("</b></td>\n");
						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("evalsubhead_name")), abnormal_font));
						cell.setRowspan(crs.getInt("subheadcount"));
						evalhead_table.addCell(cell);

						subhead = crs.getString("evalsubhead_name");
					}

					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("evaldetails_name")), abnormal_font));
					evalhead_table.addCell(cell);

					if (crs.getString("evaldetailstrans_status").equals("1")) {
						status = "Ok";
					} else if (crs.getString("evaldetailstrans_status").equals("2")) {
						status = "Not Ok";
					} else {
						status = "";
					}

					cell = new PdfPCell(new Phrase(status, abnormal_font));
					evalhead_table.addCell(cell);

					if (countamt == 0) {
						evaltrans_rf_amt = crs.getString("evaltrans_rf_amt");
						evaltrans_observation = crs.getString("evaltrans_observation");
						cell = new PdfPCell(new Phrase(evaltrans_observation, abnormal_font));
						cell.setRowspan(crs.getInt("headcount"));
						evalhead_table.addCell(cell);

						cell = new PdfPCell(new Phrase(IndFormat(evaltrans_rf_amt), abnormal_font));
						cell.setRowspan(crs.getInt("headcount"));
						evalhead_table.addCell(cell);
					}
					countamt++;
					countsubhead++;
					j++;
				}
				String str = Stramt.toString().replace("[SHROWSPAN]", countsubhead + "");
				Stramt.setLength(0);
				Stramt.append(str);
				Str.append(Stramt.toString().replace("[ROWSPAN]", countamt + ""));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
