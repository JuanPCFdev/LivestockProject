package com.jpdev.livestockproject.ui.Cow.Consult

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowResumeBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Cow.Breeding.Consult.ConsultCowBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Breeding.EditDelete.EditDeleteBreedingCowActivity
import com.jpdev.livestockproject.ui.Cow.Corral.CorralActivity
import com.jpdev.livestockproject.ui.Cow.Death.DeathActivity
import com.jpdev.livestockproject.ui.Cow.Death.NotifyDeathCow
import com.jpdev.livestockproject.ui.Cow.Lifting.Consult.ConsultCowLiftingActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.EditDelete.EditDeleteLiftingActivity
import com.jpdev.livestockproject.ui.Cow.Monta.ConsultMontaActivity
import com.jpdev.livestockproject.ui.Cow.Sold.SoldActivity
import java.text.DecimalFormat

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
        insertButton(user, farm, cow)
    }

    private fun printInfo(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.resume_cow) + " ${it.marking}"
            binding.tvTitle.text = details
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnConsultVaccine.setOnClickListener {
            showVaccine(user, farm, cow)
        }
    }

    private fun showCow(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {
            val castrado = if (it.castrated) {
                "Si"
            } else {
                "No"
            }
            when (it.type) {
                "Lifting" -> {
                    val descripcion =
                        "Marcacion: ${it.marking}\n" +
                                "Peso: ${it.weight}\n" +
                                "Fecha nacimiento: ${it.birthdate}\n" +
                                "Raza: ${it.breed}\n" +
                                "Precio de compra: ${it.cost}\n" +
                                "Genero: ${it.gender}\n" +
                                "Estado: ${it.state}\n" +
                                "Castrado:${castrado}"
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

    private fun buttonsForLifting(user: String?, farm: String?, cow: String?){

        val btnEdit = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnEdit.text = "Editar Informacion"
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDeleteLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
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
            this.finish()
        }
        binding.lledit.addView(btnWeight)
    }

    private fun buttonsForBreeding(user: String?, farm: String?, cow: String?){

        val btnEditCowBreeding = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnEditCowBreeding.text = "Editar Informacion"
        btnEditCowBreeding.setOnClickListener {
            val intent = Intent(this, EditDeleteBreedingCowActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnEditCowBreeding)

        val btnParto = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnParto.text = "Consultar partos"
        btnParto.setOnClickListener {
            val intent = Intent(this, CowDetailsBreedingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnParto)

        val btnMonta = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnMonta.text = "Consultar Montas"
        btnMonta.setOnClickListener {
            val intent = Intent(this, ConsultMontaActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnMonta)

        val btnWeight = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnWeight.text = "Consultar Pesaje"
        btnWeight.setOnClickListener {
            val intent = Intent(this, CowDetailsLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnWeight)

    }

    private fun insertButton(user: String?, farm: String?, cow: String?) {

        firebaseInstance.getCowDetails(user, farm, cow) {

            val auxCattle = it

            when (it.type) {

                "Lifting" -> {
                    if (!(it.state == "vendido" || it.state == "Muerta")) {

                        buttonsForLifting(user,farm,cow)

                    } else {

                        when (it.state) {
                            "vendido" -> {
                                soldCow(user, farm, auxCattle)
                            }

                            "Muerta" -> {
                                deadCow(user, farm, cow)
                            }
                        }
                    }
                }

                "Breeding" -> {
                    if (!(it.state == "vendido" || it.state == "Muerta")) {

                        buttonsForBreeding(user,farm,cow)

                    } else {

                        when (it.state) {
                            "vendido" -> {
                                soldCow(user, farm, auxCattle)
                            }

                            "Muerta" -> {
                                deadCow(user, farm, cow)
                            }
                        }

                    }

                }

                "corral" -> {
                    if (!(it.state == "vendido" || it.state == "Muerta")) {

                        buttonsForCorral(user,farm,cow)

                    } else {

                        when (it.state) {
                            "vendido" -> {
                                soldCow(user, farm, auxCattle)
                            }

                            "Muerta" -> {
                                deadCow(user, farm, cow)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun buttonsForCorral(user: String?, farm: String?, cow: String?){

        val btnWeight = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnWeight.text = "Consultar Pesaje"
        btnWeight.setOnClickListener {
            val intent = Intent(this, CowDetailsLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnWeight)

        val btnDeath = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnDeath.text = "Notificar Muerte"
        btnDeath.setOnClickListener {
            val intent = Intent(this, NotifyDeathCow::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow)
            this.startActivity(intent)
            this.finish()
        }
        binding.lledit.addView(btnDeath)

        val btnSetLifting = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnSetLifting.text = "Asignar como Levante"
        btnSetLifting.setOnClickListener {
            setLifting(user, farm, cow)
        }
        binding.lledit.addView(btnSetLifting)

        val btnSetBreeding = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnSetBreeding.text = "Asignar como Cria"
        btnSetBreeding.setOnClickListener {
            setBreeding(user, farm, cow)
        }
        binding.lledit.addView(btnSetBreeding)

    }

    private fun setLifting(user: String?, farm: String?, cow: String?) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Cambiar Proposito")
        builder.setMessage("¿Desea asignar el proposito de levante a esta vaca?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.editType(1, user, farm, cow)
            Toast.makeText(
                this@CowResumeActivity,
                "Vaca cambiada a proposito de levante",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun setBreeding(user: String?, farm: String?, cow: String?) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Cambiar Proposito")
        builder.setMessage("¿Desea asignar el proposito de cría a esta vaca?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.editType(2, user, farm, cow)
            Toast.makeText(
                this@CowResumeActivity,
                "Vaca cambiada a proposito de cría",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showVaccine(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowVaccines(
            user.toString(),
            farm.toString(),
            cow.toString()
        ) { vaccines, keys ->
            if (!vaccines.isNullOrEmpty()) {
                vaccines.let {
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

    private fun showDeathInfo(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDeathDetails(
            user.toString(),
            farm.toString(),
            cow.toString()
        ) { deathCowDetails ->
            deathCowDetails.let {
                val deathText = "Causa de muerte : ${it.deathCause}.\n" +
                        "Fecha de la muerte : ${it.deathDate}.\n" +
                        "Descripción : ${it.deathDescription}.\n"

                showInfoDetails(deathText, true)
            }
        }
    }

    private fun showSellInfo(user: String?, farm: String?, cow: Cattle) {
        firebaseInstance.getFarmReceips(user.toString(), farm.toString()) { receipts ->
            if (receipts.isNotEmpty()) {
                val receiptOfCow = receipts.find { it.nameReceipt.contains(cow.marking) }
                if (receiptOfCow != null) {
                    val formatter = DecimalFormat("#,###")
                    val formattedVenta = formatter.format(receiptOfCow.amountPaid)
                    val textReceipt =
                        "Fecha de la venta : ${receiptOfCow.date}.\nValor de la venta : ${formattedVenta}.\nComprador : ${receiptOfCow.name}.\nTelefono Comprador : ${receiptOfCow.tel}.\n"
                    showInfoDetails(textReceipt, false)
                }
            }
        }
    }

    private fun showInfoDetails(text: String, type: Boolean) {
        val builder = AlertDialog.Builder(this)

        if (type) {
            builder.setTitle("Informacion de Muerte")
        } else {
            builder.setTitle("Informacion de Venta")
        }

        builder.setMessage(
            text
        )

        builder.setPositiveButton("Volver") { _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
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

    private fun soldCow(user: String?, farm: String?, auxCattle: Cattle) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this, SoldActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            startActivity(intent)
            finish()
        }
        val btnSellDetails =
            Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnSellDetails.text = "Consultar Venta"
        btnSellDetails.setOnClickListener {
            showSellInfo(user, farm, auxCattle)
        }
        binding.lledit.addView(btnSellDetails)
    }

    private fun deadCow(user: String?, farm: String?, cow: String?) {
        val btnDeathDetails =
            Button(ContextThemeWrapper(this, R.style.ButtonStyle))
        btnDeathDetails.text = "Consultar Muerte"
        btnDeathDetails.setOnClickListener {
            showDeathInfo(user, farm, cow)
        }
        binding.lledit.addView(btnDeathDetails)
    }
}