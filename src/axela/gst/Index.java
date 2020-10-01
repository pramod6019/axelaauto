package axela.gst;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String action = "";
	public String msg = "";
	public String signinid = "0";
	public String password = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String user_jsessionid = "0";
	public int signincount = 0;
	public String captcha = "";
	public String code = "";
	public String emp_sales = "";
	public String uuid = "", check = "";
	public String remember = "";
	public String credentials1 = "", credentials2 = "", credentials3 = "";
	public static final String CHARSET = "ISO-8859-1";
	public String enCodeUid = "";
	public String deCodeUid = "";
	public String enCodePass = "";
	public String deCodePass = "";
	public String enCode_comp_id = "";
	public String deCode_comp_id = "";
	public Index() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			getcompsession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				moduleaccess(request);
				response.sendRedirect(response.encodeRedirectURL("../gst/gst-supplier-update.jsp"));
			} else {
				response.sendRedirect(response.encodeRedirectURL("access.jsp?msg=Please contact Administrator!"));
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	void moduleaccess(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Map setMap;
		if (session.getAttribute("sessionMap") == null) {
			setMap = new HashMap();
			session.setAttribute("sessionMap", setMap);
		}
		setMap = (Map) session.getAttribute("sessionMap");

		StrSql = "SELECT"
				+ " COALESCE (branch_logo, '') branch_logo,"
				+ " comp_name, comp_logo, branch_add,"
				+ " city_name, branch_pin"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id,"
				+ " " + compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1"
				+ " AND comp_active = '1'"
				+ " AND branch_id = 1";
		// SOP("StrSql===" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				// if (!crs.getString("branch_logo").equals("")) {
				// setMap.put("comp_logo", "../Thumbnail.do?branchlogo=" + crs.getString("branch_logo"));
				// } else
				if (!crs.getString("comp_logo").equals("")) {
					setMap.put("comp_logo", "../Thumbnail.do?complogo=" + crs.getString("comp_logo"));
				} else {
					setMap.put("comp_logo", "");
				}
				setMap.put("branch_address", crs.getString("branch_add"));
				setMap.put("city_name", crs.getString("city_name"));
				setMap.put("branch_pin", crs.getString("branch_pin"));
			}
			// session.setAttribute("sessionMap", setMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
