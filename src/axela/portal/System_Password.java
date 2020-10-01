package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class System_Password extends Connect {

	public String updateB = "";
	public String emp_id = "", branch_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_upass;
	public String oldpass = "";
	public String pass = "";
	public String pass1 = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				updateB = PadQuotes(request.getParameter("update_button"));
				if (updateB == null) {
					updateB = "";
				}

				if (!"Update Password".equals(updateB)) {
					oldpass = " ";
					pass = " ";
					pass1 = " ";
				} else {
					GetValues(request, response);
					GetUpass();
					CheckForm();
					if (msg.equals("")) {
						UpdateFields();
					}
				}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		oldpass = PadQuotes(request.getParameter("txt_oldpass"));
		pass = PadQuotes(request.getParameter("txt_pass"));
		pass1 = PadQuotes(request.getParameter("txt_pass1"));
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				int emp_idintval = Integer.parseInt(emp_id);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp Set emp_upass = '" + pass1 + "' where  emp_id =" + emp_idintval + "";
				updateQuery(StrSql);
				new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
				msg = "Password update successfully!";

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			}
		}

	}

	protected void GetUpass() {
		try {
			StrSql = "select emp_upass from " + compdb(comp_id) + "axela_emp where  emp_id =" + emp_id + "";
			// SOP(StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			while (crs.next()) {
				emp_upass = crs.getString("emp_upass");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}

	}

	protected void CheckForm() {
		msg = "";
		if (oldpass.equals("")) {
			msg = "Your current password cannot be blank!";
		}
		// SOP(oldpass+"<br>"+emp_upass);
		if (!oldpass.equals("") && !oldpass.equals(emp_upass)) {
			msg = msg + "<br>Please check your current password!";
		}
		if (pass.equals("")) {
			msg = msg + "<br>Your new password cannot be blank!";
		}
		if (!pass.equals("") && pass.length() < 8) {
			msg = msg + "<br>Your new password cannot be less than 8 Characters!";
		}
		if (pass1.equals("")) {
			msg = msg + "<br>Please check your password confirmation!";
		}
		if (!pass1.equals("") && !pass1.equals(pass)) {
			msg = msg + "<br>Your new passwords does not match!";
		}

	}
}
