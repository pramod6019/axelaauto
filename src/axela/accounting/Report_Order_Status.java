package axela.accounting;

/*saiman 28th june 2012 */

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Order_Status extends Connect {

	public String comp_id = "0";
	public String emp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String StrHTML = "";
	public int count = 0;

	public String BranchAccess = "";
	public String status = "";
	public String voucher_no = "";
	public String voucher_date = "";
	public String vouchertype_name = "";
	public String GoB = "";
	public String voucher_type = "";
	public String voucher_quote_id = "0";
	public String vouchertype_prefix = "";
	public String vouchertype_suffix = "";
	public String voucher_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_order-status_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = GetSession("emp_id", request);
				BranchAccess = GetSession("BranchAccess", request);
				GoB = PadQuotes(request.getParameter("add_button"));
				status = PadQuotes(request.getParameter("status"));
				msg = PadQuotes(request.getParameter("msg"));

				if (GoB.equals("Go")) {
					GetValues(request, response);

					StrHTML = AddOrderStatus(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		voucher_type = CNumeric(PadQuotes(request
				.getParameter("dr_voucher_type")));
		voucher_no = PadQuotes(request.getParameter("txt_voucher_no"));
	}

	public void CheckForm(HttpServletRequest request,
			HttpServletResponse response) {
		msg = "";

		if (voucher_type.equals("0")) {
			msg += "<br>Select Voucher Type!";
		}

		if (voucher_no.equals("")) {
			msg += "<br>Enter Voucher No.!";
		} else {
			StrSql = "SELECT  "
					+ " coalesce(concat(vouchertype_prefix, voucher_no, vouchertype_suffix), '') as voucher_no "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
					+ " WHERE concat(vouchertype_prefix, voucher_no, vouchertype_suffix) ="
					+ "'" + voucher_no + "'";
			// SOP("StrSql==ch==" + StrSql);
			if (ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Enter Valid Voucher No.";
			}
		}
	}

	public String AddOrderStatus(HttpServletRequest request,
			HttpServletResponse response) throws SQLException {

		CheckForm(request, response);

		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT  voucher_date, vouchertype_name, "
				+ " coalesce(concat(vouchertype_prefix, voucher_no, vouchertype_suffix), '') as voucher_no, "
				+ " vouchertype_voucherclass_id, vouchertype_id, voucher_id,"
				+ " voucher_enquiry_id, voucher_quote_id, voucher_so_id, voucher_delnote_id,"
				+ " voucher_po_id, voucher_git_id, voucher_grn_id"
				+ " FROM axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
				+ " WHERE concat(vouchertype_prefix, voucher_no, vouchertype_suffix) ="
				+ "'" + voucher_no + "'";

		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 1;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Transaction Type</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("</tr>\n");

				while (crs.next()) {

					if (voucher_type.equals("1")) {
						if (!crs.getString("voucher_enquiry_id").equals("0")) {
							Str.append(ParentVoucher(crs
									.getString("voucher_enquiry_id")));

						} else if (!crs.getString("voucher_quote_id").equals("0")) {

							Str.append(ParentVoucher(crs
									.getString("voucher_quote_id")));
						} else if (!crs.getString("voucher_so_id").equals("0")) {

							Str.append(ParentVoucher(crs.getString("voucher_so_id")));
						} else if (!crs.getString("voucher_delnote_id").equals("0")) {

							Str.append(ParentVoucher(crs
									.getString("voucher_delnote_id")));
						}
					} else if (voucher_type.equals("2")) {
						if (!crs.getString("voucher_po_id").equals("0")) {

							Str.append(ParentVoucher(crs
									.getString("voucher_po_id")));

						} else if (!crs.getString("voucher_git_id").equals("0")) {

							Str.append(ParentVoucher(crs
									.getString("voucher_git_id")));
						} else if (!crs.getString("voucher_grn_id").equals("0")) {

							Str.append(ParentVoucher(crs
									.getString("voucher_grn_id")));
						}
					}
					count = count + 1;

					if (!CNumeric(crs.getString("voucher_id")).equals("0")) {

						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count)
								.append("</td>\n");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("vouchertype_name"))
								.append("<br>")
								.append("(")
								.append(strToShortDate(crs.getString("voucher_date")))
								.append(")").append("</td>\n");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("vouchertype_name"))
								.append("<br>")
								.append("(")
								.append(strToShortDate(crs.getString("voucher_date")))
								.append(")").append("</td>\n");
						if (voucher_type.equals("1")) {
							Str.append(
									"<td valign=top align=left nowrap><a href=\"../accounting/so-update.jsp?update=yes&voucherclass_id=")
									.append(crs.getString("vouchertype_voucherclass_id"))
									.append("&vouchertype_id=")
									.append(crs.getString("vouchertype_id"))
									.append("&voucher_id=")
									.append(crs.getString("voucher_id")).append(" \">")
									.append(crs.getString("voucher_no"))
									.append("</td>\n");
						}
						else if (voucher_type.equals("2")) {
							Str.append(
									"<td valign=top align=left nowrap><a href=\"../accounting/po-update.jsp?update=yes&voucherclass_id=")
									.append(crs.getString("vouchertype_voucherclass_id"))
									.append("&vouchertype_id=")
									.append(crs.getString("vouchertype_id"))
									.append("&voucher_id=")
									.append(crs.getString("voucher_id")).append(" \">")
									.append(crs.getString("voucher_no"))
									.append("</td>\n");
						}
						Str.append("</tr>\n");
					}

					Str.append("</table>\n");
					crs.close();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateVoucherType() {
		StringBuilder Str = new StringBuilder();

		Str.append("<option value=").append("1")
				.append(StrSelectdrop("1", voucher_type));
		Str.append(">").append("Sales").append("</option>\n");
		Str.append("<option value=").append("2")
				.append(StrSelectdrop("2", voucher_type));
		Str.append(">").append("Purchase").append("</option>\n");

		return Str.toString();

	}

	public String ParentVoucher(String voucher_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		if (!voucher_id.equals("0")) {
			StrSql = " SELECT voucher_id, vouchertype_name, voucher_date,"
					+ " coalesce(concat(vouchertype_prefix, voucher_no, vouchertype_suffix), '') as voucher_no,"
					+ " vouchertype_id, vouchertype_voucherclass_id"
					+ " FROM axela_acc_voucher "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
					+ " WHERE 1=1" + " AND voucher_id = " + voucher_id;

			// +
			// " AND CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) ="+
			// "'" + voucher_no + "'";
			// SOP("StrSql=2==" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			try {
				while (crs1.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count)
							.append("</td>\n");
					Str.append("<td valign=top align=left >")
							.append(crs1.getString("vouchertype_name"))
							.append("<br>")
							.append("(")
							.append(strToShortDate(crs1
									.getString("voucher_date"))).append(")")
							.append("</td>\n");
					Str.append("<td valign=top align=left >")
							.append(crs1.getString("vouchertype_name"))
							.append("<br>")
							.append("(")
							.append(strToShortDate(crs1
									.getString("voucher_date"))).append(")")
							.append("</td>\n");
					if (voucher_type.equals("1")) {
						Str.append(
								"<td valign=top align=left nowrap><a href=\"../accounting/so-update.jsp?update=yes&voucherclass_id=")
								.append(crs1
										.getString("vouchertype_voucherclass_id"))
								.append("&vouchertype_id=")
								.append(crs1.getString("vouchertype_id"))
								.append("&voucher_id=")
								.append(crs1.getString("voucher_id"))
								.append(" \">")
								.append(crs1.getString("voucher_no"))
								.append("</td>\n");
					} else if (voucher_type.equals("2")) {
						Str.append(
								"<td valign=top align=left nowrap><a href=\"../accounting/po-update.jsp?update=yes&voucherclass_id=")
								.append(crs1
										.getString("vouchertype_voucherclass_id"))
								.append("&vouchertype_id=")
								.append(crs1.getString("vouchertype_id"))
								.append("&voucher_id=")
								.append(crs1.getString("voucher_id"))
								.append(" \">")
								.append(crs1.getString("voucher_no"))
								.append("</td>\n");
					}
					Str.append("</tr>\n");
				}
				crs1.close();

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}

		}
		return Str.toString();

	}

}
