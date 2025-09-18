import java.util.*;

public class ProUtility1 {
	int minUtility;
	List<TransactionTP> database;
	List<Itemset> itemsets; //读入所有高效用项集
	List<Integer> senIDs;
	Map<Integer,Integer> ExUtility = null;
	public long startTimestamp;
	public long endTimestamp;
	public Set<Integer> victrans;
	public ProUtility1(List<TransactionTP> Database, List<Itemset> Itemsets, List<Integer> senList, int minutil, Map<Integer,Integer> ExUtility){
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

	    for (Integer senID:senIDs){
	    	while(itemsets.get(senID).utility >=minUtility)
	    		PrivacyPreserve(senID);
	    }
	    endTimestamp = System.currentTimeMillis();
	    System.out.println("SMAU算法运行时间"+(endTimestamp-startTimestamp));
	    System.out.println(" Max memory:" + MemoryLogger1.getInstance().getMaxMemory());
	    return database;
	}																																																		
	private void PrivacyPreserve(int senID){
		List<Integer> sentrans = itemsets.get(senID).transactionsIDs;
		int victimTran = genVicTran(sentrans);
		ItemUtility victimItem = genVicItem(victimTran,senID);
		Updata(victimTran,victimItem,senID);
		MemoryLogger1.getInstance().checkMemory();
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
		int max = Integer.MIN_VALUE;
		for(Integer senItem:itemsets.get(senID).items){
			int ItemIndex = transaction.items.indexOf(senItem);
			int utility = transaction.itemsUtilities.get(ItemIndex).utility;
			if(max<utility){
				max = utility;
				victimItem = transaction.itemsUtilities.get(ItemIndex);
			}
		}
		MemoryLogger1.getInstance().checkMemory();
		return victimItem;
	}
	public void Updata(int victimTran,ItemUtility victimItem,int senID){
		victrans.add(victimTran);
		TransactionTP transaction = database.get(victimTran-1);
		//System.out.println(victimTran+" "+victimItem.item);
		Itemset senItemset = itemsets.get(senID);
		int deleteUtility = 0;
		int deleteCount = 0;
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
	public int genDeleUtility(int senID,int victimTran){
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
			Integer itemsetID= itemsetIDs.get(i);
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