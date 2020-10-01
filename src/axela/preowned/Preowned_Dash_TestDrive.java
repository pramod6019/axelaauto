package axela.preowned;
//Dilip Kumar 28 Jun 2013

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_TestDrive extends Connect {

	public String preowned_id = "";
	public String preowned_title = "";
	public String BranchAccess, branch_id = "";
	public String emp_idsession = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				StrSql = "SELECT preowned_title"
						+ " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
						+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY preowned_id"
						+ " ORDER BY preowned_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
					}
					StrHTML = ListData();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Pre Owned!");
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		String confirmed = "";

		StrSql = "SELECT testdrive_id, preowned_id, preowned_title, customer_name, contact_id, enquiry_id,"
				+ " CONCAT(contact_fname,' ',contact_lname) AS 'Contact Name', contact_mobile1, contact_mobile2,"
				+ " (preownedmodel_name) AS 'Model Name',"
				+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed, COALESCE(testdrive_notes, '') AS Notes,"
				+ " testdrive_doc_value, customer_id, preowned_id, branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS Branch, "
				+ " emp_id, CONCAT(emp_name,' (', emp_ref_no, ')') AS Pre-OwnedConsultant, "
				+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms,"
				+ " testdrive_fb_taken, COALESCE(testdrive_fb_status_id,'0') AS testdrive_fb_status_id,"
				// + " COALESCE(status_name, '') AS 'Status Name', "
				+ " testdrive_fb_status_comments,"
				+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
				+ " testdrive_fb_insurance, testdrive_fb_insurance_comments"
				+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
				+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
				+ " INNER JOIN axela_preowned_model ON preownedmodel_id = preowned_preownedmodel_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id"
				+ " WHERE 1 = 1 AND preowned_id = " + preowned_id + BranchAccess + ExeAccess
				+ " GROUP BY testdrive_id"
				+ " ORDER BY testdrive_time";
		// SOP("StrSql===="+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				int count = 0;

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th  data-toggle=\"true\">ID</th>\n");
				Str.append("<th  data-toggle=\"true\">Pre Owned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre Owned Details</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Feedback</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("testdrive_confirmed").equals("0")) {
						confirmed = "<font color=red><b>[Not Confirmed]</b></font>";
					} else {
						confirmed = "";
					}
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td align=center valign=top><a href=preowned-testdrive-list.jsp?testdrive_id=").append(crs.getInt("testdrive_id")).append(">").append(crs.getString("testdrive_id"))
							.append("</a>").append(crs.getString("testdrive_id")).append("</td>\n");
					Str.append("<td align=left valign=top>").append("<a href=../preowned/preowned-list.jsp?preowned_id=");
					Str.append(crs.getString("preowned_id")).append(">").append("ID: ").append(crs.getString("preowned_id")).append("</a></td>\n");
					Str.append("<td valign=top align=left nowrap> Customer: <a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a>");
					Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs.getString("contact_id")).append(">").append(crs.getString("Contact Name"));
					Str.append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
					}
					Str.append("</td>\n");
					Str.append("<td align=left valign=top>");
					if (!crs.getString("testdrive_doc_value").equals("")) {
						File f = new File(PreownedDocPath(comp_id) + crs.getString("testdrive_doc_value"));
						Str.append("<br><a href=../Fetchdocs.do?testdrive_id=").append(crs.getString("testdrive_id")).append("><b>Pre Owned Out Pass (").append(ConvertFileSizeToBytes(FileSize(f)))
								.append(")</b></a>");
					}

					Str.append("Model: <b>").append(crs.getString("Model Name")).append("</b>");
					if (crs.getString("testdrive_type").equals("1")) {
						Str.append("<br><b>Main Pre Owned</b>");
					} else {
						Str.append("<br><b>Alternate Pre Owned</b>");
					}
					Str.append("<br>Pre Owned Time: <b>").append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");
					if (!crs.getString("testdrive_time_from").equals("")) {
						Str.append("<br>Duration: ").append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
					}
					Str.append("<br>Location: ").append(crs.getString("location_name"));
					Str.append("<br>Pre-Owned Consultant: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("Pre-Owned Consultant")).append("</a>");
					if (!crs.getString("Notes").equals("")) {
						Str.append("<br>Notes: ").append(crs.getString("Notes")).append("<br>");
					}
					Str.append(confirmed);
					Str.append("</td>");
					Str.append("<td valign=top align=left nowrap>\n");
					if (crs.getString("testdrive_fb_taken").equals("1")) {
						Str.append("Test Drive Taken");
						// if (!crs.getString("Status Name").equals("")) {
						// Str.append("<br>").append(crs.getString("Status Name")).append("<br>").append(crs.getString("testdrive_fb_status_comments"));
						// } else {
						// if (crs.getDouble("testdrive_fb_budget") != 0) {
						// Str.append("<br>Budget: ").append(crs.getString("testdrive_fb_budget")).append("<br>");
						// }
						// if
						// (crs.getString("testdrive_fb_finance").equals("1")) {
						// Str.append("<br>Finance Required<br>%age: ").append(crs.getString("testdrive_fb_finance_amount"));
						// } else {
						// Str.append("<br>Finance Not Required<br>Comments: ").append(crs.getString("testdrive_fb_finance_comments"));
						// }
						// if
						// (crs.getString("testdrive_fb_insurance").equals("1"))
						// {
						// Str.append("<br>Insurance Required");
						// } else {
						// Str.append("<br>Insurance Not Required<br>Comments: ").append(crs.getString("testdrive_fb_insurance_comments"));
						// }
						// }
					}
					if (crs.getString("testdrive_fb_taken").equals("2")) {
						Str.append("Test Drive not taken");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left nowrap ><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">").append(crs.getString("Branch"))
							.append("</a></td>");
					Str.append("<td valign=top align=left nowrap ><a href=\"../preowned/preowned-testdrive-update.jsp?update=yes&testdrive_id=").append(crs.getString("testdrive_id"))
							.append("&enquiry_id=").append(crs.getString("enquiry_id")).append(" \">Update Test Drive</a>"
									+ "<br><a href=\"../preowned/testdrive-feedback.jsp?testdrive_id=").append(crs.getString("testdrive_id")).append("\">Update Feedback</a>"
									+ "<br><a href=\"../preowned/testdrive-mileage.jsp?testdrive_id=").append(crs.getString("testdrive_id")).append("\">Update Mileage</a>" + "");
					if (crs.getString("testdrive_fb_taken").equals("1")) {
						Str.append("<br><a href=\"../preowned/testdrive-cust-feedback.jsp?update=yes&testdrive_id=").append(crs.getString("testdrive_id")).append("\">Customer Feedback</a>");
					}
					if (!crs.getString("testdrive_out_time").equals("")) {
						Str.append("<br><a href=../preowned/testdrive-doc-upload.jsp?update=yes&testdrive_id=").append(crs.getString("testdrive_id")).append("&preowned_id=")
								.append(crs.getString("preowned_id")).append(" > Upload Document</a>");
					}
					if (!crs.getString("testdrive_out_time").equals("") && crs.getString("testdrive_in_time").equals("")) {
						Str.append("<br><a href=\"../preowned/testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id=").append(crs.getString("testdrive_id")).append("&target=")
								.append(Math.random()).append("\" target=_blank>Print Gate Pass</a><br>");
						// Str.append("<br><a href=\"../Test Drive_Report.do?exporttype=pdf&report=gatePass&testdrive_id="
						// + crs.getString("testdrive_id") + "&target=" +
						// Math.random() +
						// "\" target=_blank>Print Gate Pass</a><br>");
					}

					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Test Drive(s) found!</b></font></center><br><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
