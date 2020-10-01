package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Configure_Customer extends Connect {
	public String comp_id = "0";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String config_customer_soe = "";
	public String config_customer_sob = "";
	public String config_customer_dupnames = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				PopulateFields();
				if ("Update".equals(updateB)) {
					GetValues(request, response);
					if (msg.equals("")) {
						UpdateFields(request);
						response.sendRedirect(response.encodeRedirectURL("configure-customer.jsp?&msg=Configuration details updated successfully for Customers!"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		config_customer_soe = CheckBoxValue(PadQuotes(request.getParameter("chk_config_customer_soe")));
		config_customer_sob = CheckBoxValue(PadQuotes(request.getParameter("chk_config_customer_sob")));
		config_customer_dupnames = CheckBoxValue(PadQuotes(request.getParameter("chk_config_customer_dupnames")));
	}

	protected void PopulateFields() {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_config";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_customer_soe = crs.getString("config_customer_soe");
				config_customer_sob = crs.getString("config_customer_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_config"
					+ " SET"
					+ " config_customer_soe = '" + config_customer_soe + "',"
					+ " config_customer_sob = '" + config_customer_sob + "',"
					+ " config_customer_dupnames = '" + config_customer_dupnames + "'";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
