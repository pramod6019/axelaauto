package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.Ecover_WS;

public class Executive_Update_Password extends Connect {
	public String updateB = "";
	public String emp_id = "", branch_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String pass = "";
	public String redirect = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_upass = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(request.getParameter("emp_id"));
			redirect = CNumeric(request.getParameter("redirect"));
			CheckPerm(comp_id, "emp_executive_edit", request, response);
			msg = "";
			populateEmpName();
			if (!comp_id.equals("0")) {
				updateB = PadQuotes(request.getParameter("update_button"));
				if (updateB == null) {
					updateB = "";
				}
				if (!"Update Password".equals(updateB)) {
					pass = " ";
				} else {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						UpdateFields(request);
						if (redirect.equals("1")) {
							response.sendRedirect(response
									.encodeRedirectURL("../portal/executive-list.jsp?emp_id="
											+ emp_id + "&msg=" + msg));
						}
						else if (redirect.equals("2")) {
							response.sendRedirect(response
									.encodeRedirectURL("../portal/exe-list.jsp?emp_id="
											+ emp_id + "&msg=" + msg));
						}

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
		pass = PadQuotes(request.getParameter("txt_pass"));
	}

	public String populateEmpName() {
		try {
			StrSql = "SELECT CONCAT(emp_name,'(',emp_ref_no,')') AS emp_name,emp_upass,"
					+ "	COALESCE(emp_email1, '') AS emp_email1"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND emp_id != 1"
					+ " AND emp_id = " + emp_id;
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.next()) {
				crs.beforeFirst();
				while (crs.next()) {
					emp_name = crs.getString("emp_name");
					emp_email1 = crs.getString("emp_email1");
					emp_upass = crs.getString("emp_upass");
				}
			}
			if (redirect.equals("1"))
			{
				emp_name = "<a href=executive-list.jsp?emp_id=" + emp_id + ">" + emp_name + "</a>";
			}
			else if (redirect.equals("2"))
			{
				emp_name = "<a href=exe-list.jsp?emp_id=" + emp_id + ">" + emp_name + "</a>";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return emp_name;

	}

	protected void UpdateFields(HttpServletRequest request) {
		if (msg.equals("") && !emp_id.equals("1")) {
			try {
				int emp_idintval = Integer.parseInt(emp_id);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET emp_upass = '" + pass + "'"
						+ " WHERE  emp_id =" + emp_idintval + "";
				updateQuery(StrSql);
				new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
				msg = "Password updated successfully!";
				StrSql = "SELECT comp_ecover_integration"
						+ " FROM " + compdb(comp_id) + "axela_comp"
						+ " WHERE comp_id=" + comp_id;
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					JSONObject input = new JSONObject();
					input.put("emp_id", emp_id);
					input.put("emp_upass", pass);
					new Ecover_WS().WSRequest(input, "axelaauto-emppass", request);
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			}
		}

	}

	protected void CheckForm() {
		msg = "";
		// SOP(oldpass+"<br>"+emp_upass);
		if (pass.equals("")) {
			msg = msg + "<br>Your new password cannot be blank!";
		}
		if (!pass.equals("") && pass.length() < 8) {
			msg = msg + "<br>Your new password cannot be less than 8 Characters!";
		}

		if (emp_id.equals("1")) {
			msg = msg + "<br>Cacn't update Password for Admin!";
		}

	}
}
