package axela.service;

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

public class JobCard_PSF_Print extends Connect {

	public String jcpsf_id = "0";
	public String so_id = "0";
	public String StrSql = "";
	public String StrHTML = "", salutation = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";
	public String grandtotal = "";
	public String comp_id = "0";
	public String emp_id = "0";
	PdfPTable item_table;
	PdfPTable config_table;
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 9, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 9);
	public String quoteitem_rowcount = "0", quote_exprice = "";
	public String item_taxcal = "0";
	public int count1 = 0;
	public String quoteitem_option_group = "";
	public String group_name = "";
	public double quote_grandtotal = 0.0;
	public double quote_netamt = 0.0;
	public double quoteitem_price = 0.0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				jcpsf_id = CNumeric(PadQuotes(request.getParameter("jcpsf_id")));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				if (ReturnPerm(comp_id, "emp_enquiry_access", request).equals("1")) {
					FollowupDetails(request, response, so_id, BranchAccess,
							ExeAccess.replace("emp_id", "so_emp_id"), comp_id,
							"pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void FollowupDetails(HttpServletRequest request,
			HttpServletResponse response, String so_id, String BranchAccess,
			String ExeAccess, String comp_id, String purpose)
			throws IOException, DocumentException {
		try {
			String script = "";

			script = "replace(psfdays_script, '[SALUTATION]'," + "'"
					+ GetSalutation(ToLongDate(kknow())) + "')";
			script = "replace("
					+ script
					+ ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
			script = "replace(" + script
					+ ",'[EXENAME]',jcexe.emp_name) as psfdays_script";
			StrSql = "SELECT " + script + " ,"
					+ " jcpsf_jc_id, jc_branch_id, COALESCE (branch_ticket_email, '') AS branch_ticket_email,"
					+ " jcpsf_desc, psfdays_daycount, psfdays_desc,	jcpsf_psffeedbacktype_id,"
					+ " jcpsf_satisfied, COALESCE (entryemp.emp_id, 0) AS crmentryid,"
					+ " COALESCE (entryemp.emp_name, '') AS crmentryname,"
					+ " COALESCE (jcpsf_entry_time, '') AS jcpsf_entry_time,"
					+ " COALESCE (modifiedemp.emp_id, 0) AS crmmodifiedid,"
					+ " COALESCE (modifiedemp.emp_name, '') AS modifiedname,"
					+ " COALESCE (jcpsf_modified_time, '') AS jcpsf_modified_time, jcpsf_psfdays_id,"
					+ " customer_id, customer_name,	contact_id,"
					+ " concat(title_desc,' ',contact_fname, ' ', contact_lname	) AS contact_name,"
					+ " contact_address, title_gender, contact_mobile1,	contact_mobile2,"
					+ " contact_email1,	contact_email2,	contact_phone1,	contact_phone2,"
					+ " jc_id, variant_name,"
					+ " COALESCE (jcexe .emp_name,'') AS jcexe_name,"
					+ " COALESCE (jcexe .emp_email1,'') AS exe_email1,"
					+ " COALESCE (jcpsfexe .emp_name,'') AS jcpsfexe_name,"
					+ " branch_name, branch_ticket_email, jcpsf_ticket_emp_id, jcpsf_jcpsfconcern_id, psffeedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					// + " INNER JOIN " + compdb(comp_id)+
					// "axela_emp SESSION ON SESSION.emp_id = 1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp jcexe ON jcexe.emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp jcpsfexe ON jcpsfexe.emp_id = jcpsf_emp_id"
					+ " INNER JOIN axela_service_psf_feedbacktype ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
					+ " WHERE jcpsf_id = " + jcpsf_id
					+ " GROUP BY"
					+ " jcpsf_id";

			// SOP("StrSql--CRM Print-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql ===rs print===" + rs);
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
							CachePath(comp_id) + "So_" + so_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					if (crs.getString("title_gender").equals("1")) {
						salutation = "Sir";
					} else {
						salutation = "Madam";
					}
					// PdfPTable logo_table = new PdfPTable(1);
					// logo_table.setWidthPercentage(100);
					PdfPCell cell;
					// Paragraph p = new Paragraph();
					// cell = new PdfPCell();
					// if (!crs.getString("branch_logo").equals("")) {
					// Image branch_logo = Image.getInstance(BranchLogoPath() +
					// crs.getString("branch_logo"));
					// branch_logo.setAlignment(Image.ALIGN_CENTER |
					// Image.TEXTWRAP);
					// branch_logo.setBorder(branch_logo.BOX);
					// p.add(branch_logo);
					// } else {
					// p.add("");
					// }
					// document.add(p);
					SOP("coming..1");
					PdfPTable space_table = new PdfPTable(2);
					space_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					space_table.addCell(cell);
					document.add(space_table);

					PdfPTable sodetails_table = new PdfPTable(4);
					sodetails_table.setWidthPercentage(100);
					sodetails_table.setKeepTogether(true);

					cell = new PdfPCell(new Phrase("PSF FOLLOW UP", header_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("JC ID.: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("jc_id"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Branch Name: ", normal_font));
					// cell.addElement(new Phrase("Branch Name: ",
					// normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("branch_name"),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Customer Name: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("customer_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Contact Name: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Mobile 1: ", normal_font));
					// cell.addElement(new Phrase("Mobile 1: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile1"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Mobile 2: ", normal_font));
					// cell.addElement(new Phrase("Mobile 2: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile2"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Email 1: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_email1"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Email 2: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_email2"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Phone 1: ", normal_font));
					// cell.addElement(new Phrase("Phone 1: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_phone1"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Phone 2: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_phone2"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					// cell = new PdfPCell(new Phrase("Colour: ", normal_font));
					// // cell.addElement(new Phrase("Colour: ", normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					// sodetails_table.addCell(cell);
					// cell = new PdfPCell(new
					// Phrase(crs.getString("option_desc"),
					// normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// sodetails_table.addCell(cell);
					SOP("coming..2");
					cell = new PdfPCell(new Phrase("Variant: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("variant_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					SOP("coming..3");
					cell = new PdfPCell(new Phrase(" Executive: ", normal_font));
					// cell.addElement(new Phrase(" Executive: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("jcpsfexe_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					SOP("coming..3");
					cell = new PdfPCell(new Phrase(" Service Advisior: ", normal_font));
					// cell.addElement(new Phrase(" Executive: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("jcexe_name"),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					SOP("coming..3");
					document.add(sodetails_table);

					PdfPTable script_table = new PdfPTable(1);
					script_table.setWidthPercentage(100);
					script_table.setKeepTogether(true);

					StringBuilder PbfStr = new StringBuilder();
					PbfStr.append(crs.getString("psfdays_script"));
					cell = new PdfPCell(new Phrase(PbfStr.toString(),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					script_table.addCell(cell);
					SOP("coming..3");
					document.add(script_table);
					int count = 0;
					String StrSql = "SELECT jcpsfcf_title,"
							+ " COALESCE(jcpsfcftrans_value, '') AS jcpsfcftrans_value,"
							+ " COALESCE(jcpsfcftrans_voc, '') AS jcpsfcftrans_voc, jcpsfcf_cftype_id"
							+ " FROM "
							+ compdb(comp_id)
							+ "axela_service_jc_psf"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
							+ " LEFT JOIN "
							+ compdb(comp_id)
							+ "axela_service_jc_psf_trans ON jcpsfcftrans_jcpsfcf_id = jcpsfcf_id "
							+ "AND jcpsfcftrans_jcpsf_id = " + jcpsf_id + ""
							+ " WHERE jcpsfcf_active = 1"
							+ " AND jcpsf_id = " + jcpsf_id + ""
							+ " GROUP BY jcpsfcf_id" + " ORDER BY jcpsfcf_rank";
					CachedRowSet crs1 = processQuery(StrSql, 0);
					// SOP("StrSql--CRM Custom Fields Print-----"
					// + StrSqlBreaker(StrSql));
					PdfPTable questions_table = new PdfPTable(4);
					questions_table.setWidthPercentage(100);
					questions_table.setKeepTogether(true);
					questions_table.setWidths(new int[]{2, 40, 10, 15});
					if (crs1.isBeforeFirst()) {
						cell = new PdfPCell(new Phrase("#", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("Questions", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("Yes/No", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("VOC", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						while (crs1.next()) {
							count++;
							cell = new PdfPCell(new Phrase(count + "",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							questions_table.addCell(cell);

							cell = new PdfPCell(new Phrase(
									crs1.getString("jcpsfcf_title"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);
							if (crs1.getString("jcpsfcf_cftype_id").equals("5")
									|| crs1.getString("jcpsfcf_cftype_id").equals(
											"6")) {
								cell = new PdfPCell(
										new Phrase(
												strToShortDate(crs1
														.getString("jcpsfcftrans_value")),
												normal_font));
							} else {
								cell = new PdfPCell(new Phrase(
										crs1.getString("jcpsfcftrans_value"),
										normal_font));
							}
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);

							cell = new PdfPCell(new Phrase(
									crs1.getString("jcpsfcftrans_voc"),
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);

						}
						crs1.close();

					}
					SOP("coming..4");
					document.add(questions_table);
					if (!crs.getString("psffeedbacktype_name").equals("")
							&& !crs.getString("jcpsf_desc").equals("")) {
						// feedback table
						PdfPTable feedback_table = new PdfPTable(2);
						feedback_table.setWidthPercentage(100);
						feedback_table.setKeepTogether(true);
						feedback_table.setWidths(new int[]{7, 40});

						cell = new PdfPCell(new Phrase("Feedback", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								crs.getString("jcpsf_desc"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Feedback Type",
								bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								crs.getString("psffeedbacktype_name"),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						document.add(feedback_table);
					}

					PdfPTable sign_table = new PdfPTable(2);
					sign_table.setWidthPercentage(100);
					sign_table.setKeepTogether(true);
					StringBuilder signStr = new StringBuilder();

					signStr.append("1.Please Bring Your Docket at the time of Delivery.\n\n");

					signStr.append("2.Thank you very much for giving your valuable time. We are agreeing to see you soon on the day of delivery..\n\n");

					signStr.append("3.We assure you of our committed services in the future.\n\n");
					cell = new PdfPCell(new Phrase(signStr.toString(),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("(GM Signature)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(50);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("(CCM Signature)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(50);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("\n\nRegards", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("\nCRM DEPT", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					document.add(sign_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	// public void ItemDetails(String quote_id) throws DocumentException {
	// int count = 0;
	// int colspan = 0;
	// String configured_item = "";
	// double ex_price = 0.00;
	// item_table = new PdfPTable(3);
	// item_table.setWidths(new int[] { 2, 50, 6 });
	//
	// config_table = new PdfPTable(2);
	// config_table.setWidths(new int[] { 50, 6 });
	//
	// item_table.setWidthPercentage(100);
	// config_table.setWidthPercentage(100);
	// try {
	// PdfPCell cell;
	// StrSql =
	// "SELECT item_name, item_code, item_small_desc, item_aftertaxcal, quoteitem_rowcount,"
	// +
	// " quoteitem_qty, uom_shortname, quoteitem_price, quoteitem_disc, quoteitem_total,"
	// +
	// " quoteitem_option_group, quoteitem_option_id, quoteitem_tax, quoteitem_option_group_tax,"
	// +
	// " COALESCE((SELECT GROUP_CONCAT(CONCAT(opt.quoteitem_option_group, ': ', optitem.item_name,"
	// +
	// " IF(optitem.item_code != '', CONCAT(' (', optitem.item_code, ')'), ''),"
	// + " CONCAT(' Qty: ', opt.quoteitem_qty),"
	// +
	// " IF(optitem.item_small_desc != '', CONCAT('\n', optitem.item_small_desc), ''))"
	// + " ORDER BY opt.quoteitem_id ASC SEPARATOR '\n\n')"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote_item opt"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item optitem ON optitem.item_id = opt.quoteitem_item_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item_option ON option_item_id = opt.quoteitem_item_id"
	// + " WHERE opt.quoteitem_quote_id = invitem.quoteitem_quote_id"
	// +
	// " AND opt.quoteitem_option_id = invitem.quoteitem_rowcount), '') AS optionitems"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item item ON item.item_id = invitem.quoteitem_item_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_uom ON uom_id = item.item_uom_id"
	// + " WHERE invitem.quoteitem_quote_id = "
	// + quote_id
	// + ""
	// + " GROUP BY quoteitem_id"
	// + " ORDER BY quoteitem_option_group_tax, quoteitem_id";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// cell = new PdfPCell(new Phrase("#", bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase("Item", bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase("Amount", bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	// item_table.addCell(cell);
	//
	// count++;
	// cell = new PdfPCell(new Phrase(count + "", normal_font));
	// cell.setNoWrap(true);
	// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	// item_table.addCell(cell);
	//
	// while (crs.next()) {
	// quoteitem_rowcount = crs.getString("quoteitem_rowcount");
	// item_taxcal = crs.getString("quoteitem_option_group_tax");
	// quoteitem_option_group = crs.getString("quoteitem_option_group");
	// quoteitem_price = crs.getDouble("quoteitem_price");
	//
	// String item_name = crs.getString("item_name");
	// if (!crs.getString("item_small_desc").equals("")) {
	// item_name += "\n" + crs.getString("item_small_desc");
	// }
	// item_name += "\n\n";
	//
	// if (!quoteitem_rowcount.equals("0")) { // MAIN ITEM
	// ex_price = crs.getDouble("quoteitem_price");
	// cell = new PdfPCell(new Phrase(unescapehtml(item_name),
	// normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(
	// IndFormat(crs.getString("quoteitem_price")),
	// normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	// } else {
	// // GROUP_NAME
	// configured_item = quoteitem_option_group;
	// if (item_taxcal.equals("1")) {
	// if (!crs.getString("quoteitem_option_id").equals("0")
	// && !group_name.equals(quoteitem_option_group)) {
	// group_name = quoteitem_option_group;
	// }
	//
	// // ITEM_NAME
	// ex_price += crs.getDouble("quoteitem_price");
	// cell = new PdfPCell(new Phrase(unescapehtml(item_name),
	// normal_font));
	// config_table.addCell(cell);
	//
	// if (crs.getString("quoteitem_option_group").equals(
	// "Additional Discounts")) {
	// cell = new PdfPCell(new Phrase(
	// IndFormat(crs.getString("quoteitem_disc")),
	// normal_font));
	// } else {
	// cell = new PdfPCell(new Phrase(
	// IndFormat(crs.getString("quoteitem_price")),
	// normal_font));
	// }
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// config_table.addCell(cell);
	// }
	//
	// colspan = 2;
	// if (item_taxcal.equals("2") && count == 1) {
	// count1 = count;
	// cell = new PdfPCell(new Phrase("Ex-Showroom Price: ",
	// bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// cell.setColspan(colspan - 1);
	// config_table.addCell(cell);
	//
	// cell = new PdfPCell(
	// new Phrase(
	// IndFormat(Double.toString(ex_price)),
	// bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// config_table.addCell(cell);
	// count++;
	// }
	//
	// if (item_taxcal.equals("2")) { // AFTER TAX
	// if (!crs.getString("quoteitem_option_id").equals("0")
	// && !group_name.equals(quoteitem_option_group)) {
	// group_name = quoteitem_option_group;
	// }
	// // ITEM_NAME
	// cell = new PdfPCell(new Phrase(unescapehtml(item_name),
	// normal_font));
	// cell.setNoWrap(true);
	// config_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(
	// IndFormat(crs.getString("quoteitem_price")),
	// normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// config_table.addCell(cell);
	// }
	// }
	// }
	// crs.close();
	// if (count == 1) {
	// cell = new PdfPCell(new Phrase(""));
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(
	// new Phrase("Ex-Showroom Price: ", bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(
	// IndFormat(Double.toString(ex_price)), bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	// }
	//
	// if (!configured_item.equals("")) {
	// cell = new PdfPCell(new Phrase(""));
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(""));
	// cell.setPadding(0);
	// cell.addElement(config_table);
	// cell.setColspan(colspan);
	// item_table.addCell(cell);
	// }
	//
	// colspan = 2;
	//
	// cell = new PdfPCell(new Phrase("Total:", normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// cell.setColspan(colspan);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(
	// IndFormat(Double.toString(quote_netamt)), bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	//
	// if (!total_disc.equals("0.00")) {
	// cell = new PdfPCell(new Phrase("Consumer Offer:", normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setColspan(colspan);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(IndFormat(total_disc),
	// normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	// }
	//
	// cell = new PdfPCell(new Phrase("On-Road Price:", bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// cell.setColspan(colspan);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(IndFormat(grandtotal), bold_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	//
	// cell = new PdfPCell(new Phrase(Integer.toString(count1),
	// normal_font));
	// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	// cell.setNoWrap(true);
	// item_table.addCell(cell);
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// }
	// }

	public String GetSalutation(String date) {
		String salutation = "";
		int time = 0;
		time = Integer.parseInt(date.substring(8, 10));
		if (time < 12 && time >= 0) {
			salutation = "Good Morning";
		} else if (time < 16 && time >= 12) {
			salutation = "Good Afternoon";
		} else if (time < 24 && time >= 16) {
			salutation = "Good Evening";
		}
		return salutation;
	}
}
