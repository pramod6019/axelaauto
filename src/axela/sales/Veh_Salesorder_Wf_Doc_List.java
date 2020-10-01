package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder_Wf_Doc_List extends Connect {

	public String StrHTML = "";
	public String StrSql = "", SqlJoin = "";
	public String doc_id = "";
	public String doc_so_id = "0";
	public String so_no = "", so_id = "";
	public String emp_id = "", comp_id = "0", msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				doc_so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				so_id = PadQuotes(request.getParameter("so_id"));
				so_no = ExecuteQuery("Select so_no from " + compdb(comp_id) + "axela_sales_so where so_id=" + doc_so_id + "");
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public String
	public String Listdata() {

		int TotalRecords = 0;
		String CountSql = "";
		StringBuilder Str = new StringBuilder();
		StrSql = "Select " + compdb(comp_id) + "axela_sales_so_wf_docs.*, "
				+ " (case when doc_effective=1 then 'Sales Order Date' else 'Delivery Date' end) as effective, "
				+ " so_no ";
		CountSql = "SELECT Count(distinct doc_id) ";
		SqlJoin = " from " + compdb(comp_id) + "axela_sales_so_wf_docs "
				+ " inner join " + compdb(comp_id) + "axela_sales_so on so_id=doc_so_id"
				+ " where 1 = 1 and doc_so_id=" + doc_so_id + " ";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;
		// SOP("StrSql=="+StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				StrHTML = "";
				int count = 0;
				Str.append("<table border=1 cellpadding=0 cellspacing=0 class=listtable>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th>Title</th>\n");
				Str.append("<th>Effective From</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td valign=top align=center ><b>").append(count).append(".</b></td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("doc_title")).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("effective")).append("</td>\n");
					Str.append("<td valign=top align=left><a href=\"veh-salesorder-wf-doc-update.jsp?update=yes&doc_id=").append(crs.getString("doc_id")).append("&so_id=")
							.append(crs.getString("doc_so_id")).append("\">Update Document</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><font color=red><b>No Work Flow Documents found!</b></font>");
		}
		return Str.toString();
	}
}
