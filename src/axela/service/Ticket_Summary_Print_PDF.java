package axela.service;
//aJIt

import java.io.IOException;

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

public class Ticket_Summary_Print_PDF extends Connect {

	public String ticket_id = "0";
	public String StrSql = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String followup_data = "";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	PdfPTable followup_table;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// CheckPerm(comp_id, "emp_ticket_access", request, response);
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				// if (ReturnPerm(comp_id, "emp_ticket_access", request).equals("1")) {
				TicketSummaryDetails(request, response);
				// }
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void TicketSummaryDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_ticket.*,"
					+ " COALESCE(contact_id, 0) AS contact_id,"
					+ " COALESCE(customer_id, 0) AS customer_id,"
					+ " COALESCE(concat(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE(contact_mobile2, '') AS contact_mobile2, "
					+ " COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(CONCAT(o.emp_name, ' (', o.emp_ref_no, ')'), '') AS reopen_by,"
					+ " COALESCE(CONCAT(c.emp_name, ' (', c.emp_ref_no, ')'), '') AS closed_by,"
					+ " COALESCE(customer_branch_id, 0) AS customer_branch_id,"
					+ " tickemp.emp_id AS emp_id, e.emp_id AS entryemp_id,"
					+ " CONCAT(e.emp_name, ' (', e.emp_ref_no, ')') AS entryemp_name,"
					+ " CONCAT(tickemp.emp_name, ' (', tickemp.emp_ref_no, ')') AS ticket_emp,"
					+ " COALESCE(veh_reg_no, '') AS veh_reg_no,"
					+ " COALESCE(branch_logo, '') AS branch_logo,"
					+ " ticket_dept_name, priorityticket_name,"
					+ " ticketsource_name, ticketstatus_name, "
					+ " COALESCE(ticketcat_name, '') AS ticketcat_name, "
					+ " COALESCE(tickettype_name, '') AS tickettype_name,"
					+ " COALESCE(veh_id, 0) as veh_id, COALESCE(veh_reg_no, '') as veh_reg_no,"
					+ " COALESCE(veh_chassis_no,'') veh_chassis_no, "
					+ " COALESCE(variant_name, '') as variant_name,"
					+ " COALESCE(jc_id, 0) as jc_id"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON ticket_priorityticket_id = priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_ticket_dept_id = ticket_dept_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticket_ticketstatus_id = ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id=ticket_ticketsource_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp tickemp ON tickemp.emp_id = ticket_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = ticket_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as c ON c.emp_id=ticket_closed_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as o ON o.emp_id=ticket_reopened_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON ticket_tickettype_id = tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id "
					+ BranchAccess + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " WHERE ticket_id = " + ticket_id + ExeAccess.replace("emp_id", "" + compdb(comp_id) + "axela_emp.emp_id");
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				response.setContentType("application/pdf");
				PdfWriter.getInstance(document, response.getOutputStream());
				document.open();

				while (crs.next()) {
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

					cell = new PdfPCell(new Phrase("Ticket Summary", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);
					document.add(top_table);

					PdfPTable table = new PdfPTable(2);
					table.setWidths(new int[]{30, 70});
					table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Ticket ID:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("ticket_id"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);

					if (!crs.getString("ticket_subject").equals("")) {
						cell = new PdfPCell(new Phrase("Subject:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("ticket_subject")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_desc").equals("")) {
						cell = new PdfPCell(new Phrase("Description:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("ticket_desc")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("customer_id").equals("0")) {
						cell = new PdfPCell(new Phrase("Customer:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("customer_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("contact_id").equals("0")) {
						cell = new PdfPCell(new Phrase("Contact:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("contact_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("contact_mobile1").equals("")) {
						cell = new PdfPCell(new Phrase("Mobile1:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("contact_mobile1"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						cell = new PdfPCell(new Phrase("Mobile2:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell();
						cell.addElement(new Phrase(crs.getString("contact_mobile2"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("contact_email1").equals("")) {
						cell = new PdfPCell(new Phrase("Email1:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("contact_email1"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("contact_email2").equals("")) {
						cell = new PdfPCell(new Phrase("Email2:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("contact_email2"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}
					if (!crs.getString("variant_name").equals("")) {
						cell = new PdfPCell(new Phrase("Item:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("variant_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
						if (!crs.getString("veh_reg_no").equals("")) {
							cell = new PdfPCell(new Phrase("Reg. No.:", normal_font));
							cell.setVerticalAlignment(Element.ALIGN_TOP);
							table.addCell(cell);

							cell = new PdfPCell(new Phrase(SplitRegNo(crs.getString("veh_reg_no"), 4), normal_font));
							cell.setVerticalAlignment(Element.ALIGN_TOP);
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase("Chassis No.:", normal_font));
							cell.setVerticalAlignment(Element.ALIGN_TOP);
							table.addCell(cell);

							cell = new PdfPCell(new Phrase(crs.getString("veh_chassis_no"), normal_font));
							cell.setVerticalAlignment(Element.ALIGN_TOP);
							table.addCell(cell);
						}
						cell = new PdfPCell(new Phrase("Vehicle ID:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("veh_id"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}
					if (!crs.getString("jc_id").equals("0")) {
						cell = new PdfPCell(new Phrase("JC ID:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("jc_id"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}
					if (!crs.getString("ticketstatus_name").equals("")) {
						cell = new PdfPCell(new Phrase("Status:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("ticketstatus_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_dept_name").equals("")) {
						cell = new PdfPCell(new Phrase("Department:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("ticket_dept_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_trigger").equals("0")) {
						cell = new PdfPCell(new Phrase("Level:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("ticket_trigger"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("priorityticket_name").equals("")) {
						cell = new PdfPCell(new Phrase("Priority:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("priorityticket_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_report_time").equals("")) {
						cell = new PdfPCell(new Phrase("Report Time:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("ticket_report_time")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_due_time").equals("")) {
						cell = new PdfPCell(new Phrase("Due Time:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("ticket_due_time")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_emp").equals("")) {
						cell = new PdfPCell(new Phrase("Execuitve:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("ticket_emp")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticketcat_name").equals("")) {
						cell = new PdfPCell(new Phrase("Category:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("ticketcat_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("tickettype_name").equals("")) {
						cell = new PdfPCell(new Phrase("Type:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("tickettype_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticketsource_name").equals("")) {
						cell = new PdfPCell(new Phrase("Source:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("ticketsource_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_reopened_comments").equals("")) {
						cell = new PdfPCell(new Phrase("Reopened Comments:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("ticket_reopened_comments")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("reopen_by").equals("")) {
						cell = new PdfPCell(new Phrase("Reopened By:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("reopen_by"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_reopened_time").equals("")) {
						cell = new PdfPCell(new Phrase("Reopened Time:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("ticket_reopened_time")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_closed_comments").equals("")) {
						cell = new PdfPCell(new Phrase("Closed Comments:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("ticket_closed_comments")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("closed_by").equals("")) {
						cell = new PdfPCell(new Phrase("Closed By:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("closed_by"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					if (!crs.getString("ticket_closed_time").equals("")) {
						cell = new PdfPCell(new Phrase("Closed Time:", normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("ticket_closed_time")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}

					ListFollowupData();
					if (!followup_data.equals("")) {
						cell = new PdfPCell(followup_table);
						cell.setColspan(2);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						table.addCell(cell);
					}
					document.add(table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Ticket!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ListFollowupData() {
		StrSql = "SELECT tickettrans_followup, tickettrans_entry_date"
				+ " FROM " + compdb(comp_id) + "axela_service_ticket_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket on ticket_id=tickettrans_ticket_id"
				+ " WHERE tickettrans_ticket_id='" + ticket_id + "'"
				+ " ORDER BY tickettrans_id DESC";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				PdfPCell cell;
				followup_data = "found";
				followup_table = new PdfPTable(2);
				followup_table.setWidths(new int[]{30, 70});
				followup_table.setWidthPercentage(100);

				cell = new PdfPCell(new Phrase("Ticket Follow-up", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setColspan(2);
				followup_table.addCell(cell);
				while (crs.next()) {
					cell = new PdfPCell(new Phrase("Time: " + strToLongDate(crs.getString("tickettrans_entry_date")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					followup_table.addCell(cell);

					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("tickettrans_followup")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					followup_table.addCell(cell);
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
