//smitha nag 6 july 2013
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String displayprint = "";
	public String PrintPerm = "";
	public String RefreshForm = "";
	public String location = "";
	public String ListLink = "<a href=preowned-testdrive-list.jsp?smart=yes>Click here to List Test Drive</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_preowned_testdrive_access", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request, response);
				smart = PadQuotes(request.getParameter("smart"));
				PrintPerm = ReturnPerm(comp_id, "emp_print_access", request);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(testdrive_time_from,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(testdrive_time_from,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and enquiry_branch_id =" + dr_branch_id;
					}
					StrSearch = StrSearch + " and branch_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("testdrivestrsql", StrSearch, request);
						StrHTML = TestDriveSummary(request);
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String TestDriveSummary(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		String StrSql = "";
		int testdrive_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname, "
					+ " count(testdrive_id) as testdrivecount "
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id  "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " where 1=1  "
					+ StrSearch + BranchAccess + ExeAccess.replace("emp_id", "testdrive_emp_id")
					+ " group by branch_id order by branchname";
			// SOP("strsql in TestDriveSummary---" + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Test Drive Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr align=center>\n");
				Str.append("<th data-toggle=\"true\">Branch Name</th>\n");
				Str.append("<th width=20% data-hide=\"phone, tablet\">Test Drive Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					// SOP(".........Test Drive_count is.......---" + crs.getInt("testdrivecount"));
					testdrive_count = testdrive_count + crs.getInt("testdrivecount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=");
					Str.append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("testdrivecount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left><b>Total:  &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right ><b>").append(testdrive_count).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tr></table></div></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// commented stuffs are required in library wen export is activated.. dnt delete...
	// public String PopulatePrintOption()
	// {
	// if(printoption.equals("")) printoption="Test DriveDetails1";
	// String print = "";
	// print= print + "<option value = gatePass"+ StrSelectdrop("gatepass", printoption) +">Gate Pass</option>\n";
	// print= print + "<option value = testdriveDetails1"+ StrSelectdrop("testdrivedetails1", printoption) +">Test Drive Details</option>\n";
	// return print;
	// }
	//
	// public String PopulateExport()
	// {
	// if(exporttype.equals("")) exporttype="pdf";
	// String export = "";
	// export= export + "<option value = pdf"+ StrSelectdrop("pdf", exporttype) +">Acrobat Format (PDF)</option>\n";
	// export= export + "<option value = html"+ StrSelectdrop("html", exporttype) +">HTML Format</option>\n";
	// export= export + "<option value = xls"+ StrSelectdrop("xls", exporttype) +">MS Excel Format</option>\n";
	// export= export + "<option value = rtf"+ StrSelectdrop("rtf", exporttype) +">MS Word Format</option>\n";
	//
	// return export;
	// }
	// public String RefreshForm()
	// {
	// if(displayprint.equals("yes"))
	// RefreshForm = "onload=\"remote=window.open('" + location + "','contactsexport','');remote.focus();\"";
	// return RefreshForm;
	// }
}
