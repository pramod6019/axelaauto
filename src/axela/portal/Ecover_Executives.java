package axela.portal;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.Ecover_WS;

import com.google.gson.Gson;

public class Ecover_Executives extends Connect {
	
	public String updateB = "";
	public static String msg = "";
	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_recperpage;
	public CachedRowSet crs = null;
	String[] res = null;
	JSONObject input = new JSONObject();
	JSONObject empdetails = new JSONObject();
	JSONObject emptransdetails = new JSONObject();
	JSONObject empphotos = new JSONObject();
	JSONObject branchdetails = new JSONObject();
	JSONObject branchtransdetails = new JSONObject();
	JSONObject branchexeaccess = new JSONObject();
	JSONObject empexeaccess = new JSONObject();
	JSONObject soedetails = new JSONObject();
	JSONObject soetransdetails = new JSONObject();
	JSONObject soeexeaccess = new JSONObject();
	StringBuilder transobj = null;
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	Executives_Update updateexe = new Executives_Update();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				updateB = PadQuotes(request.getParameter("update_button"));
				if ("Update".equals(updateB)) {
					StrSql = "SELECT comp_ecover_integration "
							+ "FROM " + compdb(comp_id) + "axela_comp"
							+ " WHERE comp_id=" + comp_id;
					// SOP("StrSql=======" + StrSql);
					if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
						EcoverUpdateEmp();
						// EcoverUpdateEmpexe();
						// EcoverUpdateBranch();
						// EcoverUpdateBranchTrans();
						new Ecover_WS().WSRequest(input, "axelaauto-executives", request);
						// WSMultiPartRequest("executives-photo");
						response.sendRedirect(response.encodeRedirectURL("ecover-executives.jsp?msg=Ecover Executives Updated Succesfully!"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("ecover-executives.jsp?msg=Integration For Ecover Denied!"));
					}
				}
				if (AppRun().equals("0")) {
					SOP("input==Ecover_Executive_Univ_Check===" + input.toString(1));
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
	
	public void EcoverUpdateEmp() throws JSONException, SQLException {
		StrSql = "SELECT emp_id, emp_uuid, emp_name, emp_branch_id, emp_photo, emp_role_id, emp_upass, emp_ref_no,"
				+ " emp_sex, emp_dob, emp_married, emp_qualification, emp_certification, emp_phone1,"
				+ " emp_phone2, emp_mobile1, emp_mobile2, emp_email1, emp_email2, emp_address, emp_city,"
				+ " emp_pin, emp_state, emp_landmark, emp_weeklyoff_id, emp_active, emp_prevexp, emp_date_of_join, "
				+ " emp_date_of_relieve, emp_reason_of_leaving, emp_notes, emp_recperpage, emp_timeout,"
				+ " emp_theme_id, emp_entry_id, emp_entry_date, emp_modified_id, emp_modified_date"
				+ " FROM " + compdb(comp_id) + "axela_emp";
		crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			int i = 0;
			while (crs.next()) {
				i++;
				JSONObject emp = new JSONObject();
				emp.put("emp_id", crs.getString("emp_id"));
				emp.put("emp_uuid", crs.getString("emp_uuid"));
				emp.put("emp_name", crs.getString("emp_name"));
				emp.put("emp_photo", crs.getString("emp_photo"));
				emp.put("emp_role_id", crs.getString("emp_role_id"));
				emp.put("emp_upass", crs.getString("emp_upass"));
				emp.put("emp_ref_no", crs.getString("emp_ref_no"));
				emp.put("emp_sex", crs.getString("emp_sex"));
				emp.put("emp_dob", crs.getString("emp_dob"));
				emp.put("emp_married", crs.getString("emp_married"));
				emp.put("emp_qualification", crs.getString("emp_qualification"));
				emp.put("emp_certification", crs.getString("emp_certification"));
				emp.put("emp_phone1", crs.getString("emp_phone1"));
				emp.put("emp_phone2", crs.getString("emp_phone2"));
				emp.put("emp_mobile1", crs.getString("emp_mobile1"));
				emp.put("emp_mobile2", crs.getString("emp_mobile2"));
				emp.put("emp_email1", crs.getString("emp_email1"));
				emp.put("emp_email2", crs.getString("emp_email2"));
				emp.put("emp_address", crs.getString("emp_address"));
				emp.put("emp_pin", crs.getString("emp_pin"));
				emp.put("emp_city", crs.getString("emp_city"));
				emp.put("emp_state", crs.getString("emp_state"));
				emp.put("emp_landmark", crs.getString("emp_landmark"));
				emp.put("emp_branch_id", crs.getString("emp_branch_id"));
				emp.put("emp_weeklyoff_id", crs.getString("emp_weeklyoff_id"));
				emp.put("emp_active", crs.getString("emp_active"));
				emp.put("emp_prevexp", crs.getString("emp_prevexp"));
				emp.put("emp_date_of_join", crs.getString("emp_date_of_join"));
				emp.put("emp_date_of_relieve", crs.getString("emp_date_of_relieve"));
				emp.put("emp_reason_of_leaving", crs.getString("emp_reason_of_leaving"));
				emp.put("emp_notes", crs.getString("emp_notes"));
				emp.put("emp_recperpage", crs.getString("emp_recperpage"));
				emp.put("emp_timeout", crs.getString("emp_timeout"));
				emp.put("emp_theme_id", crs.getString("emp_theme_id"));
				emp.put("emp_entry_id", crs.getString("emp_entry_id"));
				emp.put("emp_entry_date", crs.getString("emp_entry_date"));
				emp.put("emp_modified_id", crs.getString("emp_modified_id"));
				emp.put("emp_modified_date", crs.getString("emp_modified_date"));
				emp.put("update", "yes");
				empdetails.put("emp_" + i, emp);
			}
			input.put("empdetails", empdetails);
		}
		crs.close();
	}
	
	public void EcoverUpdateEmpexe() throws JSONException, SQLException {
		try {
			StrSql = "SELECT * FROM "
					+ compdb(comp_id) + "axela_emp_exe"
					+ " ORDER BY empexe_emp_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StringBuilder empexe = new StringBuilder();
				while (crs.next()) {
					if (empexeaccess.has(crs.getString("empexe_emp_id"))) {
						empexe.append(",").append(crs.getString("empexe_id"));
						empexeaccess.put(crs.getString("empexe_emp_id"), empexe);
					} else {
						empexe = new StringBuilder();
						empexe.append(crs.getString("empexe_id"));
						empexeaccess.put(crs.getString("empexe_emp_id"), empexe);
					}
				}
			}
			crs.close();
			input.put("empexeaccess", empexeaccess);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	
	public void EcoverUpdateBranch() throws JSONException, SQLException {
		try {
			StringBuilder cols = new StringBuilder();
			StrSql = "SELECT COLUMN_NAME"
					+ " FROM information_schema.`COLUMNS`"
					+ " WHERE TABLE_SCHEMA = 'axelaauto_" + comp_id + "'"
					+ " AND TABLE_NAME = 'axela_branch'";
			crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					cols = cols.append(crs.getString("COLUMN_NAME")).append(",");
				}
			}
			crs.close();
			cols = cols.deleteCharAt(cols.length() - 1);
			res = cols.toString().split(",");
			StrSql = "SELECT " + cols
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_id";
			crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				int i = 0;
				while (crs.next() && res != null) {
					i++;
					JSONObject branch = new JSONObject();
					for (int j = 0; j < res.length; j++) {
						branch.put(res[j], crs.getString(res[j]));
					}
					branchdetails.put("branch_" + i, branch);
				}
			}
			input.put("branchdetails", branchdetails);
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	
	public void EcoverUpdateBranchTrans() throws JSONException, SQLException {
		try {
			StrSql = "SELECT * FROM "
					+ compdb(comp_id) + "axela_emp_branch"
					+ " ORDER BY emp_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StringBuilder branchexe = new StringBuilder();
				while (crs.next()) {
					if (branchexeaccess.has(crs.getString("emp_id"))) {
						branchexe.append(",").append(crs.getString("emp_branch_id"));
						branchexeaccess.put(crs.getString("emp_id"), branchexe);
						
					} else {
						branchexe = new StringBuilder();
						branchexe.append(crs.getString("emp_branch_id"));
						branchexeaccess.put(crs.getString("emp_id"), branchexe);
					}
				}
			}
			crs.close();
			input.put("branchexeaccess", branchexeaccess);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void WSMultiPartRequest(String uri) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";
		String result = "";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		
		try {
			InputStream fileInputStream = null;
			new Ecover_WS().comp_id = comp_id;
			URL url = new URL(new Ecover_WS().WSRunnerUrl() + uri);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			
			String emp_image = null;
			InputStream emp_photo = null;
			String emp_id = "";
			StrSql = "SELECT emp_photo,emp_id FROM "
					+ compdb(comp_id) + "axela_emp"
					+ " ORDER BY emp_id";
			// SOP("StrSql===========" + StrSql);
			crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					emp_id = crs.getString("emp_id");
					outputStream.writeBytes(twoHyphens + boundary + lineEnd);
					outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "emp_photo" + "\"; filename=\"emp_" + emp_id + ".jpg\"" + lineEnd);
					outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
					outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
					outputStream.writeBytes(lineEnd);
					fileInputStream = new ByteArrayInputStream(crs.getBytes("emp_photo"));
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						outputStream.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					outputStream.writeBytes(lineEnd);
					
				}
			}
			
			// Upload comp_id
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "comp_id" + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(comp_id);
			outputStream.writeBytes(lineEnd);
			
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			inputStream = connection.getInputStream();
			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}