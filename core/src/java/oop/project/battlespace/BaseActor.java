package java.oop.project.battlespace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;

public class BaseActor extends Group{
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;

    private float maxSpeed;
    private float deceleration;

    private Polygon boundaryPolygon;

    boolean collected;

    private static Rectangle worldBounds;


    public BaseActor( float x, float y, Stage s ) {
        super();
        this.setPosition( x, y );
        s.addActor(this);

        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        velocityVec = new Vector2( 0, 0 );
        accelerationVec = new Vector2(0,0);
        acceleration = 0;

        maxSpeed = 1000;
        deceleration = 0;

        collected = false;
    }



    public void wrapAroundWorld() {
        if( getX() + getWidth() < 0 ) {
            setX( worldBounds.width );
        }
        if( getX() > worldBounds.width ) {
            setX( -getWidth() );
        }
        if( getY() + getHeight() < 0 ) {
            setY( worldBounds.height );
        }
        if( getY() > worldBounds.height ) {
            setY( -getHeight() );
        }
    }

    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x,
                cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y,
                cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
        cam.update();
    }

    public void boundToWorld()
    {
        // check left edge
        if (getX() < 0)
            setX(0);
        // check right edge
        if (getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        // check bottom edge
        if (getY() < 0)
            setY(0);
        // check top edge
        if (getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }

    public static void setWorldBounds( float width, float height ) {
        worldBounds = new Rectangle(0, 0, width, height);
    }
    public static void setWorldBounds( BaseActor ba ) {
        setWorldBounds( ba.getWidth(), ba.getHeight() );
    }

    public static  int count ( Stage stage, String className ) {
        return getList(stage, className).size();
    }

    public static ArrayList<BaseActor> getList( Stage stage, String className ) {
        ArrayList<BaseActor> list = new ArrayList<>();

        Class theClass = null;
        try {
            theClass = (Class) Class.forName( className );
        }
        catch ( Exception error ) {
            error.printStackTrace();
        }

        try {
            for( Actor a : stage.getActors() ) {
                if( theClass.isInstance( a ) ) {
                    list.add( (BaseActor)a );
                }
            }
        }
        catch ( Exception error ) {
            //error.printStackTrace();
        }

        return list;
    }

    public Vector2 preventOverlap( BaseActor other ) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performence
        if( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) ) {
            return null;
        }

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons( poly1, poly2, mtv );

        if( !polygonOverlap ) {
            return null;
        }
        this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );

        return mtv.normal;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        clearActions();
        addAction( Actions.fadeOut(1) );
        addAction( Actions.after( Actions.removeActor() ) );
    }

    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

    public void centerAtActor(BaseActor other) {
        centerAtPosition( other.getX() + other.getWidth()/2 , other.getY() + other.getHeight()/2 );
    }

    public void centerAtPosition(float x, float y) {
        setPosition( x - getWidth()/2 , y - getHeight()/2 );
    }

    public boolean overlaps( BaseActor other ) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performence
        if( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;
        return Intersector.overlapConvexPolygons( poly1, poly2 );
    }

    public void setBoundaryPolygon( int numSides ) {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2 * numSides];

        for( int i = 0; i < numSides; i++ ) {
            float angle = i * 6.28f / numSides;
            vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
            vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
        }

        boundaryPolygon = new Polygon( vertices );
    }

    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition( getX(), getY() );
        boundaryPolygon.setOrigin( getOriginX(), getOriginY() );
        boundaryPolygon.setRotation( getRotation() );
        boundaryPolygon.setScale( getScaleX(), getScaleY() );

        return boundaryPolygon;
    }

    public void setBoundaryRectangle() {
        float w = this.getWidth();
        float h = this.getHeight();
        float vertices[] = { 0, 0, w, 0, w, h, 0, h };
        boundaryPolygon = new Polygon( vertices );
    }

    public void applyPhysics(float dt) {

        // apply acceleration
        velocityVec.add( accelerationVec.x * dt, accelerationVec.y * dt );
        float speed = getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0)
            speed -= deceleration * dt;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        // update velocity
        setSpeed(speed);

        // apply velocity
        moveBy( velocityVec.x * dt, velocityVec.y * dt );

        // reset acceleration
        accelerationVec.set(0,0);
    }

}
