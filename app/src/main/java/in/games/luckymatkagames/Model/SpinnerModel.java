package in.games.luckymatkagames.Model;

public class SpinnerModel {


        Integer image;
        String name ;


        public SpinnerModel(String name,Integer image) {
            this.image = image;
            this.name=name;
        }

    public String getName() {
        return name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }
}
