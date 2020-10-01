// Ved (11 Feb 2013)
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Activity_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("activitystrsql", request).equals("")) {
					StrSearch = GetSession("activitystrsql", request);
				}
				if (exportB.equals("Export")) {
					ActivityDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ActivityDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select activity_id as 'ID', activity_desc as 'Description',"
					+ " type_name as 'Type'"
					+ " from " + compdb(comp_id) + "axela_activity"
					+ " inner join " + compdb(comp_id) + "axela_activity_type on type_id = activity_type_id"
					+ " inner join " + compdb(comp_id) + "axela_priority on priority_id = activity_priority_id"
					+ " inner join " + compdb(comp_id) + "axela_emp  on activity_emp_id = emp_id"
					+ " left join " + compdb(comp_id) + "axela_activity_status on activity_status_id = status_id"
					+ " left join " + compdb(comp_id) + "axela_customer_contact on activity_contact_id = contact_id"
					+ " where 1=1 " + StrSearch + ""
					+ " group by activity_id order by activity_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			ExportToXLSX excel = new ExportToXLSX();
			excel.Export(request, response, crs, printoption, comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = ActivityDetails" + StrSelectdrop("ActivityDetails", printoption) + ">Activity Details</option>\n";
		return print;
	}

	public String PopulateExport() {
		if (exporttype.equals("")) {
			exporttype = "xlsx";
		}
		String export = "";
		export = export + "<option value = xlsx" + StrSelectdrop("xlsx", exporttype) + ">MS Excel Format</option>\n";
		return export;
	}
}
