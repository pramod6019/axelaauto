package axela.service;
// * @author Smitha
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Service_Monitoring_Board_Search extends Connect {

	public String StrSearch = "", searchdate = "";
	public String starttime = "", sa_id = "", st_id = "", jccat_id = "", jctype_id = "", zone_id = "";
	public String endtime = "", year = "", preownedmodel_id = "", dr_warranty, emp_id = "", exe_id = "", dr_branch_id = "",
			emp_branch_id = "0", brand_id = "", region_id = "";
	public String enquiry = "", jobcardemp = "", technicianemp = "", StrHTML = "", value = "", total_by = "";
	public String comp_id = "0", jclink = "", jctotal = "", emp_active = "";

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
				zone_id = PadQuotes(request.getParameter("zone_id"));
				dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				dr_warranty = PadQuotes(request.getParameter("dr_warranty"));
				emp_branch_id = PadQuotes(request.getParameter("emp_branch_id"));
				preownedmodel_id = PadQuotes(request.getParameter("preownedmodel_id"));
				jccat_id = PadQuotes(request.getParameter("jccat_id"));
				jctype_id = PadQuotes(request.getParameter("jctype_id"));
				sa_id = PadQuotes(request.getParameter("sa_id"));
				st_id = PadQuotes(request.getParameter("st_id"));
				emp_active = PadQuotes(request.getParameter("emp_active"));
				enquiry = PadQuotes(request.getParameter("enquiry"));
				jclink = PadQuotes(request.getParameter("jclink"));
				jctotal = PadQuotes(request.getParameter("jctotal"));
				value = PadQuotes(request.getParameter("value"));
				// SOP("value==" + value);
				total_by = PadQuotes(request.getParameter("total_by"));
				StrSearch = "";
				searchdate = "";

				if (!dr_warranty.equals("-1")) {
					StrSearch += " AND jc_warranty='" + dr_warranty + "'";
				}
				// *** searchdate
				if (!starttime.equals("") && !endtime.equals("")) {
					searchdate = " AND SUBSTR(searchdatestart,1,8) >= SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(searchdateend,1,8) <= SUBSTR('" + endtime + "',1,8) ";
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
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + dr_branch_id + ")";
					}
					if (!preownedmodel_id.equals("")) {
						StrSearch += " AND variant_preownedmodel_id IN (" + preownedmodel_id + ")";
					}
					if (!sa_id.equals("")) {
						StrSearch += " AND jc_emp_id IN (" + sa_id + ")";
					}
					if (!st_id.equals("")) {
						StrSearch += " AND jc_technician_emp_id IN (" + st_id + ")";
					}
					if (!jccat_id.equals("")) {
						StrSearch += " AND jccat_id IN (" + jccat_id + ")";
					}
					if (!jctype_id.equals("")) {
						StrSearch += " AND jc_jctype_id IN (" + jctype_id + ")";
					}
					// Target
					if (enquiry.equals("servtarget")) {

						response.sendRedirect(response.encodeRedirectURL("../service/service-target-list.jsp?dr_year=" + year + "&dr_branch=" + emp_branch_id + "&dr_executives=" + value));
					}
					// Achived
					else if (enquiry.equals("achived")) {
						StrSearch += " AND jc_jcstage_id IN(6)";
						switch (total_by) {
							case "brand" :
								StrSearch += " AND branch_brand_id =" + value;
								break;
							case "model" :
								StrSearch += " AND variant_preownedmodel_id =" + value;
								break;
							case "region" :
								StrSearch += " AND branch_region_id =" + value;
								break;
							case "zone" :
								StrSearch += " AND branch_zone_id =" + value;
								break;
							case "branch" :
								StrSearch += " AND branch_id =" + value;
								break;
							case "emp" :
								StrSearch += " AND jc_emp_id =" + value;
								break;
							case "tech" :
								StrSearch += " AND jc_technician_emp_id =" + value;
								break;
							case "jccat" :
								StrSearch += " AND jc_jccat_id =" + value;
								break;
							case "jctype" :
								StrSearch += " AND jc_jctype_id =" + value;
								break;
							default :
								StrSearch += " AND jc_emp_id =" + value;
								break;
						}
						switch (jclink) {
							case "labouramount" :
								StrSearch += " AND jc_bill_cash_labour !=0";
								break;
							case "vasamount" :
								StrSearch += " AND jc_bill_cash_parts_valueadd !=0";
								break;
							case "vaslabouramount" :
								StrSearch += " AND jc_bill_cash_labour_valueadd !=0";
								break;
							case "tyrecount" :
								StrSearch += " AND jc_bill_cash_parts_tyre_qty !=0";
								break;
							case "tyreamount" :
								StrSearch += " AND jc_bill_cash_parts_tyre !=0";
								break;
							case "breakamount" :
								StrSearch += " AND jc_bill_cash_parts_brake!=0";
								break;
							case "breakcount" :
								StrSearch += " AND jc_bill_cash_parts_brake_qty!=0";
								break;
							case "batterycount" :
								StrSearch += " AND jc_bill_cash_parts_battery_qty!=0";
								break;
							case "batteryamount" :
								StrSearch += " AND jc_bill_cash_parts_battery!=0";
								break;
							case "oilamount" :
								StrSearch += " AND jc_bill_cash_parts_oil!=0";
								break;
							case "acessamount" :
								StrSearch += " AND jc_bill_cash_parts_accessories!=0";
								break;
							case "extwarrtycount" :
								StrSearch += " AND jc_bill_cash_parts_extwarranty_qty!=0";
								break;
							case "extwarrtyamount" :
								StrSearch += " AND jc_bill_cash_parts_extwarranty!=0";
								break;
						}

						StrSearch += " AND SUBSTRING(jc_bill_cash_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTRING(jc_bill_cash_date,1,8) <= SUBSTR('" + endtime + "',1,8)";
						if (emp_active.equals("0")) {
							StrSearch += " AND emp_active = 1";
						}
						// SOP("StrSearch==11=" + StrSearch);

						SetSession("jcstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
					} else if (enquiry.equals("totalachived")) {
						StrSearch += " AND jc_jcstage_id IN(6)"
								+ " AND SUBSTRING(jc_bill_cash_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTRING(jc_bill_cash_date,1,8) <= SUBSTR('" + endtime + "',1,8)";
						if (emp_active.equals("0")) {
							StrSearch += " AND emp_active = 1";
						}
						StrSearch += " AND emp_service = 1"
								// + " AND jc_jccat_id != 0"
								+ " AND emp_id != 1";

						switch (jctotal) {
							case "totallabouramount" :
								StrSearch += " AND jc_bill_cash_labour !=0";
								break;
							case "totalvasamount" :
								StrSearch += " AND jc_bill_cash_parts_valueadd !=0";
								break;
							case "totalvaslabouramount" :
								StrSearch += " AND jc_bill_cash_labour_valueadd !=0";
								break;
							case "totaltyrecount" :
								StrSearch += " AND jc_bill_cash_parts_tyre_qty !=0";
								break;
							case "totaltyreamount" :
								StrSearch += " AND jc_bill_cash_parts_tyre !=0";
								break;
							case "totalbreakamount" :
								StrSearch += " AND jc_bill_cash_parts_brake!=0";
								break;
							case "totalbreakcount" :
								StrSearch += " AND jc_bill_cash_parts_brake_qty!=0";
								break;
							case "totalbatterycount" :
								StrSearch += " AND jc_bill_cash_parts_battery_qty!=0";
								break;
							case "totalbatteryamount" :
								StrSearch += " AND jc_bill_cash_parts_battery!=0";
								break;
							case "totaloilamount" :
								StrSearch += " AND jc_bill_cash_parts_oil!=0";
								break;
							case "totalacessamount" :
								StrSearch += " AND jc_bill_cash_parts_accessories!=0";
								break;
							case "totalextwarrtycount" :
								StrSearch += " AND jc_bill_cash_parts_extwarranty_qty!=0";
								break;
							case "totalextwarrtyamount" :
								StrSearch += " AND jc_bill_cash_parts_extwarranty!=0";
								break;
						}
						// SOP("StrSearch=22==" + StrSearch);
						SetSession("jcstrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
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
