package axela.inventory;

/*
 *@author GuruMurthy TS 19 FEB 2013
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

//import sun.java2d.loops.XorPixelWriter.ShortData;SOP

public class Stock_User_Import_Porsche extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", stockerrormsg = "", emp_id = "0", comp_id = "0", errormsg = "";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int stockcount = 0, updatecount = 0, count = 0;
	public String vehstock_entry_id = "";
	public String vehstock_entry_date = "";
	public String campaign_name = "";
	public String BranchAccess = "";
	public String branch_id = "0";
	public String vehstock_branch_id = "0";
	public String upload = "";
	public String similar_comm_no = "";
	public String vehstock_modelyear = "";
	Map<String, String> optionidscheck = new HashMap<String, String>();
	Map<String, String> topcolorcheck = new HashMap<String, String>();
	Map<String, String> bodycolorcheck = new HashMap<String, String>();

	public Connection conntx = null;
	public Statement stmttx = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				vehstock_entry_id = CNumeric(GetSession("emp_id", request));
				vehstock_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
				// SOP("upload==" + upload);
				Addfile(request, response);
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = StockDocPath(comp_id);
			docsize = 1;
			importdocformat = ".xls, .xlsx";

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				File f = new File(savePath);
				if (!f.exists()) {
					f.mkdirs();
				}
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
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
										} else {
											temp = "";
											break;
										}
									}
									msg = msg + temp;
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
										if (msg.equals("")) {
											msg += "<br>" + stockcount + " Stock(s) Imported successfully!";
											msg += "<br>" + updatecount + " Stock(s) Updated successfully!";
											if (!stockerrormsg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + stockerrormsg;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("stock-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			String vehstock_arrival_date = "", vehstock_vehstocklocation_id = "0";
			String vehstock_comm_no = "";
			String vehstock_invoice_date = "";
			String vehstock_invoice_amount = "";
			String vehstock_delstatus_id = "0", vehstock_status_id = "0";
			String vehstock_item_id = "0";
			String trans_option_id = "", vehstock_notes = "", vehstock_item_code = "", vehstock_engine_no = "";
			String bodycolour = "", topcolour = "", interiorcolor = "", optionid = "";
			String vehstock_nsc = "", dealer_code = "", color = "", arrivaldate = "";
			String vehstock_invoice_no = "", optionidcheck = "0", optionid1 = "0", optionid2 = "0", optionid3 = "0";
			String topcolorid = "0", bodycolorid = "0";
			String[] optionids = null, arrivaldates;
			ArrayList<String> trans_option_ids = new ArrayList<String>();
			ArrayList<String> optioncolorids = new ArrayList<String>();// for top and body color

			String[] month = {"", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
			String mon = "", year = "";
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
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

			int h = 0;
			int j = 0;
			count = 0;
			stockcount = 0;
			updatecount = 0;
			if (columnLength != 14) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					for (h = 0; h < columnLength; h++) {

						// / S no
						if (h == 0)
						{
						}

						// commission Number
						if (h == 1) {
							vehstock_branch_id = "1";
							vehstock_vehstocklocation_id = "1";
							vehstock_comm_no = "";
							vehstock_comm_no = PadQuotes(sheetData[j][h]);
							if (vehstock_comm_no.equals("")) {
								errormsg += "Commission Number should not be empty! <br>";
							}
						}

						// Availability
						if (h == 2) {
							vehstock_arrival_date = "";
							arrivaldate = "";
							vehstock_arrival_date = sheetData[j][h].trim();
							// SOP("vehstock_arrival_date===" + vehstock_arrival_date);
							arrivaldate = vehstock_arrival_date;
							if (!arrivaldate.equals("")) {
								arrivaldate = arrivaldate.substring(arrivaldate.length() - 6, arrivaldate.length());
								arrivaldates = arrivaldate.split("'");
								if (Arrays.asList(month).indexOf(arrivaldates[0].toLowerCase()) != -1) {
									mon = String.valueOf(Arrays.asList(month).indexOf(arrivaldates[0].toLowerCase()));
									if (mon.length() == 1) {
										mon = "0" + mon;
									}
									year = arrivaldates[1];
									if (year.length() == 2) {
										year = "20" + year;
									}
									if (!mon.equals("02")) {
										arrivaldate = year + mon + "30" + "000000";
									} else if (mon.equals("02")) {
										arrivaldate = year + mon + "28" + "000000";
									}
									if (arrivaldate.length() != 14) {
										errormsg += "Invalid Date! <br>";
									}
								}
								// SOP("arrivaldate==" + arrivaldate);
							}
						}

						// Model type
						if (h == 3) {
							vehstock_item_code = "";
							vehstock_item_id = "0";
							vehstock_item_code = PadQuotes(sheetData[j][h]);
							if (vehstock_item_code.equals("")) {
								errormsg = "Model type should not be empty! <br>";
							} else if (!vehstock_item_code.equals("")) {
								vehstock_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_code = '" + vehstock_item_code + "'"
										+ " AND  item_type_id = 1"));
							}
							if (vehstock_item_id.equals("0")) {
								errormsg += "Invalid Model type! <br>";
							}
							// SOP("vehstock_item_id==" + vehstock_item_id);
						}

						// Model
						if (h == 4) {
						}

						// Model details
						if (h == 5) {

						}

						// Model Year
						if (h == 6) {
							vehstock_modelyear = "";
							vehstock_modelyear = PadQuotes(sheetData[j][h]);
							if (vehstock_modelyear.equals("")) {
								errormsg = "Model Year should not be empty! <br>";
							} else if (!vehstock_modelyear.equals("") && vehstock_modelyear.length() > 4) {
								errormsg = "Invalid Model Year! <br>";
							}
							// SOP("vehstock_modelyear==" + vehstock_modelyear);
						}

						// Body Color
						if (h == 7) {
							bodycolour = "";
							bodycolorid = "0";
							optioncolorids.clear();
							bodycolour = PadQuotes(sheetData[j][h]);
							// SOP("bodycolour==" + bodycolour);
							if (!bodycolour.equals("")) {
								if (bodycolorcheck.containsKey(bodycolour)) {
									bodycolorid = bodycolorcheck.get(bodycolour);
									optioncolorids.add(bodycolorid);
								} else if (!bodycolorcheck.containsKey(bodycolour)) {
									StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
											+ " WHERE option_code = '" + bodycolour + "'"
											+ " AND  option_optiontype_id = 1"
											+ " AND  option_brand_id = 56";
									// SOP("StrSql===" + StrSql);
									bodycolorid = CNumeric(ExecuteQuery(StrSql));
									if (bodycolorid.equals("0")) {
										errormsg += "No stock option code found with name: " + bodycolour + "<br>";
									} else if (!bodycolorid.equals("0")) {
										optioncolorids.add(bodycolorid);
									}
								}
							}
						}
						// Body Color detail
						if (h == 8) {
						}

						// Top Color
						if (h == 9) {
							topcolour = "";
							topcolorid = "0";
							topcolour = PadQuotes(sheetData[j][h]);
							// SOP("topcolor==" + topcolour);
							if (!topcolour.equals("")) {
								if (topcolorcheck.containsKey(topcolour)) {
									topcolorid = topcolorcheck.get(topcolour);
									optioncolorids.add(topcolorid);
								} else if (!topcolorcheck.containsKey(topcolour)) {
									StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
											+ " WHERE option_code = '" + topcolour + "'"
											+ " AND  option_optiontype_id = 3"
											+ " AND  option_brand_id = 56";
									// SOP("StrSql===" + StrSql);
									topcolorid = CNumeric(ExecuteQuery(StrSql));
									if (topcolorid.equals("0")) {
										errormsg += "No stock option code found with name: " + topcolour + "<br>";
									} else if (!topcolorid.equals("0")) {
										optioncolorids.add(topcolorid);
									}
								}
							}

						}
						// Top Color detail
						if (h == 10) {
						}
						// Interior Color
						if (h == 11) {
							interiorcolor = "";
							interiorcolor = PadQuotes(sheetData[j][h]);
						}
						// Interior Color detail
						if (h == 12) {

						}
						// Options
						if (h == 13) {
							optionid = "";
							optionidcheck = "0";
							trans_option_ids.clear();
							optionid = PadQuotes(sheetData[j][h]);
							if (!interiorcolor.equals("")) {
								if (!optionid.equals("")) {
									optionid += ",";
								}
								optionid += interiorcolor + ",";
							}
							if (!optionid.equals("")) {
								optionids = optionid.split(",");
							}
							for (int i = 0; i < optionids.length; i++) {
								if (optionidscheck.containsKey(optionids[i])) {
									trans_option_id = optionidscheck.get(optionids[i]);
									trans_option_ids.add(trans_option_id);
								} else if (!optionidscheck.containsKey(optionids[i])) {
									StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
											+ " WHERE option_code = '" + PadQuotes(optionids[i]) + "'"
											+ " AND  option_brand_id = 56";
									// SOP("StrSql===" + StrSql);
									optionidcheck = CNumeric(ExecuteQuery(StrSql));
									if (optionidcheck.equals("0")) {
										errormsg += "No stock option code found with name: " + optionids[i] + "<br>";
									} else if (!optionidcheck.equals("0")) {
										trans_option_ids.add(optionidcheck);
									}
								}
							}
						}
					}
					if (errormsg.equals("") && !vehstock_comm_no.equals("")
							&& !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0")
							&& !vehstock_vehstocklocation_id.equals("0")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							ResultSet rs = null;
							StrSql = "SELECT vehstock_id"
									+ " FROM " + compdb(comp_id) + "axela_vehstock"
									+ " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'";
							// SOP("StrSql==stockid==" + StrSql);
							String vehstock_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("vehstock_id==" + vehstock_id);

							if (!vehstock_id.equals("0")) {
								// /// Update Stock
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_branch_id = " + vehstock_branch_id + ","
										+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
										+ " vehstock_item_id = " + vehstock_item_id + ","
										+ " vehstock_comm_no = '" + vehstock_comm_no + "',"
										+ " vehstock_arrival_date = '" + arrivaldate + "',"
										+ " vehstock_delstatus_id = 2,"// IN transit
										+ " vehstock_status_id = 3,"// ETA
										+ " vehstock_notes='',"
										+ " vehstock_modelyear = '" + vehstock_modelyear + "',"
										+ " vehstock_modified_id = " + vehstock_entry_id + ","
										+ " vehstock_modified_date = '" + vehstock_entry_date + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								// SOP("StrSql==Stock UPDATE==" + StrSqlBreaker(StrSql));
								stmttx.execute(StrSql);
								// SOP("count==" + count++);
								StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " WHERE trans_vehstock_id = " + vehstock_id + "";
								// SOP("StrSql==del==" + StrSqlBreaker(StrSql));
								updateQuery(StrSql);
								Set<String> optionidscheckunique = new LinkedHashSet<String>(trans_option_ids);
								for (int i = 0; i < optioncolorids.size(); i++) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
											+ " (trans_option_id,"
											+ " trans_vehstock_id)"
											+ " VALUES"
											+ " (" + optioncolorids.get(i) + ","
											+ " " + vehstock_id + ")";
									// SOP("StrSql==Stock trans==" + StrSqlBreaker(StrSql));
									stmttx.addBatch(StrSql);
								}
								if (optionidscheckunique.size() != 0) {
									for (String transoptionids : optionidscheckunique) {
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
												+ " (trans_option_id,"
												+ " trans_vehstock_id)"
												+ " VALUES"
												+ " (" + transoptionids + ","
												+ " " + vehstock_id + ")";
										stmttx.addBatch(StrSql);
									}
								}
								stmttx.executeBatch();
								conntx.commit();
								updatecount++;
							} else {
								if (!vehstock_comm_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0") && !vehstock_vehstocklocation_id.equals("0")) {

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
											+ "("
											+ " vehstock_item_id,"
											+ " vehstock_branch_id,"
											+ " vehstock_vehstocklocation_id,"
											+ " vehstock_comm_no,"
											+ " vehstock_status_id,"
											+ " vehstock_delstatus_id,"
											+ " vehstock_arrival_date,"
											+ " vehstock_modelyear,"
											+ " vehstock_notes,"
											+ " vehstock_entry_id,"
											+ " vehstock_entry_date)"
											+ " VALUES"
											+ "( '" + vehstock_item_id + "',"
											+ " " + vehstock_branch_id + ","
											+ " " + vehstock_vehstocklocation_id + ","
											+ " '" + vehstock_comm_no + "',"
											+ " 3,"// ETA
											+ " 2,"// IN transit
											+ " '" + arrivaldate + "',"
											+ " '" + vehstock_modelyear + "',"
											+ "'',"// vehstock_notes
											+ " " + vehstock_entry_id + ","
											+ " '" + vehstock_entry_date + "')";
									// SOP("StrSql==Insert Stock==" + StrSql);
									stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
									rs = stmttx.getGeneratedKeys();
									while (rs.next()) {
										vehstock_id = rs.getString(1);
									}
									rs.close();
									Set<String> optionidscheckunique = new LinkedHashSet<String>(trans_option_ids);
									for (int i = 0; i < optioncolorids.size(); i++) {
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
												+ " (trans_option_id,"
												+ " trans_vehstock_id)"
												+ " VALUES"
												+ " (" + optioncolorids.get(i) + ","
												+ " " + vehstock_id + ")";
										// SOP("StrSql==Stock trans==" + StrSqlBreaker(StrSql));
										stmttx.addBatch(StrSql);
									}
									if (optionidscheckunique.size() != 0) {
										for (String transoptionids : optionidscheckunique) {
											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
													+ " (trans_option_id,"
													+ " trans_vehstock_id)"
													+ " VALUES"
													+ " (" + transoptionids + ","
													+ " " + vehstock_id + ")";
											stmttx.addBatch(StrSql);
										}

									}
									stmttx.executeBatch();
									conntx.commit();
									stockcount++;
								}
							}
						} catch (Exception e) {
							if (conntx.isClosed()) {
								SOPError("connection is closed...");
							}
							if (!conntx.isClosed() && conntx != null) {
								conntx.rollback();
								SOPError("connection rollback...");
							}
							msg = "<br>Transaction Error!";
							SOPError("Axelaauto== " + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
						} finally {
							conntx.setAutoCommit(true);
							if (stmttx != null && !stmttx.isClosed()) {
								stmttx.close();
							}
							if (conntx != null && !conntx.isClosed()) {
								conntx.close();
							}
						}
					}

					if (!errormsg.equals("")) {
						stockerrormsg += "<br>" + ++count + "." + "Commission Number: " + vehstock_comm_no + "===>" + "<br>" + errormsg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
