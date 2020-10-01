package axela.portal;

/**
 *
 ** Created on August 18, 2008, 5:07 PM
 *
 * @author nivedhitha
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Fetchdocs extends HttpServlet {

	public String StrHTML = "";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String StrSqlblob = "";
	public static String filename = "";
	public static String filePath = "";
	public String QueryString = "";
	public String img_type = "";
	public String enquiry_doc_id = "";
	public String veh_doc_id = "0";
	public String testdrive_id = "0";
	public String export_id = "0";
	// public String testdrive_doc_id = "0";
	public String preowned_doc_id = "0";
	public String enquiry_brochure_id = "0";
	public String insurance_doc_id = "";
	public String inscomp_id = "";
	public String asset_doc_id = "";
	public String vehicle_id = "";
	public String name = "";
	public String doc_id = "";
	public String emp_doc_id = "0";
	public String customer_doc_id = "0";
	public String sodoc_id = "";
	public String faq_id = "";
	public String project_doc_id = "";
	public String customer_id = "0";
	public String task_doc_id = "";
	public String ticket_doc_id = "";
	public String faq_doc_id = "";
	public String ticketfaq_doc_id = "";
	public String jc_doc_id = "";
	public String filetitle = "";
	public String doc_value = "";
	public String doc_title = "";
	public String testdrive_doc_value = "";
	public byte[] testdrive_doc_data = null;
	public String export_value = "";
	public byte[] export_data = null;
	byte[] buffer = null;
	CachedRowSet crs = null;
	public Connect ct = new Connect();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		try {
			ct.CheckSession(request, response);
			HttpSession session = request.getSession(true);
			QueryString = ct.PadQuotes(request.getQueryString());
			faq_id = ct.PadQuotes(request.getParameter("faq_id"));
			// comp_id = ct.CNumeric(GetSession("comp_id", request));
			comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_doc_id = ct.CNumeric(ct.PadQuotes(request.getParameter("emp_doc_id")));
				customer_doc_id = ct.CNumeric(ct.PadQuotes(request.getParameter("customer_doc_id")));
				sodoc_id = ct.PadQuotes(request.getParameter("sodoc_id"));
				vehicle_id = ct.PadQuotes(request.getParameter("vehicle_id"));
				enquiry_doc_id = ct.PadQuotes(request.getParameter("enquiry_doc_id"));
				veh_doc_id = ct.CNumeric(ct.PadQuotes(request.getParameter("veh_doc_id")));
				// ct.SOP("testdrive_id-------------fetch---------" + testdrive_id);
				img_type = ct.PadQuotes(request.getParameter("image_type"));
				testdrive_id = ct.CNumeric(ct.PadQuotes(request.getParameter("testdrive_id")));
				export_id = ct.CNumeric(ct.PadQuotes(request.getParameter("export_id")));
				customer_id = ct.CNumeric(ct.PadQuotes(request.getParameter("customer_id")));
				// testdrive_doc_id = ct.PadQuotes(request.getParameter("testdrive_doc_id"));
				preowned_doc_id = ct.CNumeric(ct.PadQuotes(request.getParameter("doc_preowned_id")));
				enquiry_brochure_id = ct.CNumeric(ct.PadQuotes(request.getParameter("enquiry_brochure_id")));
				asset_doc_id = ct.PadQuotes(request.getParameter("asset_doc_id"));
				insurance_doc_id = ct.PadQuotes(request.getParameter("insur_doc_id"));
				inscomp_id = ct.PadQuotes(request.getParameter("inscomp_id"));
				project_doc_id = ct.PadQuotes(request.getParameter("project_doc_id"));
				task_doc_id = ct.PadQuotes(request.getParameter("task_doc_id"));
				ticket_doc_id = ct.PadQuotes(request.getParameter("doc_id"));
				jc_doc_id = ct.PadQuotes(request.getParameter("jc_doc_id"));
				faq_doc_id = ct.PadQuotes(request.getParameter("faq_doc_id"));
				ticketfaq_doc_id = ct.PadQuotes(request.getParameter("ticketfaq_doc_id"));
				if (!faq_doc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_faq_docs where doc_id = " + faq_doc_id + "";
					filePath = ct.FaqExePath(comp_id);
				}
				if (!enquiry_doc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_sales_enquiry_docs where doc_id = " + enquiry_doc_id + "";
					filePath = ct.EnquiryDocPath(comp_id);
				}
				if (!testdrive_id.equals("0") && img_type.equals("testdrive")) {
					StrSqlblob = "SELECT testdrive_doc_data, testdrive_doc_value from " + ct.compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "";
					// System.out.println("StrSql=========" + StrSql);
					crs = ct.processQuery(StrSqlblob, 0);
					while (crs.next()) {
						testdrive_doc_data = crs.getBytes("testdrive_doc_data");
						testdrive_doc_value = crs.getString("testdrive_doc_value");
					}
					crs.close();
					download1(request, response);
				}

				if (!testdrive_id.equals("0") && img_type.equals("preownedtestdrive")) {
					StrSqlblob = "SELECT testdrive_doc_data, testdrive_doc_value from " + ct.compdb(comp_id) + "axela_preowned_testdrive WHERE testdrive_id = " + testdrive_id + "";
					// System.out.println("StrSql=========" + StrSql);
					crs = ct.processQuery(StrSqlblob, 0);
					while (crs.next()) {
						testdrive_doc_data = crs.getBytes("testdrive_doc_data");
						testdrive_doc_value = crs.getString("testdrive_doc_value");
					}
					crs.close();
					download1(request, response);
				}

				if (!customer_id.equals("0")) {
					StrSqlblob = "SELECT customer_gst_doc,customer_gst_doc_value FROM " + ct.compdb(comp_id) + "axela_customer WHERE customer_id = " + customer_id + "";
					// System.out.println("StrSqlblob=========" + StrSqlblob);
					crs = ct.processQuery(StrSqlblob, 0);
					while (crs.next()) {
						testdrive_doc_data = crs.getBytes("customer_gst_doc");
						testdrive_doc_value = crs.getString("customer_gst_doc_value");
						// ct.SOP("testdrive_doc_data===" + testdrive_doc_data);
					}
					crs.close();
					download1(request, response);
				}

				if (!export_id.equals("0")) {
					StrSqlblob = "SELECT export_data, export_value from " + ct.compdb(comp_id) + "axela_emp_export where export_id = " + export_id + "";
					System.out.println("StrSql=========" + StrSql);
					crs = ct.processQuery(StrSqlblob, 0);
					while (crs.next()) {
						export_data = crs.getBytes("export_data");
						export_value = crs.getString("export_value");
					}
					crs.close();
					download2(request, response);
				}

				// if (!testdrive_id.equals("0")) {
				// StrSql = "Select testdrive_doc_value from " + ct.compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "";
				// ct.SOP("StrSql-----------" + StrSql);
				// filePath = ct.TestDriveDocPath(comp_id);
				// }

				if (!preowned_doc_id.equals("0")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_preowned_docs where doc_id = " + preowned_doc_id + "";
					filePath = ct.PreownedDocPath(comp_id);
				}

				if (!ticketfaq_doc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_service_ticket_faq_docs where doc_id = " + ticketfaq_doc_id + "";
					filePath = ct.FaqDocPath(comp_id);
				}

				if (!ticket_doc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_service_ticket_docs where doc_id = " + ticket_doc_id + "";
					filePath = ct.TicketDocPath(comp_id);
				}

				if (!veh_doc_id.equals("0")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_service_veh_docs where doc_id = " + veh_doc_id + "";
					filePath = ct.VehicleDocPath(comp_id);
				}

				if (!jc_doc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_service_jc_docs where doc_id = " + jc_doc_id + "";
					filePath = ct.JobCardDocPath(comp_id);
				}

				if (!inscomp_id.equals("")) {
					StrSql = "Select inscomp_value as doc_value, inscomp_title as doc_title from " + ct.compdb(comp_id) + "axela_insurance_comp where inscomp_id = " + inscomp_id + "";
					filePath = ct.InsurCompDocPath(comp_id);
				}

				if (!emp_doc_id.equals("0")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_emp_docs where doc_id = " + emp_doc_id + "";
					filePath = ct.ExeDocPath(comp_id);
				}

				if (!customer_doc_id.equals("0")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_customer_docs where doc_id = " + customer_doc_id + "";
					filePath = ct.CustomerDocPath(comp_id);
				}

				// if (!faq_doc_id.equals("")) {
				// StrSql = "Select faq_value as doc_value, faq_title as doc_title from " + ct.compdb() + "" + compdb(comp_id) + "axela_service_ticket_faq where faq_id = " + faq_doc_id + "";
				// filePath = ct.FaqDocPath(comp_id);
				// }

				if (!sodoc_id.equals("")) {
					StrSql = "Select doc_value, doc_title from " + ct.compdb(comp_id) + "axela_sales_so_docs where doc_id = " + sodoc_id + "";
					filePath = ct.SODocPath(comp_id);
				}

				if (!StrSql.equals("") && enquiry_brochure_id.equals("0")) {
					// ct.SOP("StrSql==ddddddd==" + StrSql);
					CachedRowSet crs = ct.processQuery(StrSql, 0);
					while (crs.next()) {
						filetitle = crs.getString("doc_title") + ct.fileext(crs.getString("doc_value"));
						filename = crs.getString("doc_value");
					}
					crs.close();
					download(request, response);
				}

				if (!enquiry_brochure_id.equals("0")) {
					StrSql = "Select brochure_value, brochure_title from " + ct.compdb(comp_id) + "axela_sales_enquiry_brochure where brochure_id = " + enquiry_brochure_id + "";
					// ct.SOP("StrSql---b-----" + StrSql);
					filePath = ct.EnquiryBrochurePath(comp_id);
					if (!StrSql.equals("")) {
						CachedRowSet crs = ct.processQuery(StrSql, 0);
						while (crs.next()) {
							filetitle = crs.getString("brochure_title") + ct.fileext(crs.getString("brochure_value"));
							filename = crs.getString("brochure_value");
						}
						crs.close();
						download(request, response);
					}
				}
			}
		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void download(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {
			String idocPath = filePath;
			String fileName = filename;

			String filePath = idocPath + fileName;
			response.setContentType("APPLICATION/OCTET-STREAM");
			String disHeader = "Attachment;Filename=" + fileName;

			response.setHeader("Content-Disposition", disHeader);
			File file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				response.getOutputStream().write(i);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
			fileInputStream.close();
		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void download1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {
			int b = 0;
			String idocPath = filePath;
			String fileName = filename;
			BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(testdrive_doc_data));
			response.setContentType("application/octet-stream");
			// response.setContentLength(Integer.parseInt(doc_filesize) * 1024);
			// ct.SOP("ext========" + ct.fileext(doc_value));
			String header_key = "Content-Disposition";
			String header_value = String.format("attachment; filename=\"%s\"", testdrive_doc_value);
			response.setHeader(header_key, header_value);
			OutputStream out = response.getOutputStream();
			buffer = new byte[2048];
			while ((b = is.read(buffer)) != -1) {
				out.write(buffer, 0, b);
			}
			is.close();
			out.close();

		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void download2(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {
			int b = 0;
			String idocPath = filePath;
			String fileName = filename;
			BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(export_data));
			response.setContentType("application/octet-stream");
			// response.setContentLength(Integer.parseInt(doc_filesize) * 1024);
			// ct.SOP("ext========" + ct.fileext(doc_value));
			String header_key = "Content-Disposition";
			String header_value = String.format("attachment; filename=\"%s\"", export_value);
			response.setHeader(header_key, header_value);
			OutputStream out = response.getOutputStream();
			buffer = new byte[2048];
			while ((b = is.read(buffer)) != -1) {
				out.write(buffer, 0, b);
			}
			is.close();
			out.close();

		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
