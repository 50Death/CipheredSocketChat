# CipheredSocketChat
# 加密TCP/IP直连聊天工具

### 环境Environment
java version "1.8.0_191" 
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)

## IDE
IntelliJ IDEA

## 简介
聊天内容通过AES, RSA, RSASignatue多重加密

建立TCP连接后首先进行RSA公钥交换（明文）

聊天内容使用AES加密，秘钥为64位ASCII码随机数

秘钥再使用对方的RSA公钥加密

然后对上述内容进行证书签名，防止伪造和中间人攻击

![图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/QQ%E6%88%AA%E5%9B%BE20181227140513.png)

## 版本说明
V0.9 2018.DEC.27 基础功能完成，可以凑合使用，将会持续更新，周期不定

## 基础功能已实现，仍存在如下问题
单次发送内容不得超过大约320chars，否则自动分为两个Socket包导致无法解密

聊天主窗口使用的是JTextPane以达到接收和发送颜色不同，但是尚未添加ScrollBar滚动条，单次发送内容过长会导致窗口变形

控制台窗口一旦关闭无法再次开启必须重新运行

受限于TCP/IP连接模式，无公网IP地址无法当主机Server，即使用Client模式时，对方IP地址Des IP不应当以192.168开头，除非您与对方处在同一局域网内

*证书文件路径无法在运行时更改，必须在代码中做出相应更改
