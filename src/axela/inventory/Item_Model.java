package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Item_Model extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../portal/manager.jsp\">Business Manager</a>"
			+ " &gt; <a href=\"../inventory/item-model.jsp?all=yes\">Item Model</a><b>:</b>";
	public String LinkListPage = "../inventory/item-model.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String comp_id = "0";
	public String LinkAddPage = "<a href=\"../inventory/item-model-update.jsp?Add=yes\">Add Model...</a>";
	public String ExportPerm = "";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public String brand_name = "";
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String model_id = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Brand", "text", "model_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Model ID", "numeric", "model_id"},
			{"Name", "text", "model_name"},
			{"Desc", "text", "model_desc"},
			{"Entry By", "text", "model_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "model_entry_date"},
			{"Modified By", "text", "model_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "model_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_service_insurance_add, emp_service_jobcard_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

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
			StrSql = "SELECT model_id, model_name, model_active, brand_id, brand_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT model_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1";

			if (!StrSearch.equals("")) {
				StrSql += StrSearch;
				CountSql += StrSearch;
			}
			// SOP("CountSql---" + CountSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

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
				PageURL = "../inventory/item-model.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " ORDER BY model_id DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				// SOP("StrSql-------" + StrSql);
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String active = "";
					StrHTML = "";
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=\"5%\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Model Details</th>\n");
					Str.append("<th data-hide=\"phone\">Brand Name</th>\n");
					Str.append("<th data-hide=\"phone\" width=\"20%\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						if (crs.getString("model_active").equals("0")) {
							active = "<b><font color=\"red\"> [Inactive]</font></b>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("model_id")).append("</td>\n");
						Str.append("<td valign=\"top\">").append(crs.getString("model_name")).append(active).append("</td>\n");
						if (crs.getString("brand_id").equals("1"))
						{
							brand_name = "Maruti";
						}
						else if (crs.getString("brand_id").equals("2"))
						{
							brand_name = "Mahindra";
						}
						Str.append("<td valign=\"top\">").append(crs.getString("brand_name")).append("</td>\n");
						Str.append("<td valign=\"top\"><a href=\"../inventory/item-model-update.jsp?Update=yes&model_id=").append(crs.getString("model_id")).append("\">Update Model</a><br/>");
						Str.append("<a href=\"../inventory/inventory-variant-list.jsp?model_id=").append(crs.getString("model_id")).append("\">List Variants</a></td>\n");
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
