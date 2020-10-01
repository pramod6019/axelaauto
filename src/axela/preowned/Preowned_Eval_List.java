package axela.preowned;
//sangita 10th July 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Preowned_Eval_List extends Connect {

	public String LinkHeader = "";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String PageURL = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public String eval_id = "0";
	public String preowned_id = "0";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Evaluation ID", "numeric", "eval_id"},
			{"Pre Owned ID", "numeric", "eval_preowned_id"},
			{"Pre Owned No.", "numeric", "preowned_no"},
			{"Branch ID", "numeric", "preowned_branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "contact_name"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Model", "text", "preowned_preownedmodel_id in (select preownedmodel_id from axela_preowned_model where preownedmodel_name"},
			{"Variant", "text", "variant_name"},
			{"Sub Variant", "text", "preowned_sub_variant"},
			{"Exterior", "numeric", "extcolour_name"},
			{"Interior", "numeric", "intcolour_name"},
			{"Options", "text", "preowned_options"},
			{"Manuf. Year", "text", "preowned_manufyear"},
			{"Regd. Year", "text", "preowned_regdyear"},
			{"Reg. No.", "text", "preowned_regno"},
			{"Kms", "numeric", "preowned_kms"},
			{"Foreclosure Amt", "numeric", "preowned_fcamt"},
			{"Insurance Date", "date", "preowned_insur_date"},
			{"Insurance Type", "text", "insurance_name"},
			{"Ownership", "text", "ownership_name"},
			{"Invoice Value", "numeric", "preowned_invoicevalue"},
			{"Expected Price", "numeric", "preowned_expectedprice"},
			{"Offered Price", "numeric", "eval_offered_price"},
			{"Quoted Price", "numeric", "preowned_quotedprice"},
			{"Status Name", "text", "preownedstatus_name"},
			{"Executive", "text", "concat(emp_name,emp_ref_no)"},
			{"Notes", "text", "eval_notes"},
			{"Entry By", "text", "eval_entry_id in (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "eval_entry_date"},
			{"Modified By", "text", "eval_modified_id in (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "eval_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../preowned/index.jsp>" + ReturnPreOwnedName(request)
					+ "</a> &gt; <a href=../preowned/preowned-eval.jsp>Evaluation</a> &gt; <a href=../preowned/preowned-eval-list.jsp?all=yes>List Evaluations</a>:";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_eval_access", request, response);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access",
				// request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				eval_id = CNumeric(PadQuotes(request.getParameter("eval_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND eval_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Evaluation!";
					// StrSearch = StrSearch + " AND eval_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Evaluation!";
					StrSearch = StrSearch + " AND eval_id > 0";
				} else if (!(eval_id.equals("0"))) {
					msg += "<br>Results for Evaluation ID = " + eval_id;
					StrSearch = StrSearch + " AND eval_id = " + eval_id + "";
				} else if (!(preowned_id.equals("0"))) {
					msg += "<br>Results for Pre Owned ID = " + preowned_id;
					StrSearch = StrSearch + " AND preowned_id = " + preowned_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND eval_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("evalstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("evalstrsql", request);

					}
				}
				StrSearch += BranchAccess.replace("branch_id", "preowned_branch_id")
						+ ExeAccess.replace("emp_id", "eval_emp_id");
				SetSession("evalstrsql", StrSearch, request);
				// }
				StrHTML = Listdata();
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

	public String Listdata() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		StringBuilder customer_info = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT eval_id, preownedmodel_id, preownedmodel_name, preowned_no, variant_id,"
						+ " variant_name, eval_date, preowned_id, preowned_title, emp_id,"
						+ " COALESCE(eval_offered_price, '') AS eval_offered_price, "
						// / +
						// " coalesce(preowned_preownedmodel_id, 0) as preowned_preownedmodel_id, "
						+ " preowned_sub_variant,"
						+ " COALESCE(fueltype_id,0) AS fueltype_id,"
						+ " COALESCE(fueltype_name,'') AS fueltype_name,"
						+ " extcolour_id, extcolour_name, intcolour_id, intcolour_name,"
						+ " preowned_options, preowned_manufyear, preowned_regdyear, preowned_regno, preowned_kms,"
						+ " preowned_fcamt, preowned_insur_date, insurance_id, insurance_name, ownership_id, ownership_name,"
						+ " preowned_invoicevalue, preowned_expectedprice, preowned_quotedprice,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
						+ " CONCAT(customer_name, ' (', customer_id, ')') AS Customer, customer_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2, preowned_branch_id,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name";

				CountSql = "SELECT COUNT(DISTINCT eval_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_eval"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
						+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype on fueltype_id = preowned_fueltype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership on ownership_id = preowned_ownership_id"

						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = eval_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_insurance on insurance_id = preowned_insurance_id"
						+ " LEFT JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"
						+ " LEFT JOIN axela_preowned_extcolour on extcolour_id = preowned_extcolour_id"
						+ " LEFT JOIN axela_preowned_intcolour on intcolour_id = preowned_intcolour_id"
						+ " WHERE 1 = 1 ";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!(StrSearch.equals(""))) {
					StrSql += StrSearch + " GROUP BY eval_id"
							+ " ORDER BY eval_id DESC";
				}
				// SOP("StrSql----" + StrSql);
				CountSql += StrSearch;

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Evaluation";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "preowned-eval-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned_eval\\b", "from " + compdb(comp_id) + "axela_preowned_eval"
								+ " inner join (select eval_id from " + compdb(comp_id) + "axela_preowned_eval "
								+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
								+ " where 1=1 " + StrSearch
								+ " group by eval_id order by eval_id desc"
								+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") as myresults using (eval_id)");

						StrSql = "select * from (" + StrSql + ") as datatable"
								+ " order by eval_id desc ";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}

					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-hover\">\n");
					Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">ID</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Pre Owned</th>\n");
					Str.append("<th style=\"width:200px;\" data-toggle=\"true\">Customer</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Model</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Variant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Evaluation Price</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("eval_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("eval_id") + ");'");
						Str.append(" style='height:200px'>\n");
						Str.append("<td align=center>").append(count).append("</td>\n");
						Str.append("</td>\n<td valign=top align=center>").append(crs.getString("eval_id"));
						Str.append("</td>\n<td valign=top align=left>").append("<a href=\"../preowned/preowned-list.jsp?preowned_id=");
						Str.append(crs.getInt("preowned_id")).append("\">").append(crs.getString("preowned_title")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td valign=top align=left>");
						// Customer Info
						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_email2"));
						}

						Str.append(CustomerContactDetailsPopup(crs.getString("customer_id"), crs.getString("customer"), customer_info.toString(), "customer"));

						customer_info.setLength(0);

						// Contact Info
						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email2"));
						}

						Str.append("<br/>" + CustomerContactDetailsPopup(crs.getString("contact_id"), crs.getString("contact_name"), customer_info.toString(), "contact"));

						customer_info.setLength(0);

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("eval_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("eval_id"), "M"));
						}

						Str.append("</td>");

						Str.append("<td valign=top align=left>").append("<a href=\"../preowned/managepreownedmodel.jsp?preownedmodel_id=");
						Str.append(crs.getInt("preownedmodel_id")).append("\">").append(crs.getString("preownedmodel_name")).append("</a></td>");
						Str.append("<td valign=top align=left>").append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=");
						Str.append(crs.getInt("variant_id")).append("\">").append(crs.getString("variant_name")).append("</a>");
						if (!crs.getString("preowned_sub_variant").equals("")) {
							Str.append("<br>").append(crs.getString("preowned_sub_variant"));
						}
						Str.append("</td>\n<td valign=top align=left>").append(strToShortDate(crs.getString("eval_date")));
						Str.append("</td>\n");
						Str.append("<td valign=top align=left nowrap>");
						if (!crs.getString("eval_offered_price").equals("")) {
							Str.append("Expected Price: ");
							Str.append(crs.getString("preowned_expectedprice")).append("/-");
							Str.append("<br>").append("Offered Price: ");
							Str.append(crs.getString("eval_offered_price")).append("/-");
						} else {
							Str.append("");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left>");
						Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), ""));
						// Str.append("<td valign=top align=left><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name"))
						// .append("</a>");
						Str.append("</td>\n<td valign=top align=left ><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("preowned_branch_id")).append("\">")
								.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n<td valign=top nowrap>");
						Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
								+ "<button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"preowned-eval-update.jsp?update=yes&eval_id=" + crs.getString("eval_id")
								+ "&preowned_id=" + crs.getString("preowned_id") + " \">Update Evaluation</a></li>"
								+ "<li role=presentation><a href=\"preowned-eval-print.jsp?eval_id=" + crs.getString("eval_id") + "&target="
								+ Math.random() + "\" target=_blank>Print Evaluation</a></li></ul></div></center></div>");
						Str.append("</td>\n");
					}
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
					crs.close();
					return Str.toString();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Evaluation found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
