package axela.accounting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Contact_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String voucher_customer_id = "0", customer_id = "";

	public String supplier = "", customer = "", ledger = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				try {
					customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
					customer = PadQuotes(request.getParameter("customer"));
					supplier = PadQuotes(request.getParameter("supplier"));
					ledger = PadQuotes(request.getParameter("ledger"));
					if (!customer_id.equals("0")) {
						voucher_customer_id = ExecuteQuery("select customer_customer_id"
								+ " FROM  " + compdb(comp_id) + " axela_customer"
								+ " where customer_id = " + customer_id);
						if (customer.equals("yes")) {
							StrHTML = new Receipt_Update().PopulateContact(voucher_customer_id);
						} else if (supplier.equals("yes")) {
							StrHTML = new SO_Update().PopulateContact(voucher_customer_id);
						} else if (ledger.equals("yes")) {
							StrHTML = new Expense_Update().PopulateContact("0", voucher_customer_id);
						}
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
