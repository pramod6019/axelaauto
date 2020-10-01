package axela.invoice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Configure_Invoice extends Connect {

	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String name = "";
	public String config_invoice_reduce_current_stock = "0";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);

			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			updateB = PadQuotes(request.getParameter("update_button"));
			msg = PadQuotes(request.getParameter("msg"));
			PopulateFields();
			if (updateB.equals("Update")) {
				config_invoice_reduce_current_stock = CheckBoxValue(PadQuotes(request.getParameter("chk_config_invoice_reduce_current_stock")));
				if (msg.equals("")) {
					UpdateFields();
					response.sendRedirect(response.encodeRedirectURL("configure-invoice.jsp?&msg=Configuration details updated successfully for Invoice!"));
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

	protected void PopulateFields() {
		StrSql = "SELECT config_invoice_reduce_current_stock"
				+ " FROM  " + compdb(comp_id) + "axela_config";
		config_invoice_reduce_current_stock = ExecuteQuery(StrSql);
	}

	protected void UpdateFields() {
		StrSql = "UPDATE  " + compdb(comp_id) + "axela_config"
				+ " SET"
				+ " config_invoice_reduce_current_stock = '" + config_invoice_reduce_current_stock + "'";
		updateQuery(StrSql);
	}
}
