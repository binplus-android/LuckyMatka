package in.games.luckymatkagames.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 05,December,2019
 */
public class CashinoTableModel {

    String digits,points,type,game_id,actualDigit;

    public CashinoTableModel() {
    }

    public CashinoTableModel(String digits, String points, String type,String game_id,String actualDigit) {
        this.digits = digits;
        this.points = points;
        this.type = type;
        this.game_id = game_id;
        this.actualDigit = actualDigit;
    }

    public String getActualDigit() {
        return actualDigit;
    }

    public void setActualDigit(String actualDigit) {
        this.actualDigit = actualDigit;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "digits='" + digits + '\'' +
                ", points='" + points + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
