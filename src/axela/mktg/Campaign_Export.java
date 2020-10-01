package axela.mktg;
//Sangita 26th june 2013

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Campaign_Export extends Connect {

	public String emp_id = "";
	public String printoption = "";
	public String exporttype = "";
	public String comp_id = "0";
	public String exportB = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "campaign-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = (session.getAttribute("emp_id")).toString();
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			exportcount = ExecuteQuery("select comp_export_count from axela_comp");
			printoption = PadQuotes(request.getParameter("report"));
			exporttype = PadQuotes(request.getParameter("exporttype"));
			exportB = PadQuotes(request.getParameter("btn_export"));
			if (session.getAttribute("campaignstrsql") != null) {
				StrSearch = session.getAttribute("campaignstrsql").toString();
				SOP("StrSearch===" + StrSearch);
			}
			if (exportB.equals("Export")) {
				LeadDetails(request, response);
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void LeadDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select campaign_id as ID,"
					+ " concat(branch_name,' (', branch_code, ')') as Branch,"
					+ " campaign_subject as 'Subject',"
					+ " DATE_FORMAT(campaign_sentdate, '%d/%m/%Y') as 'Sent Date',"
					+ " coalesce(count(email_campaign_id),'') as 'Email Count'"
					+ " from axela_mktg_campaign"
					+ " inner join axela_branch on branch_id = campaign_branch_id"
					+ " inner join axela_email on email_campaign_id = campaign_id"
					+ " where 1=1 " + StrSearch + ""
					+ " group by campaign_id order by campaign_id desc"
					+ " limit " + exportcount;
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A3", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = CampaignDetails" + StrSelectdrop("CampaignDetails", printoption) + ">Campaign Details</option>\n";
		return print;
	}
}
