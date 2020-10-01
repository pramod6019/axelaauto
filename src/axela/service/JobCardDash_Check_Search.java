package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class JobCardDash_Check_Search extends Connect {

	public String StrSearch = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String starttime = "";
	public String endtime = "";
	public String stage_id = "";
	public String priority_id = "";
	public String type_id = "";
	public String cat_id = "";
	public String jctoday = "", jcopentypetoday = "", jcready = "", jcdelivered = "", jcoverduestage = "", jcoverduepriority = "";
	public String jcopen = "", jcpriority = "", jcopentype = "", jcopencat = "", jcopenbodyshop = "";
	public String day = "", week = "", month = "", qtr = "";
	public String branch_id = "", technician = "", advisor = "";
	public String ExeAccess = "";
	public String BranchAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				starttime = PadQuotes(request.getParameter("starttime"));
				endtime = PadQuotes(request.getParameter("endtime"));
				jctoday = PadQuotes(request.getParameter("jctoday"));
				jcopentypetoday = PadQuotes(request.getParameter("jcopentypetoday"));
				jcready = PadQuotes(request.getParameter("jcready"));
				jcdelivered = PadQuotes(request.getParameter("jcdelivered"));
				jcoverduestage = PadQuotes(request.getParameter("jcoverduestage"));
				jcoverduepriority = PadQuotes(request.getParameter("jcoverduepriority"));
				jcopen = PadQuotes(request.getParameter("jcopen"));
				jcpriority = PadQuotes(request.getParameter("jcpriority"));
				jcopentype = PadQuotes(request.getParameter("jcopentype"));
				jcopencat = PadQuotes(request.getParameter("jcopencat"));
				jcopenbodyshop = PadQuotes(request.getParameter("jcopenbodyshop"));
				day = PadQuotes(request.getParameter("day"));
				week = PadQuotes(request.getParameter("week"));
				month = PadQuotes(request.getParameter("month"));
				qtr = PadQuotes(request.getParameter("qtr"));
				stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
				priority_id = CNumeric(PadQuotes(request.getParameter("priority_id")));
				type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				technician = (PadQuotes(request.getParameter("technician")));
				advisor = (PadQuotes(request.getParameter("advisor")));

				StrSearch = BranchAccess.replace("branch_id", "jc_branch_id") + ExeAccess.replace("emp_id", "jc_emp_id");

				if (!branch_id.equals("0")) {
					StrSearch = StrSearch + " and jc_branch_id =" + branch_id;
				}
				if (!technician.equals("")) {
					StrSearch = StrSearch + " and jc_emp_id in (" + technician + ")";
				}
				if (!advisor.equals("")) {
					StrSearch = StrSearch + " and jc_emp_id in (" + advisor + ")";
				}

				if (!jctoday.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and substring(jc_time_in,1,8)= '" + starttime.substring(0, 8) + "'";
					}
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
				}

				if (!jcopentypetoday.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and substring(jc_time_in,1,8)= '" + starttime.substring(0, 8) + "'";
					}
					if (!type_id.equals("0")) {
						StrSearch += " and jc_jctype_id =" + type_id;
					}
				}

				if (!jcready.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and substring(jc_time_ready,1,8)= '" + starttime.substring(0, 8) + "'";
					}
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
				}

				if (!jcdelivered.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and substring(jc_time_out,1,8)= '" + starttime.substring(0, 8) + "'";
					}
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
				}

				if (!jcoverduestage.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and jc_time_ready='' AND substring(jc_time_promised,1,8) <= '" + starttime.substring(0, 8) + "'";
					}
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
				}

				if (!jcoverduepriority.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and jc_time_ready='' AND substring(jc_time_promised,1,8) <= '" + starttime.substring(0, 8) + "'";
					}
					if (!priority_id.equals("0")) {
						StrSearch += " and jc_priorityjc_id =" + priority_id;
					}
				}

				if (!jcopentype.equals("")) {
					if (!type_id.equals("0")) {
						StrSearch += " and jc_time_out='' and jc_jctype_id =" + type_id;
					} else {
						StrSearch += " and jc_time_out=''";
					}
				}

				if (!jcopencat.equals("")) {
					StrSearch += " and jc_time_out=''";
					if (!cat_id.equals("0")) {
						StrSearch += "and jc_jccat_id =" + cat_id;
					}
				}

				if (!jcopenbodyshop.equals("")) {
					StrSearch += " and jc_time_out='' and jc_jctype_id=3";
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
				}

				if (!jcopen.equals("")) {
					StrSearch += " and jc_time_out='' and jc_jctype_id!=3";
					if (!stage_id.equals("0")) {
						StrSearch += " and jc_jcstage_id =" + stage_id;
					}
					// else {
					// StrSearch += " and jc_time_ready=''";
					//
					// }
				}

				if (!jcpriority.equals("")) {
					if (!starttime.equals("")) {
						StrSearch += " and jc_time_ready=''";
					}
					if (!priority_id.equals("0")) {
						StrSearch += " and priorityjc_id =" + priority_id;
					}
				}
				if (day.equals("day")) {
					if (!starttime.equals("")) {
						StrSearch += " and substring(jc_time_in,1,8)= '" + starttime + "'";
					}
				}
				if (week.equals("week")) {
					if (!starttime.equals("") && !endtime.equals("")) {
						StrSearch += " and substring(jc_time_in,1,8)>='" + endtime + "' and substring(jc_time_in,1,8)<'" + starttime + "'";
					}
				}

				if (month.equals("month")) {
					if (!starttime.equals("") && !endtime.equals("")) {
						StrSearch += " and substring(jc_time_in,1,8)>='" + endtime + "' and substring(jc_time_in,1,8)<'" + starttime + "'";
					}
				}
				if (qtr.equals("qtr")) {
					if (!starttime.equals("") && !endtime.equals("")) {
						StrSearch += " and (substring(jc_time_in,1,4)='" + starttime + "' and substring(jc_time_in,5,2) " + endtime + ")";
					}
				}
				StrSearch += " and jc_active=1";
				// SOP("StrSearch = " + StrSearch);

				if (!StrSearch.equals("")) {
					SetSession("jcstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				}

				// if (jctoday.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcready.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcoverduestage.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcoverduepriority.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcopen.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcpriority.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcopentype.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (jcopencat.equals("yes")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (!day.equals("")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (!week.equals("")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (!month.equals("")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// } else if (!qtr.equals("")) {
				// SetSession("jcstrsql", StrSearch, request);
				// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
				// }
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
