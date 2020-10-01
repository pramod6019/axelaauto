package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_History extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String exe_id = "";
	public String branch_id = "0";
	public String comp_id = "0";
	public String historydate = "";
	public String history_date = "";
	public String enquiry_emp_id = "";
	public String enquiry_id = "";
	public String enquiry_dmsno = "";
	public String msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));

				GetValues(request, response);
				CheckForm();
				if (go.equals("Search")) {
					if (!history_date.equals("")) {
						StrSearch = StrSearch + " and substr(history_datetime,1,8) = substr('" + ConvertShortDateToStr(history_date) + "',1,8) ";
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " and enquiry_emp_id = " + exe_id + "";
					}
					if (!enquiry_id.equals("")) {
						StrSearch = StrSearch + " and enquiry_id = " + enquiry_id + "";
					}
					if (!enquiry_dmsno.equals("")) {
						StrSearch = StrSearch + " and enquiry_dmsno = '" + enquiry_dmsno + "'";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = EnquiryHistory();
					}
					// SOP("StrSearch---"+StrSearch);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		history_date = PadQuotes(request.getParameter("txt_history_date"));
		if (history_date.equals("")) {
			history_date = strToShortDate(ToLongDate(kknow()));
		}
		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		// SOP("exe_id=="+exe_id);
		enquiry_id = PadQuotes(request.getParameter("txt_enquiry_id"));
		enquiry_dmsno = PadQuotes(request.getParameter("txt_enquiry_dmsno"));
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (history_date.equals("") && exe_id.equals("0") && enquiry_id.equals("")) {
			msg = msg + "<br>Select Date!";
		}
		if (!history_date.equals("")) {
			if (!isValidDateFormatShort(history_date)) {
				msg = msg + "<br>InValid Date!";
			}
		}
	}

	public String EnquiryHistory() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " select * from "
					+ " ((SELECT enquiry_id, enquiry_dmsno, history_datetime, "
					+ " history_actiontype, history_oldvalue, history_newvalue, "
					+ " emp_id, concat(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_history on history_enquiry_id = enquiry_id"
					+ " where 1=1 and enquiry_branch_id = " + dr_branch_id
					+ StrSearch + BranchAccess.replace("branch_id", "enquiry_branch_id") + ExeAccess + ")"
					+ " UNION "
					+ " (SELECT enquiry_id, enquiry_dmsno, followup_followup_time,  followuptype_name, '', followup_desc, "
					+ " emp_id, concat(emp_name,' (',emp_ref_no,')') as emp_name  "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry   "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id  "
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on followup_enquiry_id = enquiry_id  "
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup_type on followuptype_id = followup_followuptype_id "
					+ " where 1=1 and enquiry_branch_id = " + dr_branch_id
					+ StrSearch.replace("history_datetime", "followup_followup_time") + BranchAccess.replace("branch_id", "enquiry_branch_id") + ExeAccess
					+ " )) as t order by enquiry_id desc, history_datetime desc"
					+ " limit 1000";
			// SOP("SqlStr=========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				// Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Enquiry ID</th>\n");
				Str.append("<th data-toggle=\"true\">DMS No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">New Value</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Old Value</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr align=center>\n");
					Str.append("<td align=center><a href=enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append(">").append(crs.getString("enquiry_id")).append("</a></td>\n");
					Str.append("<td align=left>").append(crs.getString("enquiry_dmsno")).append("</td>\n");
					Str.append("<td align=center>").append(strToLongDate(crs.getString("history_datetime"))).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("history_actiontype")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("history_newvalue")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("history_oldvalue")).append("</td>\n");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("</tr>");
				}

			} else {
				Str.append("<td><br><br><br><br><b><center><font color=red>No History found!</font></b></center><br><br></td>\n");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 and emp_sales = '1'"
					+ " and (emp_branch_id = " + dr_branch_id + ""
					+ " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + ""
					+ " group by emp_id"
					+ " order by emp_name";
			// SOP("SqlStr====sales exe=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=form-control >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
