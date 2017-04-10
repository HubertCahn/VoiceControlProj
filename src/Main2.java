import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Main2 {

	static HashMap<String, Integer> hp = new HashMap<String, Integer>();
	static ArrayList<String> ListSen = new ArrayList<String>();
	static ArrayList<String> ListGram = new ArrayList<String>();

	public static ArrayList<String> readTxtFile(String ip) {
		ArrayList<String> txtList = new ArrayList<String>();
		try {
			String encoding = "UTF-16";
			File file = new File(ip);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] subTxt = lineTxt.split("\t");
					for (int i = 0; i < subTxt.length; i++) {
						if (subTxt[i].length() > 0) {
							txtList.add(subTxt[i]);
						}
					}
				}
				read.close();
				return txtList;
			} else {
				System.out.println("找不到指定的文件");
				return txtList;
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return txtList;
		}

	}

	public static String[] ngram(String text, int num) {
		int length = text.length() - num + 1;
		String[] temp = new String[length];
		for (int start = 0; start < length; start++) {
			temp[start] = text.substring(start, start + num);
		}
		return temp;
	}

	public static ArrayList<String> sen2gram(ArrayList<String> sen) {
		ArrayList<String> gram = new ArrayList<String>();
		for (String t : sen) {
			if (t.length() == 1) {
				gram.addAll(Arrays.asList(ngram(t, 1)));
			} else if (t.length() == 2) {
				gram.addAll(Arrays.asList(ngram(t, 1)));
				gram.addAll(Arrays.asList(ngram(t, 2)));
			} else {
				gram.addAll(Arrays.asList(ngram(t, 1)));
				gram.addAll(Arrays.asList(ngram(t, 2)));
				gram.addAll(Arrays.asList(ngram(t, 3)));
			}
		}
		return gram;
	}

	public static HashMap<String, Integer> addID(ArrayList<String> al) {
		HashMap<String, Integer> hp = new HashMap<String, Integer>();
		Integer value = new Integer(1);
		for (String s : al) {
			if (!hp.containsKey(s)) {
				hp.put(s, value);
				value++;
			}
		}
		System.out.println(value);
		return hp;
	}

	public static void outputFeature(HashMap<String, Integer> hp, String op) {
		try {
			String line = System.getProperty("line.separator");
			StringBuffer str = new StringBuffer();
			FileWriter fw = new FileWriter(op, true);

			Iterator iterator = hp.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Entry) iterator.next();
				str.append(entry.getKey() + ":" + entry.getValue()).append(line);
			}
			fw.write(str.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void outputTrain(String opt) {
		try {
			String line = System.getProperty("line.separator");
			StringBuffer str = new StringBuffer();
			StringBuffer datapath = new StringBuffer();
			ArrayList<String> sen4each = new ArrayList<String>();
			ArrayList<String> str2ar = new ArrayList<String>();
			ArrayList<String> gram4each = new ArrayList<String>();
			ArrayList<Integer> feat4sort = new ArrayList<Integer>();
			FileWriter fw = new FileWriter(opt, true);

			for (int i = 1; i < 12; i++) {
				datapath.append("/Users/Meng/Documents/我的文档/华为/LIBSVM/fun/" + String.valueOf(i) + ".txt");
				sen4each = readTxtFile(datapath.toString());
				for (String s : sen4each) {
					str.append(String.valueOf(i) + "\t");
					str2ar.add(s);
					gram4each = new ArrayList<String>(new HashSet<String>(sen2gram(str2ar)));  
					for (String t : gram4each) {
						feat4sort.add(Integer.valueOf(hp.get(t)));
					}
					Integer[] sort = feat4sort.toArray(new Integer[feat4sort.size()]);
					Arrays.sort(sort);
					for (Integer in : sort) {
						str.append(String.valueOf(in) + "\t");
					}
					str.append(line);
					fw.write(str.toString());
					str.delete(0, str.length());
					str2ar.clear();
					feat4sort.clear();
					gram4each.clear();
				}
				datapath.delete(0, datapath.length());
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String ipFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/pre_data.txt";
		String opFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/feature_data_gbm.txt";
		String optFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/train_data_gbm";

		ListSen = readTxtFile(ipFileName);
		ListGram = sen2gram(ListSen);
		hp = addID(ListGram);
		outputFeature(hp, opFileName);
		outputTrain(optFileName);
	}

}
