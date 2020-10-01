/* Ved Prakash (4th May 2013) */
package cloudify.connect;

import javax.sql.rowset.CachedRowSet;

public class ConnectWS extends Connect {

	public String WSCheckExeAccess(String emp_id) {
		String StrSql = "";
		String ExeAccess = "";
		if (emp_id.equals("1")) {
			ExeAccess = "";
		} else {
			StrSql = "SELECT emp_all_exe FROM axela_emp WHERE emp_id = " + emp_id;
			String emp_all_exe = ExecuteQuery(StrSql);
			if (emp_all_exe.equals("1")) {
				ExeAccess = "";
			} else {
				StrSql = "SELECT empexe_id from axela_emp_exe where empexe_emp_id = " + emp_id;
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String Str = " ";
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							Str = Str + " OR emp_id=" + crs.getString("empexe_id");
						}
					}
					ExeAccess = " and (emp_id = " + emp_id + Str + ")";
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
				}
			}
		}
		return ExeAccess;
	}

	public String WSCheckBranchAccess(String emp_id, String branch_id, String role_id) {
		String StrSql = "";
		String Str = "";
		String BranchAccess = "";
		if (!branch_id.equals("0")) {
			BranchAccess = " and branch_id = " + branch_id + "";
		} else if (role_id.equals("1") && branch_id.equals("0")) {
			BranchAccess = "";
		} else if ((!role_id.equals("1")) && branch_id.equals("0")) {
			StrSql = "SELECT emp_branch_id FROM axela_emp_branch WHERE emp_id = " + emp_id;
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				Str = " and branch_id in (";
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.isLast()) {
							Str = Str + crs.getString("emp_branch_id");
						} else {
							Str = Str + crs.getString("emp_branch_id") + ", ";
						}
					}
				} else {
					Str = Str + "0";
				}
				Str = Str + ")";
				BranchAccess = Str;
				// SOPError("BranchAccess===" + BranchAccess);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			}
		}
		return BranchAccess;
	}

	public String CheckPerm(String fieldname, String emp_id) {
		String CheckPerm = "";
		String fieldn_name = "";
		String msg = "";
		try {
			if (!emp_id.equals("1")) {
				String field[] = fieldname.split(",");
				for (int i = 0; i < field.length; i++) {
					if (field[i].equals("emp_role_id") || field[i].equals("emp_export_access")
							|| field[i].equals("emp_report_access") || field[i].equals("emp_mis_access")) {
						fieldn_name = fieldn_name + " " + field[i] + "= 1 or ";
					} else {
						fieldn_name = fieldn_name + " access_name='" + field[i].trim() + "' or ";
					}
				}
				String Strsql = "SELECT emp_id"
						+ " FROM axela_emp"
						+ " LEFT JOIN axela_emp_access ON empaccess_emp_id = emp_id"
						+ " LEFT JOIN axela_module_access ON access_id = empaccess_access_id"
						+ " WHERE emp_id = " + emp_id + " and (" + fieldn_name.substring(0, fieldn_name.lastIndexOf("or")) + ")"
						+ " GROUP BY emp_id";
				// SOPError("Checkperm SQL : " + Strsql);
				CheckPerm = ExecuteQuery(Strsql);
				if (CheckPerm.equals("")) {
					msg = "Access Denied. Please Contact System Administrator!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return msg;
	}

	public String ReturnPerm(String fieldname, String emp_id) {
		String CheckPerm = "1";
		String fieldn_name = "";
		try {
			if (!emp_id.equals("1")) {
				String field[] = fieldname.split(",");
				for (int i = 0; i < field.length; i++) {
					/* emp_export_access */
					if (field[i].equals("emp_role_id") || field[i].equals("emp_export_access") || field[i].equals("emp_report_access") || field[i].equals("emp_mis_access")) {
						fieldn_name = fieldn_name + " " + field[i] + " = 1 or ";
					} else {
						fieldn_name = fieldn_name + " access_name = '" + field[i] + "' or ";
					}
				}
				String StrSql = "SELECT emp_id"
						+ " FROM axela_emp"
						+ " LEFT JOIN axela_emp_access ON empaccess_emp_id = emp_id"
						+ " LEFT JOIN axela_module_access ON access_id = empaccess_access_id"
						+ " WHERE emp_id = " + emp_id + " and (" + fieldn_name.substring(0, fieldn_name.lastIndexOf("or")) + ")"
						+ " GROUP BY emp_id";
				// SOPError("ReturnPerm SQL---" + StrSql);
				CheckPerm = ExecuteQuery(StrSql);
				if (!CheckPerm.equals("")) {
					CheckPerm = "1";
				} else {
					CheckPerm = "0";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return CheckPerm;
	}

	public String LimitRecords(int totalrecords, String pagecurrent) {
		// SOPError("totalrecords----"+totalrecords);
		// SOPError("pagecurrent----"+pagecurrent);
		int StartRec = 0;
		int EndRec = 0;
		int recperpage = 25;
		StartRec = ((Integer.parseInt(pagecurrent) - 1) * recperpage) + 1;
		// SOPError("StartRec----"+StartRec);
		EndRec = ((Integer.parseInt(pagecurrent) - 1) * recperpage) + recperpage;
		// SOPError("EndRec----"+EndRec);
		// SOPError("limit----"+" limit " + (StartRec - 1) + ", " + recperpage + "");
		return " limit " + (StartRec - 1) + ", " + recperpage + "";
	}

	// public void SOP(String Str) {
	// // if (AppRun().equals("0")) {
	// // SOP(Str);
	// // }
	// }
	// public String LimitRecords(int totalrecords, int pagecurrent) {
	// int StartRec = 0;
	// int EndRec = 0;
	// int recperpage = 5;
	// StartRec = ((pagecurrent - 1) * recperpage) + 1;
	// EndRec = ((pagecurrent - 1) * recperpage) + recperpage;
	// return " limit " + (StartRec - 1) + ", " + recperpage + "";
	// }
}
