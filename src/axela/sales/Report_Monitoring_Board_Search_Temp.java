package axela.sales;
// * @author Gopal
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Monitoring_Board_Search_Temp extends Connect {

	public String StrSearch = "", searchdate = "", salesorderdate = "", target = "", campaign = "";
	public String starttime = "", team_id = "", followup = "", so = "", soe_id = "", sob_id = "";
	public String endtime = "", model_id = "", emp_id = "", exe_id = "", branch_id = "", brand_id = "", region_id = "";
	public String include_inactive_exe = "";
	public String enquiry = "", testdrive = "", salesorder = "", StrHTML = "";
	public String enquiry_preowned_model = "", so_preowned_model = "", td_preowned_model = "", hv_preowned_model = "";
	public String preownedmodel_model_id = "";
	public String targetvalue = "", mainfilter = "", mainfiltervalue = "";
	public String comp_id = "0";
	Connect cd = new Connect();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				brand_id = PadQuotes(request.getParameter("brand_id"));
				region_id = PadQuotes(request.getParameter("region_id"));
				branch_id = PadQuotes(request.getParameter("branch_id"));
				model_id = PadQuotes(request.getParameter("model_id"));
				team_id = PadQuotes(request.getParameter("team_id"));
				emp_id = PadQuotes(request.getParameter("exe_id"));
				// exe_id = PadQuotes(request.getParameter("exe_id"));
				soe_id = PadQuotes(request.getParameter("soe_id"));
				sob_id = cd.PadQuotes(request.getParameter("sob_id"));
				include_inactive_exe = CNumeric(PadQuotes(request.getParameter("include_inactive_exe")));
				enquiry_preowned_model = CNumeric(PadQuotes(request.getParameter("enquiry_preowned_model")));
				mainfilter = PadQuotes(request.getParameter("total_by"));
				mainfiltervalue = PadQuotes(request.getParameter("value"));
				targetvalue = PadQuotes(request.getParameter("targetvalue"));

				// *** searchdate
				if (!starttime.equals("") && !endtime.equals("")) {
					searchdate = " AND SUBSTR(searchdatestart, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) AND SUBSTR(searchdateend, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					salesorderdate = "  SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
				}

				if (include_inactive_exe.equals("0")) {
					StrSearch += " AND emp_active = 1";
				}

				// *** /enquiry
				if (targetvalue.contains("enquiry")) {

					if (include_inactive_exe.equals("0") && team_id.equals("") && emp_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
								+ " WHERE 1=1"
								+ " AND team_active = 1 "
								+ " AND teamtrans_emp_id = emp_id)";
					}
					// ------------FILTER --------------START----------------
					if (mainfilter.equals("brand") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + mainfiltervalue + ")";
					} else if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}

					if (mainfilter.equals("region") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_region_id IN (" + mainfiltervalue + ")";
					} else if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}

					if (mainfilter.equals("branch") && !mainfiltervalue.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + mainfiltervalue + ")";
					} else if (!branch_id.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + branch_id + ")";
					}

					if (mainfilter.equals("emp") && !mainfiltervalue.equals("") && emp_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id = " + mainfiltervalue + "";
					} else if (mainfilter.equals("emp") && mainfiltervalue.equals("") && !emp_id.equals("") && !include_inactive_exe.equals("0")) {
						StrSearch = StrSearch + " AND emp_id IN ( " + emp_id + ")";
					} else if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN ( " + emp_id + ")";
					}

					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN (" + model_id + ")";
					}

					if (mainfilter.equals("team") && !mainfiltervalue.equals("") && team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + mainfiltervalue + "))";
					} else if (mainfilter.equals("team") && mainfiltervalue.equals("") && team_id.equals("") && !include_inactive_exe.equals("0")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE 1=1)";
					} else if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}

					if (!soe_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					// ------------FILTER --------------END----------------

					// openenquiry
					if (targetvalue.equals("openenquiry")) {
						StrSearch = StrSearch + " AND enquiry_status_id=1"
								+ " AND SUBSTR(enquiry_date,1,8) < SUBSTR('" + starttime + "',1,8) ";
					}
					// enquiryfresh
					else if (targetvalue.equals("freshenquiry")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch;
					}
					// enquirylost
					else if (targetvalue.equals("lostenquiry")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch
								+ " AND (enquiry_status_id=3 OR enquiry_status_id=4)";
					}
					// pendingenquiry
					else if (targetvalue.equals("pendingenquiry")) {
						StrSearch = StrSearch + " AND enquiry_status_id = 1";

					}
					// homevisit
					else if (targetvalue.equals("enquiryhomevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1 = 1 " + followup
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time")
										.replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					// kpihomevisit
					else if (targetvalue.equals("enquirykpihomevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT DISTINCT(followup_enquiry_id) "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time").replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					// enquiryevaluation
					else if (targetvalue.equals("enquiryevaluation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ StrSearch + " AND enquiry_id IN (SELECT enquiry_id FROM " + cd.compdb(comp_id) + "axela_sales_enquiry" + ")"
								+ " AND enquiry_evaluation = 1 ";
					}
					// enquirypreowned
					else if (targetvalue.equals("enquirypreowned")) {
						StrSearch += " AND enquiry_id IN (SELECT preowned_enquiry_id FROM " + cd.compdb(comp_id) + "axela_preowned"
								+ " WHERE 1=1"
								+ " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
						if (!emp_id.equals("")) {
							StrSearch += " AND preowned_sales_emp_id IN (" + emp_id + ")";
						}
						StrSearch += " )";
					}
				}

				// *** testdrive
				if (targetvalue.contains("testdrives")) {
					if (mainfilter.equals("brand") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + mainfiltervalue + ")";
					} else if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}

					if (mainfilter.equals("region") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_region_id IN (" + mainfiltervalue + ")";
					} else if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}

					if (mainfilter.equals("branch") && !mainfiltervalue.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + mainfiltervalue + ")";
					} else if (mainfilter.equals("branch") && mainfiltervalue.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe WHERE 1=1)";
					} else if (!branch_id.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + branch_id + ")";
					}

					if (mainfilter.equals("emp") && !mainfiltervalue.equals("")) {
						StrSearch = StrSearch + " AND emp_id = " + mainfiltervalue + "";
					} else if (mainfilter.equals("emp") && mainfiltervalue.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe WHERE 1=1)";
					} else if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND testdrive_emp_id IN ( " + emp_id + ")";
					}

					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}

					if (mainfilter.equals("team") && !mainfiltervalue.equals("") && team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
								+ " AND team_active = 1 "
								+ " WHERE team_id IN (" + mainfiltervalue + "))";
					} else if (mainfilter.equals("team") && mainfiltervalue.equals("") && team_id.equals("") && !include_inactive_exe.equals("0")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
								+ " AND team_active = 1 "
								+ " WHERE 1=1)";
					} else if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
								+ " AND team_active = 1 "
								+ " WHERE team_id IN (" + team_id + "))";
					}

					// testdrives
					if (targetvalue.equals("testdrives")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "testdrive_time")
								.replaceAll("searchdateend", "testdrive_time")
								+ StrSearch + " AND testdrive_fb_taken = 1";
					}

					if (targetvalue.equals("kpitestdrives")) {
						// StrSearch = StrSearch + " AND testdrive_fb_taken = 1";
						// StrSearch = StrSearch + searchdate.replaceAll("searchdatestart", "testdrive_time")
						// .replaceAll("searchdateend", "testdrive_time");

						StrSearch += " AND enquiry_id IN ( SELECT DISTINCT testdrive_enquiry_id"
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_testdrive"
								+ " WHERE 1 = 1"
								+ " AND testdrive_fb_taken = 1"
								+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
								+ " GROUP BY testdrive_enquiry_id )";

					}
				}

				// *** salesorder
				// SOP("salesorder==="+salesorder);
				if (targetvalue.contains("salesorder")) {
					if (include_inactive_exe.equals("0")) {
						StrSearch += " AND so_emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
								+ " WHERE 1=1 "
								+ " AND team_active = 1)";
					}
					if (mainfilter.equals("brand") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + mainfiltervalue + ")";
					} else if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}

					if (mainfilter.equals("region") && !mainfiltervalue.equals("")) {
						StrSearch += " AND branch_region_id IN (" + mainfiltervalue + ")";
					} else if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}

					if (mainfilter.equals("branch") && !mainfiltervalue.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + mainfiltervalue + ")";
					} else if (!branch_id.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + branch_id + ")";
					}

					if (mainfilter.equals("emp") && !mainfiltervalue.equals("")) {
						StrSearch = StrSearch + " AND emp_id = " + mainfiltervalue + "";
						followup = " AND followup_emp_id =" + mainfiltervalue + "";
						so = " AND so_emp_id IN (" + mainfiltervalue + ")";
					} else if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN ( " + emp_id + ")";
					}

					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND item_model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch = StrSearch + " AND so_enquiry_id IN (SELECT enquiry_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_soe_id IN (" + soe_id + ") )";
					}
					if (!sob_id.equals("")) {
						StrSearch = StrSearch + " AND so_enquiry_id IN (SELECT enquiry_id "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_sob_id IN (" + sob_id + ") )";
					}

					if (mainfilter.equals("team") && !mainfiltervalue.equals("") && team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + mainfiltervalue + "))";
					} else if (mainfilter.equals("team") && mainfiltervalue.equals("") && team_id.equals("") && !include_inactive_exe.equals("0")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE 1=1)";
					} else if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}

					// -------------------------------
					// soretail
					if (targetvalue.equals("salesordersoretail")) {
						StrSearch = StrSearch + " AND so_active='1' "
								// + "and ( (" + salesorderdate + ") "
								+ " AND (SUBSTR(so_retail_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_retail_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// sodelivered
					if (targetvalue.equals("salesordersodelivered")) {
						StrSearch = StrSearch + " AND so_active='1' "
								// + "and ( (" + salesorderdate + ") "
								+ " AND (SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// pendingbooking
					else if (targetvalue.equals("salesorderbooking")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ StrSearch + " AND so_active='1'";
					}

					// pendingdelivery
					else if (targetvalue.equals("salesorderpendingbooking")) {
						StrSearch = StrSearch + " AND so_active = 1 AND so_delivered_date = '' AND so_retail_date = '' ";
					}

					// cancellation
					else if (targetvalue.equals("salesordercancellation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_cancel_date").replaceAll("searchdateend", "so_cancel_date")
								+ StrSearch + " AND so_active='0'";
						// + " AND so_delivered_date='' ";
					}
					// mgaamount
					else if (targetvalue.equals("salesorderaccessamt")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_mga_amount!=0";
					}
					// marutiinsur
					else if (targetvalue.equals("salesorderinsurcount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_insur_amount > 0";
					}
					// extendedwarranty
					else if (targetvalue.equals("salesorderewcount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_ew_amount > 0 ";
					}
					// fincases
					else if (targetvalue.equals("salesorderfincasecount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_finance_amt > 0  AND so_fintype_id = 1 ";
					}
					// exchange
					else if (targetvalue.equals("salesorderexchangecount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_exchange_amount > 0";
					}
				}

				// :- targetvalue
				if (targetvalue.contains("enquiry") || targetvalue.equals("kpitestdrives")) {
					StrSearch += " AND emp_branch_id = enquiry_branch_id";
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				}
				// :- testdrive
				else if (targetvalue.contains("testdrives")) {
					SetSession("testdrivestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/testdrive-list.jsp?smart=yes"));
				}
				// :- salesorder
				else if (targetvalue.contains("salesorder")) {
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				}
				// :- target
				else if (targetvalue.equals("target")) {
					SetSession("targetstrsql", StrSearch, request);
					if (starttime.substring(4, 6).equals(endtime.substring(4, 6))) {
						response.sendRedirect(response.encodeRedirectURL("../sales/target-model-list.jsp?smart=yes&emp_id="
								+ mainfiltervalue + "&year=" + starttime.substring(0, 4) + "&month=" + starttime.substring(4, 6) + ""));
					} else {
						response.sendRedirect(response.encodeRedirectURL("../sales/target-list.jsp?smart=yes&dr_executives="
								+ mainfiltervalue + "&dr_year=" + starttime.substring(0, 4) + ""));
					}
				}
				// :- campaign
				else if (!campaign.equals("")) {
					SetSession("Campaignstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/campaign-list.jsp?smart=yes"));
				}

				// :- preownedenquiry
				else if (!enquiry_preowned_model.equals("")) {
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				}

				// :- preownedso
				else if (!so_preowned_model.equals("")) {
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				}

				// :- preownedtd
				else if (!td_preowned_model.equals("")) {
					SetSession("testdrivestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?smart=yes"));
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
