package in.games.luckymatkagames.Model;

public class Notice_Model {

    String title;
    String description;
    public Notice_Model(String title, String description) {

        this.title = title;
        this.description = description;
    }

    public Notice_Model() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
