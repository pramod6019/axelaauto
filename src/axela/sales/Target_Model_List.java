// smitha nag 28, 29 march 2013
package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Model_List extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String update = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String SqlJoin1 = "";
	public String comp_id = "0";
	public String emp_id = "0", emp_name = "";
	// public String emp_role_id = "0";
	public String year = "";
	public String target_id = "0";
	public int target_enquiry_count = 0;
	public int target_so_min = 0;
	public int target_enquiry_calls_count = 0;
	public int target_enquiry_meetings_count = 0;
	public int target_enquiry_testdrives_count = 0;
	public int target_enquiry_hot_count = 0;
	public int target_so_count = 0;
	public String target_enddate = "";
	public String target_startdate = "";
	public String enquirycount = "0";
	public String enquirycall = "0";
	public String enquirymeeting = "0";
	public String enquirytestdrive = "0";
	public String enquiryhot = "0";
	public String enquiryso = "0";
	public String so_min = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String StrSearch = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public int model_count;
	public String target_model_id = "";
	public String targetmodelid = "";
	public String model_name[] = null;
	public String model_id[] = null;
	public String modeltarget_enquiry_count = "";
	public String modeltarget_enquiry_calls_count = "";
	public String modeltarget_enquiry_meetings_count = "";
	public String modeltarget_enquiry_testdrives_count = "";
	public String modeltarget_enquiry_hot_count = "";
	public String modeltarget_so_count = "";
	public String modeltarget_so_min = "";
	public int total_enquiry_count = 0, total_enquiry_calls_count = 0;
	public int total_enquiry_meetings_count = 0, total_enquiry_testdrives_count = 0;
	public int total_enquiry_hot_count = 0, total_so_count = 0;
	public int total_so_amount = 0;
	public String branchtype_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_target_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				emp_name = ExecuteQuery("select emp_name from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
				target_id = CNumeric(PadQuotes(request.getParameter("target_id")));
				month = CNumeric(PadQuotes(request.getParameter("month")));
				month_name = TextMonth(Integer.parseInt(month) - 1);
				month_name = month_name.substring(0, 3);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update_button"));
				year = CNumeric(PadQuotes(request.getParameter("year")));

				branchtype_id = CNumeric(ExecuteQuery("SELECT branch_branchtype_id "
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id"
						+ " WHERE emp_id=" + emp_id + ""));

				if (branchtype_id.equals("1")) {
					StrSql = "SELECT model_id, model_name ";
					CountSql = "SELECT COUNT(DISTINCT model_id)";
					SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_item_model "
							+ " INNER JOIN axela_brand ON brand_id = model_brand_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON  branch_brand_id = model_brand_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id "
							+ " WHERE 1=1"
							+ " AND model_sales = 1"
							+ " AND model_active = 1"
							+ " AND emp_id = " + emp_id;
					SqlJoin1 += " GROUP BY model_id "
							+ " ORDER BY model_name";
					StrSql += SqlJoin + SqlJoin1;
					CountSql += SqlJoin;
				} else if (branchtype_id.equals("2")) {
					StrSql = "select model_id, model_name ";
					CountSql = "SELECT COUNT(DISTINCT model_id)";
					SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_item_model "
							+ " WHERE 1=1"
							+ " AND model_active = 1"
							+ " AND model_name = 'Pre Owned'";
					SqlJoin1 += " GROUP BY model_id "
							+ " ORDER BY model_name";
					StrSql += SqlJoin + SqlJoin1;
					CountSql += SqlJoin;
				}

				model_count = Integer.parseInt(ExecuteQuery(CountSql));
				int mdlcount = 0;
				// SOP("StrSql----------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				try {
					if (crs.isBeforeFirst()) {
						model_id = new String[model_count];
						model_name = new String[model_count];

						while (crs.next()) {
							model_id[mdlcount] = crs.getString("model_id");
							model_name[mdlcount] = crs.getString("model_name");
							if (model_count != 1) {
								mdlcount++;
							}
						}
						crs.close();
					} else {
						StrHTML = " No Records Found";
					}

				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
				if (update.equals("Update")) {
					UpdateFields(request);
					response.sendRedirect(response.encodeRedirectURL("target-list.jsp?dr_executives=" + emp_id + "&dr_year=" + year + "&msg=" + "Target Updated Successfully!"));
				}
				StrHTML = Listdata(request);
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

	public String Listdata(HttpServletRequest request) throws SQLException {
		StringBuilder Str = new StringBuilder();
		if (!emp_id.equals("0")) {
			String tid[];
			String tmdlid[];
			String tsdate[];
			String tedate[];
			String tenquirycount[];
			String tenquirycall[];
			String tenquirymeeting[];
			String tenquirytestdrive[];
			String tenquiryhot[];
			String tenquiryso[];
			String tenquirysomin[];
			// branch_id = ExecuteQuery("SELECT emp_branch_id FROM " +
			// compdb(comp_id) + "axela_emp where emp_id =" + emp_id);
			StrSql = "SELECT modeltarget_target_id, modeltarget_model_id,"
					+ " COALESCE(target_startdate, '') AS target_startdate,"
					+ " COALESCE(target_enddate, '') AS target_enddate,"
					+ " COALESCE(modeltarget_enquiry_count, 0) AS target_enquiry_count,"
					+ " COALESCE(modeltarget_enquiry_calls_count, 0) AS target_enquiry_calls_count,"
					+ " COALESCE(modeltarget_enquiry_meetings_count, 0) AS target_enquiry_meetings_count,"
					+ " COALESCE(modeltarget_enquiry_testdrives_count, 0) AS target_enquiry_testdrives_count,"
					+ " COALESCE(modeltarget_enquiry_hot_count, 0) AS target_enquiry_hot_count,"
					+ " COALESCE(modeltarget_so_count, 0) AS target_so_count,"
					+ " COALESCE(modeltarget_so_min, 0) AS target_so_min";

			CountSql = "SELECT Count( modeltarget_target_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_target_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id=modeltarget_target_id"
					+ " WHERE target_emp_id=" + emp_id + ""
					+ " AND SUBSTR(target_startdate, 1, 8) >= " + year + doublenum(Integer.parseInt(month)) + "01"
					+ " AND SUBSTR(target_enddate, 1, 8)<=" + year + doublenum(Integer.parseInt(month)) + "31";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			// SOP("target model list query----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				// if (crs.isBeforeFirst()) {
				int ind = 0;
				int index = Integer.parseInt(ExecuteQuery(CountSql));
				tid = new String[index];
				tmdlid = new String[index];
				tsdate = new String[index];
				tedate = new String[index];
				tenquirycount = new String[index];
				tenquirycall = new String[index];
				tenquirymeeting = new String[index];
				tenquirytestdrive = new String[index];
				tenquiryhot = new String[index];
				tenquiryso = new String[index];
				tenquirysomin = new String[index];
				while (crs.next()) {
					tid[ind] = crs.getString("modeltarget_target_id");
					tmdlid[ind] = crs.getString("modeltarget_model_id");
					tsdate[ind] = crs.getString("target_startdate");
					tedate[ind] = crs.getString("target_enddate");
					tenquirycount[ind] = crs.getString("target_enquiry_count");
					tenquirycall[ind] = crs.getString("target_enquiry_calls_count");
					tenquirymeeting[ind] = crs.getString("target_enquiry_meetings_count");
					tenquirytestdrive[ind] = crs.getString("target_enquiry_testdrives_count");
					tenquiryhot[ind] = crs.getString("target_enquiry_hot_count");
					tenquiryso[ind] = crs.getString("target_so_count");
					tenquirysomin[ind] = crs.getString("target_so_min");
					if (index != 1) {
						ind++;
					}
				}
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th>Enquiry Count</th>\n");
				Str.append("<th>Enquiry Calls</th>\n");
				Str.append("<th>Enquiry Meeting</th>\n");
				Str.append("<th>Enquiry Test Drives</th>\n");
				Str.append("<th>Enquiry Hot</th>\n");
				Str.append("<th>Enquiry SO</th>\n");
				Str.append("<th>SO Minimum</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				for (int i = 0; i < model_count; i++) {
					count = count + 1;
					for (int j = 0; j < tmdlid.length; j++) {
						if (tmdlid[j].equals(model_id[i])) {
							targetmodelid = tmdlid[j];
							enquirycount = tenquirycount[j];
							enquirycall = tenquirycall[j];
							enquirymeeting = tenquirymeeting[j];
							enquirytestdrive = tenquirytestdrive[j];
							enquiryhot = tenquiryhot[j];
							enquiryso = tenquiryso[j];
							so_min = tenquirysomin[j];
							break;
						} else {
							target_model_id = "0";
							enquirycount = "0";
							enquirycall = "0";
							enquirymeeting = "0";
							enquirytestdrive = "0";
							enquiryhot = "0";
							enquiryso = "0";
							so_min = "0";
						}
					}
					Str.append("<tr><td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>");
					Str.append(model_name[i]);
					Str.append("</td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_count_").append(count).append("\" id=\"txt_count_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquirycount).append(" onKeyUp=\"CalItemTotal(1);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_call_").append(count).append("\" id=\"txt_call_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquirycall).append(" onKeyUp=\"CalItemTotal(2);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_meeting_").append(count).append("\" id=\"txt_meeting_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquirymeeting).append(" onKeyUp=\"CalItemTotal(3);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_testdrive_").append(count).append("\" id=\"txt_testdrive_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquirytestdrive).append(" onKeyUp=\"CalItemTotal(4);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_hot_").append(count).append("\" id=\"txt_hot_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquiryhot).append(" onKeyUp=\"CalItemTotal(5);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_so_").append(count).append("\" id=\"txt_so_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(enquiryso).append(" onKeyUp=\"CalItemTotal(6);\"></td>\n");
					Str.append("<td valign=top align=right><input name=\"txt_amt_").append(count).append("\" id=\"txt_amt_").append(count);
					Str.append("\" type=\"text\" class=\"form-control\" size=\"10\" maxlength=\"10\" value=");
					Str.append(so_min).append(" onKeyUp=\"CalItemTotal(7);\"></td>\n");
					Str.append("</tr>\n");
					total_enquiry_count = total_enquiry_count + Integer.parseInt(enquirycount);
					total_enquiry_calls_count = total_enquiry_calls_count + Integer.parseInt(enquirycall);
					total_enquiry_meetings_count = total_enquiry_meetings_count + Integer.parseInt(enquirymeeting);
					total_enquiry_testdrives_count = total_enquiry_testdrives_count + Integer.parseInt(enquirytestdrive);
					total_enquiry_hot_count = total_enquiry_hot_count + Integer.parseInt(enquiryhot);
					total_so_count = total_so_count + Integer.parseInt(enquiryso);
					total_so_amount = total_so_amount + (int) Double.parseDouble(so_min);
				}
				Str.append("<tr>");
				Str.append("<td colspan='2' align = 'right'><b>Total:</b></td>\n");
				Str.append("<td align = 'right' id = 'total_enquiry_count'><b>").append(total_enquiry_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_enquiry_calls_count'><b>").append(total_enquiry_calls_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_enquiry_meetings_count'><b>").append(total_enquiry_meetings_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_enquiry_testdrives_count'><b>").append(total_enquiry_testdrives_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_enquiry_hot_count'><b>").append(total_enquiry_hot_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_so_count'><b>").append(total_so_count).append("</b></td>\n");
				Str.append("<td align = 'right' id = 'total_so_amount'><b>").append(total_so_amount).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("<td align=\"center\" colspan=9><input name=\"model_count\" id=\"model_count\" type=\"hidden\"/ value=\"").append(model_count)
						.append("\"><input name=\"update_button\" type=\"submit\" ");
				Str.append("class=\"btn btn-success\" id=\"update_button\" value=\"Update\" /></td>");
				Str.append("</tr>");
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
				crs.close();
				// } else {
				// Str.append("<font color=red><b>Sales Consultant is Not Allocated to the Branch!</b></font>");
				// }
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public void Duplicate(HttpServletRequest request) throws SQLException {
		UpdateFields(request);

	}

	public void UpdateFields(HttpServletRequest request) throws SQLException {
		try {
			// String target_targetid
			target_id = CNumeric(ExecuteQuery("SELECT target_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_target"
					+ " WHERE substr(target_startdate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
					+ " AND substr(target_enddate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
					+ " AND target_emp_id = " + emp_id + ""));
			// SOP("target_targetid = " + target_id);
			// if (!target_targetid.equals("")) {
			// target_id = target_targetid;
			// }

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			if (!target_id.equals("0")) {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_target_model where modeltarget_target_id =" + target_id + "";
				stmttx.execute(StrSql);
			} else if (target_id.equals("0")) {
				AddTargetFields();
			}

			for (int i = 1; i <= model_count; i++) {
				modeltarget_enquiry_count = CNumeric(PadQuotes(request.getParameter("txt_count_" + i + "")));
				target_enquiry_count = target_enquiry_count + Integer.parseInt(modeltarget_enquiry_count);
				modeltarget_enquiry_calls_count = CNumeric(PadQuotes(request.getParameter("txt_call_" + i + "")));
				target_enquiry_calls_count = target_enquiry_calls_count + Integer.parseInt(modeltarget_enquiry_calls_count);
				modeltarget_enquiry_meetings_count = CNumeric(PadQuotes(request.getParameter("txt_meeting_" + i + "")));
				target_enquiry_meetings_count = target_enquiry_meetings_count + Integer.parseInt(modeltarget_enquiry_meetings_count);
				modeltarget_enquiry_testdrives_count = CNumeric(PadQuotes(request.getParameter("txt_testdrive_" + i + "")));
				target_enquiry_testdrives_count = target_enquiry_testdrives_count + Integer.parseInt(modeltarget_enquiry_testdrives_count);
				modeltarget_enquiry_hot_count = CNumeric(PadQuotes(request.getParameter("txt_hot_" + i + "")));
				target_enquiry_hot_count = target_enquiry_hot_count + Integer.parseInt(modeltarget_enquiry_hot_count);
				modeltarget_so_count = CNumeric(PadQuotes(request.getParameter("txt_so_" + i + "")));
				target_so_count = target_so_count + Integer.parseInt(modeltarget_so_count);
				modeltarget_so_min = CNumeric(PadQuotes(request.getParameter("txt_amt_" + i + "")));
				target_so_min = target_so_min + (int) Double.parseDouble(modeltarget_so_min);

				// if ((!modeltarget_enquiry_count.equals("0")
				// || !modeltarget_enquiry_calls_count.equals("0") ||
				// !modeltarget_enquiry_meetings_count.equals("0")
				// || !modeltarget_enquiry_testdrives_count.equals("0") ||
				// !modeltarget_enquiry_hot_count.equals("0")
				// || !modeltarget_so_count.equals("0") ||
				// !modeltarget_so_min.equals("0"))) {
				// SOP("latest_target_id......   " + target_id);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_target_model"
						+ " (modeltarget_model_id,"
						+ " modeltarget_target_id,"
						+ " modeltarget_enquiry_count,"
						+ " modeltarget_enquiry_calls_count,"
						+ " modeltarget_enquiry_meetings_count,"
						+ " modeltarget_enquiry_testdrives_count,"
						+ " modeltarget_enquiry_hot_count,"
						+ " modeltarget_so_count,"
						+ " modeltarget_so_min)"
						+ " values"
						+ " (" + model_id[i - 1] + ","
						+ " " + target_id + ","
						+ " " + modeltarget_enquiry_count + ","
						+ " " + modeltarget_enquiry_calls_count + ","
						+ " " + modeltarget_enquiry_meetings_count + ","
						+ " " + modeltarget_enquiry_testdrives_count + ","
						+ " " + modeltarget_enquiry_hot_count + ","
						+ " " + modeltarget_so_count + ","
						+ " " + modeltarget_so_min + ")";
				// SOP(StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();
			/* Updating the main table with the updated count */
			UpdateTargetFields();
			conntx.commit();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void AddTargetFields() throws SQLException {
		try {
			StrSql = "insert into " + compdb(comp_id) + "axela_sales_target (  "
					+ " target_emp_id, "
					+ " target_startdate, "
					+ " target_enddate, "
					+ " target_entry_id,  "
					+ " target_entry_date ) "
					+ " values  "
					+ " (" + emp_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "31000000" + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				target_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void UpdateTargetFields() throws SQLException {
		try {
			StrSql = "Update " + compdb(comp_id) + "axela_sales_target "
					+ " set "
					+ " target_enquiry_count= " + target_enquiry_count + ", "
					+ " target_enquiry_calls_count= " + target_enquiry_calls_count + ", "
					+ " target_enquiry_meetings_count= " + target_enquiry_meetings_count + ", "
					+ " target_enquiry_testdrives_count= " + target_enquiry_testdrives_count + ", "
					+ " target_enquiry_hot_count= " + target_enquiry_hot_count + ", "
					+ " target_so_count= " + target_so_count + ", "
					+ " target_so_min= " + target_so_min + " "
					+ " WHERE target_id=" + target_id + "";
			// SOP("Strsql===" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
}
