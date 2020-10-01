package axela.portal;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executive_Photo_Check extends Connect {

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
					// SOP("222");
					UpdatePhotoEmp("0", comp_id);
					response.sendRedirect(response.encodeRedirectURL("executive-photo-check.jsp?&msg=Executive Photo Updated Succesfully!"));
					// response.sendRedirect(response.encodeRedirectURL("executive-univ-check.jsp?&msg=Universal executive table updated succesfully!"));
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public void UpdatePhotoEmp(String emp_id, String comp_id) {
		try {
			String StrSql = "SELECT emp_photo, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_photo!=''";
			CachedRowSet crs =processQuery(StrSql, 0);
			while (crs.next()) {
				File f = new File(ExeImgPath(comp_id) + crs.getString("emp_photo"));
				if (f.exists() == false) {
					StrSql = "UPDATE " + compdb(comp_id) + " axela_emp"
							+ " SET emp_photo = ''"
							+ " WHERE emp_id =" + crs.getString("emp_id") + "";
					updateQuery(StrSql);
				}
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto-APP===" + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
