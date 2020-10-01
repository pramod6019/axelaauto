package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Branch extends Connect {

	public String submitB = "";
	public static String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String smart = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String displayprint = "";
	public String location = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "0";
	public String ListLink = "";
	public String reportURL = "../branch_Report.do?target=" + Math.random();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_branch_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));

				if (exportB == null) {
					exportB = "";
				}
				if (exporttype == null) {
					exporttype = "";
				}
				if (smart == null) {
					smart = "";
				}
				if (!smart.equals("yes")) {
					if (msg.equals("")) {
						SetSession("centerstrsql", StrSearch, request);
						StrHTML = BranchSummary(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String BranchSummary(HttpServletRequest request) {
		String count = "";
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<td  colspan=2 bgcolor=#336699><b><font color=#FFFFFF  >Branch Status</font></b></td>\n");
			Str.append("</tr>");
			Str.append("<tr align=center>\n");
			Str.append("<td align=center width=50%>Total</td>\n");
			count = ExecuteQuery("select count(branch_id) from " + compdb(comp_id) + "axela_branch where 1=1 ");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("<tr align=center>\n");
			Str.append("<td align=center width=50%>Active</td>\n");
			count = ExecuteQuery("select count(branch_id) from " + compdb(comp_id) + "axela_branch where branch_active = '1'");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("<tr align=center>\n");
			Str.append("<td align=center>Inactive</td>\n");
			count = ExecuteQuery("select count(branch_id) from " + compdb(comp_id) + "axela_branch where branch_active = '0'");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrintOption() {
		if (printoption.equals("")) {
			printoption = "BranchDetails";
		}
		String print = "";
		print = print + "<option value = BranchDetails" + StrSelectdrop("BranchDetails", printoption) + ">Branch Details</option>\n";
		return print;
	}

	public String PopulateExport() {
		if (exporttype.equals("")) {
			exporttype = "pdf";
		}
		String export = "";
		export = export + "<option value = pdf" + StrSelectdrop("pdf", exporttype) + ">Acrobat Format (PDF)</option>\n";
		export = export + "<option value = html" + StrSelectdrop("html", exporttype) + ">HTML Format</option>\n";
		export = export + "<option value = xlsx" + StrSelectdrop("xlsx", exporttype) + ">MS Excel Format</option>\n";
		export = export + "<option value = rtf" + StrSelectdrop("rtf", exporttype) + ">MS Word Format</option>\n";

		return export;
	}

	public String RefreshForm() {
		if (displayprint.equals("yes")) {
			RefreshForm = "onload=\"remote=window.open('" + location + "','contactsexport','');remote.focus();\"";
		}
		return RefreshForm;
	}
}
