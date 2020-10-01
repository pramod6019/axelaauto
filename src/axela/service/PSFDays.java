package axela.service;
//divya 31st may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class PSFDays extends Connect {

	public String brand_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String emp_id = "0";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String psfdays_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				CheckPerm(comp_id, "emp_role_id", request, response);
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				psfdays_id = CNumeric(PadQuotes(request.getParameter("psfdays_id")));

				if (!(psfdays_id.equals("0"))) {
					msg = msg + "<br>Results for Days ID = " + psfdays_id + "!";
					StrSearch = StrSearch + " and psfdays_id = " + psfdays_id + "";
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
	// sop
	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT psfdays_id, psfdays_brand_id, COALESCE(brand_name,'') AS brandname, brand_id,emp_id, "
				+ " psfdays_daycount, psfdays_desc, COALESCE(jccat_name, '') AS jccat_name, psfdays_exe_type,"
				+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'),'') AS emp_name, psfdays_active";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
				+ " INNER JOIN axela_brand on brand_id = psfdays_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = psfdays_jccat_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = psfdays_emp_id"
				+ " WHERE psfdays_brand_id = " + brand_id + "";
		StrSql = StrSql + SqlJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY psfdays_id"
				+ " ORDER BY jccat_id,psfdays_daycount";
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
				Str.append("<th>Category</th>\n");
				// Str.append("<th>Type</th>\n");
				Str.append("<th data-hide=\"phone\">Executive Type</th>\n");
				Str.append("<th data-hide=\"phone\">PSF Executive</th>\n");
				Str.append("<th>Days Count</th>\n");
				Str.append("<th data-hide=\"phone\">Description</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?brand_id=");
					Str.append(crs.getInt("brand_id")).append("\">");
					Str.append(crs.getString("brandname")).append("</a>");
					if (crs.getString("psfdays_active").equals("0")) {
						Str.append("<font color=red> [INACTIVE]</font>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=center >").append(crs.getString("jccat_name")).append("</td>\n");

					// Str.append("<td valign=top>").append(crs.getString("crmtype_name")).append("</td>\n");
					if (crs.getString("psfdays_exe_type").equals("1")) {
						Str.append("<td valign=top>").append("PSF Executive").append("</td>\n");
					} else if (crs.getString("psfdays_exe_type").equals("2")) {
						Str.append("<td valign=top>").append("Service Advisior").append("</td>\n");
					}
					// else if (crs.getString("psfdays_exe_type").equals("3")) {
					// Str.append("<td valign=top>").append("Manager").append("</td>\n");
					// }

					Str.append("<td valign=top align=left ><a href=\"../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td valign=top align=center >").append(crs.getString("psfdays_daycount")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("psfdays_daycount")).append(crs.getString("psfdays_desc")).append("</td>\n");
					Str.append("<td valign=top><a href=\"../service/psfdays-update.jsp?update=yes&psfdays_id=");
					Str.append(crs.getString("psfdays_id")).append("&dr_branch=");
					Str.append(crs.getString("psfdays_brand_id"));
					Str.append(" \">Update PSF Day</a>\n");
					Str.append("<br><a href=\"../service/psfcustomfield-list.jsp?brand_id=" + brand_id + "&psfdays_id=");
					Str.append(crs.getString("psfdays_id"));
					Str.append(" \">List Custom Fields</a></td>\n");
				}
				Str.append("</tr></tbody>\n");
				Str.append("</table></div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No PSF Days Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " AND branch_branchtype_id IN (1, 3)"
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
