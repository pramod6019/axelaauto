package axela.accounting;
//Divya 3rd Oct

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTax extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managetax.jsp?all=yes>List Taxes</a>:";
	public String LinkListPage = "asset-list.jsp";
	public String LinkExportPage = "tax-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managetax-update.jsp?add=yes>Add New Tax...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String customer_id = "0";
	public String all = "";

	public String smart = "";
	public String group = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{" ID", "numeric", "customer_id"},
			{" Tax", "text", "customer_name"},
			{" Percentage", "text", "customer_rate"},
			{"Entry By", "text", "asset_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "asset_entry_date"},
			{"Modified By", "text", "asset_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "asset_modified_date"},
			{"Custom Fields", "text", "customer_id IN (SELECT cftrans_row_id FROM " + compdb(comp_id) + "axela_cf_trans WHERE cftrans_submodule_id = 30 and cftrans_value"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			CheckPerm(comp_id, "emp_asset_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				smart = PadQuotes(request.getParameter("smart"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND customer_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Taxes!";
				} else if (!customer_id.equals("0")) {
					msg = msg + "<br>Results for Tax";
					StrSearch = StrSearch + " AND customer_id = " + customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND customer_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "Results of Search!";
					if (!GetSession("assetstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("taxstrsql", request);
					}
				}
				// StrSearch += BranchAccess.replace("branch_id", "loc_branch_id");

				SetSession("taxstrsql", StrSearch, request);
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending on search
				StrSql = "SELECT customer_id, customer_name, customer_rate, taxtype_name, taxcat_name, customer_active"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
						+ " WHERE 1=1"
						+ " AND customer_tax = 1";

				CountSql = "SELECT COUNT(DISTINCT customer_id)"
						+ " FROM  " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
						+ " WHERE 1=1";

				StrSql = StrSql + StrJoin;

				if (!StrSearch.equals("")) {
					StrSql = StrSql + StrSearch + " GROUP BY customer_id"
							+ " ORDER BY customer_id DESC";
					CountSql = CountSql + StrSearch;
				}
				// SOP("StrSql==" + StrSqlBreaker(StrSql));
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Taxes";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "managetax.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM  " + compdb(comp_id) + "axela_customer\\b",
								" FROM  " + compdb(comp_id) + "axela_customer"
										+ " INNER JOIN (SELECT customer_id FROM  " + compdb(comp_id) + "axela_customer"
										+ " WHERE 1=1" + StrSearch
										+ " GROUP BY customer_id"
										+ " ORDER BY customer_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (customer_id)");
						// StrSql = "SELECT * FROM (" + StrSql + ") AS datatable";
						StrSql += " ORDER BY customer_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Tax</th>\n");
					// Str.append("<th>Ledger</th>\n");
					Str.append("<th data-hide=\"phone\">Rate</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Type</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Category</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						if (crs.getString("customer_active").equals("0")) {
							Str.append("<td valign=top align=left nowrap>").append(crs.getString("customer_name"));
							Str.append("<br><font color=\"red\"><b>[Inactive]</b></font>").append("</td>\n");
						} else {
							Str.append("<td valign=top align=left nowrap>").append(crs.getString("customer_name")).append("</td>\n");
						}
						// Str.append("<td valign=top align=left nowrap>").append("<a href=\"ledger-list.jsp?customer_id=").append(crs.getString("tax_customer_id")).append(" \">");
						// Str.append(crs.getString("customer_name")).append("</a>").append("</td>\n");
						Str.append("<td valign=top align=right nowrap>").append(crs.getString("customer_rate")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("taxtype_name")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("taxcat_name")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append("<a href=\"managetax-update.jsp?update=yes&customer_id=").append(crs.getString("customer_id"))
								.append(" \">Update Tax</a>");
						Str.append("</td>\n</tr>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Taxs found!</font><br><br>";
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
