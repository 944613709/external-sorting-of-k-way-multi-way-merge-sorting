import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 记录生成器类，用于生成随机记录并将其写入外部文件
 */
public class RecordGenerator {
    public static void main(String[] args) {
        List<Record> records = generateRecords(100);
        System.out.println("records = " + records);
        writeRecordsToFile(records, "records.bin");
    }
    /**
     * 生成指定数量的随机记录
     *
     * @param count 记录数量
     * @return 生成的随机记录列表
     */
    public static List<Record> generateRecords(int count) {
        List<Record> records = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int a = random.nextInt(1000);
            String b = "record_" + a;
            records.add(new Record(a, b));
        }

        return records;
    }
    /**
     * 将记录写入指定文件中
     *
     * @param records  要写入文件的记录列表
     * @param fileName 目标文件名
     */
    public static void writeRecordsToFile(List<Record> records, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
