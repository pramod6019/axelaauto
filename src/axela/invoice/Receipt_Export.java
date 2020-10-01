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

public class Receipt_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "receipt-export.jsp";

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
				if (!GetSession("receiptstrsql", request).equals("")) {
					StrSearch = GetSession("receiptstrsql", request);
				}

				if (exportB.equals("Export")) {
					ReceiptDetails(request, response);

				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ReceiptDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT receipt_id as 'ID',"
					+ " concat(branch_name,' (', branch_code, ')') as Branch , "
					+ " coalesce(concat(branch_code,invoice_no),'') as 'Invoice No.',"
					+ " concat(receipt_prefix,receipt_no)  as 'Receipt No.',"
					+ " DATE_FORMAT(receipt_date, '%d/%m/%Y') as Date,"
					+ " receipt_amount as 'Total',"
					+ " coalesce(paymode_name,'') as 'Paymode Name',"
					+ " coalesce(receipt_cheque_no,'') as 'Cheque No.',"
					+ " if(receipt_cheque_date != '',DATE_FORMAT(receipt_cheque_date, '%d/%m/%Y'),'') as 'Cheque Date',"
					+ " coalesce(receipt_cheque_bank,'') as 'Bank',"
					+ " if(receipt_active=1,'yes','no') as Active,"
					+ " coalesce(emp_name,'') as 'Employee Name',"
					+ " coalesce(receipt_refno,'') as 'Reference No.',"
					+ " coalesce(receipt_notes,'') as 'Notes' "
					+ " FROM " + compdb(comp_id) + "axela_invoice_receipt "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=receipt_branch_id "
					+ " inner join axela_acc_paymode on receipt_paymode_id = paymode_id "
					+ " inner join " + compdb(comp_id) + "axela_invoice on invoice_id = receipt_invoice_id "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = receipt_emp_id "
					+ " where 1=1 " + StrSearch + ""
					+ " group by receipt_id order by receipt_id desc"
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
		print = print + "<option value =ReceiptDetails" + StrSelectdrop("ReceiptDetails", printoption) + ">Receipt Details</option>\n";
		return print;
	}
}
