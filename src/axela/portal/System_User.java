package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class System_User extends Connect {

	public String updateB = "";
	public static String msg = "";
	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_recperpage;
	public String emp_timeout = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				updateB = PadQuotes(request.getParameter("update_button"));
				PopulateFields();
				if ("Update".equals(updateB)) {
					GetValues(request, response);
					UpdateFields(request);
					session.setMaxInactiveInterval(Integer.parseInt(emp_timeout) * 60);
					response.sendRedirect(response.encodeRedirectURL("system-user.jsp?&msg=User Configuration has been updated succesfully!"));
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		emp_recperpage = PadQuotes(request.getParameter("drop_recperpage"));
		if (emp_recperpage.equals("1")) {
			emp_recperpage = "10";
		}
		if (emp_recperpage.equals("2")) {
			emp_recperpage = "25";
		}
		emp_timeout = CNumeric(PadQuotes(request.getParameter("txt_emp_timeout")));
		if (Integer.parseInt(emp_timeout) < 10) {
			emp_timeout = "10";
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_recperpage = '" + emp_recperpage + "',"
					+ " emp_timeout = '" + emp_timeout + "'"
					+ " where emp_id = " + emp_id + "";
			// SOP("StrSql====Update====="+StrSql);
			updateQuery(StrSql);
			SetSession("emp_recperpage", emp_recperpage, request);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}

	}
	public void PopulateFields() {
		try {
			StrSql = "SELECT emp_recperpage, emp_timeout"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + emp_id + "";
			// SOP("StrSql======"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					emp_recperpage = crs.getString("emp_recperpage");
					emp_timeout = crs.getString("emp_timeout");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateRecperpage() {
		emp_recperpage = ExecuteQuery("Select emp_recperpage, emp_timeout from " + compdb(comp_id) + "axela_emp where  emp_id=" + emp_id + "");
		if (emp_recperpage.equals("10")) {
			emp_recperpage = "1";
		}
		if (emp_recperpage.equals("25")) {
			emp_recperpage = "2";
		}
		String rpp = "<option value = 1" + Selectdrop(1, emp_recperpage) + ">10</option>";
		rpp = rpp + "<option value = 2" + Selectdrop(2, emp_recperpage) + ">25</option>\n";
		return rpp;
	}
}
