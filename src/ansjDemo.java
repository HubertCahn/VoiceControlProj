import java.util.Arrays;

import org.ansj.splitWord.analysis.ToAnalysis;

public class ansjDemo {
	public static void main(String[] args) {
		String str = "人生若能永远像两三岁小孩，本来没有责任，那就本来没有苦。到了长成，责任自然压在你的肩头上，如何能躲？不过有大小的分别罢了。尽得大的责任，就得大快乐；尽得小的责任，就得小快乐。你若是要躲，倒是自投苦海，永远不能解除了。";
		String[] sub = ToAnalysis.parse(str).toString().split(",");
		System.out.println(Arrays.toString(sub));
		}
	}