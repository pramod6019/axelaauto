// smitha nag june 26 2013
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageModuleReport extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String Up = "";
	public String Down = "";
	public String QueryString = "";
	public String report_id = "0";
	public String all = "";
	public String module_id = "";
	public String dr_module_id = "";
	public String LinkAddPage = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_role_id", request, response);
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				report_id = CNumeric(PadQuotes(request.getParameter("report_id")));
				all = PadQuotes(request.getParameter("all"));
				module_id = CNumeric(PadQuotes(request.getParameter("dr_module_id")));
				LinkAddPage = "<a href=../portal/managemodulereport-update.jsp?add=yes&module_id=" + module_id + ">Add New Report...</a>";
				// SOP("==="+module_id);

				if (msg.toLowerCase().contains("delete")) {
					if (!module_id.equals("0")) {
						StrSearch = " AND module_id =" + module_id;
					} else {
						StrSearch = " AND module_id = 0";
					}
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?dr_module_id=" + module_id + "&msg=Group moved up successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?dr_module_id=" + module_id + "&msg=Group moved down successfully!"));
				}
				if (!module_id.equals("0")) {
					StrHTML = Listdata();
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();

		StrSql = "Select report_id, report_name, report_active, module_id, module_name"
				+ " from " + maindb() + "module_report"
				+ " inner join " + maindb() + "module on module_id = report_module_id"
				+ " where 1 = 1 and module_id = " + module_id
				+ " group by module_id, report_id "
				+ " order by module_name, report_rank";
		// SOP("StrSql = " + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			String active = "";
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
				Str.append("<th>Report</th>\n");
				Str.append("<th>Module</th>\n");
				Str.append("<th data-hide=\"phone\">Order</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("report_active").equals("0")) {
						active = "<br><font color=red> [Inactive] </font>";
					} else {
						active = "";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("report_name")).append(" (").append(crs.getString("module_id")).append(")").append(active).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("module_name")).append(" (").append(crs.getString("module_id")).append(")" + "</td>\n");
					Str.append("<td valign=top align=center><a href=\"managemodulereport.jsp?Up=yes&report_id=").append(crs.getString("report_id"))
							.append(" \">Up</a> - <a href=\"managemodulereport.jsp?Down=yes&report_id=").append(crs.getString("report_id")).append(" \">Down</a></td>\n");
					Str.append("<td valign=top align=left><a href=\"managemodulereport-update.jsp?update=yes&report_id=").append(crs.getString("report_id")).append(" \"> Update Report </a></td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><center><font color=red><b>No Reports found!</b></font></center><br><br><br><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int report_rank;
		int report_module_id;
		try {
			report_rank = Integer.parseInt(ExecuteQuery("SELECT report_rank FROM " + maindb() + "module_report where report_id=" + report_id + ""));
			report_module_id = Integer.parseInt(ExecuteQuery("SELECT report_module_id from " + maindb() + "module_report where report_id=" + report_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select min(report_rank) as min1 from " + maindb() + "module_report where report_module_id=" + report_module_id));

			if (report_rank != tempRank) {
				tempRank = report_rank - 1;
				StrSql = "update " + maindb() + "module_report set report_rank=" + report_rank + " where report_rank=" + tempRank + " and report_module_id = " + report_module_id + "";
				updateQuery(StrSql);
				StrSql = "update " + maindb() + "module_report set report_rank=" + tempRank + " where report_rank=" + report_rank + " and report_id=" + report_id + " and report_module_id = "
						+ report_module_id + "";
				updateQuery(StrSql);
			}
			module_id = Integer.toString(report_module_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int report_rank;
		int report_module_id;
		try {
			report_rank = Integer.parseInt(ExecuteQuery("SELECT report_rank FROM " + maindb() + "module_report where report_id=" + report_id + ""));
			report_module_id = Integer.parseInt(ExecuteQuery("SELECT report_module_id from " + maindb() + "module_report where report_id=" + report_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select max(report_rank) as max1 from " + maindb() + "module_report where report_module_id=" + report_module_id));
			if (report_rank != tempRank) {
				tempRank = report_rank + 1;
				StrSql = "update " + maindb() + "module_report set report_rank=" + report_rank + " where report_rank=" + tempRank + " and report_module_id = " + report_module_id + "";
				updateQuery(StrSql);
				StrSql = "update " + maindb() + "module_report set report_rank=" + tempRank + " where report_rank=" + report_rank + " and report_id = " + report_id + " and report_module_id = "
						+ report_module_id + "";
				updateQuery(StrSql);
			}
			module_id = Integer.toString(report_module_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModule() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT module_id, module_name"
					+ " FROM " + maindb() + "module"
					+ " ORDER BY module_name";
			// SOP("PopulateModule query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select Module</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("module_id")).append("");
				Str.append(StrSelectdrop(crs.getString("module_id"), module_id));
				Str.append(">").append(crs.getString("module_name")).append("</option> \n");
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
