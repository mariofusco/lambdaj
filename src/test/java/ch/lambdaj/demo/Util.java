package ch.lambdaj.demo;

import java.text.*;
import java.util.*;

public class Util {

	public static final DateFormat DEFAUALT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Date formatDate(String date) {
		try {
			return DEFAUALT_DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getCurrentYear() {
		return getYear(new Date());
	}
	
	public static int getYear(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static boolean listsAreEqual(List<?> list1, List<?> list2) {
		if (list1 == null && list2 == null) return true;
		if (list1 == null || list2 == null) return false;
		if (list1.size() != list2.size()) return false;
		for (int i = 0; i < list1.size(); i++) if (!list1.get(i).equals(list2.get(i))) return false;
		return true;
	}
}
