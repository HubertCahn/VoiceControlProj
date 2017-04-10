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


/*
 * TRANS_SVM类
 * 输入为整合控制命令和按类划分的控制命令，输出为标准LIBSVM格式，权重均为1
 */
public class Trans_ANSJ_SVM2 {

	static HashMap<String, Integer> hp = new HashMap<String, Integer>();
	static ArrayList<String> ListSen = new ArrayList<String>();
	static HashSet<String> ListGram = new HashSet<String>();

	/*
	 * 输入原始数据，按行读取并按制表符进行分割
	 */
	public static ArrayList<String> readTxtFile(String ip) {
		Integer k = new Integer(0);
		ArrayList<String> txtList = new ArrayList<String>();// 存储文件中所有的语句单元
		try {
			String encoding = "UTF-16";// 定义文件编码类型
			File file = new File(ip);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] subTxt = lineTxt.split("\t");
					for (int i = 0; i < subTxt.length; i++) {
						if (subTxt[i].length() > 0) {// 可以过滤特定长度的语句单元
							txtList.add(subTxt[i]);
							k++;// 记录语句单元的个数
						}
					}
				}
				read.close();
				System.out.println(k);
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

	/*
	 * 输入所有的语句单元，每个单元经ANSJ算法，返回所有语句单元的ANSJ处理结果
	 */
	public static HashSet<String> sen2gram(ArrayList<String> sen) {
		HashSet<String> words = new HashSet<String>();
		for (String s : sen) {
			String[] sub = ToAnalysis.parse(s).toString().split(",");
			for (String word : sub) {
				words.add(word);
			}
		}
		return words;
	}

	/*
	 * 输入所有语句单元的NGram处理结果，将各个特征放入到HashMap中并添加唯一的编号
	 */
	public static HashMap<String, Integer> addID(HashSet<String> al) {
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

	/*
	 * 输入特征字典，遍历HashMap中的每个key-pair并打印到指定文件中
	 */
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

	/*
	 * 读取按类分好的控制命令，输出为标准LIBSVM格式
	 */
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
				datapath.append("/Users/Meng/Documents/我的文档/华为/LIBSVM/fun/" + String.valueOf(i) + ".txt");// 循环读取各类别的控制语句
				sen4each = readTxtFile(datapath.toString());
				for (String s : sen4each) {// 循环处理每个语句单元
					str.append(String.valueOf(i) + " ");// 将类别打印到行首
					str2ar.add(s);// 将语句单元放入到list中
					gram4each = new ArrayList<String>(sen2gram(str2ar));// 除去单个语句单元中重复出现的特征
					for (String t : gram4each) {
						try {
							feat4sort.add(Integer.valueOf(hp.get(t)));// 将各个特征对应的编号放入到list中
						} catch (Exception e) {
							System.out.println("特征字典中找不到该特征:"+t);
						}
					}
					Integer[] sort = feat4sort.toArray(new Integer[feat4sort.size()]);
					Arrays.sort(sort);// 对特征的编号进行排序，以满足LIBSVM格式的要求
					for (Integer in : sort) {
						str.append(String.valueOf(in) + ":1" + " ");
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
		
		String ipFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/raw_data/pre_data.txt";
		String opFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/feature_data/feature_data_ANSJ";
		String optFileName = "/Users/Meng/Documents/我的文档/华为/LIBSVM/train_data/train_data_ANSJ";

		ListSen = readTxtFile(ipFileName);
		ListGram = sen2gram(ListSen);
		hp = addID(ListGram);
		outputFeature(hp, opFileName);
		outputTrain(optFileName);
	}
}
