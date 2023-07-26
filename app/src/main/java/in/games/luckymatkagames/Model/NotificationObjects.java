package in.games.luckymatkagames.Model;

public class NotificationObjects {

    String notification_id,notification,time;

    public NotificationObjects() {
    }

    public NotificationObjects(String notification_id, String notification, String time) {
        this.notification_id = notification_id;
        this.notification = notification;
        this.time = time;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
