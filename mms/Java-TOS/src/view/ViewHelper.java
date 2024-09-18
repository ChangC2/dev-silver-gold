package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.apache.http.util.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewHelper {
    private static ViewHelper instance;

    public static ViewHelper getInstance(){
        if(instance == null){
            instance = new ViewHelper();
        }
        return instance;
    }

    public static Font fontTooltip;
    public static Font fontInformation;

    public ViewHelper() {
        //InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/fonts/productsans_regular.ttf");
        fontTooltip = Font.loadFont(getClass().getClassLoader().getResourceAsStream("resource/style/productsans_regular.ttf"), 16);
        fontInformation = Font.loadFont(getClass().getClassLoader().getResourceAsStream("resource/style/productsans_regular.ttf"), 16);
        //fontInformation = Font.font("Verdana", 18);
    }

    public Font getFontMainToolip() {
        return fontTooltip;
    }

    public Font getFontInformation() {
        return fontInformation;
    }

    public static HBox makeHBox(double space, Pos pos) {
        HBox box = new HBox();
        box.setAlignment(pos);
        box.setSpacing(space);
        return box;
    }

    public static VBox makeVBox(double space, Pos pos) {
        VBox box = new VBox();
        box.setAlignment(pos);
        box.setSpacing(space);
        return box;
    }

    public static BorderPane makeMenuIconPane(int iconSize, int padding, Image icon) {
        ImageView icoImage = new ImageView(icon);
        icoImage.setFitWidth(iconSize);
        icoImage.setFitHeight(iconSize);

        // Button button = new Button("text", icoImage);
        // button.set();

        BorderPane icoBtn = new BorderPane();
        icoBtn.getStyleClass().add("icon-button");
        icoBtn.setCursor(Cursor.HAND);
        BorderPane.setMargin(icoImage, new Insets(padding, padding, padding, padding));
        icoBtn.setCenter(icoImage);
        return icoBtn;
    }

    public static Button makeMenuIconButton(String title, int iconSize, int padding, Image icon, String strToolTip) {
        ImageView icoImage = new ImageView(icon);
        icoImage.setFitWidth(iconSize);
        icoImage.setFitHeight(iconSize);

        Button button = new Button();
        button.setGraphic(icoImage);
        button.setPadding(new Insets(padding, padding, padding, padding));
        button.getStyleClass().add("icon-button");
        button.setCursor(Cursor.HAND);
        if (!TextUtils.isEmpty(title)) {
            button.setText(title);
        }
        BorderPane.setMargin(icoImage, new Insets(padding, padding, padding, padding));

        // Now we disable the tooltip
        if (false && !TextUtils.isEmpty(strToolTip)) {
            Tooltip tooltip = new Tooltip(strToolTip);
            tooltip.setTextAlignment(TextAlignment.CENTER);
            tooltip.setFont(getInstance().getFontMainToolip());
            tooltip.setGraphic(new ImageView(LogoManager.getInstance().getTooltipImage()));
            //button.setTooltip(tooltip);
        }

        return button;
    }

    public static Button makeMenuButton(String title, int padding, String strToolTip) {

        Button button = new Button();
        button.setPadding(new Insets(padding, padding, padding, padding));
        button.getStyleClass().add("icon-button");
        button.setCursor(Cursor.HAND);
        button.setText(title);

        // Now we disable the tooltip
        if (false && !TextUtils.isEmpty(strToolTip)) {
            Tooltip tooltip = new Tooltip(strToolTip);
            tooltip.setTextAlignment(TextAlignment.CENTER);
            tooltip.setFont(getInstance().getFontMainToolip());
            tooltip.setGraphic(new ImageView(LogoManager.getInstance().getTooltipImage()));
            //button.setTooltip(tooltip);
        }

        return button;
    }

    public static Tooltip makeNormalTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setTextAlignment(TextAlignment.CENTER);
        tooltip.setFont(getInstance().getFontMainToolip());
        tooltip.setGraphic(new ImageView(LogoManager.getInstance().getTooltipImage()));
        return tooltip;
    }

    public static Button makeNavMenuButton(int iconSize, int padding, Image icon, String title) {
        ImageView icoImage = new ImageView(icon);
        icoImage.setFitWidth(iconSize);
        icoImage.setFitHeight(iconSize);

        Button button = new Button();
        button.setGraphic(icoImage);
        button.setText(title);
        button.setGraphicTextGap(25);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setPadding(new Insets(padding, padding, padding, padding));
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("icon-button");
        button.setCursor(Cursor.HAND);
        BorderPane.setMargin(icoImage, new Insets(padding, padding, padding, padding));

        return button;
    }

    public static void setButtonIcon(Button button, int iconSize, Image icon) {
        ImageView icoImage = new ImageView(icon);
        icoImage.setFitWidth(iconSize);
        icoImage.setFitHeight(iconSize);

        button.setGraphic(icoImage);
    }

    /**
     * Hack allowing to modify the default behavior of the tooltips.
     * @param openDelay The open delay, knowing that by default it is set to 1000.
     * @param visibleDuration The visible duration, knowing that by default it is set to 5000.
     * @param closeDelay The close delay, knowing that by default it is set to 200.
     * @param hideOnExit Indicates whether the tooltip should be hide on exit,
     * knowing that by default it is set to false.
     */
    public static void updateTooltipBehavior(double openDelay, double visibleDuration,
                                              double closeDelay, boolean hideOnExit) {
        try {
            // Get the non public field "BEHAVIOR"
            Field fieldBehavior = Tooltip.class.getDeclaredField("BEHAVIOR");
            // Make the field accessible to be able to get and set its value
            fieldBehavior.setAccessible(true);
            // Get the value of the static field
            Object objBehavior = fieldBehavior.get(null);
            // Get the constructor of the private static inner class TooltipBehavior
            Constructor<?> constructor = objBehavior.getClass().getDeclaredConstructor(
                    Duration.class, Duration.class, Duration.class, boolean.class
            );
            // Make the constructor accessible to be able to invoke it
            constructor.setAccessible(true);
            // Create a new instance of the private static inner class TooltipBehavior
            Object tooltipBehavior = constructor.newInstance(
                    new Duration(openDelay), new Duration(visibleDuration),
                    new Duration(closeDelay), hideOnExit
            );
            // Set the new instance of TooltipBehavior
            fieldBehavior.set(null, tooltipBehavior);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

// Reference URLs
// https://www.youtube.com/watch?v=4vTc6UZcIH4