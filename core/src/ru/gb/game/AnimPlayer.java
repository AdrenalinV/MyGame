package ru.gb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimPlayer {
    Texture texture;
    Animation<TextureRegion> animation;

    private float time;
    private boolean loop;

    public AnimPlayer(String name, int width, int height, float fps, Animation.PlayMode mode) {
        texture = new Texture(name);
        TextureRegion region = new TextureRegion(texture);
        TextureRegion[][] regionsRaw = region.split(region.getRegionWidth() / width, region.getRegionHeight() / height);
        TextureRegion[] regions = new TextureRegion[width * height];
        int cnt = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                regions[cnt++] = regionsRaw[i][j];
            }
        }
        animation = new Animation<TextureRegion>(1.0f / fps, regions);
        animation.setPlayMode(mode);
    }

    public void setTime(float time) {
        this.time += time;
    }

    public void reSetTime() {
        time = 0;
    }

    public TextureRegion getFrame() {
        return animation.getKeyFrame(time);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(time);
    }

    public void setPlayMode(Animation.PlayMode mode) {
        animation.setPlayMode(mode);
    }

    public void dispose() {
        texture.dispose();
    }
}
