org  0x7c00;

LOAD_ADDR  EQU  0X8000;当加载器读到内核代码的时候，将内核写入到系统内存0x8000开始的地方


;初始化
entry:
    mov  ax, 0
    mov  ss, ax
    mov  ds, ax
    mov  es, ax
    mov  si, ax


readFloppy:
    mov  CH, 1        ;CH 用来存储柱面号
    mov  DH, 0        ;DH 用来存储磁头号
    mov  CL, 2        ;CL 用来存储扇区号

    mov  BX, LOAD_ADDR  ;ES:BX 数据存储缓冲区

    mov  AH, 0x02      ;AH=02 表示要做的是读盘操作
    mov  AL, 1         ;AL表示要连续读取几个扇区
    mov  DL, 0         ;驱动器编号，一般我们只有一个软盘驱动器，所以写死为0

    INT  0x13          ;调用BIOS中断实现磁盘读取功能
   
    JC  fin ;如果读取出现了错误

    jmp  LOAD_ADDR ;跳转到0x8000就相当于把我们机器的控制权移交到0x8000开始的代码段



fin:
    HLT
    jmp  fin

