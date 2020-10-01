/*saiman 27th jun 2012 */
package axela.inventory;
//aJIt 9th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Inventory_Location_List extends Connect {

	// ///// List page links
	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Inventory</a>"
			+ " &gt; <a href=inventory-location-list.jsp?all=yes>List Locations</a>:";
	public String LinkExportPage = "location.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=inventory-location-update.jsp?Add=yes>Add New Location..</a>";
	public String ExportPerm = "";
	public String emp_id = "0", BranchAccess = "";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String group = "";
	public String location_id = "0", location_name = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Location ID", "numeric", "location_id"},
			{"Name", "text", "location_name"},
			{"Branch ID", "numeric", "location_branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Phone1", "text", "location_phone1"},
			{"Phone2", "text", "location_phone2"},
			{"Mobile1", "text", "location_mobile1"},
			{"Mobile2", "text", "location_mobile2"},
			{"Address", "text", "location_address"},
			{"Pin", "text", "location_pin"},
			{"Entry Date", "date", "location_entry_date"},
			{"Modified Date", "date", "location_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND location_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Locations!";
					StrSearch = StrSearch + " and location_id > 0";
				} else if (!(location_id.equals("0"))) {
					msg = msg + "<br>Results for Location!";
					StrSearch = StrSearch + " and location_id = " + location_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("inventorylocationstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("inventorylocationstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("inventorylocationstrsql", StrSearch, request);
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

		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			if (!msg.equals("")) {
				if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				if (!msg.equals("")) {
					StrSql = "SELECT location_id, location_name, location_phone1, location_phone2,"
							+ " CONCAT(branch_name,' (',branch_code,')') AS branch_name, branch_id,"
							+ " location_mobile1, location_mobile2, location_address,"
							+ " city_name, state_name, location_pin";

					StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = location_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE 1 = 1 " + BranchAccess;

					CountSql = " SELECT COUNT(distinct(location_id))";

					StrSql = StrSql + StrJoin;
					CountSql = CountSql + StrJoin;

					if (!(StrSearch.equals(""))) {
						StrSql = StrSql + StrSearch;
						CountSql = CountSql + StrSearch;
					}

					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

					if (TotalRecords != 0) {

						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Location(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "inventory-location-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						// StrSql = StrSql + StrSearch;
						if (all.equals("yes")) {
							StrSql = StrSql + " GROUP BY location_id ORDER BY location_id desc";
						} else {
							StrSql = StrSql + " GROUP BY location_id ORDER BY location_id desc";
						}
						StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

						try {
							CachedRowSet crs = processQuery(StrSql, 0);
							int count = StartRec - 1;
							Str.append("<div class=\"table-bordered\">\n");
							Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
							Str.append("<thead><tr>\n");
							Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
							Str.append("<th data-toggle=\"true\">Locations</th>\n");
							Str.append("<th>Branch</th>\n");
							Str.append("<th data-hide=\"phone\">Contacts</th>\n");
							Str.append("<th data-hide=\"phone, tablet\">Address</th>\n");
							Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
							Str.append("</tr>\n");
							Str.append("</thead><tr>\n");
							Str.append("<tbody><tr>\n");
							if (crs.isBeforeFirst()) {
								while (crs.next()) {

									count = count + 1;
									Str.append("<tr>\n");
									Str.append("<td>").append(count).append("</td>\n");
									Str.append("<td><b>").append(crs.getString("location_name")).append("</b></td>");
									Str.append("<td><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">")
											.append(crs.getString("branch_name")).append("</a></td>");
									Str.append("<td nowrap>");
									Str.append("").append(crs.getString("location_phone1")).append("");
									if (!crs.getString("location_phone2").equals("")) {
										Str.append("<br>").append(crs.getString("location_phone2")).append("");
									}
									Str.append("<br>").append(crs.getString("location_mobile1")).append("");
									if (!crs.getString("location_mobile2").equals("")) {
										Str.append("<br>").append(crs.getString("location_mobile2")).append("");
									}
									Str.append("</td>");
									Str.append("<td>");
									if (!crs.getString("location_address").equals("")) {
										Str.append(crs.getString("location_address")).append(", ");
									}
									Str.append(crs.getString("city_name")).append("");
									if (!crs.getString("location_pin").equals("")) {
										Str.append(" - ").append(crs.getString("location_pin")).append(", <br>");
									}
									if (crs.getString("location_pin").equals("")) {
										Str.append(", ");
									}
									Str.append(crs.getString("state_name")).append(".</td>\n");
									Str.append("<td nowrap>");
									Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
											+ "<button type=button style='margin: 0' class='btn btn-success'><i class='fa fa-pencil'></i></button>"
											+ "<ul class='dropdown-content dropdown-menu pull-right'>"
											+ "<li role=presentation><a href=\"inventory-location-update.jsp?Update=yes&location_id=" + crs.getString("location_id")
											+ " \">Update Location</a></li></ul></div></center></div></td>\n");
								}
							}
							Str.append("</tr>\n");
							Str.append("</tbody><tr>\n");
							Str.append("</table>\n");
							Str.append("</div>\n");
							crs.close();
						} catch (Exception ex) {
							SOPError("Axelaauto===" + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
							return "";
						}
					} else {
						RecCountDisplay = "<br><br><br><br><font color=red>No Location(s) found!</font><br><br>";
					}
				}
			}
		}
		return Str.toString();
	}
}
