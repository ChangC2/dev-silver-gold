package seemesave.businesshub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static Bitmap convert(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str.substring(base64Str.indexOf(",")  + 1), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String convert(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createImageFromString(int width, int height, int color, String name) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint2 = new Paint();
        paint2.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint2);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize((int)(width / 2));
        paint.setTextScaleX(1);
        canvas.drawText(name, (int)(width / 3), (int)(height / 1.7), paint);
        return bitmap;
    }

    public static Bitmap toBlurBitmap(Context context, Bitmap image) {
        if (null == image) return null;

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 0, out);
//        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(25f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
