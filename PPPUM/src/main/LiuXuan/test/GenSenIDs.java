import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import org.junit.Test;
// TODO OK
public class GenSenIDs {
	/**
	 * 
	 * @param count
	 * @param size
	 * @param seed
	 * @return 
	 */
	public static List<Integer> genSenIDs(int count,int size, int seed){
		List<Integer> SenIDs = new ArrayList<>();
		Random random = new Random();
		random.setSeed(seed);
		for(int i=0;i<count;i++){
			
			int Count = random.nextInt(size);
			SenIDs.add(Count);
		}
		return SenIDs;
	}
	
/*	@Test
   public void test() {
		List<Integer> list = genSenIDs(100, 100, 1);
		for (Integer in: list) {
			System.out.print(in +"   ") ;
		}
	}*/	
}
