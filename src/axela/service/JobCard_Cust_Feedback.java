// smitha nag 25 march 2013
// modified by sn, 31 may 2013
package axela.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Cust_Feedback extends Connect {

	public String StrHTML = "";
	public String StrSql = "", msg = "";
	public String deleteB = "";
	public String status = "";
	public String QueryString = "";
	public String BranchAccess, branch_id = "";
	public String ExeAccess = "";
	public String contact_name = "";
	public String jc_contact_id = "";
	public String veh_reg_no = "";
	public String veh_variant_id = "";
	public String variant_name = "";
	public String jc_veh_id = "";
	public String jc_customer_id = "", customer_name = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_email2 = "";
	public String jc_time_in = "", jc_time_out = "";
	public String jc_id = "", jc_emp_name = "", jc_emp_id = "";
	public String submitB = "";
	public int NoQtobAnswered = 5;
	public int NoQAnswered = 0;
	public String jc_fb_entry_id = "0";
	public String jc_fb_entry_date = "";
	public String jc_fb_modified_id = "0";
	public String jc_fb_modified_date = "";
	public String emp_id = "", comp_id = "", emp_mobile1 = "", emp_email1 = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public int total = 0;
	// email
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public String config_sms_enable = "";
	public String comp_sms_enable = "";
	public String branch_email1 = "";
	public String branch_jc_feedback_email_enable = "";
	public String branch_jc_feedback_email_sub = "";
	public String branch_jc_feedback_email_format = "";
	public String branch_jc_feedback_sms_enable = "";
	public String branch_jc_feedback_sms_format = "";

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
				ExeAccess = GetSession("ExeAccess", request);
				submitB = PadQuotes(request.getParameter("submit_button"));
				QueryString = PadQuotes(request.getQueryString());
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				jc_id = PadQuotes(request.getParameter("jc_id"));

				JCDetails(response);
				StrHTML = ListFeedback(request);

				if (submitB.equals("Submit")) {
					if (status.equals("Add")) {
						CheckPerm(comp_id, "emp_custfb_add", request, response);
						jc_fb_entry_id = CNumeric(GetSession("emp_id", request));
						jc_fb_entry_date = ToLongDate(kknow());
					} else {
						CheckPerm(comp_id, "emp_custfb_edit", request, response);
						jc_fb_modified_id = CNumeric(GetSession("emp_id", request));
						jc_fb_modified_date = ToLongDate(kknow());
					}
					if (NoQAnswered < NoQtobAnswered) {
						msg = "Error!<br>Answer atleast " + NoQtobAnswered + " Questions!";
					}
					if (msg.equals("")) {
						String feedbackmsg = "";
						AddFeedback(request);
						if (status.equals("Add")) {
							PopulateConfigDetails();
							if (comp_email_enable.equals("1")
									&& config_email_enable.equals("1")
									&& !branch_email1.equals("")
									&& branch_jc_feedback_email_enable.equals("1")
									&& !branch_jc_feedback_email_format.equals("")
									&& !branch_jc_feedback_email_sub.equals("")
									&& !contact_email1.equals("")) {
								SendEmail();
							}
							if (comp_sms_enable.equals("1")
									&& config_sms_enable.equals("1")
									&& branch_jc_feedback_sms_enable.equals("1")
									&& !branch_jc_feedback_sms_format.equals("")) {
								if (!contact_mobile1.equals("")) {
									SendSMS(contact_mobile1);
								}
								if (!contact_mobile2.equals("")) {
									SendSMS(contact_mobile2);
								}
							}
						}
						if (status.equals("Add")) {
							feedbackmsg = "Feedback Added Successfully";
						}
						if (status.equals("Update")) {
							feedbackmsg = "Feedback Updated Successfully";
						}
						response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=" + feedbackmsg + ""));
					}
				} else if ("Delete Feedback".equals(deleteB)) {
					CheckPerm(comp_id, "emp_jc_delete", request, response);
					DeleteFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Feedback deleted successfully."));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListFeedback(HttpServletRequest request) {
		String answer = "", checked = "";
		int count = 0;
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT group_id, group_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_fb_group"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_fb ON fb_group_id = group_id"
					+ " WHERE fb_active = '1'"
					+ " GROUP BY group_name"
					+ " ORDER BY group_rank";
			// SOP("StrSql ListFeedback --" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs1.next()) {
					count = 0;
					Str.append("<tr align=center>\n");
					Str.append("<td colspan=2 align=left><b><font color=red>").append(crs1.getString("group_name")).append("</font></b></td>\n");
					Str.append("</tr>");

					StrSql = "SELECT fb_id, fb_query,fb_type, fb_option1, fb_option_points1, fb_option2,"
							+ " fb_option_points2, fb_option3, fb_option_points3, fb_option4,"
							+ " fb_option_points4, fb_option5, fb_option_points5, fbtrans_option1,"
							+ " fbtrans_option2, fbtrans_option3, fbtrans_option4, fbtrans_option5,"
							+ " COALESCE(fbtrans_answer, '') AS fbtrans_answer, COALESCE(fbtrans_points, '0') AS fbtrans_points"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_fb"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_fb_trans ON fbtrans_fb_id = fb_id"
							+ " AND fbtrans_jc_id = " + jc_id
							+ " WHERE fb_active = '1'"
							+ " AND fb_group_id = " + crs1.getString("group_id")
							+ " GROUP BY fb_id"
							+ " ORDER BY fb_rank ASC";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						count++;
						answer = "";
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
									Str.append("<input type=checkbox id=\"chk_option_").append(crs.getString("fb_id"));
									Str.append("\" name=\"chk_option_").append(crs.getString("fb_id"));
									Str.append("\" onClick=\"SingleSelect('chk','chk_option_").append(crs.getString("fb_id"));
									Str.append("', this,'t").append(crs.getString("fb_id")).append("');\" value=");
									Str.append(crs.getString("fb_option_points" + i + "")).append(" ").append(checked).append(">");
									Str.append("&nbsp;").append(crs.getString("fb_option" + i + "")).append("&nbsp;");
								}
							}
							Str.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Points:<span id=\"t").append(crs.getString("fb_id")).append("\">")
									.append(crs.getString("fbtrans_points")).append("</span>");
							total = total + crs.getInt("fbtrans_points");
						} else {
							if (submitB.equals("")) {
								answer = crs.getString("fbtrans_answer");
							} else {
								answer = PadQuotes(request.getParameter("txt_answer_" + crs.getString("fb_id")));
								if (!answer.equals("")) {
									NoQAnswered++;
								}
							}
							Str.append("<textarea name='txt_answer_").append(crs.getString("fb_id")).append("' cols='70' rows='5' class='textbox'  id = 'txt_answer_").append(crs.getString("fb_id"))
									.append("'>").append(answer).append("</textarea>");
						}
						Str.append("</td>");
						Str.append("</tr>");
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
				StrSql = " DELETE FROM " + compdb(comp_id) + "axela_service_jc_fb_trans"
						+ " WHERE fbtrans_jc_id = " + jc_id + "";
				updateQuery(StrSql);

				StrSql = "SELECT fb_type,fb_id, fb_option_points1, fb_option_points2,"
						+ " fb_option_points3, fb_option_points4, fb_option_points5"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_fb_group"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_fb ON fb_group_id = group_id"
						+ " ORDER BY group_rank, fb_rank";
				// SOP("StrSql === : "+StrSql);
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
							StrSql = StrSql + "(" + jc_id + ", "
									+ " " + crs.getString("fb_id") + ", "
									+ " '" + fb_option1 + "', "
									+ " '" + fb_option2 + "', "
									+ " '" + fb_option3 + "', "
									+ " '" + fb_option4 + "', "
									+ " '" + fb_option5 + "', "
									+ "'" + fb_answer + "',"
									+ "'" + fb_points + "'),";
						}
					}
					if (!StrSql.equals("")) {
						StrSql = StrSql.substring(0, StrSql.lastIndexOf(","));
						// SOP("StrSql ====== : " + StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_fb_trans"
								+ " (fbtrans_jc_id,"
								+ " fbtrans_fb_id,"
								+ " fbtrans_option1,"
								+ " fbtrans_option2,"
								+ " fbtrans_option3,"
								+ " fbtrans_option4,"
								+ " fbtrans_option5,"
								+ " fbtrans_answer,"
								+ " fbtrans_points)"
								+ " VALUES " + StrSql;
						// SOP("StrSql in insert query AddOptions : " + StrSql);
						updateQuery(StrSql);
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
								+ " SET"
								+ " jc_fb_entry_id = '" + jc_fb_entry_id + "',"
								+ " jc_fb_entry_date = '" + jc_fb_entry_date + "',"
								+ " jc_fb_modified_id = " + jc_fb_modified_id + ","
								+ " jc_fb_modified_date = '" + jc_fb_modified_date + "'"
								+ " WHERE jc_id = " + jc_id + "";
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

	protected void JCDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT customer_id, customer_name, jc_emp_id, customer_name, branch_code,"
					+ " jc_fb_entry_id, jc_fb_entry_date, jc_fb_modified_id, jc_fb_modified_date,"
					+ " contact_id, jc_time_in, jc_time_out, veh_reg_no, veh_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name) AS variant_name,"
					+ " variant_id, emp_mobile1, emp_email1, contact_mobile1, contact_email1, contact_email2"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN  axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " WHERE jc_id = " + jc_id + BranchAccess + "";
			// SOP("JCDetails query:-" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					jc_customer_id = crs.getString("customer_id");
					jc_contact_id = crs.getString("contact_id");
					customer_name = crs.getString("customer_name");
					contact_name = crs.getString("contact_name");
					jc_veh_id = crs.getString("veh_id");
					jc_time_in = strToLongDate(crs.getString("jc_time_in"));
					jc_time_out = strToLongDate(crs.getString("jc_time_out"));
					veh_reg_no = crs.getString("veh_reg_no");
					veh_variant_id = crs.getString("variant_id");
					variant_name = crs.getString("variant_name");
					jc_emp_id = crs.getString("jc_emp_id");
					emp_mobile1 = crs.getString("emp_mobile1");
					emp_email1 = crs.getString("emp_email1");
					jc_emp_name = Exename(comp_id, crs.getInt("jc_emp_id"));
					if (crs.getString("jc_fb_entry_id").equals("0")) {
						status = "Add";
					} else {
						status = "Update";
					}
					jc_fb_entry_id = crs.getString("jc_fb_entry_id");
					jc_fb_entry_date = crs.getString("jc_fb_entry_date");
					jc_fb_modified_id = crs.getString("jc_fb_modified_id");
					jc_fb_modified_date = crs.getString("jc_fb_modified_date");
					if (submitB.equals("")) {
						if (!jc_fb_entry_id.equals("0")) {
							entry_by = Exename(comp_id, crs.getInt("jc_fb_entry_id"));
							entry_date = strToLongDate(crs.getString("jc_fb_entry_date"));
						}
						if (!jc_fb_modified_id.equals("0")) {
							modified_by = Exename(comp_id, Integer.parseInt(jc_fb_modified_id));
							modified_date = strToLongDate(crs.getString("jc_fb_modified_date"));
						}
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Invalid Job Card!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_fb_trans"
						+ " WHERE fbtrans_jc_id = " + jc_id + "";
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_fb_entry_id = 0,"
						+ " jc_fb_entry_date = '',"
						+ " jc_fb_modified_id = 0,"
						+ " jc_fb_modified_date = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void PopulateConfigDetails() {
		try {
			StrSql = "SELECT jc_branch_id, jc_contact_id, contact_lname, contact_fname,"
					+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2,"
					+ " COALESCE(branch_jc_feedback_email_enable, '') AS branch_jc_feedback_email_enable,"
					+ " COALESCE(branch_jc_feedback_email_sub, '') AS branch_jc_feedback_email_sub,"
					+ " COALESCE(branch_jc_feedback_email_format, '') AS branch_jc_feedback_email_format,"
					+ " COALESCE(branch_jc_feedback_sms_enable, '') AS branch_jc_feedback_sms_enable,"
					+ " COALESCE(branch_jc_feedback_sms_format, '') AS branch_jc_feedback_sms_format,"
					+ " COALESCE(branch_email1, '') AS branch_email1,"
					+ " config_email_enable, comp_email_enable,"
					+ " comp_sms_enable, config_sms_enable"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + emp_id + ","
					+ " " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
					+ " WHERE jc_id = " + jc_id + ExeAccess.replace("emp_id", "jc_emp_id") + BranchAccess;
			// SOP("PopulateConfigDetails = " + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				jc_contact_id = crs.getString("jc_contact_id");
				contact_name = crs.getString("contact_fname") + " " + crs.getString("contact_lname");
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_mobile2 = crs.getString("contact_mobile2");
				branch_jc_feedback_email_enable = crs.getString("branch_jc_feedback_email_enable");
				branch_jc_feedback_email_sub = crs.getString("branch_jc_feedback_email_sub");
				branch_jc_feedback_email_format = crs.getString("branch_jc_feedback_email_format");
				branch_jc_feedback_sms_enable = crs.getString("branch_jc_feedback_sms_enable");
				branch_jc_feedback_sms_format = crs.getString("branch_jc_feedback_sms_format");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmail() {
		String msg = "", sub = "", contact_email_foremail = "";
		if (!contact_email2.equals("")) {
			contact_email_foremail = contact_email1 + "," + contact_email2;
		} else {
			contact_email_foremail = contact_email1;
		}
		// msg = (branch_jc_feedback_email_format);
		// sub = (branch_jc_feedback_email_sub);

		sub = "REPLACE(branch_jc_feedback_email_sub, '[JOBCARDID]', jc_id)";
		sub = "REPLACE(" + sub + ", '[CUSTOMERID]', customer_id)";
		sub = "REPLACE(" + sub + ", '[CUSTOMERNAME]', customer_name)";
		sub = "REPLACE(" + sub + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		sub = "REPLACE(" + sub + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		sub = "REPLACE(" + sub + ", '[CONTACTMOBILE1]', contact_mobile1)";
		sub = "REPLACE(" + sub + ", '[CONTACTPHONE1]', contact_phone1)";
		sub = "REPLACE(" + sub + ", '[CONTACTEMAIL1]', contact_email1)";
		sub = "REPLACE(" + sub + ", '[EXENAME]', emp_name)";
		sub = "REPLACE(" + sub + ", '[EXEJOBTITLE]', jobtitle_desc)";
		sub = "REPLACE(" + sub + ", '[EXEMOBILE1]', emp_mobile1)";
		sub = "REPLACE(" + sub + ", '[EXEPHONE1]', emp_phone1)";
		sub = "REPLACE(" + sub + ", '[EXEEMAIL1]', emp_email1)";
		sub = "REPLACE(" + sub + ", '[MODELNAME]', preownedmodel_name)";
		sub = "REPLACE(" + sub + ", '[ITEMNAME]', variant_name)";
		sub = "REPLACE(" + sub + ", '[REGNO]', veh_reg_no)";

		msg = "REPLACE(branch_jc_feedback_email_format, '[JOBCARDID]', jc_id)";
		msg = "REPLACE(" + msg + ", '[CUSTOMERID]', customer_id)";
		msg = "REPLACE(" + msg + ", '[CUSTOMERNAME]', customer_name)";
		msg = "REPLACE(" + msg + ", '[CONTACTNAME]', CONCAT(contact_fname,' ', contact_lname))";
		msg = "REPLACE(" + msg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		msg = "REPLACE(" + msg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		msg = "REPLACE(" + msg + ", '[CONTACTPHONE1]', contact_phone1)";
		msg = "REPLACE(" + msg + ", '[CONTACTEMAIL1]', contact_email1)";
		msg = "REPLACE(" + msg + ", '[EXENAME]', emp_name)";
		msg = "REPLACE(" + msg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		msg = "REPLACE(" + msg + ", '[EXEMOBILE1]', emp_mobile1)";
		msg = "REPLACE(" + msg + ", '[EXEPHONE1]', emp_phone1)";
		msg = "REPLACE(" + msg + ", '[EXEEMAIL1]', emp_email1)";
		msg = "REPLACE(" + msg + ", '[MODELNAME]', preownedmodel_name)";
		msg = "REPLACE(" + msg + ", '[ITEMNAME]', variant_name)";
		msg = "REPLACE(" + msg + ", '[REGNO]', veh_reg_no)";
		try {
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + jc_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + contact_email_foremail + "',"
					+ " " + sub + ","
					+ " " + msg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id= veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE jc_id = " + jc_id + "";
			// SOP("SEND EMAIL QUERY  " + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS(String contact_mobile) {
		String smsmsg = "";

		smsmsg = "REPLACE(branch_jc_feedback_sms_format, '[JOBCARDID]', jc_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]', customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]', customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]', contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]', contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXENAME]', emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEMOBILE1]', emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEPHONE1]', emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEEMAIL1]', emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[MODELNAME]', preownedmodel_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[ITEMNAME]', variant_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[REGNO]', veh_reg_no)";
		try {
			StrSql = "SELECT"
					+ " " + jc_contact_id + ","
					+ " CONCAT(contact_fname, ' ', contact_lname),"
					+ " '" + contact_mobile + "',"
					+ " " + smsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE jc_id = " + jc_id + "";
			// SOP("select StrSql-sms-"+StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP(" insert StrSql-sms-"+StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
