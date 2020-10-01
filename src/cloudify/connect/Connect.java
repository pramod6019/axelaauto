package cloudify.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

public class Connect extends ConnectDate {

	public static String ClientName = "Axela Auto Demo";
	public static String ClientTeam = "Team Emax";
	public static String AppName = "AxelaAuto";
	public static int empcount = 65;
	public static int franchiseecount = 1;
	public static int branchcount = 1;
	public static int autosales = 1;
	public static int autousedsales = 0;
	public static int autopreowned = 1;
	public static int autoservice = 1;
	public static String jsver = "2";
	public static String ImageFormats = ".jpg, .jpeg, .gif, .png";
	public static String SmsBrand = "MySms";
	public static String helpno = "+91 9845 170 170";
	DecimalFormat df = new DecimalFormat("0.00");

	public Connect() {
	}

	public String AppRun() {
		String State = "0";
		State = new app.AppState().AppRunState();
		// State = "1";
		return State;

	}

	public String AppPath() {
		String path = System.getProperty("user.dir");

		return path;

	}

	public Date kknow() {
		ConnectDate cdate = new ConnectDate();
		Date date;
		if (AppRun().equals("1")) {
			date = cdate.AddHoursDate(new Date(), 0, 0, 0);
		} else {
			date = cdate.AddHoursDate(new Date(), 0, 0, 0);
		}
		return date;
	}

	// public Connection connectDB() {
	// try {
	// // Class.forName("org.gjt.mm.mysql.Driver");
	// if (AppRun().equals("1")) {
	// myBroker = new DbConnectionBroker(
	// "com.mysql.jdbc.Driver",
	// "jdbc:mysql://127.0.0.1:3306/axelaauto?useOldAliasMetadataBehavior=true"
	// +
	// "&autoReconnect=yes&useSSL=false&requireSSL=false&verifyServerCertificate=false",
	// "root", "test", 2, 5, MediaPath()
	// + "/log/DCB_Example1.log", 1.0);
	// } else {
	// myBroker = new DbConnectionBroker(
	// "com.mysql.jdbc.Driver",
	// //
	// "jdbc:mysql://192.168.0.6:3306/axelaauto?useOldAliasMetadataBehavior=true",
	// "jdbc:mysql://192.168.0.10:3306/axelaauto?useOldAliasMetadataBehavior=true&autoReconnect=yes&useSSL=false",
	// "root", "test", 2, 5, MediaPath()
	// + "/log/DCB_Example1.log", 1.0);
	// }
	//
	// } catch (Exception ex) {
	// SOPError("AxelaAuto===" + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// }
	// return this.myBroker.getConnection();
	// }

	public Connection connectDB() {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/ConnectDB");
			conn = ds.getConnection();

			// SOP("con=========" + conn);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		// SOP("connection returned......");
		return conn;
	}

	public String AppURL() {
		String url;
		if (AppRun().equals("1")) {
			url = "http://demo.axelaauto.com/axelaauto/";
		} else {
			url = "http://localhost:8010/axelaauto/";
		}
		return url;
	}

	public String WSUrl() {
		String url = "";
		if (AppRun().equals("1")) {
			url = "http://demo.axelaauto.com/axelaauto/ws/axelaauto/";
		} else {
			url = "http://192.168.0.15:8010/axelaauto/ws/axelaauto/";
		}
		return url;
	}

	public String MediaPath() {
		String path;
		if (AppRun().equals("1")) {
			path = "c:/media/axelaauto/media";
			path = "//var/media/axelaauto/media";
		} else {
			path = getClass().getClassLoader().getResource("").getPath();
			path = path.substring(0, path.indexOf("."));
			path = path + "axelaauto/media";
		}
		return path;
	}

	public String CompLogoPath() {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/complogo/";
		} else {
			path = MediaPath() + "/complogo/";
		}
		return path;
	}

	public String BranchLogoPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/branchlogo/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/branchlogo/";
		}
		return path;
	}

	public String TemplatePath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/template/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/template/";
		}
		return path;
	}

	public String CachePath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/cache/axelaauto_" + comp_id + "/";
		} else {
			path = MediaPath() + "/cache/axelaauto_" + comp_id + "/";
		}
		return path;
	}

	public String ModelImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/modelimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/modelimg/";
		}
		return path;
	}

	public String FeatureImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/featureimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/featureimg/";
		}
		return path;
	}

	public String ModelColourImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/modelcoloursimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/modelcoloursimg/";
		}
		return path;
	}

	public String ExeImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/exeimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/exeimg/";
		}
		return path;
	}

	public String ExeDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/exedocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/exedocs/";
		}
		return path;
	}

	public String CustomerDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/customerdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/customerdocs/";
		}
		return path;
	}

	public String ContactDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/contactdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/contactdocs/";
		}
		return path;
	}

	public String LeadImportPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/leadimport/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/leadimport/";
		}
		return path;
	}

	public String EnquiryImportPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquiryimport/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquiryimport/";
		}
		return path;
	}

	public String VehicleImportPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/vehicleimport/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/vehicleimport/";
		}
		return path;
	}

	public String ItemImportPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/itemimport/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/itemimport/";
		}
		return path;
	}

	public String EnquiryDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquirydocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquirydocs/";
		}
		return path;
	}

	public String EnquiryBrochurePath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquirybrochure/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/enquirybrochure/";
		}
		return path;
	}

	public String DemoDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/demodocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/demodocs/";
		}
		return path;
	}

	public String TestDriveDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/testdrivedocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/testdrivedocs/";
		}
		return path;
	}

	public String SODocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/sodocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/sodocs/";
		}
		return path;
	}

	public String VehicleDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/vehicledocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/vehicledocs/";
		}
		return path;
	}

	public String TicketDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/ticketdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/ticketdocs/";
		}
		return path;
	}

	public String ContractDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/contractdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/contractdocs/";
		}
		return path;
	}

	public String JobCardFuelGuagePath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcardfuelguage/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcardfuelguage/";
		}
		return path;
	}

	public String JobCardDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcarddocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcarddocs/";
		}
		return path;
	}

	public String JobCardImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcardimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/jobcardimg/";
		}
		return path;
	}

	public String CampaignImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/campaignimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/campaignimg/";
		}
		return path;
	}

	public String ItemImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/itemimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/itemimg/";
		}
		return path;
	}

	public String PreownedImgPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/preownedimg/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/preownedimg/";
		}
		return path;
	}

	public String PreownedDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/preowneddocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/preowneddocs/";
		}
		return path;
	}

	public String StockDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/stockdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/stockdocs/";
		}
		return path;
	}

	public String InsurCompDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/insurcompdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/insurcompdocs/";
		}
		return path;
	}

	public String FaqDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/faqdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/faqdocs/";
		}
		return path;
	}

	public String FaqExePath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/faqexedocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/faqexedocs/";
		}
		return path;
	}

	public String VoucherDocPath(String comp_id) {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/axelaauto_" + comp_id + "/voucherdocs/";
		} else {
			path = MediaPath() + "/axelaauto_" + comp_id + "/voucherdocs/";
		}
		return path;
	}

	public String TempPath() {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/temp/";
		} else {
			path = MediaPath() + "/temp/";
		}
		return path;
	}

	public String CachePath() {
		String path;
		if (AppRun().equals("1")) {
			path = MediaPath() + "/cache/";
		} else {
			path = MediaPath() + "/cache/";
		}
		return path;
	}

	public String CompImageSize(String comp_id) {
		return ExecuteQuery("SELECT config_image_size FROM " + compdb(comp_id)
				+ "" + compdb(comp_id) + "axela_config");
	}

	public String LimitRecords(int totalrecords, String pagecurrent) {
		int StartRec = 0;
		int EndRec = 0;
		int recperpage = 25;
		StartRec = ((Integer.parseInt(pagecurrent) - 1) * recperpage) + 1;
		EndRec = ((Integer.parseInt(pagecurrent) - 1) * recperpage)
				+ recperpage;
		return " limit " + (StartRec - 1) + ", " + recperpage + "";

	}

	public String ImageSize(String comp_id) {
		return ExecuteQuery("Select config_image_size from " + compdb(comp_id)
				+ "axela_config ");
	}

	public String ReportStartdate() {
		ConnectDate cdate = new ConnectDate();
		String date;
		if (AppRun().equals("1")) {
			date = cdate.strToShortDate(cdate.ToShortDate(kknow()));
		} else {
			date = cdate.strToShortDate(cdate.ToShortDate(cdate.AddHoursDate(
					kknow(), -30, 0, 0)));
		}
		return date;
	}

	public String maindb() {
		return "axela_";
	}

	public String compdb(String id) {
		return "axelaauto_" + id + ".";
	}

	// / ddmotors customer app
	public String ddmotors_app_comp_id() {
		String comp_id = "";
		if (AppRun().equals("1")) {
			comp_id = "1009";
		} else {
			comp_id = "1000";
		}
		return comp_id;
	}

	// axelaauto app
	public String axelaauto_app_comp_id() {
		String comp_id = "";
		if (AppRun().equals("1")) {
			comp_id = "1009";
		} else {
			comp_id = "1000";
		}
		return comp_id;
	}

	public void SOP(String Str) {
		if (AppRun().equals("0")) {
			System.out.println(Str);
		}
	}

	public void SOPInfo(String Str) {
		if (AppRun().equals("0")) {
			SOP(Str);
		} else {
			final Logger logger = Logger.getLogger(this.getClass());
			logger.info(Str);
		}
	}

	public void SOPError(String Str) {
		if (AppRun().equals("0")) {
			SOP(Str);
		} else {
			final Logger logger = Logger.getLogger(this.getClass());
			logger.error(Str);
		}
	}

	// public ResultSet processQuery(String strSQL) {
	// CachedRowSet crs =null;
	// try {
	// conn = connectDB();
	// stmt = conn.createStatement();
	// // SOPError("ProcessQuery strSQL---"+strSQL);
	// rs = stmt.executeQuery(strSQL);
	// return (rs);
	// } catch (Exception ex) {
	// SOPError("AxelaAuto=== " + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName()
	// + " : " + ex);
	// return null;
	// } finally {
	// // try {
	// // if (stmt != null) {
	// // stmt.close();
	// // }
	// // } catch (Exception ex) {
	// // SOPError("AxelaAuto=== " + this.getClass().getName());
	// // SOPError("Error in " + new
	// // Exception().getStackTrace()[0].getMethodName() + " : " + ex);
	// // }
	// // // // The connection is returned to the Broker
	// // myBroker.freeConnection(conn);
	// // myBroker.destroy();
	// }
	//
	// }
	public CachedRowSet processQuery(String StrSql) {
		ResultSet rspq = null;
		Connection conn = connectDB();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(StrSql);
			rspq = stmt.executeQuery();
			CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			// CachedRowSet crs = null;
			crs.populate(rspq);
			return (crs);
		} catch (SQLException ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return null;
		} finally {
			try {
				if (rspq != null) {
					rspq.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				SOPError(this.getClass().getName());
				SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			}
		}
	}

	public CachedRowSet processQuery(String StrSQL, int minutes) {
		// // StrSql = compdb(StrSql);

		ResultSet rspq = null;
		Connection conn = connectDB();
		Statement stmt = null;
		try {

			stmt = conn.createStatement();
			// SOPError("ProcessQuery strSQL---"+strSQL);
			rspq = stmt.executeQuery(StrSQL);
			CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			// CachedRowSet crs = null;
			// crs.setMetaData((RowSetMetaData) rspq.getMetaData());
			crs.populate(rspq);
			return (crs);
		} catch (SQLException ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return null;
		} finally {
			try {
				if (rspq != null) {
					rspq.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ " : " + ex);
			}
		}
	}

	public CachedRowSet processPrepQuery(String StrSql,
			Map<Integer, Object> qparams, int minutes) {
		// StrSql = compdb(StrSql);
		if (!StrSql.contains("axelaauto_0.axela_")) {
			ResultSet rspq = null;
			Connection conn = connectDB();
			PreparedStatement stmt = null;
			try {
				// SOPError("processPrepQuery strSQL---"+strSQL);
				stmt = conn.prepareStatement(StrSql);
				for (Integer key : qparams.keySet()) {
					stmt.setObject(key, qparams.get(key));
				}
				// SOP("From Connect class stmt====" + stmt);
				rspq = stmt.executeQuery();
				CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
				// CachedRowSet crs = null;
				crs.populate(rspq);
				return (crs);
			} catch (SQLException ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ " : " + ex);
				return null;
			} finally {
				try {
					if (rspq != null) {
						rspq.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0].getMethodName()
							+ " : " + ex);
				}
			}
		}
		return null;
	}

	public int updateQuery(String strSQL) {
		int count;
		Connection conn = connectDB();
		Statement stmt = null;
		try {

			stmt = conn.createStatement();
			count = stmt.executeUpdate(strSQL);
			return (count);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return 0;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
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

	public String ExecuteQuery(String strSQL) {
		ResultSet rs = null;
		String res = "";
		if (!strSQL.contains("axelaauto_0.axela_")) {
			Connection conn = connectDB();
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				// SOP("strSQL in ExecuteQuery--"+strSQL);
				rs = stmt.executeQuery(strSQL);
				while (rs.next()) {
					res = rs.getString(1);
				}
				// SOPError("res in ExecuteQuery--"+res);

				return res;

			} catch (Exception ex) {
				SOPError("AxelaAuto=== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ " : " + ex);
				return "0";
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (rs != null) {
						rs.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception ex) {
					SOPError("AxelaAuto=== " + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0].getMethodName()
							+ " : " + ex);
				}
				// The connection is returned to the Broker
			}
		}
		return res;
	}

	public String UpdateQueryReturnID(String strSQL) {
		ResultSet rs = null;
		PreparedStatement stmtGetLastID = null, pstmt = null;
		String res = "0";
		Connection conn = connectDB();
		try {
			pstmt = conn.prepareStatement(strSQL);
			pstmt.executeUpdate();
			stmtGetLastID = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			rs = stmtGetLastID.executeQuery();
			while (rs.next()) {
				res = rs.getString(1);
			}
			// SOPError(res+" res");
			return res;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "0";
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (stmtGetLastID != null) {
					stmtGetLastID.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				SOPError("AxelaAuto=== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ " : " + ex);
			}
		}
	}

	public String PadQuotes(String str) {
		if (str != null) {
			String str1 = str.trim();
			str1 = str1.replace("'", "&#39;");
			// str1=str1.replace("\n","\\\n");
			return str1;
		} else {
			return "";
		}
	}

	public String JSONPadQuotes(String output) {
		if (!output.equals("")) {
			output = output.replaceAll("\\\\", "");
			output = output.replace("[\"", "[");
			output = output.replace("\"]", "]");
			output = output.replace("\"{", "{");
			output = output.replace("}\"", "}");
			output = output.replace("\"[", "[");
			output = output.replace("]\"", "]");

			output = output.replace("u003cbru003e", "\n");
			output = output.replace("u003cbr/u003e", "\n");
			output = output.replace("<br>", "\n");
			output = output.replace("<br/>", "\n");
			output = output.replace("u003cu003d", "<=");
			output = output.replace("u003e", ">");
			output = output.replace("u003c", "<");
			output = output.replace("u0026#40;", "(");
			output = output.replace("u0026#41;", ")");

			output = output.replace("u003e", ">");
			output = output.replace("u003c", "<");
			output = output.replace("u003d", "=");
			output = output.replace("u0026", "&");
			output = output.replace("u0027", "'");
		}
		return output;
	}

	public JSONObject GetJSONObject(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;

		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return new JSONObject();
	}

	public String GetJSONString(JSONObject jsonObject, String str) {
		try {
			if (jsonObject == null || !jsonObject.has(str)) {
				return "";
			} else {
				return jsonObject.getString(str);
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Axelaauto===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			return "";
		}
	}

	public String StrSqlBreaker(String StrSql) {
		String str = StrSql;
		if (!str.equals("")) {
			str = str.replace(" SELECT ", "\nSELECT ");
			str = str.replace(" FROM ", "\nFROM ");
			str = str.replace(" COALESCE", "\nCOALESCE");
			str = str.replace(" CONCAT", "\nCONCAT");
			str = str.replace(" IF", "\nIF");
			str = str.replace(" UNION", "\nUNION");
			str = str.replace(" INNER ", "\nINNER ");
			str = str.replace(" LEFT ", "\nLEFT ");
			str = str.replace(" WHERE ", "\nWHERE ");
			str = str.replace(" GROUP ", "\nGROUP ");
			str = str.replace(" HAVING ", "\nHAVING ");
			str = str.replace(" ORDER ", "\nORDER ");
			str = str.replace(" LIMIT ", "\nLIMIT ");
		}
		return str;
	}

	public String PageNavi(String PageURL, int PageCurrent, int PageCount, int PageListSize) {
		String PageNavi = "<div><ul class=\"pagination\">";
		PageNavi += "&nbsp;";
		for (int i = (PageListSize * (Math
				.abs(((PageCurrent - 1) / PageListSize)))) + 1; i <= (PageListSize * (Math
				.abs((PageCurrent - 1) / PageListSize))) + PageListSize; i++) {
			if (i > PageCount) {
				break;
			}
			if (i == PageCurrent) {
				PageNavi += "<li><a><font color='red'>" + i
						+ "</font></a></li>";
			} else {
				PageNavi += "<li><a href='" + PageURL + i + "'>" + i
						+ "</a></li>";
			}
		}
		PageNavi += "</ul></div>";
		return PageNavi;
	}

	public String PageNaviJS(String JsMethod, int PageCurrent, int PageCount, int PageListSize) {

		String PageNavi = "<div><center><ul class=\"pagination\">";

		if (PageCurrent > PageListSize) {
			PageNavi += "<li><a href=\"JavaScript:void();\"  onClick=\"" + JsMethod + "('" + (PageListSize * (PageCurrent / PageListSize) - PageListSize + 1) + "')\"><b><<</b></a></li>";
		}
		for (int i = (PageListSize * (Math
				.abs(((PageCurrent - 1) / PageListSize)))) + 1; i <= (PageListSize * (Math
				.abs((PageCurrent - 1) / PageListSize))) + PageListSize; i++) {
			if (i > PageCount) {
				break;
			}
			if (i == PageCurrent) {
				PageNavi += "<li><a><font color='red'>" + i + "</font></a></li>&nbsp;&nbsp;";
			} else {
				PageNavi += "<li><a href=\"JavaScript:void();\" onClick=\"" + JsMethod + "('" + i + "')\">" + i + "</a></li>&nbsp;&nbsp;";
			}
		}
		if (PageCount > (PageListSize * (Math.abs((PageCurrent - 1) / PageListSize))) + PageListSize) {
			PageNavi += "<li><a href=\"JavaScript:void();\" onClick=\"" + JsMethod + "('" + (PageListSize * (Math.abs((PageCurrent - 1) / PageListSize)) + PageListSize + 1)
					+ "')\"><b>>></b></a></li>";
		}
		PageNavi += "</ul></center></div>";
		return PageNavi;
	}

	public String Selectdrop(int idvalue, String dropid) {
		try {
			if (!(dropid == null) && !(dropid.equals(""))) {
				if (idvalue == Integer.parseInt(dropid)) {
					return " selected";
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public String StrSelectdrop(String value, String dropid) {
		try {
			if (!(dropid == null) && !(dropid.equals(""))) {
				if (value.equals(dropid)) {
					return " selected";
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return " ";
		}
	}

	public String ArrSelectdrop(int idvalue, String[] dropid) {
		try {
			if ((dropid != null)) {

				for (int i = 0; i < dropid.length; i++) {
					if (dropid[i] != null) {
						if (idvalue == Integer.parseInt(dropid[i])) {
							return " selected";
						}
					}
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public String StrArrSelectdrop(String idvalue, String[] dropvalue) {
		try {
			if ((dropvalue != null)) {

				for (int i = 0; i < dropvalue.length; i++) {
					if (dropvalue[i] != null) {
						if (idvalue.equals(dropvalue[i])) {
							return " selected";
						}
					}
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public String ArrSelectCheck(int idvalue, String[] dropid) {
		try {
			if ((dropid != null)) {

				for (int i = 0; i < dropid.length; i++) {
					if (dropid[i] != null) {
						if (idvalue == Integer.parseInt(dropid[i])) {
							return " Checked";
						}
					}
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public String StrArrSelectCheck(String idvalue, String checkid) {
		try {
			String a[] = idvalue.split(",");
			if ((checkid != null)) {
				for (int i = 0; i < a.length; i++) {
					if (a[i].equals(checkid)) {
						return " Checked";
					}
				}
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public String PopulateCheck(String checkid) {
		try {
			if (!(checkid == null) && (checkid.equals("1"))) {
				return "Checked ";
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return " ";
		}
	}

	public boolean isNumeric(String s) {
		String validChars = "0123456789.";
		boolean isNumeric = true;
		for (int i = 0; i < s.length() && isNumeric; i++) {
			char c = s.charAt(i);
			if (validChars.indexOf(c) == -1) {
				isNumeric = false;
			} else {
				isNumeric = true;
			}
		}
		return isNumeric;
	}

	public boolean isNumericNeg(String s) {
		String validChars = "0123456789.-";
		boolean isNumeric = true;
		for (int i = 0; i < s.length() && isNumeric; i++) {
			char c = s.charAt(i);
			if (validChars.indexOf(c) == -1) {
				isNumeric = false;
			} else {
				isNumeric = true;
			}
		}
		return isNumeric;
	}

	public String CNumeric(String s) {
		if (s.equals("")) {
			return "0";
		} else if (isNumeric(s)) {
			return s;
		} else if (isNumericNeg(s)) {
			return s;
		} else {
			return "0";
		}
	}

	public boolean isFloat(String number) {
		if (!number.equals("")) {
			if (number.contains("-")) {
				if ((number.indexOf("-") != number.lastIndexOf("-"))
						|| (number.indexOf("-") != 0)) {
					return false;
				}
			}

			if (number.contains(".")) {
				if ((number.indexOf(".") != number.lastIndexOf("."))
						|| number.lastIndexOf(".") == number.length() - 1) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public String doublenum(int fNum) {
		String doublenum;
		if (fNum > 9) {
			doublenum = "" + fNum;
		} else {
			doublenum = "0" + fNum;
		}
		return doublenum;
	}

	public String triplenum(int fNum) {
		String triplenum;
		if (fNum > 99) {
			triplenum = "" + fNum;
		} else if (fNum > 9) {
			triplenum = "0" + fNum;
		} else {
			triplenum = "00" + fNum;
		}
		return triplenum;
	}

	public String getIP(HttpServletRequest request) {
		String IP = "";
		if (request.getHeader("x-forwarded-for") != null) {
			IP = request.getHeader("x-forwarded-for").toString();
		}
		if (request.getRemoteHost() != null && IP.equals("")) {
			IP = request.getRemoteHost().toString();
		}
		return IP;
	}

	public void CheckSession(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			if (GetSession("emp_id", request).equals("")) {
				if (AppRun().equals("1")) {
					response.sendRedirect(response
							.encodeRedirectURL("../portal/index.jsp?msg=Your session has expired."));
				} else {
					// SOPError("executed....");
					AssignSession("1000", "1", "0", "1", "1", "0", "0", "", "",
							"10", 100, request);
				}
			}
		} catch (Exception e) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + e);
		}
	}

	public void CheckAppSession(String emp_uuid, String comp_id,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(true);
			// if (CNumeric(session.getAttribute("emp_id") + "").equals("0") &&
			// !emp_uuid.equals("")) {
			if (CNumeric(GetSession("emp_id", request) + "").equals("0")
					&& !emp_uuid.equals("")) {
				String StrSql = " SELECT emp_id, emp_branch_id, emp_role_id, emp_all_exe, emp_all_branches,"
						+ " emp_recperpage, emp_timeout, emp_clicktocall, emp_routeno, emp_callerid "
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_emp "
						+ " WHERE 1=1 AND emp_uuid='" + emp_uuid + "'";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					AssignSession(comp_id, crs.getString("emp_id"),
							crs.getString("emp_branch_id"),
							crs.getString("emp_role_id"),
							crs.getString("emp_all_exe"),
							crs.getString("emp_all_branches"),
							crs.getString("emp_clicktocall"),
							crs.getString("emp_routeno"),
							crs.getString("emp_callerid"),
							crs.getString("emp_recperpage"), 20, request);

					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_log"
					// + " (log_emp_id,"
					// + " log_session_id,"
					// + " log_comp_id,"
					// + " log_emp_app,"
					// + " log_remote_host,"
					// + " log_remote_agent,"
					// + " log_attemptcount,"
					// + " log_signin_time, "
					// + " log_signout_time)"
					// + " VALUES"
					// + " (" + crs.getString("emp_id") + ","
					// + " '',"
					// + " " + comp_id + ","
					// + "'1',"
					// + " '',"
					// + " '',"
					// + " '0',"
					// + " '" + ToLongDate(kknow()) + "',"
					// + " '')";
					// updateQuery(StrSql);

				}
				crs.close();
			}
		} catch (Exception e) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + e);
		}
	}

	public void AssignSession(String comp_id, String emp_id, String branch_id,
			String role_id, String emp_all_exe, String emp_all_branches, String emp_clicktocall,
			String emp_routeno, String emp_callerid, String recperpage,
			int emp_timeout, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(true);
			String sessionid = session.getId();
			session.setAttribute("emp_id", emp_id);
			String accountingmodule = "";
			String StrSql = "";
			String Str = "", BranchStr = "";
			Map setMap;
			if (session.getAttribute("sessionMap") == null) {
				setMap = new HashMap();
				session.setAttribute("sessionMap", setMap);
			}
			setMap = (Map) session.getAttribute("sessionMap");
			setMap.put("emp_id", Integer.parseInt(emp_id));
			setMap.put("emp_branch_id", branch_id);
			setMap.put("emp_role_id", role_id);
			setMap.put("emp_all_exe", emp_all_exe);
			setMap.put("emp_all_branches", emp_all_branches);
			setMap.put("emp_recperpage", recperpage);
			setMap.put("emp_clicktocall", emp_clicktocall);
			setMap.put("emp_routeno", emp_routeno);
			setMap.put("emp_callerid", emp_callerid);
			setMap.put("comp_id", comp_id);
			session.setMaxInactiveInterval(emp_timeout * 60);
			// test
			if (!branch_id.equals("0")) {
				setMap.put("BranchAccess", " and branch_id = " + branch_id + "");
				BranchStr = " and branch_id = " + branch_id + "";
				// } else if (role_id.equals("1") && branch_id.equals("0")) {
				// session.setAttribute("BranchAccess", "");
			} else if (emp_all_branches.equals("1")) {
				session.setAttribute("BranchAccess", "");
			} else if ((emp_all_branches.equals("0")) && branch_id.equals("0")) {

				StrSql = "SELECT emp_branch_id"
						+ " from " + compdb(comp_id) + "axela_emp_branch"
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id "
						+ " where branch_active = 1 and emp_id = " + emp_id;

				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.isFirst()) {
							Str = Str + " branch_id="
									+ crs.getString("emp_branch_id");
						} else {
							Str = Str + " OR branch_id="
									+ crs.getString("emp_branch_id");
						}
					}
				} else {
					Str = Str + "0";
				}
				BranchStr = " and (" + Str + ")";
				setMap.put("BranchAccess", " and (" + Str + ")");
			}
			if (emp_id.equals("1")) {
				setMap.put("ExeAccess", "");
			}
			// else if (!BranchStr.equals("") && emp_all_exe.equals("1")) {
			// StrSql = "SELECT emp_id " + " FROM " + compdb(comp_id)
			// + "axela_emp " + " WHERE emp_active=1 "
			// + BranchStr.replace("branch_id", "emp_branch_id");
			// // SOP("StrSql-----" + StrSql);
			// CachedRowSet crs = processQuery(StrSql, 0);
			// Str = " ";
			// if (crs.isBeforeFirst()) {
			// while (crs.next()) {
			// Str = Str + " OR emp_id=" + crs.getString("emp_id");
			// }
			// }
			// crs.close();
			// setMap.put("ExeAccess", " and (emp_id = " + emp_id + Str + ")");
			//
			// }
			else if (emp_all_exe.equals("0")) {
				StrSql = "SELECT empexe_id from " + compdb(comp_id)
						+ "axela_emp_exe where empexe_emp_id = " + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				Str = " ";
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str = Str + " OR emp_id=" + crs.getString("empexe_id");
					}
				}
				crs.close();
				setMap.put("ExeAccess", " and (emp_id = " + emp_id + Str + ")");
			}

			// stat module access
			StrSql = "select "
					+ "CONCAT('<b>',emp_name,'</b><br>(',jobtitle_desc,')') as emp_name, emp_photo,"
					+ " emp_role_id," + " emp_copy_access,"
					+ " emp_mis_access,"
					+ " COALESCE(branch_logo,'') branch_logo," + " comp_name,"
					+ " comp_logo," + " comp_module_preowned,"
					+ " comp_module_service,"
					+ " comp_email_enable,"
					+ " comp_module_inventory, "
					+ "	comp_module_accessories,"
					+ " comp_module_insurance, "
					+ " comp_module_invoice,"
					+ " comp_module_accounting,"
					+ " comp_module_app," + " comp_sms_enable,"
					+ " COALESCE(user_emp_id, 0) as user_emp_id" + " from "
					+ compdb(comp_id) + "axela_emp"
					+ " inner join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_user on user_emp_id = emp_id"
					+ " AND user_jsessionid = '" + sessionid + "',"
					+ compdb(comp_id) + "axela_comp," + " "
					+ compdb(comp_id) + "axela_config"
					+ " WHERE emp_active = '1'"
					+ " AND emp_id = " + emp_id
					+ " AND comp_active = '1'";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				setMap.put("user_emp_id", crs.getString("user_emp_id"));
				setMap.put("emp_name", crs.getString("emp_name"));
				setMap.put("emp_photo", crs.getString("emp_photo"));
				setMap.put("emp_role_id", crs.getString("emp_role_id"));
				setMap.put("emp_mis_access", crs.getString("emp_mis_access"));
				setMap.put("emp_copy_access", crs.getString("emp_copy_access"));

				// if (!crs.getString("branch_logo").equals("")) {
				// setMap.put(
				// "comp_logo",
				// "<img src=../Thumbnail.do?branchlogo="
				// + crs.getString("branch_logo")
				// + "&width=150 alt=''>");
				// } else if (!crs.getString("comp_logo").equals("")) {
				// setMap.put(
				// "comp_logo",
				// "<img src=../Thumbnail.do?complogo="
				// + crs.getString("comp_logo")
				// + "&width=150 alt=''>");
				// }
				if (!crs.getString("branch_logo").equals("")) {
					setMap.put(
							"comp_logo",
							"../Thumbnail.do?branchlogo="
									+ crs.getString("branch_logo"));
				} else if (!crs.getString("comp_logo").equals("")) {
					setMap.put(
							"comp_logo",
							"../Thumbnail.do?complogo="
									+ crs.getString("comp_logo"));
				} else {
					setMap.put("comp_logo", "");
				}
				setMap.put("comp_sms_enable", crs.getString("comp_sms_enable"));
				setMap.put("comp_email_enable", crs.getString("comp_email_enable"));
				setMap.put("comp_module_preowned", crs.getString("comp_module_preowned"));
				accountingmodule = crs.getString("comp_module_accounting");
				setMap.put("comp_module_service", crs.getString("comp_module_service"));
				setMap.put("comp_module_inventory", crs.getString("comp_module_inventory"));
				setMap.put("comp_module_accessories", crs.getString("comp_module_accessories"));
				setMap.put("comp_module_insurance", crs.getString("comp_module_insurance"));
				setMap.put("comp_module_invoice", crs.getString("comp_module_invoice"));
				setMap.put("comp_module_accounting", crs.getString("comp_module_accounting"));
				setMap.put("comp_module_app", crs.getString("comp_module_app"));
				// setMap.put("config_inventory_current_stock",
				// crs.getString("config_inventory_current_stock"));
			}
			// if (session.getAttribute("sessionMap") == null) {
			// setMap = new HashMap();
			// session.setAttribute("sessionMap", setMap);
			// }
			// session.setAttribute("sessionMap", setMap);
			crs.close();
			if (accountingmodule.equals("1")) {
				BuildAccMenuLinks(request, comp_id);
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public void CheckPerm(String comp_id, String fieldname,
			HttpServletRequest request, HttpServletResponse response) {
		String CheckPerm = "", msg = "", fieldn_name = "", emp_id = "";
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!emp_id.equals("1")) {
				String field[] = fieldname.split(",");
				for (int i = 0; i < field.length; i++) {
					// emp_export_access emp_report_access emp_mis_access
					if ((PadQuotes(field[i]).equals("emp_role_id"))
							|| (PadQuotes(field[i]).equals("emp_export_access"))
							|| (PadQuotes(field[i]).equals("emp_report_access"))
							|| (PadQuotes(field[i]).equals("emp_mis_access"))
							|| (PadQuotes(field[i]).equals("emp_emaxpm"))) {
						fieldn_name = fieldn_name + " " + field[i].trim()
								+ "= 1 or ";
					} else {
						fieldn_name = fieldn_name + " access_name='"
								+ field[i].trim() + "' or ";
					}
					// SOP("field===" + field[i]);
				}
				String Strsql = "Select emp_id FROM " + compdb(comp_id) + "axela_emp "
						+ " left JOIN " + compdb(comp_id) + "axela_emp_access on empaccess_emp_id=emp_id "
						+ " left JOIN axela_module_access on access_id=empaccess_access_id "
						+ " where emp_id=" + CNumeric(GetSession("emp_id", request))
						+ " and ("
						+ fieldn_name.substring(0,
								fieldn_name.lastIndexOf("or")) + ")"
						+ " GROUP BY emp_id";
				// SOP("CheckPerm==strsql-----------" + Strsql);
				CheckPerm = ExecuteQuery(Strsql);
				// SOP("CheckPerm===" + CheckPerm);
				if (CheckPerm.equals("")) {
					msg = "msg=Access denied. Please contact system administrator!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public String CheckAdminPerm(String comp_id, String fieldname,
			HttpServletRequest request, HttpServletResponse response) {
		String CheckAdminPerm, msg = "";
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckAdminPerm = ExecuteQuery("Select " + fieldname + " from "
					+ compdb(comp_id) + "axela_comp_user where user_id="
					+ session.getAttribute("user_id") + " ");
			// SOPError("CheckAdminPerm : "+CheckAdminPerm);
			if (CheckAdminPerm.equals("0")) {
				msg = "msg=Access denied. Please contact system administrator!";
				response.sendRedirect("error.jsp?" + msg);
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
		return msg;
	}

	public String CheckChannelPerm(String comp_id, String fieldname,
			HttpServletRequest request, HttpServletResponse response) {
		String CheckAdminPerm, msg = "";
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckAdminPerm = ExecuteQuery("Select " + fieldname + " from "
					+ compdb(comp_id)
					+ "axela_channel_user where channeluser_id="
					+ session.getAttribute("channeluser_id") + " ");
			// SOPError("CheckAdminPerm : "+CheckAdminPerm);
			if (CheckAdminPerm.equals("0")) {
				msg = "msg=Access denied. Please contact system administrator!";
				response.sendRedirect("error.jsp?" + msg);
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
		return msg;
	}

	public String ReturnPerm(String comp_id, String fieldname,
			HttpServletRequest request) {
		String CheckPerm = "1", msg = "", fieldn_name = "", emp_id = "";
		try {
			HttpSession session = request.getSession(true);
			// emp_id = CNumeric(GetSession("emp_id", request));
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!emp_id.equals("1")) {
				String field[] = fieldname.split(",");
				for (int i = 0; i < field.length; i++) {
					// emp_export_access emp_report_access emp_mis_access
					if (field[i].equals("emp_role_id")
							|| field[i].equals("emp_export_access")
							|| field[i].equals("emp_report_access")
							|| field[i].equals("emp_mis_access")) {
						fieldn_name = fieldn_name + " " + field[i].trim()
								+ "= 1 or ";
					} else {
						fieldn_name = fieldn_name + " access_name='"
								+ field[i].trim() + "' or ";
					}
				}
				String Strsql = "Select emp_id"
						+ " from "
						+ compdb(comp_id)
						+ "axela_emp"
						+ " left JOIN "
						+ compdb(comp_id)
						+ "axela_emp_access on empaccess_emp_id=emp_id "
						+ " left JOIN axela_module_access on access_id=empaccess_access_id "
						+ " where emp_id="
						+ session.getAttribute("emp_id")
						+ " and ("
						+ fieldn_name.substring(0,
								fieldn_name.lastIndexOf("or")) + ")"
						+ " GROUP BY emp_id";

				// SOPError("Strsql : " + StrSqlBreaker(Strsql));
				CheckPerm = ExecuteQuery(Strsql);

				if (!CheckPerm.equals("")) {
					CheckPerm = "1";
				} else {
					CheckPerm = "0";
				}
			}

		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
		return CheckPerm;
	}

	public String Exename(String comp_id, int exeid) {
		if (exeid != 0) {
			return ExecuteQuery("Select CONCAT('<a href=../portal/executive-summary.jsp?emp_id=',emp_id,'>', emp_name, ' (',emp_ref_no,')','</a>') as exe"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_emp where emp_id="
					+ exeid + " ");
		} else {
			return "No Executive found!";
		}
	}

	public String ExeDetailsPopover(int emp_id, String emp_name, String emp_ref_no) {
		StringBuilder Str = new StringBuilder();
		if (emp_id != 0) {

			Str.append("<a"
					+ " class=popovers"
					+ " data-toggle=popover"
					// + " data-trigger=hover"
					+ " data-html=true"
					+ " data-placement='auto right'"
					+ " data-title='<center>" + emp_name + "</center>'"
					+ " data-content=\""
					+ "Loading executive details, Please wait..."
					+ "");

			Str.append("\" "
					+ "onmouseover=setTimeout(function(){$('.popover-content').attr('id','popover-content');showHint('../portal/executive-summary-popover.jsp?emp_id="
					+ emp_id + "','popover-content')},300);"
					+ "target='_blank' href=../portal/executive-summary.jsp?emp_id=" + emp_id + ">"
					+ emp_name);
			if (!emp_ref_no.equals("")) {
				Str.append(" (" + emp_ref_no).append(")");
			}
			Str.append("</a>");

			return Str.toString();
		} else {
			return "No Executive found!";
		}
	}
	public String ExenameHeader(String comp_id, int exeid) {
		if (exeid != 0) {

			// return
			// ExecuteQuery("Select CONCAT('<b>',emp_name,'</b><br>(',jobtitle_desc,')') "
			return ExecuteQuery("Select CONCAT('<b>',emp_name,'</b><br>(',role_name,')') "
					+ " from "
					+ compdb(comp_id)
					+ "axela_emp "
					// +
					// "inner join axela_jobtitle on jobtitle_id = emp_jobtitle_id "
					+ "inner join "
					+ compdb(comp_id)
					+ "axela_emp_role on role_id = emp_jobtitle_id "
					+ " where emp_id=" + exeid + " ");

		} else {
			return "No Executive found!";
		}

	}

	public String UsernameHeader(int userid) {
		if (userid != 0) {
			// return
			// ExecuteQuery("Select CONCAT('<b>',emp_name,'</b><br>(',jobtitle_desc,')') "
			return ExecuteQuery("Select CONCAT('<b>',user_name,'</b><br>(',user_jobtitle,')') "
					+ " from axela_comp_user "
					// +
					// "inner join axela_jobtitle on jobtitle_id = emp_jobtitle_id "
					// +
					// "inner join axela_emp_role on role_id = emp_jobtitle_id "
					+ " where user_id=" + userid + " ");
		} else {
			return "No Executive found!";
		}
	}

	public String ReturnConfig(String fieldname, String comp_id) {
		String perm = "";
		try {
			perm = ExecuteQuery("Select " + fieldname + " from "
					+ compdb(comp_id) + "axela_comp where comp_id=" + comp_id);
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
		return perm;
	}

	public String WebValidate(String strWeb) {
		if (strWeb.startsWith("http://")) {
			strWeb = strWeb.substring(7);
		} else if (strWeb.startsWith("http:/")) {
			strWeb = strWeb.substring(6);
		} else if (strWeb.startsWith("http:")) {
			strWeb = strWeb.substring(5);
		} else if (strWeb.startsWith("http")) {
			strWeb = strWeb.substring(4);
		}
		return strWeb;
	}

	// to return the checkbox value
	public String CheckBoxValue(String checkBox) {
		try {
			if (checkBox.equals("on")) {
				checkBox = "1";
			} else {
				checkBox = "0";
			}
			return checkBox;
		} catch (Exception e) {
			return "0";
		}
	}

	public String CheckNull(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static boolean IsValidBranchCode(String code) {
		boolean isvalid = true;
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(code);
		if (m.find() || code.contains(" ")) {
			isvalid = false;
		}
		return isvalid;
	}

	public static boolean IsValidSubdomain(String code) {
		String exp = "^[a-z0-9-]{2,100}$";
		boolean isvalid = false;
		CharSequence inputstr = code;
		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(inputstr);
		if (matcher.matches()) {
			isvalid = true;
		}
		return isvalid;
	}

	public static boolean IsValidURL(String url) {
		String exp = "^[\\w\\.-]+\\.([\\w\\-]+\\.)+[A-Z]{2,4}$";
		boolean isvalid = false;
		CharSequence inputstr = url;
		Pattern pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputstr);
		if (matcher.matches()) {
			isvalid = true;
		}
		return isvalid;
	}

	public static boolean IsValidEmail(String email) {
		String exp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
		boolean isvalid = false;
		CharSequence inputstr = email;
		Pattern pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputstr);
		if (matcher.matches()) {
			isvalid = true;
		}
		return isvalid;
	}

	public String CheckDuplicateEmail(String email[], String fieldname) {
		HashSet<String> set = new HashSet<String>();
		String msg = "";
		for (String checkset : email) {
			if (!set.add(checkset)) {
				msg = "<br>Duplicate " + fieldname + " Email found!";
			}
		}
		return msg;
	}

	public String RemoveDuplicateEmails(String email) {
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		email = email.toLowerCase();
		if (!email.equals("")) {
			if (email.charAt(0) == ',') {
				email = email.replaceFirst(",", "");
			}

			if (email.endsWith(",")) {
				email = email.substring(0, email.length() - 2);
			}

			for (String str : email.split(",")) {
				set.add(str.trim());
			}

			return set.toString().replace("[", "").replace("]", "");
		} else {
			return "";
		}
	}

	public boolean IsValidMobileNo(String mobileno) {
		try {
			boolean isvalid = false;
			if (mobileno.equals("")) {
				return true;
			}
			if (mobileno.contains("-")) {
				return isvalid;
			}
			if (mobileno.length() == 10) {
				isvalid = true;
			}
			return isvalid;
		} catch (Exception ex) {
			SOPError("Demo == " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return false;
		}
	}

	public boolean IsValidPhoneNo(String phoneno) {
		boolean isvalid = false;
		if (phoneno.equals("")) {
			return true;
		}
		if (phoneno.length() != 12) {
			isvalid = false;
		} else {
			if (phoneno.contains("-")) {
				String[] str = phoneno.split("-");
				if (str.length > 1) {
					if (str[0].equals("") || str[1].equals("")) {
						return isvalid;
					} else if (Integer.parseInt("" + str[0].charAt(0)) != 0) {
						return isvalid;
						// } else if (str[0].length() != 3) {
						// return isvalid;
						// } else if (str[1].length() != 8) {
						// return isvalid;
					} else {
						isvalid = true;
					}
				}
			}
		}
		return isvalid;
	}

	public boolean IsValidMobileNo11(String mobileno) { // modified by aJIt
		try {
			boolean isvalid = false;
			if (mobileno.equals("")) {
				return true;
			}
			if (mobileno.replaceAll("[^-]", "").length() != 1) {
				return isvalid;
			} else {
				String[] str = mobileno.split("-");
				if (str.length == 2) {
					if (str[0].equals("") || str[1].equals("")) {
						return isvalid;
					}
					else if (((str[0].length() + str[1].length()) > 12) || ((str[0].length() + str[1].length()) < 7)) {
						return isvalid;
					}
					else if (str[0].equals("91")) {
						if (mobileno.length() != 13) {
							return isvalid;
						}
					}
					isvalid = true;
				}
			}
			return isvalid;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return false;
		}
	}

	public boolean IsValidPhoneNo11(String phoneno) {
		boolean isvalid = false;
		if (phoneno.equals("")) {
			return true;
		}
		if (phoneno.replaceAll("[^-]", "").length() != 2) {
			return isvalid;
		} else {
			String[] str = phoneno.split("-");
			if (str[0].equals("") || str[1].equals("") || str[2].equals("")) {
				return isvalid;
			}
			else if (((str[0].length() + str[1].length() + str[2].length()) > 13) || ((str[0].length() + str[1].length() + str[2].length()) < 7)) {
				return isvalid;
			}
			else if (str[0].equals("91")) {
				if (phoneno.length() != 14) {
					return isvalid;
				}
			}
			isvalid = true;
		}
		return isvalid;
	}

	public boolean IsValidRegNo(String reg_no) {
		if (reg_no.length() < 2) {
			return false;
		}
		return true;
	}

	public String IsValidUsername(String Username) {
		String msg = "";
		Pattern p = Pattern.compile("[^A-Za-z0-9_]");
		Matcher m = p.matcher(Username);
		if (m.find() || Username.contains(" ") || Username.length() <= 3) {
			msg = msg + "4 to 20 characters(A-Z a-z 0-9 no spaces)";
		}
		return msg;
	}

	public String ReplaceStr(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		String lStr = str.toLowerCase();
		String lPattern = pattern.toLowerCase();
		StringBuilder result = new StringBuilder();
		while ((e = lStr.indexOf(lPattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString().trim();
	}

	public String toTitleCase(String str) {
		StringBuilder sb = new StringBuilder();
		str = str.toLowerCase();
		StringTokenizer strTitleCase = new StringTokenizer(str);
		while (strTitleCase.hasMoreTokens()) {
			String s = strTitleCase.nextToken();
			int scount = strTitleCase.countTokens();
			if (!s.substring(0, 1).equals("(")) {
				sb.append(s.replaceFirst(s.substring(0, 1), s.substring(0, 1)
						.toUpperCase()));
			} else {
				sb.append(s);
			}
			if (scount != 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public String SplitPhoneNo(String number, int parm, String type) {
		String output = "";
		String[] arrystr = number.split("-");
		String code = arrystr[0];
		int l = arrystr.length;
		int count = l - 1;
		// SOPError("input=="+number);
		for (int i = 1; i <= count; i++) {
			int param = parm;
			String s = arrystr[i];
			StringBuilder sb = new StringBuilder(s);
			int len = sb.length();
			int a = param;
			while (len > param) {
				sb.insert(param, ' ');
				param = param + a + 1;
				len++;
			}
			output = output + "-" + sb.toString();

		}
		code = code + output;
		if (!type.equals("")) {
			code = code + " (" + type + ")";
		}
		return code;

	}

	public String SplitPhoneNoSpan(String number, int parm, String type, String id) {
		String output = "";
		String[] arrystr = number.split("-");
		String code = arrystr[0];
		int l = arrystr.length;
		int count = l - 1;
		// SOPError("input=="+number);
		for (int i = 1; i <= count; i++) {
			int param = parm;
			String s = arrystr[i];
			StringBuilder sb = new StringBuilder(s);
			int len = sb.length();
			int a = param;
			while (len > param) {
				sb.insert(param, ' ');
				param = param + a + 1;
				len++;
			}
			output = output + "-" + sb.toString();

		}
		code = code + output;
		if (!type.equals("")) {
			code = "<span class='customer_info customer_" + id + "' style='display: none;'>" + code + "</span> (" + type + ")";
		}
		return code;

	}

	public void postMail(String to, String cc, String bcc, String from,
			String subject, String mess, String attach, String comp_id)
			throws MessagingException {
		try {// Get system properties
				// SOP("executed.... " + to);
			Session mailSession = null;
			Properties props = System.getProperties();
			if (AppRun().equals("1")) {
				// Setup mail server
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.ssl.enable", "false");
				props.put("mail.smtp.host", "localhost");
				props.put("mail.smtp.auth", "false");
				// props.put("mail.transport.protocol", "smtp");
				// props.put("mail.smtp.port", "25");
				// props.put("mail.smtp.host", "mycampus.local");
				// props.put("mail.smtp.auth", "true");
				//
				// mailSession = Session.getInstance(props,
				// new Authenticator() {
				//
				// public PasswordAuthentication getPasswordAuthentication() {
				// return new PasswordAuthentication("support@mycampus.local",
				// "support");
				// }
				// });
			} else {
				props.put("mail.smtp.host", "192.168.0.10");
				mailSession = Session.getInstance(props, null);

				// props.put("mail.transport.protocol", "smtp");
				// props.put("mail.smtp.port", "25");
				// props.put("mail.smtp.host", "mail.emax.in");
				// props.put("mail.smtp.auth", "true");
				// // Get session
				// mailSession = Session.getInstance(props,
				// new Authenticator() {
				// public PasswordAuthentication getPasswordAuthentication() {
				// return new PasswordAuthentication("vijay@emax.in",
				// "Vanilla");}
				// });
			}
			// SOPError("session===="+session);
			// Define message
			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from));
			// SOPError("from==="+from);

			if (!to.equals("")) {
				// message.addRecipient(Message.RecipientType.CC,new
				// InternetAddress(cc));
				ArrayList recipientsArray = new ArrayList();
				StringTokenizer st = new StringTokenizer(to, ",");
				while (st.hasMoreTokens()) {
					recipientsArray.add(st.nextToken());
				}
				int sizeTo = recipientsArray.size();
				InternetAddress[] addressTo = new InternetAddress[sizeTo];
				for (int i = 0; i < sizeTo; i++) {
					addressTo[i] = new InternetAddress(recipientsArray.get(i)
							.toString());
				}
				message.setRecipients(Message.RecipientType.TO, addressTo);
			}
			if (!cc.equals("")) {
				// message.addRecipient(Message.RecipientType.CC,new
				// InternetAddress(cc));
				ArrayList recipientsArray = new ArrayList();
				StringTokenizer st = new StringTokenizer(cc, ",");
				while (st.hasMoreTokens()) {
					recipientsArray.add(st.nextToken());
				}
				int sizeTo = recipientsArray.size();
				InternetAddress[] addressTo = new InternetAddress[sizeTo];
				for (int i = 0; i < sizeTo; i++) {
					addressTo[i] = new InternetAddress(recipientsArray.get(i)
							.toString());
				}
				message.setRecipients(Message.RecipientType.CC, addressTo);
			}

			if (!bcc.equals("")) {
				// message.addRecipient(Message.RecipientType.CC,new
				// InternetAddress(cc));
				ArrayList recipientsArray = new ArrayList();
				StringTokenizer st = new StringTokenizer(bcc, ",");
				while (st.hasMoreTokens()) {
					recipientsArray.add(st.nextToken());
				}
				int sizeTo = recipientsArray.size();
				InternetAddress[] addressTo = new InternetAddress[sizeTo];
				for (int i = 0; i < sizeTo; i++) {
					addressTo[i] = new InternetAddress(recipientsArray.get(i)
							.toString());
				}
				message.setRecipients(Message.RecipientType.BCC, addressTo);
				// message.addRecipient(Message.RecipientType.BCC,new
				// InternetAddress(bcc));
			}
			message.setSubject(subject, "UTF-8");

			// create the message part
			Multipart multipart = new MimeMultipart();
			// System.out.println("multipart===="+multipart);
			// fill message
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart.setContent(mess, "text/html; charset=UTF-8");

			if (!attach.equals("")) {
				// System.out.println("att" + attach);
				// attach = attach.replace("/", "\\");
				String[] filepath = attach.split(";");
				for (int j = 0; j < filepath.length; j++) {
					String[] filename = filepath[j].split(",");
					messageBodyPart = new MimeBodyPart();
					javax.activation.DataSource source = new FileDataSource(filename[0]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename[1]);
					multipart.addBodyPart(messageBodyPart);
				}
			}
			if (mess.contains("cid:one.png")) {
				messageBodyPart = new MimeBodyPart();
				javax.activation.DataSource fds = new FileDataSource(TemplatePath(comp_id)
						+ "one.png");
				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setDisposition(MimeBodyPart.INLINE);
				messageBodyPart.setHeader("Content-ID", "<one.png>");
				multipart.addBodyPart(messageBodyPart);
			}

			// Put parts in message
			message.setContent(multipart);
			message.setSentDate(kknow());
			// Send the message
			Transport.send(message);
			SOPInfo("AxelaAuto-" + comp_id + ": " + to);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("AxelaAuto-" + comp_id + ": " + to);
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public String GetEmps(String TriggerLevel, String followuptype,
			String emp_id, String branch_id, String comp_id) {
		CachedRowSet crs1 = null;
		StringBuilder ccEmail = new StringBuilder();
		String ccEmailStr = "";
		if (!TriggerLevel.equals("")) {
			String StrSql = "SELECT emp_email1 " + " FROM " + compdb(comp_id)
					+ "axela_emp " + " WHERE emp_active='1' " + " AND emp_"
					+ followuptype + "_level" + TriggerLevel + "='1'"
					+ " AND ((emp_all_exe=1 AND emp_branch_id=" + branch_id
					+ ") OR" + " (emp_all_exe=1 AND emp_id IN (SELECT "
					+ compdb(comp_id) + "axela_emp_branch.emp_id " + " FROM "
					+ compdb(comp_id) + "axela_emp_branch WHERE "
					+ compdb(comp_id) + "axela_emp_branch.emp_branch_id ="
					+ branch_id + ")) "
					+ " OR emp_id IN (SELECT empexe_emp_id FROM "
					+ compdb(comp_id) + "axela_emp_exe WHERE empexe_id="
					+ emp_id + ")) " + " AND emp_id <> " + emp_id + "";

			crs1 = processQuery(StrSql, 0);
			try {
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						if (isNotPublicEmail(crs1.getString("emp_email1"))
								.equals("1")) {
							ccEmail.append(crs1.getString("emp_email1")).append(
									",");
						}
					}
					if (!ccEmail.toString().equals("")) {
						ccEmailStr = ccEmail.toString();
						ccEmailStr = ccEmailStr.substring(0,
								ccEmailStr.lastIndexOf(","));
					}
				}
				crs1.close();
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
		return ccEmailStr;
	}

	public String GetGstType(String customer_id, String branch_id, String comp_id) {
		// SOP(customer_id + " ====== " + branch_id + " ====== " + comp_id);
		CachedRowSet crs = null;
		String gsttype = "";
		if (!customer_id.equals("0") && !branch_id.equals("0")) {
			String StrSql = "SELECT IF(customerstate.state_id  = dtbranch.state_id, 'state', 'central') AS Gsttype"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN ("
					+ " SELECT state_id, branch_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = branchcity.city_state_id"
					+ " GROUP BY branch_id"
					+ " ) AS dtbranch ON dtbranch.branch_id = " + branch_id
					+ " WHERE customer_id = " + customer_id;
			// SOP("StrSql==connect=" + StrSql);
			crs = processQuery(StrSql, 0);
			try {
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						gsttype = crs.getString("Gsttype");
					}
				}
				crs.close();
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return gsttype;
	}

	public String Filename(String filepath) {
		String name = "";
		filepath = filepath.replace("\\", "/");
		name = filepath.substring(filepath.lastIndexOf("/"));
		name = name.replace("/", "");
		return name;
	}

	public String fileext(String filename) {
		String exts = "";
		exts = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		return exts;
	}

	public String PaymentMode(int id) {
		String str = "InValid";
		switch (id) {
			case 1 :
				str = "Cash";
				break;
			case 2 :
				str = "Cheque/DD";
				break;
			case 3 :
				str = "Credit Card";
				break;
			case 4 :
				str = "Payment Gateway";
				break;
		}
		return str;
	}

	public String IPNo(String ipno) {
		int pos1 = ipno.indexOf(".") + 1;
		String ip1 = ipno.substring(0, pos1 - 1);
		String ip2 = ipno.substring(pos1, ipno.length()); // 168.0.2
		String ip3 = ip2.substring(ip2.indexOf(".") + 1, ip2.length());// 0.2
		String ip4 = ip3.substring(ip3.indexOf(".") + 1, ip3.length()); // 2

		ip2 = ip2.substring(0, ip2.indexOf(".")); // 168
		ip3 = ip3.substring(0, ip3.indexOf("."));

		int ipTot = (Integer.parseInt(ip1) * (256 * 256 * 256))
				+ (Integer.parseInt(ip2) * (256 * 256))
				+ (Integer.parseInt(ip3) * (256)) + Integer.parseInt(ip4);
		return ipTot + "";
	}

	public static String GenOTP(int size) {
		String key = "", possible = "0123";
		for (int i = 0; i < size; i++) {
			key += possible.charAt((int) Math.floor(Math.random()
					* possible.length()));
		}
		return key;
	}

	public static String GenPass(int size) {
		StringBuilder s = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			s.append(RandomValue());
		}
		return s.toString();
	}

	public static String RandomValue() {
		Random ran = new Random();
		int no = ran.nextInt(62) + 1;

		switch (no) {
			case 1 :
				return "a";
			case 2 :
				return "b";
			case 3 :
				return "c";
			case 4 :
				return "d";
			case 5 :
				return "e";
			case 6 :
				return "f";
			case 7 :
				return "g";
			case 8 :
				return "h";
			case 9 :
				return "i";
			case 10 :
				return "j";
			case 11 :
				return "k";
			case 12 :
				return "l";
			case 13 :
				return "m";
			case 14 :
				return "n";
			case 15 :
				return "o";
			case 16 :
				return "p";
			case 17 :
				return "q";
			case 18 :
				return "r";
			case 19 :
				return "s";
			case 20 :
				return "t";
			case 21 :
				return "u";
			case 22 :
				return "v";
			case 23 :
				return "w";
			case 24 :
				return "x";
			case 25 :
				return "y";
			case 26 :
				return "z";
			case 27 :
				return "A";
			case 28 :
				return "B";
			case 29 :
				return "C";
			case 30 :
				return "D";
			case 31 :
				return "E";
			case 32 :
				return "F";
			case 33 :
				return "G";
			case 34 :
				return "H";
			case 35 :
				return "I";
			case 36 :
				return "J";
			case 37 :
				return "K";
			case 38 :
				return "L";
			case 39 :
				return "M";
			case 40 :
				return "N";
			case 41 :
				return "O";
			case 42 :
				return "P";
			case 43 :
				return "Q";
			case 44 :
				return "R";
			case 45 :
				return "S";
			case 46 :
				return "T";
			case 47 :
				return "U";
			case 48 :
				return "V";
			case 49 :
				return "W";
			case 50 :
				return "X";
			case 51 :
				return "Y";
			case 52 :
				return "Z";
			case 53 :
				return "0";
			case 54 :
				return "1";
			case 55 :
				return "2";
			case 56 :
				return "3";
			case 57 :
				return "4";
			case 58 :
				return "5";
			case 59 :
				return "6";
			case 60 :
				return "7";
			case 61 :
				return "8";
			case 62 :
				return "9";
			default :
				return "....";
		}
	}

	public static String CalTaxPercentage(String Fee, String Tax) {
		DecimalFormat deci = new DecimalFormat("#.##");
		double taxpercent = Double.parseDouble(Tax) / Double.parseDouble(Fee) * 100;
		return deci.format(taxpercent);
	}

	public static String CalServiceTax(String Fee, String Tax) {
		// Given Fee=100; Tax=12.36%
		// Returns 12.36
		DecimalFormat deci = new DecimalFormat("#.##");
		double taxcomp = Double.parseDouble(Fee) * Double.parseDouble(Tax)
				/ 100;
		return deci.format(taxcomp);
	}

	public static String CalGSTTax(String Fee, String Tax1, String Tax2, String Tax3) {
		// Given Fee=100; Tax=12.36%
		// Returns 12.36
		DecimalFormat deci = new DecimalFormat("#.##");
		double taxcomp = Double.parseDouble(Fee) * Double.parseDouble(Tax1) / 100 + Double.parseDouble(Fee) * Double.parseDouble(Tax2) / 100 + Double.parseDouble(Fee) * Double.parseDouble(Tax3) / 100;
		return deci.format(taxcomp);
	}

	public static String CalDiscountamount(String Fee, String Tax) {
		// Given Fee=5000; Tax=12.36%
		// Returns 4450
		DecimalFormat deci = new DecimalFormat("#.##");
		double DiscountedTax = Double.parseDouble(Fee)
				* Double.parseDouble(Tax) / 100; // DiscountedTax=618
		double DiscountedFee = Double.parseDouble(Fee) + DiscountedTax; // DiscountedFee=5618
		double Taxout = (Double.parseDouble(Fee) * DiscountedTax)
				/ DiscountedFee; // Taxout=550
		return deci.format(Double.parseDouble(Fee) - Taxout);
	}

	public static String CalPrincipleServiceTax(String Fee, String Tax) {
		// Given Fee=100; Tax=12.36%
		// Returns 123.6
		DecimalFormat deci = new DecimalFormat("#.##");
		double taxcomp = Double.parseDouble(Fee) * Double.parseDouble(Tax)
				/ 100;
		double PrincipleFee = Double.parseDouble(Fee) + taxcomp;
		return deci.format(PrincipleFee);

	}

	public static String CalPrincipleComp(String Fee, String Tax) {
		// Given Fee=5000; Tax=12.36%
		// Returns 4450
		DecimalFormat deci = new DecimalFormat("#.##");
		double DiscountedTax = Double.parseDouble(Fee)
				* Double.parseDouble(Tax) / 100; // DiscountedTax=618
		double DiscountedFee = Double.parseDouble(Fee) + DiscountedTax; // DiscountedFee=5618
		double Taxout = (Double.parseDouble(Fee) * DiscountedTax)
				/ DiscountedFee; // Taxout=550
		return deci.format(Double.parseDouble(Fee) - Taxout);
	}

	public static String CalServiceTaxComp(String Fee, String Tax) {
		// Given Fee=5000; Tax=10%
		// Returns 500
		if (Fee.equals("") || Fee.equals("0")) {
			return "0";
		}
		DecimalFormat deci = new DecimalFormat("#.##");
		double DiscountedTax = Double.parseDouble(Fee)
				* Double.parseDouble(Tax) / 100; // DiscountedTax=500
		double DiscountedFee = Double.parseDouble(Fee) + DiscountedTax; // DiscountedFee=5500
		double Taxout = (Double.parseDouble(Fee) * DiscountedTax)
				/ DiscountedFee; // Taxout=500
		return deci.format(Taxout);
	}

	public String CalculateTax(String Fee, String Tax)// CalculateTax
	{
		// Given Fee=5000; Tax=12.36%
		String DiscountedTax = Double
				.toString((Double.parseDouble(Fee) * Double.parseDouble(Tax)) / 100);// DiscountedTax=618
		return DiscountedTax;
	}

	public String PopulateTotalAmount(String Fee, String Tax)// PopulateTotalAmount
	{
		// Given Fee=5000; Tax=12.36% ; Courseware=1500
		double DiscountedTax1 = (Double.parseDouble(Fee) * Double
				.parseDouble(Tax)) / 100;// DiscountedTax=618
		double TotalAmount = Math
				.ceil(Double.parseDouble(Fee) + DiscountedTax1);// TotalAmount=7118
		String TotalAmount1 = Double.toString(TotalAmount);
		return TotalAmount1;
	}

	public String PopulateTotal(String Fee, String Tax)// PopulateTotalAmount
	{
		double TotalAmount = Math.ceil(Double.parseDouble(Fee)
				+ Double.parseDouble(Tax));// TotalAmount=7118
		String TotalAmount1 = Double.toString(TotalAmount);
		return TotalAmount1;
	}

	public int FileSize(File file) throws IOException {
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		try {
			InputStream is = new FileInputStream(file);
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			is.close();
		} catch (Exception e) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + e);
		}
		return offset;
	}

	// //////////file size to MB,GB.....///////////////
	public String ConvertFileSizeToBytes(int size) throws IOException {
		String str = "";
		if (size < 1024) {
			str = (size) + " Bytes";
		}
		if (size > 1024 && size < 1048576) {
			str = (size / 1024) + " KB";
		}
		if (size > 1048576 && size < 1073741824) {
			str = (size / 1048576) + " MB";
		}
		if (size > 1073741824) {
			str = (size / 1073741824) + " GB";
		}
		return str;
	}

	// Indian Rupee Format

	public String IndFormat(String val) {

		String decimal = ".00";
		String negative = "";
		if (val.contains("-")) {
			val = val.replace("-", "");
			negative = "-";
		}
		if (val.contains(".")) {
			decimal = val.substring(val.indexOf("."), val.length());
		} else {
			decimal = ".00";
			val += ".00";
		}
		String val1 = "0";
		StringBuilder sb = new StringBuilder("0");
		if (Double.parseDouble(val) != 0) {
			val1 = val.substring(0, (val.length() - (val.substring(val.indexOf("."), val.length()).length())));
			sb = new StringBuilder(val1 + "");
			int len = sb.length();
			if (len < 4) {
				return negative + sb.toString() + decimal;
			}
			int cp = len - 3;
			sb.insert(cp, ',');
			if (len < 6) {
				return negative + sb.toString() + decimal;
			}
			while (cp > 2) {
				cp -= 2;
				sb.insert(cp, ',');
			}
		}
		return negative + sb.toString() + decimal;
	}
	public String PopulateBranch(String branch_id, String param, String branch_type, HttpServletRequest request) {
		String BranchAccess = "";
		StringBuilder stringval = new StringBuilder();
		HttpSession session = request.getSession(true);
		BranchAccess = GetSession("BranchAccess", request);
		String comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 ";
			if (!branch_type.equals("")) {
				SqlStr += "	AND branch_branchtype_id IN (" + branch_type + ")";
			}
			SqlStr += BranchAccess + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			if (param.equals("")) {
				stringval.append("<option value =0>Select Branch</option>");
			} else if (param.equals("all")) {
				stringval.append("<option value =0>All Branches</option>");
			} else {
				stringval.append("<option value =0>").append(param)
						.append("</option>");
			}
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"),
						branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code"))
						.append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	public String PopulateBranch(String branch_id, String param, String branch_type, String brand_id, HttpServletRequest request) {
		String BranchAccess = "";
		StringBuilder stringval = new StringBuilder();
		HttpSession session = request.getSession(true);
		BranchAccess = GetSession("BranchAccess", request);
		String comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1";
			if (!branch_type.equals("")) {
				SqlStr += "	AND branch_branchtype_id IN (" + branch_type + ")";
			}
			if (!brand_id.equals("")) {
				SqlStr += "	AND branch_brand_id IN (" + brand_id + ")";
			}
			SqlStr += BranchAccess
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr======" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			if (param.equals("")) {
				stringval.append("<option value =0>Select Branch</option>");
			} else if (param.equals("all")) {
				stringval.append("<option value =0>All Branches</option>");
			} else {
				stringval.append("<option value =0>").append(param).append("</option>");
			}
			while (crs.next()) {
				stringval.append("<option value='").append(crs.getString("branch_id")).append("' ");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public boolean CheckActTime(Date starttime, String comp_id) {
		ConnectDate cd = new ConnectDate();
		String comp_actleadtime = "";
		comp_actleadtime = ExecuteQuery("Select comp_actleadtime from "
				+ compdb(comp_id) + "axela_comp where comp_id=" + comp_id);
		double BufferTime = Double.parseDouble(comp_actleadtime);
		Date acttime = cd.AddHoursDate(kknow(), 0, BufferTime, 0);
		// SOPError("acttime====" + acttime);
		if (starttime.after(acttime)) {
			return true;
		} else {
			return false;
		}
	}

	public String isHoliday(String comp_id, String batch_branch_id, String date) {
		// date must be in yyyyMMddHHmmss format
		ConnectDate cd = new ConnectDate();
		String result = "";
		String StrSql = "";
		try {
			StrSql = "select holi_name, holitype_name from "
					+ compdb(comp_id)
					+ "axela_holi "
					+ " inner join axela_holi_type on holitype_id = holi_holitype_id "
					+ " where holi_date='"
					+ cd.ToShortDate(cd.StringToDate(date)) + "' "
					+ " and (holi_branch_id=0 or holi_branch_id="
					+ batch_branch_id + ")";
			// SOPError("StrSql--"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					result = result + crs.getString("holi_name") + " ("
							+ crs.getString("holitype_name") + ")<br>";
				}
			}
			// SOPError("result--"+result+" for "+date);
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);

		}
		return result;
	}

	static String[] b = {" hundred", " thousand", " lakh", " crore"};
	static String[] a = {"", " one", " two", " three", " four", " five",
			" six", " seven", " eight", " nine"};
	static String[] c = {" ten", " eleven", " twelve", " thirteen",
			" fourteen", " fifteen", " sixteen", " seventeen", " eighteen",
			" nineteen"};
	static String[] d = {" twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety"};
	String str = "";

	public String getPercentage(long numerator, long denominator) {
		DecimalFormat deci = new DecimalFormat("#.##");
		String str = "0";
		if (denominator != 0 && numerator > 0) {
			str = deci.format((numerator * 100) / denominator);
		}
		return str;
	}

	public String getPercentage(int numerator, int denominator) {
		DecimalFormat deci = new DecimalFormat("#.##");
		String str = "0";
		if (denominator != 0 && numerator > 0) {
			str = deci.format((numerator * 100) / denominator);
		}
		return str;
	}

	// public String getPercentage(double numerator, double denominator) {
	// DecimalFormat deci = new DecimalFormat("#.##");
	// String str = "0";
	// if (denominator != 0 && numerator > 0) {
	// str = deci.format((numerator * 100) / denominator);
	// }
	// return str;
	// }
	// public String getPercentage(Double total, Double scored) {
	// DecimalFormat deci = new DecimalFormat("#.##");
	// String str = "0";
	// if (total != 0 && scored > 0) {
	// str = deci.format((scored * 100) / total);
	// }
	// return str;
	// }
	// public String getPercentage(double total, double scored) {
	// SOPError("scored = ");
	// DecimalFormat deci = new DecimalFormat("#.##");
	// String str = "0";
	// if (total != 0 && scored > 0) {
	// str = deci.format((scored * 100) / total);
	// }
	// return str;
	// }
	public String getPercentage(double numerator, double denominator) {
		// SOPError("denominator = "+denominator+"numerator===="+numerator);
		DecimalFormat deci = new DecimalFormat("#.##");
		String str = "0";
		if (denominator != 0 && numerator > 0) {
			str = deci.format((numerator * 100) / denominator);
		}
		return str;
	}

	public String NumberToWordFormat(int num) {
		str = "";
		// SOPError("the number passed is " + num);
		if (num == 0) {
			// SOPError("zero");
			return "zero";
		}
		int c = 1;
		int rm;
		while (num != 0) {
			switch (c) {
				case 1 :
					rm = num % 100;
					pass(rm);
					if (num > 100 && num % 100 != 0) {
						display(" and");
					}
					num /= 100;
					break;
				case 2 :
					rm = num % 10;
					if (rm != 0) {
						display("");
						display(b[0]);
						display("");
						pass(rm);
					}
					num /= 10;
					break;
				case 3 :
					rm = num % 100;
					if (rm != 0) {
						display("");
						display(b[1]);
						display("");
						pass(rm);
					}
					num /= 100;
					break;
				case 4 :
					rm = num % 100;
					if (rm != 0) {
						display("");
						display(b[2]);
						display("");
						pass(rm);
					}
					num /= 100;
					break;
				case 5 :
					rm = num % 100;
					if (rm != 0) {
						display("");
						display(b[3]);
						display("");
						pass(rm);
					}
					num /= 100;
					break;
			}// end switch
			c++;
		}// end while
			// SOPError(str);
		return str;
	}// end method

	public void pass(int num) {
		int rm, q;
		if (num < 10) {
			display(a[num]);
		} else if (num > 9 && num < 20) {
			display(c[num - 10]);
		} else if (num > 19) {
			rm = num % 10;
			if (rm == 0) {
				q = num / 10;
				display(d[q - 2]);
			} else {
				q = num / 10;
				display(a[rm]);
				display("");
				display(d[q - 2]);
			}
		}
	}

	public void display(String s) {
		String t;
		t = str;
		str = s;
		str += t;
	}

	public String TextDate(int id) {
		switch (id) {
			case 01 :
				return "First";

			case 02 :
				return "Second";

			case 03 :
				return "Third";

			case 04 :
				return "Fourth";

			case 05 :
				return "Fifth";

			case 06 :
				return "Sixth";

			case 07 :
				return "Seventh";

			case 8 :
				return "Eighth";

			case 9 :
				return "Nineth";

			case 10 :
				return "Tenth";

			case 11 :
				return "Eleventh";

			case 12 :
				return "Twelveth";

			case 13 :
				return "Thirteenth";

			case 14 :
				return "Fourteenth";

			case 15 :
				return "Fifteenth";

			case 16 :
				return "Sixteenth";

			case 17 :
				return "Seventeenth";

			case 18 :
				return "Eighteenth";

			case 19 :
				return "Ninteenth";

			case 20 :
				return "Twentyth";

			case 21 :
				return "Twenty First";

			case 22 :
				return "Twenty Second";

			case 23 :
				return "Twenty Third";

			case 24 :
				return "Twenty Fourth";

			case 25 :
				return "Twenty Fifth";

			case 26 :
				return "Twenty Sixth";

			case 27 :
				return "Twenty Seventh";

			case 28 :
				return "Twenty Eighth";

			case 29 :
				return "Twenty Nineth";

			case 30 :
				return "Thirtyth";

			case 31 :
				return "Thirty First";

			default :
				return null;
		}
	}

	public String Dob_To_Word(String date) {
		// String date="30/01/1998";
		String[] dates = date.split("/");
		String day = dates[0];
		String month = dates[1];
		String year = dates[2];
		// Dob_To_Word num=new Dob_To_Word();
		return TextDate(Integer.parseInt(day)) + " "
				+ TextMonth(Integer.parseInt(month) - 1) + ", "
				+ NumberToWordFormat(Integer.parseInt(year));

	}

	public int ReturnSmsCredit(int length) {
		// SOPError("lenght: " + length);
		int count = 0, inc = 307;
		for (count = 3; length >= 0; count++) {
			if (length <= 160) {
				return 1;
			}
			if (length >= 161 && length <= 306) {
				return 2;
			}
			if (length >= inc && length <= inc + 152) {
				return count;
			}
			inc = inc + 153;
		}
		return count;
	}

	public String cal_date(String startdate, String enddate) {
		String start[] = startdate.split("/");
		int d1 = Integer.parseInt(start[0]);
		int m1 = Integer.parseInt(start[1]);
		int y1 = Integer.parseInt(start[2]);

		String end[] = enddate.split("/");
		int d2 = Integer.parseInt(end[0]);
		int m2 = Integer.parseInt(end[1]);
		int y2 = Integer.parseInt(end[2]);

		int day_odd[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int d = d2 - d1, mon = m2 - 2;
		if ((m2 - 2) < 0) {
			mon = 11;
		}
		int m = m2 - m1, y = y2 - y1;
		String str = "";

		if (y2 < y1) {
			str = "Invalid date";
		} else if (y == 0) {
			if (m < 0 || (m == 0 && d < 0)) {
				str = "Invalid date";
			}
		}
		if (str.equals("")) {
			if (y2 % 4 == 0) {
				day_odd[1] = 29;
			}
			if (m > 0) {
				if (d < 0) {
					m--;
					d = day_odd[mon] - d1 + d2;
				}
			}
			if (m == 0) {
				if (d < 0 && y > 0) {
					m = 11;
					y--;
					d = day_odd[mon] - d1 + d2;
				}
			}
			if (m < 0 && y > 0) {
				m = 12 - (m1 - m2);
				y--;
				if (d < 0) {
					m--;
					d = day_odd[mon] - d1 + d2;
				} else if (d > 0) {
					d = d2 - d1;
				}
			}
			if (y != 0) {
				if (y == 1) {
					str = str + y + " Year ";

				} else {
					str = y + " Years ";
				}
			}
			if (m != 0) {
				if (m == 1) {
					str = str + m + " Month ";
				} else {
					str = str + m + " Months ";
				}
			}
			if (d != 0) {
				if (d == 1) {
					str = str + d + " Day";
				} else {
					str = str + d + " Days";
				}
			}
			if (y == 0 && m == 0 && d == 0) {
				str = "0 Days";
			}
		}
		return str;
	}

	public String unescapehtml(String str) {
		return StringEscapeUtils.unescapeHtml4(str);
	}

	public boolean isUniqueCompUrl(String comp_id, String comp_url_portal,
			String id) {
		String StrSql = "";
		boolean isUnique = true;
		try {
			StrSql = "select comp_id FROM " + compdb(comp_id)
					+ "axela_comp where 1=1";
			if (!id.equals("")) {
				StrSql = StrSql + " and comp_id != " + id;
			}
			StrSql = StrSql + " and (comp_url_portal = '" + comp_url_portal
					+ "' or  comp_url_mobile = '" + comp_url_portal + "') ";
			// SOPError("StrSql----"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				isUnique = false;
			}
			return isUnique;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return false;
		}
	}

	public String ReturnDurType(int id) {
		String str = "InValid";
		switch (id) {
			case 1 :
				str = "Day";
				break;
			case 2 :
				str = "Week";
				break;
			case 3 :
				str = "Month";
				break;
			case 4 :
				str = "Year";
				break;
		}
		return str;
	}

	public String PopulateCategoryPop(String id, String comp_id, String cat_id, String active, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "", BranchAccess = "";
		BranchAccess = GetSession("BranchAccess", request);
		try {
			StrSql = "SELECT cat_id, cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_cat_id = cat_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1";

			if (!active.equals("")) {
				StrSql = StrSql + " AND cat_active = " + active + "";
			}
			if (!cat_id.equals("")) {
				StrSql = StrSql + " AND cat_id != " + cat_id + "";
			}

			StrSql += BranchAccess + " GROUP BY cat_id" + " ORDER BY cat_rank, cat_id";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), id));
				Str.append(">").append(crs.getString("cat_name"));
				Str.append(":</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}
	public String IndDecimalFormat(String val) {
		String deci = "";
		String negative = "";
		if (val.contains("-")) {
			val = val.replace("-", "");
			negative = "-";
		}
		if (val.contains(".")) {
			deci = val.substring(val.indexOf("."));
		}
		// SOPError("deci---"+deci);
		double d = Double.parseDouble(val);
		d = Math.floor(d);
		String val1 = Math.round(d) + "";
		StringBuilder sb = new StringBuilder(val1 + "");
		int len = sb.length();
		if (len < 4) {
			return negative + sb.toString() + deci;
		}
		int cp = len - 3;
		sb.insert(cp, ',');
		if (len < 6) {
			return negative + sb.toString() + deci;
		}

		while (cp > 2) {
			cp -= 2;
			sb.insert(cp, ',');
		}
		return negative + sb.toString() + deci;

	}

	public void VoucherCurrBalance(String customer_id,
			HttpServletRequest request) {
		String StrSql = "", query = "";
		HttpSession session = request.getSession(true);
		String comp_id = PadQuotes((String) session.getAttribute("comp_id"));
		if (customer_id.equals("0")) {
			query = "";
		} else {
			query = " where customer_id = " + customer_id + "";
		}

		StrSql = " Update "
				+ compdb(comp_id)
				+ "axela_customer"
				+ " set customer_curr_bal = (SELECT coalesce(sum(receipt_amt),0)"
				+ " FROM "
				+ compdb(comp_id)
				+ "axela_acc_receipt"
				+ " where receipt_customer_id=customer_id"
				+ " and receipt_active = '1')"
				+ " - "
				+ " (SELECT coalesce(sum(voucher_amt),0)"
				+ " from "
				+ compdb(comp_id)
				+ "axela_acc_voucher"
				+ " where voucher_customer_id=customer_id and voucher_active = '1')"
				+ "" + query + "";
		updateQuery(StrSql);
	}

	public void EnquiryPriorityUpdate(String comp_id, String enquiry_id) {
		try {
			String met_count = "0", priorityenquiry_metcount = "0";
			String testdrive_count = "0", priorityenquiry_testdrivecount = "0";
			String quote_count = "0", priorityenquiry_quotecount = "0";
			String homevisit_count = "0", priorityenquiry_homevisitcount = "0";
			String closedays_count = "0", priorityenquiry_closedays = "0";
			String priorityenquiry_optioncount = "0";
			int option_count = 0;
			String priorityenquiry_id = "0";
			String StrSql = "";
			String enquiry_status_id = "0";

			StrSql = "SELECT COALESCE(met_count, '0') AS met_count,"
					+ " COALESCE(testdrive_count, '0') AS testdrive_count,"
					+ " COALESCE(quote_count, '0') AS quote_count,"
					+ " COALESCE(homevisit_count, '0') AS homevisit_count,"
					+ " COALESCE(DATEDIFF(SUBSTR('" + ToLongDate(kknow()) + "', 1, 8), SUBSTR(enquiry_date, 1, 8)), '0') AS closedays_count,"
					+ " enquiry_status_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN (SELECT COUNT(DISTINCT followup_id) AS met_count, followup_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " WHERE followup_enquiry_id = " + enquiry_id
					+ " AND followup_feedbacktype_id = 5"
					+ " GROUP BY followup_enquiry_id) AS tblfollowup ON tblfollowup.followup_enquiry_id = enquiry_id"
					+ " LEFT JOIN (SELECT COUNT(DISTINCT testdrive_id) AS testdrive_count, testdrive_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " WHERE testdrive_enquiry_id = " + enquiry_id
					+ " AND testdrive_fb_taken = 1 " // only iff it is taken then it should be count
					+ " GROUP BY testdrive_enquiry_id) AS tbltestdrive ON tbltestdrive.testdrive_enquiry_id = enquiry_id"
					+ " LEFT JOIN (SELECT COUNT(DISTINCT quote_id) AS quote_count, quote_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " WHERE quote_enquiry_id = " + enquiry_id
					+ " GROUP BY quote_enquiry_id ) AS tblquote ON tblquote.quote_enquiry_id = enquiry_id"
					+ " LEFT JOIN (SELECT COUNT(DISTINCT followup_id) AS homevisit_count, followup_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " WHERE followup_enquiry_id = " + enquiry_id
					+ " AND followup_feedbacktype_id = 9"
					+ " GROUP BY followup_enquiry_id) AS tblhomevisit ON tblhomevisit.followup_enquiry_id = enquiry_id"
					+ " WHERE 1 = 1"
					+ " AND enquiry_id = " + enquiry_id
					+ " GROUP BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					met_count = crs.getString("met_count");
					testdrive_count = crs.getString("testdrive_count");
					quote_count = crs.getString("quote_count");
					homevisit_count = crs.getString("homevisit_count");
					closedays_count = crs.getString("closedays_count");
					enquiry_status_id = crs.getString("enquiry_status_id");
				}
			}
			crs.close();
			//
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_metcount, priorityenquiry_quotecount,"
					+ " priorityenquiry_testdrivecount, priorityenquiry_homevisitcount,"
					+ " priorityenquiry_closedays, priorityenquiry_optioncount"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " ORDER BY priorityenquiry_id ASC";
			// /SOP("StrSql==000===" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					option_count = 0;
					priorityenquiry_id = crs.getString("priorityenquiry_id");
					priorityenquiry_metcount = crs.getString("priorityenquiry_metcount");
					priorityenquiry_testdrivecount = crs.getString("priorityenquiry_testdrivecount");
					priorityenquiry_quotecount = crs.getString("priorityenquiry_quotecount");
					priorityenquiry_homevisitcount = crs.getString("priorityenquiry_homevisitcount");
					priorityenquiry_closedays = crs.getString("priorityenquiry_closedays");
					priorityenquiry_optioncount = crs.getString("priorityenquiry_optioncount");

					if ((Integer.parseInt(met_count) >= Integer.parseInt(priorityenquiry_metcount)) && !met_count.equals("0")) {
						++option_count;
					}
					if ((Integer.parseInt(testdrive_count) >= Integer.parseInt(priorityenquiry_testdrivecount)) && !testdrive_count.equals("0")) {
						++option_count;
					}
					if ((Integer.parseInt(quote_count) >= Integer.parseInt(priorityenquiry_quotecount)) && !quote_count.equals("0")) {
						++option_count;
					}
					if ((Integer.parseInt(homevisit_count) >= Integer.parseInt(priorityenquiry_homevisitcount)) && !homevisit_count.equals("0")) {
						++option_count;
					}
					if ((Integer.parseInt(closedays_count) <= Integer.parseInt(priorityenquiry_closedays)) && !closedays_count.equals("0")) {
						++option_count;
					}
					// Business logic
					if ((option_count != 0) && (option_count >= Integer.parseInt(priorityenquiry_optioncount)) && enquiry_status_id.equals("1")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_priorityenquiry_id = " + priorityenquiry_id
								+ " WHERE enquiry_id = " + enquiry_id;
						updateQuery(StrSql);
						break;
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_priorityenquiry_id = 3"
								+ " WHERE enquiry_id = " + enquiry_id;
						updateQuery(StrSql);
						// break;
					}
				}
			}
			crs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String RetrunSelectArrVal(HttpServletRequest request, String name) {
		String arr = "";
		String[] strArr = request.getParameterValues(name);
		// SOPError("strArr==" + Arrays.toString(strArr) + " name==" +
		// name);
		try {
			if (strArr != null && strArr.length > 0) {
				for (int i = 0; i < strArr.length; i++) {
					arr = arr + strArr[i] + ",";
				}
				arr = arr.substring(0, arr.length() - 1);
			}
			return arr;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String CleanArrVal(String arr) {
		if (!arr.equals("")) {
			arr = arr.substring(0, arr.length() - 1);
		}
		return arr;
	}

	public String PopulateExportFormat(String exporttype) {
		String export = "";
		if (exporttype.equals("")) {
			exporttype = "xlsx";
		}
		if (exporttype.equals("PDF")) {
			export = export + "<option value = pdf"
					+ StrSelectdrop("pdf", exporttype) + ">PDF</option>\n";
		} else {
			export = export + "<option value = xlsx"
					+ StrSelectdrop("xlsx", exporttype)
					+ ">MS Excel</option>\n";
			// export = export + "<option value = xls" + StrSelectdrop("xls",
			// exporttype) + ">MS Excel(XLS)</option>\n";
			// export = export + "<option value = pdf"
			// + StrSelectdrop("pdf", exporttype) + ">PDF</option>\n";
			// export = export + "<option value = html"
			// + StrSelectdrop("html", exporttype) + ">HTML</option>\n";
			// export = export + "<option value = csv"
			// + StrSelectdrop("csv", exporttype) + ">CSV</option>\n";
		}
		return export;
	}

	public String PopulatePrintFormat(String printtype) {
		String export = "";
		// SOP("exporttype==" + printtype);
		if (printtype.equals("")) {
			printtype = "Pdf";
		}
		if (printtype.equals("Pdf")) {
			export = export + "<option value = Pdf" + StrSelectdrop("Pdf", printtype) + ">Pdf</option>\n";
		}
		return export;
	}

	public int CheckCurrentId(int d) {
		int n = d, sum = 0;
		while (d > 0) {
			int p = d % 10;
			sum += p;
			d = d / 10;
			if ((sum > 9) && (d == 0)) {
				d = sum;
				sum = 0;
			}
		}
		if ((sum == 8) || (sum == 4)) {
			n = (n + 1);
		}
		return n;
	}

	public String PopulateCategory(String p_id, String prodcat_parent_id,
			String prodcat_id, String active, String comp_id) {
		String Str = "", StrSql = "", catname = "";
		try {
			StrSql = "SELECT prodcat_id, prodcat_name, prodcat_parent_id FROM axela_prod_cat  "
					+ "where prodcat_parent_id="
					+ p_id
					+ " and prodcat_id != "
					+ prodcat_id;

			if (!active.equals("")) {
				StrSql = StrSql + " and prodcat_active='" + active + "'";
			}
			StrSql = StrSql + " order by prodcat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (crs.getString("prodcat_parent_id").equals("0")) {
					catname = crs.getString("prodcat_name");
				} else {
					catname = PopulateCategoryStr(crs.getString("prodcat_id"),
							comp_id);
				}
				p_id = crs.getString("prodcat_id");
				Str = Str
						+ "<option value="
						+ crs.getString("prodcat_id")
						+ StrSelectdrop(crs.getString("prodcat_id"),
								prodcat_parent_id) + ">" + catname
						+ ":</option>\n";
				Str = Str
						+ PopulateCategory(p_id, prodcat_parent_id, prodcat_id,
								active, comp_id);
			}
			crs.close();
			return Str;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);

			return "";
		}
	}

	public String PopulateCategoryStr(String prodcat_id, String comp_id) {
		String StrSql = "", prodcat_name = "", parent_id = "0", sql = "";
		try {
			if (!prodcat_id.equals("") && !prodcat_id.equals("0")) {
				StrSql = "SELECT cat_id, cat_name, cat_parent_id" + " FROM "
						+ compdb(comp_id) + "axela_inventory_cat"
						+ " where cat_id = " + prodcat_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					prodcat_name = prodcat_name + crs.getString("cat_name");
					parent_id = crs.getString("cat_parent_id");

					if (!parent_id.equals("0")) {
						while (!parent_id.equals("0")) {
							sql = "SELECT cat_id, cat_name, cat_parent_id"
									+ " FROM " + compdb(comp_id)
									+ "axela_inventory_cat"
									+ " where cat_id = " + parent_id;
							CachedRowSet crs1 = processQuery(sql, 0);
							while (crs1.next()) {
								prodcat_name = crs1.getString("cat_name")
										+ " > " + prodcat_name;
								parent_id = crs1.getString("cat_parent_id");
							}
							crs1.close();
						}
					}
				}
				crs.close();
			}
			return prodcat_name;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);

			return "";
		}
	}

	public void UpdateCurrentValue(String asset_id, String comp_id) {
		String StrSql = "";
		try {
			StrSql = "UPDATE "
					+ compdb(comp_id)
					+ "axela_asset "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_asset_valuation on value_asset_id = asset_id "
					+ " and value_date = (select max(value_date) as value_date "
					+ " FROM " + compdb(comp_id) + "axela_asset_valuation "
					+ " where 1=1 and value_asset_id = asset_id ) " + " SET "
					+ " asset_curr_value = COALESCE(value_amt, asset_value) "
					+ " where 1=1 ";
			if (!asset_id.equals("0")) {
				StrSql += " and asset_id = " + asset_id + " ";
			}
			// SOPError("StrSql--" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public static boolean IsValidCode(String code) {
		boolean isvalid = true;
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(code);
		if (m.find() || code.contains(" ")) {
			isvalid = false;
		}
		return isvalid;
	}

	public void CustomerCurrBalance(String customer_id, String comp_id) {
		String StrSql = "", query = "";
		if (customer_id.equals("0")) {
			query = "";
		} else {
			query = " where customer_id = " + customer_id + "";
		}

		StrSql = " Update "
				+ compdb(comp_id)
				+ "axela_customer"
				+ " SET"
				+ " customer_curr_bal = (SELECT coalesce(sum(receipt_amount),0)"
				+ " from " + compdb(comp_id) + "axela_invoice_receipt"
				+ " where receipt_active = 1"
				+ " and receipt_customer_id = customer_id)" + " - "
				+ " (SELECT coalesce(sum(invoice_grandtotal),0)" + " from "
				+ compdb(comp_id) + "axela_invoice"
				+ " where invoice_active = 1"
				+ " and invoice_customer_id = customer_id)" + " - "
				+ " (SELECT coalesce(sum(payment_amount),0)" + " from "
				+ compdb(comp_id) + "axela_invoice_payment"
				+ " where payment_active = 1"
				+ " and payment_customer_id = customer_id)" + "" + query + "";
		updateQuery(StrSql);
	}

	public String GridLink(String label, String field, String ordernavi, String ordertype) {
		StringBuilder Str = new StringBuilder();
		Str.append("<a href=").append(ordernavi).append("&orderby=")
				.append(field).append("&ordertype=").append(ordertype)
				.append(">").append(label).append("</a>");
		return Str.toString();
	}

	public String GetTheme(HttpServletRequest request) {
		String theme = "1";
		Cookie cookie = null;
		Cookie[] cookies = null;
		cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if ((cookie.getName()).compareTo("axelatheme") == 0) {
					theme = cookie.getValue();
					// SOPError("Name : " + cookie.getName() + ",  ");
					// SOPError("Value: " + cookie.getValue() +
					// " <br/>");
				}
			}
		}
		// SOPError("theme : " +theme);
		return theme;
	}

	public String AccessDenied() {
		String msg = "../portal/error.jsp?msg=Access denied. Please contact system administrator!";
		return msg;
	}

	/*
	 * Added on 11th March2013
	 */
	// ******************** Due time calculation *********************
	public String DueTime(String report_time, String duehours,
			double starttime, double endtime, String Sun, String Mon,
			String Tue, String Wed, String Thu, String Fri, String Sat,
			ArrayList public_holidate) {

		Date mydate = null;
		int duehrs_in_mins = 0;
		int days_toadd = 0;
		int mins_toadd = 0;
		// SOPError("public_holidate's = " + public_holidate);
		// SOPError("report_time = " + report_time);
		// SOPError("duehours = " + duehours);
		// ********************************************************************************************************************
		// converting duehrs to minutes
		if (duehours.contains(":")) {
			duehrs_in_mins = GetMinsFromTime(duehours);
		} else {
			// SOP("duehours in connect===" + Integer.parseInt(duehours));
			// SOP("coming in connect...");
			int duehours1 = (int) Double.parseDouble(duehours);
			// SOP("iiiiiiiiii+++++" + i);
			duehrs_in_mins = duehours1 * 60;
			// SOP("coming in connect...1");
		}
		// SOPError("duehrs_in_mins = " + duehrs_in_mins);
		mins_toadd = duehrs_in_mins;

		// ********************************************************************************************************************
		// calculating login time

		String str_starttime = Double.toString(starttime);

		String split_start_time[] = str_starttime.split("\\.");

		split_start_time[0] = doublenum(Integer.parseInt(split_start_time[0]));
		if (split_start_time[1].length() < 2) {
			split_start_time[1] = split_start_time[1] + "0";
		}

		// calculating logoff time
		String str_endtime = Double.toString(endtime);
		String split_end_time[] = str_endtime.split("\\.");

		split_end_time[0] = doublenum(Integer.parseInt(split_end_time[0]));
		if (split_end_time[1].length() < 2) {
			split_end_time[1] = split_end_time[1] + "0";
		}

		// ********************************************************************************************************************
		Date added_date = StringToDate(report_time);

		// if report time is b4 start time
		if (Double.parseDouble(report_time.substring(8, 10) + "."
				+ report_time.substring(10, 12)) < (Double
				.parseDouble(split_start_time[0] + "." + split_start_time[1]))) {
			// SOPError("Case 1 ********* ");

			report_time = report_time.substring(0, 8) + split_start_time[0]
					+ split_start_time[1] + "00";
			mydate = StringToDate(report_time);
			if (!CheckHolidate(mydate, public_holidate, Sun, Mon, Tue, Wed,
					Thu, Fri, Sat).equals("0")) {
				while (CheckHolidate(mydate, public_holidate, Sun, Mon, Tue,
						Wed, Thu, Fri, Sat).equals("1")) {
					mydate = AddHoursDate(mydate, 1, 0, 0);
				}
				report_time = ToLongDate(added_date);
				report_time = report_time.substring(0, 8) + split_start_time[0]
						+ split_start_time[1] + "00";
				added_date = StringToDate(report_time);
			}
		} // if report time is after end time
		else if (Double.parseDouble(report_time.substring(8, 10) + "."
				+ report_time.substring(10, 12)) > (Double
				.parseDouble(split_end_time[0] + "." + split_end_time[1]))) {
			// SOPError("Case 2 ********* ");

			mydate = AddHoursDate(StringToDate(report_time), 1, 0, 0);
			if (!CheckHolidate(mydate, public_holidate, Sun, Mon, Tue, Wed,
					Thu, Fri, Sat).equals("0")) {
				while (CheckHolidate(mydate, public_holidate, Sun, Mon, Tue,
						Wed, Thu, Fri, Sat).equals("1")) {
					mydate = AddHoursDate(mydate, 1, 0, 0);
				}
				report_time = ToLongDate(mydate);
				report_time = report_time.substring(0, 8) + split_start_time[0]
						+ split_start_time[1] + "00";
				added_date = StringToDate(report_time);
			} else {
				report_time = ToLongDate(mydate);
				report_time = report_time.substring(0, 8) + split_start_time[0]
						+ split_start_time[1] + "00";
				added_date = StringToDate(report_time);
			}
		} // if report time is b/w (start - end) time
		else if (Double.parseDouble(report_time.substring(8, 10) + "."
				+ report_time.substring(10, 12)) < (Double
					.parseDouble(split_end_time[0] + "." + split_end_time[1]))
				&& Double.parseDouble(report_time.substring(8, 10) + "."
						+ report_time.substring(10, 12)) > (Double
							.parseDouble(split_start_time[0] + "."
									+ split_start_time[1]))) {
			// SOP("Case 3 ********* ");

			if (!CheckHolidate(added_date, public_holidate, Sun, Mon, Tue, Wed,
					Thu, Fri, Sat).equals("0")) {
				while (CheckHolidate(added_date, public_holidate, Sun, Mon,
						Tue, Wed, Thu, Fri, Sat).equals("1")) {
					added_date = AddHoursDate(added_date, 1, 0, 0);
				}
				report_time = ToLongDate(added_date);
				report_time = report_time.substring(0, 8) + split_start_time[0]
						+ split_start_time[1] + "00";
				added_date = StringToDate(report_time);
			}
		}

		// ********************************************************************************************************************
		// adding minutes (duehrs in mins)
		if (mins_toadd == 0) {
			report_time = ToLongDate(added_date);
			report_time = report_time.substring(0, 8) + split_end_time[0]
					+ split_end_time[1] + "00";
			added_date = StringToDate(report_time);
		} else if (mins_toadd != 0) {

			int remaining_mins = 0;
			int end_hrs = GetMinsFromTime(split_end_time[0] + "."
					+ split_end_time[1]);
			String due_starttime = ToLongDate(added_date).substring(8, 10)
					+ "." + ToLongDate(added_date).substring(10, 12);
			int report_added_time = GetMinsFromTime(due_starttime);
			double time_diff = end_hrs - report_added_time;
			remaining_mins = mins_toadd;

			if (time_diff >= mins_toadd) {
				added_date = AddHoursDate(StringToDate(ToLongDate(added_date)),
						0, 0, mins_toadd);
				remaining_mins = 0;
			} else {
				added_date = AddHoursDate(StringToDate(ToLongDate(added_date)),
						0, 0, time_diff);
				mydate = added_date;
				mins_toadd = (int) (mins_toadd - time_diff);

				// -----------------------------------------------------------------------------------------------------
				// loop until there are no minutes to add
				while (remaining_mins != 0) {
					// SOPError("inside while loop remaining_mins  = "
					// + remaining_mins + "-------------");

					days_toadd = 1;
					String day = "0";
					int min = mins_toadd;
					String c, d = "";

					// ------------------------------------------------------------------------------------------------
					// check holidays while adding mins on next day
					while (min != 0) {
						String publicholi_check = "";
						mydate = AddHoursDate(StringToDate(ToLongDate(mydate)),
								1, 0, 0);

						for (int j = 1; j <= public_holidate.size(); j++) {
							c = ToLongDate(mydate).substring(0, 8);
							d = (String) public_holidate.get(j - 1);
							d = d.substring(0, 8);
							if (c.equals(d)) {
								days_toadd = days_toadd + 1;
								publicholi_check = "no";
							}
						}

						if (!publicholi_check.equals("no")) {
							int y = ReturnDayOfWeek(ToLongDate(mydate));
							if (y == 1) {
								days_toadd = days_toadd + Integer.parseInt(Sun);
								day = Sun;
							} else if (y == 2) {
								days_toadd = days_toadd + Integer.parseInt(Mon);
								day = Mon;
							} else if (y == 3) {
								days_toadd = days_toadd + Integer.parseInt(Tue);
								day = Tue;
							} else if (y == 4) {
								days_toadd = days_toadd + Integer.parseInt(Wed);
								day = Wed;
							} else if (y == 5) {
								days_toadd = days_toadd + Integer.parseInt(Thu);
								day = Thu;
							} else if (y == 6) {
								days_toadd = days_toadd + Integer.parseInt(Fri);
								day = Fri;
							} else if (y == 7) {
								days_toadd = days_toadd + Integer.parseInt(Sat);
								day = Sat;
							}
							if (day.equals("0")) {
								min = 0;
							}
						}
					}

					// ---------------------------------------------------------------------------------------------
					// adding days (holidays)
					added_date = AddHoursDate(
							StringToDate(ToLongDate(added_date)), days_toadd,
							0, 0);

					// ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
					// calculate remaining minutes
					report_time = ToLongDate(added_date);
					report_time = report_time.substring(0, 8)
							+ split_start_time[0] + split_start_time[1] + "00";
					String next_day = report_time.substring(8, 10) + "."
							+ report_time.substring(10, 12);
					int nextday_time = GetMinsFromTime(next_day);
					time_diff = end_hrs - nextday_time;

					if (time_diff >= mins_toadd) {
						// SOPError("Match 1 ------------");
						added_date = AddHoursDate(StringToDate(report_time), 0,
								0, mins_toadd);
						remaining_mins = 0;
					} else {

						// SOPError("Match 2 ------------");
						added_date = AddHoursDate(StringToDate(report_time), 0,
								0, time_diff);
						mins_toadd = mins_toadd - ((int) time_diff);
						remaining_mins = mins_toadd;
						if (remaining_mins < 0) {
							remaining_mins = 0;
						}
					}
					// ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
				}
				// ------------------------------------------------------------------------------------------------
			}
		}

		// ********************************************************************************************************************
		if (!CheckHolidate(added_date, public_holidate, Sun, Mon, Tue, Wed,
				Thu, Fri, Sat).equals("0")) {
			while (CheckHolidate(added_date, public_holidate, Sun, Mon, Tue,
					Wed, Thu, Fri, Sat).equals("1")) {
				added_date = AddHoursDate(added_date, 1, 0, 0);
			}
		}
		// ********************************************************************************************************************
		// SOPError("due_date is......" + added_date);
		return ToLongDate(added_date).toString();
	}

	public double DurtoMin(double x) {
		String ze = x + "";
		x = Double.parseDouble(ze);
		String split_ze[] = ze.split("\\.");

		double y = Math.floor(x);

		y = y * 60;
		y = y + Integer.parseInt(split_ze[1] + "0");
		return y;
	}

	public int GetMinsFromTime(String duehrs) {
		duehrs = duehrs.replaceAll(":", ".");
		String split_hrs_mins[] = duehrs.split("\\.");
		String mins, hrs = "";
		hrs = split_hrs_mins[0];
		mins = split_hrs_mins[1];
		if (mins.length() < 2) {
			mins = mins + "0";
		}
		int ret_min = Integer.parseInt(hrs) * 60;
		ret_min = ret_min + Integer.parseInt(mins);
		return ret_min;
	}

	public double ConverttoMinutes(double x) {
		// SOPError("x=========" + x);
		String ze = x + "";
		x = Double.parseDouble(ze);
		String split_ze[] = ze.split("\\.");
		int y = Integer.parseInt(split_ze[0]) * 60;
		if (!split_ze[1].equals("")) {
			y = y + Integer.parseInt(split_ze[1]);
		}
		return y;

	}

	public ArrayList publicHolidays(String ticketholi_start_time,
			String branch_id, String comp_id) {
		ArrayList ticketholi_date = null;
		if (branch_id.equals("0") | branch_id.equals("")) {
			ticketholi_date = new ArrayList();
			return ticketholi_date;
		} else {
			CachedRowSet crs = null;
			String ticketholi_limit_date = AddDayMonthYear(
					strToShortDate(ticketholi_start_time), 100, 0, 0, 0);
			try {
				String StrSql = "SELECT ticketholi_date" + " from "
						+ compdb(comp_id) + "axela_service_ticket_holi"
						+ " WHERE ticketholi_branch_id = " + branch_id
						+ " AND SUBSTR(ticketholi_date, 1, 8) >= SUBSTR('"
						+ ticketholi_start_time + "', 1, 8)"
						+ " AND ticketholi_date <= '"
						+ ConvertShortDateToStr(ticketholi_limit_date) + "'"
						+ " ORDER BY ticketholi_date";
				// SOPError("publicHolidays StrSql = " + StrSql);
				crs = processQuery(StrSql, 0);
				ticketholi_date = new ArrayList();
				while (crs.next()) {
					ticketholi_date.add(crs.getString("ticketholi_date"));
				}
				crs.close();
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
			return ticketholi_date;
		}
	}

	public String CheckHolidate(Date mydate, ArrayList holi_date, String sun,
			String mon, String tue, String wed, String thu, String fri,
			String sat) {
		String holi = "0", publicholi_date = "";
		String strdate = ToLongDate(mydate);
		int x = ReturnDayOfWeek(strdate);
		for (int i = 1; i <= holi_date.size(); i++) {
			// if (strdate.equals(holi_date.get(i - 1))) {
			if ((strdate.substring(0, 8) + "000000").equals(holi_date
					.get(i - 1))) {
				holi = "1";
				publicholi_date = "no";
			}
		}
		if (!publicholi_date.equals("no")) {
			if (x == 1 && sun.equals("1")) {
				holi = "1";
			} else if (x == 2 && mon.equals("1")) {
				holi = "1";
			} else if (x == 3 && tue.equals("1")) {
				holi = "1";
			} else if (x == 4 && wed.equals("1")) {
				holi = "1";
			} else if (x == 5 && thu.equals("1")) {
				holi = "1";
			} else if (x == 6 && fri.equals("1")) {
				holi = "1";
			} else if (x == 7 && sat.equals("1")) {
				holi = "1";
			}
		}
		return holi;
	}

	/*
	 * ////////////////////////////End of DUE TIME CALCULATION//////////////////////
	 */

	/* Added on 22nd April 2013 Smitha */
	public String CheckSpecialCharacters(String doc_title) {
		String msg = "";
		Pattern p = Pattern.compile("[^.a-z 0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(doc_title);
		Boolean b = m.find();
		String x = "";
		if (b) {
			msg = "<br>Special Characters not allowed";
			x = "1";
			return msg;
		} else {
			return "";
		}
	}

	public String getBranchName(String branch_id, String comp_id) {
		if (!branch_id.equals("0")) {
			return ExecuteQuery("Select CONCAT('<a href=../portal/branch-summary.jsp?branch_id=',branch_id,'>', branch_name, ' (',branch_code,')','</a>') as branch_name from "
					+ compdb(comp_id)
					+ "axela_branch where branch_id="
					+ branch_id + " ");
		} else {
			return "No Branch found!";
		}
	}

	public String SplitRegNo(String number, int parm) {
		int param = parm;
		String s = number;
		String last = "";
		StringBuilder sb = new StringBuilder(s);
		StringBuilder sub = new StringBuilder();
		if (sb.length() > 4) {
			sub.append(sb.substring(0, sb.length() - 4));
			last = sb.substring((sb.length() - 4), sb.length());
		}
		int len = sub.length();
		int a = param;
		while (len > param) {
			sub.insert(param, ' ');
			param = param + a + 1;
			len++;
		}
		// return sub.toString() + " " + last;
		return number;

	}

	public String returnNoZero(String param) {
		// SOPError("===" + param);
		if (Double.parseDouble(param) == 0) {

			return "";
		} else {
			return param;
		}
	}

	public String returnNoZero(String param, String perc) {
		// SOPError("===" + param);
		if (Double.parseDouble(param) == 0) {
			return "";
		} else {
			return param + perc;
		}
	}

	public String isNotPublicEmail(String email) {
		String valid_email = "1";
		// SOPError("===" + email);
		if (AppRun().equals("1")) {
			if (!email.equals("")) {
				String free_email[] = {"yahoo", "gmail", "aol", "hotmail",
						"rediffmail", "hushmail", "fastmail", "inbox", "zoho",
						"123mail", "ccnmail", "coolmail", "test"};
				email = email.substring(email.lastIndexOf("@"), email.length());
				for (int i = 0; i < free_email.length; i++) {
					if (email.toLowerCase().contains(free_email[i])) {
						valid_email = "0";
						// SOPError("====invalid ===" + email);
						break;
					}
				}
			} else {
				valid_email = "0";
			}
		}
		return valid_email;
	}

	public String ClickToCall(String callno, String comp_id) {
		String checkno = "";
		// SOP("comp_id-------------" + comp_id);
		if (comp_id.equals("1009")) {
			callno = callno.replace("-", "");
			// checkno = "<a href=\"javascript:clicktocall_1009("
			// + checkno
			// +
			// ");\" title=\"Click To Call\"><img src=\"../admin-ifx/icon-phone.png\" align=\"bottom\"></a>";
			checkno = "&nbsp;<a href=\"../portal/clicktocall-1009.jsp?callno=" + callno + "\" data-target=\"#Hintclicktocall\" data-toggle=\"modal\">"
					+ "<img src=\"../admin-ifx/icon-phone.png\" align=\"bottom\"></a>";
		}
		if (comp_id.equals("1011")) {
			callno = callno.replace("-", "");
			checkno = "&nbsp;<a href=\"../portal/clicktocall-1011.jsp?callno=" + callno + "\" data-target=\"#Hintclicktocall\" data-toggle=\"modal\">"
					+ "<img src=\"../admin-ifx/icon-phone.png\" align=\"bottom\"></a>";
		}
		return checkno;
	}

	/**
	 * @param comp_id
	 * @param crm_id
	 * @param colspan
	 * @param request
	 * @return
	 */
	public String CRMCustomFieldView(String comp_id, String crm_id, String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id,"
					+ " crmcf_numeric, crmcf_length_min, crmcf_length_max,"
					+ " crmcf_option, crmcf_unique, crmcf_mandatory, crmcf_instruction, crmcf_voc,"
					+ " crmcf_fieldref, COALESCE(crmcftrans_value, '') as crmcftransvalue, COALESCE(crmcftrans_value, '') AS crmcftrans_value,"
					+ " COALESCE(crmcftrans_voc, '') AS crmcftrans_voc,"
					+ " crm_enquiry_id, crm_so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_trans ON crmcftrans_crmcf_id = crmcf_id"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
					+ " AND crmcftrans_crm_id = " + crm_id + ""
					+ " WHERE crmcf_active = 1 AND crm_id = " + crm_id + ""
					+ " GROUP BY crmcf_id" + " ORDER BY crmcf_rank";
			// SOP("CRMCustomFieldView---------in connect class------" + StrSql);
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						}
					} else {
						if (crs.getString("crmcftransvalue") != null && !crs.getString("crmcf_fieldref").equals(""))
							fieldvalue = ReturnFieldRef(comp_id, crs.getString("crmcf_fieldref"), crs.getString("crm_enquiry_id"), crs.getString("crm_so_id"));
						else
							fieldvalue = crs.getString("crmcftrans_value");
						if (crs.getString("crmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToShortDate(fieldvalue);
							}
						} else if (crs.getString("crmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToLongDate(fieldvalue);
							}
						}
					}
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"right\">");
					Str.append(crs.getString("crmcf_title"));
					if (crs.getString("crmcf_mandatory").equals("1")) {
						Str.append("<font color=\"#ff0000\">*</font>");
					}
					Str.append(":</td>\n");
					Str.append("<td valign=top align=left");
					// if (!colspan.equals("0")) {
					// Str.append(" colspan=").append(colspan);
					// }
					if (!crs.getString("crmcf_voc").equals("1")) {
						Str.append(" colspan=").append("4");
					}
					Str.append(">\n");
					// / Start Form Fields
					// / Start Text Field
					if (crs.getString("crmcf_cftype_id").equals("1")) {
						Str.append("<input name=crmcf_id-").append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" type=text class=form-control size=50 maxlength=").append(crs.getString("crmcf_length_max"));
						Str.append(" value=\"").append(fieldvalue).append("\"");
						if (crs.getString("crmcf_numeric").equals("1")) {
							Str.append(" onKeyUp=\"toInteger('crmcf_id-").append(crs.getString("crmcf_id")).append("','custom')\"");
						}
						Str.append("/>");
					} // / End Text Field
						// / Start Text Area
					else if (crs.getString("crmcf_cftype_id").equals("2")) {
						Str.append("<textarea name=crmcf_id-").append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append("', 'span_crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',").append(crs.getString("crmcf_length_max"));
						Str.append(")\">").append(fieldvalue).append("</textarea>");
						Str.append(" <span id=\"span_crmcf_id-").append(crs.getString("crmcf_id")).append("\"> (").append(crs.getString("crmcf_length_max")).append(" Characters)</span>");
					} // / End Text Area
						// / Start Check Box
					else if (crs.getString("crmcf_cftype_id").equals("3")) {
						Str.append("<input id=crmcf_id-").append(crs.getString("crmcf_id")).append(" type=\"checkbox\"");
						Str.append(" name=crmcf_id-").append(crs.getString("crmcf_id")).append(" ").append(PopulateCheck(fieldvalue)).append("/>");
					} // / End Check Box
						// / Start Drop Down
					else if (crs.getString("crmcf_cftype_id").equals("4")) {
						Str.append("<select name=crmcf_id-").append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" class=form-control>\n");
						String[] option = crs.getString("crmcf_option").split("\\r\\n");
						Str.append("<option value=\"\">Select</option>\n");
						for (int i = 1; i <= option.length; i++) {
							Str.append("<option" + " value=\"").append(option[i - 1]).append("\" ");
							Str.append(StrSelectdrop(option[i - 1], fieldvalue));
							Str.append(">").append(option[i - 1]).append("</option>\n");
						}
						Str.append("</select>");
						// SOP("Str--------" + Str.toString());
					} // / End Drop Down
						// / Start Date Text Box
					else if (crs.getString("crmcf_cftype_id").equals("5")) {
						Str.append("<input name=crmcf_id-")
								.append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" type=text class=\"form-control datepicker\"");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("value=\"\"");
						Str.append("/>");
					} // / End Date Text Box
						// / Start DateTime Text Box
					else if (crs.getString("crmcf_cftype_id").equals("6")) {
						Str.append("<input name=crmcf_id-")
								.append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" type=text class=\"form-control datetimepicker \"");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("value=\"\"");
						Str.append("/>");
					} // / End DateTime Text Box
						// / Start Time Text Box
					else if (crs.getString("crmcf_cftype_id").equals("7")) {
						Str.append("<input name=crmcf_id-").append(crs.getString("crmcf_id")).append(" id=crmcf_id-").append(crs.getString("crmcf_id"));
						Str.append(" type=text class='form-control timepicker'");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append("/>");
					}
					// / End Time Text Box
					// / End Form Fields
					if (!crs.getString("crmcf_instruction").equals("")) {
						Str.append("<br>").append(crs.getString("crmcf_instruction"));
					}
					Str.append("</td>\n");
					if (crs.getString("crmcf_voc").equals("1")) {
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append("VOC:");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">");
						// // Display Voc

						Str.append("<textarea name=crmcftrans_voc-").append(crs.getString("crmcf_id")).append(" id=crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append(" cols=\"70\" rows=\"1\" class=\"form-control\" onKeyUp=\"charcount('").append("crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append("', 'span_crmcftrans_voc-").append(crs.getString("crmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',8000)\">").append(crs.getString("crmcftrans_voc")).append("</textarea>");
						Str.append(" <span id=\"span_crmcftrans_voc-").append(crs.getString("crmcf_id")).append("\"> (8000 Characters)</span>");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<input type=hidden name=customfieldsubmit value=yes>\n");
				// Str.append("</table>\n");
			}
			crs.close();
			// SOP("CRM days HTML BUild----" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String CRMCustomFieldValidate(String comp_id, String crm_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		String fieldvoc = "";
		crm_id = CNumeric(crm_id);
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id,"
					+ " crmcf_numeric, crmcf_length_min, crmcf_length_max, crmcf_voc,"
					+ " crmcf_option, crmcf_unique, crmcf_mandatory"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm_trans ON crmcftrans_crmcf_id = crmcf_id"
					+ " AND crmcftrans_crm_id = " + crm_id + ""
					+ " WHERE crmcf_active = 1" + " AND crm_id = " + crm_id
					+ "" + " GROUP BY crmcf_id " + " ORDER BY crmcf_rank";
			// SOP("StrSql===cf==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));
						fieldvoc = PadQuotes(request.getParameter("crmcftrans_voc-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						}
						// SOPError("fieldvalue = " + fieldvalue);
						if (fieldvalue.equals("") && crs.getString("crmcf_mandatory").equals("1")) {
							// / For Text Field AND Text Area
							if (crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) {
								Str.append("<br>").append(crs.getString("crmcf_title")).append(" is blank!");
							}
							// / For Drop Down AND Date Time
							if (crs.getString("crmcf_cftype_id").equals("4") || crs.getString("crmcf_cftype_id").equals("5") || crs.getString("crmcf_cftype_id").equals("6")
									|| crs.getString("crmcf_cftype_id").equals("7")) {
								Str.append("<br>Select ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / For Date
						if (!fieldvalue.equals("") && crs.getString("crmcf_cftype_id").equals("5")) {
							if (!isValidDateFormatShort(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / For Date Time
						if (!fieldvalue.equals("") && crs.getString("crmcf_cftype_id").equals("6")) {
							if (!isValidDateFormatLong(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / Minimum Length
						// / For Text Field AND Text Area
						if (crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) {
							if (crs.getInt("crmcf_length_min") > 0 && fieldvalue.length() < crs.getInt("crmcf_length_min")) {
								Str.append("<br>Enter a minimum of ").append(crs.getString("crmcf_length_min")).append(" Characters for ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// For voc text field
						if ((crs.getString("crmcf_cftype_id").equals("1") || crs.getString("crmcf_cftype_id").equals("2")) && crs.getString("crmcf_voc").equals("1")) {
							if (crs.getInt("crmcf_length_min") > 0 && fieldvoc.length() < crs.getInt("crmcf_length_min")) {
								Str.append("<br>Enter a minimum of voc ").append(crs.getString("crmcf_length_min")).append(" Characters for ").append(crs.getString("crmcf_title")).append("!");
							}
						}
						// / Maximum Length
						if (fieldvalue.length() > crs.getInt("crmcf_length_max")) {
							fieldvalue = fieldvalue.substring(0, crs.getInt("crmcf_length_max") - 1);
						}
						// / Check Unique Field
						if (!fieldvalue.equals("") && crs.getString("crmcf_unique").equals("1")) {
							StrSql = "SELECT cftrans_id" + " FROM " + "" + compdb(comp_id) + "axela_sales_crm_trans"
									+ " WHERE cftrans_crmcf_id = " + crs.getString("crmcf_id")
									+ " AND cftrans_value = '" + fieldvalue + "'";
							if (!ExecuteQuery(StrSql).equals("")) {
								Str.append("<br>").append(crs.getString("crmcf_title")).append(" is not unique!");
							}
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public void CRMCustomFieldUpdate(String comp_id, String crm_id,
			String update, HttpServletRequest request) throws Exception {
		String fieldvalue = "";
		String fieldvoc = "";
		// String emp_formatdate = GetSession("formatdate_name", request);
		// String emp_formattime = GetSession("formattime_name", request);
		crm_id = CNumeric(crm_id);
		String StrSql = "";
		Connection conntx = null;
		Statement stmttx = null;
		CachedRowSet crs = null;
		if (!crm_id.equals("0")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm_trans"
					+ " WHERE crmcftrans_crm_id = " + crm_id + "";
			updateQuery(StrSql);

			if (PadQuotes(request.getParameter("customfieldsubmit")).equals(
					"yes")
					&& update.equals("yes")) {
				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();

					StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id, crmcf_numeric, crmcf_length_min, crmcf_length_max,"
							+ " crmcf_option, crmcf_unique, crmcf_mandatory, crmcf_voc, crmcf_instruction, crm_enquiry_id "
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							+ " WHERE crmcf_active = 1"
							+ " AND crm_id = " + crm_id + ""
							+ " GROUP BY crmcf_id"
							+ " ORDER BY crmcf_rank";
					// SOPError("CustomFieldUpdate StrSql==="+StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);

					while (crs.next()) {
						// / Get Field Value
						fieldvalue = PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))));

						fieldvoc = PadQuotes(request.getParameter("crmcftrans_voc-" + (crs.getString("crmcf_id"))));
						if (crs.getString("crmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						} else if (crs.getString("crmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertShortDateToStr(PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id")))));
							}
						} else if (crs.getString("crmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertLongDateToStr((PadQuotes(request.getParameter("crmcf_id-" + (crs.getString("crmcf_id"))))));
							}
						}

						// SOP("fieldvalue====="+fieldvalue);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crm_trans"
								+ " (crmcftrans_crmcf_id,"
								+ " crmcftrans_crm_id,"
								+ " crmcftrans_value,"
								+ " crmcftrans_voc)"
								+ " VALUES"
								+ " ("
								+ crs.getString("crmcf_id") + ","
								+ " " + crm_id + ","
								+ " '" + fieldvalue + "', "
								+ " '" + fieldvoc + "')";
						// SOP("StrSql===ssss = " + StrSql);
						stmttx.addBatch(StrSql);
					}
					crs.close();
					stmttx.executeBatch();
					conntx.commit();

				} catch (Exception ex) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("AxelaAuto====" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
					// msg = "<br>Transaction Error!";
				} finally {
					conntx.setAutoCommit(true);
					stmttx.close();
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				}
			}
		}
	}

	// start of prowned crm custom fields
	public String PreownedCRMCustomFieldView(String comp_id, String precrmfollowup_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_cftype_id, precrmcf_numeric, precrmcf_length_min,"
					+ " precrmcf_length_max, precrmcf_option, precrmcf_unique, precrmcf_mandatory, precrmcf_instruction,"
					+ " precrmcf_voc, precrmcf_fieldref, "
					+ " COALESCE (precrmcftrans_value, '') AS precrmcftransvalue,"
					+ " COALESCE (precrmcftrans_value, '') AS precrmcftrans_value,"
					+ " COALESCE (precrmcftrans_voc, '') AS precrmcftrans_voc,"
					+ " precrmfollowup_preowned_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_trans ON precrmcftrans_precrmcf_id = precrmcf_id"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_sales_enquiry ON enquiry_id = precrmfollowup_preowned_id"
					+ " AND precrmcftrans_crm_id = " + precrmfollowup_id + ""
					+ " WHERE precrmcf_active = 1 AND precrmfollowup_id = " + precrmfollowup_id + ""
					+ " GROUP BY precrmcf_id" + " ORDER BY precrmcf_rank";
			// SOP("CRMCustomFieldView---------in connect class------" + StrSql);
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id"))));
						if (crs.getString("precrmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id")))));
							}
						}
					} else {
						if (crs.getString("precrmcftransvalue") != null && !crs.getString("precrmcf_fieldref").equals(""))
						{
							// fieldvalue = ReturnFieldRef(comp_id, crs.getString("precrmcf_fieldref"), crs.getString("precrmfollowup_preowned_id"), crs.getString("crm_so_id"));
						}
						else
							fieldvalue = crs.getString("precrmcftrans_value");
						if (crs.getString("precrmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToShortDate(fieldvalue);
							}
						} else if (crs.getString("precrmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToLongDate(fieldvalue);
							}
						}
					}
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"right\">");
					Str.append(crs.getString("precrmcf_title"));
					if (crs.getString("precrmcf_mandatory").equals("1")) {
						Str.append("<font color=\"#ff0000\">*</font>");
					}
					Str.append(":</td>\n");
					Str.append("<td valign=top align=left");
					if (!colspan.equals("0")) {
						Str.append(" colspan=").append(colspan);
					}
					Str.append(">\n");
					// / Start Form Fields
					// / Start Text Field
					if (crs.getString("precrmcf_cftype_id").equals("1")) {
						Str.append("<input name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" type=text class=form-control size=50 maxlength=").append(crs.getString("precrmcf_length_max"));
						Str.append(" value=\"").append(fieldvalue).append("\"");
						if (crs.getString("precrmcf_numeric").equals("1")) {
							Str.append(" onKeyUp=\"toInteger('precrmcf_id-").append(crs.getString("precrmcf_id")).append("','custom')\"");
						}
						Str.append("/>");
					} // / End Text Field
						// / Start Text Area
					else if (crs.getString("precrmcf_cftype_id").equals("2")) {
						Str.append("<textarea name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("', 'span_precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',").append(crs.getString("precrmcf_length_max"));
						Str.append(")\">").append(fieldvalue).append("</textarea>");
						Str.append(" <span id=\"span_precrmcf_id-").append(crs.getString("precrmcf_id")).append("\"> (").append(crs.getString("precrmcf_length_max")).append(" Characters)</span>");
					} // / End Text Area
						// / Start Check Box
					else if (crs.getString("precrmcf_cftype_id").equals("3")) {
						Str.append("<input id=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" type=\"checkbox\"");
						Str.append(" name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" ").append(PopulateCheck(fieldvalue)).append("/>");
					} // / End Check Box
						// / Start Drop Down
					else if (crs.getString("precrmcf_cftype_id").equals("4")) {
						Str.append("<select name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" class=form-control>\n");
						String[] option = crs.getString("precrmcf_option").split("\\r\\n");
						Str.append("<option value=\"\">Select</option>\n");
						for (int i = 1; i <= option.length; i++) {
							Str.append("<option" + " value=\"").append(option[i - 1]).append("\" ");
							Str.append(StrSelectdrop(option[i - 1], fieldvalue));
							Str.append(">").append(option[i - 1]).append("</option>\n");
						}
						Str.append("</select>");
						// SOP("Str--------" + Str.toString());
					} // / End Drop Down
						// / Start Date Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("5")) {
						// / Start Jquery Date Picker Function
						Str.append("<script type=text/javascript>\n");
						Str.append("$(function()").append("{\n");
						Str.append("$('").append("#precrmcf_id-").append(crs.getString("precrmcf_id")).append("')").append(".datepicker({\n");
						Str.append("showButtonPanel: true,\n");
						Str.append("dateFormat: ").append("'").append("dd/mm/yy").append("'\n");
						Str.append("});\n");
						Str.append("});\n");
						Str.append("</script>\n");
						// / End Jquery Date Picker Function
						Str.append("<input name=precrmcf_id-")
								.append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" type=text class=\"form-control date-picker\" size=12 maxlength=10");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("data-date-format=\"dd/mm/yyyy\" value=\"\"");
						Str.append("/>");
					} // / End Date Text Box
						// / Start DateTime Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("6")) {
						// / Start Jquery DateTime Picker Function
						Str.append("<script type=text/javascript>\n");
						Str.append("$(function()").append("{\n");
						Str.append("$('").append("#precrmcf_id-").append(crs.getString("precrmcf_id")).append("')").append(".datetimepicker({\n");
						Str.append("showButtonPanel: true,\n");
						Str.append("dateFormat: ").append("'").append("dd/mm/yy").append("',\n");
						Str.append("timeFormat: ").append("'").append("HH:mm").append("'\n");
						Str.append("});\n");
						Str.append("});\n");
						Str.append("</script>\n");
						// / End Jquery DateTime Picker Function
						Str.append("<input name=precrmcf_id-")
								.append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" type=text class=\"form-control date form_datetime\" size=18 maxlength=16");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("data-date-format=\"dd/mm/yyyy HH:mm\" value=\"\"");
						Str.append("/>");
					} // / End DateTime Text Box
						// / Start Time Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("7")) {
						// / Start Jquery Time Picker Function
						Str.append("<script type=text/javascript>\n");
						Str.append("$(function()").append("{\n");
						Str.append("$('").append("#precrmcf_id-").append(crs.getString("precrmcf_id")).append("')").append(".timepicker({\n");
						Str.append("showButtonPanel: true,\n");
						// Str.append("dateFormat: ").append("'hh:mm'\n");
						Str.append("});\n");
						Str.append("});\n");
						Str.append("</script>\n");
						// / End Jquery Time Picker Function
						Str.append("<input name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" type=text class=form-control size=7 maxlength=5");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append("/>");
					}
					// / End Time Text Box
					// / End Form Fields
					if (!crs.getString("precrmcf_instruction").equals("")) {
						Str.append("<br>").append(crs.getString("precrmcf_instruction"));
					}
					// // Display Voc
					if (crs.getString("precrmcf_voc").equals("1")) {
						Str.append("<br>VOC:<textarea name=precrmcftrans_voc-").append(crs.getString("precrmcf_id")).append(" id=precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append("', 'span_precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',8000)\">").append(crs.getString("precrmcftrans_voc")).append("</textarea>");
						Str.append(" <span id=\"span_precrmcftrans_voc-").append(crs.getString("precrmcf_id")).append("\"> (8000 Characters)</span>");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<input type=hidden name=customfieldsubmit value=yes>\n");
				// Str.append("</table>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String AppPreownedCRMCustomFieldView(String comp_id, String precrmfollowup_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_cftype_id, precrmcf_numeric, precrmcf_length_min,"
					+ " precrmcf_length_max, precrmcf_option, precrmcf_unique, precrmcf_mandatory, precrmcf_instruction,"
					+ " precrmcf_voc, precrmcf_fieldref, "
					+ " COALESCE (precrmcftrans_value, '') AS precrmcftransvalue,"
					+ " COALESCE (precrmcftrans_value, '') AS precrmcftrans_value,"
					+ " COALESCE (precrmcftrans_voc, '') AS precrmcftrans_voc,"
					+ " precrmfollowup_preowned_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_trans ON precrmcftrans_precrmcf_id = precrmcf_id"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_sales_enquiry ON enquiry_id = precrmfollowup_preowned_id"
					+ " AND precrmcftrans_crm_id = " + precrmfollowup_id + ""
					+ " WHERE precrmcf_active = 1 AND precrmfollowup_id = " + precrmfollowup_id + ""
					+ " GROUP BY precrmcf_id" + " ORDER BY precrmcf_rank";
			// SOP("CRMCustomFieldView---------in connect class------" + StrSql);
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id"))));
						if (crs.getString("precrmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id")))));
							}
						}
					} else {
						if (crs.getString("precrmcftransvalue") != null && !crs.getString("precrmcf_fieldref").equals(""))
						{
							// fieldvalue = ReturnFieldRef(comp_id, crs.getString("precrmcf_fieldref"), crs.getString("precrmfollowup_preowned_id"), crs.getString("crm_so_id"));
						}
						else
							fieldvalue = crs.getString("precrmcftrans_value");
						if (crs.getString("precrmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToShortDate(fieldvalue);
							}
						} else if (crs.getString("precrmcf_cftype_id").equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToLongDate(fieldvalue);
							}
						}
					}
					// Str.append("<tr>\n");
					// Str.append("<td valign=\"top\" align=\"right\">");
					Str.append("<div class=\"form-group form-md-line-input\">");
					Str.append("<label for=\"form_control_1\">");
					Str.append(crs.getString("precrmcf_title"));
					if (crs.getString("precrmcf_mandatory").equals("1")) {
						Str.append("<font color=\"#ff0000\">*</font>");
					}
					Str.append(":</label>");
					// Str.append(":</td>\n");
					// Str.append("<td valign=top align=left");
					// if (!colspan.equals("0")) {
					// Str.append(" colspan=").append(colspan);
					// }
					// Str.append(">\n");
					// / Start Form Fields
					// / Start Text Field
					if (crs.getString("precrmcf_cftype_id").equals("1")) {
						Str.append("<input name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" type=text class=form-control size=50 maxlength=").append(crs.getString("precrmcf_length_max"));
						Str.append(" value=\"").append(fieldvalue).append("\"");
						if (crs.getString("precrmcf_numeric").equals("1")) {
							Str.append(" onKeyUp=\"toInteger('precrmcf_id-").append(crs.getString("precrmcf_id")).append("','custom')\"");
						}
						Str.append("/>");
					} // / End Text Field
						// / Start Text Area
					else if (crs.getString("precrmcf_cftype_id").equals("2")) {
						Str.append("<textarea name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("', 'span_precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',").append(crs.getString("precrmcf_length_max"));
						Str.append(")\">").append(fieldvalue).append("</textarea>");
						Str.append(" <span id=\"span_precrmcf_id-").append(crs.getString("precrmcf_id")).append("\"> (").append(crs.getString("precrmcf_length_max")).append(" Characters)</span>");
					} // / End Text Area
						// / Start Check Box
					else if (crs.getString("precrmcf_cftype_id").equals("3")) {
						Str.append("&nbsp<input id=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" type=\"checkbox\"");
						Str.append(" name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" ").append(PopulateCheck(fieldvalue)).append("/>");
					} // / End Check Box
						// / Start Drop Down
					else if (crs.getString("precrmcf_cftype_id").equals("4")) {
						Str.append("<select name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append(" class=form-control>\n");
						String[] option = crs.getString("precrmcf_option").split("\\r\\n");
						Str.append("<option value=\"\">Select</option>\n");
						for (int i = 1; i <= option.length; i++) {
							Str.append("<option" + " value=\"").append(option[i - 1]).append("\" ");
							Str.append(StrSelectdrop(option[i - 1], fieldvalue));
							Str.append(">").append(option[i - 1]).append("</option>\n");
						}
						Str.append("</select>");
						// SOP("Str--------" + Str.toString());
					} // / End Drop Down
						// / Start Date Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("5")) {

						Str.append("<input name=\"precrmcf_id-")
								.append(crs.getString("precrmcf_id")).append("\" id=\"precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("\" type=\"text\" class=\"form-control\"");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append(" onclick=\"datePicker('precrmcf_id-").append(crs.getString("precrmcf_id")).append("')\" readonly/>");

					} // / End Date Text Box
						// / Start DateTime Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("6")) {
						// SOP("fieldvalue=========" + fieldvalue);
						String date = "", time = "";
						if (!fieldvalue.equals("")) {
							date = fieldvalue.substring(0, 10);
							time = fieldvalue.substring(11, 16);
						}
						Str.append("<div class=\"container\">");
						Str.append("<input placeholder=\"Date\" name=\"precrmcf_id-")
								.append(crs.getString("precrmcf_id")).append("\" id=\"precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("\" type=\"text\" class=\"col-xs-6");
						Str.append("\" value=\"").append(date).append("\"");
						Str.append(" onclick=\"datePicker('precrmcf_id-").append(crs.getString("precrmcf_id")).append("')\" readonly/>");

						Str.append("<input placeholder=\"Time\" name=\"precrmcftime_id-")
								.append(crs.getString("precrmcf_id")).append("\" id=\"precrmcftime_id-").append(crs.getString("precrmcf_id"));
						Str.append("\" type=\"text\" class=\"col-xs-6");
						Str.append("\" value=\"").append(time).append("\"");
						Str.append(" onclick=\"timePicker('precrmcftime_id-").append(crs.getString("precrmcf_id")).append("')\" readonly/>");
						Str.append("</div>");
					} // / End DateTime Text Box
						// / Start Time Text Box
					else if (crs.getString("precrmcf_cftype_id").equals("7")) {

						Str.append("<input name=precrmcf_id-").append(crs.getString("precrmcf_id")).append(" id=\"precrmcf_id-").append(crs.getString("precrmcf_id"));
						Str.append("\" type=\"text\" class=\"form-control\" size=7 maxlength=5");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append("/>");
					}
					// / End Time Text Box
					// / End Form Fields
					if (!crs.getString("precrmcf_instruction").equals("")) {
						Str.append("<br>").append(crs.getString("precrmcf_instruction"));
					}
					// // Display Voc
					if (crs.getString("precrmcf_voc").equals("1")) {
						Str.append("<br>VOC:<textarea name=precrmcftrans_voc-").append(crs.getString("precrmcf_id")).append(" id=precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append(" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('").append("precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append("', 'span_precrmcftrans_voc-").append(crs.getString("precrmcf_id"));
						Str.append("','<font color=red>({CHAR} characters left)</font>',8000)\">").append(crs.getString("precrmcftrans_voc")).append("</textarea>");
						Str.append(" <span id=\"span_precrmcftrans_voc-").append(crs.getString("precrmcf_id")).append("\"> (8000 Characters)</span>");
					}
					// Str.append("</td>\n");
					// Str.append("</tr>\n");
					Str.append("</div>\n");
				}
				Str.append("<input type=hidden name=customfieldsubmit value=yes>\n");
				// Str.append("</table>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PreownedCRMCustomFieldValidate(String comp_id, String precrmfollowup_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		String fieldvoc = "";
		precrmfollowup_id = CNumeric(precrmfollowup_id);
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_cftype_id, precrmcf_numeric,"
					+ " precrmcf_length_min, precrmcf_length_max, precrmcf_voc, precrmcf_option,"
					+ " precrmcf_unique, precrmcf_mandatory "
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crm_trans ON precrmcftrans_precrmcf_id = precrmcf_id"
					+ " AND precrmcftrans_crm_id = " + precrmfollowup_id + ""
					+ " WHERE precrmcf_active = 1" + " AND precrmfollowup_id = " + precrmfollowup_id
					+ "" + " GROUP BY precrmcf_id " + " ORDER BY precrmcf_rank";
			// SOP("StrSql===cf==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit")).equals("yes")) {
						fieldvalue = PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id"))));
						fieldvoc = PadQuotes(request.getParameter("precrmcf_voc-" + (crs.getString("precrmcf_id"))));
						if (crs.getString("precrmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id")))));
							}
						}
						// SOPError("fieldvalue = " + fieldvalue);
						if (fieldvalue.equals("") && crs.getString("precrmcf_mandatory").equals("1")) {
							// / For Text Field AND Text Area
							if (crs.getString("precrmcf_cftype_id").equals("1") || crs.getString("precrmcf_cftype_id").equals("2")) {
								Str.append("<br>").append(crs.getString("precrmcf_title")).append(" is blank!");
							}
							// / For Drop Down AND Date Time
							if (crs.getString("precrmcf_cftype_id").equals("4") || crs.getString("precrmcf_cftype_id").equals("5") || crs.getString("precrmcf_cftype_id").equals("6")
									|| crs.getString("precrmcf_cftype_id").equals("7")) {
								Str.append("<br>Select ").append(crs.getString("precrmcf_title")).append("!");
							}
						}
						// / For Date
						if (!fieldvalue.equals("") && crs.getString("precrmcf_cftype_id").equals("5")) {
							if (!isValidDateFormatShort(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("precrmcf_title")).append("!");
							}
						}
						// / For Date Time
						if (!fieldvalue.equals("") && crs.getString("precrmcf_cftype_id").equals("6")) {
							if (!isValidDateFormatLong(fieldvalue)) {
								Str.append("<br>Enter Valid ").append(crs.getString("precrmcf_title")).append("!");
							}
						}
						// / Minimum Length
						// / For Text Field AND Text Area
						if (crs.getString("precrmcf_cftype_id").equals("1") || crs.getString("precrmcf_cftype_id").equals("2")) {
							if (crs.getInt("precrmcf_length_min") > 0 && fieldvalue.length() < crs.getInt("precrmcf_length_min")) {
								Str.append("<br>Enter a minimum of ").append(crs.getString("precrmcf_length_min")).append(" Characters for ").append(crs.getString("precrmcf_title")).append("!");
							}
						}
						// For voc text field
						if ((crs.getString("precrmcf_cftype_id").equals("1") || crs.getString("precrmcf_cftype_id").equals("2")) && crs.getString("precrmcf_voc").equals("1")) {
							if (crs.getInt("precrmcf_length_min") > 0 && fieldvoc.length() < crs.getInt("precrmcf_length_min")) {
								Str.append("<br>Enter a minimum of voc ").append(crs.getString("precrmcf_length_min")).append(" Characters for ").append(crs.getString("precrmcf_title")).append("!");
							}
						}
						// / Maximum Length
						if (fieldvalue.length() > crs.getInt("precrmcf_length_max")) {
							fieldvalue = fieldvalue.substring(0, crs.getInt("precrmcf_length_max") - 1);
						}
						// / Check Unique Field
						if (!fieldvalue.equals("") && crs.getString("precrmcf_unique").equals("1")) {
							StrSql = "SELECT precrmcftrans_id" + " FROM " + "" + compdb(comp_id) + "axela_preowned_crm_trans"
									+ " WHERE precrmcftrans_precrmcf_id = " + crs.getString("precrmcf_id")
									+ " AND precrmcftrans_value = '" + fieldvalue + "'";
							if (!ExecuteQuery(StrSql).equals("")) {
								Str.append("<br>").append(crs.getString("precrmcf_title")).append(" is not unique!");
							}
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}// end

	public void PreownedCRMCustomFieldUpdate(String comp_id, String precrmfollowup_id,
			String update, HttpServletRequest request) throws Exception {
		String fieldvalue = "";
		String fieldvalue1 = "";
		String fieldvoc = "";
		// String emp_formatdate = GetSession("formatdate_name", request);
		// String emp_formattime = GetSession("formattime_name", request);
		precrmfollowup_id = CNumeric(precrmfollowup_id);
		String StrSql = "";
		Connection conntx = null;
		Statement stmttx = null;
		CachedRowSet crs = null;
		if (!precrmfollowup_id.equals("0")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_crm_trans"
					+ " WHERE precrmcftrans_crm_id = " + precrmfollowup_id + "";
			updateQuery(StrSql);

			if (PadQuotes(request.getParameter("customfieldsubmit")).equals(
					"yes")
					&& update.equals("yes")) {
				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();

					StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_cftype_id, precrmcf_numeric, precrmcf_length_min, precrmcf_length_max,"
							+ " precrmcf_option, precrmcf_unique, precrmcf_mandatory, precrmcf_voc, precrmcf_instruction, precrmfollowup_id "
							+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
							+ " WHERE precrmcf_active = 1"
							+ " AND precrmfollowup_id = " + precrmfollowup_id + ""
							+ " GROUP BY precrmcf_id"
							+ " ORDER BY precrmcf_rank";
					SOPError("Pre CustomFieldUpdate StrSql===" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);

					while (crs.next()) {
						// / Get Field Value
						fieldvalue = PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id"))));
						fieldvalue1 = PadQuotes(request.getParameter("precrmcftime_id-" + (crs.getString("precrmcf_id"))));
						fieldvoc = PadQuotes(request.getParameter("precrmcf_voc-" + (crs.getString("precrmcf_id"))));
						if (crs.getString("precrmcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id")))));
							}
						} else if (crs.getString("precrmcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertShortDateToStr(PadQuotes(request.getParameter("precrmcf_id-" + (crs.getString("precrmcf_id")))));
							}
						} else if (crs.getString("precrmcf_cftype_id").equals("6")) {
							if (!fieldvalue1.equals("") && !fieldvalue1.equals(null)) {
								fieldvalue = fieldvalue + " " + fieldvalue1;
							}
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertLongDateToStr(fieldvalue);
							}
						}
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_crm_trans"
								+ " (precrmcftrans_precrmcf_id,"
								+ " precrmcftrans_crm_id,"
								+ " precrmcftrans_value,"
								+ " precrmcftrans_voc)"
								+ " VALUES"
								+ " ("
								+ crs.getString("precrmcf_id") + ","
								+ " " + precrmfollowup_id + ","
								+ " '" + fieldvalue + "', "
								+ " '" + fieldvoc + "')";
						// SOP("StrSql===ssss = " + StrSql);
						stmttx.addBatch(StrSql);
					}
					crs.close();
					stmttx.executeBatch();
					conntx.commit();

				} catch (Exception ex) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("AxelaAuto====" + this.getClass().getName());
						SOPError("Error in "
								+ new Exception().getStackTrace()[0]
										.getMethodName() + ": " + ex);
					}
					// msg = "<br>Transaction Error!";
				} finally {
					conntx.setAutoCommit(true);
					stmttx.close();
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				}
			}
		}
	}
	// END

	// PSF Custom field view method
	public String PSFCustomFieldView(String comp_id, String jcpsf_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, jcpsfcf_cftype_id,"
					+ " jcpsfcf_numeric, jcpsfcf_length_min, jcpsfcf_length_max,"
					+ " jcpsfcf_option, jcpsfcf_unique, jcpsfcf_mandatory, jcpsfcf_instruction, jcpsfcf_voc,"
					+ " jcpsfcf_fieldref, COALESCE(jcpsfcftrans_value, '') as jcpsfcftransvalue, COALESCE(jcpsfcftrans_value, '') AS jcpsfcftrans_value,"
					+ " COALESCE(jcpsfcftrans_voc, '') AS jcpsfcftrans_voc"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_service_jc_psf"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psf_trans ON jcpsfcftrans_jcpsfcf_id = jcpsfcf_id"
					+ " AND jcpsfcftrans_jcpsf_id = "
					+ jcpsf_id
					+ ""
					+ " WHERE jcpsfcf_active = 1 AND jcpsf_id = " + jcpsf_id
					+ "" + " GROUP BY jcpsfcf_id" + " ORDER BY jcpsfcf_rank";
			// SOP("--in connect class------"+StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit"))
							.equals("yes")) {
						fieldvalue = PadQuotes(request
								.getParameter("jcpsfcf_id-"
										+ (crs.getString("jcpsfcf_id"))));
						if (crs.getString("jcpsfcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request
										.getParameter("jcpsfcf_id-"
												+ (crs.getString("jcpsfcf_id")))));
							}
						}
					} else {
						if (crs.getString("jcpsfcftransvalue") == null
								&& !crs.getString("jcpsfcf_fieldref").equals(""))
							fieldvalue = crs.getString("jcpsfcf_fieldref");
						// fieldvalue = ReturnFieldRef(comp_id,
						// crs.getString("jcpsfcf_fieldref"),
						// crs.getString("crm_enquiry_id"),
						// crs.getString("crm_so_id"));
						else
							fieldvalue = crs.getString("jcpsfcftrans_value");
						if (crs.getString("jcpsfcf_cftype_id").equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToShortDate(fieldvalue);
							}
						} else if (crs.getString("jcpsfcf_cftype_id")
								.equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = strToLongDate(fieldvalue);
							}
						}
					}
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"right\">");
					Str.append(crs.getString("jcpsfcf_title"));
					if (crs.getString("jcpsfcf_mandatory").equals("1")) {
						Str.append("<font color=\"#ff0000\">*</font>");
					}
					Str.append(":</td>\n");
					Str.append("<td valign=top align=left");
					if (!colspan.equals("0")) {
						Str.append(" colspan=").append(colspan);
					}
					Str.append(">\n");
					// / Start Form Fields
					// / Start Text Field
					if (crs.getString("jcpsfcf_cftype_id").equals("1")) {
						Str.append("<input name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(
								" type=text class=form-control size=50 maxlength=")
								.append(crs.getString("jcpsfcf_length_max"));
						Str.append(" value=\"").append(fieldvalue).append("\"");
						if (crs.getString("jcpsfcf_numeric").equals("1")) {
							Str.append(" onKeyUp=\"toInteger('jcpsfcf_id-")
									.append(crs.getString("jcpsfcf_id"))
									.append("','custom')\"");
						}
						Str.append("/>");
					} // / End Text Field
						// / Start Text Area
					else if (crs.getString("jcpsfcf_cftype_id").equals("2")) {
						Str.append("<textarea name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(
								" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('")
								.append("jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append("', 'span_jcpsfcf_id-").append(
								crs.getString("jcpsfcf_id"));
						Str.append(
								"','<font color=red>({CHAR} characters left)</font>',")
								.append(crs.getString("jcpsfcf_length_max"));
						Str.append(")\">").append(fieldvalue)
								.append("</textarea>");
						Str.append(" <span id=\"span_jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append("\"> (")
								.append(crs.getString("jcpsfcf_length_max"))
								.append(" Characters)</span>");
					} // / End Text Area
						// / Start Check Box
					else if (crs.getString("jcpsfcf_cftype_id").equals("3")) {
						Str.append("<input id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" type=\"checkbox\"");
						Str.append(" name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id")).append(" ")
								.append(PopulateCheck(fieldvalue)).append("/>");
					} // / End Check Box
						// / Start Drop Down
					else if (crs.getString("jcpsfcf_cftype_id").equals("4")) {
						Str.append("<select name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(" class=form-control>\n");
						String[] option = crs.getString("jcpsfcf_option").split(
								"\\r\\n");
						Str.append("<option value=\"\">Select</option>\n");
						for (int i = 1; i <= option.length; i++) {
							Str.append("<option" + " value=\"")
									.append(option[i - 1]).append("\" ");
							Str.append(StrSelectdrop(option[i - 1], fieldvalue));
							Str.append(">").append(option[i - 1])
									.append("</option>\n");
						}
						Str.append("</select>");
					} // / End Drop Down
						// / Start Date Text Box
					else if (crs.getString("jcpsfcf_cftype_id").equals("5")) {
						Str.append("<input name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(" type=text ");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("class=\"form-control datepicker\"")
								.append(" value=\"\"");
						Str.append("/>");
					} // / End Date Text Box
						// / Start DateTime Text Box
					else if (crs.getString("jcpsfcf_cftype_id").equals("6")) {
						Str.append("<input name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(" type=text");
						Str.append(" value=\"").append(fieldvalue).append("\"").append("class=\"form-control datetimepicker\"")
								.append("value=\"\"");
						Str.append("/>");
					} // / End DateTime Text Box
						// / Start Time Text Box
					else if (crs.getString("jcpsfcf_cftype_id").equals("7")) {
						Str.append("<input name=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcf_id-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(" type=text class='form-control timepicker'");
						Str.append(" value=\"").append(fieldvalue).append("\"");
						Str.append("/>");
					}
					// / End Time Text Box
					// / End Form Fields
					if (!crs.getString("jcpsfcf_instruction").equals("")) {
						Str.append("<br>").append(
								crs.getString("jcpsfcf_instruction"));
					}
					// // Display Voc
					if (crs.getString("jcpsfcf_voc").equals("1")) {
						Str.append("<br>VOC:<textarea name=jcpsfcftrans_voc-")
								.append(crs.getString("jcpsfcf_id"))
								.append(" id=jcpsfcftrans_voc-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append(
								" cols=\"70\" rows=\"5\" class=\"form-control\" onKeyUp=\"charcount('")
								.append("jcpsfcftrans_voc-")
								.append(crs.getString("jcpsfcf_id"));
						Str.append("', 'span_jcpsfcftrans_voc-").append(
								crs.getString("jcpsfcf_id"));
						Str.append(
								"','<font color=red>({CHAR} characters left)</font>',8000)\">")
								.append(crs.getString("jcpsfcftrans_voc"))
								.append("</textarea>");
						Str.append(" <span id=\"span_jcpsfcftrans_voc-")
								.append(crs.getString("jcpsfcf_id"))
								.append("\"> (8000 Characters)</span>");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<input type=hidden name=customfieldsubmit value=yes>\n");
				// Str.append("</table>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PSFCustomFieldValidate(String comp_id, String jcpsf_id,
			String colspan, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		String fieldvoc = "";
		jcpsf_id = CNumeric(jcpsf_id);
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, jcpsfcf_cftype_id,"
					+ " jcpsfcf_numeric, jcpsfcf_length_min, jcpsfcf_length_max, jcpsfcf_voc,"
					+ " jcpsfcf_option, jcpsfcf_unique, jcpsfcf_mandatory"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_service_jc_psf"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_service_jc_psf_trans ON jcpsfcftrans_jcpsfcf_id = jcpsfcf_id"
					+ " AND jcpsfcftrans_jcpsf_id = "
					+ jcpsf_id
					+ ""
					+ " WHERE jcpsfcf_active = 1"
					+ " AND jcpsf_id = "
					+ jcpsf_id
					+ ""
					+ " GROUP BY jcpsfcf_id "
					+ " ORDER BY jcpsfcf_rank";
			// SOP("StrSql===cf=="+StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					// / Get Field Value
					if (PadQuotes(request.getParameter("customfieldsubmit"))
							.equals("yes")) {
						fieldvalue = PadQuotes(request
								.getParameter("jcpsfcf_id-"
										+ (crs.getString("jcpsfcf_id"))));
						fieldvoc = PadQuotes(request
								.getParameter("jcpsfcftrans_voc-"
										+ (crs.getString("jcpsfcf_id"))));
						if (crs.getString("jcpsfcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request
										.getParameter("jcpsfcf_id-"
												+ (crs.getString

														("jcpsfcf_id")))));
							}
						}
						// SOPError("fieldvalue = " + fieldvalue);
						if (fieldvalue.equals("")
								&& crs.getString("jcpsfcf_mandatory")
										.equals("1")) {
							// / For Text Field AND Text Area
							if (crs.getString("jcpsfcf_cftype_id").equals("1")
									|| crs.getString("jcpsfcf_cftype_id")
											.equals("2")) {
								Str.append("<br>")
										.append(crs.getString("jcpsfcf_title"))
										.append(" is blank!");
							}
							// / For Drop Down AND Date Time
							if (crs.getString("jcpsfcf_cftype_id").equals("4")
									|| crs.getString("jcpsfcf_cftype_id")
											.equals("5")
									|| crs.getString("jcpsfcf_cftype_id")
											.equals("6")
									|| crs.getString("jcpsfcf_cftype_id")
											.equals("7")) {
								Str.append("<br>Select ")
										.append(crs.getString("jcpsfcf_title"))
										.append("!");
							}
						}
						// / For Date
						if (!fieldvalue.equals("")
								&& crs.getString("jcpsfcf_cftype_id")
										.equals("5")) {
							if (!isValidDateFormatShort(fieldvalue)) {
								Str.append("<br>Enter Valid ")
										.append(crs.getString("jcpsfcf_title"))
										.append("!");
							}
						}
						// / For Date Time
						if (!fieldvalue.equals("")
								&& crs.getString("jcpsfcf_cftype_id")
										.equals("6")) {
							if (!isValidDateFormatLong(fieldvalue)) {
								Str.append("<br>Enter Valid ")
										.append(crs.getString("jcpsfcf_title"))
										.append("!");
							}
						}
						// / Minimum Length
						// / For Text Field AND Text Area
						if (crs.getString("jcpsfcf_cftype_id").equals("1")
								|| crs.getString("jcpsfcf_cftype_id")
										.equals("2")) {
							if (crs.getInt("jcpsfcf_length_min") > 0
									&& fieldvalue.length() <

									crs.getInt("jcpsfcf_length_min")) {
								Str.append("<br>Enter a minimum of ")
										.append(crs
												.getString("jcpsfcf_length_min"))
										.append(" Characters for ").append

										(crs.getString("jcpsfcf_title"))
										.append("!");
							}
						}
						// For voc text field
						if ((crs.getString("jcpsfcf_cftype_id").equals("1") || crs
								.getString("jcpsfcf_cftype_id").equals("2")) &&

								crs.getString("jcpsfcf_voc").equals("1")) {
							if (crs.getInt("jcpsfcf_length_min") > 0
									&& fieldvoc.length() <

									crs.getInt("jcpsfcf_length_min")) {
								Str.append("<br>Enter a minimum of voc ")
										.append(crs
												.getString("jcpsfcf_length_min"))
										.append(" Characters for ").append

										(crs.getString("jcpsfcf_title"))
										.append("!");
							}
						}
						// / Maximum Length
						if (fieldvalue.length() > crs
								.getInt("jcpsfcf_length_max")) {
							fieldvalue = fieldvalue.substring(0,
									crs.getInt("jcpsfcf_length_max") - 1);
						}
						// / Check Unique Field
						if (!fieldvalue.equals("")
								&& crs.getString("jcpsfcf_unique").equals("1")) {
							StrSql = "SELECT cftrans_id" + " FROM " + ""
									+ compdb(comp_id) + "axela_sales_crm_trans"
									+ " WHERE cftrans_jcpsfcf_id = "
									+ crs.getString("jcpsfcf_id")
									+ " AND cftrans_value = '" + fieldvalue
									+ "'";
							if (!ExecuteQuery(StrSql).equals("")) {
								Str.append("<br>")
										.append(crs.getString("jcpsfcf_title"))
										.append(" is not unique!");
							}
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto====" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public void PSFCustomFieldUpdate(String comp_id, String jcpsf_id,
			String update, HttpServletRequest request) throws Exception {
		String fieldvalue = "";
		String fieldvoc = "";
		// String emp_formatdate = GetSession("formatdate_name", request);
		// String emp_formattime = GetSession("formattime_name", request);
		jcpsf_id = CNumeric(jcpsf_id);
		String StrSql = "";
		Connection conntx = null;
		Statement stmttx = null;
		CachedRowSet crs = null;
		if (!jcpsf_id.equals("0")) {
			StrSql = "DELETE FROM " + compdb(comp_id)
					+ "axela_service_jc_psf_trans"
					+ " WHERE jcpsfcftrans_jcpsf_id = " + jcpsf_id + "";
			updateQuery(StrSql);

			if (PadQuotes(request.getParameter("customfieldsubmit")).equals(
					"yes")
					&& update.equals("yes")) {
				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();

					StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, jcpsfcf_cftype_id, jcpsfcf_numeric, jcpsfcf_length_min, jcpsfcf_length_max,"
							+ " jcpsfcf_option, jcpsfcf_unique, jcpsfcf_mandatory, jcpsfcf_voc, jcpsfcf_instruction "
							+ " FROM "
							+ compdb(comp_id)
							+ "axela_service_jc_psf"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
							+ " WHERE jcpsfcf_active = 1"
							+ " AND jcpsf_id = "
							+ jcpsf_id
							+ ""
							+ " GROUP BY jcpsfcf_id"
							+ " ORDER BY jcpsfcf_rank";
					// SOPError("CustomFieldUpdate StrSql==="+StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);

					while (crs.next()) {
						// / Get Field Value
						fieldvalue = PadQuotes(request
								.getParameter("jcpsfcf_id-"
										+ (crs.getString("jcpsfcf_id"))));
						fieldvoc = PadQuotes(request
								.getParameter("jcpsfcftrans_voc-"
										+ (crs.getString("jcpsfcf_id"))));
						if (crs.getString("jcpsfcf_cftype_id").equals("3")) {
							if (fieldvalue.equals("on")) {
								fieldvalue = CheckBoxValue(PadQuotes(request
										.getParameter("jcpsfcf_id-"
												+ (crs.getString("jcpsfcf_id")))));
							}
						} else if (crs.getString("jcpsfcf_cftype_id")
								.equals("5")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertShortDateToStr(PadQuotes(request
										.getParameter("jcpsfcf_id-"
												+ (crs.getString("jcpsfcf_id")))));
							}
						} else if (crs.getString("jcpsfcf_cftype_id")
								.equals("6")) {
							if (!fieldvalue.equals("")) {
								fieldvalue = ConvertLongDateToStr((PadQuotes(request
										.getParameter("jcpsfcf_id-"
												+ (crs.getString("jcpsfcf_id"))))));
							}
						}

						// SOP("fieldvalue====="+fieldvalue);
						StrSql = "INSERT INTO " + compdb(comp_id)
								+ "axela_service_jc_psf_trans"
								+ " (jcpsfcftrans_jcpsfcf_id,"
								+ " jcpsfcftrans_jcpsf_id,"
								+ " jcpsfcftrans_value," + " jcpsfcftrans_voc)"
								+ " VALUES" + " (" + crs.getString("jcpsfcf_id")
								+ "," + " " + jcpsf_id + "," + " '"
								+ fieldvalue + "', " + " '" + fieldvoc + "')";
						// SOP("StrSql===ssss = " + StrSql);
						stmttx.addBatch(StrSql);
					}
					crs.close();
					stmttx.executeBatch();
					conntx.commit();

				} catch (Exception ex) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("AxelaAuto====" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
					// msg = "<br>Transaction Error!";
				} finally {
					conntx.setAutoCommit(true);
					stmttx.close();
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				}
			}
		}
	}

	public String ReturnFieldRef(String comp_id, String crmcf_fieldref,
			String enquiry_id, String so_id) {
		String StrSql = "", crmcffieldref = "";
		StrSql = "SELECT COALESCE(" + crmcf_fieldref + ",'')"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_enquiry_id = enquiry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype ON buyertype_id = enquiry_buyertype_id "
				+ " WHERE 1=1" + " AND enquiry_id = " + enquiry_id + "";
		// SOP("StrSql---ReturnFieldRef------" + StrSql);
		if (!so_id.equals("0")) {
			StrSql += " AND so_id = " + so_id + "";
		}
		StrSql += " GROUP BY enquiry_id" + " LIMIT 1";
		crmcffieldref = ExecuteQuery(StrSql);
		return crmcffieldref;
	}

	public String getcompsession(HttpServletRequest request,
			HttpServletResponse response) {
		String comp_id = "0";
		String comp_name = "";
		String servername = PadQuotes(request.getServerName() + "");
		servername = servername.replace("www.", "");
		HttpSession session = request.getSession(true);
		try {
			if (AppRun().equals("0")) {
				// comp_id = "1000";
				// comp_id = "1001";
				// comp_id = "1009";
				// comp_id = "1011";
				// comp_id = "1014";
				// comp_id = "1015";
				// comp_id = "1017";
				// comp_id = "1018";
				// comp_id = "1019";
				// comp_id = "1020";
				// comp_id = "1022";
				comp_id = "1023";
				// comp_id = "1024";
				// comp_id = "1026";
				comp_name = "Localhost Demo";
			} else if (AppRun().equals("1")) {
				if (servername.equals("demo.axelaauto.com")) {
					comp_id = "1000";
					comp_name = "AxelaAuto Demo";
				} else if (servername.equals("ddmotors.axelaauto.com")) {
					comp_id = "1009";
					comp_name = "DD Motors";
				} else if (servername.equals("indel.axelaauto.com")) {
					comp_id = "1011";
					comp_name = "Indel";
				} else if (servername.equals("jubilant.axelaauto.com")) {
					comp_id = "1014";
					comp_name = "Jubilant";
				} else if (servername.equals("bbt.axelaauto.com")) {
					comp_id = "1015";
					comp_name = "BBT";
				} else if (servername.equals("joshi.axelaauto.com")) {
					comp_id = "1017";
					comp_name = "Joshi";
				}
				// else if (servername.equals("mrcar.axelaauto.com")) {
				// comp_id = "1018";
				// comp_name = "Mr. Car";
				// }
				else if (servername.equals("redfort.axelaauto.com")) {
					comp_id = "1019";
					comp_name = "RedFort";
				}
				// else if (servername.equals("highline.axelaauto.com")) {
				// comp_id = "1020";
				// comp_name = "Highline";
				// }
				// else if (servername.equals("porsche.axelaauto.com")) {
				// comp_id = "1022";
				// comp_name = "Porsche";
				// }
				else if (servername.equals("amp.axelaauto.com")) {
					comp_id = "1023";
					comp_name = "AMP";
				} else if (servername.equals("pcg.axelaauto.com")) {
					comp_id = "1024";
					comp_name = "PCG";
				} else if (servername.equals("mapl.axelaauto.com")) {
					comp_id = "1026";
					comp_name = "Manikandan Automobiles Private Limited";
				}
			}

			if (comp_id.equals("0")) {
				response.sendRedirect(response
						.encodeRedirectURL("access.jsp?msg=Please contact Administrator!"));
			}
			SetSession("comp_id", comp_id, request);
			SetSession("comp_name", comp_name, request);
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return comp_id;
	}

	public void BuildAccMenuLinks(HttpServletRequest request, String comp_id) {
		StringBuilder accincomelink = new StringBuilder();
		StringBuilder accexpenselink = new StringBuilder();
		StringBuilder allaccincomelink = new StringBuilder();
		StringBuilder allaccexpenselink = new StringBuilder();
		StringBuilder accaddlink = new StringBuilder();
		int temp = 0;
		int count = 0;
		StringBuilder ul = new StringBuilder();
		StringBuilder li = new StringBuilder();
		HttpSession session = request.getSession(true);
		try {
			String StrSql = "select voucherclass_id, vouchertype_id, vouchertype_name, voucherclass_income, voucherclass_file"
					+ " FROM"
					+ " "
					+ compdb(comp_id)
					+ "axela_acc_voucher_type"
					+ " INNER JOIN "
					+ maindb()
					+ "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " WHERE vouchertype_active = 1 AND voucherclass_active = 1"
					+ " AND voucherclass_income !=0"
					+ " ORDER BY voucherclass_id, voucherclass_income, vouchertype_rank";
			// SOP("StrSql =accmenu=== " + StrSqlBreaker(StrSql));
			ResultSet accrs = processQuery(StrSql, 0);
			while (accrs.next()) {
				// income
				if (accrs.getString("voucherclass_income").equals("1")) {
					if (!accrs.getString("vouchertype_id").equals("103")) {
						// add link
						accincomelink
								.append("<li><a href=\"../accounting/"
										+ accrs.getString("voucherclass_file")
										+ "-update.jsp?add=yes")
								.append("&voucherclass_id=")
								.append(accrs.getString("voucherclass_id"));
						accincomelink.append("&vouchertype_id="
								+ accrs.getString("vouchertype_id") + "\">Add "
								+ accrs.getString("vouchertype_name")
								+ "</a></li>\n");

						// landing, add AND list link
						// landing page link
						allaccincomelink
								.append("<li>")
								.append("<a href=\"../accounting/voucher.jsp?add=yes")
								.append("&param3=")
								.append(accrs.getString("vouchertype_id")
										+ "\">"
										+ accrs.getString("vouchertype_name")
										+ "</a>");
						// add link
						allaccincomelink
								.append("<li><a href=\"../accounting/"
										+ accrs.getString("voucherclass_file")
										+ "-update.jsp?add=yes")
								.append("&voucherclass_id=")
								.append(accrs.getString("voucherclass_id"))
								.append("&vouchertype_id="
										+ accrs.getString("vouchertype_id")
										+ "\">Add "
										+ accrs.getString("vouchertype_name")
										+ "</a>");
						// list link
						allaccincomelink
								.append("<li>")
								.append("<a href=\"../accounting/voucher-list.jsp?all=yes")
								.append("&voucherclass_id=")
								.append(accrs.getString("voucherclass_id"))
								.append("&vouchertype_id="
										+ accrs.getString("vouchertype_id")
										+ "\">List "
										+ accrs.getString("vouchertype_name")
										+ "s</a>").append("</li><br>\n");
					}
				}
				// expense
				if (accrs.getString("voucherclass_income").equals("2")) {
					// add link
					accexpenselink
							.append("<li><a href=\"../accounting/"
									+ accrs.getString("voucherclass_file")
									+ "-update.jsp?add=yes")
							.append("&voucherclass_id=")
							.append(accrs.getString("voucherclass_id"));
					accexpenselink.append("&vouchertype_id="
							+ accrs.getString("vouchertype_id") + "\">Add "
							+ accrs.getString("vouchertype_name")
							+ "</a></li>\n");
					// landing, add AND list link
					if (!accrs.getString("vouchertype_id").equals("111")) { // except
																			// bill
						// landing page link
						allaccexpenselink
								.append("<li>")
								.append("<a href=\"../accounting/voucher.jsp?add=yes")
								.append("&param3=")
								.append(accrs.getString("vouchertype_id")
										+ "\">"
										+ accrs.getString("vouchertype_name")
										+ "</a>");
						// add link
						allaccexpenselink
								.append("<li><a href=\"../accounting/"
										+ accrs.getString("voucherclass_file")
										+ "-update.jsp?add=yes")
								.append("&voucherclass_id=")
								.append(accrs.getString("voucherclass_id"))
								.append("&vouchertype_id="
										+ accrs.getString("vouchertype_id")
										+ "\">Add "
										+ accrs.getString("vouchertype_name")
										+ "</a>");
						// list link
						allaccexpenselink
								.append("<li>")
								.append("<a href=\"../accounting/voucher-list.jsp?all=yes")
								.append("&voucherclass_id=")
								.append(accrs.getString("voucherclass_id"))
								.append("&vouchertype_id="
										+ accrs.getString("vouchertype_id")
										+ "\">List "
										+ accrs.getString("vouchertype_name")
										+ "s</a>").append("</li><br>\n");
					}
				}
			}
			accrs.close();
			// / Build Add Link Menu
			accaddlink
					.append(""
							+ "<div style=\"text-align:center\">"
							+ "<li><a href=\"#\" style=\"text-decoration:underline\">Add Voucher...</a>"
							+ "<ul style=\"text-align:left\">" + "<li><a href=\"#\">Income</a>" + "<ul>"
							+ accincomelink + "</ul>" + "</li>"
							+ "<li><a href=\"#\">Expense</a>" + "<ul>"
							+ accexpenselink + "</ul>" + "</li>" + "</ul>"
							+ "</li>" + "</div>" + "");

			// add mode addlink to session
			if (!accincomelink.toString().equals("")) {
				SetSession("accincomelink", "<ul>" + accincomelink.toString()
						+ "</ul>", request);
			}
			if (!accexpenselink.toString().equals("")) {
				SetSession("accexpenselink", "<ul>" + accexpenselink.toString()
						+ "</ul>", request);
			}

			// setting expense land income link to session
			if (!allaccincomelink.toString().equals("")) {
				SetSession("listaccincomelink",
						"<ul>" + allaccincomelink.toString() + "</ul>", request);
			}
			if (!allaccexpenselink.toString().equals("")) {
				SetSession("listaccexpenselink",
						"<ul>" + allaccexpenselink.toString() + "</ul>",
						request);
			}

			// final add link setting to session
			if (!accaddlink.toString().equals("")) {
				SetSession("accaddlink", "" + accaddlink.toString() + "",
						request);
				// SOPError(GetSession("accaddlink", request));
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public String GetSession(String key, HttpServletRequest request) {
		String sessionvalue = "";
		HttpSession session = request.getSession(true);

		Map getMap = (Map) session.getAttribute("sessionMap");
		if (getMap != null) {
			sessionvalue = getMap.get(key) + "";
		}
		if (sessionvalue.equals("null")) {
			sessionvalue = "";
		}
		return sessionvalue;
	}

	public void SetSession(String key, String value, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Map setMap;
		if (session.getAttribute("sessionMap") == null) {
			setMap = new HashMap();
			session.setAttribute("sessionMap", setMap);
		}
		setMap = (Map) session.getAttribute("sessionMap");
		setMap.put(key, value);
	}

	public String ReturnCustomerCurrBalance(String customer_id, String comp_id, String vouchertype_id) {
		String str = "";
		if (!CNumeric(customer_id).equals("0")) {
			String StrSql = "SELECT"
					+ " @currentbal_amount:= COALESCE(customer_curr_bal, 0) AS currentbal_amount,"
					+ " COALESCE (customer_credit_limit, 0) AS customer_credit_limit,"
					+ " customer_type,"
					+ " COALESCE(( SELECT COUNT(voucher_id)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer sub ON sub.customer_id = voucher_customer_id AND sub.customer_type = 1"
					+ " WHERE 1=1"
					+ " AND sub.customer_id = customer_id"
					+ " AND voucher_vouchertype_id = 15 "
					+ " AND vouchertrans_paymode_id > 1"
					+ " ),0) AS cheqreturn,"
					+ " @pdcnotcleared:=COALESCE ("
					+ " (" + " SELECT" + " SUM(voucher_amount)"
					+ " FROM" + " " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer sub ON sub.customer_id = voucher_customer_id"
					+ " AND sub.customer_type = 1"
					+ " WHERE 1 = 1"
					+ " AND sub.customer_id = customer_id"
					+ " AND voucher_vouchertype_id = 9"
					+ " AND vouchertrans_paymode_id = 2"
					+ " AND SUBSTR(vouchertrans_cheque_date,1,8) > SUBSTR(" + ToShortDate(kknow()) + ",1,8)" + " )," + " 0" + " ) AS pdcnotcleared,"
					+ " cast((@currentbal_amount+@pdcnotcleared) AS DECIMAL(15,2)) AS totalbalance"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_id = " + CNumeric(customer_id)
					+ " GROUP BY customer_id" + " ORDER BY customer_id";
			// SOP("StrSql==currbal==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				while (crs.next()) {
					str += "<b>Curr. Bal.: "
							+ IndFormat(""
									+ df.format(crs
											.getDouble("currentbal_amount")))
							+ "</b>";
					str += "<br><b>Total. Bal.: "
							+ IndFormat(""
									+ df.format(crs.getDouble("totalbalance")))
							+ "</b>";
					if (!crs.getString("customer_type").equals("0")) {

						str += "<br><b>Credit Limit: "
								+ IndFormat(""
										+ crs.getDouble("customer_credit_limit"))
								+ "</b>";
					}
					if (vouchertype_id.equals("5")
							|| vouchertype_id.equals("4")
							|| vouchertype_id.equals("3")
							|| vouchertype_id.equals("6")
							|| vouchertype_id.equals("12")
							|| vouchertype_id.equals("10")
							|| vouchertype_id.equals("20")
							|| vouchertype_id.equals("21")) {
						if (crs.getInt("cheqreturn") > 0) {
							str += " <br><font color='red'><b>Cheque Return</b></font>";
						}
						str += " <br><a href='#' name='allinvoice'"
								+ " id='allinvoice' onclick='showlocallinvoice("
								+ customer_id + "," + comp_id
								+ ");'><b>All Invoice</b></a>";
					}
				}
			} catch (Exception ex) {
				SOPError(" AxA Pro===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
		return str;
	}

	public String PopulateExecutives(String emp_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			Str.append("<option value=\"0\">Select Executive</option>\n");
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  "
					+ compdb(comp_id)
					+ "axela_emp"
					+ " WHERE emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop((int) crs.getDouble("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxA Pro===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String ExecutePrepQuery(String StrSql, Map<Integer, Object> qparams, int minutes) {
		// // StrSql = compdb(StrSql);
		ResultSet rsexeq = null;
		String res = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectDB();
			stmt = conn.prepareStatement(StrSql);
			for (Integer key : qparams.keySet()) {
				stmt.setObject(key, qparams.get(key));
			}
			rsexeq = stmt.executeQuery();
			while (rsexeq.next()) {
				res = rsexeq.getString(1);
			}
			return res;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "0";
		} finally {
			try {
				if (rsexeq != null) {
					rsexeq.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ " : " + ex);
			}
		}
		// The connection is returned to the Broker
		//
	}

	public String DisplayVoucherList(CachedRowSet crs, int StartRec, String ordernavi, String ordertype, String comp_id, String vouchertype_id, String emp_id) {

		String voucher_amount = "";
		Map<Integer, Object> prepmap = new HashMap<>();
		StringBuilder Str = new StringBuilder();
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				prepmap.clear();
				if (StartRec != 0) {
					count = StartRec - 1;
				}
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr >\n");
				Str.append("<th data-hide=\"phone,tablet\" data-ignore=\"true\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>")
						.append(GridLink("ID", "voucher_id", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th data-hide=\"phone\">").append(GridLink("No.", "voucher_no", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Voucher", "vouchertype_name", ordernavi, ordertype)).append("</th>\n");
				if (!vouchertype_id.equals("27")) {

					Str.append("<th>").append(GridLink("Ledger", "customer_name", ordernavi, ordertype)).append("</th>\n");
					if (!vouchertype_id.equals("18")) {
						Str.append("<th style=\"width:200px;\" data-hide=\"phone,tablet\">").append(GridLink("Party", "customer_name", ordernavi, ordertype)).append("</th>\n");
					}

					Str.append("<th data-hide=\"phone,tablet\">")
							.append(GridLink("Ref. No.", "voucher_ref_no", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">")
							.append(GridLink("Narration", "voucher_narration", ordernavi, ordertype)).append("</th>\n");
				}

				Str.append("<th data-hide=\"phone,tablet\">")
						.append(GridLink("Date", "voucher_date", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">")
						.append(GridLink("Amount", "voucher_amount", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">")
						.append(GridLink("Executive", "emp_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">")
						.append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead><tbody>\n");
				while (crs.next()) {

					voucher_amount = df.format(crs.getDouble("voucher_amount"));
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("voucher_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("voucher_id") + ");'");
					Str.append(" style='height:100px'>\n");
					Str.append("\n<td>").append(count).append("</td>\n");
					Str.append("<td nowrap>");
					Str.append(crs.getString("voucher_id")).append("");
					Str.append("</td>");
					Str.append("<td>")
							.append("<div onmouseover=\"populatevoucherstatus('")
							.append(crs.getString("vouchertype_id") + "','" + crs.getString("voucher_id") + "'")
							.append(");\" >")
							.append("<a href='../accounting/voucher-list.jsp?voucher_id=" + crs.getString("voucher_id") + "'>");
					if (!crs.getString("voucher_no").equals("0")) {
						if (crs.getString("vouchertype_id").equals("6")) {

							Str.append(crs.getString("branch_invoice_prefix"));
							Str.append(crs.getString("voucher_no"));
							Str.append(crs.getString("branch_invoice_suffix")).append("</a>");

						} else if (crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("branch_receipt_prefix"));
							Str.append(crs.getString("voucher_no"));
							Str.append(crs.getString("branch_receipt_suffix")).append("</a>");
						} else if (crs.getString("vouchertype_id").equals("7")) {
							Str.append(crs.getString("branch_bill_prefix"));
							Str.append(crs.getString("voucher_no"));
							// Str.append(crs.getString("branch_bill_suffix"))
							Str.append("</a>");
						} else {
							Str.append(crs.getString("vouchertype_prefix"));
							Str.append(crs.getString("voucher_no"));
							Str.append(crs.getString("vouchertype_suffix")).append("</a>");

						}
					}
					Str.append("</div>");
					Str.append("<div id=\"followup_").append(crs.getString("voucher_id")).append("\">").append("</div>");
					if (crs.getString("vouchertype_id").equals("9") && !crs.getString("voucher_so_id").equals("0")) {
						Str.append("<br><a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("voucher_so_id")).append("\">SO ID: ");
						Str.append(crs.getString("voucher_so_id")).append("</a><br>");
					}
					Str.append("</td>\n");

					Str.append("<td>");
					if (crs.getString("vouchertype_voucherclass_id").equals("4")) {
						Str.append("<a href='../sales/salesorder-dash.jsp?voucherclass_id=4&vouchertype_id=4&voucher_id="
								+ crs.getString("voucher_id") + "'>").append(crs.getString("vouchertype_name")).append("</a>");
					} else {
						if (crs.getString("vouchertype_id").equals("9")
								|| crs.getString("vouchertype_id").equals("15")) {
							if (crs.getString("paymodeid").equals("1")) {
								Str.append("Cash ");
							} else if (crs.getString("paymodeid").equals("2")) {
								Str.append("Bank ");
							}
						}

						Str.append(crs.getString("vouchertype_name"));
					}
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=\"#ff0000\">[Inactive]</font>");
					}
					if (!crs.getString("voucher_vehstock_id").equals("0")) {
						Str.append("<br>Stock ID:<a href='../inventory/stock-list.jsp?vehstock_id=" + crs.getString("voucher_vehstock_id") + "'>" + crs.getString("voucher_vehstock_id") + "</a>");
					}
					if (!crs.getString("voucher_so_id").equals("0")) {
						Str.append("<br>SO ID:<a href='../sales/veh-salesorder-list.jsp?so_id=" + crs.getString("voucher_so_id") + "'>" + crs.getString("voucher_so_id") + "</a>");
					}
					if (!crs.getString("voucher_jc_id").equals("0")) {
						Str.append("<br>JC ID:<a href='../service/jobcard-list.jsp?jc_id=" + crs.getString("voucher_jc_id") + "'>" + crs.getString("voucher_jc_id") + "</a>");
					}
					if (!crs.getString("voucher_preorder_id").equals("0")) {
						Str.append("<br>Pre-Order ID:<a href='../accounting/voucher-list.jsp?voucherclass_id=27&vouchertype_id=27&voucher_id=" + crs.getString("voucher_preorder_id") + "'>"
								+ crs.getString("voucher_preorder_id") + "</a>");
					}

					Str.append("</td>\n");
					if (!vouchertype_id.equals("27")) {
						Str.append("<td>");
						if (crs.getString("vouchertype_id").equals("9")
								|| crs.getString("vouchertype_id").equals("10")
								|| crs.getString("vouchertype_id").equals("11")
								|| crs.getString("vouchertype_id").equals("15")
								|| crs.getString("vouchertype_id").equals("16")
								|| crs.getString("vouchertype_id").equals("19")) {
							Str.append("<div onmouseover=\"populatefollowup('");
							Str.append(crs.getString("customer_id") + "-" + count + "','" + crs.getString("voucher_id") + "'").append(") \">");
						}

						if (!crs.getString("customer_id").equals("0")
								&& !crs.getString("customer_name").equals("")) {
							Str.append("<a href='../accounting/report-ledgerstatement.jsp?all=yes&ledger=" + crs.getString("customer_id") + "&txt_startdate=" + crs.getString("customer_entry_date")
									+ "' targer='_blank'>"
									+ crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")</a><br>");
						}
						// Str.append("<a href=\"javascript:remote=window.open('../sales/enquiry-dash.jsp?customer_id=")
						// .append(crs.getString("customer_id")).append("','opprdash','');remote.focus();\">");
						// Str.append("Follow-up=>");
						// Str.append("</a>");
						Str.append("</div>");
						Str.append("<div id=\"followup_").append(crs.getString("customer_id") + "-" + count).append("\">").append("</div>");
						// if (customer_credit_limit > 0) {
						// Str.append("<br>Credit Limit : " + IndFormat("" + customer_credit_limit));
						// }
						if (vouchertype_id.equals("18")) {
							String strsql = "SELECT vouchertrans_customer_id,customer_id , customer_name"
									+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = vouchertrans_customer_id"
									+ " WHERE"
									+ " voucher_id = " + crs.getString("voucher_id")
									+ " LIMIT 1";
							CachedRowSet crs1 = processQuery(strsql, 0);
							while (crs1.next()) {
								// Str.append("<div><a href=../customer/customer-list.jsp?customer_id=" + crs1.getString("customer_id") + ">" + crs1.getString("customer_name") + "</a></div>");
								Str.append("<div>" + crs1.getString("customer_name") + "</div>");
							}
						}
						Str.append("</td>");

						if (!vouchertype_id.equals("18")) {
							Str.append("<td>");
							Str.append(DisplayCustomer(
									crs.getString("voucher_id"),
									crs.getString("voucher_customer_id"),
									crs.getString("customer_name"),
									crs.getString("voucher_contact_id"),
									crs.getString("title_desc"),
									crs.getString("contact_fname"),
									crs.getString("contact_lname"),
									crs.getString("contact_mobile1"),
									crs.getString("contact_mobile2"),
									crs.getString("contact_phone1"),
									crs.getString("contact_phone2"),
									crs.getString("contact_email1"),
									crs.getString("contact_email2"), comp_id));
							Str.append("</td>\n");
						}
						Str.append("<td>");
						Str.append(crs.getString("voucher_ref_no"));
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append(crs.getString("voucher_narration"));
						Str.append("</td>\n");
					}
					Str.append("<td>");
					Str.append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n");

					Str.append("<td align=\"right\">");
					Str.append(voucher_amount);
					Str.append("</td>\n");

					if (!crs.getString("emp_id").equals("0")) {
						Str.append("<td>");
						if (!crs.getString("emp_id").equals("")) {
							Str.append(ExePhotoOval(crs.getString("emp_photo"),
									crs.getString("emp_sex"), "50"));
							Str.append(
									"<a href=\"../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("emp_id"))
									.append("\">");
							Str.append(crs.getString("emp_name")).append("</a>");
						}
						Str.append("</td>\n");
					}

					Str.append("<td><a href='../portal/branch-summary.jsp?branch_id="
							+ crs.getString("voucher_branch_id") + "'>");
					if (!crs.getString("voucher_branch_id").equals("0")) {
						Str.append(crs.getString("branch_name")).append("</a>");
					}
					Str.append("</td>\n");
					// // actions column
					Str.append("<td nowrap>");
					if (crs.getString("vouchertype_id").equals("3")) {
						Str.append(
								"<a href=\"../inventory/" + crs.getString("voucherclass_file") + "-update.jsp?update=yes&voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=")
								.append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(
								crs.getString("vouchertype_id"));
						Str.append("\">Update "
								+ crs.getString("vouchertype_name") + "</a>");
					} else if (crs.getString("vouchertype_id").equals("1")
							|| crs.getString("vouchertype_id").equals("2")) {
						Str.append(
								"<a href=\"../accounting/po-update.jsp?update=yes&voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=")
								.append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(
								crs.getString("vouchertype_id"));
						Str.append("\">Update "
								+ crs.getString("vouchertype_name") + "</a>");
					} else {
						if (crs.getString("vouchertype_id").equals("20") || crs.getString("vouchertype_id").equals("24") || crs.getString("vouchertype_id").equals("12")
								|| crs.getString("vouchertype_id").equals("21") || crs.getString("vouchertype_id").equals("7") || crs.getString("vouchertype_id").equals("6")) {

							// Str.append(
							// "<a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update.jsp?update=yes&voucher_id=")
							// .append(crs.getString("voucher_id"))
							// .append("&voucherclass_id=")
							// .append(crs.getString("voucherclass_id"));
							// Str.append("&vouchertype_id=").append(
							// crs.getString("vouchertype_id"));
							// Str.append("\">Update "
							// + crs.getString("vouchertype_name") + "</a>");

							Str.append("<a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update2.jsp?update=yes&voucher_id=").append(crs.getString("voucher_id"));
							Str.append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
							Str.append("\">Update " + crs.getString("vouchertype_name") + "</a>");

						} else if (crs.getString("vouchertype_id").equals("6")) {
							Str.append(
									"<a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update.jsp?update=yes&voucher_id=")
									.append(crs.getString("voucher_id"))
									.append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id"));
							Str.append("\">Update "
									+ crs.getString("vouchertype_name") + "</a>");

						} else {
							Str.append("<a href=\"../accounting/"
									+ crs.getString("voucherclass_file")
									+ "-update.jsp?update=yes&voucher_id=")
									.append(crs.getString("voucher_id"))
									.append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
							Str.append("\">Update " + crs.getString("vouchertype_name") + "</a>");
						}
					}
					if (crs.getString("vouchertype_id").equals("21") && crs.getString("voucher_grn_id").equals("0")) {
						Str.append(
								"<br/><a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update2.jsp?add=yes&voucher_purchaseinvoice_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=20")
								.append("&vouchertype_id=20");
						Str.append("\">Add GRN</a>");

					}

					if (crs.getString("vouchertype_authorize").equals("1") || crs.getString("vouchertype_defaultauthorize").equals("1")) {

						Str.append("<br/><a href=\"../accounting/" + "voucher" + "-authorize.jsp?voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
						Str.append("\">Authorize</a>");

					}

					// quote
					if (crs.getString("vouchertype_id").equals("5")) {
						Str.append("<br/><a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update.jsp");
						Str.append("?add=yes&voucher_quote_id=").append(crs.getString("voucher_id"));
						Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
						Str.append("\">Add Sales Invoice</a>");

						Str.append("<br/><a href=\"../accounting/voucher-list.jsp?voucher_quote_id=").append(crs.getString("voucher_id"));
						Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
						Str.append("\">List Sales Invoice</a>");
					}

					// Pre-Order
					if (crs.getString("vouchertype_id").equals("27")) {
						Str.append("<br/><a href=\"../accounting/" + crs.getString("voucherclass_file") + "-update.jsp");
						Str.append("?add=yes&checkinvoice=yes&voucher_preorder_id=").append(crs.getString("voucher_id"));
						Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
						if (!crs.getString("voucher_so_id").equals("0")) {
							Str.append("&preorder_so_id=" + crs.getString("voucher_so_id"));
						}
						Str.append("\">Add Sales Invoice</a>");

						Str.append("<br/><a href=\"../accounting/voucher-list.jsp?voucher_preorder_id=").append(crs.getString("voucher_id"));
						Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
						Str.append("\">List Sales Invoice</a>");
					}

					// if (crs.getString("vouchertype_id").equals("5")) {
					// Str.append(
					// "<br/><a href=\"../accounting/"
					// + crs.getString("voucherclass_file")
					// + "-update.jsp?add=yes&acc=yes&voucher_quote_id=")
					// .append(crs.getString("voucher_id"))
					// .append("&voucherclass_id=4");
					// Str.append("&vouchertype_id=4");
					// Str.append("\">Add Sales Order</a>");
					// Str.append(
					// "<br/><a href=\"../accounting/voucher-list.jsp?voucher_quote_id=")
					// .append(crs.getString("voucher_id"));
					// Str.append("&voucherclass_id=4").append(
					// "&vouchertype_id=4");
					// Str.append("\">List Sales Order</a>");
					// }
					// GIT
					if (crs.getString("vouchertype_id").equals("10")) {
						Str.append("<br/><a href=\"../accounting/"
								+ crs.getString("voucherclass_file")
								+ "-update.jsp?add=yes&voucher_git_id=");
						Str.append(crs.getString("voucher_id")).append(
								"&voucherclass_id=1");
						Str.append("&vouchertype_id=1");
						Str.append("\">Add GRN</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_git_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=1")
								.append("&vouchertype_id=1");
						Str.append("\">List GRNs</a>");
					}

					// grn
					if (crs.getString("vouchertype_id").equals("20")) {
						// purchase invoice
						Str.append(
								"<br/><a href=\"../accounting/"
										+ crs.getString("voucherclass_file")
										+ "-update2.jsp?add=yes")
								.append("&voucherclass_id=21")
								.append("&vouchertype_id=21&voucher_grn_id=")
								.append(crs.getString("voucher_id"));
						Str.append("\">Add Purchase Invoice</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_grn_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=21")
								.append("&vouchertype_id=21");
						Str.append("\">List Purchase Invoices</a>");

						// purchase return
						Str.append(
								"<br/><a href=\"../accounting/returns-update.jsp?add=yes&voucher_grn_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=24");
						Str.append("&vouchertype_id=24");
						Str.append("\">Add Purchase Return</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_grn_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=24");
						Str.append("&vouchertype_id=24");
						Str.append("\">List Purchase Returns</a>");
					}

					if (crs.getString("vouchertype_id").equals("4")) {
						Str.append(
								"<br/><a href=\"../accounting/"
										+ crs.getString("voucherclass_file")
										+ "-update.jsp?add=yes&acc=yes&voucher_so_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=25")
								.append("&vouchertype_id=25");
						Str.append("\">Add Delivery Note</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_so_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=25")
								.append("&vouchertype_id=25");
						Str.append("\">List Delivery Notes</a>");

					}

					// if (crs.getString("vouchertype_id").equals("25")) {
					// // Sales Invoice
					// Str.append(
					// "<br/><a href=\"../accounting/"
					// + crs.getString("voucherclass_file")
					// + "-update.jsp?add=yes&voucher_delnote_id=")
					// .append(crs.getString("voucher_id"))
					// .append("&voucherclass_id=6")
					// .append("&vouchertype_id=6");
					// Str.append("\">Add Sales Invoice</a>");
					// Str.append(
					// "<br/><a href=\"../accounting/voucher-list.jsp?voucher_delnote_id=")
					// .append(crs.getString("voucher_id"))
					// .append("&voucherclass_id=6")
					// .append("&vouchertype_id=6");
					// Str.append("\">List Sales Invoices</a>");
					//
					// }
					// Invoice
					if (crs.getString("vouchertype_id").equals("6")) {
						Str.append("<br/><a href=\"../accounting/receipt-update.jsp?add=yes&ledger=")
								.append(crs.getString("voucher_customer_id"))
								.append("&voucherclass_id=9")
								.append("&vouchertype_id=9")
								.append("&voucher_invoice_id=")
								.append(crs.getString("voucher_id"));
						if (!crs.getString("voucher_so_id").equals("0")) {
							Str.append("&voucher_so_id=").append(crs.getString("voucher_so_id"));
						} else if (!crs.getString("voucher_jc_id").equals("0")) {
							Str.append("&voucher_jc_id=").append(crs.getString("voucher_jc_id"));
						}
						Str.append("\">Add Receipt</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_invoice_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=9")
								.append("&vouchertype_id=9");
						Str.append("\">List Receipts</a>");

						// Str.append(
						// "<br/><a href=\"../accounting/advance-receipt-update.jsp?add=yes&ledger=")
						// .append(crs.getString("voucher_customer_id"))
						// .append("&voucherclass_id=28")
						// .append("&vouchertype_id=28")
						// .append("&voucher_invoice_id=")
						// .append(crs.getString("voucher_id"));
						// if (!crs.getString("voucher_so_id").equals("0")) {
						// Str.append("&voucher_so_id=").append(crs.getString("voucher_so_id"));
						// } else if (!crs.getString("voucher_jc_id").equals("0")) {
						// Str.append("&voucher_jc_id=").append(crs.getString("voucher_jc_id"));
						// }
						// Str.append("\">Add Advance Receipt</a>");
						// Str.append(
						// "<br/><a href=\"../accounting/voucher-list.jsp?voucher_invoice_id=")
						// .append(crs.getString("voucher_id"))
						// .append("&voucherclass_id=28")
						// .append("&vouchertype_id=28");
						// Str.append("\">List Advance Receipts</a>");
					}

					if (crs.getString("vouchertype_id").equals("25")) {
						// user can not add "Delivery Note" for "Sales invoice"
						// if there is "Sales order" for that "Sales invoice"
						Str.append(
								"<br/><a href=\"../accounting/returns-update.jsp?add=yes&voucher_delnote_id=")
								.append(crs.getString("voucher_id")).append("&voucherclass_id=23")
								.append("&vouchertype_id=23")
								.append("&vouchertrans_customer_id=")
								.append(crs.getString("voucher_customer_id"))
								.append("&span_cont_id=")
								.append(crs.getString("voucher_contact_id"))
								.append("&voucher_rateclass_id=")
								.append(crs.getString("voucher_rateclass_id"))
								.append("&branch_id=")
								.append(crs.getString("voucher_branch_id"))
								.append("\">Add Sales Return</a>");

						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_delnote_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=23")
								.append("&vouchertype_id=23");
						Str.append("\">List Sales Return</a>");
					}

					// Delivery Note
					if (crs.getString("vouchertype_id").equals("6")) {
						// user can not add "Delivery Note" for "Sales invoice"
						// if there is "Sales order" for that "Sales invoice"
						if (crs.getString("voucher_so_id").equals("0")) {
							Str.append(
									"<br/><a href=\"../accounting/so-update.jsp?add=yes&voucher_invoice_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=25")
									.append("&vouchertype_id=25")
									.append("&vouchertrans_customer_id=")
									.append(crs.getString("voucher_customer_id"))
									.append("&span_cont_id=")
									.append(crs.getString("voucher_contact_id"))
									.append("&voucher_rateclass_id=")
									.append(crs.getString("voucher_rateclass_id"))
									.append("&branch_id=")
									.append(crs.getString("voucher_branch_id"))
									.append("\">Add Delivery Note</a>");

							Str.append(
									"<br/><a href=\"../accounting/voucher-list.jsp?voucher_invoice_id=")
									.append(crs.getString("voucher_id"))
									.append("&voucherclass_id=25")
									.append("&vouchertype_id=25");
							Str.append("\">List Delivery Note</a>");

						}

					}

					// DCR Request
					if (crs.getString("vouchertype_id").equals("12")) {
						Str.append(
								"<br/><a href=\"../accounting/"
										+ crs.getString("voucherclass_file")
										+ "-update2.jsp?add=yes&voucher_po_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=20")
								.append("&vouchertype_id=20");
						Str.append("\">Add GRN</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_po_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=20")
								.append("&vouchertype_id=20");
						Str.append("\">List GRN</a>");
					}
					// DCR
					// if (crs.getString("vouchertype_id").equals("4")) {
					// Str.append(
					// "<br/><a href=\"../accounting/"
					// + crs.getString("voucherclass_file")
					// + "-update.jsp?add=yes&voucher_dcr_id=")
					// .append(crs.getString("voucher_id"))
					// .append("&voucherclass_id=116")
					// .append("&vouchertype_id=116");
					// Str.append("\">Add Sales Return</a>");
					// Str.append(
					// "<br/><a href=\"../accounting/voucher-list.jsp?voucher_dcr_id=")
					// .append(crs.getString("voucher_id"))
					// .append("&voucherclass_id=116")
					// .append("&vouchertype_id=116");
					// Str.append("\">List Sales Returns</a>");
					// }

					// Sales Order
					if (crs.getString("vouchertype_id").equals("4")) {
						Str.append("<br/><a href=\"../accounting/" + crs.getString("voucherclass_file")
								+ "-update.jsp?add=yes&acc=yes&voucher_so_id=").append(crs.getString("voucher_id"))
								.append("&voucherclass_id=6");
						Str.append("&vouchertype_id=6");
						Str.append("\">Add Sales Invoice</a>");
						Str.append("<br/><a href=\"../accounting/voucher-list.jsp?voucher_so_id=").append(crs.getString("voucher_id"));
						Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
						Str.append("\">List Sales Invoice</a>");
					}

					// grn return
					if (crs.getString("vouchertype_id").equals("2")) {
						Str.append(
								"<br/><a href=\"../accounting/"
										+ crs.getString("voucherclass_file")
										+ "-update.jsp?add=yes&voucher_grn_return_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=117")
								.append("&vouchertype_id=117");
						Str.append("\">Add Purchase Return</a>");
						Str.append(
								"<br/><a href=\"../accounting/voucher-list.jsp?voucher_grn_return_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=117")
								.append("&vouchertype_id=117");
						Str.append("\">List Purchase Return</a>");
					}
					if (crs.getString("vouchertype_email_enable").equals("1")) {
						Str.append(
								"</br><a href=\"" + "voucher"
										+ "-email.jsp?update=yes&voucher_id=")
								.append(crs.getString("voucher_id"));
						Str.append("\">Send " + "Email" + "</a>");
					}
					if (crs.getString("vouchertype_sms_enable").equals("1")) {
						Str.append(
								"</br><a href=\"" + "voucher"
										+ "-sms.jsp?update=yes&voucher_id=")
								.append(crs.getString("voucher_id"));
						Str.append("\">Send " + "SMS" + "</a>");
					}
					if (crs.getString("vouchertype_docs").equals("1")) {
						Str.append(
								"</br><a href=\""
										+ "voucher"
										+ "-doc-list.jsp?update=yes&voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&vouchertype_id=")
								.append(crs.getString("vouchertype_id"));
						Str.append("\">List " + "Documents" + "</a>");
					}

					Str.append("<br><a href='../accounting/report-ledgerstatement.jsp?all=yes&voucher_id=").append(crs.getString("voucher_id"))
							.append("&txt_startdate=").append(strToShortDate(crs.getString("voucher_date")))
							.append("&voucherclass_id=").append(crs.getString("voucherclass_id"))
							.append("&voucherclass_id=").append(crs.getString("voucherclass_id"))
							.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("' target='_blank'>Ledger Statement</a>");

					String print = "yes";

					if ((crs.getString("vouchertype_authorize").equals("1")
							|| crs.getString("vouchertype_defaultauthorize").equals("1"))
							&& CNumeric(crs.getString("voucher_authorize")).equals("0")) {
						print = "";
					}
					if (print.equals("yes")) {
						if (crs.getString("vouchertype_id").equals("9")) {

							Str.append("</br><a target='_blank' href=\"");

							// Go to different report page for "One Triumph" branch "onetriumph-so-print.jsp"
							if (crs.getString("branch_name").toLowerCase().contains("one triumph")
									|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
								Str.append("onetriumph-");
							}

							if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-ford" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-hyundai" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-honda" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-yamaha" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-volvo" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else {
								Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							}

							// Go to different report page for "One Triumph" branch
							if (crs.getString("branch_name").toLowerCase().contains("one triumph")
									|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
								Str.append("onetriumph-");
							}
							// SOP("brand_id==" + crs.getString("branch_brand_id"));
							//

							if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-ford")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {

								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-honda")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {

								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-hyundai")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-yamaha")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-volvo")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							}
							else {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							}
						} else if (crs.getString("vouchertype_id").equals("6")) {

							if (Double.parseDouble(CNumeric(crs.getString("voucher_date"))) <= Double.parseDouble("20170630000000")) {
								Str.append("</br><a target='_blank' href=\"");
								// Go to different report page for "One Triumph" branch "onetriumph-receipt-print.jsp"
								if (crs.getString("branch_name").toLowerCase().contains("one triumph")
										|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
									Str.append("onetriumph-");
								}
								if (crs.getString("branch_brand_id").equals("2") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-maruthi-suzuki" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-honda" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-hyundai" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-ford" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-yamaha" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else if (crs.getString("branch_brand_id").equals("101") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(crs.getString("voucherclass_file") + "-print" + "-suzuki" + ".jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								} else {
									Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
											.append(crs.getString("voucher_id")).append("&voucherclass_id=")
											.append(crs.getString("voucherclass_id"));
									Str.append("&vouchertype_id=").append(
											crs.getString("vouchertype_id")).append("&dr_report=");
								}

								// Go to different report page for "One Triumph" branch
								if (crs.getString("branch_name").toLowerCase().contains("one triumph")
										|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
									Str.append("onetriumph-");
								}

								if (crs.getString("branch_brand_id").equals("2") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-maruthi-suzuki")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-honda")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-hyundai")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-ford")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-yamaha")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else if (crs.getString("branch_brand_id").equals("101") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-suzuki")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								} else {
									Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
											.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
											.append("&brand_id=").append(crs.getString("branch_brand_id"));
									Str.append("\">Print " + crs.getString("vouchertype_name")
											+ "</a>");
								}

							} else {
								Str.append("</br><a target='_blank' href=\"");
								Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
								// }
								// Gate Pass Print For Hyundai
								// if (crs.getString("vouchertype_id").equals("6") && crs.getString("branch_brand_id").equals("6")) {
								// Str.append("<br><a href=\"delivery-receipt-print.jsp?voucher_id=").append(crs.getString("voucher_id")).append(" \">Delivery Receipt Print</a>");
								// }
							}
						} else {
							// if (!crs.getString("vouchertype_id").equals("27")) {
							// Str.append("</br><a target='_blank' href=\"" + crs.getString("voucherclass_file") + "-print.jsp?voucher_id=");
							// Str.append(crs.getString("voucher_id")).append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
							// Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("&dr_report=");
							// Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print");
							// Str.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
							// Str.append("\">Print " + crs.getString("vouchertype_name") + "</a>");
							// }

							if (crs.getString("vouchertype_id").equals("7")) {
								Str.append("</br><a target='_blank' href=\"so-print.jsp?voucher_id=");
								Str.append(crs.getString("voucher_id")).append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("&dr_report=");
								Str.append("salesinvoice-print");
								Str.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
								Str.append("\">Print " + crs.getString("vouchertype_name") + "</a>");
							} else if (!crs.getString("vouchertype_id").equals("27")) {
								Str.append("</br><a target='_blank' href=\"" + crs.getString("voucherclass_file") + "-print.jsp?voucher_id=");
								Str.append(crs.getString("voucher_id")).append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("&dr_report=");
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print");
								Str.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
								Str.append("\">Print " + crs.getString("vouchertype_name") + "</a>");
							}

						}
						// else if (!crs.getString("vouchertype_id").equals("9")) {
						// Str.append(
						// "</br><a target='_blank' href=\""
						// + crs.getString("voucherclass_file")
						//
						// + "-print.jsp?voucher_id=")
						// .append(crs.getString("voucher_id"))
						// .append("&voucherclass_id=")
						// .append(crs.getString("voucherclass_id"));
						// Str.append("&vouchertype_id=").append(
						// crs.getString("vouchertype_id")).append("&dr_report=")
						// .append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", ""))
						// .append("-print")
						// .append("&dr_format=").append("pdf");
						// Str.append("\">Print " + crs.getString("vouchertype_name")
						// + "</a>");
						// }
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
			} else {
				if (StartRec == 0) {
					Str.append("<br><br><br><br><font color=red><b>No Voucher(s) Found!</b></font><br><br>");
				}
			}
			crs.close();
			Str.append("</tbody></table></div>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();

	}
	public String ExePhotoOval(String file, String gender, String width) {
		String img = "";
		String widthstr = "";
		if (!width.equals("")) {
			widthstr = "&width=" + width;
		}
		if (!file.equals("")) {
			// img = "<img src=\"../Thumbnail.do?empphoto=" + photo + width +
			// "&target=" + Math.random() + "\">";
			img = "../Thumbnail.do?empphoto=" + file + widthstr + "";
		} else {
			if (gender.equals("2")) {
				img = "../Thumbnail.do?femaleimg=yes" + widthstr + "";
			} else {
				img = "../Thumbnail.do?maleimg=yes" + widthstr + "";
			}
		}
		return "<div class=imground" + width + " style=background-image:url('"
				+ img + "');></div>";
	}

	public String DisplayCustomer(String voucher_id, String customer_id, String customer_name,
			String contact_id, String title, String contact_fname,
			String contact_lname, String contact_mobile1,
			String contact_mobile2, String contact_phone1,
			String contact_phone2, String contact_email1, String contact_email2, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<a href='../customer/customer-list.jsp?customer_id="
				+ customer_id + "'>");
		if (!customer_id.equals("0")) {
			Str.append(customer_name).append(" (" + customer_id + ")")
					.append("</a>").append("<br>");
		}

		if (!contact_id.equals("0")) {
			Str.append("<a href='../customer/customer-contact-list.jsp?contact_id="
					+ contact_id + "'>");
			Str.append(title + " " + contact_fname + contact_lname).append(
					"</a>");

			if (!contact_mobile1.equals("")) {
				Str.append("<br>").append(SplitPhoneNoSpan(contact_mobile1, 10, "M", voucher_id)).append(ClickToCall(contact_mobile1, comp_id));
			}
			if (!contact_mobile2.equals("")) {
				Str.append("<br>").append(SplitPhoneNoSpan(contact_mobile2, 10, "M", voucher_id)).append(ClickToCall(contact_mobile2, comp_id));
			}
			if (!contact_phone1.equals("")) {
				Str.append("<br>").append(SplitPhoneNoSpan(contact_phone1, 10, "P", voucher_id)).append(ClickToCall(contact_phone1, comp_id));
			}
			if (!contact_phone2.equals("")) {
				Str.append("<br>").append(SplitPhoneNoSpan(contact_phone2, 10, "P", voucher_id)).append(ClickToCall(contact_phone2, comp_id));
			}
			if (!contact_email1.equals("")) {
				Str.append("<br><span class='customer_info customer_" + voucher_id + "'  style='display: none;'><a href=\"mailto:")
						.append(contact_email1).append("\">");
				Str.append(contact_email1).append("</a></span>");
			}
			if (!contact_email2.equals("")) {
				Str.append("<br><span class='customer_info customer_" + voucher_id + "'  style='display: none;'><a href=\"mailto:")
						.append(contact_email2).append("\">");
				Str.append(contact_email2).append("</a></span>");
			}
		}
		return Str.toString();
	}
	public String ReturnBranchids(String principalid, String comp_id) {
		StringBuilder sb = new StringBuilder();
		try {
			if (!principalid.equals("")) {
				String StrSql = "SELECT branch_id " + " FROM "
						+ compdb(comp_id) + "axela_branch"
						+ " WHERE branch_brand_id in (" + principalid + ")"
						+ " AND branch_active = 1" + " ORDER BY branch_id ";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					sb.append(crs.getString("branch_id"));
					sb.append(",");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		if (!sb.toString().equals("")) {
			return sb.toString().substring(0, sb.toString().lastIndexOf(","));
		} else {
			return "";
		}
	}

	public String getActiveBranchID(HttpServletRequest request, String emp_id) {
		String branch_id = "0";
		String comp_id = CNumeric(GetSession("comp_id", request) + "");
		// if (emp_id.equals("1")) {
		branch_id = ExecuteQuery("SELECT branch_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_active = '1'"
				+ " LIMIT 1");
		// }
		// else if (!emp_id.equals("1")) {
		// branch_id = ExecuteQuery("SELECT emp_branch_id FROM "
		// + "axela_emp_branch" + " INNER JOIN "
		// + "axela_branch ON branch_id = emp_branch_id"
		// + " WHERE branch_active = '1'"
		// + " AND branch_company_id = "+company_id
		// + " AND emp_id = " + emp_id
		// + "" + " LIMIT 1");
		// // SOPError("branch_id = " + branch_id);
		// }
		return CNumeric(branch_id);
	}

	public void CopyVoucherTransToCartCheck(HttpServletRequest request,
			String emp_id, String cart_session_id, String voucher_id,
			String voucher_vouchertype_id, String comp_id) throws SQLException {
		String msg = "";
		// SOP("voucher_vouchertype_id===" + voucher_vouchertype_id);
		// SOP("cart_session_id===" + cart_session_id);
		String StrSql = "";
		String vouchertrans_voucher_id = "0";
		String vouchertrans_multivoucher_id = "0";
		String vouchertrans_customer_id = "0";
		String vouchertrans_location_id = "0";
		String vouchertrans_item_id = "0";
		String vouchertrans_discount = "0.00", vouchertrans_discount_perc = "0.00";
		String vouchertrans_tax = "0.00";
		String vouchertrans_rowcount = "0";
		String vouchertrans_option_id = "0";
		String vouchertrans_option_group = "";
		String vouchertrans_item_batch_id = "0";
		String vouchertrans_item_serial = "", vouchertrans_tax_id = "0";
		double vouchertrans_qty = 0.00;
		double vouchertrans_truckspace = 0.00;
		double vouchertrans_price = 0.00;
		double vouchertrans_netprice = 0.00;
		double vouchertrans_unit_cost = 0.00;
		double vouchertrans_amount = 0.00;
		double vouchertrans_discountamount = 0.00;
		double vouchertrans_taxamount = 0.00;
		String vouchertrans_supplier_code = "";
		double vouchertrans_alt_qty = 0.00;
		String vouchertrans_alt_uom_id = "0";
		String vouchertrans_delivery_date = "";
		String vouchertrans_dc = "", vouchertrans_convfactor = "1";
		String uom_ratio = "0";
		String uom_parent_id = "0";
		double mul_ratio = 0.00;
		double cart_uom_ratio = 0.00;
		String ratio = "0.00";
		Connection conntx = null;
		Statement stmttx = null;
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND SUBSTR(cart_time,1,8) < SUBSTR(" + ToShortDate(kknow()) + ",1,8)";
			stmttx.addBatch(StrSql);

			StrSql = "SELECT vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_option_group,"
					+ " vouchertrans_item_batch_id,"
					+ " vouchertrans_item_serial,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_discountamount,"
					+ " vouchertrans_taxamount,"
					+ " vouchertrans_supplier_code,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " COALESCE(@uom_ratio := (SELECT uom_ratio"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE uom_id = vouchertrans_alt_uom_id), 0) AS uom_ratio,"
					+ " COALESCE(@uom_parent_id := (SELECT uom_parent_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE uom_id = vouchertrans_alt_uom_id), 0) AS uom_parent_id,"
					+ " vouchertrans_delivery_date,"
					+ " " + ToLongDate(kknow()) + ","
					+ " vouchertrans_dc"
					+ " FROM " + " " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " WHERE 1=1 "
					+ " AND (vouchertrans_item_id !=0 || (vouchertrans_tax_id !=0 && vouchertrans_item_id = 0))"
					+ " AND vouchertrans_voucher_id = " + voucher_id + "";
			// SOP("StrSql==vouchertrans123==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				vouchertrans_multivoucher_id = crs1.getString("vouchertrans_multivoucher_id");
				vouchertrans_customer_id = crs1.getString("vouchertrans_customer_id");
				vouchertrans_location_id = crs1.getString("vouchertrans_location_id");
				vouchertrans_item_id = crs1.getString("vouchertrans_item_id");
				vouchertrans_discount = crs1.getString("vouchertrans_discount");
				vouchertrans_discount_perc = crs1.getString("vouchertrans_discount_perc");
				vouchertrans_tax = crs1.getString("vouchertrans_tax");
				vouchertrans_tax_id = crs1.getString("vouchertrans_tax_id");
				vouchertrans_rowcount = crs1.getString("vouchertrans_rowcount");
				vouchertrans_option_id = crs1.getString("vouchertrans_option_id");
				vouchertrans_option_group = crs1.getString("vouchertrans_option_group");
				vouchertrans_item_batch_id = crs1.getString("vouchertrans_item_batch_id");
				vouchertrans_item_serial = crs1.getString("vouchertrans_item_serial");
				vouchertrans_qty = crs1.getDouble("vouchertrans_qty");
				vouchertrans_truckspace = crs1.getDouble("vouchertrans_truckspace");
				vouchertrans_price = crs1.getDouble("vouchertrans_price");
				vouchertrans_netprice = crs1.getDouble("vouchertrans_netprice");
				vouchertrans_unit_cost = crs1.getDouble("vouchertrans_unit_cost");
				vouchertrans_amount = crs1.getDouble("vouchertrans_amount");
				vouchertrans_discountamount = crs1.getDouble("vouchertrans_discountamount");
				vouchertrans_taxamount = crs1.getDouble("vouchertrans_taxamount");
				vouchertrans_supplier_code = crs1.getString("vouchertrans_supplier_code");
				vouchertrans_alt_qty = crs1.getDouble("vouchertrans_alt_qty");
				vouchertrans_alt_uom_id = crs1.getString("vouchertrans_alt_uom_id");
				uom_ratio = crs1.getString("uom_ratio");
				uom_parent_id = crs1.getString("uom_parent_id");
				vouchertrans_delivery_date = crs1.getString("vouchertrans_delivery_date");
				vouchertrans_dc = crs1.getString("vouchertrans_dc");

				if (!vouchertrans_rowcount.equals("0") && vouchertrans_option_id.equals("0")) {
					if (!uom_ratio.equals("")) {
						cart_uom_ratio = Double.parseDouble(uom_ratio);
					}
					mul_ratio = 0.00;
					StrSql = "SELECT"
							+ " COALESCE(@ratio:=(IF (vouchertrans_discount = 0 && vouchertrans_tax = 0,"
							+ " (COALESCE (" + vouchertrans_alt_qty + ",0)"// From voucher qty
							+ " -";
					// purchase...
					// Uom_ratio or uom_parent_id we are taking only for
					// purchase side transaction...
					// we are converting eg Boxes(uom_id) To Pcs(uom_parent_id)
					// Eg: box*10(1Box=10Pcs) If 10 Box*10 means 10Boxes =
					// 100pcs
					// while taking mul_ratio divide (To voucher Qty) by
					// (uom_ratio)
					if (voucher_vouchertype_id.equals("20")) { // TO voucher GRN qty
						// SOP("voucher_vouchertype_id=grn=="+voucher_vouchertype_id);
						StrSql += " (COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher git ON git.voucher_id = vouchertrans_voucher_id"
								+ " WHERE git.voucher_po_id  = " + voucher_id
								+ " AND vouchertrans_item_id = " + vouchertrans_item_id
								+ " AND vouchertrans_rowcount = " + vouchertrans_rowcount
								+ " AND vouchertrans_option_id = " + vouchertrans_option_id
								+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id
								+ " ),0)/ (" + cart_uom_ratio + ") )";

					} else if (voucher_vouchertype_id.equals("21")) { // To purchase invoice QTY.
						// SOP("voucher_vouchertype_id=git==" // + // voucher_vouchertype_id);
						StrSql += " (COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher grn ON grn.voucher_id = vouchertrans_voucher_id"
								+ " WHERE grn.voucher_grn_id  = " + voucher_id
								+ " AND vouchertrans_item_id = " + vouchertrans_item_id
								+ " AND vouchertrans_rowcount = " + vouchertrans_rowcount
								+ " AND vouchertrans_option_id = " + vouchertrans_option_id
								+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id
								+ " ),0) / (" + cart_uom_ratio + "))";

					} else if (voucher_vouchertype_id.equals("24")) { // TO voucher Purchase Return qty
						// SOP("voucher_vouchertype_id=git=="+voucher_vouchertype_id);
						StrSql += " (COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher grn_return ON grn_return.voucher_id = vouchertrans_voucher_id"
								+ " WHERE grn_return.voucher_grn_return_id  = " + voucher_id
								+ " AND vouchertrans_item_id = " + vouchertrans_item_id
								+ " AND vouchertrans_rowcount = " + vouchertrans_rowcount
								+ " AND vouchertrans_option_id = " + vouchertrans_option_id
								+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id
								+ " ),0) / (" + cart_uom_ratio + "))";

					} else if (voucher_vouchertype_id.equals("6")) { // To sales invoice Qty.
						// SOP("voucher_vouchertype_id=grn=="+voucher_vouchertype_id);
						StrSql += " COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher delnote ON delnote.voucher_id = vouchertrans_voucher_id"
								+ " WHERE delnote.voucher_delnote_id  = " + voucher_id
								+ " AND vouchertrans_item_id = " + vouchertrans_item_id
								+ " AND vouchertrans_rowcount = " + vouchertrans_rowcount
								+ " AND vouchertrans_option_id = " + vouchertrans_option_id
								+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id + "" + " ),0)";

					} else if (voucher_vouchertype_id.equals("23")) { // To Voucher SR qty.
						// SOP("voucher_vouchertype_id=grn=="+voucher_vouchertype_id);
						StrSql += " COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher dcr ON dcr.voucher_id = vouchertrans_voucher_id"
								+ " WHERE dcr.voucher_dcr_id  = " + voucher_id
								+ " AND vouchertrans_item_id = " + vouchertrans_item_id
								+ " AND vouchertrans_rowcount = " + vouchertrans_rowcount
								+ " AND vouchertrans_option_id = " + vouchertrans_option_id
								+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id + "" + " ), 0)";// TO
						// voucher
						// qty
					}
					StrSql += " ) ,vouchertrans_alt_qty)),0)"
							+ " /"
							+ " COALESCE((IF (vouchertrans_discount = 0 && vouchertrans_tax = 0,"
							+ " COALESCE (" + vouchertrans_alt_qty + ",0)" + " , vouchertrans_alt_qty)), 0.000000)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
							+ " WHERE vouchertrans_item_id != 0"
							+ " AND vouchertrans_voucher_id = " + voucher_id
							// + " AND voucher_vouchertype_id = " +
							// voucher_vouchertype_id + ""
							+ " LIMIT 1";
					// SOP("StrSql====ratio===" + StrSql);
					ratio = ExecuteQuery(StrSql);
					if (!ratio.equals("0.0000000000")) {
						mul_ratio = Double.parseDouble(ratio);
					}
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
								+ " cart_voucher_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
								+ " cart_customer_id,"
								+ " cart_emp_id,"
								+ " cart_session_id,"
								+ " cart_location_id,"
								+ " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount,"
								+ " cart_option_id,"
								+ " cart_option_group,"
								+ " cart_item_serial,"
								+ " cart_item_batch_id,"
								+ " cart_price,"
								+ " cart_qty,"
								+ " cart_truckspace,"
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_discountamount,"
								+ " cart_taxamount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_convfactor,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ("
								+ " 0 ,"
								+ " " + voucher_vouchertype_id + ","
								+ " " + vouchertrans_multivoucher_id + ","
								+ " " + vouchertrans_customer_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + vouchertrans_location_id + "',"
								+ " " + vouchertrans_item_id + ","
								+ " '0',"
								+ " 0.0,"
								+ " '0',"
								+ " " + vouchertrans_tax_id + ","
								+ " " + vouchertrans_rowcount + ","
								+ " " + vouchertrans_option_id + "," + " ''," + " '"
								+ " " + CNumeric(vouchertrans_item_serial) + "',"
								+ " " + vouchertrans_item_batch_id + ",";
						// if (ratio.equals("0.000000")) {
						// StrSql += " " + (mul_ratio * vouchertrans_price)
						// + ",";
						// } else {
						StrSql += " " + vouchertrans_price + ",";
						// }
						// if ((vouchertrans_qty + "").equals(mul_ratio + "")) {
						// SOP("comming");
						// mul_ratio = 1.0;
						// }
						StrSql += " " + (mul_ratio * vouchertrans_qty) + ","
								+ " " + (mul_ratio * vouchertrans_truckspace) + ","
								+ " " + (mul_ratio * vouchertrans_netprice) + ","
								+ " " + (mul_ratio * vouchertrans_amount) + ","
								+ " " + (mul_ratio * vouchertrans_discountamount) + ","
								+ " " + (mul_ratio * vouchertrans_taxamount) + ","
								+ " " + vouchertrans_unit_cost + ",";

						if (voucher_vouchertype_id.equals("10")) {
							// SOP("vouchertrans_alt_qty==="+vouchertrans_alt_qty);
							// SOP("mul_ratio==="+mul_ratio);
							StrSql += " " + (mul_ratio * vouchertrans_alt_qty * cart_uom_ratio) + ",";
							if (!CNumeric(uom_parent_id).equals("0")) {
								StrSql += " " + uom_parent_id + ",";
							} else {
								StrSql += " " + vouchertrans_alt_uom_id + ",";
							}
						} else {
							StrSql += " " + (mul_ratio * vouchertrans_alt_qty)
									+ "," + " " + vouchertrans_alt_uom_id + ",";
						}
						StrSql += vouchertrans_convfactor + "," + " "
								+ ToLongDate(kknow()) + "," + " '"
								+ vouchertrans_dc + "'" + " )";

						// SOP("StrSql==cart main item="
						// +StrSqlBreaker(StrSql));
						// StrSql=compdb(StrSql);
						stmttx.addBatch(StrSql);
					}
				}

				// SOP("mul_ratio==="+mul_ratio);
				// SOP("vouchertrans_amount==="+vouchertrans_amount);
				if (!vouchertrans_rowcount.equals("0")
						&& !vouchertrans_option_id.equals("0")) {
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
								+ " cart_voucher_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
								+ " cart_customer_id,"
								+ " cart_emp_id,"
								+ " cart_session_id,"
								+ " cart_location_id,"
								+ " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount,"
								+ " cart_option_id,"
								+ " cart_option_group,"
								+ " cart_item_serial,"
								+ " cart_item_batch_id,"
								+ " cart_price,"
								+ " cart_qty,"
								+ " cart_truckspace,"
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_convfactor,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ("
								+ " 0,"
								+ " " + voucher_vouchertype_id + ","
								+ " " + vouchertrans_multivoucher_id + ","
								+ " " + vouchertrans_customer_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ "'" + vouchertrans_location_id + "',"
								+ " " + vouchertrans_item_id + ","
								+ "'" + vouchertrans_discount + "',"
								+ " " + vouchertrans_discount_perc + ","
								+ "'" + vouchertrans_tax + "',"
								+ " " + vouchertrans_tax_id + " ,"
								+ " " + vouchertrans_rowcount + ","
								+ " " + vouchertrans_option_id + ","
								+ "'',"
								+ "'',"
								+ " 0,"
								+ " " + vouchertrans_price + ","
								+ " 0.00,"
								+ " 0.00,"
								+ " 0.00,"
								+ " " + (mul_ratio * vouchertrans_amount) + ","
								+ " 0.00,"
								+ " 0.00,"
								+ " 0,"
								+ " 1,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + vouchertrans_dc + "'"
								+ " )";
						// SOP("StrSql==disc tax=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
				}
				// bill sundry tax ...
				if (vouchertrans_rowcount.equals("0")
						&& vouchertrans_option_id.equals("0")
						&& !vouchertrans_tax_id.equals("0")) {
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_emp_id,"
								+ " cart_session_id,"
								+ " cart_location_id,"
								+ " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount,"
								+ " cart_option_id,"
								+ " cart_option_group,"
								+ " cart_item_serial,"
								+ " cart_item_batch_id,"
								+ " cart_price,"
								+ " cart_qty,"
								+ " cart_truckspace,"
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_convfactor,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ("
								+ " " + vouchertrans_customer_id + ","
								+ " " + voucher_vouchertype_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + vouchertrans_location_id + "',"
								+ " " + vouchertrans_item_id + ","
								+ " '" + vouchertrans_discount + "',"
								+ " 0.0,"
								+ " '" + vouchertrans_tax + "',"
								+ " " + vouchertrans_tax_id + " ,"
								+ " " + vouchertrans_rowcount + ","
								+ " " + vouchertrans_option_id + ","
								+ "'',"
								+ "'',"
								+ " 0,"
								+ " " + vouchertrans_price + ","
								+ " 0.00,"
								+ " 0.00,"
								+ " 0.00,"
								// + (mul_ratio * vouchertrans_amount)
								+ " " + (vouchertrans_amount) + "," + " 0.00,"
								+ " 0.00,"
								+ " 0,"
								+ " 1,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + vouchertrans_dc + "'"
								+ " )";
						// SOP("StrSql==bill sundry tax=" +
						// StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
				}

			}
			stmttx.executeBatch();
			conntx.commit();
			crs1.close();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("AxA Pro===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
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

	// In update mode, copy all the items for that particular voucher-
	// -to the cart table by creating a new session

	public void CopyVoucherTransToCart(HttpServletRequest request,
			String emp_id, String cart_session_id, String voucher_id,
			String voucher_vouchertype_id, String status) {
		String StrSql = "";
		String comp_id = CNumeric(GetSession("comp_id", request));
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " WHERE 1=1"
				+ " AND cart_vouchertype_id = " + voucher_vouchertype_id
				+ " AND cart_emp_id = " + emp_id
				+ " AND cart_session_id = " + cart_session_id
				+ " AND SUBSTR(cart_time,1,8) < SUBSTR(" + ToShortDate(kknow()) + ",1,8)";
		updateQuery(StrSql);

		StrSql = "SELECT ";
		if (status.equals("Add")) {
			StrSql += " 0,";
		} else if (status.equals("Update")) {
			StrSql += " " + voucher_id + ",";
		}

		StrSql += " " + voucher_vouchertype_id + ", "
				+ " " + emp_id + ","
				+ " " + cart_session_id + ","
				+ " vouchertrans_multivoucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_location_id,"
				+ " vouchertrans_item_id,"
				+ " vouchertrans_discount,"
				+ " vouchertrans_discount_perc,"
				+ " vouchertrans_tax,"
				+ " vouchertrans_tax_id,"
				+ " vouchertrans_rowcount,"
				+ " vouchertrans_option_id,"
				+ " vouchertrans_option_group,"
				+ " vouchertrans_item_batch_id,"
				+ " vouchertrans_item_serial,"
				+ " vouchertrans_qty,"
				+ " vouchertrans_truckspace,"
				+ " vouchertrans_price,"
				+ " vouchertrans_netprice,"
				+ " vouchertrans_unit_cost,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_discountamount,"
				+ " vouchertrans_taxamount,"
				+ " vouchertrans_supplier_code,"
				+ " vouchertrans_alt_qty,"
				+ " vouchertrans_alt_uom_id,"
				+ " vouchertrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + ","
				+ " vouchertrans_dc"
				+ " FROM " + " " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + " " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
				+ " WHERE vouchertrans_item_id != 0"
				+ " AND vouchertrans_voucher_id = " + voucher_id + "";
		// SOP("StrSql =select== item tax disc=== " + StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_cart"
				+ " (cart_voucher_id,"
				+ " cart_vouchertype_id,"
				+ " cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_multivoucher_id,"
				+ " cart_customer_id,"
				+ " cart_location_id,"
				+ " cart_item_id,"
				+ " cart_discount,"
				+ " cart_discount_perc,"
				+ " cart_tax,"
				+ " cart_tax_id,"
				+ " cart_rowcount,"
				+ " cart_option_id,"
				+ " cart_option_group,"
				+ " cart_item_batch_id,"
				+ " cart_item_serial,"
				+ " cart_qty,"
				+ " cart_truckspace,"
				+ " cart_price,"
				+ " cart_netprice,"
				+ " cart_unit_cost,"
				+ " cart_amount,"
				+ " cart_discountamount,"
				+ " cart_taxamount,"
				+ " cart_supplier_code,"
				+ " cart_alt_qty,"
				+ " cart_alt_uom_id,"
				+ " cart_delivery_date,"
				+ " cart_time,"
				+ " cart_dc)" + " "
				+ StrSql + "";
		// SOP("StrSql =insert= item tax disc=== " + StrSqlBreaker(StrSql));
		updateQuery(StrSql);

		StrSql = "SELECT ";
		if (status.equals("Add")) {
			StrSql += " 0,";
		} else if (status.equals("Update")) {
			StrSql += " " + voucher_id + ",";
		}

		StrSql += " " + voucher_vouchertype_id + " ,"
				+ " " + emp_id + ","
				+ " " + cart_session_id + ","
				+ " vouchertrans_multivoucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_location_id,"
				+ " vouchertrans_item_id,"
				+ " vouchertrans_discount,"
				+ " vouchertrans_discount_perc,"
				+ " vouchertrans_tax,"
				+ " vouchertrans_tax_id,"
				+ " vouchertrans_rowcount,"
				+ " vouchertrans_option_id,"
				+ " vouchertrans_option_group,"
				+ " vouchertrans_item_batch_id,"
				+ " vouchertrans_item_serial,"
				+ " vouchertrans_qty,"
				+ " vouchertrans_truckspace,"
				+ " vouchertrans_price,"
				+ " vouchertrans_netprice,"
				+ " vouchertrans_unit_cost,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_supplier_code,"
				+ " vouchertrans_alt_qty,"
				+ " vouchertrans_alt_uom_id,"
				+ " vouchertrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + ","
				+ " vouchertrans_dc"
				+ " FROM " + " " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + " " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
				+ " WHERE  vouchertrans_tax_id !=0"
				+ " AND vouchertrans_item_id = 0"
				+ " AND vouchertrans_voucher_id = " + voucher_id + "";
		// SOP("StrSql =select=bs tax=== " + StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_cart"
				+ " (cart_voucher_id,"
				+ " cart_vouchertype_id,"
				+ " cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_multivoucher_id,"
				+ " cart_customer_id,"
				+ " cart_location_id,"
				+ " cart_item_id,"
				+ " cart_discount,"
				+ " cart_discount_perc,"
				+ " cart_tax,"
				+ " cart_tax_id,"
				+ " cart_rowcount,"
				+ " cart_option_id,"
				+ " cart_option_group,"
				+ " cart_item_batch_id,"
				+ " cart_item_serial,"
				+ " cart_qty,"
				+ " cart_truckspace,"
				+ " cart_price,"
				+ " cart_netprice,"
				+ " cart_unit_cost,"
				+ " cart_amount,"
				+ " cart_supplier_code,"
				+ " cart_alt_qty,"
				+ " cart_alt_uom_id,"
				+ " cart_delivery_date,"
				+ " cart_time,"
				+ " cart_dc)" + " " + StrSql + "";
		SOP("StrSql =insert==bs tax=== " + StrSqlBreaker(StrSql));
		updateQuery(StrSql);
	}

	public void CopySalesItemToCart(HttpServletRequest request, String emp_id, String cart_session_id, String voucher_so_id,
			String voucher_vouchertype_id, String status) throws Exception {
		CachedRowSet crs = null;
		int rowcount = 1, optionid = 0;
		double tax_value[] = new double[3];
		String msg = "", rateclass_id = "0";
		try {
			String StrSql = "";
			String comp_id = CNumeric(GetSession("comp_id", request));

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND SUBSTR(cart_time,1,8) < SUBSTR(" + ToShortDate(kknow()) + ",1,8)";
			updateQuery(StrSql);

			StrSql = "SELECT rateclass_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " WHERE so_id = " + voucher_so_id;
			rateclass_id = CNumeric(ExecuteQuery(StrSql));

			StrSql = "SELECT COALESCE(price_id, 0) AS price_id,"
					+ " COALESCE(price_rateclass_id, 0) AS price_rateclass_id,"
					+ " soitem_item_id, item_name,"
					+ " soitem_qty, soitem_price, soitem_disc,"
					+ " COALESCE (tax1.customer_id, 0) AS tax_id1,"
					+ " COALESCE (tax1.customer_rate, 0) AS tax_value1,"
					+ "	COALESCE (tax1.customer_id, 0) AS tax_customer_id1,"
					+ "	COALESCE (item_salestax2_aftertax1, 0 ) AS item_salestax2_aftertax1,"
					+ " COALESCE (tax2.customer_id, 0) AS tax_id2,"
					+ " COALESCE (tax2.customer_rate, 0) AS tax_value2,"
					+ "	COALESCE (tax2.customer_id, 0) AS tax_customer_id2,"
					+ "	COALESCE (item_salestax3_aftertax2, 0 ) AS item_salestax3_aftertax2,"
					+ " COALESCE (tax3.customer_id, 0) AS tax_id3,"
					+ " COALESCE (tax3.customer_rate, 0) AS tax_value3,"
					+ "	COALESCE (tax3.customer_id, 0) AS tax_customer_id3,"
					+ " soitem_total, item_alt_uom_id,"
					+ " COALESCE(item_sales_ledger_id, 0) AS item_sales_ledger_id,"
					+ " COALESCE(item_salesdiscount_ledger_id, 0) AS item_salesdiscount_ledger_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = soitem_so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " AND price_effective_from = ( SELECT price_effective_from"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_item_id = item_id"
					+ "	AND price_rateclass_id = " + rateclass_id
					+ " AND price_effective_from <= ( SELECT quote_date "
					+ "	FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " WHERE quote_id = so_quote_id )"
					+ " ORDER BY price_effective_from DESC LIMIT 1 )"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = ( SELECT branch_rateclass_id"
					+ " FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = so_branch_id )"
					+ " AND rateclass_id = price_rateclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = item_salestax1_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = item_salestax2_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id"
					+ " WHERE so_id = " + voucher_so_id;

			// SOP("main Query====" + StrSql);

			crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if ((crs.getString("price_rateclass_id").equals(rateclass_id)) ||
						(crs.getString("price_id").equals("0") && crs.getString("price_rateclass_id").equals("0"))) {
					optionid = rowcount;
					if (!crs.getString("tax_id1").equals("0")) {
						// Tax value for tax_value1
						tax_value[0] = (crs.getDouble("tax_value1")
								* crs.getDouble("soitem_total")) / 100;
					}

					if (!crs.getString("tax_id2").equals("0")) {
						// Tax value for tax_value2
						if (crs.getString("item_salestax2_aftertax1").equals("1")) {
							tax_value[1] = (crs.getDouble("tax_value2") *
									(crs.getDouble("soitem_total") + tax_value[0])) / 100;
						} else {
							tax_value[1] = (crs.getDouble("tax_value2")
									* crs.getDouble("soitem_total")) / 100;
						}
					}

					if (!crs.getString("tax_id3").equals("0")) {
						// Tax value for tax_value3
						if (crs.getString("item_salestax3_aftertax2").equals("1")) {
							tax_value[2] = (crs.getDouble("tax_value3")
									* (crs.getDouble("soitem_total") + tax_value[1])) / 100;
						} else {
							tax_value[2] = (crs.getDouble("tax_value3")
									* crs.getDouble("soitem_total")) / 100;
						}
					}

					// This Query will insert row for main item into cart...

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
							+ " (cart_voucher_id,"
							+ " cart_vouchertype_id,"
							+ " cart_emp_id,"
							+ " cart_session_id,"
							+ " cart_multivoucher_id,"
							+ " cart_customer_id,"
							+ " cart_location_id,"
							+ " cart_item_id,"
							+ " cart_rowcount,"
							+ " cart_option_id,"
							+ " cart_qty,"
							+ " cart_price,"
							+ " cart_netprice,"
							+ " cart_unit_cost,"
							+ " cart_amount,"
							+ " cart_discountamount,"
							+ " cart_alt_qty,"
							+ " cart_alt_uom_id,"
							+ " cart_time,"
							+ " cart_dc)"
							+ " values("
							+ " 0," // cart_voucher_id
							+ " " + voucher_vouchertype_id + ","
							+ " " + emp_id + ","
							+ " " + cart_session_id + ","
							+ " 0," // cart_multivoucher_id
							+ " " + crs.getString("item_sales_ledger_id") + "," // cart_customer_id
							+ " 0," // cart_location_id
							+ "	" + crs.getString("soitem_item_id") + ","
							+ " " + rowcount + "," // cart_rowcount
							+ " 0," // cart_option_id
							+ "	" + crs.getString("soitem_qty") + ","
							+ "	" + crs.getString("soitem_price") + ","
							+ "	" + crs.getString("soitem_total") + ","
							+ "	" + crs.getString("soitem_price") + ","
							+ "	" + crs.getDouble("soitem_price") * crs.getDouble("soitem_qty") + ","
							+ "	" + crs.getString("soitem_disc") + ","
							+ "	" + crs.getString("soitem_qty") + ","
							+ "	" + crs.getString("item_alt_uom_id") + ","
							+ " " + ToLongDate(kknow()) + ","
							+ " 0" // cart_dc
							+ " )";
					// SOP("StrSql =insert==main item=== " + StrSqlBreaker(StrSql));
					updateQuery(StrSql);

					double discount_perc = 0.00;
					if (crs.getDouble("soitem_disc") > 0) {
						if (!crs.getString("soitem_price").equals("0.0")) {
							discount_perc = (double) ((crs.getDouble("soitem_disc") / crs.getDouble("soitem_price")) * 100);
						}
					}

					// This Query will insert row for item Discount into cart...

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart "
							+ "( cart_voucher_id,"
							+ " cart_vouchertype_id,"
							+ " cart_emp_id,"
							+ " cart_session_id,"
							+ " cart_multivoucher_id,"
							+ " cart_customer_id,"
							+ " cart_location_id,"
							+ " cart_item_id,"
							+ " cart_discount,"
							+ " cart_discount_perc,"
							+ " cart_rowcount,"
							+ " cart_option_id,"
							+ " cart_qty,"
							+ " cart_price,"
							+ " cart_amount,"
							+ " cart_time,"
							+ " cart_dc )"
							+ "values( "
							+ " 0," // cart_voucher_id
							+ " " + voucher_vouchertype_id + ","
							+ " " + emp_id + ","
							+ " " + cart_session_id + ","
							+ " 0," // cart_multivoucher_id
							+ " " + crs.getString("item_salesdiscount_ledger_id") + "," // cart_customer_id
							+ " 0," // cart_location_id
							+ "	" + crs.getString("soitem_item_id") + ","
							+ " 1," // cart_discount
							+ " " + discount_perc + ","
							+ " " + rowcount + ","
							+ " " + optionid + ","
							+ " 0," // cart_dc
							+ "	" + crs.getString("soitem_disc") + "," // cart_price
							+ "	" + crs.getString("soitem_disc") + ","// cart_amount
							+ " " + ToLongDate(kknow()) + "," // cart_time
							+ "1)";

					// SOP("StrSql =insert==disc=== " + StrSqlBreaker(StrSql));

					updateQuery(StrSql);
					// Tax
					for (int i = 1, j = 0; i <= 3; i++, j++) {

						if (!crs.getString("tax_id" + i).equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart "
									+ "( cart_voucher_id,"
									+ " cart_vouchertype_id,"
									+ " cart_emp_id,"
									+ " cart_session_id,"
									+ " cart_multivoucher_id,"
									+ " cart_customer_id,"
									+ " cart_location_id,"
									+ " cart_item_id,"
									+ " cart_tax,"
									+ " cart_tax_id,"
									+ " cart_rowcount,"
									+ " cart_option_id,"
									+ " cart_price,"
									+ " cart_amount,"
									+ " cart_time,"
									+ " cart_dc )"
									+ " values("
									+ " 0," // cart_voucher_id
									+ " " + voucher_vouchertype_id + ","
									+ " " + emp_id + ","
									+ " " + cart_session_id + ","
									+ " 0," // cart_multivoucher_id
									+ " " + crs.getString("tax_customer_id" + i) + "," // cart_customer_id
									+ " 0," // cart_location_id
									+ "	" + crs.getString("soitem_item_id") + ","
									+ "	1," // cart_tax
									+ "	" + crs.getString("tax_id" + i) + ","
									+ " " + rowcount + ","
									+ " " + optionid + ","
									+ "	" + tax_value[j] + ",";
							if (crs.getDouble("tax_value" + i) > 0) {
								StrSql += " " + (tax_value[j] * (crs.getDouble("soitem_qty"))) + ",";
							} else {
								StrSql += " 0,";
							}
							StrSql += " " + ToLongDate(kknow()) + "," + "0)";
							// SOP("StrSql==Tax==" + StrSql);
							updateQuery(StrSql);
						}
					}
					// SOP("StrSql==Tax==" + StrSql);
					// updateQuery(StrSql);
					rowcount++;
				}
			}
		} catch (Exception e) {

			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void CopyJCTransToCart(HttpServletRequest request,
			String emp_id, String cart_session_id, String voucher_id,
			String voucher_vouchertype_id, String status) {
		String StrSql = "";
		String comp_id = CNumeric(GetSession("comp_id", request));
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " WHERE 1=1"
				+ " AND cart_vouchertype_id = " + voucher_vouchertype_id
				+ " AND cart_emp_id = " + emp_id
				+ " AND cart_session_id = " + cart_session_id
				+ " AND SUBSTR(cart_time,1,8) < SUBSTR(" + ToShortDate(kknow()) + ",1,8)";
		updateQuery(StrSql);

		StrSql = "SELECT ";
		if (status.equals("Add")) {
			StrSql += " 0,";
		} else if (status.equals("Update")) {
			StrSql += " " + voucher_id + ",";
		}

		StrSql += " " + voucher_vouchertype_id + " ,"
				+ " " + emp_id + ","
				+ " " + cart_session_id + ","
				+ " jctrans_multivoucher_id,"
				+ " jctrans_customer_id,"
				+ " jctrans_location_id,"
				+ " jctrans_item_id,"
				+ " jctrans_discount,"
				+ " jctrans_discount_perc,"
				+ " jctrans_tax,"
				+ " jctrans_tax_id,"
				+ " jctrans_rowcount,"
				+ " jctrans_option_id,"
				+ " jctrans_option_group,"
				+ " jctrans_item_batch_id,"
				+ " jctrans_item_serial,"
				+ " jctrans_qty,"
				+ " jctrans_truckspace,"
				+ " jctrans_price,"
				+ " jctrans_netprice,"
				+ " jctrans_unit_cost,"
				+ " jctrans_amount,"
				+ " jctrans_discountamount,"
				+ " jctrans_taxamount,"
				+ " jctrans_supplier_code,"
				+ " jctrans_alt_qty,"
				+ " jctrans_alt_uom_id,"
				+ " jctrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + ","
				+ " jctrans_dc"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jctrans_jc_id"
				+ " WHERE jctrans_item_id != 0"
				+ " AND jctrans_jc_id = " + voucher_id + "";
		// SOP("StrSql =select== item tax disc=== " + StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_cart"
				+ " (cart_voucher_id,"
				+ " cart_vouchertype_id,"
				+ " cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_multivoucher_id,"
				+ " cart_customer_id,"
				+ " cart_location_id,"
				+ " cart_item_id,"
				+ " cart_discount,"
				+ " cart_discount_perc,"
				+ " cart_tax,"
				+ " cart_tax_id,"
				+ " cart_rowcount,"
				+ " cart_option_id,"
				+ " cart_option_group,"
				+ " cart_item_batch_id,"
				+ " cart_item_serial,"
				+ " cart_qty,"
				+ " cart_truckspace,"
				+ " cart_price,"
				+ " cart_netprice,"
				+ " cart_unit_cost,"
				+ " cart_amount,"
				+ " cart_discountamount,"
				+ " cart_taxamount,"
				+ " cart_supplier_code,"
				+ " cart_alt_qty,"
				+ " cart_alt_uom_id,"
				+ " cart_delivery_date,"
				+ " cart_time,"
				+ " cart_dc)" + " "
				+ StrSql + "";
		// SOP("StrSql =insert= item tax disc=== " + StrSqlBreaker(StrSql));
		updateQuery(StrSql);

		StrSql = "SELECT ";
		if (status.equals("Add")) {
			StrSql += " 0,";
		} else if (status.equals("Update")) {
			StrSql += " " + voucher_id + ",";
		}

		StrSql += " " + voucher_vouchertype_id + " ,"
				+ " " + emp_id + ","
				+ " " + cart_session_id + ","
				+ " jctrans_multivoucher_id,"
				+ " jctrans_customer_id,"
				+ " jctrans_location_id,"
				+ " jctrans_item_id,"
				+ " jctrans_discount,"
				+ " jctrans_discount_perc,"
				+ " jctrans_tax,"
				+ " jctrans_tax_id,"
				+ " jctrans_rowcount,"
				+ " jctrans_option_id,"
				+ " jctrans_option_group,"
				+ " jctrans_item_batch_id,"
				+ " jctrans_item_serial,"
				+ " jctrans_qty,"
				+ " jctrans_truckspace,"
				+ " jctrans_price,"
				+ " jctrans_netprice,"
				+ " jctrans_unit_cost,"
				+ " jctrans_amount,"
				+ " jctrans_supplier_code,"
				+ " jctrans_alt_qty,"
				+ " jctrans_alt_uom_id,"
				+ " jctrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + ","
				+ " jctrans_dc"
				+ " FROM " + " " + compdb(comp_id) + "axela_service_jc_trans"
				+ " INNER JOIN " + " " + compdb(comp_id) + "axela_service_jc ON jc_id = jctrans_jc_id"
				+ " WHERE  jctrans_tax_id !=0"
				+ " AND jctrans_item_id = 0"
				+ " AND jctrans_jc_id = " + voucher_id + "";
		// SOP("StrSql =select=bs tax=== " + StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_cart"
				+ " (cart_voucher_id,"
				+ " cart_vouchertype_id,"
				+ " cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_multivoucher_id,"
				+ " cart_customer_id,"
				+ " cart_location_id,"
				+ " cart_item_id,"
				+ " cart_discount,"
				+ " cart_discount_perc,"
				+ " cart_tax,"
				+ " cart_tax_id,"
				+ " cart_rowcount,"
				+ " cart_option_id,"
				+ " cart_option_group,"
				+ " cart_item_batch_id,"
				+ " cart_item_serial,"
				+ " cart_qty,"
				+ " cart_truckspace,"
				+ " cart_price,"
				+ " cart_netprice,"
				+ " cart_unit_cost,"
				+ " cart_amount,"
				+ " cart_supplier_code,"
				+ " cart_alt_qty,"
				+ " cart_alt_uom_id,"
				+ " cart_delivery_date,"
				+ " cart_time,"
				+ " cart_dc)"
				+ " " + StrSql + "";
		// SOP("StrSql =insert==bs tax=== " + StrSqlBreaker(StrSql));
		updateQuery(StrSql);
	}

	public String PopulateGroupStr(String prodgroup_id, String comp_id) {
		String StrSql = "", prodgroup_name = "", parent_id = "0";
		try {
			if (!prodgroup_id.equals("") && !prodgroup_id.equals("0")) {
				StrSql = "SELECT accgroup_id, accgroup_name, accgroup_parent_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_group"
						+ " WHERE accgroup_id = " + prodgroup_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {

					prodgroup_name = prodgroup_name + crs.getString("accgroup_name");
					parent_id = crs.getString("accgroup_parent_id");

					if (!parent_id.equals("0")) {
						while (!parent_id.equals("0")) {
							StrSql = "SELECT accgroup_id, accgroup_name, accgroup_parent_id"
									+ " FROM " + compdb(comp_id) + "axela_acc_group"
									+ " WHERE accgroup_id = " + parent_id;
							CachedRowSet crs1 = processQuery(StrSql, 0);
							while (crs1.next()) {
								prodgroup_name = crs1.getString("accgroup_name") + " > " + prodgroup_name;
								parent_id = crs1.getString("accgroup_parent_id");
							}
							crs1.close();
						}
					}
				}
				crs.close();
			}
			return prodgroup_name;
		} catch (Exception ex) {
			SOPError("error n PopulateCategoryStr: " + ex);
			return "";
		}
	}

	public String ReturnPreOwnedName(HttpServletRequest request) {
		String Str = "";
		Str = GetSession("PreOnwedName", request);
		if (str.equals(""))
		{
			String emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
			if (emp_branch_id.equals("0"))
				Str = "Pre-Owned";
			else {
				String comp_id = CNumeric(GetSession("comp_id", request));
				String strSQL = "SELECT brand_name "
						+ " FROM " + compdb(comp_id) + " axela_branch "
						+ " INNER JOIN axela_brand ON brand_id = branch_brand_id "
						+ " WHERE branch_branchtype_id = 2 "
						+ " AND branch_id = " + emp_branch_id;
				// SOP("strsql====" + strSQL);
				Str = ExecuteQuery(strSQL);
				// SOP("str========" + Str);
				if (Str.equals(""))
				{
					Str = "Pre-Owned";
				}
			}
			SetSession("PreOnwedName", str, request);
		}
		return Str;
	}

	public String IndianCurrencyFormatToWord(long num) {
		str = "";
		if (num == 0) {
			return "zero";
		}
		int c = 1;
		long rm;
		while (num != 0) {
			switch (c) {
				case 1 :
					rm = num % 100;
					IndianPass((int) rm);
					if (num > 100 && num % 100 != 0) {
						IndianDisplay(" AND");
					}
					num /= 100;
					break;
				case 2 :
					rm = num % 10;
					if (rm != 0) {
						IndianDisplay("");
						IndianDisplay(b[0]);
						IndianDisplay("");
						IndianPass((int) rm);
					}
					num /= 10;
					break;
				case 3 :
					rm = num % 100;
					if (rm != 0) {
						IndianDisplay("");
						IndianDisplay(b[1]);
						IndianDisplay("");
						IndianPass((int) rm);
					}
					num /= 100;
					break;
				case 4 :
					rm = num % 100;
					if (rm != 0) {
						IndianDisplay("");
						IndianDisplay(b[2]);
						IndianDisplay("");
						IndianPass((int) rm);
					}
					num /= 100;
					break;
				case 5 :
					rm = num % 100;
					if (rm != 0) {
						IndianDisplay("");
						IndianDisplay(b[3]);
						IndianDisplay("");
						IndianPass((int) rm);
					}
					num /= 100;
					break;
			}// end switch
			c++;
		}// end while
		return str;
	}// end method

	public void IndianPass(int num) {
		int rm, q;
		if (num < 10) {
			IndianDisplay(a[num]);
		} else if (num > 9 && num < 20) {
			IndianDisplay(c[num - 10]);
		} else if (num > 19) {
			rm = num % 10;
			if (rm == 0) {
				q = num / 10;
				IndianDisplay(d[q - 2]);
			} else {
				q = num / 10;
				IndianDisplay(a[rm]);
				IndianDisplay("");
				IndianDisplay(d[q - 2]);
			}
		}
	}

	public void IndianDisplay(String s) {
		String t;
		t = str;
		str = s;
		str += t;
	}

	public String PopulateNumberDrop(int from, int to, String id, String format) {
		StringBuilder Str = new StringBuilder();
		for (int i = from; i <= to; i++) {
			Str.append("<option value=").append(i);
			Str.append(Selectdrop(i, id));
			Str.append(">").append(i + " " + format);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String CustomerContactDetailsPopup(String customer_id, String customer_name, String customer_info, String type) {
		StringBuilder Str = new StringBuilder();
		Str.append("<a "
				+ " data-toggle=popover"
				+ " data-html=true"
				+ " data-placement=top"
				+ " data-title='" + customer_name + "'"
				+ " data-content='" + customer_info + "'");
		if (type.equals("customer")) {
			Str.append(" href='../customer/customer-list.jsp?customer_id=" + customer_id + "' >" + customer_name + "</a>");
		}
		if (type.equals("contact")) {
			Str.append(" href='../customer/customer-contact-list.jsp?contact_id=" + customer_id + "' >" + customer_name + "</a>");
		}

		return Str.toString();
	}

	public String ContactMobilePopup(String comp_id, String contact_mobile, String id, String type) {
		StringBuilder Str = new StringBuilder();
		Str.append("<br />"
				+ "<a "
				+ " data-toggle=popover"
				+ " data-html=true"
				+ " data-placement=top"
				+ " data-content='" + contact_mobile + "'"
				+ " href=#>"
				+ SplitPhoneNoSpan(contact_mobile, 10, type, id) +
				ClickToCall(contact_mobile, comp_id) + "</a>");

		return Str.toString();
	}

}
