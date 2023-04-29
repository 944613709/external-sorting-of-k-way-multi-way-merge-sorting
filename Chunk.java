import java.util.ArrayList;
import java.util.List;

/**
 * <h4>szf-lab2</h4>
 * <p></p>
 *
 * @author : Fars
 * @date : 2023-04-27 23:31
 **/
class Chunk{
    public Chunk() {
    }

    public Chunk(int chunkId) {
        this.chunkId = chunkId;
    }

    public Chunk(List<Record> recordList, int chunkId) {
        this.recordList = new ArrayList<>(recordList);
        this.chunkId = chunkId;
    }
    private int chunkId =-1;//未赋值默认为-1
    private List<Record> recordList = new ArrayList<>();
    //实现add
    public void add(Record record){
        recordList.add(record);
    }
    //实现poll
    public Record poll(){
        return recordList.remove(0);
    }

    public List<Record> getRecordList() {
        return recordList;
    }
    //实现getSize
    public int getSize(){
        return recordList.size();
    }
    //实现compareTo
    public int compareTo(Chunk chunk){
        Record record1 = this.recordList.get(0);
        Record record2 = chunk.recordList.get(0);
        return record1.compareTo(record2);
    }
    //实现isEmpty
    public boolean isEmpty(){
        return recordList.isEmpty();
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "chunkId=" + chunkId +
                ", recordList=" + recordList +
                '}';
    }
    public int getChunkId() {
        return chunkId;
    }
}