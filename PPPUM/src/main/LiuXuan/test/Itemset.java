import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Itemset {
	public List<Integer> items = new ArrayList<Integer>();
    public int utility=0;
    public List<Integer> transactionsIDs=new ArrayList<>();
    public Integer itemID;
    public Itemset(){ 	
    }
    //有参构造函数
    public Itemset(List<Integer> items,int utility,int itemID){
    	this.items=items;
    	this.utility=utility;
    	this.itemID = itemID;
    	Collections.sort(items,new Comparator<Integer>() {
			public int compare(Integer item1,Integer item2){
					return(item1-item2);
			}
		});
    }
    public Itemset(List<Integer> items,int utility, List<Integer> transactionIDs,int itemID){
    	this.items=items;
    	this.utility=utility;
    	this.transactionsIDs=transactionIDs;
    	this.itemID=itemID;
    }
}
