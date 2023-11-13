package com.ava.engine;

import java.awt.image.DataBufferInt;

import com.ava.engine.gfx.Font;
import com.ava.engine.gfx.Image;
public class Renderer {
    private int pw,ph;
    private int[] p;
    private Font font  = Font.STANDARD;

    public Renderer(GameContainer gc) {
         pw = gc.getWidth();
         ph = gc.getHeight();
         p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();

    }

    public void clear(){
        for(int i=0;i<p.length;i++)
            p[i] = 0;
    }
  public void setPixel(int x,int y, int value){
        if((x<0 || x>= pw || y<0 || y>=ph)||( (value >> 24) & 0xff)==0)
            return;
  p[x + y*pw] = value;
    }
    int offset =0;
    public void drawText(String text, int offX, int offY, int color){
         text = text.toUpperCase();

        for(int i=0;i<text.length();i++){
            int unicode = text.codePointAt(i) - 32;
            for(int y=0;y<font.getFontImage().getH();y++){
                for(int x=0; x<font.getWidths()[unicode];x++){
                    if(font.getFontImage().getP()[(x+ font.getOffsets()[unicode])+ y*font.getFontImage().getW()] == 0xffffffff){
                        setPixel(x + offX + offset,y+ offY,color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }

    }

    public void drawImage(Image image, int offX,int offY){
        //offx and offy are the mouse inputs which offset the posn of the image



        //dont render code
        if(offX < -image.getW()) return;
        if(offY < -image.getH()) return;
        if(offX >= pw) return;
        if(offY >= ph) return;
        int newX=0;
        int newY=0;
        int newWidth= image.getW();
        int newHeight = image.getH();

        //clipping code
        if(offX<0) newX -= offX;
        if(offY<0) newY -= offY;
        if( newWidth + offX > pw){newWidth -= (newWidth + offX -pw);}
        if( newHeight + offY > ph){newHeight -=(newHeight + offY -ph);}
        for(int y=newY;y<newHeight;y++){
            for(int x=newX;x<newWidth;x++){
            setPixel(x+offX,y+offY, image.getP()[x+y*image.getW()]);
            }
        }
    }
    public void drawRect(int offX, int offY, int width, int height, int color){
        for(int y =0;y<=width;y++){
            setPixel(offX,y+offY,color);
            setPixel(offX + width,y+offY,color);

        }
        for(int x=0;x<=width;x++){
            setPixel(x+ offX,offY,color);
            setPixel(x+ offX ,height+offY,color);

        }
    }
    public void drawfillRect(int offX, int offY, int width, int height, int color){
        for(int y =0;y<=width;y++){
            for(int x=0;x<=width;x++){
                setPixel(x+ offX,y+offY,color);
               ;

            }
        }

        }

}
