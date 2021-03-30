import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.sun.javafx.geom.Rectangle;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import javafx.scene.image.Image;
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

        /** Amazing music **/
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("player", 0, getAppHeight() / 2 - 30);


        getGameScene().getViewport().setBounds(0, 0, getAppWidth(), getAppHeight());

        /** Legendary background **/
        Image image = getAssetLoader().loadImage("map.jpg");
        getGameScene().setBackgroundRepeat(image);

        /** Beautiful audio player **/
        Music music = getAssetLoader().loadMusic("yee-yee.mp3");
        AudioPlayer ap = getAudioPlayer();
        ap.loopMusic(music);


        /** Lets goooo **/
        Sound s = getAssetLoader().loadSound("lets-go.mp3");
        ap.playSound(s);

        run(() -> {
            double x = FXGLMath.random(0, getAppWidth());
            double y = FXGLMath.random(0, getAppHeight());

            spawn("block", x, y);
        }, Duration.seconds(1));
    }

    @Override
    protected void initInput() {

        onKey(KeyCode.W, () -> {
            if(getGameWorld().getSingleton(Types.PLAYER).getY() -3 < 3) { return; }
            getGameWorld().getSingleton(Types.PLAYER).translateY(-3);
        });
        onKey(KeyCode.A, () ->  {
            if(getGameWorld().getSingleton(Types.PLAYER).getX() -3 < 3) { return; }
            getGameWorld().getSingleton(Types.PLAYER).translateX(-3);
        });
        onKey(KeyCode.S, () -> {
            if(getGameWorld().getSingleton(Types.PLAYER).getY() + 35 > getAppHeight()) { return; }
            getGameWorld().getSingleton(Types.PLAYER).translateY(3);
        });
        onKey(KeyCode.D, () -> {
            if(getGameWorld().getSingleton(Types.PLAYER).getX() + 35 > getAppWidth()) { return; }
            getGameWorld().getSingleton(Types.PLAYER).translateX(3);
        });


        onBtnDown(MouseButton.PRIMARY, () -> {

            Sound s = getAssetLoader().loadSound("shoot.mp3");
            AudioPlayer ap = getAudioPlayer();
            ap.playSound(s);

            spawn("bullet", getGameWorld().getSingleton(Types.PLAYER).getCenter());
        });

    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString().concat(" amogus impostors down"));
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initPhysics() {

        onCollision(Types.BULLET, Types.BLOCK, (proj, block) -> {
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

    @Override
    protected void onUpdate(double tpf) {


    }
}