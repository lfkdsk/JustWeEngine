# JustWeEngine
An easy open source Android game engine.  
![logo](https://github.com/lfkdsk/JustWeTools/blob/master/picture/justwe.png)
## 引擎核心类流程图  

## 快速入门  
  由于框架全部使用SurfaceView进行绘制，不使用诸如Button、Layout等原生控件，所以应该首先新建类继承引擎核心类Engine，负责游戏的流程，注释中已有明确的标明功能。  
### 基础功能  
* 继承引擎核心类： 
 
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
  
* 绘制文字：
    
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

* 绘制图片：
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
PicUtils中提供了在Bitmap处理中很有用的各种特效和压缩方法，大家可以一试。  

* 使用精灵：
  使用精灵可以使用BaseSprite也可以继承该类使用，BaseSprite封装了很多方法供各种动画使用。  
  1. 简单初始化:  
  1. 初始化连续帧动画：  
  1. 使用从大图取出的多帧图片： 
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
  ```
  1. 一些重要的其他设定：  
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
	  ...
  ``` 
  
## 引擎初步封装完毕  
以之开发的微信打飞机游戏Demo：[Demo地址](https://github.com/lfkdsk/EngineDemo)  
