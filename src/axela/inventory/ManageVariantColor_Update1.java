package axela.inventory;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageVariantColor_Update1 extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public String StrSql1 = "";
	public static String msg = "";
	public String variantcolour_id = "0";
	public String variantcolour_name = "";
	public String variantcolour_code = "";
	public String QueryString = "";
	public String variantcolour_entry_id = "0";
	public String variantcolour_entry_date = "";
	public String variantcolour_modified_id = "0";
	public String variantcolour_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				variantcolour_id = CNumeric(PadQuotes(request.getParameter("variantcolour_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						variantcolour_name = "";
					} else {
						GetValues(request, response);
						variantcolour_entry_id = emp_id;
						variantcolour_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("../inventory/managevariantcolor.jsp?variantcolour_id=" + variantcolour_id + "&msg=Variant Color Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Variant Color".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Variant Color".equals(deleteB)) {
						GetValues(request, response);
						variantcolour_modified_id = emp_id;
						variantcolour_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("../inventory/managevariantcolor.jsp?variantcolour_id=" + variantcolour_id + "&msg=Variant Color Updated Successfully!"
									+ msg + ""));
						}
					} else if ("Delete Variant Color".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managevariantcolor.jsp?msg=Variant Color Deleted Successfully!"));
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
		variantcolour_name = PadQuotes(request.getParameter("txt_variantcolour_name"));
		variantcolour_code = PadQuotes(request.getParameter("txt_variantcolour_code"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		if (variantcolour_name.equals("")) {
			msg = msg + "<br>Enter Variant Color!";
		}

		try {
			CachedRowSet crs = null;
			if (!variantcolour_name.equals("")) {
				StrSql = "Select variantcolour_name from " + compdb(comp_id) + "axela_inventory_variantcolour where variantcolour_name = '" + variantcolour_name + "'";
				if (update.equals("yes")) {
					StrSql += " and variantcolour_id != " + variantcolour_id + "";
				}
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Variant Color Found!";
				}
				crs.close();
			}
			if (!variantcolour_code.equals("")) {
				StrSql1 = "Select variantcolour_code from " + compdb(comp_id) + "axela_inventory_variantcolour where variantcolour_code = '" + variantcolour_code + "'";
				if (update.equals("yes")) {
					StrSql1 += " and variantcolour_id != " + variantcolour_id + "";
				}
				crs = processQuery(StrSql1, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Variant Code Found!";
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				variantcolour_id = ExecuteQuery("Select max(variantcolour_id) as ID from " + compdb(comp_id) + "axela_inventory_variantcolour");
				if (variantcolour_id == null || variantcolour_id.equals("")) {
					variantcolour_id = "0";
				}
				int variantcolour_idi = Integer.parseInt(variantcolour_id) + 1;
				variantcolour_id = "" + variantcolour_idi;
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_variantcolour"
						+ " (variantcolour_id,"
						+ " variantcolour_name,"
						+ " variantcolour_code,"
						+ " variantcolour_entry_id,"
						+ " variantcolour_entry_date)"
						+ " VALUES"
						+ " ('" + variantcolour_id + "',"
						+ " '" + variantcolour_name + "',"
						+ " '" + variantcolour_code + "',"
						+ " " + variantcolour_entry_id + ","
						+ " '" + variantcolour_entry_date + "')";
				// SOP("SqlStr--------"+SqlStr);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_inventory_variantcolour WHERE variantcolour_id = " + variantcolour_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					variantcolour_id = crs.getString("variantcolour_id");
					variantcolour_name = crs.getString("variantcolour_name");
					variantcolour_code = crs.getString("variantcolour_code");
					variantcolour_entry_id = crs.getString("variantcolour_entry_id");
					if (!variantcolour_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(variantcolour_entry_id));
						entry_date = strToLongDate(crs.getString("variantcolour_entry_date"));
					}
					variantcolour_modified_id = crs.getString("variantcolour_modified_id");
					if (!variantcolour_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(variantcolour_modified_id));
						modified_date = strToLongDate(crs.getString("variantcolour_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item Variant Color!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_variantcolour"
					+ " SET"
					+ " variantcolour_name = '" + variantcolour_name + "',"
					+ " variantcolour_code = '" + variantcolour_code + "',"
					+ " variantcolour_modified_id = " + variantcolour_modified_id + ","
					+ " variantcolour_modified_date = '" + variantcolour_modified_date + "'"
					+ " where variantcolour_id = " + variantcolour_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_variantcolour_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_variantcolour_id = " + variantcolour_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Variant Color is associated with a Enquiry!";
		}

		// StrSql = "SELECT item_variantcolour_id FROM " + compdb(comp_id) + "axela_inventory_item where item_variantcolour_id = " + variantcolour_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Variant Color is associated with a Item(s)!";
		// }
		// if (variantcolour_id.equals("1")) {
		// msg = msg + "<br>Cannot Delete First Record!";
		// }
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_variantcolour"
					+ " WHERE variantcolour_id = " + variantcolour_id + "";
			updateQuery(StrSql);
		}
	}
}
