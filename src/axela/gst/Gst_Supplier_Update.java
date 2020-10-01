package axela.gst;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Gst_Supplier_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String customer_id = "0";
	public String QueryString = "";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String customer_name = "";
	public String contact_address = "";
	public String customer_pan_no = "";
	public String customer_arn_no = "";
	public String contact_city_id = "0";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_pin = "";
	public String branch_name = "", customer_branch_id = "1", customer_type = "2";
	public int contact_customer_id = 0;
	public String customer_itstatus_id = "0";
	public String customer_contact_name = "";
	public byte[] gst_doc_value = null;
	public boolean isMultipart;
	public InputStream inputStream = null;
	public List<FileItem> items = null;
	public ServletFileUpload upload = null;
	DiskFileItemFactory factory = null;
	Map<String, String> fieldsMap = new HashMap<String, String>();
	public String customer_gst_regdate = "";
	public String customer_gst_doc_value = "";
	public String gst_regdate = "";
	public String customer_gst_no = "";
	// public String customer_gst_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String entry_by = "", entry_date = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String modified_by = "";
	public String modified_date = "";
	public PreparedStatement psmt = null;
	public ResultSet rs = null;
	public CachedRowSet crs = null;
	public Connection conn = null;
	public City_Check citycheck = new City_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				factory = new DiskFileItemFactory();
				upload = new ServletFileUpload(factory);
				isMultipart = ServletFileUpload.isMultipartContent(request);
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				msg = PadQuotes(request.getParameter("msg"));
				gst_regdate = strToShortDate(ToLongDate(kknow()));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if (isMultipart) {
					items = upload.parseRequest(request);
					// SOP("items======" + items);
					for (FileItem item : items) {
						if (item.isFormField()) {
							fieldsMap.put(item.getFieldName(), item.getString());
						} else {
							fieldsMap.put("customer_gst_doc_value", item.getName());
							inputStream = item.getInputStream();
							// if (!item.getName().equals("") && item.getName().contains(".")) {
							// inputStream = resizeImage(inputStream, "1024", fileext(item.getName()).substring(1));
							// }
							// fieldsMap.put("fileName", item.getFieldName()
							// + "");
						}
					}
					for (String key : fieldsMap.keySet()) {
						// SOP("fieldsMap.get(key)========" + fieldsMap.get(key).contains("Add Vendor"));
						if (fieldsMap.get(key).equals("Add Vendor")) {
							GetValues();

							AddSupplierDetails(response);
							// SOP("after addcontact fields");
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../gst/supplier-gst.jsp?msg=Thanks For Updateing Company Information!"));
							}
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

	public void AddSupplierDetails(HttpServletResponse response) throws SQLException {
		CheckForm(response);
		if (msg.equals("")) {

			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);

				AddCustomerFields(response);
				AddContactFields(response);

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOP("Transaction Error==");
					conntx.rollback();
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
			} finally {
				conntx.setAutoCommit(true);
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void GetValues() throws ServletException, IOException {

		contact_title_id = PadQuotes(fieldsMap.get("dr_title"));
		customer_name = PadQuotes(fieldsMap.get("txt_customer_name"));
		contact_fname = PadQuotes(fieldsMap.get("txt_contact_fname"));
		contact_lname = PadQuotes(fieldsMap.get("txt_contact_lname"));
		customer_pan_no = PadQuotes(fieldsMap.get("txt_customer_pan_no"));
		gst_regdate = PadQuotes(fieldsMap.get("txt_customer_gst_regdate"));
		customer_itstatus_id = CNumeric(PadQuotes(fieldsMap.get("dr_customer_itstatus_id")));
		// customer_gst_regdate = ConvertShortDateToStr(gst_regdate);
		customer_gst_no = PadQuotes(fieldsMap.get("txt_customer_gst_no"));
		customer_arn_no = PadQuotes(fieldsMap.get("txt_customer_arn_no"));
		customer_gst_doc_value = PadQuotes(fieldsMap.get("customer_gst_doc_value"));
		contact_mobile1 = PadQuotes(fieldsMap.get("txt_contact_mobile1"));
		contact_phone1 = PadQuotes(fieldsMap.get("txt_contact_phone1"));
		contact_email1 = PadQuotes(fieldsMap.get("txt_contact_email1"));
		contact_address = PadQuotes(fieldsMap.get("txt_contact_address"));
		contact_city_id = CNumeric(PadQuotes(fieldsMap.get("maincity")));
		contact_pin = PadQuotes(fieldsMap.get("txt_contact_pin"));
	}

	protected void CheckForm(HttpServletResponse response) {
		String duplicate_gst = "";
		try {
			if (customer_name.equals("")) {
				msg = msg + "<br>Enter the Customer Name!";
			} else {
				customer_name = toTitleCase(customer_name);
			}

			if (!customer_name.equals("")) {
				StrSql = "SELECT customer_name FROM " + compdb(comp_id) + "axela_customer where customer_name = '" + customer_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and customer_id != " + customer_id;
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Customer found!";
				}
			}

			if (contact_title_id.equals("0")) {
				msg += "<br>Select Title!";
			}

			if (contact_fname.equals("")) {
				msg += "<br>Enter the Contact Person First Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}

			if (!contact_lname.equals("")) {
				contact_lname = toTitleCase(contact_lname);
			}

			if (customer_gst_no.equals("")) {
				msg = msg + "<br>Enter GSTIN!";
			} else if (!customer_gst_no.equals("") && !customer_gst_no.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
				msg = msg + "<br>Enter Valid GSTIN!";

			} else {
				duplicate_gst = CNumeric(PadQuotes(ExecuteQuery("SELECT customer_id FROM " + compdb(comp_id) + "axela_customer WHERE customer_gst_no = '" + customer_gst_no + "'")));
				if (!duplicate_gst.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("../gst/supplier-gst.jsp?msg=Thanks Your GST Details already updated for GST No: " + customer_gst_no + "!"));
					msg = msg + "<br>GST No: " + customer_gst_no + " already exists!";
				}
			}

			if (gst_regdate.equals("")) {
				msg = msg + "<br>Select GSTIN Date!";
			} else if (!gst_regdate.equals("") && isValidDateFormatShort(gst_regdate)) {
				customer_gst_regdate = ConvertShortDateToStr(gst_regdate);
			} else {
				msg = msg + "<br>Enter Valid GSTIN Date!";

			}

			if (!customer_arn_no.equals("") && !customer_arn_no.matches("[a-zA-Z]{2}\\d{6}\\d{6}\\d{1}")) {
				msg = msg + "<br>Enter Valid ARN No !";
			}
			if (customer_pan_no.equals("")) {
				msg = msg + "<br>Enter PAN!";
			} else if (!customer_pan_no.equals("") && !customer_pan_no.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
				msg = msg + "<br>PAN Invalid!";
			}
			if (customer_itstatus_id.equals("0")) {
				msg = msg + "<br>Select Status!";
			}

			if (contact_mobile1.equals("91-")) {
				contact_mobile1 = "";
			}

			if (contact_mobile1.equals("")) {
				msg += "<br>Enter Contact Mobile !";
			} else {
				if (!IsValidMobileNo11(contact_mobile1)) {
					msg += "<br>Enter Valid Mobile !";
				}
			}

			if (contact_email1.equals("")) {
				msg += "<br>Enter Email !";
			}
			if (!contact_email1.equals("") && IsValidEmail(contact_email1) != true) {
				msg += "<br>Enter valid Email !";
			} else {
				contact_email1 = contact_email1.toLowerCase();
			}

			if (contact_address.equals("")) {
				msg += "<br>Enter Address!";
			}
			if (contact_city_id.equals("0")) {
				msg += "<br>Enter City!";
			}
			if (contact_pin.equals("")) {
				msg += "<br>Enter Pin Code!";
			} else if (!contact_pin.equals("") && !isNumeric(contact_pin)) {
				msg += "<br>Enter Numeric Pin Code!";
			}

			// if (((inputStream.available()) == 0)) {
			// msg = msg + "<br>Select Document!";
			// }
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}

	}
	protected void AddCustomerFields(HttpServletResponse response) throws SQLException {
		CheckForm(response);
		int count = 0;
		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_phone1,"
					+ " customer_email1,"
					+ " customer_address,"
					+ " customer_city_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_pin,"
					+ " customer_pan_no,";
			if (inputStream.available() != 0) {
				StrSql += " customer_gst_doc,"
						+ " customer_gst_doc_value,";
			}
			StrSql += " customer_active,"
					+ " customer_gst_regdate,"
					+ " customer_notes,"
					+ " customer_gst_no,"
					+ " customer_arn_no,"
					+ " customer_itstatus_id,"
					+ " customer_user_entry_date"
					+ " )"
					+ " VALUES "
					+ "(?,?,?,?,?,?,?,?,?,?,"
					+ "?,";
			if (inputStream.available() != 0) {
				StrSql += "?,?,";
			}
			StrSql += "?,?,?,?,?,?,?)";

			psmt = conntx.prepareStatement(StrSql, Statement.RETURN_GENERATED_KEYS);

			psmt.setString(++count, customer_branch_id);
			psmt.setString(++count, customer_name);
			psmt.setString(++count, contact_mobile1);
			psmt.setString(++count, contact_phone1);
			psmt.setString(++count, contact_email1);
			psmt.setString(++count, contact_address);
			psmt.setString(++count, contact_city_id);
			psmt.setInt(++count, 31);
			psmt.setInt(++count, 2);
			psmt.setString(++count, contact_pin);
			psmt.setString(++count, customer_pan_no);
			if (inputStream.available() != 0) {
				psmt.setBlob(++count, inputStream);
				psmt.setString(++count, customer_gst_doc_value);
			}
			psmt.setString(++count, "1");
			psmt.setString(++count, customer_gst_regdate);
			psmt.setString(++count, "");
			psmt.setString(++count, customer_gst_no);
			psmt.setString(++count, customer_arn_no);
			psmt.setString(++count, customer_itstatus_id);
			psmt.setString(++count, ToLongDate(kknow()));
			psmt.executeUpdate();
			// SOP("StrSql==1111==" + StrSql);
			rs = psmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				contact_customer_id = rs.getInt(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}

	}
	protected void AddContactFields(HttpServletResponse response) throws SQLException {
		CheckForm(response);
		int count = 0;
		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_mobile1,"
					+ " contact_phone1,"
					+ " contact_email1,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			psmt = conntx.prepareStatement(StrSql, Statement.RETURN_GENERATED_KEYS);
			psmt.setInt(++count, contact_customer_id);
			psmt.setInt(++count, 2);
			psmt.setString(++count, contact_title_id);
			psmt.setString(++count, contact_fname);
			psmt.setString(++count, contact_lname);
			psmt.setString(++count, contact_mobile1);
			psmt.setString(++count, contact_phone1);
			psmt.setString(++count, contact_email1);
			psmt.setString(++count, contact_address);
			psmt.setString(++count, contact_city_id);
			psmt.setString(++count, contact_pin);
			psmt.setString(++count, "1");
			psmt.setString(++count, "");
			psmt.setString(++count, "0");

			// SOP("StrSql===AddContactFields===" + StrSql);
			psmt.executeUpdate();
			// SOP("alfasfjajlsk");

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateItStatus() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT itstatus_id, itstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_itstatus"
					+ " ORDER BY itstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("itstatus_id"));
				Str.append(StrSelectdrop(crs.getString("itstatus_id"), customer_itstatus_id));
				Str.append(">").append(crs.getString("itstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		Str.append("</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", "2"))
				.append(">").append("Supplier");
		Str.append("</option>\n");

		return Str.toString();
	}

	protected void DeleteFields() {
		StrSql = "SELECT emp_department_id FROM " + compdb(comp_id) + "axela_emp where emp_department_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Department is Associated with Executive!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_emp_department where department_id = " + customer_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
