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
        final int allRecordCount = 10000; // RecordCount
        final int loadMaxCountInMemory = 2500; // 内存每次能装下的最大RecordCount
        //模拟内存和外存的交互，考虑内存有限的条件进行生成随机记录并写入文件
        System.out.println("-----------------Start 生成随机记录写入input_records-----------------");
        RecordManager.generateRecords_simulation(allRecordCount,loadMaxCountInMemory);
        System.out.println("-----------------End 生成随机记录写入input_records-----------------");
        //外部排序
        int k = 4;
        System.out.println("开始外部排序,k=" + k);
        long startTime = System.currentTimeMillis();
        System.out.println("-----------------Start 第一趟排序-----------------" );
        MultiwayMergeSort.firstSort( 4,allRecordCount,loadMaxCountInMemory);
        System.out.println("-----------------End 第一趟排序-----------------" );
        System.out.println("-----------------Start 归并-----------------" );
        MultiwayMergeSort.mergeKSortedLists(4,allRecordCount,loadMaxCountInMemory);
        System.out.println("-----------------End 归并-----------------" );
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
//        System.out.println("最终排序结果");
//        RecordManager.printAllRecordsFromFile("output_records.bin");
    }
}
