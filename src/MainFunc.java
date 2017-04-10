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

import org.ansj.splitWord.analysis.ToAnalysis;

public class MainFunc {

	public static Integer id = new Integer(0);//特征id
	public static HashMap<String, Integer> hp = new HashMap<String, Integer>();
//	public static HashSet<Integer> verb = new HashSet<Integer>();
	public static Integer x = new Integer(0); //功能类别
	public static Integer[] out = new Integer[id];

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

	public static HashSet<String> sen2gram(ArrayList<String> sen) {
		HashSet<String> words = new HashSet<String>();
		for (String s : sen) {
			String[] sub = ToAnalysis.parse(s).toString().split(",");
			for (String word : sub) {
				words.add(word);
			}
		}
		// System.out.println(words);
		return words;
	}

	public static HashMap<String, Integer> addID(HashSet<String> al) {
		for (String s : al) {
			if (!hp.containsKey(s)) {
				hp.put(s, id);
				id++;
			}
		}
		System.out.println(id);
		return hp;
	}

	public static void outputFeature(HashMap<String, Integer> hp, String op) {
		try {
			String line = System.getProperty("line.separator");
			StringBuffer str = new StringBuffer();
			FileWriter fw = new FileWriter(op);

			Iterator iterator = hp.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Entry) iterator.next();
				str.append(entry.getKey() + ":" + entry.getValue()).append(line);
			}
			fw.write(str.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void outputTrain(ArrayList<String> al, String op) {

		try {
			FileWriter fw = new FileWriter(op, true);
			ArrayList<String> gram4each = new ArrayList<String>();
			ArrayList<Integer> sort = new ArrayList<Integer>();
			ArrayList<String> st = new ArrayList<String>();

			for (String s : al) {
				StringBuffer text = new StringBuffer();
				st.add(s);
				// gram4each--sentence
				gram4each = new ArrayList<String>(new HashSet<String>(sen2gram(st)));
				fw.write(x.toString() + "\t");
				/* 全零数组初始化 */
				Integer[] feat = new Integer[id];
				for (int i = 0; i < id; i++) {
					feat[i] = 0;
				}
				/*改变对应特征的值*/
				for (String t : gram4each) {
					feat[Integer.valueOf(hp.get(t))] = 1;
				}
				/*打印*/
				for (int k = 0; k < id; k++) {
					if(k != id-1){
						fw.write(Integer.toString(feat[k]) + "\t");
					}
					else {
						fw.write(Integer.toString(feat[k]));
					}
				}

				fw.write("\n");
				st.clear();
				text.delete(0, text.length());
				gram4each.clear();
			}
			x++;
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String ipFileName = "/Users/leoymr/Documents/华为实习/data/pre_data.txt";
		String opFileName = "/Users/leoymr/Documents/华为实习/data/feature.txt";
		String opFileName2 = "/Users/leoymr/Documents/华为实习/data/sentence";
		String[] str = { "1.txt", "2.txt", "3.txt", "4.txt", "5.txt", "6.txt", "7.txt", "8.txt", "9.txt", "10.txt",
				"11.txt" };

		ArrayList<String> ListSen = new ArrayList<String>();
		HashSet<String> ListGram = new HashSet<String>();
		HashMap<String, Integer> hp1 = new HashMap<String, Integer>();

		/**
		 * 生成feature
		 */
		ListSen = readTxtFile(ipFileName);
		ListGram = sen2gram(ListSen);
		hp1 = addID(ListGram);
		outputFeature(hp1, opFileName);
		/**
		 * 生成sentence
		 */

		for (int i = 0; i < 11; i++) {
			ArrayList<String> sen = new ArrayList<String>();
			sen = readTxtFile("/Users/leoymr/Documents/华为实习/data/func/" + str[i]);
			outputTrain(sen, opFileName2);
		}

	}

}
