import java.util.ArrayList;
import java.util.List;

public class testList {
    public static void main(String[] args) {
        List<List<Integer>> list1 = new ArrayList<>();
        List<List<Integer>> list2 = new ArrayList<>();
        List<List<Integer>> sen = new ArrayList<>();



        int j=0;

        while(j<5){
            List<Integer> li1 = new ArrayList<>();
            List<Integer> li2 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                li1.add(i+j+1);
                li2.add(i+j+3);
            }
            System.out.println("li1: "+li1);
            System.out.println("li2: "+li2);
            list1.add(li1);
            list2.add(li2);
            j++;
        }
        List<Integer> sks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sks.add(i+3);
        }
        sen.add(sks);

        System.out.println("list1: "+list1);
        System.out.println("list2: "+list2);
        System.out.println("sen: "+sen);

        List<List<Integer>> list1All = new ArrayList<>();
        list1All.addAll(list1);
        System.out.println("list1All: "+list1All);

        list1All.retainAll(list2);
        System.out.println(list1All);
        System.out.println("===");

        //list2.retainAll(list1);
        //System.out.println("list2Retain: "+list2);
        //System.out.println(list2.size());

        list2.removeAll(list1);
        System.out.println("list2Remove: "+list2);
        System.out.println(list2.size());


        /*lostItemset.addAll(correctItemList); //原始数据集中发现的非敏感高效用项集
        errorItemset.addAll(newItemsList); //处理后的数据集发现的所有高效用项集
        failItemset.addAll(senItemset); //原始数据集中的敏感高效用项集
        //隐藏失败的项集
        failItemset.retainAll(newItemsList);//原始数据集中的敏感高效用项集，在处理后的数据集中依然存在
        //产生丢失的非敏感项集
        lostItemset.removeAll(newItemsList);//原始数据集中发现的非敏感高效用项集，在清洗后的数据集中不存在，也就是丢失的部分
        //产生新增的项集
        errorItemset.removeAll(totalItemList);//处理后的数据集发现的所有高效用项集，在原始数据集中不存在*/

    }
}
