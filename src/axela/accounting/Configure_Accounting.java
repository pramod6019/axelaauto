package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Configure_Accounting extends Connect {

	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String config_invoice_terms = "";
	public String config_receipt_terms = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0"))
			{
				updateB = PadQuotes(request.getParameter("update_button"));
				// SOP("updateB======" + updateB);
				msg = PadQuotes(request.getParameter("msg"));
				PopulateFields();
				if ("Update".equals(updateB)) {
					GetValues(request, response);
					UpdateFields(request);
					response.sendRedirect(response.encodeRedirectURL("configure-accounting.jsp?&msg=Configuration details updated successfully for Accounting!"));
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		config_invoice_terms = PadQuotes(request.getParameter("txt_invoice_terms"));
		config_receipt_terms = PadQuotes(request.getParameter("txt_receipt_terms"));

	}

	protected void PopulateFields() {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_config ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_invoice_terms = crs.getString("config_invoice_terms");
				config_receipt_terms = crs.getString("config_receipt_terms");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_config Set "
					+ "config_invoice_terms = '" + config_invoice_terms + "',"
					+ "config_receipt_terms = '" + config_receipt_terms + "'";

			// SOP("SqlStr==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
