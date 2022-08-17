package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class HelpScreen extends BaseScreen {

    private Rock[] rock;

    @Override
    public void initialize() {

        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadTexture("space03.gif");
        space.setSize(800, 600);

        rock = new Rock[10];
        for( int i = 0; i < 10; i++ ) {
            float random = MathUtils.random(600);
            rock[0] = new Rock( random, random, mainStage );
        }

        BaseActor helpScreen = new BaseActor(0, 0, uiStage);
        helpScreen.loadTexture("helpScreen.png");
        helpScreen.setSize(800,600);

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
        ArrayList<BaseActor> list = BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock");

        int sz = list.size();
        for( int i = 0; i < sz; i++ ) {
            for( int j = 0; j < sz; j++ ) {
                if( i == j ) continue;
                ( list.get(i) ).preventOverlap( (list.get(j)) );
            }
        }
    }

    private void backButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        SpaceGame.setActiveScreen( new MenuScreen() );
    }

    public boolean keyDown( int keyCode ) {
        if( keyCode == Input.Keys.BACKSPACE ) {
            backButtonListener();
        }
        return false;
    }
}
