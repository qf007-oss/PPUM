import java.io.*;
import java.util.*;

public class TestAns {
    protected static double getDUS(String before, String after) {
        BufferedReader brBefore = null;
        BufferedReader brAfter = null;
        double bu = 0;
        double au = 0;
        try {

            brBefore = new BufferedReader(new FileReader(new File(before)));

            String lineBefore = null;
            while (null != (lineBefore = brBefore.readLine())) {
                String[] arr1 = lineBefore.split(":");
                bu += Integer.parseInt(arr1[1]);
            }
            brBefore.close();

            // =================================================
            brAfter = new BufferedReader(new FileReader(new File(after)));

            String lineAfter = null;
            while (null != (lineAfter = brAfter.readLine())) {
                String[] arr2 = lineAfter.split(":");
                String items[] = arr2[0].split(" ");

                if(items.length==1&&-1==Integer.parseInt(items[0])){
                    continue;
                }
                au += Integer.parseInt(arr2[1]);
            }
            brAfter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return au / bu;
    }


    /**
     * Get IUS = sum(u(X)) -- X in HUIs D'  / sum(u(X)) -- X in HUIs D
     *
     * @param bString
     * @param aString
     * @return
     */
    protected static double getIUS(String bString, String aString, Set<String> sks) {
        Map<String, Object> hui = getMapFromFile(bString, 1, sks);
        double bu = 0;
        double au = 0;

        for (String key : hui.keySet()) {
            bu += Double.parseDouble((String) hui.get(key));
        }

        hui = getMapFromFile(aString, 2, sks);
        for (String key : hui.keySet()) {
            au += Double.parseDouble((String) hui.get(key));
        }

        return au / bu;
    }


    /**
     * Get DSS
     *
     * @param before 原始数据集
     * @param after  处理后的数据集
     * @return
     */
    protected static double getDSS(String before, String after) {
        Map<String, Integer> beforeMap = getBitSetMap(before, " ");
        Map<String, Integer> afterMap = getBitSetMap(after, " ");

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

        // the sum product
        return sumProduct / (Math.sqrt(sumBefore) * Math.sqrt(sumAfter));
    }

    /**
     * Get bitSet map
     *
     * @param db
     * @param spliter 分割符 " "
     * @return Map<String, Integer> map
     */
    public static Map<String, Integer> getBitSetMap(String db, String spliter) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(db)));
            String key = "";
            String line = null;

            while (null != (line = br.readLine())) {
                //数据格式 "[项目:数量] [项目:数量] [项目:数量]"
                String[] items = line.split(spliter); // [项目:数量]
                BitSet bitSet = new BitSet();

                for (String item : items) {
                    if (item.equals("")) {
                        continue;
                    }
                    key = item.split(":")[0];
                    bitSet.set(Integer.parseInt(key), true);
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
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }


    /**
     * Compare
     *
     * @param before 原始数据集挖掘出的周期高效用项集文档
     * @param after  隐藏敏感项集后的数据集挖掘出的周期高效用项集文档
     * @param sensitive
     * @return HF, MC
     */
    public static String compare(String before, String after, String sensitive) {
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
            System.out.println("AC: "+errorItemset);



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

    public static String compare1(String before, String after,
                                 Set<String> sks) {
        StringBuffer sb = new StringBuffer();
        BufferedReader brb = null;
        BufferedReader bra = null;
        BufferedWriter result = null;
        Map<String, Double> b = new HashMap<String, Double>(); // [敏感项集, 效用值]
        Map<String, Double> a = new HashMap<String, Double>(); // [敏感项集, 效用值]
        try {
            brb = new BufferedReader(new FileReader(new File(before)));
            bra = new BufferedReader(new FileReader(new File(after)));
            result = new BufferedWriter(new FileWriter(new File("src/McAc.txt")));
            String line = null;
            while (null != (line = brb.readLine())) {
                // 数据格式为 项集:效用值:支持度:minPer:maxPer:avgPer
                String[] kv = line.split(":");
                b.put(kv[0], Double.parseDouble(kv[1]));
            }
            brb.close();
            while (null != (line = bra.readLine())) {
                String[] kv = line.split(":");
                a.put(kv[0], Double.parseDouble(kv[1]));
            }
            bra.close();


            result.newLine();
            result.append("Total num is =" + b.size());// 原始数据集总的高效用项集个数
            result.newLine();
            // missing rule.
            int lost = 0; // MC
            int hf = 0; // HF
            int ghost = 0; // AC
            for (String key : b.keySet()) {
                if (a.get(key) == null) {
                    if (sks.contains(key)) {
//						System.out.println(key + "=" + b.get(key)
//								+ "---->sensitive.");
                    } else {
                        // System.out
                        // .println(key + "=" + b.get(key) + "---->lost");
                        result.append(key + "=" + b.get(key) + "---->lost");
                        result.newLine();
                        lost++; // MC
                    }

                } else {
                    if (sks.contains(key) && b.get(key) != null) {
//						System.out.println(key + "=" + a.get(key)
//								+ "---->hiding failure");
                        result.append(key + "=" + a.get(key)
                                + "----->hiding failure");
                        hf++; // HF
                        result.newLine();
                    }
                }
            }

            // Artificial rule
            for (String key : a.keySet()) {
                if (!b.keySet().contains(key)) {
//					System.out.println(key + "=" + a.get(key)
//							+ "---->arificial");
                    result.append(key + "=" + a.get(key) + "---->arificial");
                    ghost++;
                    result.newLine();
                } else {
                    // System.out.println(key +"="+a.get(key)+"---->a large");
                }
            }

            result.append("Lost=" + lost);
            result.append("\r\nMissingRate=" + lost * 1.0 / (b.size() - sks.size()));
            result.newLine();
            result.append("Ghost=" + ghost);
            result.newLine();
            result.append("Failure=" + hf);

            sb.append("LOST=" + lost + "\n");
            sb.append("MC=" + lost * 1.0 / (b.size() - sks.size()) + "\n");
            sb.append("HF=" + hf * 1.0 / sks.size() + "\n");
            sb.append("AC=" + ghost * 1.0 / b.size() + "\n");


            result.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }




    /**
     * Get map from file
     *
     * @param file     数据格式为 9 2:269:4:2:5:2.0
     *                 项集:效用值:支持度:minPer:maxPer:avgPer
     * @param selectDB 1:原始数据集  2:隐藏后的数据集
     * @return 得到 Map<String, Object> map 数据格式为 [项集,效用]
     */
    public static Map<String, Object> getMapFromFile(String file, Integer selectDB, Set<String> sks) {
        Map<String, Object> map = new HashMap<String, Object>(); //
        BufferedReader br = null;

        try {
            String line = null;
            String key = null;
            String value = null;
            br = new BufferedReader(new FileReader(new File(file)));

            if (selectDB == 2) {
                while (null != (line = br.readLine())) {
                    key = line.split(":")[0]; // 项集
                    value = line.split(":")[1]; // 效用值
                    map.put(key, value);
                }
            } else if (selectDB == 1) {
                while (null != (line = br.readLine())) {
                    key = line.split(":")[0]; // 项集
                    if(sks.contains(key)){
                        continue;
                    }
                    value = line.split(":")[1]; // 效用值
                    map.put(key, value);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                } finally {
                    br = null;
                }
            }
        }

        return map;
    }
}
