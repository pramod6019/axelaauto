package axela.sales;
// * @author Smitha
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Monitoring_Board_Search extends Connect {

	public String StrSearch = "", searchdate = "", salesorderdate = "", target = "", campaign = "";
	public String starttime = "", team_id = "", followup = "", so = "", soe_id = "", sob_id = "";
	public String endtime = "", model_id = "", emp_id = "", exe_id = "", dr_branch_id = "", brand_id = "", region_id = "";
	public String include_inactive_exe = "";
	public String enquiry = "", testdrive = "", salesorder = "", StrHTML = "";
	public String enquiry_preowned_model = "", so_preowned_model = "", td_preowned_model = "", hv_preowned_model = "";
	public String preownedmodel_model_id = "";
	public String comp_id = "0";
	Connect cd = new Connect();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = cd.CNumeric(GetSession("comp_id", request));
			SOP("coming");
			if (!comp_id.equals("0")) {
				starttime = cd.PadQuotes(request.getParameter("starttime"));
				endtime = cd.PadQuotes(request.getParameter("endtime"));
				model_id = cd.PadQuotes(request.getParameter("model_id"));
				preownedmodel_model_id = cd.PadQuotes(request.getParameter("model_id"));
				soe_id = cd.PadQuotes(request.getParameter("soe_id"));
				sob_id = cd.PadQuotes(request.getParameter("sob_id"));
				SOP("Soe===" + soe_id);
				SOP("Sob===" + sob_id);
				emp_id = cd.PadQuotes(request.getParameter("emp_id"));
				exe_id = cd.PadQuotes(request.getParameter("exe_id"));
				dr_branch_id = cd.PadQuotes(request.getParameter("dr_branch_id"));
				enquiry = cd.PadQuotes(request.getParameter("enquiry"));
				target = cd.PadQuotes(request.getParameter("target"));
				campaign = cd.PadQuotes(request.getParameter("campaign"));
				testdrive = cd.PadQuotes(request.getParameter("testdrive"));
				salesorder = cd.PadQuotes(request.getParameter("salesorder"));
				team_id = cd.PadQuotes(request.getParameter("team_id"));
				brand_id = cd.PadQuotes(request.getParameter("brand_id"));
				region_id = cd.PadQuotes(request.getParameter("region_id"));
				include_inactive_exe = cd.PadQuotes(request.getParameter("include_inactive_exe"));
				StrSearch = "";
				searchdate = "";
				enquiry_preowned_model = cd.PadQuotes(request.getParameter("enquiry_preowned_model"));
				so_preowned_model = cd.PadQuotes(request.getParameter("so_preowned_model"));
				td_preowned_model = cd.PadQuotes(request.getParameter("td_preowned_model"));
				// *** searchdate
				if (!starttime.equals("") && !endtime.equals("")) {
					searchdate = " AND SUBSTR(searchdatestart, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) AND SUBSTR(searchdateend, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					salesorderdate = "  SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
				}

				if (include_inactive_exe.equals("0")) {
					if (!enquiry.equals("")) {
						StrSearch += " AND emp.emp_active = 1";
					} else if (!testdrive.equals("")) {
						StrSearch += " AND emp_active = 1";
					} else if (!salesorder.equals("")) {
						StrSearch += " AND emp_active = 1";
					}
				}

				// *** enquiry
				if (!enquiry.equals("")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN ( " + emp_id + ")";
						followup = " AND followup_emp_id IN ( " + emp_id + ")";
						so = " AND so_emp_id IN (" + emp_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
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
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (select teamtrans_emp_id from " + cd.compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id IN (" + team_id + "))";
						followup = " AND followup_emp_id IN (select teamtrans_emp_id from " + cd.compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id IN (" + team_id + "))";
						so = " AND so_emp_id IN (select teamtrans_emp_id from " + cd.compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id IN (" + team_id + "))";
					}
					// enquiryopen
					if (enquiry.equals("enquiryopen")) {
						// cd.SOP("====enquiryopen====");
						StrSearch = StrSearch + " AND enquiry_status_id=1"
								+ " AND SUBSTR(enquiry_date,1,8) < SUBSTR('" + starttime + "',1,8) ";
					}
					// enquiryfresh
					else if (enquiry.equals("enquiryfresh")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch;
						// + " AND enquiry_status_id = 1 ";
					}
					// enquirylost

					else if (enquiry.equals("enquirylost")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch
								+ " AND (enquiry_status_id=3 OR enquiry_status_id=4)";
					}
					// enquiryrecvd
					else if (enquiry.equals("enquiryrecvd")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch + " "
								+ " AND campaign_id!='1' AND "
								+ " ((SUBSTR(campaign_startdate,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(campaign_startdate,1,8) < SUBSTR('" + endtime + "',1,8))"
								+ " OR (SUBSTR(campaign_enddate,1,8) > SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(campaign_enddate,1,8) <= SUBSTR('" + endtime + "',1,8))"
								+ " OR (SUBSTR(campaign_startdate,1,8) < SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(campaign_enddate,1,8) > SUBSTR('" + endtime + "',1,8)))"
								+ "";
					}
					// pendingenquiry
					else if (enquiry.equals("pendingenquiry")) {
						StrSearch = StrSearch + " AND enquiry_status_id = 1";
						// + " AND SUBSTR(enquiry_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
						// + " AND SUBSTR(enquiry_date,1,8) <= SUBSTR('" + endtime + "',1,8)";
						// + " AND enquiry_status_id=1 "
						// + " AND (enquiry_status_id!=3 OR enquiry_status_id!=4 )"
						// + " AND enquiry_id Not IN (SELECT so_enquiry_id FROM " + cd.compdb(comp_id) + "axela_sales_so "
						// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id "
						// + " WHERE so_active='1' " // + so + " "
						// + " AND so_delivered_date = ''"
						// + " AND ((" + salesorderdate + ") "
						// + " AND (SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
						// + " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8))))";

					}
					// corpcases
					else if (enquiry.equals("corpcases")) {
						StrSearch = StrSearch + " AND 1=2 AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<SUBSTR('" + endtime + "',1,8) "
								+ " AND SUBSTR(campaign_startdate,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<SUBSTR('" + endtime + "',1,8)";
					}
					// corpvisit
					else if (enquiry.equals("corpvisit")) {
						StrSearch = StrSearch + " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<SUBSTR('" + endtime + "',1,8) "
								+ " AND SUBSTR(campaign_startdate,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<SUBSTR('" + endtime + "',1,8)";
					}
					// ssiscore
					else if (enquiry.equals("ssiscore")) {
						StrSearch = StrSearch + " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<SUBSTR('" + endtime + "',1,8) "
								+ " AND SUBSTR(campaign_startdate,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
					}
					// homevisit
					else if (enquiry.equals("homevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1 = 1 " + followup
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time")
										.replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					// kpihomevisit
					else if (enquiry.equals("kpihomevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (select DISTINCT(followup_enquiry_id) "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "// + followup
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time")
										.replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					else if (enquiry.equals("evaluation")) {
						// SOP("salesorder------" + salesorder);
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ StrSearch + " AND enquiry_id IN (select enquiry_id FROM " + cd.compdb(comp_id) + "axela_sales_enquiry" + ")"
								+ " AND enquiry_evaluation = 1 ";
						// + " where 1=1";
						// + "so_exchange=1 ";
					}
					else if (enquiry.equals("preowned")) {
						StrSearch += " AND enquiry_id IN (select preowned_enquiry_id FROM " + cd.compdb(comp_id) + "axela_preowned"
								+ " WHERE 1=1"
								+ " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
						if (!emp_id.equals("")) {
							StrSearch += " AND preowned_sales_emp_id IN (" + emp_id + ")";
						}
						StrSearch += " )";
						// SOP("StrSearch------" + StrSearch);
					}

					else if (enquiry.equals("kpitestdrives")) {
						StrSearch += " AND enquiry_id IN ( SELECT DISTINCT enquiry_id"
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_testdrive"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
								+ " WHERE 1 = 1"
								+ " AND testdrive_fb_taken = 1"
								+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) )";
					}
				}

				// *** campaign
				if (!campaign.equals("")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND branch_id IN (" + dr_branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (select teamtrans_emp_id "
								+ "from " + cd.compdb(comp_id) + "axela_sales_team_exe "
								+ "where teamtrans_team_id IN (" + team_id + "))";
					}
					// eventplanned
					if (campaign.equals("eventplanned")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "campaign_startdate")
								.replaceAll("searchdateend", "campaign_enddate")
								+ StrSearch + " AND campaign_active='1'";
						// SOP("==="+StrSearch);
					}
					// eventactual
					else if (campaign.equals("eventactual")) {
						StrSearch = StrSearch + " AND "
								+ " ((SUBSTR(campaign_startdate,1,8) >= SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(campaign_startdate,1,8) < SUBSTR('" + endtime + "',1,8))"
								+ " OR (SUBSTR(campaign_enddate,1,8) > SUBSTR('" + starttime + "',1,8)"
								+ " AND SUBSTR(campaign_enddate,1,8) <= SUBSTR('" + endtime + "',1,8))"
								+ " OR (SUBSTR(campaign_startdate,1,8) < SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(campaign_enddate,1,8) > SUBSTR('" + endtime + "',1,8)))"
								+ " AND campaign_active='1' AND campaign_id!='1' "
								+ " AND campaign_id IN (SELECT enquiry_campaign_id"
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_emp_id=" + emp_id + ")";
					}
				}

				// *** testdrive
				if (!testdrive.equals("")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND emp_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND testdrive_emp_id IN ( " + emp_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND testdrive_emp_id IN (" + exe_id + ")";
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
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (select teamtrans_emp_id "
								+ "from " + cd.compdb(comp_id) + "axela_sales_team_exe "
								+ "where teamtrans_team_id IN (" + team_id + "))";
					}
					// testdrives
					if (testdrive.equals("testdrives")) {
						StrSearch = StrSearch + " AND testdrive_fb_taken = 1";
						StrSearch = StrSearch + searchdate.replaceAll("searchdatestart", "testdrive_time")
								.replaceAll("searchdateend", "testdrive_time");
					}
				}

				// *** salesorder
				// cd.SOP("salesorder==="+salesorder);
				if (!salesorder.equals("")) {

					StrSearch += " AND so_emp_id IN (SELECT teamtrans_emp_id"
							+ " FROM " + cd.compdb(comp_id) + "axela_sales_team_exe"
							+ " INNER JOIN " + cd.compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
							+ " WHERE 1=1 "
							+ " AND team_active = 1)";

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND so_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN ( " + emp_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND item_model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch = StrSearch + " AND so_enquiry_id IN (SELECT enquiry_id "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_soe_id IN (" + soe_id + ") )";
					}
					if (!sob_id.equals("")) {
						StrSearch = StrSearch + " AND so_enquiry_id IN (SELECT enquiry_id "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_sob_id IN (" + sob_id + ") )";
					}
					if (!team_id.equals("")) {
						// SOP("team_id===" + team_id);
						StrSearch = StrSearch + " AND emp_id IN (select teamtrans_emp_id "
								+ "from " + cd.compdb(comp_id) + "axela_sales_team_exe "
								+ "where teamtrans_team_id IN (" + team_id + "))";
					}
					// soretail
					if (salesorder.equals("soretail")) {
						StrSearch = StrSearch + " AND so_active='1' "
								// + "and ( (" + salesorderdate + ") "
								+ " AND (SUBSTR(so_retail_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_retail_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// sodelivered
					if (salesorder.equals("sodelivered")) {
						StrSearch = StrSearch + " AND so_active='1' "
								// + "and ( (" + salesorderdate + ") "
								+ " AND (SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// pendingbooking
					else if (salesorder.equals("pendingbooking")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ StrSearch + " AND so_active='1'";
						// + " AND so_delivered_date='' ";
					}

					// pendingdelivery
					else if (salesorder.equals("pendingdelivery")) {
						StrSearch = StrSearch + " AND so_active = 1 AND so_delivered_date = '' AND so_retail_date = '' ";
						// StrSearch += searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date");
					}

					// cancellation
					else if (salesorder.equals("cancellation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_cancel_date").replaceAll("searchdateend", "so_cancel_date")
								+ StrSearch + " AND so_active='0'";
						// + " AND so_delivered_date='' ";
					}
					// mgaamount
					else if (salesorder.equals("mgaamount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_mga_amount!=0";
					}
					// marutiinsur
					else if (salesorder.equals("marutiinsur")) {
						// cd.SOP("====marutiinsur====");
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_insur_amount > 0 AND so_id IN (select soitem_so_id from " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " where 1 = 1)";
						// cd.SOP("StrSearch==="+StrSearch);
					}
					// extendedwarranty
					else if (salesorder.equals("extendedwarranty")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_ew_amount > 0 AND so_id IN (select soitem_so_id from " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " where 1=1 AND soitem_rowcount != 0 )";
					}
					// fincases
					else if (salesorder.equals("fincases")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_finance_amt > 0  AND so_fintype_id = 1 ";
						// + " AND so_id IN (SELECT soitem_so_id FROM " + cd.compdb(comp_id) + "axela_sales_so_item"
						// + " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
						// + " WHERE 1 = 1)";
						// so_delivered_date!='' AND so_fintype_id=1 ";
					}
					// exchange
					else if (salesorder.equals("exchange")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ StrSearch + " AND so_active='1' AND so_exchange_amount > 0"
								// + AND so_exchange=1 "
								+ " AND so_id IN (SELECT soitem_so_id FROM " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " WHERE 1 = 1)";
						// + "so_exchange=1 ";
					}

				}

				// // Enquiry Preowned Model Part
				if (!enquiry_preowned_model.equals("")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_emp_id IN (" + emp_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN (SELECT teamtrans_emp_id FROM " + cd.compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}

					if (enquiry_preowned_model.equals("enquirypreownedmodel_open")) {
						StrSearch += " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND enquiry_status_id = 1"
								+ " AND enquiry_enquirytype_id = 2";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ") ";
						}

					}

					else if (enquiry_preowned_model.equals("enquirypreownedmodel_fresh")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ " AND enquiry_enquirytype_id = 2";
						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ") ";
						}
					}

					else if (enquiry_preowned_model.equals("enquirypreownedmodel_lost")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ " AND (enquiry_status_id = 3 OR enquiry_status_id = 4)";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ") ";
						}
					}

					else if (enquiry_preowned_model.equals("enquirypreownedmodel_pending")) {
						StrSearch += " AND enquiry_status_id = 1";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (enquiry_preowned_model.equals("enquirypreownedmodel_homevisit")) {
						StrSearch += " AND enquiry_id IN (SELECT followup_enquiry_id "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1 = 1 " + followup
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time").replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (enquiry_preowned_model.equals("enquirypreownedmodel_kpihomevisit")) {
						StrSearch += " AND enquiry_id IN (SELECT DISTINCT(followup_enquiry_id) "
								+ " FROM " + cd.compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1 = 1 "// + followup
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time").replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (enquiry_preowned_model.equals("enquiry_preowned_model_preowned")) {
						StrSearch += StrSearch += " AND enquiry_id IN (select preowned_enquiry_id FROM " + cd.compdb(comp_id) + "axela_preowned"
								+ " WHERE 1=1"
								+ " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
						if (!emp_id.equals("")) {
							StrSearch += " AND preowned_sales_emp_id IN (" + emp_id + ")";
						}
						StrSearch += " )";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (enquiry_preowned_model.equals("enquiry_preowned_model_evaluation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ StrSearch + " AND enquiry_id IN (SELECT enquiry_id FROM " + cd.compdb(comp_id) + "axela_sales_enquiry" + ")"
								+ " AND enquiry_evaluation = 1 ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND enquiry_id IN("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

				}

				if (!so_preowned_model.equals("")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND so_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + emp_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN (SELECT teamtrans_emp_id FROM " + cd.compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}

					if (so_preowned_model.equals("so_preowned_model_soretail")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ " AND so_active = '1' ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (so_preowned_model.equals("so_preowned_model_sodelivered")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "so_delivered_date").replaceAll("searchdateend", "so_delivered_date")
								+ " AND so_active = '1' ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (so_preowned_model.equals("so_preowned_model_pendingbooking")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ " AND so_active = '1' ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ("
									+ " SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (so_preowned_model.equals("so_preowned_model_pendingdelivery")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ " AND so_active = '1' "
								+ " AND so_delivered_date = '' "
								+ " AND so_retail_date = '' ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ( "
									+ " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (so_preowned_model.equals("so_preowned_model_cancellation")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ " AND so_active = '0' "
								+ " AND so_delivered_date = '' "
								+ " AND so_retail_date = '' ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ( "
									+ " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}
					else if (so_preowned_model.equals("so_preowned_model_mgaamount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_mga_amount!= 0";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND so_enquiry_id IN ( "
									+ " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (so_preowned_model.equals("so_preowned_model_marutiinsur")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_insur_amount > 0 AND so_id IN (select soitem_so_id from " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " where 1 = 1)";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}

					}

					else if (so_preowned_model.equals("so_preowned_model_extendedwarranty")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_ew_amount > 0 AND so_id IN (SELECT soitem_so_id FROM " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " WHERE 1 = 1 AND soitem_rowcount != 0 )";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}
					else if (so_preowned_model.equals("so_preowned_model_fincases")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_finance_amt > 0  AND so_fintype_id = 1 "
								+ " AND so_id IN (SELECT soitem_so_id FROM " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " WHERE 1 = 1)";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}
					else if (so_preowned_model.equals("so_preowned_model_exchange")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ StrSearch + " AND so_active='1' AND so_exchange_amount > 0"
								+ " AND so_id IN (SELECT soitem_so_id FROM " + cd.compdb(comp_id) + "axela_sales_so_item"
								+ " INNER JOIN " + cd.compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
								+ " WHERE 1 = 1)";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

				}

				if (!td_preowned_model.equals("")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (" + dr_branch_id + ")";
					}
					if (!emp_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_emp_id IN (" + emp_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch += StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN (SELECT teamtrans_emp_id FROM " + cd.compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}

					if (td_preowned_model.equals("td_preowned_model_testdrives")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "testdrive_time").replaceAll("searchdateend", "testdrive_time")
								+ " AND testdrive_fb_taken = 1 ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND testdrive_enquiry_id IN ( "
									+ " SELECT enquiry_id "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}

					else if (td_preowned_model.equals("td_preowned_model_kpitestdrives")) {
						StrSearch += searchdate.replaceAll("searchdatestart", "testdrive_time").replaceAll("searchdateend", "testdrive_time")
								+ " AND testdrive_fb_taken = 1 ";

						if (!preownedmodel_model_id.equals("")) {
							StrSearch += " AND testdrive_enquiry_id IN ( "
									+ " SELECT DISTINCT(enquiry_id) "
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
									+ " WHERE variant_preownedmodel_id = " + preownedmodel_model_id + ")";
						}
					}
				}

				// cd.SOP("StrSearch ---" + StrSearch); evaluation
				// :- enquiry
				if (!enquiry.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				}
				// :- testdrive
				else if (!testdrive.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("testdrivestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/testdrive-list.jsp?smart=yes"));
				}
				// :- salesorder
				else if (!salesorder.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				}
				// :- target
				else if (!target.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("targetstrsql", StrSearch, request);
					if (starttime.substring(4, 6).equals(endtime.substring(4, 6))) {
						response.sendRedirect(response.encodeRedirectURL("../sales/target-model-list.jsp?smart=yes&emp_id="
								+ emp_id + "&year=" + starttime.substring(0, 4) + "&month=" + starttime.substring(4, 6) + ""));
					} else {
						response.sendRedirect(response.encodeRedirectURL("../sales/target-list.jsp?smart=yes&dr_executives="
								+ emp_id + "&dr_year=" + starttime.substring(0, 4) + ""));
					}
				}
				// :- campaign
				else if (!campaign.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("Campaignstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/campaign-list.jsp?smart=yes"));
				}

				// :- preownedenquiry
				else if (!enquiry_preowned_model.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				}

				// :- preownedso
				else if (!so_preowned_model.equals("")) {
					// SOP("StrSearch==" + StrSearch);
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				}

				// :- preownedtd
				else if (!td_preowned_model.equals("")) {
					// SOP("StrSearch==" + StrSearch);
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
