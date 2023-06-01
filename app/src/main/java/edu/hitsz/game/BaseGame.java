package edu.hitsz.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.hitsz.ImageManager;
import edu.hitsz.R;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.dao.ScoreDao;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.prop.*;

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 * @author hitsz
 */
public abstract class BaseGame extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static final String TAG = "BaseGame";
    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Handler handler;

    //点击屏幕位置
    float clickX = 0, clickY=0;

    private int backGroundTop = 0;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;



    /**
     * 时间间隔(ms)，控制刷新频率
     */
    public int timeInterval = 16;

    private final HeroAircraft heroAircraft;

    public static List <Enemy> enemyAircrafts;

    private final List<AbstractBullet> heroBullets;
    public static List<AbstractBullet> enemyBullets;
    protected List<BaseProp> baseProps;
    protected EnemyFactory enemyFactory;
    protected Enemy enemy;

    public static int score = 0;
    public boolean gameOverFlag = false;
    private int time = 0;

    public static boolean needMusic = false;

    public static SoundPool mysp;
    private AudioAttributes audioAttributes = null;
    public static HashMap soundPoolMap;

    protected static boolean bossVaild = false;
    protected static boolean bossVailded = false;

    /**
     * 游戏难度等级
     */
    public static int difficulty;

    public static ScoreDao scoreDao;

    /**
     * 周期（ms)
     * 指示英雄机子弹的发射、敌机的产生频率
     */
    private final int heroCycleDuration = 450;
    private int heroCycleTime = 0;

    protected int enemyCycleTime = 0;

    /**
     * 周期（ms）
     * 指示游戏难度提升的频率
     */

    private final int difficultyCycleDuration = 15000;
    private int difficultyCycleTime = 0;



    public BaseGame(Context context, Handler handler){
        super(context);
        this.handler = handler;
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        // 创建音乐
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mysp = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        soundPoolMap = new HashMap<Integer,Integer>();
        soundPoolMap.put("bullet_hit", mysp.load(context, R.raw.bullet_hit,1));
        soundPoolMap.put("bomb_explosion",mysp.load(context, R.raw.bomb_explosion,1));
        soundPoolMap.put("game_over",mysp.load(context, R.raw.game_over,1));
        soundPoolMap.put("get_supply",mysp.load(context, R.raw.get_supply,1));

        // 初始化英雄机
        heroAircraft = HeroAircraft.getHeroAircraft();
        heroAircraft.setHp(1000);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        baseProps = new LinkedList<>();

        heroController();
    }
    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        //new Thread(new Runnable() {
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制游戏难度提升频率）
            if (difficultyTimeCountAndNewCycleJudge()) {
                difficulty += 1;
                System.out.println("------------------------");
                System.out.println("游戏难度提升！ 当前游戏难度为："+difficulty);
                System.out.println("最大敌机数量："+difficulty);
                System.out.println("敌机血量提升倍率："+(1+difficulty*0.1));
                System.out.println("Boss机血量提升！");
                System.out.println("敌机生成加快！");
                System.out.println("敌机射击加快！");
            }
            // 周期性执行（控制英雄机射击频率）
            if (heroTimeCountAndNewCycleJudge()) {
                // 新敌机产生
                newEnemy();
                // 英雄机射出子弹
                heroAircraftShootAction();
            }

            if (enemyTimeCountAndNewCycleJudge()) {
                newEnemy();
                enemyShootAction();
            }

//            // 周期性执行（控制频率）
//            if (timeCountAndNewCycleJudge()) {
//                // 生成新敌机
//                newEnemy();
//                // 英雄机和敌机射击子弹
//                shootAction();
//            }

            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            // 道具移动
            propsMoveAction();
            // 撞击检测
            try {
                crashCheckAction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 后处理
            postProcessAction();

            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(task).start();
    }

    public void heroController(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickX = motionEvent.getX();
                clickY = motionEvent.getY();
                heroAircraft.setLocation(clickX, clickY);

                if ( clickX<0 || clickX> MainActivity.screenWidth || clickY<0 || clickY>MainActivity.screenHeight){
                    // 防止超出边界
                    return false;
                }
                return true;
            }
        });
    }

    protected boolean difficultyTimeCountAndNewCycleJudge() {
        difficultyCycleTime += timeInterval;
        if (difficultyCycleTime >= difficultyCycleDuration && difficultyCycleTime - timeInterval < difficultyCycleTime) {
            // 跨越到新的周期
            difficultyCycleTime %= difficultyCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 敌机的生成方法，在不同难度下有不同实现
     */
    protected abstract void newEnemy();

    private void heroAircraftShootAction(){
        heroBullets.addAll(heroAircraft.shoot());
    }

    /**
     * 敌机的射击方法，在不同难度下有不同实现
     */
    protected abstract void enemyShootAction();

    private boolean heroTimeCountAndNewCycleJudge() {
        heroCycleTime += timeInterval;
        if (heroCycleTime >= heroCycleDuration && heroCycleTime - timeInterval < heroCycleTime) {
            // 跨越到新的周期
            heroCycleTime %= heroCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean enemyTimeCountAndNewCycleJudge();

    private void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractFlyingObject baseProp : baseProps) {
            baseProp.forward();
        }
    }

    /**
     * 碰撞检测：
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() throws InterruptedException {
        // 敌机子弹攻击英雄
        for (AbstractBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (AbstractBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (Enemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if(needMusic){
                        Runnable r1 = () -> {
                            mysp.play((Integer) soundPoolMap.get("bullet_hit"),1,1,0,0,1.2f);
                        };
                        new Thread(r1,"1").start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if (enemyAircraft instanceof BossEnemy) {
                            bossVaild = false;
                        }
                        baseProps.addAll(enemyAircraft.drop_prop());
                        score = enemyAircraft.increaseScore(score);
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }
        // 我方获得道具，道具生效
        for (BaseProp baseProp : baseProps) {
            if (baseProp.notValid()) {
                continue;
            }
            if (heroAircraft.crash(baseProp)) {
                baseProp.active(heroAircraft);
                baseProp.vanish();
            }
        }
    }
    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        baseProps.removeIf(AbstractFlyingObject::notValid);

        if (heroAircraft.notValid()) {
            gameOverFlag = true;
            mbLoop = false;
            Log.i(TAG, "heroAircraft is not Valid");
            if(needMusic){
                Runnable r2 = () -> {
                    mysp.play((Integer) soundPoolMap.get("game_over"),1,1,0,0,1.2f);
                };
                new Thread(r2,"2").start();
            }

        }
    }

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder == null || canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround,0,this.backGroundTop-backGround.getHeight(),mPaint);
        canvas.drawBitmap(backGround,0,this.backGroundTop,mPaint);
        backGroundTop +=1;
        if (backGroundTop == MainActivity.screenHeight)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets); //敌机子弹
        paintImageWithPositionRevised(heroBullets);  //英雄机子弹
        paintImageWithPositionRevised(enemyAircrafts);//敌机
        //绘制道具
        paintImageWithPositionRevised(baseProps);

        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY()- ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 40;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);

        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        y = y + 60;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.screenWidth = i1;
        MainActivity.screenHeight = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @Override
    public void run() {

        while (mbLoop){   //游戏结束停止绘制
            synchronized (mSurfaceHolder){
                action();
                draw();
            }
        }
        Message message = Message.obtain();
        message.what = 1 ;
        handler.sendMessage(message);
    }
}
