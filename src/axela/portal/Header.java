package axela.portal;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Header extends Connect {

	public String branch_id = "";
	public String comp_logo = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String comp_module_preowned = "", comp_module_accounting = "0", comp_module_invoice = "0";
	public String comp_module_inventory = "0", comp_module_service = "0", comp_module_accessories = "0", comp_module_insurance = "0";
	public String comp_module_app = "";
	public String emp_copy_access = "";
	public String emp_name = "";
	public String emp_role_id = "";
	public String emp_rs_access = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String emp_mis_access = "";
	public String user_emp_id = "0";
	public String sessionid = "";
	public String exeaccess = "0";
	public String Img = "";
	public String alt = "";
	private String fileName;
	public String updatestatus = "";
	public String deleteB = "";
	public String updateB = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String name = "";
	public String emp_photo = "";
	public String img_imgsize = "";
	public String chkPermMsg = "";
	public String msg2 = "";
	public String emp_idsession = "";
	public String QueryString = "";
	public String admin = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			emp_photo = GetSession("emp_photo", request);
			admin = GetSession("admin", request);
			if (!comp_id.equals("0")) {
				// Start Duplicate Sign in
				sessionid = session.getId();
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (!CNumeric(emp_id).equals("0")) {
					StrSql = "SELECT COALESCE(user_emp_id, 0) user_emp_id"
							+ " FROM " + compdb(comp_id) + "axela_emp_user"
							+ " WHERE user_emp_id = " + emp_id + ""
							+ " AND user_jsessionid = '" + sessionid + "'";
					// SOP("StrSql===" + StrSql);
					user_emp_id = CNumeric(ExecuteQuery(StrSql));
					if (AppRun().equals("1") && user_emp_id.equals("0") && !emp_id.equals("1") && admin.equals("")) {
						response.sendRedirect("../portal/index.jsp?msg=Duplicate Sign In Detected!");
					}
					moduleaccess(session);
					if (AppRun().equals("1")) {
						UserActivity(emp_id, request.getRequestURI(), "0", comp_id);
					}
				}
			}
			else {
				response.sendRedirect("../portal/index.jsp?msg=Session Expired!");
			}
			// End Duplicate Sign in
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	void moduleaccess(HttpSession session) {
		Map getMap = (Map) session.getAttribute("sessionMap");
		user_emp_id = getMap.get("user_emp_id") + "";
		emp_name = getMap.get("emp_name") + "";
		emp_role_id = getMap.get("emp_role_id") + "";
		emp_mis_access = getMap.get("emp_mis_access") + "";
		emp_copy_access = getMap.get("emp_copy_access") + "";
		comp_logo = getMap.get("comp_logo") + "";
		// emp_photo = getMap.get("emp_photo") + "";
		comp_sms_enable = getMap.get("comp_sms_enable") + "";
		comp_email_enable = getMap.get("comp_email_enable") + "";
		comp_module_preowned = getMap.get("comp_module_preowned") + "";
		comp_module_accounting = getMap.get("comp_module_accounting") + "";
		comp_module_invoice = getMap.get("comp_module_invoice") + "";
		comp_module_app = getMap.get("comp_module_app") + "";
		comp_module_inventory = getMap.get("comp_module_inventory") + "";
		comp_module_accessories = getMap.get("comp_module_accessories") + "";
		comp_module_insurance = getMap.get("comp_module_insurance") + "";
		comp_module_service = getMap.get("comp_module_service") + "";

	}

	public void UserActivity(String emp_id, String empactivity_url, String empactivity_app, String comp_id) {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_activity"
				+ " (empactivity_emp_id,"
				+ " empactivity_url,"
				+ " empactivity_app,"
				+ " empactivity_time)"
				+ " VALUES"
				+ " (" + emp_id + ","
				+ " '" + empactivity_url + "',"
				+ " '" + empactivity_app + "',"
				+ " '" + ToLongDate(kknow()) + "')";
		// SOP("StrSql=====" + StrSql);
		updateQuery(StrSql);
	}

	protected void PopulateFields() {

		try {
			StrSql = "SELECT emp_name, emp_photo"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_name = crs.getString("emp_name");
				emp_photo = crs.getString("emp_photo");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET "
						+ " emp_name = '" + emp_name + "',"
						+ " emp_photo = '" + fileName + "'"
						+ " WHERE emp_id = " + emp_id + " ";
				updateQuery(StrSql);
				// SOP("SqlStr==" + StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {

		msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Photo!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
