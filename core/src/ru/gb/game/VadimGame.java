package ru.gb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VadimGame extends ApplicationAdapter {
    private PhysX physX;
    private SpriteBatch batch;
    private Label label;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private List<Coin> coinList;
    private Texture fon;
    private MyCharacter chip;
    private int[] foreGround, backGround;
    private int score;
    private Music music;
    private int live = 3;
    private boolean contact = false;

    @Override
    public void create() {

        chip = new MyCharacter();
        fon = new Texture("fon.png");
        TiledMap map = new TmxMapLoader().load("maps/map2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        physX = new PhysX();
        if (map.getLayers().get("land") != null) {
            MapObjects mObjects = map.getLayers().get("land").getObjects();
            physX.addObjects(mObjects);
        }
        if (map.getLayers().get("Слой объектов 2") != null) {
            MapObjects mObjects = map.getLayers().get("Слой объектов 2").getObjects();
            for (MapObject mo : mObjects) {
                if (mo.getName().equals("hero") || mo.getName().equals("ball")) {
                    physX.addObject(mo);
                }
            }
        }

        foreGround = new int[1];
        foreGround[0] = map.getLayers().getIndex("Слой тайлов 2");
        backGround = new int[1];
        backGround[0] = map.getLayers().getIndex("Слой тайлов 1");

        batch = new SpriteBatch();
        label = new Label(50);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.x = physX.getHero().getPosition().x;
        camera.position.y = physX.getHero().getPosition().y;
        camera.zoom = 0.5f;
        camera.update();

        coinList = new ArrayList<>();
        MapLayer ml = map.getLayers().get("coins");
        if (ml != null) {
            MapObjects mo = ml.getObjects();
            for (MapObject ob : mo) {
                Rectangle rect = ((RectangleMapObject) ob).getRectangle();
                coinList.add(new Coin(new Vector2(rect.x, rect.y)));
            }
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("Soundtrack.mp3"));
        music.setLooping(true);
        music.setVolume(0.025f);
        music.play();

    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        chip.setWalk(false);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && physX.cl.isOnGround()) {
            physX.setHeroForce(new Vector2(-3000, 0));
            chip.setDir(true);
            chip.setWalk(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && physX.cl.isOnGround()) {
            physX.setHeroForce(new Vector2(3000, 0));
            chip.setDir(false);
            chip.setWalk(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && physX.cl.isOnGround()) {
            physX.setHeroForce(new Vector2(0, 1300));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            physX.setHeroForce(new Vector2(0, -1300));
        }

        camera.position.x = physX.getHero().getPosition().x - chip.getFrame().getRegionWidth() / 4f;
        camera.position.y = physX.getHero().getPosition().y - chip.getFrame().getRegionHeight() / 4f;

        camera.update();

        batch.begin();
        batch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render(backGround);

        batch.begin();
        batch.draw(chip.getFrame(), Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        label.draw(batch, "Собрано монет: " + score, 0, 0);

        Iterator<Coin> iter = coinList.iterator();
        while (iter.hasNext()) {
            Coin cn = iter.next();
            int state = cn.draw(batch, camera);
            if (cn.isOverLaps(chip.getRect(), camera)) {
                if (state == 0) cn.setState();
                if (state == 2) {
                    iter.remove();
                    score++;
                }
            }
        }
        label.draw(batch, "Жизней: " + live, 0, Gdx.graphics.getHeight() - 80);
        batch.end();

        if (physX.cl.isOnBallContact() && !contact) {
            contact = true;
        }
        if (!physX.cl.isOnBallContact() && contact) {
            contact = false;
            if (live <= 0) {
                Gdx.app.exit();
            } else {
                live--;
            }
        }

        mapRenderer.render(foreGround);
        physX.step();
        physX.debugDraw(camera);

    }

    @Override
    public void dispose() {
        batch.dispose();
        coinList.get(0).dispose();
        physX.dispose();
        music.stop();
        music.dispose();
    }
}
