// smitha nag 26 june 2013
package axela.portal;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "";
	public String StrSearch = "", modSearch = "";
	public static ArrayList<String> comp_mod_name_arr = new ArrayList<String>();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_mis_access", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
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

	public String ListReports() {
		StringBuilder Str = new StringBuilder();
		String check_module_id = "0";
		int count = 0;

		try {
			StrSql = "SELECT concat('comp_module_',module_name) as module_name"
					+ " from axela_module";
			CachedRowSet rscomp = processQuery(StrSql, 0);
			if (rscomp.isBeforeFirst()) {
				while (rscomp.next()) {
					Str.append(rscomp.getString("module_name")).append(", ");
					comp_mod_name_arr.add(rscomp.getString("module_name"));
				}
				StrSearch = Str.toString();
				StrSearch = StrSearch.substring(0, StrSearch.lastIndexOf(","));
				// SOP("StrSearch----------" + StrSearch);
			}
			rscomp.close();
			StrSql = "SELECT " + StrSearch + " FROM " + compdb(comp_id)
					+ "axela_comp";
			rscomp = processQuery(StrSql, 0);
			int mod_name_arr_count = comp_mod_name_arr.size();
			if (rscomp.isBeforeFirst()) {
				Str = new StringBuilder();
				Str.append(" and (");
				while (rscomp.next()) {
					for (int i = 0; i < mod_name_arr_count; i++) {
						if (rscomp.getString(comp_mod_name_arr.get(i)).equals(
								"1")) {
							String mod_name[] = comp_mod_name_arr.get(i).split(
									"_");
							Str.append(" module_name='").append(
									mod_name[2].toString().toLowerCase());
							Str.append("' or ");
						}
					}
				}
				modSearch = Str.toString();
				modSearch = modSearch.substring(0, modSearch.lastIndexOf("or"))
						+ " )";
			}
			rscomp.close();

			StrSql = "SELECT report_id, report_name, report_url, module_id, module_name"
					+ " FROM axela_module_report"
					+ " INNER JOIN axela_module ON module_id = report_module_id"
					+ " WHERE report_misdisplay = 1"
					+ " AND report_active = 1"
					+ modSearch + " ORDER BY module_rank, report_rank";
			SOP("StrSql-----res---" + StrSqlBreaker(StrSql));
			int space_count = 0;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str = new StringBuilder();
				Str.append("<table class=\"table table-bordered\">");
				while (crs.next()) {
					space_count = space_count + 1;
					if (!check_module_id.equals(crs.getString("module_id"))) {
						if (count == 1) {
							// Str.append("<tr><td>&nbsp;</td></tr>\n");
							count = 0;
						}
						if (space_count != 1) {
							// Str.append("<tr><td colspan=2>&nbsp;</td></tr>");
						}
						Str.append("<tr>\n<th colspan=2 >\n");
						Str.append("<b>").append(crs.getString("module_name"))
								.append("</b></th>\n</tr>\n");
						check_module_id = crs.getString("module_id");
					}
					count = count + 1;
					if (count == 1) {
						Str.append("<tr>\n");
					}
					Str.append("<td><a href=" + crs.getString("report_url")
							+ " target=_blank >" + crs.getString("report_name")
							+ "</a></td>");
					if (count == 2) {
						Str.append("</tr>\n");
						count = 0;
					}
				}
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
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
