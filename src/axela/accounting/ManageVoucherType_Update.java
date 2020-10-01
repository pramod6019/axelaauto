package axela.accounting;

/**
 * JEET 24 DEC 2014
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageVoucherType_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";

	public String emp_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String vouchertype_name = "";
	public String vouchertype_label = "";
	public String vouchertype_voucherclass_id = "0";
	public String vouchertype_base_type = "";
	public String vouchertype_numbering = "";
	public String vouchertype_prefix = "";
	public String vouchertype_suffix = "";
	public String vouchertype_zero_padding = "";
	public String vouchertype_bank_cash_customer_restriction = "";
	public String vouchertype_ref_no_enable_active = "";
	public String vouchertype_defaultauthorize = "";
	public String vouchertype_authorize = "";
	public String vouchertype_affects_inventory = "";
	public String vouchertype_roundoff = "";

	public String vouchertype_roundoff_ledger_cr = "";
	public String vouchertype_roundoff_ledger_dr = "";
	public String vouchertype_tds = "";
	public String vouchertype_tds_ledger_id = "";
	public String vouchertype_affects_accounts = "";
	public String vouchertype_mobile = "";
	public String vouchertype_email = "";
	public String vouchertype_dob = "";
	public String vouchertype_dnd = "";
	public String vouchertype_billing_add = "";
	public String vouchertype_consignee_add = "";
	public String vouchertype_gatepass = "";
	public String vouchertype_driver_no = "";
	public String vouchertype_tempo_no = "";
	public String vouchertype_lrno = "";
	public String vouchertype_cashdiscount = "";
	public String vouchertype_turnoverdisc = "";
	public String vouchertype_terms = "";
	public String vouchertype_ref_no_mandatory_active = "";
	public String vouchertype_email_enable = "";
	public String vouchertype_email_auto = "";
	public String vouchertype_email_sub = "";
	public String vouchertype_email_format = "";
	public String vouchertype_sms_enable = "";
	public String vouchertype_sms_auto = "";
	public String vouchertype_sms_format = "";
	public String vouchertype_rank = "";
	public String vouchertype_active = "1";
	public String vouchertype_entry_id = "0", vouchertype_entry_date = "";
	public String vouchertype_modified_id = "0",
			vouchertype_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";
	public String QueryString = "";
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
					vouchertype_active = "1";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB)) {
						GetValues(request, response);
						vouchertype_modified_id = emp_id;
						vouchertype_modified_date = ToLongDate((kknow()));
						UpdateFields();
						BuildAccMenuLinks(request, comp_id);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managevouchertype.jsp?vouchertype_id="
									+ vouchertype_id
									+ "&msg=Voucher Type updated successfully!\n"
									+ msg + ""));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
		vouchertype_name = PadQuotes(unescapehtml(request.getParameter("vouchertype_name")));
		vouchertype_label = PadQuotes(request.getParameter("txt_vouchertype_label"));
		vouchertype_base_type = PadQuotes(request.getParameter("dr_vouchertype_base_type"));
		vouchertype_prefix = PadQuotes(request.getParameter("txt_vouchertype_prefix"));
		vouchertype_suffix = PadQuotes(request.getParameter("txt_vouchertype_suffix"));
		vouchertype_zero_padding = PadQuotes(request.getParameter("txt_vouchertype_zero_padding"));
		// Modified on 10/12/2014
		vouchertype_email = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_emails")));
		vouchertype_defaultauthorize = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_defaultauthorize")));
		vouchertype_authorize = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_authorize")));
		vouchertype_affects_inventory = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_affects_inventory")));
		vouchertype_roundoff = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_vouchertype_roundoff")));
		vouchertype_roundoff_ledger_dr = CNumeric(PadQuotes(request.getParameter("ledger1")));
		vouchertype_roundoff_ledger_cr = CNumeric(PadQuotes(request.getParameter("ledger2")));
		vouchertype_tds = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_tds")));
		vouchertype_tds_ledger_id = CNumeric(PadQuotes(request.getParameter("ledger3")));
		vouchertype_affects_accounts = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_affects_accounts")));
		vouchertype_mobile = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_mobile")));
		vouchertype_dob = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_dob")));
		vouchertype_dnd = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_dob")));
		vouchertype_billing_add = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_billing_add")));
		vouchertype_consignee_add = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_consignee_add")));

		vouchertype_gatepass = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_gatepass")));
		vouchertype_driver_no = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_driver_no")));
		vouchertype_tempo_no = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_tempo_no")));
		vouchertype_lrno = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_lrno")));
		vouchertype_cashdiscount = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_cashdiscount")));
		vouchertype_turnoverdisc = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_turnoverdisc")));

		vouchertype_terms = PadQuotes(request.getParameter("txt_vouchertype_terms"));
		vouchertype_ref_no_enable_active = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_ref_no_enable_active")));
		vouchertype_ref_no_mandatory_active = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_ref_no_mandatory_active")));
		// vouchertype_ref_no_mandatory_active =
		// CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_ref_no_mandatory_active")));
		// Modified on 18/11/2014
		vouchertype_email_enable = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_email_enable")));
		vouchertype_email_auto = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_email_auto")));
		vouchertype_email_sub = PadQuotes(request.getParameter("txt_vouchertype_email_sub"));
		vouchertype_email_format = PadQuotes(request.getParameter("txt_vouchertype_email_format"));
		vouchertype_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_email_enable")));
		vouchertype_sms_auto = CheckBoxValue(PadQuotes(request.getParameter("ch_vouchertype_email_auto")));
		vouchertype_sms_format = PadQuotes(request.getParameter("txt_vouchertype_sms_format"));
		//
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

		vouchertype_numbering = "0";
		vouchertype_bank_cash_customer_restriction = "0";
		vouchertype_zero_padding = "0";
	}

	protected void CheckForm() {
		msg = "";
		if (vouchertype_label.equals("")) {
			msg = msg + "<br>Enter Label!";
		}
		if (!vouchertype_roundoff.equals("0")) {
			if (vouchertype_roundoff_ledger_dr.equals("0") || vouchertype_roundoff_ledger_dr.equals("0")) {
				if (vouchertype_roundoff_ledger_dr.equals("0")) {
					msg += "<br>Select Round Off Dr!";
				}
				if (vouchertype_roundoff_ledger_cr.equals("0")) {
					msg += "<br>Select Round Off Cr!";
				}
			}
			else {
				// if (vouchertype_roundoff_ledger_dr.equals(vouchertype_roundoff_ledger_cr)) {
				// msg += "<br>Same Round Off Ledger Found!!";
				// }
			}
		}
		else {
			// if (!vouchertype_roundoff_ledger_dr.equals("0") && !vouchertype_roundoff_ledger_cr.equals("0")) {
			// if (vouchertype_roundoff_ledger_dr.equals(vouchertype_roundoff_ledger_cr)) {
			// msg += "<br>Same Round Off Ledger Found!";
			// }
			// }
		}
		if (!vouchertype_tds.equals("0")) {
			if (vouchertype_tds_ledger_id.equals("0")) {
				msg += "<br>Select TDS Ledger !";
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * " + " FROM  " + compdb(comp_id) + "axela_acc_voucher_type "
					+ " WHERE  vouchertype_id = ?";
			prepmap.clear();
			prepmap.put(1, Integer.parseInt(vouchertype_id));
			// SOP("StrSql==="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vouchertype_name = crs.getString("vouchertype_name");
					vouchertype_label = crs.getString("vouchertype_label");
					vouchertype_voucherclass_id = crs.getString("vouchertype_voucherclass_id");
					vouchertype_base_type = crs.getString("vouchertype_base_type");
					vouchertype_prefix = crs.getString("vouchertype_prefix");
					vouchertype_suffix = crs.getString("vouchertype_suffix");
					vouchertype_zero_padding = crs.getString("vouchertype_zero_padding");
					vouchertype_bank_cash_customer_restriction = crs.getString("vouchertype_bank_cash_customer_restriction");
					// Modified on 10/12/2014
					vouchertype_mobile = crs.getString("vouchertype_mobile");
					vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
					vouchertype_authorize = crs.getString("vouchertype_authorize");
					vouchertype_affects_inventory = crs.getString("vouchertype_affects_inventory");
					vouchertype_roundoff = crs.getString("vouchertype_roundoff");
					vouchertype_roundoff_ledger_cr = crs.getString("vouchertype_roundoff_ledger_cr");
					vouchertype_roundoff_ledger_dr = crs.getString("vouchertype_roundoff_ledger_dr");
					vouchertype_tds = crs.getString("vouchertype_tds");
					vouchertype_tds_ledger_id = crs.getString("vouchertype_tds_ledger_id");
					vouchertype_affects_accounts = crs.getString("vouchertype_affects_accounts");
					vouchertype_email = crs.getString("vouchertype_email");
					vouchertype_dob = crs.getString("vouchertype_dob");
					vouchertype_dnd = crs.getString("vouchertype_dnd");
					vouchertype_billing_add = crs.getString("vouchertype_billing_add");
					vouchertype_consignee_add = crs.getString("vouchertype_consignee_add");

					vouchertype_gatepass = crs.getString("vouchertype_gatepass");
					vouchertype_driver_no = crs.getString("vouchertype_driver_no");
					vouchertype_tempo_no = crs.getString("vouchertype_tempo_no");
					vouchertype_lrno = crs.getString("vouchertype_lrno");
					vouchertype_cashdiscount = crs.getString("vouchertype_cashdiscount");
					vouchertype_turnoverdisc = crs.getString("vouchertype_turnoverdisc");

					vouchertype_terms = crs.getString("vouchertype_terms");
					vouchertype_ref_no_enable_active = crs.getString("vouchertype_ref_no_enable");
					vouchertype_ref_no_mandatory_active = crs.getString("vouchertype_ref_no_mandatory");

					// Modified on 18/11/2014
					vouchertype_email_enable = crs.getString("vouchertype_email_enable");
					vouchertype_email_auto = crs.getString("vouchertype_email_auto");
					vouchertype_email_sub = crs.getString("vouchertype_email_sub");
					vouchertype_email_format = crs.getString("vouchertype_email_format");
					vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
					vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
					vouchertype_sms_format = crs.getString("vouchertype_sms_format");
					//
					vouchertype_active = crs.getString("vouchertype_active");
					vouchertype_entry_id = crs.getString("vouchertype_entry_id");
					if (!vouchertype_entry_id.equals("")) {

						entry_by = Exename(comp_id, Integer
								.parseInt(vouchertype_entry_id));
						entry_date = strToLongDate(crs.getString("vouchertype_entry_date"));
					}

					vouchertype_modified_id = crs.getString("vouchertype_modified_id");
					if (!vouchertype_modified_id.equals("")) {
						modified_by = Exename(comp_id, Integer
								.parseInt(vouchertype_modified_id));
						modified_date = strToLongDate(crs.getString("vouchertype_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity Type!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type" + " SET"
						+ " vouchertype_name = '" + vouchertype_name + "',"
						+ " vouchertype_label = '" + vouchertype_label + "',"
						+ " vouchertype_base_type = '" + vouchertype_base_type + "',"
						// + " vouchertype_numbering = '" +
						// "(Select (coalesce(max(vouchertype_numbering),0)+1) FROM  "+compdb(comp_id)+"axela_acc_voucher_type as Number where 1=1"
						// + ")',"
						+ " vouchertype_prefix = '" + vouchertype_prefix + "',"
						+ " vouchertype_suffix = '" + vouchertype_suffix + "',"
						+ " vouchertype_bank_cash_customer_restriction = '" + vouchertype_bank_cash_customer_restriction + "',"
						// Modified on 10/12/2014
						+ " vouchertype_defaultauthorize = '" + vouchertype_defaultauthorize + "',"
						+ " vouchertype_authorize = '" + vouchertype_authorize + "',"
						+ " vouchertype_affects_inventory = '" + vouchertype_affects_inventory + "',"
						+ " vouchertype_roundoff = '" + vouchertype_roundoff + "',"
						+ " vouchertype_roundoff_ledger_dr = '" + vouchertype_roundoff_ledger_dr + "',"
						+ " vouchertype_roundoff_ledger_cr = '" + vouchertype_roundoff_ledger_cr + "',"
						+ " vouchertype_tds = '" + vouchertype_tds + "',"
						+ " vouchertype_tds_ledger_id = '" + vouchertype_tds_ledger_id + "',"
						+ " vouchertype_affects_accounts = '" + vouchertype_affects_accounts + "',"
						+ " vouchertype_mobile = '" + vouchertype_mobile + "',"
						+ " vouchertype_email = '" + vouchertype_email + "',"
						+ " vouchertype_dob = '" + vouchertype_dob + "',"
						+ " vouchertype_dnd = '" + vouchertype_dnd + "',"
						+ " vouchertype_billing_add = '" + vouchertype_billing_add + "',"
						+ " vouchertype_consignee_add = '" + vouchertype_consignee_add + "',"
						+ " vouchertype_gatepass = '" + vouchertype_gatepass + "',"
						+ " vouchertype_driver_no = '" + vouchertype_driver_no + "',"
						+ " vouchertype_tempo_no = '" + vouchertype_tempo_no + "',"
						+ " vouchertype_lrno = '" + vouchertype_lrno + "',"
						+ " vouchertype_cashdiscount = '" + vouchertype_cashdiscount + "',"
						+ " vouchertype_turnoverdisc = '" + vouchertype_turnoverdisc + "',"
						+ " vouchertype_terms = '" + vouchertype_terms + "',"
						+ " vouchertype_ref_no_enable = '" + vouchertype_ref_no_enable_active + "',"
						+ " vouchertype_ref_no_mandatory = '" + vouchertype_ref_no_mandatory_active + "',"
						// Modified on 18/11/2014
						+ " vouchertype_email_enable = '" + vouchertype_email_enable + "',"
						+ " vouchertype_email_auto = '" + vouchertype_email_auto + "',"
						+ " vouchertype_email_sub = '" + vouchertype_email_sub + "',"
						+ " vouchertype_email_format = '" + vouchertype_email_format + "',"
						+ " vouchertype_sms_enable = '" + vouchertype_sms_enable + "',"
						+ " vouchertype_sms_auto = '" + vouchertype_sms_auto + "',"
						+ " vouchertype_sms_format = '" + vouchertype_sms_format + "',"
						+ " vouchertype_active = '" + vouchertype_active + "',"
						+ " vouchertype_modified_id = '" + vouchertype_modified_id + "',"
						+ " vouchertype_modified_date = '" + vouchertype_modified_date + "'"
						+ " where vouchertype_id = " + vouchertype_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateBaseType() {
		prepmap.clear();
		StringBuilder Str = new StringBuilder();
		StrSql = " ";
		for (int i = 0; i <= 2; i++) {
			Str.append("<option value=" + i + "");
			Str.append(StrSelectdrop(i + "", vouchertype_base_type));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateCheck() {
		prepmap.clear();
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		StrSql = "SELECT voucherclass_id ,vouchertype_name"
				+ " FROM axela_acc_voucher_class"
				+ " GROUP BY voucherclass_id " + " ORDER BY voucherclass_id ";
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			crs = processPrepQuery(StrSql, prepmap, 0);
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("voucherclass_id"));
				Str.append(StrSelectdrop(crs.getString("voucherclass_id"),
						vouchertype_voucherclass_id));
				Str.append(">").append(crs.getString("vouchertype_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
