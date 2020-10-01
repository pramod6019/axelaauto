/* Ved Prakash (12th Sept 2013) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Veh_Salesorder_Update;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Salesorder_Dash extends ConnectWS {

	public String msg = "";
	public String StrSql = "";
	public String update = "";
	public String branch_id = "";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String role_id = "";
	public String enquiry_id = "0";
	public String so_id = "";
	public String so_date = "";
	public String variantcolour_id = "";
	public String so_allot_no = "";
	public String fincomp_id = "";
	public String so_finance_amt = "";
	public String so_booking_amount = "";
	public String so_refund_amount = "";
	public String so_payment_date = "";
	public String so_promise_date = "";
	public String so_retail_date = "";
	public String so_delivered_date = "";
	public String so_open = "";
	public String so_refno = "";
	public String so_cancel_date = "";
	public String cancelreason_id = "0";
	public String so_active = "";
	public String so_notes = "";
	public String enquiry_emp_id = "";
	public String enquiry_branch_id = "0";
	public String model_id = "0";
	public String name = "";
	public String value = "";
	public String name1 = "";
	public String value1 = "";
	public String make_id = "0", enquiry_tradeinmake_id = "0", status_id = "";
	public String commonfields = "";
	public String so_vehstock_id = "0";
	public String item_id = "0";
	public String lostcase2 = "";
	public String lostcase3 = "";
	public String lostcase1 = "";
	public String enquiry_age_id = "";
	public String enquiry_occ_id = "";
	public String item_model_id = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Salesorder_Dash(JSONObject input) {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!input.isNull("comp_id")) {
					comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				}
				if (!input.isNull("emp_uuid")) {
					emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
				}
				if (!input.isNull("so_id")) {
					so_id = CNumeric(PadQuotes((String) input.get("so_id")));
				}

				if (!input.isNull("commonfields")) {
					commonfields = PadQuotes((String) input.get("commonfields"));
				}
				if (!input.isNull("update")) {
					update = PadQuotes((String) input.get("update"));
				}

				if (!emp_id.equals("0")) {
					String StrSql1 = "SELECT emp_branch_id, emp_role_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_id;
					CachedRowSet crs1 = processQuery(StrSql1, 0);
					while (crs1.next()) {
						role_id = crs1.getString("emp_role_id");
						branch_id = crs1.getString("emp_branch_id");
					}
					crs1.close();

					if (!so_id.equals("0")) {
						if (!commonfields.equals("yes")) {
							PopulateFields();
							PopulateCancelReason();
							PopulateFinanceBy();
							PopulateColour();
						} else {
							PopulateFields();
						}
					}
					if (!so_id.equals("0") && update.equals("yes")) {
						output = UpdateFields(input);
					}
				}
			}
			if (AppRun().equals("0")) {
				SOP("output = " + output);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateFields() {
		CachedRowSet crs = null;
		StrSql = "SELECT so_id, so_date, so_branch_id, so_payment_date, so_promise_date,"
				+ " so_open, so_refno, so_fincomp_id, so_finance_amt,  so_cancel_date,"
				+ " so_po, so_no, so_desc, so_emp_id, so_active, so_notes, so_entry_id,"
				+ " so_entry_date, so_modified_id, so_modified_date, so_cancelreason_id,"
				+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
				+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name,contact_mobile1,"
				+ " rateclass_id, soitem_item_id,title_desc, contact_fname, contact_lname, customer_name, customer_id, contact_id,"
				+ " COALESCE(so_quote_id, 0) AS so_quote_id, so_grandtotal, so_retail_date,"
				+ " so_discamt, so_netamt, so_exprice, so_totaltax, so_vehstock_id, so_option_id, so_allot_no,"
				+ " so_delivered_date, so_enquiry_id, so_booking_amount, so_refund_amount, so_customer_id, so_entry_date, so_modified_date,"
				+ " coalesce((SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = so_entry_id), '') as entryby,"
				+ " coalesce((SELECT CONCAT(emp_name, ' (', emp_ref_no, ')')  FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = so_modified_id), '') as modifiedby,"
				+ " so_reg_no, so_reg_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
				+ " AND soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
				+ " WHERE so_id = " + so_id;
		// SOP("StrSql =populate= " + StrSql);
		try {
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (commonfields.equals("yes")) {
						map.put("so_id", crs.getString("so_id"));
						map.put("branch_name", crs.getString("branch_name"));
						map.put("so_no", crs.getString("so_no"));
						map.put("so_date", strToShortDate(crs.getString("so_date")));
						map.put("contact_fname", crs.getString("contact_fname"));
						map.put("contact_lname", crs.getString("contact_lname"));
						map.put("customer_name", crs.getString("customer_name"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("title_desc", crs.getString("title_desc"));
						map.put("customer_id", crs.getString("customer_id"));
					} else {
						map.put("so_id", crs.getString("so_id"));
						map.put("so_enquiry_id", crs.getString("so_enquiry_id"));
						map.put("so_quote_id", crs.getString("so_quote_id"));
						map.put("so_payment_date", strToShortDate(crs.getString("so_payment_date")));
						map.put("so_promise_date", strToShortDate(crs.getString("so_promise_date")));
						map.put("so_open", crs.getString("so_open"));
						map.put("so_refno", crs.getString("so_refno"));
						map.put("so_fincomp_id", crs.getString("so_fincomp_id"));
						map.put("so_finance_amt", crs.getString("so_finance_amt"));
						map.put("so_cancel_date", strToShortDate(crs.getString("so_cancel_date")));
						map.put("so_po", crs.getString("so_po"));
						map.put("so_desc", crs.getString("so_desc"));
						map.put("so_emp_id", crs.getString("so_emp_id"));
						map.put("so_active", crs.getString("so_active"));
						map.put("so_notes", crs.getString("so_notes"));
						map.put("so_entry_date", strToShortDate(crs.getString("so_entry_date")));
						map.put("so_modified_id", crs.getString("so_modified_id"));
						map.put("so_modified_date", strToShortDate(crs.getString("so_modified_date")));
						map.put("so_cancelreason_id", crs.getString("so_cancelreason_id"));
						map.put("so_discamt", crs.getString("so_discamt"));
						map.put("so_netamt", crs.getString("so_netamt"));
						map.put("so_exprice", crs.getString("so_exprice"));
						map.put("so_totaltax", crs.getString("so_totaltax"));
						map.put("so_vehstock_id", crs.getString("so_vehstock_id"));
						map.put("so_option_id", crs.getString("so_option_id"));
						map.put("so_allot_no", crs.getString("so_allot_no"));
						map.put("so_delivered_date", strToShortDate(crs.getString("so_delivered_date")));
						map.put("so_registration_no", crs.getString("so_reg_no"));
						map.put("so_registration_date", strToShortDate(crs.getString("so_reg_date")));
						map.put("so_enquiry_id", crs.getString("so_enquiry_id"));
						map.put("so_booking_amount", crs.getString("so_booking_amount"));
						map.put("so_refund_amount", crs.getString("so_refund_amount"));
						map.put("so_customer_id", crs.getString("so_customer_id"));
						map.put("so_entry_id", crs.getString("so_entry_id"));
						map.put("so_entry_by", crs.getString("entryby"));
						map.put("so_entry_date", strToShortDate(crs.getString("so_entry_date")));
						map.put("so_modified_id", crs.getString("so_modified_id"));
						map.put("so_modified_by", crs.getString("modifiedby"));
						map.put("so_modified_date", strToShortDate(crs.getString("so_modified_date")));

					}
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatefields", list);
				list.clear();
			} else {
				output.put("msg", "No Enquiry Found!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateColour() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT option_id, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE 1 = 1"
					+ " GROUP BY option_id"
					+ " ORDER BY option_name";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("option_id", "0");
				map.put("option_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("option_id", crs.getString("option_id"));
					map.put("option_desc", crs.getString("option_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("option_id", "0");
				map.put("option_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecolour", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateFinanceBy() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY fincomp_name";
			// SOP("StrSql==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("fincomp_id", "0");
				map.put("fincomp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("fincomp_id", crs.getString("fincomp_id"));
					map.put("fincomp_name", unescapehtml(crs.getString("fincomp_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("fincomp_id", "0");
				map.put("fincomp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatefinanceby", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateCancelReason() {
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("cancelreason_id", "0");
				map.put("cancelreason_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("cancelreason_id", crs.getString("cancelreason_id"));
					map.put("cancelreason_name", unescapehtml(crs.getString("cancelreason_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("cancelreason_id", "0");
				map.put("cancelreason_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecancelreason", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject UpdateFields(JSONObject input) {
		String history_oldvalue = "";
		String history_newvalue = "";
		String history_actiontype = "";
		try {
			if (!input.isNull("name")) {
				name = PadQuotes((String) input.get("name"));
			}
			if (!input.isNull("value")) {
				value = PadQuotes((String) input.get("value"));
			}
			if (!input.isNull("name1")) {
				name1 = PadQuotes((String) input.get("name1"));
			}
			if (!input.isNull("value1")) {
				value1 = PadQuotes((String) input.get("value1"));
			}
			if (!so_id.equals("0")) {
				StrSql = "SELECT so_id, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					so_id = crs.getString("so_id");
					emp_id = crs.getString("emp_id");
				}
				if (CNumeric(so_id).equals("0")) {
					output.put("msg", "Update Permission Denied!");
				}
				crs.close();
			}
			// else {
			// output.put("msg", "Update Permission Denied!");
			// }
			output.put("msg", "Update Permission Denied!");
			if (1 == 2) {
				if (!so_id.equals("0") || !emp_id.equals("0") && update.equals("yes")) {
					if (name.equals("txt_so_vehstock_id")) {
						value = value.replaceAll("nbsp", "&");
						// if (!CNumeric(value).equals("0")) {
						// StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
						// + " WHERE vehstock_id = '" + value + "'";
						// SOP("value==="+value);
						// SOP("StrSql===123+++" + StrSql);
						// value = CNumeric(ExecuteQuery(StrSql));
						// SOP("value==1="+value);
						// }
						// if (value.equals("0")) {
						// msg = "Invalid Stock";
						// output.put("msg", msg);
						// } else {
						// StrSql = "SELECT vehstock_item_id FROM " + compdb(comp_id) + "axela_vehstock"
						// + " WHERE vehstock_id = '" + value + "'";
						// if (!ExecuteQuery(StrSql).equals(item_id)) {
						// msg = "Invalid Stock ID!";
						// output.put("msg", msg);
						// }
						// }
						if (!CNumeric(value).equals("0")) {
							StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_sales_so"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
									+ " WHERE vehstock_id = " + value + ""
									+ " AND so_active = 1"
									+ " AND so_id != " + so_id + "";
							if (ExecuteQuery(StrSql).equals(value)) {
								msg = "Stock ID is associated with other Sales Order !";
								output.put("msg", msg);
							}
						} else if (value.equals("")) {
							so_vehstock_id = "0";
						}
						if (msg.equals("")) {
							String old_vehstock_id = "", branch_email1 = "";
							StrSql = "SELECT so_vehstock_id, COALESCE(vehstock_id, 0) AS vehstock_id, branch_email1"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
									+ " WHERE so_id = " + so_id + "";
							CachedRowSet crset = processQuery(StrSql, 0);

							while (crset.next()) {
								old_vehstock_id = crset.getString("so_vehstock_id");
								history_oldvalue = crset.getString("vehstock_id");
								branch_email1 = crset.getString("branch_email1");
							}
							crset.close();

							if (!CNumeric(value).equals("0")) {
								if (!old_vehstock_id.equals(value)) {
								}
							}

							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
									+ " SET"
									+ " so_vehstock_id = " + value + ""
									+ " WHERE so_id = " + so_id + "";
							updateQuery(StrSql);

							history_actiontype = "Stock ID";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
									+ " (history_so_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " (" + so_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + CNumeric(value) + "')";
							updateQuery(StrSql);
							msg = "Stock ID Updated!";
							output.put("msg", msg);
						}
					} else if (name.equals("dr_so_option_id")) {
						value = value.replaceAll("nbsp", "&");
						SOP("value====" + value);
						history_oldvalue = ExecuteQuery("SELECT option_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
								+ " WHERE so_id = " + so_id);
						SOP("comming--1");
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_option_id = " + value + ""
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						SOP("comming--2");
						history_newvalue = ExecuteQuery("SELECT option_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
								+ " WHERE so_option_id = " + value);
						SOP("comming--12");
						history_actiontype = "Colour";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						SOP("STRSQL==color==" + StrSql);
						SOP("comming--4");
						msg = "Colour Updated!";
						output.put("msg", msg);
					} else if (name.equals("txt_so_allot_no")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT so_allot_no"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_allot_no = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						history_actiontype = "Allotment No.";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						// StrHTML = "Allotment No. Updated!";
						msg = "Allotment No. Updated!";
						output.put("msg", msg);
					} else if (name.equals("dr_so_fincomp_id")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT COALESCE(fincomp_name, '') AS fincomp_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_fincomp_id = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("SELECT COALESCE(fincomp_name, '') AS fincomp_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
								+ " WHERE so_fincomp_id = " + value);

						history_actiontype = "Finance By";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " ('" + so_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						// StrHTML = "Finance By Update!";
						output.put("msg", "Finance By Updated!");
					} else if (name.equals("txt_so_finance_amt")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT so_finance_amt"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_finance_amt = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						history_actiontype = "Finance Amount";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						// StrHTML = "Finance Amount Updated!";
						output.put("msg", "Finance Amount Updated!");
					} else if (name.equals("txt_so_booking_amount")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT so_booking_amount"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id =" + so_id);
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_booking_amount = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						history_actiontype = "SO Booking Amount";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						// StrHTML = "Booking Amount Updated!";
						output.put("msg", "Booking Amount Updated!");
					} else if (name.equals("txt_so_refund_amount")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT so_refund_amount"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_refund_amount = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						history_actiontype = "Excess Refund";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						// StrHTML = "Excess Refund Updated!";
						output.put("msg", "Excess Refund Updated!");
					} else if (name.equals("txt_so_payment_date")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (!isValidDateFormatShort(value)) {
								// StrHTML = "<font color=\"red\">Enter Valid Payment Date!</font>";
								output.put("msg", "Enter Valid Payment Date!");
							} else {
								StrSql = "SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id + "";
								so_date = ExecuteQuery(StrSql);
								if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
									// StrHTML = "<font color=\"red\">Payment Date must be greater than or equal to Sales Order Date!</font>";
								} else {
									history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_payment_date"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_payment_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									history_actiontype = "Payment Date";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + so_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// StrHTML += "Payment Date updated!";
									output.put("msg", "Payment Date updated!");

								}
							}
						} else {
							// StrHTML = "<font color=\"red\">Enter Payment Date!</font>";
							output.put("msg", "Enter Payment Date!");
						}

					} else if (name.equals("txt_so_promise_date")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");

							if (!isValidDateFormatShort(value)) {
								// StrHTML = "<font color=\"red\">Enter valid Tentative Delivery Date!</font>";
								output.put("msg", "Enter valid Tentative Delivery Date!");
							} else {
								StrSql = "SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id + "";
								so_date = ExecuteQuery(StrSql);

								if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
									// StrHTML = "<font color=\"red\">Tentative Delivery Date must be greater than or equal to Sales Order Date!</font>";
									output.put("msg", "Tentative Delivery Date must be greater than or equal to Sales Order Date!");
								} else {
									history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_promise_date"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_promise_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									history_actiontype = "Tentative Delivery Date";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + so_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// StrHTML = StrHTML + "Tentative Delivery Date updated!";
									output.put("msg", "Tentative Delivery Date updated!");
								}
							}
						} else {
							// StrHTML = "<font color=\"red\">Enter Tentative Delivery Date!</font>";
							output.put("msg", "Enter Tentative Delivery Date!");
						}
					} else if (name.equals("txt_so_retail_date")) {
						value = value.replaceAll("nbsp", "&");

						if (!value.equals("")) {
							if (!isValidDateFormatShort(value)) {
								// StrHTML = "<font color=\"red\">Enter valid Retail Date!</font>";
								msg = "Enter valid Retail Date!";
								output.put("msg", msg);
							} else {
								StrSql = "SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id + "";
								so_date = ExecuteQuery(StrSql);

								if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
									// StrHTML = "<font color=\"red\">Retail Date must be greater than or equal to Sales Order Date!</font>";
									msg = "Retail Date must be greater than or equal to Sales Order Date!";
									output.put("msg", msg);
								}
							}
						}

						if (msg.equals("")) {
							history_oldvalue = ExecuteQuery("SELECT so_retail_date"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);
							if (!history_oldvalue.equals("")) {
								history_oldvalue = strToShortDate(history_oldvalue);
							}

							if (!value.equals("")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_retail_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_retail_date = ''"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
							}

							history_actiontype = "Retail Date";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
									+ " (history_so_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + so_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							// StrHTML = StrHTML + "Retail Date updated!";
							output.put("msg", "Retail Date updated!");
						}
					} else if (name.equals("txt_so_delivered_date")) {
						value = value.replaceAll("nbsp", "&");
						if (!isValidDateFormatShort(value) && !value.equals("")) {
							// StrHTML = "<font color=\"red\">Enter valid Delivered Date!</font>";
							msg = "Enter valid Delivered Date!";
							output.put("msg", msg);
						} else {
							SOP("2");
							String so_vehstock_id = "0";
							StrSql = "SELECT so_date, so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id + "";
							CachedRowSet crs = processQuery(StrSql, 0);
							while (crs.next()) {
								so_date = crs.getString("so_date");
								so_vehstock_id = crs.getString("so_vehstock_id");
							}
							crs.close();

							if (!value.equals("")) {
								if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
									// StrHTML = "<font color=\"red\">Delivered Date must be greater than or equal to Sales Order Date!</font>";
									msg = "Delivered Date must be greater than or equal to Sales Order Date!";
									output.put("msg", msg);
								} else if (CNumeric(so_vehstock_id).equals("0")) {
									// StrHTML = "<font color=\"red\">Stock ID is not associated with Delivered Date!</font>";
									msg = "Stock ID is not associated with Delivered Date!";
									output.put("msg", msg);
								}
							}
							if (msg.equals("")) {
								String enquiry_id = "0", so_active = "";
								String branch_id = "0", psf_crm_emp_id = "0", so_emp_id = "0";
								history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_delivered_date"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id));

								if (!value.equals("")) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_delivered_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE so_id = " + so_id + "";
									new Veh_Salesorder_Update().AddSOVehicle(so_id, emp_id, comp_id, ConvertShortDateToStr(value));
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_delivered_date = ''"
											+ " WHERE so_id = " + so_id + "";
								}
								updateQuery(StrSql);

								StrSql = "SELECT so_enquiry_id, so_vehstock_id, so_active,"
										+ " so_branch_id, so_emp_id"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id + "";
								CachedRowSet crset = processQuery(StrSql, 0);

								if (crset.isBeforeFirst()) {
									while (crset.next()) {
										enquiry_id = crset.getString("so_enquiry_id");
										so_vehstock_id = crset.getString("so_vehstock_id");
										so_active = crset.getString("so_active");
										branch_id = crset.getString("so_branch_id");
										so_emp_id = crset.getString("so_emp_id");
									}

									StrSql = "SELECT COALESCE((SELECT team_crm_emp_id"
											+ " FROM " + compdb(comp_id) + "axela_sales_team"
											+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
											+ " WHERE team_branch_id = " + branch_id + ""
											+ " AND teamtrans_emp_id = " + so_emp_id + ""
											+ " AND team_crm_emp_id != 0"
											+ " LIMIT 1), 0)";
									psf_crm_emp_id = ExecuteQuery(StrSql);
									if (value.equals("")) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
												+ " SET"
												+ " enquiry_stage_id = 5"
												+ " WHERE enquiry_id = " + enquiry_id + "";
									} else {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
												+ " SET"
												+ " enquiry_stage_id = 6"
												+ " WHERE enquiry_id = " + enquiry_id + "";
									}
									updateQuery(StrSql);

									if (!value.equals("") && !CNumeric(so_vehstock_id).equals("0")) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_delstatus_id = '6'"
												+ " WHERE vehstock_id = " + so_vehstock_id + "";
										updateQuery(StrSql);
									}
									if (so_active.equals("1") && !value.equals("")) {
										new Veh_Salesorder_Update().AddCustomCRMFields(so_id, "3", "0", comp_id);
									} else if (value.equals("")) { // if
										// so_delivered_date is not there,delete all the PSFs for that SO
										StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm"
												+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
												+ " WHERE crm_so_id = " + so_id
												+ " AND crmdays_crmtype_id = 3";
										updateQuery(StrSql);
									}
								}
								crset.close();

								history_actiontype = "Delivered Date";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + so_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// StrHTML += "Delivered Date updated!";
								output.put("msg", "Delivered Date updated!");
							}
						}
					} else if (name.equals("txt_so_registration_no")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT so_reg_no"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_reg_no = '" + CNumeric(value) + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);
						history_actiontype = "Registration No.";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						// StrHTML = "Registration No. Updated!";
						output.put("msg", "Registration No. Updated!");
					} else if (name.equals("txt_so_registration_date")) {
						value = value.replaceAll("nbsp", "&");

						if (msg.equals("")) {
							history_oldvalue = ExecuteQuery("SELECT so_reg_date"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);
							if (!history_oldvalue.equals("")) {
								history_oldvalue = strToShortDate(history_oldvalue);
							}

							if (!value.equals("")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_reg_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_reg_date = ''"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
							}

							history_actiontype = "Registration Date";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
									+ " (history_so_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + so_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							// StrHTML = StrHTML + "Registration Date updated!";
							output.put("msg", "Registration Date updated!");
						}
					} else if (name.equals("chk_so_open")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT so_open"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);
							if (history_oldvalue.equals("1")) {
								history_oldvalue = "Open";
							} else {
								history_oldvalue = "Close";
							}

							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
									+ " SET"
									+ " so_open = '" + value + "'"
									+ " WHERE so_id = " + so_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT so_open"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);

							if (history_newvalue.equals("1")) {
								history_newvalue = "Open";
							} else {
								history_newvalue = "Close";
							}

							history_actiontype = "Sales Order Open";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
									+ " (history_so_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + so_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// StrHTML = "Sales Order Open updated!";
							output.put("msg", "Sales Order Open updated!");
						}
					} else if (name.equals("txt_so_refno")) {
						if (!value.equals("")) {
							if (value.length() >= 2) {
								StrSql = "SELECT so_refno FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_refno = " + value + ""
										+ " AND so_id != " + so_id + "";
								if (ExecuteQuery(StrSql).equals("")) {
									value = value.replaceAll("nbsp", "&");
									history_oldvalue = ExecuteQuery("SELECT so_refno"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_refno = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									history_actiontype = "Ref No.";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " (" + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')" + "";
									updateQuery(StrSql);
									// StrHTML = "Reference No. Updated!";
									output.put("msg", "Reference No. Updated!");
								} else {
									// StrHTML = "<font color=\"red\">Similar Sales Order Reference No. found!</font>";
									output.put("msg", "Similar Sales Order Reference No. found!");
								}
							} else {
								// StrHTML = "<font color=\"red\">Sales Order Reference No. should be atleast Two Digits!</font>";
								output.put("msg", "Sales Order Reference No. should be atleast Two Digits!");
							}
						} else {
							// StrHTML = "<font color=\"red\">Enter Reference No.!</font>";
							output.put("msg", "Enter Reference No.!");
						}
					} else if (name.equals("txt_so_cancel_date")) {
						value = value.replaceAll("nbsp", "&");

						if (!value.equals("") && !isValidDateFormatShort(value)) {
							// StrHTML = "<font color=\"red\">Enter valid Cancel Date!</font>";
							output.put("msg", "Enter valid Cancel Date!");
						} else {
							StrSql = "SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id + "";
							so_date = ExecuteQuery(StrSql);

							if (!value.equals("") && Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
								// StrHTML = "<font color=\"red\">Cancel Date must be greater than or equal to Sales Order Date!</font>";
								output.put("msg", "Cancel Date must be greater than or equal to Sales Order Date!");
							} else {
								history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_cancel_date"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id));

								if (!value.equals("")) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_cancel_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE so_id = " + so_id + "";
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET"
											+ " so_cancel_date = ''"
											+ " WHERE so_id = " + so_id + "";
								}
								updateQuery(StrSql);

								history_actiontype = "Cancel Date";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + so_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								output.put("msg", "Cancel Date updated!");
							}
						}
					} else if (name.equals("dr_so_cancelreason_id")) {
						SOP("1");
						history_oldvalue = "";
						so_active = "0";

						StrSql = "SELECT so_active, so_cancel_date,"
								+ " COALESCE(cancelreason_name, '') AS cancelreason_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
								+ " WHERE so_id = " + so_id + "";
						CachedRowSet crset = processQuery(StrSql, 0);
						SOP("2");
						while (crset.next()) {
							history_oldvalue = crset.getString("cancelreason_name");
							so_active = crset.getString("so_active");
							so_cancel_date = crset.getString("so_cancel_date");
						}
						crset.close();

						if (!value.equals("0") && so_active.equals("1")) {
							SOP("2.1");
							// StrHTML = "<font color=\"red\">Active Sales Order cannot have Cancel Reason!</font>";
							// StrHTML = "";
						} else if (value.equals("0") && so_active.equals("0")) {
							SOP("2.2");
							// StrHTML = "";
						} else {
							if (so_cancel_date.equals("")) {
								// StrHTML = "<font color=\"red\">Enter Cancel Date!</font>";
								output.put("msg", "Enter Cancel Date!");
							} else {
								SOP("2.3");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_cancelreason_id = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT COALESCE(cancelreason_name, '') AS cancelreason_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
										+ " WHERE so_cancelreason_id = " + value);

								history_actiontype = "Cancel Reason";
								SOP("3");
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + so_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								// StrHTML = "Cancel Reason Update!";
								output.put("msg", "Cancel Reason Update!");
							}
						}
					} else if (name.equals("chk_so_active")) {
						if (!value.equals("")) {
							SOP("value==1=" + value);
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = "";
							so_vehstock_id = "0";
							SOP("1");
							String vehstock_id = "", branch_email1 = "";
							StrSql = "SELECT so_active, so_vehstock_id, branch_email1, so_cancel_date,"
									+ " COALESCE(vehstock_id, 0) AS vehstock_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
									+ " WHERE so_id = " + so_id + "";
							CachedRowSet crset = processQuery(StrSql, 0);
							SOP("2");
							while (crset.next()) {
								history_oldvalue = crset.getString("so_active");
								so_vehstock_id = crset.getString("so_vehstock_id");
								vehstock_id = crset.getString("vehstock_id");
								branch_email1 = crset.getString("branch_email1");
								so_cancel_date = strToShortDate(crset.getString("so_cancel_date"));
							}
							crset.close();
							SOP("so_cancel_date===" + so_cancel_date);
							SOP("cancelreason_id===" + cancelreason_id);
							SOP("history_oldvalue===" + history_oldvalue);
							SOP("so_cancel_date===" + isValidDateFormatShort(so_cancel_date));

							if ((!so_cancel_date.equals("") && !value1.equals("0")
									&& history_oldvalue.equals("1")) || history_oldvalue.equals("0")) {
								if (!isValidDateFormatShort(so_cancel_date) && history_oldvalue.equals("1")) {
									SOP("2");
									// StrHTML = "<font color=\"red\">Enter valid Cancel Date!</font>";
									output.put("msg", "Enter valid Cancel Date!");
								} else {
									if (history_oldvalue.equals("1")) {
										history_oldvalue = "Active";
									} else {
										history_oldvalue = "Inactive";
									}

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET";
									if (value.equals("1")) {
										StrSql += " so_cancel_date = '',"
												+ " so_cancelreason_id = 0,";
									} else if (value.equals("0")) {
										SOP("oooooo");
										StrSql += " so_cancel_date = '" + ConvertShortDateToStr(so_cancel_date) + "',"
												+ " so_cancelreason_id = " + value1 + ",";
									}
									StrSql += " so_active = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									SOP("print===" + StrSql);
									updateQuery(StrSql);

									SOP("3");
									if (history_oldvalue.equals("Active")) {
										// /// SO is Inactive
										new Veh_Salesorder_Update().AddCustomCRMFields(so_id, "2", "1", comp_id);
										new Veh_Salesorder_Update().SendEmailToNewSales(so_id, vehstock_id, emp_id, "0", branch_email1, comp_id);
									} else {
										// /// SO is active
										StrSql = "DELETE salescrm.* FROM " + compdb(comp_id) + "axela_sales_crm AS salescrm"
												+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = salescrm.crm_crmdays_id"
												+ " WHERE salescrm.crm_so_id = " + so_id + ""
												+ " AND crmdays_crmtype_id = 2"
												+ " AND crmdays_so_inactive = 1";
										updateQuery(StrSql);
									}

									history_newvalue = ExecuteQuery("SELECT so_active"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);
									SOP("history_newvalue====" + history_newvalue);

									if (history_newvalue.equals("1")) {
										history_newvalue = "Active";
									} else {
										history_newvalue = "Inactive";
									}

									history_actiontype = "Sales Order Active";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + so_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);
									if (history_newvalue.equals("Inactive")) {
										// StrHTML = "Sales Order Inactivated!";
										output.put("msg", "Sales Order Inactivated!");
									} else {
										// StrHTML = "Sales Order Activated!";
										output.put("msg", "Sales Order Activated!");
									}
								}
							} else {
								// StrHTML = "<font color=\"red\">Enter Cancel Date and Select Cancel Reason for Inactivating the Sales Order!</font>";
								output.put("msg", "Enter Cancel Date and Select Cancel Reason for Inactivating the Sales Order!");
							}
						}
					} else if (name.equals("txt_so_notes")) {
						value = value.replaceAll("nbsp", "&");

						if (value.length() > 255) {
							value = value.substring(0, 254);
						}

						history_oldvalue = ExecuteQuery("SELECT so_notes"
								+ " FROM " + compdb(comp_id) + "axela_sales_so"
								+ " WHERE so_id = " + so_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET"
								+ " so_notes = '" + value + "'"
								+ " WHERE so_id = " + so_id + "";
						updateQuery(StrSql);

						history_actiontype = "Notes";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
								+ " (history_so_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + so_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						// StrHTML = "Notes Updated!";
						output.put("msg", "Notes Updated!");
					}
				} else {
					output.put("msg", "Update Permission Denied!");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
