package axela.accounting;
//Divya 3rd Oct

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManagePrincipalSupport extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageprincipalsupport.jsp?all=yes>List Principal Support</a>:";
	// public String LinkListPage = "asset-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=manageprincipalsupport-update.jsp?add=yes>Add New Principal Support...</a>";
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
	public String principalsupport_id = "0";
	public String all = "";

	public String smart = "";
	public String group = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "principalsupport_id"},
			{"Entry By", "text", "asset_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "asset_entry_date"},
			{"Modified By", "text", "asset_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "asset_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			CheckPerm(comp_id, "emp_principal_support_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				principalsupport_id = CNumeric(PadQuotes(request.getParameter("principalsupport_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				smart = PadQuotes(request.getParameter("smart"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND principalsupport_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Principal Support!";
				} else if (!principalsupport_id.equals("0")) {
					msg = msg + "<br>Results for Principal Support";
					StrSearch = StrSearch + " AND principalsupport_id = " + principalsupport_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND principalsupport_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "Results of Search!";
					if (!GetSession("stocksupportstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("stocksupportstrsql", request);
					}
				}
				// StrSearch += BranchAccess.replace("branch_id", "loc_branch_id");

				SetSession("stocksupportstrsql", StrSearch, request);

				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				StrSql = "SELECT principalsupport_id, principalsupport_brand_id, brand_name,"
						+ " principalsupport_model_id, model_name, principalsupport_fueltype_id, fueltype_name,"
						+ " DATE_FORMAT(principalsupport_month,'%M') AS principalsupport_month,"
						+ " principalsupport_insurance, principalsupport_cashdiscount, principalsupport_exchange,"
						+ " principalsupport_corporate, principalsupport_loyalty,"
						// + " principalsupport_eb1, principalsupport_eb2, principalsupport_eb3,"
						+ " principalsupport_entry_id, principalsupport_entry_date"
						+ " FROM " + compdb(comp_id) + "axela_principal_support"
						+ " INNER JOIN axela_brand ON brand_id = principalsupport_brand_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = principalsupport_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = principalsupport_fueltype_id "
						+ " WHERE 1 = 1";

				CountSql = "SELECT COUNT(DISTINCT principalsupport_id)"
						+ " FROM  " + compdb(comp_id) + "axela_principal_support"
						+ " INNER JOIN axela_brand ON brand_id = principalsupport_brand_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = principalsupport_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = principalsupport_fueltype_id "
						+ " WHERE 1 = 1";

				StrSql = StrSql + StrJoin;

				if (!StrSearch.equals("")) {
					StrSql = StrSql
							+ StrSearch + " GROUP BY principalsupport_id"
							+ " ORDER BY principalsupport_id";
					CountSql = CountSql + StrSearch;
				}
				// SOP("StrSql====" + StrSql);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Principal Support";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "managestocksupport.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM  " + compdb(comp_id) + "axela_principal_support\\b",
								" FROM  " + compdb(comp_id) + "axela_principal_support"
										+ " INNER JOIN axela_brand ON brand_id = principalsupport_brand_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = principalsupport_model_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = principalsupport_fueltype_id"
										+ " WHERE 1 = 1"
										// + StrSearch
										+ " GROUP BY principalsupport_id"
										+ " ORDER BY principalsupport_id"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (principalsupport_id)");
						// StrSql = "SELECT * FROM (" + StrSql + ") AS datatable";
						StrSql += " ORDER BY principalsupport_id DESC";

					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					// SOP("StrSql-------" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th>Brand</th>\n");
					Str.append("<th data-hide=\"phone\">Model</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Fueltype</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Month</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("brand_name"));
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("model_name")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("fueltype_name")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("principalsupport_month")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>").append("<a href=\"manageprincipalsupport-update.jsp?update=yes&principalsupport_id=")
								.append(crs.getString("principalsupport_id")).append(" \">Update Principal Support</a>").append("</td>\n");
						Str.append("</tr>\n");

					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Principal Support found!</font><br><br>";
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
