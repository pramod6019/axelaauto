package axela.customer;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cloudify.connect.Connect;

public class Gst_Document_Update extends Connect {

	private String customer_gst_doc_value;
	public String LinkHeader = "";
	public String add = "";
	public String update = "";
	public String addB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_id = "0";
	public String doc_value = "";
	private String ImgWidth = "";
	public String doc_title = "";
	public double doc_size = 0.0;
	public long config_doc_size;
	public String config_doc_format = "";
	public String[] docformat;
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String exe_id = "0";
	public String customer_id = "0";
	public String asset_id = "0";
	public String insur_id = "0";
	public static String ImageFormats = ".jpg, .jpeg, .gif, .png";
	public String img_title = "";
	public String fileName1 = "";
	// public String project_id = "0";
	public String task_id = "0";
	public String faq_id = "0";
	public String brochure_rateclass_id = "0";
	public String brochure_model_id = "0";
	public String brochure_item_id = "0";
	public String accessAdd = "";
	public String accessEdit = "";
	public String accessDelete = "";
	public String name = "";
	public String id = "0";
	public String fieldName = "";
	public String tableName = "";
	public String group = "";
	public String fieldId = "";
	public String url = "";
	public String filePrefix = "";
	public String docTable = "";
	public String updateUrl = "";
	public String docFieldId = "";
	public String msgValid = "";
	public String document = "";
	public String brochure = "";
	public String voucher_id = "";
	public String project_name = "";
	public String projectbrochure_project_id = "0";
	public String projectbrochure_id = "0";
	public boolean isMultipart;
	public String button_name = "";
	public BufferedImage bufferedImage = null;
	public String tag = "";
	public String url_tag = "";

	public InputStream inputStream = null;
	public List<FileItem> items = null;
	public ServletFileUpload upload = null;
	DiskFileItemFactory factory = null;
	Map<String, String> fieldsMap = new HashMap<String, String>();
	public java.sql.PreparedStatement psmt = null;
	public byte[] doc_data = null;
	public Connection conn = null;
	public String itemsize = "0";
	HttpServletResponse response;
	HttpServletRequest request;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				QueryString = PadQuotes(request.getQueryString());
				factory = new DiskFileItemFactory();
				upload = new ServletFileUpload(factory);
				isMultipart = ServletFileUpload.isMultipartContent(request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				document = PadQuotes(request.getParameter("document"));
				brochure = PadQuotes(request.getParameter("brochure"));
				url_tag = PadQuotes(request.getParameter("tag"));
				// SOP("update=======" + update);
				msg = PadQuotes(request.getParameter("msg"));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if (url_tag.equals("vendors")) {
					tag = "Supplier";
				} else {
					tag = "Customer";
				}

				if (!customer_id.equals("0")) {
					// name = ExecuteQuery("Select testdrive_doc_title from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "");
					accessAdd = "emp_customer_add";
					accessEdit = "emp_customer_edit";
					accessDelete = "emp_customer_delete";
					savePath = TestDriveDocPath(comp_id);
					id = customer_id;
					fieldName = "customer_id";
					tableName = "" + compdb(comp_id) + "axela_customer";
					fieldId = "customer_id";
					url = "../customer/customer-list.jsp?customer_id=";
					filePrefix = "testdrivedoc_";
					docTable = "" + compdb(comp_id) + "axela_customer";
					updateUrl = "../customer/gst-document-update.jsp?customer_id=";
					docFieldId = "doc_enquiry_id";
					LinkHeader = "<a href=../portal/home.jsp>Home</a>"
							+ " &gt; <a href=customer.jsp>Customers</a>"
							+ " &gt; <a href=../customer/customer-list.jsp?all=yes&tag=" + url_tag + ">List " + tag + "s</a><b></b>"
							+ " &gt; <a href=../customer/gst-document-update.jsp?" + QueryString + ">Update GST Document</a><b>:</b>";
				}
				PopulateConfigDetails(response);

				CheckPerm(comp_id, accessAdd, request, response);
				String button_name = "";

				if (isMultipart) {
					items = upload.parseRequest(request);
					for (FileItem item : items) {
						if (item.isFormField()) {
							fieldsMap.put(item.getFieldName(), item.getString());
						} else {
							itemsize = String.valueOf(item.getSize());
							fieldsMap.put("itemsize", itemsize);
							fieldsMap.put("customer_gst_doc_value", item.getName());
							fieldsMap.put("gst_doc_modified_id", emp_id);
							fieldsMap.put("gst_doc_modified_date", ToLongDate(kknow()));
							inputStream = item.getInputStream();

						}

					}

				}
			}
			for (String key : fieldsMap.keySet()) {
				// SOP("fieldsMap.get(key)=====" + fieldsMap.get(key));
				if (fieldsMap.get(key).equals("Update GST Document")) {
					if (!customer_id.equals("0")) {
						if (ReturnPerm(comp_id, "emp_customer_edit", request).equals("1")) {

							GetValues();
							UpdateFields();

							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Image updated successfully!";
								if (!customer_id.equals("0")) {
									response.sendRedirect(response.encodeRedirectURL(url + id + "&msg=Gst Document updated successfully!"));
								}
							}
						}
						else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if (fieldsMap.get(key).equals("Delete GST Document")) {
					if (!customer_id.equals("0")) {
						if (ReturnPerm(comp_id, "emp_customer_delete", request).equals("1")) {
							GetValues();
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Image updated successfully!";
								if (!customer_id.equals("0")) {
									response.sendRedirect(response.encodeRedirectURL(url + id + "&msg=Driving Licence uploaded successfully!"));
								}
							}
						}
						else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (FileUploadException Fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			if (status.equals("add")) {
				response.sendRedirect(updateUrl + id + "&add=yes&msg=" + msg);
			}
			if (status.equals("update")) {
				response.sendRedirect(updateUrl + id + "&update=yes&doc_id=" + fieldId + "&msg=" + msg);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		// g.createHeadlessSmoothBufferedImage(originalImage, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	protected void GetValues() {
		customer_gst_doc_value = fieldsMap.get("customer_gst_doc_value");

		// SOP("itemsize----------g-----" + itemsize);

	}

	protected void UpdateFields() throws Exception {
		CheckForm();
		// int count = 0;
		if (msg.equals("")) {
			try {
				int count = 0;
				if (!customer_id.equals("0")) {
					StrSql = "UPDATE " + docTable
							+ " SET";

					if (inputStream.available() != 0) {
						StrSql += " customer_gst_doc = ?,"
								+ " customer_gst_doc_value=?";

					}

					StrSql += " WHERE customer_id = ?";
					// SOP("StrSql===update query===" + StrSql);
					conn = null;
					conn = connectDB();
					psmt = conn.prepareStatement(StrSql);
					if (inputStream.available() != 0) {
						psmt.setBlob(1, inputStream);
						psmt.setString(2, customer_gst_doc_value);
					}
					psmt.setString(3, customer_id);
					psmt.executeUpdate();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
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
					SOPError(this.getClass().getName());
					SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
				}

			}
		}
	}
	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				if (!customer_id.equals("0")) {
					StrSql = "UPDATE " + docTable
							+ " SET "
							+ " customer_gst_doc = ?,"
							+ " customer_gst_doc_value=?"
							+ " WHERE customer_id = ?";
					// }
					// SOP("StrSql===update query==1111=" + StrSql);
					conn = null;
					conn = connectDB();
					psmt = conn.prepareStatement(StrSql);
					// SOP("inputStream====update=====" + inputStream);
					psmt.setString(1, "");
					psmt.setString(2, "");
					psmt.setString(3, customer_id);

					// SOP("StrSql====" + StrSql);
					psmt.executeUpdate();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		String Msg1 = "", Msg2 = "";
		if (add.equals("yes") && customer_gst_doc_value.equals("")) {
			msg = msg + "<br>Select Image!";
		}
		// if ((Long.parseLong(itemsize) > (5 * 1024 * 1024)) && !testdrive_doc_value.equals("")) {
		// msg = msg + "<br>Image size should not exceed 5 Mb!";
		// }

		// SOP("testdrive_doc_value====" + testdrive_doc_value);

		msg = msg + Msg1 + Msg2;
	}
	protected void PopulateConfigDetails(HttpServletResponse response) {
		try {
			if (add.equals("yes")) {
				fieldName = "''";
				tableName = "''";
			}
			if (!fieldId.equals("")) {
				if (add.equals("yes")) {
					StrSql = "Select (config_doc_size * 1024 * 1024) AS doc_size, config_doc_size, config_doc_formats"
							+ " from " + compdb(comp_id) + "axela_config";
				} else {
					StrSql = "Select config_doc_size, config_doc_formats, " + fieldName
							+ " from " + compdb(comp_id) + "axela_config, " + tableName + "";
					if (brochure_rateclass_id.equals("0")) {
						StrSql = StrSql + " where " + fieldId + " = " + id + "";
					}
				}
				// SOP("StrSql-PopulateConfigDetails--" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						config_doc_format = crs.getString("config_doc_formats");
						if (update.equals("yes")) {
							name = crs.getString(fieldName);
						}
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order" + msg));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
