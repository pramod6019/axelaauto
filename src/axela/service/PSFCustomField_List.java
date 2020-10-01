package axela.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class PSFCustomField_List extends Connect {

	public String LinkHeader = "";
	public String LinkListPage = "psfcustomfield-list.jsp";
	public String LinkAddPage = "";
	// public String LinkAddPage =
	// "<a href=\"enquiry-quickadd.jsp\">Add New Enquiry...</a>";
	public String StrHTML = "";
	public String msg = "";
	// public String comp_id = "0";
	// for psf branch to band
	public String Up = "";
	public String Down = "";
	public String StrSql = "";
	public String StrSearch = "", daydesc = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String psfdays_id = "0";
	public String psfdays_brand_id = "0";
	public String jcpsfcf_id = "0";
	public String jcpsfcf_entry_id = "0";
	public String jcpsfcf_modified_id = "0";
	public String jcpsfcf_rank = "0";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String all = "";
	public int norecords;
	public String modified_date = "", StrCustomField = "";
	public static ArrayList<String> comp_mod_name_arr = new ArrayList<String>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				Up = PadQuotes(request.getParameter("Up"));
				jcpsfcf_id = CNumeric(PadQuotes(request.getParameter("jcpsfcf_id")));
				jcpsfcf_rank = CNumeric(PadQuotes(request.getParameter("jcpsfcf_rank")));
				Down = PadQuotes(request.getParameter("Down"));

				psfdays_brand_id = CNumeric(PadQuotes(request.getParameter("psfdays_brand_id")));

				psfdays_id = CNumeric(PadQuotes(request.getParameter("psfdays_id")));
				if (psfdays_brand_id.equals("0")) {
					psfdays_brand_id = ExecuteQuery("SELECT psfdays_brand_id FROM "
							+ compdb(comp_id) + "axela_service_jc_psfdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
							+ " WHERE jcpsfcf_id = " + jcpsfcf_id + "");
					// + " WHERE 1=1";
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response
							.encodeRedirectURL("../service/psfcustomfield-list.jsp?psfdays_id="
									+ psfdays_id
									+ "&msg=Custom Field Moved Up Successfully!"));
				} else if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response
							.encodeRedirectURL("../service/psfcustomfield-list.jsp?psfdays_id="
									+ psfdays_id
									+ "&msg=Custom Field Moved Down Successfully!"));
				}
				daydesc = ExecuteQuery("SELECT CONCAT(psfdays_daycount,psfdays_desc) FROM "
						+ compdb(comp_id)
						+ "axela_service_jc_psfdays"
						+ " WHERE psfdays_id = " + psfdays_id + "");

				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; "
						+ "<a href=../portal/manager.jsp>Business Manager</a>  "
						+ " &gt; <a href=../service/psfdays.jsp?psfdays_brand_id="
						+ psfdays_brand_id + ">List PSF Days</a><b>:</b> ";

				LinkHeader += "<a href=../service/psfdays.jsp?psfdays_brand_id="
						+ psfdays_brand_id + ">" + daydesc + "</a><b>:</b>";
				LinkHeader += " &gt; <a href=../service/psfcustomfield-list.jsp?psfdays_id="
						+ psfdays_id + " > List Custom Fields</a><b>:</b>";

				LinkAddPage += "<a href=\"psfcustomfield-update.jsp?add=yes\">Add Custom Field...</a>";

				if ("yes".equals(all)) {
					msg = "Results for all Custom Fields!";
				} else if (!psfdays_id.equals("0")) {
					msg += "<br>Results for PSF Days ID = " + psfdays_id + "!";
					StrSearch += " AND jcpsfcf_crmdays_id = " + psfdays_id + "";
				}
				if ("yes".equals(all)) {
					msg = "Results for all Custom Fields!";
					StrSearch = StrSearch + " AND jcpsfcf_id > 0";
				}
				if (!jcpsfcf_id.equals("0")) {
					msg += "<br>Results for Custom Field ID = " + jcpsfcf_id + "!";
					StrSearch += " AND jcpsfcf_id = " + jcpsfcf_id + "";
				}

				// if(psfdays_id.equals("0") && (!jcpsfcf_id.equals("0"))){
				// psfdays_id = ExecuteQuery("SELECT crmcf_psfdays_id FROM " +
				// compdb(comp_id) + "axela_service_jc_psf_cf"
				// + " WHERE jcpsfcf_id = "+jcpsfcf_id+"");
				// }

				// if (!psfdays_id.equals("0")) {
				StrCustomField = Listdata();
				// }
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, jcpsfcf_rank, jcpsfcf_refjcpsfcf_id,"
					+ " jcpsfcf_cftype_id, jcpsfcf_numeric, jcpsfcf_numeric,"
					+ " jcpsfcf_length_max, jcpsfcf_option, jcpsfcf_unique,"
					+ " jcpsfcf_mandatory, jcpsfcf_instruction, jcpsfcf_active, cftype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsfcf_crmdays_id"
					// +
					// " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
					+ " INNER JOIN axela_cf_type ON cftype_id = jcpsfcf_cftype_id"
					+ " WHERE 1=1" + StrSearch + " GROUP BY jcpsfcf_id"
					+ " ORDER BY jcpsfcf_rank";
			// SOP("Strsql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Custom Field </th>\n");
				Str.append("<th data-toggle=\"true\">Ref </th>\n");
				Str.append("<th>Type </th>\n");
				Str.append("<th data-hide=\"phone, tablet\">PSF Type </th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count)
							.append("</td>\n");
					Str.append("<td valign=top>")
							.append(crs.getString("jcpsfcf_title"))
							.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("jcpsfcf_refjcpsfcf_id")).append("</td>\n");
					Str.append("<td valign=top align=left>")
							.append(crs.getString("cftype_name"))
							.append("</td>\n");
					// Str.append("<td valign=top align=left>").append(crs.getString("crmtype_name")).append("</td>\n");
					Str.append("<td valign=top align=left width=20%>");
					Str.append("PSF</td>\n");
					Str.append("<td valign=top align=center width=20%>");
					Str.append(
							"<a href=\"psfcustomfield-list.jsp?Up=yes&jcpsfcf_id=")
							.append(crs.getString("jcpsfcf_id"));
					Str.append(
							"\">Up</a> - <a href=\"psfcustomfield-list.jsp?Down=yes&jcpsfcf_id=")
							.append(crs.getString("jcpsfcf_id"));
					Str.append("\">Down</a>");
					Str.append(
							"&nbsp;</td>\n<td valign=top width=20% nowrap><a href=\"psfcustomfield-update.jsp?update=yes&jcpsfcf_id=")
							.append(crs.getString("jcpsfcf_id"));
					Str.append("&psfdays_id=" + psfdays_id
							+ "&psfdays_brand_id =" + psfdays_brand_id);
					Str.append("\">Update Custom Field</a></td>\n");
				}
				Str.append("</tr></tbody>\n</table></div>\n");
			} else {
				Str.append("<br><br><font color=\"red\"><b>No Custom Fields Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int crm_customfield_rank;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT jcpsfcf_rank FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
				+ " WHERE jcpsfcf_id = " + jcpsfcf_id));

		tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(jcpsfcf_rank) AS min1 FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"));

		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank - 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " SET"
					+ " jcpsfcf_rank = " + crm_customfield_rank + ""
					+ " WHERE jcpsfcf_rank = " + tempRank;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " SET"
					+ " jcpsfcf_rank = " + tempRank + ""
					+ " WHERE jcpsfcf_rank = " + crm_customfield_rank + ""
					+ " AND jcpsfcf_id = " + jcpsfcf_id;
			updateQuery(StrSql);
		}
	}

	public void movedown() {
		int tempRank;
		int crm_customfield_rank;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT jcpsfcf_rank FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
				+ " WHERE jcpsfcf_id = " + jcpsfcf_id));

		tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(jcpsfcf_rank) AS max1 FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"));
		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank + 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " SET"
					+ " jcpsfcf_rank = " + crm_customfield_rank + ""
					+ " WHERE jcpsfcf_rank = " + tempRank;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " SET"
					+ " jcpsfcf_rank = " + tempRank + ""
					+ " WHERE jcpsfcf_rank = " + crm_customfield_rank + ""
					+ " AND jcpsfcf_id = " + jcpsfcf_id;
			updateQuery(StrSql);
		}
	}

	// public String PopulateModule() throws SQLException {
	// StringBuilder Str1 = new StringBuilder();
	// StringBuilder Str = new StringBuilder();
	// String modSearch = "";
	//
	// StrSql = "SELECT CONCAT('comp_module_', module_name) AS module_name"
	// + " FROM " + maindb() + "module";
	// try {
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// // if (crs.isBeforeFirst()) {
	// while (crs.next()) {
	// Str.append(crs.getString("module_name")).append(", ");
	// comp_mod_name_arr.add(crs.getString("module_name"));
	// }
	// StrSearch = Str.toString();
	// StrSearch = StrSearch.substring(0, StrSearch.lastIndexOf(","));
	// }
	// crs.close();
	//
	// StrSql = "SELECT " + StrSearch + " FROM " + compdb(comp_id) + "" +
	// compdb(comp_id) + "axela_comp";
	// rs = processQuery(StrSql, 0);
	//
	// int mod_name_arr_count = comp_mod_name_arr.size();
	// if (crs.isBeforeFirst()) {
	// Str = new StringBuilder();
	// Str.append(" AND (");
	// while (crs.next()) {
	// for (int i = 0; i < mod_name_arr_count; i++) {
	// if (crs.getString(comp_mod_name_arr.get(i)).equals("1")) {
	// String mod_name[] = comp_mod_name_arr.get(i).split("_");
	// Str.append(" module_name = '").append(mod_name[2].toString().toLowerCase());
	// Str.append("' OR ");
	// }
	// }
	// }
	// modSearch = Str.toString();
	// modSearch = modSearch.substring(0, modSearch.lastIndexOf("OR")) + " )";
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("AxelaAuto===" + this.getClass().getName());
	// SOPError("Error in " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	//
	// StrSql = "SELECT module_id, module_name"
	// + " FROM " + maindb() + "module"
	// + " WHERE 1 = 1" + modSearch + ""
	// + " GROUP BY module_id"
	// + " ORDER BY module_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// Str1.append("<option value=\"0\">Select</option>\n");
	// while (crs.next()) {
	// Str1.append("<option value=").append(crs.getString("module_id"));
	// Str1.append(StrSelectdrop(crs.getString("module_id"), module_id));
	// Str1.append(">").append(crs.getString("module_name")).append("</option>\n");
	// }
	// crs.close();
	// return Str1.toString();
	// }
	//
	// public String PopulateSubModule() throws SQLException {
	//
	// StringBuilder Str = new StringBuilder();
	// StrSql = "SELECT submodule_id, submodule_name"
	// + " FROM " + maindb() + "sub_module"
	// + " WHERE submodule_module_id = " + module_id + ""
	// + " GROUP BY submodule_id"
	// + " ORDER BY submodule_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// Str.append("<select name=\"submodule_id\" class=\"selectbox\" id=\"submodule_id\" onChange=\"document.form1.submit()\">\n");
	// Str.append("<option value=0>Select</option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("submodule_id"));
	// Str.append(" ").append(StrSelectdrop(crs.getString("submodule_id"),
	// submodule_id));
	// Str.append(">").append(crs.getString("submodule_name")).append("</option>\n");
	// }
	// crs.close();
	// Str.append("</select>\n");
	// return Str.toString();
	// }
}
