import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
// 没用到
public class effects {
	static StringBuffer sb = new StringBuffer();

	protected static Map<String, Double> sensitive = new HashMap<String, Double>();

	protected static Map<String, Double> RI1 = new HashMap<String, Double>();

	protected static Map<String, Double> RI2 = new HashMap<String, Double>();

	public static void main(String[] args) throws IOException {

		String before_db = "D:\\idea_workspace\\itheima_web_project\\PPRPM\\src\\main\\resources\\NewTest.txt";

		String after_db = "src/pprpm/NewTest.txt";

		String before = "temp_file/contextRP.txt";

		String after = "temp_file/contextRPP.txt";

		String SRIFile = "temp_file/contextRP.txt_si.txt";

		readSensitiveRI(SRIFile);

		readRI1(before);

		readRI2(after);

		double ius = getIUS(before, after);

		double dss = getDSS(before_db, after_db);
		System.out.println("ius :" + ius);
		System.out.println("dss :" + dss);

		sb.append(compare(before, after, sensitive.keySet()));
		 
		return;
	}

	protected static void readSensitiveRI(String SRIFile) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(new File(SRIFile)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] k_v = line.split("support :");
				double sup = Double.parseDouble(k_v[1]);
				sensitive.put(k_v[0], sup);
				 
			}
			br.close();
		} catch (Exception e) {

		} 
	}

	public static double getDSS(String before, String after) {
		Map<String, Integer> beforeMap = getBitSetMap(before);
		Map<String, Integer> afterMap = getBitSetMap(after);
		double sumBefore = 0;
		double sumAfter = 0;
		double sumProduct = 0;
		for (String key : beforeMap.keySet()) {
			sumBefore += Math.pow(beforeMap.get(key), 2);
			int tmp = afterMap.get(key) == null ? 0 : afterMap.get(key);
			sumAfter += Math.pow(tmp, 2);
			sumProduct += beforeMap.get(key) * tmp;
		}
		for (String key : afterMap.keySet()) {
			if (!beforeMap.keySet().contains(key)) {
				sumAfter += Math.pow(afterMap.get(key), 2);
			}
		}
		return sumProduct / (Math.sqrt(sumBefore) * Math.sqrt(sumAfter));
	}

	public static Map<String, Integer> getBitSetMap(String db) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(db)));
			String nul = "";
			String key = "";
			String line = null;
			while (null != (line = br.readLine())) {
				String[] items = line.split(" "); 
				BitSet bitSet = new BitSet();
				for (String item : items) {
					bitSet.set(Integer.parseInt(item), true);
				}
				key = bitSet.toString();
				if (map.get(key) == null) {
					map.put(key, 1);
				} else {
					map.put(key, map.get(key) + 1);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	private static void readRI2(String after) throws FileNotFoundException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(after)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] k_v = line.split("support :");
				double sup = Double.parseDouble(k_v[1]);
				RI2.put(k_v[0].trim(), sup);
				 
			}
			br.close();
		} catch (Exception e) {

		} 
	}

	private static void readRI1(String before) throws FileNotFoundException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(before)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] k_v = line.split("support :");
				double sup = Double.parseDouble(k_v[1]);
				RI1.put(k_v[0].trim(), sup);
				 
			}
			br.close();
		} catch (Exception e) {

		} 
	}

	private static double getIUS(String before, String after) {
		// TODO Auto-generated method stub
		double bu = 0;
		double au = 0;

		for (String key : RI1.keySet()) {
			bu += RI1.get(key);
		} 
		for (String key : RI2.keySet()) {
			au += RI2.get(key);
		}
		return au / bu; 
	}

	public static String compare(String before, String after, Set<String> sks) {
		StringBuffer sb = new StringBuffer();
		BufferedReader brb = null;
		BufferedReader bra = null;
		BufferedWriter result = null;
		Map<String, Double> b = new HashMap<String, Double>();
		Map<String, Double> a = new HashMap<String, Double>();
		try {
			brb = new BufferedReader(new FileReader(new File(before)));
			bra = new BufferedReader(new FileReader(new File(after)));
			result = new BufferedWriter(new FileWriter(new File("src/pprpm/ba_result.txt")));
			String line = null;

			while (null != (line = brb.readLine())) {
				//String[] kv = line.split("support :");
				String[] kv = line.split("#SUP:");
				b.put(kv[0], Double.parseDouble(kv[1]));
			}
			brb.close();
			while (null != (line = bra.readLine())) {
				//String[] kv = line.split("support :");
				String[] kv = line.split("#SUP:");
				a.put(kv[0], Double.parseDouble(kv[1]));
			}
			bra.close(); 
			result.newLine();
			result.append("Total num is =" + b.size());
			result.newLine();
			// missing rule.
			int lost = 0;
			int hf = 0;
			int ghost = 0;
			int hide = 0;

			for (String key : b.keySet()) {
				if (a.get(key) == null) {
					if (sks.contains(key)) {
//						System.out.println(key + "=" + b.get(key)
//								+ "---->sensitive.");
						result.append(key + "=" + b.get(key) + "---->hiding success");
						result.newLine();
						hide++;
					} else {
						// System.out
						// .println(key + "=" + b.get(key) + "---->lost");
						result.append(key + "=" + b.get(key) + "---->lost");
						result.newLine();
						lost++;
					}

				} else {
					if (sks.contains(key) && a.get(key) != null) { 
						result.append(key + "=" + b.get(key) + "----->hiding failure");
						hf++;
						result.newLine();
					}else{
						result.append(key + "=" + b.get(key) + "----->remain");
						result.newLine();
					}
				}
			}
			// artifical rule
			for (String key : a.keySet()) {
				if (!b.keySet().contains(key)) { 
					result.append(key + "=" + a.get(key) + "---->arificial");
					ghost++;
					result.newLine();
				} else {
					// System.out.println(key +"="+a.get(key)+"---->a large");
				}
			}
			result.append("HIDE=" + hide);
			sb.append("HIDE="+hide+"\n");
			result.newLine();
			result.append("Lost=" + lost);
			sb.append("LOST="+lost+"\n");
			sb.append("MC=" + lost * 1.0 / b.size()+"\n");
			result.append("\r\nMissingRate=" + lost * 1.0 / b.size());
			result.newLine();
			result.append("Ghost=" + ghost);
			result.newLine();
			result.append("Failure=" + hf);

			result.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	 
}
