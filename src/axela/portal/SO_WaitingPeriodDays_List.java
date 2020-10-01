package axela.portal;
//divya 31st may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SO_WaitingPeriodDays_List extends Connect {

	public String LinkHeader = "";
	public String brand_id = "0";
	public String brandconfig_id = "0";
	public String brand_name = "";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String sowaitingperiod_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				brandconfig_id = CNumeric(PadQuotes(request.getParameter("brandconfig_id")));
				CheckPerm(comp_id, "emp_role_id", request, response);
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				sowaitingperiod_id = CNumeric(PadQuotes(request.getParameter("sowaitingperiod_id")));

				brand_name = PadQuotes(ExecuteQuery("SELECT brand_name FROM axela_brand WHERE brand_id = " + brand_id));

				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; "
						+ "<a href=../portal/manager.jsp>Business Manager</a>  "
						+ " &gt; <a href=../portal/managebrandconfig-list.jsp>  List Brand Config</a> > ";

				LinkHeader += "<a href=../portal/managebrandconfig-list.jsp>" + brand_name + "</a>";
				LinkHeader += " &gt; <a href=../portal/so-waitingperioddays-list.jsp?dr_brand=" + brand_id + "&brandconfig_id=" + brandconfig_id + " > List SO Waiting Period Days</a>";
				if (!(sowaitingperiod_id.equals("0"))) {
					msg = msg + "<br>Results for SO WaitingPeriod ID = " + sowaitingperiod_id + "!";
					// StrSearch = StrSearch + " and crmfollowupdays_id = " +
					// sowaitingperiod_id + "";
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

		StrSql = "Select sowaitingperiod_id, sowaitingperiod_brand_id, brand_name , brand_id,"
				+ " sowaitingperiod_days, sowaitingperiod_enable";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
				+ " INNER JOIN axela_brand on brand_id = sowaitingperiod_brand_id"
				+ " WHERE sowaitingperiod_brand_id = " + brand_id + "";
		StrSql = StrSql + SqlJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY sowaitingperiod_id"
				+ " ORDER BY sowaitingperiod_id";

		try {
			String active = "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-hover \">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				// Str.append("<tr align=center>\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Brand</th>\n");
				Str.append("<th>Days</th>\n");
				Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					if (crs.getString("sowaitingperiod_enable").equals("0")) {
						active = "<b><font color=\"red\"> [Inactive]</font></b>";
					} else {
						active = "";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?brand_id=");
					Str.append(crs.getInt("brand_id")).append("\">");
					Str.append(crs.getString("brand_name")).append(active).append("</a><br/>").append("</td>");
					Str.append("<td valign=top align=center >").append(crs.getString("sowaitingperiod_days")).append("</td>\n");
					Str.append("<td valign=top><a href=\"../portal/so-waitingperioddays-update.jsp?update=yes&sowaitingperiod_id=");
					Str.append(crs.getString("sowaitingperiod_id")).append("&dr_brand=");
					Str.append(crs.getString("sowaitingperiod_brand_id")).append("&brandconfig_id=").append(brandconfig_id);
					Str.append(" \">Update SO Waiting Period Day</a>\n");
					// Str.append("<br><a href=\"../sales/crmcustomfield-list.jsp?brand_id="
					// + brand_id + "&sowaitingperiod_id=");
					// Str.append(crs.getString("sowaitingperiod_id"));
					// Str.append(" \">List Custom Fields</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody></table></div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No SO Waiting Period Found!</b></font>");
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return "";
		}
	}
}
