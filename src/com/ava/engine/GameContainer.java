package com.ava.engine;

import java.awt.event.KeyEvent;

public class GameContainer implements Runnable {
    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;
    private boolean running =false;
private final double UPDATE_CAP = 1.0/60.0;
private int width=320, height=240;



    private float scale = 4f;
private String title= "AvaEngine";
    public GameContainer(AbstractGame game){
        this.game = game;
    }
    public void start(){

        window = new Window(this);

        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
    thread.run();
    }

    public Input getInput() {
        return input;
    }

    public void stop(){

    }
    public void run(){
        running=true;
        boolean render = false;
        double firstTime=0;
        double lastTime=System.nanoTime()/ 1000000000.0;
        double passedTime=0;
        double unproccessedTime=0;
        double frameTime=0;
        int frames=0;
        int fps=0;
        while(running){
            render=false;
    firstTime =System.nanoTime()/1000000000.0;
    passedTime=firstTime-lastTime;
    lastTime=firstTime;
    unproccessedTime += passedTime;
    frameTime+= passedTime;
    while (unproccessedTime >= UPDATE_CAP){
        unproccessedTime -= UPDATE_CAP;
        render=true;

        //TODO:Update game
        game.update(this,(float)UPDATE_CAP);
        if(input.isKey(KeyEvent.VK_E))
            System.out.println("E is pressed");
//        System.out.println(input.getScroll());
//        System.out.println("mouseX: "+ input.getMouseX()+" mouseY: "+ input.getMouseY());
        input.update();

        if(frameTime >= 1.0){
            frameTime=0;
            fps=frames;
            frames=0;
            System.out.println("fps:"+fps);

        }
    }
    if(render){
        //TODO: RENDER GAME
        renderer.clear();
        game.render(this,renderer);
        renderer.drawText("FPS:"+fps,0,0,0xff00ffff );

        window.update();
        frames++;

    }
    else{
        try {
            Thread.sleep(1);
            //helps to manage tasks
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }
}
        dispose();
    }
    private  void dispose(){

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Window getWindow() {
        return window;
    }


}

