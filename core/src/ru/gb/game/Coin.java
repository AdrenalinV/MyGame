package ru.gb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private final AnimPlayer animPlayer;
    private final Vector2 position;
    private final Rectangle rectangle;

    public Coin(Vector2 position) {
        animPlayer = new AnimPlayer("Full Coinss.png", 8, 1, 10f, Animation.PlayMode.LOOP);
        this.position = position;
        rectangle = new Rectangle(position.x,
                position.y,
                animPlayer.getFrame().getRegionWidth(),
                animPlayer.getFrame().getRegionHeight()
        );
    }

    public void draw(SpriteBatch batch, OrthographicCamera camera) {
        animPlayer.setTime(Gdx.graphics.getDeltaTime());
        float cx = (position.x - camera.position.x) / camera.zoom + Gdx.graphics.getWidth() / 2f;
        float cy = (position.y - camera.position.y) / camera.zoom + Gdx.graphics.getHeight() / 2f;
        batch.draw(animPlayer.getFrame(), cx, cy);
    }


    public boolean isOverLaps(Rectangle r, OrthographicCamera camera) {
        float cx = (rectangle.x - camera.position.x) / camera.zoom + Gdx.graphics.getWidth() / 2f;
        float cy = (rectangle.y - camera.position.y) / camera.zoom + Gdx.graphics.getHeight() / 2f;
        Rectangle rect = new Rectangle(cx, cy, rectangle.getWidth(), rectangle.getHeight());
        return rect.overlaps(r);
    }


    public void dispose() {
        animPlayer.dispose();
    }
}
