package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class FAQ_Executive extends Connect {

	public String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String StrSearch = "";
	public String faq_cat_id = "";
	public String txt_search = "";
	public String drop_search = "";
	public String search = "";
	public String all = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));

				if ("yes".equals(search)) {
					GetValues(request, response);
					if (!txt_search.equals("")) {
						msg = "Result for Search = " + txt_search + "!";
						StrSearch = StrSearch + " and (faq_question like '%" + txt_search + "%'";
						StrSearch = StrSearch + " or faq_answer like '%" + txt_search + "%'";
						StrSearch = StrSearch + ") ";
					}
					if (!faq_cat_id.equals("0")) {
						StrSearch = StrSearch + " and faq_cat_id =" + CNumeric(faq_cat_id) + "";
					}
				}
				StrHTML = ListData();
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			txt_search = PadQuotes(request.getParameter("txt_search"));
			faq_cat_id = PadQuotes(request.getParameter("dr_faq_cat_id"));
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";

		try {
			// if (msg.equals("")) {
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			StrSql = "select faq_id, faq_question, faq_answer from " + compdb(comp_id) + "axela_faq where 1=1 and faq_active='1' ";
			CountSql = "select count(distinct faq_id) from " + compdb(comp_id) + "axela_faq where 1=1 and faq_active='1' ";
			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			// SOP("TotalRecords=="+TotalRecords);

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " FAQ(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "faq-executive.jsp?" + QueryString + "&PageCurrent=";
				if (recperpage != 0) {
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
				}
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by faq_rank limit " + (StartRec - 1) + ", " + recperpage + "";
				CachedRowSet crs =processQuery(StrSql, 0);
				count = StartRec - 1;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=listtable>");
				Str.append("<tr>");
				Str.append("<th width=5%>#</th>");
				Str.append("<th>FAQs</th>");
				Str.append("</tr>");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td align=center valign=top><b>").append(count).append(".</b></td>");
					Str.append("<td><span id=\"s").append(count).append("\">[+]</span> <a href=\"javascript:ToggleDisplay(").append(count).append(");\">").append(crs.getString("faq_question"))
							.append("</a>" + "<div id=\"d").append(count).append("\" style=\"display: none;\">").append(crs.getString("faq_answer")).append("");
					// if (!crs.getString("faq_value").equals("")) {
					// Str.append("<br><a href=../portal/Fetchdocs.do?faqexe_id=" + crs.getString("faq_id") + "><b>" + crs.getString("faq_title") + "</b></a>");
					// }
					Str.append("</div>");
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</table>");
				crs.close();
			} else {
				Str.append("<font color=red><b><br><br><br><br>No FAQs found!</b></font>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCategory() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT cat_id, cat_name from " + compdb(comp_id) + "axela_faq_cat "
					+ " where 1=1 order by cat_name ";
			CachedRowSet crs =processQuery(StrSql, 0);
			Str.append("<option value=0").append(StrSelectdrop("0", faq_cat_id)).append(">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("cat_id"), faq_cat_id));
				Str.append(">").append(crs.getString("cat_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
