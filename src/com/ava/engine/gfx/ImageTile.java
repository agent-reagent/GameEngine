package com.ava.engine.gfx;

public class ImageTile extends Image {
    int tileW; int tileH;
public ImageTile(String path, int tileW, int tileH){
    super(path);
    this.tileH = tileH;
    this.tileW = tileW;

}
}

