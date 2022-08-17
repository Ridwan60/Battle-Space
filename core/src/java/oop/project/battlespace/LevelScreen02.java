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
import com.badlogic.gdx.utils.compression.lzma.Base;

import java.util.ArrayList;

public class LevelScreen02 extends BaseScreen{
    private int life;
    private Spaceship spaceship;
    private ArrayList<AlienShip> alienShips;
    private boolean levelOver, win;
    private int cnt_for_enemy_shooting, cnt_for_sending_rocks, cnt_for_sending_alienship;
    private int rockSentTimes = 0, alienShipSentTimes = 0;
    private final int MAX_ROCK_SENDING_TIMES = 10;
    private final int MAX_ALIENSHIP_SENT_TIMES = 10;
    private Heart[] heart = new Heart[5];


    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage );
        space.loadTexture("level01Background.jpg" );
        space.setSize(800, 600);
        BaseActor.setWorldBounds( space );

        spaceship = new Spaceship( 5, 300, mainStage);
        spaceship.centerAtPosition(100, 300);
        spaceship.shieldPower = (life-1) * 20;

        heart = new Heart[5];
        for( int i = 0; i < 5; i++ ) {
            heart[i] = new Heart(i*40, 560, uiStage);
        }

        levelOver = false;
        win = false;
        cnt_for_enemy_shooting = 0;
        cnt_for_sending_rocks = 0;
        cnt_for_sending_alienship = 0;

        alienShips = new ArrayList<>();

        generateAlienShip();

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
        updateAlienShip(dt);
        showLife();

        cnt_for_sending_alienship++; cnt_for_sending_alienship %= 480;
        if( cnt_for_sending_alienship == 0 && alienShipSentTimes < MAX_ALIENSHIP_SENT_TIMES ) generateAlienShip();

        cnt_for_sending_rocks %= 360;
        if(cnt_for_sending_rocks == 0 && rockSentTimes < MAX_ROCK_SENDING_TIMES ) generateRocks();
        cnt_for_sending_rocks++;

        cnt_for_enemy_shooting++; cnt_for_enemy_shooting %= 120;
        if( cnt_for_enemy_shooting == 0 ) {
            for( AlienShip alienShip : alienShips) {
                if( alienShip.isAlive ) {
                    alienShip.shoot( spaceship );
                }
            }
        }

        for (BaseActor laserActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Laser")) {

            for (BaseActor enemyLaserActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.EnemyLaser")) {

                if (enemyLaserActor.overlaps(laserActor)) {
                    laserActor.remove();
                    enemyLaserActor.remove();
                }
            }
        }

        for (BaseActor laserActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Laser")) {

            for( AlienShip alienShip : alienShips) {
                if( alienShip.isAlive && laserActor.overlaps(alienShip) ) {
                    alienShip.life--;

                    if( alienShip.life <= 0 ) {
                        laserActor.remove();
                        Explosion boom = new Explosion(0, 0, mainStage);
                        boom.centerAtActor(alienShip);
                        alienShip.isAlive = false;
                        alienShip.remove();
                    }
                }
            }
        }

        for (BaseActor enemyLaser : BaseActor.getList(mainStage, "java.oop.project.battlespace.EnemyLaser")) {
            if(spaceship.overlaps(enemyLaser)) {
                deductSpaceshipLife(enemyLaser);
            }
        }

        for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
            if(rockActor.getX() + rockActor.getWidth() <= 0 ) {
                rockActor.remove();
                continue;
            }
            if (rockActor.overlaps(spaceship)) {
                deductSpaceshipLife(rockActor);
            }
            for (BaseActor laserActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Laser")) {
                if (laserActor.overlaps(rockActor)) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(rockActor);
                    rockActor.remove();
                    laserActor.remove();
                }
            }
        }

        if ( !levelOver &&
                BaseActor.count(mainStage, "java.oop.project.battlespace.Rock") +
                        BaseActor.count(mainStage, "java.oop.project.battlespace.AlienShip") == 0 &&
        rockSentTimes + alienShipSentTimes == MAX_ROCK_SENDING_TIMES + MAX_ALIENSHIP_SENT_TIMES )
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

    private void deductSpaceshipLife( BaseActor actor) {
        if (spaceship.shieldPower <= 0) {
            Explosion boom = new Explosion(0, 0, mainStage);
            boom.centerAtActor(spaceship);
            spaceship.remove();
            spaceship.setPosition(-1000, -1000);

            BaseActor messageLose = new BaseActor(0, 0, uiStage);
            messageLose.loadTexture("message-lose.png");
            messageLose.centerAtPosition(400, 300);
            messageLose.setOpacity(0);
            messageLose.addAction(Actions.fadeIn(1));
            levelOver = true;
        } else {
            spaceship.shieldPower -= 21;
            life--;
            System.out.println("life: " + life);
            Explosion boom = new Explosion(0, 0, mainStage);
            boom.centerAtActor(actor);
            actor.remove();
        }
    }

    private void generateAlienShip() {
        alienShipSentTimes++;
        alienShips.add(new AlienShip(736, 120, mainStage));
        alienShips.add(new AlienShip(736, 240, mainStage));
        alienShips.add(new AlienShip(736, 360, mainStage));
        alienShips.add(new AlienShip(736, 480, mainStage));
    }

    private void generateRocks() {
        rockSentTimes++;
        Rock[] rock = new Rock[6];
        rock[0] = new Rock(800,512.5f, mainStage, 2);
        rock[1] = new Rock(800,427.5f, mainStage, 2);
        rock[2] = new Rock(800,342.5f, mainStage, 2);
        rock[3] = new Rock(800,257.5f, mainStage, 2);
        rock[4] = new Rock(800,172.5f, mainStage, 2);
        rock[5] = new Rock(800,87.5f, mainStage, 2);
    }

    private void updateAlienShip(float dt ) {
        for( AlienShip alienShip : alienShips ) {
            if( alienShip.isAlive ) {
                alienShip.setY( alienShip.getY() + alienShip.direction * 2.0f );
                if( (alienShip.getY() + alienShip.getHeight() >= Gdx.graphics.getHeight()) || (alienShip.getY() <= 0) ) {
                    alienShip.direction *= -1;
                }
            }
        }
    }

    private void updateSpaceship( float dt ) {
        spaceship.shieldPower = (life) * 20;
        if( Gdx.input.isKeyPressed(Input.Keys.UP) ) {
            spaceship.setY( spaceship.getY() + 2 );
            if( spaceship.getY() + spaceship.getHeight() >= Gdx.graphics.getHeight() ) {
                spaceship.setY( Gdx.graphics.getHeight() - spaceship.getHeight() );
            }
        }
        if( Gdx.input.isKeyPressed(Input.Keys.DOWN) ) {
            spaceship.setY( spaceship.getY() - 2 );
            if( spaceship.getY() <= 0 ) {
                spaceship.setY(0);
            }
        }
    }

    private void backButtonListener() {
        for( BaseActor rockActor : BaseActor.getList(mainStage, "java.oop.project.battlespace.Rock") ) {
            rockActor.remove();
        }
        spaceship.remove();
        SpaceGame.setActiveScreen( new MenuScreen() );
    }

    @Override
    public boolean keyDown( int keyCode ) {
        if( keyCode == Input.Keys.X ) {
            spaceship.warp();
        }
        if( keyCode == Input.Keys.SPACE ) {
            spaceship.shoot();
        }
        if( keyCode == Input.Keys.BACKSPACE ) {
            backButtonListener();
        }
        if( keyCode == Input.Keys.ENTER && levelOver ) {
            if( !win ) SpaceGame.setActiveScreen( new MenuScreen() );
            else SpaceGame.setActiveScreen( new EndScreen() );
        }
        return false;
    }

    LevelScreen02( int life ) {
        super();
        this.life = life;
    }
}
