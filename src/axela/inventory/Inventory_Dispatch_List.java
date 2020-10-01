package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Dispatch_List extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String inst_id = "";
	public String StrHTML = "";
	public String all = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String dispatch_id = "";
	public String warehouse_id = "";
	public String item_id = "";
	public String prevsubgroup_id = "";
	public String cat_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				all = PadQuotes(request.getParameter("all"));
				msg = PadQuotes(request.getParameter("msg"));
				dispatch_id = PadQuotes(request.getParameter("dispatch_id"));
				warehouse_id = PadQuotes(request.getParameter("dr_warehouse"));
				item_id = PadQuotes(request.getParameter("dr_item"));
				cat_id = PadQuotes(request.getParameter("dr_category"));

				if (warehouse_id.equals("")) {
					warehouse_id = "0";
				}
				if (cat_id.equals("")) {
					cat_id = "0";
				}
				if (item_id.equals("")) {
					item_id = "0";
				}
				if (warehouse_id.equals("0")) {
					msg = "Select Location!";
				}
				if (cat_id.equals("0")) {
					msg = msg + "<br>Select Category!";
				}
				if (item_id.equals("0")) {
					msg = msg + "<br>Select Item!";
				}
				if (!(warehouse_id.equals("0")) && !(cat_id.equals("0")) && !(item_id.equals("0"))) {
					msg = msg + "<br>Results for Item!";
					StrSearch = StrSearch + " and dispatch_warehouse_id =" + CNumeric(warehouse_id) + "";
					StrSearch = StrSearch + " and item_cat_id =" + CNumeric(cat_id) + "";
					StrSearch = StrSearch + " and item_id =" + CNumeric(item_id) + "";
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
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = "SELECT dispatch_id, dispatch_date, dispatch_qty, dispatch_item_id, dispatch_naration, cat_name, cat_pid,"
				+ " item_name, item_code, warehouse_name, warehouse_pid";

		StrJoin = " FROM campus_inventory_item_dispatch"
				+ " INNER JOIN campus_inventory_warehouse ON warehouse_id = dispatch_warehouse_id"
				+ " INNER JOIN campus_inventory_item ON item_id = dispatch_item_id"
				+ " INNER JOIN campus_inventory_cat ON cat_id = item_cat_id"
				+ " WHERE dispatch_inst_id = " + inst_id;

		CountSql = "SELECT COUNT(distinct dispatch_id)";
		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		CountSql = ExecuteQuery(CountSql);
		if (!CountSql.equals("")) {
			TotalRecords = Integer.parseInt(CountSql);
		} else {
			TotalRecords = 0;
		}

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Dispatch(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "inventory-dispatch-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " GROUP BY dispatch_id ORDER BY dispatch_entry_date ";
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<table border=1 cellpadding=0 cellspacing=0 class=listtable>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th>Category</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th>Location</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Quantity</th>\n");
				Str.append("<th>Narration</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center ><b>").append(count).append(".</b></td>\n");
					Str.append("<td valign=top align=left ><a href=\"inventory-cat-list.jsp?cat_id=").append(cat_id).append(" \">").append(crs.getString("cat_name")).append(" (")
							.append(crs.getString("cat_pid")).append(")" + "</a>");
					Str.append("</td>\n");
					Str.append("<td valign=top align=left ><a href=\"inventory-item-list.jsp?item_id=").append(item_id).append(" \">").append(crs.getString("item_name"));
					if (!crs.getString("item_code").equals("")) {
						Str.append(" (").append(crs.getString("item_code")).append(")");
					}
					Str.append("</a></td>\n");
					Str.append("<td valign=top align=left ><a href=\"inventory-warehouse-list.jsp?warehouse_id=").append(warehouse_id).append(" \">").append(crs.getString("warehouse_name"))
							.append(" (").append(crs.getString("warehouse_pid")).append(")" + "</a></td>\n");
					Str.append("<td valign=top align=center >").append(strToLongDate(crs.getString("dispatch_date"))).append("</td>");
					Str.append("<td valign=top align=right >").append(crs.getString("dispatch_qty")).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("dispatch_naration")).append("</td>\n");
					Str.append("<td valign=top align=left nowrap><a href=\"inventory-dispatch-update.jsp?Update=yes&dispatch_id=").append(crs.getString("dispatch_id")).append(" \">Update</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><font color=red><b>No Dispatch(s) found!</b></font>");
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT warehouse_id, warehouse_name"
					+ " FROM campus_inventory_warehouse"
					+ " WHERE warehouse_inst_id = " + inst_id + ""
					+ " ORDER BY warehouse_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("warehouse_id"));
				Str.append(StrSelectdrop(crs.getString("warehouse_id"), warehouse_id));
				Str.append(">").append(crs.getString("warehouse_name")).append(" (").append(crs.getString("warehouse_id")).append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = " SELECT cat_id, cat_pid, cat_name from campus_inventory_cat "
					+ " WHERE cat_inst_id = " + inst_id
					+ " ORDER BY cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), cat_id));
				Str.append(">").append(crs.getString("cat_name")).append(" (").append(crs.getString("cat_pid")).append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		try {
			if (cat_id.equals("")) {
				cat_id = "0";
			}
			StrSql = "SELECT item_id, item_code, item_name from campus_inventory_item "
					+ " WHERE item_inst_id=" + inst_id
					+ " AND item_cat_id=" + cat_id
					+ " AND item_active = '1' "
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("");
				if (!crs.getString("item_code").equals("")) {
					Str.append(" (").append(crs.getString("item_code")).append(")");
				}
				Str.append("</option>\n");
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
