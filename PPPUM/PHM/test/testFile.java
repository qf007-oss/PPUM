import java.io.*;

public class testFile {
    public static void main(String[] args) {
        String src = "C:\\study\\PPPUM\\PHM\\test\\123.txt";
        String dst = "C:\\study\\PPPUM\\PHM\\test\\123after.txt";
        String s = transDBformat2HUIMiner(src, dst);
        System.out.println(s);
    }
    public static String transDBformat2HUIMiner(String src, String dst) {

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            FileReader fr = new FileReader(new File(src));
            fw = new FileWriter(new File(dst));
            BufferedReader br = new BufferedReader(fr);

            bw = new BufferedWriter(fw);
            String str = null;
            StringBuffer sb = new StringBuffer();

            while (null != (str = br.readLine())) {
                int line = Integer.parseInt(str);
                if(line==-1){
                    System.out.println("我是-1："+line);
                }else{
                    System.out.println("我不是-1："+line);
                }
                System.out.println(str);
                if (str.length() == 0) {
                    System.out.println("看看我执行了吗");
                    sb.append("-1"+"\r");
                    System.out.println("null:"+"=="+str+"==");
                    continue;
                }else {
                    System.out.println("  :"+"---"+str+"---");
                }
                sb.append("123123"+"\r");
            }

            bw.write(sb.toString());
            br.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dst;
    }
}
