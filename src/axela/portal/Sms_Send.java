package axela.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sms_Send extends Connect {

	public String sms_id = "0", sms_contact = "", sms_customer = "";
	public String sms_mobileno = "";
	public String sms_msg = "";
	public String sms_date = "";
	public String sms_sent = "";
	public String sms_entry_id = "0";
	public String target = "";
	public String contact_id = "0", contact_fname = "";
	public String customer_id = "0";
	public String customer_branch_id = "0", branch_id = "0", branchfilter = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String status = " Contact";
	public String linkheader = "";
	public String sendB = "";
	public String msg = "";
	public String BranchAccess = "";
	public String comp_sms_enable = "";
	public String smart = "";
	public String StrSql = "";
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
				CheckPerm(comp_id, "emp_sms_send", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				target = PadQuotes(request.getParameter("target"));
				customer_id = CNumeric(PadQuotes(request
						.getParameter("customer_id")));
				contact_id = CNumeric(PadQuotes(request
						.getParameter("contact_id")));
				msg = PadQuotes(request.getParameter("msg"));
				sms_id = CNumeric(PadQuotes(request.getParameter("sms_id")));
				sendB = PadQuotes(request.getParameter("send_button"));
				smart = PadQuotes(request.getParameter("smart"));
				StrSql = "select comp_sms_enable " + " from " + compdb(comp_id)
						+ "axela_comp " + " where comp_active = '1'";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					comp_sms_enable = crs.getString("comp_sms_enable");
				}
				crs.close();
				if (comp_sms_enable.equals("0")) {
					response.sendRedirect(response
							.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				if (!branch_id.equals("0")) {
					branchfilter = " and customer_branch_id = " + branch_id;
				}
				contact_fname = ExecuteQuery("Select contact_fname" + " from "
						+ compdb(comp_id) + "axela_customer_contact"
						+ " where contact_id = " + CNumeric(contact_id) + "");
				if (contact_fname.equals("") && !contact_id.equals("0")) {
					response.sendRedirect(response
							.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact"));
				}
				if (!target.equals("")) {
					linkheader = "<li><a href=\"home.jsp\">Home</a> &gt; </li><li><a href=\"sms.jsp\">SMS</a> &gt; </li><li><a href=\"sms-send.jsp?target="
							+ target + "\">Send SMS</a>:</li>";
				} else if (!contact_id.equals("")) {
					linkheader = "<li><a href=\"home.jsp\">Home</a> &gt; </li><li><a href=\"sms.jsp\">SMS</a> &gt; </li><li><a href=\"sms-send.jsp?contact_id="
							+ contact_id + "\">Send SMS</a>:</li>";
				}
				if (target.equals("1")) {
					status = " to All Contacts";
				} else if (target.equals("2")) {
					status = " to All Customers";
				} else if (target.equals("3")) {
					status = " to All Suppliers";
				}
				if (!"Send".equals(sendB)) {
					sms_mobileno = "";
					sms_msg = "";
					sms_date = "";
					sms_sent = "";
					sms_entry_id = "";
				} else if (sendB.equals("Send")) {
					CheckPerm(comp_id, "emp_sms_send", request, response);
					GetValues(request, response);
					sms_entry_id = CNumeric(GetSession("emp_id", request));
					sms_date = ToLongDate(kknow());
					CheckForm();
					if (smart.equals("yes")) {
						SmartSearch = SmartSearch
								+ GetSession("contactstrsql", request);
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (msg.equals("")) {
							Format();
						}
						if (msg.equals("")) {
							if (smart.equals("yes")) {
								response.sendRedirect(response
										.encodeRedirectURL("sms-send.jsp?smart=yes"));
							} else if (target.equals("1") || target.equals("2")
									|| target.equals("3")) {
								if (target.equals("1")) {
									msg = "SMS Sent to all Contacts successfully!";
								} else if (target.equals("2")) {
									msg = "SMS Sent to all Customers successfully!";
								} else {
									msg = "SMS Sent to all Suppliers successfully!";
								}
								response.sendRedirect(response
										.encodeRedirectURL("sms-list.jsp?&msg="
												+ msg + ""));
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("sms-list.jsp?sms_id="
												+ sms_id
												+ "&msg=SMS sent successfully!"));
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void CheckForm() {
		msg = "";
		if (customer_id.equals("0") && customer_branch_id.equals("")) {
			msg = "<br>Enter Customer ID!";
		}
		if (!customer_id.equals("0") && isNumeric(customer_id)) {
			StrSql = "SELECT count(customer_id) from " + compdb(comp_id)
					+ "axela_customer " + " inner join " + compdb(comp_id)
					+ "axela_branch on branch_id = customer_branch_id "
					+ " where customer_active='1' " + " and customer_id="
					+ customer_id + " " + branchfilter + BranchAccess;

			sms_customer = ExecuteQuery(StrSql);
			if (sms_customer.equals("0")) {
				msg = msg + "<br> Customer ID " + customer_id + " is Invalid!";
			}
		} else if (contact_id.equals("0") && customer_branch_id.equals("")) {
			msg = "<br>Enter Contact ID!";
		}
		if (!contact_id.equals("0") && isNumeric(contact_id)) {
			StrSql = "select count(contact_id) from " + compdb(comp_id)
					+ "axela_customer_contact" + " inner join "
					+ compdb(comp_id)
					+ "axela_customer on customer_id = contact_customer_id "
					+ " inner join " + compdb(comp_id)
					+ "axela_branch on branch_id = customer_branch_id "
					+ " where contact_active='1' " + " and contact_id="
					+ contact_id + " " + branchfilter + BranchAccess;
			sms_contact = ExecuteQuery(StrSql);
			if (sms_contact.equals("0")) {
				msg = msg + "<br> Contact ID " + contact_id + " is Invalid!";
			}
		}
		if (sms_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			if (branch_id.equals("0")) {
				customer_branch_id = CNumeric(PadQuotes(request
						.getParameter("dr_branch")));
			} else {
				customer_branch_id = branch_id;
			}
			sms_id = CNumeric(PadQuotes(request.getParameter("sms_id")));
			sms_mobileno = PadQuotes(request.getParameter("txt_sms_mobileno"));
			sms_msg = PadQuotes(request.getParameter("txt_sms_msg"));
			smart = PadQuotes(request.getParameter("smart"));
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void Format() throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String StrSearch = "";
			String StrTo = "contact_mobile1";

			if (sms_msg.length() > 500) {
				sms_msg = sms_msg.substring(0, 499);
			}
			if (target.equals("1")) {
				StrTo = "contact_mobile1";
			} else if (target.equals("2")) {
				StrTo = "contact_mobile1";
			} else if (target.equals("3")) {
				StrTo = "contact_mobile1";
			}
			sms_msg = "replace('" + sms_msg
					+ "','[CUSTOMERNAME]',customer_name)";
			sms_msg = "replace(" + sms_msg + ",'[CUSTOMERID]',customer_id)";
			sms_msg = "replace(" + sms_msg + ",'[CONTACTNAME]',contact_fname)";
			sms_msg = "replace(" + sms_msg + ",'[CONTACTID]',contact_id)";

			StrSql = "SELECT " + " " + StrTo + ", " + " " + sms_msg + ", "
					+ " '" + ToLongDate(kknow()) + "', " + " 0, "
					+ " contact_id, " + " " + emp_id + " " + " from "
					+ compdb(comp_id) + "axela_customer_contact "
					+ " inner join " + compdb(comp_id)
					+ "axela_customer on customer_id = contact_customer_id "
					+ " inner join " + compdb(comp_id)
					+ "axela_branch on branch_id = customer_branch_id ";
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
						+ " where contact_active = '1' and contact_id = "
						+ contact_id + BranchAccess;
			} else {
				StrSql = StrSql + " where contact_active = '1'" + BranchAccess;
			}
			if (!smart.equals("")) {
				StrSql = StrSql + SmartSearch;
			}
			if (!branch_id.equals("0")) {
				StrSearch = StrSearch + " and customer_branch_id = "
						+ customer_branch_id;
			}
			if (branch_id.equals("0") && !customer_branch_id.equals("0")
					&& !customer_branch_id.equals("")) {
				StrSearch = StrSearch + " and customer_branch_id = "
						+ customer_branch_id;
			}
			if (target.equals("2")) {
				StrSearch = StrSearch
						+ " and contact_active='1' and contact_mobile1!=''";
			} else if (target.equals("3")) {
				StrSearch = StrSearch + " and contact_mobile1!=''";
			}
			StrSql = StrSql + StrSearch;
			if (target.equals("1")) {
				StrSql = StrSql
						+ " group by contact_id order by contact_id desc ";
			} else if (target.equals("2")) {
				StrSql = StrSql
						+ " group by contact_id order by contact_id desc ";
			} else if (target.equals("3")) {
				StrSql = StrSql
						+ " group by contact_id order by contact_id desc ";
			}
			AddFields(StrSql);
			SOP("StrSQlll===" + StrSql);
			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...\n sql--" + StrSql);

			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
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

	protected void AddFields(String Sql) {
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_sms"
						+ "(sms_mobileno," + " sms_msg," + " sms_date,"
						+ " sms_sent," + " sms_contact_id," + " sms_entry_id)"
						+ " " + Sql + "";
				if (target.equals("1") || target.equals("2")
						|| target.equals("3")) {
					updateQuery(StrSql);
					SOP("StrSql===" + StrSql);
				} else {
					sms_id = UpdateQueryReturnID(StrSql);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	public String PopulateSendTo() {
		String Srt = "";
		Srt = Srt + "<option value=1" + StrSelectdrop("1", target)
				+ ">All Contacts</option>\n";
		Srt = Srt + "<option value=2" + StrSelectdrop("2", target)
				+ ">All Customers</option>\n";
		Srt = Srt + "<option value=3" + StrSelectdrop("3", target)
				+ ">All Suppliers</option>\n";
		return Srt;
	}
}
