package evs.factory.temperature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import evs.factory.temperature.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener{
            //scope имеет жизнынный цикл как у активити
            lifecycleScope.launch {
                loadData()
            }
        }
    }

    private suspend fun loadData(){
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()

        binding.tvLocation.text = city
        val temp = loadTemperature(city)

        binding.tvTemp.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonLoad.isEnabled = true
    }

    private suspend fun loadCity():String{
        delay(5000)
        return "Екатеринбург"
    }

    private suspend fun loadTemperature(city:String):Int{
        Toast.makeText(this, "Loading temp in $city", Toast.LENGTH_SHORT).show()
        delay(5000)
        return -17

    }


    //Демо

    private fun loadCityWithoutCoroutine(callback: (String)->Unit){
        thread{
            Thread.sleep(5000)
            runOnUiThread {
                callback.invoke("Екатеринбург")
            }
        }
    }

    private fun loadTemperatureWithoutCoroutine(city:String, callback: (Int) -> Unit){
        thread{
            runOnUiThread { Toast.makeText(this, "Loading temp in $city", Toast.LENGTH_SHORT).show() }
            Thread.sleep(5000)
            runOnUiThread { callback.invoke(-17)}
        }
    }
    private fun loadWithoutCoroutine(step: Int = 0, obj:Any? = null){
        when(step){
            0->{
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithoutCoroutine{
                    loadWithoutCoroutine(1, it)
                }
            }
            1->{
                val city = obj as String
                binding.tvLocation.text = city
                loadTemperatureWithoutCoroutine(city) {
                    loadWithoutCoroutine(2,it)
                }
            }
            2->{
                val temp = obj as Int
                binding.tvTemp.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }


}