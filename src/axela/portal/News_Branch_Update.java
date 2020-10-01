package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class News_Branch_Update extends Connect {

	public String add = "", emp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String comp_id = "0";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String news_id = "0";
	public String news_branch_id = "0";
	public String news_topic = "";
	public String news_desc = "";
	public String news_date = "", newsdate = "";
	public String news_featured = "";
	public String news_active = "1";
	public String news_entry_id = "0", entry_by = "";
	public String news_entry_date = "";
	public String news_modified_id = "0", modified_by = "";
	public String news_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_branch_news_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));

				// if (update.equals("yes")) {
				// if (news_id.equals("0")) {
				// response.sendRedirect(response.encodeRedirectURL("index.jsp"));
				// }
				// }
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"Add News".equals(addB)) {
					} else {
						CheckPerm(comp_id, "emp_branch_news_add", request, response);
						news_entry_id = emp_id;
						news_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("news-branch-list.jsp?news_branch_id=" + news_branch_id
									+ "&msg=News Added Successfully!" + msg + ""));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"Update News".equals(updateB)
							&& !"Delete News".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update News".equals(updateB)
							&& !"Delete News".equals(deleteB)) {
						CheckPerm(comp_id, "emp_branch_news_edit", request, response);
						news_modified_id = emp_id;
						news_modified_date = ToLongDate(kknow());
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("news-branch-list.jsp?news_id=" + news_id
									+ "&msg=News Updated Successfully!" + msg + ""));
						}
					} else if ("Delete News".equals(deleteB)) {
						CheckPerm(comp_id, "emp_branch_news_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("news-branch-list.jsp?msg=News Deleted Successfully!"));
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		news_branch_id = PadQuotes(request.getParameter("dr_branch"));
		news_topic = PadQuotes(request.getParameter("txt_branchnews_topic"));
		news_desc = PadQuotes(request.getParameter("txt_branchnews_desc"));
		news_date = PadQuotes(request.getParameter("txt_branchnews_date"));
		news_featured = CheckBoxValue(PadQuotes(request
				.getParameter("ch_branchnews_featured")));
		// if (news_featured.equals("on")) {
		// news_featured = "1";
		// } else {
		// news_featured = "0";
		// }
		news_active = CheckBoxValue(PadQuotes(request
				.getParameter("ch_branchnews_active")));
		// if (news_active.equals("on")) {
		// news_active = "1";
		// } else {
		// news_active = "0";
		// }
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (news_topic.equals("")) {
			msg = msg + "<br>Enter News Topic!";
		}
		if (news_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (news_date.equals("")) {
			msg = msg + "<br>Select Date!";
		}
		if (!news_date.equals("")) {
			if (isValidDateFormatShort(news_date)) {
				news_date = ConvertShortDateToStr(news_date);
				newsdate = strToShortDate(news_date);
			} else {
				msg = msg + "<br>Enter Valid news Date!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				news_id = ExecuteQuery("Select coalesce(max(news_id),0)+1 from " + compdb(comp_id) + "axela_news_branch");

				StrSql = "Insert into " + compdb(comp_id) + "axela_news_branch" + " (news_id,"
						+ " news_branch_id," + " news_topic," + " news_desc,"
						+ " news_date," + " news_featured," + " news_active,"
						+ " news_entry_id," + " news_entry_date)" + " values "
						+ " ('"
						+ news_id
						+ "',"
						+ " '"
						+ news_branch_id
						+ "',"
						+ " '"
						+ news_topic
						+ "',"
						+ " '"
						+ news_desc
						+ "',"
						+ " '"
						+ news_date
						+ "',"
						+ " '"
						+ news_featured
						+ "',"
						+ " '"
						+ news_active
						+ "',"
						+ " '"
						+ news_entry_id
						+ "',"
						+ " '" + news_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select news_branch_id, news_topic, news_topic,"
					+ " news_desc, news_date, news_featured, news_active,"
					+ " news_entry_id, news_entry_date, news_modified_id, news_modified_date"
					+ " from " + compdb(comp_id) + "axela_news_branch"
					+ " left join  " + compdb(comp_id) + "axela_branch on branch_id = news_branch_id"
					+ " where news_id = " + news_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					news_branch_id = crs.getString("news_branch_id");
					news_topic = crs.getString("news_topic");
					news_desc = crs.getString("news_desc");
					news_date = crs.getString("news_date");
					newsdate = strToShortDate(news_date);
					news_featured = crs.getString("news_featured");
					news_active = crs.getString("news_active");
					news_entry_id = crs.getString("news_entry_id");
					if (!news_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(news_entry_id));
						entry_date = strToLongDate(crs
								.getString("news_entry_date"));
					}
					news_modified_id = crs.getString("news_modified_id");
					if (!news_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer
								.parseInt(news_modified_id));
						modified_date = strToLongDate(crs
								.getString("news_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../portal/error.jsp?msg=Invalid News!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_news_branch" + " SET"
						+ " news_branch_id = '"
						+ news_branch_id
						+ "',"
						+ " news_topic = '"
						+ news_topic
						+ "',"
						+ " news_desc = '"
						+ news_desc
						+ "',"
						+ " news_date = '"
						+ news_date
						+ "',"
						+ " news_featured = '"
						+ news_featured
						+ "',"
						+ " news_active = '"
						+ news_active
						+ "',"
						+ " news_modified_id = '"
						+ news_modified_id
						+ "',"
						+ " news_modified_date = '"
						+ news_modified_date
						+ "'"
						+ " where news_id = " + news_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (news_id.equals("1")) {
			msg = msg + "<br>Cannot delete first record!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_news_branch" + " where news_id = "
						+ news_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}
}
