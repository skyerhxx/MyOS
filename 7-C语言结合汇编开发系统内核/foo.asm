extern bar_func  ;首先必须要声明这个外部函数

[section .data]
arg1  dd 3
arg2  dd 4

[section .text]
global _start
global foo_print

_start:

mov   eax, dword[arg1]
push  eax
mov   eax, dword [arg2]
push  eax
call  bar_func
add   esp, 8

mov   ebx,0
mov   eax, 1
int   0x80

foo_print:
mov   edx, [esp + 8]
mov   ecx, [esp + 4]
mov   ebx, 1
mov   eax, 4
int   0x80
ret

;%include "bar.asm"
