package axela.portal;

/** @saiman 21st June 2012 */
///sarvana 10th sept
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Email_Send extends Connect {

	public String email_from = "";
	public String email_customer = "";
	public String email_contact = "";
	public String comp_id = "0";
	public String email_subject = "";
	public String email_msg = "";
	public String email_date = "";
	public String target = "";
	public String contact_id = "0";
	public String contact_fname = "";
	public String customer_id = "0";
	public String customer_branch_id = "0";
	public String branch_id = "0";
	public String branchfilter = "";
	public String email_entry_id = "0";
	public String email_id = "", emp_id = "0";
	public String msg = "";
	public String StrHTML = "";
	public String sendB = "";
	public String StrSql = "";
	public String status = "";
	public String linkheader = "";
	public String BranchAccess = "";
	public String comp_email_enable = "";
	public String sendto = "";
	public String StrSearch = "";
	public String smart = "";
	public String SmartSearch = "";
	public Connection conntx = null;
	public Statement stmttx = null;

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
				target = PadQuotes(request.getParameter("target"));
				msg = PadQuotes(request.getParameter("msg"));
				sendB = PadQuotes(request.getParameter("send_button"));
				smart = PadQuotes(request.getParameter("smart"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));

				if (!branch_id.equals("0")) {
					branchfilter = " and customer_branch_id = " + branch_id;
				}
				if (!target.equals("")) {
					linkheader = "<a href=\"home.jsp\">Home</a> &gt; <a href=\"email.jsp\">Email</a> &gt; <a href=\"email-send.jsp?target=" + target + "\">Send Email</a>:";
				} else if (!contact_id.equals("")) {
					linkheader = "<a href=\"home.jsp\">Home</a> &gt; <a href=\"email.jsp\">Email</a> &gt; <a href=\"email-send.jsp?contact_id=" + contact_id + "\">Send Email</a>:";
				}
				contact_fname = ExecuteQuery("Select contact_fname"
						+ " from " + compdb(comp_id) + "axela_customer_contact"
						+ " where contact_id = " + CNumeric(contact_id));
				if (contact_fname.equals("") && !contact_id.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact"));
				}
				if (target.equals("1")) {
					status = " to All Contacts";
				} else if (target.equals("2")) {
					status = " to All Customers";
				} else if (target.equals("3")) {
					status = " to All Suppliers";
				}
				GetValues(request, response);
				if (sendB.equals("Send")) {
					CheckPerm(comp_id, "emp_email_send", request, response);
					email_entry_id = CNumeric(GetSession("emp_id", request));
					CheckForm();
					if (smart.equals("yes")) {
						SmartSearch = SmartSearch + GetSession("contactstrsql", request);
					}
					if (msg.equals("")) {
						Format();
					}
					if (msg.equals("")) {
						if (smart.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("email-send.jsp?smart=yes"));
						} else if (target.equals("1") || target.equals("2") || target.equals("3")) {
							if (target.equals("1")) {
								msg = "Email Sent To All Contacts Successfully!";
							} else if (target.equals("2")) {
								msg = "Email Sent To All Customers Successfully!";
							} else {
								msg = "Email Sent To All Suppliers Successfully!";
							}
							response.sendRedirect(response.encodeRedirectURL("email-list.jsp?&msg=" + msg + ""));
						} else {
							response.sendRedirect(response.encodeRedirectURL("email-list.jsp?email_id=" + email_id + "&msg=Email Sent Successfully!"));
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
		if (branch_id.equals("0")) {
			customer_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			customer_branch_id = branch_id;
		}
		email_id = CNumeric(PadQuotes(request.getParameter("email_id")));
		email_subject = PadQuotes(request.getParameter("txt_email_subject"));
		email_msg = PadQuotes(request.getParameter("txt_email_msg"));
		email_date = (ToLongDate(kknow()));
		smart = PadQuotes(request.getParameter("smart"));
	}

	protected void CheckForm() {
		msg = "";
		if (smart.equals("")) {
			if (customer_id.equals("") && customer_branch_id.equals("")) {
				msg = "<br>Enter Customer ID!";
			}
			if (!customer_id.equals("") && isNumeric(customer_id)) {
				StrSql = "select count(customer_id) from " + compdb(comp_id) + "axela_customer "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
						+ " where customer_active='1' "
						+ " and customer_id=" + customer_id + " " + branchfilter + BranchAccess;
			}
			if (contact_id.equals("") && customer_branch_id.equals("")) {
				msg = "<br>Enter Contact ID!";
			}
			if (!contact_id.equals("") && isNumeric(contact_id)) {
				StrSql = "select count(contact_id) from " + compdb(comp_id) + "axela_customer_contact"
						+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
						+ " where contact_active='1' "
						+ " and contact_id=" + contact_id + " " + branchfilter + BranchAccess;
				email_contact = ExecuteQuery(StrSql);
			}
			if (email_subject.equals("")) {
				msg = msg + "<br>Enter Subject!";
			}
			if (email_msg.equals("")) {
				msg = msg + "<br>Enter Message!";
			}
			if (!msg.equals("")) {
				msg = "Error!" + msg;
			}
		}
	}

	public void Format() {
		StrSearch = "";
		String StrTo = "";
		email_from = ExecuteQuery("select config_admin_email from " + compdb(comp_id) + "axela_config");
		if (email_subject.length() > 1000) {
			email_subject = email_subject.substring(0, 1000);
		}
		if (email_msg.length() > 10000) {
			email_msg = email_msg.substring(0, 10000);
		}
		if (target.equals("1")) {
			StrTo = "contact_email1";
		} else if (target.equals("2")) {
			StrTo = "contact_email1";
		} else if (target.equals("3")) {
			StrTo = "contact_email1";
		} else if (!contact_id.equals("0")) {
			StrTo = "contact_email1";
		}
		try {
			email_subject = "replace('" + email_subject + "','[CUSTOMERNAME]',customer_name)";
			email_subject = "replace(" + email_subject + ",'[CUSTOMERID]',customer_id)";
			email_subject = "replace(" + email_subject + ",'[CONTACTNAME]',contact_fname)";
			email_subject = "replace(" + email_subject + ",'[CONTACTID]',contact_id)";
			email_subject = "replace(" + email_subject + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			email_subject = "replace(" + email_subject + ",'[CONTACTMOBILE1]',contact_mobile1)";
			email_subject = "replace(" + email_subject + ",'[CONTACTMOBILE2]',contact_mobile2)";
			email_subject = "replace(" + email_subject + ",'[CONTACTPHONE1]',contact_phone1)";
			email_subject = "replace(" + email_subject + ",'[CONTACTPHONE2]',contact_phone2)";
			email_subject = "replace(" + email_subject + ",'[CONTACTEMAIL1]',contact_email1)";
			email_subject = "replace(" + email_subject + ",'[CONTACTEMAIL2]',contact_email2)";

			email_msg = "replace('" + email_msg + "','[CUSTOMERNAME]',customer_name)";
			email_msg = "replace(" + email_msg + ",'[CUSTOMERID]',customer_id)";
			email_msg = "replace(" + email_msg + ",'[CONTACTNAME]',contact_fname)";
			email_msg = "replace(" + email_msg + ",'[CONTACTID]',contact_id)";
			email_msg = "replace(" + email_msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			email_msg = "replace(" + email_msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
			email_msg = "replace(" + email_msg + ",'[CONTACTMOBILE2]',contact_mobile2)";
			email_msg = "replace(" + email_msg + ",'[CONTACTPHONE1]',contact_phone1)";
			email_msg = "replace(" + email_msg + ",'[CONTACTPHONE2]',contact_phone2)";
			email_msg = "replace(" + email_msg + ",'[CONTACTEMAIL1]',contact_email1)";
			email_msg = "replace(" + email_msg + ",'[CONTACTEMAIL2]',contact_email2)";

			StrSql = "SELECT"
					+ " '" + email_from + "',"
					+ " " + StrTo + ","
					+ " " + email_subject + ","
					+ " " + email_msg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " contact_id,"
					+ " " + email_entry_id + ","
					+ " 0"
					+ " from " + compdb(comp_id) + "axela_customer_contact"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id";

			if (target.equals("2")) {
				StrSql = StrSql;
			} else if (target.equals("3")) {
				StrSql = StrSql + " inner join " + compdb(comp_id)
						+ "axela_acc_voucher on voucher_customer_id =contact_id "
						+ "	INNER JOIN axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
						+ "	AND voucher_vouchertype_id = 12";
			}
			if (!contact_id.equals("0")) {
				StrSql = StrSql
						+ " WHERE contact_active = '1' "
						+ " AND contact_id = " + contact_id
						+ " AND contact_email1 != ''"
						+ BranchAccess;
			} else {
				StrSql = StrSql + " WHERE contact_active = '1'  AND contact_email1 != '' " + BranchAccess;
			}
			if (!smart.equals("")) {
				StrSql = StrSql + SmartSearch;
			}
			if (!branch_id.equals("0")) {
				StrSearch = StrSearch + " and customer_branch_id = " + customer_branch_id;
			}
			if (branch_id.equals("0") && !customer_branch_id.equals("0") && !customer_branch_id.equals("")) {
				StrSearch = StrSearch + " and customer_branch_id = " + customer_branch_id;
			}
			if (target.equals("2")) {
				StrSearch = StrSearch + " ";
			} else if (target.equals("3")) {
				StrSearch = StrSearch + " ";
			}
			StrSql = StrSql + StrSearch;
			if (target.equals("1")) {
				StrSql = StrSql + " group by contact_id order by contact_id desc ";
			} else if (target.equals("2")) {
				StrSql = StrSql + " group by contact_id order by contact_id desc ";
			} else if (target.equals("3")) {
				StrSql = StrSql + " group by contact_id order by contact_id desc ";
			}
			AddFields(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(String Sql) throws SQLException {
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
						+ " ("
						+ " email_from,"
						+ " email_to,"
						+ " email_subject,"
						+ " email_msg,"
						+ " email_date,"
						+ " email_contact_id,"
						+ " email_entry_id,"
						+ " email_sent)"
						+ " " + Sql + "";
				SOP("sql--" + StrSql);
				if (target.equals("1") || target.equals("2") || target.equals("3") || !contact_id.equals("0")) {
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						email_id = rs.getString(1);
					}
				}
				conntx.commit();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...\n sql--" + StrSql);
				}
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
	}

	public String PopulateSendTo() {
		String Srt = "";
		Srt = Srt + "<option value=1" + StrSelectdrop("1", target) + ">All Contacts</option>\n";
		Srt = Srt + "<option value=2" + StrSelectdrop("2", target) + ">All Customers</option>\n";
		Srt = Srt + "<option value=3" + StrSelectdrop("3", target) + ">All Suppliers</option>\n";
		return Srt;
	}
}
