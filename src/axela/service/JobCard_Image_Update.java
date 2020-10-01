package axela.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

public class JobCard_Image_Update extends Connect {

	private String fileName;
	public String LinkHeader = "";
	public String updatestatus = "";
	public String displayform = "yes";
	public String RefreshForm = "";
	public String deleteB = "";
	public String updateB = "";
	public String comp_id = "0";
	public String add = "";
	public String update = "";
	public static String status = "";
	public String StrSql = "";
	public String QueryString = "";
	public static String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String name = "";
	public String jc_id = "", img_id = "";
	public String img_title = "", img_value = "";
	public String img_imgsize = "", img_rank = "";
	public String img_entry_id = "", img_entry_date = "";
	public String img_modified_id = "", img_modified_date = "";
	public String chkPermMsg = "";
	public String emp_idsession = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(GetSession("emp_id", request));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				img_id = CNumeric(PadQuotes(request.getParameter("img_id")));
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				savePath = JobCardImgPath(comp_id);
				img_imgsize = ImageSize(comp_id);
				msg = PadQuotes(request.getParameter("msg"));
				name = ExecuteQuery("SELECT jc_title from " + compdb(comp_id) + "axela_service_jc where jc_id=" + jc_id + "");
				LinkHeader = "<a href=../service/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/jobcard.jsp>Job Card</a> &gt; <a href=../service/jobcard-list.jsp?all=yes>List Job Cards</a> &gt; <a href=../service/jobcard-list.jsp?jc_id="
						+ jc_id
						+ ">"
						+ name
						+ "</a> &gt; <a href=../service/jobcard-dash-image.jsp?jc_id="
						+ jc_id
						+ ">List Images</a> &gt; <a href=../service/jobcard-image-update.jsp?"
						+ QueryString + ">" + status + " Document</a>:";
				if (add.equals("yes")) {
					status = "Add";
					displayform = "yes";
				} else if (update.equals("yes")) {
					status = "Update";
					displayform = "yes";
				}
				if (update.equals("yes")) {
					PopulateFields();
				}

				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				if (isMultipart) {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
					factory.setSizeThreshold(val);
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
					msg = "";
					Iterator iter = items.iterator();
					for (int i = 0; iter.hasNext() && i < 9; i++) {
						if (str1[i].equals("Update Image") || str1[i].equals("Add Image")) {
							FileItem item = (FileItem) iter.next();
							if (!item.isFormField()) {
								fileName = item.getName();
								img_title = item.getName();
								CheckForm();
								if (!fileName.equals("") && fileName.contains(".")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1) {
										fileName = "jc_" + fileName.substring(pos);
									}

									if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
										msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
									}
								} else {
									msg = msg + "<br>Invalid file format!";
								}

								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (msg.equals("") && chkPermMsg.equals("")) {
										// String prevFile = ExecuteQuery("Select img_value from " + compdb(comp_id) + "axela_service_jc_img"
										// + " inner join " + compdb(comp_id) + "axela_service_jc on jc_id = img_jc_id"
										// + " where jc_id = " + jc_id + "");
										// if (!prevFile.equals("")) {
										// File uploadedFile1 = new File(JobCardImgPath() + prevFile);
										// if (uploadedFile1.exists()) {
										// uploadedFile1.delete();
										// }
										// }
										if (!fileName.equals("") && update.equals("yes")) {
											UpdateFields();
											msg = "Image uploaded successfully!";
											File uploadedFile = new File(JobCardImgPath(comp_id) + fileName);

											if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
											item.write(uploadedFile);
											response.sendRedirect(response.encodeRedirectURL("../service/jobcard-dash.jsp?jc_id=" + jc_id + "&msg=" + msg + "#tabs-6"));
										} else if (!fileName.equals("") && add.equals("yes")) {
											AddFields();
											msg = "Image added successfully!";
											File uploadedFile = new File(JobCardImgPath(comp_id) + fileName);

											if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
											item.write(uploadedFile);
											response.sendRedirect(response.encodeRedirectURL("../service/jobcard-dash.jsp?jc_id=" + jc_id + "&msg=" + msg + "#tabs-6"));
										}
										displayform = "no";
										RefreshForm = RefreshForm();
									}
								}
							}
						}

						if (str1[i].equals("Delete Image")) {
							// chkPermMsg = CheckPerm(comp_id, "emp_patient_delete", request, response);
							if (chkPermMsg.equals("")) {
								fileName = ExecuteQuery("Select img_value from " + compdb(comp_id) + "axela_service_jc_img"
										+ " where img_jc_id = " + jc_id + ""
										+ " and img_id = " + img_id + "");
								if (!fileName.equals("")) {
									File uploadedFile = new File(JobCardImgPath(comp_id) + fileName);
									if (uploadedFile.exists()) {
										uploadedFile.delete();
									}
									DeleteFields();
									msg = "Image deleted successfully!";
								} else {
									msg = " No Image found!";
								}
								displayform = "no";
								RefreshForm = RefreshForm();
								response.sendRedirect(response.encodeRedirectURL("../service/jobcard-dash.jsp?jc_id=" + jc_id + "&msg=" + msg + "#tabs-6"));
							}
						}
					}
				}
			}
		} catch (FileUploadException Fe) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("executives-photo.jsp?msg=" + msg);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	protected void PopulateFields() {

		try {
			StrSql = "select img_title, img_value, img_id from " + compdb(comp_id) + "axela_service_jc_img"
					+ " where img_jc_id = " + jc_id + ""
					+ " and img_id = " + img_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				img_id = crs.getString("img_id");
				img_title = crs.getString("img_title");
				img_value = crs.getString("img_value");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				int pos = fileName.lastIndexOf(".");
				if (pos != -1) {
					fileName = "jcimg_" + img_id + fileName.substring(pos);
				}
				img_modified_id = emp_idsession;
				img_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_img"
						+ " SET "
						+ " img_value = '" + fileName + "',"
						+ " img_title = '" + img_title + "',"
						+ " img_modified_id = '" + img_modified_id + "',"
						+ " img_modified_date = '" + img_modified_date + "'"
						+ " where img_id = " + img_id + "";
				// SOP("StrSQl==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_img"
					+ " where img_jc_id = " + jc_id + ""
					+ " and img_id = " + img_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Select coalesce(max(img_id),0)+1 as ID from " + compdb(comp_id) + "axela_service_jc_img";
				img_id = ExecuteQuery(StrSql);
				int pos = fileName.lastIndexOf(".");
				if (pos != -1) {
					fileName = "jcimg_" + img_id + fileName.substring(pos);
				}
				img_entry_id = emp_idsession;
				img_entry_date = ToLongDate(kknow());
				StrSql = "Insert into " + compdb(comp_id) + "axela_service_jc_img"
						+ " (img_id,"
						+ " img_jc_id,"
						+ " img_value,"
						+ " img_title,"
						+ " img_entry_id,"
						+ " img_entry_date)"
						+ " values"
						+ " (" + img_id + ","
						+ " '" + jc_id + "',"
						+ " '" + fileName + "',"
						+ " '" + img_title + "',"
						+ " '" + img_entry_id + "',"
						+ " '" + img_entry_date + "')";
				// SOP("StrSql==" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String RefreshForm() {
		if (displayform.equals("no")) {
			if (!jc_id.equals("0") && !jc_id.equals("")) {
				RefreshForm = "onunload=\"opener.location ='executive-list.jsp?all=yes&jc_id=" + jc_id + "'\"";
			}
		}
		return RefreshForm;
	}

	protected void CheckForm() {
		msg = "";
		String name_msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Image!";
		}
		for (int i = 0; i < str1.length; i++) {
			if (!str1[1].equals("")) {
				img_title = str1[1];
			}
		}
		msg = msg + name_msg;
	}
}
