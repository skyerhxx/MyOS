import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
 
 
public class OperatingSystem {
    private int[] imgContent = new int[]{
            //这段二进制数据将是我们的HelloWorld OS的内核，它的作用是让BIOS将其加载到内存地址0x8000，然后调用BIOS终端在屏幕上打印出一行字符串
            0xeb,0x4e,0x90,0x48,0x45,0x4c,0x4c,0x4f,0x49,0x50,0x4c,0x00,0x02,0x01,0x01,0x00,0x02,0xe0,
            0x00,0x40,0x0b,0xf0,0x09,0x00,0x12,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x40,0x0b,0x00,0x00,0x00,0x00,0x29,
            0xff,0xff,0xff,0xff,0x48,0x45,0x4c,0x4c,0x4f,0x2d,0x4f,0x53,0x20,0x20,0x20,0x46,0x41,0x54,0x31,0x32,
            0x20,0x20,0x20,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xb8,0x00,0x00,0x8e,
            0xd0,0xbc,0x00,0x7c,0x8e,0xd8,0x8e,0xc0,0xbe,0x74,0x7c,0x8a,
            0x04,0x83,0xc6,0x01,0x3c,0x00,0x74,0x09,0xb4,0x0e,0xbb,0x0f,0x00,0xcd,0x10,0xeb,0xee,0xf4,0xeb,0xfd
    };
 
    private ArrayList<Integer> imgByteToWrite = new ArrayList<Integer>();
 
 
 
    public OperatingSystem(String s) {
        for (int i = 0; i < imgContent.length; i++) {
            imgByteToWrite.add(imgContent[i]);
        }
 
        //-----------------------------这一段是写了几个换行符，没有太大的作用，把这段删了也问题不大
        imgByteToWrite.add(0x0a);
        imgByteToWrite.add(0x0a);
        for (int j = 0; j < s.length(); j++) {
            imgByteToWrite.add((int)s.charAt(j));
        }
        imgByteToWrite.add(0x0a);
        //------------------------------
 
        int len = 0x1fe;  //0x1f3（16）=510（10）
        //如果这一段删了的话这里的len要做相应的改变
 
        int curSize = imgByteToWrite.size();
        for (int k = 0; k < len - curSize; k++) {
            imgByteToWrite.add(0);
        }
 
        /*
         * 要想让虚拟机将软盘的头512字节当作操作系统的内核加载到内存
         * 前512字节的最后两个字节必须是55,aa
         * 即第510和第511字节必须是55,aa（这是Intel规定的）
         */
        //0x1fe-0x1f: 0x55, 0xaa
        //0x200-0x203: f0 ff  ff
        imgByteToWrite.add(0x55);
        imgByteToWrite.add(0xaa);
        imgByteToWrite.add(0xf0);
        imgByteToWrite.add(0xff);
        imgByteToWrite.add(0xff);
 
        len = 0x168000;
        curSize = imgByteToWrite.size();
        for (int l = 0; l < len - curSize; l++) {   // 其余部分都用0补全
            imgByteToWrite.add(0);
        }
 
    }
 
    public void makeFllopy()   {
        /*
         * 构建虚拟软盘
         *
         * 在磁盘上创建一个1474560字节的二进制文件，将imgContent的内容写入该文件的前512字节
         * 这个二进制文件将作为一个1.5M的虚拟软盘用于当作虚拟机的启动软盘
         */
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream("system.img"));
            for (int i = 0; i < imgByteToWrite.size(); i++) {
                out.writeByte(imgByteToWrite.get(i).byteValue());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
    public static void main(String[] args) {
        OperatingSystem op = new OperatingSystem("hello, this is my first line of my operating system code");
        op.makeFllopy();
        //System.out.println(op);
    }
}