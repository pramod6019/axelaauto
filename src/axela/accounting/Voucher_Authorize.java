package axela.accounting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_Authorize extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String StrSql = "";
	public String voucher_id = "0", voucher_jc_id = "0";
	public String vouchertype_id = "0", voucherclass_id = "0", voucherclass_file = "";
	public String voucher_authorize_time = "";
	public String voucher_authorizetime = "";
	public String voucher_authorize = "", old_voucher_authorize = "";
	public String voucher_no = "", voucher_no_check = "0";
	public String BranchAccess = "", ExeAccess = "";
	public String voucher_authorize_id = "0";
	public String voucher_authorized_by = "";
	public String vouchertype_authorize = "0", vouchertype_defaultauthorize = "0";
	public String comp_module_inventory = "0";
	public String config_inventory_current_stock = "0";
	public String vouchertype_affects_inventory = "0";
	public String voucher_location_id = "0";
	public String voucher_branch_id = "0";
	public String msg = "";
	public String voucher_active = "";
	public String contact_email1 = "", contact_email2 = "",
			contact_mobile1 = "", contact_mobile2 = "", emailto = "";

	public String brandconfig_receipt_authorize_sms_enable = "", brandconfig_receipt_authorize_email_enable = "", brandconfig_receipt_authorize_email_sub = "",
			brandconfig_receipt_authorize_email_format = "", brandconfig_receipt_authorize_sms_format = "";
	public String emailmsg = "", emailsub = "";

	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public int prepkey = 1;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
			vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
			voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
			PopulateConfigDetails();
			comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request) + "");
			config_inventory_current_stock = "1";
			String StrSql = "SELECT voucherclass_id , voucherclass_file"
					+ " FROM axela_acc_voucher_class"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_voucherclass_id = voucherclass_id"
					+ " WHERE vouchertype_id = ?";
			prepmap.put(prepkey++, Integer.parseInt(vouchertype_id));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			while (crs.next()) {
				if (crs.getString("voucherclass_id").equals("4")) {
					voucherclass_file = "sales_order";
				} else if (crs.getString("voucherclass_id").equals("5")) {
					voucherclass_file = "acc_quote";
				} else if (crs.getString("voucherclass_id").equals("6")) {
					voucherclass_file = "acc_sales_invoice";
				} else if (crs.getString("voucherclass_id").equals("7")) {
					voucherclass_file = "acc_bill";
				} else if (crs.getString("voucherclass_id").equals("9")) {
					voucherclass_file = "acc_receipt";
				} else if (crs.getString("voucherclass_id").equals("10")) {
					voucherclass_file = "acc_credit_note";
				} else if (crs.getString("voucherclass_id").equals("11")) {
					voucherclass_file = "acc_debit_note";
				} else if (crs.getString("voucherclass_id").equals("12")) {
					voucherclass_file = "acc_purchase_order";
				} else if (crs.getString("voucherclass_id").equals("15")) {
					voucherclass_file = "acc_payment";
				} else if (crs.getString("voucherclass_id").equals("16")) {
					voucherclass_file = "acc_expense";
				} else if (crs.getString("voucherclass_id").equals("18")) {
					voucherclass_file = "acc_journal";
				} else if (crs.getString("voucherclass_id").equals("19")) {
					voucherclass_file = "acc_contra";
				} else if (crs.getString("voucherclass_id").equals("20")) {
					voucherclass_file = "acc_grn";
				} else if (crs.getString("voucherclass_id").equals("21")) {
					voucherclass_file = "acc_purchase_invoice";
				} else if (crs.getString("voucherclass_id").equals("23")) {
					voucherclass_file = "acc_sales_return";
				} else if (crs.getString("voucherclass_id").equals("24")) {
					voucherclass_file = "acc_purchase_return";
				} else if (crs.getString("voucherclass_id").equals("25")) {
					voucherclass_file = "sales_order_delivery";
				} else {
					voucherclass_file = crs.getString("voucherclass_file").toLowerCase();
				}

			}
			crs.close();
			CheckPerm(comp_id, "emp_" + voucherclass_file + "_access", request, response);
			emp_id = CNumeric(GetSession("emp_id", request)) + "";
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			updateB = PadQuotes(request.getParameter("update_button"));
			voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
			voucher_jc_id = CNumeric(PadQuotes(request.getParameter("voucher_jc_id")));
			if (!"yes".equals(updateB)) {
				PopulateFields(request, response);
			} else if ("yes".equals(updateB)) {
				CheckPerm(comp_id, "emp_" + voucherclass_file + "_edit, emp_" + voucherclass_file + "_authorize", request, response);
				GetValues(request, response);
				if (voucher_authorize.equals("1") && CNumeric(voucher_no_check).equals("0")) {
					voucher_no = getVoucherNo(voucher_branch_id, vouchertype_id, "0", "0");
				} else {
					voucher_no = voucher_no_check;
				}

				UpdateFields(request, response);
				if (msg.equals("")) {
					if (!voucher_jc_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../service/jobcard-dash.jsp?jc_id=" + voucher_jc_id + "&msg=Voucher updated successfully!#tabs-13"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucherclass_id=" + voucherclass_id + "&vouchertype_id=" + vouchertype_id + "&voucher_id=" + voucher_id
								+ "&msg=Voucher updated successfully!"));
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
		voucher_authorize = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_authorize")));
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_branch_id")));
		voucher_no_check = CNumeric(PadQuotes(request.getParameter("txt_voucher_no_check")));
		old_voucher_authorize = PadQuotes(request.getParameter("old_voucher_authorize"));
		voucher_authorizetime = PadQuotes(request.getParameter("txt_voucher_authorizetime"));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		if (voucher_authorize_id.equals("0")) {
			voucher_authorize_id = emp_id;
			voucher_authorize_time = ToLongDate(kknow());
		} else {
			voucher_authorized_by = Exename(comp_id, Integer.parseInt(voucher_authorize_id));
			voucher_authorize_time = voucher_authorizetime;
		}
		voucher_active = CNumeric(PadQuotes(request.getParameter("txt_voucher_active")));
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_authorize_time, voucher_authorize, voucher_authorize_id, voucher_active, voucher_branch_id,"
					+ " COALESCE(voucher_no,'0') AS voucher_no_check,"
					+ " CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) as voucher_no"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = voucher_emp_id"
					+ " WHERE voucher_id = " + voucher_id
					+ BranchAccess
					+ ExeAccess;
			// SOP("StrSql==PopulateFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_no_check = crs.getString("voucher_no_check");
					voucher_branch_id = crs.getString("voucher_branch_id");
					voucher_active = CNumeric(crs.getString("voucher_active"));
					voucher_no = crs.getString("voucher_no");
					voucher_authorize = crs.getString("voucher_authorize");
					old_voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					if (!voucher_authorize_id.equals("")) {
						voucher_authorized_by = Exename(comp_id, Integer.parseInt(voucher_authorize_id));
					}
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_authorizetime = strToLongDate(voucher_authorize_time);
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Access denied. Please contact system administrator!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm(request);
		String voucher_emp_id = ExecuteQuery("SELECT voucher_authorize_id"
				+ " FROM " + compdb(comp_id) + " axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + " axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " WHERE vouchertype_id=" + vouchertype_id
				+ " AND voucher_id=" + voucher_id);
		// SOP("voucher_emp_id=====" + voucher_emp_id);
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + " axela_acc_voucher"
						+ " SET"
						+ " voucher_no = " + voucher_no + ","
						+ " voucher_authorize = " + voucher_authorize + "";
				if (voucher_emp_id.equals("0")) {
					StrSql += ", voucher_authorize_id = " + voucher_authorize_id + ","
							+ " voucher_authorize_time = '" + voucher_authorize_time + "'";
				}
				StrSql += " WHERE voucher_id = " + voucher_id;
				// SOP("update StrSql====" + StrSql);
				updateQuery(StrSql);

				if ((old_voucher_authorize.equals("") || old_voucher_authorize.equals("0")) && voucher_authorize.equals("1")
						&& vouchertype_id.equals("9")) {

					if (brandconfig_receipt_authorize_email_enable.equals("1")
							&& !brandconfig_receipt_authorize_email_sub.equals("")
							&& !brandconfig_receipt_authorize_email_format.equals("")
							&& (!contact_email1.equals("") || !contact_email2.equals(""))) {
						SendEmail();
					}
					if (brandconfig_receipt_authorize_sms_enable.equals("1") && !brandconfig_receipt_authorize_sms_format.equals("")) {
						if (!contact_mobile1.equals("")) {
							SendSMS(contact_mobile1);
						}

						if (!contact_mobile2.equals("")) {
							SendSMS(contact_mobile2);
						}
					}
				}

				// SOP("comp_module_inventory==" + comp_module_inventory);
				// SOP("config_inventory_current_stock==" + config_inventory_current_stock);
				// SOP("vouchertype_affects_inventory==" + vouchertype_affects_inventory);
				// SOP("vouchertype_id==" + vouchertype_id);
				// SOP("voucher_authorize==" + voucher_authorize);
				// SOP("voucher_location_id==" + voucher_location_id);
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& ((vouchertype_id.equals("6") && voucher_authorize.equals("1")))) {
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("0", voucher_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
				}

			} catch (Exception e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}

		}
	}
	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " vouchertype_authorize, vouchertype_defaultauthorize, voucher_branch_id,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
					+ " COALESCE(brandconfig_receipt_authorize_email_enable, '') AS brandconfig_receipt_authorize_email_enable,"
					+ " COALESCE(brandconfig_receipt_authorize_email_sub, '') AS brandconfig_receipt_authorize_email_sub,"
					+ " COALESCE(brandconfig_receipt_authorize_email_format, '') AS brandconfig_receipt_authorize_email_format,"
					+ " COALESCE(brandconfig_receipt_authorize_sms_enable, '') AS brandconfig_receipt_authorize_sms_enable,"
					+ " COALESCE(brandconfig_receipt_authorize_sms_format, '') AS brandconfig_receipt_authorize_sms_format,"
					+ " branch_email1, branch_mobile1,"
					+ "	vouchertype_affects_inventory, voucher_location_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = voucher_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " INNER JOIN axela_brand on brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config on brandconfig_brand_id = brand_id,"
					+ compdb(comp_id) + "axela_config, "
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE"
					+ " voucher_id = " + voucher_id + "";

			// SOP("StrSql=config details==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucher_branch_id = CNumeric(PadQuotes(crs.getString("voucher_branch_id")));
				vouchertype_authorize = CNumeric(PadQuotes(crs.getString("contact_email1")));
				vouchertype_defaultauthorize = CNumeric(PadQuotes(crs.getString("vouchertype_defaultauthorize")));
				contact_email1 = PadQuotes(crs.getString("contact_email1"));
				contact_email2 = PadQuotes(crs.getString("contact_email2"));
				contact_mobile1 = PadQuotes(crs.getString("contact_mobile1"));
				contact_mobile2 = PadQuotes(crs.getString("contact_mobile2"));

				if (!contact_email1.equals(""))
					emailto += contact_email1;
				if (!contact_email2.equals(""))
					emailto += "," + contact_email2;

				brandconfig_receipt_authorize_email_enable = PadQuotes(crs.getString("brandconfig_receipt_authorize_email_enable"));
				brandconfig_receipt_authorize_sms_enable = PadQuotes(crs.getString("brandconfig_receipt_authorize_sms_enable"));
				brandconfig_receipt_authorize_email_sub = PadQuotes(crs.getString("brandconfig_receipt_authorize_email_sub"));
				brandconfig_receipt_authorize_email_format = PadQuotes(crs.getString("brandconfig_receipt_authorize_email_format"));
				brandconfig_receipt_authorize_sms_format = PadQuotes(crs.getString("brandconfig_receipt_authorize_sms_format"));

				vouchertype_affects_inventory = CNumeric(crs.getString("vouchertype_affects_inventory"));
				voucher_location_id = CNumeric(crs.getString("voucher_location_id"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	protected void CheckForm(HttpServletRequest request) {
		// try {
		// StrSql = "SELECT voucher_active"
		// + " FROM " + compdb(comp_id) + "axela_acc_voucher"
		// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
		// + " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
		// + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = voucher_emp_id"
		// + " WHERE voucher_id = " + voucher_id + BranchAccess + ExeAccess;
		// // SOP("StrSql==check form==" + StrSql);
		// CachedRowSet crs = processQuery(StrSql, 0);
		// if (crs.isBeforeFirst()) {
		// while (crs.next()) {
		// voucher_active = crs.getString("voucher_active");
		// }
		// }
		// crs.close();
		// } catch (Exception ex) {
		// SOPError("Axelaauto===" + this.getClass().getName());
		// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// / }
		msg = "";
		if (voucher_active.equals("0")) {
			msg += "<br>Voucher is Inactive!";
			voucher_authorize = "0";
		}
	}

	protected void SendEmail() {
		emailmsg = brandconfig_receipt_authorize_email_format;
		emailsub = brandconfig_receipt_authorize_email_sub;
		emailmsg = "replace('" + emailmsg + "','[RECEIPTID]', voucher_id)";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		emailmsg = "replace(" + emailmsg + ",'[INVOICEID]', voucher_invoice_id)";
		emailmsg = "replace(" + emailmsg + ",'[PAYMENTTYPE]', COALESCE(paymode_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[SOID]', COALESCE(so_id,''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]',  COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]', COALESCE(model_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHER]', vouchertype_name)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]', customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		emailmsg = "replace(" + emailmsg + ",'[AMOUNT]', ROUND(voucher_amount, 2))";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		emailsub = "replace('" + emailsub + "','[RECEIPTID]', voucher_id)";
		emailsub = "replace(" + emailsub + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		emailsub = "replace(" + emailsub + ",'[INVOICEID]', voucher_invoice_id)";
		emailsub = "replace(" + emailsub + ",'[PAYMENTTYPE]',  COALESCE(paymode_name,''))";
		emailsub = "replace(" + emailsub + ",'[SOID]', COALESCE(so_id,''))";
		emailsub = "replace(" + emailsub + ",'[MODELNAME]', COALESCE(model_name,''))";
		emailsub = "replace(" + emailsub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		emailsub = "replace(" + emailsub + ",'[VOUCHER]', vouchertype_name)";
		emailsub = "replace(" + emailsub + ",'[CUSTOMERID]', customer_id)";
		emailsub = "replace(" + emailsub + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		emailsub = "replace(" + emailsub + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		emailsub = "replace(" + emailsub + ",'[AMOUNT]', ROUND(voucher_amount, 2))";
		emailsub = "replace(" + emailsub + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		emailsub = "replace(" + emailsub + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		emailsub = "replace(" + emailsub + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		emailsub = "replace(" + emailsub + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		emailsub = "replace(" + emailsub + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		emailsub = "replace(" + emailsub + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		emailsub = "replace(" + emailsub + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		try {
			StrSql = "SELECT"
					+ " branch_id,"
					+ " voucher_contact_id,"
					+ " emp_id,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " branch_email1,"
					+ " '" + emailto + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " " + ToLongDate(kknow()) + ","
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE voucher_id = " + voucher_id + ""
					+ " GROUP BY voucher_id";

			// SOP("StrSql--Email-----1-----" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_emp_id, "
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql---Email----2-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	protected void SendSMS(String tomobile_no) {
		String smsmsg = (brandconfig_receipt_authorize_sms_format);

		smsmsg = "replace('" + smsmsg + "','[RECEIPTID]', voucher_id)";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		smsmsg = "replace(" + smsmsg + ",'[INVOICEID]', voucher_invoice_id)";
		smsmsg = "replace(" + smsmsg + ",'[SOID]', COALESCE(so_id,''))";
		smsmsg = "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]', COALESCE(model_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHER]', vouchertype_name)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]', customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		smsmsg = "replace(" + smsmsg + ",'[PAYMENTTYPE]', COALESCE(paymode_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		smsmsg = "replace(" + smsmsg + ",'[AMOUNT]', ROUND(voucher_amount, 2))";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		StrSql = "SELECT"
				+ " branch_id,"
				+ " voucher_contact_id,"
				+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
				+ " emp_name,"
				+ " '" + tomobile_no + "',"
				+ " " + smsmsg + ","
				+ " " + ToLongDate(kknow()) + ","
				+ " 0,"
				+ " " + emp_id + ""
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE voucher_id = " + voucher_id + ""
				+ " GROUP BY voucher_id";
		// SOP("StrSql====" + StrSql);
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
				+ " (sms_branch_id,"
				+ " sms_contact_id,"
				+ " sms_contact,"
				+ " sms_emp,"
				+ " sms_mobileno,"
				+ " sms_msg,"
				+ " sms_date,"
				+ " sms_sent,"
				+ " sms_entry_id)"
				+ " " + StrSql + "";

		// SOP("StrSql====" + StrSql);

		updateQuery(StrSql);

	}

	public String getVoucherNo(String branch_id, String vouchertype_id, String voucher_no, String voucher_id) {
		StrSql = "SELECT COALESCE(MAX(voucher_no),0)+1"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE 1=1"
				+ " AND voucher_branch_id = " + branch_id
				+ " AND voucher_vouchertype_id = " + vouchertype_id;

		if (!voucher_no.equals("0")) {
			StrSql += " AND voucher_no = " + voucher_no;
		}
		if (!voucher_id.equals("0")) {
			StrSql += " AND voucher_id != " + voucher_id;
		}
		// SOP("getVoucherNo==StrSql===" + StrSql);
		StrSql = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
		return StrSql.toString();
	}
}
