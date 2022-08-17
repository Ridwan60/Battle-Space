package java.oop.project.battlespace;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class AlienShip extends BaseActor{

    boolean isAlive;
    protected int life;
    protected float direction = +1;

    public AlienShip(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("enemyShip.png");
        isAlive = true;
        life = 30;
    }

    @Override
    public void act( float dt ) {
        super.act(dt);
        applyPhysics(dt);
    }

    public void shoot(Spaceship spaceship) {
        if( getStage() == null ) {
            return;
        }

        float x1 = this.getX() + this.getWidth()/2;
        float y1 = this.getY() + this.getHeight()/2;

        float x2 = spaceship.getX() + spaceship.getWidth()/2;
        float y2 = spaceship.getY() + spaceship.getHeight()/2;

        float delX = x2-x1;
        float delY = y2-y1;

        float theta = (float) Math.atan( Math.abs(delY) / Math.abs(delX) );
        theta = theta * (float)180.0 / (float)3.1416;

        if( delX < 0 && delY > 0 ) theta = 180 - theta;
        else if( delX < 0 && delY < 0 ) theta = 180 + theta;
        else if( delX > 0 && delY < 0 ) theta = 360 - theta;

        EnemyLaser laser = new EnemyLaser(0, 0, this.getStage());
        laser.centerAtActor( this );
        laser.setRotation( theta );
        laser.setMotionAngle( theta );
    }
}
