package axela.accounting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageVoucherClass extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managevoucherclass.jsp?all=yes>List Voucher Class(s)</a>:";
	public String LinkListPage = "managevoucherclass.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managevoucherclass-update.jsp?add=yes>Add Voucher Class...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;

	public String PageCurrents = "";
	public String QueryString = "";
	public String strq = "";
	public String entity_id = "0";
	public String voucherclass_id = "0";
	public String all = "";
	public String advSearch = "";
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	int prepkey = 1;
	public Smart SmartSearch = new Smart();

	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Voucher ID", "numeric", "voucherclass_id"},
			{"Label", "text", "vouchertype_label"},
			{"Name", "text", "vouchertype_name"},
			{"Prefix", "text", "vouchertype_prefix"},
			{"Suffix", "text", "vouchertype_suffix"},
			{"Entry By", "text", "vouchertype_entry_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Entry Date", "date", "vouchertype_entry_date"},
			{"Modified By", "text", "vouchertype_modified_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vouchertype_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				emp_id = CNumeric(GetSession("emp_id", request));
				entity_id = CNumeric(GetSession("entity_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND voucherclass_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Voucher Class(s)!";
					StrSearch = StrSearch + " and voucherclass_id > 0";
				} else if (!(voucherclass_id.equals("0"))) {
					msg = msg + "<br>Results for Voucher Class ID = " + voucherclass_id + "!";
					StrSearch = StrSearch + " and voucherclass_id = ?";
					prepmap.put(prepkey++, voucherclass_id);
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " and voucherclass_id = 0 ";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("jobtitlestrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
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
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "SELECT voucherclass_id, voucherclass_name"
				// + " vouchertype_prefix, vouchertype_suffix"
				+ " FROM axela_acc_voucher_class "
				+ " WHERE 1 = 1";
		CountSql = "SELECT COUNT(DISTINCT voucherclass_id) FROM axela_acc_voucher_class where 1 = 1";

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		prepmap.clear();
		TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, prepmap, 0));
		prepmap.clear();

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Voucher Class(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "managevoucherclass.jsp?" + strq + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by voucherclass_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			try {
				prepmap.clear();
				CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
				prepmap.clear();
				int count = StartRec - 1;
				Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				// Str.append("<th>Label</th>\n");
				Str.append("<th>Name</th>\n");
				// Str.append("<th>Prefix</th>\n");
				// Str.append("<th>Suffix</th>\n");
				Str.append("<th width=20%>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					// Str.append("<td valign=top>").append(crs.getString("vouchertype_label")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("voucherclass_name")).append("</td>\n");
					// Str.append("<td valign=top>").append(crs.getString("vouchertype_prefix")).append("</td>\n");
					// Str.append("<td valign=top>").append(crs.getString("vouchertype_suffix")).append("</td>\n");
					Str.append("<td valign=top nowrap> " + " <a href=\"managevoucherclass-update.jsp?update=yes&voucherclass_id=").append(crs.getString("voucherclass_id"))
							.append(" \">UPDATE  " + compdb(comp_id) + "Voucher Class</a></td>\n");
				}
				crs.close();
				prepmap.clear();
				Str.append("</tr>\n");
				Str.append("</table>\n");
			} catch (Exception ex) {
				SOP(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red><b>No Voucher Class(s) Found!</b></font><br><br>";
		}
		return Str.toString();
	}
}
