package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executive_Summary_Popover extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_prevexp[];
	public String currexp[];
	public int years = 0;
	public int months = 0;
	public int days = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				if (!emp_id.equals("0")) {
					StrHTML = ExecutiveSummary(response);
				}
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

	public String ExecutiveSummary(HttpServletResponse response) {
		String marital_status = "", emp_active = "", sex = "", address = "", Img = "";
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT "
				+ " emp_id,"
				+ " emp_name,"
				+ " emp_ref_no,"
				+ " emp_active,"
				+ " jobtitle_desc,"
				+ " emp_date_of_join,"
				+ " emp_prevexp,"
				+ " emp_sex,"
				+ " emp_dob,"
				+ " emp_married,"
				+ " emp_qualification,"
				+ " emp_phone1,"
				+ " emp_mobile1,"
				+ " emp_email1,"
				+ " emp_address,"
				+ " emp_landmark,"
				+ " emp_city,"
				+ " emp_pin,"
				+ " emp_state,"
				+ " emp_photo, "
				+ "	COALESCE(( "
				+ " SELECT MAX(so_entry_date)"
				+ "	FROM " + compdb(comp_id) + "axela_emp"
				+ "	INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
				+ "	INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_emp_id = teamtrans_emp_id"
				+ "	WHERE so_active = 1"
				+ "	AND emp_id = " + emp_id
				+ "	GROUP BY so_emp_id"
				+ "	ORDER BY so_entry_date"
				+ "), '')AS last_so_date"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
				+ " WHERE 1 = 1"
				+ " AND emp_id =" + emp_id;

		// SOP("StrSql===" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					if (!crs.getString("emp_photo").equals("")) {
						Img = "<b><img src=../Thumbnail.do?empphoto=" + crs.getString("emp_photo") + "&width=200 alt=''></b>";
					} else {
						Img = "";
					}

					// Image
					if (!Img.equals("")) {
						Str.append("<center>" + Img + "</center>");
					}

					Str.append("Executive ID: " + crs.getString("emp_id"));

					// Reference No.
					if (!crs.getString("emp_ref_no").equals("")) {
						Str.append("</br>Reference No.: " + crs.getString("emp_ref_no"));
					}
					// Designation
					if (!crs.getString("jobtitle_desc").equals("")) {
						Str.append("</br>Designation: " + crs.getString("jobtitle_desc"));
					}
					// Date Of Joining:
					if (!crs.getString("emp_date_of_join").equals("")) {
						currexp = DiffBetweenDates(crs.getString("emp_date_of_join"), ToLongDate(kknow())).split(",");
						Str.append("</br>Date Of Joining: " + strToShortDate(crs.getString("emp_date_of_join")) + " (")
								.append(currexp[0] + " Years ").append(currexp[1] + " Months ").append(currexp[2] + " Days").append(") ");
					}
					// Previous Experience:
					if (!crs.getString("emp_prevexp").equals("")) {
						emp_prevexp = crs.getString("emp_prevexp").split(",");
						Str.append("</br>Previous Experience: ").append(emp_prevexp[0]).append(" Years ").append(emp_prevexp[1]).append(" Months");
					}
					// Total Experience:
					if (!crs.getString("emp_prevexp").equals("") && !crs.getString("emp_date_of_join").equals("")) {
						currexp = DiffBetweenDates(crs.getString("emp_date_of_join"), ToLongDate(kknow())).split(",");
						emp_prevexp = crs.getString("emp_prevexp").split(",");
						months = Integer.parseInt(currexp[1]) + Integer.parseInt(emp_prevexp[1]);
						days = Integer.parseInt(currexp[2]);
						if (months > 11) {
							months = months % 12;
							years = Integer.parseInt(emp_prevexp[0]) + Integer.parseInt(currexp[0]) + 1;
						} else {
							years = Integer.parseInt(emp_prevexp[0]) + Integer.parseInt(currexp[0]);
						}
						Str.append("</br><b>Total Experience: ")
								.append(years).append(" Years ")
								.append(months).append(" Months ")
								.append(days).append(" Days")
								.append("</b>");
					} else if (!crs.getString("emp_date_of_join").equals("")) {
						currexp = DiffBetweenDates(crs.getString("emp_date_of_join"), ToLongDate(kknow())).split(",");
						Str.append("</br><b>Total Experience: ");
						Str.append(currexp[0] + " Years ");
						Str.append(currexp[1] + " Months ");
						Str.append(currexp[2] + " Days</b>");
					}
					// Sex
					if (!crs.getString("emp_sex").equals("")) {
						if (crs.getString("emp_sex").equals("0")) {
							sex = "Female";
						} else {
							sex = "Male";
						}
						Str.append("</br>Sex: " + sex);
					}

					// Marital Status:
					if (crs.getString("emp_married").equals("0")) {
						marital_status = "Unmarried";
					} else {
						marital_status = "Married";
					}
					if (!marital_status.equals("")) {
						Str.append("</br>Marital Status: " + marital_status);
					}
					// Qualification
					if (!crs.getString("emp_qualification").equals("")) {
						Str.append("</br>Qualification: " + crs.getString("emp_qualification"));
					}
					// Mobile 1
					if (!crs.getString("emp_mobile1").equals("")) {
						Str.append("</br>Mobile1: " + crs.getString("emp_mobile1"));
					}
					// Email 1
					if (!crs.getString("emp_email1").equals("")) {
						Str.append("</br>Email1: " + crs.getString("emp_email1"));
					}
					// Address
					if (!crs.getString("emp_address").equals("")) {
						address = crs.getString("emp_address");
					}
					if (!crs.getString("emp_city").equals("")) {
						address = address + ", " + crs.getString("emp_city");
					}
					if (!crs.getString("emp_pin").equals("")) {
						address = address + " - " + crs.getString("emp_pin") + ",";
					}
					if (!crs.getString("emp_state").equals("")) {
						address = address + " " + crs.getString("emp_state") + " ";
					}

					if (!address.equals("")) {
						Str.append("</br>Address: " + address);
					}
					// Landmark
					// SOP("Landmark===" + crs.getString("emp_landmark"));
					// if (!crs.getString("emp_landmark").equals("")) {
					// Str.append("</br>Landmark" + crs.getString("emp_landmark"));
					// }
					// Status
					Str.append("</br>Status: ");

					if (crs.getString("emp_active").equals("1")) {
						Str.append("Active");
					} else {
						Str.append(" <font color=red><b>[Inactive]</b></font>");
					}
					if (!crs.getString("last_so_date").equals("")) {
						Str.append("<br/><font color=red><b>Last SO: ").append((int) getDaysBetween(crs.getString("last_so_date"), ToLongDate(kknow())) + " Days Ago</b></font>");
					} else {
						Str.append("<br/><font color=red><b>Last SO: No SO </b></font>");
					}

				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access Denied!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
