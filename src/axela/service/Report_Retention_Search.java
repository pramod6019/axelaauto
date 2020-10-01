package axela.service;
// * @author Smitha
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Retention_Search extends HttpServlet {

	public String StrSearch = "";
	public String comp_id = "0";
	public String StrHTML = "";
	Connect cd = new Connect();
	public String sold = "", servicein = "", appt = "";
	public String salesmonth = "", serviceyear = "", currentmonth = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = cd.CNumeric(cd.GetSession("comp_id", request));

			StrSearch = "";

			sold = cd.PadQuotes(request.getParameter("sold"));
			servicein = cd.PadQuotes(request.getParameter("servicein"));
			appt = cd.PadQuotes(request.getParameter("appt"));
			salesmonth = cd.PadQuotes(request.getParameter("salesmonth"));
			serviceyear = cd.PadQuotes(request.getParameter("serviceyear"));
			currentmonth = cd.PadQuotes(request.getParameter("currentmonth"));

			if (sold.equals("yes")) {
				StrSearch = " AND SUBSTR(veh_sale_date, 5, 2) = " + salesmonth;
				cd.SetSession("vehstrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
			}

			if (servicein.equals("yes")) {
				StrSearch = " AND SUBSTR(veh_sale_date, 5, 2) = " + salesmonth;
				StrSearch += " AND veh_id in (Select jc_veh_id from " + cd.compdb(comp_id) + "axela_service_jc where SUBSTR(jc_time_in, 1, 6) = " + serviceyear + currentmonth + ")";
				cd.SetSession("vehstrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
			}

			if (appt.equals("yes")) {
				StrSearch = " AND SUBSTR(veh_sale_date, 5, 2) = " + salesmonth;
				StrSearch += " AND veh_id in (Select booking_veh_id from " + cd.compdb(comp_id) + "axela_service_booking where SUBSTR(booking_time, 1, 6) = " + serviceyear + currentmonth + ")";
				cd.SetSession("vehstrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			cd.SOPError("Axelaauto== " + this.getClass().getName());
			cd.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
