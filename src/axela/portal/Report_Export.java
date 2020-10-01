package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Export extends Connect {

	public String emp_idsession = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String emp_name = "";
	public String emp_namesession = "";
	public String StrHTML = "";
	public String BranchAccess = "", ExeAccess = "";
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
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "";
	public String go = "";
	public String export_value = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_executive_access", request, response);
			emp_idsession = CNumeric(PadQuotes(GetSession("emp_id", request) + ""));
			emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
			recperpage = Integer.parseInt(GetSession("emp_recperpage", request) + "");
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			search = PadQuotes(request.getParameter("search_button"));
			start_time = PadQuotes(request.getParameter("txt_starttime"));
			end_time = PadQuotes(request.getParameter("txt_endtime"));
			export_value = PadQuotes(request.getParameter("dr_export_value"));
			go = PadQuotes(request.getParameter("submit_button"));
			msg = PadQuotes(request.getParameter("msg"));
			emp_name = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_idsession + "");
			GetValues(request, response);
			CheckForm();
			StrSearch = StrSearch + "AND  SUBSTR(export_time,1,8) >= SUBSTR('" + ConvertShortDateToStr(start_time) + "',1,8)"
					+ " AND SUBSTR(export_time,1,8) <= SUBSTR('" + ConvertShortDateToStr(end_time) + "',1,8)";
			if (!emp_id.equals("0")) {
				StrSearch += " AND export_emp_id =" + emp_id + "";
			}
			if (!export_value.equals("0")) {
				StrSearch += " AND export_value = '" + export_value + ".xlsx'";
			}
			//
			if (!msg.equals("")) {
				msg = "Error!" + msg;
			}
			if (msg.equals("")) {
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			starttime = PadQuotes(request.getParameter("txt_starttime"));
			endtime = PadQuotes(request.getParameter("txt_endtime"));
			if (starttime.equals("")) {
				starttime = strToShortDate(ToShortDate(kknow()));
			}
			if (endtime.equals("")) {
				endtime = strToShortDate(ToShortDate(kknow()));
			}
			// if (emp_idsession.equals("0")) {
			emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql, SqlJoin = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search
		StrSql = " SELECT export_emp_id, export_id, export_value, export_time, emp_name, emp_ref_no, "
				+ " COALESCE(jobtitle_desc, '') AS jobtitle_desc";

		CountSql = " SELECT COUNT(DISTINCT export_id) ";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_emp_export "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = export_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
				+ " WHERE 1 = 1 "
				+ ExeAccess;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + SqlJoin + StrSearch;
			CountSql = CountSql + SqlJoin + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Record(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "report-export.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " ORDER BY export_time DESC";
			// StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql = " + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql);

				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-hover\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
				Str.append("<th>Job Description</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>File Name</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("jobtitle_desc")).append("</td>\n");

					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("export_emp_id")).append(">").append(crs.getString("emp_name"))
							.append("" + " (").append(crs.getString("emp_ref_no")).append(")</a>").append("</td>\n");
					Str.append("<td valign=top align=left>").append("<a href=../Fetchdocs.do?export_id=").append(crs.getString("export_id")).append(">").append(crs.getString("export_value"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=center>").append(strToLongDate(crs.getString("export_time"))).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><br><br><b><font color=red>No Record(s) found!</font></b><br><br>");
		}
		return Str.toString();
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id != '1'"
					+ " AND emp_active = 1"
					+ ExeAccess;
			StrSql = StrSql + " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"dropdown form-control\">");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDocumentType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT REPLACE(export_value,'.xlsx','')  AS export_value"
					+ " FROM " + compdb(comp_id) + "axela_emp_export"
					+ " WHERE 1 = 1"
					+ " GROUP BY export_value";
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<select name=\"dr_export_value\" id=\"dr_export_value\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("export_value")).append("");
				Str.append(StrSelectdrop(crs.getString("export_value"), export_value));
				Str.append(">").append(crs.getString("export_value")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
