import java.io.*;
import java.util.List;

/**
 * <h4>szf-lab2</h4>
 * <p></p>
 *
 * @author : Fars
 * @date : 2023-04-29 11:40
 **/
public class Utils {
    public static Chunk readChunkFromFile(String fileName) {
        //读取文件中的记录列表
        Chunk chunk = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            chunk = (Chunk) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功读取文件" + fileName + "中的记录列表");
        return chunk;
    }
    /*
     * @Description: 直接写入文件，若存在则覆盖
     * @param fileName
     * @param chunk
     * @return: void
     * @Author: Fars
     * @Date: 2023/4/29 11:58
     */
    public static void writeChunkToFile_cover(String fileName, Chunk chunk) {
        //直接写入文件，若存在则覆盖
        try (FileOutputStream fos = new FileOutputStream(fileName,false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject( chunk);
            System.out.println("成功写入文件" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * @Description: 若存在则继续写入追加
     * @param fileName
     * @param chunks
     * @return: void
     * @Author: Fars
     * @Date: 2023/4/29 11:58
     */
    public static void appendChunkToFile(String fileName, Chunk chunk) {
        //若存在则继续写入追加
        try (FileOutputStream fos = new FileOutputStream(fileName,true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject( chunk);
            System.out.println("成功写入文件" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void appendRecordsToFile(String fileName,List<Record> records)
    {
        //若存在则继续写入追加
//        try (FileOutputStream fos = new FileOutputStream(fileName,true);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject( records);
//            System.out.println("成功写入文件" + fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try{
            File f = new File(fileName);
            FileOutputStream fos = new FileOutputStream(fileName,true);
            MyObjectOutputStream moos = MyObjectOutputStream.newInstance(f, fos);
            moos.writeObject( records);
            System.out.println("成功写入文件" + fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
     * @Description: 模拟流读入，只读取第Number条记录
     * @param fileName
     * @param start
     * @param end
     * @return: void
     * @Author: Fars
     * @Date: 2023/4/29 12:33
     */
    public static List<Record> readRecordsFromFileByNumber(String fileName,int number) {
        //读取文件中的记录列表
        List<Record> records = null;
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            for(int i=0;i<=number;i++)
            {
                records = (List<Record>) ois.readObject();
            }
        } catch (Exception e) {
            System.out.println("读取完毕");
        }
        System.out.println("成功读取文件" + fileName + "中的记录列表");

        return records;
    }

}
