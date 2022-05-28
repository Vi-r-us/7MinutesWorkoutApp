package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var totalExerciseDone = 0

    // Variable for Timer which will be initialized later.
    private var restTimer: CountDownTimer? = null
    // The progress goes up to 10
    private var restProgress = 0
    // pauseOffset = timerDuration - time Left
    private var pauseOffsetRest: Long = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var pauseOffsetExercise: Long = 0

    private var btnIsPlay: Boolean = false
    private var isPaused = false

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    // Text to Speech Object
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    // RecyclerView (ExerciseStatus) getting adapter object
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get the exercise list
        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnPrevious?.setOnClickListener {
            if (currentExercisePosition == 0) {
                // Play the sound - Can't Change to previous exercise
                try {
                    val soundURI = Uri.parse(
                        "android.resource://com.example.a7minutesworkout/" + R.raw.ex_change_negative)
                    player = MediaPlayer.create(applicationContext, soundURI)
                    player?.isLooping = false
                    player?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Play the sound - Can Change to previous exercise
                try {
                    val soundURI = Uri.parse(
                        "android.resource://com.example.a7minutesworkout/" + R.raw.ex_change_success)
                    player = MediaPlayer.create(applicationContext, soundURI)
                    player?.isLooping = false
                    player?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                exerciseList!![currentExercisePosition].setIsSelected(false)
                currentExercisePosition--
                pauseOffsetRest = 0
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }

        binding?.btnNext?.setOnClickListener {
            // Play the sound - Can Change to next exercise
            try {
                val soundURI = Uri.parse(
                    "android.resource://com.example.a7minutesworkout/" + R.raw.ex_change_success)
                player = MediaPlayer.create(applicationContext, soundURI)
                player?.isLooping = false
                player?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            pauseOffsetRest = 0
            // Set is selected false
            exerciseList!![currentExercisePosition].setIsSelected(false)
            exerciseAdapter!!.notifyDataSetChanged()

            if (currentExercisePosition == exerciseList!!.size-1) {
                Toast.makeText(this@ExerciseActivity,
                    "$totalExerciseDone Exercises are completed.",
                    Toast.LENGTH_LONG).show()

                finish()
                val intent = Intent(this@ExerciseActivity, WorkoutCompletionActivity::class.java)
                intent.putExtra("totalExerciseDone", totalExerciseDone)
                startActivity(intent)
            }

            setupRestView()
        }

        binding?.btnPlayPause?.setOnClickListener {
            Log.i("MyActivity", "$pauseOffsetExercise")
            if (btnIsPlay) {
                binding?.btnPlayPause?.setBackgroundResource(R.drawable.pause)
                isPaused = true
                //pauseTimer(false)

            } else {
                binding?.btnPlayPause?.setBackgroundResource(R.drawable.play)
                isPaused = false
                setExerciseProgressBar(pauseOffsetExercise)
            }
            btnIsPlay = !btnIsPlay

        }

        // binding?.flProgressBar?.visibility = View.INVISIBLE
        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    // Setup Exercise Status RecyclerView's Adapter
    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupExerciseView() {
        // Set Rest screen invisible
        binding?.flRestProgressBar?.visibility = View.INVISIBLE
        binding?.tvRestTitle?.visibility = View.INVISIBLE
        binding?.tvRestNextExercise?.visibility = View.INVISIBLE

        // Set Exercise screen visible
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.options?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.ExerciseTitle?.visibility = View.VISIBLE

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        // Set Image and Exercise Title
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.ExerciseTitle?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar(pauseOffsetExercise)
    }

    private fun setupRestView() {
        // Play the sound - Rest Start Sound
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout/" + R.raw.rest_start_sound)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Set Rest screen visible
        binding?.flRestProgressBar?.visibility = View.VISIBLE
        binding?.tvRestTitle?.visibility = View.VISIBLE
        binding?.tvRestNextExercise?.visibility = View.VISIBLE

        // Set Exercise screen invisible
        binding?.flExerciseProgressBar?.visibility = View.INVISIBLE
        binding?.options?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.ExerciseTitle?.visibility = View.INVISIBLE

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        val exerciseName = "NEXT STEP: " + exerciseList!![currentExercisePosition+1].getName()
        binding?.tvRestNextExercise?.text = exerciseName

        setRestProgressBar(pauseOffsetRest)
    }
    
    private fun setRestProgressBar(pauseOffsetL: Long) {
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(10000 - pauseOffsetL,1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++

                // Play Boop sound
                if (restProgress in 8..10) {
                    playBoop()
                }
                pauseOffsetRest = 10000 - millisUntilFinished
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                pauseOffsetExercise = 0
                currentExercisePosition++
                // now the exercise is selected
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar(pauseOffsetL: Long) {
        binding?.ProgressBarEx?.progress = exerciseProgress

        exerciseTimer = object: CountDownTimer(30000 - pauseOffsetL,1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++

                // Play Boop sound
                if (exerciseProgress in 28..30) {
                    playBoop()
                }

                Log.i("Clicked", "$pauseOffsetExercise + $millisUntilFinished")
                binding?.ProgressBarEx?.progress = 30 - exerciseProgress
                binding?.tvTimerEx?.text = (30 - exerciseProgress).toString()
                if (isPaused) {
                    pauseOffsetExercise = 30000  - millisUntilFinished
                    Log.i("Clicked", pauseOffsetExercise.toString())
                    cancel()
                }
            }

            override fun onFinish() {
                pauseOffsetRest = 0

                if (!exerciseList!![currentExercisePosition].getIsCompleted())
                    totalExerciseDone++

                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    Toast.makeText(this@ExerciseActivity,
                    "All Exercises are completed",
                    Toast.LENGTH_LONG).show()
                    finish()
                    val intent = Intent(this@ExerciseActivity, WorkoutCompletionActivity::class.java)
                    intent.putExtra("totalExerciseDone", totalExerciseDone)
                    startActivity(intent)
                }
            }

        }.start()
    }

    // true - Rest , false - Exercise
    private fun pauseTimer(type: Boolean) {
        Log.i("MyActivity", "$pauseOffsetExercise")
        if (type) {
            if (restTimer != null)
                restTimer?.cancel()
        }
        else {
            if (exerciseTimer != null)
                exerciseTimer?.cancel()
        }
    }

    override fun onBackPressed() {
        // Dialog Builder "Are you ready?"
        val sureDialog = Dialog(this)

        sureDialog.setContentView(R.layout.are_you_sure_layout_dialog)
        sureDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        sureDialog.setCancelable(false)
        sureDialog.setTitle("Are you sure?")

        // on ready Start the exercise activity
        sureDialog.findViewById<Button>(R.id.ok_button).setOnClickListener {
            this@ExerciseActivity.finish()
            sureDialog.dismiss()
        }
        // on cancel dismiss the dialog box
        sureDialog.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            sureDialog.dismiss()
        }

        sureDialog.show()
    }

    // Play Boop Sound
    private fun playBoop() {
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout/" + R.raw.boop)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Initialize the textToSpeech model
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            // Set the locale language
            val result = tts?.setLanguage(Locale.UK)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                Log.e("TTS", "The Language specified is not supported!")

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    // Speak out the text
    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Shutdown all the features in the activity
    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null
    }
}