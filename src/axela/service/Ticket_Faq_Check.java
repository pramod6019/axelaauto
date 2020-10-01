/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

/*
 * @author Gurumurthy TS 11 FEB 2013
 */
public class Ticket_Faq_Check extends Connect {

	public String StrHTML = "";
	public String comp_id = "0";
	public String faqservice_id = "0";
	public String ticket_dept_id = "0";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			ticket_dept_id = CNumeric(PadQuotes(request.getParameter("ticket_dept_id"))).trim();

			if (!ticket_dept_id.equals("0") && !ticket_dept_id.equals("")) {
				String str = "";
				StrSql = "Select faqservice_id, faqservice_name"
						+ " from " + compdb(comp_id) + "axela_service_ticket_faq_service"
						+ " where faqservice_ticket_dept_id=" + ticket_dept_id + ""
						+ " order by faqservice_name";
				try {
					CachedRowSet crs1 = processQuery(StrSql, 0);
					str = "<select name=dr_cat id=dr_cat class=form-control ><option value = 0>Select</option>";
					int count = 1;
					while (crs1.next()) {
						str = str + "<option value=" + crs1.getString("faqservice_id") + "";
						str = str + ">" + crs1.getString("faqservice_name") + "</option> \n";
						count++;
					}
					str = str + "</select>";
					crs1.close();
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
