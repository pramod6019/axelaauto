package axela.inventory;

//aJIt 8th October, 2012
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

public class Inventory_Item_Image extends Connect {

	private String fileName;
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	// public String img_id = "0";
	public String item_id = "0";
	// public String asset_name = "";
	public String img_value = "";
	// public String img_title = "";
	public String img_imgsize = "";
	// public String img_entry_id = "0";
	// public String img_entry_by = "";
	// public String img_modified_id = "0";
	// public String img_entry_date = "";
	// public String img_modified_date = "";
	// public String img_modified_by = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				savePath = ItemImgPath(comp_id);
				img_imgsize = ImageSize(comp_id);
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				// asset_name = ExecuteQuery("select concat(asset_name,' (',asset_id,')') from " + compdb(comp_id) + "axela_asset where item_id=" + item_id + "");
				// SOP("item_id--"+item_id);

				PopulateFields(response);
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
						// if (str1[i].equals("Add Image")) {
						// CheckPerm(comp_id, "emp_asset_add", request, response);
						// FileItem item = (FileItem) iter.next();
						// if (!item.isFormField()) {
						// fileName = item.getName();
						// img_entry_id = CNumeric(GetSession("emp_id", request));
						// img_entry_date = ToLongDate(kknow());
						// CheckForm();
						// if (!fileName.equals("")) {
						// String temp = "";
						// fileName = item.getName();
						// if (!fileName.equals("")) {
						// int pos = fileName.lastIndexOf(".");
						// if (pos != -1) {
						// fileName = "img_" + img_id + fileName.substring(pos);
						// }
						// if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
						// msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
						// }
						// }
						// if (!msg.equals("")) {
						// msg = "Error!" + msg;
						// } else {
						// if (!fileName.equals("")) {
						// AddFields();
						// File uploadedFile = new File(AssetImgPath() + fileName);
						// if (uploadedFile.exists()) {
						// uploadedFile.delete();
						// }
						// item.write(uploadedFile);
						// msg = msg + "<br>Image added successfully!";
						// response.sendRedirect(response.encodeRedirectURL("asset-image-list.jsp?item_id=" + item_id + "&msg=" + msg));
						// }
						// }
						// }
						// }
						// }
						if (str1[i].equals("Update Image")) {
							CheckPerm(comp_id, "emp_item_edit", request, response);
							FileItem item = (FileItem) iter.next();
							// SOP("item==" + item);
							if (!item.isFormField()) {
								fileName = item.getName();
								CheckForm();
								if (!fileName.equals("") && fileName.contains(".")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1) {
										fileName = "item_img" + item_id + "" + fileName.substring(pos);
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
									// img_modified_id = CNumeric(GetSession("emp_id", request));
									// img_modified_date = ToLongDate(kknow());
									if (!fileName.equals("")) {
										String prevFile = ExecuteQuery("select item_img from " + compdb(comp_id) + "axela_inventory_item"
												+ " where item_id = " + item_id + "");
										if (!prevFile.equals("")) {
											File uploadedFileprevFile = new File(ItemImgPath(comp_id) + prevFile);
											if (uploadedFileprevFile.exists()) {
												uploadedFileprevFile.delete();
											}
										}
										UpdateFields();
										msg = msg + "<br>Image updated successfully!";
										File uploadedFile = new File(ItemImgPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										response.sendRedirect(response.encodeRedirectURL("inventory-item-list.jsp?item_id=" + item_id + "&msg=" + msg));
									}
								}
							}
						}
						if (str1[i].equals("Delete Image")) {
							CheckPerm(comp_id, "emp_item_delete", request, response);
							fileName = ExecuteQuery("Select item_img from " + compdb(comp_id) + "axela_inventory_item"
									+ " where item_id = " + item_id + "");
							File uploadedFile = new File(ItemImgPath(comp_id) + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}
							DeleteFields();
							response.sendRedirect(response.encodeRedirectURL("inventory-item-list.jsp?item_id=" + item_id + "&msg=Image deleted successfully!"));
						}
					}
				}
			}

		} catch (FileUploadException Fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			// if (status.equals("add")) {
			// response.sendRedirect("asset-image-update.jsp?add=yes&item_id=" + item_id + "&msg=" + msg);
			// }
			// if (status.equals("update")) {
			response.sendRedirect("inventory-item-image.jsp?update=yes&item_id=" + item_id + "&msg=" + msg);
			// }
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
		// img_title = PadQuotes(request.getParameter("txt_img_name"));
	}

	// protected void AddFields() {
	// if (msg.equals("")) {
	// try {
	// String cmp_id = "";
	// img_id = CNumeric(PadQuotes(ExecuteQuery("Select max(img_id) as img_id from " + compdb(comp_id) + "axela_asset_img")));
	// int i = Integer.parseInt(img_id) + 1;
	// img_id = "" + i;
	// int pos = fileName.lastIndexOf(".");
	// if (pos != -1) {
	// fileName = "img_" + img_id + fileName.substring(pos);
	// }
	// StrSql = "insert into " + compdb(comp_id) + "axela_asset_img"
	// + "( img_id,"
	// + " img_asset_id,"
	// + " img_value,"
	// + " img_title,"
	// + " img_entry_id,"
	// + " img_entry_date,"
	// + " img_modified_id,"
	// + " img_modified_date )"
	// + " values	"
	// + "( '" + img_id + "',"
	// + "'" + item_id + "',"
	// + "'" + fileName + "',"
	// + "'" + str1[1] + "',"
	// + "'" + img_entry_id + "',"
	// + "'" + img_entry_date + "',"
	// + "'0',"
	// + "''"
	// + " )";
	// // SOP("StrSql--ii--"+StrSql);
	// updateQuery(StrSql);
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
	// }
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT item_img FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + item_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// img_title = crs.getString("img_title");
					img_value = crs.getString("item_img");
					// img_entry_id = crs.getString("img_entry_id");
					// if (!img_entry_id.equals("")) {
					// img_entry_by = Exename(comp_id, Integer.parseInt(img_entry_id));
					// }
					// img_entry_date = strToLongDate(crs.getString("img_entry_date"));
					// img_modified_id = crs.getString("img_modified_id");
					// if (!img_modified_id.equals("0")) {
					// img_modified_by = Exename(comp_id, Integer.parseInt(img_modified_id));
					// img_modified_date = strToLongDate(crs.getString("img_modified_date"));
					// }
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_inventory_item"
						+ " SET "
						+ " item_img = '" + fileName + "'"
						+ " WHERE item_id = " + item_id + "";
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
						+ " SET"
						+ " item_img = ''"
						+ " WHERE item_id = " + item_id + "";
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
		// img_title = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Image!";
		}
		// for (int i = 0; i < str1.length; i++) {
		// if (!str1[1].equals("")) {
		// img_title = str1[1];
		// } else {
		// Msg1 = "<br>Enter Image Name!";
		// }
		// }
		Msg1 = Msg1 + Msg2;
		msg = msg + Msg1;
	}
}
