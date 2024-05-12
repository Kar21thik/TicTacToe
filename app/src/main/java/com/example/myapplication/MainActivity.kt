package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Accessing constraintLayout using View Binding
        val constraintLayout: LinearLayout = binding.constraintLayout

        // Start animation on constraintLayout background
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(2000)
            start()
        }

        // Set click listeners using View Binding
        binding.plyOfflineBtn.setOnClickListener {
            createOfflineGame()
        }

        binding.createOnlineGameBtn.setOnClickListener {
            createOnlineGame()
        }

        binding.joinOnlineGameBtn.setOnClickListener {
            joinOnlineGame()
        }
    }

    private fun createOfflineGame() {
        GameData.saveGameModel(
            GameModel(
                gamestatus = GameStatus.JOINED
            )
        )
        startGame()
    }

    private fun createOnlineGame() {
        GameData.myID = "X"
        GameData.saveGameModel(
            GameModel(
                gamestatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
    }
// colorhunt pastel
    private fun joinOnlineGame() {
        val gameId = binding.gameIdInput.text.toString()
        if (gameId.isEmpty()) {
            binding.gameIdInput.error = "Please Enter Game Id"
            return
        }
        GameData.myID = "O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val model = documentSnapshot.toObject(GameModel::class.java)
                if (model == null) {
                    binding.gameIdInput.error = "Please Enter a Valid GameId"
                } else {
                    if (model.gamestatus == GameStatus.CREATED) {
                        // If the game is still in CREATED status, then the second player can join
                        model.gamestatus = GameStatus.JOINED
                        GameData.saveGameModel(model)
                        startGame()
                    } else {
                        // If the game is already joined by another player, show an error
                        binding.gameIdInput.error = "Game is already joined by another player"
                    }
                }
            }
    }

    private fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }
}
