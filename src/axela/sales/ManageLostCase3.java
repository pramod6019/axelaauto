//  Bhagwan Singh (10th July 2013)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageLostCase3 extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managelostcase3.jsp?all=yes>List Lost Case 3</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
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
	public String all = "";
	public String lostcase3_id = "0";
	public String lostcase3_lostcase2_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "lostcase3_id"},
			{"Name", "text", "lostcase3_name"},
			// {"Sub Head ID", "numeric", "lostcase3_evalsubhead_id"},
			// {"Lost Case 2 ID", "numeric", "lostcase3_lostcase2_id"},
			{"Lost Case 2 Name", "text", "lostcase2_name"},
			{"Entry By", "text", "lostcase3_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "lostcase3_entry_date"},
			{"Modified By", "text", "lostcase3_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "lostcase3_modified_date"}
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
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				lostcase3_id = CNumeric(PadQuotes(request.getParameter("lostcase3_id")));
				lostcase3_lostcase2_id = CNumeric(PadQuotes(request.getParameter("lostcase2_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!lostcase3_lostcase2_id.equals("0")) {
					LinkAddPage = "<a href=managelostcase3-update.jsp?add=yes&lostcase2_id=" + lostcase3_lostcase2_id + ">Add Lost Case 3...</a>";
				} else {
					LinkAddPage = "<a href=managelostcase3-update.jsp?add=yes>Add Lost Case 3...</a>";
				}

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND lostcase3_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Lost Case 3!";
					StrSearch = StrSearch + " AND lostcase3_id > 0";
				}

				if (!(lostcase3_id.equals("0"))) {
					msg = msg + "<br>Results for Lost Case 3 ID = " + lostcase3_id + "!";
					StrSearch = StrSearch + " AND lostcase3_id = " + lostcase3_id + "";
				} else if (!lostcase3_lostcase2_id.equals("0")) {
					msg = msg + "<br>Results for Lost Case 2 ID = " + lostcase3_lostcase2_id + "!";
					StrSearch = StrSearch + " AND lostcase3_lostcase2_id = " + lostcase3_lostcase2_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND lostcase3_id = 0";
					} else {
						msg = "Results for Search!";
					}
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

		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// to know no of records depending on search
			StrSql = "SELECT lostcase3_id, lostcase3_name, lostcase3_lostcase2_id, lostcase2_lostcase1_id,"
					+ " lostcase2_id, lostcase2_name,"
					+ " COALESCE(lostcase1_id,0) AS lostcase1_id,"
					+ " COALESCE(lostcase1_name,'') AS lostcase1_name ";

			CountSql = "SELECT Count(DISTINCT lostcase3_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = lostcase3_lostcase2_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = lostcase2_lostcase1_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
				// SOP("StrSql==="+StrSqlBreaker(StrSql));
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY lostcase3_id";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<br><div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Lost Case 1</th>\n");
					Str.append("<th>Lost Case 2</th>\n");
					Str.append("<th data-hide=\"phone\">Lost Case 3</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left > <a href=\"managelostcase1.jsp?lostcase1_id=").append(crs.getString("lostcase2_lostcase1_id")).append("\">")
								.append(crs.getString("lostcase1_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left > <a href=\"managelostcase2.jsp?lostcase2_id=").append(crs.getString("lostcase3_lostcase2_id")).append("\">")
								.append(crs.getString("lostcase2_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("lostcase3_name")).append("</td>\n");
						Str.append("<td valign=top align=left > <a href=\"managelostcase3-update.jsp?update=yes&lostcase3_id=").append(crs.getString("lostcase3_id"))
								.append(" \">Update Lost Case 3</a></td>\n");
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
				Str.append("<br><br><br><br><font color=red><b>No Lost Case 3 Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
