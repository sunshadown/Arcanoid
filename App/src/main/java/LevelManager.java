import org.dyn4j.geometry.Vector2;
import org.joml.Vector2f;

public class LevelManager {
    public int level;
    public boolean isPlaying;

    public LevelManager(){
        level = 1;
        isPlaying = false;
    }

    public void LoadActualLevel(){
        if(isPlaying) return;

        switch (level){
            case 1:Level_1();break;
            case 2:Level_2();break;
            default:Level_1();
        }
        isPlaying = true;
        Application.getInstance().getManager().getLogic().setNumber_of_lifes(5);
    }


    public void Level_1(){
        //Application.getInstance().getManager().getLogic().getParalaxScroll().Level_1();
        Application.getInstance().getManager().getLogic().getParalaxScroll().LoadRandom();

        Application.getInstance().getManager().getLogic().GenerateSideBlocks(1900,100,0,new Vector2f(50,50),new Vector2f(10,860),-1);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(1900,100,0,new Vector2f(50,50),new Vector2f(10,790),-1);
    }

    public void Level_2(){
        //Application.getInstance().getManager().getLogic().getParalaxScroll().Level_2();
        Application.getInstance().getManager().getLogic().getParalaxScroll().LoadRandom();

        //Application.getInstance().getManager().getLogic().GenerateSideBlocks(400,100,0,new Vector2f(100,20),new Vector2f(300,500),2);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(800,100,0,new Vector2f(100,20),new Vector2f(600,800),1);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(600,100,0,new Vector2f(100,20),new Vector2f(675,850),2);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(400,100,0,new Vector2f(50,50),new Vector2f(770,870),1);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(200,100,0,new Vector2f(100,20),new Vector2f(870,920),3);

        Application.getInstance().getManager().getLogic().GenerateSideBlocks(100,400,0,new Vector2f(100,200),new Vector2f(600,300),1);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(100,400,0,new Vector2f(100,200),new Vector2f(1200,300),1);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(700,400,0,new Vector2f(100,100),new Vector2f(600,500),4);

        Application.getInstance().getManager().getLogic().GenerateSideBlocks(500,400,0,new Vector2f(50,50),new Vector2f(30,500),5);
        Application.getInstance().getManager().getLogic().GenerateSideBlocks(500,400,0,new Vector2f(50,50),new Vector2f(1350,500),5);
    }

    public void LoadRandom(){
        int offset = 10;
        int x = 1920 - 2*offset;
        int y = 800; //
        int square_size = x * y;

        int initial_y_size = 50;
        Vector2 init_pos = new Vector2(10,1080-offset-initial_y_size);

        int regions = 4;
        int x_reg = x / regions;
        int y_reg = y / regions;

        int size_of_blocks[] = new int[regions];

        int size_of_block = (int) ((Math.random() * 1000.0f)%100);
        if(size_of_block < 20)size_of_block = 20;

        for(int i = 0 ; i < x ; i++){
            for(int j = 0 ; j < y ;j++){

            }
        }

    }

}
