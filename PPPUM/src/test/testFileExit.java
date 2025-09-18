import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class testFileExit {
    public static void main(String[] args) {
        String str = "database/PHUIs/beforeDB/chainstore.txt";
        Path file = Paths.get(str);
        if(Files.exists(file)){
            System.out.println("文件存在");
            System.out.println("==============11111111=========");
        }else {
            System.out.println("文件不存在");
        }
    }

}
