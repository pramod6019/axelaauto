package cloudify.connect;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PrintToPDF extends PageSize {

	Connect ct = new Connect();

	public void Export(HttpServletRequest request, HttpServletResponse response, CachedRowSet crs, String filename, String size) throws IOException {
		StringBuilder Str = new StringBuilder();
		try {
			// size = "A1";
			String CountColumn = "";
			int rowcount = 0;
			int count = 1;
			response.setContentType("application/pdf");
			Str.append("<html>");
			Str.append("<HEAD>");
			Str.append("</HEAD>");
			Str.append("<body>");
			while (crs.next()) {
				String customername = crs.getString("customer_name");
				if (!crs.getString("customer_phone1").equals("")) {
					customername = customername + " <br>" + crs.getString("customer_phone1") + " ";
				}
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=listtable >\n");
				Str.append("<tr>");
				Str.append("<td colspan='2'><b>Pro Forma Invoice</b>");
				Str.append("</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td rowspan='3' valign='top' >" + crs.getString("customer_name") + "</td>");
				Str.append("<td valign='top'> Date: " + ct.strToShortDate(crs.getString("quote_date")) + "</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td>Quote ID:" + crs.getString("quote_id") + "</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td>Quote No.:" + crs.getString("quote_no") + "</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td width='50%' valign='top'>Billing Address<br>" + crs.getString("quote_bill_address") + "</td>");
				Str.append("<td width='50%' valign='top'>Shipping Address<br>" + crs.getString("quote_ship_address") + "</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td height='200' colspan='2' align='center'>Items</td>");
				Str.append("</tr>");
				if (!crs.getString("quote_desc").equals("")) {
					Str.append("<tr>");
					Str.append("<td colspan='2'>Description" + crs.getString("quote_desc") + "</td>");
					Str.append("</tr>");
				}
				// if (!crs.getString("quote_terms").equals("")) {
				// Str.append("<tr>");
				// Str.append("<td colspan='2'>Terms" + crs.getString("quote_terms") + "</td>");
				// Str.append("</tr>");
				// }
				// Str.append("<tr>");
				// Str.append("<td align='center'>&nbsp;</td>");
				// Str.append("<td align='right'>Executive<br>Desig" + crs.getString("emp_name") + "</td>");
				// Str.append("</tr>");
				Str.append("</table>");

			}
			// if (crs.isBeforeFirst()) {
			// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=listtable >\n");
			// while (crs.next()) {
			// String customername = crs.getString("customer_name");
			// if (!crs.getString("customer_phone1").equals("")) {
			// customername = customername + " <br>" + crs.getString("customer_phone1") + " ";
			// }
			// if (!crs.getString("customer_mobile1").equals("")) {
			// customername = customername + " <br>" + crs.getString("customer_mobile1") + " ";
			// }
			// if (!crs.getString("customer_email1").equals("")) {
			// customername = customername + " <br>" + crs.getString("customer_email1") + " ";
			// }
			// String exename = crs.getString("emp_name");
			// if (!crs.getString("emp_ref_no").equals("")) {
			// exename = exename + " (" + crs.getString("emp_ref_no") + ")";
			// }
			// Str.append("<table width='700' border='1' cellspacing='0' cellpadding='0'>");
			// Str.append("<tr>");
			// Str.append("<td colspan='2'><b>Pro Forma Invoice</b></td>");
			// Str.append("</tr><tr>");
			// Str.append("<td rowspan='3' valign='top' >" + customername + "</td>");
			// Str.append("<td valign='top'> Date: " + ct.strToShortDate(crs.getString("quote_date")) + "</td>");
			// Str.append("</tr><tr>");
			// Str.append("<td >Quote ID:" + crs.getString("quote_id") + "</td>");
			// Str.append("</tr><tr>");
			// Str.append("<td>Quote No.:" + crs.getString("quote_no") + "</td>");
			// Str.append("</tr><tr>");
			// Str.append("<td width='50%' valign='top' scope='col'>Billing Address<br>" + crs.getString("quote_bill_address") + "</td>");
			// Str.append("<td width='50%' valign='top' scope='col'>Shipping Address<br>" + crs.getString("quote_ship_address") + "</td>");
			// Str.append("</tr><tr>");
			// Str.append("<td height='200' colspan='2' align='center'>Items</td>");
			// Str.append("</tr>");
			// if (!crs.getString("quote_desc").equals("")) {
			// Str.append("<tr>");
			// Str.append("<td colspan='2' scope='col'>Description" + crs.getString("quote_desc") + "</td>");
			// Str.append("</tr>");
			// }
			// if (!crs.getString("quote_terms").equals("")) {
			// Str.append("<tr>");
			// Str.append("<td colspan='2' scope='col'>Terms" + crs.getString("quote_terms") + "</td>");
			// Str.append("</tr>");
			// }
			// Str.append("<tr>");
			// Str.append("<td align='center' scope='col'>&nbsp;</td>");
			// Str.append("<td align='right' scope='col'>Executive<br>Desig" + exename + "</td>");
			// Str.append("</tr>");
			// Str.append("</table>");
			// }
			//
			// Str.append("</table>");
			// } else {
			// Str.append("No Records Found");
			// }
			Str.append("</body>");
			Str.append("</html>");
			Document document = new Document();
			if (size.equals("A0")) {
				document.setPageSize(PageSize.A0.rotate());
			} else if (size.equals("A1")) {
				document.setPageSize(PageSize.A1.rotate());
			} else if (size.equals("A2")) {
				document.setPageSize(PageSize.A2.rotate());
			} else if (size.equals("A3")) {
				document.setPageSize(PageSize.A3.rotate());
			} else {
				document.setPageSize(PageSize.A4.rotate());
			}
			PdfWriter pdfwriter = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, new StringReader(Str.toString()));
			document.close();
		} catch (Exception ex) {
			ct.SOPError("Axelaauto== " + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
}
