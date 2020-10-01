package axela.app;

////////////Divya 5 oct
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cloudify.connect.Connect;

public class Features_Image extends Connect {

	private String fileName;
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String img_id = "0";
	public String feature_id = "0";
	public String feature_name = "";
	public String img_value = "";
	public String img_title = "";
	public String img_imgsize = "";
	public String img_entry_id = "0";
	public String img_entry_by = "";
	public String img_modified_id = "0";
	public String img_entry_date = "";
	public String img_modified_date = "";
	public String img_modified_by = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
			savePath = FeatureImgPath(comp_id);
			img_imgsize = (ImageSize(comp_id));
			add = PadQuotes(request.getParameter("add"));
			update = PadQuotes(request.getParameter("update"));
			msg = PadQuotes(request.getParameter("msg"));
			QueryString = PadQuotes(request.getQueryString());
			feature_id = CNumeric(PadQuotes(request.getParameter("feature_id")));
			feature_name = ExecuteQuery("select concat(feature_name,' (',feature_id,')') from " + compdb(comp_id) + "axela_app_model_feature where feature_id=" + feature_id + "");
			img_id = CNumeric(PadQuotes(request.getParameter("img_id")));

			if (add.equals("yes")) {
				status = "Add";
			} else if (update.equals("yes")) {
				status = "Update";
			}
			if (update.equals("yes")) {
				img_id = CNumeric(PadQuotes(request.getParameter("img_id")));
				PopulateFields();
			}
			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				// Create a factory for disk-based file items
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// Set factory constraints
				int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
				factory.setSizeThreshold(val);
				// File f = new File("d:/");
				File f = new File(savePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				factory.setRepository(f);
				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// Set overall request size constraint
				upload.setSizeMax(val);
				// Parse the request
				List items = upload.parseRequest(request);
				Iterator it = items.iterator();
				for (int i = 0; it.hasNext() && i < 9; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				for (int i = 0; iter.hasNext() && i < 9; i++) {
					if (str1[i].equals("Add Image")) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							img_entry_id = emp_id;
							img_entry_date = ToLongDate(kknow());
							CheckForm();
							if (!fileName.equals("")) {
								String temp = "";
								fileName = item.getName();
								if (!fileName.equals("")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1) {
										fileName = "img_" + img_id + fileName.substring(pos);
									}
									if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
										msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
									}
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (!fileName.equals("")) {
										AddFields();
										File uploadedFile = new File(FeatureImgPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										msg = msg + "Image added successfully!";
										response.sendRedirect(response.encodeRedirectURL("features-image-list.jsp?feature_id=" + feature_id + "&msg=" + msg));
									}
								}
							}
						}
					}
					if (str1[i].equals("Update Image")) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!fileName.equals("")) {
								int pos = fileName.lastIndexOf(".");
								if (pos != -1) {
									fileName = "img_" + img_id + fileName.substring(pos);
								}
								if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
									msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
								}
							}
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								img_modified_id = emp_id;
								img_modified_date = ToLongDate(kknow());
								if (!fileName.equals("")) {
									String prevFile = ExecuteQuery("select img_value from " + compdb(comp_id) + "axela_app_model_feature_img where img_id=" + img_id + " and img_feature_id="
											+ feature_id + "");
									StrSql = "select img_value from " + compdb(comp_id) + "axela_app_model_feature_img where img_id=" + img_id + " and img_feature_id=" + feature_id;
									if (!prevFile.equals("")) {
										File uploadedFileprevFile = new File(FeatureImgPath(comp_id) + prevFile);
										if (uploadedFileprevFile.exists()) {
											uploadedFileprevFile.delete();
										}
									}
									UpdateFields();
									msg = msg + "Image updated successfully!";
									File uploadedFile = new File(FeatureImgPath(comp_id) + fileName);
									if (uploadedFile.exists()) {
										uploadedFile.delete();
									}
									item.write(uploadedFile);
									response.sendRedirect(response.encodeRedirectURL("features-image-list.jsp?feature_id=" + feature_id + "&msg=" + msg));
								}
							}
						}
					}
					if (str1[i].equals("Delete Image")) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						fileName = ExecuteQuery("select img_value from " + compdb(comp_id) + "axela_app_model_feature_img where img_id=" + img_id + " and img_feature_id=" + feature_id + " ");
						File uploadedFile = new File(FeatureImgPath(comp_id) + fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						DeleteFields();
						msg = msg + "Image deleted successfully!";
						response.sendRedirect(response.encodeRedirectURL("features-image-list.jsp?feature_id=" + feature_id + "&msg=" + msg));
					}
				}
			}
		} catch (FileUploadException Fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			if (status.equals("add")) {
				response.sendRedirect("features-image.jsp?add=yes&feature_id=" + feature_id + "&msg=" + msg);
			}
			if (status.equals("update")) {
				response.sendRedirect("features-image.jsp?update=yes&feature_id=" + feature_id + "&img_id=" + img_id + "&msg=" + msg);
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		img_title = PadQuotes(request.getParameter("txt_img_name"));
	}

	protected void AddFields() {
		if (msg.equals("")) {
			try {
				int pos = fileName.lastIndexOf(".");
				if (pos != -1) {
					fileName = "feature_" + feature_id + fileName.substring(pos);
				}
				StrSql = "insert into " + compdb(comp_id) + "axela_app_model_feature_img"
						+ " (img_feature_id,"
						+ " img_value,"
						+ " img_title,"
						+ " img_entry_id,"
						+ " img_entry_date,"
						+ " img_modified_id,"
						+ " img_modified_date"
						+ " )"
						+ " values"
						+ " ('" + feature_id + "',"
						+ " '" + fileName + "',"
						+ " '" + str1[1] + "',"
						+ " '" + img_entry_id + "',"
						+ " " + img_entry_date + ","
						+ " 0,"
						+ " ''"
						+ " )";
				// SOPError("StrSql--AddFields--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "select * from " + compdb(comp_id) + "axela_app_model_feature_img where img_id=?";
			mapkey++;
			map.put(mapkey, img_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			while (crs.next()) {
				img_title = crs.getString("img_title");
				img_value = crs.getString("img_value");
				img_entry_id = crs.getString("img_entry_id");
				if (!img_entry_id.equals("")) {
					img_entry_by = Exename(comp_id, Integer.parseInt(img_entry_id));
				}
				img_entry_date = strToLongDate(crs.getString("img_entry_date"));
				img_modified_id = crs.getString("img_modified_id");
				if (!img_modified_id.equals("0")) {
					img_modified_by = Exename(comp_id, Integer.parseInt(img_modified_id));
					img_modified_date = strToLongDate(crs.getString("img_modified_date"));
				}
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_app_model_feature_img"
						+ " SET"
						+ " img_value = '" + fileName + "',"
						+ " img_title = '" + str1[1] + "',"
						+ " img_modified_id = " + img_modified_id + ", "
						+ " img_modified_date = " + img_modified_date + ""
						+ " where img_id = " + img_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_app_model_feature_img where img_id =" + img_id + "";
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
		img_title = "";
		if (update.equals("yes") && fileName.equals("")) {
			msg = msg + "<br>Select Image!";
		} else {
			if (add.equals("yes") && fileName.equals("")) {
				msg = msg + "<br>Select Image!";
			}
		}
		for (int i = 0; i < str1.length; i++) {
			if (!str1[1].equals("")) {
				img_title = str1[1];
			} else {
				Msg1 = "<br>Enter Image Name!";
			}
		}
		Msg1 = Msg1 + Msg2;
		msg = msg + Msg1;
	}
}
