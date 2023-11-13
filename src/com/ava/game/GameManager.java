package com.ava.game;

import com.ava.engine.AbstractGame;
import com.ava.engine.GameContainer;
import com.ava.engine.Renderer;
import com.ava.engine.audio.SoundClip;
import com.ava.engine.gfx.Image;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame{
    private Image image;
    private SoundClip clip;
    public GameManager() {
        image = new Image("/test.jpg");
        clip = new SoundClip("/audio.wav");
    }

    @Override
    public void update(GameContainer gc, float dt) {
//if(gc.getInput().isKeyDown(KeyEvent.VK_E))
  if(gc.getInput().getScroll()>0)
      clip.play();
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
r.drawImage(image,gc.getInput().getMouseX() -32,gc.getInput().getMouseY()-32);
   r.drawfillRect(gc.getInput().getMouseX()-16,gc.getInput().getMouseY()-16,32,32,0xffffccff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(3f);
        gc.start();

    }
}