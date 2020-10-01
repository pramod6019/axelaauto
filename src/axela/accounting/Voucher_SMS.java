package axela.accounting;
//aJIt 11th January, 2013
// JEET 20th NOV 2014

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_SMS extends Connect {

	public String emp_id = "0", comp_id = "0";
	public String StrSql = "";

	public String voucher_id = "0";
	public String comp_sms_enable = "1";
	public String vouchertype_sms_enable = "0";
	public String vouchertype_sms_format = "";
	public String config_sms_enable = "0";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String msg = "", sendB = "";
	public String contact_name = "";
	Map<Integer, Object> prepmap = new HashMap<>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				sendB = PadQuotes(request.getParameter("sendB"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));

				if (!voucher_id.equals("0")) {
					PopulateFields();
					if (sendB.equals("yes")) {
						if (msg.equals("")) {

							if (comp_sms_enable.equals("1")
									&& config_sms_enable.equals("1")) {
								if (!vouchertype_sms_format.equals("")) {
									if (!contact_mobile1.equals("")) {
										SendSMS(contact_mobile1, comp_id);
										response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=SMS Sucessfully sent!");
									}
									if (!contact_mobile2.equals("")) {
										SendSMS(contact_mobile2, comp_id);
										response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=SMS Sucessfully sent!");
									}
								} else {
									response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=SMS not sent!");
								}
							} else {
								response.sendRedirect("voucher-list.jsp?voucher_id=" + voucher_id + "&msg=SMS not sent!");
							}
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

	protected void SendSMS(String contact_mobile, String comp_id) throws SQLException {
		String msg;
		msg = (vouchertype_sms_format);

		msg = "replace('" + msg + "','[VOUCHERID]', voucher_id)";
		msg = "replace(" + msg + ",'[VOUCHERNO]',  CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		msg = "replace(" + msg + ",'[VOUCHER]', vouchertype_name)";
		msg = "replace(" + msg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		msg = "replace(" + msg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		msg = "replace(" + msg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		msg = "replace(" + msg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		msg = "replace(" + msg + ",'[CONTACTEMAIL1]', COALESCE(contact_mobile1,''))";
		msg = "replace(" + msg + ",'[CONTACTEMAIL2]', COALESCE(contact_mobile2,''))";
		msg = "replace(" + msg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		msg = "replace(" + msg + ",'[AMOUNT]', ROUND(voucher_amount, 2))";
		msg = "replace(" + msg + ",'[EXENAME]',  COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		msg = "replace(" + msg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		msg = "replace(" + msg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";

		StrSql = "SELECT"
				+ " voucher_contact_id,"
				+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
				+ " emp_name,"
				+ " '" + contact_mobile + "',"
				+ " " + msg + ","
				+ " " + ToLongDate(kknow()) + ","
				+ " 1,"
				+ " " + emp_id + ""
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE voucher_id = " + voucher_id + ""
				+ " GROUP BY voucher_id";
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
				+ " (sms_contact_id,"
				+ " sms_contact,"
				+ " sms_emp,"
				+ " sms_mobileno,"
				+ " sms_msg,"
				+ " sms_date,"
				+ " sms_sent,"
				+ " sms_entry_id)"
				+ " " + StrSql + "";
		updateQuery(StrSql);

	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT contact_mobile1, contact_mobile2,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),'') AS contact_name ,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					+ " config_sms_enable, comp_sms_enable "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id ,"
					+ " " + compdb(comp_id) + "axela_config ,"
					+ " " + compdb(comp_id) + "axela_comp "
					+ " WHERE voucher_id = " + voucher_id;
			prepmap.clear();
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			while (crs.next()) {
				contact_name = crs.getString("contact_name");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_mobile2 = crs.getString("contact_mobile2");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_format = crs.getString("vouchertype_sms_format");
				config_sms_enable = crs.getString("config_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
