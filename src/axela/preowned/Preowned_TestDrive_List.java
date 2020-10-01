package axela.preowned;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Preowned_TestDrive_List extends Connect {
	public String LinkHeader = "";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String testdrive_id = "", enquiry_id = "";
	public String branch_id = "";
	public String ExportPerm = "";
	public String smart = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Test Drive ID", "numeric", "testdrive_id"},
			{"Opportunity ID", "numeric", "testdrive_enquiry_id"},
			{"Vehicle ID", "numeric", "testdrive_preownedstock_id"},
			{"Employee ID", "numeric", "testdrive_emp_id"},
			{"Location ID", "numeric", "testdrive_location_id"},
			{"Test Drive Type ID", "numeric", "testdrive_type"},
			{"Test Drive Time", "date", "testdrive_time"},
			{"Time From", "date", "testdrive_time_from"},
			{"Time To", "date", "testdrive_time_to"},
			{"Test Drive Confirmed", "boolean", "testdrive_confirmed"},
			{"Notes", "text", "testdrive_notes"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc,' ',contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Entry ID", "numeric", "testdrive_entry_id"},
			{"Modified ID", "numeric", "testdrive_modified_id"},
			{"Modified ID", "date", "testdrive_modified_date"},
			{"FB Taken", "numeric", "testdrive_fb_taken"},
			{"FB Status ID", "numeric", "testdrive_fb_status_id"},
			{"FB Status Comments", "text", "testdrive_fb_status_comments"},
			{"FB Budget", "numeric", "testdrive_fb_budget"},
			{"FB Delexp Date", "date", "testdrive_fb_delexp_date"},
			{"FB Finance", "numeric", "testdrive_fb_finance"},
			{"FB Finance Amount", "numeric", "testdrive_fb_finance_amount"},
			{"FB Finance Comments", "text", "testdrive_fb_finance_comments"},
			{"FB Insurance", "numeric", "testdrive_fb_insurance"},
			{"FB Insurance Comments", "text", "testdrive_fb_insurance_comments"},
			{"FB Notes", "text", "testdrive_fb_notes"},
			{"FB Entry ID", "numeric", "testdrive_fb_entry_id"},
			{"FB Entry date", "date", "testdrive_fb_entry_date"},
			{"FB Modified ID", "numeric", "testdrive_fb_modified_id"},
			{"FB Modified date", "date", "testdrive_fb_modified_date"},
			{"Driver", "text", "testdrive_out_driver_id"},
			{"License No", "text", "testdrive_license_no"},
			{"License Address", "text", "testdrive_license_address"},
			{"License Issued By", "text", "testdrive_license_issued_by"},
			{"Licence Validity", "text", "testdrive_license_valid"},
			{"Out Time", "date", "testdrive_out_time"},
			{"Out Kms", "numeric", "testdrive_out_kms"},
			{"In Time", "date", "testdrive_in_time"},
			{"In Kms", "numeric", "testdrive_in_kms"},
			{"Mileage Notes", "text", "testdrive_mileage_notes"},
			{"Mileage Entry ID", "numeric", "testdrive_mileage_entry_id"},
			{"Mileage Entry Date", "date", "testdrive_mileage_entry_date"},
			{"Mileage Modified ID", "numeric", "testdrive_mileage_modified_id"},
			{"Mileage Modified Date", "date", "testdrive_mileage_modified_date"},
			{"Document Value", "text", "testdrive_doc_value"},
			{"Document Entry ID", "numeric", "testdrive_doc_entry_id"},
			{"Document Entry Date", "date", "testdrive_doc_entry_date"},
			{"Document Modified ID", "numeric", "testdrive_doc_modified_id"},
			{"Document Modified Date", "date", "testdrive_doc_modified_date"},
			{"Client FB Entry By", "text", "testdrive_client_fb_entry_id in (select emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Client FB Entry Date", "date", "testdrive_client_fb_entry_date"},
			{"Client FB Modified By", "text", "testdrive_client_fb_modified_id in (select emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Client FB Modified Date", "date", "testdrive_client_fb_modified_date"},
			{"Website Enquiry Date", "date", "testdrive_website_date"},
			{"Website IP", "text", "testdrive_website_ip"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../preowned/index.jsp>" + ReturnPreOwnedName(request)
					+ "</a> &gt; <a href=preowned-testdrive.jsp>Test Drives</a> &gt; <a href=preowned-testdrive-list.jsp?all=yes>List Test Drives</a>:";
			LinkExportPage = "testdrive-export.jsp?smart=yes&target=" + Math.random() + "";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_testdrive_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND testdrive_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Test Drives!";
					// StrSearch = StrSearch + " and testdrive_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Test Drives!";
					StrSearch = StrSearch + " and testdrive_id > 0";
				} else if (!testdrive_id.equals("0")) {
					msg = msg + "<br>Result for Test Drive ID =" + testdrive_id + "!";
					StrSearch = StrSearch + " and testdrive_id =" + testdrive_id + "";
				} else if (!enquiry_id.equals("0")) {
					msg = msg + "<br>Result for " + ReturnPreOwnedName(request) + "=" + enquiry_id + "!";
					StrSearch = StrSearch + " and enquiry_id =" + enquiry_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND testdrive_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("testdrivestrsql", request).equals("")) {
						StrSearch = GetSession("testdrivestrsql", request);
					}
				}
				StrSearch += BranchAccess.replace("branch_id", "enquiry_branch_id")
						+ ExeAccess.replace("emp_id", "testdrive_emp_id");
				SetSession("testdrivestrsql", StrSearch, request);

				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		CachedRowSet crs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		String confirmed = "";
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		String update_info = "";
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT testdrive_id, variant_id, variant_name, branch_code, customer_name,"
						+ " contact_id,"
						+ " CONCAT(contact_fname,' ', contact_lname) AS contactname,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, "
						+ " COALESCE(preownedmodel_name, '') AS  preownedmodel_name, testdrive_time_to,"
						+ " testdrive_time_from, testdrive_type, testdrive_confirmed,"
						+ " COALESCE(testdrive_notes, '') AS testdrive_notes,"
						+ " CONCAT('OPR',branch_code,enquiry_no) AS enquiry_no, testdrive_doc_value, customer_id,"
						+ " enquiry_id, branch_id,"
						+ " CONCAT(branch_name,' (',branch_code,')') AS branchname,"
						+ " emp_id,"
						+ " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name,  testdrive_time,"
						+ " location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms,"
						+ " testdrive_fb_taken,"
						+ " COALESCE(testdrive_fb_status_id,'0') AS testdrive_fb_status_id,"
						// + " COALESCE(status_name, '') AS status_name,"
						+ " testdrive_fb_status_comments,"
						+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount,"
						+ " testdrive_fb_finance_comments, testdrive_fb_insurance, testdrive_fb_insurance_comments";

				CountSql = " SELECT COUNT(distinct testdrive_id) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
						+ " INNER Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id"
						// + " left JOIN " + compdb(comp_id) +
						// "axela_sales_testdrive_status on status_id= testdrive_fb_status_id"
						+ " WHERE 1 = 1 AND enquiry_enquirytype_id = 2 ";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;
				if (!StrSearch.equals("")) {
					StrSql = StrSql + StrSearch + " GROUP BY testdrive_id"
							+ " ORDER BY testdrive_id DESC";
				}
				CountSql = CountSql + StrSearch;
				// SOP("StrSql in ==========" + StrSql);
				// SOP("CountSql in ==========" + CountSql);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Test Drive(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "preowned-testdrive-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned_testdrive\\b", "FROM " + compdb(comp_id) + "axela_preowned_testdrive "
								+ " INNER JOIN (select testdrive_id FROM " + compdb(comp_id) + "axela_preowned_testdrive"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
								+ " WHERE 1=1 " + StrSearch
								+ " GROUP BY testdrive_id "
								+ " ORDER BY testdrive_id DESC"
								// + " LIMIT " + (StartRec - 1) + ", " + recperpage + ""
								+ ") AS myresults using (testdrive_id)");

						StrSql += " GROUP BY testdrive_id ORDER BY testdrive_id DESC";
					}
					// else {
					// StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// }
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("StrSql==" + StrSql);
					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
					Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">ID</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Enquiry</th>\n");
					Str.append("<th style=\"width:200px;\" data-toggle=\"true\">Customer</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Test Drive Details</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Feedback</th>\n");
					// if (branch_id.equals("0")) {
					Str.append("<th>Branch</th>\n");
					// }
					Str.append("<th>Actions</th>\n");
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
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("testdrive_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("testdrive_id") + ");'");
						Str.append("style='height:200px'>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td align=center valign=top>").append(crs.getString("testdrive_id")).append("</td>\n");
						Str.append("<td align=left valign=top>").append("<a href=../sales/enquiry-list.jsp?enquiry_id=");
						Str.append(crs.getString("enquiry_id")).append(">").append("ID: ").append(crs.getString("enquiry_id")).append("</a></td>\n");
						Str.append("<td valign=top align=left nowrap> Customer: <a href=../customer/customer-list.jsp?customer_id=");
						Str.append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a>");;
						Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=");
						Str.append(crs.getString("contact_id")).append(">").append(crs.getString("contactname"));
						Str.append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("testdrive_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("testdrive_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("testdrive_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("testdrive_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email2")).append("\">");
							Str.append(crs.getString("contact_email2")).append("</a></span>");
						}
						if (!crs.getString("testdrive_doc_value").equals("")) {
							Str.append("<br><a href=../Thumbnailblob.do?image_type=preownedtestdrive&testdrive_id="
									+ crs.getString("testdrive_id") + " target=_blank><img src=../Thumbnailblob.do?testdrive_id="
									+ crs.getString("testdrive_id") + "&width=200&image_type=preownedtestdrive&border=0></a>");
						}
						Str.append("</td>");
						Str.append("</td>\n");
						Str.append("<td  valign=top align=left>Model: <b>").append(crs.getString("preownedmodel_name")).append("</b>");
						if (crs.getString("testdrive_type").equals("1")) {
							Str.append("<br><b>Main Test Drive</b>");
						} else {
							Str.append("<br><b>Alternate Test Drive</b>");
						}
						Str.append("<br>Variant: <b><a href=../preowned/managepreownedvariant.jsp?variant_id=").append(crs.getString("variant_id")).append(">").append(crs.getString("variant_name"))
								.append("</b></a>");
						Str.append("<br>Test Drive Time: <b>").append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");
						if (!crs.getString("testdrive_time_from").equals("")) {
							Str.append("<br>Duration: ").append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
						}
						Str.append("<br>Location: ").append(crs.getString("location_name"));
						Str.append("<br>Pre-Owned Consultant: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name"))
								.append("</a>");
						if (!crs.getString("testdrive_notes").equals("")) {
							// Str.append("<br>Notes: ").append(crs.getString("testdrive_notes").substring(0,
							// crs.getString("testdrive_notes").lastIndexOf(","))).append("<br>");
							Str.append("<br>Notes: ").append(crs.getString("testdrive_notes")).append("<br>");
						}
						Str.append(confirmed);
						Str.append("</td>");
						Str.append("<td valign=top align=left>\n");
						// if (crs.getString("testdrive_fb_taken").equals("1"))
						// {
						// Str.append("Test Drive Taken");
						// if (!crs.getString("status_name").equals("")) {
						// Str.append("<br>").append(crs.getString("status_name")).append("<br>").append(crs.getString("testdrive_fb_status_comments"));
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
						// Str.append("<br>Insurance Not Required<br>Comments: "
						// + crs.getString("testdrive_fb_insurance_comments"));
						// }
						// }
						// }
						if (crs.getString("testdrive_fb_taken").equals("1")) {
							Str.append("Test Drive taken");
						}
						if (crs.getString("testdrive_fb_taken").equals("2")) {
							Str.append("Test Drive not taken");
						}
						Str.append("&nbsp;</td>\n");
						// if (branch_id.equals("0")) {
						Str.append("<td valign=top align=left nowrap ><a href=../portal/branch-summary.jsp?branch_id=" + crs.getInt("branch_id") + ">" + crs.getString("branchname") + "</a></td>");
						// }
						Str.append("<td valign=top align=left nowrap >");
						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"preowned-testdrive-update.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
								+ "&enquiry_id=" + crs.getString("enquiry_id") + " \">Update Test Drive</a></li>"
								+ "<li role=presentation><a href=\"../preowned/preowned-testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Feedback</a></li>"
								+ "<li role=presentation><a href=\"../preowned/preowned-testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Mileage</a></li>";

						// if (crs.getString("testdrive_fb_taken").equals("1"))
						// {
						// Str.append("<br><a href=\"../preowned/preowned-testdrive-cust-feedback.jsp?update=yes&testdrive_id=").append(crs.getString("testdrive_id")).append("\">Customer Feedback</a>");
						// }
						// Document upload......
						// if (!crs.getString("testdrive_out_time").equals(""))
						// {
						// Str.append("<br><a href=testdrive-doc-upload.jsp?update=yes&testdrive_id="
						// + crs.getString("testdrive_id") + "&preowned_id=" +
						// crs.getString("preowned_id") +
						// " > Upload Document</a>");
						// }
						// if (!crs.getString("testdrive_out_time").equals("")
						// && crs.getString("testdrive_in_time").equals("")) {
						// Str.append("<br><a href=\"../sales/testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id="
						// + crs.getString("testdrive_id") + "&target=" +
						// Math.random() +
						// "\" target=_blank>Print Gate Pass</a><br>");
						// }
						if (!crs.getString("testdrive_out_time").equals("") && crs.getString("testdrive_in_time").equals("")) {
							update_info += "<li role=presentation><a href=\"../preowned/preowned-testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id="
									+ crs.getString("testdrive_id") + "&target=" + Math.random() + "\" target=_blank>Print Gate Pass</a></li>";
							// Str.append("<br><a href=\"../Test Drive_Report.do?exporttype=pdf&report=gatePass&testdrive_id="
							// + crs.getString("testdrive_id") + "&target=" +
							// Math.random() +
							// "\" target=_blank>Print Gate Pass</a><br>");
						}
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Test Drive(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
