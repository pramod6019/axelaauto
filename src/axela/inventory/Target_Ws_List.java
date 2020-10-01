/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.inventory;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Ws_List extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0", dr_branch_id = "0";
	public String year = "";
	public int curryear = 0;
	public String targetid = "0";
	public String wscount = "0";
	public String amt = "0";
	public int total_ws_count = 0;
	public String QueryString = "";
	public String ExeAccess = "";
	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("brnach id====" + branch_id);
				msg = PadQuotes(request.getParameter("msg"));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));

				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}
				if (branch_id.equals("0")) {
					dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));

				} else {
					dr_branch_id = branch_id;
				}
				CheckForm();
				StrHTML = ListData(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData(HttpServletRequest request) throws SQLException {

		StringBuilder Str = new StringBuilder();
		if (!dr_branch_id.equals("0")) {
			String month = "";
			for (int i = 1; i <= 12; i++) {
				month = i < 10 ? "0" + i : i + "";
				StrSql += " SELECT " + "MONTHNAME('" + year + month + "01'), COALESCE((SELECT SUM(wstarget_count)"
						+ " FROM " + compdb(comp_id) + "axela_sales_target_wholesale"
						+ " WHERE wstarget_branch_id = " + dr_branch_id
						+ " AND SUBSTR(wstarget_month, 1, 6) = '" + year + month + "'), 0) as target";
				if (i != 12)
					StrSql += " UNION";
			}
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			try {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>");
				Str.append("<th data-toggle=\"true\">Month</th>");
				Str.append("<th>Wholesale Count</th>");
				Str.append("<th data-hide=\"phone\">Actions</th>");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				int count = 0;
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr><td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>");
					Str.append(TextMonth(count - 1)).append("-").append(year);
					// Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(targetid).append(">");
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("target")).append("</td>\n");

					Str.append("<td valign=top align=left nowrap=nowrap>");
					Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
							+ "<button type=button style='margin: 0' class='btn btn-success'>"
							+ "<i class='fa fa-pencil'></i></button>"
							+ "<ul class='dropdown-content dropdown-menu pull-right'>"
							+ "<li role=presentation><a href=\"target-ws-update.jsp?update=yes&target_id="
							+ targetid + "&dr_branch_id=" + dr_branch_id + "&year=" + year
							+ "&month=" + count + "\">Update Target</a></li></ul></div></center></div></td>\n");
					Str.append("</tr>\n");
					total_ws_count += Integer.parseInt(crs.getString("target"));
				}
				Str.append("<tr>\n");
				Str.append("<td>\n</td>\n");
				Str.append("<td align='right'><b>Total:</b></td>\n");
				Str.append("<td align='right'><b>").append(total_ws_count).append("</b></td>\n");
				Str.append("<td>\n</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs.close();

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void CheckForm() {
		if (dr_branch_id.equals("0")) {
			msg = "Select Branch!";
		}
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}
}
