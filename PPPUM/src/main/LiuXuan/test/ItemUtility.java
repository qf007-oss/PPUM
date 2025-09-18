public class ItemUtility{

	public Integer item;
	public  int utility;
	public int in_utility;
	
	public ItemUtility(int item, int In_Utility,int utility) {
		this.item = item;
		this.utility = utility;
		this.in_utility = In_Utility;
	}
	
	
	public String toString() {
		return "[" + item + "," + in_utility + "]";
	}
}