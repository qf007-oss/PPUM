import java.io.*;
import java.util.*;

public class Select {

    double sensitiveThreshold;
    int num;

    public int getNum(){
        return num;
    }

    public Select(double sensitiveThreshold) {
        this.sensitiveThreshold = sensitiveThreshold;
    }

    public String generateSensitive(String hui, String SPHUI) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(hui)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(SPHUI)));

        List<String> huis = new ArrayList<String>();
        String line = null;
        try {
            while (null != (line = br.readLine())) {
                huis.add(line);
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        num = (int) (huis.size() * sensitiveThreshold);

        Random r = new Random(); //�������

        List<String> choosed = new ArrayList<String>();
        try {
            while (num > 0) {
                int lastIndex = huis.size() - choosed.size() - 1;
                int index = r.nextInt(lastIndex);
                bw.append(huis.get(index));
                bw.newLine();
                String tmp = huis.get(index);

                huis.set(index, huis.get(lastIndex));
                huis.set(lastIndex, tmp);
                choosed.add(tmp);
                num--;
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SPHUI;

    }
}
