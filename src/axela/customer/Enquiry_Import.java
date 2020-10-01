package axela.customer;
//divya 18th may

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

//import javax.servlet.http.HttpSession;
import cloudify.connect.Connect;

public class Enquiry_Import extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String enquiry_id = "";
	public String enquiry_branch_id = "";
	public String enquiry_dmsno = "";
	public String enquiry_date = "";
	public String enquiry_close_date = "";
	public String enquiry_emp_id = "";
	public String emp_name = "";
	public String enquiry_custtype_id = "";
	public String enquiry_title = "";
	public String customer_id = "";
	public String customer_name = "";
	public String contact_id = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String customer_address = "";
	public String contact_address = "";
	public String customer_city_id = "";
	public String customer_pin = "";
	public String contact_pin = "";
	public String customer_mobile = "";
	public String contact_mobile = "";
	public String customer_email = "";
	public String contact_email = "";
	public String enquiry_model_id = "";
	public String model_code = "";
	public String model_name = "";
	public String enquiry_item_id = "";
	public String item_code = "";
	public String item_name = "";
	public String enquiry_priorityenquiry_id = "";
	public String enquiry_soe_id = "";
	public String enquiry_status_id = "";
	public String enquiry_buyertype_id = "";
	public String followup_desc = "";
	public String entry_date = "";
	public String BranchAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String contact_title_id = "";
	public String update = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {

			// update = PadQuotes(request.getParameter("update"));
			// if (update.equals("yes")) {
			// updateenquirydate();
			// } else {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			entry_date = ToLongDate(kknow());
			StrSql = "select * from data_enquiry where `Mobile Number`!='' "
					// + " and enquiry_id<=200";
					// + " and enquiry_id>200 and enquiry_id<=400";
					// + " and enquiry_id>500 and enquiry_id<=600";
					+ " and enquiry_id>600";
			// ResultSet rset = null;
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				enquiry_branch_id = CNumeric(PadQuotes(crs.getInt("Dealer Location") + ""));
				enquiry_dmsno = PadQuotes(crs.getString("Enquiry No."));
				// enquiry_date = ConvertShortDateToStr(crs.getString("Enquiry Date"));
				enquiry_date = ConvertShortDateToStr(fmtShr2tofmtShr1(crs.getString("Enquiry Date")));
				// String dat =strToShortDate(enquiry_date);
				// SOP("enquiry_date--" + enquiry_date);
				// SOP("dat--"+dat);
				enquiry_close_date = "20130601000000";
				enquiry_emp_id = CNumeric(PadQuotes(crs.getInt("DSE Code") + ""));
				emp_name = PadQuotes(crs.getString("DSE Name"));
				enquiry_custtype_id = CNumeric(PadQuotes(crs.getInt("Customer Type") + ""));
				contact_title_id = CNumeric(PadQuotes(crs.getInt("Title_Name") + ""));
				customer_name = PadQuotes(crs.getString("Prospect Name"));
				customer_name = customer_name.replaceAll("\\\\", " ");
				// SOP("customer_name--"+customer_name);
				if (customer_name.contains(" ")) {
					if (customer_name.split(" ").length > 2) {
						contact_fname = customer_name.split(" ")[0] + " " + customer_name.split(" ")[1];
						contact_lname = customer_name.split(" ")[2];
					} else if (customer_name.split(" ").length > 1) {
						contact_fname = customer_name.split(" ")[0];
						contact_lname = customer_name.split(" ")[1];
					} else {
						contact_lname = "";
					}
				} else {
					contact_fname = customer_name;
				}
				customer_address = PadQuotes(crs.getString("Address"));
				customer_pin = PadQuotes(crs.getString("Pin Code"));
				customer_mobile = PadQuotes(crs.getString("Mobile Number"));
				customer_mobile = customer_mobile;
				// if (customer_mobile.contains("91-")) {
				// customer_mobile = customer_mobile.substring(3, 12);
				// }
				customer_email = PadQuotes(crs.getString("Email-Id"));
				model_code = PadQuotes(crs.getString("Model Code"));
				model_name = PadQuotes(crs.getString("Model Name"));
				item_code = PadQuotes(crs.getString("Variant Code"));
				item_name = PadQuotes(crs.getString("Variant Name"));
				enquiry_priorityenquiry_id = PadQuotes(crs.getString("Enquiry Status"));
				if (enquiry_priorityenquiry_id.equals("Hot")) {
					enquiry_priorityenquiry_id = "1";
				} else {
					enquiry_priorityenquiry_id = "2";
				}
				enquiry_soe_id = CNumeric(PadQuotes(crs.getInt("Source") + ""));
				enquiry_buyertype_id = CNumeric(PadQuotes(crs.getInt("Buyer Type") + ""));
				enquiry_status_id = PadQuotes(crs.getString("DropReason"));
				if (enquiry_status_id.equals("")) {
					enquiry_status_id = "1";
				} else {
					enquiry_status_id = "3";
				}
				followup_desc = PadQuotes(crs.getString("Feedback or Remarks"));

				StrSql = "select customer_mobile1 "
						+ " from " + compdb(comp_id) + "axela_customer "
						// + " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id = customer_id"
						+ " where customer_mobile1 = '" + customer_mobile + "'";
				// SOP("StrSql--" + StrSql);
				ResultSet rset = processQuery(StrSql, 0);

				if (!rset.isBeforeFirst()) {
					StrSql = "Insert into " + compdb(comp_id) + "axela_customer ("
							+ " customer_branch_id,"
							+ " customer_name,"
							+ " customer_address,"
							+ " customer_city_id,"
							+ " customer_pin,"
							+ " customer_mobile1,"
							+ " customer_email1,"
							+ " customer_emp_id,"
							+ " customer_active,"
							+ " customer_notes,"
							+ " customer_entry_id,"
							+ " customer_entry_date)"
							+ " values ("
							+ " " + enquiry_branch_id + ","
							+ " '" + toTitleCase(customer_name) + "',"
							+ " '" + customer_address + "',"
							+ " '14',"
							+ " '" + customer_pin + "',"
							+ " '" + customer_mobile + "',"
							+ " '" + customer_email + "',"
							+ " " + enquiry_emp_id + ","
							+ " '1',"
							+ " '',"
							+ " " + enquiry_emp_id + ","
							+ " '" + entry_date + "')";
					// SOP("StrSql--" + StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet crs1 = stmttx.getGeneratedKeys();
					while (crs1.next()) {
						customer_id = crs1.getString(1);
					}
					crs1.close();

					StrSql = "Insert into " + compdb(comp_id) + "axela_customer_contact ("
							+ " contact_title_id,"
							+ " contact_fname,"
							+ " contact_lname,"
							+ " contact_customer_id,"
							+ " contact_address,"
							+ " contact_city_id,"
							+ " contact_pin,"
							+ " contact_mobile1,"
							+ " contact_email1,"
							+ " contact_active,"
							+ " contact_notes,"
							+ " contact_entry_id,"
							+ " contact_entry_date)"
							+ " values ("
							+ " " + contact_title_id + ","
							+ " '" + toTitleCase(contact_fname) + "',"
							+ " '" + toTitleCase(contact_lname) + "',"
							+ " " + customer_id + ","
							+ " '" + customer_address + "',"
							+ " 14,"
							+ " '" + customer_pin + "',"
							+ " '" + customer_mobile + "',"
							+ " '" + customer_email + "',"
							+ " '1',"
							+ " '',"
							+ " " + enquiry_emp_id + ","
							+ " '" + entry_date + "')";
					// SOP("StrSql--" + /StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet crs2 = stmttx.getGeneratedKeys();
					while (crs2.next()) {
						contact_id = crs2.getString(1);
					}
					crs2.close();

					StrSql = "Insert into " + compdb(comp_id) + "axela_sales_enquiry"
							+ " (enquiry_branch_id,"
							+ " enquiry_no,"
							+ " enquiry_customer_id,"
							+ " enquiry_contact_id,"
							+ " enquiry_date,"
							+ " enquiry_close_date,"
							+ " enquiry_item_id,"
							+ " enquiry_model_id,"
							+ " enquiry_title,"
							+ " enquiry_custtype_id,"
							+ " enquiry_buyertype_id,"
							+ " enquiry_emp_id,"
							+ " enquiry_stage_id,"
							+ " enquiry_status_id,"
							+ " enquiry_soe_id,"
							+ " enquiry_campaign_id,"
							+ " enquiry_priorityenquiry_id,"
							+ " enquiry_dmsno,"
							+ " enquiry_notes,"
							+ " enquiry_entry_id,"
							+ " enquiry_entry_date)"
							+ " values"
							+ " (" + enquiry_branch_id + ","
							+ " (select coalesce(max(enquiry.enquiry_no),0)+1 "
							+ " from " + compdb(comp_id) + "axela_sales_enquiry as enquiry "
							+ " where enquiry.enquiry_branch_id  = " + enquiry_branch_id + "),"
							+ " " + customer_id + ","
							+ " " + contact_id + ","
							+ " '"
							+ enquiry_date
							+ "',"
							+ " '"
							+ enquiry_close_date
							+ "',"
							// + " @item_id:=(SELECT COALESCE((SELECT item_id FROM " + compdb(comp_id) +
							// "axela_inventory_item WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + item_code + "') > 6, SUBSTR('" + item_code + "', 1, 6), '" +
							// item_code + "')) LIMIT 1), 0)),"
							// + " (select coalesce((select item_model_id from " + compdb(comp_id) + "axela_inventory_item where item_id = @item_id),0)),"
							+ " (SELECT COALESCE((SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('"
							+ item_code + "') > 6, SUBSTR('" + item_code + "', 1, 6), '" + item_code
							+ "')) LIMIT 1), 0)),"
							// + " (SELECT COALESCE((SELECT item_model_id FROM " + compdb(comp_id) +
							// "axela_inventory_item WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + item_code + "') > 6, SUBSTR('" + item_code + "', 1, 6), '" +
							// item_code + "')) LIMIT 1), 0)),"
							+ " @model_id:=(SELECT COALESCE((SELECT item_model_id FROM " + compdb(comp_id)
							+ "axela_inventory_item WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + item_code + "') > 6, SUBSTR('" + item_code + "', 1, 6), '"
							+ item_code + "')) LIMIT 1), 0)),"
							+ " (select COALESCE((select concat('New', ' ', model_name) from " + compdb(comp_id) + "axela_inventory_item_model where model_id = @model_id), 'New Enquiry')),"
							// + " (select COALESCE((select concat('New', ' ', model_name) from " + compdb(comp_id) + "axela_inventory_item_model where model_name = '" + model_name +
							// "'), 'New Enquiry')),"
							+ " " + enquiry_custtype_id + ","
							+ " " + enquiry_buyertype_id + ","
							+ " " + enquiry_emp_id + ","
							+ " 2,"
							+ " '" + enquiry_status_id + "', "
							+ " " + enquiry_soe_id + ","
							+ " 1,"
							+ " '" + enquiry_priorityenquiry_id + "',"
							+ " '" + enquiry_dmsno + "',"
							+ " '',"
							+ " " + enquiry_emp_id + ","
							+ " '" + entry_date + "')";
					// SOP("StrSql--" + StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs5 = stmttx.getGeneratedKeys();
					while (rs5.next()) {
						enquiry_id = rs5.getString(1);
					}
					rs5.close();

					StrSql = " INSERT into " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " ("
							+ " followup_enquiry_id,"
							+ " followup_emp_id,"
							+ " followup_followup_time,"
							+ " followup_followuptype_id,"
							+ " followup_desc,"
							+ " followup_entry_id,"
							+ " followup_entry_time)"
							+ " values "
							+ " ("
							+ " '" + enquiry_id + "',"
							+ " " + enquiry_emp_id + ","
							+ " '" + entry_date + "',"
							+ " 1,"
							+ " '" + followup_desc + "',"
							+ " " + enquiry_emp_id + ","
							+ " '" + entry_date + "')";
					stmttx.execute(StrSql);
					// stmttx.execute(StrSql);
					// }
					// stmttx.executeBatch();
					// rset.close();

				}
				rset.close();
			}

			crs.close();
			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	// public void updateenquirydate() throws SQLException {
	// String enquiry_dat = "", id = "";
	// connectDB();
	// conntx = myBroker.getConnection();
	// conntx.setAutoCommit(false);
	// stmttx = conntx.createStatement();
	// StrSql = "select enquiry_id,enquiry_date, `Enquiry Date` from " + compdb(comp_id) + "axela_sales_enquiry"
	// + " inner join data_enquiry on `Enquiry No.` = enquiry_dmsno ";
	// try {
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// enquiry_dat = ConvertShortDateToStr(fmtShr2tofmtShr1(crs.getString("Enquiry Date")));
	// id = crs.getString("enquiry_id");
	// // SOP("enquiry_dat--" + enquiry_dat);
	// // SOP("id--" + id);
	// StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry set enquiry_date = '" + enquiry_dat + "'"
	// + " where enquiry_id = " + id + " ";
	// // SOP("StrSql--" + StrSql);
	// // + " and enquiry_entry_date = '20130522155415'";
	// // updateQuery(StrSql);
	// stmttx.addBatch(StrSql);
	// }
	// crs.close();
	// stmttx.executeBatch();
	// conntx.commit();
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("connection is closed.....");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// } finally {
	// stmttx.close();
	// myBroker.freeConnection(conntx);
	// myBroker.destroy();
	// if (conntx != null && !conntx.isClosed()) {
	// conntx.close();
	// }
	// }
	// }
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}
}
