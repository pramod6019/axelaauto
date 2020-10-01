// smitha nag 1 march 2013, 6 july modified
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageAccess extends Connect {

	public String LinkAddPage = "";
	public String user_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String Up = "";
	public String Down = "";
	public String module_id = "";
	public String QueryString = "";
	public String access_id = "0";
	public String all = "";
	public String advSearch = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				user_id = CNumeric(PadQuotes(GetSession("user_id", request)
						+ ""));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				access_id = CNumeric(PadQuotes(request
						.getParameter("access_id")));
				all = PadQuotes(request.getParameter("all"));
				module_id = CNumeric(PadQuotes(request
						.getParameter("dr_module_id")));
				LinkAddPage = "<a href=manageaccess-update.jsp?add=yes&module_id="
						+ module_id + ">Add New Access ...</a>";

				if (msg.toLowerCase().contains("delete")) {
					if (!module_id.equals("0")) {
						StrSearch = " AND access_id =" + access_id;
					} else {
						StrSearch = " AND access_id = 0";
					}
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response
							.encodeRedirectURL("manageaccess.jsp?dr_module_id="
									+ module_id
									+ "&msg=Group moved up successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response
							.encodeRedirectURL("manageaccess.jsp?dr_module_id="
									+ module_id
									+ "&msg=Group moved down successfully!"));
				}
				if (!module_id.equals("0")) {
					StrHTML = Listdata();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
		StrSql = "Select module_id, module_name, access_id, access_name"
				+ " from " + maindb() + "module" + " inner join " + maindb()
				+ "module_access on access_module_id = module_id"
				+ " where 1 = 1 and module_id = " + module_id + StrSearch
				+ " group by module_id, access_id "
				+ " order by module_name, access_rank";
		// SOP("==="+StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"container-fluid portlet box\">");	
				Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");	
				Str.append("<div class=\"caption\" style=\"float: none\">Manage Access</div></div>");
				Str.append("<div class=\"container-fluid\">\n");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Access Details</th>\n");
				Str.append("<th>Module</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("<th data-hide=\"phone\">Order</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				Str.append("</div></div>");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>").append(crs.getString("access_name"))
							.append(" (").append(crs.getString("access_id"))
							.append(")" + "</td>\n");
					Str.append("<td>").append(crs.getString("module_name"))
							.append(" (").append(crs.getString("module_id"))
							.append(")" + "</td>\n");
					Str.append(
							"<td align=center><a href=\"manageaccess-update.jsp?update=yes&access_id=")
							.append(crs.getString("access_id"))
							.append(" \"> Update Access </a></td>\n");
					Str.append(
							"<td align=center><a href=\"manageaccess.jsp?Up=yes&access_id=")
							.append(crs.getString("access_id"))
							.append(" \">Up</a> - <a href=\"manageaccess.jsp?Down=yes&access_id=")
							.append(crs.getString("access_id"))
							.append(" \">Down</a></td>\n");
				}	
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Access found!</b></font><br><br><br><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int access_rank;
		int access_module_id;
		try {
			access_rank = Integer
					.parseInt(ExecuteQuery("SELECT access_rank from "
							+ maindb() + "module_access where  access_id="
							+ access_id + ""));
			access_module_id = Integer
					.parseInt(ExecuteQuery("SELECT access_module_id from "
							+ maindb() + "module_access where access_id="
							+ access_id + ""));
			tempRank = Integer
					.parseInt(ExecuteQuery("select min(access_rank) as min1 from "
							+ maindb()
							+ "module_access where access_module_id="
							+ access_module_id));
			if (access_rank != tempRank) {
				tempRank = access_rank - 1;
				StrSql = "update " + maindb()
						+ "module_access set access_rank=" + access_rank
						+ " where access_rank=" + tempRank
						+ " and access_module_id=" + access_module_id;
				updateQuery(StrSql);
				StrSql = "update " + maindb()
						+ "module_access set access_rank=" + tempRank
						+ " where access_rank=" + access_rank
						+ " and access_id=" + access_id
						+ "  and access_module_id = " + access_module_id + "";
				updateQuery(StrSql);
			}
			module_id = Integer.toString(access_module_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void movedown() {
		int tempRank;
		int access_rank;
		int access_module_id;
		try {
			access_rank = Integer
					.parseInt(ExecuteQuery("SELECT access_rank from "
							+ maindb() + "module_access where access_id="
							+ access_id + ""));
			access_module_id = Integer
					.parseInt(ExecuteQuery("SELECT access_module_id from "
							+ maindb() + "module_access where access_id="
							+ access_id + ""));
			tempRank = Integer
					.parseInt(ExecuteQuery("select max(access_rank) as max1 from "
							+ maindb()
							+ "module_access where access_module_id="
							+ access_module_id));
			if (access_rank != tempRank) {
				tempRank = access_rank + 1;
				StrSql = "update " + maindb()
						+ "module_access set access_rank=" + access_rank
						+ " where access_rank=" + tempRank
						+ " and access_module_id=" + access_module_id;
				updateQuery(StrSql);
				StrSql = "update " + maindb()
						+ "module_access set access_rank=" + tempRank
						+ " where access_rank=" + access_rank
						+ " and access_id=" + access_id
						+ " and access_module_id = " + access_module_id + "";
				updateQuery(StrSql);
			}
			module_id = Integer.toString(access_module_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateModule() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT module_id, module_name" + " FROM " + maindb()
					+ "module" + " ORDER BY module_name";
			// SOP("PopulateModule query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select Module</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("module_id"))
						.append("");
				Str.append(StrSelectdrop(crs.getString("module_id"), module_id));
				Str.append(">").append(crs.getString("module_name"))
						.append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
}
