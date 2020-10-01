package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_ExeAccess extends Connect {

	public String updateB = "";
	public static String msg = "";
	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_recperpage;
	public String emp_branchaccess ="";
	public String emp_exeaccess = "", emp_all_exe="";
	Executives_Update updateexe = new Executives_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {  
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
			// SOP("hi");
			emp_id = CNumeric(GetSession("emp_id", request));
//			SOP("emp_id---------"+emp_id);
			msg = PadQuotes(request.getParameter("msg"));
			updateB = PadQuotes(request.getParameter("update_button"));
			if ("Update".equals(updateB)) {
				StrSql = "SELECT emp_id,"
						+ " emp_all_exe"
	    				+ " "
	    				+ " FROM " + compdb(comp_id) + "axela_emp "
	    				+ " WHERE 1=1";
//				SOP("StrSql===" + StrSql);
				CachedRowSet crs =processQuery(StrSql, 0);
				while(crs.next())
				{   
					updateexe.UpdateExeAccess(crs.getString("emp_id"), crs.getString("emp_all_exe"), comp_id );
					
				}
	    	crs.close();
	    	msg = "ExeAccess updated successfully!";
			}
            }
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	
}
