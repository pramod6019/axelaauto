package axela.sales;
//aJIt 11th March, 2013

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Update_Kms_Check extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String addB = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String salesgatepass_id = "0";
	public String salesgatepass_testdriveveh_id = "0";
	public String salesgatepass_in_kms = "0", salesgatepass_out_kms = "0";
	public String emp_id = "0";

	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					addB = PadQuotes(request.getParameter("add_button"));
					emp_id = CNumeric(GetSession("emp_id", request));
					salesgatepass_id = PadQuotes(request.getParameter("salesgatepass_id"));
					salesgatepass_testdriveveh_id = PadQuotes(request.getParameter("salesgatepass_testdriveveh_id"));
					salesgatepass_in_kms = PadQuotes(request.getParameter("salesgatepass_in_kms"));
					if (addB.equals("yes")) {
						UpdateKms();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							msg = "In Kms updated";
						}
					}
					StrHTML = msg;

				}
			} else {
				StrHTML = "Access Denied!";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		StrSql = "SELECT"
				+ " salesgatepass_out_kms"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
				+ " WHERE salesgatepass_id = " + salesgatepass_id + "";
		salesgatepass_out_kms = ExecuteQuery(StrSql);
		if (Double.parseDouble(salesgatepass_out_kms) > Double.parseDouble(salesgatepass_in_kms)) {
			msg = msg + "<br>In KMS cannot be less than Out KMS!";
		}
	}

	protected void UpdateKms() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
						+ " SET "
						+ " salesgatepass_in_kms= " + salesgatepass_in_kms + ","
						+ " salesgatepass_inkms_entry_id= " + emp_id + ","
						+ " salesgatepass_inkms_entry_time= " + ToLongDate(kknow()) + ""
						+ " WHERE salesgatepass_id = " + salesgatepass_id + "";
				// SOP("Update==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
