package axela.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Customer_GST_Update extends Connect {

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
	public String customer_name = "";
	public String customer_address = "";
	public String customer_pan_no = "";
	public String customer_city_id = "";
	public String customer_city = "";
	public byte[] gst_doc_value = null;
	public boolean isMultipart;
	public InputStream inputStream = null;
	public List<FileItem> items = null;
	public ServletFileUpload upload = null;
	DiskFileItemFactory factory = null;
	Map<String, String> fieldsMap = new HashMap<String, String>();
	public String customer_gst_regdate = "";
	public String gst_regdate = "";
	public String customer_gst_no = "";
	// public String customer_gst_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String department_entry_id = "0";
	public String department_entry_date = "";
	public String department_modified_id = "0";
	public String department_modified_date = "";
	public String entry_by = "", entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public PreparedStatement psmt = null;
	public CachedRowSet crs = null;
	public Connection conn = null;
	public City_Check citycheck = new City_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
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
				PopulateCustomerDetails(response);
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
							// fieldsMap.put("emp_photo", item.getName());
							inputStream = item.getInputStream();
							// if (!item.getName().equals("") && item.getName().contains(".")) {
							// inputStream = resizeImage(inputStream, "1024", fileext(item.getName()).substring(1));
							// }
							// fieldsMap.put("fileName", item.getFieldName()
							// + "");
						}
					}
					for (String key : fieldsMap.keySet()) {
						// SOP("fieldsMap.get(key)========" + fieldsMap.get(key));
						if (fieldsMap.get(key).equals("Add GST")) {
							GetValues();
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "GST Details Added Successfully!";
								// response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=" + msg));
							}
						}
						// else if (fieldsMap.get(key).equals("Update Photo")) {
						// GetValues();
						// UpdateFields();
						//
						// if (!msg.equals("")) {
						// msg = "Error!" + msg;
						// } else {
						// msg = "Image updated successfully!";
						// response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=" + msg));
						//
						// }
						// } else if (fieldsMap.get(key).equals("Delete Photo")) {
						// DeleteFields();
						// if (!msg.equals("")) {
						// msg = "Error!" + msg;
						// } else {
						// response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=" + msg));
						// }
						// }
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

	protected void GetValues() throws ServletException, IOException {
		customer_address = PadQuotes(fieldsMap.get("txt_customer_address"));
		customer_city_id = PadQuotes(fieldsMap.get("maincity"));
		customer_pan_no = PadQuotes(fieldsMap.get("txt_customer_pan_no"));
		gst_regdate = fieldsMap.get("txt_customer_gst_regdate");
		customer_gst_regdate = ConvertShortDateToStr(gst_regdate);
		customer_gst_no = PadQuotes(fieldsMap.get("txt_customer_gst_no"));

	}

	protected void CheckForm() {

		if (!customer_pan_no.equals("") && !customer_pan_no.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
			msg = msg + "<br>Pan Number Invalid!";
		}
		if (customer_gst_regdate.equals("")) {
			msg = msg + "<br>Select GST Reg Date!";
		}
		if (customer_gst_no.equals("")) {
			msg = msg + "<br>Enter GST No!";
		}
		// if (((inputStream.available()) == 0)) {
		// msg = msg + "<br>Select Document!";
		// }

	}

	protected void PopulateCustomerDetails(HttpServletResponse response) {
		try {
			StrSql = "Select * FROM " + compdb(comp_id) + "axela_customer WHERE customer_id = " + customer_id + "";
			SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					customer_name = crs.getString("customer_name");
					customer_address = crs.getString("customer_address");
					customer_pan_no = crs.getString("customer_pan_no");
					customer_city_id = crs.getString("customer_city_id");
					// customer_city = ExecuteQuery("Select city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + customer_city_id);

				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Department!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		int count = 0;
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer" + " SET ";

				if (inputStream != null) {
					StrSql += " customer_address =?,"
							+ " customer_city_id =?,"
							+ " customer_pan_no =?,"
							+ " customer_gst_regdate =?,"
							+ " customer_gst_no = ?,"
							+ " customer_gst_doc=?"
							+ " WHERE customer_id = " + customer_id;
				}

				// SOPError("StrSql--" + StrSqlBreaker(StrSql));
				conn = connectDB();
				psmt = conn.prepareStatement(StrSql);

				if (inputStream != null) {
					psmt.setString(++count, customer_address);
					psmt.setString(++count, customer_city_id);
					psmt.setString(++count, customer_pan_no);
					psmt.setString(++count, customer_gst_regdate);
					psmt.setString(++count, customer_gst_no);
					psmt.setBlob(++count, inputStream);
				}

				psmt.executeUpdate();
				SOP("document added");
			} catch (Exception ex) {
				SOPError("Terracle===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				try {

					if (psmt != null) {
						psmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception ex) {
					SOPError("Terracle===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
				// The connection is returned to the Broker

			}
		}
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
