package axela.service;

//@Bhagwan Singh
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Movement_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../portal/manager.jsp\">Business Manager</a>"
			+ " &gt; <a href=\"movement-list.jsp?all=yes\">List Vehicle Movements</a>:";
	public String LinkExportPage = "movement-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
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
	public String QueryString = "";
	public String all = "";
	public String BranchAccess = "", smart = "";
	public String advSearch = "";
	public String vehmove_id = "0";

	public String Img = "";

	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Movement ID", "numeric", "vehmove_id"},
			{"Reg. No.", "text", "vehmove_reg_no"},
			{"Time In", "date", "vehmove_timein"},
			{"Time Out", "date", "vehmove_timeout"},
			{"JobCard ID", "numeric", "jc_id"},
			{"Vehicle ID", "numeric", "veh_id"},
			{"Model", "text", "item_model_id IN (SELECT model_id FROM compdb.axela_inventory_item_model WHERE model_name"},
			{"Variant", "text", "item_name"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Time In Entry By", "text", "vehmove_timein_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified By", "text", "vehmove_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vehmove_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				smart = PadQuotes(request.getParameter("smart"));
				CheckPerm(comp_id, "emp_movement_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				vehmove_id = CNumeric(PadQuotes(request.getParameter("vehmove_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehmove_id = 0";
				} else if ("yes".equals(all)) {
					msg += "<br>Results for All vehicle Movements!";
					StrSearch += " AND vehmove_id > 0"
							+ "	AND vehmove_timeout = ''";
				}

				if (!vehmove_id.equals("0")) {
					msg = msg + "<br>Results for vehicle Movement ID = " + vehmove_id + "!";
					StrSearch += " AND vehmove_id = " + vehmove_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch += " AND vehmove_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				else if (smart.equals("yes")) {
					msg += "<br/>Results of Search!";
					if (!session.getAttribute("vehmovestrsql").equals("")) {
						StrSearch = session.getAttribute("vehmovestrsql").toString();
					}
				}
				SetSession("vehmovestrsql", StrSearch, request);
				StrSearch += BranchAccess.replace("branch_id", "vehmove_branch_id");
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
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			if (!msg.equals("")) {
				StrSql = "SELECT vehmove_id, vehmove_reg_no, vehmove_mobile1, vehmove_timein, vehmove_timeout, branch_id,"

						+ " COALESCE(insurimg_value, '') AS insurimg_value,"

						+ " COALESCE(jctype_name,'') AS jctype_name,"
						+ " COALESCE(jc_id, 0) AS jc_id,"
						+ " COALESCE(veh_id, 0) AS veh_id, branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " COALESCE(variant_id, 0) AS variant_id, IF(vehmove_internal = '1', 'Yes', 'No') AS vehmove_internal,"
						+ " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variantname, variant_preownedmodel_id";

				CountSql = "SELECT COUNT(DISTINCT vehmove_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh_move"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehmove_branch_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehmove_veh_id"

						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh_insur_img ON insurimg_veh_id = veh_id"

						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = preownedmodel_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_vehmove_id = vehmove_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = vehmove_jctype_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin + StrSearch;
				CountSql += SqlJoin + StrSearch;
				SOP("strsql----move---" + StrSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Vehicle Movement(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "movement-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " GROUP BY vehmove_id"
							+ " ORDER BY vehmove_id DESC"
							+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("StrSql------final========" + StrSql);
					try {
						CachedRowSet crs = processQuery(StrSql, 0);
						int count = StartRec - 1;
						Str.append("<div class=\"  table-bordered\">\n");
						Str.append("<table class=\"table table-hover table-bordered  \" data-filter=\"#filter\">\n");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\">Reg. No.</th>\n");
						Str.append("<th>Mobile</th>\n");
						Str.append("<th>Time In</th>\n");
						Str.append("<th>Time Out</th>\n");
						Str.append("<th data-hide=\"phone\">Job Card</th>\n");
						Str.append("<th data-hide=\"phone\">Job Card Type</th>\n");
						Str.append("<th data-hide=\"phone\">Vehicle</th>\n");
						Str.append("<th data-hide=\"phone,tablet\">Internal</th>\n");
						Str.append("<th data-hide=\"phone,tablet\">Insurance</th>\n");
						Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
						Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							count++;
							Str.append("<tr>\n");
							Str.append("<td align=center>").append(count).append("</td>\n");
							Str.append("<td>").append(SplitRegNo(crs.getString("vehmove_reg_no"), 2)).append("</td>\n");
							Str.append("<td align=center>").append(crs.getString("vehmove_mobile1")).append("</td>\n");
							Str.append("<td align=center>").append(strToLongDate(crs.getString("vehmove_timein"))).append("</td>\n");
							Str.append("<td align=center>").append(strToLongDate(crs.getString("vehmove_timeout"))).append("</td>\n");
							Str.append("<td>");
							if (!crs.getString("jc_id").equals("0")) {
								Str.append("ID: <a href=\"jobcard-list.jsp?jc_id=").append(crs.getString("jc_id"));
								Str.append("\">").append(crs.getString("jc_id")).append("</a><br>");
							}
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("jctype_name"));
							Str.append("</td>\n<td>");

							if (!crs.getString("veh_id").equals("0")) {

								Str.append("ID: <a href=\"vehicle-list.jsp?veh_id=").append(crs.getString("veh_id"));
								Str.append("\">").append(crs.getString("veh_id")).append("</a><br>");

								Str.append("Item: <a href=\"../preowned/managepreownedvariant.jsp?variant_id=");
								Str.append(crs.getString("variant_id")).append("\">").append(crs.getString("variantname")).append("</a>");
							}
							Str.append("</td>\n<td align=center>").append(crs.getString("vehmove_internal"));

							if (!crs.getString("insurimg_value").equals("") && !crs.getString("veh_id").equals("0")) {
								Img = "<a href=../Thumbnailblob.do?image_type=vehiclein&veh_id="
										+ crs.getString("veh_id") + " target=_blank><img src=../Thumbnailblob.do?veh_id="
										+ crs.getString("veh_id") + "&width=200&image_type=vehiclein&border=0></a>";

							} else {
								Img = "";
							}

							Str.append("<td>" + Img + "</td>");

							Str.append("</td>\n<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
							Str.append(crs.getString("branch_name")).append("</a>");
							Str.append("</td>");

							Str.append("\n<td><a href=\"movement-update.jsp?update=yes&vehmove_id=").append(crs.getString("vehmove_id")).append(" \">Update</a>");
							Str.append("</td>\n</tr>\n");
						}
						crs.close();
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");

					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Vehicle Movement(s) Found!</font><br><br>";
				}
			}
		}
		return Str.toString();
	}
}
