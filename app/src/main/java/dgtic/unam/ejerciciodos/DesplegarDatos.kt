package dgtic.unam.ejerciciodos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import dgtic.unam.ejerciciodos.databinding.ActivityDesplegarDatosBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class DesplegarDatos : AppCompatActivity() {

    private lateinit var binding: ActivityDesplegarDatosBinding
    private lateinit var volleyAPI: VolleyAPI

    private val ipPuerto= "192.168.68.55:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDesplegarDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        volleyAPI = VolleyAPI(this)

        binding.restjson.setOnClickListener {
            studentsJSON()
        }




    }


    private fun studentsJSON() {
        val urlJSON = "http://"+ipPuerto+"/estudiantesJSON"
        var cadena = ""
        val jsonRequest = object : JsonArrayRequest(
            urlJSON,
            Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {val estudiante = response.getJSONObject(it)
                    val materia=estudiante.getJSONArray("materias")
                    cadena += estudiante.get("cuenta").toString() + "<-"
                    (0 until materia.length()).forEach {
                        val datos=materia.getJSONObject(it)
                        cadena += datos.get("nombre").
                        toString() + "----" + datos.get("creditos").toString() + "--"
                    }
                    cadena+="> \n"
                }
                binding.outText.text = cadena
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