package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Exe_Dash extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	DecimalFormat deci = new DecimalFormat("#");
	public String go = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + "" + ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
						SOP("TargetSearch" + StrSearch);
					}
					if (!dr_branch_id.equals("0")) {
						// TargetSearch = TargetSearch + " and emp_branch_id =" + dr_branch_id;
						StrSearch = StrSearch + " and  (emp_branch_id=" + dr_branch_id + " or emp_id= 1 or emp_id in (select empbr.emp_id from "
								+ " " + compdb(comp_id) + "axela_emp_branch empbr where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and "
								+ " empbr.emp_branch_id = " + dr_branch_id + "))";
						// SOP("TargetSearch" + StrSearch);

					}
					// if (!model_id.equals("")) {
					// // TargetSearch = TargetSearch + " and model_id in ("+model_id+")";
					// //enquiry_Model = " and enquiry_model_id in ("+model_id+")";
					// item_Model = " and item_model_id in (" + model_id + ")";
					// }
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (select teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id in (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListTarget();

					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListTarget() {
		int empid = 0, empcount = 0;
		int total_lead = 0, total_enquiry = 0;
		int total_enquiry_closed = 0;
		int total_enquiry_hot = 0, total_enquiry_open = 0, total_quote_count = 0, total_quote_kpi_count = 0;
		int total_quote_value = 0;
		int total_enquiry_meeting = 0, total_enquiry_call = 0, total_so_count = 0, total_so_value = 0;
		int total_enquiry_followup = 0, total_followup_esc = 0, total_invoice_count = 0, total_invoice_value = 0;
		int total_receipt_count = 0, total_receipt_value = 0;
		double salesratio = 0;
		double total_salesratio = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = " SELECT emp_id, emp_name, emp_ref_no, "
				+ " (select coalesce(count(lead_id),0) from " + compdb(comp_id) + "axela_sales_lead "
				+ " where lead_date>='" + starttime + "' and lead_date<'" + endtime + "'  and lead_emp_id=emp_id) as leadcount,"
				+ " (select coalesce(count(distinct enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry "
				+ " where enquiry_date>='" + starttime + "' and enquiry_date<'" + endtime + "'  and enquiry_emp_id=emp_id) as enquirycount,"
				+ " (select coalesce(count(distinct enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry"
				+ " where enquiry_status_id=1  and enquiry_emp_id=emp_id) as enquiryopen,"
				+ " (select coalesce(count(distinct enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry"
				+ " where enquiry_date>='" + starttime + "' and enquiry_date<'" + endtime + "'  and enquiry_status_id!=1 and "
				+ " enquiry_emp_id=emp_id) as enquiryclosed,"
				+ " (select coalesce(count(distinct enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry"
				+ " where 1=1  and (enquiry_priorityenquiry_id=1) and enquiry_status_id=1  and "
				+ " enquiry_emp_id=emp_id) as enquiryhot,"
				+ " (select coalesce(count(distinct followup_id),0) from " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " where followup_followup_time>='" + starttime + "' and followup_followup_time<'" + endtime + "' and "
				+ " followup_emp_id=emp_id) as enquiryfollowup,"
				+ " (select coalesce(count(distinct followup_id),0) from " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " where followup_followup_time>='" + starttime + "' and followup_followup_time<'" + endtime + "'  and "
				+ " followup_followuptype_id=1 and followup_emp_id=emp_id) as enquirycalls,"
				+ " (select coalesce(count(distinct followup_id),0) from " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " where followup_followup_time>='" + starttime + "' and followup_followup_time<'" + endtime + "'  and "
				+ " followup_followuptype_id=2 and followup_emp_id=emp_id) as enquirymeetings,"
				+ " (select coalesce(count(distinct followup_id),0) "
				+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " where followup_followup_time>='" + starttime + "' and followup_followup_time<'" + endtime + "'  and "
				+ " followup_trigger=5 and followup_emp_id=emp_id) as followupesc,"
				+ " (select coalesce(count(distinct quote_enquiry_id),0) from " + compdb(comp_id) + "axela_sales_quote"
				+ " where quote_date>='" + starttime + "' and quote_date<'" + endtime + "' " + item_Model + " and quote_active='1' "
				+ " and quote_emp_id=emp_id) as quotekpicount,"
				+ " (select coalesce(count(quote_id),0) from " + compdb(comp_id) + "axela_sales_quote"
				+ " where quote_date>='" + starttime + "' and quote_date<'" + endtime + "' " + item_Model + " and "
				+ " quote_active='1' and quote_emp_id=emp_id) as quotecount,"
				+ " (select coalesce(sum(quote_grandtotal),0) from " + compdb(comp_id) + "axela_sales_quote "
				+ " where quote_date>='" + starttime + "' and quote_date<'" + endtime + "'  " + item_Model + " and quote_active='1' and "
				+ "  quote_emp_id=emp_id) as quoteamount,"
				+ " (select coalesce(count(so_id)) from " + compdb(comp_id) + "axela_sales_so"
				+ " where so_date>='" + starttime + "' and so_date<'" + endtime + "' " + item_Model + " and so_active='1' "
				+ " and so_emp_id=emp_id) as socount,"
				+ " (select coalesce(sum(so_grandtotal),0) from " + compdb(comp_id) + "axela_sales_so"
				+ " where so_date>='" + starttime + "' and so_date<'" + endtime + "' " + item_Model + " and "
				+ "  so_active='1' and so_emp_id=emp_id) as soamount,"
				+ " (select coalesce(count(invoice_id)) from " + compdb(comp_id) + "axela_invoice"
				+ " where invoice_date>='" + starttime + "' and invoice_date<'" + endtime + "' " + item_Model + " and invoice_active='1' "
				+ " and invoice_emp_id=emp_id) as invoicecount,"
				+ " (select coalesce(sum(invoice_grandtotal),0) from " + compdb(comp_id) + "axela_invoice"
				+ " where invoice_date>='" + starttime + "' and invoice_date<'" + endtime + "' " + item_Model + " and invoice_active='1' "
				+ " and invoice_emp_id=emp_id) as invoiceamount,"
				+ " (select coalesce(count(receipt_id)) from " + compdb(comp_id) + "axela_invoice_receipt"
				+ " where receipt_date>='" + starttime + "' and receipt_date<'" + endtime + "' " + item_Model + " and receipt_active='1' "
				+ " and receipt_emp_id=emp_id) as receiptcount,"
				+ " (select coalesce(sum(receipt_amount),0) from " + compdb(comp_id) + "axela_invoice_receipt"
				+ " where receipt_date>='" + starttime + "' and receipt_date<'" + endtime + "' " + item_Model + " and receipt_active='1' "
				+ "  and receipt_emp_id=emp_id) as receiptamount"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				// + " left join " + compdb(comp_id) + "axela_branch on branch_id=emp_branch_id"
				+ " where emp_active='1' and emp_sales='1' " + StrSearch + "" + ExeAccess + ""
				+ " group by emp_id "
				+ " order by emp_name";
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Sales Consultant</th>\n");
				Str.append("<th>Lead</th>\n");
				Str.append("<th>Enquiry</th>\n");
				Str.append("<th>Open Enquiry</th>\n");
				Str.append("<th>Closed Enquiry</th>\n");
				Str.append("<th>Hot Enquiry</th>\n");
				Str.append("<th>FollowUp</th>\n");
				Str.append("<th>Calls</th>\n");
				Str.append("<th>Meetings</th>\n");
				Str.append("<th>Escalation</th>\n");
				Str.append("<th>Quotes</th>\n");
				Str.append("<th>KPI Quotes</th>\n");
				Str.append("<th>Quotes Amount</th>\n");
				Str.append("<th>Sales Orders</th>\n");
				Str.append("<th>Sales Ratio</th>\n");
				Str.append("<th>Sales Amount</th>\n");
				Str.append("<th>Invoices</th>\n");
				Str.append("<th>Invoices Amount</th>\n");
				Str.append("<th>Receipts</th>\n");
				Str.append("<th>Receipts Amount</th>\n");
				Str.append("</tr>\n");
				crs.last();
				int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					if (empid != crs.getInt("emp_id")) {
						empcount++;
						//
						empid = crs.getInt("emp_id");
					}
					total_lead = total_lead + crs.getInt("leadcount");
					total_enquiry = total_enquiry + crs.getInt("enquirycount");
					total_enquiry_closed = total_enquiry_closed + crs.getInt("enquiryclosed");
					total_enquiry_hot = total_enquiry_hot + crs.getInt("enquiryhot");
					total_enquiry_open = total_enquiry_open + crs.getInt("enquiryopen");
					total_enquiry_call = total_enquiry_call + crs.getInt("enquirycalls");
					total_enquiry_meeting = total_enquiry_meeting + crs.getInt("enquirymeetings");
					total_enquiry_followup = total_enquiry_followup + crs.getInt("enquiryfollowup");
					total_followup_esc = total_followup_esc + crs.getInt("followupesc");
					total_quote_count = total_quote_count + crs.getInt("quotecount");
					total_quote_kpi_count = total_quote_kpi_count + crs.getInt("quotekpicount");
					total_quote_value = total_quote_value + crs.getInt("quoteamount");
					total_so_count = total_so_count + crs.getInt("socount");
					total_so_value = total_so_value + crs.getInt("soamount");
					total_invoice_count = total_invoice_count + crs.getInt("invoicecount");
					total_invoice_value = total_invoice_value + crs.getInt("invoiceamount");
					total_receipt_count = total_receipt_count + crs.getInt("receiptcount");
					total_receipt_value = total_receipt_value + crs.getInt("receiptamount");
					salesratio = 0;
					if (!crs.getString("socount").equals("0") && !crs.getString("enquirycount").equals("0")) {
						salesratio = (crs.getDouble("socount") / crs.getDouble("enquirycount")) * 100;
						salesratio = Math.round(salesratio * 100.0) / 100.0;
					}
					if (total_enquiry != 0) {
						total_salesratio = (Double.parseDouble(Integer.toString(total_so_count)) / Double.parseDouble(Integer.toString(total_enquiry))) * 100;
						total_salesratio = Math.round(total_salesratio * 100.0) / 100.0;
					}

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + empcount + ".</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " ("
							+ crs.getString("emp_ref_no") + ")</a></td>");
					// / Lead
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("leadcount")
							+ "</a></td>");
					// / Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("enquirycount")
							+ "</a></td>");
					// / Open Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=open"
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("enquiryopen")
							+ "</a></td>");
					// / Closed Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=closed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryclosed") + "</a></td>");
					// / Hot Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=hot&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("enquiryhot")
							+ "</a></td>");
					// / Follow-up
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=followup&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryfollowup") + "</a></td>");
					// / Calls
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=meetings&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("enquirycalls")
							+ "</a></td>");
					// / Meetings
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=meetings&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquirymeetings") + "</a></td>");
					// / Escalation
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=escalation&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("followupesc")
							+ "</a></td>");
					// / Quotes
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=quote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("quotecount")
							+ "</a></td>");
					// / KPI Quotes
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("quotekpicount") + "</a></td>");
					// Quote Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ IndFormat(crs.getString("quoteamount")) + "</td>");
					// Sales Order Count
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("socount")
							+ "</a></td>");
					// /sales Ratio
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + salesratio + "</a></td>");
					// Sales Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ IndFormat(crs.getString("soamount")) + "</a></td>");
					// Invoice Count
					Str.append("<td valign=top align=right>" + crs.getInt("invoicecount") + "</td>");
					// Invoice Amount
					Str.append("<td valign=top align=right>" + IndFormat(crs.getString("invoiceamount")) + "</td>");
					// Receipt Countt
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>" + crs.getInt("receiptcount")
							+ "</a></td>");
					// Receipt Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ IndFormat(crs.getString("receiptamount")) + "</a></td>");
					Str.append("</tr>\n");
				}
				if (empcount > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
					Str.append("<td valign=top align=right><b>Total:</b></td>");
					// / Lead
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_lead + "</b></a></td>");
					// / Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry + "</b></a></td>");
					// / Open Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=open"
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_open + "</b></a></td>");
					// / Closed Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=closed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_closed + "</b></a></td>");
					// / Hot Enquiry
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=hot&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_hot + "</b></a></td>");
					// / Follow-up
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=followup&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_followup + "</b></a></td>");
					// / Calls
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=meetings&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_call + "</b></a></td>");
					// / Meetings
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=meetings&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquiry_meeting + "</b></a></td>");
					// / Escalation
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?enquiry=escalation&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_followup_esc + "</b></a></td>");
					// id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_enquirycustomertestdrivefeedback + "</b></a></td>");
					// / Quotes Count
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=quote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_quote_count + "</b></a></td>");
					// / KPI Quotes
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_quote_kpi_count + "</b></a></td>");
					// Quote Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + IndFormat(total_quote_value + "")
							+ "</b></a></td>");
					// Sales Order Count
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_so_count + "</b></a></td>");
					// Sales Ratio
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_salesratio + "</b></a></td>");
					// sales Order Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + IndFormat(total_so_value + "")
							+ "</b></a></td>");
					// Invoice Count
					Str.append("<td valign=top align=right><b>" + total_invoice_count + "</b></td>");
					// Invoice Amount
					Str.append("<td valign=top align=right><b>" + IndFormat(total_invoice_value + "") + "</b></td>");
					// Receipt Count
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + total_receipt_count + "</b></a></td>");
					// Receipt Amount
					Str.append("<td valign=top align=right><a href=../sales/Enquiry_Search_Check.do?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&emp_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>" + IndFormat(total_receipt_value + "")
							+ "</b></a></td>");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTeam() {
		String stringval = "";
		try {
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where 1=1 " + ExeAccess + ""
					+ " group by team_id"
					+ " order by team_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				stringval = stringval + "<option value=" + crs.getString("team_id") + "";
				stringval = stringval + ArrSelectdrop(crs.getInt("team_id"), team_ids);
				stringval = stringval + ">" + crs.getString("team_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			String exe = "";
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' and emp_sales='1' and (emp_branch_id = " + branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + branch_id + ")) " + ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id order by emp_name";
			// SOP("StrSql==== in PopulateExecutive" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("emp_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">" + (crs.getString("emp_name")) + "</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return exe = Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// public String PopulateModel() {
	// String stringval = "";
	// try {
	// String StrSql = "select model_id, model_name from " + compdb(comp_id) + "axela_inventory_item_model "
	// + " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id"
	// + " group by model_id order by model_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// // SOP("StrSql in PopulateCountry==========" + StrSql);
	// while (crs.next()) {
	// stringval = stringval + "<option value=" + crs.getString("model_id") + "";
	// stringval = stringval + ArrSelectdrop(crs.getInt("model_id"), model_ids);
	// stringval = stringval + ">" + crs.getString("model_name") + "</option>\n";
	// }
	//
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return stringval;
	// }
}
