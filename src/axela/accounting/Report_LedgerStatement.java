package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_LedgerStatement extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String StrSql_Summary = "";
	public String StrSearch = "";
	public String all = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String comp_id = "0";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String vouchertype_name = "";
	public String QueryString = "";
	public String customer_id = "0";
	public String so_id = "0";
	public String voucher_id = "0";
	public double running_bal1 = 0.00;
	public double running_bal = 0.00;
	public double oldrunning_bal = 0.00;
	public double closing_bal = 0.00;
	public double drtrans_total = 0.00, crtrans_total = 0.00;
	public double drtrans_total1 = 0.00, crtrans_total1 = 0.00;
	public double drrtrans_total1 = 0.00, crrtrans_total1 = 0.00;
	public String start_date = "";
	public String end_date = "";
	public String startdate = "";
	public String enddate = "";
	public String brand_id = "", branch_id = "0", region_id = "0";
	public String[] brand_ids, branch_ids;
	public String vouchertype_id = "0", accgroup_id = "0", accsubgroup_id = "0", principal_id = "0";
	public String go = "", submit_btn = "";
	DecimalFormat df = new DecimalFormat("0.0000");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public String orderby = "0";
	public String bal_sheat = "", trial_bal = "";
	public Ledger_Check ledgercheck = new Ledger_Check();
	public SubGroup_Check subgroupcheck = new SubGroup_Check();
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				submit_btn = PadQuotes(request.getParameter("submit_btn"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				all = PadQuotes(request.getParameter("all"));

				if (PadQuotes(request.getParameter("txt_startdate")).equals("")) {
					startdate = ReportStartdate();
				} else {
					startdate = PadQuotes(request.getParameter("txt_startdate"));
				}

				if (PadQuotes(request.getParameter("txt_enddate")).equals("")) {
					enddate = DateToShortDate(kknow());
				} else {
					enddate = PadQuotes(request.getParameter("txt_enddate"));
				}

				bal_sheat = PadQuotes(request.getParameter("bal_sheat"));
				trial_bal = PadQuotes(request.getParameter("trial_bal"));

				if (bal_sheat.equals("yes") || trial_bal.equals("yes")) {
					go = "Go";
				}

				if (PadQuotes(RetrunSelectArrVal(request, "dr_principal")).equals("") && !PadQuotes(request.getParameter("trial_bal_principal")).equals("")) {
					brand_id = request.getParameter("trial_bal_principal");
					brand_ids = request.getParameter("trial_bal_principal").split(",");
				}

				if (PadQuotes(RetrunSelectArrVal(request, "dr_branch")).equals("") && !PadQuotes(request.getParameter("trial_bal_branch")).equals("")) {
					branch_id = request.getParameter("trial_bal_branch");
					branch_ids = request.getParameter("trial_bal_branch").split(",");
				}

				if (!so_id.equals("0")) {
					start_date = PadQuotes(request.getParameter("so_date"));
					startdate = strToShortDate(PadQuotes(request.getParameter("so_date")));
					end_date = ToLongDate(kknow());
				}

				if (all.equals("yes") && !customer_id.equals("0")) {
					start_date = ConvertShortDateToStr(startdate);
					end_date = ConvertShortDateToStr(enddate);
					// GetValues(request, response);
				} else if (all.equals("yes") && !voucher_id.equals("0")) {
					start_date = ConvertShortDateToStr(startdate);
					end_date = ConvertShortDateToStr(startdate);
					enddate = startdate;
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = Listdata();
					}
					// GetValues(request, response);
				} else if (all.equals("yes") && bal_sheat.equals("yes")) {
					start_date = ConvertShortDateToStr(startdate);
					end_date = ConvertShortDateToStr(enddate);
					GetValues(request, response);
					CheckForm();
				}
				if (go.equals("Go")) {
					if (submit_btn.equals("Submit")) {
						GetValues(request, response);
					}
					CheckForm();
					if (msg.equals("")) {
						StrHTML = Listdata();
					}
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void CheckForm() {
		msg = "";

		// if (customer_id.equals("0") || customer_id.equals("")) {
		// if (accgroup_id.equals("0") || accgroup_id.equals("")) {
		// if (branch_id.equals("0") || branch_id.equals("")) {
		// msg += "<br>Select Ledger or Group!";
		// }
		// }
		// }

		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter Valid Start Date!";
			}
		}
		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!startdate.equals("")
						&& !enddate.equals("")
						&& Long.parseLong(start_date) > Long
								.parseLong(end_date)) {
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter Valid End Date!";
			}
		}

		if (!customer_id.equals("0") && !start_date.equals("")
				&& end_date.equals("")) {
			msg += "<br>Select End Date!";
		} else if (!customer_id.equals("0") && start_date.equals("")
				&& !end_date.equals("")) {
			msg += "<br>Select Start Date!";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		vouchertype_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_type")));
		accgroup_id = CNumeric(PadQuotes(request.getParameter("dr_group")));
		accsubgroup_id = CNumeric(PadQuotes(request.getParameter("dr_subgroup")));
		principal_id = CNumeric(PadQuotes(request.getParameter("dr_principal")));
		orderby = CNumeric(PadQuotes(request.getParameter("dr_orderby")));

	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}

		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = "SELECT accgroup_id, accgroup_name,"
				+ " accgroup_alie, vouchertrans_customer_id,"
				+ " voucher_id, voucher_vouchertype_id,"
				+ " voucher_date, voucher_no, voucher_ref_no,"
				+ " voucher_narration, voucher_notes,"
				+ " sum(vouchertrans_amount) AS vouchertrans_amount,"
				+ " CONCAT(vouchertype_name,' ','ID:',voucher_id) AS vouchertype_name,"
				+ " vouchertrans_dc, vouchertype_label,"
				+ " vouchertrans_paymode_id, voucher_active,"
				+ " COALESCE(inactivestatus_name,'[Inactive]') AS inactivestatus_name,"
				+ " voucher_inactivestatus_date";
		SqlJoin += " FROM " + compdb(comp_id) + "axela_acc_voucher "
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id "
				+ " LEFT JOIN axela_acc_receipt_inactive_status ON inactivestatus_id = voucher_inactivestatus_id AND voucher_vouchertype_id = 9"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE 1 = 1"
				+ " AND vouchertype_affects_accounts = 1"
				+ " AND ((vouchertype_authorize = 1 AND voucher_authorize = 1) OR (vouchertype_authorize = 0 ))"
				+ " AND vouchertrans_amount != 0"
				+ " AND voucher_active = 1";

		CountSql = "SELECT COUNT(DISTINCT vouchertrans_id)";
		if (!customer_id.equals("0")) {
			SqlJoin += " AND customer_id = " + customer_id;
		}
		if (!so_id.equals("0")) {
			SqlJoin += " AND voucher_so_id = " + so_id;
		}
		if (!accgroup_id.equals("0")) {
			SqlJoin += " AND accgroup_id = " + accgroup_id;
		}

		if (!brand_id.equals("") && !brand_id.equals("0")) {
			SqlJoin += " AND branch_brand_id IN (" + brand_id + ")";
		}
		if (!branch_id.equals("0") && !branch_id.equals("")) {
			SqlJoin += " AND branch_id IN (" + branch_id + ")";
		}

		if (!start_date.equals("")) {
			SqlJoin += " AND SUBSTR(voucher_date,1,8) >= SUBSTR(" + start_date + ",1,8)";
		}
		if (!end_date.equals("")) {
			SqlJoin += " AND SUBSTR(voucher_date,1,8) <= SUBSTR(" + end_date + ",1,8)";
		}
		if (!vouchertype_id.equals("0")) {
			SqlJoin += " AND vouchertype_id = " + vouchertype_id;
		}
		if (!voucher_id.equals("0")) {
			SqlJoin += " AND voucher_id = " + voucher_id;
		}

		SqlJoin += BranchAccess;

		StrSql_Summary = SqlJoin;
		CountSql += SqlJoin;
		SqlJoin += " GROUP BY voucher_vouchertype_id, vouchertrans_voucher_id, vouchertrans_customer_id, vouchertrans_dc";

		StrSql += SqlJoin;

		// SOP("CountSql===" + StrSqlBreaker(CountSql));

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		// SOP("TotalRecords======" + TotalRecords);

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " Voucher(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "report-ledgerstatement.jsp?" + QueryString
					+ "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			if (orderby.equals("0")) {
				// StrSql += " ORDER BY voucher_date, voucher_id, vouchertrans_dc DESC, vouchertrans_id";
				StrSql += " ORDER BY  vouchertype_id, voucher_id, voucher_date, vouchertrans_id";
			} else if (orderby.equals("1")) {
				StrSql += " ORDER BY  vouchertype_id, voucher_id, voucher_date, vouchertrans_id";
			} else if (orderby.equals("2")) {
				StrSql += " ORDER BY  vouchertrans_dc DESC, voucher_id, voucher_date, vouchertrans_id";
			} else if (orderby.equals("3")) {
				StrSql += " ORDER BY vouchertrans_dc, voucher_id, voucher_date, vouchertrans_id";
			}

			StrSql_Summary = "SELECT ( customer_open_bal + ("
					+ " SELECT COALESCE ( ( SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) )"
					+ " - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) ), 0 )"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " WHERE voucher_active = 1"
					+ " AND vouchertrans_customer_id = customer_id"
					+ " AND SUBSTR(voucher_date, 1, 8) < SUBSTR(" + start_date + ", 1, 8) ) ) AS oppbalance,"
					+ " SUM(IF(vouchertrans_dc=1,vouchertrans_amount,0))as debit_amount,"
					+ " SUM(IF(vouchertrans_dc=0,vouchertrans_amount,0))as credit_amount" + StrSql_Summary;

			StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("Report_LedgerStatement=====StrSql===" + StrSql);
			crs = processQuery(StrSql, 0);
			try {

				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>ID</th>\n");
				Str.append("<th data-hide=\"phone\">Ledger</th>\n");
				Str.append("<th data-hide=\"phone\">Voucher</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Voucher No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Ref No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Debit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Credit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Running Balance</th>\n");
				Str.append("</tr></thead>\n");
				if (crs.isBeforeFirst()) {
					crs.first();

					crs.beforeFirst();
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align='center'>");
						if (crs.getString("voucher_vouchertype_id").equals("9") && !crs.getString("voucher_active").equals("1")) {
							Str.append(strToShortDate(crs.getString("voucher_inactivestatus_date")));
						} else {
							Str.append(strToShortDate(crs.getString("voucher_date")));
						}
						if (!crs.getString("voucher_notes").equals("")) {
							Str.append("<br>Comments: ").append(crs.getString("voucher_notes"));
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("voucher_id")).append("</td>\n");
						Str.append("<td valign=top align=left>")
								.append(ExecuteQuery("SELECT customer_name FROM " + compdb(comp_id) + "axela_customer WHERE customer_id=" + crs.getString("vouchertrans_customer_id")))
								.append("</td>\n");
						Str.append("<td valign=top align=center>");
						Str.append("<a href='../accounting/voucher-list.jsp?voucher_id=" + crs.getString("voucher_id"));
						Str.append("&vouchertype_id=" + crs.getString("voucher_vouchertype_id")).append("'>");
						Str.append(crs.getString("vouchertype_name") + "</a>");
						if (crs.getString("voucher_active").equals("0")) {
							Str.append("<br><font color='#ff0000'>" + crs.getString("inactivestatus_name") + "</font>");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align='center'>").append(crs.getString("voucher_no")).append("</td>\n");
						Str.append("<td valign=top align='center'>").append(crs.getString("voucher_ref_no")).append("</td>\n");
						if (crs.getString("vouchertrans_dc").equals("1")) {

							Str.append("<td valign=top align=right>");
							Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))));
							Str.append("</td>\n");
							drrtrans_total1 += Double.parseDouble(crs.getString("vouchertrans_amount"));

							if (drtrans_total != 0.00) {
								drtrans_total += Double.parseDouble(crs.getString("vouchertrans_amount"));
							} else {
								drtrans_total = Double.parseDouble(crs.getString("vouchertrans_amount"));
							}
							if (crs.getString("voucher_active").equals("1")) {
								Str.append("<td valign=top align=right>");
								Str.append("&nbsp;");
								Str.append("</td>\n");
							} else {
								Str.append("<td valign=top align=right>");
								Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))));
								Str.append("</td>\n");
								drrtrans_total1 -= Double.parseDouble(crs.getString("vouchertrans_amount"));

								if (drtrans_total != 0.00) {
									drtrans_total -= Double.parseDouble(crs.getString("vouchertrans_amount"));
								} else {
									drtrans_total = Double.parseDouble(crs.getString("vouchertrans_amount"));
								}
							}
						} else {

							if (crs.getString("voucher_active").equals("1")) {
								Str.append("<td valign=top align=left>");
								Str.append("&nbsp;");
								Str.append("</td>\n");
							} else {
								Str.append("<td valign=top align=right>");
								Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))));
								Str.append("</td>\n");
								crrtrans_total1 -= Double.parseDouble(crs.getString("vouchertrans_amount"));

								if (crtrans_total != 0.00) {
									crtrans_total -= Double.parseDouble(crs.getString("vouchertrans_amount"));
								} else {
									crtrans_total = -Double.parseDouble(crs.getString("vouchertrans_amount"));
								}
							}
							Str.append("<td valign=top align=right>");
							Str.append(IndFormat(df.format(crs.getDouble("vouchertrans_amount"))));
							Str.append("</td>\n");
							crrtrans_total1 += Double.parseDouble(crs.getString("vouchertrans_amount"));

							if (crtrans_total != 0.00) {
								crtrans_total += Double.parseDouble(crs.getString("vouchertrans_amount"));

							} else {
								crtrans_total = Double.parseDouble(crs.getString("vouchertrans_amount"));
							}
						}
						if (crs.getString("vouchertrans_dc").equals("1")) {

							if (crs.getString("voucher_active").equals("1")) {

								if (running_bal != 0.00) {
									running_bal = running_bal + Double.parseDouble(crs.getString("vouchertrans_amount"));
								} else {
									running_bal = Double.parseDouble(crs.getString("vouchertrans_amount"));
								}
							}

							Str.append("<td valign=top align=right>");
							if (running_bal >= 0) {
								Str.append(IndFormat(df.format(running_bal)) + " DR");
							} else {
								Str.append(IndFormat(df.format(Double.parseDouble(String.valueOf(running_bal).substring(1)))) + " CR");
							}
							Str.append("</td>\n");

						} else {

							if (crs.getString("voucher_active").equals("1")) {
								if (running_bal != 0.00) {
									running_bal = running_bal - Double.parseDouble(crs.getString("vouchertrans_amount"));
								} else {
									running_bal = -Double.parseDouble(crs.getString("vouchertrans_amount"));
								}
							}

							Str.append("<td valign=top align=right>");
							if (running_bal >= 0) {
								Str.append(IndFormat(df.format(running_bal)) + " DR");
							} else {
								Str.append(IndFormat(df.format(Double.parseDouble(String.valueOf(running_bal).substring(1)))) + " CR");
							}
							Str.append("</td>\n");
						}
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("<tr style=\"border:none;background-color:white; height:70px;\">");
					Str.append("<td colspan=10 valign=top align=center>").append(PageNaviStr);
					Str.append("</td>");
					Str.append("</tr>");
					// SOP("StrSql_Summary======" + StrSql_Summary);

					if (!customer_id.equals("0")) {
						DisplayLedgerSummary(StrSql_Summary, Str);
					} else {
						DisplaySummary(StrSql_Summary, Str);
					}
					Str.append("</table>");
					Str.append("</div>");

				}
			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {

			RecCountDisplay = "<br><br><br><br><font color=red><b>No Voucher(s) Found!</b></font><br><br>";
		}
		return Str.toString();
	}
	public String DisplayLedgerSummary(String StrSql_Summary, StringBuilder Str) {
		double closing_bal = 0.00, drrtrans_total1 = 0.00, crrtrans_total1 = 0.00;
		double oppbalance1 = 0.00, current_bal = 0.00;
		Str.append("");
		if (!StrSql_Summary.equals("")) {
			try {

				// SOP("StrSql_Summary===" + StrSql_Summary);
				CachedRowSet crs = processQuery(StrSql_Summary, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (!so_id.equals("0")) {
							oppbalance1 = 0;
							drrtrans_total1 = crs.getDouble("debit_amount");
							crrtrans_total1 = crs.getDouble("credit_amount");
						} else {
							oppbalance1 = crs.getDouble("oppbalance");
							drrtrans_total1 = crs.getDouble("debit_amount");
							crrtrans_total1 = crs.getDouble("credit_amount");
						}
					}

					crs.close();
					Str.append("<tr>");
					Str.append("<td valign=top colspan=8 align=right><b>").append("Opening Balance:").append("<b></td>\n");
					if (oppbalance1 >= 0) {
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(oppbalance1)));
						Str.append(" DR</b></td>\n");
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
					} else {
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(Double.parseDouble(String.valueOf(oppbalance1).substring(1)))));
						Str.append(" CR</b></td><br>\n");
					}
					Str.append("</tr>");

					Str.append("<tr>\n");
					Str.append("<td valign=top colspan=8 align=right>").append("<b>Transaction Total:</b>").append("</td>\n");
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(drrtrans_total1)));
					Str.append(" DR</b></td>\n");
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(crrtrans_total1)));
					Str.append(" CR</b></td>\n");

					closing_bal = (drrtrans_total1) - (crrtrans_total1);

					Str.append("</tr>\n");

					Str.append("<td valign=top colspan=8 align=right><b>").append("Closing Balance:").append("<b></td>\n");
					if (closing_bal < 0) {
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(closing_bal).substring(1)));
						Str.append(" CR</b></td>\n");
					} else {
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(closing_bal)));
						Str.append(" DR</b></td>\n");
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
					}
					Str.append("</tr>\n");
					Str.append("<tr>\n");

					current_bal = (oppbalance1) + (closing_bal);

					Str.append("<td valign=top colspan=8 align=right><b>").append("Current Balance:").append("<b></td>\n");
					if (current_bal < 0) {
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(current_bal).substring(1)));
						Str.append(" CR</b></td>\n");
					} else {
						Str.append("<td valign=top align=right><b>");
						Str.append(IndFormat(df.format(current_bal)));
						Str.append(" DR</b></td>\n");
						Str.append("<td valign=top align=right>");
						Str.append("&nbsp;");
						Str.append("</td>\n");
					}
					Str.append("</tr>\n");

				}

			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public String DisplaySummary(String StrSql_Summary, StringBuilder Str) {

		double closing_bal = 0.00, drrtrans_total1 = 0.00, crrtrans_total1 = 0.00;
		Str.append("");
		if (!StrSql_Summary.equals("")) {
			try {
				// SOP("StrSql_Summary=====" + StrSql_Summary);
				CachedRowSet crs = processQuery(StrSql_Summary, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						drrtrans_total1 = crs.getDouble("debit_amount");
						crrtrans_total1 = crs.getDouble("credit_amount");
					}
				}

				Str.append("<tr>\n");
				Str.append("<td valign=top colspan=8 align=right>").append("<b>Transaction Total:</b>").append("</td>\n");
				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(df.format(drrtrans_total1)));
				Str.append(" DR</b></td>\n");
				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(df.format(crrtrans_total1)));
				Str.append(" CR</b></td>\n");

				closing_bal = drrtrans_total1 - crrtrans_total1;

				Str.append("</tr>\n");

				Str.append("<td valign=top colspan=8 align=right><b>").append("Closing Balance:").append("<b></td>\n");
				if (closing_bal < 0) {
					Str.append("<td valign=top align=right>");
					Str.append("&nbsp;");
					Str.append("</td>\n");
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(closing_bal).substring(1)));
					Str.append(" CR</b></td>\n");
				} else {
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(closing_bal)));
					Str.append(" DR</b></td>\n");
					Str.append("<td valign=top align=right>");
					Str.append("&nbsp;");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");

			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public String PopulateVoucherType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT vouchertype_id, vouchertype_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " ORDER BY vouchertype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vouchertype_id"));
				Str.append(StrSelectdrop(crs.getString("vouchertype_id"), vouchertype_id));
				Str.append(">").append(crs.getString("vouchertype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateGroup() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT accgrouppop_id, accgrouppop_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_group_pop"
					+ " ORDER BY accgrouppop_alie, accgrouppop_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("accgrouppop_id"));
				Str.append(StrSelectdrop(crs.getString("accgrouppop_id"), accgroup_id));
				Str.append(">").append(crs.getString("accgrouppop_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			Str.append("<option value=1").append(StrSelectdrop("1", orderby)).append(">Voucher").append("</option>\n");
			Str.append("<option value=2").append(StrSelectdrop("2", orderby)).append(">DR").append("</option>\n");
			Str.append("<option value=3").append(StrSelectdrop("3", orderby)).append(">CR").append("</option>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
