package search;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.Bookmark;
import model.Environment;

/**
 * A custom Cell Card for the @{@link SearchController#resultList}.
 * Shows the environment color as a colored rectangle on the left hand side.
 * And shows the title and a part of a bookmarks description on the righthand side. The title is bold.
 *
 * @author Dominik Strässle
 */
public class CellCard extends HBox {

    /**
     * A Colored Rectangle. Shows the color of the Bookmarks environment
     */
    private Region colorRegion;

    /**
     * A Property that contains the actual color of the environment.
     */
    private SimpleObjectProperty<Color> colorProperty = new SimpleObjectProperty<>();

    private SimpleObjectProperty<Environment> environmentProperty = new SimpleObjectProperty<>();

    /**
     * Creates a CellCard with a given Bookmark.
     *
     * @param bookmark Bookmark with the information to show
     * @param listView listview to bind the widthProperty
     */
    public CellCard(Bookmark bookmark, ListView<Bookmark> listView) {
        super();//call the super constructor
        VBox textVBox = new VBox();//VBox to hold the title above the description
        Label titleLabel = new Label();//title
        Label descLabel = new Label();//description
        this.colorRegion = new Region();//colored Rectangle

        //configure the labels
        titleLabel.textProperty().bind(bookmark.titleProperty());
        titleLabel.getStyleClass().add("bold_text");
        descLabel.textProperty().bind(bookmark.descProperty());

        //configure the colored Rectangle
        colorRegion.setPrefWidth(10);
        colorRegion.setMinWidth(Control.USE_PREF_SIZE);

        //bind the colorProperty and set the color
        this.colorProperty.bind(bookmark.getEnvironment().colorProperty());
        this.colorProperty.addListener((observable, oldValue, newValue) -> changeColor(newValue));
        changeColor(bookmark.getEnvironment().getColor());

        //add labels to vbox
        textVBox.getChildren().addAll(titleLabel, descLabel);
        this.getChildren().addAll(colorRegion, textVBox);

        //bind the list's width to the cellcard's width
        this.prefWidthProperty().bind(listView.widthProperty().subtract(2));
        this.setMaxWidth(Control.USE_PREF_SIZE);

        //set spacing for a space between rectangle and labels
        this.setSpacing(5);

        //when the environment changes, also the color binding should change
        environmentProperty.bind(bookmark.environmentProperty());
        environmentProperty.addListener((observable, oldValue, newValue) -> rebindColorProperty(newValue));
    }

    /**
     * Changes the binding to the color when the environment of a bookmarks changed
     *
     * @param newValue new Environment
     */
    private void rebindColorProperty(Environment newValue) {
        this.colorProperty.unbind();
        this.colorProperty.bind(environmentProperty.get().colorProperty());
    }

    /**
     * Changes the backgroundcolor of the Rectangle.
     * Gets called at instantiation and when the color gets changed
     *
     * @param newColor new color to set
     */
    private void changeColor(Color newColor) {
        colorRegion.setBackground(
                new Background(
                        new BackgroundFill(
                                Paint.valueOf(newColor.toString()),
                                new CornerRadii(3),
                                new Insets(0))));
    }

}