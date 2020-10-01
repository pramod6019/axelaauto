package axela.invoice;
//Murali 28th aug

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Payment_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "payment-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("paymentstrsql", request).equals("")) {
					StrSearch = GetSession("paymentstrsql", request);
				}

				if (exportB.equals("Export")) {
					PaymentDetails(request, response);

				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PaymentDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT payment_id as 'ID',"
					+ " concat(branch_name,' (', branch_code, ')') as Branch , "
					+ " payment_no  as 'Payment No.',"
					+ " DATE_FORMAT(payment_date, '%d/%m/%Y') as Date,"
					+ " payment_amount as 'Total',"
					+ " coalesce(paymode_name,'') as 'Paymode Name',"
					+ " coalesce(payment_cheque_no,'') as 'Cheque No.',"
					+ " if(payment_cheque_date != '',DATE_FORMAT(payment_cheque_date, '%d/%m/%Y'),'') as 'Cheque Date',"
					+ " coalesce(payment_cheque_bank,'') as 'Bank',"
					+ " if(payment_active=1,'yes','no') as Active,"
					+ " coalesce(emp_name,'') as 'Employee Name',"
					+ " coalesce(payment_refno,'') as 'Reference No.',"
					+ " coalesce(payment_notes,'') as 'Notes' "
					+ " FROM " + compdb(comp_id) + "axela_invoice_payment "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=payment_branch_id "
					+ " inner join axela_acc_paymode on payment_paymode_id = paymode_id "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = payment_emp_id "
					+ " where 1=1 " + StrSearch + ""
					+ " group by payment_id order by payment_id desc"
					+ " limit " + exportcount;
			// SOP("StrSql==ajju=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A4", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =PaymentDetails" + StrSelectdrop("PaymentDetails", printoption) + ">Payment Details</option>\n";
		return print;
	}
}
