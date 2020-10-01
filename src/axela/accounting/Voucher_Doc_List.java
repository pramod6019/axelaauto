package axela.accounting;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_Doc_List extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String voucher_id = "0", vouchertype_id = "0", comp_id = "0";
	public String StrHTML = "";
	public String CountSql = "";
	public String Sqljoin = "";
	public String StrJoin = "";
	public String StrSql = "";
	public String PageCurrents = "0";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String fileext = "";
	public String fileName = "";
	public String StrSearch = "";
	public String LinkHeader = "";
	public String LinkAddPage = "";
	public String BranchAccess = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));

				if (!voucher_id.equals("0")) {
					LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href='../accounting/index.jsp'>Accounting</a> &gt; <a href=voucher-list.jsp?voucher_id=" + voucher_id + ">Voucher Id: "
							+ voucher_id + "</a> &gt; <a href=\"voucher-doc-list.jsp?voucher_id=" + voucher_id + "&vouchertype_id=" + vouchertype_id + "\">List Documents</a>:";
					LinkAddPage = "<a href=\"../portal/docs-update.jsp?add=yes&voucher_id=" + voucher_id + "&vouchertype_id=" + vouchertype_id + "\">Add New Document...</a>";
					StrSearch = " AND doc_voucher_id = " + voucher_id + "&vouchertype_id=" + vouchertype_id + "";
				}
				StrHTML = ListDocs();
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

	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		int PageListSize = 10;
		String effective = "";
		String duedate = "";

		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks, doc_effective,"
				+ " doc_duedays, doc_wf_title, doc_voucher_id, voucher_date, voucher_delivery_date,vouchertype_name, vouchertype_id";

		StrJoin = " FROM  " + compdb(comp_id) + "axela_acc_docs"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = doc_voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " WHERE 1=1 " + BranchAccess + ExeAccess;

		CountSql = "SELECT COUNT(DISTINCT doc_id)";

		StrSql += StrJoin;
		CountSql += StrJoin;

		if (!(StrSearch.equals(""))) {
			StrSql += StrSearch;
			CountSql += StrSearch;
		}
		// SOP("StrSql==" + StrSql);

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "voucher-doc-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			StrSql += " GROUP BY doc_id ORDER BY doc_id DESC";
			StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=listtable>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th>Documents</th>\n");
				Str.append("<th>Voucher </th>\n");
				// Str.append("<th>Title</th>\n");
				// Str.append("<th>Effective From</th>\n");
				// Str.append("<th>Due Date</th>\n");
				Str.append("<th width=20%>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					if (crs.getString("doc_effective").equals("0")) {
						effective = "";
					}

					if (crs.getString("doc_effective").equals("1")) {
						effective = "Sales Order Date";
					}

					if (crs.getString("doc_effective").equals("2")) {
						effective = "Delivery Date";
					}

					count++;
					Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(VoucherDocPath(comp_id)).exists()) {
							new File(VoucherDocPath(comp_id)).mkdirs();
						}
						File f = new File(VoucherDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<a href=../Fetchdocs.do?").append(QueryString).append("&doc_voucher_id=").append(crs.getString("doc_voucher_id")).append(">").append(crs.getString("doc_title"))
								.append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a>");
						if (!crs.getString("doc_remarks").equals("")) {
							Str.append("<br>").append(crs.getString("doc_remarks"));
						}
					} else {
						Str.append("&nbsp;</td>\n");
					}
					Str.append("<td valign=top align=left>").append(crs.getString("vouchertype_name"));
					Str.append("</td>\n");
					// Str.append("<td valign=top align=left>").append(crs.getString("doc_wf_title"));
					// Str.append("</td>\n");
					// Str.append("<td valign=top align=left>").append(effective);
					// Str.append("</td>\n<td valign=top align=center>");
					//
					// if (crs.getString("doc_effective").equals("0")) {
					// Str.append("-");
					// } else {
					// if (crs.getString("doc_effective").equals("1")) {
					// duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("voucher_date")), crs.getDouble("doc_duedays"), 0, 0));
					// Str.append(strToShortDate(duedate));
					// }
					// if (crs.getString("doc_effective").equals("2") && !crs.getString("voucher_delivery_date").equals("")) {
					// duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("voucher_delivery_date")), crs.getDouble("doc_duedays"), 0, 0));
					// Str.append(strToShortDate(duedate));
					// }
					// if (crs.getString("doc_effective").equals("2") && crs.getString("voucher_delivery_date").equals("")) {
					// Str.append("-");
					// }
					// }
					// Str.append("</td>\n");

					Str.append("<td valign=top align=left>");
					if (voucher_id.equals("0")) {
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&voucher_id=").append(voucher_id).append("&vouchertype_id=").append(crs.getString("vouchertype_id"))
								.append("\">UPDATE  " + compdb(comp_id) + "Document</a>"
										+ "<br><a href=\"../portal/docs-update-title.jsp?update=yes&title=yes")
								.append("\">UPDATE  " + compdb(comp_id) + "Title</a>"
										+ "");
					} else {
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&voucher_id=").append(voucher_id).append("&vouchertype_id=").append(crs.getString("vouchertype_id"))
								.append("\">UPDATE  " + compdb(comp_id) + "Document</a>"
										+ "<br><a href=\"../portal/docs-update-title.jsp?update=yes&title=yes&doc_id=").append(crs.getString("doc_id"))
								.append("\">UPDATE  " + compdb(comp_id) + "Title</a>"
										+ "");
					}
					Str.append("&nbsp;</td>\n");
				}
				Str.append("</tr>\n</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
