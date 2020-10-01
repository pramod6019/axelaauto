package axela.inventory;

/*saiman 9th july 2012*/

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Price_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "", effective_from = "";
	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";
	public String price_id = "0";
	public String price_rateclass_id = "0";
	public String item_id = "0";
	public String price_item_id = "0";
	public String item_name = "";
	public String price_amt = "";
	public String pricetrans_variable = "0";
	public String price_disc_type = "0";
	public String price_disc = "", price_desc = "";
	public String price_active = "0";
	public String price_variable = "0";
	public String price_effective_from = "", emp_id = "0";
	public String price_entry_id = "", price_entry_by = "",
			price_modified_id = "0";
	public String price_modified_by = "", entry_date = "";
	public String modified_date = "", price_entry_date = "";
	public String price_modified_date = "";
	public String branch_id = "";
	public String cat_id = "0";
	public String StrHTML = "";
	// BranchClass
	public String rateclass_type = "1", rate_classtype = "", rate_class_type_id = "0";

	public String QueryString = "";
	public String comp_module_dealer = "0", price_customer_price = "";
	public String price_exe_comm = "", price_technician_comm = "",
			price_dealerexe_comm = "";
	public String comp_module_accounting = "0";
	public String comp_businesstype_id = "0";
	public String comp_module_laundry = "0";
	public String emp_role_id = "0";
	public String config_inventory_current_stock = "0";
	public String config_customer_name = "";
	public String checked = "";
	public String jc_id = "0";
	public DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request);
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				emp_role_id = CNumeric(GetSession("emp_role_id", request) + "") + "";
				branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				price_id = CNumeric(PadQuotes(request.getParameter("price_id")));
				price_item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				rate_classtype = PadQuotes(request.getParameter("rateclasstype"));
				rate_class_type_id = CNumeric(PadQuotes(request.getParameter("rateclass_type")));
				comp_module_accounting = CNumeric(GetSession("comp_module_accounting", request));
				rateclass_type = CNumeric(PadQuotes(request.getParameter("dr_rateclass_type")));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				if (rateclass_type.equals("0")) {
					rateclass_type = "1";
				}
				if (rate_classtype.equals("yes")) {
					if (branch_id.equals("0")) {
						branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					}
					StrHTML = PopulateBranchClass(rate_class_type_id, branch_id, jc_id);
				}
				if (!price_item_id.equals("0")) {
					StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_id = "
							+ price_item_id + "" + " ORDER BY item_name";
					item_name = ExecuteQuery(StrSql);
				}
				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						price_active = "1";
						price_effective_from = ToLongDate(kknow());
						effective_from = strToShortDate(price_effective_from);
					} else {
						if (ReturnPerm(comp_id, "emp_item_add", request).equals("1")) {
							GetValues(request, response);
							price_entry_id = GetSession("emp_id", request);
							price_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../inventory/item-price-list.jsp?item_id="
												+ price_item_id
												+ "&price_id="
												+ price_id
												+ "&msg=Item Price added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Price")) {
						PopulateFields();
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Price")) {
						if (ReturnPerm(comp_id, "emp_item_edit", request).equals("1")) {
							GetValues(request, response);
							price_modified_id = GetSession("emp_id", request)
									.toString();
							price_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../inventory/item-price-list.jsp?item_id="
												+ price_item_id
												+ "&price_id="
												+ price_id
												+ "&msg=Item Price updated successfully!"
												+ msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Price")) {
						if (ReturnPerm(comp_id, "emp_item_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../inventory/item-price-list.jsp?item_id="
												+ price_item_id
												+ "&msg=Item Price deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
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
		rateclass_type = CNumeric(PadQuotes(request.getParameter("dr_rateclass_type")));
		price_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_price_rateclass_id")));
		price_item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
		price_amt = CNumeric(PadQuotes(request.getParameter("txt_price_amt")));
		price_disc_type = CNumeric(PadQuotes(request.getParameter("dr_price_disc_type")));
		price_desc = PadQuotes(request.getParameter("txt_price_desc"));
		price_disc = CNumeric(PadQuotes(request.getParameter("txt_price_disc")));
		price_active = CheckBoxValue(PadQuotes(request.getParameter("chk_price_active")));

		if (comp_module_dealer.equals("1")) {
			price_customer_price = CNumeric(PadQuotes(request.getParameter("txt_price_customer_price")));
			price_exe_comm = CNumeric(PadQuotes(request.getParameter("txt_price_exe_comm")));
			price_technician_comm = CNumeric(PadQuotes(request.getParameter("txt_price_technician_comm")));
			price_dealerexe_comm = CNumeric(PadQuotes(request.getParameter("txt_price_dealerexe_comm")));
		}
		price_variable = CheckBoxValue(PadQuotes(request.getParameter("chk_price_variable")));
		price_effective_from = ConvertShortDateToStr(PadQuotes(request.getParameter("txt_price_effective_from")));
		effective_from = strToShortDate(price_effective_from);
		price_entry_by = PadQuotes(request.getParameter("price_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		price_modified_by = PadQuotes(request.getParameter("price_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

	}
	protected void CheckForm() {
		msg = "";

		if (price_rateclass_id.equals("0")) {
			msg += "<br>Select Rate Class!";
		}

		if (price_amt.equals("")) {
			msg += "<br>Enter Amount!";
		}

		if (!price_amt.equals("") && !isNumeric(price_amt)) {
			msg += "<br>Amount: Enter Numeric!";
		}

		if (price_disc.equals("")) {
			msg += "<br>Enter Discount!";
		}

		if (!price_disc.equals("") && !isNumeric(price_disc)) {
			msg += "<br>Discount: Enter Numeric!";
		}

		if (!price_disc.equals("") && isNumeric(price_disc)
				&& !price_amt.equals("") && isNumeric(price_amt)) {
			if (price_disc_type.equals("1")) {
				if (Double.parseDouble(price_disc) > Double
						.parseDouble(price_amt)) {
					msg += "<br>Discount should be less than Amount!";
				}
			}
			if (price_disc_type.equals("2")) {
				if (Double.parseDouble(price_disc) > 100) {
					msg += "<br>Discount can not be more than 100%!";
				}
			}
		}
		if (!price_effective_from.equals("")) {
			if (isValidDateFormatStr(price_effective_from)) {
				effective_from = strToShortDate(price_effective_from);
			} else {
				msg += "<br>Enter Valid Effective Date!";
				effective_from = price_effective_from;
			}
		} else {
			msg += "<br>Enter Effective From!";
			effective_from = price_effective_from;
		}

		if (!price_rateclass_id.equals("0") && !price_effective_from.equals("")) {
			if (update.equals("yes")) {
				StrSql = "SELECT price_item_id"
						+ " FROM  " + compdb(comp_id) + "axela_inventory_item_price"
						+ " WHERE price_effective_from = '"
						+ price_effective_from
						+ "'"
						+ " AND price_item_id = "
						+ price_item_id
						+ ""
						+ " AND price_rateclass_id	 = "
						+ price_rateclass_id
						+ ""
						+ " AND price_id != "
						+ price_id + "";
			} else if (add.equals("yes")) {
				StrSql = "SELECT price_item_id"
						+ " FROM  " + compdb(comp_id) + "axela_inventory_item_price"
						+ " WHERE price_effective_from = '"
						+ price_effective_from
						+ "'"
						+ " AND price_rateclass_id	 = "
						+ price_rateclass_id
						+ "" + " AND price_item_id = " + price_item_id + "";
			}
			// SOP("StrSql===" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Item Price already updated this Date!";
			}
		}
	}

	protected void AddFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " ("
					+ " price_rateclass_id," + " price_item_id,"
					+ " price_desc,"
					+ " price_amt,"
					+ " price_disc,"
					+ " price_disc_type,"
					+ " price_variable,";
			if (comp_module_dealer.equals("1")) {
				StrSql += " price_customer_price," + " price_exe_comm,"
						+ " price_technician_comm," + " price_dealerexe_comm,";
			}
			StrSql += " price_effective_from," + " price_active,"
					+ " price_entry_id," + " price_entry_date)" + " VALUES"
					+ " ("
					+ " " + price_rateclass_id + ","
					+ " " + price_item_id + ","
					+ " '" + price_desc + "',"
					+ " " + price_amt + ","
					+ " " + price_disc + ","
					+ " " + price_disc_type + ","
					+ " '" + price_variable + "',";
			if (comp_module_dealer.equals("1")) {
				StrSql += " " + price_customer_price + "," + " "
						+ price_exe_comm + "," + " " + price_technician_comm
						+ "," + " " + price_dealerexe_comm + ",";
			}
			StrSql += " '" + price_effective_from + "'," + " '" + price_active
					+ "'," + " " + price_entry_id + "," + " "
					+ ToLongDate(kknow()) + ")";
			price_id = UpdateQueryReturnID(StrSql);

			StrSql = "SELECT item_type_id"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + price_item_id + "";
			if (ExecuteQuery(StrSql).equals("1")) {
				AddConfigItemPrice(request);
			}
		}
	}

	public void AddConfigItemPrice(HttpServletRequest request) {
		int config_item_count = Integer.parseInt(CNumeric(PadQuotes(request
				.getParameter("txt_config_item_count"))));
		String pricetrans_item_id = "", pricetrans_amt = "";

		if (config_item_count > 0) {
			StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price_trans"
					+ " (pricetrans_price_id," + " pricetrans_item_id, pricetrans_variable,"
					+ " pricetrans_amt)" + " VALUES";
			for (int i = 1; i <= config_item_count; i++) {
				pricetrans_item_id = CNumeric(PadQuotes(request.getParameter("txt_config_" + i + "_id")));
				pricetrans_amt = CNumeric(PadQuotes(request.getParameter("txt_config_" + i + "_amt")));
				pricetrans_variable = CheckBoxValue(PadQuotes(request.getParameter("txt_config_" + i + "_var")));
				StrSql += " (" + price_id + ","
						+ " '" + pricetrans_item_id + "',"
						+ " '" + pricetrans_variable + "',"
						+ " '" + pricetrans_amt + "'),";
			}
			StrSql = StrSql.substring(0, StrSql.lastIndexOf(","));
			// SOP("StrSql=AddConfigItemPrice==" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT * , rateclass_type"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = price_rateclass_id	"
					+ " WHERE price_id = " + price_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				price_rateclass_id = crs.getString("price_rateclass_id");
				price_item_id = crs.getString("price_item_id");
				price_amt = df.format(crs.getDouble("price_amt"));
				price_desc = crs.getString("price_desc");
				price_disc = df.format(crs.getDouble("price_disc"));
				price_disc_type = crs.getString("price_disc_type");
				price_variable = crs.getString("price_variable");
				if (comp_module_dealer.equals("1")) {
					price_customer_price = crs.getString("price_customer_price");
					price_exe_comm = crs.getString("price_exe_comm");
					price_technician_comm = crs
							.getString("price_technician_comm");
					price_dealerexe_comm = crs.getString("price_dealerexe_comm");
				}

				price_effective_from = crs.getString("price_effective_from");
				effective_from = strToShortDate(price_effective_from);
				price_active = crs.getString("price_active");

				rateclass_type = crs.getString("rateclass_type");

				price_entry_id = crs.getString("price_entry_id");
				if (!price_entry_id.equals("0")) {
					price_entry_by = Exename(comp_id, Integer.parseInt(price_entry_id));
				}
				entry_date = strToLongDate(crs.getString("price_entry_date"));
				price_modified_id = crs.getString("price_modified_id");
				if (!price_modified_id.equals("0")) {
					price_modified_by = Exename(comp_id, Integer.parseInt(price_modified_id));
				}
				modified_date = strToLongDate(crs
						.getString("price_modified_date"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " SET"
					+ " price_rateclass_id	 = " + price_rateclass_id + ","
					+ " price_item_id = " + price_item_id + ","
					+ " price_desc = '" + price_desc + "',"
					+ " price_amt = " + price_amt + ","
					+ " price_disc = " + price_disc + ","
					+ " price_disc_type = " + price_disc_type + ","
					+ " price_variable = " + price_variable + ",";
			if (comp_module_dealer.equals("1")) {
				StrSql += " price_customer_price = " + price_customer_price
						+ "," + " price_exe_comm = " + price_exe_comm + ","
						+ " price_technician_comm = " + price_technician_comm
						+ "," + " price_dealerexe_comm = "
						+ price_dealerexe_comm + ",";
			}
			StrSql += " price_effective_from = '" + price_effective_from + "',"
					+ " price_active = " + price_active + ","
					+ " price_modified_id = " + price_modified_id + ","
					+ " price_modified_date = " + (ToLongDate(kknow())) + ""
					+ " WHERE price_id = " + price_id + "";
			// SOP("StrSql-update====" + StrSql);
			updateQuery(StrSql);

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_inventory_item_price_trans"
					+ " WHERE pricetrans_price_id = " + price_id + "";
			updateQuery(StrSql);

			StrSql = "SELECT item_type_id FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + price_item_id + "";
			if (ExecuteQuery(StrSql).equals("1")) {
				AddConfigItemPrice(request);
			}
		}
	}

	public String GetConfigItemPrice() {
		int config_item_count = 0;
		String group_name = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COALESCE(pricetrans_amt, 0) AS pricetrans_amt, item_id, group_name, item_name,"
					+ " COALESCE(pricetrans_item_id, 0) AS pricetrans_item_id, COALESCE(pricetrans_variable, 0) AS pricetrans_variable, option_item_id, item_code"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = " + price_id + ""
					+ " AND pricetrans_item_id = item_id"
					+ " WHERE 1 = 1"
					+ " AND option_itemmaster_id = " + price_item_id + ""
					+ " AND item_active = 1"
					+ " AND group_active = 1"
					+ " GROUP BY group_name, group_type, item_id"
					+ " ORDER BY group_rank, group_name DESC, item_id";
			// SOP("StrSql===ConfigItemPrice====================" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<center>");
				Str.append("<div class=\"table table-bordered\"  style=\"width:100%;align:center\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th>Code</th>\n");
				Str.append("<th>Variable</th>\n");
				Str.append("<th>Price</th>\n</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					config_item_count++;
					if (!group_name.equals(crs.getString("group_name"))) {
						group_name = crs.getString("group_name");
						Str.append("<tr align=\"center\" valign=\"top\">\n<thead><th colspan=\"4\">");
						Str.append(group_name).append("</th></thead>\n</tr>\n");
					}
					Str.append(
							"<tr align=\"center\" valign=\"top\">\n<td align=\"left\">")
							.append(crs.getString("item_name"));
					Str.append("<input type=\"hidden\" id=\"txt_config_")
							.append(config_item_count).append("_id");
					Str.append("\" name=\"txt_config_")
							.append(config_item_count).append("_id\" value=\"")
							.append(crs.getString("option_item_id"))
							.append("\"/>\n");
					Str.append("</td>\n<td align=\"left\">").append(
							crs.getString("item_code"));

					if (crs.getString("pricetrans_variable").equals("1")) {
						checked = "checked";
					}
					else {
						checked = "";
					}

					Str.append("<td align=\"center\"><input type=\"checkbox\" id=\"chk_config_").append(config_item_count).append("_var");
					Str.append("\" name=\"txt_config_").append(config_item_count).append("_var\").append(\" ").append(checked).append(" /></td>");

					Str.append("<input type=\"hidden\" id=\"txt_config_").append(config_item_count).append("_var");
					Str.append("\" name=\"txt_config_").append(config_item_count).append("_var\" value=\"").append(checked).append("\"/>\n");

					Str.append(
							"</td>\n<td align=\"center\"><input type=\"text\" id=\"txt_config_")
							.append(config_item_count).append("_amt");
					Str.append("\" name=\"txt_config_")
							.append(config_item_count)
							.append("_amt\" class=\"form-control\" value=\"")
							.append(crs.getString("pricetrans_amt"));
					Str.append("\" onKeyUp=\"toNumber('txt_config_")
							.append(config_item_count)
							.append("_amt','Amount')\" size=\"10\" maxlength=\"10\">\n</td>\n</tr>\n");
				}
				Str.append(
						"<input type=\"hidden\" id=\"txt_config_item_count\" name=\"txt_config_item_count\" value=\"")
						.append(config_item_count).append("\"/>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_inventory_item_price_trans"
					+ " WHERE pricetrans_price_id = " + price_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_id = " + price_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateBranchClassType() {
		StringBuilder Str = new StringBuilder();

		Str.append("<option value=").append("1").append(StrSelectdrop("1", rateclass_type));
		Str.append(">").append("Sales").append("</option>\n");
		Str.append("<option value=").append("2").append(StrSelectdrop("2", rateclass_type)).append(">");
		Str.append("Purchase").append("</option>\n");

		return Str.toString();

	}

	public String PopulateBranchClass(String rateclass_type, String Branch_id, String jc_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class";
			if (!jc_id.equals("0") && rateclass_type.equals("1")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_rateclass_id = rateclass_id";
			}

			StrSql += " WHERE 1 = 1";

			if (!jc_id.equals("0") && rateclass_type.equals("1")) {
				StrSql += " AND branch_id = " + Branch_id;
			}

			StrSql += " AND rateclass_type = " + rateclass_type
					+ " ORDER BY rateclass_name";
			// SOP("PopulateBranchClass====StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_price_rateclass_id' id='dr_price_rateclass_id' class='form-control' style='margin-top: 9px;'>");
			if (jc_id.equals("0")) {
				Str.append("<option value=0>Select</option>\n");
			}
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), price_rateclass_id)).append(">");
				Str.append(crs.getString("rateclass_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateDiscountType() {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<option value=1")
					.append(StrSelectdrop("1", price_disc_type)).append(">")
					.append("Amount").append("</option>\n");
			Str.append("<option value=2")
					.append(StrSelectdrop("2", price_disc_type)).append(">")
					.append("Percentage").append("</option>\n");
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
