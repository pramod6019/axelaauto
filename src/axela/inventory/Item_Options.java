package axela.inventory;
//aJIt 17th January, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Options extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../inventory/index.jsp>Inventory</a>"
			+ " &gt; <a href=../inventory/inventory-item-list.jsp?all=yes>List Items</a>:";
	public String LinkAddPage = "<a href=../inventory/inventory-item-update.jsp?add=yes>Add New Item..</a>";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String all = "", add = "", delete = "";
	public String option_id = "0", option_itemmaster_id = "0";
	public String option_item_id = "0", item_name = "", itemmaster_name = "";
	public String group_type = "", option_type_name = "", group_type_name = "";
	public String group_name = "", option_group_id = "";
	public String option_qty = "", option_select = "", option_exediscount = "";
	public String option_validfrom = "", option_valid_from = "";
	public String option_validtill = "", option_valid_till = "";
	public String emp_role_id = "0", principal_support = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				add = PadQuotes(request.getParameter("add"));
				delete = PadQuotes(request.getParameter("delete"));
				option_itemmaster_id = PadQuotes(request.getParameter("item_id"));
				option_id = CNumeric(PadQuotes(request.getParameter("option_id")));
				PopulateConfigDetails();
				GetValues(request);
				if (!option_itemmaster_id.equals("")) {
					StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_id = " + CNumeric(option_itemmaster_id) + "";
					itemmaster_name = ExecuteQuery(StrSql);
					if (itemmaster_name.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Items"));
					}
				} else {
					response.sendRedirect("../inventory/inventory-item-list.jsp?all=yes");
				}
				if (add.equals("yes") && !option_item_id.equals("")) {
					AddFields();
				} else if (add.equals("yes") && option_item_id.equals("")) {
					msg = "<br>Select an Item to add!";
				}

				if (!option_id.equals("0") && delete.equals("yes")) {
					DeleteOptionItem();
					msg = "<br>Item deleted successfully!";
				}

				if (!option_itemmaster_id.equals("")) {
					StrSearch += " AND option_itemmaster_id = " + CNumeric(option_itemmaster_id) + "";
				}
				StrHTML = Listdata();
				if (msg.equals("")) {
					option_qty = "1";
					option_select = "1";
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

	public void GetValues(HttpServletRequest request) {
		option_type_name = "";
		option_itemmaster_id = PadQuotes(request.getParameter("item_id"));
		option_item_id = PadQuotes(request.getParameter("txt_item_id"));
		if (!option_item_id.equals("")) {
			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + option_item_id + "";
			item_name = ExecuteQuery(StrSql);
		}
		option_group_id = CNumeric(PadQuotes(request.getParameter("dr_option_group_id")));
		if (!option_group_id.equals("0")) {
			group_type = ExecuteQuery("SELECT group_type"
					+ " FROM " + compdb(comp_id) + "axela_inventory_group"
					+ " WHERE group_id = " + option_group_id);
		}

		if (group_type.equals("1")) {
			group_type_name = "Default";
		} else if (group_type.equals("2")) {
			group_type_name = "All Selected";
		} else if (group_type.equals("3")) {
			group_type_name = "Multi Select";
		}

		option_qty = PadQuotes(request.getParameter("txt_option_qty"));
		option_select = PadQuotes(request.getParameter("chk_option_select"));
		if (option_select.equals("on")) {
			option_select = "1";
		} else {
			option_select = "0";
		}
		option_exediscount = PadQuotes(request.getParameter("chk_option_exediscount"));
		if (option_exediscount.equals("on")) {
			option_exediscount = "1";
		} else {
			option_exediscount = "0";
		}
		option_validfrom = PadQuotes(request.getParameter("txt_option_validfrom"));
		option_valid_from = option_validfrom;
		option_validtill = PadQuotes(request.getParameter("txt_option_validtill"));
		option_valid_till = option_validtill;
		if (option_valid_from.equals("") && option_valid_till.equals("")) {
			option_valid_from = strToShortDate(ToLongDate(kknow()));
			option_valid_till = AddDayMonthYear(option_valid_from, 0, 0, 6, 0);
		}
		principal_support = PadQuotes(request.getParameter("dr_principalsupport_id"));
	}

	public void CheckForm() {
		boolean valid_from = false, valid_till = false;
		if (option_item_id.equals("")) {
			msg = msg + "<br>Select Item to add!";
		} else {
			StrSql = "SELECT option_item_id FROM " + compdb(comp_id) + "axela_inventory_item_option"
					+ " WHERE option_item_id = " + option_item_id + ""
					+ " AND option_itemmaster_id = " + CNumeric(option_itemmaster_id) + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Item already present!";
			} else {
				if (option_group_id.equals("0")) {
					msg = msg + "<br>Select Group!";
				} else {
					StrSql = "SELECT option_item_id from " + compdb(comp_id) + "axela_inventory_item_option"
							+ " WHERE option_item_id = " + option_item_id + ""
							+ " AND option_group_id = '" + option_group_id + "'"
							+ " AND option_itemmaster_id = " + CNumeric(option_itemmaster_id) + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>This item already added for this group!";
					}
				}

				if (group_type.equals("1") && !option_group_id.equals("0")) { // For 'Default' Option Type
					if (option_select.equals("1")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
								+ " SET"
								+ " option_select = '0'"
								+ " WHERE group_type = 1"
								+ " AND option_group_id = '" + option_group_id + "'"
								+ " AND option_itemmaster_id = " + option_itemmaster_id + "";
						updateQuery(StrSql);
					} else {
						StrSql = "SELECT group_type FROM " + compdb(comp_id) + "axela_inventory_item_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group on group_id = option_group_id"
								+ " WHERE group_type = 1"
								+ " AND option_group_id = '" + option_group_id + "'"
								+ " AND option_itemmaster_id = " + CNumeric(option_itemmaster_id) + "";
						if (ExecuteQuery(StrSql).equals("")) {
							option_select = "1";
						}
					}
				}

				if (group_type.equals("2") && option_select.equals("0")) { // For 'All Selected' Option Type
					option_select = "1";
				}

				if (option_qty.equals("")) {
					msg += "<br>Enter Option Quantity!";
				}

				if (!option_validfrom.equals("")) {
					valid_from = isValidDateFormatShort(option_validfrom);
					if (valid_from == true) {
						option_validfrom = ConvertShortDateToStr(option_validfrom);
					} else if (valid_from == false) {
						msg += "<br>Enter Valid 'From Date'!";
					}
				} else {
					msg += "<br>Enter 'From Date'!";
				}

				if (!option_validtill.equals("")) {
					valid_till = isValidDateFormatShort(option_validtill);
					if (valid_till == true) {
						option_validtill = ConvertShortDateToStr(option_validtill);
					} else if (valid_till == false) {
						msg = msg + "<br>Enter Valid 'Till Date'!";
					}
				} else {
					msg = msg + "<br>Enter 'Till Date'!";
				}
				if (valid_from == true && valid_till == true) {
					if (Long.parseLong(option_validfrom) > Long.parseLong(option_validtill)) {
						msg = msg + "<br>'Till Date' must be greater than the 'From date'";
					}
				}
			}
		}

		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_option"
					+ " (option_id,"
					+ " option_itemmaster_id,"
					+ " option_item_id,"
					+ " option_group_id,"
					+ " option_principalsupport,"
					+ " option_select,"
					+ "	option_exediscount,"
					+ " option_qty,"
					+ " option_validfrom,"
					+ " option_validtill)"
					+ " VALUES"
					+ " ((SELECT COALESCE(MAX(option_id), 0) + 1"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option AS ID),"
					+ " '" + option_itemmaster_id + "',"
					+ " '" + option_item_id + "',"
					+ " '" + option_group_id + "',"
					+ " '" + principal_support + "',"
					+ " '" + option_select + "',"
					+ " '" + option_exediscount + "',"
					+ " '" + option_qty + "',"
					+ " '" + option_validfrom + "',"
					+ " '" + option_validtill + "')";
			SOP("StrSql ==addf= " + StrSql);
			updateQuery(StrSql);
			msg = "<br>Item added successfully!";
		}
	}

	public void DeleteOptionItem() {
		StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_item_option"
				+ " where option_id = " + option_id + "";
		updateQuery(StrSql);
	}

	public String Listdata() {
		String group = "";
		int count = 0;
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT item_id, item_name, group_type, group_name, option_qty, item_code,"
				+ " option_validfrom, option_validtill, option_id, option_select, option_exediscount"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
				+ " WHERE 1 = 1";

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " group by group_name, group_type, item_id"
				+ " order by group_rank, group_name";
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th data-hide=\"phone\">Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Selected</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Executive Discount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Qty</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Valid From</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Valid Till</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					if (!group.equals(crs.getString("group_name")) && !group.equals("")) {
						count = 0;
					}
					if (!group.equals(crs.getString("group_name"))) {
						Str.append("<tr align=center>\n");
						Str.append("<th align=center colspan=8>\n");
						Str.append(crs.getString("group_name"));
						Str.append("</th></tr>\n");
					}
					if (crs.getString("group_type").equals("1")) {
						option_type_name = "Default";
					} else if (crs.getString("group_type").equals("2")) {
						option_type_name = "All Selected";
					} else if (crs.getString("group_type").equals("3")) {
						option_type_name = "Multi Select";
					}

					if (crs.getString("option_select").equals("1")) {
						option_select = "Selected";
					} else {
						option_select = "";
					}
					if (crs.getString("option_exediscount").equals("1")) {
						option_exediscount = "Selected";
					} else {
						option_exediscount = "";
					}
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("item_name"));
					if (!crs.getString("item_code").equals("")) {
						Str.append(" <br>").append("Code: ").append(crs.getString("item_code"));
					}

					Str.append("</td>");
					Str.append("<td valign=top align=left>");
					Str.append(option_type_name);
					Str.append("</td>");
					Str.append("<td valign=top align=center >");
					Str.append(option_select);
					Str.append("</td>");
					Str.append("<td valign=top align=center >");
					Str.append(option_exediscount);
					Str.append("</td>");
					Str.append("<td valign=top align=right>");
					Str.append("").append(crs.getString("option_qty"));
					Str.append("</td>");
					Str.append("<td valign=top align=center >");
					Str.append(strToShortDate(crs.getString("option_validfrom")));
					Str.append("</td>");
					Str.append("<td valign=top align=center >");
					Str.append(strToShortDate(crs.getString("option_validtill")));
					Str.append("</td>");
					Str.append("<td valign=top align=left nowrap><a href=\"item-options.jsp?delete=yes&item_id=").append(option_itemmaster_id).append("&option_id=");
					Str.append(crs.getString("option_id")).append(" \">Delete Item</a>");
					Str.append("</td>");
					Str.append("</tr>\n");
					group = crs.getString("group_name");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><b><center><font color=red>No Option Item(s) found!</font></center></b><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {

		StrSql = " SELECT emp_role_id from " + compdb(comp_id) + "axela_emp"
				+ " where emp_id = " + emp_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("StrSql--" + StrSql);
		try {
			while (crs.next()) {
				emp_role_id = crs.getString("emp_role_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateGroups() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select group_id, group_name"
				+ " from " + compdb(comp_id) + "axela_inventory_group";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("group_id"), option_group_id));
				Str.append(">").append(crs.getString("group_name")).append("</option>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrincipalSupport() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", principal_support)).append(">Select</option>\n");
		Str.append("<option value=principalsupport_customer3rdyearextwty").append(StrSelectdrop("principalsupport_customer3rdyearextwty", principal_support))
				.append(">Customer 3rd Year Ext Wty</option>\n");
		Str.append("<option value=principalsupport_customerrsa").append(StrSelectdrop("principalsupport_customerrsa", principal_support)).append(">Customer RSA</option>\n");
		Str.append("<option value=principalsupport_customerinsurance").append(StrSelectdrop("principalsupport_customerinsurance", principal_support))
				.append(">Customer Insurance</option>\n");
		Str.append("<option value=principalsupport_customercashdiscount").append(StrSelectdrop("principalsupport_customercashdiscount", principal_support))
				.append(">Customer Cash Discount</option>\n");
		Str.append("<option value=principalsupport_customerexchange").append(StrSelectdrop("principalsupport_customerexchange", principal_support))
				.append(">Customer Exchange</option>\n");
		Str.append("<option value=principalsupport_customerloyalty").append(StrSelectdrop("principalsupport_customerloyalty", principal_support))
				.append(">Customer Loyalty</option>\n");
		Str.append("<option value=principalsupport_customergovtempscheme").append(StrSelectdrop("principalsupport_customergovtempscheme", principal_support))
				.append(">Customer Govt Emp Scheme</option>\n");
		Str.append("<option value=principalsupport_customerotherbenefit").append(StrSelectdrop("principalsupport_customerotherbenefit", principal_support))
				.append(">Customer Other Benefit</option>\n");
		Str.append("<option value=principalsupport_customercorporate").append(StrSelectdrop("principalsupport_customercorporate", principal_support))
				.append(">Customer Corporate</option>\n");
		return Str.toString();
	}
}
