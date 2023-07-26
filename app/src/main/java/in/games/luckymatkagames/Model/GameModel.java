package in.games.luckymatkagames.Model;

public class GameModel {
    String id ;
    String name ;
    int img ;
    String type ;

    public GameModel(String id, String name, int img, String type) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
