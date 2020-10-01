// Ved Prakash (12 Feb 2013)
package axela.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Branch_Email extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String comp_id = "0";
	public String emp_id = "";
	public String branch_name = "";
	public String branch_email_server = "";
	public String branch_email_username = "";
	public String branch_email_password = "";
	public String branch_email_port = "";
	public String branch_email_ssl = "";
	public String branch_email_tls = "";

	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String emp_theme_id = "1";
	public String branch_entry_id = "0";
	public String branch_entry_date = "";
	public String branch_modified_id = "0";
	public String branch_modified_date = "";
	public String branch_entry_by = "";
	public String entry_date = "";
	public String branch_modified_by = "";
	public String modified_date = "";
	public String emailregex = "@";
	public DecimalFormat deci = new DecimalFormat("0.00");
	public int emp_count = empcount;
	public int active_empcount = 0;
	public String active = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_executive_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				status = "Update";
				if (!"yes".equals(updateB)) {
					PopulateFields();
				} else if (updateB.equals("yes")) {
					GetValues(request, response);
					branch_modified_id = CNumeric(GetSession("emp_id", request));
					branch_modified_date = ToLongDate(kknow());
					UpdateFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						msg = "Branch Email Settings Updated Successfully!";
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		branch_email_server = PadQuotes(request.getParameter("txt_branch_email_server"));
		branch_email_username = PadQuotes(request.getParameter("txt_branch_email_username"));
		branch_email_password = PadQuotes(request.getParameter("txt_branch_email_password"));
		branch_email_port = PadQuotes(request.getParameter("txt_branch_email_port"));
		branch_email_ssl = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_email_ssl")));
		branch_email_tls = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_email_tls")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		branch_entry_by = PadQuotes(request.getParameter("branch_entry_by"));
		branch_modified_by = PadQuotes(request.getParameter("branch_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		if (branch_email_server.equals("")) {
			msg = "<br>Enter Email server!";

		}
		// SOP(oldpass+"<br>"+emp_upass);

		if (branch_email_username.equals("")) {
			msg = msg + "<br>Enter Email username!";

		}
		if (branch_email_password.equals("")) {
			msg = msg + "<br>Enter Email password!";

		}
		if (branch_email_port.equals("")) {
			msg = msg + "<br>Enter Port!";
		}
		if (!branch_email_server.equals("")) {
			if (branch_email_server.length() < 4) {
				msg = msg + "<br>Enter Atleast 4 Characters for Email Server";
			}
		}
		if (!branch_email_username.equals("")) {
			if (branch_email_username.length() < 4) {
				msg = msg + "<br>Enter Atleast 4 Characters for Email Username";
			}
		}

		if (!branch_email_password.equals("")) {
			if (branch_email_password.length() < 4) {
				msg = msg + "<br>Enter Atleast 4 Characters for Password";
			}

		}

		if (!branch_email_port.equals("")) {
			if (branch_email_port.length() <= 2) {
				msg = msg + "<br>Enter Atleast 2 Characters for Port";
			}
		}

	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_branch"
						+ " SET branch_email_server = '" + branch_email_server + "',"
						+ " branch_email_username = '" + branch_email_username + "',"
						+ " branch_email_password = '" + branch_email_password + "',"
						+ " branch_email_port = '" + branch_email_port + "',"
						+ " branch_email_ssl = '" + branch_email_ssl + "',"
						+ " branch_email_tls = '" + branch_email_tls + "',"
						+ "	branch_modified_id = " + branch_modified_id + ","
						+ " branch_modified_date = '" + branch_modified_date + "'"
						+ " WHERE branch_id = " + branch_id + "";

				// SOP("StrSql=UpdateFields==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception e) {
				msg = "<br>Transaction Error!";
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}

	protected void PopulateFields() {
		try {

			StrSql = "SELECT branch_id,"
					+ "	branch_email_server,"
					+ "	branch_email_username,"
					+ "	branch_email_password,"
					+ "	branch_email_port,"
					+ "	branch_email_ssl,"
					+ " branch_email_tls,"
					+ "	branch_entry_id,"
					+ "	branch_entry_date,"
					+ "	branch_modified_id,"
					+ "	branch_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id = " + branch_id + "";
			// SOP("StrSql====PopulateFields====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				// branch_id = crs.getString("branch_id");
				// .... Email.....
				branch_email_server = crs.getString("branch_email_server");
				branch_email_username = crs.getString("branch_email_username");
				branch_email_password = crs.getString("branch_email_password");
				branch_email_port = crs.getString("branch_email_port");
				branch_email_ssl = crs.getString("branch_email_ssl");
				branch_email_tls = crs.getString("branch_email_tls");

				branch_entry_id = crs.getString("branch_entry_id");
				if (!branch_entry_id.equals("0")) {
					branch_entry_by = Exename(comp_id, Integer.parseInt(branch_entry_id));
				}
				entry_date = strToLongDate(crs.getString("branch_entry_date"));
				branch_modified_id = crs.getString("branch_modified_id");
				if (!branch_modified_id.equals("0")) {
					branch_modified_by = Exename(comp_id, Integer.parseInt(branch_modified_id));
					modified_date = strToLongDate(crs.getString("branch_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
