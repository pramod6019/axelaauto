package axela.portal;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Dash1 extends Connect {

	public String submitB = "";
	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess = "";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String EnquiryCount = "0";
	public String EnquiryValue = "0";
	public String StudentCount = "0";
	public String StudentValue = "0";
	public String StudentRatio = "0";
	public String StudentValueRatio = "0";
	public String InvoiceCount = "0";
	public String InvoiceValue = "0";
	public String ReceiptCount = "0";
	public String ReceiptValue = "0";
	public String InstallCount = "0";
	public String InstallValue = "0";
	public String InstallReceiptCount = "0";
	public String InstallReceiptValue = "0";
	public String InstallRatio = "0";
	public String InstallValueRatio = "0";
	public String empexe_id = "";
	DecimalFormat deci = new DecimalFormat("#.##");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_mis_access", request, response);

				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				GetValues(request, response);
				CheckForm();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
		empexe_id = PadQuotes(request.getParameter("dr_executive"));
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
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
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
		if (!dr_branch_id.equals("") && !dr_branch_id.equals("0")) {
			StrSearch = " and branch_id=" + dr_branch_id;
		}
		if (msg.equals("")) {
			try {
				// enquiry
				StrSql = "SELECT count(student_id) as studentcount,"
						+ " coalesce(sum(student_course_total),0) as total"
						+ " from " + compdb(comp_id) + "axela_student"
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id= student_branch_id"
						+ " where 1=1 branch_active='1'" + BranchAccess + StrSearch
						+ " and student_active='1' "
						+ " and student_enquiry_date >= " + starttime
						+ " and student_enquiry_date <" + endtime;
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and student_counsel_emp_id=" + empexe_id;
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					EnquiryCount = crs.getString("studentcount");
					EnquiryValue = crs.getString("total");
				}
				crs.close();
				// student
				StrSql = " SELECT count(distinct(student_id)) as studentcount, "
						+ " coalesce(sum(invoice_grandtotal),0) as total "
						+ " from " + compdb(comp_id) + "axela_student"
						+ " inner join " + compdb(comp_id) + "axela_invoice on student_id= invoice_student_id "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id= student_branch_id "
						+ " where branch_active='1' " + BranchAccess + StrSearch
						+ " and student_active='1' and invoice_active='1' "
						+ " and student_enquiry_date >= " + starttime
						+ " and student_enquiry_date <=" + endtime
						+ " and invoice_date >= '" + starttime + "'"
						+ " and invoice_date < '" + endtime + "'";
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and invoice_emp_id=" + empexe_id;
				}
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					StudentCount = crs.getString("studentcount");
					StudentValue = crs.getString("total");
				}
				if (!EnquiryCount.equals("0")) {
					StudentRatio = deci.format(((Integer.parseInt(StudentCount) * 100) / Integer.parseInt(EnquiryCount)));
				}

				if (Double.parseDouble(EnquiryValue) != 0) {
					StudentValueRatio = deci.format(((Double.parseDouble(StudentValue) * 100) / Double.parseDouble(EnquiryValue)));
				}
				// invoice
				StrSql = " SELECT  count(invoice_id) as invoicecount, "
						+ " coalesce(sum(invoice_grandtotal),0) as total "
						+ " from " + compdb(comp_id) + "axela_invoice "
						+ "inner join " + compdb(comp_id) + "axela_branch on branch_id= invoice_branch_id "
						+ " where branch_active='1' and invoice_active='1' " + BranchAccess + StrSearch
						+ " and invoice_date >= '" + starttime + "'"
						+ " and invoice_date < '" + endtime + "'";
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and invoice_emp_id=" + empexe_id;
				}
				crs = processQuery(StrSql, 0);
				// SOP("StrSql----- " + StrSql);
				while (crs.next()) {
					InvoiceCount = crs.getString("invoicecount");
					InvoiceValue = crs.getString("total");
				}
				// receipt
				StrSql = " SELECT count(receipt_id) as receiptcount, "
						+ " coalesce(sum(receipt_total),0) as total "
						+ " from " + compdb(comp_id) + "axela_invoice_receipt "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id= receipt_branch_id "
						+ " where branch_active='1' and receipt_active='1' " + BranchAccess + StrSearch
						+ " and receipt_date >= '" + starttime + "'"
						+ " and receipt_date < '" + endtime + "'";
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and receipt_emp_id=" + empexe_id;
				}
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					ReceiptCount = crs.getString("receiptcount");
					ReceiptValue = crs.getString("total");
				}
				// installment
				StrSql = " SELECT  count(installtrack_id) as installcount,"
						+ " coalesce(sum(installtrack_amt),0) as amount"
						+ " from " + compdb(comp_id) + "axela_invoice_balance_track"
						+ " inner join " + compdb(comp_id) + "axela_invoice on installtrack_invoice_id= invoice_id"
						+ " inner join " + compdb(comp_id) + "axela_branch on invoice_branch_id= branch_id"
						+ " where invoice_active='1' and branch_active='1'" + BranchAccess + StrSearch
						+ " and installtrack_date >= '" + starttime + "'"
						+ " and installtrack_date < '" + endtime + "'";
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and invoice_emp_id=" + empexe_id;
				}
				// SOP("StrSql----- " + StrSql);
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					InstallCount = crs.getString("installcount");
					InstallValue = crs.getString("amount");
				}
				// installment with receipts
				StrSql = " SELECT  count(installtrack_id) as collCount,"
						+ " coalesce(sum(installtrack_amt),0) as collAmount"
						+ " from " + compdb(comp_id) + "axela_invoice_balance_track"
						+ " inner join " + compdb(comp_id) + "axela_invoice_receipt_trans on receipttrans_balancetrack_id = installtrack_id"
						+ " inner join " + compdb(comp_id) + "axela_invoice_receipt on receipttrans_receipt_id = receipt_id"
						+ " inner join " + compdb(comp_id) + "axela_invoice on installtrack_invoice_id = invoice_id"
						+ " inner join " + compdb(comp_id) + "axela_branch on invoice_branch_id = branch_id"
						+ " where invoice_active='1'"
						+ " and receipt_active='1'"
						+ " and branch_active='1'" + BranchAccess + StrSearch
						+ " and installtrack_date >= '" + starttime + "'"
						+ " and installtrack_date < '" + endtime + "'";
				if (!empexe_id.equals("") && !empexe_id.equals("0")) {
					StrSql = StrSql + " and receipt_emp_id=" + empexe_id;
				}
				// SOP("StrSql----- " + StrSql);
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					InstallReceiptCount = crs.getString("collCount");
					InstallReceiptValue = crs.getString("collAmount");
				}
				if (!InstallCount.equals("0")) {
					InstallRatio = deci.format(((Integer.parseInt(InstallReceiptCount) * 100) / Integer.parseInt(InstallCount)));
				}
				if (Double.parseDouble(InstallValue) != 0) {
					InstallValueRatio = deci.format((Double.parseDouble(InstallReceiptValue) * 100 / Double.parseDouble(InstallValue)));
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateExecutive() {
		try {
			String exe = "";

			String SqlStr = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " where 1=1  and emp_active='1'  and emp_branch_id = " + dr_branch_id
					+ " group by emp_id order by emp_name ";
			// SOP("SqlStr==== in PopulateExecutive" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			exe = "<select name=dr_executive id=dr_executive class=selectbox><option value = 0>Select</option>";
			if (!dr_branch_id.equals("0")) {
				while (crs.next()) {
					exe = exe + "<option value=" + crs.getString("emp_id") + "";
					exe = exe + Selectdrop(crs.getInt("emp_id"), empexe_id);
					exe = exe + ">" + (crs.getString("emp_name")) + "</option> \n";
				}
			}
			exe = exe + "</select>";
			crs.close();
			return exe;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
