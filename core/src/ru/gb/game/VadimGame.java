package ru.gb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import java.util.List;


public class VadimGame extends ApplicationAdapter {
    private SpriteBatch batch;

    private AnimPlayer batmanAnim;

    private Label label;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private List<Coin> coinList;

    private int[] foreGround, backGround;

    private int x;

    @Override
    public void create() {
        map = new TmxMapLoader().load("maps/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        foreGround = new int[1];
        foreGround[0] = map.getLayers().getIndex("Слой тайлов 2");
        backGround = new int[1];
        backGround[0] = map.getLayers().getIndex("Слой тайлов 1");

        batch = new SpriteBatch();
        batmanAnim = new AnimPlayer("runRight.png", 8, 1, 16.0f, Animation.PlayMode.LOOP);
        label = new Label(100);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        RectangleMapObject o = (RectangleMapObject) map.getLayers().get("Слой объектов 1").getObjects().get("camera");
        camera.position.x = o.getRectangle().x;
        camera.position.y = o.getRectangle().y;
        camera.zoom = 0.25f;
        camera.update();

        coinList = new ArrayList<>();
        MapLayer ml = map.getLayers().get("монетки");
        if (ml != null) {
            MapObjects mo = ml.getObjects();
            for (MapObject ob : mo) {
                Rectangle rect = ((RectangleMapObject) ob).getRectangle();
                coinList.add(new Coin(new Vector2(rect.x, rect.y)));
            }
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.position.x--;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.position.x++;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y--;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.position.y++;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(backGround);

        batmanAnim.setTime(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(batmanAnim.getFrame(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        label.draw(batch, "Привет Мир!!!", 0, 0);
        for (Coin cn : coinList) {
            cn.draw(batch, camera);
        }

        batch.end();
        mapRenderer.render(foreGround);

    }

    @Override
    public void dispose() {
        batch.dispose();
        batmanAnim.dispose();
        coinList.get(0).dispose();
    }
}
