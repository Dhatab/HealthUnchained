package org.healthunchained.healthunchained;

/**
 * Created by User on 5/30/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "org.healthunchained.healthunchained.action.main";
        public static String INIT_ACTION = "org.healthunchained.healthunchained.action.init";
        public static String PREV_ACTION = "org.healthunchained.healthunchained.action.prev";
        public static String PLAY_ACTION = "org.healthunchained.healthunchained.action.play";
        public static String NEXT_ACTION = "org.healthunchained.healthunchained.action.next";
        public static String STARTFOREGROUND_ACTION = "org.healthunchained.healthunchained.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "org.healthunchained.healthunchained.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.healthbackground, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}
