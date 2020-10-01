package axela.sales;
/*saiman 27th june 2012 */

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Veh_Quote_Discount_List extends Connect {
	// ////// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Sales</a>"
			+ " &gt; <a href=veh-quote-list.jsp?all=yes>Quote List</a>";
	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String BranchAccess = "";
	public String StrHTML = "", emp_id = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String quotediscount_id = "";
	private String quote_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Discount ID", "numeric", "quotediscount_id"},
			{"Quote ID", "numeric", "quote_id"},
			{"Amount", "numeric", "quotediscount_requestedamount"},
			{"Request Consultant", "text", "quotediscount_request_emp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Authorize Consultant", "text", "quotediscount_authorize_emp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Status", "boolean", "quotediscount_authorize_status"},
			{"Entry By", "text", "quotediscount_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "quotediscount_entry_date"},
			{"Modified By", "text", "quotediscount_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "quotediscount_modified_date"}
	};

	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_discount_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				quotediscount_id = CNumeric(PadQuotes(request.getParameter("quotediscount_id")));

				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				LinkAddPage = "<a href=veh-quote-discount-update.jsp?add=yes&quote_id=" + quote_id + ">Add New Discount Authorization...</a>";
				LinkHeader += " &gt; <a href=veh-quote-discount-list.jsp?quote_id=" + quote_id + ">List Discount Authorization</a>:";

				// if (msg.toLowerCase().contains("delete")) {
				// StrSearch = " AND quotediscount_id = 0";
				// } else
				if ("yes".equals(all)) {
					msg = "Results for all Discounts!";
					StrSearch = StrSearch + " AND quotediscount_id > 0 ";
				} else if (!(quotediscount_id.equals("0"))) {
					msg = msg + "<br>Results for Quote Discount ID = " + quotediscount_id + "!";
					StrSearch = StrSearch + " AND quotediscount_id = " + quotediscount_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND quotediscount_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("quotediscountstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("quotediscountstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("quotediscountstrsql", StrSearch, request);
				}
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

		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		String StrJoin = "";
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		StrSql = "SELECT quotediscount_id, quotediscount_quote_id, quotediscount_requestedamount, quotediscount_request_emp_id,"
				+ " quotediscount_authorize_emp_id, quotediscount_authorize_time, quotediscount_authorize_status,"
				+ " quotediscount_entry_id, quotediscount_entry_date, quotediscount_modified_id, quotediscount_modified_date,"
				+ " quote_id,"
				+ " quote_emp_id,"
				+ " quotediscount_entry_date,"
				+ " CONCAT(quoteemp.emp_name, ' (', quoteemp.emp_ref_no, ')') AS quote_emp_name,"
				+ " CONCAT(reqemp.emp_name, ' (', reqemp.emp_ref_no, ')') AS reqemp_name,"
				+ " COALESCE(reqemp.emp_id, 0) AS reqemp_id,"
				+ "	CONCAT(authorizeemp.emp_name, ' (', authorizeemp.emp_ref_no, ')') AS authorizeemp_name,"
				+ " COALESCE(authorizeemp.emp_id, 0) AS authorizeemp_id";

		StrJoin = " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote on quote_id = quotediscount_quote_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp quoteemp on quoteemp.emp_id = quote_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp reqemp on reqemp.emp_id = quotediscount_request_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp authorizeemp on authorizeemp.emp_id = quotediscount_authorize_emp_id"
				+ " WHERE 1 = 1"
				+ " AND quotediscount_quote_id = " + quote_id + "";

		CountSql = "SELECT COUNT(DISTINCT(quotediscount_id))";

		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;

		if (!StrSearch.equals("")) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		// SOP("StrSql------> " + StrSql);

		CountSql = ExecuteQuery(CountSql);
		if (!CountSql.equals("")) {
			TotalRecords = Integer.parseInt(CountSql);
		} else {
			TotalRecords = 0;
		}

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Discounts";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "veh-quote-discount-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			if (all.equals("yes")) {
				StrSql = StrSql + " GROUP BY quotediscount_id ORDER BY quotediscount_id DESC";
			} else {
				StrSql = StrSql + " GROUP BY quotediscount_id";
			}
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql===list=" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">ID</th>\n");
				Str.append("<th>Quote ID</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Authorize Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Quote Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Request Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Authorize Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Requested Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Authorized Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					LinkAddPage = "<a href=veh-quote-discount-update.jsp?add=yes&quote_id=" + crs.getString("quote_id") + ">Add New Discount Authorization...</a>";
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\">").append(crs.getString("quotediscount_id")).append("</td>\n");
					Str.append("<td align=\"center\">");
					Str.append("<a href=\"veh-quote-list.jsp?quote_id=").append(crs.getString("quote_id")).append("\">");
					Str.append(crs.getString("quote_id")).append("</a><br>");
					Str.append("</td>\n");
					Str.append("<td align=\"right\">").append(IndDecimalFormat(df.format(crs.getDouble("quotediscount_requestedamount")))).append("</td>\n");
					Str.append("<td align=\"center\">");
					if (!crs.getString("quotediscount_authorize_status").equals("0")) {
						if (crs.getString("quotediscount_authorize_status").equals("1")) {
							Str.append("<font color=\"red\">[Authorized]</font><br>");
						} else if (crs.getString("quotediscount_authorize_status").equals("2")) {
							Str.append("<font color=\"red\">[Not Authorized]</font><br>");
						}
					} else {
						Str.append("<font color=\"red\">[Pending]</font><br>");
					}
					// }
					Str.append("</td>");
					Str.append("<td>").append(ExeDetailsPopover(crs.getInt("quote_emp_id"), crs.getString("quote_emp_name"), "")).append("</td>");
					Str.append("<td>").append(ExeDetailsPopover(crs.getInt("reqemp_id"), crs.getString("reqemp_name"), "")).append("</td>");
					Str.append("<td>").append(ExeDetailsPopover(crs.getInt("authorizeemp_id"), crs.getString("authorizeemp_name"), "")).append("</td>");
					Str.append("<td>").append(strToLongDate(crs.getString("quotediscount_entry_date"))).append("</td>");
					Str.append("<td>").append(strToLongDate(crs.getString("quotediscount_authorize_time"))).append("</td>");

					Str.append("<td align=\"left\">");
					Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
							+ "<i class='fa fa-pencil'></i></button>"
							+ "<ul class='dropdown-content dropdown-menu pull-right'>"
							+ "<li role=presentation><a href=\"veh-quote-discount-update.jsp?update=yes&quote_id="
							+ crs.getString("quote_id") + "&quotediscount_id=" + crs.getString("quotediscount_id")
							+ " \">Update Discount Authorization</a></li>"
							+ "<li role=presentation><a href=\"veh-quote-discount-authorize.jsp?Update=yes&quote_id="
							+ crs.getString("quote_id") + "&quotediscount_id=" + crs.getString("quotediscount_id")
							+ "&quotediscount_authorize_status=" + crs.getString("quotediscount_authorize_status")
							+ " \">Authorize Discount</a></li>"
							+ "</ul></div></center></div>");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><font color=\"red\"><b>No Quote Discount Authorization(s) found!</b></font>");
		}
		return Str.toString();
	}
}
