/**
 * EnterpriseSheet - online spreadsheet solution
 * Copyright (c) FeyaSoft Inc 2015. All right reserved.
 * http://www.enterpriseSheet.com
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cubedrive.base.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Strings;

public class DateTimeUtil {

	public static final long ONE_DAY_MILLS = 24 * 60 * 60 * 1000L;

	public static final long ONE_HOUR_MILLS = 60 * 60 * 1000L;

	public static final String ymdFmt = "yyyy-MM-dd";

	public static final String ymdHmFmt = "yyyy-MM-dd HH:mm";

	public static final String ymdHmsFmt = "yyyy-MM-dd HH:mm:ss";

	private static final int[] monthDays = new int[]{0, 31, 28, 31, 30, 31,
			30, 31, 31, 30, 31, 30, 31};


	private static DateTimeFormatter dateTimeFormatterWithTimeZone(TimeZone tz, String dateTimeFmt) {
		return DateTimeFormat.forPattern(dateTimeFmt).withZone(DateTimeZone.forTimeZone(tz));
	}


	private DateTimeUtil() {
	}

	private static final DateTimeFormatter _YMd = DateTimeFormat.forPattern(ymdFmt);

	private static final DateTimeFormatter _YMdHms = DateTimeFormat.forPattern(ymdHmsFmt);


	public static String formatAsYYYYMMdd(Date date) {
		if (date != null)
			return _YMd.print(date.getTime());
		return null;
	}

	public static String formatAsYYYYMMddHHmmss(Date date) {
		if (date != null)
			return _YMdHms.print(date.getTime());
		return null;
	}

	public static Date parseAsYYYYMMdd(String _dateString) {
		if (!Strings.isNullOrEmpty(_dateString)) {
			return _YMd.parseDateTime(_dateString).toDate();
		}
		return null;
	}

	public static Date parseAsYYYYMMddHHmmss(String _dateString) {
		if (!Strings.isNullOrEmpty(_dateString)) {
			if (_dateString.length() == 16) {
				_dateString = _dateString + ":00";
			}
			return _YMdHms.parseDateTime(_dateString).toDate();
		}
		return null;
	}

	public static String formatAsPattern(Date date, String pattern) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.toString(pattern);
	}

	public static String getToday() {
		return DateTime.now().toString(_YMd);
	}

	public static Date today() {
		return LocalDate.now().toDate();
	}

	public static Date parseAsPattern(String _dateString, String pattern) {
		DateTimeFormatter _formatter = DateTimeFormat.forPattern(pattern);
		return _formatter.parseDateTime(_dateString).toDate();
	}

	public static Date addYear(Date date, int n) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.plusYears(n).toDate();
	}

	public static Date addMonth(Date date, int n) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.plusMonths(n).toDate();
	}

	public static Date addDay(Date date, int n) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.plusDays(n).toDate();
	}


	public static Date addHourMin(Date date, int hours, int minutes) {
		long duration = hours * (60 * 60 * 1000L) + minutes * (60 * 1000L);
		return new Date(date.getTime() + duration);
	}

	public static Date getWeekDay(Date date, int dayOfWeek) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.withDayOfWeek(dayOfWeek).toDate();
	}

	public static int getYear(Date date) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.getYear();
	}



	public static int getMonth(Date date) {
		return new DateTime(date.getTime()).getMonthOfYear();
	}

	public static String weekdayOfNextWeek(Date date, int weekday) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.withDayOfWeek(weekday).plusDays(7)
				.toString("EEE dd MMM, yyyy");
	}

	public static String weekdayOfThisWeek(Date date, int weekday) {
		DateTime dateTime = new DateTime(date.getTime());
		return dateTime.withDayOfWeek(weekday).toString("EEE dd MMM, yyyy");
	}

	public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
		DateTime _start = new DateTime(startDate.getTime());
		DateTime _end = new DateTime(endDate.getTime());

		DateTime _startFriday = _start.withDayOfWeek(5);
		int _startInterval = Days.daysBetween(_start, _startFriday).getDays();
		if (_startInterval < 0)
			_startInterval = 0;

		DateTime _lastEndFriday = _end.withDayOfWeek(5).minusWeeks(1);
		int _endInterval = _end.getDayOfWeek();

		if (_endInterval >= 6)
			_endInterval = 5;

		int days = Days.daysBetween(_startFriday, _lastEndFriday).getDays();
		int weeks = Weeks.weeksBetween(_startFriday, _lastEndFriday).getWeeks();

		return days - (weeks * 2) + _startInterval + _endInterval;

	}

	/**
	 * return week of day by date
	 *
	 * @param date
	 * @return 0 - 6 (Sunday - Sat)
	 */
	public static int getWeekOfDay(Date date) {
		return getWeekOfDay2(date) - 1;
	}

	/**
	 * return week of day by date
	 *
	 * @param date
	 * @return 1 - 7 (Monday - Sunday)
	 */
	public static int getWeekOfDay2(Date date) {
		return new DateTime(date.getTime()).getDayOfWeek();
	}

	/**
	 * return week of month by date
	 *
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	// check to see whether it is the same week day
	// For example, 1st Monday of month
	// 3rd Friday in month
	public static Date nextMonthOfTheSameWeekday(Date date, int monthNum) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		int weekOfMonth = cal1.get(Calendar.WEEK_OF_MONTH);
		int dayOfWeek = cal1.get(Calendar.DAY_OF_WEEK);
		int year = cal1.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);

		Calendar cal2 = Calendar.getInstance();
		// because calendar's lenient default vaule is true,so we set month more than 12,the year will auto computer
		cal2.set(Calendar.YEAR, year);
		cal2.set(Calendar.MONTH, month + monthNum);
		cal2.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
		cal2.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		return cal2.getTime();
	}

	public static Date nextMonthOfTheSameDay(Date currentDate, int monthNum) {
		return addMonth(currentDate, monthNum);
	}

//	public static int numberOfWeekdayInMonth(Date date) {
//		return new DateTime(date.getTime()).getDayOfMonth() / 7;
//	}

	// check to see whether it is the same week day
	// For example, 1st Monday of month... Need pass two parameters
	// 3rd Friday in month
	// This will be used for email notification for calendar event ...
//	public static boolean isSameWeekDayInMonth(Date date1, Date date2) {
//		boolean match = false;
//
//		// check whether the date is same - for example Monday, Tuesday etc ...
//		if (getWeekOfDay(date1) == getWeekOfDay(date2)
//				&& numberOfWeekdayInMonth(date1) == numberOfWeekdayInMonth(date2)) {
//			match = true;
//		}
//
//		return match;
//	}

	// this will check whether it is same day in 2 date ..
	// For example, Nov 29, Oct 29 will be return true ...
	public static boolean isSameDayInMonth(Date date1, Date date2) {
		return new DateTime(date1.getTime()).getDayOfMonth() == new DateTime(
				date2.getTime()).getDayOfMonth();
	}

	// this will check whether it is same week in same year
	// need more test ...
	public static boolean isSameWeekInSameYear(Date date1, Date date2) {
		DateTime dateTime1 = new DateTime(date1.getTime());
		DateTime dateTime2 = new DateTime(date2.getTime());
		return (dateTime1.getYear() == dateTime2.getYear() && dateTime1
				.getWeekOfWeekyear() == dateTime2.getWeekOfWeekyear()) ? true
				: false;
	}

	public static boolean isSameMonthYear(Date date1, Date date2) {
		DateTime dateTime1 = new DateTime(date1.getTime());
		DateTime dateTime2 = new DateTime(date2.getTime());
		return (dateTime1.getYear() == dateTime2.getYear() && dateTime1
				.getMonthOfYear() == dateTime2.getMonthOfYear()) ? true : false;
	}

	public static boolean isSameDayMonth(Date date1, Date date2) {
		DateTime dateTime1 = new DateTime(date1.getTime());
		DateTime dateTime2 = new DateTime(date2.getTime());
		return (dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear() && dateTime1
				.getDayOfYear() == dateTime2.getDayOfYear()) ? true : false;
	}

	// this wil get the last Friday of month and year ...
	public static Date getLastFriday(int month, int year) {
		int monthLastDay = monthDays[month];
		if (month == 2 && isLeapYear(year)) {
			monthLastDay += 1;
		}
		DateTime localDateAtMonthLastDay = new DateTime(year, month, monthLastDay, 0, 0, 0);
		int dayOfWeekAtMonthLastDay = localDateAtMonthLastDay.getDayOfWeek();
		if (dayOfWeekAtMonthLastDay == 5) {
			return localDateAtMonthLastDay.toDate();
		} else {
			if (dayOfWeekAtMonthLastDay < 5) {
				return localDateAtMonthLastDay.minusDays(
						dayOfWeekAtMonthLastDay + 2).toDate();
			} else {
				return localDateAtMonthLastDay.withDayOfWeek(5).toDate();
			}
		}

	}

	/**
	 * set start end date period...
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int escapeDays(Date startDate, Date endDate) {
		int result = -1;
		Long mills = endDate.getTime() - startDate.getTime();

		try {
			result = (int) (mills / 86400000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * set start end date period...
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int escapeMonths(Date startDate, Date endDate) {
		DateTime _startDateTime = new DateTime(startDate.getTime());
		DateTime _endDateTime = new DateTime(endDate.getTime());
		int sy = _startDateTime.getYear();
		int ey = _endDateTime.getYear();
		int sm = _startDateTime.getMonthOfYear();
		int em = _endDateTime.getMonthOfYear();
		return ey * 12 + em - sy * 12 - sm;
	}

	/**
	 * set start end date period...
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int escapeMins(Date startDate, Date endDate) {
		int result = -1;
		Long mills = endDate.getTime() - startDate.getTime();

		try {
			result = (int) (mills / 60000);
		} catch (Exception e) {
			// do nothing
		}

		return result;
	}

	public static int escapeSeconds(Date startDate, Date endDate) {
		int result = -1;
		Long mills = endDate.getTime() - startDate.getTime();

		try {
			result = (int) (mills / 1000);
		} catch (Exception e) {
			// do nothing
		}

		return result;
	}

	// this is midnight date ...
	public static Date getMidnightDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	public static Date getLastMinDate(Date date) {
		return addHourMin(new DateMidnight(date.getTime()).toDate(), 23, 59);
	}

	// this will compare date based on midnight date
	// this will be based on MMM dd, YYY
	// ignore HH:mm
	public static boolean equalAfter(Date firstDate, Date secondDate) {
		boolean result = false;

		Date firstOne = getMidnightDate(firstDate);
		Date secondOne = getMidnightDate(secondDate);

		if (firstOne.equals(secondOne) || firstOne.after(secondOne)) {
			result = true;
		}

		return result;
	}
	
	public static Date timeZoneDate(Date date, int gmtMinutes, String serverTimeZoneGmt) {

		int myDiffMin = gmtMinutes - Integer.parseInt(serverTimeZoneGmt);
		return new DateTime(date.getTime()).minusMinutes(myDiffMin).toDate();

	}

	// this will change date to 23:59:00
	public static Date change2Lastmin(Date enterDate) {
		return new DateMidnight(enterDate.getTime()).plusDays(1).minus(60000L).toDate();
	}

	// check whether it is same day
	public static boolean isSameDay(Date day1, Date day2) {
		return new LocalDate(day1.getTime()).compareTo(new LocalDate(day2
				.getTime())) == 0 ? true : false;
	}

	// pass a date and reutn a string - if less than 24 hours, ....
	public static String getTimeDesc(Date updateDate) {
		String updateTime;
		int escpaseMins = escapeMins(updateDate, new Date());
		int hours = (int) (escpaseMins / 60);
		if (hours == 0)
			updateTime = escpaseMins % 60 + " minutes ago";
		else
			updateTime = hours + " hours " + escpaseMins % 60 + " minutes ago";

		return updateTime;
	}


	public static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? true
				: false;
	}

	public static Date nextMonthFirstDay(Date currentDate, int monthNum) {
		LocalDate currentLocalDate = new LocalDate(currentDate);
		return currentLocalDate.plusMonths(monthNum).withDayOfMonth(1).toDate();
	}


	public static long timeInMills(Date dateTime) {
		DateTime dt = new DateTime(dateTime);
		return dt.getMillisOfDay();
	}

	public static Date parseUserTimeStringAsUtc(String userDateString, TimeZone userTimeZone, String dateTimeFmt) {
		DateTimeFormatter dateTimeFormatterWithTimeZone = dateTimeFormatterWithTimeZone(userTimeZone, dateTimeFmt);
		return dateTimeFormatterWithTimeZone.parseDateTime(userDateString).toDate();
	}


	public static String formatUtcAsUserTime(Date date, TimeZone userTimeZone, String dateTimeFmt) {
		DateTimeFormatter dateTimeFormatterWithTimeZone = dateTimeFormatterWithTimeZone(userTimeZone, dateTimeFmt);
		return dateTimeFormatterWithTimeZone.print(date.getTime());
	}
	

	public static Date parseUserCalendarAsUtc(Calendar userCalendar, TimeZone userTimeZone) {
		String dateString = String.format("%d-%d-%d %d:%d:%d", userCalendar.get(Calendar.YEAR),
				userCalendar.get(Calendar.MONDAY) + 1,
				userCalendar.get(Calendar.DAY_OF_MONTH),
				userCalendar.get(Calendar.HOUR_OF_DAY),
				userCalendar.get(Calendar.MINUTE),
				userCalendar.get(Calendar.SECOND));
		return parseUserTimeStringAsUtc(dateString, userTimeZone, ymdHmsFmt);

	}

}
