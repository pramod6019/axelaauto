package axela.app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Model_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../app/home.jsp?all=yes\">App</a>"
			+ " &gt; Model"
			+ " &gt; <a href=\"../app/model-list.jsp?all=yes\">List Models</a>:";
	public String LinkListPage = "../app/model-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String comp_id = "0";
	public String LinkAddPage = "<a href=\"../app/model-update.jsp?add=yes\">Add Model...</a>";
	public String ExportPerm = "";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String msg = "", img_value = "";
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
	public String all = "";
	public String model_id = "";
	public String model_img_value = "";
	public String advSearch = "";
	public String homefilter = "", fromdate = "", todate = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Model ID", "numeric", "model_id"},
			{"Name", "text", "model_name"},
			{"Desc", "text", "model_desc"},
			{"Entry By", "text", "model_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "model_entry_date"},
			{"Modified By", "text", "model_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "model_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				homefilter = PadQuotes(request.getParameter("homefilter"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND model_id = 0";
				} else if ("yes".equals(all)) {
					msg += "<br>Results for All Model(s)!";
					StrSearch += " AND model_id > 0";
				} else if (!model_id.equals("0")) {
					msg += "<br>Results for Model ID = " + model_id + "!";
					StrSearch += " AND model_id = " + model_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND model_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (homefilter.equals("yes")) {
					fromdate = PadQuotes(request.getParameter("from"));
					todate = PadQuotes(request.getParameter("to"));
					fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
					todate = ConvertShortDateToStr(todate).substring(0, 8);
					StrSearch += " AND (SUBSTR(model_entry_date, 1,8) >= '" + fromdate + "'"
							+ " AND SUBSTR(model_entry_date, 1,8) <= '" + todate + "')";
					msg = "Results for Models!";
				}

				if (!StrSearch.equals("")) {
					SetSession("modelstrsql", StrSearch, request);
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

	public String ListData() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search
			StrSql = "SELECT model_id, model_name, model_active, model_img_value, model_mileage, model_engine, model_emi "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " WHERE 1 = 1"
					// + " AND  model_sales = 1"
					+ " AND model_active = 1";

			CountSql = "SELECT COUNT(DISTINCT model_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1";
			// SOP("StrSql=================" + StrSqlBreaker(StrSql));
			if (!StrSearch.equals("")) {
				StrSql += StrSearch;
				CountSql += StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			// SOP("TotalRecords----" + TotalRecords);

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Model(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent, "");
				}
				PageURL = "../app/model-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " ORDER BY model_id DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				// SOP("StrSql---- ----Model_list------" + StrSql);
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String active = "";
					StrHTML = "";
					int count = StartRec - 1;
					Str.append("<div class=\"  table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th >Model Details</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						Date d = new Date();
						count++;
						if (crs.getString("model_active").equals("0")) {
							active = "<b><font color=\"red\"> [Inactive]</font></b>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("model_id")).append("</td>\n");
						Str.append("<td valign=\"top\">");
						if (!crs.getString("model_img_value").equals("")) {
							model_img_value = "<img src=../Thumbnail.do?modelimg=" + crs.getString("model_img_value")
									+ "&width=150&target=" + Math.random()
									+ " alt=" + crs.getString("model_name") + ">";
							Str.append(model_img_value).append("<br>");
						}

						Str.append(crs.getString("model_name")).append(active).append("<br>");
						Str.append("Mileage :").append(crs.getString("model_mileage")).append("<br>")
								.append("Engine :").append(crs.getString("model_engine")).append("<br>")
								.append("EMI :").append(crs.getString("model_emi"));
						Str.append("</td>\n");
						// Str.append(crs.getString("model_mileage")).append(active).append("</td>\n");
						// Str.append(crs.getString("model_engine")).append(active).append("</td>\n");
						// Str.append(crs.getString("model_emi")).append(active).append("</td>\n");

						Str.append("<td valign=\"top\"><a href=\"../app/model-update.jsp?update=yes&model_id=").append(crs.getString("model_id")).append("\">Update Model</a><br/>");
						Str.append("<a href=\"../app/model-image.jsp?model_id=").append(crs.getString("model_id")).append("\">Update Model Image</a><br>");
						Str.append("<a href=\"../app/features-list.jsp?model_id=").append(crs.getString("model_id")).append("\">List Features</a><br>");
						Str.append("<a href=\"../app/model-colours-list.jsp?model_id=").append(crs.getString("model_id")).append("\">List Colours</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><b><font color=\"red\">No Model(s) Found!</font></b><br><br>");
			}
		}
		return Str.toString();
	}
}
