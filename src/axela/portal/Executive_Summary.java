package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executive_Summary extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String QueryString = "";
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
				QueryString = PadQuotes(request.getQueryString());
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				StrHTML = ExecutiveListData(response);
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

	public String ExecutiveListData(HttpServletResponse response) {
		String address = "";
		StringBuilder Str = new StringBuilder();
		StrSql = "Select " + compdb(comp_id) + "axela_emp.*,jobtitle_desc"
				+ " from " + compdb(comp_id) + "axela_emp"
				+ " inner join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
				+ " where 1=1"
				+ " AND emp_id =" + emp_id;
		// + ExeAccess;
		// SOP("StrSql..ExecutiveListData..." + StrSql);
		// SOP("StrSql=====" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<table class=\"table table-responsive\">");
		// Str.append("<tr align=center></tr>");

		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("emp_photo").equals("")) {
						Str.append("<tr valign=center>" + "<td align=center colspan=2>"
								+ "<b><img src=../Thumbnail.do?empphoto=").append(crs.getString("emp_photo")).append("&width=200 alt=").append(crs.getString("emp_name")).append("></b>"
								+ "</td></tr>");
					}
					if (!crs.getString("emp_name").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td width=\"25%\" align=\"left\">Name: </td>\n");
						Str.append("<td width=\"75%\" align=\"left\">\n");
						Str.append("<a href=\"../portal/executive-list.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
						Str.append("<b>").append(crs.getString("emp_name")).append("</b></a>\n</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("emp_ref_no").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Reference No.: </td>" + "<td align=left>").append(crs.getString("emp_ref_no")).append("</td></tr>\n");
					}
					if (!crs.getString("emp_jobtitle_id").equals("")) {
						Str.append("<tr>\n" + " <td align=left>  Designation: </td>" + "<td align=left>").append(crs.getString("jobtitle_desc")).append("</td></tr>\n");
					}
					if (!crs.getString("emp_date_of_join").equals("")) {
						currexp = DiffBetweenDates(crs.getString("emp_date_of_join"), ToLongDate(kknow())).split(",");
						Str.append("<tr>\n" + " <td align=left>Date Of Joining: </td>" + "<td align=left>")
								.append(strToShortDate(crs.getString("emp_date_of_join")) + " (");
						Str.append(currexp[0] + " Years ");
						Str.append(currexp[1] + " Months ");
						Str.append(currexp[2] + " Days");
						Str.append(") " + "</td></tr>\n");
					}

					if (!crs.getString("emp_prevexp").equals("")) {
						emp_prevexp = crs.getString("emp_prevexp").split(",");

						Str.append("<tr>\n" + " <td align=left>Previous Exprience: </td>" + "<td align=left>")
								.append(emp_prevexp[0]).append(" Years ")
								.append(emp_prevexp[1]).append(" Months")
								.append("</td></tr>\n");
					}

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
						Str.append("<tr>\n" + " <td align=left>Total Exprience: </td>" + "<td align=left>")
								.append(years).append(" Years ")
								.append(months).append(" Months ")
								.append(days).append(" Days")
								.append("</td></tr>\n");
					} else if (!crs.getString("emp_date_of_join").equals("")) {
						currexp = DiffBetweenDates(crs.getString("emp_date_of_join"), ToLongDate(kknow())).split(",");
						Str.append("<tr>\n" + " <td align=left>Total Exprience: </td>" + "<td align=left>");
						Str.append(currexp[0] + " Years ");
						Str.append(currexp[1] + " Months ");
						Str.append(currexp[2] + " Days");
						Str.append("</td></tr>\n");
					}

					if (!crs.getString("emp_sex").equals("")) {
						String Sex = "";
						if (crs.getString("emp_sex").equals("0")) {
							Sex = "Female";
						} else {
							Sex = "Male";
						}
						Str.append("<tr >\n" + "<td align=left> Sex: </td>" + "<td align=left>").append(Sex).append("</td></tr>\n");
					}
					if (!crs.getString("emp_dob").equals("")) {
						Str.append("<tr >\n" + "<td align=left> DOB: </td>" + "<td align=left>").append(strToShortDate(crs.getString("emp_dob"))).append("</td></tr>\n");
					}
					if (!crs.getString("emp_married").equals("")) {
						String marital_status = "";
						if (crs.getString("emp_married").equals("0")) {
							marital_status = "Unmarried";
						} else {
							marital_status = "Married";
						}
						Str.append("<tr>\n" + "<td align=left> Marital Status: </td>" + "<td align=left>").append(marital_status).append("</td></tr>\n");
					}
					if (!crs.getString("emp_qualification").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Qualification: </td>" + "<td align=left>").append(crs.getString("emp_qualification")).append("</td></tr>\n");
					}
					if (!crs.getString("emp_phone1").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Phone 1: </td>" + "<td align=left>").append(SplitPhoneNo(crs.getString("emp_phone1"), 4, "T"))
								.append(ClickToCall(crs.getString("emp_phone1"), comp_id)).append("</td></tr>\n");
					}
					if (!crs.getString("emp_phone2").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Phone 2: </td>" + "<td align=left>").append(SplitPhoneNo(crs.getString("emp_phone2"), 4, "T"))
								.append(ClickToCall(crs.getString("emp_phone2"), comp_id)).append("</td></tr>\n");
					}
					if (!crs.getString("emp_mobile1").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Mobile 1: </td>" + "<td align=left>").append(SplitPhoneNo(crs.getString("emp_mobile1"), 5, "M"))
								.append(ClickToCall(crs.getString("emp_mobile1"), comp_id)).append("</td></tr>\n");
					}
					if (!crs.getString("emp_mobile2").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Mobile 2: </td>" + "<td align=left>").append(SplitPhoneNo(crs.getString("emp_mobile2"), 5, "M"))
								.append(ClickToCall(crs.getString("emp_mobile2"), comp_id)).append("</td></tr>\n");
					}
					if (!crs.getString("emp_email1").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Email 1: </td>" + "<td align=left>").append(crs.getString("emp_email1")).append("</td></tr>\n");
					}
					if (!crs.getString("emp_email2").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Email 2: </td>" + "<td align=left>").append(crs.getString("emp_email2")).append("</td></tr>\n");
					}
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
						Str.append("<tr><td align=left> Address:</td>");
						Str.append("<td align=left >").append(address).append("</td></tr>\n");
					}
					if (!crs.getString("emp_landmark").equals("")) {
						Str.append("<tr>\n" + "<td align=left> Landmark: </td>" + "<td align=left>").append(crs.getString("emp_landmark")).append("</td>");
						Str.append("</tr>\n");
					}
					Str.append("<tr><td align=left>Status: </td>");
					Str.append("<td align=left >");
					if (crs.getString("emp_active").equals("1")) {
						Str.append("Active");
					} else {
						Str.append("InActive");
					}
					Str.append("</td></tr>\n");
					Str.append(GetReportingExecutives());
				}
				Str.append("</table>\n");
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
	public String GetReportingExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<tr>\n" + "<td align=left valign=top> Executives for Reporting: </td>" + "");
			Str.append("<td align=left>");

			StrSql = " Select (select concat(emp_name,' (', emp_ref_no, ')')"
					+ " from " + compdb(comp_id) + "axela_emp where emp_id=empexe_id) as empname,"
					+ " empexe_id, empexe_emp_id  "
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " inner join " + compdb(comp_id) + "axela_emp_exe on empexe_emp_id = emp_id"
					+ " where emp_id =" + emp_id + ""
					+ " ORDER BY empname";
			// SOP("Executives under him..." + StrSql);

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs1.getInt("empexe_id")).append("\">").append(crs1.getString("empname")).append("</a><br>");
				}
			} else {
				Str.append("---");
			}
			crs1.close();
			Str.append("</td>");
			Str.append("</tr>\n");

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
