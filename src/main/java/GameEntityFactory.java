import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameEntityFactory implements EntityFactory {

    @Spawns("player")
    public Entity player(SpawnData data) {
        var box = new Rectangle(32, 32, Color.rgb(1, 1, 1, 0));

        return entityBuilder()
                .type(Types.PLAYER)
                .view("player.png")
                .viewWithBBox(box)
                .at(data.getX(), data.getY())
                .collidable()
                .build();
    }



    @Spawns("block")
    public Entity block(SpawnData data) {
        var top = new Rectangle(20, 20, Color.RED);
        top.setStroke(Color.RED);

        return entityBuilder()
                .type(Types.BLOCK)
                .viewWithBBox(top)
                .at(data.getX(), data.getY())
                .collidable()
                .build();
    }


    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {

        Entity player = getGameWorld().getSingleton(Types.PLAYER);
        Point2D direction = getInput().getMousePositionWorld()
                .subtract(player.getCenter());

        return entityBuilder()
                .type(Types.BULLET)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(10, 2, Color.BLACK))
                .with(new ProjectileComponent(direction, 1000))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

}
