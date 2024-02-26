package com.jpdev.livestockproject.ui.Cow.Consult

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowResumeBinding
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Cow.Breeding.EditDelete.EditDeleteBreedingCowActivity
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.EditDelete.EditDeleteLiftingActivity

class CowResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCowResumeBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var vaccineList = mutableListOf<Vaccine>()
    private var vaccineKeys = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCowResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cow = intent.extras?.getString("cowKey")

        initListeners(user, farm, cow)
        printInfo(user, farm, cow)
        showCow(user, farm, cow)
        insertButtom(user, farm, cow)
    }

    private fun printInfo(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.resume_cow) + " ${it.marking}"
            binding.tvTitle.text = details
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.btnBack.setOnClickListener {
            goToHome(user, farm)
        }
        binding.btnConsultVaccine.setOnClickListener {
            showVaccine(user, farm, cow)
        }
    }

    private fun showCow(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {

            when (it.type) {
                "Lifting" -> {
                    val descripcion =
                        "Marcacion: ${it.marking}\n" +
                                "Peso: ${it.weight}\n" +
                                "Fecha nacimiento: ${it.birthdate}\n" +
                                "Raza: ${it.breed}\n" +
                                "Precio de compra: ${it.cost}\n" +
                                "Genero: ${it.gender}\n" +
                                "Estado: ${it.state}\n"
                    binding.informationcow.text = descripcion
                }

                "Breeding" -> {
                    val descripcion =
                        "Marcacion: ${it.marking}\n" +
                                "Peso: ${it.weight}\n" +
                                "Fecha nacimiento: ${it.birthdate}\n" +
                                "Raza: ${it.breed}\n" +
                                "Genero: ${it.gender}\n" +
                                "Estado: ${it.state}\n" +
                                "Madre: ${it.motherMark}\n" +
                                "Padre: ${it.fatherMark}"
                    binding.informationcow.text = descripcion
                }

                "corral" -> {
                    val descripcion =
                        "Marcacion: ${it.marking}\n" +
                                "Peso: ${it.weight}\n" +
                                "Fecha nacimiento: ${it.birthdate}\n" +
                                "Raza: ${it.breed}\n" +
                                "Genero: ${it.gender}\n" +
                                "Estado: ${it.state}\n" +
                                "Madre: ${it.motherMark}\n"
                    binding.informationcow.text = descripcion
                }
            }
        }
    }

    private fun insertButtom(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {

            when (it.type) {
                "Lifting" -> {

                    val btnEdit = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnEdit.text = "Editar Informacion"
                    btnEdit.setOnClickListener {
                        val intent = Intent(this, EditDeleteLiftingActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                    }
                    binding.lledit.addView(btnEdit)

                    val btnWeight = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnWeight.text = "Consultar Pesaje"
                    btnWeight.setOnClickListener {
                        val intent = Intent(this, CowDetailsLiftingActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                    }
                    binding.lledit.addView(btnWeight)

                }

                "Breeding" -> {
                    val btnEdit = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnEdit.text = "Editar Informacion"
                    btnEdit.setOnClickListener {
                        val intent = Intent(this, EditDeleteBreedingCowActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                    }
                    binding.lledit.addView(btnEdit)

                    val btnParto = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnParto.text = "Consultar partos"
                    btnParto.setOnClickListener {
                        val intent = Intent(this, CowDetailsBreedingActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                    }
                    binding.lledit.addView(btnParto)

                    val btnMonta = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnMonta.text = "Consultar Montas"
                    btnMonta.setOnClickListener {
                        val intent = Intent(this, HomeCowActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                    }
                    binding.lledit.addView(btnMonta)
                }

            }
        }
    }


    private fun showVaccine(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowVaccines(
            user.toString(),
            farm.toString(),
            cow.toString()
        ) { vaccines, keys ->
            if (vaccines != null && vaccines.isNotEmpty()) {
                vaccines?.let {
                    vaccineList.clear()
                    vaccineList.addAll(vaccines)
                    keys?.let {
                        vaccineKeys.clear()
                        vaccineKeys.addAll(keys)

                        // Obtener solo los nombres de las vacunas
                        val vaccineNames = vaccines.map { it.vacineName }

                        // Unir los nombres en una cadena con saltos de línea
                        val vaccineText = vaccineNames.joinToString("\n")
                        showInfo(vaccineText)
                    }

                }
            } else {
                val vaccineText = "No hay vacunas registradas"
                showInfo(vaccineText)
            }
        }

    }

    private fun showInfo(vaccine: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Información sobre vacunas")
        builder.setMessage(
            vaccine
        )

        builder.setPositiveButton("Volver") { _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun goToHome(user: String?, farmKey: String?) {
        val intent = Intent(this, HomeCowActivity::class.java)
        intent.putExtra("userKey", user)
        intent.putExtra("farmKey", farmKey)
        startActivity(intent)
        finish()
    }
}

