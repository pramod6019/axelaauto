package axela.axelaauto_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.Ledger_Check;
import axela.accounting.Receipt_Update;
import axela.portal.Header;
import axela.sales.Veh_SoToSi;
import cloudify.connect.Connect;

public class App_Receipt_Add extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_acc_receipt_access = "0";
	public String emp_uuid = "";
	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String voucher_vouchertype_id = "0";
	public String vouchertype_suffix = "", vouchertype_prefix = "";
	public String voucher_tag_id = "0";
	public String voucher_entry_type = "0";
	public String voucher_no = "0", voucher_so_id = "0", voucher_jc_id = "0";
	public String voucher_branch_id = "0";
	public String voucher_date = "";
	public String voucher_amount = "", mainparty_amount = "0";
	public String voucher_prev_amount = "";
	public String voucher_narration = "";
	public String voucher_customer_id = "";
	public String voucher_contact_id = "0", contact_title_id = "0",
			contact_fname = "", contact_lname = "";
	public String voucher_emp_id = "0";
	public String voucher_location_id = "0";
	public String voucher_ref_no = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_notes = "";
	public String voucher_active = "1";
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_date = "";
	public String vouchertrans_id = "0";
	public String vouchertrans_voucher_id = "0";
	public String vouchertrans_customer_id = "0";
	public String vouchertrans_amount = "0";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "0";
	public String vouchertrans_paymode_id = "-1";
	public String payment_id = "0";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "", vouchertrans_chequedate = "";
	public String vouchertrans_cheque_bank = "", vouchertrans_transaction_no = "";
	public String vouchertrans_bank_id = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_id = "0", vouchertype_name = "";
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String vouchertype_driver_no = "0";
	public String vouchertype_tempo_no = "0";
	public String vouchertype_terms = "";
	public String emp_branch_id = "0";
	public String voucherdate = "";
	public String BranchAccess = "";
	public String empEditperm = "1";
	public String ExeAccess = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "", voucherclass_acc = "0", session_id = "0";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String voucher_invoice_id = "0";
	public String customer_type = "0-0";

	public String emp_formattime = "", config_customer_name = "";
	public int voucher_count = 0;
	public String voucherbal_trans_id = "0", checked = "";
	public double voucherrecpt_amount = 0.00;
	public int voucherbal_amount = 0;
	// config variables ///
	public String vouchertype_email_enable;
	public String vouchertype_email_auto;
	// public String vouchertype_email_sub;
	// public String vouchertype_email_format;
	public String vouchertype_sms_enable;
	public String vouchertype_sms_auto;
	// public String vouchertype_sms_format;
	public String config_admin_email;
	public String config_email_enable;
	public String config_sms_enable;
	public String comp_email_enable;
	public String comp_sms_enable;
	public String contact_mobile = "";
	public String comp_id = "0";
	public String checkinvoice = "";
	public Ledger_Check ledger = new Ledger_Check();
	public int prepkey = 1;
	DecimalFormat deci = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public Connection conntx = null;
	public Statement stmttx = null;
	public String theme_color = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
				addB = PadQuotes(request.getParameter("add_button1"));
				msg = PadQuotes(request.getParameter("msg"));
				checkinvoice = PadQuotes(request.getParameter("checkinvoice"));
				PopulateConfigDetails();
				voucher_so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				if (checkinvoice.equals("yes")) {
					voucher_invoice_id = checkinvoice(request, response);
				}
				if (addB.equals("yes")) {
					emp_acc_receipt_access = ReturnPerm(comp_id, "emp_acc_receipt_access", request);
					if (emp_acc_receipt_access.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
					}
					voucher_entry_date = ToLongDate(kknow());
					GetValues(request, response);
					if (msg.equals("")) {
						AddReceipt(request);
						voucher_no = vouchertype_prefix + voucher_no + vouchertype_suffix;
					}
					if (msg.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("callurlreceipt-list.jsp?" + "voucher_id=" + voucher_id + "&voucherclass_id=" + voucherclass_id + "&vouchertype_id="
								+ vouchertype_id
								+ "&msg=Receipt Added Successfully!"));
					} else if (!msg.equals("")) {
						msg = "" + unescapehtml(msg);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void AddReceipt(HttpServletRequest request) {
		try {
			Receipt_Update receipt = new Receipt_Update();
			receipt.emp_branch_id = emp_branch_id;
			receipt.payment_id = payment_id;
			receipt.vouchertrans_paymode_id = vouchertrans_paymode_id;
			// Start axela_acc_voucher details
			receipt.emp_id = emp_id;
			receipt.voucher_tag_id = voucher_tag_id;
			receipt.voucher_no = voucher_no;
			receipt.voucher_branch_id = voucher_branch_id;
			receipt.voucherdate = DateToShortDate(kknow());
			receipt.voucher_narration = voucher_narration;// "Towards Payment Of SO ID: " + voucher_so_id;
			receipt.voucher_customer_id = voucher_customer_id;
			receipt.voucher_contact_id = voucher_contact_id;
			receipt.voucher_invoice_id = voucher_invoice_id;
			receipt.vouchertype_id = vouchertype_id;
			receipt.voucher_entry_type = voucher_entry_type;
			receipt.voucher_amount = voucher_amount;
			receipt.voucher_emp_id = emp_id;// 22
			receipt.voucher_so_id = voucher_so_id;// 40
			receipt.voucher_jc_id = voucher_jc_id;
			receipt.voucher_ref_no = voucher_ref_no;
			receipt.voucher_driver_no = voucher_driver_no;
			receipt.voucher_tempo_no = voucher_tempo_no;
			receipt.vouchertype_terms = vouchertype_terms;
			receipt.voucher_active = "1";
			receipt.voucher_notes = voucher_notes;
			receipt.voucher_entry_id = voucher_entry_id;
			receipt.voucher_entry_date = voucher_entry_date;
			receipt.comp_id = comp_id;
			// receipt.sotosi.comp_id = comp_id;
			// END axela_acc_voucher details

			// Start axela_acc_voucher_trans
			receipt.vouchertrans_voucher_id = vouchertrans_voucher_id;
			receipt.vouchertrans_customer_id = vouchertrans_customer_id;
			receipt.vouchertrans_amount = vouchertrans_amount;
			receipt.vouchertrans_item_id = vouchertrans_item_id;
			receipt.vouchertrans_paymode_id = vouchertrans_paymode_id;
			receipt.vouchertrans_cheque_no = vouchertrans_cheque_no;
			receipt.vouchertrans_chequedate = vouchertrans_chequedate;
			// receipt.vouchertrans_cheque_bank = vouchertrans_cheque_bank;
			receipt.vouchertrans_bank_id = vouchertrans_bank_id;
			receipt.vouchertrans_cheque_branch = vouchertrans_cheque_branch;
			receipt.vouchertrans_cheque_no = vouchertrans_cheque_no;
			receipt.vouchertrans_transaction_no = vouchertrans_transaction_no;
			// receipt.vouchertrans_reconciliation = vouchertrans_reconciliation;
			// End axela_acc_voucher_trans
			receipt.voucherbal_trans_id = voucherbal_trans_id;
			receipt.voucherbal_amount = voucherbal_amount;
			receipt.voucherclass_acc = voucherclass_acc;
			receipt.customer_type = customer_type;
			receipt.AddFields(request);
			msg = receipt.msg;
			voucher_id = receipt.voucher_id;
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (empEditperm.equals("1")) {
			voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		} else {
			voucherdate = PadQuotes(request.getParameter("txt_hid_voucher_date"));
		}
		vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
		voucher_customer_id = PadQuotes(request.getParameter("customer_id"));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		voucher_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		mainparty_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		voucher_prev_amount = PadQuotes(request.getParameter("txt_voucher_prev_amount"));
		customer_type = PadQuotes(request.getParameter("dr_voucher_payment"));
		payment_id = customer_type.substring(customer_type.indexOf("-") + 1, customer_type.length());
		vouchertrans_customer_id = customer_type.substring(0, customer_type.indexOf("-"));
		vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));
		if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no"));
			vouchertrans_chequedate = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date"));
			// vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
			vouchertrans_cheque_branch = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_card_no"));
			// vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_card_bank"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			// vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_bank"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			// vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_bank"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		}
		else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			// vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_bank"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		}
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_driver_no = PadQuotes(request.getParameter("txt_voucher_driver_no"));
		voucher_tempo_no = PadQuotes(request.getParameter("txt_voucher_tempo_no"));
		voucher_narration = PadQuotes(request.getParameter("txt_voucher_narration"));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		voucher_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_count"))));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		vouchertrans_amount = voucher_amount;
	}

	public String PopulatePayment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			Str.append("<option value='0-0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"),
						customer_type));
				Str.append(">").append(crs.getString("customer_name"))
						.append("</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = "
					+ CNumeric(customer_id)
					+ ""
					+ " GROUP BY contact_id" + " order by contact_fname";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count++;
				if (count == 1) {
					Str.append("<option selected value=").append(
							crs.getString("contact_id"));
				} else {
					Str.append("<option value=").append(
							crs.getString("contact_id"));
				}
				Str.append(
						StrSelectdrop(
								crs.getString("contact_id"),
								voucher_contact_id)).append(">");
				Str.append(crs.getString("contact_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}

	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM  " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"),
						contact_title_id));
				Str.append(">").append(crs.getString("title_desc"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulatePaymode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT paymode_id, paymode_name"
					+ " FROM axela_acc_paymode" + " WHERE 1=1"
					+ " GROUP BY paymode_id" + " ORDER BY paymode_id";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			// Str.append("<option value='1'>Cash</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"),
						vouchertrans_paymode_id));
				Str.append(">").append(crs.getString("paymode_name"))
						.append("</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	// Before adding Receipt it will check for the sails-invoice of that sails-order
	public void PopulateConfigDetails() {

		try {
			StrSql = "SELECT  voucherclass_acc, vouchertype_name, vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms,"
					+ " vouchertype_prefix, vouchertype_suffix, "
					+ " vouchertype_driver_no, vouchertype_tempo_no,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ compdb(comp_id) + "axela_comp,"
					+ compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=config details==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_prefix = crs.getString("vouchertype_prefix");
				vouchertype_suffix = crs.getString("vouchertype_suffix");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_auto = crs.getString("vouchertype_email_auto");
				// vouchertype_email_sub =
				// crs.getString("vouchertype_email_sub");
				// vouchertype_email_format =
				// crs.getString("vouchertype_email_format");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
				vouchertype_driver_no = crs.getString("vouchertype_driver_no");
				vouchertype_tempo_no = crs.getString("vouchertype_tempo_no");
				// vouchertype_sms_format =
				// crs.getString("vouchertype_sms_format");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				vouchertype_terms = crs.getString("vouchertype_terms");
				// SOP("branch_name==="+branch_name);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String checkinvoice(HttpServletRequest request, HttpServletResponse response) {
		String invoice_id = "0";
		try {
			StrSql = "SELECT"
					+ " voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1 = 1 AND voucher_so_id = " + voucher_so_id
					+ " AND voucher_vouchertype_id = '6'";
			invoice_id = CNumeric(ExecuteQuery(StrSql));
			if (invoice_id.equals("0")) {
				Veh_SoToSi sotosi = new Veh_SoToSi();
				sotosi.so_id = voucher_so_id;
				sotosi.addSingle = "yes";
				sotosi.allinvoice = "Add Invoive";
				sotosi.Listdata(comp_id, request, response);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return invoice_id;
	}

	public String PopulateFinanceBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT"
					+ " fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY"
					+ " fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id"));
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), vouchertrans_bank_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

}
