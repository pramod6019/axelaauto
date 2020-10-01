package axela.insurance;
// Dilip Kumar 09 APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Insurance_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../insurance/index.jsp\">Insurance</a>"
			// + " &gt; <a href=\"../service/insurance.jsp\">Insurance</a>"
			+ " &gt; <a href=\"../insurance/insurance-list.jsp?all=yes\">List Policy</a>:";
	public String LinkAddPage = "";
	public String LinkExportPage = "insurance-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkListPage = "../insurance/insurance-list.jsp?all=yes";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String insurpolicy_id = "0";
	public String insurenquiry_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Insurance ID", "numeric", "insurpolicy_id"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Insurance Enquiry ID", "numeric", "insurpolicy_insurenquiry_id"},
			{"Date", "date", "insurpolicy_date"},
			{"Start Date", "date", "insurpolicy_start_date"},
			{"End Date", "date", "insurpolicy_end_date"},
			{"Type", "text", "insurtype_name"},
			{"Policy Name", "text", "policytype_name"},
			{"Company", "text", "inscomp_name"},
			{"Policy No.", "text", "insurpolicy_policy_no"},
			{"Description", "text", "insurpolicy_desc"},
			{"Terms", "text", "insurpolicy_terms"},
			{"Executive", "text", "CONCAT(emp_name,emp_ref_no)"},
			{"Active", "boolean", "insurpolicy_active"},
			{"Notes", "text", "insurpolicy_notes"},
			{"Entry By", "text", "insurpolicy_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "insurpolicy_entry_date"},
			{"Modified By", "text", "insurpolicy_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "insurpolicy_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_policy_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "insurpolicy_branch_id");
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				insurpolicy_id = CNumeric(PadQuotes(request.getParameter("insurpolicy_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND insurpolicy_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Insurance!";
				} else if (!insurpolicy_id.equals("0")) {
					msg += "<br>Results for Insurance ID = " + insurpolicy_id + "!";
					StrSearch += " AND insurpolicy_id = " + insurpolicy_id + "";
				} else if (!insurenquiry_id.equals("0")) {
					msg += "<br>Results for Vehicle ID = " + insurenquiry_id + "!";
					StrSearch += " AND insurenquiry_id = " + insurenquiry_id + "";
				}

				else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("insurancestrsql", request).equals("")) {
						StrSearch += GetSession("insurancestrsql", request);
						// SOP("StrSearch===" + StrSearch);
					}
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND insurpolicy_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}

				StrSearch += BranchAccess;
				SetSession("insurancestrsql", StrSearch, request);
				StrHTML = ListData();
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

	public String ListData() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, insurpolicy_insurenquiry_id,"
						+ " COALESCE(insurenquiry_id, 0) AS insurenquiry_id,"
						+ " COALESCE(insurenquiry_reg_no, 0) AS insurenquiry_reg_no, insurpolicy_contact_id,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " insurpolicy_date,"
						+ " COALESCE(policytype_name, '') AS policytype_name,"
						+ " insurpolicy_policy_no, inscomp_name,"
						+ " insurpolicy_premium_amt, insurpolicy_idv_amt, insurpolicy_od_amt, insurpolicy_od_discount, insurpolicy_payout,"
						+ " insurtype_name, insurpolicy_customer_id, insurpolicy_start_date, insurpolicy_end_date, insurpolicy_entry_date, customer_name,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(preownedmodel_name, '') AS preownedmodel_name, insurpolicy_active,"
						+ " COALESCE(variant_name, '') AS variant_name, insurpolicy_emp_id,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";

				CountSql = "SELECT COUNT(DISTINCT insurpolicy_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_policy"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = insurpolicy_inscomp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurpolicy_insurenquiry_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch
							+ " GROUP BY insurpolicy_id"
							+ " ORDER BY insurpolicy_id DESC";
				}
				SOP("StrSql===" + StrSql);
				CountSql += StrSearch;
				// SOP("CountSql---------" + CountSql);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Insurance";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "insurance-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_insurance_policy\\b",
								"FROM " + compdb(comp_id) + "axela_insurance_policy"
										+ " INNER JOIN (SELECT insurpolicy_id FROM " + compdb(comp_id) + "axela_insurance_policy"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY insurpolicy_id"
										+ " ORDER BY insurpolicy_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (insurpolicy_id)");
						// StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
						// StrSql += " ORDER BY insurpolicy_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					// SOP("StrSql------insurance list-----" + StrSql);
					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th>Insurance Enquiry ID</th>\n");
					Str.append("<th>Insurance</th>\n");
					Str.append("<th style=\"width:200px;\" data-hide=\"phone\">Customer</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Type</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Term</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Amount</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Executive</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("insurpolicy_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("insurpolicy_id") + ");'");
						Str.append(" style='height:200px'>\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td nowrap>").append(crs.getString("insurpolicy_id")).append("</td>\n");
						Str.append("<td nowrap>");
						Str.append("<a href=\"../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=").append(crs.getString("insurpolicy_insurenquiry_id")).append("\">");
						Str.append(crs.getString("insurpolicy_insurenquiry_id")).append("</a>").append("</td>\n");
						Str.append("<td nowrap>Company: ").append(crs.getString("inscomp_name"));
						Str.append("<br>Policy: ").append(crs.getString("policytype_name"));
						Str.append("<br>Policy No.: ").append(crs.getString("insurpolicy_policy_no"));
						if (crs.getString("insurpolicy_active").equals("0")) {
							Str.append("<br><font color=\"red\">[Inactive]</font>");
						}
						Str.append("</td>\n<td nowrap>");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("insurpolicy_customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
						Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
						Str.append(crs.getString("contact_name")).append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("insurpolicy_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("insurpolicy_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email2")).append("\">");
							Str.append(crs.getString("contact_email2")).append("</a></span>");
						}
						Str.append("</td>\n<td>").append(crs.getString("insurtype_name")).append("</td>\n");

						if (!crs.getString("insurpolicy_entry_date").equals("")) {
							Str.append("<td>").append(strToShortDate(crs.getString("insurpolicy_entry_date"))).append("</td>\n");
						}

						Str.append("<td nowrap>");
						if (!crs.getString("insurpolicy_start_date").equals("")) {
							Str.append(strToShortDate(crs.getString("insurpolicy_start_date"))).append("-").append(strToShortDate(crs.getString("insurpolicy_end_date"))).append(" ");
						}
						String startdate = crs.getString("insurpolicy_start_date").substring(0, 8);
						String enddate = crs.getString("insurpolicy_end_date").substring(0, 8);
						if (Long.parseLong(enddate) < Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
							Str.append("<br><font color=\"red\">[Expired]</font>");
						} else if (Long.parseLong(startdate) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
							Str.append("<br><font color=\"blue\">[Future Insurance]</font>");
						}
						Str.append("</td>\n<td nowrap>");
						if (!crs.getString("insurpolicy_premium_amt").equals("0")) {
							Str.append("Premium Amount: ").append(crs.getString("insurpolicy_premium_amt")).append("<br>");
						}
						if (!crs.getString("insurpolicy_idv_amt").equals("0")) {
							Str.append("IDV Amount: ").append(crs.getString("insurpolicy_idv_amt")).append("<br>");
						}
						if (!crs.getString("insurpolicy_od_amt").equals("0")) {
							Str.append("OD Amount: ").append(crs.getString("insurpolicy_od_amt")).append("<br>");
						}
						if (!crs.getString("insurpolicy_od_discount").equals("0")) {
							Str.append("OD Discount: ").append(crs.getString("insurpolicy_od_discount")).append("<br>");
						}
						if (!crs.getString("insurpolicy_payout").equals("0")) {
							Str.append("Payout Amount: ").append(crs.getString("insurpolicy_payout"));
						}
						Str.append("</td>\n<td>");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("insurpolicy_emp_id")).append("\">");
						Str.append(crs.getString("emp_name")).append("</a>");
						Str.append("</td>\n<td nowrap>");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n<td nowrap>");
						Str.append("<a href=\"insurance-update.jsp?update=yes&insurpolicy_id=").append(crs.getString("insurpolicy_id"))
								.append("\">Update Insurance</a>");
						Str.append("<br><a href=\"insurance-docs-list.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">List Documents</a>");
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					Str.append("<br><br><font color=\"red\"><b>No Insurance found!</b></font>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
