package axela.sales;
//aJIt

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import cloudify.connect.Connect;

public class Salesorder_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize = 0;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int socount = 0;
	public String so_entry_id = "0";
	public String so_entry_date = "";
	public String campaign_name = "";
	public String BranchAccess = "";
	public String so_branch_id = "0";
	public String upload = "";
	public String similar_comm_no = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				so_entry_id = CNumeric(GetSession("emp_id", request));
				so_entry_date = ToLongDate(kknow());
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
			savePath = StockDocPath(comp_id);
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
					so_branch_id = str1[0];
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
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
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
										msg += "<br>Document contents not in order!";
										String fileName1 = StockDocPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = socount + " Stock imported successfully!";
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
			response.sendRedirect("stock-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (so_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (fileName.equals("")) {
			msg += "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			String so_id = "";
			String so_date = "";
			String so_delivered_date = "";
			String so_enquiry_id = "";
			String so_item_id = "";
			String so_quote_id = "";
			String so_customer_id = "";
			String so_contact_id = "";
			String so_payment_date = "";
			String so_promise_date = "";

			String vehstock_modelyear = "";
			String colour_code = "", vehstock_key_no = "", status_name = "";

			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			// XlsxReadFile readFile = new XlsxReadFile(); //if i/p file is .xlsx type
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls type
				sheetData = readFile.getSheetData(fileName, 0);

				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);

				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 300) {
				rowLength = 300;
			}
			int h = 0;
			int j = 0;

			for (j = 1; j < rowLength + 1; j++) {
				for (h = 0; h < columnLength; h++) {
					if (h == 0) {
						if (so_branch_id.equals("0")) {
							so_branch_id = "1";
						}
					}
					if (h == 4) {
						so_customer_id = sheetData[j][h];
					}

					if (h == 6) {
						colour_code = sheetData[j][h];
					}

					if (h == 7) {
						so_date = sheetData[j][h];
					}

					if (h == 8) {
						so_delivered_date = sheetData[j][h];
					}

					if (h == 9) {
						vehstock_key_no = sheetData[j][h];
					}

					if (h == 11) {
						status_name = sheetData[j][h];
					}

					if (h == 12) {
						so_contact_id = sheetData[j][h];
					}

					if (h == 13) {
						so_quote_id = sheetData[j][h];
						if (!so_quote_id.equals("")) {
							String year = "20" + so_quote_id.substring(8, 10);
							vehstock_modelyear = year;
							so_quote_id = so_quote_id.substring(0, 6) + year;
							boolean t2 = isValidDateFormatShort(so_quote_id);
							if (t2 == true) {
								so_quote_id = ConvertShortDateToStr(so_quote_id);
							} else if (t2 == false) {
								so_quote_id = "";
							}
						}
					}

					if (h == 14) {
						so_enquiry_id = sheetData[j][h];
					}

					if (h == 15) {
						so_item_id = sheetData[j][h];
						if (!so_item_id.equals("")) {
							String year = "20" + so_item_id.substring(8, 10);
							so_item_id = so_item_id.substring(0, 6) + year;
							boolean t2 = isValidDateFormatShort(so_item_id);
							if (t2 == true) {
								so_item_id = ConvertShortDateToStr(so_item_id);
							} else if (t2 == false) {
								so_item_id = "";
							}
						}
					}

					if (h == 18) {
						so_payment_date = sheetData[j][h];
					}

					if (h == 22) {
						so_promise_date = sheetData[j][h];
					}
				}
				if (!so_date.equals("") && !so_delivered_date.equals("")) {
					StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE CONCAT(vehstock_chassis_no, '-', vehstock_engine_no) = '" + so_date + "-" + so_delivered_date + "'";
					String vehstock_id = CNumeric(ExecuteQuery(StrSql));
					if (!vehstock_id.equals("0")) {
						// /// Update Stock
						StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
								+ " SET"
								+ " vehstock_branch_id = '" + so_branch_id + "',"
								+ " vehstock_item_id = (SELECT COALESCE((SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + so_customer_id + "') > 6,"
								+ " SUBSTR('" + so_customer_id + "', 1, 6), '" + so_customer_id + "')) LIMIT 1), 0)),";
						if (!so_customer_id.equals("")) {
							StrSql += " vehstock_colour_id = (SELECT COALESCE((SELECT colour_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_colour"
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = colour_item_id"
									+ " WHERE colour_code = '" + colour_code + "'"
									+ " AND IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + so_customer_id + "') > 6,"
									+ " SUBSTR('" + so_customer_id + "', 1, 6),"
									+ " '" + so_customer_id + "')) LIMIT 1), 0) AS vehstock_colour_id),";
						} else {
							StrSql += " vehstock_colour_id = '0',";
						}

						StrSql += " vehstock_item_code = '" + so_customer_id + "',"
								+ " vehstock_chassis_no = '" + so_date + "',"
								+ " vehstock_engine_no = '" + so_delivered_date + "',"
								+ " vehstock_key_no = '" + vehstock_key_no + "',"
								+ " vehstock_invoice_no = " + so_contact_id + ","
								+ " vehstock_invoice_date = '" + so_quote_id + "',"
								+ " vehstock_grn_no = '" + so_enquiry_id + "',"
								+ " vehstock_grn_date = '" + so_item_id + "',"
								+ " vehstock_delstatus_id = (SELECT COALESCE((SELECT delstatus_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
								+ " WHERE delstatus_name = '" + status_name + "'), 0) AS vehstock_delstatus_id),"
								+ " vehstock_discount = " + so_promise_date + ","
								+ " vehstock_invoice_amount = " + so_payment_date + ","
								+ " vehstock_modelyear = '" + vehstock_modelyear + "',"
								+ " vehstock_notes = '',"
								+ " vehstock_modified_id = " + so_entry_id + ","
								+ " vehstock_modified_date = '" + so_entry_date + "'"
								+ " WHERE vehstock_id = " + vehstock_id + "";
						updateQuery(StrSql);
					} else {
						socount++;

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
								+ "(vehstock_item_id,"
								+ " vehstock_branch_id,"
								+ " vehstock_colour_id,"
								+ " vehstock_item_code,"
								+ " vehstock_status_id,"
								+ " vehstock_chassis_no,"
								+ " vehstock_engine_no,"
								+ " vehstock_key_no,"
								+ " vehstock_invoice_no,"
								+ " vehstock_invoice_date,"
								+ " vehstock_grn_no,"
								+ " vehstock_grn_date,"
								+ " vehstock_delstatus_id,"
								+ " vehstock_discount,"
								+ " vehstock_invoice_amount,"
								+ " vehstock_modelyear,"
								+ " vehstock_notes,"
								+ " vehstock_entry_id,"
								+ " vehstock_entry_date)"
								+ " VALUES"
								+ " ((SELECT COALESCE((SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + so_customer_id + "') > 6,"
								+ " SUBSTR('" + so_customer_id + "', 1, 6), '" + so_customer_id + "')) LIMIT 1), 0)),"
								+ " '" + so_branch_id + "',";
						if (!so_customer_id.equals("")) {
							StrSql += " (SELECT COALESCE((SELECT colour_id FROM " + compdb(comp_id) + "axela_sales_testdrive_colour"
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = colour_item_id"
									+ " WHERE colour_code = '" + colour_code + "'"
									+ " AND IF(LENGTH(item_code) > 6, SUBSTR(item_code, 1, 6), item_code) = (IF(LENGTH('" + so_customer_id + "') > 6,"
									+ " SUBSTR('" + so_customer_id + "', 1, 6),"
									+ " '" + so_customer_id + "')) LIMIT 1), 0) AS vehstock_colour_id),";
						} else {
							StrSql += " '0',";
						}
						StrSql += " '" + so_customer_id + "',"
								+ " 1,"
								+ " '" + so_date + "',"
								+ " '" + so_delivered_date + "',"
								+ " " + vehstock_key_no + ","
								+ " " + so_contact_id + ","
								+ " '" + so_quote_id + "',"
								+ " '" + so_enquiry_id + "',"
								+ " '" + so_item_id + "',"
								+ " (SELECT COALESCE((SELECT delstatus_id FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
								+ " WHERE delstatus_name = '" + status_name + "'), 0) AS vehstock_delstatus_id),"
								+ " " + so_promise_date + ","
								+ " " + so_payment_date + ","
								+ " '" + vehstock_modelyear + "',"
								+ " '',"
								+ " " + so_entry_id + ","
								+ " '" + so_entry_date + "')";
						updateQuery(StrSql);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
