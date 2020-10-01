// Ved (30 Jan 2013)
package axela.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Home extends Connect {

	public static String msg = "";
	public String StrSql = "", StrHTML = "";
	public String emp_id = "";
	public String from_date = "", to_date = "";
	public String user = "", assist = "", newtestdrive = "", preowned = "",
			preownedtestdrive = "", servicebooking = "", insur = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = session.getAttribute("emp_id") + "";
			from_date = PadQuotes(request.getParameter("txt_from_date"));
			to_date = PadQuotes(request.getParameter("txt_to_date"));
			if (from_date.equals("") || to_date.equals("")) {
				to_date = DateToShortDate(kknow());
				from_date = ReportStartdate();
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

}
