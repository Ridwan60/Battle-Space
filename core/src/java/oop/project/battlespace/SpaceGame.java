package java.oop.project.battlespace;

public class SpaceGame extends BaseGame {

    public void create() {
        super.create();
        setActiveScreen( new MenuScreen() );
    }
}
