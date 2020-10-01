package axela.insurance;
//divya 26th nov

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Vehicle_Check extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String booking_id = "0";
	public String vehfollowup_pickupdriver_emp_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
			// SOP("enquiry_branch_id--" + enquiry_branch_id);
			// response.setContentType("text/html");
			// PrintWriter out = response.getWriter();

		}
		// out.println(StrHTML);
		// out.flush();
		// out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
