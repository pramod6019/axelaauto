package axela.accounting;

// shivaprasad 8 oct 2014       

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class All_Invoice extends Connect {

	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String emp_id = "0";
	public String RecCountDisplay = "";
	public String customer_id = "0";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				if (!GetSession("emp_id", request).equals("")) {
					emp_id = CNumeric(GetSession("emp_id", request));
					customer_id = CNumeric(PadQuotes(request.getParameter("customerid")));
					// comp_id = CNumeric(PadQuotes(request.getParameter("companyid")));
					StrHTML = Listdata();

				} else {
					StrHTML = "SignIn";
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		if (!customer_id.equals("0")) {
			StrSearch += " AND Voucher_customer_id =" + customer_id;
		}
		if (!comp_id.equals("0")) {
			// StrSearch += " AND branch_company_id = " + comp_id;
		}

		StrSql = "SELECT voucher_id, CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
				+ " voucher_date, voucher_amount, voucher_ref_no, voucher_authorize,"
				+ " branch_name,"
				+ " vouchertype_name,"
				+ " rateclass_name"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_rate_class ON rateclass_id = voucher_rateclass_id	"
				+ " WHERE 1=1"
				+ StrSearch
				+ " AND voucher_vouchertype_id IN (6, 21)"
				+ " AND voucher_active = 1"
				+ " GROUP BY voucher_id"
				+ " ORDER BY voucher_id";
		try {
			// SOP("StrSql==ls==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Voucher</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th>Branch Class</th>\n");
				Str.append("</tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("voucher_id")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("voucher_no")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("vouchertype_name"));
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_date"))).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("voucher_ref_no")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("voucher_amount"))).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("branch_name")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("rateclass_name")).append("</td>\n");
					Str.append("</tr>\n");
				}

				Str.append("</table>\n");
			} else {
				RecCountDisplay = "<br><br><br><br><font color='red'><b>No Record Found!</b></font><br><br>";
				return RecCountDisplay;
			}
			crs.close();
		} catch (Exception ex) {

			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}
}
