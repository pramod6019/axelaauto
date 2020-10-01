package axela.accounting;

// Saiman on 24th april 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Customer_Followup extends Connect {

	public String customer_id = "0";
	public String voucher_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String salesvouchertstaus = "";
	public String purchasevouchertstaus = "";
	public String customerfollowup = "";
	public String vouchertype_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				customer_id = CNumeric(PadQuotes(request
						.getParameter("customer_id")));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				purchasevouchertstaus = PadQuotes(request.getParameter("purchasevouchertstaus"));
				salesvouchertstaus = PadQuotes(request.getParameter("salesvouchertstaus"));
				customerfollowup = PadQuotes(request.getParameter("customerfollowup"));
				// SOP("voucher_id---"+voucher_id);
				// SOP("salesvouchertstaus---"+salesvouchertstaus);
				// SOP("purchasevouchertstaus---"+purchasevouchertstaus);
				//
				if (!voucher_id.equals("0") && customerfollowup.equals("yes")) {
					StrHTML = ListFollowup();
				} else if (!voucher_id.equals("0") && purchasevouchertstaus.equals("yes")) {
					StrHTML = ListPurchaseVoucherStatus();
				} else if (!voucher_id.equals("0") && salesvouchertstaus.equals("yes")) {
					StrHTML = ListSalesVoucherStatus();
				}
			} else {
				StrHTML = "SignIn";
			}
			// SOP("StrHTML = " + StrHTML);
		}
	}
	public String ListFollowup() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT distinct vouchertrans_customer_id, customer_name, customer_code "
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " WHERE 1=1"
					+ " AND vouchertrans_voucher_id=" + voucher_id
					+ " AND vouchertrans_customer_id !=" + customer_id
					+ " GROUP BY vouchertrans_customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql--cf--"+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append(crs.getString("customer_name"));
				Str.append(" (");
				Str.append(crs.getString("vouchertrans_customer_id")).append(") <br>");
				// Str.append(crs.getString("followup_desc")).append("<br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String ListPurchaseVoucherStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT "
					+ " COALESCE((select COUNT(git.voucher_id)"
					+ " FROM axela_acc_voucher git"
					+ " WHERE git.voucher_po_id = main.voucher_id AND git.voucher_vouchertype_id = 10),0) AS count_git,"
					+ " COALESCE((select COUNT(grn.voucher_id)"
					+ " FROM axela_acc_voucher grn "
					+ " WHERE main.voucher_id IN(grn.voucher_so_id, grn.voucher_git_id) AND grn.voucher_vouchertype_id = 1),0) AS count_grn,"
					+ " COALESCE((select COUNT(pi.voucher_id)"
					+ " FROM axela_acc_voucher pi "
					+ " WHERE main.voucher_id IN(pi.voucher_so_id, pi.voucher_git_id, pi.voucher_grn_id) AND pi.voucher_vouchertype_id = 115),0) AS count_pi,"
					+ " COALESCE((select COUNT(pr.voucher_id)"
					+ " FROM axela_acc_voucher pr"
					+ " WHERE main.voucher_id IN(pr.voucher_so_id, pr.voucher_git_id, pr.voucher_grn_id) AND pr.voucher_vouchertype_id = 117),0) AS count_pr"
					+ " FROM axela_acc_voucher main"
					+ " WHERE voucher_id =" + voucher_id;
			// SOP("StrSql--vs1--"+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (vouchertype_id.equals("108") || vouchertype_id.equals("10") || vouchertype_id.equals("1") || vouchertype_id.equals("115") || vouchertype_id.equals("117")) {
					if (vouchertype_id.equals("108")) {
						Str.append("<br>").append("GIT: " + crs.getString("count_git"));
					}
					if (vouchertype_id.equals("108") || vouchertype_id.equals("10")) {
						Str.append("<br>").append("GRN: " + crs.getString("count_grn"));
					}
					if (vouchertype_id.equals("108") || vouchertype_id.equals("10") || vouchertype_id.equals("1")) {
						Str.append("<br>").append("PI: " + crs.getString("count_pi"));
						Str.append("<br>").append("PR: " + crs.getString("count_pr"));
					}
				}
				// Str.append(crs.getString("followup_desc")).append("<br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
	public String ListSalesVoucherStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT "
					+ " COALESCE((select COUNT(delnote.voucher_id)"
					+ " FROM axela_acc_voucher delnote"
					+ " WHERE delnote.voucher_so_id = main.voucher_id AND delnote.voucher_vouchertype_id = 3),0) AS count_delnote,"
					+ " COALESCE((select COUNT(si.voucher_id)"
					+ " FROM axela_acc_voucher si "
					+ " WHERE main.voucher_id IN(si.voucher_so_id, si.voucher_delnote_id) AND si.voucher_vouchertype_id = 102),0) AS count_si,"
					+ " COALESCE((select COUNT(sr.voucher_id)"
					+ " FROM axela_acc_voucher sr"
					+ " WHERE main.voucher_id IN(sr.voucher_so_id, sr.voucher_delnote_id) AND sr.voucher_vouchertype_id = 116),0) AS count_sr"
					+ " FROM axela_acc_voucher main"
					+ " WHERE voucher_id =" + voucher_id;
			// SOP("StrSql--vs--"+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (vouchertype_id.equals("114") || vouchertype_id.equals("3") || vouchertype_id.equals("102") || vouchertype_id.equals("117")) {
					if (vouchertype_id.equals("114")) {
						Str.append("<br>").append("DELNOTE: " + crs.getString("count_delnote"));
					}
					if (vouchertype_id.equals("114") || vouchertype_id.equals("3")) {
						Str.append("<br>").append("SI: " + crs.getString("count_si"));
						Str.append("<br>").append("SR: " + crs.getString("count_sr"));
					}
				}
				// Str.append(crs.getString("followup_desc")).append("<br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
