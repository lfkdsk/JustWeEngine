# JustWeEngine - 轻量级游戏框架
An easy open source Android game engine.  
![logo](https://github.com/lfkdsk/JustWeTools/blob/master/picture/justwe.png)
## 引擎核心类流程图  
![engine](https://github.com/lfkdsk/JustWeEngine/blob/master/art/engine.jpg)  
## 使用方法  
	引入Engine作为Library进行使用。
## 快速入门  
  由于框架全部使用SurfaceView进行绘制，不使用诸如Button、Layout等原生控件，所以应该首先新建类继承引擎核心类Engine，负责游戏的流程，注释中已有明确的标明功能。  
### 基础功能  
#### 继承引擎核心类： 
 
``` java

	public class Game extends Engine {
	// 一般在构造函数完成变量的初始化
    public Game() {
    	// 控制debug模式是否开始，debug模式打印日志、帧数、pause次数等信息
        super(true);
        
    }
	
	// 载入一些UI参数和设置屏幕放向，默认背景色，设定屏幕的扫描方式等
    @Override
    public void init() {
    	// 初始化UI默认参数，必须调用该方法，其中有一些用于多机型适配的参数需要初始化
        UIdefaultData.init(this);
    }

	// 通常用于精灵，背景，图片等物体的设置和载入
    @Override
    public void load() {

    }

	// draw 和 update 在线程中进行不断的循环刷新
	// draw 负责绘制 update 负责更新数据和对象信息
    @Override
    public void draw() {

    }

    @Override
    public void update() {

    }
	
	// 将touch 事件传回 功能和所设定的屏幕扫描方式有关
    @Override
    public void touch(MotionEvent event) {

    }
    
	// 将碰撞事件传回 传回精灵和物品的基类 
	// 用于处理碰撞事件 默认使用矩形碰撞
    @Override
    public void collision(BaseSub baseSub) {

    }
    }

```   
  
#### 绘制文字：
    
使用GamePrinter进行文字绘制,除此以外还有多种方法绘制:  
  
``` java

    @Override
    public void draw() {
        Canvas canvas = getCanvas();
        GameTextPrinter printer = new GameTextPrinter(canvas);
        printer.drawText("哈哈哈", 100, 100);
    }
    
```  
效果图：  
![text](https://github.com/lfkdsk/JustWeEngine/blob/master/art/printer.png)  

#### 绘制图片：
建议图片存放在Asset中：  
``` java  
	GameTexture texture = new GameTexture(this);
	texture.loadFromAsset("pic/logo.jpg")
	texture.draw(canvas, 100, 100);
```  
效果图：    
![pic](https://github.com/lfkdsk/JustWeEngine/blob/master/art/pic.png)  
另外也可使用`loadFromAssetStripFrame`从一个大的图片中取出对应位置的图片。  

``` java

	/**
     * get bitmap from a big bitmap
     *
     * @param filename
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public boolean loadFromAssetStripFrame(String filename,
                                           int x, int y,
                                           int width, int height)
```  
比如可以通过这四个参数把这个小飞机取出来： 
![back](https://github.com/lfkdsk/JustWeEngine/blob/master/art/back.png)  
![ship](https://github.com/lfkdsk/JustWeEngine/blob/master/art/ship.png)  
PicUtils中提供了在Bitmap处理中很有用的各种特效和压缩方法，大家可以一试。  

#### 使用精灵：
  使用精灵可以使用BaseSprite也可以继承该类使用，BaseSprite封装了很多方法供各种动画使用，这些功能很多都是需要结合动画系统来使用的，动画系统会在后面介绍。  
##### 新建精灵：
  1.简单初始化:  
  ``` java
  
          sprite = new BaseSprite(this);
          
  ```
  2.初始化连续帧动画：  
  连续帧的初始化需要这样的连续帧图片:  
  ![zombie](https://github.com/lfkdsk/JustWeEngine/blob/master/art/zombie.png)
  
  ``` java 
  
        GameTexture texture = new GameTexture(this);
        texture.loadFromAsset("pic/zombie.png");
        // 长宽以及列数
        sprite = new BaseSprite(this, 96, 96, 8);
        sprite.setTexture(texture);
        sprite.setPosition(100, 100);
        sprite.setDipScale(100, 100);
        // 帧切换动画是关键
        sprite.addAnimation(new FrameAnimation(0, 63, 1));
        addToSpriteGroup(sprite);
        
  ```
  
  效果图:  
  ![zombiegif](https://github.com/lfkdsk/JustWeEngine/blob/master/art/zombie.gif)  
  3.使用从大图取出的多帧图片： 
  ``` java  
  
    	// 新建图片资源（此图为上图的大图）
        GameTexture texture = new GameTexture(this);
        texture.loadFromAsset("pic/shoot.png");
        // 初始化设定模式和长宽
  		ship = new BaseSprite(this, 100, 124, FrameType.COMMON);
  		// 设定资源
        ship.setTexture(texture);
        // 从大图中取出两帧
        ship.addRectFrame(0, 100, 100, 124);
        ship.addRectFrame(167, 361, 100, 124);
        ship.addAnimation(new FrameAnimation(0, 1, 1));

  ```
  效果图(两帧图片不断切换):  
  ![ship](https://github.com/lfkdsk/JustWeEngine/blob/master/art/ship.gif)  

  4.一些重要的其他设定：
    
  ``` java  
  
  	  // 图片资源
  	  ship.setTexture(texture);
  	  // 大图取帧模式
  	  ship.addRectFrame(0, 100, 100, 124);
  	  // 设定位置
  	  ship.setPosition(x, y);
  	  // 设置dp大小
      ship.setDipScale(96, 96);
      // 设定dp位置
	  ship.setDipPosition(x, y);
	  // 设定透明度
	  ship.setAlpha(...);
	  // 只有将精灵添加到SpriteGroup中框架才会自行绘制，否则需要手动调用
	  addToSpriteGroup(ship);
	  ...
	  
  ``` 

#### 使用按钮：  
  使用的按钮可以继承BaseButton进行拓展，也可以直接使用TextureButton和TextButton进行使用。  
  Button设定功能的方式和原生一样，通过设定接口回调的方式进行：
  ``` java  
  
  		button.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick() {
              Log.e("button", "onClick");
          }
        });
        
  ```
  1.TextureButton: 
   
  ``` java  
  
      TextureButton button;
      // 初始化并设定名字
      button = new TextureButton(this, "logo");
	  texture = new GameTexture(this);
      texture.loadFromAsset("pic/logo.jpg");
      // 添加图片资源
      button.setTexture(texture);
      // button的接口回调，不是View的那个接口
      button.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick() {
              Log.e("button", "onClick");
          }
        });
      button.setPosition(200, 300);
      button.setDipScale(100, 150);
      // 添加到ButtonGroup进行绘制和处理
      addToButtonGroup(button);

  ``` 
  效果图:  
  ![texturebutton](https://github.com/lfkdsk/JustWeEngine/blob/master/art/Texturebutton.png)  
    结合PicUtil中的各种Bitmap处理方法可以很容易的做出各种样式的Button：  
  ![buttons](https://github.com/lfkdsk/JustWeEngine/blob/master/art/buttons.jpg)  
  
  2.TextButton:  
  
  ``` java  
  	  
      TextButton button;  
      button = new TextButton(this, "logo");
      button.setText("刘丰恺");
      addToButtonGroup(button);
      // 余略见源码
	  ...
	  
  ```
  效果图：  
![button](https://github.com/lfkdsk/JustWeEngine/blob/master/art/singlebutton.png)  

### 动画系统  
  目前的动画系统可以使用已经封装好的继承了BaseAnimation的动画，也可以继承BaseAnim进行自我定义动画类进行使用。  
#### 绑定在`BaseSub`（物品及精灵基类）上的动画类：  
AnimType中保存了Animation的应用类型。

| Animation     | method        |function|
| ------------- |:-------------:|-------:|
| AliveAnimation|adjustAlive(boolean ori) | 碰撞检测的时候进行判断存活状态 |
| AlphaAnimation|adjustAlpha(int ori)     | 修改物体透明度              |
| CircleMoveAnimation | adjustPosition(Float2 ori)| 沿某一圆心进行圆周运动 |
| FenceAnimation | adjustPosition(Float2 ori)| 使用围栏动画防止出界 |
| FrameAnimation | adjustFrame(int ori) | 逐帧动画 |
| MoveAnimation | adjustPosition(Float2 ori) | 位移动画 |
| SpinAnimation | adjustRotation(float ori) | 旋转动画 |
| ThrobAnimation | adjustScale(Float2 ori) | 跳跃动画 |
| VelocityAnimation | adjustPosition/adjustAlive | 线性加速度计 |
| WrapMoveAnimation | adjustPosition(Float2 ori) | 围栏动画防止出界 |
| ZoomAnimation | adjustScale(Float2 ori) | 放大缩小动画 |
| 待续 | ... | ... |

绑定动画分为两类，ListAnimation和FixedAnimation,ListAnimation将动画存储到固定的一个List中，用于重复更新的动画，
而FixedAnimation存储在Map中，使用名字进行调用，用于点击或者非自动更新的动画。
比如前面精灵类动画的就是添加到ListAnimation。
下面的这种写法就是FixedAnimation，这个动画是小飞机入场，因为只使用了一次，所以使用了FixedAnimation。
``` java

        ship.addfixedAnimation("start",
                new MoveAnimation(UIdefaultData.centerInHorizontalX -   ship.getWidthWithScale() / 2,
                        UIdefaultData.screenHeight - 2 * ship.getHeightWidthScale(), new Float2(10, 10)));
           
```

效果图:  
![fly](https://github.com/lfkdsk/JustWeEngine/blob/master/art/fly.gif)  

#### 绑定在`Button`上的动画类：  
BaseButtonAnimation是BaseButton的动画类继承了BaseAnim的动画基类，通过提供Button的状态，设定Button的动画。

| Animation        | method           | function  |
| ------------- |:-------------:| -----:|
| ZoomCenterButtonAnim |adjustButtonRect(Rect ori,boolean touchType) | 按钮放缩动画 |
| ColorAnimation|adjustButtonBackGround(int ori,boolean type)| TextButton点击变色 |
| 待续 | ... | ... |

为Button设定放缩动画:  
``` java

	// 设定中心放缩
    button.setZoomCenter(true);
    // 三个参数 初始值／放大值／帧数
    button.setAnimation(new ZoomCenterButtonAnim(10, 30, 3));
	
```
效果图:  
![zoom](https://github.com/lfkdsk/JustWeEngine/blob/master/art/zoom.gif)    

为Button设定颜色动画:  

``` java

	// 初始颜色 ／ 按下颜色
	button.setAnimation(
       new ColorAnimation(UIdefaultData.colorAccent,
       UIdefaultData.colorPressed));

```
效果图:  
![color](https://github.com/lfkdsk/JustWeEngine/blob/master/art/button.gif)    

### 碰撞检测和死亡判定
只要使用了`addToSpriteGroup(sprite)`的精灵对象就会自动进行碰撞检测，而对碰撞检测的结果会从
`collision`中进行发回。
``` java

    @Override
    public void collision(BaseSub baseSub) {
    	// 获取与之碰撞的对象
        BaseSprite other = (BaseSprite) baseSub.getOffender();
        // 获取ID分组处理
        if (baseSub.getIdentifier() == BULLET &&
                other.getIdentifier() == ENEMY) {
            // 设定死亡
            other.setAlive(false);
            // 回收
            removeFromSpriteGroup(other);
            addToRecycleGroup(other);
        }
    }
    
```

其中`getOffender()`获得与之碰撞的对象，通过`getIdentifier()`获取设定的对象分组，实行逻辑判断。
开启Debug模式会看见碰撞线。  
效果图:  
![debug](https://github.com/lfkdsk/JustWeEngine/blob/master/art/co.png)
### 屏幕扫描模式  
屏幕扫描模式是用来优先响应屏幕点击、Button点击、和多点触控而设的，放置在不同情况下都能优化屏幕的刷新。  
``` java

  	// 检测单一移动
  	SINGLE,
  	// 检测Button
    BUTTON,
    // 多点检测
    FULL,
    // 单击＋Button
    SINGLE_BUTTON
  
```

## 引擎初步封装完毕  
以之开发的微信打飞机游戏Demo：[Demo地址](https://github.com/lfkdsk/EngineDemo)  

##有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮件:lfk_dsk@hotmail.com  
* weibo: [@亦狂亦侠_亦温文](http://www.weibo.com/u/2443510260)  
* 博客:  [刘丰恺](http://www.cnblogs.com/lfk-dsk/)  

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

