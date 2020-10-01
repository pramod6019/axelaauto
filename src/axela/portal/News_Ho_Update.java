package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class News_Ho_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String news_id = "0";
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
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_ho_news_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
				// SOP("news_id==in do post=" + news_id);
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if (status.equals("Add")) {
					if (!"Add News".equals(addB)) {
					} else {
						if (ReturnPerm(comp_id, "emp_ho_news_add", request).equals("1")) {
							news_entry_id = CNumeric(GetSession("emp_id", request));
							news_entry_date = ToLongDate(kknow());
							news_modified_date = "";
							news_modified_id = "0";
							GetValues(request, response);
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("news-ho-list.jsp?news_id=" + news_id + "&msg=News added successfully!" + msg + ""));
							}
						} else {
							msg = "msg=Access denied. Please contact system administrator!";
							response.sendRedirect("error.jsp?" + msg);
						}
					}
				}
				if (status.equals("Update")) {
					if (!"Update News".equals(updateB) && !"Delete News".equals(deleteB)) {
						PopulateFields(response);

					} else if ("Update News".equals(updateB) && !"Delete News".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_ho_news_edit", request).equals("1")) {
							news_modified_id = CNumeric(GetSession("emp_id", request));
							news_modified_date = ToLongDate(kknow());
							GetValues(request, response);
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("news-ho-list.jsp?news_id=" + news_id + "&msg=News updated successfully!" + msg + ""));
							}
						} else {
							msg = "msg=Access denied. Please contact system administrator!";
							response.sendRedirect("error.jsp?" + msg);
						}
					} else if ("Delete News".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_ho_news_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("news-ho-list.jsp?msg=News deleted successfully!"));
							}
						} else {
							msg = "msg=Access denied. Please contact system administrator!";
							response.sendRedirect("error.jsp?" + msg);
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
		news_topic = PadQuotes(request.getParameter("txt_news_topic"));
		news_desc = PadQuotes(request.getParameter("txt_news_desc"));
		news_date = PadQuotes(request.getParameter("txt_news_date"));
		news_featured = CheckNull(PadQuotes(request.getParameter("ch_news_featured")));
		news_active = CheckNull(request.getParameter("ch_news_active"));
		if (news_featured.equals("on")) {
			news_featured = "1";
		} else {
			news_featured = "0";
		}
		if (news_active.equals("")) {
			news_active = "0";
		} else {
			news_active = "1";
		}
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
				news_id = CNumeric(PadQuotes(ExecuteQuery("Select max(news_id) as ID from " + compdb(comp_id) + "axela_news_ho")));
				int honews_idi = Integer.parseInt(news_id) + 1;
				news_id = "" + honews_idi;

				StrSql = "insert into " + compdb(comp_id) + "axela_news_ho"
						+ "(news_id , "
						+ "news_topic,"
						+ "news_desc,"
						+ "news_date,"
						+ "news_featured,"
						+ "news_active,"
						+ "news_entry_id,"
						+ "news_entry_date,"
						+ "news_modified_id,"
						+ "news_modified_date) "
						+ "values "
						+ "('" + news_id + "'," // news_id
						+ "'" + news_topic + "',"// news_topic
						+ "'" + news_desc + "',"// news_desc
						+ "'" + news_date + "',"// news_date
						+ "'" + news_featured + "',"// news_featured
						+ "'" + news_active + "',"// news_active
						+ "'" + news_entry_id + "',"// news_entry_id
						+ "'" + news_entry_date + "',"// news_entry_date
						+ "'0'," // news_modified_id
						+ "'')";// news_modified_date
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select * from " + compdb(comp_id) + "axela_news_ho where  news_id=" + news_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					news_id = crs.getString("news_id");
					news_topic = crs.getString("news_topic");
					news_desc = crs.getString("news_desc");
					news_date = crs.getString("news_date");
					newsdate = strToShortDate(news_date);
					news_featured = crs.getString("news_featured");
					news_active = crs.getString("news_active");
					news_entry_id = crs.getString("news_entry_id");
					if (!news_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(news_entry_id));
					}
					entry_date = strToLongDate(crs.getString("news_entry_date"));
					news_modified_id = crs.getString("news_modified_id");
					if (!news_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(news_modified_id));
						modified_date = strToLongDate(crs.getString("news_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid News!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = " UPDATE " + compdb(comp_id) + "axela_news_ho SET "
						+ "news_topic  ='" + news_topic + "',"
						+ "news_desc  ='" + news_desc + "',"
						+ "news_date  ='" + news_date + "',"
						+ "news_featured  ='" + news_featured + "',"
						+ "news_active  ='" + news_active + "',"
						+ "news_modified_id  ='" + news_modified_id + "',"
						+ "news_modified_date  ='" + news_modified_date + "' "
						+ "where news_id = " + news_id + " ";
				// SOP("strsql===" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (news_id.equals("1")) {
			msg = msg + "<br>Cannot delete first record!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_news_ho where  news_id =" + news_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in:  " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
