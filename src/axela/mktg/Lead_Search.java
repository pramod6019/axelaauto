package axela.mktg;

//@Murali 25th aug 2012

//public class Lead_Search extends Smart_Search {
//
// public String BranchAccess = "";
// public String ExeAccess = "";
// public String config_sales_soe = "";
// public String config_sales_sob = "";
// //Get the branchcount and franchiseecount from Connect class
// public int branch_count = branchcount;
// public int franchisee_count = franchiseecount;
// public Smart SmartSearch = new Smart();
// public Lead_List modulelist = new Lead_List();
// public String StrSearch = "";
// public String advSearch = "";
// StringBuilder Str = new StringBuilder();
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response) {
// try {
// Str_Link = "Search Leads";
// StrSession = "leadstrsql";
// StrLocation = "lead-list.jsp";
// StrPopWin = "leadlist";
// StrCheckPerm = "emp_lead_access";
// advSearch = PadQuotes(request.getParameter("advsearch_button"));
// CheckSession(request, response);
// HttpSession session = request.getSession(true);
// BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
// ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
// CheckPerm(StrCheckPerm, request, response);
// PopulateConfigDetails();
// if (advSearch.equals("Search")) {
// StrSearch = SmartSearch.BuildSmartSql(modulelist.smartarr, request);
// doPostSmart(request, response, StrSearch, AdvSearchLists(request));
// }
// } catch (Exception ex) {
// SOPError(ClientName+"===" + this.getClass().getName());
// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
// }
//	}
//
// public String PopulateBranch(HttpServletRequest request) {
// StrSql = "select branch_id, concat(branch_name,' (', branch_code,')') as branchname"
// + " from  axela_branch "
// + " inner join axela_mktg_lead on lead_branch_id = branch_id"
// + " where 1=1 and branch_active='1' " + BranchAccess
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by branch_id order by branch_name";
// String str = PopulateList(StrSql, "branch_id", "branchname", request.getParameterValues("dr_branch"));
// return str;
// }
//
// public String PopulateBranchType(HttpServletRequest request) {
// StrSql = "select branchtype_id, branchtype_name "
// + " from axela_branch_type "
// + " inner join axela_branch on branch_branchtype_id = branchtype_id"
// + " inner join axela_mktg_lead on lead_branch_id = branch_id"
// + " where 1 = 1 and branch_active='1' " + BranchAccess
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by branchtype_id order by branchtype_name";
// String str = PopulateList(StrSql, "branchtype_id", "branchtype_name", request.getParameterValues("dr_branchtype"));
// return str;
// }
//
// public String PopulateFranchisee(HttpServletRequest request) {
// StrSql = "select franchisee_id, franchisee_name "
// + " from axela_franchisee"
// + " inner join axela_branch on branch_franchisee_id=franchisee_id"
// + " inner join axela_mktg_lead on lead_branch_id = branch_id"
// + " where 1=1 and branch_active='1' " + BranchAccess
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by franchisee_id order by franchisee_name";
// String str = PopulateList(StrSql, "franchisee_id", "franchisee_name", request.getParameterValues("dr_franchisee"));
// return str;
// }
//
// public String PopulateFranchiseeType(HttpServletRequest request) {
// StrSql = "select franchiseetype_id, franchiseetype_name, franchisee_franchiseetype_id "
// + " from axela_franchisee_type "
// + " inner join axela_franchisee on franchisee_franchiseetype_id=franchiseetype_id"
// + " inner join axela_branch on branch_franchisee_id=franchisee_id"
// + " inner join axela_mktg_lead on lead_branch_id = branch_id"
// + " where 1=1 and branch_active='1' " + BranchAccess
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by franchiseetype_id order by franchiseetype_name";
// String str = PopulateList(StrSql, "franchiseetype_id", "franchiseetype_name", request.getParameterValues("dr_franchiseetype"));
// return str;
// }
//
// public String PopulateTitle(HttpServletRequest request) {
// StrSql = "SELECT title_id, title_desc "
// + " from axela_title "
// + " inner join axela_mktg_lead on lead_title_id = title_id"
// + " where 1=1 " + BranchAccess.replace("branch_id", "account_branch_id")
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by title_id order by title_desc ";
// String str = PopulateList(StrSql, "title_id", "title_desc", request.getParameterValues("dr_title"));
// return str;
// }
//
// public String PopulateEmployeeCount(HttpServletRequest request) {
// StrSql = "select empcount_id, empcount_desc "
// + " from axela_empcount "
// + " inner join axela_mktg_lead on lead_empcount_id = empcount_id"
// + " where 1=1 " + BranchAccess.replace("branch_id", "account_branch_id")
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by empcount_id order by empcount_desc";
// String str = PopulateList(StrSql, "empcount_id", "empcount_desc", request.getParameterValues("dr_employee"));
// return str;
// }
//
// public String PopulateSOE(HttpServletRequest request) {
// StrSql = "select soe_id, soe_name "
// + " from axela_soe "
// + " inner join axela_mktg_lead on lead_soe_id = soe_id"
// + " where 1=1 " + BranchAccess.replace("branch_id", "lead_branch_id")
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by soe_id order by soe_name";
// String str = PopulateList(StrSql, "soe_id", "soe_name", request.getParameterValues("dr_soe"));
// return str;
// }
//
// public String PopulateSOB(HttpServletRequest request) {
// StrSql = "select sob_id, sob_name "
// + " from axela_sob "
// + " inner join axela_mktg_lead on lead_sob_id = sob_id"
// + " where 1=1 " + BranchAccess.replace("branch_id", "lead_branch_id")
// + ExeAccess.replace("emp_id", "lead_emp_id")
// + " group by sob_id order by sob_name";
// String str = PopulateList(StrSql, "sob_id", "sob_name", request.getParameterValues("dr_sob"));
// return str;
// }
//
// public String PopulateExecutive(HttpServletRequest request) {
// StrSql = "select emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name "
// + " from axela_emp "
// + " inner join axela_mktg_lead on lead_emp_id = emp_id"
// + " where 1=1 and emp_sales=1 and emp_active = '1'"
// + BranchAccess.replace("branch_id", "lead_branch_id")
// + ExeAccess
// + " group by emp_id order by emp_name";
// String str = PopulateList(StrSql, "emp_id", "emp_name", request.getParameterValues("dr_executive"));
// return str;
// }
//
// public String PopulateEntryBy(HttpServletRequest request) {
// StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name "
// + " from axela_emp "
// + " inner join axela_mktg_lead on lead_entry_id = emp_id"
// + " WHERE 1=1" + BranchAccess.replace("branch_id", "lead_branch_id")
// + ExeAccess
// + " group by emp_id ORDER BY emp_name";
// String str = PopulateList(StrSql, "emp_id", "emp_name", request.getParameterValues("dr_entryby"));
// return str;
// }
//
// public String PopulateModifiedBy(HttpServletRequest request) {
// StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name "
// + " from axela_emp "
// + " inner join axela_mktg_lead on lead_modified_id = emp_id"
// + " WHERE 1=1" + BranchAccess.replace("branch_id", "lead_branch_id")
// + ExeAccess
// + " group by emp_id ORDER BY emp_name";
// String str = PopulateList(StrSql, "emp_id", "emp_name", request.getParameterValues("dr_modifiedby"));
// return str;
// }
//
// protected String AdvSearchLists(HttpServletRequest request) {
// StringBuilder Str = new StringBuilder();
// String sql = "";
// Str.append(BuildListStr(request.getParameterValues("dr_branch"), "lead_branch_id", "", PadQuotes(request.getParameter("dr_branch_list"))));
//
// sql = " lead_branch_id in (SELECT branch_id from axela_branch "
// + " where ";
// Str.append(BuildListStr(request.getParameterValues("dr_branchtype"), "branch_branchtype_id", sql, PadQuotes(request.getParameter("dr_branchtype_list"))));
//
// sql = " lead_branch_id in (SELECT branch_id from axela_branch "
// + " where ";
// Str.append(BuildListStr(request.getParameterValues("dr_franchisee"), "branch_franchisee_id", sql, PadQuotes(request.getParameter("dr_franchisee_list"))));
//
// sql = " lead_branch_id in (SELECT branch_id from axela_branch "
// + " inner join axela_franchisee on franchisee_id = branch_franchisee_id"
// + " where ";
// Str.append(BuildListStr(request.getParameterValues("dr_franchiseetype"), "franchisee_franchiseetype_id", sql, PadQuotes(request.getParameter("dr_franchiseetype_list"))));
//
// Str.append(BuildListStr(request.getParameterValues("dr_executive"), "lead_emp_id", "", PadQuotes(request.getParameter("dr_executive_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_soe"), "lead_soe_id", "", PadQuotes(request.getParameter("dr_soe_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_sob"), "lead_sob_id", "", PadQuotes(request.getParameter("dr_sob_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_title"), "lead_title_id", "", PadQuotes(request.getParameter("dr_title_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_employee"), "lead_empcount_id", "", PadQuotes(request.getParameter("dr_employee_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_entryby"), "lead_entry_id", "", PadQuotes(request.getParameter("dr_entryby_list"))));
// Str.append(BuildListStr(request.getParameterValues("dr_modifiedby"), "lead_modified_id", "", PadQuotes(request.getParameter("dr_modifiedby_list"))));
// return Str.toString();
// }
//
// public void doGet(HttpServletRequest request, HttpServletResponse response)
// throws ServletException, IOException {
// doPost(request, response);
// }
//
// protected void PopulateConfigDetails() {
// StrSql = "Select config_sales_soe, config_sales_sob"
// + " from axela_config";
// // SOP(StrSqlBreaker(StrSql));
// try {
// CachedRowSet crs = processQuery(StrSql);
// while (crs.next()) {
// config_sales_soe = crs.getString("config_sales_soe");
// config_sales_sob = crs.getString("config_sales_sob");
// }
// crs.close();
// } catch (Exception ex) {
// SOPError(ClientName+"===" + this.getClass().getName());
// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
// }
// }
// }
