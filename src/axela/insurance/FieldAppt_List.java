// smitha nag 11 feb 2013
/*Modified by Smitha nag 19th april 2013 */
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class FieldAppt_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../isurance/index.jsp>Insurance</a>"
			+ " &gt; <a href=fieldappt.jsp>Field Appointment</a>"
			+ " &gt; <a href=fieldappt-list.jsp?all=yes>List Field Appointment</a>:";
	public String LinkExportPage = "";
	public String ExportPerm = "";
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
	public String comp_id = "0";
	public String fieldappt_id = "", fieldappt_insurenquiry_id = "";
	public String branch_id = "";
	public String smart = "";
	public String advSearch = "";
	public String emp_all_exe = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Field Appointment ID", "numeric", "fieldappt_id"},
			{"Enquiry ID", "numeric", "fieldappt_insurenquiry_id"},
			{"Employee ID", "numeric", "fieldappt_emp_id"},
			{"Field Appointment Type", "numeric", "fieldappt_fieldappttype_id"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "concat(contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''), REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "concat(contact_email1,contact_email2)"},
			{"Field Appointment Time", "date", "fieldappt_appttime"},
			{"Field Appointment From", "date", "fieldappt_fromtime"},
			{"Field Appointment Time To", "date", "fieldappt_totime"},
			{"Notes", "text", "fieldappt_status_notes"},
			{"Entry ID", "numeric", "fieldappt_entry_id"},
			{"Modified ID", "numeric", "fieldappt_modified_id"},
			{"Modified ID", "date", "fieldappt_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_field_appointment_access", request, response);
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
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				fieldappt_id = CNumeric(PadQuotes(request.getParameter("fieldappt_id")));
				fieldappt_insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND fieldappt_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Field Appointment!";
					StrSearch = StrSearch + " and fieldappt_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Field Appointmen!";
					StrSearch = StrSearch + " AND fieldappt_id > 0";
				} else if (!fieldappt_insurenquiry_id.equals("0")) {
					msg += "<br>Result for Field Insurance Enquiry Id =" + fieldappt_insurenquiry_id + "!";
					StrSearch = StrSearch + " AND fieldappt_insurenquiry_id =" + fieldappt_insurenquiry_id + "";
				}
				else if (!fieldappt_id.equals("0")) {
					msg += "<br>Result for Field Appointment Id =" + fieldappt_id + "!";
					StrSearch = StrSearch + " AND fieldappt_id =" + fieldappt_id + "";
				}
				else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND fieldappt_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("fieldaptstrsql", request).equals("")) {
						StrSearch = GetSession("fieldaptstrsql", request);
						// SOP("StrSearch===" + StrSearch);
					}
				}
				StrSearch += BranchAccess + ExeAccess;

				if (!StrSearch.equals("")) {
					SetSession("fieldaptstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = " SELECT fieldappt_id, insurenquiry_id, fieldappt_fieldappttype_id, fieldappttype_name,"
						+ " emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
						+ " fieldappt_appttime, fieldappt_fromtime, fieldappt_totime,"
						+ " insurenquiry_branch_id,"
						+ " fieldappt_status_taken, fieldappt_status_notes,"
						+ " CONCAT( branch_name, ' (', branch_code, ')' ) AS branchname, preownedmodel_name, branch_id, branch_name,"
						+ " customer_id, customer_name, contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contactname,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " REPLACE(COALESCE((SELECT "
						+ " GROUP_CONCAT('StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' "
						+ " ) FROM " + compdb(comp_id) + "axela_customer_tag "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id "
						+ " WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

				CountSql = "SELECT COUNT(DISTINCT fieldappt_id) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_fieldappt"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_fieldappt_type ON fieldappttype_id = fieldappt_fieldappttype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = fieldappt_insurenquiry_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = fieldappt_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
						+ " WHERE 1 = 1";

				if ("yes".equals(all)) {
					SqlJoin += " AND fieldappt_appttime >= DATE_FORMAT(DATE_ADD( " + ToLongDate(kknow()) + ", INTERVAL - 90 DAY ),'%Y%m%d%H%i%s')";
				}

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY fieldappt_id"
							+ " ORDER BY fieldappt_id DESC";
					CountSql += StrSearch;
				}
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					// SOP("CountSql===" + CountSql);
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Field Appointment(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "fieldappt-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_insurance_fieldappt\\b",
								"FROM " + compdb(comp_id) + "axela_insurance_fieldappt "
										+ " INNER JOIN (SELECT fieldappt_id"
										+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt "
										// + " GROUP BY fieldappt_id "
										+ " ORDER BY fieldappt_id desc) AS myresults USING (fieldappt_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable ";

						if (orderby.equals("")) {
							StrSql = StrSql + " ORDER BY fieldappt_id DESC ";
						} else {
							StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}

					if (!all.equals("recent")) {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql, 0);
					}

					// SOP("Field Appt-list-----" + StrSql);

					int count = StartRec - 1;
					Str.append("<div class=\" table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th data-hide=\"phone\">Insurance Enquiry ID</th>\n");
					Str.append("<th style=\"width:200px;\">Customer</th></style>\n");
					Str.append("<th data-hide=\"phone\">Field Appointment Details</th>\n");
					Str.append("<th data-hide=\"phone\">Field Execuitve</th>\n");
					Str.append("<th data-hide=\"phone\">Status</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("fieldappt_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("fieldappt_id") + ");'");
						Str.append(" style='height:200px'>\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("fieldappt_id")).append("</td>\n");
						Str.append("<td>").append("<a href=../insurance/insurance-enquiry-list.jsp?insurenquiry_id=");
						Str.append(crs.getString("insurenquiry_id")).append(">").append("ID: ")
								.append(crs.getString("insurenquiry_id")).append("</a></td>\n");
						Str.append("<td> Customer: <a href=../customer/customer-list.jsp?customer_id=");
						Str.append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a>");
						Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=");
						Str.append(crs.getString("contact_id")).append(">").append(crs.getString("contactname"));
						Str.append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("fieldappt_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("fieldappt_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("fieldappt_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email2")).append("\">");
							Str.append(crs.getString("contact_email2")).append("</a></span>");
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
						Str.append("</td>\n<td valign=top align=left>Model: <b>").append(crs.getString("preownedmodel_name") + "</b>")
								.append("</br> Appt Type: ").append("<b>" + crs.getString("fieldappttype_name") + "</b>")
								.append("</br> Appt Time: ").append("<b>" + strToLongDate(crs.getString("fieldappt_appttime")) + "</b>")
								.append("</br> Duration: ").append(strToLongDate(crs.getString("fieldappt_fromtime")).substring(10, 16))
								.append("-" + strToLongDate(crs.getString("fieldappt_totime")).substring(10, 16));
						// Str.append("<td valign=top align=left>Appt Type: <b>").append(crs.getString("fieldappttype_name")).append("</b></td>");
						// SOP("==" + strToLongDate(crs.getString("fieldappt_fromtime")).substring(10, 14));
						Str.append("<td>\n");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
						Str.append(crs.getString("emp_name")).append("</a></td>\n");
						Str.append("</td>\n");

						Str.append("<td>\n");
						if (crs.getString("fieldappt_status_taken").equals("1")) {
							Str.append("Field Appointment Done");
							Str.append("<br>").append(crs.getString("fieldappt_status_notes"));
						}
						else {
							Str.append("Appointment Not completed");
						}
						Str.append("</td>\n");

						Str.append("<td>\n");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n");

						Str.append("<td nowrap><a href=\"fieldappt-update.jsp?update=yes&fieldappt_id=").append(crs.getString("fieldappt_id"));
						Str.append("&insurenquiry_id=").append(crs.getString("insurenquiry_id"))
								.append(" \">Update Field Appointment</a>");
						Str.append("<br><a href=\"fieldappt-status-update.jsp?fieldappt_id=").append(crs.getString("fieldappt_id"))
								.append("\">Update Status</a>");
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Field Appointment(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
