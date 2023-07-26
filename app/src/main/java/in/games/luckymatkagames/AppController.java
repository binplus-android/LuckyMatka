package in.games.luckymatkagames;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

public class AppController extends Application {
    private static final String ONESIGNAL_APP_ID = "b689e59b-38b4-4520-a780-18a28d4928fe";
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable verbose OneSignal logging to debug issues if needed.
        mInstance = this;

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);


//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                .setNotificationOpenedHandler(new NotificationClickHandler(mInstance))
////                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
//        OneSignal.setSubscription(true);
//        cresteNotificationChannel();
//        EmojiManager.install(new GoogleEmojiProvider());

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

//    public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void notificationReceived(OSNotification notification) {
//            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
//            JSONObject data = notification.payload.additionalData;
//            String notificationID = notification.payload.notificationID;
//            String title =String.valueOf(HtmlCompat.fromHtml("<font color=\"" + color + "\">" + notification.payload.title + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
//            String body = notification.payload.body;
//            String smallIcon = notification.payload.smallIcon;
//            String largeIcon = notification.payload.largeIcon;
//            String bigPicture = notification.payload.bigPicture;
//            String smallIconAccentColor = notification.payload.smallIconAccentColor;
//            String sound = notification.payload.sound;
//            String ledColor = notification.payload.ledColor;
//            int lockScreenVisibility = notification.payload.lockScreenVisibility;
//            String groupKey = notification.payload.groupKey;
//            String groupMessage = notification.payload.groupMessage;
//            String fromProjectNumber = notification.payload.fromProjectNumber;
//            String rawPayload = notification.payload.rawPayload;
//
//            String customKey;
//
//            Log.e("ASDfgrtadsefr234",title+"++"+body+"++"+rawPayload);
//
//
//            Log.i("OneSignalExample43545", "NotificationID received: " + notificationID);
//
//            if (data != null) {
//                customKey = data.optString("customkey", null);
//                if (customKey != null)
//                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
//            }
//
//            showNotification(title,body,rawPayload);
//        }
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void showNotification(String title1, String body, String rawPayload) {
//        String title = title1;
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
//        LocalDateTime now = LocalDateTime.now();
//        String time = dtf.format(now);
//        String alert = body;
//        String type="";
//        try {
//            JSONObject object1= new JSONObject(rawPayload);
//            String custom = object1.getString("custom");
//            JSONObject object = new JSONObject(custom);
//            JSONObject obj = object.getJSONObject("a");
//            type = obj.getString("type");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            type="1";
//        }
//        try {
//            @SuppressLint("RemoteViewLayout") RemoteViews notificationLayout = null;
//            if (type.equals("2")) {
//                notificationLayout = new RemoteViews(getPackageName(), R.layout.row_push_notification);
//                notificationLayout.setTextViewText(R.id.tv_name, Html.fromHtml(title));
//                notificationLayout.setTextViewText(R.id.tv_result, Html.fromHtml(alert));
//                notificationLayout.setTextViewText(R.id.tv_time, time);
//                notificationLayout.setImageViewResource(R.id.img_time, R.drawable.card);
//            }
//            else {
//                Log.e("erftg", type);
//                notificationLayout = new RemoteViews(getPackageName(), R.layout.push_notification_two);
//                notificationLayout.setTextViewText(R.id.tv_name, Html.fromHtml(title));
//                notificationLayout.setTextViewText(R.id.tv_result, Html.fromHtml(alert));
//
//
//            }
//            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
////you can use your launcher Activity insted of SplashActivity, But if the Activity you used here is not launcher Activty than its not work when App is in background.
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////Add Any key-value to pass extras to intent
////            intent.putExtra("pushnotification", "yes");
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            Notification customNotification;
//            if (type.equals("2")) {
//                customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.card)
//                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                        .setCustomContentView(notificationLayout)
//                        .setCustomBigContentView(notificationLayout)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
//                        .setGroup("GROUP_KEY_EMAILS")
//                        .setGroupSummary(true)
//                        .build();
//            }else {
//                customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.card)
//                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                        .setCustomContentView(notificationLayout)
//                        .setCustomBigContentView(notificationLayout)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
//                        .build();
//            }
//
//
//
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//
//            int NOTI_ID= Random(System.currentTimeMillis()).nextInt(1000);
//            managerCompat.notify(NOTI_ID, customNotification);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                NotificationManager manager = getSystemService(NotificationManager.class);
//                manager.getNotificationChannels();
//                manager.deleteNotificationChannel("restored_OS_notifications");
//                manager.deleteNotificationChannel("fcm_fallback_notification_channel");
////                manager.cancel(NOTI_ID);
//                Log.e("getNotificationChannels", String.valueOf(manager.getNotificationChannels()));
////         Channel ids
////         1. fcm_fallback_notification_channel
////         2. restored_OS_notifications
////         3. exampleChannel
//            }
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

}
