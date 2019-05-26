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

程序加密算法如下：

![加密算法图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6%20(1).png)

![程序图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/QQ%E6%88%AA%E5%9B%BE20181227142345.png)

## 版本说明
V0.9 2018.DEC.27 基础功能完成，可以凑合使用，将会持续更新，周期不定

## 文件完整性验证
```
Military.QQ.jar
MD5:    b797d110fd1de5e97ae8a756440fdb32
SHA1:   8d3d25b135777d2a8fd9b0b0eb07e8821d7d986d
SHA256: d6d484324798f5d708f1cad9f5111b6f3e2fe2babb10676cd84df61e43ed80c8
```

# 不求捐赠但求领红包

![图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/%E6%94%AF%E4%BB%98%E5%AE%9D%E7%BA%A2%E5%8C%85.jpg)
