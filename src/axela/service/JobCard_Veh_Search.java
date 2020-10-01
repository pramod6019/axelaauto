package axela.service;
// satish 2nd march 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class JobCard_Veh_Search extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/jobcard.jsp>Job Card</a> &gt; <a href=jobcard-veh-search.jsp>Search Vehicles</a>:";
	public String search = "";
	public String comp_id = "";
	public String txt_search = "";
	public String LinkAddPage = "<a href=vehicle-update.jsp?add=yes>Add New Vehicle...</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			search = PadQuotes(request.getParameter("search_button"));
			txt_search = PadQuotes(request.getParameter("txt_search"));
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
