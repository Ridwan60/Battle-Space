package com.java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Button playButton, helpButton, exitButton;

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
            rock[0] = new Rock( 100 + random, random, mainStage );
        }

        // adding play button
        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("playButton.png"));
        TextureRegion buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        playButton = new Button(buttonStyle);
        playButton.setPosition(200, 250);
        uiStage.addActor( playButton );

        playButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                playButtonListener();
                return false;
            }
        });

        // adding help button
        buttonStyle = new ButtonStyle();
        buttonTex = new Texture(Gdx.files.internal("helpButton.png"));
        buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        helpButton = new Button(buttonStyle);
        helpButton.setPosition(200, 130);
        uiStage.addActor( helpButton );

        helpButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                helpButtonListener();
                return false;
            }
        });

        // adding exit button
        buttonStyle = new ButtonStyle();
        buttonTex = new Texture(Gdx.files.internal("exitButton.png"));
        buttonRegion = new TextureRegion( buttonTex );
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        exitButton = new Button(buttonStyle);
        exitButton.setPosition(200, 10);
        uiStage.addActor( exitButton );

        exitButton.addListener( new ClickListener(){
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
                exitButtonListener();
                return false;
            }
        });
    }

    @Override
    public void update(float dt) {

        ArrayList<BaseActor> list = BaseActor.getList(mainStage, "com.java.oop.project.battlespace.Rock");

        int sz = list.size();
        for( int i = 0; i < sz; i++ ) {
            for( int j = 0; j < sz; j++ ) {
                if( i == j ) continue;
                ( list.get(i) ).preventOverlap( (list.get(j)) );
            }
        }
    }

    private void playButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "com.java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        SpaceGame.setActiveScreen( new TransitionScreen01() );
    }
    private void helpButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "com.java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        SpaceGame.setActiveScreen( new HelpScreen() );
    }
    private void exitButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "com.java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        System.exit(0);
    }

    @Override
    public boolean keyDown( int keyCode ) {
        if(keyCode == Input.Keys.P) {
            playButtonListener();
        }
        if(keyCode == Input.Keys.H) {
            helpButtonListener();
        }
        if(keyCode == Input.Keys.E) {
            exitButtonListener();
        }
        return false;
    }
}
