import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SVM {
	public static void main(String[] args) throws IOException {
		String[] arg = { "/Users/Meng/Documents/我的文档/华为/LIBSVM/train_data123.txt", // 训练集
				"/Users/Meng/Documents/我的文档/华为/LIBSVM/model" }; // 存放SVM训练模型
		String[] parg = { "/Users/Meng/Documents/我的文档/华为/LIBSVM/test.txt", // 测试数据
				"/Users/Meng/Documents/我的文档/华为/LIBSVM/model", // 调用训练模型
				"/Users/Meng/Documents/我的文档/华为/LIBSVM/result" }; // 预测结果
		System.out.println("........SVM运行开始..........");
		long start = System.currentTimeMillis();
		svm_train.main(arg); // 训练
		System.out.println("用时:" + (System.currentTimeMillis() - start));
		// 预测
		//svm_predict.main(parg);
		/*do {
			System.out.println("请输入测试语句：");
			try {
				FileWriter fw = new FileWriter("/Users/Meng/Documents/我的文档/华为/LIBSVM/test.txt");
				Scanner sc = new Scanner(System.in);
				String test = sc.nextLine();
				fw.write(test);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			svm_predict.main(parg);
		} while (true);*/
	}
}