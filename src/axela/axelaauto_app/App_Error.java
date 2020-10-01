package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class App_Error extends Connect {

	public String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// CheckBranchSession(request, response);
		// CheckPerm(comp_id, "emp_role_id",request,response);
		// HttpSession session = request.getSession(true);
		// comp_id = CNumeric(GetSession("comp_id", request));
		// emp_id = CNumeric(GetSession("emp_id", request));
		// comp_id = CNumeric(GetSession("comp_id", request));
		// branch_id = CNumeric(GetSession("emp_branch_id", request));
		// SOP("-error page---------11");
		msg = PadQuotes(request.getParameter("msg"));
		// msg="Access Denied. .Please contact system admin";
		// SOP("msg---------erroe page---------" + msg);
		msg = unescapehtml(msg);

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
