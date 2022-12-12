package dgtic.unam.ejerciciodos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import dgtic.unam.ejerciciodos.databinding.ActivityBuscarEstudianteBinding
import dgtic.unam.ejerciciodos.databinding.ActivityIngresarDatosBinding
import org.json.JSONObject

class BuscarEstudiante : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarEstudianteBinding
    private lateinit var volleyAPI: VolleyAPI

    private val ipPuerto= "192.168.68.55:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarEstudianteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        volleyAPI = VolleyAPI(this)

        binding.restjsonid.setOnClickListener {
            studentsID()
        }
    }

    private fun studentsID(){
        val urlJSON = "http://"+ipPuerto+"/id/"+binding.searchText.text.toString()
        println(urlJSON)
        var cadena = ""
        val jsonRequest = object : JsonObjectRequest(
            Method.GET,
            urlJSON,
            null,
            Response.Listener<JSONObject> { response ->
                binding.outText.text = response.get("cuenta")
                    .toString() + "----" + response.get("nombre").toString() + "-"+response.get("edad") +"\n"
            },
            Response.ErrorListener {
                binding.outText.text = getString(R.string.error)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
        }
        volleyAPI.add(jsonRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


}