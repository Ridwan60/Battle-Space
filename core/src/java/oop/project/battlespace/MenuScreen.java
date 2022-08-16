package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import java.util.ArrayList;

public class MenuScreen extends BaseScreen {

    private Rock[] rock;

    @Override
    public void initialize() {

        BaseActor space = new BaseActor(0, 0, mainStage );
        space.loadTexture("space.png" );
        space.setSize(800, 600);
        BaseActor.setWorldBounds( space );

        BaseActor gameLogo = new BaseActor(200, 450, uiStage);
        gameLogo.loadTexture("battleSpace.png");
        gameLogo.setSize(400, 100);

        rock = new Rock[10];
        for( int i = 0; i < 10; i++ ) {
            float random = MathUtils.random(600);
            rock[0] = new Rock( random, random, mainStage );
        }

        // adding play button
        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("playButton.png"));
        TextureRegion buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        Button playButton = new Button(buttonStyle);
        playButton.setPosition(200, 250);
        uiStage.addActor( playButton );

        playButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
                    rockActor.remove();
                }
                SpaceGame.setActiveScreen( new LevelScreen() );
                return false;
            }
        });

        // adding help button
        buttonStyle = new ButtonStyle();
        buttonTex = new Texture(Gdx.files.internal("helpButton.png"));
        buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        Button helpButton = new Button(buttonStyle);
        helpButton.setPosition(200, 130);
        uiStage.addActor( helpButton );

        helpButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
                    rockActor.remove();
                }
//                SpaceGame.setActiveScreen( new HelpScreen() );
                return false;
            }
        });

        // adding exit button
        buttonStyle = new ButtonStyle();
        buttonTex = new Texture(Gdx.files.internal("exitButton.png"));
        buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        Button exitButton = new Button(buttonStyle);
        exitButton.setPosition(200, 10);
        uiStage.addActor( exitButton );

        exitButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
                    rockActor.remove();
                }
                System.exit(0);
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
}
