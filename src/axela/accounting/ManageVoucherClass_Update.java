package axela.accounting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageVoucherClass_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String voucherclass_id = "0";
	public String voucherclass_name = "";
	public String voucherclass_desc = "";
	public String voucherclass_notes = "";
	public String voucherclass_status = "";
	public String voucherclass_rank = "";
	public String voucherclass_active = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";

	public String voucherclass_entry_id = "0";
	public String voucherclass_entry_date = "";
	public String voucherclass_modified_id = "0";
	public String voucherclass_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String entity_id = "0";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request)) + "";
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				entity_id = CNumeric(GetSession("entity_id", request)) + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						voucherclass_desc = "";
					} else {
						GetValues(request, response);
						voucherclass_entry_id = emp_id;
						voucherclass_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managevoucherclass.jsp?voucherclass_id=" + voucherclass_id + "&msg=Voucher Type Added Successfully!"));
						}
					}
				} else {
					if ("yes".equals(update)) {
						status = "Update";
						if (!"yes".equals(updateB) && !"Delete Voucher Type".equals(deleteB)) {
							PopulateFields(response);
						} else if ("yes".equals(updateB) && !"Delete Voucher Type".equals(deleteB)) {
							GetValues(request, response);
							voucherclass_modified_id = emp_id;
							voucherclass_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managevoucherclass.jsp?voucherclass_id=" + voucherclass_id + "&msg=Voucher Type Updated Successfully!"));
							}
						} else if ("Delete Voucher Type".equals(deleteB)) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managevoucherclass.jsp?msg=Voucher Type Deleted Successfully!"));
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		voucherclass_name = PadQuotes(request.getParameter("txt_vouchertype_name"));
		voucherclass_desc = PadQuotes(request.getParameter("txt_vouchertype_desc"));
		voucherclass_notes = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_base_type")));
		voucherclass_status = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_numbering")));
		voucherclass_rank = PadQuotes(request.getParameter("txt_vouchertype_Prefix"));
		voucherclass_active = PadQuotes(request.getParameter("txt_vouchertype_suffix"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (voucherclass_name.equals("")) {
			msg += "<br>Enter Voucher Name!";
		} else if (!voucherclass_name.equals("")) {
			StrSql = "SELECT voucherclass_name FROM axela_acc_voucher_class"
					+ " WHERE voucherclass_name = ?"
					+ " AND voucherclass_id != ?";
			map.put(1, voucherclass_name);
			map.put(2, voucherclass_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			try {
				if (crs.next()) {
					msg += "<br>Simillar Name found!";
				}
			} catch (Exception ex) {
				SOP(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

		if (voucherclass_desc.equals("")) {
			msg += "<br>Enter Description!";
		}

		// if (vouchertype_prefix.equals("")) {
		// msg += "<br>Enter Prefix!";
		// }
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			voucherclass_id = ExecuteQuery("Select (coalesce(max(voucherclass_id),0)+1) from axela_acc_voucher_class");
			StrSql = "Insert INTO " + compdb(comp_id) + "axela_acc_voucher_class"
					+ " (voucherclass_id,"
					+ " voucherclass_name,"
					+ " voucherclass_desc,"
					+ " voucherclass_notes,"
					+ " voucherclass_status,"
					+ " voucherclass_rank,"
					+ " voucherclass_active)"
					+ " values"
					+ " (" + voucherclass_id + ","
					+ " '" + voucherclass_name + "',"
					+ " '" + voucherclass_desc + "',"
					+ " '" + voucherclass_notes + "',"
					+ " '" + voucherclass_status + "',"
					+ " '" + voucherclass_rank + "',"
					+ " '" + voucherclass_active + "')";
			// SOP("StrSql" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from axela_acc_voucher_class"
					+ " where voucherclass_id = ?";
			map.put(1, voucherclass_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucherclass_id = crs.getString("voucherclass_id");
					voucherclass_name = crs.getString("voucherclass_name");
					voucherclass_desc = crs.getString("voucherclass_desc");
					voucherclass_notes = crs.getString("voucherclass_notes");
					voucherclass_status = crs.getString("voucherclass_status");
					voucherclass_rank = crs.getString("voucherclass_rank");
					voucherclass_active = crs.getString("voucherclass_active");
					if (!voucherclass_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(voucherclass_entry_id));
						entry_date = strToLongDate(crs.getString("vouchertype_entry_date"));
					}
					voucherclass_modified_id = crs.getString("vouchertype_modified_id");
					if (!voucherclass_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(voucherclass_modified_id));
						modified_date = strToLongDate(crs.getString("vouchertype_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Voucher Type!"));
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type"
					+ " SET"
					+ " voucherclass_name = '" + voucherclass_name + "',"
					// // + " voucherclass_base_type = '" + vouchertype_base_type + "',"
					// + " vouchertype_numbering = '" + vouchertype_numbering + "',"
					// + " vouchertype_prefix = '" + vouchertype_prefix + "',"
					// + " vouchertype_suffix = '" + vouchertype_suffix + "',"
					// + " vouchertype_zero_padding = '" + vouchertype_zero_padding + "',"
					// + " vouchertype_bank_cash_customer_restriction = '" + vouchertype_bank_cash_customer_restriction + "',"
					// + " vouchertype_modified_id = " + vouchertype_modified_id + ","
					// + " vouchertype_modified_date = '" + vouchertype_modified_date + "'"
					+ " where voucherclass_id = " + voucherclass_id + "";
			// SOP("StrSql" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "Delete FROM  " + compdb(comp_id) + "axela_acc_voucher_type where voucherclass_id = " + voucherclass_id + "";
		updateQuery(StrSql);
		// StrSql = "SELECT emp_voucherclass_id FROM axela_emp"
		// + " where emp_voucherclass_id = " + voucherclass_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Voucher Type is Associated with Executive!";
		// }
		// if (msg.equals("")) {
		// try {
		// StrSql = "Delete FROM  "+compdb(comp_id)+"axela_acc_voucher_type where voucherclass_id = " + voucherclass_id + "";
		// updateQuery(StrSql);
		// } catch (Exception ex) {
		// SOP(" Axelaauto===" + this.getClass().getName());
		// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// }
		// }
	}
}
