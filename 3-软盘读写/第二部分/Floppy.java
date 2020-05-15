import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class Floppy {
    enum MAGNETIC_HEAD {
        //磁头编号0对应着上面的磁头(读上盘面)，1对应着下面的磁头(读下盘面)
        MAGNETIC_HEAD_0,
        MAGETIC_HEAD_1
    };


    public int SECTOR_SIZE = 512;//1个扇区存储容量大小是512字节
    private int CYLINDER_COUNT = 80; //80个柱面
    private int SECTORS_COUNT = 18;//每个柱面都有18个扇区，也就是1个圆圈含有18个扇形
    private MAGNETIC_HEAD magneticHead = MAGNETIC_HEAD.MAGNETIC_HEAD_0;
    private int current_cylinder = 0;
    private int current_sector = 0;

    //HashMap来模拟整个盘片
    //byte[] 对应一个扇区
    //ArrayList对应一个磁道，或者叫一个柱面   一个ArrayList含有18个byte[]
    //外层的ArrayList含有80个里面的子ArrayList
    private HashMap<Integer,ArrayList<ArrayList<byte[]>> > floppy = new HashMap<Integer,ArrayList<ArrayList<byte[]>> >(); //一个磁盘两个面

    public Floppy() {
        //初始化
        initFloppy();
    }

    private void initFloppy() {
        //一个磁盘有两个盘面
        //初始化两个盘面，将2个盘面加入到floppy Hashmap中
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_0.ordinal(), initFloppyDisk());
        floppy.put(MAGNETIC_HEAD.MAGETIC_HEAD_1.ordinal(), initFloppyDisk());
    }

    //初始化盘面
    private ArrayList<ArrayList<byte[]>> initFloppyDisk() {
        ArrayList<ArrayList<byte[]>> floppyDisk = new ArrayList<ArrayList<byte[]>>(); //磁盘的一个面
        //一个磁盘面有80个柱面
        for(int i = 0; i < CYLINDER_COUNT; i++) {
            floppyDisk.add(initCylinder());
        }

        return floppyDisk;
    }

    private ArrayList<byte[]> initCylinder() {
        //构造一个柱面，一个柱面有18个扇区
        ArrayList<byte[]> cylinder = new ArrayList<byte[]> ();
        for (int i = 0; i < SECTORS_COUNT; i++) {
            byte[] sector = new byte[SECTOR_SIZE];
            cylinder.add(sector);
        }

        return cylinder;
    }

    //读写的时候首先要确定磁头，即确定要读取的盘面是上面还是下面
    public void setMagneticHead(MAGNETIC_HEAD head) {
        magneticHead = head;
    }

    //设置磁道编号0-79
    public void setCylinder(int cylinder) {
        if (cylinder < 0) {
            this.current_cylinder = 0;
        }
        else if (cylinder >= 80) {
            this.current_cylinder = 79;
        }
        else {
            this.current_cylinder = cylinder;
        }
    }

    //确定要访问的扇区编号
    public void setSector(int sector) {
        //sector 编号从1到18
        if (sector < 0) {
            this.current_sector = 0;
        }
        else if (sector > 18) {
            this.current_sector = 18 - 1;
        }
        else {
            this.current_sector = sector - 1;
        }
    }

    //读扇区
    public byte[] readFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num) {
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        ArrayList<ArrayList<byte[]>> disk = floppy.get(this.magneticHead.ordinal());
        ArrayList<byte[]> cylinder = disk.get(this.current_cylinder);

        byte[] sector = cylinder.get(this.current_sector);

        return sector;
    }

    //写扇区
    public void writeFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num, byte[] buf) {
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        ArrayList<ArrayList<byte[]>> disk = floppy.get(this.magneticHead.ordinal());
        ArrayList<byte[]> cylinder = disk.get(this.current_cylinder);

        byte[] buffer = cylinder.get(this.current_sector);
        System.arraycopy(buf, 0, buffer, 0, buf.length);
    }

    public void makeFloppy(String fileName) {
        try {
            /*
                虚拟软盘是二进制文件
                前512*18字节的内容对应盘面0，柱面0的所有扇区内容
                接着的512*18字节对应盘面1，柱面0的所有扇区内容
                再接着的512*18字节对应盘面0，柱面1的所有扇区内容
                再接着的512*18字节对应盘面1，柱面1的所有扇区内容
                以此类推
             */
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            for (int cylinder = 0; cylinder < CYLINDER_COUNT; cylinder++) {
                for (int head = 0; head <= MAGNETIC_HEAD.MAGETIC_HEAD_1.ordinal(); head++) {
                    for (int sector = 1; sector <= SECTORS_COUNT; sector++) {
                        byte[] buf = readFloppy(MAGNETIC_HEAD.values()[head], cylinder, sector);

                        out.write(buf);
                    }

                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
