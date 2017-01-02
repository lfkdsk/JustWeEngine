# JustWeEngine - Android Game FrameWork  
![logo](art/logo.png)  
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-JustWeEngine-green.svg?style=true)](https://android-arsenal.com/details/1/2903) 
[![](https://jitpack.io/v/lfkdsk/JustWeEngine.svg)](https://jitpack.io/#lfkdsk/JustWeEngine)
[![GitHub release](https://img.shields.io/github/release/qubyte/rubidium.svg)](https://github.com/lfkdsk/JustWeEngine/releases/tag/v1.13)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/lfkdsk/JustWeEngine)
  
  
An easy open source Android Native Game FrameWork.  

## Engine Flow Chart  
![engine](art/engine.jpg)  
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
          compile 'com.github.lfkdsk:JustWeEngine:v1.13'
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
      <version>v1.10</version>
  </dependency>

  ```

## Engine in V1.10

* A plane game Demo：[Demo地址](https://github.com/lfkdsk/EngineDemo)  
* Extra modules：[JustWeTools](https://github.com/lfkdsk/JustWeTools)  
* Demo for network：[JustWe-WebServer](https://github.com/lfkdsk/JustWe-WebServer)  
* StudioVSEclipse from [ice1000](https://github.com/ice1000)：[StudioVSEclipse](https://github.com/ice1000/StudioVSEclipse)  

## User Guidance  

### [English Guidance](eng_info.md)

### [中文教程](info.md)  

### [繁體中文教程](tw_info.md)  

### [JustWeEngine's WiKi](https://github.com/lfkdsk/JustWeEngine/wiki)

## Feedback    
Please send your feedback as long as there occurs any inconvenience or problem. You can contact me with:
* Email: lfk_dsk@hotmail.com  
* Weibo: [@亦狂亦侠_亦温文](http://www.weibo.com/u/2443510260)  
* Blog:  [刘丰恺](http://www.cnblogs.com/lfk-dsk/)  

## License

    Copyright 2017 [刘丰恺](http://www.cnblogs.com/lfk-dsk/)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

