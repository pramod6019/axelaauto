package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Dash_Attachment extends Connect {

	public String ticket_id = "0";
	public String StrHTML = "";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String ExeAccess = "";
	public String ticket_subject = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				ExeAccess = GetSession("ExeAccess", request);
				// CheckPerm(comp_id, "emp_ticket_access", request, response);
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				// SOP("ticket_id=="+ticket_id);
				msg = PadQuotes(request.getParameter("msg"));
				StrSql = "select ticket_subject "
						+ " from " + compdb(comp_id) + "axela_service_ticket "
						+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
						+ " where 1=1 and ticket_id =" + ticket_id + ExeAccess + ""
						+ " group by ticket_id "
						+ " order by ticket_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						ticket_subject = crs.getString("ticket_subject");
					}
					StrHTML = ListDocs();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Ticket!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		StrSql = "select * ";

		CountSql = "select count(doc_id) ";

		SqlJoin = " from " + compdb(comp_id) + "axela_service_ticket_docs "
				+ " where doc_ticket_id='" + ticket_id + "'";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

		StrSql = StrSql + " order by doc_id desc";

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th>Attachment</th>");
				Str.append("<th width=20%>Actions </th>");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td height=20 valign=top align=center >").append(count).append("</td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(TicketDocPath(comp_id)).exists()) {
							new File(TicketDocPath(comp_id)).mkdirs();
						}
						File f = new File(TicketDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=top align=left><a href=../Fetchdocs.do?" + "doc_id=").append(crs.getString("doc_id")).append(">").append(crs.getString("doc_title")).append(" (")
								.append(ConvertFileSizeToBytes(FileSize(f))).append(")</a><br> ").append(crs.getString("doc_remarks")).append("</td>\n");// <a
																																							// href=../Fetchdocs.do?+QueryString+&doc_id="+ crs.getString("doc_id") +"
																																							// \"target=_blank>
					} else {
						Str.append("<td valign=top align=left>").append(crs.getString("doc_title")).append(" (").append(crs.getString("doc_id")).append(") (0 Bytes)<br>")
								.append(crs.getString("doc_remarks")).append("</td>\n");// <a href=../Fetchdocs.do?+QueryString+&doc_id="+ crs.getString("doc_id") +" \"target=_blank>
					}
					if (!ticket_id.equals("")) {
						Str.append("<td valign=top align=left><a href=\"../portal/docs-update.jsp?update=yes&group=ticket&doc_id=").append(crs.getString("doc_id")).append("&ticket_id=")
								.append(crs.getString("doc_ticket_id")).append("\">Update Document</a></td>\n");
					}
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
}
