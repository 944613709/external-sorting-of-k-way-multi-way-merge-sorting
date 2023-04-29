import java.io.Serializable;
/**
 * 记录类，包含一个整数型属性A和一个字符串型属性B
 */
public class Record implements Serializable {
    private int a;
    private String b;

    public Record(int a, String b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    @Override
    public String toString() {
        return "Record{" +
                "a=" + a +
                '}';
    }
    //写compareTo
    public int compareTo(Record o) {
        if (this.a > o.a) {
            return 1;
        } else if (this.a < o.a) {
            return -1;
        } else {
            return 0;
        }
    }
}
