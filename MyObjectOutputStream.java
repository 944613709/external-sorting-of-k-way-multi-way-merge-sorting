import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {
    //定义成静态的好处  
    private static File f;

    /**
     * 初始化静态文件对象，并返回类对象 
     * @param file 文件对象，用于初始化静态文件对象 
     * @param out 输出流 
     * @return MyObjectOutputStream
     * @throws IOException
     */
    public static  MyObjectOutputStream newInstance(File file, OutputStream out)
            throws IOException {
        f = file;//本方法最重要的地方：构建文件对象，是两个文件对象属于同一个  
        return new MyObjectOutputStream(out, f);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        if (!f.exists() || (f.exists() && f.length() == 0)) {
            super.writeStreamHeader();
        } else {
            super.reset();
        }

    }

    public MyObjectOutputStream(OutputStream out, File f) throws IOException {
        super(out);
    }

}  