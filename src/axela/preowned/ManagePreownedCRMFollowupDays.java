package axela.preowned;
//Sangita 22nd may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedCRMFollowupDays extends Connect {

	public String brand_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String precrmfollowupdays_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				CheckPerm(comp_id, "emp_role_id", request, response);
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("precrmfollowupdays_id")));

				if (!(precrmfollowupdays_id.equals("0"))) {
					msg = msg + "<br>Results for Pre-Owned CRM Follow-up Days ID = " + precrmfollowupdays_id + "!";
					StrSearch = StrSearch + " and precrmfollowupdays_id = " + precrmfollowupdays_id + "";
				}
				SOP("brand_id-----------" + brand_id);
				if (!brand_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					StrHTML = "<font color=\"red\"><b>Select Brand!</b></font>";
				}
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

	// ////////////////////////////
	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		String active = "", followup = "";
		StrSql = "Select precrmfollowupdays_id, precrmfollowupdays_brand_id, COALESCE(brand_name, '') as brandname, brand_id,"
				+ " precrmfollowupdays_daycount, precrmfollowupdays_desc, precrmfollowupdays_active, precrmfollowupdays_lostfollowup,"
				+ " precrmfollowupdays_exe_type";
		SqlJoin = " from " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
				+ " inner JOIN axela_brand on brand_id = precrmfollowupdays_brand_id"
				+ " where precrmfollowupdays_brand_id = " + brand_id + "";
		StrSql = StrSql + SqlJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " group by precrmfollowupdays_id"
				+ " order by precrmfollowupdays_daycount";
		SOP("StrSql===" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
				Str.append("<th>Brand</th>\n");
				Str.append("<th>Lost Case</th>\n");
				Str.append("<th data-hide=\"phone\">Pre-Owned Consultant Type</th>\n");
				Str.append("<th>Days Count</th>\n");
				Str.append("<th data-hide=\"phone\">Description</th>\n");
				Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					if (crs.getString("precrmfollowupdays_active").equals("0")) {
						active = "<b><font color=\"red\"> [Inactive]</font></b>";
					} else {
						active = "";
					}
					if (crs.getString("precrmfollowupdays_lostfollowup").equals("1"))
					{
						followup = "Yes";
					} else {
						followup = "No";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top>");
					Str.append(crs.getString("brandname")).append("</td>");
					Str.append("<td valign=top align=center>").append(followup).append("</td>\n");
					if (crs.getString("precrmfollowupdays_exe_type").equals("1"))
					{
						Str.append("<td valign=top>").append("CRM Executive").append("</td>\n");
					} else if (crs.getString("precrmfollowupdays_exe_type").equals("2"))
					{
						Str.append("<td valign=top>").append("Sales Consultant").append("</td>\n");
					}
					else if (crs.getString("precrmfollowupdays_exe_type").equals("3"))
					{
						Str.append("<td valign=top>").append("Manager").append("</td>\n");
					}

					Str.append("<td valign=top align=center >").append(crs.getString("precrmfollowupdays_daycount")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("precrmfollowupdays_daycount")).append(crs.getString("precrmfollowupdays_desc")).append("</td>\n");
					Str.append("<td valign=top><a href=\"../preowned/managepreownedcrmfollowupdays-update.jsp?update=yes&precrmfollowupdays_id=");
					Str.append(crs.getString("precrmfollowupdays_id")).append("&dr_brand=");
					Str.append(crs.getString("precrmfollowupdays_brand_id"));
					Str.append(" \">Update Pre-Owned CRM Follow-up Day</a>");
					Str.append("<br><a href=\"../preowned/managepreownedcrmcustomfield-list.jsp?brand_id=" + brand_id + "&precrmfollowupdays_id=");
					Str.append(crs.getString("precrmfollowupdays_id"));
					Str.append(" \">List Custom Fields</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Pre-Owned CRM Follow-up Days Found!</b></font>");
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
					+ " AND branch_branchtype_id = 2"
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
