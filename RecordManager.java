import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 记录生成器类，用于生成随机记录并将其写入外部文件
 */
public class RecordManager {
    static final int allRecordCount = 10000; // RecordCount
    static final int loadMaxCountInMemory = 2500; // 内存每次能装下的最大RecordCount
    public static void main(String[] args) {
        generateRecords_simulation(allRecordCount,loadMaxCountInMemory);
    }
    /*
     * @Description: 模拟内存和外存的交互，考虑内存有限的条件进行生成随机记录并写入文件
     * @param allRecordCount
     * @param loadMaxCountInMemory
     * @return: void
     * @Author: Fars
     * @Date: 2023/4/29 12:19
     */
    public static int generateRecords_simulation(int allRecordCount,int loadMaxCountInMemory)
    {
        int count = 0;
        //生成allRecordCount条记录
        while(count<allRecordCount)
        {
            //每次最多生成loadMaxCountInMemory条记录
            //如果剩余记录数不足loadMaxCountInMemory条，则生成剩余记录数条记录
            List<Record> records = null;
            if(allRecordCount-count<loadMaxCountInMemory)
            {
                records = generateRecords(allRecordCount-count);
                count+=allRecordCount-count;
            }
            else//否则生成loadMaxCountInMemory条记录
            {
                records = generateRecords(loadMaxCountInMemory);
                count+=loadMaxCountInMemory;
            }
            Utils.appendRecordsToFile( "input_records.bin",records);
        }
        System.out.println("最终总共成功生成" + count + "条随机记录并写入input_records.bin文件");
        return count;
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
    /*
     * @Description:打印所有Records里的记录
     * @param s
     * @return: java.util.List<Record>
     * @Author: Fars
     * @Date: 2023/4/29 11:39
     */
    public static void printAllRecordsFromFile(String fileName) {
        //读取文件中的记录列表
        List<Record> records = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while(true) {
                records = (List<Record>) ois.readObject();
                System.out.println("records = " + records);
            }
        } catch (EOFException e) {
            System.out.println("最终结果打印完毕完毕");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
