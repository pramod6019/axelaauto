package axela.accounting;
//aJIt 11th January, 2013
// JEET 19th NOV 2014

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_Email extends Connect {

	public String emp_id = "0", comp_id = "0";
	public String StrSql = "";

	public String voucher_id = "0";
	public String voucher_no = "";
	public String voucherclass_id = "0";
	public String vouchertype_name = "";
	public String comp_email_enable = "1", attachment = "";
	public String customer_id = "0", customer_name = "";
	public String vouchertype_email_enable = "0";
	public String vouchertype_email_sub = "";
	public String vouchertype_email_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "0";
	public String contact_name = "";
	public String contact = "";
	public String contact_email1 = "";
	public String cc = "";
	public String bcc = "";
	public String send_contact_email = "";
	public String msg = "", sendB = "";

	public String BranchAccess = "";
	public String ExeAccess = "";
	Map<Integer, Object> prepmap = new HashMap<>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				sendB = PadQuotes(request.getParameter("sendB"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));

				if (!voucher_id.equals("0")) {
					PopulateFields();
					if (sendB.equals("yes")) {
						GetValues(request, response);
						CheckForm(request);

						if (msg.equals("")) {

							if (comp_email_enable.equals("1")
									&& config_email_enable.equals("1")
									&& !config_admin_email.equals("")) {

								if (!contact_email1.equals("")
										&& vouchertype_email_enable.equals("1")
										&& !vouchertype_email_sub.equals("")
										&& !vouchertype_email_format.equals("")) {

									attachment = CachePath(comp_id) + vouchertype_name + "_" + voucher_no + ".pdf";
									// // For Quote
									if (voucherclass_id.equals("101")) {
										SO_Print_PDF PrintPdf = new SO_Print_PDF();
										PrintPdf.voucherclass_id = voucherclass_id;
										PrintPdf.SalesOrderDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");

									}
									// invoice
									else if (voucherclass_id.equals("102")) {
										SO_Print_PDF PrintPdf = new SO_Print_PDF();
										PrintPdf.voucherclass_id = voucherclass_id;
										PrintPdf.SalesOrderDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");

									}
									// bill
									// else if (voucherclass_id.equals("103")) {
									// ItemDetails_Print_PDF PrintPdf = new ItemDetails_Print_PDF();
									// PrintPdf.BillDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");
									//
									// }
									// request receipt
									else if (voucherclass_id.equals("105")) {
										Receipt_Print_PDF PrintPdf = new Receipt_Print_PDF();
										PrintPdf.ReceiptDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");

									}
									// po
									else if (voucherclass_id.equals("108")) {
										PO_Print_PDF PrintPdf = new PO_Print_PDF();
										PrintPdf.voucherclass_id = voucherclass_id;
										PrintPdf.PurchaseOrderDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");

									}
									// payment
									else if (voucherclass_id.equals("109")) {
										Payment_Print_PDF PrintPdf = new Payment_Print_PDF();
										PrintPdf.PaymentDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");

									}// so
									else if (voucherclass_id.equals("114")) {
										SO_Print_PDF PrintPdf = new SO_Print_PDF();
										PrintPdf.voucherclass_id = voucherclass_id;
										PrintPdf.SalesOrderDetails(request, response, voucher_id, BranchAccess, ExeAccess, "file");
									}
									SendEmail(comp_id);
									response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=Email sucessfully sent!");

								} else {
									response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=Email not sent!");
								}
							} else {
								response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=Email not sent1!");
							}
						} else {
							msg = "Error!" + msg;
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
		cc = PadQuotes(request.getParameter("txt_cc"));
		bcc = PadQuotes(request.getParameter("txt_bcc"));
	}

	protected void SendEmail(String comp_id) throws SQLException {
		String msg, sub;
		msg = (vouchertype_email_format);
		sub = (vouchertype_email_sub);
		if (!msg.equals("") && !sub.equals("") && !send_contact_email.equals("")) {

			sub = "replace('" + sub + "','[VOUCHERID]', voucher_id)";
			sub = "replace(" + sub + ",'[VOUCHERNO]',  CONCAT(vouchertype_prefix, ' ', voucher_no,' ', vouchertype_suffix))";
			sub = "replace(" + sub + ",'[VOUCHER]', vouchertype_name)";
			sub = "replace(" + sub + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
			sub = "replace(" + sub + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
			sub = "replace(" + sub + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
			sub = "replace(" + sub + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
			sub = "replace(" + sub + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
			sub = "replace(" + sub + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
			sub = "replace(" + sub + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
			sub = "replace(" + sub + ",'[AMOUNT]',  ROUND(voucher_amount, 2))";
			sub = "replace(" + sub + ",'[EXENAME]',  COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
			sub = "replace(" + sub + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
			sub = "replace(" + sub + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";

			msg = "replace('" + msg + "','[VOUCHERID]', voucher_id)";
			msg = "replace(" + msg + ",'[VOUCHERNO]',  CONCAT(vouchertype_prefix, ' ', voucher_no,' ', vouchertype_suffix))";
			msg = "replace(" + msg + ",'[VOUCHER]', vouchertype_name)";
			msg = "replace(" + msg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
			msg = "replace(" + msg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
			msg = "replace(" + msg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
			msg = "replace(" + msg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
			msg = "replace(" + msg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
			msg = "replace(" + msg + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
			msg = "replace(" + msg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
			msg = "replace(" + msg + ",'[AMOUNT]',  ROUND(voucher_amount, 2))";
			msg = "replace(" + msg + ",'[EXENAME]',  COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
			msg = "replace(" + msg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
			msg = "replace(" + msg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";

			StrSql = "SELECT"
					+ " voucher_contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + config_admin_email + "',"
					+ " '" + send_contact_email + "',"
					+ " '" + cc + "',"
					+ " '" + bcc + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "," + vouchertype_name + "_" + voucher_no + ".pdf',"
					+ " " + ToLongDate(kknow()) + ","
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE voucher_id = " + voucher_id + ""
					+ " GROUP BY voucher_id";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_bcc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_attach1,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";

			updateQuery(StrSql);
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT vouchertype_voucherclass_id, vouchertype_name,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " vouchertype_email_enable, vouchertype_email_sub, vouchertype_email_format,"
					+ " COALESCE(customer_id,'0') AS customer_id, COALESCE(customer_name,'') AS customer_name,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),'') AS contact_name,"
					+ " COALESCE(contact_email1,'') AS contact_email1,"
					+ " COALESCE(contact_email2,'') AS contact_email2,"
					+ " config_admin_email, config_email_enable, comp_email_enable "
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id ,"
					+ "  " + compdb(comp_id) + "axela_config,"
					+ "  " + compdb(comp_id) + "axela_comp "
					+ " WHERE voucher_active = '1' AND vouchertype_active = '1'"
					+ " AND voucher_id = " + voucher_id + ""
					+ " GROUP BY voucher_id";

			prepmap.clear();
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			while (crs.next()) {
				voucherclass_id = crs.getString("vouchertype_voucherclass_id");
				vouchertype_name = crs.getString("vouchertype_name");
				voucher_no = "<a href='voucher-list.jsp?voucher_id=" + voucher_id + "'>" + crs.getString("voucher_no") + "</a>";
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_sub = crs.getString("vouchertype_email_sub");
				vouchertype_email_format = crs.getString("vouchertype_email_format");
				if (!crs.getString("customer_name").equals("")) {
					customer_name = "<a href='ledger-list.jsp?customer_id=" + crs.getString("customer_id") + "'>" + crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")</a>";
				}
				contact_name = crs.getString("contact_name");
				contact_email1 = crs.getString("contact_email1");
				if (!crs.getString("contact_email2").equals("")) {
					contact_email1 = contact_email1.concat(", " + crs.getString("contact_email2"));
				}
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";

		if (!cc.equals("")) {
			String strcc[] = cc.split(",");
			for (int i = 0; i < strcc.length; i++) {
				strcc[i] = strcc[i].replace(" ", "");
				if (!IsValidEmail(strcc[i])) {
					msg = "<br>Enter Valid Email CC!";
				}
			}
			msg = msg;
		}

		if (!bcc.equals("")) {
			String strbcc[] = bcc.split(",");
			for (int i = 0; i < strbcc.length; i++) {
				strbcc[i] = strbcc[i].replace(" ", "");
				if (!IsValidEmail(strbcc[i])) {
					msg = "<br>Enter Valid Email BCC!";
				}
			}
			msg = msg;
		}

	}
}
