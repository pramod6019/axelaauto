// smitha nag june 6 2013
package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import cloudify.connect.Connect;

public class Report_LedgerStatement_Print extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";

	public String StrSql_Summary = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String company_id = "0";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public int count = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String customer_id = "";
	public String customer_name = "";
	public String comp_id = "0";
	public double running_bal1 = 0.00;
	public double running_bal = 0.00;
	public double oldrunning_bal = 0.00;
	public double drtrans_total = 0.00, crtrans_total = 0.00;
	public double drtrans_total1 = 0.00, crtrans_total1 = 0.00;
	public Double trans_total1 = 0.00;
	public String currentbal_opp_amount = "";
	public double current_bal = 0.00;
	// public String accgroup_alie = "";
	public String start_date = "";
	public String end_date = "";
	public String startdate = "";
	public String report_date = "";
	public String enddate = "";
	public String vouchertype_id = "0";
	public String go = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public String orderby = "0";
	public String trialbalance = "", reportfrom = "", balancesheet = "", values = "",
			profitloss = "";
	double oppbalance = 0.00;
	public String format = "pdf";
	public Map parameters = new HashMap();
	HashMap dataMap;
	public List dataList = new ArrayList();
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			SOP("doPost");
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				company_id = CNumeric(GetSession("company_id", request) + "");
				QueryString = PadQuotes(request.getQueryString());

				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				trialbalance = PadQuotes(request.getParameter("trialbalance"));
				balancesheet = PadQuotes(request.getParameter("balancesheet"));
				profitloss = PadQuotes(request.getParameter("profitloss"));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				SOP("reportfrom=====" + reportfrom);
				conn = connectDB();
				if (go.equals("Go")) {
					SOP("go=====" + go);
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						// StrHTML = GetStrSql();
					}
				}
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				// SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = LedgerStatement();
				report.doPost(request, response);

				if (balancesheet.equals("yes")) {
					startdate = PadQuotes(session
							.getAttribute("balancesheet_filters_startdate") + "");
					enddate = PadQuotes(session
							.getAttribute("balancesheet_filters_enddate") + "");
				} else if (trialbalance.equals("yes")) {
					startdate = PadQuotes(session
							.getAttribute("trailbalance_filters_startdate") + "");
					enddate = PadQuotes(session
							.getAttribute("trailbalance_filters_enddate") + "");
				} else if (profitloss.equals("yes")) {
					startdate = PadQuotes(session
							.getAttribute("profitloss_filters_startdate") + "");
					enddate = PadQuotes(session
							.getAttribute("profitloss_filters_enddate") + "");
				} else {
					startdate = PadQuotes(request.getParameter("txt_startdate"));
					enddate = PadQuotes(request.getParameter("txt_enddate"));
				}
				SOP("1111");
				int year = Integer.parseInt(SplitYear(ToShortDate(kknow())));
				int month = Integer.parseInt(SplitMonth(ToShortDate(kknow())));
				SOP("2222");
				if (month >= 1 && month <= 3) {
					--year;
				}
				if (startdate.equals("")) {
					// startdate = ReportStartdate();
					startdate = "01/04/" + year;

				}
				if (enddate.equals("")) {
					// enddate = DateToShortDate(kknow());
					enddate = "31/03/" + (++year);
				}
				customer_id = PadQuotes(request.getParameter("ledger"));
				if (!CNumeric(customer_id).equals("0")) {
					go = "Go";
				}
			}
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		if (customer_id.equals("0") || customer_id.equals("")) {
			msg += "<br>Select Ledger!";
		}
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

		if (!customer_id.equals("") && !start_date.equals("")
				&& end_date.equals("")) {
			msg += "<br>Select End Date!";
		} else if (!customer_id.equals("") && start_date.equals("")
				&& !end_date.equals("")) {
			msg += "<br>Select Start Date!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_id = CNumeric(PadQuotes(GetSession("ledgerstatement_customer_id", request) + ""));
		startdate = PadQuotes(GetSession("ledgerstatement_startdate", request) + "");
		// SOP("startdate=="+startdate);
		enddate = PadQuotes(GetSession("ledgerstatement_enddate", request) + "");
		// SOP("enddate=="+enddate);
		vouchertype_id = CNumeric(PadQuotes(GetSession("ledgerstatement_vouchertype_id", request) + ""));
		StrSql = PadQuotes(GetSession("ledgerstatement_StrSql", request));
		SOP("StrSql======" + StrSql);
		// SOP("StrSql===" + StrSql);
		StrSql = StrSql.replace("&#39;", "'");
		// oppbalance = Double.parseDouble(PadQuotes(GetSession("ledgerstatement_oppbalance", request)));
		format = PadQuotes(request.getParameter("dr_format"));
	}
	public Map BuildParameters() {
		report_date = "From " + startdate + " To " + enddate;
		// SOP("report_date==" + report_date);
		parameters.put("report_date", report_date);
		if (format.equals("pdf")) {
			// SOP("11111");
			parameters.put("report_format", "pdf");
		} else if (format.equals("xlsx")) {
			parameters.put("report_format", "xlsx");
		}
		return parameters;
	}

	public List<Map> LedgerStatement() {
		// HashMap dataMap;
		double summary_oppbalance = 0.00, summery_runningbalance = 0.00, summary_drtrans_total = 0.00, summary_crtrans_total = 0.00;
		running_bal = oppbalance;
		// SOP("oppbalance=="+oppbalance);
		try {

			SOP("StrSql==1111===" + StrSql);
			if (StrSql != "") {
				CachedRowSet rs = processQuery(StrSql, 0);
				if (rs.isBeforeFirst()) {
					while (rs.next()) {
						SOP("1111111111");
						dataMap = new HashMap();
						SOP("aaaaaaaa");
						// dataMap.put("customer_name", rs.getString("customer_name"));
						// SOP("aaaaaaaa");
						// dataMap.put("oppcustomer_name", rs.getString("oppcustomer_name"));
						SOP("ddddddddd");
						if (rs.getDouble("oppbalance") >= 0) {
							dataMap.put("openingdr_amount", oppbalance);
							dataMap.put("openingcr_amount", 0.00);
						} else {
							dataMap.put("openingdr_amount", 0.00);
							dataMap.put("openingcr_amount", Double.parseDouble((oppbalance + "").substring(1)));
						}
						SOP("2222222222");
						dataMap.put("serial_no", ++count);
						dataMap.put("date", strToShortDate(rs.getString("voucher_date")));
						dataMap.put("id", rs.getString("voucher_id"));
						dataMap.put("voucher", rs.getString("vouchertype_name"));
						dataMap.put("voucher_no", rs.getString("voucher_no"));

						if (rs.getString("vouchertrans_dc").equals("1")) {
							if (rs.getString("voucher_vouchertype_id").equals("109") && rs.getString("vouchertrans_paymode_id").equals("2")) {
								dataMap.put("debit", rs.getDouble("vouchertrans_amount"));
							} else {
								dataMap.put("debit", rs.getDouble("vouchertrans_amount"));
							}
							dataMap.put("credit", 0.00);
						} else {
							dataMap.put("debit", 0.00);
							if (rs.getString("voucher_vouchertype_id").equals("109") && rs.getString("vouchertrans_paymode_id").equals("2")) {
								dataMap.put("credit", rs.getDouble("vouchertrans_amount"));
							} else {
								dataMap.put("credit", rs.getDouble("vouchertrans_amount"));
							}
						}
						StringBuilder Str = new StringBuilder();
						SOP("4444444444444");
						if (rs.getString("vouchertrans_dc").equals("1")) {
							if (running_bal != 0.00) {
								running_bal = running_bal + Double.parseDouble(rs.getString("vouchertrans_amount"));
							} else {
								running_bal = oppbalance + Double.parseDouble(rs.getString("vouchertrans_amount"));
							}
							if (running_bal >= 0) {
								dataMap.put("runningbalance", running_bal);
								dataMap.put("drcr", "Dr");
							} else {
								dataMap.put("runningbalance", Double.parseDouble(String.valueOf(running_bal).substring(1, String.valueOf(running_bal).length())));
								dataMap.put("drcr", "Cr");
							}
						} else {
							if (running_bal != 0.00) {
								running_bal = running_bal - Double.parseDouble(rs.getString("vouchertrans_amount"));
							} else {
								running_bal = oppbalance - Double.parseDouble(rs.getString("vouchertrans_amount"));
							}
							if (running_bal >= 0) {
								dataMap.put("runningbalance", running_bal);
								dataMap.put("drcr", "Dr");
							} else {
								dataMap.put("runningbalance", Double.parseDouble(String.valueOf(running_bal).substring(1, String.valueOf(running_bal).length())));
								dataMap.put("drcr", "Cr");
							}
						}
						SOP("3333333333");
						SOP("dataMap=====" + dataMap);
						dataList.add(dataMap);
					}
				}

				summary_oppbalance = oppbalance;
				summery_runningbalance = oppbalance;
				rs.beforeFirst();
				// dataMap = new HashMap();
				while (rs.next()) {
					if (rs.getString("vouchertrans_dc").equals("1")) {
						summery_runningbalance += rs.getDouble("vouchertrans_amount");
						summary_drtrans_total += rs.getDouble("vouchertrans_amount");
					} else if (rs.getString("vouchertrans_dc").equals("0")) {
						summery_runningbalance -= rs.getDouble("vouchertrans_amount");
						summary_crtrans_total += rs.getDouble("vouchertrans_amount");
					}
				}
				rs.close();
				if (summery_runningbalance < 0) {
					dataMap.put("current_balance_dr", Double.parseDouble(df.format(summery_runningbalance).substring(1)));
					dataMap.put("current_balance_cr", 0.00);
				} else {
					dataMap.put("current_balance_dr", 0.00);
					dataMap.put("current_balance_cr", Double.parseDouble(df.format(summery_runningbalance)));
				}
				if (summary_oppbalance >= 0) {
					dataMap.put("transaction_total_dr", Double.parseDouble(df.format((summary_drtrans_total + summary_oppbalance))));
					dataMap.put("transaction_total_cr", Double.parseDouble(df.format(summary_crtrans_total)));
				} else if (summary_oppbalance < 0) {
					dataMap.put("transaction_total_dr", Double.parseDouble(df.format(summary_drtrans_total)));
					dataMap.put("transaction_total_cr", Double.parseDouble(df.format(summary_crtrans_total + Double.parseDouble(String.valueOf(summary_oppbalance).substring(1)))));
				}
				if (summery_runningbalance < 0) {
					dataMap.put("total_dr", Double.parseDouble(df.format((summary_drtrans_total + summary_oppbalance) - (summery_runningbalance))));
				} else {
					dataMap.put("total_dr", Double.parseDouble(df.format(summary_drtrans_total + summary_oppbalance)));
				}
				if (summery_runningbalance > 0) {
					dataMap.put("total_cr", Double.parseDouble(df.format(summary_crtrans_total + summery_runningbalance)));
				} else {
					dataMap.put("total_cr", Double.parseDouble(df.format(summary_crtrans_total)));
				}
				// SOP("summary_oppbalance=="+summary_oppbalance);
				// dataList.add(dataMap);
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return dataList;

	}

	public String DisplaySummary(String StrSql_Summary, StringBuilder Str) {
		double summary_oppbalance = 0.00, summery_runningbalance = 0.00, summary_drtrans_total = 0.00, summary_crtrans_total = 0.00;
		Str.append("");
		try {
			summary_oppbalance = oppbalance;
			summery_runningbalance = oppbalance;
			CachedRowSet rs = processQuery(StrSql_Summary, 0);
			while (rs.next()) {
				if (rs.getString("vouchertrans_dc").equals("1")) {
					summery_runningbalance += rs.getDouble("vouchertrans_amount");
					summary_drtrans_total += rs.getDouble("vouchertrans_amount");
				} else if (rs.getString("vouchertrans_dc").equals("0")) {
					summery_runningbalance -= rs.getDouble("vouchertrans_amount");
					summary_crtrans_total += rs.getDouble("vouchertrans_amount");
				}
			}
			rs.close();
			Str.append("<td valign=top colspan=6 align=right><b>").append("Current Balance:").append("<b></td>\n");
			if (summery_runningbalance < 0) {
				Str.append("<td valign=top align=right><b>");
				Str.append(df.format(summery_runningbalance).substring(1));
				Str.append("</b></td>\n");
				Str.append("<td valign=top align=right>");
				Str.append("&nbsp;");
				Str.append("</td>\n");
			} else {
				Str.append("<td valign=top align=right>");
				Str.append("&nbsp;");
				Str.append("</td>\n");
				Str.append("<td valign=top align=right  width=\"8%\"><b>");
				Str.append(df.format(summery_runningbalance));
				Str.append("</b></td>\n");
			}
			Str.append("<td valign=top align=right>").append("&nbsp;")
					.append("</td>\n");
			Str.append("</tr>\n");

			Str.append("<tr>\n");
			Str.append("<td valign=top colspan=6 align=right>")
					.append("<b>Transaction Total:</b>").append("</td>\n");
			if (summary_oppbalance >= 0) {
				Str.append("<td valign=top align=right><b>");
				Str.append(df
						.format((summary_drtrans_total + summary_oppbalance)));
				Str.append("</b></td>\n");
				Str.append("<td valign=top align=right><b>");
				Str.append(df.format(summary_crtrans_total));
				Str.append("</b></td>\n");
			} else if (summary_oppbalance < 0) {
				Str.append("<td valign=top align=right><b>");
				Str.append(df.format(summary_drtrans_total));
				Str.append("</b></td>\n");
				Str.append("<td valign=top align=right><b>");
				Str.append(df.format(summary_crtrans_total
						+ Double.parseDouble(summary_oppbalance
								+ "".substring(1))));
				Str.append("</b></td>\n");
			}
			Str.append("<td valign=top align=right>").append("&nbsp;")
					.append("</td>\n");
			Str.append("</tr>\n");

			Str.append("<tr>\n");
			Str.append("<td valign=top align=right colspan=6>")
					.append("<b>Total:</b>").append("</td>\n");
			if (summery_runningbalance < 0) {
				Str.append("<td valign=top align=right><b>")
						.append(df
								.format((summary_drtrans_total + summary_oppbalance)
										- (summery_runningbalance)))
						.append("</b></td>\n");
			} else {
				Str.append("<td valign=top align=right><b>")
						.append(df.format(summary_drtrans_total
								+ summary_oppbalance)).append("</b></td>\n");
			}
			if (summery_runningbalance > 0) {
				Str.append("<td valign=top align=right><b>")
						.append(df.format(summary_crtrans_total
								+ summery_runningbalance))
						.append("</b></td>\n");
			} else {
				Str.append("<td valign=top align=right><b>")
						.append(df.format(summary_crtrans_total))
						.append("</b></td>\n");
			}
			Str.append("<td valign=top align=right>").append("&nbsp;")
					.append("</td>\n");
			Str.append("</tr>\n");

		} catch (Exception ex) {
			System.out.println(" AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateVoucherType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT vouchertype_id, vouchertype_name"
					+ " FROM  axela_acc_voucher_type"
					+ " ORDER BY vouchertype_name";
			CachedRowSet rs = processQuery(StrSql, 0);

			while (rs.next()) {
				Str.append("<option value=").append(
						rs.getString("vouchertype_id"));
				Str.append(StrSelectdrop(rs.getString("vouchertype_id"),
						vouchertype_id));
				Str.append(">").append(rs.getString("vouchertype_name"))
						.append("</option>\n");
			}
			rs.close();
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			Str.append("<option value=1").append(StrSelectdrop("1", orderby))
					.append(">Voucher").append("</option>\n");
			Str.append("<option value=2").append(StrSelectdrop("2", orderby))
					.append(">DR").append("</option>\n");
			Str.append("<option value=3").append(StrSelectdrop("3", orderby))
					.append(">CR").append("</option>\n");
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

}
