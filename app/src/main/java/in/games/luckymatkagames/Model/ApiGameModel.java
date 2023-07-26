package in.games.luckymatkagames.Model;

public class ApiGameModel {
    String game_id,game_name,name,points,is_close,is_deleted;
    int game_logo;
    Boolean is_visible=true;

    public ApiGameModel(String game_id, String game_name, String name, String points, String is_close, String is_deleted, int game_logo) {
        this.game_id = game_id;
        this.game_name = game_name;
        this.name = name;
        this.points = points;
        this.is_close = is_close;
        this.is_deleted = is_deleted;
        this.game_logo = game_logo;
    }

    public ApiGameModel(){}

    public Boolean getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(Boolean is_visible) {
        this.is_visible = is_visible;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIs_close() {
        return is_close;
    }

    public void setIs_close(String is_close) {
        this.is_close = is_close;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getGame_logo() {
        return game_logo;
    }

    public void setGame_logo(int game_logo) {
        this.game_logo = game_logo;
    }
}
