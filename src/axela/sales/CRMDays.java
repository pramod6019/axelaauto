package axela.sales;
//divya 31st may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CRMDays extends Connect {

	public String brand_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String crmdays_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				crmdays_id = CNumeric(PadQuotes(request.getParameter("crmdays_id")));

				if (!(crmdays_id.equals("0"))) {
					msg = msg + "<br>Results for Days ID = " + crmdays_id + "!";
					StrSearch = StrSearch + " and crmfollowupdays_id = " + crmdays_id + "";
				}
				if (!brand_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					StrHTML = "<font color=\"red\"><b>Select Brand!</b></font>";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		String active = "", followup = "";
		StrSql = "SELECT crmdays_id, crmdays_brand_id, crmdays_active, brand_name , brand_id,"
				+ " crmdays_daycount, crmdays_desc, crmtype_name, crmdays_lostfollowup, crmdays_exe_type";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_crmdays"
				+ " INNER JOIN axela_brand ON brand_id = crmdays_brand_id"
				+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
				+ " WHERE crmdays_brand_id = " + brand_id + "";
		StrSql = StrSql + SqlJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY crmdays_id"
				+ " ORDER BY crmdays_crmtype_id, crmdays_daycount";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\" table-bordered table-hover \">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				// Str.append("<tr align=center>\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Brand</th>\n");
				Str.append("<th>Type</th>\n");
				Str.append("<th>Lost Case</th>\n");
				Str.append("<th data-hide=\"phone\">Sales Consultant Type</th>\n");
				Str.append("<th>Days Count</th>\n");
				Str.append("<th data-hide=\"phone\">Description</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					if (crs.getString("crmdays_active").equals("0")) {
						active = "<b><font color=\"red\"> [Inactive]</font></b>";
					} else {
						active = "";
					}

					if (crs.getString("crmdays_lostfollowup").equals("1"))
					{
						followup = "Yes";
					} else {
						followup = "No";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?brand_id=");
					Str.append(crs.getInt("brand_id")).append("\">");
					Str.append(crs.getString("brand_name")).append("</a><br/>").append(active).append("</td>");
					Str.append("<td valign=top>").append(crs.getString("crmtype_name")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(followup).append("</td>\n");
					if (crs.getString("crmdays_exe_type").equals("1")) {
						Str.append("<td valign=top>").append("CRM Executive").append("</td>\n");
					} else if (crs.getString("crmdays_exe_type").equals("2")) {
						Str.append("<td valign=top>").append("Sales Consultant").append("</td>\n");
					} else if (crs.getString("crmdays_exe_type").equals("3")) {
						Str.append("<td valign=top>").append("Manager").append("</td>\n");
					} else if (crs.getString("crmdays_exe_type").equals("4")) {
						Str.append("<td valign=top>").append("Service PSF Executive").append("</td>\n");
					}
					Str.append("<td valign=top align=center >").append(crs.getString("crmdays_daycount")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("crmdays_daycount")).append(crs.getString("crmdays_desc")).append("</td>\n");
					Str.append("<td valign=top><a href=\"../sales/crmdays-update.jsp?update=yes&crmdays_id=");
					Str.append(crs.getString("crmdays_id")).append("&dr_brand=");
					Str.append(crs.getString("crmdays_brand_id"));
					Str.append(" \">Update CRM Follow-up Day</a>\n");
					Str.append("<br><a href=\"../sales/crmcustomfield-list.jsp?brand_id=" + brand_id + "&crmdays_id=");
					Str.append(crs.getString("crmdays_id"));
					Str.append(" \">List Custom Fields</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody></table></div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No CRM Days Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN ( 1, 2)"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
