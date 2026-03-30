package com.example.coraconnect

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // TAG helps me filter my logs in Logcat easily
    companion object {
        private const val TAG = "CoraConnect"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity onCreate started")  // Log when activity starts

        setContentView(R.layout.activity_main)

        // Find all my views by their IDs from XML
        val timeInput = findViewById<EditText>(R.id.timeInput)
        val suggestionView = findViewById<TextView>(R.id.suggestionText)
        val resetBtn = findViewById<Button>(R.id.resetButton)
        val submitBtn = findViewById<Button>(R.id.submitButton)

        // This makes suggestions appear as user types (real-time)
        timeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim().lowercase()
                Log.d(TAG, "Real-time input: '$input'")  // Log what user types

                if (input.isNotEmpty()) {
                    val spark = getSocialSpark(input)  // Get suggestion
                    displaySuggestion(spark, suggestionView)  // Show it
                } else {
                    resetSuggestionDisplay(suggestionView)  // Clear when empty
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Submit button - confirms user's choice
        submitBtn.setOnClickListener {
            val input = timeInput.text.toString().trim().lowercase()
            Log.i(TAG, "Submit clicked for input: '$input'")  // Important action log

            if (input.isEmpty()) {
                showMotivationalError("Enter a time first!", suggestionView)
                return@setOnClickListener
            }

            val spark = getSocialSpark(input)
            displaySuggestion(spark, suggestionView)
            Toast.makeText(this, "Perfect spark!", Toast.LENGTH_SHORT).show()  // User feedback
        }

        // Reset clears everything back to start
        resetBtn.setOnClickListener {
            timeInput.text.clear()
            resetSuggestionDisplay(suggestionView)
            Log.i(TAG, "App reset by user")
            Toast.makeText(this, "Ready for new spark!", Toast.LENGTH_SHORT).show()
        }

        Log.d(TAG, "All listeners attached successfully")  // Confirm setup done
    }

    // Main logic - checks what user typed and picks right suggestion
    private fun getSocialSpark(input: String): String {
        Log.d(TAG, "Processing spark logic for: '$input'")

        // 'when' is like if-else but cleaner for many options
        return when {
            input.contains("morning") -> {
                "Send 'Good morning' text to family member!"
            }
            input.contains("mid-morning") -> {
                "Quick 'Thank you' to colleague."
            }
            input.contains("afternoon") -> {
                "Share funny meme or link with friend!"
            }
            input.contains("snack") -> {
                "Send 'thinking of you' message."
            }
            input.contains("dinner") -> {
                "5-minute call to friend/relative!"
            }
            input.contains("night") || input.contains("after dinner") -> {
                "Thoughtful comment on friend's post."
            }
            else -> {
                // Handles wrong input nicely
                Log.w(TAG, "Invalid input detected: '$input'")
                "Try 'morning', 'afternoon', or 'dinner'! Keep connecting!"
            }
        }
    }

    // Makes text green when suggestion works
    private fun displaySuggestion(spark: String, view: TextView) {
        view.text = spark
        view.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        Log.i(TAG, "Success spark displayed: $spark")
    }

    // Makes text red + shows helpful message for errors
    private fun showMotivationalError(message: String, view: TextView) {
        view.text = message
        view.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        Toast.makeText(this, "Try again! You're almost there", Toast.LENGTH_SHORT).show()
        Log.w(TAG, "Error displayed: $message")
    }

    // Resets suggestion area to default text and black color
    private fun resetSuggestionDisplay(view: TextView) {
        view.text = "Type a time to see your social spark!"
        view.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }
}