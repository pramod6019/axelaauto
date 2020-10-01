package axela.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.Ledger_Check;
import cloudify.connect.Connect;

public class Inventory_Item_Update extends Connect {
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";

	public String StrSql = "";
	public String msg = "";
	public String item_id = "0";
	public String item_name = "";
	public String item_code = "";
	public String item_service_code = "";
	public String item_hsn = "";
	public String item_sac = "";
	public String item_cat_id = "0";
	public String item_small_desc = "";
	public String item_big_desc = "";
	public String item_perishable = "";
	public String item_nonstock = "";
	public String item_url = "";
	public String item_serial = "0";
	public String item_type_id = "0";
	public String item_fueltype_id = "0";
	public String item_loyaltycard_id = "0";
	public static String loyaltycard_id = "0";
	public String item_durtype = "0";
	public String item_durcount = "0";
	public String item_uom_id = "0", item_alt_uom_id = "0";
	public String item_eoq = "";
	public String item_aftertaxcal = "";
	public String item_aftertaxcal_formulae = "";
	public String item_active = "0";
	public String item_notes = "";
	public String item_entry_date = "";
	public String item_modified_date = "";
	public String item_entry_id = "0";
	public String entry_date = "";
	public String item_modified_id = "0";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String item_entry_by = "";
	public String item_modified_by = "";
	public String QueryString = "";
	public String item_model_id = "0";
	public String item_optiontype_id = "0";
	public String cat_id = "0";
	// / sales price
	public String salesrateclass_id = "0";
	public String item_sales_ledger_id = "1";
	public String item_salesdiscount_ledger_id = "2";
	public String item_salestax1_ledger_id = "0";
	public String item_salestax2_ledger_id = "0";
	public String item_salestax3_ledger_id = "0";
	public String item_salestax4_ledger_id = "0";
	public String item_salestax2_aftertax1 = "0", item_salestax3_aftertax2 = "0", item_salestax4_aftertax3 = "0";
	public String sales_price_amt = "";
	public String sales_price_disc = "";
	public String sales_price_disc_type = "0";
	// purchase price
	public String purchaserateclass_id = "0";
	public String item_purchase_ledger_id = "5";
	public String item_purchasediscount_ledger_id = "6";
	public String item_purchasetax1_ledger_id = "0";
	public String item_purchasetax2_ledger_id = "0";
	public String item_purchasetax3_ledger_id = "0";
	public String item_purchasetax4_ledger_id = "0";
	public String item_purchasetax2_aftertax1 = "0", item_purchasetax3_aftertax2 = "0", item_purchasetax4_aftertax3 = "0";
	public String purchase_price_amt = "";
	public String purchase_price_disc = "";
	public String purchase_price_disc_type = "0";

	public String comp_module_accounting = "0";

	public String price_variable = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				comp_module_accounting = CNumeric(GetSession("comp_module_accounting", request));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				item_cat_id = PadQuotes(request.getParameter("item_cat_id"));
				QueryString = PadQuotes(request.getQueryString());
				if (!item_cat_id.equals("")) {
					StrSql = "SELECT cat_name "
							+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
							+ " WHERE cat_id = " + CNumeric(item_cat_id);
					item_name = ExecuteQuery(StrSql);
					if (item_name.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Category!"));
					}
				}

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						item_name = "";
						item_active = "1";
					} else {
						CheckPerm(comp_id, "emp_item_add, emp_sales_item_add, emp_pos_item_add", request, response);
						GetValues(request, response);
						item_entry_id = emp_id;
						item_entry_date = ToLongDate(kknow());
						item_modified_date = "";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-list.jsp?item_id=" + item_id + "&msg=Item added successfully!"));
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Item")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Item")) {
						CheckPerm(comp_id, "emp_item_edit, emp_sales_item_edit, emp_pos_item_edit", request, response);
						GetValues(request, response);
						item_entry_id = emp_id;
						item_entry_date = ToLongDate(kknow());
						item_modified_id = emp_id;
						item_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-list.jsp?item_id=" + item_id + "&msg=Item updated successfully!" + msg + ""));
						}
					} else if (deleteB.equals("Delete Item")) {
						CheckPerm(comp_id, "emp_item_delete, emp_sales_item_delete, emp_pos_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-list.jsp?msg=Item deleted successfully!"));
						}
					}
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
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		item_code = PadQuotes(request.getParameter("txt_item_code"));
		item_service_code = PadQuotes(request.getParameter("txt_item_service_code"));
		item_hsn = PadQuotes(request.getParameter("txt_item_hsn"));
		item_sac = PadQuotes(request.getParameter("txt_item_sac"));
		item_cat_id = CNumeric(PadQuotes(request.getParameter("dr_item_cat_id")));
		item_optiontype_id = CNumeric(PadQuotes(request.getParameter("dr_optiontype_id")));
		item_model_id = CNumeric(PadQuotes(request.getParameter("drop_item_model_id")));
		item_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_item_fueltype_id")));
		item_url = PadQuotes(request.getParameter("txt_item_url"));
		item_small_desc = PadQuotes(request.getParameter("txt_item_small_desc"));
		item_big_desc = PadQuotes(request.getParameter("txt_item_big_desc"));
		item_notes = PadQuotes(request.getParameter("txt_item_notes"));
		item_aftertaxcal = CheckBoxValue(PadQuotes(request.getParameter("chk_item_aftertaxcal")));
		item_aftertaxcal_formulae = PadQuotes(request.getParameter("txt_item_aftertaxcal_formulae"));
		item_active = CheckBoxValue(PadQuotes(request.getParameter("chk_item_active")));
		item_uom_id = CNumeric(PadQuotes(request.getParameter("dr_item_uom_id")));
		item_alt_uom_id = PadQuotes(request.getParameter("dr_item_alt_uom_id")).split("-")[0];
		item_eoq = CNumeric(PadQuotes(request.getParameter("txt_item_eoq")));
		item_perishable = CheckBoxValue(PadQuotes(request.getParameter("chk_item_perishable")));
		item_nonstock = CheckBoxValue(PadQuotes(request.getParameter("chk_item_nonstock")));
		item_serial = CheckBoxValue(PadQuotes(request.getParameter("chk_item_serial")));
		item_type_id = CNumeric(PadQuotes(request.getParameter("dr_item_type_id")));
		if (add.equals("yes")) {
			item_loyaltycard_id = CNumeric(PadQuotes(request.getParameter("dr_item_loyaltycard_id")));
		} else {
			item_loyaltycard_id = loyaltycard_id;
		}

		item_durcount = CNumeric(PadQuotes(request.getParameter("txt_item_durcount")));
		item_durtype = CNumeric(PadQuotes(request.getParameter("dr_item_durtype")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		item_entry_by = unescapehtml(PadQuotes(request.getParameter("item_entry_by")));
		item_modified_by = PadQuotes(request.getParameter("item_modified_by"));

		// sales price
		salesrateclass_id = CNumeric(PadQuotes(request.getParameter("sales_rateclass_id")));

		sales_price_amt = CNumeric(PadQuotes(request.getParameter("txt_sales_price_amt")));
		item_sales_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_sales_ledger_id")));
		item_salesdiscount_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_salesdiscount_ledger_id")));
		item_salestax1_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_salestax1_ledger_id")));
		item_salestax2_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_salestax2_ledger_id")));
		item_salestax2_aftertax1 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_salestax2_aftertax1")));
		item_salestax3_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_salestax3_ledger_id")));
		item_salestax4_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_salestax4_ledger_id")));
		item_salestax3_aftertax2 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_salestax3_aftertax2")));
		item_salestax4_aftertax3 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_salestax4_aftertax3")));
		sales_price_disc = CNumeric(PadQuotes(request.getParameter("txt_sales_price_disc")));
		sales_price_disc_type = CNumeric(PadQuotes(request.getParameter("dr_sales_price_disc_type")));

		// purchase price
		purchaserateclass_id = CNumeric(PadQuotes(request.getParameter("purchase_rateclass_id")));

		purchase_price_amt = CNumeric(PadQuotes(request.getParameter("txt_purchase_price_amt")));
		item_purchase_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchase_ledger_id")));
		item_purchasediscount_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchasediscount_ledger_id")));
		item_purchasetax1_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchasetax1_ledger_id")));
		item_purchasetax2_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchasetax2_ledger_id")));
		item_purchasetax2_aftertax1 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_purchasetax2_aftertax1")));
		item_purchasetax3_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchasetax3_ledger_id")));
		item_purchasetax4_ledger_id = CNumeric(PadQuotes(request.getParameter("dr_item_purchasetax4_ledger_id")));
		item_purchasetax3_aftertax2 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_purchasetax3_aftertax2")));
		item_purchasetax4_aftertax3 = CheckBoxValue(PadQuotes(request.getParameter("chk_item_purchasetax4_aftertax3")));
		purchase_price_disc = CNumeric(PadQuotes(request.getParameter("txt_purchase_price_disc")));
		purchase_price_disc_type = CNumeric(PadQuotes(request.getParameter("dr_purchase_price_disc_type")));

	}

	protected void CheckForm() {
		msg = "";

		if (item_name.equals("")) {
			msg += "<br>Enter Name!";
		} else {
			StrSql = "SELECT item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_name = '" + item_name + "'";
			if (!item_code.equals("")) {
				StrSql += " AND item_code = '" + item_code + "'";
			}
			if (update.equals("yes")) {
				StrSql += " AND item_id != " + item_id + "";
			}

			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Item Name found!";
			}
		}

		if (!item_code.equals("")) {
			StrSql = "SELECT item_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_code = '" + item_code + "'";
			if (update.equals("yes")) {
				StrSql += " AND item_id != " + item_id + "";
			}
			if (!item_name.equals("")) {
				StrSql += " AND item_name = '" + item_name + "'";
			}
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Item Code found!";
			}
		}

		if (!item_service_code.equals("")) {
			StrSql = "SELECT item_service_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_service_code = '" + item_service_code + "'";
			if (update.equals("yes")) {
				StrSql += " AND item_id != " + item_id + "";
			}

			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Service Code found!";
			}
		}
		if (!item_sac.equals("") && !item_hsn.equals("")) {
			msg += "<br>Enter either HSN or SAC!";
		}
		if (item_cat_id.equals("0")) {
			msg += "<br>Select Category!";
		}

		if (item_type_id.equals("0")) {
			msg += "<br>Select Type!";
		}

		if (item_type_id.equals("1") && item_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}
		if (item_type_id.equals("1") && item_fueltype_id.equals("0")) {
			msg += "<br>Select Fuel Type!";
		}

		if (item_uom_id.equals("0")) {
			msg += "<br>Select Unit of Measurement!";
		}

		if (item_alt_uom_id.equals("0")) {
			msg += "<br>Select Alternative Unit of Measurement!";
		}

		if (status.equals("Add")) {

			if (!salesrateclass_id.equals("0")) {
				if (item_sales_ledger_id.equals("0")) {
					msg += "<br>Select Sales Ledger!";

				}
				if (item_salesdiscount_ledger_id.equals("0")) {
					msg += "<br>Select Sales Discount Ledger!";
				}
			}

			if (!item_salestax1_ledger_id.equals("0")
					&& !item_salestax2_ledger_id.equals("0")) {

				if (item_salestax2_ledger_id.equals(item_salestax1_ledger_id)
						|| item_salestax3_ledger_id.equals(item_salestax1_ledger_id)
						|| item_salestax3_ledger_id.equals(item_salestax2_ledger_id)) {
					msg += "<br>Similar Sales Tax Found!";
				}
			}

			if (!purchaserateclass_id.equals("0")) {
				if (item_purchase_ledger_id.equals("0")) {
					msg += "<br>Select Purchase Ledger!";

				}
				if (item_purchasediscount_ledger_id.equals("0")) {
					msg += "<br>Select Purchase Discount Ledger!";
				}
			}

			if (!item_purchasetax1_ledger_id.equals("0")
					&& !item_purchasetax2_ledger_id.equals("0")) {

				if (item_purchasetax2_ledger_id.equals(item_purchasetax1_ledger_id)
						|| item_purchasetax3_ledger_id.equals(item_purchasetax1_ledger_id)
						|| item_purchasetax3_ledger_id.equals(item_purchasetax2_ledger_id)) {
					msg += "<br>Similar Purchase Tax Found!";
				}
			}

		}
		if (item_type_id.equals("1")) {
			if (item_salestax1_ledger_id.equals("0")) {
				msg += "<br>Select Sales SGST!";
			}
			if (item_salestax2_ledger_id.equals("0")) {
				msg += "<br>Select Sales CGST!";
			}
			if (item_salestax3_ledger_id.equals("0")) {
				msg += "<br>Select Sales IGST!";
			}
			// if (item_salestax4_ledger_id.equals("0")) {
			// msg += "<br>Select Sales Cess!";
			// }

			if (item_purchasetax1_ledger_id.equals("0")) {
				msg += "<br>Select Purchase SGST!";
			}
			if (item_purchasetax2_ledger_id.equals("0")) {
				msg += "<br>Select Purchase CGST!";
			}
			if (item_purchasetax3_ledger_id.equals("0")) {
				msg += "<br>Select Purchase IGST!";
			}
			// if (item_purchasetax4_ledger_id.equals("0")) {
			// msg += "<br>Select Purchase Cess!";
			// }

		}
		if (updateB.equals("Update Item") && !deleteB.equals("Delete Item")) {
			if (item_nonstock.equals("1")) {
				String x = ExecuteQuery("SELECT vouchertrans_item_id"
						+ " FROM" + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN axela_acc_voucher ON voucher_id  = vouchertrans_voucher_id"
						+ " WHERE voucher_vouchertype_id = 12"
						+ " AND vouchertrans_item_id = " + item_id);
				if (!x.equals("")) {
					msg += "<br>PO Exists for this item, cannot be made non stock!";
				}
			}
		}
	}
	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// item_id =
				// ExecuteQuery("SELECT (COALESCE(MAX(item_id), 0) + 1) FROM " +
				// compdb(comp_id) + "axela_inventory_item AS item_id");
				if (item_nonstock.equals("1")) {
					item_eoq = "0";
				}
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
						+ " (item_name,"
						+ " item_code,"
						+ " item_service_code,"
						+ "	item_hsn,"
						+ "	item_sac,"
						+ " item_cat_id,"
						+ " item_type_id,"
						+ " item_optiontype_id,"
						+ " item_model_id,"
						+ " item_fueltype_id,"
						+ " item_small_desc,"
						+ " item_big_desc,"
						+ " item_url,"
						// + " item_loyaltycard_id,"
						// + " item_durtype,"
						// + " item_durcount,"
						+ " item_serial,"
						+ " item_uom_id,"
						+ " item_alt_uom_id,"
						+ " item_eoq,"
						+ " item_perishable,"
						+ " item_nonstock,"
						+ " item_aftertaxcal,"
						+ " item_aftertaxcal_formulae,"
						+ " item_sales_ledger_id,"
						+ " item_salesdiscount_ledger_id,"
						+ " item_salestax1_ledger_id,"
						+ " item_salestax2_ledger_id,"
						+ " item_salestax2_aftertax1,"
						+ " item_salestax3_ledger_id,"
						+ " item_salestax4_ledger_id,"
						+ " item_salestax3_aftertax2,"
						+ " item_salestax4_aftertax3,"
						+ " item_purchase_ledger_id,"
						+ " item_purchasediscount_ledger_id,"
						+ " item_purchasetax1_ledger_id,"
						+ " item_purchasetax2_ledger_id,"
						+ " item_purchasetax2_aftertax1,"
						+ " item_purchasetax3_ledger_id,"
						+ " item_purchasetax4_ledger_id,"
						+ " item_purchasetax3_aftertax2,"
						+ " item_purchasetax4_aftertax3,"
						+ " item_active,"
						+ " item_notes,"
						+ " item_entry_id,"
						+ " item_entry_date)"
						+ " VALUES"
						+ " ('" + item_name + "',"
						+ " '" + item_code + "',"
						+ " '" + item_service_code + "',"
						+ " '" + item_hsn + "',"
						+ " '" + item_sac + "',"
						+ " " + item_cat_id + ","
						+ " " + item_type_id + ","
						+ " " + item_optiontype_id + ","
						+ " " + item_model_id + ","
						+ " " + item_fueltype_id + ","
						+ " '" + item_small_desc + "',"
						+ " '" + item_big_desc + "',"
						+ " '" + item_url + "',"
						// + " '" + item_loyaltycard_id + "',"
						// + " '" + item_durtype + "',"
						// + " '" + item_durcount + "',"
						+ " '" + item_serial + "',"
						+ " " + item_uom_id + ","
						+ " " + item_alt_uom_id + ","
						+ " " + item_eoq + ","
						+ " '" + item_perishable + "',"
						+ " '" + item_nonstock + "',"
						+ " '" + item_aftertaxcal + "',"
						+ " '" + item_aftertaxcal_formulae + "',"
						+ " " + item_sales_ledger_id + ","
						+ " " + item_salesdiscount_ledger_id + ","
						+ " " + item_salestax1_ledger_id + ","
						+ " " + item_salestax2_ledger_id + ","
						+ " " + item_salestax2_aftertax1 + ","
						+ " " + item_salestax3_ledger_id + ","
						+ " " + item_salestax4_ledger_id + ","
						+ " " + item_salestax3_aftertax2 + ","
						+ " " + item_salestax4_aftertax3 + ","
						+ "	" + item_purchase_ledger_id + ","
						+ " " + item_purchasediscount_ledger_id + ","
						+ " " + item_purchasetax1_ledger_id + ","
						+ " " + item_purchasetax2_ledger_id + ","
						+ " " + item_purchasetax2_aftertax1 + ","
						+ " " + item_purchasetax3_ledger_id + ","
						+ " " + item_purchasetax4_ledger_id + ","
						+ " " + item_purchasetax3_aftertax2 + ","
						+ " " + item_purchasetax4_aftertax3 + ","
						+ " '" + item_active + "',"
						+ " '" + item_notes + "',"
						+ " '" + item_entry_id + "',"
						+ " '" + item_entry_date + "')";
				item_id = UpdateQueryReturnID(StrSql);
				// SOP("strsql=======item========" + StrSqlBreaker(StrSql));

				if (status.equals("Add")) {
					if (!salesrateclass_id.equals("0")
							&& !item_sales_ledger_id.equals("0")) {
						// addinjg sales price
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
								+ " ("
								+ " price_rateclass_id,"
								+ " price_item_id,"
								+ " price_amt,"
								+ " price_disc,"
								+ " price_disc_type,"
								+ " price_variable,"
								+ " price_effective_from,"
								+ " price_active,"
								+ " price_entry_id,"
								+ " price_entry_date)"
								+ " VALUES"
								+ " (" + " " + salesrateclass_id + ","
								+ " " + item_id + ","
								+ " " + sales_price_amt + ","
								+ " " + sales_price_disc + ","
								+ " '" + sales_price_disc_type + "',"
								+ " '" + price_variable + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '1',"
								+ " " + item_entry_id + ","
								+ " " + item_entry_date + ")";
						// SOP("strsql=======sales price========" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);

					}

					// adding purchase price
					if (!purchaserateclass_id.equals("0")
							&& !item_purchase_ledger_id.equals("0")) {
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
								+ " ("
								+ " price_rateclass_id,"
								+ " price_item_id,"
								+ " price_amt,"
								+ " price_disc,"
								+ " price_disc_type,"
								+ " price_variable,"
								+ " price_effective_from,"
								+ " price_active,"
								+ " price_entry_id,"
								+ " price_entry_date)"
								+ " VALUES"
								+ " (" + " " + purchaserateclass_id + ","
								+ " " + item_id + ","
								+ " " + purchase_price_amt + ","
								+ " " + purchase_price_disc + ","
								+ " '" + purchase_price_disc_type + "',"
								+ " '" + price_variable + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '1',"
								+ " " + item_entry_id + ","
								+ " " + item_entry_date + ")";
						SOP("strsql=======purchase price========" + StrSqlBreaker(StrSql));

						stmttx.addBatch(StrSql);
					}
				}
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock "
						+ "(" + " stock_location_id,"
						+ " stock_item_id" + " )"
						+ " (" + " SELECT"
						+ " location_id," + item_id + " FROM " + compdb(comp_id) + "axela_inventory_location)";
				stmttx.addBatch(StrSql);
				stmttx.executeBatch();
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axela Auto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + item_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_name = crs.getString("item_name");
					item_code = crs.getString("item_code");
					item_service_code = crs.getString("item_service_code");
					item_hsn = crs.getString("item_hsn");
					item_sac = crs.getString("item_sac");
					item_cat_id = crs.getString("item_cat_id");
					item_type_id = crs.getString("item_type_id");
					item_optiontype_id = crs.getString("item_optiontype_id");
					item_fueltype_id = crs.getString("item_fueltype_id");
					item_model_id = crs.getString("item_model_id");
					item_small_desc = crs.getString("item_small_desc");
					item_big_desc = crs.getString("item_big_desc");
					item_url = crs.getString("item_url");
					item_uom_id = crs.getString("item_uom_id");
					item_alt_uom_id = crs.getString("item_alt_uom_id");
					item_eoq = crs.getString("item_eoq");
					item_perishable = crs.getString("item_perishable");
					item_nonstock = crs.getString("item_nonstock");
					item_loyaltycard_id = crs.getString("item_loyaltycard_id");
					loyaltycard_id = crs.getString("item_loyaltycard_id");
					item_durtype = crs.getString("item_durtype");
					item_durcount = crs.getString("item_durcount");
					item_serial = crs.getString("item_serial");
					item_aftertaxcal = crs.getString("item_aftertaxcal");
					item_aftertaxcal_formulae = crs.getString("item_aftertaxcal_formulae");
					item_sales_ledger_id = crs.getString("item_sales_ledger_id");
					item_salesdiscount_ledger_id = crs.getString("item_salesdiscount_ledger_id");
					item_salestax1_ledger_id = crs.getString("item_salestax1_ledger_id");
					item_salestax2_ledger_id = crs.getString("item_salestax2_ledger_id");
					item_salestax2_aftertax1 = crs.getString("item_salestax2_aftertax1");
					item_salestax3_ledger_id = crs.getString("item_salestax3_ledger_id");
					item_salestax4_ledger_id = crs.getString("item_salestax4_ledger_id");
					item_salestax3_aftertax2 = crs.getString("item_salestax3_aftertax2");
					item_salestax4_aftertax3 = crs.getString("item_salestax4_aftertax3");
					item_purchase_ledger_id = crs.getString("item_purchase_ledger_id");
					item_purchasediscount_ledger_id = crs.getString("item_purchasediscount_ledger_id");
					item_purchasetax1_ledger_id = crs.getString("item_purchasetax1_ledger_id");
					item_purchasetax2_ledger_id = crs.getString("item_purchasetax2_ledger_id");
					item_purchasetax2_aftertax1 = crs.getString("item_purchasetax2_aftertax1");
					item_purchasetax3_ledger_id = crs.getString("item_purchasetax3_ledger_id");
					item_purchasetax4_ledger_id = crs.getString("item_purchasetax4_ledger_id");
					item_purchasetax3_aftertax2 = crs.getString("item_purchasetax3_aftertax2");
					item_purchasetax4_aftertax3 = crs.getString("item_purchasetax4_aftertax3");
					item_active = crs.getString("item_active");
					item_notes = crs.getString("item_notes");
					item_entry_id = crs.getString("item_entry_id");
					if (!item_entry_id.equals("")) {
						item_entry_by = Exename(comp_id, Integer.parseInt(item_entry_id));
					}
					entry_date = strToLongDate(crs.getString("item_entry_date"));

					item_modified_id = crs.getString("item_modified_id");
					if (!item_modified_id.equals("0")) {
						item_modified_by = Exename(comp_id, Integer.parseInt(item_modified_id));
						modified_date = strToLongDate(crs.getString("item_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			if (item_nonstock.equals("1")) {
				item_eoq = "0";
			}
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
					+ " SET"
					+ " item_name = '" + item_name + "',"
					+ " item_code = '" + item_code + "',"
					+ " item_service_code = '" + item_service_code + "',"
					+ " item_hsn = '" + item_hsn + "',"
					+ " item_sac = '" + item_sac + "',"
					+ " item_cat_id = " + item_cat_id + ","
					+ " item_model_id = " + item_model_id + ","
					+ " item_type_id = " + item_type_id + ","
					+ " item_fueltype_id = " + item_fueltype_id + ","
					+ " item_optiontype_id = " + item_optiontype_id + ","
					+ " item_small_desc = '" + item_small_desc + "',"
					+ " item_big_desc = '" + item_big_desc + "',"
					+ " item_url = '" + item_url + "',"
					+ " item_perishable = '" + item_perishable + "',"
					+ " item_nonstock = '" + item_nonstock + "',"
					+ " item_loyaltycard_id = '" + item_loyaltycard_id + "',"
					+ " item_durtype = '" + item_durtype + "',"
					+ " item_durcount = '" + item_durcount + "',"
					+ " item_serial = '" + item_serial + "',"
					+ " item_uom_id = '" + item_uom_id + "',"
					+ " item_alt_uom_id = " + item_alt_uom_id + ","
					+ " item_eoq = '" + item_eoq + "',"
					+ " item_aftertaxcal = '" + item_aftertaxcal + "',"
					+ " item_aftertaxcal_formulae = '" + item_aftertaxcal_formulae + "',"
					+ "	item_sales_ledger_id= " + item_sales_ledger_id + ","
					+ "	item_salesdiscount_ledger_id= " + item_salesdiscount_ledger_id + ","
					+ "	item_salestax1_ledger_id = " + item_salestax1_ledger_id + ","
					+ "	item_salestax2_ledger_id = " + item_salestax2_ledger_id + ","
					+ "	item_salestax2_aftertax1 = " + item_salestax2_aftertax1 + ","
					+ "	item_salestax3_ledger_id = " + item_salestax3_ledger_id + ","
					+ "	item_salestax4_ledger_id = " + item_salestax4_ledger_id + ","
					+ "	item_salestax3_aftertax2 = " + item_salestax3_aftertax2 + ","
					+ "	item_salestax4_aftertax3 = " + item_salestax4_aftertax3 + ","
					+ " item_purchase_ledger_id = " + item_purchase_ledger_id + ","
					+ " item_purchasediscount_ledger_id = " + item_purchasediscount_ledger_id + ","
					+ "	item_purchasetax1_ledger_id = " + item_purchasetax1_ledger_id + ","
					+ "	item_purchasetax2_ledger_id = " + item_purchasetax2_ledger_id + ","
					+ "	item_purchasetax2_aftertax1 = " + item_purchasetax2_aftertax1 + ","
					+ "	item_purchasetax3_ledger_id = " + item_purchasetax3_ledger_id + ","
					+ "	item_purchasetax4_ledger_id = " + item_purchasetax4_ledger_id + ","
					+ "	item_purchasetax3_aftertax2 = " + item_purchasetax3_aftertax2 + ","
					+ "	item_purchasetax4_aftertax3 = " + item_purchasetax4_aftertax3 + ","
					+ " item_active = '" + item_active + "',"
					+ " item_notes = '" + item_notes + "',"
					+ " item_modified_id = '" + item_modified_id + "',"
					+ " item_modified_date = '" + item_modified_date + "'"
					+ " WHERE item_id = " + item_id + "";
			updateQuery(StrSql);
		}
	}
	protected void DeleteFields() {

		// Enquiry Association
		StrSql = "SELECT enquiry_item_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Enquiry!";
		}

		// sales Association
		StrSql = "SELECT quoteitem_item_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
				+ " WHERE quoteitem_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Quote!";
		}

		StrSql = "SELECT soitem_item_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
				+ " WHERE soitem_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Sales Order!";
		}

		// Voucher association
		StrSql = "SELECT vouchertrans_voucher_id"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE vouchertrans_item_id = " + item_id
				+ " LIMIT 1";

		if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
			msg += "<br>Item is associated with Voucher!";
		}

		// Stock Option association
		StrSql = "SELECT option_item_id"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
				+ " WHERE option_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Item Option!";
		}

		// association with Test Drive
		StrSql = "SELECT veh_item_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
				+ " WHERE veh_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Vehicle!";
		}

		StrSql = "SELECT COALESCE(option_item_id, '')"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
				+ " WHERE option_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is configured for an Item!";
		}
		// jobcard association
		StrSql = "SELECT jctrans_item_id"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
				+ " WHERE jctrans_item_id = " + item_id + ""
				+ " AND jctrans_option_id = 0 ";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Job Card!";
		}

		// Delete records
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_item_id = " + item_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_stock"
					+ " WHERE stock_item_id = " + item_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + item_id + "";
			updateQuery(StrSql);
		}
	}
	public String PopulateUOM(String comp_id, String item_uom_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT uom_id, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE uom_parent_id = 0"
					+ " ORDER BY uom_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("uom_id"));
				Str.append(StrSelectdrop(crs.getString("uom_id"), item_uom_id));
				Str.append(">").append(crs.getString("uom_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateAltUOM(String comp_id, String item_uom_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT  uom_id, uom_ratio, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE 1=1"
					+ " AND (uom_id = " + item_uom_id
					+ " OR uom_parent_id = " + item_uom_id + ")"
					// + " AND uom_active = 1"
					+ " GROUP BY CONCAT(uom_name,uom_ratio)"
					+ " ORDER BY uom_name, uom_ratio";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_alt_uom_id\" id=\"dr_item_alt_uom_id\" class=\"form-control\">");
			Str.append("<option value=\"0\"> Select  </option>\n");
			if (crs.isBeforeFirst()) {
				if (!item_uom_id.equals("0")) {
					crs.first();
					item_alt_uom_id = crs.getString("uom_id");
					crs.beforeFirst();
				}
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("uom_id")).append("-").append(crs.getString("uom_ratio"));
					Str.append(StrSelectdrop(crs.getString("uom_id"), item_alt_uom_id)).append(">");
					Str.append(crs.getString("uom_name")).append(" X ").append(crs.getString("uom_ratio"));
					Str.append("</option>\n");
				}
				Str.append("</select>");
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCat(String comp_id, String item_cat_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cat_id, cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat"
					+ " ORDER BY cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), item_cat_id));
				Str.append(">").append(crs.getString("cat_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String comp_id, String item_model_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), item_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItemType(String comp_id, String item_type_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT type_id, type_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_type"
					+ " ORDER BY type_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("type_id")).append("");
				Str.append(StrSelectdrop(crs.getString("type_id"), item_type_id));
				Str.append(">").append(crs.getString("type_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String comp_id, String item_fueltype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), item_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateOptions(String comp_id, String item_optiontype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT optiontype_id, optiontype_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_optiontype"
					+ " ORDER BY optiontype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("optiontype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("optiontype_id"), item_optiontype_id));
				Str.append(">").append(crs.getString("optiontype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDurtype(String item_durtype) {
		StringBuilder stringval = new StringBuilder();
		try {
			stringval.append("<option value=0> Select </option>\n");
			stringval.append("<option value=1 ").append(StrSelectdrop("1", item_durtype)).append(">");
			stringval.append(ReturnDurType(1)).append("</option>\n");
			stringval.append("<option value=2 ").append(StrSelectdrop("2", item_durtype)).append(">");
			stringval.append(ReturnDurType(2)).append("</option>\n");
			stringval.append("<option value=3 ").append(StrSelectdrop("3", item_durtype)).append(">");
			stringval.append(ReturnDurType(3)).append("</option>\n");
			stringval.append("<option value=4 ").append(StrSelectdrop("4", item_durtype)).append(">");
			stringval.append(ReturnDurType(4)).append("</option>\n");
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesBranchClass(String comp_id,
			String salesrateclass_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT rateclass_id, rateclass_name"
				+ " FROM " + compdb(comp_id) + "axela_rate_class"
				+ " WHERE rateclass_type = 1"
				+ " GROUP BY rateclass_id"
				+ " ORDER BY rateclass_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option");
				Str.append(" value=" + crs.getString("rateclass_id") + " " + StrSelectdrop(crs.getString("rateclass_id"), salesrateclass_id) + ">");
				Str.append(crs.getString("rateclass_name"));
				Str.append("</option>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePurchaseBranchClass(String comp_id,
			String purchaserateclass_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT rateclass_id, rateclass_name"
				+ " FROM " + compdb(comp_id) + "axela_rate_class"
				+ " WHERE rateclass_type = 2"
				+ " GROUP BY rateclass_id"
				+ " ORDER BY rateclass_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option");
				Str.append(" value=" + crs.getString("rateclass_id") + " " + StrSelectdrop(crs.getString("rateclass_id"), purchaserateclass_id) + ">");
				Str.append(crs.getString("rateclass_name"));
				Str.append("</option>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesLedgers(String comp_id,
			String sales_price_sales_customer_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE accgroup_alie = 3"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), sales_price_sales_customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
			SOP(Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
	public String PopulateSalesDiscountLedgers(String comp_id,
			String sales_price_discount_customer_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE accgroup_alie = 4"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), sales_price_discount_customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// purchase
	public String PopulatePurchaseLedgers(String comp_id,
			String purchase_price_sales_customer_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE accgroup_alie = 4"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), purchase_price_sales_customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulatePurchaseDiscountLedgers(String comp_id,
			String purchase_price_discount_customer_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE accgroup_alie = 3"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), purchase_price_discount_customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// purchase tax
	public String PopulatePurchaseTax(String comp_id, String price_tax_id, String tax_type) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE customer_taxtype_id = " + tax_type
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesTax(String comp_id, String price_tax_id, String tax_type) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE customer_taxtype_id = " + tax_type
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesDiscountType(String comp_id,
			String sales_price_disc_type) {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<option value=1").append(StrSelectdrop("1", sales_price_disc_type)).append(">").append("Amount").append("</option>\n");
			Str.append("<option value=2").append(StrSelectdrop("2", sales_price_disc_type)).append(">").append("Percentage").append("</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePurchaseDiscountType(String comp_id,
			String sales_price_disc_type) {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<option value=1").append(StrSelectdrop("1", sales_price_disc_type)).append(">").append("Amount").append("</option>\n");
			Str.append("<option value=2").append(StrSelectdrop("2", sales_price_disc_type)).append(">").append("Percentage").append("</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
