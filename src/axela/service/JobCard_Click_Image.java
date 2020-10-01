/* Ved Prakash & $at!sh (16th July 2013 )*/
package axela.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class JobCard_Click_Image extends Connect {

	public String StrSql = "";
	public String LinkHeader = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String jc_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/jobcard.jsp>Job Card</a> &gt; <a href=../service/jobcard-list.jsp?all=yes>List Job Cards</a> &gt; <a href=../service/jobcard-list.jsp?jc_id="
						+ jc_id + "> Job Card ID: " + jc_id + "</a> &gt; <a href=../service/jobcard-dash-image.jsp?jc_id=" + jc_id + ">List Images</a>:";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
