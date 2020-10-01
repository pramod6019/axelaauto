package axela.accounting;
//Divya 3rd Oct

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTax_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_round = "";
	public String customer_rate = "";
	public String customer_taxcat_id = "0";
	public String customer_taxtype_id = "0";
	public String customer_active = "";
	public String customer_entry_id = "0";
	public String customer_entry_date = "";
	public String customer_modified_id = "0";
	public String customer_modified_date = "";
	public String BranchAccess = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String accgroup_id = "0";
	public String itemgroup_id = "0";
	public String subgroup_id = "0";
	public String QueryString = "";
	public String StrHTML = "";
	public String group = "";

	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = GetSession("comp_id", request);
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request).toString();
				emp_id = GetSession("emp_id", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				itemgroup_id = CNumeric(PadQuotes(request.getParameter("itemgroup_id")));
				subgroup_id = CNumeric(PadQuotes(request.getParameter("subgroup_id")));
				group = PadQuotes(request.getParameter("group"));
				if (group.equals("yes")) {
					customer_taxtype_id = CNumeric(PadQuotes(request.getParameter("taxtype_id")));
					StrHTML = PopulateGroup(comp_id, "0");
				}

				PopulateConfigDetails();

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						customer_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_tax_add", request).equals("1")) {
							customer_entry_id = emp_id;
							customer_entry_date = ToShortDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Tax added successfully!";
								response.sendRedirect(response.encodeRedirectURL("managetax.jsp?customer_id=" + customer_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Tax".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Tax".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_tax_edit", request).equals("1")) {
							customer_modified_id = emp_id;
							customer_modified_date = ToShortDate(kknow());
							UpdateFields(request);
							// UpdateCurrentValue( customer_id);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Tax updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("managetax.jsp?customer_id=" + customer_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Tax".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_tax_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Tax deleted successfully!";
								response.sendRedirect(response.encodeRedirectURL("managetax.jsp?msg=" + msg));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		customer_rate = PadQuotes(request.getParameter("txt_customer_rate"));
		customer_round = PadQuotes(request.getParameter("dr_customer_round_id"));
		customer_taxcat_id = CNumeric(PadQuotes(request.getParameter("dr_customer_taxcat_id")));
		customer_taxtype_id = CNumeric(PadQuotes(request.getParameter("dr_customer_taxtype_id")));
		accgroup_id = CNumeric(PadQuotes(request.getParameter("dr_accgroup_id")));
		customer_active = CheckBoxValue(PadQuotes(request.getParameter("chk_customer_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

		// customer_accgroup_id = ExecuteQuery("SELECT customer_accgroup_id FROM  axela_customer"
		// + " WHERE customer_id = "+tax_customer_id);
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";

		if (customer_name.equals("")) {
			msg += "<br>Enter Tax Name!";
		}

		if (customer_rate.equals("")) {
			msg += "<br>Enter Tax Rate!";
		}

		if (customer_taxcat_id.equals("0")) {
			msg += "<br>Select Category!";
		}

		if (customer_round.equals("0")) {
			msg += "<br>Select Round!";
		}

		if (customer_taxtype_id.equals("0")) {
			msg += "<br>Select Type!";
		}
		if (accgroup_id.equals("0")) {
			msg += "<br>Select Ledger Group!";
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				customer_id = ExecuteQuery("SELECT MAX(customer_id) + 1 FROM " + compdb(comp_id) + "axela_customer WHERE customer_id < 2001");
				customer_id = "" + CheckCurrentId(Integer.parseInt(customer_id));
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
						+ " (customer_id,"
						+ " customer_name,"
						+ " customer_accgroup_id,"
						+ " customer_tax,"
						+ " customer_rate,"
						+ " customer_round,"
						+ " customer_taxcat_id,"
						+ " customer_taxtype_id,"
						+ " customer_active,"
						+ " customer_notes,"
						+ " customer_entry_id,"
						+ " customer_entry_date)"
						+ " VALUES"
						+ " (" + customer_id + ","
						+ " '" + customer_name + "',"
						+ " '" + accgroup_id + "',"
						+ " 1,"
						+ " '" + customer_rate + "',"
						+ " '" + customer_round + "',"
						+ " " + customer_taxcat_id + ","
						+ " " + customer_taxtype_id + ","
						+ " '" + customer_active + "',"
						+ " '',"
						+ " " + customer_entry_id + ","
						+ " " + ToLongDate(kknow()) + ")";
				// SOP("StrSql===" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *, customer_accgroup_id"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					// + " left JOIN  " + compdb(comp_id) + "axela_customer on customer_id = tax_customer_id"
					+ " WHERE customer_id = " + customer_id
					+ " AND customer_tax = 1";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_name = crs.getString("customer_name");
					accgroup_id = crs.getString("customer_accgroup_id");
					customer_round = crs.getString("customer_round");
					customer_rate = crs.getString("customer_rate");
					customer_taxcat_id = crs.getString("customer_taxcat_id");
					customer_taxtype_id = crs.getString("customer_taxtype_id");
					customer_active = crs.getString("customer_active");
					customer_entry_id = crs.getString("customer_entry_id");
					customer_entry_date = crs.getString("customer_entry_date");
					customer_modified_id = crs.getString("customer_modified_id");
					if (!crs.getString("customer_entry_id").equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("customer_entry_id"));
						entry_date = strToLongDate(crs.getString("customer_entry_date"));
					}
					if (!crs.getString("customer_modified_id").equals("0")) {
						modified_by = Exename(comp_id, crs.getInt("customer_modified_id"));
						modified_date = strToLongDate(crs.getString("customer_modified_date"));
					}
				}
			} else {
				msg = "msg=Invalid Tax!";
				response.sendRedirect("../portal/error.jsp?" + msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_customer"
						+ " SET"
						+ " customer_id = " + customer_id + ","
						+ " customer_accgroup_id = " + accgroup_id + ","
						+ " customer_name = '" + customer_name + "',"
						+ " customer_round = '" + customer_round + "',"
						+ " customer_rate = '" + customer_rate + "',"
						+ " customer_taxcat_id = " + customer_taxcat_id + ","
						+ " customer_taxtype_id = " + customer_taxtype_id + ","
						+ " customer_active = '" + customer_active + "',"
						+ " customer_modified_id = " + customer_modified_id + ","
						+ " customer_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE customer_id = " + customer_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "SELECT price_id FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE 1=1"
					+ " AND("
					+ "(price_tax_id = " + customer_id + ")"
					+ " OR price_tax1_id = " + customer_id + ""
					+ " OR price_tax2_id = " + customer_id + ""
					+ " OR price_tax3_id = " + customer_id + ""
					+ ")";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Tax is associated with Price!";
			}
			if (msg.equals("")) {

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_customer"
						+ " WHERE customer_id = " + customer_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTaxCategory() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>\n");
		try {
			StrSql = "SELECT taxcat_id, taxcat_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_tax_cat"
					+ " ORDER BY taxcat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("taxcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("taxcat_id"), customer_taxcat_id));
				Str.append(">").append(crs.getString("taxcat_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTaxType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>\n");
		try {
			StrSql = "SELECT taxtype_id, taxtype_name"
					+ " FROM axela_acc_tax_type"
					+ " ORDER BY taxtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("taxtype_id"));
				Str.append(StrSelectdrop(crs.getString("taxtype_id"), customer_taxtype_id));
				Str.append(">").append(crs.getString("taxtype_name")).append("</option>\n");
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
		StrSql = "SELECT emp_role_id FROM  " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		emp_role_id = ExecuteQuery(StrSql);
	}

	public String PopulateRounding() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", customer_round)).append(">Normal Rounding</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", customer_round)).append(">Upward Rounding</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", customer_round)).append(">Downward Rounding</option>\n");
		return Str.toString();
	}

	public String PopulateLedger(String subgroup_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>\n");
		try {
			StrSql = "SELECT customer_id, customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_accgroup_id IN (15, 26)"
					+ " group BY customer_id"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateGroup(String comp_id, String accgroup_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT accgrouppop_id, accgrouppop_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_group_pop"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE 1 = 1";
			if (customer_taxtype_id.equals("1")) {
				StrSql += " AND accgroup_alie = 1";
			} else {
				StrSql += " AND accgroup_alie = 2";
			}

			StrSql += " AND accgroup_id NOT IN ( SELECT accgroup_parent_id FROM " + compdb(comp_id) + "axela_acc_group )"
					+ " ORDER BY accgrouppop_name";
			// SOP("StrSql===" + StrSql);
			crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("accgrouppop_id"));
				Str.append(StrSelectdrop(crs.getString("accgrouppop_id"), accgroup_id));
				Str.append(">").append(crs.getString("accgrouppop_name")).append("</option>\n");
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