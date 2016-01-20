# JustWeEngine - Android游戏框架
An easy open source Android Native Game FrameWork.  
![logo](https://github.com/lfkdsk/JustWeTools/blob/master/picture/justwe.png)  
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-JustWeEngine-green.svg?style=true)](https://android-arsenal.com/details/1/2903)  
## Engine Flow Chart  
![engine](https://github.com/lfkdsk/JustWeEngine/blob/master/art/engine.jpg)  
## How To Use?  
* Import Engine's module as Library.  
* Import engine.jar in your project from "/jar".    
* With Gradle:  
  * Step 1. Add the JitPack repository to your build file  
  Add it in your root build.gradle at the end of repositories:  
  
  ``` groovy  
  
    	allprojects {
			repositories {
				...
				maven { url "https://jitpack.io" }
			}
		}
   	
  ```
  
  * Step 2. Add the dependency  
  
  
  ``` groovy
  
      dependencies {
	        compile 'com.github.lfkdsk:JustWeEngine:v1.02'
	  }
		
  ```
* With Maven:  
  * Step 1. Add the JitPack repository to your build file  
  
  ``` xml
  
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
  ```
  
  * Step 2. Add the dependency  
  
  ``` xml
  	
    <dependency>
	    <groupId>com.github.lfkdsk</groupId>
	    <artifactId>JustWeEngine</artifactId>
	    <version>v1.02</version>
	</dependency>
	
  ```

## Engine in V1.02

以之开发的微信打飞机游戏Demo：[Demo地址](https://github.com/lfkdsk/EngineDemo)  
很多额外控件：[JustWeTools](https://github.com/lfkdsk/JustWeTools)  
网络功能的Demo：[JustWe-WebServer](https://github.com/lfkdsk/JustWe-WebServer)  

## User Guidance 

### [JustWeEngine's WiKi](https://github.com/lfkdsk/JustWeEngine/wiki)

## Feedback    
Please send your feedback as long as there occurs any inconvenience or problem. You can contact me with:
* Email: lfk_dsk@hotmail.com  
* Weibo: [@亦狂亦侠_亦温文](http://www.weibo.com/u/2443510260)  
* Blog:  [刘丰恺](http://www.cnblogs.com/lfk-dsk/)  

## License

    Copyright 2015 [刘丰恺](http://www.cnblogs.com/lfk-dsk/)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

