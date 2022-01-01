package com.develogica.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

//private const val STATE_PENDING_OPERATION = "PendingOperation"
//private const val STATE_OPERAND1 = "Operand1"
//private const val STATE_OPERAND1_STORED = "Operand1_Stored"

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: CalculatorViewModel by viewModels()
        viewModel.stringResult.observe(this) { stringResult -> result.text = stringResult }
        viewModel.stringNewNumber.observe(this) { stringNumber -> newNumber.setText(stringNumber) }
        viewModel.stringOperation.observe(this) { stringOperation -> operation.text = stringOperation}

//        val viewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)
        val listener = View.OnClickListener { view ->
            viewModel.digitPressed(( view as Button).text.toString())
        }


        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { view ->
            viewModel.operandPressed((view as Button).text.toString())
        }

        buttonEquals.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            viewModel.negPressed()

        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if (operand1 != null) {
//            outState.putDouble(STATE_OPERAND1, operand1!!)
//            outState.putBoolean(STATE_OPERAND1_STORED, true)
//        }
//
//        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
//            savedInstanceState.getDouble(STATE_OPERAND1)
//        } else {
//            null
//        }
//        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)!!
//        operation.text = pendingOperation
//    }
}
