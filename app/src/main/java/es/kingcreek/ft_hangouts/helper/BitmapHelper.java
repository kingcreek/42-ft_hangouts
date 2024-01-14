package es.kingcreek.ft_hangouts.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapHelper {

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        if (bytes != null)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

}
