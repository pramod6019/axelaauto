package axela.insurance;
//aJIt 1st March, 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Insurance_Enquiry_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../insurance/index.jsp\">Insurance Dashboard</a>"
			+ " &gt; <a href=\"insurance-enquiry-list.jsp?all=yes\">Insurance Enquiry List</a>:";
	public String LinkExportPage = "insurance-enquiry-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"../insurance/insurance-enquiry.jsp?add=yes\">Add New Insurance Enquiry...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	// public String BranchAccess = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String insurenquiry_id = "0";
	public String insurenquiry_customer_id = "0";
	// public String jc_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Insurance Enquiry ID", "numeric", "insurenquiry_id"},
			{"Variant ID", "numeric", "variant_id"},
			{"Model Name", "text", "preownedmodel_name"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Reg. No.", "text", "insurenquiry_reg_no"},
			{"Chassis Number", "text", "insurenquiry_chassis_no"},
			{"Engine No.", "text", "insurenquiry_engine_no"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Title", "text", "contact_title_id IN (SELECT title_id FROM compdb.axela_title WHERE title_desc"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Contact Address", "text", "contact_address"},
			{"Contact City", "text", "contact_city_id IN (SELECT contact_city_id FROM compdb.axela_city WHERE city_name"},
			{"Contact Pin", "text", "contact_pin"},
			{"Sale Date", "date", "insurenquiry_sale_date"},
			{"Insurance Executive", "text", "insurenquiry_emp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Insurance Executive ID", "numeric", "insurenquiry_emp_id"},
			{"Notes", "text", "insurenquiry_notes"},
			{"Entry By", "text", "insurenquiry_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "insurenquiry_entry_date"},
			{"Modified By", "text", "insurenquiry_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "insurenquiry_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				insurenquiry_customer_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND insurenquiry_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br/>Results for all Insurance Enquiry!";
				} else if (!insurenquiry_id.equals("0")) {
					msg += "<br/>Results for Insurance Enquiry ID = " + insurenquiry_id + "!";
					StrSearch += " AND insurenquiry_id = " + insurenquiry_id + "";
				} else if (!insurenquiry_customer_id.equals("0")) {
					msg += "<br/>Results for Customer ID = " + insurenquiry_customer_id + "!";
					StrSearch += " AND customer_id = " + insurenquiry_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					// SOP("StrSearch====before=="+StrSearch);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND insurance_enquiry_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {

					msg += "<br/>Results of Search!";
					if (!GetSession("insurstrsql", request).equals("")) {
						StrSearch = GetSession("insurstrsql", request);
					}
				}

				SetSession("insurstrsql", StrSearch, request);
				SOP("StrSearch==00=" + StrSearch);
				// StrSearch += BranchAccess.replace("branch_id", "insurenquiry_branch_id");
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		CachedRowSet crs;
		int TotalRecords;
		String PageURL;
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = " SELECT"
						+ " insurenquiry_id,"
						+ " insurenquiry_emp_id,"
						+ " COALESCE (CONCAT(emp_name, ' (', emp_ref_no, ')'),'') AS emp_name,"
						+ " insurenquiry_branch_id,"
						+ " COALESCE (preownedmodel_id, 0) AS preownedmodel_id,"
						+ " COALESCE (preownedmodel_name, '') AS preownedmodel_name,"
						+ " COALESCE (variant_id, 0) AS variant_id,"
						+ " COALESCE (variant_name,'') AS variant_name,"
						+ " insurenquiry_chassis_no,"
						+ " insurenquiry_engine_no,"
						+ " insurenquiry_reg_no,"
						+ " insurenquiry_insurstatus_id,"
						+ " insurenquiry_entry_date,"
						+ " COALESCE (customer_id, 0) AS customer_id,"
						+ " COALESCE (customer_name, '') AS customer_name,"
						+ " COALESCE (contact_id, 0) AS contact_id,"
						+ " insurenquiry_modelyear,"
						+ " branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " COALESCE ( CONCAT( title_desc, ' ', contact_fname, ' ', contact_lname ), '' ) AS contactname,"
						+ " COALESCE (contact_address, '') AS contact_address,"
						+ " COALESCE (contact_landmark, '') AS contact_landmark,"
						+ " COALESCE (contact_mobile1, '') AS contact_mobile1, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile1_phonetype_id ), '' ) AS phonetypemobile1,"
						+ " COALESCE (contact_mobile2, '') AS contact_mobile2, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile2_phonetype_id ), '' ) AS phonetypemobile2,"
						+ " COALESCE (contact_mobile3, '') AS contact_mobile3, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile3_phonetype_id ), '' ) AS phonetypemobile3,"
						+ " COALESCE (contact_mobile4, '') AS contact_mobile4, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile4_phonetype_id ), '' ) AS phonetypemobile4,"
						+ " COALESCE (contact_mobile5, '') AS contact_mobile5, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile5_phonetype_id ), '' ) AS phonetypemobile5,"
						+ " COALESCE (contact_mobile6, '') AS contact_mobile6, COALESCE ( ( SELECT phonetype_name FROM axelaauto.axela_phonetype WHERE phonetype_id = contact_mobile6_phonetype_id ), '' ) AS phonetypemobile6,"
						+ " COALESCE (contact_email1, '') AS contact_email1,"
						+ " COALESCE (contact_email2, '') AS contact_email2,"
						+ " COALESCE (fieldappt_id,0) AS fieldappt_id,"
						+ " REPLACE (COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM  " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
						+ " WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag";

				CountSql = "SELECT COUNT(DISTINCT(insurenquiry_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id "
						+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
						+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " LEFT JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_fieldappt ON fieldappt_insurenquiry_id = insurenquiry_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY insurenquiry_id";
					// + " ORDER BY insurenquiry_id DESC";
					// SOP("StrSql====" + StrSql);
				}
				CountSql += StrSearch;
				// SOP("CountSql========" + CountSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Insurance Enquiry(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "insurance-enquiry-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " ORDER BY insurenquiry_id DESC";
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("Strsql==insurenquiry list-===" + StrSql);
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th style=\"width:200px;\">Customer</th>\n");
					Str.append("<th>Model</th>\n");
					Str.append("<th data-hide=\"phone\">Variant</th>\n");
					Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Chassis Number</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Engine No.</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Year</th>\n");
					Str.append("<th data-hide=\"phone,tablet\" nowrap>Enquiry Date</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Insurance Executive</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("insurenquiry_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("insurenquiry_id") + ");'");
						Str.append(" style='height:200px'>\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
						Str.append(crs.getString("insurenquiry_id")).append("</a>");
						Str.append("</td>\n<td nowrap>");
						if (!crs.getString("customer_id").equals("0")) {
							Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
							Str.append(crs.getString("customer_name")).append("</a>");
						}

						if (!crs.getString("contactname").equals("")) {
							Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("contactname")).append("</a>");
						}

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>" + crs.getString("contact_mobile1")
											+ "</span> (M) ("
											+ crs.getString("phonetypemobile1") + ")")
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}

						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>"
											+ crs.getString("contact_mobile2") + "</span> (M) (" + crs.getString("phonetypemobile2") + ")")
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}

						if (!crs.getString("contact_mobile3").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>" + crs.getString("contact_mobile3")
											+ "</span> (M) ("
											+ crs.getString("phonetypemobile3") + ")")
									.append(ClickToCall(crs.getString("contact_mobile3"), comp_id));
						}

						if (!crs.getString("contact_mobile4").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>" + crs.getString("contact_mobile4")
											+ "</span> (M) ("
											+ crs.getString("phonetypemobile4") + ")")
									.append(ClickToCall(crs.getString("contact_mobile4"), comp_id));
						}

						if (!crs.getString("contact_mobile5").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>" + crs.getString("contact_mobile5")
											+ "</span> (M) ("
											+ crs.getString("phonetypemobile5") + ")")
									.append(ClickToCall(crs.getString("contact_mobile5"), comp_id));
						}

						if (!crs.getString("contact_mobile6").equals("")) {
							Str.append("<br/>")
									.append("<span class='customer_info customer_" + crs.getString("insurenquiry_id") + "' style='display: none;'>" + crs.getString("contact_mobile6")
											+ "</span> (M) ("
											+ crs.getString("phonetypemobile6") + ")")
									.append(ClickToCall(crs.getString("contact_mobile6"), comp_id));
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
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

						Str.append("</td>\n<td>").append(crs.getString("preownedmodel_name"));
						Str.append("</td>\n<td>");
						if (!crs.getString("variant_id").equals("0")) {
							Str.append(crs.getString("variant_name"));
						}
						Str.append("</td>\n<td nowrap>");
						Str.append(SplitRegNo(crs.getString("insurenquiry_reg_no"), 2));
						Str.append("</td>\n<td>").append(crs.getString("insurenquiry_chassis_no"));
						Str.append("</td>\n<td>").append(crs.getString("insurenquiry_engine_no"));
						Str.append("</td>\n<td>").append(crs.getString("insurenquiry_modelyear"));
						Str.append("</td>\n");
						Str.append("<td>").append(strToShortDate(crs.getString("insurenquiry_entry_date")));
						Str.append("</td>");
						Str.append("<td >");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("insurenquiry_emp_id")).append("\">");
						Str.append(crs.getString("emp_name")).append("</a></td>\n");
						Str.append("\n<td>");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td nowrap>");
						Str.append("<a href=\"../insurance/insurance-enquiry-update.jsp?update=yes&insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
						Str.append("Update Insurance Enquiry").append("</a>");

						if (crs.getString("insurenquiry_insurstatus_id").equals("1")) {
							Str.append("</br><a href=\"../insurance/fieldappt-update.jsp?add=yes&insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
							Str.append("Add Field Appointment").append("</a>");
						}

						if (!crs.getString("fieldappt_id").equals("0")) {
							Str.append("</br><a href=\"../insurance/fieldappt-list.jsp?insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
							Str.append("List Field Appointment").append("</a>");
						}

						Str.append("</br><a href=\"../insurance/insurance-update.jsp?add=yes&insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
						Str.append("Add Policy").append("</a>");

						if (crs.getString("insurenquiry_insurstatus_id").equals("2")) {
							Str.append("</br><a href=\"../insurance/insurance-list.jsp?add=yes&insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("\">");
							Str.append("List Policy").append("</a>");
						}
						Str.append("</td>\n");

						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
					// Add News Letter Link
					// LinkAddPage += "<br/><a href=\"../portal/news-letter-enquiry.jsp?target=insurenquiryicles\" target=_blank>Send News Letter</a>";
				} else {
					Str.append("<br/><br/><font color=\"red\"><b>No Insurance Enquiry(s) found!</b></font>");
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
