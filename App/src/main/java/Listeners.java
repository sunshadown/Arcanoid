import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.joml.Vector2f;

import java.util.List;

public class Listeners {

    public static class Panel_Ball_Listerner extends CollisionAdapter{

        private Body ball;
        private Body panel;

        public Panel_Ball_Listerner(Body panel, Body ball) {
            this.ball = ball;
            this.panel = panel;
        }


        @Override
        public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
            if((body1 == ball && body2 == panel) || (body1 == panel && body2 == ball)){
                Application.getInstance().getManager().getAudio_Bounce().play();
                //System.out.println("Collision ball-panel");
            }
            return super.collision(body1, fixture1, body2, fixture2, manifold);
        }
    }

    public static class Destroyer_Listener extends CollisionAdapter{
        private List<Panel> blocks;


        public Destroyer_Listener(List<Panel>blocks){
            this.blocks = blocks;
        }


        @Override
        public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
            for(int i = 0 ; i < blocks.size() ; i++){
                if(blocks.get(i).getBody() == body1){
                    blocks.remove(blocks.get(i));
                    Application.getInstance().getManager().getLogic().getWorld().removeBody(body1);
                    Application.getInstance().getManager().getAudio_Explosion().play();
                }
                if(blocks.get(i).getBody() == body2){
                    blocks.remove(blocks.get(i));
                    Application.getInstance().getManager().getLogic().getWorld().removeBody(body2);
                    Application.getInstance().getManager().getAudio_Explosion().play();
                }
            }
            return super.collision(body1, fixture1, body2, fixture2, penetration);

        }
    }

    public static class Ball_BWall extends CollisionAdapter{
        private Body ball;
        private Body b_wall;

        Ball_BWall(Body ball, Body b_wall){
            this.ball = ball;
            this.b_wall = b_wall;
        }

        @Override
        public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
            if((body1 == ball && body2 == b_wall) || (body2 == ball && body1 == b_wall) ){
                Application.getInstance().getManager().getLogic().LifeDecrese();
                Transform transform = new Transform();
                transform.setTranslation(Application.getInstance().getManager().getLogic().getM_Panel().getBody().getTransform().getTranslation().add(new Vector2(0,50)));
                Application.getInstance().getManager().getLogic().getTest().getBody().setTransform(transform);
                Application.getInstance().getManager().getLogic().getTest().getBody().setLinearVelocity(0,-1000);
                return false;
            }
            return super.collision(body1, fixture1, body2, fixture2, penetration);
        }
    }
}
