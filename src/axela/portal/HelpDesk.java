package axela.portal;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class HelpDesk extends Connect {

	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String project_id = "20";
	public String url = "";
	public String parameters = "";
	Encrypt encrypt = new Encrypt();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			CheckPerm(comp_id, "emp_emaxpm", request, response);
			// SOP("comp_id====" + comp_id);
			if (!comp_id.equals("0")) {

				StrSql = "SELECT emp_name, emp_upass, jobtitle_desc, emp_sex, emp_phone1, emp_phone2,\n"
						+ " emp_mobile1, emp_mobile2, emp_email1, emp_email2, emp_address, emp_notes,\n"
						+ " COALESCE(branch_name,'Head Office')as branch_name\n"
						+ " FROM " + compdb(comp_id) + "axela_emp\n"
						+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id\n"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id\n"
						+ " WHERE emp_id = " + emp_id + "";
				// SOP("StrSql ==== " + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					parameters = URLEncoder.encode(encrypt.encrypt(
							"emp_id=" + emp_id
									+ "&project_id=11"// + project_id
									+ "&emp_name=" + crs.getString("emp_name")
									+ "&emp_jobtitle=" + crs.getString("jobtitle_desc")
									+ "&emp_gender=" + crs.getString("emp_sex")
									+ "&emp_mobile1=" + crs.getString("emp_mobile1")
									+ "&emp_mobile2=" + crs.getString("emp_mobile2")
									+ "&emp_phone1=" + crs.getString("emp_phone1")
									+ "&emp_phone2=" + crs.getString("emp_phone2")
									+ "&emp_email1=" + crs.getString("emp_email1")
									+ "&emp_email2=" + crs.getString("emp_email2")
									+ "&emp_address=" + crs.getString("emp_address")
									+ "&emp_branch=" + crs.getString("branch_name")
									+ "&emp_notes=" + crs.getString("emp_notes")
									+ "&emp_upass=" + crs.getString("emp_upass")
									+ "&comp_id=" + comp_id
									+ ""), "UTF-8");
				}
				crs.close();
				if (AppRun().equals("1")) {
					url = "http://pm.emax.in/pm/";
				} else {
					url = "http://localhost:7070/pm/";
				}
				url = url + "client/user-authentication.jsp?data=" + parameters;

				// SOP("url = " + url);
				response.sendRedirect(url);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
