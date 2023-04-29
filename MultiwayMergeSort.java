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
        List<Record> records = readRecordsFromFile("records.bin");
        long startTime = System.currentTimeMillis();
        List<Record> sortedRecords = firstSort(records, 4);
        long endTime = System.currentTimeMillis();
        System.out.println("Sorted records: " + sortedRecords);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
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
     * 对包含记录的列表进行多路归并排序
     *
     * @param records 要排序的记录列表
     * @param k       分块数量
     * @return 排序后的记录列表
     */
    public static List<Record> firstSort(List<Record> records, int k) {
        if (records == null || records.size() <= 1 || k <= 0) {
            return records;
        }

        int n = records.size();
        if (k > n) {
            k = n;
        }

        // 模拟第一趟排序，将记录分为k个块，对每个块进行排序，形成初始归并段
        List<List<Record>> chunks = new ArrayList<>(k);
        int chunkSize = (int) Math.ceil((double) n / k);

        for (int i = 0; i < k; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, n);
            List<Record> chunk = records.subList(start, end);
            chunk.sort(Comparator.comparing(Record::getA));
            chunks.add(chunk);
            //把初始归并段chunk,写入二进制文件
            RecordManager.writeRecordsToFile(chunk, "chunk_" + i + ".bin");
        }

        // 归并排序后的初始归并段，形成最后的排序结果
        return mergeKSortedLists(chunks);
    }

    /**
     * 利用败者树，对多个有序升序记录列表进行归并
     *
     * @param chunks_input 要排序的块
     * @return 排序后的记录列表
     */
    public static List<Record> mergeKSortedLists(List<List<Record>> chunks_input) {
        //用败者树实现
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        int chunkId = 0;
        for (List<Record> chunk : chunks_input) {
            chunks.add(new Chunk(chunk, chunkId++));
        }


        List<Record> result = new ArrayList<>();
        //初始化败者树
        LoserTree loserTree = new LoserTree(chunks);
        //开始归并
        while (true) {
            Chunk minChunk = chunks.get(loserTree.getWinnerChunkIndex());
            Record minRecord = minChunk.poll();
            //minChunk.poll()会导致chunks中的chunk发生变化，所以需要重新获取minChunk？？
            result.add(minRecord);
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

        return result;



//        //用最小堆实现
//        // 使用优先队列（最小堆）存储各个块中最小的记录
//        PriorityQueue<RecordNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(o -> o.record.getA()));
//        List<Record> result = new ArrayList<>();
//
//        // 初始化优先队列
//        for (int i = 0; i < chunks.size(); i++) {
//            if (chunks.get(i).size() > 0) {
//                minHeap.offer(new RecordNode(chunks.get(i).remove(0), i));
//            }
//        }
//
//        // 归并排序
//        while (!minHeap.isEmpty()) {
//            RecordNode current = minHeap.poll();
//            result.add(current.record);
//
//            if (!chunks.get(current.chunkIndex).isEmpty()) {
//                minHeap.offer(new RecordNode(chunks.get(current.chunkIndex).remove(0), current.chunkIndex));
//            }
//        }
//
//        return result;
    }

    /**
     * 记录节点类，用于在优先队列中存储记录和所属的块
     */
    private static class RecordNode {
        Record record;
        int chunkIndex;

        public RecordNode(Record record, int chunkIndex) {
            this.record = record;
            this.chunkIndex = chunkIndex;
        }
    }
}
