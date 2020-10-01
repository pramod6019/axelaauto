package axela.inventory;
//aJIt 29th November 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Prepaiditems extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String LinkListPage = "inventory-prepaiditems.jsp";
	public String ExportPerm = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "", qty = "";
	public String add = "", delete = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String prepaid_item_id = "", itemmaster_id = "";
	public String item_name = "", item_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				add = PadQuotes(request.getParameter("add"));
				delete = PadQuotes(request.getParameter("delete"));
				qty = PadQuotes(request.getParameter("qty"));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				itemmaster_id = CNumeric(PadQuotes(request.getParameter("trans_itemmaster_id")));
				// SOP("itemmaster_id==" + itemmaster_id);
				if (!itemmaster_id.equals("0")) {
					StrSql = "select item_name, item_code "
							+ " from " + compdb(comp_id) + "axela_inventory_item"
							+ " where item_loyaltycard_id=1 and item_id=" + itemmaster_id
							+ " order by item_name ";
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							item_name = crs.getString("item_name");
						}
					} else {
						msg = "msg=Invalid Prepaid Card Item!";
						response.sendRedirect("../portal/error.jsp?" + msg);
					}
					crs.close();
				}
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=index.jsp>Inventory</a> &gt; <a href=inventory-item-list.jsp?all=yes>List Items</a> &gt; <a href=inventory-item-list.jsp?item_id="
						+ itemmaster_id + ">" + item_name + "</a> > <a href=../inventory/inventory-prepaiditems.jsp?trans_itemmaster_id=" + itemmaster_id + ">List Prepaid Items</a>:";

				if (!(itemmaster_id.equals("0")) && !(item_id.equals("0")) && add.equals("yes")) {
					AddPrepaidItem();
				}
				if (!(itemmaster_id.equals("0")) && !(item_id.equals("0")) && delete.equals("yes")) {
					DeletePrepaidItem();
				}
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
		StrSql = " Select prepaid_itemmaster_id, prepaid_item_id, prepaid_qty, item_name, item_code"
				+ " FROM " + compdb(comp_id) + "axela_inventory_prepaiditem"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = prepaid_item_id"
				+ " where prepaid_itemmaster_id =" + itemmaster_id + ""
				+ " group by prepaid_item_id desc"
				+ " order by item_name desc";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			StrHTML = "";
			if (crs.isBeforeFirst()) {
				int count = 0;
				StrHTML = StrHTML + "\n<table width=100%  border=1 cellpadding=0 cellspacing=0 class=listtable>\n";
				StrHTML = StrHTML + "<tr align=center>\n";
				StrHTML = StrHTML + "<th>#</th>\n";
				StrHTML = StrHTML + "<th>Item Details</th>\n";
				StrHTML = StrHTML + "<th>Qty</th>\n";
				StrHTML = StrHTML + "<th>Action</th>\n";
				StrHTML = StrHTML + "</tr>\n";
				while (crs.next()) {
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					count = count + 1;
					StrHTML = StrHTML + "<tr>\n";
					StrHTML = StrHTML + "<td valign=top align=center >" + count + ".</td>\n";
					StrHTML = StrHTML + "<td valign=top align=left >" + item_name + "";
					StrHTML = StrHTML + "</td>\n";
					StrHTML = StrHTML + "<td valign=top align=right nowrap>" + crs.getString("prepaid_qty");
					StrHTML = StrHTML + "</td>\n";
					StrHTML = StrHTML + "<td valign=top align=center ><a href=\"javascript:DeletePrepaidItem(" + crs.getString("prepaid_item_id") + ");\">Delete Item</a>";
					StrHTML = StrHTML + "</td>\n";
					StrHTML = StrHTML + "</tr>\n";
				}
				StrHTML = StrHTML + "</table>\n";
			} else {
				StrHTML = StrHTML + "<font color=red><b>No Items found for this prepaid card!</b></font>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return StrHTML;
	}

	public void AddPrepaidItem() {
		try {
			if (qty.equals("0") || qty.equals("")) {
				qty = "1";
			}
			StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_prepaiditem"
					+ " where prepaid_itemmaster_id = " + itemmaster_id + ""
					+ " and prepaid_item_id = " + item_id + "";
			updateQuery(StrSql);
			StrSql = "Insert into " + compdb(comp_id) + "axela_inventory_prepaiditem"
					+ " (prepaid_itemmaster_id,"
					+ " prepaid_item_id,"
					+ " prepaid_qty)"
					+ " values"
					+ " (" + itemmaster_id + ","
					+ " " + item_id + ","
					+ " " + qty + ")";
			updateQuery(StrSql);
			msg = "Item Added Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void DeletePrepaidItem() {
		try {
			StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_prepaiditem"
					+ " where prepaid_itemmaster_id = " + itemmaster_id + ""
					+ " and prepaid_item_id = " + item_id + "";
			updateQuery(StrSql);
			msg = "Item Deleted Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
