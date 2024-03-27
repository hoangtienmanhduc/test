package vn.techres.order.online.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;

public class Utils {

	public static String convertTimestampToString(long timestamp) {
		try {
			// Create a Date object from the timestamp

			if (timestamp<= 0) {
				return "";
			}
			Date date = new Date(timestamp);

			// Format the Date object as a String
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(date);
		} catch (Exception e) {
			// Handle exceptions
			System.err.println("An unexpected error occurred: " + e.getMessage());
			return null; // or throw a custom exception, return a default value, etc.
		}
	}

	public static String getDatetimeFormatVN(Date date) {
		if (date == null) {
			return "";
		} else {
			return (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));
		}
	}
	public static <T> List<T> convertJsonStringToListObject(String jsonString, Class<T[]> objectclass) {
		try {
			jsonString = jsonString == null || jsonString.isEmpty() ? "[]" : jsonString;
			ObjectMapper mapper = new ObjectMapper();
			T[] array = mapper.readValue(jsonString, objectclass);
			return new ArrayList<>(Arrays.asList(array));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON string to list of objects", e);
		}

	}

	public static List<Integer> convertStringToArrayList(String arrayString) {
		// Check if the input string is empty or null
		if (arrayString == null || arrayString.isEmpty() || arrayString.equals("[]")) {
			return  new ArrayList<>();
		}

		String[] arrayElements = arrayString.substring(1, arrayString.length() - 1).split(",");

		// Convert array elements to integers and add to the ArrayList
		List<Integer> arrayList = new ArrayList<>();

		for (String element : arrayElements) {
			arrayList.add(Integer.parseInt(element.trim()));
		}

		return arrayList;
	}

	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static String convertListObjectToJsonArray(List<?> objects) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.writeValue(out, objects);
			final byte[] data = out.toByteArray();
			return new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> String convertListToJson(List<T> itemList) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(itemList);
		} catch (JsonProcessingException e) {
			e.printStackTrace(); // Handle the exception according to your needs
			return null;
		}
	}

	public static String convertObjectToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(object);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @format YYYY-MM-DD HH:mm, YYYY-MM-DD
	 * @return
	 * @throws ParseException
	 */
	public static boolean isCheckFoodUseTemporaryPrice(String fromDateString, String toDateString)
			throws ParseException {

		if (!Utils.isValidateUsingDateFormat(fromDateString))
			return false;

		if (!Utils.isValidateUsingDateFormat(toDateString))
			return false;

		long fromDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(fromDateString).getTime();
		long toDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(toDateString).getTime();
		long currentDate = new Date().getTime();

		if (currentDate > fromDate && currentDate < toDate)
			return true;

		return false;
	}

	public static boolean isValidateUsingDateFormat(String dateStr) {
		DateFormat sdf = new SimpleDateFormat(dateStr);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}



	public static String decodeBase64String(String s) {
		try {
			byte[] decodedBytes = BaseEncoding.base64().decode(s);
			return new String(decodedBytes);
		} catch (Exception e) {
			return "";
		}
	}

	public static List<Integer> convertStringToList(String s) {
		List<Integer> result = new ArrayList<>();

		// Check if the input string is empty
		if (s.isEmpty()) {
			return result; // Return an empty list if the input string is empty
		}

		// Split the input string by commas
		String[] stringArray = s.split(",");

		// Convert each substring to an integer and add to the result list
		for (String str : stringArray) {
			try {
				int num = Integer.parseInt(str);
				result.add(num);
			} catch (NumberFormatException e) {
				// Handle the case where a substring is not a valid integer if needed
				System.err.println("Invalid integer: " + str);
			}
		}

		return result;
	}
}
