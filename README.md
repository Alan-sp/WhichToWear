# Android assignment for BIT 2024 (with mirage)
  
<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=29"><img alt="API" src="https://img.shields.io/badge/API-29%2B-brightgreen.svg?style=flat"/></a>
</p>

## 1、架构设计
**Clean Android Architect**  
以Sui为例
![[./pic/架构.png]]
## 2、ui
总共ui如下  
![[./pic/ui.png]]
## 3、数据库设计
![[./pic/数据库.png]]
## 4、功能全解
1. **实现天气获取**  
Hilt + retrofit2 组件访问 OpenWeatherMap API 获取当地的天气信息  
将访问API 返回的JSON字符串自行解码，获取所需要的信息（JSON 对象），再利用viewmodel中的业务逻辑刷新UI状态  
2. **实现数据库**

Room + hilt  
clothing主要拥有id、image、warmth等属性  

3. **实现问卷**

一个surveyScreen内部进行跳转  
写出简单question模板    
survey中具体实现  

日期选取的版本迭代  
https://github.com/android/compose-samples  

4. **实现调色盘**

第三方库  
https://github.com/skydoves/colorpicker-compose/tree/main  

合并调色盘与问卷  
使用ColorEnvelope进行期间的颜色数据传递  

为了实现图片的传递则需要将url转为ImageBitmap  

5. **服装推荐算法**

通过“今日是否有运动安排”、“今日是否参加正式活动”等选项简要描述当天活动  
推荐算法中采用了Kotlin中的random方法，保留了一定的随机性，便于用户自行调整  
先利用一些已知条件筛选出备选集合，再从集合中随机选取衣服进行搭配  

6. **实现图片添加的两种方法**

调用系统图库进行选取  
调用系统摄像头进行拍照获得  



## License
```xml
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```