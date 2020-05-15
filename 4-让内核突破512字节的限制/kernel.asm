org   0x8000 ;告诉编译器我们这段代码将会被加载到0x8000位置。这样编译器在编译的时候，寻址的时候会以0x8000开始

;初始化
entry:
    mov  ax, 0
    mov  ss, ax
    mov  ds, ax
    mov  es, ax
    mov  si, msg


;将msg这句话打印到屏幕上
putloop:
    mov  al, [si]
    add  si, 1
    cmp  al, 0
    je   fin
    mov  ah, 0x0e
    mov  bx, 15
    int  0x10
    jmp  putloop

fin:
    HLT
    jmp  fin

msg:

    DB   "This is Hello World from kernel"
