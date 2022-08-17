package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelScreen01 extends BaseScreen{

    private Spaceship spaceship;
    private boolean levelOver;
    private int life = 5;
    private boolean win = false;
    private Heart[] heart = new Heart[5];

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage );
        space.loadTexture("level01Background.jpg" );
        space.setSize(800, 600);
        BaseActor.setWorldBounds( space );

        spaceship = new Spaceship(400, 300, mainStage );
        heart = new Heart[5];
        for( int i = 0; i < 5; i++ ) {
            heart[i] = new Heart(i*40, 560, uiStage);
        }

        new Rock(600,500, mainStage);
        new Rock(600,300, mainStage);
        new Rock(600,100, mainStage);
        new Rock(400,100, mainStage);
        new Rock(200,100, mainStage);
        new Rock(200,300, mainStage);
        new Rock(200,500, mainStage);
        new Rock(400,500, mainStage);

        levelOver = false;

        // adding button
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

        updateSpaceship(dt);
        showLife();

        for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
            if( rockActor.overlaps( spaceship ) ) {
                if( spaceship.shieldPower <= 0 ) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(spaceship);
                    spaceship.remove();
                    spaceship.setPosition(-1000, -1000 );

                    BaseActor messageLose = new BaseActor(0, 0, mainStage);
                    messageLose.loadTexture("message-lose.png");
                    messageLose.centerAtPosition(400, 300);
                    messageLose.setOpacity(0);
                    messageLose.addAction(Actions.fadeIn(1));
                    levelOver = true;
                }
                else {
                    spaceship.shieldPower -= 21;
                    life--;
                    System.out.println("life: " + life);
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor( rockActor );
                    rockActor.remove();
                }
            }
            for( BaseActor leisureActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Laser")) {
                if( leisureActor.overlaps(rockActor) ) {
                    Explosion boom = new Explosion(0,0, mainStage);
                    boom.centerAtActor(rockActor);
                    rockActor.remove();
                    leisureActor.remove();
                }
            }
        }

        if ( !levelOver && BaseActor.count(mainStage, "java.oop.project.battlespace.Rock") == 0 )
        {
            BaseActor messageWin = new BaseActor(0,0, uiStage);
            messageWin.loadTexture("message-win.png");
            messageWin.centerAtPosition(400,300);
            messageWin.setOpacity(0);
            messageWin.addAction( Actions.fadeIn(1) );
            levelOver = true;
            win = true;
        }
    }

    private void showLife() {
        for( BaseActor heart : BaseActor.getList(uiStage, "java.oop.project.battlespace.Heart")) {
            heart.remove();
        }
        heart = new Heart[life];
        for( int i = 0; i < life; i++ ) {
            heart[i] = new Heart(i*40, 560, uiStage);
        }
    }

    private void updateSpaceship( float dt ) {

        float degreesPerSecond = 120; // rotation speed
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spaceship.rotateBy( degreesPerSecond * dt );
        }
        if( Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) {
            spaceship.rotateBy(( -degreesPerSecond * dt ) );
        }
        if( Gdx.input.isKeyPressed(Input.Keys.UP) ) {
            spaceship.accelerateAtAngle( spaceship.getRotation() );
            spaceship.applyPhysics( dt );
            spaceship.thrusters.setVisible(true);
        }
        else {
            spaceship.thrusters.setVisible(false);
        }

        spaceship.shield.setOpacity(spaceship.shieldPower / 100f);
        if( spaceship.shieldPower <= 0 ) {
            spaceship.shield.setVisible( false );
        }
    }

    private void backButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        spaceship.remove();
        SpaceGame.setActiveScreen( new MenuScreen() );
    }

    // discrete input processing
    @Override
    public boolean keyDown( int keyCode ) {

        if( keyCode == Input.Keys.SPACE ) spaceship.shoot();
        if( keyCode == Input.Keys.BACKSPACE ) backButtonListener();
        if( keyCode == Input.Keys.ENTER && levelOver ){
            if(win) SpaceGame.setActiveScreen( new TransitionScreen02(life) );
            else SpaceGame.setActiveScreen(new MenuScreen());
        }
        return false;
    }
}
