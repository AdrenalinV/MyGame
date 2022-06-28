package ru.gb.game;

import com.badlogic.gdx.Game;
import ru.gb.game.screens.InScreen;

public class Main extends Game {
    @Override
    public void create() {
        this.setScreen(new InScreen(this));
    }
}
