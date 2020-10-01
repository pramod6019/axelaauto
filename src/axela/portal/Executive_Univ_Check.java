package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Executive_Univ_Check extends Connect {

	public String updateB = "";
	public static String msg = "";
	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_recperpage;
	Executives_Update updateexe = new Executives_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				// SOP("hi");
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				updateB = PadQuotes(request.getParameter("update_button"));
				if ("Update".equals(updateB)) {
					UpdateUniversalEmp("0", comp_id);
					response.sendRedirect(response.encodeRedirectURL("executive-univ-check.jsp?&msg=Universal Executive Table Updated Succesfully!"));
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public void UpdateUniversalEmp(String emp_id, String comp_id) {
		StrSql = " DELETE FROM " + maindb() + "uni_emp "
				+ " WHERE 1=1"
				+ " AND comp_id = " + comp_id;
		if (!emp_id.equals("0")) {
			StrSql += " AND emp_id = " + emp_id + "";
		}
		updateQuery(StrSql);

		StrSql = "SELECT emp_id, " + comp_id + ", emp_uuid,"
				+ " emp_name, emp_role_id, emp_sales, "
				+ " emp_upass, emp_email1, emp_branch_id,"
				+ " emp_active, emp_all_exe, emp_device_id,"
				+ " emp_device_fcmtoken, emp_device_os,"
				+ " emp_ip_access, emp_recperpage,"
				+ " emp_entry_id, emp_entry_date,"
				+ " emp_modified_id, emp_modified_date"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE 1=1 "
				+ " AND emp_active = 1";
		// + " AND emp_sales = 1";
		if (!emp_id.equals("0")) {
			StrSql += " AND emp_id = " + emp_id + "";
		}

		StrSql = " INSERT INTO " + maindb() + "uni_emp " + "( emp_id,"
				+ " comp_id, emp_uuid, emp_name,"
				+ " emp_role_id, emp_sales, emp_upass, "
				+ " emp_email1, emp_branch_id, emp_active,"
				+ " emp_all_exe, emp_device_id,"
				+ " emp_device_fcmtoken, emp_device_os, emp_ip_access,"
				+ " emp_recperpage, emp_entry_id,"
				+ " emp_entry_date, emp_modified_id,"
				+ " emp_modified_date)(" + StrSql + " )";
		// SOP("StrSql======uni========" + StrSql);
		updateQuery(StrSql);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
