//Smitha Nag 18 feb 2013
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managecourtesyvehicaloutage extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Master</a> &gt; <a href=managecourtesyvehicalcourtesyoutage.jsp?all=yes>List Test Drive courtesyoutage</a>:";
	public String LinkListPage = "managecourtesyvehicalcourtesyoutage.jsp";
	public String LinkExportPage = "testdrivecourtesyoutage.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=Managecourtesyvehicalcourtesyoutage-update.jsp?add=yes>Add Courtesy vehical courtesyoutage...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String smart = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String PageURL = "";
	public String StrSql = "";
	public String StrSearch = "";
	public Smart SmartSearch = new Smart();
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	// public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String courtesyoutage_id = "0";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"courtesyoutage ID", "numeric", "courtesyoutage_id"},
			{"Vehicle ID", "numeric", "courtesyoutage_altveh_id"},
			{"From time", "date", "courtesyoutage_fromtime"},
			{"To Time", "date", "courtesyoutage_totime"},
			{"Description", "text", "courtesyoutage_desc"},
			{"Notes", "text", "courtesyoutage_notes"},
			{"Entry ID", "numeric", "courtesyoutage_entry_id"},
			{"Entry Date", "date", "courtesyoutage_entry_date"},
			{"Modified ID", "numeric", "courtesyoutage_modified_id"},
			{"Modified Date", "date", "courtesyoutage_modified_date"}
	};
	public String branch_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				courtesyoutage_id = CNumeric(PadQuotes(request.getParameter("courtesyoutage_id")));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " and courtesyoutage_id = 0";
				} else if ("yes".equals(all)) // for all students to b displayed
				{
					msg = msg + "<br>Results for All Test Drive courtesyoutage!";
					StrSearch = StrSearch + " and courtesyoutage_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if (!courtesyoutage_id.equals("0")) {
					msg = msg + "<br>Results for Test Drive courtesyoutage ID = " + courtesyoutage_id + "!";
					StrSearch = StrSearch + " and courtesyoutage_id = " + courtesyoutage_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("courtesyoutagestrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("courtesyoutagestrsql", StrSearch, request);
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			if (!msg.equals("")) {
				StrSql = "Select courtesyoutage_id, veh_name, branch_code, courtesyoutage_veh_id, branch_id,"
						+ " CONCAT(branch_name,' (',branch_code,')') as branchname, courtesyoutage_desc,"
						+ " coalesce(model_name, '') as  model_name,"
						+ " courtesyoutage_fromtime, courtesyoutage_totime, courtesyoutage_desc, courtesyoutage_desc";
				CountSql = " SELECT Count(distinct courtesyoutage_id)";
				SqlJoin = " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle_courtesyoutage"
						+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on veh_id = courtesyoutage_veh_id"
						+ " left join " + compdb(comp_id) + "axela_inventory_item on item_id = veh_variant_id"
						+ " inner Join " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id"
						+ " where 1=1 " + BranchAccess;
				StrSql = StrSql + SqlJoin;
				// SOP("....StrSql....."+StrSql);
				CountSql = CountSql + SqlJoin;
				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch;
					CountSql = CountSql + StrSearch;
				}
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Test Drive courtesyoutage(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "testdrivecourtesyoutage-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql + " GROUP BY courtesyoutage_id ORDER BY courtesyoutage_id desc";
					} else {
						StrSql = StrSql + " GROUP BY courtesyoutage_id ORDER BY courtesyoutage_id";
					}
					StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					try {
						int count = StartRec - 1;
						Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
						Str.append("<tr align=center>\n");
						Str.append("<th width=5%>#</th>\n");
						Str.append("<th>Test Drive courtesyoutage Details</th>\n");
						Str.append("<th>Vehicle</th>\n");
						if (branch_id.equals("0")) {
							Str.append("<th>Branch</th>\n");
						}
						Str.append("<th width=20%>Actions</th>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td valign=top align=center>").append(count).append("</td>\n");
							Str.append("<td valign=top><b>Vehicle courtesyoutage ID: ").append(crs.getString("courtesyoutage_id")).append("</b>");
							// if (crs.getString("courtesyoutage_fromtime").equals(""));
							if (!crs.getString("courtesyoutage_fromtime").equals("")) {
								Str.append("<br>From: ").append(strToLongDate(crs.getString("courtesyoutage_fromtime"))).append("");
							}
							if (!crs.getString("courtesyoutage_totime").equals("")) {
								Str.append("<br>To: ").append(strToLongDate(crs.getString("courtesyoutage_totime"))).append("");
							}
							if (!crs.getString("courtesyoutage_desc").equals("")) {
								Str.append("<br>").append(crs.getString("courtesyoutage_desc")).append("");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top>Model: <b>").append(crs.getString("model_name")).append("</b>" + "<br>Vehicle: <b>").append(crs.getString("veh_name")).append("</b>");
							Str.append("</td>");
							if (branch_id.equals("0")) {
								Str.append("<td valign=top><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">").append(crs.getString("branchname"))
										.append("</a></td>");
							}
							Str.append("<td valign=top><a href=\"testdrivecourtesyoutage-update.jsp?update=yes&courtesyoutage_id=").append(crs.getString("courtesyoutage_id"))
									.append("\">Update Test Drive courtesyoutage</a>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</table>\n");
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					Str.append("<br><br><br><br><font color=red><b>No Test Drive courtesyoutage(s) Found!</b></font><br><br>");
				}
			}
		}
		return Str.toString();
	}
}
