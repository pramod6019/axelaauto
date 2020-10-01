package axela.service;
//aJIt 2nd June 2014

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Vehicle_Stock_Check extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public String emp_role_id = "0";
	public String importdocformat = "";
	public String comp_id = "0";
	public long docsize = 0;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = { "", "", "", "", "", "", "", "", "" };
	public String doc_value = "";
	public String BranchAccess = "";
	public String upload = "";
	protected String[] reg_arr;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				upload = PadQuotes(request.getParameter("add_button"));
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			docsize = 1;
			importdocformat = ".xls, .xlsx";

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				File f = new File("d:/");
				factory.setRepository(f);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));
				List items = upload.parseRequest(request);
				Iterator it = items.iterator();
				for (int i = 0; it.hasNext() && i < 9; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				msg = "";
				for (int i = 0; iter.hasNext() && i < 9; i++) {
					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							}
							if (!fileName.equals("")) {
								String temp = "";
								if (fileName.contains("/") || fileName.contains("\\")) {
									fileName = Filename(item.getName());
								} else {
									fileName = item.getName();
								}

								if (!fileName.equals("")) {
									docformat = importdocformat.split(", ");
									for (int j = 0; j < docformat.length; j++) {
										if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format!";
										} else {
											temp = "";
											break;
										}
									}
									msg += temp;
								}

								if (msg.equals("")) {
									if (!fileName.equals("")) {
										File uploadedFile = new File(StockDocPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = StockDocPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("vehicle-stock-check.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (fileName.equals("")) {
			msg += "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			CachedRowSet crs;
			int count = 0;
			StringBuilder Str = new StringBuilder();
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls
															// type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is
															// .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 1000) {
				rowLength = 1000;
			}

			reg_arr = new String[rowLength];

			int h = 0;
			int j = 0;
			for (j = 1; j < rowLength + 1; j++) {
				for (h = 0; h < columnLength; h++) {
					if (h == 0) {
						if (!sheetData[j][h].equals("")) {
							reg_arr[j - 1] = "'" + sheetData[j][h] + "'";
						}
					}
				}
			}
			Thread.sleep(100);

			if (reg_arr.length > 0) {
				String jc_time_out = "";
				String union_sql = "";
				StringBuilder StrNew = new StringBuilder();
				Str.append("<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\">\n");
				Str.append("<tr>\n");
				Str.append("<th align=\"center\" width=\"5%\">#</th>\n");
				Str.append("<th align=\"center\">Reg. No.</th>\n");
				Str.append("<th align=\"center\">Status</th>\n");
				Str.append("<th align=\"center\">Last Time Out</th>\n");
				Str.append("</tr>\n");

				for (int i = 0; i < reg_arr.length; i++) {
					if (i > 0) {
						StrNew.append(" UNION ");
					}
					StrNew.append("SELECT ").append(reg_arr[i].replace(" ", "")).append(" AS reg_no");
				}

				if (!StrNew.toString().equals("")) {
					union_sql = "SELECT reg_no FROM (" + StrNew.toString() + ") t"
							+ " WHERE Binary t.reg_no NOT IN (SELECT Binary jc_reg_no FROM " + compdb(comp_id) + "axela_service_jc"
							+ " WHERE jc_time_out = '')";
					crs = processQuery(union_sql, 0);
					while (crs.next()) {
						StrSql = "SELECT jc_time_out FROM " + compdb(comp_id) + "axela_service_jc"
								+ " WHERE jc_time_out != ''"
								+ " AND jc_reg_no = '" + crs.getString("reg_no") + "'"
								+ " ORDER BY jc_id DESC"
								+ " LIMIT 1";
						jc_time_out = ExecuteQuery(StrSql);

						if (!jc_time_out.equals("")) {
							jc_time_out = strToLongDate(jc_time_out);
						} else {
							jc_time_out = "&nbsp;";
						}
						count++;
						Str.append("<tr>\n");
						Str.append("<td align=\"center\">").append(count).append("</td>\n");
						Str.append("<td align=\"center\">").append(crs.getString("reg_no")).append("</td>\n");
						Str.append("<td align=\"center\">Not in Axela</td>\n");
						Str.append("<td align=\"center\">").append(jc_time_out).append("</td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
				}

				StrSql = "SELECT jc_id, jc_time_out, jc_reg_no FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_reg_no NOT IN " + Arrays.toString(reg_arr).replace("[", "(").replace("]", ")") + ""
						+ " AND jc_time_out = ''"
						+ " GROUP BY jc_reg_no";
				crs = processQuery(StrSql, 0);

				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\">").append(crs.getString("jc_reg_no")).append("</td>\n");
					Str.append("<td align=\"center\">Not in Excel</td>\n");
					Str.append("<td align=\"center\">&nbsp;</td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
			}

			if (!Str.toString().equals("")) {
				Str.append("</table>\n");
			}

			if (count == 0) {
				Str.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\"><b>All Vehicles matching!</b></font>");
			}
			StrHTML = Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
