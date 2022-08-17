package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Laser extends BaseActor{

    public Laser(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture( "laser.png" );

        addAction(Actions.delay(2));
        addAction(Actions.after( Actions.fadeOut(0.5f) ));
        addAction(Actions.after( Actions.removeActor() ));

        setSpeed( 400 );
        setMaxSpeed( 400 );
        setDeceleration( 0 );
    }

    public void act( float dt ) {
        super.act(dt);
        applyPhysics( dt );
        wrapAroundWorld();
    }

    @Override
    public void wrapAroundWorld() {
        if( getX() + getWidth() < 0 ) {
            addAction( Actions.removeActor() );
        }
        if( getX() > Gdx.graphics.getWidth() ) {
            addAction( Actions.removeActor() );
        }
        if( getY() + getHeight() < 0 ) {
            addAction( Actions.removeActor() );
        }
        if( getY() > Gdx.graphics.getHeight() ) {
            addAction( Actions.removeActor() );
        }
    }
}
