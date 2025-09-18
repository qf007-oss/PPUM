import java.util.*;

public class ProUtility2 {
	int minUtility;
	List<TransactionTP> database;
	List<Itemset> itemsets;
	List<Integer> senIDs;
	Map<Integer,Integer> ExUtility = null;
	public long startTimestamp;
	public long endTimestamp;
	public Set<Integer> victrans;
	public ProUtility2(List<TransactionTP> Database, List<Itemset> Itemsets, List<Integer> senList, int minutil, Map<Integer,Integer> ExUtility){
		this.database=Database;
		this.minUtility=minutil;
		this.itemsets = Itemsets;
		this.senIDs=senList ;
		this.ExUtility = ExUtility;
	}
	public List<TransactionTP>  runAlgorithm(){
		startTimestamp = System.currentTimeMillis();
	    MemoryLogger1.getInstance().reset();
		victrans = new HashSet<>();
		Collections.sort(senIDs,new Comparator<Integer>() {
			public int compare(Integer item1,Integer item2){
				int compare = itemsets.get(item2).items.size()-itemsets.get(item1).items.size();
				if(compare==0)
					return(item1-item2);
				return compare;
			}
		});

	    for (Integer senID:senIDs){
	    	while(itemsets.get(senID).utility >=minUtility)
	    		PrivacyPreserve(senID);
	    }
	    endTimestamp = System.currentTimeMillis();
	    System.out.println("SMSE�㷨����ʱ��"+(endTimestamp-startTimestamp));
	    System.out.println(" Max memory:" + MemoryLogger1.getInstance().getMaxMemory());
	    return database;
	}																																																		
	private void PrivacyPreserve(int senID){
		List<Integer> sentrans = itemsets.get(senID).transactionsIDs;
		int victimTran = genVicTran(sentrans);
		ItemUtility victimItem = genVicItem(victimTran,senID);
		Updata(victimTran,victimItem,senID);
	
	}
	public int genVicTran(List<Integer> sentrans){
		int min = Integer.MAX_VALUE;
		int victimTran = 0;
		for(Integer sentran:sentrans){
			TransactionTP transaction = database.get(sentran-1);
			if(min>transaction.nonItemsID.size()){
				min = transaction.nonItemsID.size();
				victimTran = transaction.TID;
			}
		}
		MemoryLogger1.getInstance().checkMemory();
		return victimTran;
	}
	public ItemUtility genVicItem(int victimTran,int senID){
		ItemUtility victimItem = null;
		TransactionTP transaction = database.get(victimTran-1);
		List<Integer> maxItem = new ArrayList<>();
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for(Integer senItem:itemsets.get(senID).items){
			int max_temp = genItemSetCount(senItem,transaction.senItemsID);
			if(max<max_temp){
				max = max_temp;
				maxItem.clear();
				maxItem.add(senItem);
			}else if(max==max_temp)
				maxItem.add(senItem);
		}
		if(maxItem.size()>1){
			int vicItem = 0;
			for(Integer senmaxitem:maxItem){
				int min_temp = genItemSetCount(senmaxitem,transaction.nonItemsID);
				if(min>min_temp){
					min = min_temp;
					vicItem = senmaxitem;
				}
			}
			int index = transaction.items.indexOf(vicItem);
			victimItem = transaction.itemsUtilities.get(index);
		}else{
			int index = transaction.items.indexOf(maxItem.get(0));
			victimItem = transaction.itemsUtilities.get(index);
		}
		MemoryLogger1.getInstance().checkMemory();
		return victimItem;
	}
	public int genItemSetCount(int senitem,List<Integer> itemsetID){
		int senCount = 0;
		for(Integer itemID:itemsetID){
			if(itemsets.get(itemID).items.contains(senitem)){
				senCount++;
			}
		}
		return senCount;
	}
	public void Updata(int victimTran,ItemUtility victimItem,int senID){
		victrans.add(victimTran);
		TransactionTP transaction = database.get(victimTran-1);
		//System.out.println(victimTran+" "+victimItem.item);
		Itemset senItemset = itemsets.get(senID);
		int deleteUtility = 0;
		int deleteCount=0;
		int diff = senItemset.utility-minUtility+1;
		if(diff>=victimItem.utility){
			deleteUtility = genDeleUtility(senID, victimTran);
		}else{
			double itemExUtil = ExUtility.get(victimItem.item);
			deleteCount = (int)Math.ceil(diff/itemExUtil);
			deleteUtility = deleteCount*(int)itemExUtil;
			if(deleteUtility>=victimItem.utility){
				deleteUtility = genDeleUtility(senID, victimTran);
			}
		}
		UpdateItems(victimTran,victimItem,deleteUtility,transaction.senItemsID,1);
		UpdateItems(victimTran,victimItem,deleteUtility,transaction.nonItemsID,0);
		if(deleteUtility>=victimItem.utility){
			database.get(victimTran-1).itemsUtilities.remove(victimItem);
			database.get(victimTran-1).items.remove(victimItem.item);
		}else{
			victimItem.in_utility = victimItem.in_utility-deleteCount;
			victimItem.utility=victimItem.utility-deleteUtility;
		}
		MemoryLogger1.getInstance().checkMemory();
	}
	public int genDeleUtility(int senID,Integer victimTran){
		int deleteUtility = 0;
		for(Integer senitem:itemsets.get(senID).items){
			TransactionTP transaction = database.get(victimTran-1);
			int index = transaction.items.indexOf(senitem);
			deleteUtility += transaction.getItemsUtilities().get(index).utility;
		}
		return deleteUtility;
	}
	public void UpdateItems(Integer victimTran,ItemUtility victimItem,int deleteUtility,List<Integer> itemsetIDs,int flag){
		List<Integer> removeID = new ArrayList<>();
		for(int i=0;i<itemsetIDs.size();i++){
			Integer itemsetID = itemsetIDs.get(i);
			Itemset itemset = itemsets.get(itemsetID);
			if(itemset.items.contains(victimItem.item)){
				if(deleteUtility<victimItem.utility){
					itemset.utility-=deleteUtility;
				}else{
					int newDeleUtility = genDeleUtility(itemsetID,victimTran);
					itemset.utility-=newDeleUtility;
					removeID.add(itemsetID);
					itemset.transactionsIDs.remove(victimTran);
				}
				if(itemset.utility<minUtility){
					for(Integer tranID:itemset.transactionsIDs){
						if(tranID==victimTran)
							itemsetIDs.removeAll(removeID);
					    TransactionTP tran = database.get(tranID-1);
					    if(flag==0){
					    	tran.nonItemsID.remove(itemsetID);
					    }else{
					    	 tran.senItemsID.remove(itemsetID);
					    }
					}
				}
			}
		}
		itemsetIDs.removeAll(removeID);
	}
}