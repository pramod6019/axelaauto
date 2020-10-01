package axela.service;
//bhagwan singh 12 March, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Booking_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../service/index.jsp\">Service</a>"
			+ " &gt; <a href=\"../service/appt.jsp\">Bookings</a>"
			+ " &gt; <a href=\"booking-list.jsp?all=yes\">List Bookings</a>:";
	public String LinkExportPage = "booking-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String comp_id = "0";
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
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String booking_id = "0";
	public String booking_customer_id = "0";
	public String booking_veh_id = "0";
	public String booking_emp_id = "0";
	public String customer_id = "0";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Booking ID", "numeric", "booking_id"},
			{"Booking Time", "date", "booking_time"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "booking_contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Vehicle ID", "numeric", "booking_veh_id"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"CRM Executive", "text", "booking_crm_emp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Service Advisor", "text", "booking_service_emp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry By", "text", "booking_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "booking_entry_date"},
			{"Modified By", "text", "booking_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "booking_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_booking_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "booking_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "booking_service_emp_id");
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = unescapehtml(PadQuotes(request.getParameter("msg")));
				booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
				booking_veh_id = CNumeric(PadQuotes(request.getParameter("booking_veh_id")));
				booking_customer_id = CNumeric(PadQuotes(request.getParameter("booking_customer_id")));
				booking_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND booking_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Bookings!";
					// StrSearch += " AND booking_id > 0";
				} else if (!booking_id.equals("0")) {
					msg += "<br>Results for Booking ID = " + booking_id + "!";
					StrSearch += " AND booking_id = " + booking_id + "";
				} else if (!booking_veh_id.equals("0")) {
					msg += "<br>Results for Vehicle ID = " + booking_veh_id + "!";
					StrSearch += " AND booking_veh_id = " + booking_veh_id + "";
				} else if (!customer_id.equals("0")) {
					msg += "<br>Results for Customer ID = " + customer_id + "!";
					StrSearch += " AND customer_id = " + customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch += SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Results for all Booking!";
						// msg = "Enter search text!";
						// StrSearch += " AND booking_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				else if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("apptstrsql", request).equals("")) {
						StrSearch = GetSession("apptstrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess;

				SetSession("apptstrsql", StrSearch, request);
				StrHTML = ListData();

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT booking_id, booking_branch_id, booking_veh_id, contact_id,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS contact_name, booking_time, contact_mobile1,"
						+ " contact_mobile2, contact_email1, contact_email2, item_id, customer_name, customer_id,"
						+ " veh_reg_no, bookingstatus_name, veh_id, title_desc, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " COALESCE(model_name, '') AS model_name,"
						+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
						+ " COALESCE(crm.emp_id, 0) AS crmemp_id, COALESCE(CONCAT(crm.emp_name, ' (', crm.emp_ref_no, ')'),'') AS crmempname,"
						+ " COALESCE(service.emp_id,0) AS serviceemp_id,COALESCE(CONCAT(service.emp_name, ' (', service.emp_ref_no, ')'),'') AS serviceempname,"
						+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
						+ " WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag";

				CountSql = "SELECT COUNT(DISTINCT(booking_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_booking"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = booking_crm_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp service ON service.emp_id = booking_service_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = booking_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = booking_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_booking_status ON bookingstatus_id = booking_bookingstatus_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY booking_id"
							+ " ORDER BY booking_id DESC";
				}
				// SOP("StrSql------booking list-------" + StrSql);
				CountSql += StrSearch;
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// SOP("CountSql----------" + CountSql);
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Booking(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "booking-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_booking\\b",
								"FROM " + compdb(comp_id) + "axela_service_booking"
										+ " INNER JOIN (SELECT booking_id FROM " + compdb(comp_id) + "axela_service_booking"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY booking_id"
										+ " ORDER BY booking_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (booking_id)");
						StrSql = StrSql
								+ " ORDER BY booking_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}

					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;

					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					// Str.append("<th nowrap>Booking ID</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th data-hide=\"phone\">Booking Time</th>\n");
					Str.append("<th data-hide=\"phone\">Vehicle</th>\n");
					Str.append("<th data-hide=\"phone\"> Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">CRM Executive</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Service Advisor</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("booking_id") + ")'"
								+ " onmouseout='HideCustomerInfo(" + crs.getString("booking_id") + ")' style='height:200px'>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");

						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("booking_id")).append("</td>\n");
						// Str.append("<td valign=\"top\" align=\"center\"><a href=booking-list.jsp?booking_id=").append(crs.getString("booking_id"));
						// Str.append(">").append(crs.getString("booking_id")).append("</a>\n");
						// Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
						if (!crs.getString("contact_name").equals("")) {
							Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contact_name")).append("</a>");
						}
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("booking_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("booking_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("booking_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("booking_id") + "'  style='display: none;'><a href=\"mailto:")
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

						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("booking_time"))).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">");
						if (!crs.getString("item_id").equals("") && !crs.getString("item_id").equals("0")) {
							Str.append("<a href=../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append(">");
							Str.append(crs.getString("item_name")).append("</a>");
						}
						Str.append("</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" nowrap>\n<a href=vehicle-list.jsp?veh_id=");
						Str.append(crs.getString("veh_id")).append(">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>\n");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("bookingstatus_name")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">\n<a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getString("crmemp_id")).append("\">").append(crs.getString("crmempname")).append("</a>\n");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">\n<a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getString("serviceemp_id")).append("\">").append(crs.getString("serviceempname")).append("</a>\n");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getString("booking_branch_id")).append("\">").append(crs.getString("branchname")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" nowrap=\"nowrap\">");
						Str.append("<a href=\"booking-update.jsp?update=yes&booking_id=").append(crs.getString("booking_id")).append("&veh_id=").append(crs.getString("booking_veh_id"))
								.append("\">Update Booking</a>");
						Str.append("<br><a href=\"booking-item.jsp?update=yes&booking_id=").append(crs.getString("booking_id")).append("\">Update Item</a>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} else {
					Str.append("<br><br><font color=\"red\"><b>No Booking(s) found!</b></font>");
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
