package axela.preowned;

////sangita
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_Docs extends Connect {

	public String preowned_id = "0";
	public String preowned_title = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String QueryString = "";
	public String msg = "";
	public String doc_id = "0";
	public String all = "";
	public String StrSearch = "";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
				all = PadQuotes(request.getParameter("all"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND doc_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Documents";
					StrSearch = StrSearch + " and doc_id > 0";
				} else if (!(doc_id.equals("0"))) {
					msg = msg + "<br>Results for Doc ID = " + doc_id + "!";
					StrSearch = StrSearch + " and doc_id =" + doc_id + "";
				}
				StrSql = "select  preowned_title "
						+ " from " + compdb(comp_id) + "axela_preowned "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id "
						+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
						+ " where 1=1 and preowned_id =" + preowned_id + BranchAccess + ExeAccess + ""
						+ " group by preowned_id "
						+ " order by preowned_id desc ";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
					}
					StrHTML = ListDocs();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Pre Owned!");
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		String CountSql = "";
		String SqlJoin = "";
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		int PageListSize = 10;

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		StrSql = " Select doc_id, doc_value, doc_title, doc_remarks ";

		CountSql = " SELECT Count(doc_id)";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_docs "
				+ " where 1=1 "
				+ " and doc_preowned_id=" + preowned_id + "";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "preowned-dash-docs.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by doc_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;
				// Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				// Str.append("<tr align=center>\n");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Document Details</th>\n");
				Str.append("<th data-toggle=\"true\" width=20%>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(PreownedDocPath(comp_id)).exists()) {
							new File(PreownedDocPath(comp_id)).mkdirs();
						}
						File f = new File(PreownedDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "doc_preowned_id=").append(crs.getString("doc_id")).append(">").append(crs.getString("doc_title"))
								.append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a><br> ").append(crs.getString("doc_remarks")).append("</td>\n");
					} else {
						Str.append("<td valign=top align=left >").append(crs.getString("doc_title")).append(" (").append(crs.getString("doc_id")).append(") (0 Bytes)<br> ")
								.append(crs.getString("doc_remarks")).append("</td>\n");
					}
					if (!preowned_id.equals("0")) {
						Str.append("<td valign=top align=left ><a href=\"../portal/docs-update.jsp?update=yes&preowned_id=").append(preowned_id).append("&doc_id=").append(crs.getString("doc_id"))
								.append("\">Update Document</a></td>\n");
					}
					Str.append("</tr>\n");

				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Document(s) found!</b></font></center><br><br><br><br><br>");
		}
		return Str.toString();
	}
}
