package java.oop.project.battlespace;

import com.badlogic.gdx.Input;

public class EndScreen extends BaseScreen{
    @Override
    public void initialize() {
        BaseActor transitionMessage = new BaseActor(0, 0, mainStage );
        transitionMessage.loadTexture("endScreen.png");
        transitionMessage.setSize(800, 600);
    }

    @Override
    public void update(float dt) {

    }

    public boolean keyDown( int keyCode ) {
        if( keyCode == Input.Keys.P ) {
            SpaceGame.setActiveScreen( new MenuScreen() );
        }
        return false;
    }
}
