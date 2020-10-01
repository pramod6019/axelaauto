package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class TestDrive_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../sales/index.jsp>Sales</a>"
			+ " &gt; <a href=testdrive.jsp>Test Drives</a>"
			+ " &gt; <a href=testdrive-list.jsp?all=yes>List Test Drive</a>:";
	public String LinkExportPage = "testdrive-export.jsp?smart=yes&target="
			+ Math.random() + "";
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
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String Img = "";
	public String comp_id = "0";
	public String testdrive_id = "", enquiry_id = "";
	public String branch_id = "";
	public String ExportPerm = "";
	public String smart = "";
	public String advSearch = "";
	public String emp_all_exe = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			// {"Keyword", "text", "keyword_arr"},
			{"Test Drive ID", "numeric", "testdrive_id"},
			{"Enquiry ID", "numeric", "testdrive_enquiry_id"},
			{"Vehicle ID", "numeric", "testdrive_testdriveveh_id"},
			{"Employee ID", "numeric", "testdrive_emp_id"},
			{"Location ID", "numeric", "testdrive_location_id"},
			{"Test Drive Type ID", "numeric", "testdrive_type"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "concat(contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''), REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "concat(contact_email1,contact_email2)"},
			{"Test Drive Time", "date", "testdrive_time"},
			{"Time From", "date", "testdrive_time_from"},
			{"Time To", "date", "testdrive_time_to"},
			{"Test Drive Confirmed", "boolean", "testdrive_confirmed"},
			{"Notes", "text", "testdrive_notes"},
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
			// {"Website Enquiry Date", "date", "testdrive_website_date"},
			// {"Website IP", "text", "testdrive_website_ip"},
			{"Client FB Entry By", "numeric", "testdrive_client_fb_entry_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Client FB Entry Date", "date", "testdrive_client_fb_entry_date"},
			{"Client FB Modified By", "numeric", "testdrive_client_fb_modified_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Client FB Modified Date", "date", "testdrive_client_fb_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " and testdrive_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Test Drives!";
					StrSearch = StrSearch + " and testdrive_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Test Drives!";
					StrSearch = StrSearch + " AND testdrive_id > 0";
				} else if (!testdrive_id.equals("0")) {
					msg += "<br>Result for Test Drive id =" + testdrive_id + "!";
					StrSearch = StrSearch + " and testdrive_id =" + testdrive_id + "";
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
				StrSearch += BranchAccess + ExeAccess;

				if (!StrSearch.equals("")) {
					SetSession("testdrivestrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
		StringBuilder Str = new StringBuilder();
		String action_info = "";
		StringBuilder customer_info = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT testdrive_id, testdriveveh_id, testdriveveh_name, branch_code, branch_brand_id, customer_name, contact_id,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS contactname, contact_mobile1,"
						+ " contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(model_name, '') AS model_name,"
						+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
						+ " COALESCE(testdrive_notes, '') AS testdrive_notes, testdrive_out_kms,"
						+ " customer_id, enquiry_id, branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " emp_id, concat(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
						+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms,"
						+ " testdrive_fb_taken, COALESCE(testdrive_fb_status_id, 0) AS testdrive_fb_status_id,"
						+ " coalesce(status_name, '') AS status_name, testdrive_out_time,"
						+ " testdrive_fb_status_comments, testdrive_fb_budget, testdrive_fb_finance,"
						+ " testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
						// + " COALESCE(OCTET_LENGTH(testdrive_doc_data), 0) AS testdrive_doc_data,"
						+ " COALESCE(testdrive_doc_value, '') AS testdrive_doc_value,"

						+ " testdrive_fb_insurance, testdrive_fb_insurance_comments "
						+ ", REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM  " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " ON tagtrans_tag_id = tag_id WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

				CountSql = "SELECT COUNT(DISTINCT testdrive_id) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id= testdrive_fb_status_id"
						+ " WHERE 1 = 1";

				if ("yes".equals(all)) {
					SqlJoin += " AND testdrive_time >= DATE_FORMAT(DATE_ADD( " + ToLongDate(kknow()) + ", INTERVAL - 90 DAY ),'%Y%m%d%H%i%s')";
				}

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY testdrive_id"
							+ " ORDER BY testdrive_id DESC";
					CountSql += StrSearch;
				}
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
				// SOP("Refer this query-----" + StrSql);// remove this line
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
					PageURL = "testdrive-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_sales_testdrive\\b",
								"FROM " + compdb(comp_id) + "axela_sales_testdrive "
										+ " INNER JOIN (SELECT testdrive_id"
										+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
										// + " GROUP BY testdrive_id "
										+ " ORDER BY testdrive_id desc) AS myresults USING (testdrive_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable ";

						if (orderby.equals("")) {
							StrSql = StrSql + " ORDER BY enquiry_id DESC ";
						} else {
							StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}

					if (!all.equals("recent")) {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql, 0);
					}

					// SOPInfo("testdrive-list===" + StrSql);

					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th data-hide=\"phone\">Enquiry</th>\n");
					Str.append("<th >Customer</th></style>\n");
					Str.append("<th data-hide=\"phone\">Test Drive Details</th>\n");
					Str.append("<th data-hide=\"phone\">Feedback</th>\n");
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

						Str.append("<tr>");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("testdrive_id")).append("</td>\n");
						Str.append("<td>").append("<a href=../sales/enquiry-list.jsp?enquiry_id=");
						Str.append(crs.getString("enquiry_id")).append(">").append("ID: ")
								.append(crs.getString("enquiry_id")).append("</a></td>\n");

						Str.append("<td>");
						// Customer Info

						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email2"));
						}

						Str.append(CustomerContactDetailsPopup(crs.getString("customer_id"), crs.getString("customer_name"), customer_info.toString(), "customer"));

						customer_info.setLength(0);

						// Contact Info

						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email2"));
						}

						Str.append("<br/>" + CustomerContactDetailsPopup(crs.getString("contact_id"), crs.getString("contactname"), customer_info.toString(), "contact"));

						customer_info.setLength(0);

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("testdrive_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("testdrive_id"), "M"));
						}

						// Populating Tags in Enquiry list
						Str.append("<br><br>");

						String Tag = crs.getString("tag");
						Tag = ReplaceStr(Tag, "StartColor", "<button class='btn-xs btn-arrow-left' style='top:-16px; background:");
						Tag = ReplaceStr(Tag, "EndColor", " ; color:white'  disabled>&nbsp");
						Tag = ReplaceStr(Tag, "StartName", "");
						Tag = ReplaceStr(Tag, "EndName", "</button>&nbsp&nbsp&nbsp");
						Str.append(Tag);
						// Tags End
						Str.append("</td>");
						// if (!crs.getString("testdrive_doc_value").equals("")) {
						// File f = new File(TestDriveDocPath(comp_id) + Str.append("<br>") + crs.getString("testdrive_doc_value"));
						// Str.append(
						// "<br><a href=../Fetchdocs.do?testdrive_id=")
						// .append(crs.getString("testdrive_id"))
						// .append("><b>Test Drive Out Pass (")
						// .append(ConvertFileSizeToBytes(crs.getBytes("testdrive_doc_data").length))
						// .append(")</b></a>");
						// }

						Str.append("<td valign=top align=left>Model: <b>").append(crs.getString("model_name")).append("</b>");

						if (crs.getString("testdrive_type").equals("1")) {
							Str.append("<br><b>Main Test Drive</b>");
						} else {
							Str.append("<br><b>Alternate Test Drive</b>");
						}

						Str.append("<br>Vehicle: <b><a href=../sales/managetestdrivevehicle.jsp?testdriveveh_id=")
								.append(crs.getString("testdriveveh_id")).append(">");
						Str.append(crs.getString("testdriveveh_name")).append("</b></a>");
						Str.append("<br>Test Drive Time: <b>").append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");

						if (!crs.getString("testdrive_time_from").equals("")) {
							Str.append("<br>Duration: ").append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1"));
						}

						Str.append("<br>Location: ").append(crs.getString("location_name"));
						Str.append("<br>Sales Consultant: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">");
						Str.append(crs.getString("emp_name")).append("</a>");

						if (!crs.getString("testdrive_notes").equals("")) {
							Str.append("<br>Notes: ").append(crs.getString("testdrive_notes"));
						}

						Str.append("<br>").append(confirmed);
						Str.append("</td>\n<td>\n");

						if (crs.getString("testdrive_fb_taken").equals("1")) {

							Str.append("Test Drive Taken");

							if (!crs.getString("status_name").equals("")) {
								Str.append("<br>").append(crs.getString("status_name"));
								Str.append("<br>").append(crs.getString("testdrive_fb_status_comments"));
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
									Str.append("<br>Insurance Not Required<br>Comments: ").append(crs.getString("testdrive_fb_insurance_comments"));
								}
							}
						}
						if (crs.getString("testdrive_fb_taken").equals("2")) {
							Str.append("Test Drive not taken");
						}

						Str.append("&nbsp;</td>\n");
						if (!crs.getString("testdrive_doc_value").equals("") && !crs.getString("testdrive_id").equals("0")) {
							Img = "<a href=../Thumbnailblob.do?image_type=testdrive&testdrive_id="
									+ crs.getString("testdrive_id") + " target=_blank><img src=../Thumbnailblob.do?testdrive_id="
									+ crs.getString("testdrive_id") + "&width=200&image_type=testdrive&border=0></a>";

						} else {
							Img = "";
						}

						Str.append("<td><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">");
						Str.append(crs.getString("branchname")).append("</a><br>").append(Img);

						// if (!crs.getString("testdrive_doc_value").equals("")) {
						//
						// Str.append("<br><a href=../Fetchdocs.do?").append(QueryString).append("&testdrive_id=")
						// .append(crs.getString("testdrive_id")).append(">").append("<br> Driving Licence (")
						// .append(ConvertFileSizeToBytes(crs.getInt("testdrive_doc_data")))
						// .append(")</a>");
						//
						// }
						Str.append("</td>");
						Str.append("<td nowrap>");
						action_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=testdrive-update.jsp?update=yes"
								+ "&testdrive_id=" + crs.getString("testdrive_id") + "&enquiry_id=" + crs.getString("enquiry_id") + ">Update Test Drive</a></li>";

						action_info += "<li role=presentation><a href=testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ ">Update Feedback</a></li>";
						action_info += "<li role=presentation><a href=testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ ">Update Mileage</a></li>";
						action_info += "<li role=presentation><a href=../portal/new-docs-update.jsp?add=yes&testdrive_id=" + crs.getString("testdrive_id")
								+ " >Add Driving Licence</a></li>";

						if (crs.getString("testdrive_fb_taken").equals("1")) {
							action_info += "<li role=presentation><a href=testdrive-cust-feedback.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
									+ ">Customer Feedback</a></li>";
						}

						// if (!crs.getString("testdrive_out_time").equals(""))
						// {
						// Str.append("<br><a href=testdrive-doc-upload.jsp?update=yes&testdrive_id=").append(crs.getString("testdrive_id")).append("&enquiry_id=").append(crs.getString("enquiry_id")).append("> Upload Document</a>");
						// }
						// SOP("brand id===" + crs.getString("branch_brand_id")
						// + "------" + crs.getString("branchname"));
						// SOP("===" + crs.getString("branch_brand_id"));
						if (crs.getString("branch_brand_id").equals("55")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-mb.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (crs.getString("branch_brand_id").equals("11")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-skoda.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (crs.getString("branch_brand_id").equals("56")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-porsche.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (crs.getString("branch_brand_id").equals("60")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-JLR.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (crs.getString("branch_brand_id").equals("151")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-onetriumph.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass1.jsp?brand_id=" + crs.getString("branch_brand_id")
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						}

						action_info += "</ul></div></center></div>";
						Str.append(action_info);
						Str.append("</td>\n");
						Str.append("</tr>\n");
						action_info = "";

					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Test Drive(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
