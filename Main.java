import java.util.List;
/*
 * @Description: 模拟外部排序的启动类
 * @param null
 * @return:
 * @Author: Fars
 * @Date: 2023/4/28 10:52
 */
public class Main {
    public static void main(String[] args) {
        List<Record> records = RecordGenerator.generateRecords(100);
        RecordGenerator.writeRecordsToFile(records, "records.bin");
        List<Record> sortedRecords = MultiwayMergeSort.multiwayMergeSort(records, 4);
        System.out.println("Sorted records: " + sortedRecords);
    }
}
