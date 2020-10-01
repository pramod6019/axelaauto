package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ledger_Update extends Connect {
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public int mapkey = 0;
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	// Ledger varialble
	public String customer_id = "0";
	public String customer_accgroup_id = "0";
	public String customer_open_bal = "";
	public String customer_type = "0", customer_ledgertype = "0";
	public String customer_reconciliation = "";
	public String customer_name = "";
	public String customer_alias = "";
	public String customer_code = "";
	public String customer_mobile1 = "";
	public String customer_mobile2 = "";
	public String customer_phone1 = "";
	public String customer_phone2 = "";
	public String customer_email1 = "";
	public String customer_email2 = "";
	public String customer_cst_no = "";
	public String customer_service_tax_no = "";
	public String customer_paydays = "";
	public String customer_credit_limit = "";
	public String customer_pan_no = "";
	public String customer_curr_bal = "";
	public String customer_active = "0";
	public String customer_notes = "";
	// public String customer_tin_no = "";
	public String customer_group_name = "";
	public String customer_entry_id = "0";
	public String customer_entry_date = "";
	public String customer_modified_id = "0";
	public String customer_modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request)) + "";
			CheckPerm(comp_id, "emp_acc_ledger_access, emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!addB.equals("yes")) {
						customer_mobile1 = "91-";
					}
					if ("yes".equals(addB)) {
						if (ReturnPerm(comp_id, "emp_acc_ledger_add, emp_acc_voucher_add", request).equals("1")) {
							GetValues(request, response);
							customer_entry_id = emp_id;
							customer_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ledger-list.jsp?customer_id=" + customer_id + "&msg=Ledger added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Ledger".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Ledger".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_acc_ledger_edit,emp_acc_voucher_edit", request).equals("1")) {
							GetValues(request, response);
							customer_modified_id = emp_id;
							customer_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ledger-list.jsp?customer_id=" + customer_id + "&msg=Ledger updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Ledger".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_acc_ledger_delete, emp_acc_voucher_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ledger-list.jsp?msg=Ledger deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		customer_alias = PadQuotes(request.getParameter("txt_customer_alias"));
		customer_code = PadQuotes(request.getParameter("txt_customer_ledger_code"));
		customer_mobile1 = PadQuotes(request.getParameter("txt_customer_mobile1"));
		customer_mobile2 = PadQuotes(request.getParameter("txt_customer_mobile2"));
		customer_phone1 = PadQuotes(request.getParameter("txt_customer_phone1"));
		customer_phone2 = PadQuotes(request.getParameter("txt_customer_phone2"));
		customer_email1 = PadQuotes(request.getParameter("txt_customer_email1"));
		customer_email2 = PadQuotes(request.getParameter("txt_customer_email2"));
		customer_cst_no = PadQuotes(request.getParameter("txt_customer_cst_no"));
		customer_service_tax_no = PadQuotes(request.getParameter("txt_customer_service_tax_no"));
		customer_paydays = CNumeric(PadQuotes(request.getParameter("txt_paymentdays")));
		customer_credit_limit = CNumeric(PadQuotes(request.getParameter("txt_customer_credit_limit")));;
		customer_pan_no = PadQuotes(request.getParameter("txt_customer_pan_no"));
		// customer_tin_no = PadQuotes(request.getParameter("txt_customer_tin_no"));
		customer_accgroup_id = PadQuotes(request.getParameter("dr_customer_fingroup_id"));
		customer_group_name = PadQuotes(request.getParameter("txt_subgroup_name"));
		if (customer_accgroup_id.equals("32")) {
			customer_type = "1";
		} else if (customer_accgroup_id.equals("31")) {
			customer_type = "2";
		} else {
			customer_type = "0";
		}
		customer_open_bal = CNumeric(PadQuotes(request.getParameter("txt_customer_open_bal")));
		customer_ledgertype = CNumeric(PadQuotes(request.getParameter("dr_customer_ledgertype")));
		customer_reconciliation = CheckBoxValue(PadQuotes(request.getParameter("chk_customer_reconciliation")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (customer_name.equals("")) {
			msg = "<br>Enter Name!";
		}
		if (!customer_alias.equals("")) {
			StrSql = "SELECT customer_alias FROM " + compdb(comp_id) + "axela_customer WHERE customer_alias = '" + customer_alias + "'";
			if (update.equals("yes")) {
				StrSql += " AND customer_id != " + customer_id;
			}
			// SOP("StrSql=====" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Alias found!";
			}

		}

		if (customer_accgroup_id.equals("0")) {
			msg += "<br>Select Group!";
		}
		if (customer_mobile1.equals("91-")) {
			customer_mobile1 = "";
		}

		if (!customer_mobile1.equals("")) {
			if (!IsValidMobileNo11(customer_mobile1)) {
				msg += "Enter valid Mobile1!";
			}

		}
		if (!customer_mobile2.equals("")) {
			if (!IsValidMobileNo11(customer_mobile2)) {
				msg += "Enter valid Mobile2!";
			}

		}

		if (!customer_phone1.equals("")) {
			if (!IsValidPhoneNo11(customer_phone1)) {
				msg += "Enter valid Phone1!";
			}

		}
		if (!customer_phone2.equals("")) {
			if (!IsValidPhoneNo11(customer_phone2)) {
				msg += "Enter valid Phone2!";
			}

		}

		if (!customer_email1.equals("")) {
			if (!IsValidEmail(customer_email1)) {
				msg += "Enter valid Email1!";
			}

		}
		if (!customer_email2.equals("")) {
			if (!IsValidEmail(customer_email2)) {
				msg += "Enter valid Email2!";
			}

		}

	}
	protected void AddFields() {// customer_name
		CheckForm();
		if (msg.equals("")) {

			customer_id = ExecuteQuery("SELECT MAX(customer_id)+1 FROM " + compdb(comp_id) + "axela_customer WHERE customer_id < 2001");

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ "(" + " customer_id,"
					+ " customer_name,"
					+ " customer_alias,"
					+ " customer_code,"
					+ " customer_mobile1,"
					+ " customer_mobile2,"
					+ " customer_phone1,"
					+ " customer_phone2,"
					+ " customer_email1,"
					+ " customer_email2,"
					+ " customer_pan_no,"
					+ " customer_open_bal,"
					+ " customer_accgroup_id,"
					+ " customer_ledgertype,"
					+ " customer_type,"
					+ " customer_credit_limit,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_reconciliation,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " VALUES"
					+ " (" + customer_id + ","
					+ " '" + customer_name + "',"
					+ " '" + customer_alias + "',"
					+ " '" + customer_code + "',"
					+ " '" + customer_mobile1 + "',"
					+ " '" + customer_mobile2 + "',"
					+ " '" + customer_phone1 + "',"
					+ " '" + customer_phone2 + "',"
					+ " '" + customer_email1 + "',"
					+ " '" + customer_email2 + "',"
					+ " '" + customer_pan_no + "',"
					+ " " + customer_open_bal + ","
					+ " " + customer_accgroup_id + ","
					+ " " + customer_ledgertype + ","
					+ " " + customer_type + ","
					+ " " + customer_credit_limit + ","
					+ " " + '1' + ","
					+ " '" + customer_notes + "',"
					+ " '" + customer_reconciliation + "',"
					+ " " + customer_entry_id + ","
					+ " '" + customer_entry_date + "')";
			// SOP("StrSql----add--" + StrSql);
			updateQuery(StrSql);

		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {

			StrSql = "SELECT ledger.*, COALESCE (accgrouppop_name, '') AS customer_subgroup_name,"
					+ " ( SELECT COUNT(vouchertrans_id) FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE 1 = 1"
					+ " AND vouchertrans_customer_id = ledger.customer_id ) AS customer_trans,"
					+ " customer_open_bal"
					+ " FROM " + compdb(comp_id) + "axela_customer ledger"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
					+ " WHERE ledger.customer_id = " + customer_id;
			// SOP("PopulateFields=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					customer_name = crs.getString("customer_name");
					customer_alias = crs.getString("customer_alias");
					customer_accgroup_id = crs.getString("customer_accgroup_id");
					customer_group_name = crs.getString("customer_subgroup_name");
					customer_type = crs.getString("customer_type");
					customer_ledgertype = crs.getString("customer_ledgertype");
					customer_open_bal = crs.getString("customer_open_bal");
					customer_code = crs.getString("customer_code");
					customer_mobile1 = crs.getString("customer_mobile1");
					customer_mobile2 = crs.getString("customer_mobile2");
					customer_phone1 = crs.getString("customer_phone1");
					customer_phone2 = crs.getString("customer_phone2");
					customer_email1 = crs.getString("customer_email1");
					customer_email2 = crs.getString("customer_email2");
					customer_credit_limit = crs.getString("customer_credit_limit");
					customer_pan_no = crs.getString("customer_pan_no");
					customer_entry_id = crs.getString("customer_entry_id");
					customer_reconciliation = crs.getString("customer_reconciliation");
					if (!customer_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(customer_entry_id));
						entry_date = strToLongDate(crs.getString("customer_entry_date"));
					}
					customer_modified_id = crs.getString("customer_modified_id");
					if (!customer_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(customer_modified_id));
						modified_date = strToLongDate(crs.getString("customer_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ledger!"));
			}
			crs.close();
			mapkey = 0;
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE  " + compdb(comp_id) + " axela_customer"
					+ " SET"
					+ " customer_name = '" + customer_name + "',"
					+ " customer_alias = '" + customer_alias + "',"
					+ " customer_code= '" + customer_code + "',"
					+ " customer_open_bal = " + customer_open_bal + ","
					+ " customer_type = " + customer_type + ","
					+ " customer_ledgertype = " + customer_ledgertype + ","
					+ " customer_mobile1 = '" + customer_mobile1 + "',"
					+ " customer_mobile2 = '" + customer_mobile2 + "',"
					+ " customer_phone1 = '" + customer_phone1 + "',"
					+ " customer_phone2 = '" + customer_phone2 + "',"
					+ " customer_email1 = '" + customer_email1 + "',"
					+ " customer_email2 = '" + customer_email2 + "',"
					+ " customer_pan_no = '" + customer_pan_no + "',"
					+ " customer_credit_limit = '" + customer_credit_limit + "',"
					+ " customer_reconciliation = '" + customer_reconciliation + "',"
					+ " customer_modified_id = " + customer_modified_id + ","
					+ " customer_modified_date = '" + customer_modified_date
					+ "'" + " where customer_id = " + customer_id + "";
			// SOP("customer_accgroup_id===" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		msg = "";
		StrSql = "SELECT voucher_customer_id" + " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_customer_id = " + customer_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Ledger is associated with Voucher!";
		}

		StrSql = "SELECT vouchertrans_customer_id"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE vouchertrans_customer_id = " + customer_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Ledger is associated with Voucher Transaction!";
		}

		StrSql = "SELECT price_sales_customer_id"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
				+ " WHERE price_sales_customer_id = " + customer_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Ledger is associated with Sales Item!";
		}

		StrSql = "SELECT price_discount1_customer_id"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
				+ " WHERE price_discount1_customer_id = " + customer_id;

		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Ledger is associated with Sales Item Discount!";
		}
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer" + " WHERE customer_id = "
					+ customer_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateGroup() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		StrSql = "SELECT accgrouppop_id, accgrouppop_name"
				+ " FROM " + compdb(comp_id) + "axela_acc_group_pop"
				+ " WHERE"
				+ " accgrouppop_id NOT IN ("
				+ " SELECT accgroup_parent_id FROM " + compdb(comp_id) + "axela_acc_group )"
				+ " GROUP BY accgrouppop_id"
				+ " ORDER BY accgrouppop_name";
		// SOP("StrSql====" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("accgrouppop_id"));
				Str.append(StrSelectdrop(crs.getString("accgrouppop_id"), customer_accgroup_id));
				Str.append(">").append(crs.getString("accgrouppop_name"));
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

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", customer_ledgertype)).append(">Cash</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", customer_ledgertype)).append(">Bank</option>\n");
		return Str.toString();
	}

}
