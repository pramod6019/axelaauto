package axela.insurance;
/*saiman 27th june 2012 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_Disposition extends Connect {
	// ////// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Insurance</a>"
			+ " &gt; <a href=manage-disposition.jsp?insurdisposition_parent_id=0>List Disposition</a><b>:</b>";
	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=managedisposition-update.jsp?Add=yes>Add New Disposition...</a>";
	public String ExportPerm = "";
	public String BranchAccess = "", branch_id = "0";
	public String StrHTML = "", emp_id = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "", CountSql = "";
	public String StrSearch = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String all = "";
	public String group = "";
	public String smart = "";
	public String insurdisposition_id = "", insurdisposition_parent_id = "0";
	public String advSearch = "";
	public String Up = "";
	public String Down = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			/*
			 * {"Keyword", "text", "keyword_arr"}, {"Disposition ID", "numeric", "insurdisposition_id"}, {"Name", "text", "insurdisposition_name"}, {"Active", "boolean", "insurdisposition_active"},
			 * {"Entry By", "text", "insurdisposition_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"}, {"Entry Date", "date", "insurdisposition_entry_date"}, {"Modified By", "text",
			 * "insurdisposition_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"}, {"Modified Date", "date", "insurdisposition_modified_date"}
			 */
			};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_disposition_configurator_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				insurdisposition_id = CNumeric(PadQuotes(request.getParameter("insurdisposition_id")));
				insurdisposition_parent_id = CNumeric(PadQuotes(request.getParameter("insurdisposition_parent_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				if (Up.equals("yes")) {
					MoveUp();
					response.sendRedirect(response.encodeRedirectURL("manage-disposition.jsp?insurdisposition_parent_id=" + insurdisposition_parent_id + "&msg=Disposition Moved UP Successfully!"));
				}
				if (Down.equals("yes")) {
					MoveDown();
					response.sendRedirect(response.encodeRedirectURL("manage-disposition.jsp?insurdisposition_parent_id=" + insurdisposition_parent_id + "&msg=Disposition Moved Down Successfully!"));
				}
				// advSearch = PadQuotes(request.getParameter("advsearch_button"));
				StrHTML = Listdata();
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

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		String StrJoin = "";
		int TotalRecords = 0;
		StrSql = "SELECT insurdisposition_id, insurdisposition_name, insurdisposition_parent_id ";
		CountSql = "SELECT COUNT(insurdisposition_id)";
		StrJoin = " FROM " + compdb(comp_id) + "axela_insurance_disposition"
				+ " WHERE 1 = 1"
				+ " AND insurdisposition_parent_id = " + insurdisposition_parent_id;
		if (!insurdisposition_id.equals("0")) {
			StrJoin += " AND insurdisposition_id = " + insurdisposition_id;
		}

		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}

		StrSql = StrSql + " GROUP BY insurdisposition_id ORDER BY insurdisposition_rank ";
		// // SOP("StrSql===" + StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		try {
			if (TotalRecords != 0) {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = 0;
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">ID</th>\n");
				Str.append("<th>Dispositions</th>\n");
				Str.append("<th data-hide=\"phone\">Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\">").append(crs.getString("insurdisposition_id")).append("</td>\n");
					Str.append("<td align=\"left\">").append(crs.getString("insurdisposition_name"));

					Str.append("<td align=center><a href=\"manage-disposition.jsp?Up=yes&insurdisposition_id=")
							.append(crs.getString("insurdisposition_id"))
							.append("&insurdisposition_parent_id=").append(crs.getString("insurdisposition_parent_id"))
							.append(" \">Up</a> - <a href=\"manage-disposition.jsp?Down=yes&insurdisposition_id=")
							.append(crs.getString("insurdisposition_id"))
							.append("&insurdisposition_parent_id=").append(crs.getString("insurdisposition_parent_id"))
							.append(" \">Down</a></td>\n");
					Str.append("</td>");

					Str.append("<td align=\"left\">");
					if (!crs.getString("insurdisposition_parent_id").equals("0")) {
						Str.append("<a href=\"managedisposition-update.jsp?Update=yes&insurdisposition_id=");
						Str.append(crs.getString("insurdisposition_id")).append(" \">Update Disposition</a><br>");
					}

					Str.append("<a href=managedisposition-update.jsp?Add=yes&insurdisposition_parent_id=")
							.append(crs.getString("insurdisposition_id")).append(" \">Add Child Disposition</a><br>");
					Str.append("<a href=\"manage-disposition.jsp?insurdisposition_parent_id=");
					Str.append(crs.getString("insurdisposition_id")).append(" \">List Child Dispositions</a>");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				crs.close();
			} else {
				Str.append("<br><br><center><font color=\"red\"><b>No Disposition(s) found!</b></font></center>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void MoveUp() {
		int tempRank;
		int disprank;
		String disprank_up;
		try {

			disprank = Integer.parseInt(ExecuteQuery("SELECT insurdisposition_rank "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_id=" + insurdisposition_id + ""));
			// SOP("disprank===" + disprank);

			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(insurdisposition_rank) as min1"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_parent_id = " + insurdisposition_parent_id));
			// SOP("tempRank===" + tempRank);

			disprank_up = ExecuteQuery("SELECT "
					+ " insurdisposition_rank "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition "
					+ " WHERE 1 =1 "
					+ " AND insurdisposition_parent_id = " + insurdisposition_parent_id
					+ " AND insurdisposition_rank < " + disprank
					+ " ORDER BY  insurdisposition_rank DESC "
					+ " LIMIT 1 ");

			if (disprank > tempRank && !disprank_up.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_disposition "
						+ " SET insurdisposition_rank=" + disprank
						+ " WHERE insurdisposition_rank = " + disprank_up
						+ " AND insurdisposition_parent_id = " + insurdisposition_parent_id;
				// SOP("StrSql==1=" + StrSql);
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_disposition"
						+ " SET insurdisposition_rank = " + disprank_up
						+ " WHERE insurdisposition_id=" + insurdisposition_id
						+ " AND insurdisposition_parent_id=" + insurdisposition_parent_id;
				// SOP("StrSql==1=" + StrSql);
				updateQuery(StrSql);

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void MoveDown() {
		int tempRank;
		int disprank;
		String disprank_down;
		// // SOP("sop==");
		try {
			disprank = Integer.parseInt(ExecuteQuery("SELECT insurdisposition_rank "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_id=" + insurdisposition_id + ""));
			// SOP("disprank===" + disprank);

			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(insurdisposition_rank) as max1"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_parent_id = " + insurdisposition_parent_id));
			// SOP("tempRank===" + tempRank);

			disprank_down = ExecuteQuery("SELECT "
					+ " insurdisposition_rank "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition "
					+ " WHERE 1 =1 "
					+ " AND insurdisposition_parent_id = " + insurdisposition_parent_id
					+ " AND insurdisposition_rank > " + disprank
					+ " ORDER BY  insurdisposition_rank "
					+ " LIMIT 1 ");
			// SOP("disprank_down==" + disprank_down);

			if (disprank < tempRank && !disprank_down.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_disposition "
						+ " SET insurdisposition_rank=" + disprank
						+ " WHERE insurdisposition_rank = " + disprank_down
						+ " AND insurdisposition_parent_id = " + insurdisposition_parent_id;
				// SOP("StrSql==1=" + StrSql);
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_disposition"
						+ " SET insurdisposition_rank = " + disprank_down
						+ " WHERE insurdisposition_parent_id=" + insurdisposition_parent_id
						+ " AND insurdisposition_id=" + insurdisposition_id;
				// SOP("StrSql==1=" + StrSql);
				updateQuery(StrSql);

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
