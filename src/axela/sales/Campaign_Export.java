package axela.sales;
//smitha nag 25 june 2013 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Campaign_Export extends Connect {

	public String emp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String comp_id = "0";
	public String BranchAccess = "", ExeAccess = "";
	public String exportpage = "campaign-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = GetSession("emp_id", request).toString();
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));

				if (!GetSession("campaignstrsql", request).equals("")) {
					StrSearch = GetSession("campaignstrsql", request);
					StrSearch += BranchAccess + ExeAccess;
					// SOP("StrSearch----cexport----" + StrSearch);
				}
				if (exportB.equals("Export")) {
					CampaignDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CampaignDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT campaign_id AS 'ID',"
					+ " COALESCE(campaign_name,'') AS 'Name',"
					+ " COALESCE(campaign_desc,'') AS Descripition,"
					+ " COALESCE(camptype_desc,'') AS 'Type',"
					+ " COALESCE(DATE_FORMAT(campaign_startdate, '%d/%m/%Y %h:%i'),'') AS 'Start Time',"
					+ " COALESCE(DATE_FORMAT(campaign_enddate, '%d/%m/%Y %h:%i'),'') AS 'End Time',"
					+ " COALESCE(campaign_budget,'') AS 'Budget',"
					+ " COALESCE(GROUP_CONCAT(branch_name), '') AS 'Branch Name',"
					+ " @enquirycount := COALESCE((SELECT count(enquiry_id) FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_campaign_id=campaign_id), 0) AS 'Opportunity Count',"
					+ " COALESCE(round(campaign_budget/@enquirycount,2),0) AS 'Cost per Opportunity',"
					+ " @socount := COALESCE((SELECT count(so_id) FROM " + compdb(comp_id) + "axela_sales_so"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " where enquiry_campaign_id=campaign_id), 0) AS 'So Count',"
					+ " COALESCE(round(campaign_budget/@socount,2),0) AS 'Cost per Conversation',"
					+ " IF(campaign_active=1,'yes','no') AS 'Active',"
					+ " COALESCE(campaign_notes,'') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_type ON camptype_id = campaign_campaigntype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_campaign_id = campaign_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = camptrans_branch_id"
					+ " WHERE 1 = 1 " + StrSearch
					+ " AND campaign_id > 0"
					+ " GROUP BY campaign_id"
					+ " ORDER BY campaign_id desc"
					+ " limit " + exportcount;

			// SOP("StrSql------campaign export----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A2", comp_id);
				// PrintToPDF exportToPDF = new PrintToPDF();
				// exportToPDF.Export(request, response, rs, printoption, "A2");
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
