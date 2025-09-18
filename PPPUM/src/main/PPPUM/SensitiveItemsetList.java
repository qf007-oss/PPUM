import java.util.*;

public class SensitiveItemsetList {
    String sensitive;
    long su;
    int count;

    int largestPeriod = 0;
    int supp = 0;
    List<Integer> tidList = new ArrayList<>();
    List<Integer> perList = new ArrayList<>();

    Map<Integer,Integer> mapTidToUtility = new HashMap<>();

    List<SensitiveItems> SIs = new ArrayList<SensitiveItems>();

    public SensitiveItemsetList(String sensitive, long su, int count, int largestPeriod, int supp, List<Integer> tidList, List<Integer> perList, Map<Integer, Integer> mapTidToUtility, List<SensitiveItems> SIs) {
        this.sensitive = sensitive;
        this.su = su;
        this.count = count;
        this.largestPeriod = largestPeriod;
        this.supp = supp;
        this.tidList = tidList;
        this.perList = perList;
        this.mapTidToUtility = mapTidToUtility;
        this.SIs = SIs;
    }

    /**
     * Method to add an element to this utility list and update the sums at the same time.
     */
    public void addSensitiveItems(SensitiveItems element){
        SIs.add(element);
    }

    public SensitiveItemsetList() {
    }
    public SensitiveItemsetList(String sensitive) {
        this.sensitive = sensitive;
    }

    public SensitiveItemsetList(String sensitive, long su, int count, int largestPeriod, int supp, List<Integer> tidList, List<Integer> perList) {
        this.sensitive = sensitive;
        this.su = su;
        this.count = count;
        this.largestPeriod = largestPeriod;
        this.supp = supp;
        this.tidList = tidList;
        this.perList = perList;
    }

    /**
     * 获取
     * @return sensitive
     */
    public String getSensitive() {
        return sensitive;
    }

    /**
     * 设置
     * @param sensitive
     */
    public void setSensitive(String sensitive) {
        this.sensitive = sensitive;
    }

    /**
     * 获取
     * @return su
     */
    public long getSu() {
        return su;
    }

    /**
     * 设置
     * @param su
     */
    public void setSu(long su) {
        this.su = su;
    }

    /**
     * 获取
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取
     * @return largestPeriod
     */
    public int getLargestPeriod() {
        return largestPeriod;
    }

    /**
     * 设置
     * @param largestPeriod
     */
    public void setLargestPeriod(int largestPeriod) {
        this.largestPeriod = largestPeriod;
    }

    /**
     * 获取
     * @return supp
     */
    public int getSupp() {
        supp = tidList.size();
        return supp;
    }

    /**
     * 设置
     * @param supp
     */
    public void setSupp(int supp) {
        this.supp = supp;
    }

    /**
     * 获取
     * @return tidList
     */
    public List<Integer> getTidList() {
        return tidList;
    }

    /**
     * 设置
     * @param tidList
     */
    public void setTidList(List<Integer> tidList) {
        this.tidList = tidList;
    }

    /**
     * 获取
     * @return perList
     */
    public List<Integer> getPerList() {
        return perList;
    }

    /**
     * 设置
     * @param perList
     */
    public void setPerList(List<Integer> perList) {
        this.perList = perList;
    }


    /**
     * 获取
     * @return mapTidToUtility
     */
    public Map<Integer, Integer> getMapTidToUtility() {
        return mapTidToUtility;
    }

    /**
     * 设置
     * @param mapTidToUtility
     */
    public void setMapTidToUtility(Map<Integer, Integer> mapTidToUtility) {
        this.mapTidToUtility = mapTidToUtility;
    }

    /**
     * 获取
     * @return SIs
     */
    public List<SensitiveItems> getSIs() {
        return SIs;
    }

    /**
     * 设置
     * @param SIs
     */
    public void setSIs(List<SensitiveItems> SIs) {
        this.SIs = SIs;
    }

    public String toString() {
        return "SensitiveItemsetList{sensitive = " + sensitive +
                ", su = " + su + ", count = " + count +
                ", largestPeriod = " + largestPeriod +
                ", supp = " + supp + ", tidList = " + tidList +
                ", perList = " + perList +
                ", mapTidToUtility = " + mapTidToUtility +
                ", SIs = " + SIs.toString() + "}";
    }
}
