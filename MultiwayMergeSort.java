import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 多路归并排序类，用于对包含记录的列表进行排序
 */
public class MultiwayMergeSort {
    public static void main(String[] args) {
        final int allRecordCount = 10000; // RecordCount
        final int loadMaxCountInMemory = 2500; // 内存每次能装下的最大RecordCount

        long startTime = System.currentTimeMillis();
        firstSort( 4,allRecordCount,loadMaxCountInMemory);
        mergeKSortedLists(4,allRecordCount,loadMaxCountInMemory);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println("最终排序结果");
        RecordManager.printAllRecordsFromFile("output_records.bin");
    }
    /**
     * 从文件中读取记录列表
     *
     * @param fileName 要读取的文件名
     * @return 读取到的记录列表
     */
    public static List<Record> readRecordsFromFile(String fileName) {
        List<Record> records = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            records = (List<Record>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * 第一趟排序，将记录分为k个块，对每个块进行排序，形成初始归并段保存到二进制文件
     *
     * @param records 要排序的记录列表
     * @param k       分块数量
     */
    public static void firstSort(int k,int allRecordCount,int loadMaxCountInMemory) {

        List<Record> records = readRecordsFromFile("input_records.bin");
        int n = records.size();
        System.out.println("n = " + n);
        System.out.println("allRecordCount = " + allRecordCount);
        n=allRecordCount;
//        int n = allRecordCount;
        // 模拟第一趟排序，将记录分为k个块，对每个块进行排序，形成初始归并段
        int chunkSize = (int) Math.ceil((double) n / k);

        for (int i = 0; i < k; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, n);

            //对每个块进行排序
            //由于内存有限，每次只能读入一部分记录，所以需要分段读入
            List<Record> sub_records = Utils.readRecordsFromFileByNumber("input_records.bin", i);
            sub_records.sort(Comparator.comparing(Record::getA));
            //转为chunk
            Chunk chunk = new Chunk(sub_records,i);
            //把初始归并段chunk,写入二进制文件
            Utils.writeChunkToFile_cover( "chunk_" + i + ".bin", chunk);
        }
    }

    /**
     * 利用败者树，对多个有序升序记录列表进行归并
     *
     * @param k 有序升序记录列表的数量
     */
    public static void mergeKSortedLists(int k,int allRecordCount,int loadMaxCountInMemory) {
        //用败者树实现
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        //通过读入二进制文件，将初始归并段chunk读入chunks
        for (int i = 0; i < k; i++) {
            Chunk chunk = Utils.readChunkFromFile("chunk_" + i + ".bin");
            chunks.add(chunk);
        }

        List<Record> bufferRecordList = new ArrayList<>();
        //初始化败者树
        LoserTree loserTree = new LoserTree(chunks);
        //开始归并
        while (true) {
            Chunk minChunk = chunks.get(loserTree.getWinnerChunkIndex());
            Record minRecord = minChunk.poll();

            bufferRecordList.add(minRecord);
            if(bufferRecordList.size() == loadMaxCountInMemory)
            {
                //如果缓冲区满了，则把当前的result追加写入二进制文件
                Utils.appendRecordsToFile("output_records.bin",bufferRecordList);
//                System.out.println("bufferRecordList = " + bufferRecordList);
                bufferRecordList.clear();
            }
            int chunkIndex = loserTree.getWinnerChunkIndex();
            //如果该chunk为空，则去除该chunk
            if (loserTree.getWinnerChunk().isEmpty())
            {
                loserTree.del(chunkIndex);//在败者树中删除该chunk
                //如果chunks为空，则跳出循环
                if (chunks.isEmpty()) {
                    break;
                }
            }
            else//否则调整败者树
                loserTree.adjust(chunkIndex);
        }
    }

}
