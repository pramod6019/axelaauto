package axela.service;
//aJIt 11th March, 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Kms_List extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String LinkExportPage = "vehicle-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=vehicle-update.jsp?add=yes>Add Kms...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String veh_id = "0";
	public String jc_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	// variables used in MileageUpdate("?");
	public String veh_sale_date = "";
	public String current_date = "";
	public int minveh_id = 0;
	public int maxveh_id = 0;
	public int kmscount = 0;
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Kms ID", "numeric", "vehkms_id"},
			{"Item ID", "numeric", "veh_variant_id"},
			{"Model Name", "text", "model_name"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"Chassis Number", "text", "veh_chassis_no"},
			{"Engine No.", "text", "veh_engine_no"},
			{"Customer ID", "numeric", "veh_customer_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact ID", "numeric", "veh_contact_id"},
			{"Contact Name", "text", "CONCAT(contact_fname, ' ', contact_lname)"},
			{"Entry By", "text", "vehkms_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "vehkms_entry_date"},
			{"Modified By", "text", "vehkms_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vehkms_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				veh_id = CNumeric(PadQuotes(request.getParameter("vehkms_veh_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/vehicle.jsp>Vehicle</a>"
						+ " &gt; <a href=vehicle-list.jsp?all=yes>List Vehicles</a>"
						+ " &gt; <a href=kms-list.jsp?vehkms_veh_id=" + veh_id + ">List Kms</a>:";
				LinkAddPage = "<a href=kms-update.jsp?add=yes&vehkms_veh_id=" + veh_id + ">Add Kms...</a>";

				if (!veh_id.equals("0")) {
					msg += "<br>Results for Vehicle ID = " + veh_id + "!";
					StrSearch += " AND vehkms_veh_id = " + veh_id;
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT vehkms_id, vehkms_veh_id, vehkms_entry_date, veh_variant_id,  preownedmodel_name, variant_name,"
					+ " variant_service_code, vehkms_kms, veh_chassis_no, veh_engine_no, veh_reg_no, customer_id,"
					+ " customer_name, contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contact_name";

			CountSql = "SELECT COUNT(DISTINCT(vehkms_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh_kms ON vehkms_veh_id = veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " WHERE 1 = 1"
					+ BranchAccess.replace("branch_id", "customer_branch_id");

			StrSql += SqlJoin + StrSearch;
			CountSql += SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Kms(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "kms-list.jsp?" + QueryString + "&PageCurrent=";

				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " GROUP BY vehkms_id"
						+ " ORDER BY vehkms_entry_date DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				CachedRowSet crs = processQuery(StrSql, 0);
				try {
					int count = StartRec - 1;
					// Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
					// Str.append("<tr align=\"center\">\n");
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Item</th>\n");
					Str.append("<th data-hide=\"phone\">Model</th>\n");
					Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Chassis Number</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Engine No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Kms</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr>\n<td align=\"center\" valign=\"top\">").append(count);
						Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("vehkms_id"));
						Str.append("</td>\n<td align=\"left\" valign=\"top\">").append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=");
						Str.append(crs.getString("veh_variant_id")).append("\">").append(crs.getString("variant_name")).append("</a>");
						if (!crs.getString("variant_service_code").equals("")) {
							Str.append(" (").append(crs.getString("variant_service_code")).append(")");
						}

						Str.append("</td>\n<td align=\"left\" valign=\"top\">").append(crs.getString("preownedmodel_name"));
						Str.append("</td>\n<td align=\"center\" valign=\"top\">").append("<a href=\"../service/vehicle-list.jsp?veh_id=");
						Str.append(crs.getString("vehkms_veh_id")).append("\">").append(crs.getString("veh_reg_no")).append("</a>");
						Str.append("</td>\n<td align=\"center\" valign=\"top\">").append(crs.getString("veh_chassis_no"));
						Str.append("</td>\n<td align=\"center\" valign=\"top\">").append(crs.getString("veh_engine_no"));
						Str.append("</td>\n<td align=\"right\" valign=\"top\">").append(strToShortDate(crs.getString("vehkms_entry_date")));
						Str.append("</td>\n<td align=\"right\" valign=\"top\">").append(IndFormat(crs.getString("vehkms_kms")));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
						Str.append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_id")).append(")</a>");
						if (!crs.getString("contact_name").equals("")) {
							Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
							Str.append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("contact_name")).append(" (").append(crs.getString("contact_id")).append(")</a>");
						}

						Str.append("</td>\n<td valign=\"top\"><a href=\"kms-update.jsp?update=yes&vehkms_id=").append(crs.getString("vehkms_id")).append("\">Update Kms</a>");
						Str.append("</td>\n</tr>\n");
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
				Str.append("<br><br><font color=red><b>No Kms found!</b></font>");
			}
		}
		return Str.toString();
	}

}
