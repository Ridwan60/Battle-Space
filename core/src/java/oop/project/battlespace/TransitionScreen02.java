package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TransitionScreen02 extends BaseScreen{

    private int life;
    TransitionScreen02( int life ) {
        super();
        this.life = life;
    }

    @Override
    public void initialize() {
        BaseActor transitionMessage = new BaseActor(0, 0, mainStage );
        transitionMessage.loadTexture("transitionScreen02.png");
        transitionMessage.setSize(800, 600);

        // adding back button
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("backButton.png"));
        TextureRegion buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        Button backButton = new Button(buttonStyle);
        backButton.setPosition(750, 550);
        uiStage.addActor( backButton );

        backButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                backButtonListener();
                return false;
            }
        });
    }

    @Override
    public void update(float dt) {
    }

    private void backButtonListener() {
        SpaceGame.setActiveScreen( new MenuScreen() );
    }

    public boolean keyDown( int keyCode ) {
        if( keyCode == Input.Keys.ENTER ) {
            SpaceGame.setActiveScreen( new LevelScreen02(life) );
        }
        if( keyCode == Input.Keys.BACKSPACE ) {
            backButtonListener();
        }
        return false;
    }
}
