package axela.service;
//aJIt

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Movement_Print_PDF extends Connect {

	public String StrSql = "", StrSearch = "", order_by = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String branch_id = "0", dr_branch_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String msg = "", button = "";
	public String emp_role_id = "0";
	public String comp_id = "0";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_movement_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				button = PadQuotes(request.getParameter("print_button"));
				start_time = strToShortDate(ToShortDate(kknow()));
				end_time = strToShortDate(ToShortDate(kknow()));
				if (button.equals("Print In") || button.equals("Print Out")) {
					GetValues(request, response);
					CheckForm();
					if (button.equals("Print In")) {
						StrSearch = " AND SUBSTR(vehmove_timein, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ "  AND SUBSTR(vehmove_timein, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
						order_by = " ORDER BY vehmove_timein ASC";
					} else if (button.equals("Print Out")) {
						StrSearch += " AND SUBSTR(vehmove_timeout, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(vehmove_timeout, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
						order_by = " ORDER BY vehmove_timeout ASC";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (ReturnPerm(comp_id, "emp_movement_access", request).equals("1")) {
							MovementDetails(request, response);
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_start_time"));
		endtime = PadQuotes(request.getParameter("txt_end_time"));
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (starttime.equals("")) {
			msg += "<br>Select From Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter valid From Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select To Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>From Date should be less than To date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg += "<br>Enter valid To Date!";
				endtime = "";
			}
		}
	}

	public void MovementDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
		try {
			StrSql = "SELECT vehmove_reg_no, vehmove_timein, vehmove_timeout, branch_name,"
					+ " COALESCE(jc_ro_no, '') AS jc_ro_no, vehmove_branch_id, branch_logo,"
					+ " COALESCE(jc_id, '') AS jc_id, COALESCE(veh_id, '') AS veh_id,"
					+ " IF(vehmove_internal = 1, 'Yes', 'No') AS vehmove_internal,"
					+ " IF(vehmove_timein != '' AND vehmove_timeout != '',"
					+ " DATE_FORMAT(CONCAT('2014-12-22 ' , (CONCAT(FLOOR(HOUR(TIMEDIFF(vehmove_timein, vehmove_timeout))/24), ':',"
					+ " MOD(HOUR(TIMEDIFF(vehmove_timein, vehmove_timeout)), 24), ':',"
					+ " MINUTE(TIMEDIFF(vehmove_timein, vehmove_timeout)), ''))), '%H:%i:%s'), '') AS tat"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehmove_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_reg_no = vehmove_reg_no"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_vehmove_id = vehmove_id"
					+ " WHERE vehmove_branch_id = " + dr_branch_id + StrSearch + ""
					+ " GROUP BY vehmove_id" + order_by + "";
			SOP("StrSql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				Document document = new Document();
				document.setMargins(20, 20, 5, 5);
				response.setContentType("application/pdf");
				PdfWriter.getInstance(document, response.getOutputStream());
				document.open();

				crs.last();

				PdfPTable top_table = new PdfPTable(2);
				top_table.setWidthPercentage(100);
				PdfPCell cell;

				if (!crs.getString("branch_logo").equals("")) {
					Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
					cell = new PdfPCell(branch_logo);
				} else {
					cell = new PdfPCell(new Phrase(""));
				}
				cell.setBorderWidth(0);
				cell.setPaddingLeft(0);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				top_table.addCell(cell);

				crs.beforeFirst();

				cell = new PdfPCell(new Phrase(button, header_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				top_table.addCell(cell);

				document.add(top_table);

				PdfPTable table = new PdfPTable(8);
				table.setWidths(new int[] { 5, 15, 15, 20, 10, 15, 15, 15 });
				table.setWidthPercentage(100);

				cell = new PdfPCell(new Phrase("#", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Time In", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Time Out", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Reg. No.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// cell = new PdfPCell(new Phrase("Vehicle ID", bold_font));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// table.addCell(cell);
				cell = new PdfPCell(new Phrase("Internal", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("RO No.", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Job Card ID", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("TAT", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				while (crs.next()) {
					count++;

					cell = new PdfPCell(new Phrase(count + "", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("vehmove_timein")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("vehmove_timeout")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(SplitRegNo(crs.getString("vehmove_reg_no"), 2), normal_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					// cell = new PdfPCell(new Phrase(crs.getString("veh_id"), normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					// table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("vehmove_internal"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("jc_ro_no"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("jc_id"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("tat"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);
				}
				document.add(table);
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=No Movement Found!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Branch</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), dr_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
