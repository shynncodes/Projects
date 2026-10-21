package com.projectzero.assistant

import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommandProcessor(private val context: Context) {

    fun process(input: String): String {
        val command = input.trim().lowercase(Locale.ROOT)

        if (command.isBlank()) {
            return "Kuch bolo, main sun raha hoon."
        }

        return when {
            command.contains("hello") ||
            command.contains("hi") ||
            command.contains("namaste") ||
            command.contains("hey zero") ->
                "Namaste! Main ZERO hoon. Batao, kya karna hai?"

            command.contains("tumhara naam") ||
            command.contains("your name") ->
                "Mera naam ZERO hai."

            command.contains("time") ||
            command.contains("kitne baje") ||
            command.contains("samay") ->
                "Abhi " + SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(Date()) + " baj rahe hain."

            command.contains("date") ||
            command.contains("tarikh") ||
            command.contains("aaj ki date") ->
                "Aaj ki date " + SimpleDateFormat(
                    "dd MMMM yyyy",
                    Locale("hi", "IN")
                ).format(Date()) + " hai."

            command.contains("youtube") ->
                openApp(
                    listOf("com.google.android.youtube"),
                    "YouTube"
                )

            command.contains("whatsapp") ->
                openApp(
                    listOf("com.whatsapp"),
                    "WhatsApp"
                )

            command.contains("instagram") ->
                openApp(
                    listOf("com.instagram.android"),
                    "Instagram"
                )

            command.contains("chrome") ||
            command.contains("browser") ->
                openApp(
                    listOf(
                        "com.android.chrome",
                        "com.chrome.beta"
                    ),
                    "Browser"
                )

            command.contains("settings") ||
            command.contains("setting kholo") ->
                openSettings()

            command.contains("calculator") ||
            command.contains("calculate") ||
            command.contains("hisab") ->
                openApp(
                    listOf(
                        "com.google.android.calculator",
                        "com.android.calculator2"
                    ),
                    "Calculator"
                )

            command.contains("help") ||
            command.contains("commands") ||
            command.contains("kya kar sakte") ->
                getHelp()

            command.contains("goodbye") ||
            command.contains("bye") ->
                "Theek hai! Jab zaroorat ho, ZERO ko open kar lena."

            else ->
                "Main tumhari baat samajh gaya: $input. " +
                "Is command ke liye abhi mujhe additional functionality chahiye."
        }
    }

    private fun openApp(
        packageNames: List<String>,
        appName: String
    ): String {
        for (packageName in packageNames) {
            try {
                val launchIntent =
                    context.packageManager.getLaunchIntentForPackage(
                        packageName
                    )

                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)

                    return "$appName khol raha hoon."
                }
            } catch (_: Exception) {
                // Try the next package.
            }
        }

        return "$appName nahi mil raha. Ho sakta hai app installed na ho."
    }

    private fun openSettings(): String {
        return try {
            val intent = Intent(
                android.provider.Settings.ACTION_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
            "Phone settings khol raha hoon."
        } catch (_: Exception) {
            "Phone settings nahi khul payi."
        }
    }

    fun getHelp(): String {
        return """
ZERO VOICE COMMANDS

• Hello ZERO
• What is your name?
• Time kya hua hai?
• Aaj ki date batao
• YouTube kholo
• WhatsApp kholo
• Instagram kholo
• Chrome kholo
• Calculator kholo
• Phone settings kholo

Aur commands add ki ja sakti hain.
        """.trimIndent()
    }
}
