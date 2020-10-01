package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Enquiry_Check_Search extends Connect {

	public String StrSearch = "", enquirydate = "", followupdate = "", testdrivedate = "", quotedate = "",
			salesorderdate = "", receiptdate = "", sodelivereddate = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String starttime = "";
	public String endtime = "", model_id = "", region_id = "",
			emp_id = "", exe_id = "", team_id = "", dr_branch_id = "";
	public String enquiry = "", testdrive = "", quote = "",
			salesorder = "", receipt = "", netdeliveries = "", currentdeliveries = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				model_id = PadQuotes(request.getParameter("model_id"));
				region_id = PadQuotes(request.getParameter("region_id"));
				// SOP("model_id = " + model_id + "\t" + "region_id =" + region_id);
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				exe_id = PadQuotes(request.getParameter("exe_id"));
				// SOP("exe_id = " + exe_id);
				team_id = PadQuotes(request.getParameter("team_id"));
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				enquiry = PadQuotes(request.getParameter("enquiry"));
				testdrive = PadQuotes(request.getParameter("testdrive"));
				quote = PadQuotes(request.getParameter("quote"));
				salesorder = PadQuotes(request.getParameter("salesorder"));
				receipt = PadQuotes(request.getParameter("receipt"));
				netdeliveries = PadQuotes(request.getParameter("netdeliveries"));
				currentdeliveries = PadQuotes(request.getParameter("currentdeliveries"));

				// SOP("starttime===" + starttime);

				StrSearch = "";
				enquirydate = "";
				followupdate = "";
				testdrivedate = "";
				quotedate = "";
				salesorderdate = "";
				receiptdate = "";
				sodelivereddate = "";

				if (!starttime.equals("") && !endtime.equals("")) {
					enquirydate = " AND substr(enquiry_date,1,8)>=substr('" + starttime + "',1,8) AND substr(enquiry_date,1,8)<=substr('" + endtime + "',1,8)";
					followupdate = " AND substr(followup_followup_time,1,8)>=substr('" + starttime + "',1,8) AND substr(followup_followup_time,1,8)<=substr('" + endtime + "',1,8)";
					testdrivedate = " AND substr(testdrive_time,1,8)>=substr('" + starttime + "',1,8) AND substr(testdrive_time,1,8)<=substr('" + endtime + "',1,8)";
					quotedate = " AND substr(quote_date,1,8)>=substr('" + starttime + "',1,8) AND substr(quote_date,1,8)<=substr('" + endtime + "',1,8)";
					salesorderdate = " AND substr(so_date,1,8)>=substr('" + starttime + "',1,8) AND substr(so_date,1,8)<=substr('" + endtime + "',1,8)";
					sodelivereddate = " AND substr(so_delivered_date,1,8)>=substr('" + starttime + "',1,8) AND substr(so_delivered_date,1,8)<=substr('" + endtime + "',1,8)";
					receiptdate = " AND substr(receipt_date,1,8)>=substr('" + starttime + "',1,8) AND substr(receipt_date,1,8)<=substr('" + endtime + "',1,8)";

				}
				if (!enquiry.equals("")) {
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND enquiry_branch_id =" + dr_branch_id;
					}
					if (!emp_id.equals("0")) {
						StrSearch = StrSearch + " AND emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (enquiry.equals("enquiry")) {
						StrSearch = enquirydate + StrSearch + "";
					} else if (enquiry.equals("unqual")) {
						StrSearch = enquirydate + StrSearch + " AND enquiry_model_id=0 ";
					} else if (enquiry.equals("qual")) {
						StrSearch = enquirydate + StrSearch + " AND enquiry_model_id>0 ";
					} else if (enquiry.equals("closed")) {
						StrSearch = enquirydate + StrSearch + " AND enquiry_status_id>2";
					} else if (enquiry.equals("hot")) {
						StrSearch = enquirydate + StrSearch + " AND enquiry_priorityenquiry_id=1 AND enquiry_status_id=1";
						// SOP("StrSearch-------" + StrSearch);
					} else if (enquiry.equals("open")) {
						StrSearch = enquirydate + StrSearch + " AND enquiry_status_id = 1 ";
					} else if (enquiry.equals("meetingsplanned")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_followuptype_id=2 AND followup_emp_id=emp_id)";
					} else if (enquiry.equals("meetingscompleted")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_feedbacktype_id = 5 AND followup_emp_id=emp_id)";
					} else if (enquiry.equals("kpimeetings")) {
						StrSearch = StrSearch + enquirydate + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_feedbacktype_id=2"
								+ " AND followup_emp_id=emp_id)";

					} else if (enquiry.equals("followupplanned")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_emp_id=emp_id)";
					}
					else if (enquiry.equals("followupcompleted")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_desc != '' AND followup_emp_id = emp_id)";
					}
					else if (enquiry.equals("homevisitplanned")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_followuptype_id = 3 AND followup_emp_id=emp_id)";
					}
					else if (enquiry.equals("homevisitcompleted")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_feedbacktype_id = 9 AND followup_desc != '' AND followup_emp_id=emp_id)";
					}
					else if (enquiry.equals("escalation")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ followupdate
								+ " AND followup_trigger=5 AND followup_emp_id=emp_id)";
					}
				}

				if (!testdrive.equals("")) {
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND enquiry_branch_id =" + dr_branch_id;
					}
					if (!emp_id.equals("0") && !testdrive.equals("kpi")) {
						StrSearch = StrSearch + " AND testdrive_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("") && !testdrive.equals("kpi")) {
						StrSearch = StrSearch + " AND testdrive_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (testdrive.equals("planned")) {
						StrSearch = StrSearch + testdrivedate;
					} else if (testdrive.equals("completed")) {
						StrSearch = StrSearch + testdrivedate + " AND testdrive_fb_taken=1 ";
					} else if (testdrive.equals("kpi")) {
						StrSearch = StrSearch +
								// enquirydate +
								" AND enquiry_id IN (SELECT testdrive_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
								+ " WHERE 1=1 " + testdrivedate
								+ " AND testdrive_type = 1"
								+ " AND testdrive_fb_taken=1"
								+ " AND testdrive_fb_entry_date != '' ";
						if (!emp_id.equals("0") && testdrive.equals("kpi")) {
							StrSearch = StrSearch + " AND testdrive_emp_id = " + emp_id + "";
						}
						if (!exe_id.equals("") && testdrive.equals("kpi")) {
							StrSearch = StrSearch + " AND testdrive_emp_id IN (" + exe_id + ")";
						}
						StrSearch = StrSearch + " )"; // AND
														// testdrive_fb_status_id=0
					} else if (testdrive.equals("feedback")) {
						StrSearch = StrSearch + " AND substr(testdrive_client_fb_entry_date,1,8)>=substr('" + starttime + "',1,8)"
								+ " AND substr(testdrive_client_fb_entry_date,1,8)<substr('" + endtime + "',1,8)"
								+ " AND testdrive_fb_taken=1"
								+ " AND testdrive_fb_entry_date != ''";
					}
				}

				if (!quote.equals("")) {
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("0") && !quote.equals("kpiquote")) {
						StrSearch += " AND quote_branch_id =" + dr_branch_id;
					}
					if (!emp_id.equals("0") && !quote.equals("kpiquote")) {
						StrSearch = StrSearch + " AND quote_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("") && !quote.equals("kpiquote")) {
						StrSearch = StrSearch + " AND quote_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND item_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (quote.equals("quote")) {
						StrSearch = StrSearch + quotedate + " AND quote_active='1' ";
					} else if (quote.equals("kpiquote")) {
						StrSearch = StrSearch + enquirydate + " AND enquiry_id IN (SELECT quote_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_quote "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON quoteitem_item_id = item_id"
								+ " WHERE 1=1 " + quotedate + ""
								+ " AND quote_active='1'";
						if (!dr_branch_id.equals("0") && quote.equals("kpiquote")) {
							StrSearch = StrSearch + " AND quote_branch_id =" + dr_branch_id;
						}
						if (!emp_id.equals("0") && quote.equals("kpiquote")) {
							StrSearch = StrSearch + " AND quote_emp_id = " + emp_id + "";
						}

						if (!exe_id.equals("") && quote.equals("kpiquote")) {
							StrSearch = StrSearch + " AND quote_emp_id IN (" + exe_id + ")";
						}
						StrSearch = StrSearch + " )";
						// } else if (quote.equals("kpi")) {
						// StrSearch = StrSearch + quotedate +
						// " AND testdrive_fb_taken=1 AND testdrive_fb_entry_date != '' AND testdrive_fb_status_id=0";
					}
				}

				if (!salesorder.equals("")) {
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND so_branch_id =" + dr_branch_id;
					}
					if (!emp_id.equals("0")) {
						StrSearch = StrSearch + " AND so_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND item_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (salesorder.equals("salesorder")) {
						StrSearch = StrSearch + salesorderdate + " AND so_active='1' ";
					}
					if (salesorder.equals("netdeliveries")) {
						StrSearch = StrSearch + sodelivereddate + " AND so_active='1' ";
					}

					if (salesorder.equals("currentdeliveries")) {
						StrSearch = StrSearch + sodelivereddate + " AND substr(so_date,1,6) = substr(so_delivered_date,1,6) AND so_active='1'";
					}
				}

				if (!receipt.equals("")) {
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND receipt_branch_id =" + dr_branch_id;
					}
					if (!emp_id.equals("0")) {
						StrSearch = StrSearch + " AND receipt_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND receipt_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND item_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (receipt.equals("receipt")) {
						StrSearch = StrSearch + receiptdate + " AND receipt_active='1' ";
					}
				}
				// SOP("StrSearch   ---"+StrSearch);

				if (!enquiry.equals("")) {
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				} else if (!testdrive.equals("") && !testdrive.equals("kpi")) {
					SetSession("testdrivestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/testdrive-list.jsp?smart=yes"));
				} else if (testdrive.equals("kpi")) {
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				} else if (!quote.equals("") && !quote.equals("kpiquote")) {
					SetSession("quotestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-quote-list.jsp?smart=yes"));
				} else if (quote.equals("kpiquote")) {
					SetSession("enquirystrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				} else if (!salesorder.equals("")) {
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				} else if (!receipt.equals("")) {
					SetSession("receiptstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../invoice/receipt-list.jsp?smart=yes"));
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
