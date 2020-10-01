// smitha nag 19,20 march 2013
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Cust_Feedback extends Connect {

	public String StrHTML = "";
	public String StrSql = "", msg = "", chkpermmsg = "";
	public String deleteB = "";
	public String status = "";
	public String QueryString = "";
	public String BranchAccess, branch_id = "";
	public String testdrive_customer_id = "", customer_name = "", enquiry_no = "", testdrive_enquiry_id = "";
	public String testdrive_id = "", testdrive_emp_name = "", testdrive_emp_id = "";
	public String submitB = "";
	public int NoQtobAnswered = 5;
	public int NoQAnswered = 0;
	public String testdrive_client_fb_entry_id = "0";
	public String testdrive_client_fb_entry_date = "";
	public String testdrive_client_fb_modified_id = "0";
	public String testdrive_client_fb_modified_date = "";
	public String emp_id = "";
	public String comp_id = "0";
	public int total = 0;
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_custfb_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				submitB = PadQuotes(request.getParameter("submit_button"));
				QueryString = PadQuotes(request.getQueryString());
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdrive_id = PadQuotes(request.getParameter("testdrive_id"));

				EnquiryDetails(response);
				StrHTML = ListFeedback(request);

				if (submitB.equals("Submit")) {
					GetValues(request, response);
					if (status.equals("Add")) {
						if (ReturnPerm(comp_id, "emp_custfb_add", request).equals("1")) {
							testdrive_client_fb_entry_id = CNumeric(GetSession("emp_id", request));
							testdrive_client_fb_entry_date = ToLongDate(kknow());
							if (NoQAnswered < NoQtobAnswered) {
								msg = "Error!<br>Answer atleast " + NoQtobAnswered + " Questions!";
							}
							if (msg.equals("")) {
								AddFeedback(request);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else {
						if (ReturnPerm(comp_id, "emp_custfb_edit", request).equals("1")) {
							testdrive_client_fb_modified_id = CNumeric(GetSession("emp_id", request));
							testdrive_client_fb_modified_date = ToLongDate(kknow());
							if (NoQAnswered < NoQtobAnswered) {
								msg = "Error!<br>Answer atleast " + NoQtobAnswered + " Questions!";
							}
							if (msg.equals("")) {
								AddFeedback(request);
								response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("Delete Feedback".equals(deleteB)) {
					if (ReturnPerm(comp_id, "emp_custfb_delete", request).equals("1")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Feedback deleted successfully."));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	public String ListFeedback(HttpServletRequest request) {
		String answer = "", checked = "", points = "";
		int count = 0;
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "select group_id,group_name  "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_client_fb_group  "
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_client_fb on fb_group_id=group_id "
					+ " where 1=1 and fb_active='1' group by group_name order by group_rank";
			// SOP("StrSql ListFeedback --" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				while (crs1.next()) {
					count = 0;
					Str.append("<tr align=center>\n");
					Str.append("<thead text-align=\"center\"><b><font color=red>").append(crs1.getString("group_name")).append("</font></b></thead>\n");
					Str.append("</tr>");

					StrSql = "select fb_id, fb_query,fb_type, fb_option1, fb_option_points1, fb_option2, "
							+ " fb_option_points2, fb_option3, fb_option_points3, fb_option4, "
							+ " fb_option_points4, fb_option5, fb_option_points5, fbtrans_option1, "
							+ " fbtrans_option2, fbtrans_option3, fbtrans_option4, fbtrans_option5, "
							+ " coalesce(fbtrans_answer,'') as fbtrans_answer, COALESCE(fbtrans_points,'0') as fbtrans_points  "
							+ " from " + compdb(comp_id) + "axela_sales_testdrive_client_fb  "
							+ " left join " + compdb(comp_id) + "axela_sales_testdrive_client_fb_trans on fbtrans_fb_id=fb_id and fbtrans_testdrive_id=" + testdrive_id
							+ " where fb_active='1' and fb_group_id=" + crs1.getString("group_id")
							+ " group by fb_id "
							+ " order by fb_rank asc";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {

						count++;
						answer = "";
						points = "";
						Str.append("<tbody>\n");
						Str.append("<tr align=center>\n");
						Str.append("<td align=center width=5%>").append(count).append("</td>\n");
						Str.append("<td align=left>").append(crs.getString("fb_query")).append("</td>\n");
						Str.append("</tr>");
						Str.append("<tr align=center>\n");
						Str.append("<td align=left>&nbsp;</td><td align=left>\n");
						if (crs.getString("fb_type").equals("1")) {

							for (int i = 1; i <= 5; i++) {
								checked = "";
								if (!crs.getString("fb_option" + i + "").equals("")) {
									if (submitB.equals("")) {
										if (crs.getString("fbtrans_option" + i + "") != null && crs.getString("fbtrans_option" + i + "").equals("1")) {
											checked = "checked";
										}
									} else {
										checked = PadQuotes(request.getParameter("chk_option_" + crs.getString("fb_id")));
										// SOP("=======checked=======..."+checked);
										if (i == 1 && checked.equals(crs.getString("fb_option_points" + i + ""))) {
											checked = "checked";
											NoQAnswered++;
										} else if (i == 2 && checked.equals(crs.getString("fb_option_points" + i + ""))) {
											checked = "checked";
											NoQAnswered++;
										} else if (i == 3 && checked.equals(crs.getString("fb_option_points" + i + ""))) {
											checked = "checked";
											NoQAnswered++;
										} else if (i == 4 && checked.equals(crs.getString("fb_option_points" + i + ""))) {
											checked = "checked";
											NoQAnswered++;
										} else if (i == 5 && checked.equals(crs.getString("fb_option_points" + i + ""))) {
											checked = "checked";
											NoQAnswered++;
										}
									}
									Str.append("<input type=checkbox id=\"chk_option_").append(crs.getString("fb_id")).append("\" name=\"chk_option_")
											.append(crs.getString("fb_id"))
											.append("\" onClick=\"SingleSelect('chk','chk_option_").append(crs.getString("fb_id")).append("', this,'t").append(crs.getString("fb_id"))
											.append("');\" value=").append(crs.getString("fb_option_points" + i + "")).append(" ").append(checked).append(">");
									Str.append("&nbsp;").append(crs.getString("fb_option" + i + "")).append("&nbsp;");
								}
							}
							Str.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Points:<span id=\"t").append(crs.getString("fb_id")).append("\">")
									.append(crs.getString("fbtrans_points")).append("</span>");
							total = total + crs.getInt("fbtrans_points");
						} else {
							if (submitB.equals("")) {
								answer = crs.getString("fbtrans_answer");
								// points = crs.getString("fbtrans_points");
							} else {
								answer = PadQuotes(request.getParameter("txt_answer_" + crs.getString("fb_id")));
								if (!answer.equals("")) {
									NoQAnswered++;
								}
							}
							Str.append("<textarea name='txt_answer_").append(crs.getString("fb_id")).append("' cols='70' rows='5' class='form-control'  id ='txt_answer_")
									.append(crs.getString("fb_id"))
									.append("'>").append(answer).append("</textarea>");
						}
						Str.append("</td>");
						Str.append("</tr>");
						Str.append("</tbody>");
					}
					crs.close();
				}
				Str.append("</table>");
			} else {
				Str.append("<font color=red><b>No Questions(s) found!</b></font>");
			}
			crs1.close();
			// SOP("NoQAnswered at the last"+NoQAnswered);
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void AddFeedback(HttpServletRequest request) {
		if (msg.equals("")) {
			String checked = "", fb_option1 = "", fb_option2 = "", fb_option3 = "", fb_option4 = "", fb_option5 = "", fb_answer = "", fb_points = "0";
			try {
				StrSql = " delete from " + compdb(comp_id) + "axela_sales_testdrive_client_fb_trans where fbtrans_testdrive_id=" + testdrive_id + "";
				updateQuery(StrSql);

				StrSql = "select fb_type, fb_id , fb_option_points1, fb_option_points2, fb_option_points3, "
						+ " fb_option_points4, fb_option_points5 "
						+ " from " + compdb(comp_id) + "axela_sales_testdrive_client_fb_group  "
						+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_client_fb on fb_group_id=group_id "
						+ " where 1=1  "
						+ " order by group_rank, fb_rank  ";
				// SOP("StrSql in AddProductOptions : "+StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					StrSql = "";
					while (crs.next()) {
						checked = "";
						fb_option1 = "0";
						fb_option2 = "0";
						fb_option3 = "0";
						fb_option4 = "0";
						fb_option5 = "0";
						fb_answer = "";
						fb_points = "0";
						if (crs.getString("fb_type").equals("1")) {
							checked = PadQuotes(request.getParameter("chk_option_" + crs.getString("fb_id")));
							if (!checked.equals("")) {
								if (checked.equals(crs.getString("fb_option_points1"))) {
									fb_option1 = "1";
									fb_points = crs.getString("fb_option_points1");
								} else if (checked.equals(crs.getString("fb_option_points2"))) {
									fb_option2 = "1";
									fb_points = crs.getString("fb_option_points2");
								} else if (checked.equals(crs.getString("fb_option_points3"))) {
									fb_option3 = "1";
									fb_points = crs.getString("fb_option_points3");
								} else if (checked.equals(crs.getString("fb_option_points4"))) {
									fb_option4 = "1";
									fb_points = crs.getString("fb_option_points4");
								} else if (checked.equals(crs.getString("fb_option_points5"))) {
									fb_option5 = "1";
									fb_points = crs.getString("fb_option_points5");
								}
							}
						} else {
							fb_answer = PadQuotes(request.getParameter("txt_answer_" + crs.getString("fb_id")));
						}
						if (!checked.equals("") || !fb_answer.equals("")) {
							StrSql = StrSql + "("
									+ "" + testdrive_id + ", "
									+ " " + crs.getString("fb_id") + ", "
									+ " '" + fb_option1 + "', "
									+ " '" + fb_option2 + "', "
									+ " '" + fb_option3 + "', "
									+ " '" + fb_option4 + "', "
									+ " '" + fb_option5 + "', "
									+ "'" + fb_answer + "',"
									+ "'" + fb_points + "'"
									+ "),";
						}
					}
					if (!StrSql.equals("")) {
						StrSql = StrSql.substring(0, StrSql.lastIndexOf(","));
						// SOP("StrSql in AddProductOptions : " + StrSql);
						StrSql = "insert into " + compdb(comp_id) + "axela_sales_testdrive_client_fb_trans"
								+ " (fbtrans_testdrive_id , "
								+ " fbtrans_fb_id ,"
								+ " fbtrans_option1,"
								+ " fbtrans_option2,"
								+ " fbtrans_option3,"
								+ " fbtrans_option4,"
								+ " fbtrans_option5,"
								+ " fbtrans_answer,"
								+ " fbtrans_points"
								+ " ) values " + StrSql;
						// SOP("StrSql in insert query AddOptions : " + StrSql);
						updateQuery(StrSql);
						StrSql = "update " + compdb(comp_id) + "axela_sales_testdrive set "
								+ " testdrive_client_fb_entry_id = '" + testdrive_client_fb_entry_id + "', "
								+ " testdrive_client_fb_entry_date = '" + testdrive_client_fb_entry_date + "',"
								+ " testdrive_client_fb_modified_id = " + testdrive_client_fb_modified_id + ", "
								+ " testdrive_client_fb_modified_date = '" + testdrive_client_fb_modified_date + "' "
								+ " where testdrive_id = " + testdrive_id + " ";
						// SOP("StrSql in update query AddOptions : " + StrSql);
						updateQuery(StrSql);
					}
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "Select customer_id, customer_name, testdrive_emp_id,testdrive_enquiry_id,"
					+ " concat('ENQ',branch_code,enquiry_no) as enquiry_no,  "
					+ " customer_name, branch_code, testdrive_client_fb_entry_id, testdrive_client_fb_entry_date, "
					+ " testdrive_client_fb_modified_id, testdrive_client_fb_modified_date "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive "
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id=testdrive_enquiry_id  "
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id=enquiry_customer_id  "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id  "
					+ " where testdrive_id =" + testdrive_id + BranchAccess + "";
			// SOP("StrSql EnquiryDetails : " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_customer_id = crs.getString("customer_id");
					testdrive_enquiry_id = crs.getString("testdrive_enquiry_id");
					customer_name = crs.getString("customer_name");
					enquiry_no = crs.getString("enquiry_no");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					testdrive_emp_name = Exename(comp_id, crs.getInt("testdrive_emp_id"));
					if (crs.getString("testdrive_client_fb_entry_id").equals("0")) {
						status = "Add";
					} else {
						status = "Update";
					}
					testdrive_client_fb_entry_id = crs.getString("testdrive_client_fb_entry_id");
					testdrive_client_fb_entry_date = crs.getString("testdrive_client_fb_entry_date");
					testdrive_client_fb_modified_id = crs.getString("testdrive_client_fb_modified_id");
					testdrive_client_fb_modified_date = crs.getString("testdrive_client_fb_modified_date");
					// SOP("emo_client_fb_entry_id : " +
					// testdrive_client_fb_entry_id);
					if (submitB.equals("")) {
						if (!testdrive_client_fb_entry_id.equals("0")) {
							entry_by = Exename(comp_id, crs.getInt("testdrive_client_fb_entry_id"));
							entry_date = strToLongDate(crs.getString("testdrive_client_fb_entry_date"));
						}
						if (!testdrive_client_fb_modified_id.equals("0")) {
							modified_by = Exename(comp_id, Integer.parseInt(testdrive_client_fb_modified_id));
							modified_date = strToLongDate(crs.getString("testdrive_client_fb_modified_date"));
						}
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=No Enquiry found!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_client_fb_trans where fbtrans_testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
				StrSql = "update " + compdb(comp_id) + "axela_sales_testdrive set  testdrive_client_fb_entry_id = 0 ,  "
						+ " testdrive_client_fb_entry_date = '' , "
						+ " testdrive_client_fb_modified_id = 0 ,  "
						+ " testdrive_client_fb_modified_date = ''  "
						+ " where testdrive_id =" + testdrive_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
