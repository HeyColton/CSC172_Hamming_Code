/*Name:Xiaoyu Zheng
 * Email:xzheng10@u.rochester.edu
 * Project number:1
 * Lab Section: Tue 2:00 pm and Thu 2:00 pm
 */
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Hamming {
	public static void encode_7_4(String inFileName, String outFileName) {
		// printwriter is used at the end
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(outFileName, false));
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// each time input one byte
		FileInputStream is;
		try {
			is = new FileInputStream(inFileName);
			int c;

			while ((c = is.read()) != -1) {
				// convert byte into string and cut it into two parts
				String inFile = Integer.toBinaryString(c);
				String strings = ("00000000" + inFile).substring(inFile.length());
				int mid = strings.length() / 2;
				String[] parts = { strings.substring(0, mid), strings.substring(mid) };
				String s1 = parts[0];
				String s2 = parts[1];
				// call twoString method
				twoString(s1, outFileName);
				twoString(s2, outFileName);
			}
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void twoString(String inFile, String outFileName) {
		ArrayList<Integer> list = new ArrayList<Integer>(8);
		list.add(0, 0);
		int dataIndex = 0;
		// convert string into a list
		for (int i = 1; i <= 7; i++) {
			if (i != 1 & i != 2 & i != 4) {
				list.add(i, Character.getNumericValue(inFile.charAt(dataIndex)));
				dataIndex++;
			} else {
				list.add(i, 0);
			}
		}
		// set parity bits
		int[] FirstDigit = { 1, 3, 5, 7 };
		list.set(1, sum(list, FirstDigit) % 2);
		int[] SecondDigit = { 2, 3, 6, 7 };
		list.set(2, sum(list, SecondDigit) % 2);
		int[] FourthDigit = { 4, 5, 6, 7 };
		list.set(4, sum(list, FourthDigit) % 2);
		// build a string for output
		StringBuilder stringBuilder = new StringBuilder(8);
		for (int i = 0; i <= 7; i++) {
			stringBuilder.append(list.get(i));
		}
		String string = stringBuilder.toString();

		int c = Integer.parseInt(string, 2);

		try {
			PrintWriter writer = new PrintWriter(new FileWriter(outFileName, true));
			writer.print(Character.toString((char) c));
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void encode_15_11(String inFileName, String outFileName) {

		FileInputStream is;
		try {
			is = new FileInputStream(inFileName);
			int c;
			PrintWriter writer = new PrintWriter(outFileName, "ISO-8859-1");
			while ((c = is.read()) != -1) {
				// convert 1 byte into 8-char string into 11, and then into 16
				String inFile = Integer.toBinaryString(c);
				String strings = ("00000000000" + inFile).substring(inFile.length());
				ArrayList<Integer> list = new ArrayList<Integer>(16);
				list.add(0, 0);
				int dataIndex = 0;
				// convert string into list and leave parity bits
				for (int i = 1; i <= 15; i++) {
					if (i != 1 & i != 2 & i != 4 & i != 8) {
						list.add(i, Character.getNumericValue(strings.charAt(dataIndex)));
						dataIndex++;
					} else {
						list.add(i, 0);
					}
				}
				// set parity bits
				int[] FirstDigit = { 1, 3, 5, 7, 9, 11, 13, 15 };
				list.set(1, sum(list, FirstDigit) % 2);
				int[] SecondDigit = { 2, 3, 6, 7, 10, 11, 14, 15 };
				list.set(2, sum(list, SecondDigit) % 2);
				int[] FourthDigit = { 4, 5, 6, 7, 12, 13, 14, 15 };
				list.set(4, sum(list, FourthDigit) % 2);
				int[] EighthDigit = { 8, 9, 10, 11, 12, 13, 14, 15 };
				list.set(8, sum(list, EighthDigit) % 2);
				// build two byte strings
				StringBuilder stringBuilder1 = new StringBuilder(8);
				StringBuilder stringBuilder2 = new StringBuilder(8);
				for (int i = 0; i <= 7; i++) {
					stringBuilder1.append(list.get(i));
				}
				for (int i = 8; i <= 15; i++) {
					stringBuilder2.append(list.get(i));
				}
				String string1 = stringBuilder1.toString();
				String string2 = stringBuilder2.toString();
				c = Integer.parseInt(string1, 2);
				int d = Integer.parseInt(string2, 2);
				// print out
				writer.print(Character.toString((char) c));
				writer.print(Character.toString((char) d));

			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static int sum(ArrayList<Integer> list, int[] num) {
		int n = num.length;
		int sum = 0;
		for (int i = 1; i <= n - 1; i++) {
			sum += list.get(num[i]);
		}
		return sum;
	}

	public static void decode_7_4(String inFileName, String outFileName) {
		try {
			PrintWriter writer = new PrintWriter(outFileName, "ISO-8859-1");

			StringBuilder Builder = null;
			FileInputStream is;
			try {
				// read int one byte
				is = new FileInputStream(inFileName);
				int c;
				int count = 0;
				while ((c = is.read()) != -1) {
					count++;
					String inFile = Integer.toBinaryString(c);
					String strings = ("00000000" + inFile).substring(inFile.length());
					ArrayList<Integer> list = new ArrayList<Integer>(8);
					// convert one byte into a list
					for (int i = 0; i <= 7; i++) {

						list.add(i, Character.getNumericValue(strings.charAt(i)));

					}
					// check if there are wrongs in parity bits
					int[] FirstDigit = { 1, 3, 5, 7 };
					int[] SecondDigit = { 2, 3, 6, 7 };
					int[] FourthDigit = { 4, 5, 6, 7 };
					Boolean b1 = Parity(list, FirstDigit, list.get(1));
					Boolean b2 = Parity(list, SecondDigit, list.get(2));
					Boolean b4 = Parity(list, FourthDigit, list.get(4));
					int correctNo = 0;
					// if wrong, i is in the list. If right, i is not in the
					// list
					for (int i = 1; i <= 7; i++) {
						if (check(i, FirstDigit, b1) == true & check(i, SecondDigit, b2) == true
								& check(i, FourthDigit, b4) == true) {
							correctNo = i;
							break;
						}
					}
					// set the correct value
					if (correctNo != 0) {
						list.set(correctNo, (list.get(correctNo) + 1) % 2);
					}
					// every two times start to build one string
					if (count % 2 != 0) {

						Builder = new StringBuilder(8);
					}
					StringBuilder stringBuilder = new StringBuilder(4);
					stringBuilder.append(list.get(3));
					stringBuilder.append(list.get(5));
					stringBuilder.append(list.get(6));
					stringBuilder.append(list.get(7));
					String string = stringBuilder.toString();
					Builder.append(string);
					String strings1 = Builder.toString();
					// every two times print one string
					if (count % 2 == 0) {

						c = Integer.parseInt(strings1, 2);

						writer.print(Character.toString((char) c));

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void decode_15_11(String inFileName, String outFileName) {
		FileInputStream is;
		try {
			// read two bytes to deal with together
			PrintWriter writer = new PrintWriter(outFileName, "ISO-8859-1");
			is = new FileInputStream(inFileName);
			int c;
			int count = 0;
			StringBuilder builder = null;
			while ((c = is.read()) != -1) {

				count++;
				// every two times start to build one string
				if (count % 2 != 0) {
					builder = new StringBuilder();
				}
				String inFile = Integer.toBinaryString(c);
				String s = ("00000000" + inFile).substring(inFile.length());
				builder.append(s);
				// every two times, deal with the string
				if (count % 2 == 0) {
					String string = builder.toString();
					String strings = ("0000000000000000" + string).substring(string.length());
					ArrayList<Integer> list = new ArrayList<Integer>(16);
					for (int i = 0; i <= 15; i++) {

						list.add(i, Character.getNumericValue(strings.charAt(i)));
					}
					// check if any parity is wrong
					int[] FirstDigit = { 1, 3, 5, 7, 9, 11, 13, 15 };
					int[] SecondDigit = { 2, 3, 6, 7, 10, 11, 14, 15 };
					int[] FourthDigit = { 4, 5, 6, 7, 12, 13, 14, 15 };
					int[] EighthDigit = { 8, 9, 10, 11, 12, 13, 14, 15 };
					Boolean p1 = (((list.get(3) + list.get(5) + list.get(7) + list.get(9) + list.get(11) + list.get(13)
							+ list.get(15)) % 2) == list.get(1));
					Boolean p2 = (((list.get(14) + list.get(3) + list.get(6) + list.get(7) + list.get(10) + list.get(11)
							+ list.get(15)) % 2) == list.get(2));
					Boolean p4 = (((list.get(5) + list.get(6) + list.get(7) + list.get(12) + list.get(13) + list.get(14)
							+ list.get(15)) % 2) == list.get(4));
					Boolean p8 = (((list.get(9) + list.get(10) + list.get(11) + list.get(12) + list.get(13)
							+ list.get(14) + list.get(15)) % 2) == list.get(8));

					int correctNo = 0;
					for (int i = 1; i <= 15; i++) {
						if (check(i, FirstDigit, p1) == true & check(i, SecondDigit, p2) == true
								& check(i, FourthDigit, p4) == true & check(i, EighthDigit, p8) == true) {
							correctNo = i;

						}
					}

					if (correctNo != 0) {
						list.set(correctNo, (list.get(correctNo) + 1) % 2);
					}
					StringBuilder output = new StringBuilder(8);
					output.append(list.get(7));
					output.append(list.get(9));
					output.append(list.get(10));
					output.append(list.get(11));
					output.append(list.get(12));
					output.append(list.get(13));
					output.append(list.get(14));
					output.append(list.get(15));
					String out = output.toString();

					c = Integer.parseInt(out, 2);

					writer.print(Character.toString((char) c));

				}
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Boolean check(int i, int[] list, Boolean p) {
		int length = list.length;

		Boolean check;
		if (p == true) {
			check = true;
		} else {
			check = false;
		}
		for (int j = 0; j <= length - 1; j++) {
			if (list[j] == i) {
				if (p == true) {
					check = false;
				} else {
					check = true;
				}
			}
		}
		return check;

	}

	public static Boolean Parity(ArrayList<Integer> SuperList, int[] list, int value) {
		int length = list.length;
		int sum = 0;
		for (int i = 1; i < length; i++) {
			sum += SuperList.get(list[i]);
		}

		sum = sum % 2;

		return sum == value;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Hamming.encode_7_4("sample.txt","out.txt");
		// Hamming.encode_7_4("wiki.txt","out.txt");
		// Hamming.decode_7_4("sample_7_4.txt", "out.txt");
		// Hamming.decode_7_4("wiki_7_4.txt", "out.txt");
		// Hamming.decode_7_4("wiki_7_4_flip.txt", "out.txt");
		// Hamming.encode_7_4("sample.txt","out.txt");
		// Hamming.encode_7_4("wiki.txt","out.txt");
		// Hamming.encode_15_11("sample.txt","out.txt");
		// Hamming.encode_15_11("wiki.txt","out.txt");
		// Hamming.decode_15_11("sample_15_11.txt", "out.txt");
		// Hamming.decode_15_11("wiki_15_11_flip.txt", "out.txt");
		// Hamming.decode_15_11("wiki_15_11.txt", "out.txt");
		// int c;
		// String in = "in.txt";
		// String out = "out.txt";
		// try {
		// FileOutputStream os = new FileOutputStream(in, false);
		// while((c=System.in.read()) != -1) {
		// os.write(c);
		// }
		// os.close();
		// Hamming.decode_15_11(in, out);
		// FileInputStream is = new FileInputStream(out);
		// while((c=is.read()) != -1) {
		// System.out.write(c);
		// }
		// is.close();
		// System.out.flush();
		// }
		// catch(Exception e) {
		//
		// }
		// }
	}

}
