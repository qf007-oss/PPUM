import java.io.*;
import java.util.*;

public class testJudge {
    public static void main(String[] args) {
        String before = "C:\\study\\PPPUM\\PHM\\test\\Before_retail.txt";
        String after = "C:\\study\\PPPUM\\PHM\\test\\After0.03_retail.txt";
        String sensitive = "C:\\study\\PPPUM\\PHM\\test\\Sensitive0.03_retail.txt";
        String compare = compare(before, after, sensitive);
        System.out.println(compare);
    }
    private static String compare(String before, String after, String sensitive) {
        // get HF , MC , AC
        StringBuffer sb = new StringBuffer();
        BufferedReader brb = null;
        BufferedReader bra = null;
        BufferedReader sks = null;
        BufferedWriter result = null;

        List<List<Integer>> correctItemList = new ArrayList<>();//原始数据集中的非敏感高效用项集
        List<List<Integer>> beforeAllItemList = new ArrayList<>();//涵盖所有原始的高效用项集，即包括敏感项集
        List<List<Integer>> afterAllItemsList = new ArrayList<>();//新数据库挖掘的高效用项集
        List<List<Integer>> selectSensitive = new ArrayList<>();//选择的敏感项集
        List<List<Integer>> lostItemset = new ArrayList<>();//丢失的非敏感项集  MC
        List<List<Integer>> errorItemset = new ArrayList<>();//新增的伪项集 AC
        List<List<Integer>> failItemset = new ArrayList<>();//隐藏失败的项集 HF

        try {
            brb = new BufferedReader(new FileReader(new File(before)));
            bra = new BufferedReader(new FileReader(new File(after)));
            sks = new BufferedReader(new FileReader(new File(sensitive)));
            result = new BufferedWriter(new FileWriter(new File("C:/study/PPPUM/PHM/test/McAc.txt")));
            String line = null;
            // 读取原始高效用项集文件
            while (null != (line = brb.readLine())) {
                // 数据格式为 项集:效用值:支持度:minPer:maxPer:avgPer
                String[] kv = line.split(":");
                List<Integer> bef = new ArrayList<>();
                String items = kv[0];
                String[] split = items.split(" "); // 项集
                for (String s : split) {
                    int parseInt = Integer.parseInt(s);
                    bef.add(parseInt);
                }
                bef.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                });
                beforeAllItemList.add(bef);
            }
            //System.out.println(beforeAllItemList);
            brb.close();
            // 读取处理后的高效用项集文件
            while (null != (line = bra.readLine())) {
                String[] kv = line.split(":");
                List<Integer> aft = new ArrayList<>();
                String items = kv[0];
                String[] split = items.split(" "); // 项集
                for (String s : split) {
                    int parseInt = Integer.parseInt(s);
                    aft.add(parseInt);
                }
                aft.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                });
                afterAllItemsList.add(aft);
            }
            //System.out.println(afterAllItemsList);
            bra.close();
            // 读取敏感高效用项集文件
            while (null != (line = sks.readLine())) {
                String[] kv = line.split(":");
                List<Integer> sens = new ArrayList<>();
                String items = kv[0];
                String[] split = items.split(" "); // 项集
                for (String s : split) {
                    int parseInt = Integer.parseInt(s);
                    sens.add(parseInt);
                }
                sens.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                });
                selectSensitive.add(sens);
            }
            //System.out.println(selectSensitive);
            sks.close();

            result.newLine();
            result.append("Total num is =" + beforeAllItemList.size());// 原始数据集总的高效用项集个数
            result.newLine();
            // missing rule.
            int lost = 0; // MC
            int hf = 0; // HF
            int ghost = 0; // AC

            // HF
            failItemset.addAll(afterAllItemsList);
            failItemset.retainAll(selectSensitive);
            hf = failItemset.size();
            // MC
            correctItemList.addAll(beforeAllItemList);
            correctItemList.removeAll(selectSensitive);//原始数据集中的非敏感高效用项集
            lostItemset.addAll(correctItemList);
            lostItemset.removeAll(afterAllItemsList);//原始数据集中的非敏感高效用项集,在清洗后的数据集中依然存在的项集
            lost=lostItemset.size();//原始数据集中的非敏感高效用项集,在清洗后的数据集中不存在的项集
            // AC
            errorItemset.addAll(afterAllItemsList); //新数据集中的所有高效用项集
            errorItemset.removeAll(selectSensitive);// 新数据集中的非敏感高效用项集
            errorItemset.removeAll(correctItemList);
            ghost = errorItemset.size();



            result.append("Lost=" + lost);
            result.append("\r\nMissingRate=" + lost * 1.0 / (beforeAllItemList.size() - selectSensitive.size()));
            result.newLine();
            result.append("Ghost=" + ghost);
            result.newLine();
            result.append("Failure=" + hf);

            sb.append("LOST=" + lost + "\n");
            sb.append("MC=" + lost * 1.0 / (beforeAllItemList.size() - selectSensitive.size()) + "\n");
            sb.append("HF=" + hf * 1.0 / selectSensitive.size() + "\n");
            sb.append("AC=" + ghost * 1.0 / afterAllItemsList.size() + "\n");

            result.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
}
