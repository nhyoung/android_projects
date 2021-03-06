package edu.uw.nhyoung.hello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btn_greet)

        button.setOnClickListener {
            val messages = resources.getStringArray(R.array.greetings_array)
            val message = messages[Random.nextInt(0,messages.size)]
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
        }
    }
}