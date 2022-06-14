package ru.gb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyCharacter {
    private AnimPlayer idle, jump, walkRight;
    private boolean isJump, isWalk, dir;
    private Vector2 pos;
    private Rectangle rect;

    public MyCharacter() {
        idle = new AnimPlayer("hero/idle.png", 1, 1, 16f, Animation.PlayMode.LOOP);
        jump = new AnimPlayer("hero/jump.png", 1, 1, 16f, Animation.PlayMode.LOOP);
        walkRight = new AnimPlayer("hero/runRight.png", 4, 1, 16f, Animation.PlayMode.LOOP);
        pos = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        rect = new Rectangle(Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2,
                walkRight.getFrame().getRegionWidth(),
                walkRight.getFrame().getRegionHeight()
        );
    }

    public void setWalk(boolean walk) {
        isWalk = walk;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public TextureRegion getFrame() {
        TextureRegion tmpTexture = null;
        if (!isJump && !isWalk && !dir) {
            idle.setTime(Gdx.graphics.getDeltaTime());
            if (idle.getFrame().isFlipX()) idle.getFrame().flip(true, false);
            tmpTexture = idle.getFrame();
        } else if (!isJump && !isWalk && dir) {
            idle.setTime(Gdx.graphics.getDeltaTime());
            if (!idle.getFrame().isFlipX()) idle.getFrame().flip(true, false);
            tmpTexture = idle.getFrame();
        } else if (!isJump && isWalk && !dir) {
            walkRight.setTime(Gdx.graphics.getDeltaTime());
            if (walkRight.getFrame().isFlipX()) walkRight.getFrame().flip(true, false);
            tmpTexture = walkRight.getFrame();
        } else if (!isJump && isWalk && dir) {
            walkRight.setTime(Gdx.graphics.getDeltaTime());
            if (!walkRight.getFrame().isFlipX()) walkRight.getFrame().flip(true, false);
            tmpTexture = walkRight.getFrame();
        } else if (isJump && !isWalk && !dir) {
            jump.setTime(Gdx.graphics.getDeltaTime());
            if (jump.getFrame().isFlipX()) jump.getFrame().flip(true, false);
            tmpTexture = jump.getFrame();
        } else if (isJump && !isWalk && dir) {
            jump.setTime(Gdx.graphics.getDeltaTime());
            if (!jump.getFrame().isFlipX()) jump.getFrame().flip(true, false);
            tmpTexture = jump.getFrame();
        }
        return tmpTexture;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Rectangle getRect() {
        return rect;
    }
}
