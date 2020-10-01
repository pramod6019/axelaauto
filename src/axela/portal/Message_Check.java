package axela.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Message_Check extends HttpServlet {

	public String emp_id = "", branch_id = "";
	public String comp_id = "0";
	public String faculty_branch_id = "";
	public String exe_branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String post_id = "";
	public String ptmsg = "";
	Connect ct = new Connect();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		emp_id = ct.CNumeric(ct.GetSession("emp_id", request));
		comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			branch_id = ct.CNumeric(ct.GetSession("emp_branch_id", request));
			faculty_branch_id = ct.PadQuotes(request.getParameter("faculty_branch_id")).trim();
			post_id = ct.PadQuotes(request.getParameter("post_id"));
			exe_branch_id = ct.PadQuotes(request.getParameter("exe_branch_id")).trim();
			ptmsg = ct.PadQuotes(request.getParameter("ptmsg"));
			// StrHTML = ListMessage();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			if (!ptmsg.equals("")) {
				// out.println(StrHTML);
				// out.flush();
				// out.close();
				// SOP("ptmsg==" + ptmsg);
				out.println(ptmsg);
			}
		}

	}
	// public String ListMessage() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// String SqlStr="";
	// SqlStr = " SELECT post_id, post_desc" +
	// " from " + ct.compdb(comp_id) + "" + compdb(comp_id) + "axela_post " +
	// " where 1=1  ";
	// if(!exe_branch_id.equals(""))
	// {
	// SqlStr = SqlStr + " and (emp_branch_id=0 or emp_branch_id="+ exe_branch_id +")";
	// }
	//
	// SqlStr = SqlStr + " group by emp_id order by emp_name";
	// // SOP("SqlStr==== in PopulateExecutive" + SqlStr);
	// CachedRowSet crs =ct.processQuery(SqlStr);
	// while (crs.next())
	// {
	// Str.append("<tr>");
	// Str.append("<td>").append(crs.getString("post_desc")).append("</td>");
	// Str.append("</tr>");
	// }
	//
	// ct.crs.close();
	// return "";
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
}
