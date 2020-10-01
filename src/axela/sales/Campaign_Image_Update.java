package axela.sales;

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

public class Campaign_Image_Update extends Connect {

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
	public String campaign_id = "", img_id = "";
	public String img_title = "", img_value = "";
	public String img_imgsize = "", img_rank = "";
	public String img_entry_id = "", img_entry_date = "";
	public String img_modified_id = "", img_modified_date = "";
	public String chkPermMsg = "";
	public String emp_idsession = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(GetSession("emp_id", request));
				campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				img_id = CNumeric(PadQuotes(request.getParameter("img_id")));
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				entry_by = PadQuotes(request.getParameter("entry_by"));
				entry_date = PadQuotes(request.getParameter("entry_date"));
				modified_by = PadQuotes(request.getParameter("modified_by"));
				modified_date = PadQuotes(request.getParameter("modified_date"));

				savePath = CampaignImgPath(comp_id);
				img_imgsize = ImageSize(comp_id);
				msg = PadQuotes(request.getParameter("msg"));
				name = ExecuteQuery("SELECT campaign_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
						+ " WHERE campaign_id=" + campaign_id + "");
				LinkHeader = "<a href=../service/home.jsp>Home</a> &gt; <a href=../sales/index.jsp>Sales</a> &gt; <a href=../sales/campaign.jsp>Campaign</a> &gt; <a href=../sales/campaign-list.jsp?all=yes>List Campaign</a> &gt; <a href=../sales/campaign-list.jsp?campaign_id="
						+ campaign_id + ">" + name + "</a>"
						+ " &gt; <a href=../sales/campaign-image-list.jsp?campaign_id=" + campaign_id
						+ ">List Images</a> &gt; <a href=../sales/campaign-image-update.jsp?"
						+ QueryString + ">" + status + " Image</a>:";
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
										SOP(fileName.substring(pos));
										fileName = "campaign_" + fileName.substring(pos);
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
										if (!fileName.equals("") && update.equals("yes")) {
											if (ReturnPerm(comp_id, "emp_campaign_edit", request).equals("1")) {
												UpdateFields();
												msg = "Image uploaded successfully!";
												File uploadedFile = new File(CampaignImgPath(comp_id) + fileName);

												if (uploadedFile.exists()) {
													uploadedFile.delete();
												}
												item.write(uploadedFile);
												response.sendRedirect(response.encodeRedirectURL("../sales/campaign-image-list.jsp?campaign_id=" + campaign_id + "&msg=" + msg + ""));
											} else {
												response.sendRedirect(AccessDenied());
											}
										} else if (!fileName.equals("") && add.equals("yes")) {
											if (ReturnPerm(comp_id, "emp_campaign_add", request).equals("1")) {
												AddFields();
												msg = "Image added successfully!";
												File uploadedFile = new File(CampaignImgPath(comp_id) + fileName);

												if (uploadedFile.exists()) {
													uploadedFile.delete();
												}
												item.write(uploadedFile);
												response.sendRedirect(response.encodeRedirectURL("../sales/campaign-image-list.jsp?campaign_id=" + campaign_id + "&msg=" + msg + ""));
											} else {
												response.sendRedirect(AccessDenied());
											}
										}
										displayform = "no";
										RefreshForm = RefreshForm();
									}
								}
							}
						}
						if (str1[i].equals("Delete Image")) {
							if (chkPermMsg.equals("")) {
								fileName = ExecuteQuery("SELECT img_value"
										+ " FROM " + compdb(comp_id) + "axela_sales_campaign_img"
										+ " WHERE img_campaign_id = " + campaign_id + ""
										+ " AND img_id = " + img_id + "");
								if (!fileName.equals("")) {
									File uploadedFile = new File(CampaignImgPath(comp_id) + fileName);
									if (uploadedFile.exists()) {
										uploadedFile.delete();
									}
									if (ReturnPerm(comp_id, "emp_campaign_delete", request).equals("1")) {
										DeleteFields();
										msg = "Image deleted successfully!";
									} else {
										response.sendRedirect(AccessDenied());
									}
								} else {
									msg = " No Image found!";
								}
								displayform = "no";
								RefreshForm = RefreshForm();
								response.sendRedirect(response.encodeRedirectURL("../sales/campaign-image-list.jsp?campaign_id=" + campaign_id + "&msg=" + msg + ""));
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
			StrSql = "SELECT img_title, img_value, img_id, img_entry_id,"
					+ " img_entry_date, img_modified_id, img_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign_img"
					+ " WHERE img_campaign_id = " + campaign_id + ""
					+ " AND img_id = " + img_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("Strsql==" + StrSql);
			while (crs.next()) {
				img_id = crs.getString("img_id");
				img_title = crs.getString("img_title");
				img_value = crs.getString("img_value");
				img_entry_id = crs.getString("img_entry_id");
				img_entry_date = crs.getString("img_entry_date");
				img_modified_id = crs.getString("img_modified_id");
				if (!crs.getString("img_entry_id").equals("0")) {
					entry_by = Exename(comp_id, crs.getInt("img_entry_id"));
					entry_date = strToLongDate(crs.getString("img_entry_date"));
				}
				img_modified_date = crs.getString("img_modified_date");
				if (!crs.getString("img_modified_id").equals("0")) {
					modified_by = Exename(comp_id, crs.getInt("img_modified_id"));
					modified_date = strToLongDate(crs.getString("img_modified_date"));
				}
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
					fileName = "campaignimg_" + img_id + fileName.substring(pos);
				}
				img_modified_id = emp_idsession;
				img_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_campaign_img"
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
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_campaign_img"
					+ " WHERE img_campaign_id = " + campaign_id + ""
					+ " AND img_id = " + img_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		StrSql = "SELECT COALESCE(MAX(img_id),0)+1 AS ID"
				+ " FROM " + compdb(comp_id) + "axela_sales_campaign_img";
		img_id = ExecuteQuery(StrSql);
		if (msg.equals("")) {
			try {
				int pos = fileName.lastIndexOf(".");
				if (pos != -1) {
					fileName = "campaignimg_" + img_id + fileName.substring(pos);
				}
				img_entry_id = emp_idsession;
				img_entry_date = ToLongDate(kknow());
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_campaign_img"
						+ " ("
						+ " img_campaign_id,"
						+ " img_value,"
						+ " img_title,"
						+ " img_entry_id,"
						+ " img_entry_date)"
						+ " VALUES"
						+ " ('" + campaign_id + "',"
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
			if (!campaign_id.equals("0") && !campaign_id.equals("")) {
				RefreshForm = "onunload=\"opener.location ='executive-list.jsp?all=yes&campaign_id=" + campaign_id + "'\"";
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
