/*Bhagwan Singh 18th April 2013*/
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Vehicle_Dash_Insurance_Followup_Check extends Connect {

	public String followupinfo = "";
	public String veh_id = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
			followupinfo = PadQuotes(request.getParameter("followupinfo"));
			if (followupinfo.equals("yes")) {
				StrHTML = new Vehicle_Dash_Insurance_Followup().ListInsuranceFollowup(veh_id, comp_id);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
