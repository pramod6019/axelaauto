package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Day_Book extends Connect {

	public String StrSql = "";
	public String startdate = "", start_date = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_id = "";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String go = "";
	public String export = "", StrSearch = "";
	public String vouchertype_id = "0", voucher_customer_id = "0", vouchertrans_customer_id = "0";
	public String vouchertype_idarr[] = null;
	public double credit = 0.00, debit = 0.00;
	public double drtotal = 0.00, crtotal = 0.00;
	public String ledger_id = "0";
	public String voucher_id = "0";
	public String brand_id = "", region_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	double running_bal = 0.00;
	public Ledger_Check ledgercheck = new Ledger_Check();
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = strToShortDate(ToShortDate(kknow()));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = Listdata(request, response);
					}
				}
				if (!voucher_id.equals("0")) {
					StrHTML = ListVoucherInfo(voucher_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			brand_id = RetrunSelectArrVal(request, "dr_principal");
			brand_ids = request.getParameterValues("dr_principal");
			branch_id = RetrunSelectArrVal(request, "dr_branch");
			branch_ids = request.getParameterValues("dr_branch");
			region_id = RetrunSelectArrVal(request, "dr_region");
			region_ids = request.getParameterValues("dr_region");
			vouchertype_idarr = request.getParameterValues("dr_voucher_type");
			ledger_id = CNumeric(PadQuotes(request.getParameter("ledger")));
			startdate = PadQuotes(request.getParameter("txt_startdate"));

			if (vouchertype_idarr != null) {
				StrSearch += " AND (";
				for (int i = 0; i < vouchertype_idarr.length; i++) {
					if (i == 0) {
						StrSearch += " vouchertype_id = "
								+ vouchertype_idarr[i] + "";
					} else {
						StrSearch += " OR vouchertype_id = "
								+ vouchertype_idarr[i] + "";
					}
				}
				StrSearch += " )";
			}

			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	protected void CheckForm() {
		msg = "";
		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter Valid Start Date!";
			}
		} else {
			msg += "<br>Select Date!";

		}

		if (branch_id.equals("0") || branch_id.equals("")) {
			msg += "<br>Branch Not Selected!";
		}

		if (ledger_id.equals("0")) {
			msg += "<br>Select Ledger!";
		}

	}

	public String Listdata(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT voucher_id, CONCAT(vouchertype_prefix, voucher_no,"
					+ " vouchertype_suffix) AS voucher_no,"
					+ " voucher_ref_no, voucher_date,voucher_active,"
					+ " COALESCE (main2.customer_name,'')AS customer_name, "
					+ " COALESCE (main1.customer_name,'')AS patment_name,"
					+ " COALESCE (voucher_narration, '') AS voucher_narration,"
					+ " COALESCE (paymode_name, '') AS patment_mode,"
					+ " COALESCE (vouchertrans_cheque_no, '') AS cheque_no,"
					+ " COALESCE (bank_name, '') AS bank_name,"
					+ " vouchertrans_paymode_id, voucher_customer_id,"
					+ " COALESCE ( ( SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) )"
					+ " - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) ), 0 ) AS vouchertrans_amount,"
					+ " vouchertrans_customer_id, vouchertrans_dc,"
					+ " voucherclass_name, vouchertype_id, vouchertype_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer main1 ON main1.customer_id = vouchertrans_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer main2 ON main2.customer_id = voucher_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_payment_mode ON paymode_id = vouchertrans_paymode_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = vouchertrans_bank_id "
					+ " WHERE 1 = 1"
					+ " AND voucher_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND ( ( vouchertype_authorize = 1 AND voucher_authorize = 1 ) OR (vouchertype_authorize = 0) )"
					+ " AND vouchertrans_amount != 0"
					+ " AND main1.customer_id=" + ledger_id
					+ " AND SUBSTR(voucher_date,1,8) = SUBSTR(" + start_date + ",1,8)"
					+ StrSearch
					+ " GROUP BY  voucher_id, main1.customer_id"
					// + " vouchertrans_id"
					+ " ORDER BY vouchertrans_id DESC"
					+ " LIMIT 5000";

			// SOP("StrSql==Day Book====" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;

			if (crs.isBeforeFirst()) {

				export = "Export";

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");

				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>ID</th>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th data-toggle=\"true\">Particulars</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Voucher No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Ref No.</th>\n");
				Str.append("<th>Voucher</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Debit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Credit</th>\n");
				Str.append("</tr></thead>\n");
				crs.beforeFirst();

				while (crs.next()) {

					count++;
					Str.append("<tr onClick=ListshowHint(" + crs.getString("voucher_id") + "," + count + ")>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");

					Str.append("<td valign=top align=left>").append("<a href='../accounting/voucher-list.jsp?update=yes");
					Str.append("&voucher_id=" + crs.getString("voucher_id")).append("&vouchertype_id=" + crs.getString("vouchertype_id")).append("'>");
					Str.append(crs.getString("voucher_id")).append("</a>").append("</td>\n");

					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_date"))).append("</td>\n");

					Str.append("<td valign=top>").append(crs.getString("patment_name"));
					if (!crs.getString("voucher_narration").equals("")) {
						Str.append("<br>Narration: " + crs.getString("voucher_narration"));
					}
					Str.append("</td>\n");

					Str.append("<td valign=top align=center>").append(crs.getString("voucher_no")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("voucher_ref_no")).append("</td>\n");

					Str.append("<td valign=top align=left>").append("<a href='../accounting/voucher-list.jsp?update=yes");
					Str.append("&voucher_id=" + crs.getString("voucher_id")).append("&vouchertype_id=" + crs.getString("vouchertype_id")).append("'>");
					Str.append(crs.getString("vouchertype_name")).append("</a>").append("</td>\n");

					if (CNumeric(PadQuotes(crs.getString("vouchertrans_dc"))).equals("1")) {

						Str.append("<td valign=top align=right>").append(IndDecimalFormat(df.format(crs.getDouble("vouchertrans_amount"))).replace("-", "")).append("</td>\n");

						Str.append("<td valign=top align=left>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
						if (drtotal != 0.00) {
							drtotal += Double.parseDouble(crs.getString("vouchertrans_amount"));
						} else {
							drtotal = Double.parseDouble(crs.getString("vouchertrans_amount"));
						}

					} else {

						Str.append("<td valign=top align=left>");
						Str.append("&nbsp;");
						Str.append("</td>\n");

						Str.append("<td valign=top align=right>");
						Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))).replace("-", ""));
						Str.append("</td>\n");

						if (crtotal != 0.00) {
							crtotal += Double.parseDouble(crs.getString("vouchertrans_amount"));
						} else {
							crtotal = Double.parseDouble(crs.getString("vouchertrans_amount"));
						}

					}

					Str.append("</tr>\n");
					Str.append("<tr class='listbody_row' id='listbody_row_" + count + "' style='display:none;'><td class='listbody_row_td' colspan=9 id=list_" + count + " ></td></tr>");

				}

				if (crtotal < 0) {
					crtotal = (crtotal * (-1));
				}
				if (drtotal < 0) {
					drtotal = (drtotal * (-1));
				}

				Str.append("<tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>").append("Total:").append("</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(drtotal))).append("</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(crtotal))).append("</b></td>\n");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>");

				if (drtotal > crtotal) {
					Str.append("Total Debit:");
				} else {
					Str.append("Total Credit:");
				}
				Str.append("</b></td>\n");

				if (drtotal > crtotal) {
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(drtotal - crtotal))).append("</b></td>\n");
					Str.append("<td valign=top align=right><b>").append("0.00").append("</b></td>\n");
				} else {
					Str.append("<td valign=top align=right><b>").append("0.00").append("</b></td>\n");
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(crtotal - drtotal))).append("</b></td>\n");
				}
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<br><br><br><br><center><font color=red><b>No Records found!</b></font></center><br><br><br><br><br>");
			}
			// rscopy.close();
			crs.close();

		}

		catch (Exception ex) {
			SOPError("Report Day Book===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String ListVoucherInfo(String voucher_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;

		StrSql = "SELECT accgroup_id, accgroup_name,"
				+ " accgroup_alie, customer_name, vouchertrans_customer_id,"
				+ " voucher_id, voucher_vouchertype_id,"
				+ " voucher_date, voucher_no, voucher_ref_no,"
				+ " voucher_narration, voucher_notes,"
				+ " sum(vouchertrans_amount) AS vouchertrans_amount,"
				+ " CONCAT(vouchertype_name,' ','ID:',voucher_id) AS vouchertype_name,"
				+ " vouchertrans_dc, vouchertype_label,"
				+ " vouchertrans_paymode_id, voucher_active,"
				+ " COALESCE(inactivestatus_name,'[Inactive]') AS inactivestatus_name,"
				+ " voucher_inactivestatus_date";
		StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher "
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
				+ " LEFT JOIN axela_acc_receipt_inactive_status ON inactivestatus_id = voucher_inactivestatus_id AND voucher_vouchertype_id = 9"
				+ " WHERE 1 = 1"
				+ " AND vouchertype_affects_accounts = 1"
				+ " AND ((vouchertype_authorize = 1 AND voucher_authorize = 1) OR (vouchertype_authorize = 0 ))"
				+ " AND vouchertrans_amount != 0"
				+ " AND voucher_active = 1"
				// + " AND customer_ledgertype = 0
				+ " ";

		if (!voucher_id.equals("0")) {
			StrSql += " AND voucher_id = " + voucher_id;
		}

		StrSql += " GROUP BY voucher_vouchertype_id, vouchertrans_voucher_id,"
				+ " vouchertrans_customer_id, vouchertrans_dc"
				+ " ORDER BY vouchertrans_dc DESC";

		// SOP("ListVoucherInfo StrSql ======" + StrSql);

		crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td valign=top align=left colspan=9>");
					Str.append("<span class='col-md-offset-1 col-md-3'><b>\n")
							.append(crs.getString("customer_name"))
							.append("</b></span>\n");
					if (crs.getString("vouchertrans_dc").equals("1")) {
						Str.append("<span class='col-md-2'><b>\n");
						Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))) + " Dr.");
						Str.append("</b></span>\n");
						Str.append("<span class='col-md-2'><b>\n");
						Str.append("&nbsp;");
						Str.append("</b></span>\n");
					} else {
						Str.append("<span class='col-md-2'><b>\n");
						Str.append("&nbsp;");
						Str.append("</b></span>\n");
						Str.append("<span class='col-md-2'><b>\n");
						Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))) + " Cr.");
						Str.append("</b></span>\n");
					}
					Str.append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</table>");
				crs.close();
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateVoucherType(String[] vouchertype_idarr) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vouchertype_id, vouchertype_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " WHERE 1 = 1"
					+ " AND vouchertype_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " GROUP BY vouchertype_id"
					+ " ORDER BY vouchertype_name";

			// SOP("PopulateVoucherType======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vouchertype_id"));
				Str.append(ArrSelectdrop(crs.getInt("vouchertype_id"), vouchertype_idarr));
				Str.append(">").append(crs.getString("vouchertype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePayment(String payment) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, customer_name"
					+ " FROM " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql);

			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), payment));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
