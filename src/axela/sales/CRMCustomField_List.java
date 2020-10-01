package axela.sales;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CRMCustomField_List extends Connect {

	public String LinkHeader = "";
	public String LinkListPage = "customfield-list.jsp";
	public String LinkAddPage = "<a href=customfield-update.jsp?add=yes>Add Custom Field...</a>";
	public String StrHTML = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
	public String StrSql = "";
	public String StrSearch = "", daydesc = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String crmdays_id = "0";
	public String brand_id = "0";
	public String crmcf_id = "0";
	public String crmcf_entry_id = "0";
	public String crmcf_modified_id = "0";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String crmcf_crmdays_id = "";
	public String all = "";
	public int norecords;
	public String modified_date = "", StrCustomField = "";
	public static ArrayList<String> comp_mod_name_arr = new ArrayList<String>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				crmcf_id = CNumeric(PadQuotes(request.getParameter("crmcf_id")));
				crmdays_id = CNumeric(PadQuotes(request.getParameter("crmdays_id")));
				// submodule_id = CNumeric(PadQuotes(request.getParameter("submodule_id")));
				if (brand_id.equals("0")) {
					brand_id = CNumeric(ExecuteQuery("SELECT crmdays_brand_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							+ " WHERE crmcf_id = " + crmcf_id + ""));
					// + " WHERE 1=1";
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("../sales/crmcustomfield-list.jsp?crmdays_id=" + crmdays_id + "&msg=Custom Field Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("../sales/crmcustomfield-list.jsp?crmdays_id=" + crmdays_id + "&msg=Custom Field Moved Down Successfully!"));
				}
				daydesc = ExecuteQuery("SELECT CONCAT(crmdays_daycount,crmdays_desc)"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " WHERE crmdays_id = " + crmdays_id + "");
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; "
						+ "<a href=../portal/manager.jsp>Business Manager</a> &gt; "
						+ "<a href=crmdays.jsp?brand_id=" + brand_id + ">List CRM Days</a>";

				LinkHeader += " &gt; <a href=crmdays.jsp?brand_id= " + brand_id + ">" + daydesc + "</a> &gt;";
				LinkHeader += " <a href=crmcustomfield-list.jsp?crmdays_id=" + crmdays_id + ">List Custom Fields</a><b>:</b>";

				if ("yes".equals(all)) {
					msg = "Results for all Custom Fields!";
				} else if (!crmdays_id.equals("0")) {
					// daydesc = ExecuteQuery("SELECT CONCAT(crmdays_daycount,crmdays_desc) FROM " + compdb(comp_id) + "axela_sales_crmdays"
					// + " WHERE crmdays_id = "+crmdays_id+"") ;
					// LinkHeader+=" &gt; <a href=crmdays.jsp?dr_branch="+brand_id+ ">"+daydesc+"</a> &gt;";
					// LinkHeader+= " <a href=crmcustomfield-list.jsp?crmdays_id=" + crmdays_id + ">List Custom Fields</a>";
					msg += "<br>Results for CRM Days ID = " + crmdays_id + "!";
					StrSearch += " AND crmcf_crmdays_id = " + crmdays_id + "";
				}
				if (!crmcf_id.equals("0")) {
					msg += "<br>Results for Custom Field ID = " + crmcf_id + "!";
					StrSearch += " AND crmcf_id = " + crmcf_id + "";
				}
			}
			// if(crmdays_id.equals("0") && (!crmcf_id.equals("0"))){
			// crmdays_id = ExecuteQuery("SELECT crmcf_crmdays_id FROM " + compdb(comp_id) + "axela_sales_crm_cf"
			// + " WHERE crmcf_id = "+crmcf_id+"");
			// }

			// if (!crmdays_id.equals("0")) {
			StrCustomField = Listdata();
			// }
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmcf_id, crmcf_title, crmcf_rank, crmdays_brand_id, "
					+ " crmcf_cftype_id, cftype_name, crmcf_numeric, crmcf_length_min,"
					+ " crmcf_length_max, crmcf_option, crmcf_unique,"
					+ " crmcf_mandatory, crmcf_instruction, crmcf_active, crmtype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crmcf_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
					+ " INNER JOIN axela_cf_type ON cftype_id = crmcf_cftype_id"
					+ " WHERE 1=1" + StrSearch
					+ " GROUP BY crmcf_id"
					+ " ORDER BY crmcf_rank ASC";
			// SOP("StrSql-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				String active = "";
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Custom Field </th>\n");
				Str.append("<th>Type </th>\n");
				Str.append("<th data-hide=\"phone, tablet\">CRM Type </th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				while (crs.next()) {
					count++;
					if (crs.getString("crmcf_active").equals("0")) {
						active = "<font color=red><b> [Inactive] </b></font>";
					} else {
						active = "";
					}
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("crmcf_title")).append(active).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("cftype_name")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("crmtype_name")).append("</td>\n");
					Str.append("<td valign=top align=center><a href=\"crmcustomfield-list.jsp?Up=yes&crmcf_id=")
							.append(crs.getString("crmcf_id")).append(" \">Up</a> - <a href=\"crmcustomfield-list.jsp?Down=yes&crmcf_id=")
							.append(crs.getString("crmcf_id")).append(" \">Down</a></td>\n");
					Str.append("<td valign=top width=20% nowrap><a href=\"crmcustomfield-update.jsp?update=yes&crmcf_id=").append(crs.getString("crmcf_id"));
					Str.append("&crmdays_id=" + crmdays_id + "&brand_id =" + brand_id);
					Str.append("\">Update Custom Field</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=\"red\"><b>No Custom Fields Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int crm_customfield_rank;
		int crmcf_crmdays_id;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT crmcf_rank"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
				+ " WHERE crmcf_id = " + crmcf_id));
		crmcf_crmdays_id = Integer.parseInt(ExecuteQuery("SELECT crmcf_crmdays_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf WHERE crmcf_id = " + crmcf_id + ""));
		tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(crmcf_rank) AS min1"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
				+ " WHERE crmcf_crmdays_id = " + crmcf_crmdays_id));

		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank - 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " SET"
					+ " crmcf_rank = " + crm_customfield_rank + ""
					+ " WHERE crmcf_rank = " + tempRank
					+ " AND crmcf_crmdays_id = " + crmcf_crmdays_id;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " SET"
					+ " crmcf_rank = " + tempRank + ""
					+ " WHERE crmcf_rank = " + crm_customfield_rank + ""
					+ " AND crmcf_id = " + crmcf_id
					+ " AND crmcf_crmdays_id = " + crmcf_crmdays_id;
			updateQuery(StrSql);
		}
		crmdays_id = Integer.toString(crmcf_crmdays_id);
	}
	public void movedown() {
		int tempRank;
		int crm_customfield_rank;
		int crmcf_crmdays_id;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT crmcf_rank"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
				+ " WHERE crmcf_id = " + crmcf_id));
		crmcf_crmdays_id = Integer.parseInt(ExecuteQuery("SELECT crmcf_crmdays_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
				+ " WHERE crmcf_id = " + crmcf_id + ""));
		tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(crmcf_rank) AS max1"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"));

		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank + 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " SET"
					+ " crmcf_rank = " + crm_customfield_rank + ""
					+ " WHERE crmcf_rank = " + tempRank
					+ " AND crmcf_crmdays_id = " + crmcf_crmdays_id;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " SET"
					+ " crmcf_rank = " + tempRank + ""
					+ " WHERE crmcf_rank = " + crm_customfield_rank + ""
					+ " AND crmcf_id = " + crmcf_id
					+ " AND crmcf_crmdays_id = " + crmcf_crmdays_id;
			updateQuery(StrSql);
		}
		crmdays_id = Integer.toString(crmcf_crmdays_id);
	}
}
