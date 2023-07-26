package in.games.luckymatkagames.Model;

public class GameRateModel {

    String id,rate_range,name,rate,type;

    public GameRateModel() {
    }

    public GameRateModel(String id, String rate_range, String name, String rate, String type) {
        this.id = id;
        this.rate_range = rate_range;
        this.name = name;
        this.rate = rate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate_range() {
        return rate_range;
    }

    public void setRate_range(String rate_range) {
        this.rate_range = rate_range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
