package evs.factory.temperature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isVisible
import evs.factory.temperature.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener{
            loadData()
        }
    }

    fun loadData(){
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        loadCity{
            binding.tvLocation.text = it
            val temp = loadTemperature(it){
                binding.tvTemp.text = it.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }

        }

    }

    private fun loadCity(callback:(String)->Unit){
        thread {
            Thread.sleep(5000)
            runOnUiThread(){callback.invoke("Екатеринбург")}
        }
    }

    private fun loadTemperature(city:String, callback:(Int)->Unit){
        thread {
            runOnUiThread(){
                Toast.makeText(this, "Loading temp in $city", Toast.LENGTH_SHORT).show()
            }
            Thread.sleep(5000)
            runOnUiThread(){
                callback.invoke(-17)
            }

        }
    }
}