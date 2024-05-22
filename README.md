# Android assignment for BIT 2024 (with mirage)

## 架构设计
Clean Android Architect
## 预期功能

## 现行成果

1. 实现天气获取  



2. 实现数据库

Room + hilt  
clothing主要拥有id、image、warmth等属性  

3. 实现问卷

一个surveyScreen内部进行跳转  
写出简单question模板    
survey中具体实现  

日期选取的版本迭代  

4. 实现调色盘

第三方库  
https://github.com/skydoves/colorpicker-compose/tree/main  

合并调色盘与问卷  
使用ColorEnvelope进行期间的颜色数据传递  

为了实现图片的传递则需要将url转为ImageBitmap  

5. 服装推荐算法


6. 实现图片添加的两种方法

调用系统图库进行选取  
调用系统摄像头进行拍照获得  
