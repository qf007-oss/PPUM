import java.util.ArrayList;
import java.util.List;
// TODO ok
public class DataCopy {
	public static List<List<Integer>> DBCopy(List<TransactionTP> Database){
		List<List<Integer>> DBList = new ArrayList<>();
		for(TransactionTP transaction:Database){
			DBList.add(transaction.items);
		}
		return DBList;
	}
	public static List<Itemset> ItemsetsCopy(List<Itemset> itemsets){
		List<Itemset> itemsetsCopy = new ArrayList<>();
		for(Itemset itemset:itemsets){
			List<Integer> items=itemset.items;
			int utility=itemset.utility;
			List<Integer> TIDs = itemset.transactionsIDs;
			int itemID=itemset.itemID;
			itemsetsCopy.add(new Itemset(items,utility,TIDs,itemID));
		}
		return itemsetsCopy;
	}
}
