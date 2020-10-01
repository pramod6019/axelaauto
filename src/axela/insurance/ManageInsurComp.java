package axela.insurance;

/**
 * @author Dilip Kumar
 */
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageInsurComp extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=manageinsurcomp.jsp?all=yes>List Insurance Companies</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageinsurcomp-update.jsp?add=yes>Add Insurance Company...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String inscomp_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Company ID", "numeric", "inscomp_id"},
			{"Company Name", "text", "inscomp_name"},
			{"Active", "boolean", "inscomp_active"},
			{"Entry By", "text", "inscomp_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "inscomp_entry_date"},
			{"Modified By", "text", "inscomp_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "inscomp_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				inscomp_id = CNumeric(PadQuotes(request.getParameter("inscomp_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete") && !msg.toLowerCase().contains("document")) {
					StrSearch = " AND inscomp_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Insurance Company(s)!";
					StrSearch = StrSearch + " AND inscomp_id > 0";
				} else if (!(inscomp_id.equals("0"))) {
					msg = msg + "<br>Results for Insurance Insurance Company ID = " + inscomp_id + "!";
					StrSearch = StrSearch + " AND inscomp_id = " + inscomp_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND inscomp_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "SELECT inscomp_id, inscomp_name, inscomp_title, inscomp_value, inscomp_active";

			CountSql = "SELECT COUNT(DISTINCT inscomp_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin + StrSearch;
			CountSql = CountSql + SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Insurance Company(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				// PageURL = "managecontractservice.jsp?" + QueryString + "&PageCurrent=";
				PageURL = "manageinsurcomp.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " ORDER BY inscomp_id DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th >#</th>\n");
					Str.append("<th >Insurance Company Details</th>\n");
					Str.append("<th data-toggle=\"true\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td style='text-align:left'>").append(crs.getString("inscomp_name"));
						if (crs.getString("inscomp_active").equals("0")) {
							Str.append("<font color=red><b> [Inactive]</b></font>");
						}
						if (!crs.getString("inscomp_value").equals("")) {
							if (!new File(InsurCompDocPath(comp_id)).exists()) {
								new File(InsurCompDocPath(comp_id)).mkdirs();
							}

							File f = new File(InsurCompDocPath(comp_id) + crs.getString("inscomp_value"));
							Str.append("<br><a href=\"../Fetchdocs.do?").append(QueryString).append("&inscomp_id=").append(crs.getString("inscomp_id")).append("\">");
							Str.append(crs.getString("inscomp_title")).append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a>");
						}
						Str.append("</td>\n");
						Str.append("<td style='text-align:left'><a href=\"manageinsurcomp-update.jsp?update=yes&inscomp_id=").append(crs.getString("inscomp_id")).append(" \">Update Insurance Company</a>\n");
						Str.append("<br><a href=\"insurcomp-doc.jsp?inscomp_id=").append(crs.getString("inscomp_id")).append(" \">Update Document</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Insurance Company(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
