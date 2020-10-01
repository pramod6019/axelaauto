package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Check_Search extends Connect {
	
	public String StrSearch = "", StrFilter = "", followupdate = "", testdrivedate = "",
			preowneddate = "", closeddate = "", evaldate = "", stockdate = "",
			quotedate = "", salesorderdate = "", receiptdate = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String starttime = "";
	public String endtime = "";
	public String model_id = "", emp_id = "", exe_id = "", team_id = "", region_id = "", brand_id = "",
			branch_id = "", carmanuf_id = "";
	public String preowned = "", salesenquires = "", eval = "", testdrive = "", quote = "",
			salesorder = "", receipt = "";
	public String type = "", typeid = "", include_inactive_exe = "";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				carmanuf_id = PadQuotes(request.getParameter("carmanuf_id"));
				model_id = PadQuotes(request.getParameter("model_id"));
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				exe_id = PadQuotes(request.getParameter("exe_id"));
				team_id = PadQuotes(request.getParameter("team_id"));
				brand_id = PadQuotes(request.getParameter("brand_id"));
				region_id = PadQuotes(request.getParameter("region_id"));
				branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				preowned = PadQuotes(request.getParameter("preowned"));
				salesenquires = PadQuotes(request.getParameter("salesenquires"));
				eval = PadQuotes(request.getParameter("eval"));
				testdrive = PadQuotes(request.getParameter("testdrive"));
				quote = PadQuotes(request.getParameter("quote"));
				salesorder = PadQuotes(request.getParameter("salesorder"));
				receipt = PadQuotes(request.getParameter("receipt"));
				type = PadQuotes(request.getParameter("type"));
				typeid = PadQuotes(request.getParameter("typeid"));
				include_inactive_exe = CNumeric(PadQuotes(request.getParameter("include_inactive_exe")));
				
				StrSearch = "";
				// followupdate = "";
				followupdate = "";
				testdrivedate = "";
				quotedate = "";
				salesorderdate = "";
				receiptdate = "";
				
				if (!brand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + region_id + ")";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND branch_id IN (" + branch_id + ")";
				}
				
				// Add this filter only when include inactice executive checkbox is not checked.
				if (include_inactive_exe.equals("0")) {
					StrSearch += " AND emp_active = 1 ";
				}
				
				if (!starttime.equals("") && !endtime.equals("")) {
					preowneddate = " AND SUBSTR(preowned_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(preowned_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					evaldate = " AND SUBSTR(eval_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(eval_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					followupdate = " AND SUBSTR(preownedfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(preownedfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					closeddate = " AND SUBSTR(preowned_preownedstatus_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(preowned_preownedstatus_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					testdrivedate = " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					
					stockdate = " AND SUBSTR(preownedstock_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(preownedstock_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					quotedate = " AND SUBSTR(quote_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(quote_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					salesorderdate = " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					receiptdate = " AND SUBSTR(receipt_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(receipt_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					
				}
				if (!preowned.equals("") && !preowned.equals("stock")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND preowned_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
						case "emp" :
							StrSearch += " AND preowned_emp_id =" + typeid;
							break;
					}
					
					if (preowned.equals("preowned")) {
						StrSearch += preowneddate
								+ " AND preowned_enquiry_id = 0";
					} else if (preowned.equals("salesenquires")) {
						StrSearch += preowneddate
								+ " AND preowned_enquiry_id != 0";
					} else if (preowned.equals("eval")) {
						StrSearch += " AND preowned_id IN ("
								+ " SELECT eval_preowned_id"
								+ " FROM " + compdb(comp_id) + "axela_preowned_eval "
								+ " WHERE 1 = 1"
								+ evaldate
								+ " )";
					} else if (preowned.equals("open")) {
						StrSearch += " AND preowned_preownedstatus_id = 1 "
								+ preowneddate;
					} else if (preowned.equals("closed")) {
						StrSearch += closeddate
								+ " AND preowned_preownedstatus_id >= 2";
					} else if (preowned.equals("hot")) {
						StrSearch += " AND preowned_prioritypreowned_id = 1"
								+ " AND preowned_preownedstatus_id = 1 "
								+ preowneddate;
					} else if (preowned.equals("meetingsplanned")) {
						
						StrSearch += " AND preownedfollowup_id IN ( "
								+ " SELECT preownedfollowup_id FROM " + compdb(comp_id) + "axela_preowned_followup "
								+ " WHERE 1 = 1 "
								+ " AND preownedfollowup_preownedfollowuptype_id IN (2, 4)"
								+ followupdate
								+ " )";
						
					} else if (preowned.equals("meetingscompleted")) {
						StrSearch += " AND preownedfollowup_id IN ("
								+ " SELECT preownedfollowup_id FROM " + compdb(comp_id) + "axela_preowned_followup "
								+ " WHERE 1 = 1 "
								+ followupdate
								+ " AND preownedfollowup_preownedfeedbacktype_id IN (2, 5)" // for meetings planned
								+ " )";
					} else if (preowned.equals("kpimeetings")) {
						StrSearch += " and preownedfollowup_id IN ("
								+ " SELECT preownedfollowup_id FROM " + compdb(comp_id) + "axela_preowned_followup "
								+ " WHERE 1 = 1 "
								+ followupdate
								+ " AND preownedfollowup_preownedfeedbacktype_id IN (2, 5)"
								+ " )";
						
					} else if (preowned.equals("followup")) {
						StrSearch += " AND preownedfollowup_id IN ("
								+ " SELECT preownedfollowup_id FROM " + compdb(comp_id) + "axela_preowned_followup "
								+ " WHERE 1 = 1 "
								+ followupdate
								+ " )";
					} else if (preowned.equals("escalation")) {
						StrSearch += " AND preownedfollowup_id IN ("
								+ " SELECT preownedfollowup_id FROM " + compdb(comp_id) + "axela_preowned_followup "
								+ " WHERE 1 = 1 "
								+ followupdate
								+ " AND preownedfollowup_trigger = 5"
								+ " )";
					}
					
					if (!branch_id.equals("")) {
						StrSearch += " AND preowned_branch_id IN (" + branch_id + ")";
					}
					if (!emp_id.equals("0")) {
						StrSearch += " AND emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("")) {
						StrSearch += " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND preowned_preownedmodel_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
								+ " WHERE preownedteamtrans_team_id IN (" + team_id + "))";
					}
					
				}
				
				if (preowned.equals("stock")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND preowned_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND stock.emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
					}
					
					StrSearch += " AND preowned_id IN ( "
							+ " SELECT "
							+ " preownedstock_preowned_id "
							+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
							+ " WHERE 1 = 1";
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					StrSearch += stockdate;
					if (type.equals("emp")) {
						StrSearch += " AND preownedstock_emp_id = " + typeid + "";
					}
					if (!exe_id.equals("")) {
						StrSearch += " AND preownedstock_emp_id IN (" + exe_id + ")";
					}
					StrSearch += " )";
					// SOP("StrSearch==" + StrSearch);
					
				}
				
				if (!testdrive.equals("")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND preowned_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
						case "emp" :
							StrSearch += " AND testdrive_emp_id =" + typeid; // changes for checking TD
							break;
					}
					
					if (!branch_id.equals("")) {
						StrSearch += " AND preowned_branch_id  IN (" + branch_id + ")";
					}
					if (!emp_id.equals("0") && !testdrive.equals("kpi")) {
						StrSearch += " AND testdrive_emp_id = " + emp_id + "";
					}
					if (!exe_id.equals("") && !testdrive.equals("kpi")) {
						StrSearch += " AND testdrive_emp_id IN (" + exe_id + ")";
					}
					
					if (!model_id.equals("")) {
						StrSearch += " AND preowned_preownedmodel_id IN (" + model_id + ")";
					}
					
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					
					if (testdrive.equals("planned")) {
						StrSearch += testdrivedate;
					} else if (testdrive.equals("completed")) {
						StrSearch += testdrivedate
								+ " AND testdrive_fb_taken = 1"
								+ " AND testdrive_fb_entry_date != ''";
					} else if (testdrive.equals("kpi")) {
						StrSearch += " AND enquiry_enquirytype_id IN (2,4)"
								+ " AND enquiry_id IN (SELECT DISTINCT testdrive_enquiry_id FROM " + compdb(comp_id) + "axela_preowned_testdrive " // for checking kpi count
								+ " WHERE 1 = 1 "
								+ testdrivedate
								+ " AND testdrive_fb_taken = 1"
								+ " AND testdrive_fb_entry_date != '' ";
						if (!emp_id.equals("0") && testdrive.equals("kpi")) {
							StrSearch += " AND testdrive_emp_id = " + emp_id + "";
						}
						if (!exe_id.equals("") && testdrive.equals("kpi")) {
							StrSearch += " AND enquiry_emp_id IN (" + exe_id + ")";
						}
						StrSearch += " )";
						
					} else if (testdrive.equals("feedback")) {
						StrSearch += " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
								+ " AND SUBSTR(testdrive_time, 1, 8)  <=  SUBSTR('" + endtime + "', 1, 8) "
								+ " AND testdrive_fb_taken = 1"
								+ " AND testdrive_fb_entry_date != ''";
					}
				}
				
				if (!quote.equals("")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND quote_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
						case "emp" :
							StrSearch += " AND quote_emp_id =" + typeid;
							break;
					}
					
					if (!branch_id.equals("") && !quote.equals("kpiquote")) {
						StrSearch = " AND quote_branch_id IN (" + branch_id + ")";
					}
					
					if (!emp_id.equals("0") && !quote.equals("kpiquote")) {
						StrSearch += " AND quote_emp_id = " + emp_id + "";
					}
					
					if (!exe_id.equals("") && !quote.equals("kpiquote")) {
						StrSearch += " AND quote_emp_id IN (" + exe_id + ")";
					}
					
					if (!model_id.equals("")) {
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}
					
					if (!team_id.equals("")) {
						StrSearch += " AND quote_emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id in (" + team_id + "))";
					}
					
					if (quote.equals("quote")) {
						
						StrSearch += " AND quote_id IN ( "
								+ " SELECT quote_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_quote "
								+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = quote_preownedstock_id"
								+ " WHERE 1 = 1 "
								+ quotedate
								+ " AND quote_active = '1'"
								+ " ) ";
					} else if (quote.equals("kpiquote")) {
						StrSearch += " AND quote_id IN ( "
								+ " SELECT DISTINCT quote_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_quote "
								+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = quote_preownedstock_id"
								+ " WHERE 1 = 1 "
								+ quotedate
								+ " AND quote_active = '1'";
						if (!branch_id.equals("") && quote.equals("kpiquote")) {
							StrSearch += " AND quote_branch_id  IN (" + branch_id + ")";
						}
						if (!emp_id.equals("0") && quote.equals("kpiquote")) {
							StrSearch += " AND quote_emp_id = " + emp_id + "";
						}
						if (!exe_id.equals("") && quote.equals("kpiquote")) {
							StrSearch += " AND quote_emp_id in (" + exe_id + ")";
						}
						StrSearch += " )";
					}
				}
				
				if (!salesorder.equals("")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND preowned_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
						case "emp" :
							StrSearch += " AND preowned_emp_id =" + typeid;
							break;
					}
					
					if (!branch_id.equals("")) {
						StrSearch = " AND so_branch_id  IN (" + branch_id + ")";
					}
					
					if (!emp_id.equals("0")) {
						StrSearch += " AND so_emp_id = " + emp_id + "";
					}
					
					if (!exe_id.equals("")) {
						StrSearch += " AND so_emp_id IN (" + exe_id + ")";
					}
					
					if (!model_id.equals("")) {
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}
					
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN ( "
								+ " SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					
					if (salesorder.equals("salesorder")) {
						StrSearch += salesorderdate + " AND so_active = 1 ";
					}
				}
				
				if (!receipt.equals("")) {
					
					switch (type) {
						case "region" :
							StrSearch += " AND branch_region_id =" + typeid;
							break;
						case "branch" :
							StrSearch += " AND preowned_branch_id =" + typeid;
							break;
						case "team" :
							StrSearch += " AND emp_id IN (SELECT preownedteamtrans_emp_id"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
									+ " WHERE preownedteamtrans_team_id IN (" + typeid + "))";
							break;
						case "emp" :
							StrSearch += " AND preowned_emp_id =" + typeid;
							break;
					}
					
					if (!branch_id.equals("")) {
						StrSearch = " AND receipt_branch_id IN (" + branch_id + ")";
					}
					
					if (!emp_id.equals("0")) {
						StrSearch += " AND receipt_emp_id = " + emp_id + "";
					}
					
					if (!exe_id.equals("")) {
						StrSearch += " AND receipt_emp_id IN (" + exe_id + ")";
					}
					
					if (!model_id.equals("")) {
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}
					
					if (!team_id.equals("")) {
						StrSearch += " AND emp_id IN ( "
								+ "SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					
					if (receipt.equals("receipt")) {
						StrSearch += receiptdate + " AND receipt_active = 1 ";
					}
				}
				// SOP("StrSearch    ------------" + StrSearch);
				
				if (!preowned.equals("") && !preowned.equals("stock") && !preowned.equals("eval")) {
					SetSession("preownedstrsql", StrSearch.replaceAll("emp_active", "preowned.emp_active"), request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-list.jsp?smart=yes"));
				} else if (!testdrive.equals("") && !testdrive.equals("kpi")) {
					SetSession("testdrivestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-testdrive-list.jsp?smart=yes"));
				} else if (testdrive.equals("kpi")) {
					SetSession("enquirystrsql", StrSearch.replaceAll("testdrive_emp_id", "emp_id"), request);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
				} else if (preowned.equals("preowned")) {
					SetSession("preownedstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-list.jsp?smart=yes"));
				} else if (preowned.equals("eval")) {
					SetSession("evalstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-eval-list.jsp?smart=yes"));
				} else if (preowned.equals("stock")) {
					SetSession("preownedstockstrsql", StrSearch.replaceAll("emp_active", "stock.emp_active"), request);
					response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-stock-list.jsp?smart=yes"));
				}
				else if (quote.equals("quote") || quote.equals("kpiquote")) {
					SetSession("quotestrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-quote-list.jsp?smart=yes"));
				} else if (!salesorder.equals("")) {
					SetSession("sostrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
				} else if (!receipt.equals("")) {
					SetSession("receiptstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../accounting/voucher-list.jsp?voucherclass_id=9&vouchertype_id=9&smart=yes"));
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
