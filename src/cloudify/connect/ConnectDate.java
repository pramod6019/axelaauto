package cloudify.connect;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ConnectDate {

	public ConnectDate() {
	}
	final Logger logger = Logger.getLogger(this.getClass());
	public boolean isValidDateFormatShort(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date testdate = null;
		try {
			if (Pattern.matches("[0-9/]+", date) && date.length() == 10) {
				testdate = sdf.parse(date);// String to date
			}
		} catch (ParseException ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return false;
		}
		if (testdate == null) {
			return false;
		} else if (!sdf.format(testdate).equals(date)) {
			return false;
		}
		return true;
	}

	public boolean isValidDateFormatLong(String date) {
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date testdate = null;
		try {
			// if (Pattern.matches("[0-9/: ]+", date) && date.length() == 19) {
			if (Pattern.matches("[0-9/: ]+", date) && date.length() == 16) {
				testdate = sdf.parse(date);
			}// String to date
				// logger.error("testdate==" + testdate);
		} catch (ParseException ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return false;
		}
		if (testdate == null) {
			return false;
		} else if (!sdf.format(testdate).equals(date)) {
			return false;
		}
		return true;
	}

	public boolean isValidDateFormatStr(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date testdate = null;
		try {
			testdate = sdf.parse(date);// String to date
		} catch (ParseException e) {
			return false;
		}
		if (!sdf.format(testdate).equals(date)) {
			return false;
		}
		return true;
	}

	public String ToLongDate(Date dtDateTime) {
		try {
			String strOutDt = new SimpleDateFormat("yyyyMMddHHmmss").format(dtDateTime);
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String ToShortDate(Date dtDateTime) {
		try {
			String strOutDt = new SimpleDateFormat("yyyyMMdd").format(dtDateTime);
			return strOutDt + "000000";
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String DateToShortDate(Date dtDateTime) {
		try {
			String strOutDt = new SimpleDateFormat("dd/MM/yyyy").format(dtDateTime);
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String strToLongDate(String strDateTime) {
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date dttemp = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				strOutDt = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String strToShortDate(String strDateTime) {
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date dttemp = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				strOutDt = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String strToMonthYear(String strDateTime) {
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date dttemp = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				strOutDt = new SimpleDateFormat("MMMM yyyy").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto===" + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String ConvertLongDateToStr(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(strDateTime);
				strOutDt = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ConvertShortDateToStr(String strDateTime) {
		// logger.error("strDateTime======="+strDateTime);
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ConvertLongDate(String strDateTime) {
		// logger.error("strDateTime======="+strDateTime);
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				strOutDt = new SimpleDateFormat("dd MMMM, yyyy EEEE").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ConvertMonthYearToStr(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("MMMM yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto===" + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String fmt1tofmt2(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(strDateTime);
				strOutDt = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String fmtShr1tofmtShr2(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("MM/dd/yyyy").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String fmtShr2tofmtShr1(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("MM/dd/yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String fmtShr3tofmtShr1(String strDateTime) {
		Date dttemp;
		String strOutDt = "";
		try {

			if (strDateTime != null && !strDateTime.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				sdf.setLenient(false); // Set false to fail on In-valid dates
				dttemp = sdf.parse(strDateTime);
				strOutDt = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			return "";
		}
	}

	public double getHoursBetween(Date first, Date second) {
		// logger.error("second" + second.getTime());
		double milliElapsed = second.getTime() - first.getTime();
		double hoursElapsed = (milliElapsed / 3600F / 1000F);
		return (Math.round(hoursElapsed * 100F) / 100F);
	}

	public double getMinBetween(Date first, Date second) {
		double milliElapsed = second.getTime() - first.getTime();
		double hoursElapsed = (milliElapsed / 60F / 1000F);
		return (Math.round(hoursElapsed) % 60F);
	}

	public double getSecBetween(Date first, Date second) {
		double milliElapsed = second.getTime() - first.getTime();
		double secElapsed = (milliElapsed / 1000F);
		return (Math.round(secElapsed));
	}

	public String ConvertMintoHrsMins(long totalmin) {
		DecimalFormat deci = new DecimalFormat("#");
		String output = "0";
		if (totalmin == 0) {
			return "0";
		}
		String hours = deci.format(Math.floor(totalmin / 60));
		String min = deci.format(Math.floor(totalmin % 60));
		if (!hours.equals("0")) {
			output = hours + " Hrs ";
		}
		if (!min.equals("0")) {
			output = output + min + " Mins";
		}
		return output;
	}

	public String SplitMonth(String strDateTime) {
		String sm = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sm = new SimpleDateFormat("MM").format(date);
			}
			return sm;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

			return "";
		}
	}

	public String SplitHourMin(String strDateTime) {
		String sm = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sm = new SimpleDateFormat("HH:mm").format(date);
			}
			return sm;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SplitDate(String strDateTime) {
		String sd = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sd = new SimpleDateFormat("dd").format(date);
			}
			return sd;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SplitYear(String strDateTime) {
		String sy = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sy = new SimpleDateFormat("yyyy").format(date);
			}
			return sy;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public Date StringToDate(String str) {
		Connect ct = new Connect();
		try {

			Date dtTmp = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
			return dtTmp;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return ct.kknow();
		}
	}
	public int DaysInMonth(int year, int month) {
		Calendar cal = new GregorianCalendar();
		cal.set(year, month - 1, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public Date AddHoursDate(Date first, double days, double hours, double minutes) {
		double additionalTime = (days * 24F * 3600F * 1000F) + (hours * 3600F * 1000F) + (minutes * 60 * 1000);
		return new Date(Math.round((double) first.getTime() + additionalTime));
	}

	public String AddDayMonthYear(String fromdate, int days, int week, int month, int year) {
		String Str = "";
		Date date;
		Calendar c1 = Calendar.getInstance();
		try {
			if (fromdate != null && !fromdate.equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(fromdate);
				c1.setTime(date);
				c1.add(Calendar.DATE, days);
				c1.add(Calendar.WEDNESDAY, week);
				c1.add(Calendar.MONTH, month);
				c1.add(Calendar.YEAR, year);
				Str = strToShortDate(ToLongDate(c1.getTime()));
				return Str;
			}
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str;
	}

	public String AddDayMonthYearStr(String fromdate, int days, int week, int month, int year) {
		String Str = "";
		Date date;
		Calendar c1 = Calendar.getInstance();
		// logger.error("fromdate = " + fromdate);
		// logger.error("month = " + month);
		// fromdate = "20140327000000";
		// month = -3;
		try {
			if (fromdate != null && !fromdate.equals("")) {
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(fromdate);
				c1.setTime(date);
				c1.add(Calendar.DATE, days);
				c1.add(Calendar.WEDNESDAY, week);
				c1.add(Calendar.MONTH, month);
				c1.add(Calendar.YEAR, year);
				Str = strToLongDate(ToLongDate(c1.getTime()));
				// logger.error("Str = " + Str);
				return Str;
			}
		} catch (Exception ex) {
			logger.error("Axelaauto===" + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str;
	}
	public double getDaysBetween(String first, String second) {
		try {
			Date First = new SimpleDateFormat("yyyyMMddHHmmss").parse(first);
			Date Second = new SimpleDateFormat("yyyyMMddHHmmss").parse(second);
			double milliElapsed = Second.getTime() - First.getTime();
			double daysElapsed = (milliElapsed / 24F / 3600F / 1000F);
			return (Math.round(daysElapsed * 100F) / 100F);
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return 0;
		}
	}

	public double getMinutesBetween(String first, String second) {
		try {
			Date First = new SimpleDateFormat("yyyyMMddHHmmss").parse(first);
			Date Second = new SimpleDateFormat("yyyyMMddHHmmss").parse(second);
			double milliElapsed = Second.getTime() - First.getTime();
			double minutesElapsed = (milliElapsed / 60F / 1000F);
			return (Math.round(minutesElapsed * 100F) / 100F);
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return 0;
		}
	}

	public String getMonthsBetween(String first, String second) {
		try {
			Date First = new SimpleDateFormat("yyyyMMddHHmmss").parse(first);
			Date Second = new SimpleDateFormat("yyyyMMddHHmmss").parse(second);
			double milliElapsed = Second.getTime() - First.getTime();
			double avgmonth = 365.24 * 24 * 60 * 60 * 1000 / 12;
			double MonthsElapsed = (milliElapsed / avgmonth);
			// logger.error("MonthsElapsed==="+MonthsElapsed);
			if (MonthsElapsed < 0) {
				MonthsElapsed = 0;
			}
			return (Math.round((MonthsElapsed * 100F) / 100F)) + "";
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "0";
		}
	}

	public String getYearBetween(String first, String second) {
		try {
			Date First = new SimpleDateFormat("yyyyMMddHHmmss").parse(first);
			Date Second = new SimpleDateFormat("yyyyMMddHHmmss").parse(second);
			double milliElapsed = Second.getTime() - First.getTime();
			double avgmonth = 365.24 * 24 * 60 * 60 * 1000;
			double MonthsElapsed = (milliElapsed / avgmonth);
			// logger.error("MonthsElapsed==="+MonthsElapsed);
			if (MonthsElapsed < 0) {
				MonthsElapsed = 0;
			}
			return (Math.round((MonthsElapsed * 100F) / 100F)) + "";
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "0";
		}
	}

	public String DiffBetweenDates(String first, String second) {
		try {
			String diff = "";
			int year = 0;
			int month = 0;
			int days = 0;
			Date First = new SimpleDateFormat("yyyyMMddHHmmss").parse(first);
			Date Second = new SimpleDateFormat("yyyyMMddHHmmss").parse(second);

			// for year
			double milliElapsed = Second.getTime() - First.getTime();
			double avgmonth = 365.24 * 24 * 60 * 60 * 1000;
			double YearsElapsed = (milliElapsed / avgmonth);
			year = (int) ((YearsElapsed * 100F) / 100F);
			diff = year + ",";

			// for month
			avgmonth = 365.24 * 24 * 60 * 60 * 1000 / 12;
			double MonthsElapsed = (int) (milliElapsed / avgmonth);
			month = (int) ((MonthsElapsed * 100F) / 100F) % 12;
			diff += month + ",";

			// for days
			double daysElapsed = (milliElapsed / 24F / 3600F / 1000F);
			daysElapsed = ((daysElapsed * 100F / 100F) % 30.4375);
			diff = diff + (int) daysElapsed + ",";

			return diff;
		} catch (Exception ex) {
			logger.error("Axelaauto===" + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "0";
		}
	}
	public String TextMonth(int id) {
		switch (id) {
			case 0 :
				return "January";

			case 1 :
				return "February";

			case 2 :
				return "March";

			case 3 :
				return "April";

			case 4 :
				return "May";

			case 5 :
				return "June";

			case 6 :
				return "July";

			case 7 :
				return "August";

			case 8 :
				return "September";

			case 9 :
				return "October";

			case 10 :
				return "November";

			case 11 :
				return "December";

			default :
				return null;
		}
	}

	public String TextDayOfWeek(int id) {
		switch (id) {
			case 1 :
				return "Sunday";

			case 2 :
				return "Monday";

			case 3 :
				return "Tuesday";

			case 4 :
				return "Wednesday";

			case 5 :
				return "Thursday";

			case 6 :
				return "Friday";

			case 7 :
				return "Saturday";

			case 9 :
				return "Include Holidays";

			default :
				return null;
		}
	}

	public String DiffDaystoDate(String id) {
		Connect ct = new Connect();
		String days = id;
		double duehrs = Double.valueOf(days).doubleValue();
		days = ToLongDate(AddHoursDate(ct.kknow(), -duehrs, 0, 0));
		return days;
	}

	public String FormatDate(String date, String format) {
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
			String dt = formatter.format(date1);
			return dt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}
	// populate date in mobile
	public String FormatDateMobileStr(String date) {
		try {
			if (isValidDateFormatShort(date)) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
				String dt = formatter.format(date1);

				return dt;
			} else {
				return "";
			}
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}
	// to convert from mobile to backend
	public String FormatDateStr(String date) {
		try {
			if (!date.equals("")) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				String dt = formatter.format(date1);
				return dt;
			} else {
				return "";
			}
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String CalDateFormat(String date, String format) {
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
			String dt = formatter.format(date1);
			return dt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String PeriodTime(String starttime, String endtime, String format) {
		String periodtime = "";
		try {
			if (format.equals("1")) {
				periodtime = CalDateFormat(starttime, "dd/MM/yyyy HH:mm") + " - " + CalDateFormat(endtime, "HH:mm");
			} else if (format.equals("2")) {
				periodtime = CalDateFormat(starttime, "HH:mm") + " - " + CalDateFormat(endtime, "HH:mm");
			} else if (format.equals("3")) {
				periodtime = CalDateFormat(starttime, "dd/MM/yyyy") + "<br>" + CalDateFormat(starttime, "HH:mm") + "-" + CalDateFormat(endtime, "HH:mm");
			} else if (format.equals("4")) {
				if (getHoursBetween(StringToDate(starttime), StringToDate(endtime)) > 12) {
					periodtime = CalDateFormat(starttime, "dd/MM/yyyy HH:mm") + "<br>" + CalDateFormat(endtime, "dd/MM/yyyy HH:mm");
				} else {
					periodtime = CalDateFormat(starttime, "dd/MM/yyyy") + "<br>" + CalDateFormat(starttime, "HH:mm") + " - " + CalDateFormat(endtime, "HH:mm");
				}
			}
			return periodtime;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return " " + ex;
		}
	}

	public String CalcYearMonth(String month) {
		int yr = 0, mth = 0;
		String yearmonth = "";
		try {
			if (!month.equals("")) {
				mth = Integer.parseInt(month) % 12;
				yr = Integer.parseInt(month) / 12;
				if (yr > 1) {
					yearmonth = (yr + "") + " Years ";
				} else {
					yearmonth = (yr + "") + " Year ";
				}
				if (mth > 1) {
					yearmonth = yearmonth + (mth + "") + " Months";
				} else {
					yearmonth = yearmonth + (mth + "") + " Month";
				}
			}
			return yearmonth;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String AddMonth(String fromdate, int month) {
		String Str = "";
		Date date;
		Calendar c1 = Calendar.getInstance();
		try {
			if (fromdate != null && !fromdate.equals("")) {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(fromdate);
				c1.setTime(date);
				c1.add(Calendar.MONTH, month);
				Str = strToShortDate(ToLongDate(c1.getTime()));
				return Str;
			}
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str;
	}

	public boolean isSunday(String date) {
		boolean result = false;
		int day;
		Calendar cal = new GregorianCalendar();
		cal.setTime(StringToDate(date));
		day = cal.get(cal.DAY_OF_WEEK);
		if (day == 1) {
			result = true;
		}
		// logger.error("result--"+result+" for date--"+date);
		return result;
	}

	public int ReturnDayOfWeek(String date) {
		int day = 0;
		Calendar cal = new GregorianCalendar();
		cal.setTime(StringToDate(date));
		day = cal.get(Calendar.DAY_OF_WEEK);
		return day;
	}

	public int ReturnWeekOfYear(String date) {
		int week = 0;
		Calendar cal = new GregorianCalendar();
		cal.setTime(StringToDate(date));
		week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	public String TextDayOfWeek(int id, int chop) {
		switch (id) {
			case 1 :
				return "Sunday".substring(0, chop);

			case 2 :
				return "Monday".substring(0, chop);

			case 3 :
				return "Tuesday".substring(0, chop);

			case 4 :
				return "Wednesday".substring(0, chop);

			case 5 :
				return "Thursday".substring(0, chop);

			case 6 :
				return "Friday".substring(0, chop);

			case 7 :
				return "Saturday".substring(0, chop);

			case 9 :
				return "Include Holidays";

			default :
				return null;
		}
	}

	public String StrShorttoMonthYear(String strDateTime) {
		// logger.error("strDateTime======="+strDateTime);
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("MMMM yyyy").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String StrShorttoYearMonth(String strDateTime) {
		// logger.error("strDateTime======="+strDateTime);
		Date dttemp;
		String strOutDt = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				dttemp = new SimpleDateFormat("dd/MM/yyyy").parse(strDateTime);
				strOutDt = new SimpleDateFormat("yyyy - MMMM").format(dttemp);
			}
			return strOutDt;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String ConvertHoursToDays(int hours) {
	// String StrDays = "";
	// if (hours < 24) {
	// StrDays = hours + " hrs";
	// } else {
	// StrDays = (hours / 24) + " Days ";
	// if ((hours % 24) != 0) {
	// StrDays = StrDays + (hours % 24) + " Hrs";
	// }
	// }
	// return StrDays;
	// }
	/* ===Added on 5th july 2013 Saiman=== */

	public String ConvertHoursToDaysHrsMins(double hours) {
		String hrs = hours + "";
		String split_hours[] = hrs.split("\\.");
		String StrDays = "";
		if (!split_hours[0].equals("0")) {
			if (Integer.parseInt(split_hours[0]) < 24) {
				StrDays = Integer.parseInt(split_hours[0]) + " Hrs ";
			} else {
				StrDays = (Integer.parseInt(split_hours[0]) / 24) + " Days ";
				if ((Integer.parseInt(split_hours[0]) % 24) != 0) {
					StrDays = StrDays + (Integer.parseInt(split_hours[0]) % 24) + " Hrs ";
				}
			}
		}
		if (Integer.parseInt(split_hours[1]) != 0) {
			StrDays = StrDays + Integer.parseInt(split_hours[1]) + " Mins";
		} else {
			StrDays = StrDays + " 00 Mins";
		}
		return StrDays;
	}

	public String ConvertMintoDaysHrsMins(long totalmin) {
		//
		DecimalFormat deci = new DecimalFormat("#00.###");
		String output = "";
		String days = deci.format(Math.floor(totalmin / 60 / 24));
		String hours = deci.format(Math.floor(totalmin / 60 % 24));
		String min = deci.format(Math.floor(totalmin % 60));
		if (!days.equals("0")) {
			output = days;
		} else {
			output += "00";
		}
		if (!hours.equals("0")) {
			output += ":" + hours;
		} else {
			output += ":00";
		}
		if (!min.equals("0")) {
			output += ":" + min;
		} else {
			output += ":00";
		}
		return output;
	}

	public long ConvertHoursToMins(String hours) {
		long StrMins = (long) 0;
		if (!hours.equals("0") && hours.contains(":")) {
			String splithours[] = hours.split(":");
			if (splithours.length != 0 && !splithours[0].equals("0")) {
				StrMins = (Long.parseLong(splithours[0]) * 60);
			}

			if (splithours.length > 1 && !splithours[1].equals("0")) {
				StrMins += Long.parseLong(splithours[1]);
			}
		}
		return StrMins;
	}

	public String SplitHour(String strDateTime) {
		String sm = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sm = new SimpleDateFormat("HH").format(date);
			}
			return sm;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SplitMin(String strDateTime) {
		String sm = "";
		try {
			if (strDateTime != null && !strDateTime.equals("")) {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
				sm = new SimpleDateFormat("mm").format(date);
			}
			return sm;
		} catch (Exception ex) {
			logger.error("Axelaauto== " + this.getClass().getName());
			logger.error("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
