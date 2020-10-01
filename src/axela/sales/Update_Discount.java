package axela.sales;

//Saiman 12th Feb 2013
// divya
// modified by sn 6, 7 may 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Update_Discount extends Connect {

	public String comp_id = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String msg = "";
	public String year = "", month = "";
	public int curryear = 0;
	public String discount_entry_id = "0";
	public String discount_entry_date = "";
	public String discount_entry_by = "";
	public String entry_date = "";
	public String discount_modified_id = "0";
	public String discount_modified_date = "";
	public String modified_date = "";
	public String discount_modified_by = "";
	public String discount_id = "", existing_discount_id;
	// variavles to add
	public String discount_jobtitle_id = "0";
	public String discount_model_id = "0", discount_brand_id = "0";
	public String discount_amount = "0";
	public String discount_month = "";
	public String dr_year = "0", dr_month = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckPerm(comp_id, "emp_discount_access", request, response);
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			discount_id = PadQuotes(request.getParameter("discount_id"));
			add = PadQuotes(request.getParameter("add"));
			update = PadQuotes(request.getParameter("update"));
			addB = PadQuotes(request.getParameter("add_button"));
			updateB = PadQuotes(request.getParameter("update_button"));
			deleteB = PadQuotes(request.getParameter("delete_button"));
			msg = PadQuotes(request.getParameter("msg"));
			if (!comp_id.equals("0")) {
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				month = CNumeric(PadQuotes(request.getParameter("dr_month")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				discount_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}
				if (month.equals("0")) {
					month = ToLongDate(kknow()).substring(4, 6);
				}
				if (add.equals("yes")) {
					status = "Add";

					if (!addB.equals("yes")) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_discount_add", request).equals("1")) {
							discount_entry_id = emp_id;
							// SOP("discount_entry_id====" + discount_entry_id);
							discount_entry_date = ToLongDate(kknow());
							// SOP("discount_entry_date====" + discount_entry_date);
							AddFields();
							// SOP("msg===22==" + msg);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("list-discount.jsp?discount_id=" + discount_id + "&msg=Discount added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Discount")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Discount")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_discount_edit", request).equals("1")) {
							discount_modified_id = emp_id;
							discount_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("list-discount.jsp?discount_id=" + discount_id + "&msg=Discount updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Discount")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_discount_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("list-discount.jsp?msg=Discount deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
			// SOP("status====" + status);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		discount_jobtitle_id = CNumeric(request.getParameter("dr_jobtitle_id"));
		// discount_brand_id = CNumeric(request.getParameter("dr_brand"));
		discount_model_id = CNumeric(request.getParameter("dr_model"));
		discount_amount = CNumeric(PadQuotes(request.getParameter("txt_disc_amount")));
		dr_year = CNumeric(request.getParameter("dr_year"));
		dr_month = CNumeric(request.getParameter("dr_month"));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		if (discount_jobtitle_id.equals("0")) {
			msg += " <br>Select Job Title!";
		}
		if (discount_brand_id.equals("0")) {
			msg += " <br>Select Brand!";
		}
		if (discount_amount.equals("") || discount_amount.equals("0")) {
			msg += " <br>Discount Can't be zero!";
		}
		if (dr_month.equals("0")) {
			msg += " <br>Select Month!";
		}
		if (!dr_month.equals("0")) {
			discount_month = dr_year + dr_month + "010000";
		}

		if (msg.equals("")) {
			StrSql = " SELECT discount_id FROM " + compdb(comp_id) + "axela_sales_discount "
					+ " WHERE 1 = 1"
					+ " AND discount_jobtitle_id = " + discount_jobtitle_id
					+ " AND discount_brand_id = " + discount_brand_id
					+ " AND discount_model_id = " + discount_model_id
					+ " AND SUBSTR(discount_month,1,6) = " + dr_year + dr_month;
			if (updateB.equals("yes")) {
				StrSql += " AND discount_id != " + discount_id;
			}
			// SOP("strsql==" + StrSql);
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0"))
				msg += "<br> Discount Already Exist!";
		}

	}

	public void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_discount "
						+ "(discount_jobtitle_id,"
						+ " discount_brand_id,"
						+ " discount_model_id,"
						+ " discount_amount,"
						+ " discount_month,"
						+ " discount_entry_id,"
						+ " discount_entry_date,"
						+ " discount_modified_id,"
						+ " discount_modified_date)"
						+ " VALUES( "
						+ discount_jobtitle_id + ","
						+ discount_brand_id + ","
						+ discount_model_id + ","
						+ discount_amount + ","
						+ discount_month + ","
						+ discount_entry_id + ","
						+ discount_entry_date + ","
						+ "0,"
						+ "''"
						+ ")";
				// SOP("strsql===" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					discount_id = rs.getString(1);
				}
				rs.close();
				conntx.commit();

			} catch (Exception ex) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
	public void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_discount "
						+ " SET "
						+ " discount_jobtitle_id = " + discount_jobtitle_id + ","
						+ " discount_brand_id = " + discount_brand_id + ","
						+ " discount_model_id = " + discount_model_id + ","
						+ " discount_amount = " + discount_amount + ","
						+ " discount_month = " + discount_month + ","
						+ " discount_modified_id = " + discount_modified_id + ","
						+ " discount_modified_date = " + discount_modified_date + ""
						+ " WHERE discount_id= " + discount_id;

				// SOP("StrSql====" + StrSql);

				stmttx.execute(StrSql);
				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}

	}

	public void DeleteFields() throws SQLException {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_discount"
				+ " WHERE discount_id = " + discount_id;
		updateQuery(StrSql);

	}

	public void PopulateFields(HttpServletResponse response) throws SQLException {
		CachedRowSet crs = null;
		StrSql = "SELECT"
				+ " discount_jobtitle_id,"
				+ " discount_brand_id,"
				+ " discount_model_id,"
				+ " discount_amount,"
				+ " discount_month,"
				+ " discount_entry_id,"
				+ " discount_entry_date,"
				+ " discount_modified_id,"
				+ " discount_modified_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_discount"
				+ " WHERE 1 = 1 "
				+ " AND discount_id =" + discount_id;
		// SOP("strsql====" + StrSql);
		crs = processQuery(StrSql, 0);
		while (crs.next()) {
			discount_jobtitle_id = crs.getString("discount_jobtitle_id");
			discount_brand_id = crs.getString("discount_brand_id");
			discount_model_id = crs.getString("discount_model_id");
			discount_amount = crs.getDouble("discount_amount") + "";
			dr_year = crs.getString("discount_month").substring(0, 4);
			dr_month = crs.getString("discount_month").substring(4, 6);
			year = dr_year;
			month = dr_month;
			discount_entry_id = crs.getString("discount_entry_id");
			if (!discount_entry_id.equals("")) {
				discount_entry_by = Exename(comp_id, Integer.parseInt(discount_entry_id));
			}
			entry_date = strToLongDate(crs.getString("discount_entry_date"));
			discount_modified_id = crs.getString("discount_modified_id");
			if (!discount_modified_id.equals("0")) {
				discount_modified_by = Exename(comp_id, Integer.parseInt(discount_modified_id));
				modified_date = strToLongDate(crs.getString("discount_modified_date"));
			}

		}
		crs.close();
	}
	public String PopulateJobtitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jobtitle_id, jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_jobtitle "
					+ " ORDER BY jobtitle_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jobtitle_id"));
				Str.append(StrSelectdrop(crs.getString("jobtitle_id"), discount_jobtitle_id));
				Str.append(">").append(crs.getString("jobtitle_desc")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String discount_brand_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1"
					+ " And model_brand_id=" + discount_brand_id
					+ " AND model_active = 1"
					+ " AND model_sales = 1";
			StrSql += " ORDER BY model_name";
//			SOP("StrSql-----PopulateModel----" + StrSql);
//			SOP("discount_brand_id===" + discount_brand_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_model\" id=\"dr_model\" class=\"form-control\">");
			Str.append("<option value=\"0\"> ALL Model </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), discount_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateBrand(String discount_brand_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Brand</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), discount_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 00>Select</option>");
		Str.append("<option value = 01").append(Selectdrop(1, month)).append(">January</option>\n");
		Str.append("<option value = 02").append(Selectdrop(2, month)).append(">February</option>\n");
		Str.append("<option value = 03").append(Selectdrop(3, month)).append(">March</option>\n");
		Str.append("<option value = 04").append(Selectdrop(4, month)).append(">April</option>\n");
		Str.append("<option value = 05").append(Selectdrop(5, month)).append(">May</option>\n");
		Str.append("<option value = 06").append(Selectdrop(6, month)).append(">June</option>\n");
		Str.append("<option value = 07").append(Selectdrop(7, month)).append(">July</option>\n");
		Str.append("<option value = 08").append(Selectdrop(8, month)).append(">August</option>\n");
		Str.append("<option value = 09").append(Selectdrop(9, month)).append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month)).append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month)).append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month)).append(">December</option>\n");
		return Str.toString();
	}

	public String PopulateYear(String dr_year) {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear - 1; i <= curryear + 1; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), dr_year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
