import java.io.*;
import java.util.List;

public class GetAfterDB {
    public void getDB(List<TransactionTP> newDB,String outputAfterDBPHM,String outputAfterDB){
        FileWriter fw = null;
        BufferedWriter bw = null;

        FileWriter fw1 = null;
        BufferedWriter bw1 = null;
        try {
            fw = new FileWriter(new File(outputAfterDBPHM));
            bw = new BufferedWriter(fw);

            fw1 = new FileWriter(new File(outputAfterDB));
            bw1 = new BufferedWriter(fw1);

            String str = null;
            StringBuffer sb = new StringBuffer();
            StringBuffer sb1 = new StringBuffer();

            for (int i = 0; i < newDB.size(); i++) {
                List<ItemUtility> itemsUtilities = newDB.get(i).itemsUtilities;
                int tu = 0;
                String items = "";
                String counts = "";
                String line = "";
                for (int j = 0; j < itemsUtilities.size(); j++) {
                    Integer item = itemsUtilities.get(j).item;
                    int inUtility = itemsUtilities.get(j).in_utility;
                    int utility = itemsUtilities.get(j).utility;
                    items += item+" ";
                    counts += utility+" ";
                    tu+=utility;
                    line+=item+":"+inUtility+" ";
                }
                if(items.length()==0){
                    continue;
                }
                sb.append(items.substring(0, items.length() - 1) + ":"
                        + tu + ":"
                        + counts.substring(0, counts.length() - 1) + "\r");
                sb1.append(line.substring(0, line.length() - 1) + "\r");
            }
            bw.write(sb.toString());
            bw.close();
            fw.close();

            bw1.write(sb1.toString());
            bw1.close();
            fw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
