package axela.inventory;
/*
 *@author GuruMurthy TS 19 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Import_Stock extends Connect {
	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0";
	public String BranchAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_stock_add", request, response);
		if (!comp_id.equals("0")) {
			emp_id = session.getAttribute("emp_id").toString();
			BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
			StrHTML = GetStockImportList();
		}
	}

	public String GetStockImportList()
	{
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " " + BranchAccess
					+ " GROUP BY branch_brand_id"
					+ " ORDER BY branch_brand_id";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// SOP("branch_brand_id==" + crs.getString("branch_brand_id"));
					Str.append("<tr align=\"center\">\n");
					if (crs.getString("branch_brand_id").equals("2")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-maruti.jsp><b>Click Here To Import Maruti Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("6")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-hyundai.jsp><b>Click Here To Import Hyundai Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("7")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-ford.jsp><b>Click Here To Import Ford Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("10")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-nexa.jsp><b>Click Here To Import Nexa Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("151")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-triumph.jsp><b>Click Here To Import Triumph Stock</b></a>");
						Str.append("</td>");
					}

					// else if (crs.getString("branch_brand_id").equals("9"))
					// {
					// Str.append("<td>");
					// Str.append("<br><a href=stock-user-import-honda.jsp><b>Click Here To Import Honda Stock</b></a>");
					// Str.append("</td>");
					// }
					// else if
					// (crs.getString("branch_brand_id").equals("51")) {
					// Str.append("<td>");
					// Str.append("<br><a href=stock-user-import-volvo.jsp><b>Click Here To Import Volvo Stock</b></a>");
					// Str.append("</td>");
					// }

					// this is suzuki 2 wheeler (1011 D/B)
					else if (crs.getString("branch_brand_id").equals("101")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-suzuki.jsp><b>Click Here To Import Suzuki Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("102")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-yamaha.jsp><b>Click Here To Import Yamaha Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("56")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-porsche.jsp><b>Click Here To Import Porsche Stock</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("60")) {
						Str.append("<td>");
						Str.append("<br><a href=stock-user-import-jlr.jsp><b>Click Here To Import JLR Stock</b></a>");
						Str.append("</td>");
					}

					Str.append("</tr>\n");
				}
			}
			else
			{
				Str.append("<font color=\"red\"><b> No Branch Found!</b></font>");
			}
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
