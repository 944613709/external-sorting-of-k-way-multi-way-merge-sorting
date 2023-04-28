import javax.print.attribute.IntegerSyntax;
import java.util.ArrayList;
import java.util.List;
public class LoserTree
{
    private int[] ls = null;// 以顺序存储方式保存所有非叶子结点
    private int size = 0;
    private ArrayList<Chunk> leaves = null;// 叶子节点

    public LoserTree(ArrayList<Chunk> chunks)
    {
        this.leaves = chunks;// 叶子结点
        this.size = chunks.size();// 叶子结点个数 = 块数 = ls个数
        this.ls = new int[size];//ls个数 = 块数 = 叶子结点个数
        for (int i = 0; i < size; ++i)
        {
            ls[i] = -1;
        }
        for (int i = size - 1; i >= 0; --i)
        {
            adjust(i);
        }
    }
    /* 
     * @Description: 从chunks中删除第s块，并调整败者树
     * @param s 
     * @return: void 
     * @Author: Fars
     * @Date: 2023/4/27 23:28
     */
    public void del(int s)
    {
        leaves.remove(s);
        size--;
        ls = new int[size];
        for (int i = 0; i < size; ++i)
        {
            ls[i] = -1;
        }
        for (int i = size - 1; i >= 0; --i)
        {
            adjust(i);
        }
    }

    public void add(Chunk leaf, int s)
    {
        leaves.set(s, leaf);// 调整叶子结点
        adjust(s);// 调整非叶子结点
    }

    public Chunk getLeaf(int i)
    {
        return leaves.get(i);
    }

    public int getWinnerChunkIndex()
    {
        return ls[0];
    }

    public Chunk getWinnerChunk()
    {
        return leaves.get(ls[0]);
    }
//    public void adjust(int leafIndex)
//    {
//        // s指向当前的值最小的叶子结点（胜者）
//        int parent = (leafIndex + size) / 2;// parent是leafIndex的双亲
//
//        while (parent > 0)// 沿从叶子到根的路径比较，直到根结点
//        {
//            if (leafIndex >= 0 && (ls[parent] == -1 || leaves.get(leafIndex).compareTo(leaves.get(ls[parent])) > 0))
//            {
//                // 将树中的当前结点指向其子树中值最小的叶子
//                int tmp = leafIndex;
//                leafIndex = ls[parent];
//                ls[parent] = tmp;
//            }
//    }

    public void adjust(int leafIndex)
    {
        // s指向当前的值最小的叶子结点（胜者）
        int parent = (leafIndex + size) / 2;// parent是leafIndex的双亲

        while (parent > 0)// 沿从叶子到根的路径比较，直到根结点
        {
            int winnerIndex;
            int loserIndex;
            //如果是父节点ls[parent]为-1,代表还没有比赛过，应该要和兄弟比赛
            if(ls[parent] == -1){
                if (leaves.get(leafIndex).compareTo(leaves.get(leafIndex - 1)) < 0){
                    winnerIndex = leafIndex;
                    loserIndex = leafIndex - 1;
                }
                else{
                    winnerIndex = leafIndex - 1;
                    loserIndex = leafIndex;
                }
            }
            //如果s战胜了父亲节点代表的leaf ( leave.get(s) < leaves.get(ls[parent]) )，那么胜者是s
            else if(leaves.get(leafIndex).compareTo(leaves.get(ls[parent])) < 0){
                winnerIndex= leafIndex;
                loserIndex = ls[parent];
            }
            else{
                winnerIndex = ls[parent];
                loserIndex = leafIndex;
            }
            //将败者索引放到父亲节点
            ls[parent] = loserIndex;
            //将胜者继续向上比较
            leafIndex = winnerIndex;
            parent /= 2;
        }
        ls[0] = leafIndex;// 树根指向胜者
    }

    public static void main(String[] args) {
        //给定示例输入数据
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        Chunk chunk1 = new Chunk(0);
        chunk1.add(new Record(1,"record_1"));
        chunk1.add(new Record(7,"record_7"));
        chunk1.add(new Record(8,"record_8"));
        Chunk chunk2 = new Chunk(1);
        chunk2.add(new Record(4,"record_4"));
        chunk2.add(new Record(5,"record_5"));
        chunk2.add(new Record(6,"record_6"));
        chunks.add(chunk1);
        chunks.add(chunk2);
        //利用败者树排序
        LoserTree loserTree = new LoserTree(chunks);
        //输出排序结果
        System.out.println("排序结果：");
        while(true) {
            System.out.println("当前winner代表的chunkIndex = " + loserTree.getWinnerChunkIndex());
            System.out.println("该chunk = " + loserTree.getWinnerChunk().toString());
            System.out.println("该chunk的块首元素 = " + loserTree.getWinnerChunk().poll());
            int chunkIndex = loserTree.getWinnerChunkIndex();
            Chunk winnerChunk = loserTree.getWinnerChunk();
            //如果该chunk为空，则去除该chunk
            if (loserTree.getWinnerChunk().isEmpty())
            {
                loserTree.del(chunkIndex);
                System.out.println("删除chunk，其Id:" + winnerChunk.getChunkId());
                if(chunks.size() == 0)
                    break;
            }

            else//否则调整败者树
                loserTree.adjust(chunkIndex);
        }
    }
}