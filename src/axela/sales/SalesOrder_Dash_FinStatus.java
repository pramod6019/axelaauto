package axela.sales;
//Saiman 11th Feb 2013
//sangita 15th july 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Dash_FinStatus extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String so_id = "0";
	public String finnancetrans_desc = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String so_date = "", enquiry_enquirytype_id = "0";
	public String so_desc = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String quote_link = "";
	public String enquiry_link = "";
	public String msg = "";
	public String so_fintype_id = "";
	public String so_finstatus_id = "";
	public String so_finstatus_date = "";
	public String so_finstatus_emp_id = "";
	public String so_finstatus_loan_amt = "", so_finstatus_tenure = "";
	public String so_finstatus_bank = "", so_finstatus_scheme = "", so_finstatus_desc = "";
	public String so_finstatus_mode = "", so_finstatus_subvention = "";
	public String so_finstatus_payout_rate = "", so_finstatus_payback = "", so_finstatus_netincome = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				StrSql = "SELECT so_id, so_desc FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
						so_desc = crs.getString("so_desc");
					}
					PopulateSOFinStatusDetails();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		so_fintype_id = PadQuotes(request.getParameter("dr_so_fintype_id"));
		so_finstatus_id = PadQuotes(request.getParameter("dr_so_finstatus_id"));
		finnancetrans_desc = PadQuotes(request.getParameter("txt_finnancetrans_desc"));
	}

	public void PopulateSOFinStatusDetails() {
		try {
			StrSql = "SELECT so_date, customer_id, customer_name, contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name, so_enquiry_id, so_quote_id,"
					// + " enquiry_enquirytype_id,"
					+ " so_fintype_id, so_finstatus_id, so_finstatus_date,"
					+ " so_finstatus_emp_id,"
					// + " so_finstatus_loan_amt, "
					// + " so_finstatus_desc, so_finstatus_tenure,"
					// + " so_finstatus_bank, so_finstatus_scheme, so_finstatus_mode, so_finstatus_subvention,"
					// + " so_finstatus_payout_rate, so_finstatus_payback,
					// + " so_finstatus_netincome,"
					+ "  so_desc"
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
					+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_date = strToShortDate(crs.getString("so_date"));
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contact_name") + "</a>";
					enquiry_link = "<a href=\"../sales/enquiry-list.jsp?enquiry_id=" + crs.getString("so_enquiry_id") + "\">" + crs.getString("so_enquiry_id") + "</a>";
					quote_link = "<a href=\"../sales/veh-quote-list.jsp?quote_id=" + crs.getString("so_quote_id") + "\">" + crs.getString("so_quote_id") + "</a>";
					// enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");

					so_desc = crs.getString("so_desc");
					so_fintype_id = crs.getString("so_fintype_id");
					so_finstatus_id = crs.getString("so_finstatus_id");
					so_finstatus_date = crs.getString("so_finstatus_date");
					so_finstatus_emp_id = crs.getString("so_finstatus_emp_id");
					// so_finstatus_desc = crs.getString("so_finstatus_desc");
					// so_finstatus_loan_amt = crs.getString("so_finstatus_loan_amt");
					// so_finstatus_tenure = crs.getString("so_finstatus_tenure");
					// so_finstatus_bank = crs.getString("so_finstatus_bank");
					// so_finstatus_scheme = crs.getString("so_finstatus_scheme");
					// so_finstatus_mode = crs.getString("so_finstatus_mode");
					// so_finstatus_subvention = crs.getString("so_finstatus_subvention");
					// so_finstatus_payout_rate = crs.getString("so_finstatus_payout_rate");
					// so_finstatus_payback = crs.getString("so_finstatus_payback");
					// so_finstatus_netincome = crs.getString("so_finstatus_netincome");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFinanceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fintype_id, fintype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
					+ " GROUP BY fintype_id"
					+ " ORDER BY fintype_id";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fintype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fintype_id"), so_fintype_id));
				Str.append(">").append(crs.getString("fintype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT finstatus_id, finstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_status"
					+ " WHERE finstatus_active = '1'"
					+ " GROUP BY finstatus_id"
					+ " ORDER BY finstatus_id";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("finstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("finstatus_id"), so_finstatus_id));
				Str.append(">").append(crs.getString("finstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
