package axela.sales;

import java.io.File;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Dash_TestDrive extends Connect {
	
	public String enquiry_id = "";
	public String enquiry_title = "";
	public String BranchAccess, branch_id = "";
	public String emp_idsession = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				StrSql = "SELECT enquiry_title "
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
						+ " WHERE 1=1 and enquiry_id =" + enquiry_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY enquiry_id "
						+ " ORDER BY enquiry_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						enquiry_title = crs.getString("enquiry_title");
					}
					StrHTML = ListData();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public String ListData() {
		String confirmed = "";
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		
		StrSql = "SELECT testdrive_id, testdriveveh_id, testdriveveh_name, branch_code, customer_name, contact_id,"
				+ " concat(contact_fname,' ', contact_lname) AS contactname, contact_mobile1, contact_mobile2,  "
				+ " COALESCE(model_name, '') as model_name,"
				+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
				+ " COALESCE(testdrive_notes, '') AS testdrive_notes, "
				+ " testdrive_doc_value, customer_id, enquiry_id, branch_id,"
				+ " CONCAT(branch_name,' (',branch_code,')') AS branchname, "
				+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') AS emp_name, "
				+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms, "
				+ " testdrive_fb_taken,"
				+ " COALESCE(testdrive_fb_status_id,'0') AS testdrive_fb_status_id,"
				+ " COALESCE(status_name, '') AS status_name,"
				+ " testdrive_fb_status_comments,"
				+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
				+ " testdrive_fb_insurance, testdrive_fb_insurance_comments  "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
				+ " inner Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id= testdrive_fb_status_id"
				+ " WHERE 1=1"
				+ " AND testdrive_enquiry_id=" + enquiry_id + BranchAccess + ExeAccess + ""
				+ " GROUP BY testdrive_id"
				+ " ORDER BY testdrive_id desc";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Enquiry</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Test Drive Details</th>\n");
				Str.append("<th>Feedback</th>\n");
				if (branch_id.equals("0")) {
					Str.append("<th>Branch</th>\n");
				}
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("testdrive_confirmed").equals("0")) {
						confirmed = "<font color=red><b>[Not Confirmed]</b></font>";
					} else {
						confirmed = "";
					}
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td align=center valign=top><a href=testdrive-list.jsp?testdrive_id=").append(crs.getInt("testdrive_id")).append(">").append(crs.getString("testdrive_id"))
							.append("</a>").append("</td>\n");
					Str.append("<td align=left valign=top>").append("<a href=../sales/enquiry-list.jsp?enquiry_id=");
					Str.append(crs.getString("enquiry_id")).append(">").append("ID: ").append(crs.getString("enquiry_id")).append("</a></td>\n");
					Str.append("<td valign=top align=left nowrap> Customer: <a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a>");
					Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs.getString("contact_id")).append(">").append(crs.getString("contactname"));
					Str.append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
					}
					if (!crs.getString("testdrive_doc_value").equals("")) {
						File f = new File(TestDriveDocPath(comp_id) + crs.getString("testdrive_doc_value"));
						Str.append("<br><a href=../Fetchdocs.do?testdrive_id=").append(crs.getString("testdrive_id")).append("><b>Test Drive Out Pass (").append(ConvertFileSizeToBytes(FileSize(f)))
								.append(")</b></a>");
					}
					Str.append("</td>");
					Str.append("</td>\n");
					Str.append("<td  valign=top align=left>Model: <b>").append(crs.getString("model_name")).append("</b>");
					if (crs.getString("testdrive_type").equals("1")) {
						Str.append("<br><b>Main Test Drive</b>");
					} else {
						Str.append("<br><b>Alternate Test Drive</b>");
					}
					Str.append("<br>Vehicle: <b><a href=../sales/managetestdrivevehicle.jsp?testdriveveh_id=").append(crs.getString("testdriveveh_id")).append(">")
							.append(crs.getString("testdriveveh_name")).append("</b></a>");
					Str.append("<br>Test Drive Time: <b>").append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");
					if (!crs.getString("testdrive_time_from").equals("")) {
						Str.append("<br>Duration: ").append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
					}
					Str.append("<br>Location: ").append(crs.getString("location_name"));
					Str.append("<br>Executive: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
					if (!crs.getString("testdrive_notes").equals("")) {
						// Str.append("<br>Notes: ").append(crs.getString("testdrive_notes").substring(0, crs.getString("testdrive_notes").lastIndexOf(","))).append("<br>");
						Str.append("<br>Notes: ").append(crs.getString("testdrive_notes")).append("<br>");
					}
					Str.append(confirmed);
					Str.append("</td>");
					Str.append("<td valign=top align=left nowrap>\n");
					if (crs.getString("testdrive_fb_taken").equals("1")) {
						Str.append("Test Drive Taken");
						if (!crs.getString("status_name").equals("")) {
							Str.append("<br>").append(crs.getString("status_name")).append("<br>").append(crs.getString("testdrive_fb_status_comments"));
						} else {
							if (crs.getDouble("testdrive_fb_budget") != 0) {
								Str.append("<br>Budget: ").append(crs.getString("testdrive_fb_budget")).append("<br>");
							}
							if (crs.getString("testdrive_fb_finance").equals("1")) {
								Str.append("<br>Finance Required<br>%age: ").append(crs.getString("testdrive_fb_finance_amount"));
							} else {
								Str.append("<br>Finance Not Required<br>Comments: ").append(crs.getString("testdrive_fb_finance_comments"));
							}
							if (crs.getString("testdrive_fb_insurance").equals("1")) {
								Str.append("<br>Insurance Required");
							} else {
								Str.append("<br>Insurance Not Required<br>Comments: " + crs.getString("testdrive_fb_insurance_comments"));
							}
						}
					}
					if (crs.getString("testdrive_fb_taken").equals("2")) {
						Str.append("Test Drive not taken");
					}
					Str.append("&nbsp;</td>\n");
					if (branch_id.equals("0")) {
						Str.append("<td valign=top align=left nowrap ><a href=../portal/branch-summary.jsp?branch_id=" + crs.getInt("branch_id") + ">" + crs.getString("branchname") + "</a></td>");
					}
					Str.append("<td valign=top align=left nowrap ><a href=\"testdrive-update.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id") + "&enquiry_id="
							+ crs.getString("enquiry_id") + " \">Update Test Drive</a>"
							+ "<br><a href=\"testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id") + "\">Update Feedback</a>"
							+ "<br><a href=\"testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id") + "\">Update Mileage</a>"
							+ "");
					if (crs.getString("testdrive_fb_taken").equals("1")) {
						Str.append("<br><a href=\"testdrive-cust-feedback.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id") + "\">Customer Feedback</a>");
					}
					if (!crs.getString("testdrive_out_time").equals("")) {
						Str.append("<br><a href=testdrive-doc-upload.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id") + "&enquiry_id=" + crs.getString("enquiry_id")
								+ " > Upload Document</a>");
					}
					if (!crs.getString("testdrive_out_time").equals("") && crs.getString("testdrive_in_time").equals("")) {
						Str.append("<br><a href=\"../sales/testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id=" + crs.getString("testdrive_id") + "&target=" + Math.random()
								+ "\" target=_blank>Print Gate Pass</a><br>");
						// Str.append("<br><a href=\"../TestDrive_Report.do?exporttype=pdf&report=gatePass&testdrive_id=" + crs.getString("testdrive_id") + "&target=" + Math.random() +
						// "\" target=_blank>Print Gate Pass</a><br>");
					}
					
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Test Drive(s) found!</b></font>");
			}
			
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
