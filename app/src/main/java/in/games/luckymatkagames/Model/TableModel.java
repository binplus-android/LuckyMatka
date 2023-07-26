package in.games.luckymatkagames.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 05,December,2019
 */
public class TableModel {

    String digits,points,type;

    public TableModel() {
    }

    public TableModel(String digits, String points, String type) {
        this.digits = digits;
        this.points = points;
        this.type = type;
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
