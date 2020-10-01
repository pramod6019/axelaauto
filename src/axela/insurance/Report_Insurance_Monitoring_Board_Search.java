package axela.insurance;
// * @author Smitha
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Insurance_Monitoring_Board_Search extends Connect {

	public String StrSearch = "", searchdate = "";
	public String starttime = "", insuremp_id = "", fieldemp_id = "";
	public String endtime = "", year = "", carmanuf_id = "", model_id = "", emp_id = "", exe_id = "", dr_branch_id = "", brand_id = "", region_id = "";
	public String enquiry = "", jobcardemp = "", technicianemp = "", StrHTML = "", value = "", total_by = "";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				year = PadQuotes(request.getParameter("dr_year"));
				brand_id = PadQuotes(request.getParameter("brand_id"));
				region_id = PadQuotes(request.getParameter("region_id"));
				dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				carmanuf_id = PadQuotes(request.getParameter("carmanuf_id"));
				model_id = PadQuotes(request.getParameter("model_id"));
				insuremp_id = PadQuotes(request.getParameter("insuremp_id"));
				fieldemp_id = PadQuotes(request.getParameter("fieldemp_id"));
				enquiry = PadQuotes(request.getParameter("enquiry"));
				value = PadQuotes(request.getParameter("value"));
				total_by = PadQuotes(request.getParameter("total_by"));
				StrSearch = "";
				searchdate = "";

				// *** searchdate
				if (!starttime.equals("") && !endtime.equals("")) {
					searchdate = " AND SUBSTR(searchdatestart,1,8) >= SUBSTR('" + starttime + "',1,8) and SUBSTR(searchdateend,1,8) <= SUBSTR('" + endtime + "',1,8) ";
				}
				// *** enquiry
				// SOP("enquiry=====" + enquiry);
				if (!enquiry.equals("")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + dr_branch_id + ")";
					}

					if (!carmanuf_id.equals("")) {
						StrSearch += " AND carmanuf_id IN (" + carmanuf_id + ")";
					}

					if (!model_id.equals("")) {
						StrSearch += " AND preownedmodel_id IN (" + model_id + ")";
					}

					if (!insuremp_id.equals("") || !fieldemp_id.equals("")) {
						StrSearch += " AND emp_id IN (" + insuremp_id + fieldemp_id + ")";
					} else if (!insuremp_id.equals("")) {
						StrSearch += " AND emp_id IN (" + insuremp_id + ")";
					} else if (!fieldemp_id.equals("")) {
						StrSearch += " AND emp_id IN (" + fieldemp_id + ")";
					}

					// Target
					if (enquiry.equals("target")) {
						// StrSearch += " AND insurance_target_emp_id = " + insuremp_id
						// + " AND (SUBSTR(insurance_target_startdate, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
						// + " AND SUBSTR(insurance_target_enddate, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
						//
						// SetSession("insurancetargetstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-target-list.jsp?dr_year=" + year + "&dr_executives=" + emp_id));
					}
					// Open Enquiry
					else if (enquiry.equals("openenquiry")) {

						switch (total_by) {
							case "brand" :
								StrSearch += " AND branch_brand_id =" + value;
								break;
							case "region" :
								StrSearch += " AND branch_region_id =" + value;
								break;
							case "branch" :
								StrSearch += " AND branch_id =" + value;
								break;
							case "emp" :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
							default :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
						}

						StrSearch += " AND SUBSTR(insurenquiry_date, 1, 8) < SUBSTR('" + starttime + "',1,8)"
								+ " AND insurenquiry_insurstatus_id = 1";
						// SOP("StrSearch===" + StrSearch);
						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}

					// Fresh Enquiry
					else if (enquiry.equals("freshenquiry")) {

						switch (total_by) {
							case "brand" :
								StrSearch += " AND branch_brand_id =" + value;
								break;
							case "region" :
								StrSearch += " AND branch_region_id =" + value;
								break;
							case "branch" :
								StrSearch += " AND branch_id =" + value;
								break;
							case "emp" :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
							default :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
						}

						StrSearch += " AND SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
						// SOP("StrSearch===" + StrSearch);

						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}

					// Lost Enquiry
					else if (enquiry.equals("lostenquiry")) {

						switch (total_by) {
							case "brand" :
								StrSearch += " AND branch_brand_id =" + value;
								break;
							case "region" :
								StrSearch += " AND branch_region_id =" + value;
								break;
							case "branch" :
								StrSearch += " AND branch_id =" + value;
								break;
							case "emp" :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
							default :
								StrSearch += " AND insurenquiry_emp_id =" + value;
								break;
						}

						StrSearch += " AND SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurenquiry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)"
								+ " AND (insurenquiry_insurstatus_id = 3 OR insurenquiry_insurstatus_id = 4	)";
						// SOP("StrSearch===" + StrSearch);

						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}
					// Total Open Enquiry
					else if (enquiry.equals("totalopenenquiry")) {
						StrSearch += " AND insurenquiry_emp_id != 0 "
								+ " AND SUBSTR(insurenquiry_date, 1, 8) < SUBSTR('" + starttime + "',1,8)"
								+ " AND insurenquiry_insurstatus_id = 1";
						// SOP("StrSearch===" + StrSearch);
						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}
					// Total Fresh Enquiry
					else if (enquiry.equals("totalfreshenquiry")) {
						StrSearch += " AND insurenquiry_emp_id != 0 "
								+ " AND SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
						// SOP("StrSearch===" + StrSearch);
						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}
					// Total Lost Enquiry
					else if (enquiry.equals("totallostenquiry")) {
						StrSearch += " AND insurenquiry_emp_id != 0 "
								+ " AND SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurenquiry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)"
								+ " AND (insurenquiry_insurstatus_id = 3 OR insurenquiry_insurstatus_id = 4	)";
						// SOP("StrSearch===" + StrSearch);
						SetSession("insurstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-list.jsp?smart=yes"));
					}
					// Policy
					else if (enquiry.equals("policy")) {

						switch (total_by) {
							case "brand" :
								StrSearch += " AND branch_brand_id =" + value;
								break;
							case "region" :
								StrSearch += " AND branch_region_id =" + value;
								break;
							case "branch" :
								StrSearch += " AND branch_id =" + value;
								break;
							case "emp" :
								StrSearch += " AND insurpolicy_emp_id =" + value;
								break;
							default :
								StrSearch += " AND insurpolicy_emp_id =" + value;
								break;
						}

						StrSearch += " AND SUBSTR(insurpolicy_entry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurpolicy_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)";

						// SOP("StrSearch===" + StrSearch);
						SetSession("insurancestrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-list.jsp?smart=yes"));
					}
					// Total Policy
					else if (enquiry.equals("totalpolicy")) {

						StrSearch += " AND SUBSTR(insurpolicy_entry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(insurpolicy_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)";

						SetSession("insurancestrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-list.jsp?smart=yes"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
