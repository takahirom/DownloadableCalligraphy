package uk.co.chrisjenx.calligraphy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper loading {@link android.graphics.Typeface} avoiding the leak of the font when loaded
 * by multiple calls to {@link android.graphics.Typeface#createFromAsset(android.content.res.AssetManager, String)}
 * on pre-ICS versions.
 * <p>
 * More details can be found here https://code.google.com/p/android/issues/detail?id=9904
 * <p>
 * Created by Chris Jenkins on 04/09/13.
 */
public final class TypefaceUtils {

    private static final Map<Typeface, CalligraphyTypefaceSpan> sCachedSpans = new HashMap<Typeface, CalligraphyTypefaceSpan>();

    /**
     * A helper loading a custom font.
     *
     * @param context    App's asset manager.
     * @param fontFamily The path of the file.
     * @param textView
     * @return Return {@link android.graphics.Typeface} or null if the path is invalid.
     */
    @SuppressLint("RestrictedApi")
    public static Typeface load(final Context context, final int fontFamily, TextView textView) {
        try {
            return ResourcesCompat.getFont(context, fontFamily, new TypedValue(), Typeface.NORMAL, textView);
        } catch (Exception e) {
            Log.w("Calligraphy", "Can't create asset from " + fontFamily + ". Make sure you have passed in the correct path and file name.", e);
            return null;
        }
    }

    /**
     * A helper loading custom spans so we don't have to keep creating hundreds of spans.
     *
     * @param typeface not null typeface
     * @return will return null of typeface passed in is null.
     */
    public static CalligraphyTypefaceSpan getSpan(final Typeface typeface) {
        if (typeface == null) return null;
        synchronized (sCachedSpans) {
            if (!sCachedSpans.containsKey(typeface)) {
                final CalligraphyTypefaceSpan span = new CalligraphyTypefaceSpan(typeface);
                sCachedSpans.put(typeface, span);
                return span;
            }
            return sCachedSpans.get(typeface);
        }
    }

    private TypefaceUtils() {
    }
}
