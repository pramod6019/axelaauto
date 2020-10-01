package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Sales_Pipeline extends Connect {
	
	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	public String go = "";
	public String comp_id = "0";
	public String ExeAccess = "";
	public String NoSalesPipeline = "";
	public String SalesPipeline = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				// StrHTML = ListTarget();
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "enquiry_branch_id");
					
					StrSearch += ExeAccess.replace("emp_id", "enquiry_emp_id");
					
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_brand_id IN (" + brand_id + "))";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_region_id IN (" + region_id + ") )";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ListTarget();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}
	
	protected void CheckForm() {
		msg = "";
	}
	
	public String ListTarget() {
		
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT stage_name, count(enquiry_id) AS stage_count"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id =enquiry_emp_id "
				+ " WHERE 1=1 AND enquiry_status_id =1 "
				+ StrSearch + ""
				+ " GROUP BY stage_id ";
		// SOP("strsql IN LmsSummary--222222222 -" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				
				SalesPipeline = "[";
				while (crs.next()) {
					SalesPipeline += "{'stage': " + "'" + crs.getString("stage_name") + "'" + ","
							+ " 'column-1':'" + crs.getString("stage_count") + "'"
							+ "}";
					if (!crs.isLast()) {
						SalesPipeline += ",";
					}
				}
				SalesPipeline += "]";
			} else {
				NoSalesPipeline = "No Open Enquiry!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
