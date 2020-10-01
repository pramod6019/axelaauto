package axela.preowned;
//sangita 10th Jul 2013
//modified by divya sangeetha

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

public class Preowned_Eval_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0";
	public String eval_id = "0";
	public String eval_date = "";
	public String evaldate = "";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String preownedeval_title = "";
	public String preownedmodel_name = "";
	public String variant_name = "";
	public String preownedmodel_id = "";
	public String variant_id = "";
	public String eval_acc_stereo = "";
	public String eval_acc_powersteering = "";
	public String eval_acc_powerwindows = "";
	public String eval_acc_centrallocking = "";
	public String eval_acc_alloywheels = "";
	public String eval_acc_keys = "";
	public String eval_acc_toolkit = "";
	public String eval_acc_parkingsensor = "";
	public String eval_acc_others = "";
	public String eval_acc_stereo_make = "";
	public String eval_acc_powersteering_make = "";
	public String eval_acc_powerwindows_make = "";
	public String eval_acc_centrallocking_make = "";
	public String eval_acc_alloywheels_make = "";
	public String eval_acc_keys_make = "";
	public String eval_acc_toolkit_make = "";
	public String eval_acc_parkingsensor_make = "";
	public String eval_acc_others_make = "";
	public String eval_offered_price = "0";
	public String chk_accidental = "0";
	public String eval_emp_id = "0";
	public String eval_notes = "";
	public String eval_entry_id = "0";
	public String eval_entry_date = "";
	public String eval_entry_by = "";
	public String eval_modified_id = "0";
	public String eval_modified_date = "";
	public String eval_modified_by = "";
	public String QueryString = "";
	public String StrHTML = "";
	public String evalstatus = "";
	public String evaltrans_rf_amt = "";
	public String evaltrans_observation = "";
	public String evaltrans_evalhead_id = "";
	public String eval_rf_total = "0";
	public int totalcount = 0;
	public int statuscount = 0;
	public String eval_evaltype_id = "0";
	public String evaldetailstrans_status = "";
	public String evaldetailstrans_evaldetails_id = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String return_perm = "0";
	public String preowned_expectedprice = "0";

	//
	// public String preowned_preownedmodel_id = "0";
	public String preowned_variant_id = "0", preowned_enquiry_id = "0", preowned_customer_id = "0", preowned_contact_id = "0", preowned_branch_id = "0";
	public String contact_fname = "", contact_lname = "", contact_mobile1 = "", contact_phone1 = "";
	public String customer_address = "", contact_city_id = "", contact_pin = "";
	public String preowned_date = "", preowned_close_date = "", preowned_fueltype_id = "", preowned_manufyear = "";
	public String preowned_regno = "", preowned_insur_date = "";
	public String preowned_ownership_id = "0", preowned_emp_id = "0", preowned_sales_emp_id = "0", preowned_preownedstatus_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_eval_access", request, response);
				eval_id = CNumeric(PadQuotes(request.getParameter("eval_id")));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				evalstatus = PadQuotes(request.getParameter("dr_evalstatus"));
				return_perm = ReturnPerm(comp_id, "emp_preowned_eval_edit", request);
				PreownedDetails(response);
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				StrSql = "SELECT preowned_title, COALESCE(preownedmodel_id, 0) As preownedmodel_id,"
						+ " COALESCE(preownedmodel_name,'') AS preownedmodel_name, "
						+ " variant_id, variant_name"
						+ " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " WHERE preowned_id = " + preowned_id + ""
						+ BranchAccess.replace("branch_id", "preowned_branch_id")
						+ ExeAccess.replace("emp_id", "preowned_emp_id");
				// SOP("StrSql-------------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
						preownedmodel_id = crs.getString("preownedmodel_id");
						preownedmodel_name = crs.getString("preownedmodel_name");
						variant_id = crs.getString("variant_id");
						variant_name = crs.getString("variant_name");
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned!"));
				}
				crs.close();

				StrHTML = EvalHead(request);
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						evaldate = strToShortDate(ToLongDate(kknow()));
						eval_emp_id = emp_id;
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_eval_add", request).equals("1")) {
							eval_entry_id = emp_id;
							eval_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-eval-list.jsp?eval_id=" + eval_id + "&msg=Evaluation added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Evaluation".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Evaluation".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_eval_edit", request).equals("1")) {
							eval_modified_id = emp_id;
							eval_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-eval-list.jsp?eval_id=" + eval_id + "&msg=Evaluation updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Evaluation".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_eval_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-eval-list.jsp?msg=Evaluation deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		evaldate = PadQuotes(request.getParameter("txt_eval_date"));
		eval_date = ConvertShortDateToStr(evaldate);
		eval_evaltype_id = PadQuotes(request.getParameter("dr_evaltype_id"));
		preowned_title = PadQuotes(request.getParameter("txt_preowned_title"));
		preownedmodel_name = PadQuotes(request.getParameter("txt_preownedmodel_name"));
		variant_name = PadQuotes(request.getParameter("txt_variant_name"));
		preownedmodel_id = PadQuotes(request.getParameter("txt_preownedmodel_id"));
		variant_id = PadQuotes(request.getParameter("txt_variant_id"));
		eval_offered_price = CNumeric(PadQuotes(request.getParameter("txt_eval_offered_price")));
		eval_acc_stereo = PadQuotes(request.getParameter("chk_eval_acc_stereo"));
		eval_acc_powersteering = PadQuotes(request.getParameter("chk_eval_acc_powersteering"));
		eval_acc_powerwindows = PadQuotes(request.getParameter("chk_eval_acc_powerwindows"));
		eval_acc_centrallocking = PadQuotes(request.getParameter("chk_eval_acc_centrallocking"));
		eval_acc_alloywheels = PadQuotes(request.getParameter("chk_eval_acc_alloywheels"));
		eval_acc_keys = PadQuotes(request.getParameter("chk_eval_acc_keys"));
		eval_acc_toolkit = PadQuotes(request.getParameter("chk_eval_acc_toolkit"));
		eval_acc_parkingsensor = PadQuotes(request.getParameter("chk_eval_acc_parkingsensor"));
		eval_acc_others = PadQuotes(request.getParameter("chk_eval_acc_others"));
		eval_acc_stereo_make = PadQuotes(request.getParameter("txt_eval_acc_stereo_make"));
		chk_accidental = CheckBoxValue(PadQuotes(request.getParameter("txt_eval_acc_stereo_make")));
		preowned_expectedprice = CNumeric(PadQuotes(request.getParameter("txt_preowned_expectedprice")));

		if (eval_acc_stereo_make.length() > 255) {
			eval_acc_stereo_make = eval_acc_stereo_make.substring(0, 254);
		}
		eval_acc_powersteering_make = PadQuotes(request.getParameter("txt_eval_acc_powersteering_make"));

		if (eval_acc_powersteering_make.length() > 255) {
			eval_acc_powersteering_make = eval_acc_powersteering_make.substring(0, 254);
		}
		eval_acc_powerwindows_make = PadQuotes(request.getParameter("txt_eval_acc_powerwindows_make"));

		if (eval_acc_powerwindows_make.length() > 255) {
			eval_acc_powerwindows_make = eval_acc_powerwindows_make.substring(0, 254);
		}

		eval_acc_centrallocking_make = PadQuotes(request.getParameter("txt_eval_acc_centrallocking_make"));
		if (eval_acc_centrallocking_make.length() > 255) {
			eval_acc_centrallocking_make = eval_acc_centrallocking_make.substring(0, 254);
		}

		eval_acc_alloywheels_make = PadQuotes(request.getParameter("txt_eval_acc_alloywheels_make"));
		if (eval_acc_alloywheels_make.length() > 255) {
			eval_acc_alloywheels_make = eval_acc_alloywheels_make.substring(0, 254);
		}

		eval_acc_keys_make = PadQuotes(request.getParameter("txt_eval_acc_keys_make"));
		if (eval_acc_keys_make.length() > 255) {
			eval_acc_keys_make = eval_acc_keys_make.substring(0, 254);
		}

		eval_acc_toolkit_make = PadQuotes(request.getParameter("txt_eval_acc_toolkit_make"));
		if (eval_acc_toolkit_make.length() > 255) {
			eval_acc_toolkit_make = eval_acc_toolkit_make.substring(0, 254);
		}

		eval_acc_parkingsensor_make = PadQuotes(request.getParameter("txt_eval_acc_parkingsensor_make"));
		if (eval_acc_parkingsensor_make.length() > 255) {
			eval_acc_parkingsensor_make = eval_acc_parkingsensor_make.substring(0, 254);
		}

		eval_acc_others_make = PadQuotes(request.getParameter("txt_eval_acc_others_make"));
		if (eval_acc_others_make.length() > 255) {
			eval_acc_others_make = eval_acc_others_make.substring(0, 254);
		}

		eval_emp_id = CNumeric(PadQuotes(request.getParameter("dr_eval_executive")));
		eval_notes = PadQuotes(request.getParameter("txt_eval_notes"));
		eval_entry_by = PadQuotes(request.getParameter("stock_entry_by"));
		eval_entry_date = PadQuotes(request.getParameter("preownedstock_entry_date"));
		eval_modified_by = PadQuotes(request.getParameter("stock_modified_by"));
		eval_modified_date = PadQuotes(request.getParameter("preownedstock_modified_date"));
		totalcount = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_totalcount"))));
		eval_rf_total = CNumeric(PadQuotes(request.getParameter("txt_eval_rf_total")));
		// SOP("eval_rf_total------" + eval_rf_total);
		statuscount = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_statuscount"))));
		if (eval_acc_stereo.equals("on")) {
			eval_acc_stereo = "1";
		} else {
			eval_acc_stereo = "0";
		}

		if (eval_acc_powersteering.equals("on")) {
			eval_acc_powersteering = "1";
		} else {
			eval_acc_powersteering = "0";
		}

		if (eval_acc_powerwindows.equals("on")) {
			eval_acc_powerwindows = "1";
		} else {
			eval_acc_powerwindows = "0";
		}
		if (eval_acc_centrallocking.equals("on")) {
			eval_acc_centrallocking = "1";
		} else {
			eval_acc_centrallocking = "0";
		}
		if (eval_acc_alloywheels.equals("on")) {
			eval_acc_alloywheels = "1";
		} else {
			eval_acc_alloywheels = "0";
		}
		if (eval_acc_keys.equals("on")) {
			eval_acc_keys = "1";
		} else {
			eval_acc_keys = "0";
		}
		if (eval_acc_toolkit.equals("on")) {
			eval_acc_toolkit = "1";
		} else {
			eval_acc_toolkit = "0";
		}
		if (eval_acc_parkingsensor.equals("on")) {
			eval_acc_parkingsensor = "1";
		} else {
			eval_acc_parkingsensor = "0";
		}
		if (eval_acc_others.equals("on")) {
			eval_acc_others = "1";
		} else {
			eval_acc_others = "0";
		}

	}

	public void CheckForm() {
		msg = "";
		try {
			if (return_perm.equals("1")) {
				if (evaldate.equals("")) {
					msg = msg + "<br>Enter Date!";
				} else {
					if (isValidDateFormatShort(evaldate)) {
						eval_date = ConvertShortDateToStr(evaldate);
					} else {
						msg = msg + "<br>Enter Valid Date!";
					}
					if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(evaldate))) {
						msg = msg + " <br>Date can't be greater than Current Date!";
					}
				}
			}
			if (eval_emp_id.equals("0")) {
				msg = msg + "<br>Select Pre-Owned Consultant!";
			}

			if (eval_evaltype_id.equals("0")) {
				msg = msg + "<br>Select Evaluation Type!";
			}

			if (Long.parseLong(eval_offered_price) <= 0) {
				msg = msg + "<br>Enter Offered Price!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields(HttpServletRequest request) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				eval_id = ExecuteQuery("SELECT COALESCE(MAX(eval_id), 0) + 1 AS ID FROM " + compdb(comp_id) + "axela_preowned_eval");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval"
						+ " (eval_id,"
						+ " eval_evaltype_id,"
						+ " eval_date,"
						+ " eval_acc_stereo,"
						+ " eval_acc_powersteering,"
						+ " eval_acc_powerwindows,"
						+ " eval_acc_centrallocking,"
						+ " eval_acc_alloywheels,"
						+ " eval_acc_keys,"
						+ " eval_acc_toolkit,"
						+ " eval_acc_parkingsensor,"
						+ " eval_acc_others,"
						+ " eval_acc_stereo_make,"
						+ " eval_acc_powersteering_make,"
						+ " eval_acc_powerwindows_make,"
						+ " eval_acc_centrallocking_make,"
						+ " eval_acc_alloywheels_make,"
						+ " eval_acc_keys_make,"
						+ " eval_acc_toolkit_make,"
						+ " eval_acc_parkingsensor_make,"
						+ " eval_acc_others_make,"
						+ " eval_preowned_id,"
						+ " eval_rf_total,"
						+ " eval_offered_price,"
						+ " eval_emp_id,"
						+ " eval_notes,"
						+ " eval_entry_id,"
						+ " eval_entry_date)"
						+ " VALUES"
						+ " (" + eval_id + ","
						+ " " + eval_evaltype_id + ","
						+ " '" + eval_date + "',"
						+ " '" + eval_acc_stereo + "',"
						+ " '" + eval_acc_powersteering + "',"
						+ " '" + eval_acc_powerwindows + "',"
						+ " '" + eval_acc_centrallocking + "',"
						+ " '" + eval_acc_alloywheels + "',"
						+ " '" + eval_acc_keys + "',"
						+ " '" + eval_acc_toolkit + "',"
						+ " '" + eval_acc_parkingsensor + "',"
						+ " '" + eval_acc_others + "',"
						+ " '" + eval_acc_stereo_make + "',"
						+ " '" + eval_acc_powersteering_make + "',"
						+ " '" + eval_acc_powerwindows_make + "',"
						+ " '" + eval_acc_centrallocking_make + "',"
						+ " '" + eval_acc_alloywheels_make + "',"
						+ " '" + eval_acc_keys_make + "',"
						+ " '" + eval_acc_toolkit_make + "',"
						+ " '" + eval_acc_parkingsensor_make + "',"
						+ " '" + eval_acc_others_make + "',"
						+ " " + preowned_id + ","
						+ " " + eval_rf_total + ","
						+ " " + eval_offered_price + ","
						+ " " + eval_emp_id + ","
						+ " '" + eval_notes + "',"
						+ " " + eval_entry_id + ","
						+ " '" + eval_entry_date + "')";
				// SOP("StrSql------------" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);

				if (!eval_id.equals("0")) {
					AddEvalHead(request);
				}
				if (!preowned_enquiry_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET enquiry_evaluation = 1"
							+ " WHERE enquiry_id =" + preowned_enquiry_id;
					stmttx.addBatch(StrSql);
				}
				if (!preowned_expectedprice.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
							+ " SET preowned_expectedprice = " + preowned_expectedprice + ""
							+ " WHERE preowned_id =" + preowned_id;
					stmttx.addBatch(StrSql);
				}
				stmttx.executeBatch();
				conntx.commit();
				// SOPInfo("preowned_sales_emp_id------------" +
				// preowned_sales_emp_id);
				if (!preowned_sales_emp_id.equals("0")) {
					SendSalesExecutiveSMS();
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
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

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT eval_id, eval_evaltype_id, eval_date, preowned_title, preowned_expectedprice,"
					+ " eval_acc_stereo, eval_acc_powersteering, eval_acc_powerwindows, eval_acc_centrallocking,"
					+ " eval_acc_alloywheels, eval_acc_keys, eval_acc_toolkit, eval_acc_parkingsensor,"
					+ " eval_acc_others, eval_acc_stereo_make, eval_acc_powersteering_make, eval_acc_powerwindows_make,"
					+ " eval_acc_centrallocking_make, eval_acc_alloywheels_make, eval_acc_keys_make,"
					+ " eval_acc_toolkit_make, eval_acc_parkingsensor_make, eval_acc_others_make, eval_offered_price,"
					+ " eval_emp_id, eval_notes, eval_entry_id, eval_entry_date, eval_modified_id, eval_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = eval_emp_id"
					+ " WHERE eval_id = " + eval_id;
			// SOP("StrSql====" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					eval_id = crs.getString("eval_id");
					eval_evaltype_id = crs.getString("eval_evaltype_id");
					eval_date = crs.getString("eval_date");
					evaldate = strToShortDate(eval_date);
					preowned_title = crs.getString("preowned_title");
					preownedeval_title = crs.getString("preowned_title");
					preowned_expectedprice = crs.getString("preowned_expectedprice");
					eval_acc_stereo = crs.getString("eval_acc_stereo");
					eval_acc_powersteering = crs.getString("eval_acc_stereo");
					eval_acc_powerwindows = crs.getString("eval_acc_powerwindows");
					eval_acc_centrallocking = crs.getString("eval_acc_centrallocking");
					eval_acc_alloywheels = crs.getString("eval_acc_alloywheels");
					eval_acc_keys = crs.getString("eval_acc_keys");
					eval_acc_toolkit = crs.getString("eval_acc_toolkit");
					eval_acc_parkingsensor = crs.getString("eval_acc_parkingsensor");
					eval_acc_others = crs.getString("eval_acc_others");
					eval_acc_stereo_make = crs.getString("eval_acc_stereo_make");
					eval_acc_powersteering_make = crs.getString("eval_acc_powersteering_make");
					eval_acc_powerwindows_make = crs.getString("eval_acc_powerwindows_make");
					eval_acc_centrallocking_make = crs.getString("eval_acc_centrallocking_make");
					eval_acc_alloywheels_make = crs.getString("eval_acc_alloywheels_make");
					eval_acc_keys_make = crs.getString("eval_acc_keys_make");
					eval_acc_toolkit_make = crs.getString("eval_acc_toolkit_make");
					eval_acc_parkingsensor_make = crs.getString("eval_acc_parkingsensor_make");
					eval_acc_others_make = crs.getString("eval_acc_others_make");
					eval_offered_price = crs.getString("eval_offered_price");
					eval_emp_id = crs.getString("eval_emp_id");
					eval_notes = crs.getString("eval_notes");
					eval_entry_id = crs.getString("eval_entry_id");
					if (!eval_entry_id.equals("")) {
						eval_entry_by = Exename(comp_id, Integer.parseInt(eval_entry_id));
					}
					eval_entry_date = strToLongDate(crs.getString("eval_entry_date"));
					eval_modified_id = crs.getString("eval_modified_id");
					if (!eval_modified_id.equals("0")) {
						eval_modified_by = Exename(comp_id, Integer.parseInt(eval_modified_id));
						eval_modified_date = strToLongDate(crs.getString("eval_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Evalution!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval"
						+ " SET"
						+ " eval_evaltype_id = '" + eval_evaltype_id + "',"
						+ " eval_date = '" + eval_date + "',"
						+ " eval_acc_stereo = '" + eval_acc_stereo + "',"
						+ " eval_acc_powersteering = '" + eval_acc_powersteering + "',"
						+ " eval_acc_powerwindows = '" + eval_acc_powerwindows + "',"
						+ " eval_acc_centrallocking = '" + eval_acc_centrallocking + "',"
						+ " eval_acc_alloywheels = '" + eval_acc_alloywheels + "',"
						+ " eval_acc_keys = '" + eval_acc_keys + "',"
						+ " eval_acc_toolkit = '" + eval_acc_toolkit + "',"
						+ " eval_acc_parkingsensor = '" + eval_acc_parkingsensor + "',"
						+ " eval_acc_others = '" + eval_acc_others + "',"
						+ " eval_acc_stereo_make = '" + eval_acc_stereo_make + "',"
						+ " eval_acc_powersteering_make = '" + eval_acc_powersteering_make + "',"
						+ " eval_acc_powerwindows_make = '" + eval_acc_powerwindows_make + "',"
						+ " eval_acc_centrallocking_make = '" + eval_acc_centrallocking_make + "',"
						+ " eval_acc_alloywheels_make = '" + eval_acc_alloywheels_make + "',"
						+ " eval_acc_keys_make = '" + eval_acc_keys_make + "',"
						+ " eval_acc_toolkit_make = '" + eval_acc_toolkit_make + "',"
						+ " eval_acc_parkingsensor_make = '" + eval_acc_parkingsensor_make + "',"
						+ " eval_acc_others_make = '" + eval_acc_others_make + "',"
						+ " eval_offered_price = " + eval_offered_price + ","
						+ " eval_emp_id = " + eval_emp_id + ","
						+ " eval_notes = '" + eval_notes + "',"
						+ " eval_modified_id = " + eval_modified_id + ","
						+ " eval_modified_date = '" + eval_modified_date + "'"
						+ " WHERE eval_id = " + eval_id + "";

				stmttx.addBatch(StrSql);

				if (totalcount != 0) {
					UpdateEvalHead(request);
				}

				if (!preowned_expectedprice.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
							+ " SET preowned_expectedprice = " + preowned_expectedprice + ""
							+ " WHERE preowned_id =" + preowned_id;
					stmttx.addBatch(StrSql);
				}

				stmttx.executeBatch();
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("Connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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

	protected void DeleteFields() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval"
					+ " WHERE eval_id = " + eval_id + "";

			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateEvalExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_preowned = 1"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), eval_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String EvalHead(HttpServletRequest request) {
		String head = "", subhead = "";
		StringBuilder Str = new StringBuilder();
		StringBuilder Stramt = new StringBuilder();
		int countamt = 0, countsubhead = 0, i = 0, j = 1;
		try {
			StrSql = "SELECT evalhead_id, evalhead_name, evalsubhead_id, evalsubhead_name,"
					+ " evaldetails_id, evaldetails_name, evaldetails_active,"
					+ " COALESCE(evaltrans_observation, '') AS evaltrans_observation,"
					+ " COALESCE(evaltrans_rf_amt, '') AS evaltrans_rf_amt,"
					+ " COALESCE(eval_preowned_id, 0) AS eval_preowned_id,"
					+ " COALESCE(evaldetailstrans_status, 0) AS evaldetailstrans_status"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval_head"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_subhead ON evalsubhead_evalhead_id = evalhead_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_details ON evaldetails_evalsubhead_id = evalsubhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_trans ON evaltrans_evalhead_id = evalhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_id = evaltrans_eval_id";

			if (add.equals("yes")) {
				StrSql += " AND eval_preowned_id = " + preowned_id + "";
			}

			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_details_trans ON evaldetailstrans_evaldetails_id = evaldetails_id"
					+ " AND evaldetailstrans_eval_id = eval_id"
					+ " WHERE evaldetails_active = '1' ";

			if (update.equals("yes")) {
				StrSql += " AND eval_preowned_id = " + preowned_id + ""
						+ " AND eval_id = " + eval_id;
			}

			StrSql += " GROUP BY evalhead_id, evalsubhead_id, evaldetails_id"
					+ " ORDER BY evalhead_rank, evalsubhead_rank, evaldetails_rank";
			// SOP("StrSql--" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				while (crs.next()) {
					if (!subhead.equals(crs.getString("evalsubhead_name"))) {
						String str = Stramt.toString().replace("[SHROWSPAN]", countsubhead + "");
						Stramt.setLength(0);
						Stramt.append(str);
						countsubhead = 0;
					}

					if (!head.equals(crs.getString("evalhead_name"))) {
						if (Stramt.length() > 0) {
							Str.append(Stramt.toString().replace("[ROWSPAN]", countamt + ""));
							Stramt.setLength(0);
							countamt = 0;
						}
						// if (i == 0) {
						evaltrans_evalhead_id = crs.getString("evalhead_id");
						i++;
						Str.append("<thead>\n");
						Str.append("<tr align=center>\n");
						Str.append("<th colspan=2>").append(crs.getString("evalhead_name")).append("</th>\n");
						Str.append("<th colspan=1>").append("<a href=\"javascript:statusallok(").append(i).append(");\">").append("All Ok").append("</a></th>\n");
						Str.append("<th colspan=2>").append("").append("</th>\n");
						Str.append("</tr>");
						Str.append("</thead>\n");

						// i++;
						// // } else {
						// evaltrans_evalhead_id = crs.getString("evalhead_id");
						// Str.append("<tr align=center>\n");
						// Str.append("<th colspan=5>").append(crs.getString("evalhead_name")).append("</th>\n");
						// Str.append("</tr>");
						// }
						j = 1;
						head = crs.getString("evalhead_name");

					}
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Stramt.append("<tr valign=top>\n");

					if (!subhead.equals(crs.getString("evalsubhead_name"))) {
						Stramt.append("<td rowspan=[SHROWSPAN]><b>").append(crs.getString("evalsubhead_name")).append("</b></td>\n");
						subhead = crs.getString("evalsubhead_name");
					}

					Stramt.append("<td>").append(crs.getString("evaldetails_name")).append("</td>\n");

					evaldetailstrans_evaldetails_id = crs.getString("evaldetails_id");

					if (!PadQuotes(request.getParameter("dr_evalstatus_" + i + "_" + j)).equals("")) {
						evaldetailstrans_status = PadQuotes(request.getParameter("dr_evalstatus_" + i + "_" + j + ""));
					} else {
						evaldetailstrans_status = crs.getString("evaldetailstrans_status");
					}

					Stramt.append("<td>\n<input type=hidden name=txt_detail_id_").append(i).append("_").append(j).append(" name=txt_detail_id_").append(i).append("_").append(j);
					Stramt.append(" value=").append(evaldetailstrans_evaldetails_id).append(">");
					Stramt.append("<select name=dr_evalstatus_").append(i).append("_").append(j).append(" class=form-control id=dr_evalstatus_").append(i).append("_").append(j).append(">");
					Stramt.append(PopulateStatus(evaldetailstrans_status));
					Stramt.append("</select>\n</td>\n");

					if (countamt == 0) {
						evaltrans_rf_amt = "";
						evaltrans_observation = "";
						if (!PadQuotes(request.getParameter("txt_evalamt" + i)).equals("")) {
							evaltrans_rf_amt = PadQuotes(request.getParameter("txt_evalamt" + i));
							eval_rf_total = Integer.parseInt(eval_rf_total) + Integer.parseInt(evaltrans_rf_amt) + "";
						} else {
							if (!crs.getString("eval_preowned_id").equals("0")) {
								evaltrans_rf_amt = crs.getString("evaltrans_rf_amt");
								eval_rf_total = Integer.parseInt(eval_rf_total) + Integer.parseInt(evaltrans_rf_amt) + "";
							}
						}
						if (!PadQuotes(request.getParameter("txt_evalobservation" + i)).equals("")) {
							evaltrans_observation = PadQuotes(request.getParameter("txt_evalobservation" + i));
							if (evaltrans_observation.length() > 500) {
								evaltrans_observation = evaltrans_observation.substring(0, 499);
							}
						} else {
							if (!crs.getString("eval_preowned_id").equals("0")) {
								evaltrans_observation = crs.getString("evaltrans_observation");
							}
						}
						Stramt.append("<td rowspan=[ROWSPAN]><textarea name=txt_evalobservation").append(i).append(" cols=40 rows=4 class=\"form-control\" id=txt_evalobservation").append(i)
								.append(" style=width:250px>");
						Stramt.append(evaltrans_observation).append("</textarea></td>\n");
						Stramt.append("<td rowspan=[ROWSPAN]><input name=\"txt_evalamt").append(i).append("\" type=\"text\" class=\"form-control\" id=\"txt_evalamt").append(i)
								.append("\" size=\"12\" maxlength=\"11\"  value=\"");
						Stramt.append(evaltrans_rf_amt).append("\" onKeyUp=\"toInteger(this.id);GetTotal();\">");
						Stramt.append("<input type=hidden id=txt_evaltrans_evalhead_id").append(i).append(" name=txt_evaltrans_evalhead_id").append(i).append(" value=").append(evaltrans_evalhead_id)
								.append(">");
						Stramt.append("<input type=hidden id=txt_evalhead_statuscount_").append(i).append(" name=txt_evalhead_statuscount_").append(i).append(" value=[ROWSPAN]></td>");

					}
					Stramt.append("</tr>\n");
					countamt++;
					countsubhead++;
					j++;
				}
				String str = Stramt.toString().replace("[SHROWSPAN]", countsubhead + "");
				Stramt.setLength(0);
				Stramt.append(str);
				Str.append(Stramt.toString().replace("[ROWSPAN]", countamt + ""));
				Str.append("<tr align=center>\n<input type=hidden name=txt_statuscount id=txt_statuscount value=").append(j)
						.append("><td align=right colspan=4><input type=hidden id=txt_totalcount name=txt_totalcount value=").append(i).append("><b>Total: ").append("</b></td>\n");
				Str.append("<td align=left><input type=hidden id=txt_eval_rf_total name=txt_eval_rf_total value=").append(eval_rf_total).append("><div id=div_totalamt>").append("<b>")
						.append(eval_rf_total).append("</b></div></td>\n</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void AddEvalHead(HttpServletRequest request) throws SQLException {
		try {
			for (int i = 1; i <= totalcount; i++) {
				evaltrans_observation = PadQuotes(request.getParameter("txt_evalobservation" + i + ""));
				evaltrans_rf_amt = PadQuotes(request.getParameter("txt_evalamt" + i + ""));
				evaltrans_evalhead_id = PadQuotes(request.getParameter("txt_evaltrans_evalhead_id" + i + ""));

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_trans"
						+ " (evaltrans_eval_id,"
						+ " evaltrans_evalhead_id,"
						+ " evaltrans_observation,"
						+ " evaltrans_rf_amt)"
						+ " VALUES"
						+ " (" + eval_id + ","
						+ " " + evaltrans_evalhead_id + ","
						+ " '" + evaltrans_observation + "',"
						+ " " + CNumeric(evaltrans_rf_amt) + ")";
				stmttx.addBatch(StrSql);

				statuscount = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_evalhead_statuscount_" + i + ""))));
				for (int j = 1; j <= statuscount; j++) {
					evaldetailstrans_evaldetails_id = PadQuotes(request.getParameter("txt_detail_id_" + i + "_" + j + ""));
					evaldetailstrans_status = PadQuotes(request.getParameter("dr_evalstatus_" + i + "_" + j + ""));

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_details_trans"
							+ " (evaldetailstrans_eval_id,"
							+ " evaldetailstrans_evaldetails_id,"
							+ " evaldetailstrans_status)"
							+ " VALUES"
							+ " (" + eval_id + ","
							+ " " + evaldetailstrans_evaldetails_id + ","
							+ " '" + evaldetailstrans_status + "')";
					stmttx.addBatch(StrSql);
				}
			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("Connection rollback...");
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void UpdateEvalHead(HttpServletRequest request) throws SQLException {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval_trans"
					+ " WHERE evaltrans_eval_id = " + eval_id;
			stmttx.addBatch(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval_details_trans"
					+ " WHERE evaldetailstrans_eval_id = " + eval_id;
			stmttx.addBatch(StrSql);

			for (int i = 1; i <= totalcount; i++) {
				evaltrans_observation = PadQuotes(request.getParameter("txt_evalobservation" + i + ""));
				evaltrans_rf_amt = PadQuotes(request.getParameter("txt_evalamt" + i + ""));
				evaltrans_evalhead_id = PadQuotes(request.getParameter("txt_evaltrans_evalhead_id" + i + ""));

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_trans"
						+ " (evaltrans_eval_id,"
						+ " evaltrans_evalhead_id,"
						+ " evaltrans_observation,"
						+ " evaltrans_rf_amt)"
						+ " VALUES"
						+ " (" + eval_id + ","
						+ " " + evaltrans_evalhead_id + ","
						+ " '" + evaltrans_observation + "',"
						+ " " + CNumeric(evaltrans_rf_amt) + ")";
				stmttx.addBatch(StrSql);

				statuscount = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_evalhead_statuscount_" + i + ""))));
				for (int j = 1; j <= statuscount; j++) {
					evaldetailstrans_evaldetails_id = PadQuotes(request.getParameter("txt_detail_id_" + i + "_" + j + ""));
					evaldetailstrans_status = PadQuotes(request.getParameter("dr_evalstatus_" + i + "_" + j + ""));

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_details_trans"
							+ " (evaldetailstrans_eval_id,"
							+ " evaldetailstrans_evaldetails_id,"
							+ " evaldetailstrans_status)"
							+ " VALUES"
							+ " (" + eval_id + ","
							+ " " + evaldetailstrans_evaldetails_id + ","
							+ " '" + evaldetailstrans_status + "')";
					stmttx.addBatch(StrSql);
				}
			}
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("Connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public String PopulateStatus(String evaldetailstrans_status) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", evaldetailstrans_status)).append(">OK</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", evaldetailstrans_status)).append(">NOT OK</option>\n");
		return Str.toString();

	}

	public void PreownedDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT contact_fname, contact_lname, contact_mobile1, contact_phone1, "
					// + " preowned_preownedmodel_id, "
					+ " preowned_variant_id, preowned_customer_id, preowned_contact_id,"
					+ " preowned_date, preowned_close_date, preowned_fueltype_id, preowned_manufyear,"
					+ " preowned_regno, preowned_expectedprice,"
					// + " preowned_insur_date,"
					+ " preowned_ownership_id, contact_city_id,"
					+ " contact_pin, preowned_branch_id,"
					+ " preowned_emp_id, preowned_sales_emp_id, preowned_preownedstatus_id, contact_mobile1, contact_phone1, preowned_enquiry_id "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id=preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id=preowned_contact_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_customer_contact ON contact_id=preowned_contact_id"
					+ " WHERE preowned_id = " + preowned_id + ""
					+ BranchAccess.replace("branch_id", "preowned_branch_id") + ""
					+ ExeAccess.replace("emp_id", "preowned_emp_id") + "";
			// SOP("StrSql-----PreownedDetails-------------" +
			// StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_branch_id = crs.getString("preowned_branch_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_phone1 = crs.getString("contact_phone1");
					contact_city_id = crs.getString("contact_city_id");
					contact_pin = crs.getString("contact_pin");
					preowned_date = crs.getString("preowned_date");
					preowned_close_date = crs.getString("preowned_close_date");
					// preowned_preownedmodel_id =
					// crs.getString("preowned_preownedmodel_id");
					preowned_variant_id = crs.getString("preowned_variant_id");
					preowned_fueltype_id = crs.getString("preowned_fueltype_id");
					preowned_manufyear = crs.getString("preowned_manufyear");
					preowned_regno = crs.getString("preowned_regno");
					// preowned_insur_date = crs.getString("preowned_insur_date");
					preowned_ownership_id = crs.getString("preowned_ownership_id");
					preowned_emp_id = crs.getString("preowned_emp_id");
					preowned_sales_emp_id = crs.getString("preowned_sales_emp_id");
					preowned_preownedstatus_id = crs.getString("preowned_preownedstatus_id");
					preowned_enquiry_id = crs.getString("preowned_enquiry_id");
					preowned_expectedprice = crs.getString("preowned_expectedprice");
					// branch_brand_id =
					// crs.getString("branch_brand_id");
				}
				if (preowned_branch_id.equals("0")) {
					msg += "<font color=red>Update Pre-Owned Branch! </font>";
				}
				if (contact_fname.equals("")) {
					msg += "<br><font color=red>Update Contact First Name! </font>";
				}
				// if (contact_lname.equals("")) {
				// msg += "<br><font color=red>Update Contact Last Name! </font>";
				// }
				if (contact_mobile1.equals("")) {
					msg += "<br><font color=red>Update Contact Mobile 1! </font>";
				}
				// if (contact_phone1.equals("")) {
				// msg += "<br><font color=red>Update Contact Phone 1! </font>";
				// }
				if (contact_city_id.equals("0")) {
					msg += "<br><font color=red>Update Contact City! </font>";
				}
				if (contact_pin.equals("")) {
					msg += "<br><font color=red>Update Contact Pin! </font>";
				}
				if (preowned_date.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Date!</font>";
				}
				if (preowned_close_date.equals("")) {
					msg += "<br><font color=red>Update Closing Date!</font>";
				}
				// if (preowned_preownedmodel_id.equals("0")) {
				// msg += "<br><font color=red>Update Preowned Model!</font>";
				// }
				// if (preowned_variant_id.equals("0")) {
				// msg += "<br><font color=red>Update Preowned Variant!</font>";
				// }
				// if (preowned_fueltype_id.equals("0")) {
				// msg += "<br><font color=red>Update Pre-Owned Fuel Type!</font>";
				// }
				if (preowned_manufyear.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Manufacture Year!</font>";
				}
				if (preowned_regno.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Registration No!</font>";
				}
				// if (preowned_insur_date.equals("")) {
				// msg += "<br><font color=red>Update Pre-Owned Insurance Date!</font>";
				// }
				if (preowned_ownership_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Ownership!</font>";
				}
				if (preowned_emp_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Consultant!</font>";
				}
				if (preowned_preownedstatus_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Status!</font>";
				}
				if (!msg.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=" + msg);
				}

			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Pre-Owned!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	protected void SendSalesExecutiveSMS() throws SQLException {
		String smsmsg = "";

		smsmsg = "'Evaluation done for Enquiry ID [ENQUIRYID] for Customer [CUSTOMERNAME]."
				+ " Offered Price [OFFEREDPRICE]."
				+ " Evaluation Pre-Owned Consultant [EVALUATIONEXECUTIVE],"
				+ " Mobile [MOBILE].'";

		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYID]',preowned_enquiry_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[OFFEREDPRICE]'," + eval_offered_price + ")";
		smsmsg = "replace(" + smsmsg + ",'[EVALUATIONEXECUTIVE]',eval.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[MOBILE]',eval.emp_mobile1)";

		try {
			StrSql = "SELECT"
					+ " preowned_branch_id,"
					+ " preowned_contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " sales.emp_mobile1,"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp eval ON eval.emp_id = eval_emp_id"
					+ " WHERE preowned_id = " + preowned_id + "";
			// SOPInfo("select StrSql-------------sms1-----" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOPInfo(" insert StrSql-sms1-------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateEvalType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT evaltype_id, evaltype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_evaltype"
					+ " WHERE 1 = 1 "
					+ " GROUP BY evaltype_id"
					+ " ORDER BY evaltype_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_evaltype_id id=dr_evaltype_id class=form-control>");

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("evaltype_id"));
				Str.append(StrSelectdrop(crs.getString("evaltype_id"), eval_evaltype_id));
				Str.append(">").append(crs.getString("evaltype_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
