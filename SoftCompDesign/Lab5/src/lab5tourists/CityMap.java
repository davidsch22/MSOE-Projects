/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     Dr. Yoder and David Schulz
 * Date:       13 Jan 2019
 */
package lab5tourists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lab5tourists.mobileEntities.Bus;
import lab5tourists.mobileEntities.Car;
import lab5tourists.mobileEntities.MobileEntity;
import lab5tourists.mobileEntities.Person;
import lab5tourists.observers.BusObserver;
import lab5tourists.observers.CarObserver;
import lab5tourists.observers.MuseumObserver;
import lab5tourists.observers.Observer;

/**
 * This application represents a city map which a tourist can explore solving a variety of
 * challenges.
 *
 * The CityMap holds all of the items appearing on the map, populating itself at the beginning of
 * the game.
 *
 * The basic game framework is based on a SO answer about how to make Canvas elements clickable [1],
 * but in the end, we don't use a Canvas.
 *
 * [1] https://stackoverflow.com/questions/27999430/javafx-clickable-line-on-canvas
 */
public class CityMap extends Application {

    public static final int NUM_TOURISTS = 15;

    /** Width of the "challenges" space on the right side of the map in pixels */
    public static final int MIN_CHALLENGES_WIDTH = 250;
    public static final int NUM_BUSSES = 2;
    private static Image backgroundImage = new Image(CityMap.class.getResource("img/map.png")
            .toString());
    private Pane overlay = new Pane();

    private ImageView backgroundView = new ImageView(backgroundImage);
    private Pane challengePane;
    private Collection<Taggable> taggables = new ArrayList<>();

    private List<MobileEntity> mobileEntities = new ArrayList<>();
    private List<Museum> museums = new ArrayList<>();

    /** For ease of access, there is a single character accessible as a sort of Singleton. */
    private static MobileEntity mainCharacter = null;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Each taggable must also call addTaggableToMap() to add itself to the map.
     * @param node node the GUI element to add.
     */
    public void addNodeToMap(Node node) {
        overlay.getChildren().addAll(node);
    }

    /**
     * Add a taggable to the map. Muse also call addNodeToMap for each visual element to be
     * placed on the map.
     * @param taggable the taggable item to be added to the map.
     */
    public void addTaggableToMap(Taggable taggable) {
        taggables.add(taggable);
    }

    /**
     * Adds a visual pane for a Challenge to the map.
     * @param node the visual pane to add.
     */
    public void addChallengeNode(Node node) {
        challengePane.getChildren().addAll(node);
    }

    /**
     * @return a copy of list of the mobile entities found on the map. The entities themselves
     * are NOT copies.
     */
    public List<MobileEntity> getMobileEntities() {
        return new ArrayList<>(mobileEntities);
    }

    /**
     * @return a copy of the list of the museums found on the map.
     * The areas themselves are NOT copies.
     */
    public List<Museum> getMuseums() {
        return new ArrayList<>(museums);
    }

    /**
     * Called by an entity each time it moves.
     *
     * Causes the entity to tag any entities that it now touches.
     *
     * @param entity The entity doing the tagging.
     */
    public void taggedBy(MobileEntity entity) {
        for(Taggable taggable: taggables) {
            if(taggable.isTagged(entity.getLocation())) {
                taggable.taggedBy(entity);
            }
        }
    }

    public double getWidth(){
        return backgroundView.getImage().getWidth();
    }

    public double getHeight() {
        return backgroundView.getImage().getHeight();
    }

    /**
     * Set up the basic map layout.
     *
     * See addEntities() for the fun part (MobileEntities, etc.)
     *
     * @param primaryStage The main window.
     */
    @Override
    public void start(Stage primaryStage) {
        Pane root = new HBox();
        challengePane = new VBox();
        challengePane.setMinWidth(MIN_CHALLENGES_WIDTH);
        Pane mapPane = new Pane();

        backgroundView.relocate(0,0);
        mapPane.setMaxWidth(backgroundImage.getWidth());
        mapPane.setMaxHeight(backgroundImage.getHeight());

        overlay.getChildren().addAll(backgroundView);
        mapPane.getChildren().addAll(overlay);
        root.getChildren().addAll(mapPane, challengePane);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        addEntities();
    }

    /**
     * Get the main character Singleton
     * @return the main character (the Person)
     */
    public static MobileEntity getMainCharacter() {
        if(mainCharacter == null) {
            throw new RuntimeException("getMainCharacter called before game was initialized!");
        }
        return mainCharacter;
    }

    /**
     * Set up all the Mobile and non-mobile Entities on the map.
     *
     * Also sets up Challenges.
     */
    private void addEntities() {
        // Each entity places itself on the map, so we place the goal first
        // so it will show at the bottom.
        Person.Goal goal = new Person.Goal(this);

        for(int i = 0; i < NUM_TOURISTS; i++) {
            Car car = new Car(this);
            car.addToCityMap(); // This method can be moved into the Mobile Entity constructor
                                // for some implementations.
            mobileEntities.add(car);
        }

        for(int i = 0; i < NUM_BUSSES; i++) {
            Bus bus = new Bus(this);
            bus.addToCityMap(); // This method can be moved into the Mobile Entity constructor
                                // for some implementations.
            mobileEntities.add(bus);
        }

        synchronized (CityMap.class) {
            if(mainCharacter == null) {
                mainCharacter = new Person(this, goal);
                mainCharacter.addToCityMap();
            } else {
                System.out.println("Warning: initializing map more than once!");
            }
        }

        museums.add(new Museum(this));

        for (MobileEntity mobileEntity : mobileEntities) {
            if (mobileEntity instanceof Car) {
                mobileEntity.attach(new CarObserver(challengePane, "MSOE"));
            } else if (mobileEntity instanceof Bus) {
                mobileEntity.attach(new BusObserver(challengePane));
            }
        }

        Observer museumObserver = new MuseumObserver(challengePane);
        for (Museum museum : museums) {
            museum.attach(museumObserver);
        }
    }
}