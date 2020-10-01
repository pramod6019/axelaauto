package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email_Contact_Send1 extends Connect {

	public String email_from = "";
	public String email_subject = "";
	public String email_msg = "";
	public String email_date = "";
	public String branch_id = "0";
	public String branchfilter = "";
	public String email_entry_id = "0";
	public String email_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String StrHTML = "";
	public String sendB = "";
	public String StrSql = "";
	public String status = "";
	public String BranchAccess = "";
	public String comp_email_enable = "";
	public String StrSearch = "";
	public String[] group_ids;
	public String group_id = "0";
	public String customer_id = "0";
	public String customer_contact_id = "0";
	public String sendto = "";
	public String email_allgroup = "";
	public String allconn = "";
	public String cont = "";
	public String contkey = "";
	public String smartcont = "";
	public String tag = "";
	public String smartcontkey = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_email_send", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				allconn = PadQuotes(request.getParameter("allconn"));
				cont = PadQuotes(request.getParameter("cont"));
				contkey = PadQuotes(request.getParameter("contkey"));
				smartcont = PadQuotes(request.getParameter("smartcont"));
				tag = PadQuotes(request.getParameter("tag"));
				smartcontkey = PadQuotes(request.getParameter("smartcontkey"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				customer_contact_id = CNumeric(PadQuotes(request.getParameter("customer_contact_id")));
				email_allgroup = PadQuotes(request.getParameter("chk_email_allgroup"));
				if (email_allgroup.equals("on")) {
					email_allgroup = "1";
				} else {
					email_allgroup = "0";
				}
				if (cont.equals("yes")) {
					sendto = "Contact";
				} else if (contkey.equals("yes")) {
					sendto = "Contact Key Persons";

				} else if (smartcont.equals("yes")) {
					sendto = "Contact";

				} else if (smartcontkey.equals("yes")) {
					sendto = "Contact Key Persons";
				} else {
					sendto = "Contact and Key Persons";
				}
				if ("yes".equals(smartcont)) {
					if (!GetSession("" + tag + "PrintSearchStr", request).equals("")) {
						StrSearch = StrSearch + GetSession("" + tag + "PrintSearchStr", request);
					}
				}
				if ("yes".equals(smartcontkey)) {
					if (!GetSession("PrintSearchkeyStr", request).equals("")) {
						StrSearch = StrSearch + GetSession("PrintSearchkeyStr", request);
					}
				}
				String SqlStr = "select comp_email_enable "
						+ " from " + compdb(comp_id) + "axela_comp "
						+ " where comp_active = '1'";
				CachedRowSet crs = processQuery(SqlStr, 0);
				while (crs.next()) {
					comp_email_enable = crs.getString("comp_email_enable");
				}
				crs.close();
				if (comp_email_enable.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				if (!branch_id.equals("0")) {
					branchfilter = " and student_branch_id = " + branch_id;
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				sendto = PadQuotes(request.getParameter("sendto"));
				msg = PadQuotes(request.getParameter("msg"));
				if (sendB.equals("Send")) {
					CheckPerm(comp_id, "emp_email_send", request, response);
					email_entry_id = CNumeric(GetSession("emp_id", request));
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						ListContacts();
					}
					if (msg.equals("")) {
						if (cont.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("email-contact-send.jsp?customer_id=" + customer_id + "&cont=yes&msg=Email sent successfully!"));
						} else if (contkey.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("email-contact-send.jsp?customer_customer_id=" + customer_contact_id + "&contkey=yes&msg=Email sent successfully!"));

						} else if (smartcont.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("email-contact-send.jsp?tag=" + tag + "&smartcont=yes&msg=Email sent successfully!"));

						} else if (smartcontkey.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("email-contact-send.jsp?smartcontkey=yes&msg=Email sent successfully!"));

						} else {
							response.sendRedirect(response.encodeRedirectURL("email-contact-send.jsp?chk_email_allgroup=on&allconn=yes&msg=Email sent successfully!"));
						}
					}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_id = PadQuotes(request.getParameter("customer_id"));
		email_subject = PadQuotes(request.getParameter("txt_email_subject"));
		email_msg = PadQuotes(request.getParameter("txt_email_msg"));
		email_date = (ToLongDate(kknow()));
		group_ids = request.getParameterValues("dr_group");
		email_allgroup = PadQuotes(request.getParameter("chk_email_allgroup"));
		if (email_allgroup.equals("on")) {
			email_allgroup = "1";
		} else {
			email_allgroup = "0";
		}
	}

	protected void CheckForm() {
		msg = "";
		if (customer_id.equals("") && cont.equals("yes")) {
			msg = msg + "<br>Enter Contact ID!";
		}
		if (customer_contact_id.equals("") && contkey.equals("yes")) {
			msg = msg + "<br>Enter Contact Key ID!";
		}
		if (allconn.equals("yes")) {
			if (email_allgroup.equals("0") && group_ids == null) {
				msg = msg + "<br>Select group!";
			}
		}
		if (group_ids != null) {
			group_id = "";
			for (int i = 0; i < group_ids.length; i++) {
				group_id = group_id + group_ids[i] + ",";
			}
			StrSearch = " and trans_group_id in (" + group_id.substring(0, group_id.lastIndexOf(",")) + ")";
		}
		if (email_msg.equals("")) {
			msg = msg + "<br>Enter Subject!";
		}
		if (email_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}
		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void ListContacts() {
		email_from = ExecuteQuery("select comp_email1 from " + compdb(comp_id) + "axela_comp");
		if (email_subject.length() > 1000) {
			email_subject = email_subject.substring(0, 1000);
		}
		if (email_msg.length() > 10000) {
			email_msg = email_msg.substring(0, 10000);
		}
		try {
			email_subject = "replace('" + email_subject + "', '[NAME]', customer_name)";
			email_msg = "replace('" + email_msg + "', '[NAME]', customer_name)";
			if (!customer_id.equals("") && cont.equals("yes")) {
				StrSql = "SELECT "
						+ "" + emp_id + ", "
						+ " '" + email_from + "', "
						+ " customer_email1, "
						+ " " + email_subject + ", "
						+ " " + email_msg + ", "
						+ " '" + ToLongDate(kknow()) + "', "
						+ " customer_id, "
						+ " " + email_entry_id + ", "
						+ " 0 "
						+ " from " + compdb(comp_id) + "axela_customer "
						+ " left  join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
						+ " inner join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
						+ " where customer_active = '1' and customer_email1!='' ";
				if (!customer_id.equals("")) {
					StrSearch = StrSearch + customer_id;
				}
				StrSql = StrSql + StrSearch;
				StrSql = StrSql + " group by customer_id order by customer_id desc ";
				AddFields(StrSql);
			}

			if (!customer_contact_id.equals("") && contkey.equals("yes") || smartcontkey.equals("yes")) {
				StrSql = "SELECT "
						+ " '" + email_from + "', "
						+ " customer_email1, "
						+ " " + email_subject + ", "
						+ " " + email_msg + ", "
						+ " '" + ToLongDate(kknow()) + "', "
						+ " customer_id, "
						+ " " + email_entry_id + ", "
						+ " 0 "
						+ " from " + compdb(comp_id) + "axela_customer_key "
						+ " left  join " + compdb(comp_id) + "axela_customer on customer_customer_id = customer_id "
						+ " inner join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
						+ " left  join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
						+ " where customer_active = '1' and customer_email1!='' ";
				if (!customer_contact_id.equals("")) {
					StrSearch = StrSearch + customer_contact_id;
				}
				StrSql = StrSql + StrSearch;
				StrSql = StrSql + " group by customer_id order by customer_id desc ";
				AddFields(StrSql);
			}
			if (allconn.equals("yes") || smartcont.equals("yes")) {
				StrSql = "SELECT "
						+ " '" + email_from + "', "
						+ " customer_email1, "
						+ " " + email_subject + ", "
						+ " " + email_msg + ", "
						+ " '" + ToLongDate(kknow()) + "', "
						+ " customer_id, "
						+ " " + email_entry_id + ", "
						+ " 0 "
						+ " from " + compdb(comp_id) + "axela_customer "
						+ " left  join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
						+ " inner join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
						+ " where customer_active = '1' and customer_email1!='' ";
				StrSql = StrSql + StrSearch;
				StrSql = StrSql + " group by customer_id order by customer_id desc ";
				AddFields(StrSql);

				StrSql = "SELECT "
						+ " '" + email_from + "', "
						+ " customer_email1, "
						+ " " + email_subject + ", "
						+ " " + email_msg + ", "
						+ " '" + ToLongDate(kknow()) + "', "
						+ " customer_id, "
						+ " " + email_entry_id + ", "
						+ " 0 "
						+ " from " + compdb(comp_id) + "axela_customer_key "
						+ " left  join " + compdb(comp_id) + "axela_customer on customer_customer_id = customer_id "
						+ " inner join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
						+ " left  join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
						+ " where customer_active = '1' and customer_active = '1' and customer_email1!='' ";
				StrSql = StrSql + StrSearch;
				StrSql = StrSql + " group by customer_id order by customer_id desc ";
				AddFields(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(String Sql) {
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_email "
						+ "("
						+ "email_emp_id, "
						+ "email_from, "
						+ "email_to, "
						+ "email_subject,"
						+ "email_msg, "
						+ "email_date, "
						+ "email_customer_id, "
						+ "email_entry_id,"
						+ "email_sent)"
						+ " " + Sql + "";
				email_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateGroup() {
		try {
			StrSql = " select group_id, concat(group_desc,'') as group_desc "
					+ " from " + compdb(comp_id) + "axela_customer_group "
					+ " where 1=1 order by group_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			StringBuilder Str = new StringBuilder();
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id")).append(ArrSelectdrop(crs.getInt("group_id"), group_ids)).append(">").append(crs.getString("group_desc"))
						.append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
