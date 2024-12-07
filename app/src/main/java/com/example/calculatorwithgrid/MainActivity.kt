package com.example.calculatorwithgrid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculatorwithgrid.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            setSupportActionBar(toolbar)
            Log.d(TAG, "binding apply")
            val numListener = View.OnClickListener { view ->
                val num = view.tag.toString()
                Log.d(TAG, "pressed: $num")
                inputET.setText("${inputET.text}$num")
            }
            val opListener = View.OnClickListener { view ->
                val text = inputET.text.toString()
                val op = view.tag.toString()
                when {
                    text.matches("^\\d+$".toRegex()) -> {
                        inputET.setText("${inputET.text}$op")
                    }
                    text.matches("^\\d+[+\\-*/]".toRegex()) -> {
                        val num = "^\\d+".toRegex().find(inputET.text)?.value ?: throw IllegalStateException("Wrong parent regex")
                        inputET.setText("$num$op")
                    }
                    text.isBlank() -> makeToast(R.string.enter_first_operand)
                    else -> makeToast(R.string.you_have_begun_entering_second)
                }
            }
            oneBTN.setOnClickListener(numListener)
            twoBTN.setOnClickListener(numListener)
            threeBTN.setOnClickListener(numListener)
            fourBTN.setOnClickListener(numListener)
            fiveBTN.setOnClickListener(numListener)
            sixBTN.setOnClickListener(numListener)
            sevenBTN.setOnClickListener(numListener)
            eightBTN.setOnClickListener(numListener)
            nineBTN.setOnClickListener(numListener)
            zeroBTN.setOnClickListener(numListener)
            multBTN.setOnClickListener(opListener)
            divBTN.setOnClickListener(opListener)
            minusBTN.setOnClickListener(opListener)
            plusBTN.setOnClickListener(opListener)
            resetBTN.setOnClickListener { view ->
                inputET.text.clear()
            }
            equalBTN.setOnClickListener {
                val input = inputET.text
                if (input.matches("^\\d+[+\\-*/]\\d+$".toRegex())) {
                    val regex = "\\d+".toRegex()
                    val matches = regex.find(input)
                    val first = matches?.value?.toInt() ?: throw IllegalStateException("Wrong parent regex")
                    val second: Int = matches.next()?.value?.toInt() ?: throw IllegalStateException("Wrong parent regex")
                    val op = "[+\\-*/]".toRegex().find(input)?.value ?: throw IllegalStateException("Wrong parent regex")
                    val result = when(op) {
                        "+" -> first + second
                        "-" -> first - second
                        "*" -> first * second
                        "/" -> first / second
                        else -> throw IllegalArgumentException("Wrong operand")
                    }
                    resultTV.text = String.format(Locale.getDefault(),
                        getString(R.string.result_placeholder), result.toString())
                } else {
                    makeToast(R.string.enter_both_operands_and_operation)
                }
            }
        }
    }
    private fun makeToast(@StringRes str: Int) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_exit) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}