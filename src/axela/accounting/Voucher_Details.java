package axela.accounting;
//aJIt 20th November, 2012

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_Details extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String msg = "";
	public String entity_id = "0";
	public String emp_role_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	//
	public String curr_balance = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String[] blockname;
	public String block_name = "";
	public String AddRowGroupStr = "", ledgerid = "0";
	public String currentbal_opp_amount = "";
	// public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		HttpSession session = request.getSession(true);
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(PadQuotes(GetSession("emp_role_id", request) + ""));
				entity_id = CNumeric(PadQuotes(GetSession("entity_id", request))) + "";
				ledgerid = CNumeric(PadQuotes(request.getParameter("ledgerid")));
				QueryString = PadQuotes(request.getQueryString());

				if (!ledgerid.equals("0")) {
					LedgerInfo(ledgerid);
					curr_balance = currentbal_opp_amount;
					StrHTML = curr_balance + "";
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}
	public void LedgerInfo(String customer_id) throws SQLException {
		StrSql = "SELECT currentbal_opp_amount FROM  " + compdb(comp_id) + "axela_acc_currentbal "
				+ "WHERE currentbal_company_id =" + comp_id
				+ " AND currentbal_customer_id = " + customer_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		while (crs.next()) {
			currentbal_opp_amount = crs.getString("currentbal_opp_amount");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doGet(request, response);
	}
}
