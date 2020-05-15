%macro Descriptor 3
    dw    %2  &  0FFFFh  ;byte1和byte0
    dw    %1  &  0FFFFh  ;byte3和byte2
    db   (%1>>16) & 0FFh
    dw   ((%2 >> 8) & 0F00h) | (%3 & 0F0FFh)
    db   (%1 >> 24) & 0FFh
%endmacro


DA_32		EQU	4000h	; 32 位段
DA_C		EQU	98h	; 存在的只执行代码段属性值
DA_DRW		EQU	92h	; 存在的可读写数据段属性值