package java.oop.project.battlespace.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import java.oop.project.battlespace.SpaceGame;

public class Launcher {
    public static void main(String[] args) {
        Game myGame = new SpaceGame();
        LwjglApplication launcher = new LwjglApplication( myGame, "Space Rocks", 800, 600 );

    }
}
