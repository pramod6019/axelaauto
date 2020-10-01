package axela.accounting;

// shivaprasad 8 oct 2014

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Location_Stock extends Connect {

	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String RecCountDisplay = "";
	public String item_id = "0";
	public String branch_id = "0";
	public String location = "";

	Map<Integer, Object> map = new HashMap<Integer, Object>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				if (!GetSession("emp_id", request).equals("")) {
					emp_id = CNumeric(GetSession("emp_id", request));

					location = PadQuotes(request.getParameter("location"));
					item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
					branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					if (location.equals("yes")) {
						StrHTML = Listdata();
					}

				} else {
					StrHTML = "SignIn";
				}
			}
		} catch (Exception ex) {
			// SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		int count = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!item_id.equals("0")) {
			StrSearch += " AND item_id =" + item_id;
		}
		if (!branch_id.equals("0")) {
			StrSearch += " AND location_branch_id = " + branch_id;
		}

		StrSql = "SELECT location_id, location_name, stock_current_qty,"
				+ " item_name, item_code"
				+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_stock ON stock_location_id = location_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = stock_item_id"
				+ " WHERE 1=1"
				+ StrSearch
				+ " ORDER BY location_name";
		try {
			// SOP("StrSql==ls=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>");
				Str.append("<td colspan=\"3\"><b>").append("(" + crs.getString("item_code") + ") " + crs.getString("item_name")).append("</b></td>");
				Str.append("</tr>");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Location</th>\n");
				Str.append("<th>Quantity</th>\n");
				Str.append("</tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("location_name")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("stock_current_qty")).append("</td>\n");
					Str.append("</tr>\n");
				}

				Str.append("</table>\n");
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red><b>No Record Found!</b></font><br><br>";
				return RecCountDisplay;
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {

			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}
}
