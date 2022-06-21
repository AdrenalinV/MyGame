package ru.gb.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Iterator;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    private Body hero;
    public Contact cl;


    public PhysX() {
        world = new World(new Vector2(0, -9.81f), true);
        cl = new Contact();
        world.setContactListener(cl);
        debugRenderer = new Box2DDebugRenderer();
    }

    public void setHeroForce(Vector2 force) {
        hero.applyForceToCenter(force, true);
    }

    public Body getHero() {
        return hero;
    }

    public void step() {
        world.step(1 / 60.0f, 2, 2);
    }

    public void debugDraw(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void addObject(MapObject mObject) {
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape poly_h = new PolygonShape();
        CircleShape circle = new CircleShape();

        switch ((String) mObject.getProperties().get("type")) {
            case "StaticBody":
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case "DynamicBody":
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case "KinematicBody":
                def.type = BodyDef.BodyType.KinematicBody;
                break;
        }
        String name = (String) mObject.getProperties().get("name");
        switch (name) {
            case "wall":
                RectangleMapObject rect = (RectangleMapObject) mObject;
                def.position.set(new Vector2(rect.getRectangle().x + rect.getRectangle().width / 2,
                        rect.getRectangle().y + rect.getRectangle().height / 2));
                poly_h.setAsBox(rect.getRectangle().width / 2, rect.getRectangle().height / 2);
                fdef.shape = poly_h;
                break;
            case "circle":
                EllipseMapObject ellipseMapObject = (EllipseMapObject) mObject;
                def.position.set(new Vector2(ellipseMapObject.getEllipse().x + ellipseMapObject.getEllipse().width / 2,
                        ellipseMapObject.getEllipse().y + ellipseMapObject.getEllipse().height / 2));
                circle.setRadius(ellipseMapObject.getEllipse().width / 2);
                fdef.shape = circle;
                break;
            default:
        }

        def.gravityScale = (float) mObject.getProperties().get("gravityScale");

        fdef.restitution = (float) mObject.getProperties().get("restitution");
        fdef.density = (float) mObject.getProperties().get("density");
        fdef.friction = (float) mObject.getProperties().get("friction");

        if (mObject.getName().equals("hero")) {
            hero = world.createBody(def);
            hero.createFixture(fdef).setUserData("hero");
            poly_h.setAsBox(3, 5, new Vector2(0, -7), 0);
            fdef.shape = poly_h;
            fdef.isSensor = true;
            hero.createFixture(fdef).setUserData("sensor");
        } else if (mObject.getName().equals("ball")) {
            def.gravityScale = MathUtils.random(1, 5);
            Body ball = world.createBody(def);
            ball.createFixture(fdef).setUserData(name);
            circle.setRadius(16);
            fdef.shape = circle;
            fdef.isSensor = true;
            ball.createFixture(fdef).setUserData("ballContact");
        } else {
            world.createBody(def).createFixture(fdef).setUserData(name);
        }

        poly_h.dispose();
        circle.dispose();
    }

    public void addObjects(MapObjects mObjects) {
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape poly_h = new PolygonShape();
        CircleShape circle = new CircleShape();

        Iterator<MapObject> objectIterator = mObjects.iterator();
        while (objectIterator.hasNext()) {
            MapObject mObject = objectIterator.next();

            switch ((String) mObject.getProperties().get("type")) {
                case "StaticBody":
                    def.type = BodyDef.BodyType.StaticBody;
                    break;
                case "DynamicBody":
                    def.type = BodyDef.BodyType.DynamicBody;
                    break;
                case "KinematicBody":
                    def.type = BodyDef.BodyType.KinematicBody;
                    break;
            }
            String name = (String) mObject.getProperties().get("name");
            switch (name) {
                case "wall":
                    RectangleMapObject rect = (RectangleMapObject) mObject;
                    def.position.set(new Vector2(rect.getRectangle().x + rect.getRectangle().width / 2,
                            rect.getRectangle().y + rect.getRectangle().height / 2));
                    poly_h.setAsBox(rect.getRectangle().width / 2, rect.getRectangle().height / 2);
                    fdef.shape = poly_h;
                    break;
                case "circle":
                    EllipseMapObject ellipseMapObject = (EllipseMapObject) mObject;
                    def.position.set(new Vector2(ellipseMapObject.getEllipse().x + ellipseMapObject.getEllipse().width / 2,
                            ellipseMapObject.getEllipse().y + ellipseMapObject.getEllipse().height / 2));
                    circle.setRadius(ellipseMapObject.getEllipse().width / 2);
                    fdef.shape = circle;
                    break;
                default:
            }
            def.gravityScale = (float) mObject.getProperties().get("gravityScale");

            fdef.restitution = (float) mObject.getProperties().get("restitution");
            fdef.density = (float) mObject.getProperties().get("density");
            fdef.friction = (float) mObject.getProperties().get("friction");

            world.createBody(def).createFixture(fdef).setUserData(name);

        }
        poly_h.dispose();
        circle.dispose();
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }

    public class Contact implements ContactListener {
        private int count;
        private int ballCount;

        public boolean isOnGround() {
            return count > 0;
        }

        public boolean isOnBallContact() {
            return ballCount > 0;
        }

        @Override
        public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            if (fa.getUserData() != null) {
                String s = (String) fa.getUserData();
                if (s.contains("sensor")) {
                    count++;
                }
            }
            if (fb.getUserData() != null) {
                String s = (String) fb.getUserData();
                if (s.contains("sensor")) {
                    count++;
                }
            }
            if (fa.getUserData() != null && fb.getUserData() != null) {
                String sA = (String) fa.getUserData();
                String sB = (String) fb.getUserData();
                if ((sA.contains("ballContact") && sB.contains("hero")) ||
                        (sB.contains("ballContact") && sA.contains("hero"))) {
                    ballCount++;
                }
            }
        }

        @Override
        public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            if (fa.getUserData() != null) {
                String s = (String) fa.getUserData();
                if (s.contains("sensor")) {
                    count--;
                }
            }
            if (fb.getUserData() != null) {
                String s = (String) fb.getUserData();
                if (s.contains("sensor")) {
                    count--;
                }
            }
            if (fa.getUserData() != null && fb.getUserData() != null) {
                String sA = (String) fa.getUserData();
                String sB = (String) fb.getUserData();
                if ((sA.contains("ballContact") && sB.contains("hero")) ||
                        (sB.contains("ballContact") && sA.contains("hero"))) {
                    ballCount--;
                }
            }
        }

        @Override
        public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, ContactImpulse impulse) {

        }
    }

}
