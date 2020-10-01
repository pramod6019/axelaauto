package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import axela.accounting.SO_Update;
import cloudify.connect.Connect;

public class AddAllInvoiceThread extends Connect implements Runnable {

	private String voucher_id, customer_id = "0";
	private String comp_id, adddirect, add, voucher_customer_id;
	private String contact_id, voucher_contact_id, voucher_rateclass_id;
	private String voucher_so_id, so_branch_id;
	private String branch_id, voucher_downpayment;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public SO_Update soupdate = new SO_Update();

	AddAllInvoiceThread(
			String comp_id, String adddirect, String add,
			String voucher_customer_id, String contact_id,
			String voucher_contact_id, String voucher_rateclass_id,
			String voucher_so_id, String so_branch_id, String branch_id,
			String voucher_downpayment, HttpServletRequest request,
			HttpServletResponse response) {

		this.comp_id = comp_id;
		this.adddirect = adddirect;
		this.add = add;
		this.voucher_customer_id = voucher_customer_id;
		this.contact_id = contact_id;
		this.voucher_contact_id = voucher_contact_id;
		this.voucher_rateclass_id = voucher_rateclass_id;
		this.voucher_so_id = voucher_so_id;
		this.so_branch_id = so_branch_id;
		this.voucher_downpayment = voucher_downpayment;
		this.request = request;
		this.response = response;
		this.branch_id = branch_id;
	}

	public void run() {
		AddMultipleInvoice();
	}

	synchronized void AddMultipleInvoice() {
		try {
			soupdate.comp_id = comp_id;
			soupdate.adddirect = "1";
			soupdate.add = "yes";
			soupdate.vouchertype_id = "6";
			soupdate.voucherclass_id = "6";
			soupdate.voucher_customer_id = voucher_customer_id;
			soupdate.contact_id = contact_id;
			soupdate.voucher_contact_id = voucher_contact_id;
			soupdate.voucher_rateclass_id = voucher_rateclass_id;
			soupdate.voucher_so_id = voucher_so_id;
			soupdate.so_branch_id = so_branch_id;
			soupdate.branch_id = branch_id;
			soupdate.voucher_downpayment = voucher_downpayment;
			soupdate.voucher_cashdiscount = "0";
			soupdate.voucher_turnoverdisc = "0";

			soupdate.doPost(request, response);

			soupdate.addB = "yes";

			soupdate.doPost(request, response);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

}
