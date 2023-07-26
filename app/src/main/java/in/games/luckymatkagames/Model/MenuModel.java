package in.games.luckymatkagames.Model;

public class MenuModel {

    int icon;
    String name;
public MenuModel(){}
    public MenuModel(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}


