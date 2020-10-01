/* saiman 26th june 2012 */
//aJIt 9th October, 2012
package axela.app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Model_Colours_List extends Connect {
	// ///// List page links.

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/home.jsp>App</a>"
			+ " &gt; <a href=../app/model-list.jsp?all=yes>Models</a>"
			+ " &gt; <a href=../app/model-colours-list.jsp?all=yes>List Model Colours</a>:";
	public String LinkExportPage = "warehouse.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../app/model-colours-update.jsp?add=yes>Add New Colour..</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "", colours_value = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String colours_id = "0", model_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Colours ID", "numeric", "colours_id"},
			{"Name", "text", "colours_title"},
			{"Model", "text", "model_name"},
			{"Entry By", "text", "colours_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "colours_entry_date"},
			{"Modified By", "text", "colours_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "colours_modified_date"}
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
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				colours_id = CNumeric(PadQuotes(request.getParameter("colours_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));

				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					if (!colours_id.equals("0")) {
						StrSearch = " AND colours_id = " + colours_id;
					} else {
						StrSearch = " AND colours_id = 0";
					}
				} else if ("yes".equals(all)) {
					msg = "Results for all Colours!";
					StrSearch += " AND colours_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Colours!";
					StrSearch += " AND colours_id > 0";
				} else if (!colours_id.equals("0")) {
					msg += "<br>Results for Colours ID = " + colours_id + "!";
					StrSearch += " AND colours_id = " + colours_id + "";
				}
				else if (!model_id.equals("0")) {
					msg += "<br>Results for Model ID = " + model_id + "!";
					StrSearch += " AND colours_model_id = " + model_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND colours_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("variantstrsql", request).equals("")) {
						StrSearch += GetSession("variantstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("variantstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search

				if (!msg.equals("")) {
					StrSql = "SELECT colours_id, colours_title, colours_colour, colours_value, model_id, model_name";

					StrJoin = " FROM " + compdb(comp_id) + "axela_app_model_colours";
					StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = colours_model_id"
							+ " WHERE 1 = 1";

					CountSql = "SELECT COUNT(DISTINCT(colours_id))";
					StrSql += StrJoin;
					CountSql += StrJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " GROUP BY colours_id"
								+ " ORDER BY colours_id DESC";
						CountSql += StrSearch;
					}
					// SOP("StrSql-------List---" + StrSql);
					if (all.equals("recent")) {
						StrSql += " LIMIT " + recperpage + "";
						// SOP("StrSql===" + StrSql);
						crs = processQuery(StrSql, 0);
						crs.last();
						TotalRecords = crs.getRow();
						crs.beforeFirst();
					} else {
						TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
					}

					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Colours(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "model-colours-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						if (!all.equals("recent")) {
							crs = processQuery(StrSql, 0);
						}
						int count = StartRec - 1;
						String active = "";
						Str.append("<div class=\"  table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th>Colour</th>\n");
						Str.append("<th>Model</th>\n");
						Str.append("<th data-hide=\"phone, tablet\" >Code</th>");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						Str.append("</tr>\n");

						while (crs.next()) {
							Date d = new Date();
							count++;

							Str.append("<tr>\n");
							Str.append("<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=left >");
							if (!crs.getString("colours_value").equals("")) {
								colours_value = "<img src=../Thumbnail.do?modelcoloursimg=" + crs.getString("colours_value")
										+ "&width=150" + ">";
								// + " alt=" + crs.getString("colours_title") + ">";
								Str.append(colours_value).append("<br>");
								// colours_value = "<img src=../Thumbnail.do?modelcoloursimg=" + crs.getString("colours_value")
								// + "&width=150&time=" + d.getTime()
								// + " alt=" + crs.getString("colours_title") + ">";
								// Str.append(colours_value).append("<br>");
								// SOP("colours_value===" + colours_value);
							}
							else
							{
								colours_value = "";
							}
							Str.append(crs.getString("colours_title")).append("</td>\n");
							Str.append("<td valign=top align=left >").append(crs.getString("model_name")).append("</td>\n");
							Str.append("<td valign=top align=left >").append(crs.getString("colours_colour")).append("</td>\n");
							Str.append("<td valign=top align=left nowrap><a href=\"model-colours-update.jsp?update=yes&colours_id=");
							Str.append(crs.getString("colours_id")).append(" \">Update Colour</a><br>");
							Str.append("<a href=\"model-colours-image-update.jsp?update=yes&colours_id=").append(crs.getString("colours_id")).append("\">Update Image</a><br>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
					} else {
						RecCountDisplay = "<br><br><br><br><font color=red>No Colour(s) found!</font><br><br>";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
