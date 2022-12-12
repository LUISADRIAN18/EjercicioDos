package dgtic.unam.ejerciciodos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import dgtic.unam.ejerciciodos.databinding.ActivityDesplegarDatosBinding
import dgtic.unam.ejerciciodos.databinding.ActivityIngresarDatosBinding
import org.json.JSONArray
import org.json.JSONObject

class IngresarDatos : AppCompatActivity() {
    private lateinit var binding: ActivityIngresarDatosBinding
    private lateinit var volleyAPI: VolleyAPI

    private val ipPuerto= "192.168.68.55:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngresarDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        volleyAPI = VolleyAPI(this)
        binding.restjsonadd.setOnClickListener {

            if (binding.cuenta.text.isEmpty()or binding.nombre.text.isEmpty()or binding.edad.text.isEmpty()or binding.idMat.text.isEmpty()or
                binding.nombreMat.text.isEmpty()or binding.creditosMat.text.isEmpty()){

                binding.cuenta.error = getString(R.string.valor_requerido)
                binding.nombre.error = getString(R.string.valor_requerido)
                binding.edad.error = getString(R.string.valor_requerido)
                binding.idMat.error = getString(R.string.valor_requerido)
                binding.nombreMat.error = getString(R.string.valor_requerido)
                binding.creditosMat.error = getString(R.string.valor_requerido)




            }else{

                studentAdd()



            }





        }


    }


    private fun studentAdd(){
        val urlJSON = "http://" + ipPuerto + "/agregarestudiante"
        var cadena=""
        val jsonRequest=object : JsonArrayRequest(
            urlJSON, Response.Listener <JSONArray>{ response->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materias= estudiante.getJSONArray("materias")
                    cadena+=estudiante.get("cuenta").toString()+ "<"
                    (0 until materias.length()).forEach {
                        val datos=materias.getJSONObject(it)
                        cadena+=datos.get("nombre").toString()+"**"+datos.get("creditos").toString()+"---"
                    }
                    cadena+="> \n"
                }


            },
            Response.ErrorListener {
                println(it.localizedMessage)
                Toast.makeText(this,R.string.error, Toast.LENGTH_LONG ).show()

            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }

            override fun getBody(): ByteArray {
                val estudiante = JSONObject()
                estudiante.put("cuenta",binding.cuenta.text)
                estudiante.put("nombre",binding.nombre.text)
                estudiante.put("edad",binding.edad.text)
                val materias= JSONArray()
                val itemMaterias= JSONObject()
                itemMaterias.put("id",binding.idMat.text)
                itemMaterias.put("nombre",binding.nombreMat.text)
                itemMaterias.put("creditos",binding.creditosMat.text)
                materias.put(itemMaterias)
                estudiante.put("materias",materias)

                return estudiante.toString().toByteArray(charset= Charsets.UTF_8)

            }


            override fun getMethod(): Int {
                return Method.POST

            }

        }
        volleyAPI.add(jsonRequest)



    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
