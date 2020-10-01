package axela.insurance;
//Bhagwan Singh 27th july 2013

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Doc_List extends Connect {

	public String insurpolicy_id = "0";
	public String name = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String msg = "";
	public String doc_id = "0";
	public String all = "";
	public String search = "";
	public String StrSearch = "";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				insurpolicy_id = CNumeric(PadQuotes(request.getParameter("insurpolicy_id")));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
				all = PadQuotes(request.getParameter("all"));
				name = ExecuteQuery("SELECT insurpolicy_id"
						+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
						+ " WHERE insurpolicy_id = " + insurpolicy_id + "");
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND doc_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Documents";
					StrSearch = StrSearch + " AND doc_id > 0";
				} else if (!doc_id.equals("0")) {
					StrSearch = StrSearch + " AND doc_id = " + doc_id + "";
				}
				StrHTML = ListDocs();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		int PageListSize = 10;

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks ";

		CountSql = "SELECT COUNT(DISTINCT(doc_id))";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_docs"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy ON insurpolicy_id = doc_insur_id"
				+ " WHERE doc_insur_id = " + insurpolicy_id + "";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (!StrSearch.equals("")) {
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
			PageURL = "insurance-docs-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount++;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql += " ORDER BY doc_id DESC"
					+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th width=\"5%\">#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th width=\"20%\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					if (!crs.getString("doc_value").equals("")) {
						File f = new File(ContractDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=top align=left>");
						Str.append("<a href=../Fetchdocs.do?insur_doc_id=").append(crs.getString("doc_id")).append(">");
						Str.append(crs.getString("doc_title")).append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a><br> ");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					} else {
						Str.append("<td valign=top align=left>");
						Str.append(crs.getString("doc_title")).append(" (0 Bytes)<br>");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					}
					if (!insurpolicy_id.equals("0")) {
						Str.append("<td valign=top align=left >");
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&insurpolicy_id=").append(insurpolicy_id);
						Str.append("&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a></td>\n");
					}
				}
				crs.close();
				Str.append("</tr></tbody>\n</table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
