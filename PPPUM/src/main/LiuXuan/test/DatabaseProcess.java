import java.util.List;

// TODO ok
public class DatabaseProcess {
	public void genLink(List<TransactionTP> database,List<Itemset> itemsets,List<Integer> senIDs){
	//�����������������ϵ
		double senitemsCount=0.0;
		double nonitemsCount=0.0;
		for(TransactionTP transaction:database){
			for(int i=0;i<itemsets.size();i++){
				// �����д��ڸ��
				if(transaction.items.containsAll(itemsets.get(i).items)){
					if(senIDs.contains(i)){
						transaction.senItemsID.add(i);//���ʶ������������е�λ��һ��
					}else{
						transaction.nonItemsID.add(i);
					}
					itemsets.get(i).transactionsIDs.add(transaction.TID);//�����ʶ��=�������λ��+1
				}
			}
		}
		for(TransactionTP transaction:database){
			
			senitemsCount += (double)transaction.senItemsID.size();
			nonitemsCount += (double)transaction.nonItemsID.size();
		}
		senitemsCount = senitemsCount/database.size();
		nonitemsCount = nonitemsCount/database.size();
		System.out.println("����ƽ�����������������"+senitemsCount + " " + "����ƽ����������������� "+ nonitemsCount);
	}
	
}
