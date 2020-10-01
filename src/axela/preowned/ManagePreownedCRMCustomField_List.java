package axela.preowned;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedCRMCustomField_List extends Connect {

	public String LinkHeader = "";
	public String LinkListPage = "customfield-list.jsp";
	public String LinkAddPage = "<a href=managepreownedmanagepreownedcrmcustomfield-update.jsp?add=yes>Add Custom Field...</a>";
	public String StrHTML = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
	public String StrSql = "";
	public String StrSearch = "", daydesc = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String precrmfollowupdays_id = "0";
	public String brand_id = "0";
	public String precrmcf_id = "0";
	public String crmcf_entry_id = "0";
	public String crmcf_modified_id = "0";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String precrmcf_precrmfollowupdays_id = "";
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
				precrmcf_id = CNumeric(PadQuotes(request.getParameter("precrmcf_id")));
				precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("precrmfollowupdays_id")));
				// submodule_id = CNumeric(PadQuotes(request.getParameter("submodule_id")));
				if (brand_id.equals("0")) {
					brand_id = CNumeric(ExecuteQuery("SELECT precrmfollowupdays_brand_id FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
							+ " WHERE precrmcf_id = " + precrmcf_id + ""));
					// + " WHERE 1=1";
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("../preowned/managepreownedcrmcustomfield-list.jsp?precrmfollowupdays_id=" + precrmfollowupdays_id
							+ "&msg=Custom Field Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("../preowned/managepreownedcrmcustomfield-list.jsp?precrmfollowupdays_id=" + precrmfollowupdays_id
							+ "&msg=Custom Field Moved Down Successfully!"));
				}
				daydesc = ExecuteQuery("SELECT CONCAT(precrmfollowupdays_daycount,precrmfollowupdays_desc) FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
						+ " WHERE precrmfollowupdays_id = " + precrmfollowupdays_id + "");
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; "
						+ "<a href=../portal/manager.jsp>Business Manager</a> &gt; "
						+ "<a href=managepreownedcrmfollowupdays.jsp?brand_id=" + brand_id + ">List Pre-Owned CRM Days</a>";

				LinkHeader += " &gt; <a href=managepreownedcrmfollowupdays.jsp?brand_id= " + brand_id + ">" + daydesc + "</a> &gt;";
				LinkHeader += " <a href=managepreownedcrmcustomfield-list.jsp?precrmfollowupdays_id=" + precrmfollowupdays_id + ">List Custom Fields</a><b>:</b>";

				if ("yes".equals(all)) {
					msg = "Results for all Custom Fields!";
				} else if (!precrmfollowupdays_id.equals("0")) {
					// daydesc = ExecuteQuery("SELECT CONCAT(precrmfollowupdays_daycount,precrmfollowupdays_desc) FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					// + " WHERE precrmfollowupdays_id = "+precrmfollowupdays_id+"") ;
					// LinkHeader+=" &gt; <a href=managepreownedcrmfollowupdays.jsp?dr_branch="+brand_id+ ">"+daydesc+"</a> &gt;";
					// LinkHeader+= " <a href=managepreownedcrmcustomfield-list.jsp?precrmfollowupdays_id=" + precrmfollowupdays_id + ">List Custom Fields</a>";
					msg += "<br>Results for Pre-Owned CRM Days ID = " + precrmfollowupdays_id + "!";
					StrSearch += " AND precrmcf_precrmfollowupdays_id = " + precrmfollowupdays_id + "";
				}
				if (!precrmcf_id.equals("0")) {
					msg += "<br>Results for Custom Field ID = " + precrmcf_id + "!";
					StrSearch += " AND precrmcf_id = " + precrmcf_id + "";
				}
			}
			// if(precrmfollowupdays_id.equals("0") && (!precrmcf_id.equals("0"))){
			// precrmfollowupdays_id = ExecuteQuery("SELECT precrmcf_precrmfollowupdays_id FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
			// + " WHERE precrmcf_id = "+precrmcf_id+"");
			// }

			// if (!precrmfollowupdays_id.equals("0")) {
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
			StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_rank, precrmfollowupdays_brand_id, "
					+ " precrmcf_cftype_id,  precrmcf_numeric, precrmcf_length_min,"
					+ " precrmcf_length_max, precrmcf_option, precrmcf_unique,"
					+ " precrmcf_mandatory, precrmcf_instruction, precrmcf_active, cftype_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmcf_precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = precrmfollowupdays_brand_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_type ON precrmtype_id = precrmfollowupdays_precrmtype_id"
					+ " INNER JOIN axela_cf_type ON cftype_id = precrmcf_cftype_id"
					+ " WHERE 1=1" + StrSearch
					+ " GROUP BY precrmcf_id"
					+ " ORDER BY precrmcf_rank ASC";
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
				// Str.append("<th data-hide=\"phone, tablet\">CRM Type </th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				while (crs.next()) {
					count++;
					if (crs.getString("precrmcf_active").equals("0")) {
						active = "<font color=red><b> [Inactive] </b></font>";
					} else {
						active = "";
					}
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("precrmcf_title")).append(active).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("cftype_name")).append("</td>\n");
					// Str.append("<td valign=top align=left>").append(crs.getString("precrmtype_name")).append("</td>\n");
					Str.append("<td valign=top align=center><a href=\"managepreownedcrmcustomfield-list.jsp?Up=yes&precrmcf_id=")
							.append(crs.getString("precrmcf_id")).append(" \">Up</a> - <a href=\"managepreownedcrmcustomfield-list.jsp?Down=yes&precrmcf_id=")
							.append(crs.getString("precrmcf_id")).append(" \">Down</a></td>\n");
					Str.append("<td valign=top width=20% nowrap><a href=\"managepreownedcrmcustomfield-update.jsp?update=yes&precrmcf_id=").append(crs.getString("precrmcf_id"));
					Str.append("&precrmfollowupdays_id=" + precrmfollowupdays_id + "&brand_id =" + brand_id);
					Str.append("\">Update Custom Field</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=\"red\"><b><center>No Custom Fields Found!</center></b></font>");
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
		int precrmcf_precrmfollowupdays_id;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT precrmcf_rank FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
				+ " WHERE precrmcf_id = " + precrmcf_id));
		precrmcf_precrmfollowupdays_id = Integer.parseInt(ExecuteQuery("SELECT precrmcf_precrmfollowupdays_id from " + compdb(comp_id) + "axela_preowned_crm_cf WHERE precrmcf_id = " + precrmcf_id
				+ ""));
		tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(precrmcf_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_crm_cf WHERE precrmcf_precrmfollowupdays_id = "
				+ precrmcf_precrmfollowupdays_id));

		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank - 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " SET"
					+ " precrmcf_rank = " + crm_customfield_rank + ""
					+ " WHERE precrmcf_rank = " + tempRank
					+ " AND precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " SET"
					+ " precrmcf_rank = " + tempRank + ""
					+ " WHERE precrmcf_rank = " + crm_customfield_rank + ""
					+ " AND precrmcf_id = " + precrmcf_id
					+ " AND precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id;
			updateQuery(StrSql);
		}
		precrmfollowupdays_id = Integer.toString(precrmcf_precrmfollowupdays_id);
	}
	public void movedown() {
		int tempRank;
		int crm_customfield_rank;
		int precrmcf_precrmfollowupdays_id;
		crm_customfield_rank = Integer.parseInt(ExecuteQuery("SELECT precrmcf_rank FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
				+ " WHERE precrmcf_id = " + precrmcf_id));
		precrmcf_precrmfollowupdays_id = Integer.parseInt(ExecuteQuery("SELECT precrmcf_precrmfollowupdays_id from " + compdb(comp_id) + "axela_preowned_crm_cf WHERE precrmcf_id = " + precrmcf_id
				+ ""));
		tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(precrmcf_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_crm_cf"));

		if (crm_customfield_rank != tempRank) {
			tempRank = crm_customfield_rank + 1;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " SET"
					+ " precrmcf_rank = " + crm_customfield_rank + ""
					+ " WHERE precrmcf_rank = " + tempRank
					+ " AND precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id;
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " SET"
					+ " precrmcf_rank = " + tempRank + ""
					+ " WHERE precrmcf_rank = " + crm_customfield_rank + ""
					+ " AND precrmcf_id = " + precrmcf_id
					+ " AND precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id;
			updateQuery(StrSql);
		}
		precrmfollowupdays_id = Integer.toString(precrmcf_precrmfollowupdays_id);
	}
}
