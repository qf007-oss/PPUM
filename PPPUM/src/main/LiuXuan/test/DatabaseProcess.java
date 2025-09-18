import java.util.List;

// TODO ok
public class DatabaseProcess {
	public void genLink(List<TransactionTP> database,List<Itemset> itemsets,List<Integer> senIDs){
	//创建事务表与项集表的联系
		double senitemsCount=0.0;
		double nonitemsCount=0.0;
		for(TransactionTP transaction:database){
			for(int i=0;i<itemsets.size();i++){
				// 事务中存在该项集
				if(transaction.items.containsAll(itemsets.get(i).items)){
					if(senIDs.contains(i)){
						transaction.senItemsID.add(i);//项集标识符与其在项集表中的位置一致
					}else{
						transaction.nonItemsID.add(i);
					}
					itemsets.get(i).transactionsIDs.add(transaction.TID);//事务标识符=事务表中位置+1
				}
			}
		}
		for(TransactionTP transaction:database){
			
			senitemsCount += (double)transaction.senItemsID.size();
			nonitemsCount += (double)transaction.nonItemsID.size();
		}
		senitemsCount = senitemsCount/database.size();
		nonitemsCount = nonitemsCount/database.size();
		System.out.println("交易平均包含的敏感项集数："+senitemsCount + " " + "交易平均包含非敏感项集数： "+ nonitemsCount);
	}
	
}
