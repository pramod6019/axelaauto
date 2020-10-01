package axela.mktg;
//Saiman 11th Feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Lead_List1 extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../mktg/index.jsp>Marketing</a> &gt; <a href=lead.jsp>Leads</a> &gt; <a href=lead-list.jsp?all=recent>List Leads</a>:";
	public String LinkExportPage = "../mktg/lead-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href='../mktg/lead-import.jsp'>Import Leads</a><br><a href=lead-update.jsp?add=yes>Add New Lead...</a>";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "", oppr_id = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String lead_id = "0";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String ExportPerm = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Lead ID", "numeric", "lead_id"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Title", "text", "title_desc"},
			{"First Name", "text", "lead_fname"},
			{"Last Name", "text", "lead_lname"},
			{"Job Title", "text", "lead_jobtitle"},
			{"Date", "date", "lead_date"},
			{"Email", "text", "lead_email"},
			{"Website", "text", "lead_website"},
			{"Mobile", "text", "lead_mobile"},
			{"Phone", "text", "lead_phone"},
			{"Company", "text", "lead_company"},
			{"Employee Count", "text", "empcount_desc"},
			{"Requirement", "text", "lead_req"},
			{"Ref. No.", "text", "lead_refno"},
			{"SOE Name", "text", "soe_name"},
			{"SOB Name", "text", "sob_name"},
			{"Executive", "text", "concat(emp_name,emp_ref_no)"},
			{"Active", "boolean", "lead_active"},
			{"Entry By", "text", "lead_entry_id in (Select emp_id from axela_emp where emp_name"},
			{"Entry Date", "date", "lead_entry_date"},
			{"Modified By", "text", "lead_modified_id in (Select emp_id from axela_emp where emp_name"},
			{"Modified Date", "date", "lead_modified_date"},
			{"Website Enquiry Date", "date", "lead_website_date"},
			{"Website IP", "text", "lead_website_ip"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			recperpage = Integer.parseInt(session.getAttribute("emp_recperpage") + "");
			emp_id = session.getAttribute("emp_id").toString();
			BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
			ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_lead_access", request, response);
			ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
			PopulateConfigDetails();
			msg = PadQuotes(request.getParameter("msg"));
			all = PadQuotes(request.getParameter("all"));
			smart = PadQuotes(request.getParameter("smart"));
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
			advSearch = PadQuotes(request.getParameter("advsearch_button"));
			if ("yes".equals(all)) // For all Leads to b displayed
			{
				msg = "Results for all Leads!";
				StrSearch = StrSearch + " and lead_id > 0";
			} else if (all.equals("recent")) {
				msg = "Recent Leads!";
				StrSearch = StrSearch + " and lead_id > 0";
				// StrSearch = StrSearch + " LIMIT " + recperpage + "";
			} else if (!lead_id.equals("0")) {
				msg = msg + "<br>Results for Lead ID = " + lead_id + "!";
				StrSearch = StrSearch + " and lead_id = " + lead_id + "";
			} else if (advSearch.equals("Search")) // for keyword search
			{
				StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				if (StrSearch.equals("")) {
					msg = "Enter search text!";
					StrSearch = StrSearch + " and lead_id = 0";
				} else {
					msg = "Results for Search!";
				}
			} else if (smart.equals("yes")) {
				msg = msg + "<br>Results of Search!";
				if (session.getAttribute("leadstrsql") != null) {
					StrSearch = session.getAttribute("leadstrsql").toString();
				}
			}
			if (!StrSearch.equals("")) {
				session.setAttribute("leadstrsql", StrSearch);
			}
			StrHTML = Listdata();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		CachedRowSet crs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending on search

				StrSql = " SELECT lead_id, lead_branch_id, lead_fname, lead_lname, title_desc,"
						+ " lead_date, lead_company, lead_empcount_id, lead_req, lead_soe_id, lead_mobile, lead_email,"
						+ " lead_sob_id, lead_emp_id, lead_entry_id, lead_entry_date,"
						+ " lead_modified_id, lead_modified_date, branch_id, empcount_desc,"
						+ " branch_code, concat(branch_name,' (', branch_code, ')') as branchname,"
						+ " coalesce(soe_name,'') as soe_name, coalesce(lead_website_date,'') as lead_website_date,"
						+ " coalesce(sob_name,'') as sob_name, coalesce(lead_website_ip,'') as lead_website_ip,"
						+ " emp_id, Concat(emp_name,' (',emp_ref_no,')') as emp_name,"
						+ " COALESCE(oppr_id,0) as oppr_id ";

				CountSql = " SELECT Count(distinct(lead_id)) ";

				SqlJoin = " from axela_mktg_lead"
						+ " inner join axela_branch on branch_id = lead_branch_id"
						+ " inner join axela_title on title_id=lead_title_id"
						+ " inner join axela_empcount on empcount_id=lead_empcount_id"
						+ " inner join axela_emp on emp_id = lead_emp_id"
						+ " left join axela_sales_oppr on oppr_lead_id = lead_id"
						+ " left join axela_soe on soe_id=lead_soe_id"
						+ " left join axela_sob on sob_id=lead_sob_id"
						+ " where 1=1 " + BranchAccess + ExeAccess + "";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch + " group by lead_id order by lead_id desc ";
					CountSql = CountSql + StrSearch;
				}
				if (all.equals("recent")) {
					StrSql = StrSql + " LIMIT " + recperpage + "";
					crs = processQuery(StrSql);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Lead(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "lead-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql);
					}
					// SOP("list query===" + StrSqlBreaker(StrSql));
					int count = StartRec - 1;
					Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=center>\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Date</th>\n");
					Str.append("<th>Contact</th>\n");
					Str.append("<th>Company</th>\n");
					Str.append("<th>Requirement</th>\n");
					if (config_sales_soe.equals("1")) {
						Str.append("<th>SOE</th>\n");
					}
					if (config_sales_sob.equals("1")) {
						Str.append("<th>SOB</th>\n");
					}
					Str.append("<th>Executive</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						oppr_id = crs.getString("oppr_id");
						count = count + 1;

						Str.append("<tr>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>");
						Str.append(crs.getString("lead_id")).append("");
						Str.append("</td>");
						Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("lead_date"))).append("</td>");
						Str.append("<td valign=top align=left nowrap>").append(crs.getString("title_desc")).append(" ").append(crs.getString("lead_fname")).append(" ")
								.append(crs.getString("lead_lname")).append("");
						if (!crs.getString("lead_mobile").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("lead_mobile"), 5, "M"));
						}
						if (!crs.getString("lead_email").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("lead_email")).append(">").append(crs.getString("lead_email")).append("</a>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("lead_company")).append(" (").append(crs.getString("empcount_desc")).append(")</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("lead_req")).append("</td>\n");
						if (config_sales_soe.equals("1")) {
							Str.append("<td valign=top align=left nowrap>").append(crs.getString("soe_name")).append("</td>");
						}
						if (config_sales_sob.equals("1")) {
							Str.append("<td valign=top align=left>").append(crs.getString("sob_name")).append("</td>\n");
						}
						Str.append("<td valign=top align=left >");
						Str.append(" <a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
						Str.append("<td valign=top align=left nowrap><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">")
								.append(crs.getString("branchname")).append("</a></td>");
						Str.append("<td valign=top align=left nowrap>  " + " <a href=\"../mktg/lead-update.jsp?update=yes&lead_id=").append(crs.getString("lead_id")).append(" \">Update Lead</a><br>");

						if (oppr_id.equals("0")) {
							Str.append("<a href=\"oppr-quickadd.jsp?lead_id=").append(crs.getString("lead_id")).append("\">Convert to Opportunity</a>");
						} else {
							Str.append(" <a href=\"oppr-list.jsp?&oppr_id=").append(oppr_id).append("&msg=Opportunity already present \">List Opportunity</a>");
						}
						Str.append("<br><br></td></tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Lead(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob"
				+ " from axela_config,axela_emp "
				+ " where 1=1 and emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql);
		try {
			while (crs.next()) {
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				// ExportPerm = crs.getString("emp_export_access");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
