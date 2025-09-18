public class SensitiveItems {
    // The three variables as described in the paper:
    /** transaction id */
    public int tid ;
    /** sensitive item */
    public String item ;
    /** sensitive item utility */
    public int iu;
    /** sensitive item count in the tid */
    public int cnt;
    /** sensitive item maxPer(item) */
    public int mp;

    /**
     * Constructor.
     * @param tid  the transaction id
     * @param item  the sensitive item
     * @param iu  sensitive item utility
     * @param cnt  sensitive item count in the tid
     //* @param mp  sensitive item maxPer(item)
     */
    public SensitiveItems(int tid,String item, int iu, int cnt){
        this.tid = tid;
        this.item = item;
        this.iu = iu;
        this.cnt = cnt;
    }

    public SensitiveItems() {
    }

    public SensitiveItems(int tid, String item, int iu, int cnt, int mp) {
        this.tid = tid;
        this.item = item;
        this.iu = iu;
        this.cnt = cnt;
        this.mp = mp;
    }

    /**
     * 获取
     * @return tid
     */
    public int getTid() {
        return tid;
    }

    /**
     * 设置
     * @param tid
     */
    public void setTid(int tid) {
        this.tid = tid;
    }

    /**
     * 获取
     * @return item
     */
    public String getItem() {
        return item;
    }

    /**
     * 设置
     * @param item
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * 获取
     * @return iu
     */
    public int getIu() {
        return iu;
    }

    /**
     * 设置
     * @param iu
     */
    public void setIu(int iu) {
        this.iu = iu;
    }

    /**
     * 获取
     * @return cnt
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * 设置
     * @param cnt
     */
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    /**
     * 获取
     * @return mp
     */
    public int getMp() {
        return mp;
    }

    /**
     * 设置
     * @param mp
     */
    public void setMp(int mp) {
        this.mp = mp;
    }

    public String toString() {
        return "SensitiveItems{tid = " + tid + ", item = " + item + ", iu = " + iu + ", cnt = " + cnt + ", mp = " + mp + "}";
    }
}
