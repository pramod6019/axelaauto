package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Outstanding_Adj_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String strHTML = "";
	public String msg = "";
	public String readOnly = "";
	public String QueryString = "";
	public String comp_id = "0";

	public String voucher_branch_id = "0";
	public String voucher_date = "";
	public String voucher_customer_id = "";
	public String voucher_contact_id = "0";
	public String emp_branch_id = "0";
	public String voucherdate = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public double totalamount = 0.00, totalamountadj = 0.00;

	public int voucher_count = 0;
	public String voucherbal_voucher_id = "", voucherbal_trans_id = "", checked = "";
	public String voucherbal_voucher_idarr[] = null, voucherbal_trans_idarr[] = null;
	public Connection conntx = null;
	public Statement stmttx = null;
	int voucher_unallocated_count = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_receipt_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				voucher_branch_id = CNumeric(PadQuotes(request
						.getParameter("dr_branch")));
				QueryString = PadQuotes(request.getQueryString());
				voucherdate = PadQuotes(request
						.getParameter("txt_voucher_date"));

				if (add.equals("yes")) {
					status = "Add";
					if (emp_branch_id.equals("0")) {
						emp_branch_id = CNumeric(GetSession("voucher_branch_id",
								request));
						if (emp_branch_id.equals("0")) {
							emp_branch_id = getActiveBranchID(request, emp_id);
							SetSession("voucher_branch_id", emp_branch_id, request);
						}
						voucher_branch_id = emp_branch_id;
					} else {
						voucher_branch_id = emp_branch_id;
					}
					if (!addB.equals("yes")) {
						voucher_date = ToLongDate(kknow());
						voucherdate = strToShortDate(voucher_date);
					} else if (addB.equals("yes")) {
						GetValues(request, response);
						CheckForm(request, response);
						if (ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1")) {
							InsertPendingInvoice(request, "");
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("outstanding-adj-update.jsp?add=yes"
												+ "&txt_voucher_date =" + voucherdate
												+ "&dr_branch="
												+ voucher_branch_id
												+ "&msg=Outstanding Adjustment done Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// no. of invoices
		voucher_count = Integer.parseInt(CNumeric(PadQuotes(request
				.getParameter("txt_count"))));
		// no. of un allocated vouchers
		voucher_unallocated_count = Integer.parseInt(CNumeric(PadQuotes(request
				.getParameter("txt_unallocated_count"))));
		// for all invoice checked
		for (int i = 0; i <= voucher_count; i++) {
			if (CheckBoxValue(PadQuotes(request.getParameter("chk_" + i))).equals("1")) {
				voucherbal_trans_id += CNumeric(PadQuotes(request.getParameter("txt_voucherid_" + i))) + ",";
			}
		}
		voucherbal_trans_id = (String) voucherbal_trans_id.subSequence(0, voucherbal_trans_id.length() - 1);
		if (voucherbal_trans_id.contains(",")) {
			voucherbal_trans_idarr = voucherbal_trans_id.split(",");
		} else {
			voucherbal_trans_idarr = new String[1];
			voucherbal_trans_idarr[0] = voucherbal_trans_id;
		}
		// for all unallocated invoices checked
		for (int i = 0; i <= voucher_unallocated_count; i++) {
			if (CheckBoxValue(PadQuotes(request.getParameter("chk_unallocated_" + i))).equals("1")) {
				totalamount = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_unallocated_amount_" + i))));
				voucherbal_voucher_id += CNumeric(PadQuotes(request.getParameter("txt_unallocated_voucherid_" + i))) + ",";
			}
		}
		voucherbal_voucher_id = (String) voucherbal_voucher_id.subSequence(0, voucherbal_voucher_id.length() - 1);
		if (voucherbal_voucher_id.contains(",")) {
			voucherbal_voucher_idarr = voucherbal_voucher_id.split(",");
		} else {
			voucherbal_voucher_idarr = new String[1];
			voucherbal_voucher_idarr[0] = voucherbal_voucher_id;
		}

	}
	protected void CheckForm(HttpServletRequest request, HttpServletResponse response) {
		msg = "";
		int unallocatedcount = 0, invoicecount = 0;
		// for un allocated bills
		for (int i = 0; i <= voucher_unallocated_count; i++) {
			if (CheckBoxValue(PadQuotes(request.getParameter("chk_unallocated_" + i))).equals("1")) {
				unallocatedcount++;
			}

		}
		// for invoices
		for (int i = 0; i <= voucher_count; i++) {
			if (CheckBoxValue(PadQuotes(request.getParameter("chk_" + i))).equals("1")) {
				invoicecount++;
			}

		}
		// checking many-to-many mapping
		if ((unallocatedcount > 1 && invoicecount > 1)) {
			msg += "<br>can't Adjust Many-To-Many Voucher!";
		}

		SOP("msg===" + msg);
	}
	// //////////Insert Pending Balance
	protected void InsertPendingInvoice(HttpServletRequest request,
			String update) throws Exception {
		String voucherbal_amount = "0", checked = "0";
		if (msg == "") {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (voucherbal_voucher_idarr.length >= 1 && voucherbal_trans_idarr.length == 1) {
					for (int i = 0; i <= voucher_unallocated_count; i++) {
						voucherbal_trans_id = voucherbal_trans_idarr[0];
						voucherbal_voucher_id = CNumeric(PadQuotes(request
								.getParameter("txt_unallocated_voucherid_" + i + "")));
						voucherbal_amount = CNumeric(PadQuotes(request
								.getParameter("txt_unallocated_amount_" + i + "")));
						checked = CheckBoxValue(PadQuotes(request.getParameter("chk_unallocated_" + i + "")));
						if (checked.equals("1")) {
							// inserting to voucherbal table
							StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_bal" + " ("
									+ " voucherbal_voucher_id,"
									+ " voucherbal_trans_id," + " voucherbal_amount,"
									+ " voucherbal_date)" + " VALUES" + " (" + " "
									+ voucherbal_voucher_id + "," + " " + voucherbal_trans_idarr[0]
									+ "," + " " + CNumeric(voucherbal_amount) + ","
									+ " '" + ConvertShortDateToStr(voucherdate) + "'" + " )";

							stmttx.executeUpdate(StrSql);

							// updating voucher
							StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher"
									+ " SET voucher_modified_id = " + emp_id + ","
									+ " voucher_modified_date = '" + ToLongDate(kknow()) + "'"
									+ " WHERE voucher_id = " + voucherbal_voucher_id;

							stmttx.executeUpdate(StrSql);
						}
					}
				} else if (voucherbal_voucher_idarr.length == 1 && voucherbal_trans_idarr.length > 1) {
					for (int i = 0; i <= voucher_unallocated_count; i++) {
						voucherbal_voucher_id = voucherbal_voucher_idarr[0];
						voucherbal_trans_id = CNumeric(PadQuotes(request
								.getParameter("txt_voucherid_" + i + "")));
						voucherbal_amount = CNumeric(PadQuotes(request
								.getParameter("txt_amount_" + i + "")));
						checked = CheckBoxValue(PadQuotes(request.getParameter("chk_" + i + "")));
						if (checked.equals("1")) {

							if (totalamount > Double.parseDouble(voucherbal_amount)) {
								totalamount = totalamount - Double.parseDouble(voucherbal_amount);
							} else {
								voucherbal_amount = totalamount + "";
							}

							// inserting to voucherbal table
							StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_bal" + " ("
									+ " voucherbal_voucher_id,"
									+ " voucherbal_trans_id," + " voucherbal_amount,"
									+ " voucherbal_date)" + " VALUES" + " (" + " "
									+ voucherbal_voucher_idarr[0] + "," + " " + voucherbal_trans_id
									+ "," + " " + CNumeric(voucherbal_amount) + ","
									+ " '" + ConvertShortDateToStr(voucherdate) + "'" + " )";
							// SOP("singlepayment-multi inv===" + StrSql);
							stmttx.executeUpdate(StrSql);

							// updating voucher
							StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher"
									+ " SET voucher_modified_id = " + emp_id + ","
									+ " voucher_modified_date = '" + ToLongDate(kknow()) + "'"
									+ " WHERE voucher_id = " + voucherbal_voucher_idarr[0];
							// SOP("singlepayment-multi inv==update=" + StrSql);
							stmttx.executeUpdate(StrSql);
						}
					}
				}
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOPError("connection Rollback...");
					conntx.rollback();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}
}
