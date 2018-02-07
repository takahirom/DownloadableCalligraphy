DownloadableCalligraphy
=======================

This library provides a way to set default (downloadable) fonts using [Calligraphy](https://github.com/chrisjenx/Calligraphy) methods.

This library supports [Downloadable Font](https://developer.android.com/guide/topics/ui/look-and-feel/downloadable-fonts.html) of Support Library 26.0.

# Credit
This library based on [chrisjenx/Calligraphy](https://developer.android.com/guide/topics/ui/look-and-feel/downloadable-fonts.html)

And this library use [Android Support Library](https://developer.android.com/guide/topics/ui/look-and-feel/downloadable-fonts.html)


## Getting started

### Dependency

Latest Version : [ ![Download](https://api.bintray.com/packages/takahirom/maven/downloadable-calligraphy/images/download.svg) ](https://bintray.com/takahirom/maven/downloadable-calligraphy/_latestVersion)

```gradle
implementation 'com.github.takahirom.downloadable.calligraphy:downloadable-calligraphy:[Latest Version]'
```

### Add Fonts

Add your custom fonts to `font/`. All font definitions are relative to this path.

Assuming that you are using Gradle you should create the fonts directory under `src/main/` in your project directory if it does not already exist.
As it's popular to use multi-project build with Gradle the path is usually `app/src/main/font/`, where `app` is the project name.

### Usage

```xml
<TextView android:fontFamily="@font/my_font"/>
``` 
**Note: The missing namespace, this __IS__ intentional.**

### Installation

Define your default font using `CalligraphyConfig`, in your `Application` class in the `#onCreate()` method.

```java
@Override
public void onCreate() {
    super.onCreate();
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFont(R.font.roboto_regular)
                            .build()
            );
    //....
}
```

### Inject into Context

Wrap the `Activity` Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
}
```

_You're good to go!_


## Usage

### Custom font per TextView

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:fontFamily="@font/roboto_bold"/>
```

_Note: Popular IDE's (Android Studio, IntelliJ) will likely mark this as an error despite being correct. You may want to add `tools:ignore="MissingPrefix"` to either the View itself or its parent ViewGroup to avoid this. You'll need to add the tools namespace to have access to this "ignore" attribute. `xmlns:tools="
http://schemas.android.com/tools"`. See https://code.google.com/p/android/issues/detail?id=65176._

### Custom font in TextAppearance


```xml
<style name="TextAppearance.FontPath" parent="android:TextAppearance">
    <!-- Custom Attr-->
    <item name="android:fontFamily">@font/roboto_condensed_regular</item>
</style>
```

```xml
<TextView
    android:text="@string/hello_world"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textAppearance="@style/TextAppearance.FontPath"/>

```

### Custom font in Styles


```xml
<style name="TextViewCustomFont">
    <item name="android:fontFamily">@font/roboto_condensed_regular</item>
</style>
```

### Custom font defined in Theme

```xml
<style name="AppTheme" parent="android:Theme.Holo.Light.DarkActionBar">
    <item name="android:textViewStyle">@style/AppTheme.Widget.TextView</item>
</style>

<style name="AppTheme.Widget"/>

<style name="AppTheme.Widget.TextView" parent="android:Widget.Holo.Light.TextView">
    <item name="android:fontFamily">@fonts/roboto_thinitalic</item>
</style>
```


# FAQ

### Font Resolution 

The `CalligraphyFactory` looks for the font in a pretty specific order, for the _most part_ it's
 very similar to how the Android framework resolves attributes.
 
1. `View` xml - attr defined here will always take priority.
2. `Style` xml - attr defined here is checked next.
3. `TextAppearance` xml - attr is checked next, the only caveat to this is **IF** you have a font 
 defined in the `Style` and a `TextAttribute` defined in the `View` the `Style` attribute is picked first!
4. `Theme` - if defined this is used.
5. `Default` - if defined in the `CalligraphyConfig` this is used of none of the above are found 
**OR** if one of the above returns an invalid font. 

### Why no jar?

We needed to ship a custom ID with Calligraphy to improve the Font Injection flow. This
unfortunately means that is has to be an `aar`. But you're using Gradle now anyway right?

### Multiple Typeface's per TextView / Spannables

It is possible to use multiple Typefaces inside a `TextView`, this isn't new concept to Android.

This _could_ be achieved using something like the following code.

```java
SpannableStringBuilder sBuilder = new SpannableStringBuilder();
sBuilder.append("Hello!") // Bold this
        .append("I use Calligraphy"); // Default TextView font.
// Create the Typeface you want to apply to certain text
CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/Roboto-Bold.ttf"));
// Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
sBuilder.setSpan(typefaceSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
setText(sBuilder, TextView.BufferType.SPANNABLE);
```
Of course this is just an example. Your mileage may vary.

### Exceptions / Pitfalls

To our knowledge (try: `grep -r -e "void set[^(]*(Typeface " <android source dir>`) there are two standard Android widgets that have multiple methods to set typefaces. They are:

 - android.support.v7.widget.SwitchCompat
 - android.widget.Switch

Both have a method called `setSwitchTypeface` that sets the typeface within the switch (e.g. on/off, yes/no). `SetTypeface` sets the typeface of the label. You will need to create your own subclass that overrides `setTypeface` and calls both `super.setTypeface` and `super.setSwitchTypeface`.





# Collaborators

- [@mironov-nsk](https://github.com/mironov-nsk)
- [@Roman Zhilich](https://github.com/RomanZhilich)
- [@Smuldr](https://github.com/Smuldr)
- [@Codebutler](https://github.com/codebutler)
- [@loganj](https://github.com/loganj)
- [@dlew](https://github.com/dlew)
- [@ansman](https://github.com/ansman)

# Licence

    Copyright 2013 Christopher Jenkins,
    Modifications Copyright (C) 2017 takahirom

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
