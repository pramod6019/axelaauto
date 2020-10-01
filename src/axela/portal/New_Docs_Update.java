package axela.portal;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import cloudify.connect.Connect;

public class New_Docs_Update extends Connect {

	private String testdrive_doc_value;
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
	public String doc_remarks = "";
	public String doc_entry_id = "0";
	public String doc_entry_by = "";
	public String doc_modified_id = "0";
	public String doc_entry_date = "";
	public String doc_modified_date = "";
	public String doc_modified_by = "";
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
	public String brand_id = "0", brochure_rateclass_id = "0", brochure_id = "0";
	public String brochure_model_id = "0";
	public String brochure_item_id = "0";
	public String item_name = "";
	public String ticketfaq_id = "0";
	public String enquiry_id = "0";
	public String preowned_id = "0";
	public String so_id = "0";
	public String ticket_id = "0";
	public String jc_id = "0";
	public String landbank_id = "0";
	public String accessAdd = "";
	public String accessEdit = "";
	public String accessDelete = "";
	public String project_id = "0";
	public String unit_id = "0";
	public String name = "";
	public String type = "";
	public String id = "0";
	public String fieldName = "";
	public String tableName = "";
	public String group = "";
	public String fieldId = "";
	public String booking_id = "0";
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
	public String testdrive_id = "0";
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
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				document = PadQuotes(request.getParameter("document"));
				brochure = PadQuotes(request.getParameter("brochure"));
				type = PadQuotes(request.getParameter("type"));
				SOP("type=======" + type);
				msg = PadQuotes(request.getParameter("msg"));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));

				if (!brochure_id.equals("0")) {
					doc_id = brochure_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if (!testdrive_id.equals("0") && type.equals("")) {
					// name = ExecuteQuery("Select testdrive_doc_title from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "");
					accessAdd = "emp_testdrive_add";
					accessEdit = "emp_testdrive_edit";
					accessDelete = "emp_testdrive_delete";
					savePath = TestDriveDocPath(comp_id);
					id = testdrive_id;
					fieldName = "testdrive_id";
					tableName = "" + compdb(comp_id) + "axela_sales_testdrive";
					fieldId = "testdrive_id";
					url = "../sales/testdrive-list.jsp?testdrive_id=";
					filePrefix = "testdrivedoc_";
					docTable = "" + compdb(comp_id) + "axela_sales_testdrive";
					updateUrl = "../portal/docs-update.jsp?testdrive_id=";
					docFieldId = "doc_enquiry_id";
					LinkHeader = "<a href=../sales/home.jsp>Home</a>"
							+ " &gt; <a href=../sales/index.jsp>Sales</a>"
							+ " &gt; <a href=../sales/testdrive.jsp>Test Drive</a>"
							+ " &gt; <a href=../sales/testdrive-list.jsp?all=yes>List Test Drive</a>"
							// + " &gt; <a href=../sales/testdrive-list.jsp?testdrive=" + testdrive_id + ">" + name + "</a>"
							+ " &gt; <a href=../portal/new-docs-update.jsp?" + QueryString + ">" + status + " Driving Licence</a>:";
				}

				if (!testdrive_id.equals("0") && type.equals("preowned")) {
					// name = ExecuteQuery("Select testdrive_doc_title from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "");
					accessAdd = "emp_testdrive_add";
					accessEdit = "emp_testdrive_edit";
					accessDelete = "emp_testdrive_delete";
					savePath = TestDriveDocPath(comp_id);
					id = testdrive_id;
					fieldName = "testdrive_id";
					tableName = "" + compdb(comp_id) + "axela_preowned_testdrive";
					fieldId = "testdrive_id";
					url = "../preowned/preowned-testdrive-list.jsp?testdrive_id=";
					filePrefix = "testdrivedoc_";
					docTable = "" + compdb(comp_id) + "axela_preowned_testdrive";
					updateUrl = "../portal/docs-update.jsp?testdrive_id=";
					docFieldId = "doc_enquiry_id";
					LinkHeader = "<a href=../sales/home.jsp>Home</a>"
							+ " &gt; <a href=../sales/index.jsp>Sales</a>"
							+ " &gt; <a href=../sales/testdrive.jsp>Test Drive</a>"
							+ " &gt; <a href=../sales/testdrive-list.jsp?all=yes>List Test Drive</a>"
							// + " &gt; <a href=../sales/testdrive-list.jsp?testdrive=" + testdrive_id + ">" + name + "</a>"
							+ " &gt; <a href=../portal/new-docs-update.jsp?" + QueryString + ">" + status + " Driving Licence</a>:";
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
							fieldsMap.put("testdrive_doc_value", item.getName());
							fieldsMap.put("testdrive_doc_modified_id", emp_id);
							fieldsMap.put("testdrive_doc_modified_date", ToLongDate(kknow()));
							inputStream = item.getInputStream();
							// SOPInfo("before inputStream reader===" + inputStream.available());
							byte[] data = IOUtils.toByteArray(inputStream);

							if (data != null) {
								// SOPInfo("before bufferedImage reader===" + data.length);
								bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
								// SOPInfo("bufferedImage===before===" + bufferedImage);
								if (bufferedImage != null) {
									// Calculate the target width and height
									float scale = 1;
									int targetWidth = 0;
									int targetHeight = 0;
									ImgWidth = PadQuotes("1024");
									targetWidth = (int) (bufferedImage.getWidth(null) * scale);
									targetHeight = (int) (bufferedImage.getHeight(null) * scale);
									// SOP("ImgWidth===" + ImgWidth);
									// SOP("targetWidth===" + targetWidth);
									// SOP("targetHeight===" + targetHeight);
									if (ImgWidth == null || ImgWidth.equals("")) {
										ImgWidth = targetWidth + "";
									}
									if (targetWidth > Integer.parseInt(ImgWidth) && !ImgWidth.equals("0")) {
										targetHeight = Integer.parseInt(ImgWidth) * targetHeight / targetWidth;
										targetWidth = Integer.parseInt(ImgWidth);
									}
									bufferedImage = createResizedCopy(bufferedImage, targetWidth, targetHeight);
									ByteArrayOutputStream bios = new ByteArrayOutputStream();

									if (!item.getName().equals("") && item.getName().contains(".")) {
										// SOPInfo("skksksk===" + fileext(item.getName()));
										ImageIO.write(bufferedImage, fileext(item.getName()).substring(1), bios);
										inputStream = new ByteArrayInputStream(bios.toByteArray());
										// SOP("inputStream====after===" + inputStream.available());
									}

								}

							}

						}
					}
					for (String key : fieldsMap.keySet()) {

						if (fieldsMap.get(key).equals("Add Driving Licence")) {
							if (!testdrive_id.equals("0")) {
								if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
									GetValues();
									UpdateFields();
									if (!msg.equals("")) {
										msg = "Error!" + msg;
									} else {
										msg = "Image updated successfully!";
										if (!testdrive_id.equals("0")) {
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
		testdrive_doc_value = fieldsMap.get("testdrive_doc_value");
		itemsize = fieldsMap.get("itemsize");
		// SOP("itemsize----------g-----" + itemsize);
		doc_entry_id = fieldsMap.get("testdrive_doc_modified_id");
		doc_entry_date = fieldsMap.get("testdrive_doc_modified_date");
	}

	protected void UpdateFields() throws Exception {
		CheckForm();
		// int count = 0;
		if (msg.equals("")) {
			try {
				int count = 0;
				if (!testdrive_id.equals("0")) {
					StrSql = "UPDATE " + docTable
							+ " SET";

					if (inputStream.available() != 0) {
						StrSql += " testdrive_doc_data = ?,";

					}

					StrSql += " testdrive_doc_value = ?,"
							+ " testdrive_doc_modified_id=?,"
							+ " testdrive_doc_modified_date =?"
							+ " WHERE testdrive_id = ?";
					// }
					// SOP("StrSql===update query===" + StrSql);
					conn = null;
					conn = connectDB();
					psmt = conn.prepareStatement(StrSql);
					if (inputStream.available() != 0) {
						// SOP("inputStream====update=====" + inputStream);
						psmt.setBlob(1, inputStream);
					}

					psmt.setString(2, testdrive_doc_value);
					psmt.setString(3, emp_id);
					psmt.setString(4, ToLongDate(kknow()));
					psmt.setString(5, testdrive_id);

					// SOP("StrSql====" + StrSql);
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
				if (projectbrochure_id.equals("0")) {
					StrSql = "Delete from " + docTable + ""
							+ " where doc_id = " + doc_id + "";
				} else if (!projectbrochure_id.equals("0")) {
					StrSql = "Delete from " + docTable + ""
							+ " where projectbrochure_id = " + projectbrochure_id + "";
				}
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		String Msg1 = "", Msg2 = "";
		if (add.equals("yes") && testdrive_doc_value.equals("")) {
			msg = msg + "<br>Select Image!";
		}
		// if ((Long.parseLong(itemsize) > (5 * 1024 * 1024)) && !testdrive_doc_value.equals("")) {
		// msg = msg + "<br>Image size should not exceed 5 Mb!";
		// }

		// SOP("testdrive_doc_value====" + testdrive_doc_value);

		if (!testdrive_doc_value.equals("") && testdrive_doc_value.contains(".")) {
			int pos = testdrive_doc_value.lastIndexOf(".");
			if (pos != -1) {
				testdrive_doc_value = "testdrive_" + testdrive_id + testdrive_doc_value.substring(pos);
			}
			// SOP("vvvvvvvvvv=========" + testdrive_doc_value.substring(
			// testdrive_doc_value.lastIndexOf(".")).toLowerCase());
			if (!ImageFormats.contains(testdrive_doc_value.substring(
					testdrive_doc_value.lastIndexOf(".")).toLowerCase())) {
				msg = msg + "<br>Unable to upload"
						+ testdrive_doc_value.substring(testdrive_doc_value.lastIndexOf("."))
						+ " format";
			}
		}
		else {
			msg = msg + "<br>Invalid file format!";
		}
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
						doc_size = (long) crs.getDouble("doc_size");
						config_doc_size = (long) crs.getDouble("config_doc_size");
						config_doc_format = crs.getString("config_doc_formats");
						// SOP("config_doc_size--------" + config_doc_size);
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
