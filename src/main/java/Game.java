import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.sun.javafx.geom.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Game extends GameApplication {
    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        // https://github.com/AlmasB/FXGL/wiki/Basic-Game-Example-%28FXGL-11%29#requirement-1-window
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("BaDaby's collision shooter");
        settings.setVersion("[69.420]");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initGame() {
//        Image image = get("map.jpg");
//        getGameScene().setBackgroundRepeat();
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("player", 0, getAppHeight() / 2 - 30);

        getGameScene().getViewport().setBounds(0, 0, getAppWidth(), getAppHeight());


        run(() -> {
            double x = FXGLMath.random(0, getAppWidth());
            double y = FXGLMath.random(0, getAppHeight());

            System.out.println(x);
            System.out.println(y);

            spawn("block", x, y);
        }, Duration.seconds(1));
    }

    @Override
    protected void initInput() {

        onKey(KeyCode.W, () -> {
            getGameWorld().getSingleton(Types.PLAYER).translateY(-3);
        });
        onKey(KeyCode.A, () ->  getGameWorld().getSingleton(Types.PLAYER).translateX(-3));
        onKey(KeyCode.S, () -> getGameWorld().getSingleton(Types.PLAYER).translateY(3));
        onKey(KeyCode.D, () -> getGameWorld().getSingleton(Types.PLAYER).translateX(3));


        onBtnDown(MouseButton.PRIMARY, () -> {
            spawn("bullet", getGameWorld().getSingleton(Types.PLAYER).getCenter());
        });

    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initPhysics() {

        onCollision(Types.BULLET, Types.BLOCK, (proj, block) -> {
            System.out.println("ok");
            proj.removeFromWorld();
            block.removeFromWorld();
            inc("score", +1);
        });

        onCollision(Types.BLOCK, Types.PLAYER, (block, player) -> {
            block.removeFromWorld();
            player.removeFromWorld();


            showMessage("You Died with a score of " + getWorldProperties().getValue("score") + "!", () -> {
                getGameController().startNewGame();
            });
        });
    }
}