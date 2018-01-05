package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
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
     * @param deferred
     * @return Return {@link android.graphics.Typeface} or null if the path is invalid.
     */
    public static boolean load(final Context context, final int fontFamily, final TextView textView, final boolean deferred) {
        try {
            final WeakReference<TextView> weakTextView = new WeakReference<>(textView);
            final ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {

                @Override
                public void onFontRetrieved(@NonNull final Typeface typeface) {
                    final TextView view = weakTextView.get();
                    if (view == null) {
                        return;
                    }
                    view.setTypeface(typeface);
                    if (deferred) {
                        view.setText(CalligraphyUtils.applyTypefaceSpan(view.getText(), typeface), TextView.BufferType.SPANNABLE);
                        view.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                CalligraphyUtils.applyTypefaceSpan(s, typeface);
                            }
                        });
                    }
                }

                @Override
                public void onFontRetrievalFailed(int reason) {
                    Log.w("Calligraphy", "Font RetrievalFail reason:" + reason);
                }
            };

            ResourcesCompat.getFont(context, fontFamily, fontCallback, new Handler(Looper.getMainLooper()));
            return true;
        } catch (Exception e) {
            Log.w("Calligraphy", "Can't create asset from " + fontFamily + ". Make sure you have passed in the correct path and file name.", e);
            return false;
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
