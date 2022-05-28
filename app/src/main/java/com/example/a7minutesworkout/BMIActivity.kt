package com.example.a7minutesworkout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    private var isWeightKg = true
    private var isHeightCm = true

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBMI)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI - Body Mass Index"
        }
        binding?.toolbarBMI?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.rgWeight?.setOnCheckedChangeListener() {
            _, checkedId: Int ->
            if (checkedId == R.id.rb_kg) {
                isWeightKg = true
                binding?.etUnitWeight?.hint = "WEIGHT (in kg)"
            } else {
                isWeightKg = false
                binding?.etUnitWeight?.hint = "WEIGHT (in lb)"
            }
        }

        binding?.rgHeight?.setOnCheckedChangeListener() {
                _, checkedId: Int ->
            if (checkedId == R.id.rb_cm) {
                isHeightCm = true
                binding?.tilUnitHeightCm?.visibility = View.VISIBLE
                binding?.tilUnitHeightFeet?.visibility = View.GONE
                binding?.tilUnitHeightInch?.visibility = View.GONE
            } else {
                isHeightCm = false
                binding?.tilUnitHeightCm?.visibility = View.GONE
                binding?.tilUnitHeightFeet?.visibility = View.VISIBLE
                binding?.tilUnitHeightInch?.visibility = View.VISIBLE
            }
        }

        binding?.btnCalculate?.setOnClickListener {
            if (isWeightKg && isHeightCm) {
                if (validateUnits(binding?.etUnitWeight) &&
                    validateUnits(binding?.etUnitHeightCm)) {

                    val height: Float = binding?.etUnitHeightCm?.text.toString().toFloat() / 100
                    val weight: Float = binding?.etUnitWeight?.text.toString().toFloat()

                    val bmi = weight/(height*height)
                    displayBMIResult(bmi)
                } else
                    showToast()
            } else if (!isWeightKg && isHeightCm) {
                if (validateUnits(binding?.etUnitWeight) &&
                    validateUnits(binding?.etUnitHeightCm)) {

                    val height: Float = binding?.etUnitHeightCm?.text.toString().toFloat() / 100
                    val weight: Float = binding?.etUnitWeight?.text.toString().toFloat() / 2.2f

                    val bmi = weight/(height*height)
                    displayBMIResult(bmi)
                } else
                    showToast()
            } else if (isWeightKg && !isHeightCm) {
                if (validateUnits(binding?.etUnitWeight) &&
                    validateUnits(binding?.etUnitHeightFeet) &&
                        validateUnits(binding?.etUnitHeightInch)) {

                    val height: Float = (binding?.etUnitHeightFeet?.text.toString().toFloat()*12 +
                            binding?.etUnitHeightInch?.text.toString().toFloat()) * 2.56f/100
                    val weight: Float = binding?.etUnitWeight?.text.toString().toFloat()

                    val bmi = weight/(height*height)
                    displayBMIResult(bmi)
                } else
                    showToast()
            } else {
                if (validateUnits(binding?.etUnitWeight) &&
                    validateUnits(binding?.etUnitHeightFeet) &&
                        validateUnits(binding?.etUnitHeightInch)) {

                    val height: Float = binding?.etUnitHeightFeet?.text.toString().toFloat()*12 +
                            binding?.etUnitHeightInch?.text.toString().toFloat()
                    val weight: Float = binding?.etUnitWeight?.text.toString().toFloat()

                    val bmi = 703*weight/(height*height)
                    displayBMIResult(bmi)
                } else
                    showToast()
            }
        }
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val labelColor: String

        if (bmi.compareTo(18.5f) < 0) {
            bmiLabel = "Underweight"
            labelColor = "#009CF7"  // blue
        } else if (bmi.compareTo(18.5f) >= 0 && bmi.compareTo(25f) < 0) {
            bmiLabel = "Normal Weight"
            labelColor = "#02A437"  // green
        } else if (bmi.compareTo(25f) >= 0 && bmi.compareTo(30f) < 0) {
            bmiLabel = "Overweight"
            labelColor = "#F5B945"  // yellow
        } else {
            bmiLabel = "Obese"
            labelColor = "#EB5463"  // red
        }

        val res = "${BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN)} $bmiLabel"
        binding?.result?.text = res
        binding?.result?.setTextColor(Color.parseColor(labelColor))
    }

    private fun showToast() {
        Toast.makeText(this, "Enter the valid values.", Toast.LENGTH_LONG).show()
    }

    private fun validateUnits(unit: androidx.appcompat.widget.AppCompatEditText?): Boolean {
        return unit?.text.toString().isNotEmpty()
    }
}