package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Group_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt;"
			+ " <a href='../accounting/index.jsp'>Accounting</a> &gt;"
			+ " <a href=group-list.jsp?all=yes>List Groups</a><b>:</b>";
	public String LinkListPage = "group-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href='group-update.jsp?add=yes' data-target=#Hintclicktocall data-toggle=modal>Add Group...</a>";

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
	public String accgroup_id = "0";
	public String PageCurrents = "";
	public String QueryString = "";
	public String strq = "";
	public String accgrouppop_id = "0";
	public String all = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Group ID", "numeric", "accgrouppop_id"},
			{"Group Name", "text", "accgrouppop_name"},
			{"Entry By", "text", "accsubgroup_entry_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Entry Date", "date", "accsubgroup_entry_date"},
			{"Modified By", "text", "accsubgroup_modified_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Modified Date", "date", "accsubgroup_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request)) + "";
			CheckPerm(comp_id, "emp_role_id,emp_acc_group_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				accgrouppop_id = CNumeric(PadQuotes(request.getParameter("accgrouppop_id")));
				accgroup_id = CNumeric(PadQuotes(request.getParameter("accgroup_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Group!";
				} else if (!(accgrouppop_id.equals("0"))) {
					msg = msg + "<br>Results for Group ID = " + accgrouppop_id + "!";
					StrSearch = StrSearch + " AND accgrouppop_id = ?";
				} else if (!accgroup_id.equals("0")) {
					msg = msg + "<br>Result for Group ID: " + accgroup_id + "!";
					StrSearch = StrSearch + " AND accgroup_id = " + accgroup_id + "";
				}
				else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND accsubgroup_id = 0 ";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("accgroupstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int

		// to know no of records depending on search
		StrSql = "SELECT accgrouppop_id, accgrouppop_name,"
				+ " IF ( accgrouppop_alie = 1, 'Assets',"
				+ " IF ( accgrouppop_alie = 2, 'Liabilities',"
				+ " IF ( accgrouppop_alie = 3, 'Income',"
				+ " IF ( accgrouppop_alie = 4, 'Expense',"
				+ " IF ( accgrouppop_alie = 5, 'Owners Equity',"
				+ " '' ) ) ) ) ) AS accgrouppop_alie, accgroup_notes"
				+ " FROM " + compdb(comp_id) + "axela_acc_group_pop"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
				+ " WHERE 1 = 1" + StrSearch;
		StrSql += " GROUP BY accgrouppop_id"
				+ " ORDER BY accgrouppop_id DESC";
		// SOP("Listdata======" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs != null) {
			try {

				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th align=center>ID</th>\n");
				Str.append("<th>Group</th>\n");
				Str.append("<th width='25%'>Parent Group</th>\n");
				Str.append("<th width='25%'>Description</th>\n");
				Str.append("<th data-hide=\"phone\">Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					String temp_group_name = PadQuotes(crs.getString("accgrouppop_name"));
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("accgrouppop_id")).append("</td>\n");
					if (temp_group_name.contains(">")) {
						Str.append("<td valign=top>").append(temp_group_name.substring(temp_group_name.lastIndexOf(">") + 1, temp_group_name.length())).append("</td>\n");
					}
					else {
						Str.append("<td valign=top>").append(temp_group_name).append("</td>\n");
					}
					if (temp_group_name.contains(">")) {
						Str.append("<td valign=top>").append(temp_group_name.substring(0, temp_group_name.lastIndexOf(" >")) + "<b>:</b>").append("</td>\n");
					}
					else {
						Str.append("<td valign=top> </td>\n");
					}
					Str.append("<td valign=top>").append(crs.getString("accgroup_notes")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("accgrouppop_alie")).append("</td>\n");
					Str.append("<td valign=top nowrap align=right> <a href='group-update.jsp?update=yes&accgroup_id=" + crs.getString("accgrouppop_id")
							+ "' data-target=#Hintclicktocall data-toggle=modal>Update Group</a></td>");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><br><br><font color=red><b>No Sub Group(s) Found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
