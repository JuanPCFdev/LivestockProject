package com.jpdev.livestockproject.ui.Cow.Death

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityNotifyDeathCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.DeathDetails
import com.jpdev.livestockproject.ui.Cow.Breeding.EditDelete.EditDeleteBreedingCowActivity
import com.jpdev.livestockproject.ui.Cow.Corral.CorralActivity
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.EditDelete.EditDeleteLiftingActivity
import java.util.Calendar

class NotifyDeathCow : AppCompatActivity() {
    private lateinit var binding: ActivityNotifyDeathCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyDeathCowBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user, farmKey, cowKey)
        insertButtom(user, farmKey, cowKey)
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?){
        binding.btnConfirmDeath.setOnClickListener {
            saveChangeCowDeath(user,farmKey,cowKey)
        }
        binding.etDeathDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Nacimiento")
    }
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etDeathDate.setText("$day/$mes/$year")

    }
    private fun saveChangeCowDeath(user: String?, farmKey: String?, cowKey: String?) {

        val deathCow = DeathDetails(
            deathDate = binding.etDeathDate.text.toString(),
            deathCause = binding.etDeathCause.text.toString(),
            deathDescription = binding.etDeathDescription.text.toString()
        )

        firebaseInstance.editDeath(deathCow, user, farmKey, cowKey)

        firebaseInstance.getCowDetails(user, farmKey, cowKey) {
            val updatedCow = Cattle(
                0,
                it.marking,
                it.birthdate,
                it.weight,
                it.age,
                it.breed,
                "Muerta",
                it.gender,
                it.type,
                it.motherMark,
                it.fatherMark,
                it.cost,
                it.castrated
            )

            firebaseInstance.editCow(updatedCow, user, farmKey, cowKey)

        }



        Toast.makeText(this, "Se ha notificado muerte", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomeCowActivity::class.java)
        intent.putExtra("userKey", user)
        intent.putExtra("farmKey", farmKey)
        startActivity(intent)
        finish()
    }

    private fun insertButtom(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {

            when (it.type) {
                "Lifting" -> {
                    binding.viewToolBar.back.setOnClickListener {
                        val intent = Intent(this, EditDeleteLiftingActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        startActivity(intent)
                        finish()
                    }

                }

                "Breeding" -> {
                    binding.viewToolBar.back.setOnClickListener {
                        val intent = Intent(this, EditDeleteBreedingCowActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        startActivity(intent)
                        finish()
                    }
                }

                "corral" -> {
                    binding.viewToolBar.back.setOnClickListener {
                        val intent = Intent(this, CorralActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        startActivity(intent)
                        finish()
                    }

                }

            }
        }
    }

}