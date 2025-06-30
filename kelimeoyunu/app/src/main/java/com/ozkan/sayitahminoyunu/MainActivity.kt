package com.ozkan.sayitahminoyunu

import android.graphics.Color
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    // Kelimeler ve ipuçları
    private val wordList = listOf("elma", "masa", "araba", "kitap", "kalem", "telefon", "bilgisayar", "defter", "sandalye", "bardak",
        "çanta", "gözlük", "kapı", "pencere", "uçak", "tren", "televizyon", "radyo", "lamba", "saat",
        "ayna", "silgi", "çorap", "elbise", "şemsiye", "makas", "cetvel", "kamera", "mikrofon", "çekiç",
        "buzdolabı", "fırın", "tabak", "kaşık", "çatal", "bıçak", "tepsi", "yastık", "battaniye", "halı",
        "perde", "dolap", "çamaşır", "kalorifer", "klima", "sabun", "diş fırçası", "şampuan", "havlu", "çantam")
    private val hints = mapOf(
        "elma" to "Kırmızı veya yeşil renkte, yenilebilir bir meyve.",
        "masa" to "Üzerine eşya konulan dört ayaklı mobilya.",
        "araba" to "Yolculuk yapmak için kullanılan motorlu taşıt.",
        "kitap" to "Bilgi ya da hikaye içeren, sayfalardan oluşan nesne.",
        "kalem" to "Yazı yazmak için kullanılır.",
        "telefon" to "İletişim kurmak için kullanılan cihaz.",
        "bilgisayar" to "Veri işlemek için kullanılan elektronik cihaz.",
        "defter" to "Yazı yazmak için kullanılan yapraklı nesne.",
        "sandalye" to "Oturmak için kullanılan mobilya.",
        "bardak" to "Sıvı içmek için kullanılan kap.",
        "çanta" to "Eşyaları taşımak için kullanılan aksesuar.",
        "gözlük" to "Görme problemlerini düzeltmek için kullanılır.",
        "kapı" to "Odalar arasında geçişi sağlar.",
        "pencere" to "Dışarıyı görmek ve hava almak için kullanılır.",
        "uçak" to "Hava yoluyla ulaşım sağlayan taşıt.",
        "tren" to "Raylar üzerinde giden uzun taşıt.",
        "televizyon" to "Görsel ve işitsel yayınları izlemek için kullanılır.",
        "radyo" to "Sesli yayınları dinlemek için kullanılır.",
        "lamba" to "Ortama ışık sağlayan araç.",
        "saat" to "Zamanı gösteren araç.",
        "ayna" to "Yansımanızı görebileceğiniz yüzey.",
        "silgi" to "Yazılanları silmek için kullanılır.",
        "çorap" to "Ayakları sıcak tutmak için giyilir.",
        "elbise" to "Vücudu örtmek için giyilen kıyafet.",
        "şemsiye" to "Yağmurdan korunmak için kullanılır.",
        "makas" to "Kağıt veya kumaş kesmek için kullanılır.",
        "cetvel" to "Düz çizgi çizmek ve ölçüm yapmak için kullanılır.",
        "kamera" to "Fotoğraf veya video çekmek için kullanılır.",
        "mikrofon" to "Ses kaydetmek veya iletmek için kullanılır.",
        "çekiç" to "Çivi çakmak için kullanılan alet.",
        "buzdolabı" to "Yiyecekleri soğuk tutmak için kullanılır.",
        "fırın" to "Yemek pişirmek için kullanılan cihaz.",
        "tabak" to "Yemekleri üzerine koymak için kullanılır.",
        "kaşık" to "Sulu yemekleri yemek için kullanılır.",
        "çatal" to "Katı yemekleri yemek için kullanılır.",
        "bıçak" to "Yiyecekleri kesmek için kullanılır.",
        "tepsi" to "Birden fazla eşyayı taşımak için kullanılır.",
        "yastık" to "Başınızı üzerine koyarak uyursunuz.",
        "battaniye" to "Soğukta üzerinize örtersiniz.",
        "halı" to "Zemini kaplamak için kullanılır.",
        "perde" to "Pencereyi kapatmak için kullanılır.",
        "dolap" to "Eşyaları saklamak için kullanılır.",
        "çamaşır" to "Kıyafetler için genel ad.",
        "kalorifer" to "Odayı ısıtmak için kullanılır.",
        "klima" to "Havayı ısıtmak veya soğutmak için kullanılır.",
        "sabun" to "Ellerinizi temizlemek için kullanılır.",
        "dişfırçası" to "Dişlerinizi fırçalamak için kullanılır.",
        "şampuan" to "Saç temizliği için kullanılır.",
        "havlu" to "Kurulanmak için kullanılır.",
        "ayna" to "Yüzünüze bakmak için kullanılır.",
        "çantam" to "Eşyalarımı koyduğum kişisel eşya."
    )

    // Arayüz
    private lateinit var textViewHint: TextView
    private lateinit var textViewScore: TextView
    private lateinit var textViewGuessLeft: TextView
    private lateinit var textViewTimer: TextView
    private lateinit var editTextGuess: EditText
    private lateinit var buttonCheck: Button
    private lateinit var buttonStart: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonResume: Button
    private lateinit var letterContainer: LinearLayout
    private lateinit var buttonPass: Button

    // Oyun değişkenleri
    private var currentWord = ""
    private var shuffledWord = ""
    private var score = 0
    private var guessLeft = 3
    private var remainingTime: Long = 30000
    private lateinit var countDownTimer: CountDownTimer
    private val usedWords = mutableSetOf<String>()

    // Ses
    private lateinit var soundPool: SoundPool
    private var soundCorrect = 0
    private var soundWrong = 0

    // Animasyon
    private lateinit var bounceAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bağlantılar
        textViewHint = findViewById(R.id.textViewHint)
        textViewScore = findViewById(R.id.textViewScore)
        textViewGuessLeft = findViewById(R.id.textViewGuessLeft)
        textViewTimer = findViewById(R.id.textViewTimer)
        editTextGuess = findViewById(R.id.editTextGuess)
        buttonCheck = findViewById(R.id.buttonCheck)
        buttonStart = findViewById(R.id.buttonStart)
        buttonPause = findViewById(R.id.buttonPause)
        buttonResume = findViewById(R.id.buttonResume)
        letterContainer = findViewById(R.id.letterContainer)

        // Ses
        soundPool = SoundPool.Builder().setMaxStreams(2).build()
        soundCorrect = soundPool.load(this, R.raw.correct, 1)
        soundWrong = soundPool.load(this, R.raw.wrong, 1)

        // Animasyon
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.button_bounce)

        // Başlat
        buttonStart.setOnClickListener {
            buttonStart.visibility = View.GONE
            buttonPause.visibility = View.VISIBLE
            startNewRound()
        }

        // Kontrol et
        buttonCheck.setOnClickListener {
            buttonCheck.startAnimation(bounceAnimation)

            val guess = editTextGuess.text.toString().trim().lowercase(Locale.getDefault())
            if (guess.isEmpty()) {
                Toast.makeText(this, "Lütfen bir tahmin girin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (guess == currentWord) {
                soundPool.play(soundCorrect, 1f, 1f, 0, 0, 1f)
                Toast.makeText(this, "Doğru tahmin!", Toast.LENGTH_SHORT).show()
                score += 10


                textViewScore.setTextColor(Color.GREEN)
                textViewScore.postDelayed({
                    textViewScore.setTextColor(Color.BLACK)
                }, 500)

                nextRound()
            } else {
                soundPool.play(soundWrong, 1f, 1f, 0, 0, 1f)
                guessLeft--
                score = (score - 5).coerceAtLeast(0)
                Toast.makeText(this, "Yanlış! Kalan hakkınız: $guessLeft", Toast.LENGTH_SHORT).show()

                // KIRMIZI efekt
                textViewScore.setTextColor(Color.RED)
                textViewScore.postDelayed({
                    textViewScore.setTextColor(Color.BLACK)
                }, 500)

                if (guessLeft == 0) {
                    Toast.makeText(this, "Cevap: $currentWord", Toast.LENGTH_SHORT).show()
                    nextRound()
                }
            }

            editTextGuess.text.clear()
            updateUI()
        }

        // Duraklat
        buttonPause.setOnClickListener {
            countDownTimer.cancel()
            buttonPause.visibility = View.GONE
            buttonResume.visibility = View.VISIBLE
            editTextGuess.isEnabled = false
            buttonCheck.isEnabled = false
        }

        // Devam et
        buttonResume.setOnClickListener {
            resumeTimer()
            buttonResume.visibility = View.GONE
            buttonPause.visibility = View.VISIBLE
            editTextGuess.isEnabled = true
            buttonCheck.isEnabled = true
        }

        updateUI()
    }

    private fun startNewRound() {
        if (usedWords.size == wordList.size) {
            Toast.makeText(this, "Tüm kelimeler bitti! Skorunuz: $score", Toast.LENGTH_LONG).show()
            usedWords.clear()
            score = 0
        }

        do {
            currentWord = wordList.random()
        } while (usedWords.contains(currentWord))

        usedWords.add(currentWord)
        shuffledWord = currentWord.toCharArray().apply { shuffle() }.joinToString("")
        guessLeft = 3
        remainingTime = 20000
        editTextGuess.isEnabled = true
        buttonCheck.isEnabled = true

        updateUI()
        startTimer()

        // Harf kutuları
        letterContainer.removeAllViews()
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        for ((index, char) in shuffledWord.withIndex()) {
            val letterBox = TextView(this).apply {
                text = char.toString()
                textSize = 32f
                setPadding(24, 24, 24, 24)
                setBackgroundResource(R.drawable.letter_box_background)
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(12, 12, 12, 12)
                }
            }

            fadeIn.startOffset = (index * 150).toLong()
            letterBox.startAnimation(fadeIn)

            letterContainer.addView(letterBox)
        }
    }

    private fun nextRound() {
        countDownTimer.cancel()
        startNewRound()
    }

    private fun updateUI() {
        textViewHint.text = "İpucu: ${hints[currentWord]}"
        textViewScore.text = "Skor: $score"
        textViewGuessLeft.text = "Kalan Hak: $guessLeft"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                textViewTimer.text = "Süre: ${millisUntilFinished / 1000} sn"
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Süre doldu! Cevap: $currentWord", Toast.LENGTH_SHORT).show()
                nextRound()
            }
        }.start()
    }

    private fun resumeTimer() {
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        soundPool.release()
    }
}
