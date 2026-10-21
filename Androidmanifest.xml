package com.projectzero.assistant

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var statusText: TextView
    private lateinit var conversationText: TextView
    private lateinit var voiceButton: Button

    private lateinit var voiceEngine: VoiceEngine
    private lateinit var commandProcessor: CommandProcessor
    private lateinit var tts: TextToSpeech

    private val micRequestCode = 1001
    private var ttsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        commandProcessor = CommandProcessor(this)

        voiceEngine = VoiceEngine(
            activity = this,
            onResult = { recognizedText ->
                runOnUiThread {
                    processCommand(recognizedText)
                }
            },
            onError = { message ->
                runOnUiThread {
                    statusText.text = message
                    voiceButton.isEnabled = true
                }
            }
        )

        createInterface()

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                micRequestCode
            )
        }
    }

    private fun createInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 36, 24, 24)
            setBackgroundColor(Color.rgb(16, 24, 32))
        }

        val title = TextView(this).apply {
            text = "PROJECT ZERO"
            textSize = 30f
            setTextColor(Color.rgb(0, 220, 255))
            gravity = Gravity.CENTER
        }

        statusText = TextView(this).apply {
            text = "Your personal AI assistant"
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 24)
        }

        conversationText = TextView(this).apply {
            text = "ZERO: Namaste! Main ZERO hoon.\n" +
                    "Neeche button dabakar mujhse baat karo."
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(16, 16, 16, 16)
        }

        val scrollView = ScrollView(this).apply {
            addView(conversationText)
        }

        voiceButton = Button(this).apply {
            text = "🎙  TALK TO ZERO"
            textSize = 18f
            setOnClickListener {
                startListening()
            }
        }

        val helpButton = Button(this).apply {
            text = "COMMAND HELP"
            setOnClickListener {
                val help = commandProcessor.getHelp()
                conversationText.text = help
                speak("Yeh hain mere supported commands.")
            }
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            statusText,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        root.addView(
            voiceButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            helpButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)
    }

    private fun startListening() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                micRequestCode
            )
            return
        }

        voiceButton.isEnabled = false
        statusText.text = "Listening..."

        voiceEngine.startListening()
    }

    private fun processCommand(command: String) {
        conversationText.text =
            "YOU: $command\n\nZERO: Soch raha hoon..."

        statusText.text = "Processing command..."

        val response = commandProcessor.process(command)

        conversationText.text = "YOU: $command\n\nZERO: $response"

        statusText.text = "Ready"

        speak(response)

        voiceButton.isEnabled = true
    }

    private fun speak(text: String) {
        if (ttsReady) {
            tts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "ZERO_RESPONSE"
            )
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("hi", "IN"))
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED

            if (!ttsReady) {
                tts.setLanguage(Locale.US)
                ttsReady = true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == micRequestCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                statusText.text = "Microphone ready"
            } else {
                statusText.text = "Microphone permission required"
                Toast.makeText(
                    this,
                    "Voice commands ke liye microphone permission do.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroy() {
        voiceEngine.destroy()
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
