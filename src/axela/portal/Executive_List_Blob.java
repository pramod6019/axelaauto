// Ved Prakash (12 Feb 2013), 
// edited- caching issue -4 april 2013(smitha nag)
// Modify & Formatting - Ved Prakash (28th August 2013)
package axela.portal;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Executive_List_Blob extends Connect {

	// public String LinkHeader = "";
	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=../portal/executives.jsp>Executives</a>"
			+ " &gt; <a href=\"../portal/executive-list.jsp?all=yes\">List Executives</a>:";
	// public String LinkListPage = "executive-list.jsp";
	public String LinkAddPage = "<a href=executives-update.jsp?add=yes>Add New Executive...</a>";
	public String LinkExportPage = "executive-export.jsp?smart=yes&target=" + Math.random() + "";
	public String ExportPerm = "";
	public String all = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String smart = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_idsession = "0";
	public String drop_search;
	public String txt_search = "";
	public String active = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Executive ID", "numeric", "emp_id"},
			{"Name", "text", "emp_name"},
			{"User Name", "text", "emp_uname"},
			{"Role", "text", "role_name"},
			{"Department", "text", "department_name"},
			{"Reference No.", "text", "emp_ref_no"},
			{"Job Title", "text", "jobtitle_desc"},
			{"Sex", "boolean", "emp_sex"},
			{"DOB", "date", "emp_dob"},
			{"Marital Status", "boolean", "emp_married"},
			{"Qualification", "text", "emp_qualification"},
			{"Certification", "text", "emp_certification"},
			{"Phone1", "text", "emp_phone1"},
			{"Phone2", "text", "emp_phone2"},
			{"Mobile1", "text", "emp_mobile1"},
			{"Mobile2", "text", "emp_mobile2"},
			{"Email1", "text", "emp_email1"},
			{"Email2", "text", "emp_email2"},
			{"Full Address", "text", "CONCAT(emp_address, ', ', emp_city,' - ', emp_pin, ', ',emp_state)"},
			{"City", "text", "emp_city"},
			{"Pin/Zip", "numeric", "emp_pin"},
			{"State", "text", "emp_state"},
			{"Landmark", "text", "emp_landmark"},
			{"Active", "boolean", "emp_active"},
			{"Branch ID", "numeric", "emp_branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Branch Type", "text", "branchtype_name"},
			{"Region Name", "text", "region_name"},
			{"Brand Name", "text", "brand_name"},
			{"Executive", "boolean", "emp_all_exe"},
			{"Sales", "boolean", "emp_sales"},
			{"Monitoring Board", "boolean", "emp_mtrboard"},
			{"Quote Price Update", "boolean", "emp_quote_priceupdate"},
			{"Quote Discount Update", "boolean", "emp_quote_discountupdate"},
			{"SO Price Update", "boolean", "emp_so_priceupdate"},
			{"SO Discount Update", "boolean", "emp_so_discountupdate"},
			{"Invoice Price Update", "boolean", "emp_invoice_priceupdate"},
			{"Invoice Discount Update", "boolean", "emp_invoice_discountupdate"},
			{"Service", "boolean", "emp_service"},
			{"MIS", "boolean", "emp_mis_access"},
			{"Reports Access", "boolean", "emp_report_access"},
			{"Export Reports", "boolean", "emp_export_access"},
			{"IP Access", "text", "emp_ip_access"},
			{"Prbranch ID", "numeric", "emp_prbranch_id"},
			{"Structure ID", "numeric", "emp_structure_id"},
			{"Sal Calc From", "date", "emp_sal_calc_from"},
			{"Esi No", "text", "emp_esi_no"},
			{"Dispensary ID", "numeric", "emp_dispensary_id"},
			{"PF NO", "numeric", "emp_pf_no"},
			{"Pf No Dept File", "numeric", "emp_pf_no_dept_file"},
			{"Date of Join", "date", "emp_date_of_join"},
			{"Date of Relieve", "date", "emp_date_of_relieve"},
			{"Reason of Leaving", "text", "emp_reason_of_leaving"},
			{"Notes", "text", "emp_notes"},
			{"Entry By", "text", "emp_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "emp_entry_date"},
			{"Modified By", "text", "emp_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "emp_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = "1000";
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND emp_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Executives!";
					StrSearch += " AND emp_id > 0";
				} else if (!(emp_id.equals("0"))) {
					msg += "<br>Result for Executive = " + emp_id + "!";
					StrSearch += " AND emp_id = " + emp_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND emp_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("exestrsql", request).equals("")) {
						StrSearch += GetSession("exestrsql", request);
					}
				}
				StrHTML = Listdata();
				// session.setAttribute("StrSql", StrSql);
				SetSession("ExportStrSearch", StrSearch, request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String Img = "", address;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT emp_id, emp_name, emp_ref_no, "
					+ " OCTET_LENGTH(COALESCE (emp_photo, '0')) AS emp_photo ";

			StrJoin = " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT(emp_id))";

			StrSql += StrJoin + StrSearch;
			CountSql += StrJoin + StrSearch;
			// SOP("StrSql=====" + StrSql);
			// SOP("StrSearch=====" + StrSearch);
			// SOP("Executive list---------//-------" + StrSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Executive(s)";

				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "executive-list-blob.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);

				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				if (all.equals("yes")) {
					StrSql += " ORDER BY emp_id DESC";
				} else {
					StrSql += " ORDER BY emp_name";
				}
				StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				// SOP(StrSqlBreaker(StrSql));
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					if (crs.isBeforeFirst()) {
						Str.append("<div class=\"table-bordered table-hover\">\n");
						Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
						Str.append("<thead><tr>\n");
						// count
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\">ID</th>\n");
						// name
						Str.append("<th >Name</th>\n");
						// photo
						Str.append("<th data-hide=\"phone\">Photo</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							Date d = new Date();
							if (crs.getString("emp_photo").equals("0")) {
								Img = "";
							} else {
								Img = "<img src=../Thumbnailblob.do?image_type=emp&emp_id=" + crs.getString("emp_id")
										+ "&width=100&time=" + d.getTime() + "&target=" + Math.random()
										+ "&dummy=84456663 alt=" + crs.getString("emp_name") + "><br>";
							}
							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td valign=\"top\" align=\"center\" bgcolor='").append("'>").append(count).append("</td>\n");
							// id
							Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_id")).append("</td>\n");
							// emp_name
							Str.append("<td align=\"left\" >").append(crs.getString("emp_name")).append("</td>");
							// image
							Str.append("<td align=\"center\">").append(Img);
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Executive(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
