package dgtic.unam.ejerciciodos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import dgtic.unam.ejerciciodos.databinding.ActivityBuscarEstudianteBinding
import dgtic.unam.ejerciciodos.databinding.ActivityEliminarEstudianteBinding
import org.json.JSONArray

class EliminarEstudiante : AppCompatActivity() {
    private lateinit var binding: ActivityEliminarEstudianteBinding
    private lateinit var volleyAPI: VolleyAPI

    private val ipPuerto = "192.168.68.55:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEliminarEstudianteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        volleyAPI = VolleyAPI(this)

        binding.restjsondelete.setOnClickListener {
            studentsDelete()
        }


    }

    private fun studentsDelete() {
        val urlJSON =
            "http://" + ipPuerto + "/borrarestudiante/" + binding.searchText.text.toString()
        var cadena = ""
        val jsonRequest = object : JsonArrayRequest(
            urlJSON,
            Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materia = estudiante.getJSONArray("materias")
                    cadena += estudiante.get("cuenta").toString() + "<-"
                    (0 until materia.length()).forEach {
                        val datos = materia.getJSONObject(it)
                        cadena += datos.get("nombre").toString() + "----" + datos.get("creditos")
                            .toString() + "--"
                    }
                    cadena += "> \n"
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

            override fun getMethod(): Int {
                return Request.Method.DELETE
            }
        }
        volleyAPI.add(jsonRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


}


