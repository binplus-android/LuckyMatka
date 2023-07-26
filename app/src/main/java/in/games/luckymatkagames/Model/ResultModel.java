package in.games.luckymatkagames.Model;

public class ResultModel {

    String result ;
    String user_id;
    String start_time;
    String time;
    String date;
    String status;
    String id,end_time,next_time,close_bid_time;


    public ResultModel(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getResult() {
        return result;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getNext_time() {
        return next_time;
    }

    public void setNext_time(String next_time) {
        this.next_time = next_time;
    }

    public String getClose_bid_time() {
        return close_bid_time;
    }

    public void setClose_bid_time(String close_bid_time) {
        this.close_bid_time = close_bid_time;
    }
}
