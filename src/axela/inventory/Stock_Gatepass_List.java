package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Stock_Gatepass_List extends Connect {
	
	public String LinkHeader = "";
	public String LinkExportPage = "stock-gatepass-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String PageURL = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public String vehstock_id = "0";
	public String vehstockgatepass_id = "0";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Stock Gatepass ID", "numeric", "vehstockgatepass_id"},
			{"Stock ID", "numeric", "vehstock_id"},
			{"Driver Name", "text", "vehstockdriver_name"},
			{"Branch Name", "text", "branch_name"},
			{"Notes", "text", "vehstockgatepass_notes"},
			{"Entry By", "text", "vehstockgatepass_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "vehstockgatepass_entry_date"},
			{"Modified By", "text", "vehstockgatepass_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vehstockgatepass_modified_date"}
	};
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				if (vehstock_id.equals("0")) {
					vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_vehstock_id")));
				}
				vehstockgatepass_id = CNumeric(PadQuotes(request.getParameter("vehstockgatepass_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				LinkHeader = "<a href=../portal/home.jsp>Home</a>"
						+ " &gt; <a href=../inventory/index.jsp>Inventory</a>"
						+ " &gt; <a href=../inventory/stock.jsp>Stock</a>"
						+ " &gt; <a href=stock-list.jsp?all=yes>List Stocks</a>"
						+ " &gt; <a href=stock-gatepass-list.jsp?all=yes>List Stock Gate Passes</a>:";
				
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehstockgatepass_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Stock Gate Passes!";
					StrSearch += " AND vehstockgatepass_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Stock!";
					StrSearch += " AND vehstockgatepass_id > 0";
				} else if (!vehstockgatepass_id.equals("0")) {
					msg += "<br>Results for Stock Gate Pass ID = " + vehstockgatepass_id + "!";
					StrSearch += " AND vehstockgatepass_id = " + vehstockgatepass_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND vehstockgatepass_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("stockgatepassstrsql", request).equals("")) {
						StrSearch += GetSession("stockgatepassstrsql", request);
					}
				}
				if (!vehstock_id.equals("0")) {
					msg += "<br>Results for Stock ID = " + vehstock_id + "!";
					StrSearch += " AND vehstockgatepass_vehstock_id = " + vehstock_id + "";
				}
				
				if (!StrSearch.equals("")) {
					SetSession("stockgatepassstrsql", StrSearch, request);
				}
				StrHTML = "<input type=\"hidden\" name=\"txt_vehstock_id\" id=\"txt_vehstock_id\" value=\"" + vehstock_id + "\"/>"
						+ Listdata();
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT vehstockgatepass_id, vehstockgatepass_vehstock_id, vehstockgatepass_time, vehstockgatepass_from_location_id, vehstockgatepass_to_location_id,"
						+ " vehstockgatepass_stockdriver_id, vehstockdriver_name, CONCAT(fromloc.vehstocklocation_name, ' (', fromloc.vehstocklocation_id, ')') AS from_location_name,"
						+ " CONCAT(toloc.vehstocklocation_name, ' (', toloc.vehstocklocation_id, ')') AS to_location_name, branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " vehstockgatepass_out_kms, vehstockgatepass_in_kms";
				
				CountSql = "SELECT COUNT(DISTINCT vehstockgatepass_id)";
				
				SqlJoin = " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = vehstockgatepass_vehstock_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_driver ON vehstockdriver_id = vehstockgatepass_stockdriver_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location fromloc ON fromloc.vehstocklocation_id = vehstockgatepass_from_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location toloc ON toloc.vehstocklocation_id = vehstockgatepass_to_location_id"
						+ " WHERE 1 = 1 ";
				
				StrSql += SqlJoin;
				CountSql += SqlJoin;
				
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY vehstockgatepass_id"
							+ " ORDER BY vehstockgatepass_id DESC";
					CountSql += StrSearch;
				}
				
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
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
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock Gate Pass(es)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					
					PageURL = "stock-gatepass-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}
					// SOP("StrSql===" + StrSqlBreaker(StrSql));
					int count = StartRec - 1;
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">ID</th>\n");
					Str.append("<th>Stock ID</th>\n");
					Str.append("<th data-hide=\"phone\">Time</th>\n");
					Str.append("<th data-hide=\"phone\">Location</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Driver</th>\n");
					if (branch_id.equals("0")) {
						Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					}
					if (emp_id.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">Kms</th>\n");
					}
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead><tr>\n");
					Str.append("<tbody><tr>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("vehstockgatepass_id")).append("</td>\n");
						Str.append("<td nowrap><a href=\"stock-list.jsp?vehstock_id=").append(crs.getString("vehstockgatepass_vehstock_id")).append(" \">")
								.append(crs.getString("vehstockgatepass_vehstock_id")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td nowrap>").append(strToLongDate(crs.getString("vehstockgatepass_time")));
						Str.append("</td>\n");
						Str.append("<td>From: <a href=\"manage-stock-location.jsp?vehstocklocation_id=").append(crs.getString("vehstockgatepass_from_location_id")).append(" \">");
						Str.append(crs.getString("from_location_name")).append("</a><br>");
						
						Str.append("To: <a href=\"manage-stock-location.jsp?vehstocklocation_id=").append(crs.getString("vehstockgatepass_to_location_id")).append(" \">");
						Str.append(crs.getString("to_location_name")).append("</a>");
						
						Str.append("</td>\n<td><a href=\"../sales/managedemodriver.jsp?vehstockdriver_id=").append(crs.getString("vehstockgatepass_stockdriver_id")).append(" \">");
						Str.append(crs.getString("vehstockdriver_name")).append("</a>");
						if (branch_id.equals("0")) {
							Str.append("</td>\n");
							Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=");
							Str.append(crs.getString("branch_id"));
							Str.append("\">").append(crs.getString("branchname")).append("</a>");
						}
						Str.append("</td>\n");
						
						if (emp_id.equals("1")) {
							Str.append("<td>");
							Str.append("Out Kms: " + crs.getString("vehstockgatepass_out_kms") + "</br>");
							Str.append("In Kms: " + crs.getString("vehstockgatepass_in_kms"));
							Str.append("");
							Str.append("</td>\n");
						}
						
						Str.append("<td nowrap>");
						Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
								+ "<button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"stock-gatepass-update.jsp?update=yes&vehstockgatepass_id="
								+ crs.getString("vehstockgatepass_id") + " \">Update Stock Gate Pass</a></li>"
								+ "<li role=presentation><a href=\"stock-gatepass-print.jsp?report=gatepass&vehstockgatepass_id="
								+ crs.getString("vehstockgatepass_id") + "&target=" + Math.random()
								+ "\" target=_blank>Print Gate Pass</a></li>"
								+ "<li role=presentation><a href=\"update-kms.jsp?vehstockgatepass_id=" + crs.getString("vehstockgatepass_id")
								+ "&vehstock_id=" + crs.getInt("vehstockgatepass_vehstock_id")
								+ "\"data-target='#Hintclicktocall' data-toggle='modal'>Update KMS</a></li></ul></div></center></div>");
						
						Str.append("</td>\n");
					}
					crs.close();
					Str.append("</tr>\n");
					Str.append("</tbody><tr>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					
					return Str.toString();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Stock Gate Pass found!</font><br><br>";
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
