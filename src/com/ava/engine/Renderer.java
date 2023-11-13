package com.ava.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.ava.engine.gfx.Font;
import com.ava.engine.gfx.Image;
import com.ava.engine.gfx.ImageRequest;
import com.ava.engine.gfx.ImageTile;

public class Renderer {
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
    private int pw,ph;
    private int[] p;
    private int[] zb;
    private int zDepth = 0;
    private boolean processing = true;

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }


    //z buffer, if we have an opaque image overlaying a transparent one,this buffer handles it
    private Font font  = Font.STANDARD;

    public Renderer(GameContainer gc) {
         pw = gc.getWidth();
         ph = gc.getHeight();
         p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
       zb = new int[p.length];
    }

    public void clear(){
        for(int i=0;i<p.length;i++){
            p[i] = 0;
        zb[i]=0;//default state
    }
    }
    public void process(){
        processing = true;
        for(int i=0;i<imageRequest.size();i++){
            ImageRequest ir = imageRequest.get(i);
            setzDepth(ir.zDepth);
            ir.image.setAlpha(false);
            drawImage(ir.image, ir.offX, ir.offY);
        }
        imageRequest.clear();
        processing=false;
    }

  public void setPixel(int x,int y, int value){
        int alpha = (value >> 24)& 0xff;
        if((x<0 || x>= pw || y<0 || y>=ph)||(alpha) ==0)
            return;
        if(zb[x+ y*pw] > zDepth)
            return;
        if(alpha == 255)
            p[x+ y*pw] =value;
        else {
            int pixelColor = p[x+ y*pw];

            int newRed = ((pixelColor >> 16 )& 0xff)- (int)((((pixelColor >> 16) & 0xff) -( (value >> 16) & 0xff)) *(alpha/255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) *(alpha/255f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor  & 0xff)- (value   & 0xff)) *(alpha/255f));
//            newRed = Math.max(0, Math.min(255, newRed));
//            newGreen = Math.max(0, Math.min(255, newGreen));
//            newBlue = Math.max(0, Math.min(255, newBlue));
            System.out.println(newRed+ " "+ newGreen +" "+ newBlue);
            p[x+ y*pw]=(255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }

            //means transparent pixel


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


 if(image.isAlpha() && !processing){
     imageRequest.add(new ImageRequest(image,zDepth,offX,offY));return;
 }
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

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY){
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pw) return;
        if(offY >= ph) return;
        int newX=0;
        int newY=0;
        int newWidth= image.getTileW();
        int newHeight = image.getTileH();

        //clipping code
        if(offX<0) newX -= offX;
        if(offY<0) newY -= offY;
        if( newWidth + offX > pw){newWidth -= (newWidth + offX -pw);}
        if( newHeight + offY > ph){newHeight -=(newHeight + offY -ph);}
        for(int y=newY;y<newHeight;y++){
            for(int x=newX;x<newWidth;x++){
                setPixel(x+offX,y+offY, image.getP()[(x + tileX + image.getTileW())+(y+ tileY+image.getTileH())*image.getW()]);
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
