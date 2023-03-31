package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), DataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateView()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_right, TaskFragment())
            .add(R.id.fragment_left, OperationFragment())
            .commit()
    }

    var correct: Int = 0
    var incorrect: Int = 0

    override fun getData(isCorrect: Boolean) {
        if (isCorrect)
            correct++
        else
            incorrect++

        updateView()
    }

    override fun sendData(): Pair<Int, Int> {
        return Pair(correct, incorrect)
    }

    fun updateView() {
        val textView: TextView = findViewById(R.id.scoreContainer)
        textView.textSize = 16f
        textView.text = getString(R.string.score_text, correct, incorrect)
    }
}