package com.ruoyousi.common.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	private static final DateFormat DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final TimeZone DefaultTimeZone = TimeZone
			.getTimeZone("Asia/Shanghai");

	private static final Locale DefaultLocale = Locale.CHINA;

	private DateUtil() {
	}

	public static boolean isWorkday(Date d) {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.setTime(d);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return ((day == Calendar.SUNDAY) || (day == Calendar.SATURDAY));
	}

	public static Date getYMDDate(Date d) {
		TimeZone.setDefault(DefaultTimeZone);
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.setTimeInMillis(d.getTime());
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date today() {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		Date now = c.getTime();
		return getYMDDate(now);
	}

	public static Date yesterday() {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.add(Calendar.DATE, -1);
		return getYMDDate(c.getTime());
	}

	public static Date lastWeek(Date d) {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.setTime(d);
		c.add(Calendar.WEEK_OF_MONTH, -1);
		return getYMDDate(c.getTime());
	}

	public static Date lastMonth(Date d, int months) {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.setTime(d);
		c.add(Calendar.MONTH, months);
		return getYMDDate(c.getTime());
	}

	public static Date lastYear(Date d, int years) {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		c.setTime(d);
		c.add(Calendar.YEAR, years);
		return getYMDDate(c.getTime());
	}

	public static Date now() {
		Calendar c = Calendar.getInstance(DefaultTimeZone, DefaultLocale);
		return c.getTime();
	}

	public static Date parse(String s) {
		if (s == null) return null;
		try {
			return getYMDDate(DateFormat.parse(s));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String format(Date d) {
		if (d == null) return null;
		return DateFormat.format(d);
	}

}