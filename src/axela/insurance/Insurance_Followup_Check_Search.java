package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Insurance_Followup_Check_Search extends Connect {

	public String StrSearch = "", insurdate = "", insurfollowupdate = "";
	public String StrHTML = "";
	public String starttime = "";
	public String comp_id = "0";
	public String endtime = "", emp_id = "", exe_id = "";
	public String insur = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				ExeAccess = GetSession("ExeAccess", request);
				insur = PadQuotes(request.getParameter("insur"));

				// SOP("emp_id===" + emp_id);
				// StrSearch = StrSearch + ExeAccess;
				if (!starttime.equals("") && !endtime.equals("")) {
					insurfollowupdate = " AND SUBSTR(insurfollowup_followup_time,1,8)>=SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(insurfollowup_followup_time,1,8)<=SUBSTR('" + endtime + "',1,8)";
					insurdate = " AND SUBSTR(insurpolicy_start_date,1,8)>=SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(insurpolicy_start_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
				}
				if (!insur.equals("") && !insur.equals("converted")) {
					StrSearch = StrSearch + ExeAccess.replace("emp_id", "insurfollowup_emp_id");
					if (!emp_id.equals("0")) {
						StrSearch = StrSearch + " AND insurfollowup_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " AND insurfollowup_emp_id IN (" + exe_id + ")";
					}
				}
				if (!insur.equals("") && insur.equals("converted")) {
					StrSearch = StrSearch + ExeAccess.replace("emp_id", "insurpolicy_emp_id");
					if (!emp_id.equals("0")) {
						StrSearch = StrSearch + " AND insurpolicy_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " AND insurpolicy_emp_id IN (" + exe_id + ")";
					}
				}

				if (insur.equals("veh")) {
					StrSearch = " AND veh_id IN (SELECT insurfollowup_veh_id"
							+ " FROM " + compdb(comp_id) + "axela_insurance_followup "
							+ " WHERE 1=1 " + insurfollowupdate + StrSearch + ")";
				} else if (insur.equals("hot")) {
					StrSearch = " AND veh_id IN (SELECT insurfollowup_veh_id"
							+ " FROM " + compdb(comp_id) + "axela_insurance_followup "
							+ " WHERE 1=1 " + insurfollowupdate + StrSearch
							+ " AND insurfollowup_priorityinsurfollowup_id = 1)";
				} else if (insur.equals("warm")) {
					StrSearch = " AND veh_id IN (SELECT insurfollowup_veh_id "
							+ " FROM " + compdb(comp_id) + "axela_insurance_followup "
							+ " WHERE 1=1 " + insurfollowupdate + StrSearch
							+ " AND insurfollowup_priorityinsurfollowup_id = 2)";
				}
				else if (insur.equals("cold")) {
					StrSearch = " AND veh_id IN (SELECT insurfollowup_veh_id "
							+ " FROM " + compdb(comp_id) + "axela_insurance_followup "
							+ " WHERE 1=1 " + insurfollowupdate + StrSearch
							+ " AND insurfollowup_priorityinsurfollowup_id = 3)";
				} else if (insur.equals("converted")) {
					StrSearch = insurdate + StrSearch + " AND insurpolicy_active =1";
				}
				// }
				// SOP("StrSearch = " + StrSearch);
				if (!insur.equals("") && !insur.equals("converted")) {
					SetSession("vehstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
				} else if (insur.equals("converted")) {
					SetSession("insurancestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-list.jsp?smart=yes"));
				}
			}
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
