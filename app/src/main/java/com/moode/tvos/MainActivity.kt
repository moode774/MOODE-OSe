package com.moode.tvos

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 14, 28)
    private val cyan = Color.rgb(63, 224, 255)
    private val white = Color.rgb(230, 240, 255)
    private val apps = listOf(
        Triple("▶  YouTube", "com.google.android.youtube.tv", "فيديو"),
        Triple("✦  Crunchyroll", "com.crunchyroll.crunchyroid", "أنمي"),
        Triple("◈  OSN+", "com.osn.go", "أفلام ومسلسلات"),
        Triple("♫  Spotify", "com.spotify.tv.android", "موسيقى"),
        Triple("▣  Shahid", "net.mbc.shahidTV", "ترفيه")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_VERTICAL; setPadding(56, 35, 56, 35); setBackgroundColor(bg) }
        root.addView(label("MOODE  /  OS", 48, cyan, true))
        root.addView(label("مركز التحكم الذكي  •  نسخة تجريبية 0.1", 23, white))
        root.addView(label("التطبيقات", 30, white, true).apply { setPadding(0, 34, 0, 18) })
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        apps.forEach { (title, pkg, desc) ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; isFocusable = true; isFocusableInTouchMode = true
                background = panel(false)
                setPadding(12, 20, 12, 20)
                setOnFocusChangeListener { v, focused -> v.background = panel(focused); v.scaleX = if (focused) 1.04f else 1f; v.scaleY = if (focused) 1.04f else 1f }
                setOnClickListener { openApp(pkg) }
            }
            card.addView(label(title, 21, white, true))
            card.addView(label(desc, 16, cyan))
            row.addView(card, LinearLayout.LayoutParams(0, 130, 1f).apply { marginEnd = 14 })
        }
        root.addView(row)
        root.addView(label("تحكم سريع", 28, white, true).apply { setPadding(0, 35, 0, 16) })
        val buttons = LinearLayout(this)
        listOf("الصوت +" to 1, "الصوت −" to -1, "الإعدادات" to 2, "الرئيسية الأصلية" to 3).forEach { (name, action) ->
            val b = label(name, 19, white, true).apply {
                gravity = Gravity.CENTER; isFocusable = true; background = panel(false); setPadding(12, 18, 12, 18)
                setOnFocusChangeListener { v, focused -> v.background = panel(focused) }
                setOnClickListener { when (action) { 1, -1 -> { val am = getSystemService(AUDIO_SERVICE) as AudioManager; am.adjustStreamVolume(AudioManager.STREAM_MUSIC, if (action == 1) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI) }; 2 -> startActivity(Intent("android.settings.SETTINGS")); else -> startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)) } }
            }
            buttons.addView(b, LinearLayout.LayoutParams(0, 66, 1f).apply { marginEnd = 15 })
        }
        root.addView(buttons)
        root.addView(label("مساعد المحادثة غير مدمج بعد. الأوامر الحالية محلية فقط.  |  استخدم الريموت للتنقل", 15, Color.LTGRAY).apply { setPadding(0, 25, 0, 0) })
        setContentView(root)
        row.getChildAt(0).requestFocus()
    }
    private fun label(t: String, size: Int, color: Int, bold: Boolean = false) = TextView(this).apply { text = t; textSize = size.toFloat(); setTextColor(color); if (bold) setTypeface(null, Typeface.BOLD); gravity = Gravity.CENTER_VERTICAL }
    private fun panel(focused: Boolean) = GradientDrawable(GradientDrawable.Orientation.TL_BR, if (focused) intArrayOf(Color.rgb(18, 82, 112), Color.rgb(22, 32, 66)) else intArrayOf(Color.rgb(22, 36, 62), Color.rgb(14, 23, 43))).apply { cornerRadius = 24f; setStroke(if (focused) 3 else 1, if (focused) cyan else Color.rgb(40, 72, 104)) }
    private fun openApp(pkg: String) {
        try { val intent = packageManager.getLaunchIntentForPackage(pkg); if (intent != null) startActivity(intent) else Toast.makeText(this, "التطبيق غير متاح على هذا الجهاز", Toast.LENGTH_SHORT).show() }
        catch (e: Exception) { Toast.makeText(this, "تعذر فتح التطبيق", Toast.LENGTH_SHORT).show() }
    }
}
