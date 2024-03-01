package com.jpdev.livestockproject.ui.Cow.Death

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.Toast
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityNotifyDeathCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.DeathDetails
import com.jpdev.livestockproject.ui.Cow.Breeding.EditDelete.EditDeleteBreedingCowActivity
import com.jpdev.livestockproject.ui.Cow.Consult.CowDetailsBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Consult.CowDetailsLiftingActivity
import com.jpdev.livestockproject.ui.Cow.Corral.CorralActivity
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.EditDelete.EditDeleteLiftingActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity
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
//        binding.btnBack.setOnClickListener {
//            val intent = Intent(this,EditDeleteLiftingActivity::class.java)
//            intent.putExtra("userKey", user)
//            intent.putExtra("farmKey", farmKey)
//            intent.putExtra("cowKey",cowKey)
//            startActivity(intent)
//            finish()
//        }
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

        val birthDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }

    }
    private fun saveChangeCowDeath(user: String?, farmKey: String?, cowKey: String?) {

        val deathCow = DeathDetails(
            deathDate = binding.etDeathDate.toString(),
            deathCause = binding.etDeathCause.toString(),
            deathDescription = binding.etDeathDescription.toString()
        )

        firebaseInstance.editDeath(deathCow, user, farmKey, cowKey)

        Toast.makeText(this, "Se ha notificado muerte", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("userKey", user)
        intent.putExtra("farmKey", farmKey)
        startActivity(intent)
        finish()
    }

    private fun insertButtom(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {

            when (it.type) {
                "Lifting" -> {
                    val btnBack = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnBack.text = "Volver"
                    btnBack.setOnClickListener {
                        val intent = Intent(this, EditDeleteLiftingActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                        this.finish()
                    }
                    binding.lledit.addView(btnBack)
                }

                "Breeding" -> {
                    val btnBack = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnBack.text = "Volver"
                    btnBack.setOnClickListener {
                        val intent = Intent(this, EditDeleteBreedingCowActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                        this.finish()
                    }
                    binding.lledit.addView(btnBack)
                }

                "corral" -> {
                    val btnBack = Button(ContextThemeWrapper(this, R.style.ButtonStyle))
                    btnBack.text = "Volver"
                    btnBack.setOnClickListener {
                        val intent = Intent(this, CorralActivity::class.java)
                        intent.putExtra("userKey", user)
                        intent.putExtra("farmKey", farm)
                        intent.putExtra("cowKey", cow)
                        this.startActivity(intent)
                        this.finish()
                    }
                    binding.lledit.addView(btnBack)
                }

            }
        }
    }

}