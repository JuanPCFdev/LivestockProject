package com.jpdev.livestockproject.data.network

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.domain.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseInstance(context: Context) {

    private val database = Firebase.database
    private val myRef = database.reference

    init {
        FirebaseApp.initializeApp(context)
    }

    //Metodos para escribir un objeto en la base de datos
    fun writeOnFirebase(user: User) {
        val newUser = myRef.push()
        newUser.setValue(user)
    }

    // Método para registrar una finca
    fun registerFarm(farm: Farm, key: String?) {
        val userReference = myRef.child(key.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingUser = snapshot.getValue(User::class.java)
                    existingUser?.farms?.add(farm)
                    userReference.setValue(existingUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    fun editFarm(farm: Farm, key: String?, farmKey: String?) {
        if (key != null && farmKey != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        existingUser?.farms?.get(farmKey.toInt())?.apply {
                            this.nameFarm = farm.nameFarm
                            this.hectares = farm.hectares
                            this.numCows = farm.numCows
                            this.address = farm.address
                        }

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })
        }
    }

    fun deleteFarm(key: String?, farmKey: String?) {
        if (key != null && farmKey != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        // Eliminar la finca con la farmKey específica
                        existingUser?.farms?.removeAt(farmKey.toInt())

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })
        }
    }

    suspend fun getUser(key: String?): User? = suspendCancellableCoroutine {
        val userRef = myRef.child(key.toString())

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                it.resume(user)
            }

            override fun onCancelled(error: DatabaseError) {
                it.resumeWithException(error.toException())
            }

        }

        userRef.addListenerForSingleValueEvent(valueEventListener)

        it.invokeOnCancellation {
            userRef.removeEventListener(valueEventListener)
        }
    }

    fun getUserFarms(key: String?, callback: (List<Farm>?, List<String>?) -> Unit) {
        // Crea una referencia al nodo "farms" dentro del usuario identificado por "key"
        val userReference = myRef.child(key.toString()).child("farms")

        // Agrega un listener que se ejecutará una vez para leer los datos
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            // Este método se llama cuando los datos se leen exitosamente
            override fun onDataChange(snapshot: DataSnapshot) {
                // Crea una lista mutable para almacenar las fincas del usuario
                val farms = mutableListOf<Farm>()
                val farmKeys = mutableListOf<String>()

                // Itera sobre los hijos (fincas) del nodo "farms"
                for (farmSnapshot in snapshot.children) {
                    // Obtiene cada finca y la convierte a la clase Farm
                    val key = farmSnapshot.key.toString()
                    val farm = farmSnapshot.getValue(Farm::class.java)

                    if (farm != null && key != null) {
                        farms.add(farm)
                        farmKeys.add(key)
                    }
                }

                // Llama al callback proporcionando la lista de fincas al código que lo llamó
                callback(farms, farmKeys)
            }

            // Este método se llama si la operación se cancela, por ejemplo, debido a un error
            override fun onCancelled(error: DatabaseError) {
                // Imprime un mensaje de error en los detalles del error
                Log.i("Algo fallo", error.details)

                // Llama al callback con un valor nulo para indicar que hubo un error
                callback(null, null)
            }
        })
    }

    fun setupDatabaseListener(postListener: ValueEventListener) {
        database.reference.addValueEventListener(postListener)
    }

    fun getCleanSnapshot(snapshot: DataSnapshot): List<Pair<String, User>> {
        val list = snapshot.children.map { user ->
            Pair(user.key!!, user.getValue(User::class.java)!!)
        }
        return list
    }

    fun getFarmDetails(userKey: String, farmKey: String, callback: (Farm?) -> Unit) {
        val userRef = myRef.child(userKey).child("farms").child(farmKey)
        // Agrega un listener para obtener los detalles de la granja
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Verifica si existe la información de la granja
                if (snapshot.exists()) {
                    val farm: Farm? = snapshot.getValue(Farm::class.java)
                    callback(farm)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar la cancelación, por ejemplo, mostrando un mensaje de error
                callback(null)
            }
        })
    }

    fun getUserCows(
        user: String?,
        farmKey: String?,
        callback: (List<Cattle>?, List<String>?) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("cattles")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val cows = mutableListOf<Cattle>()
                val cowsKeys = mutableListOf<String>()

                for (cowSnapshot in snapshot.children) {
                    // Obtiene cada finca y la convierte a la clase Farm
                    val key = cowSnapshot.key.toString()
                    val cow = cowSnapshot.getValue(Cattle::class.java)

                    if (cow != null && key != null) {
                        cows.add(cow)
                        cowsKeys.add(key)
                    }
                }
                callback(cows, cowsKeys)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(null, null)
            }
        })
    }

    fun registerCow(cow: Cattle, user: String?, farm: String?) {
        val userReference = myRef.child(user.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingUser = snapshot.getValue(User::class.java)
                    existingUser?.farms?.get(farm.toString().toInt())?.cattles?.add(cow)
                    userReference.setValue(existingUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    //metodo para registrar un recibo de compra
    fun registerReceiptBuy(receipt: Receipt, user: String?, farm: String?) {
        val userReference = myRef.child(user.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingUser = snapshot.getValue(User::class.java)
                    existingUser?.farms?.get(farm.toString().toInt())?.receipts?.add(receipt)
                    userReference.setValue(existingUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    fun registerCowAndReceipt(cow: Cattle, receipt: Receipt, user: String?, farm: String?) {
        val userReference = myRef.child(user.toString())

        userReference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val existingUser = mutableData.getValue(User::class.java)

                if (existingUser != null) {
                    existingUser.farms?.get(farm.toString().toInt())?.receipts?.add(receipt)

                    existingUser.farms?.get(farm.toString().toInt())?.cattles?.add(cow)

                    mutableData.value = existingUser
                }

                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                dataSnapshot: DataSnapshot?
            ) {
                if (databaseError != null || !committed) {
                    Log.e("FirebaseTransaction", "Error: ${databaseError?.message}")
                } else {
                    Log.d("FirebaseTransaction", "Transacción completada con éxito")
                }
            }
        })
    }

    fun editUser(user: User, key: String?) {
        if (key != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        existingUser?.apply {
                            this.name = user.name
                            this.password = user.password
                            this.phone = user.phone
                        }

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })
        }
    }

    fun deleteUser(key: String?) {
        if (key != null) {
            val userReference = myRef.child(key)

            userReference.removeValue()

        }
    }

    fun deleteCow(key: String?, farmKey: String?, cowKey: String?) {
        if (key != null && farmKey != null && cowKey != null) {
            val userReference =
                myRef.child(key).child("farms").child(farmKey).child("cattles").child(cowKey)

            userReference.removeValue()

        }
    }

    fun editCow(cow: Cattle, key: String?, farmKey: String?, cowKey: String?) {
        if (key != null && farmKey != null && cowKey != null) {
            val userReference =
                myRef.child(key).child("farms").child(farmKey).child("cattles").child(cowKey)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(Cattle::class.java)

                        existingUser?.apply {
                            this.marking = cow.marking
                            this.birthdate = cow.birthdate
                            this.weight = cow.weight
                            this.age = cow.age
                            this.breed = cow.breed
                            this.state = cow.state
                            this.gender = cow.gender
                            this.type = cow.type
                            this.motherMark = cow.motherMark
                            this.fatherMark = cow.fatherMark
                            this.cost = cow.cost
                        }

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })
        }
    }

    fun getCowDetails(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        callback: (Cattle) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var cow = snapshot.getValue(Cattle::class.java)

                if (cow != null) {
                    callback(cow)
                } else {
                    callback(Cattle())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(Cattle())
            }
        })
    }

}