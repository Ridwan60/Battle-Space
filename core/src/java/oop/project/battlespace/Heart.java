package java.oop.project.battlespace;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Heart extends BaseActor{
    public Heart(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("heart.png");
    }
}
