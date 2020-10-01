package axela.sales;
//divya nov 29th

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.SO_Update;
import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Veh_SoToSi extends Connect {
	// ///// /List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"veh-salesorder.jsp\">Sales Orders</a>"
			+ " &gt; <a href=\"veh-salesorder-list.jsp?all=yes\">List Sales Orders</a>:";
	public String LinkExportPage = "veh-salesorder-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String so_id = "0";
	public String so_quote_id = "0", so_customer_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String start_date = "";
	public String startdate = "";
	public String end_date = "";
	public String enddate = "";
	public String emp_all_exe = "";
	public int TotalRecords = 0;
	public String allinvoice = "";
	public String config_sales_enquiry_refno = "";
	public String voucher_customer_id = "0";
	public String contact_id = "0";
	public String voucher_contact_id = "0";
	public String voucher_rateclass_id = "0";
	public String voucher_so_id = "0";
	public String so_branch_id = "0";
	public String branch_id = "0";
	public String adddirect = "1", TotalRescords = "";
	public String add = "yes";
	public String vouchertype_id = "6";
	public String voucherclass_id = "6";
	public String addSingle = "no";

	public String voucher_downpayment = "0";
	public String voucher_cashdiscount = "0";
	public String voucher_turnoverdisc = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public SO_Update soupdate = new SO_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				so_quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				so_customer_id = CNumeric(PadQuotes(request.getParameter("so_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				startdate = PadQuotes(request.getParameter("txt_startdate"));
				enddate = PadQuotes(request.getParameter("txt_enddate"));
				PopulateConfigDetails(comp_id);
				allinvoice = PadQuotes(request.getParameter("allinvoice"));
				CheckForm();

				StrSearch += BranchAccess + ExeAccess.replace("emp_id", "so_emp_id");
				if (msg.equals("")) {
					StrHTML = Listdata(comp_id, request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(String comp_id, HttpServletRequest request, HttpServletResponse response) {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		if (msg.equals("") || !msg.equals("")) {
			try {

				StrSql = "SELECT so_id, contact_id,"
						+ " customer_id, branch_rateclass_id,"
						+ " so_branch_id,so_booking_amount"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
						+ " WHERE 1 = 1";
				if (addSingle.equals("no")) {
					StrSql += " AND so_id NOT IN ("
							+ " SELECT voucher_so_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE 1 = 1 AND voucher_vouchertype_id = '6'"
							+ " AND so_date >= " + start_date
							+ " AND so_date <= " + end_date + " )"
							+ " AND so_date >= " + start_date
							+ " AND so_date <= " + end_date + "";
				} else {
					StrSql += " AND so_id = '" + so_id + "'";
				}
				// SOP("StrSql===SO List===" + StrSqlBreaker(StrSql));
				crs = processQuery(StrSql, 0);
				crs.last();
				TotalRecords = crs.getRow();

				if (TotalRecords != 0) {
					crs.beforeFirst();
					if (allinvoice.equals("Add Invoive")) {
						while (crs.next()) {

							adddirect = "1";
							add = "yes";
							vouchertype_id = "6";
							voucherclass_id = "6";
							voucher_customer_id = PadQuotes(crs.getString("customer_id"));
							contact_id = PadQuotes(crs.getString("contact_id"));
							voucher_contact_id = PadQuotes(crs.getString("contact_id"));
							voucher_rateclass_id = PadQuotes(crs.getString("branch_rateclass_id"));
							voucher_so_id = PadQuotes(crs.getString("so_id"));
							so_branch_id = PadQuotes(crs.getString("so_branch_id"));
							branch_id = PadQuotes(crs.getString("so_branch_id"));
							voucher_downpayment = CNumeric(PadQuotes(crs.getString("so_booking_amount")));
							voucher_cashdiscount = "0";
							voucher_turnoverdisc = "0";

							AddAllInvoiceThread AddAllInvoiceThread = new AddAllInvoiceThread(
									comp_id, adddirect, add, voucher_customer_id, contact_id,
									voucher_contact_id, voucher_rateclass_id, voucher_so_id, so_branch_id,
									branch_id, voucher_downpayment, request, response);

							Thread thread = new Thread(AddAllInvoiceThread);

							thread.start();
							if (addSingle.equals("no")) {
								Thread.sleep(200);
							}
							thread.join();
						}
					}
					Str.append("<br><br><br><br><font ><b>" + TotalRecords + " No Of Sales Order(s) found!</b></font><br><br>");
				} else {
					Str.append("<br><br><br><br><font color=\"red\"><b>No Sales Order(s) found!</b></font><br><br>");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}

		return Str.toString();
	}
	public void PopulateConfigDetails(String comp_id) {
		StrSql = "SELECT config_sales_enquiry_refno"
				+ " FROM " + compdb(comp_id) + "axela_config";
		config_sales_enquiry_refno = CNumeric(ExecuteQuery(StrSql));
	}

	public void CheckForm() {

		if (startdate.equals("")) {
			msg = msg + "<br>Select Start Date!";
		} else if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
			}
		}

		if (enddate.equals("")) {
			msg = msg + "<br>Select End Date!";
		} else if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (Long.parseLong(ConvertShortDateToStr(enddate)) > Long.parseLong(ToLongDate(kknow()))) {
					msg += "<br>End Date can't be greater than Today's Date!";
				} else if (!startdate.equals("") && !enddate.equals("") && Long.parseLong(start_date) > Long.parseLong(end_date)) {
					// msg = msg + "<br>Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}
	}

}
