package com.jpdev.livestockproject.data.network

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.jpdev.livestockproject.domain.model.BreedingPerformance
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DeathDetails
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.domain.model.User
import com.jpdev.livestockproject.domain.model.Vaccine
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
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
                val userData: Map<String, Any>? =
                    snapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

                if (userData != null) {
                    val user = User(
                        userId = userData["userId"].toString().toInt() as? Int ?: 0,
                        name = userData["name"] as? String ?: "",
                        password = userData["password"] as? String ?: "",
                        phone = userData["phone"] as? String ?: "",
                        farms = userData["farms"] as? MutableList<Farm> ?: mutableListOf()
                    )
                    it.resume(user)
                }
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
                    val key = farmSnapshot.key ?: ""
                    val farmData: Map<String, Any>? =
                        farmSnapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

                    if (farmData != null) {

                        val farm = Farm(
                            nameFarm = farmData["nameFarm"] as? String ?: "",
                            hectares = farmData["hectares"].toString().toDouble() as? Double ?: 0.0,
                            numCows = farmData["numCows"].toString().toInt() as? Int ?: 0,
                            address = farmData["address"] as? String ?: "",
                            cattles = farmData["cattles"] as? MutableList<Cattle>
                                ?: mutableListOf(),
                            receipts = farmData["receipts"] as? MutableList<Receipt>
                                ?: mutableListOf()
                        )

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
        val list = mutableListOf<Pair<String, User>>()

        for (userSnapshot in snapshot.children) {
            val userKey = userSnapshot.key ?: ""

            // Intenta obtener un objeto genérico de Firebase (en este caso, un Map)
            val userData: Map<String, Any>? =
                userSnapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

            // Verifica si los datos son válidos y luego crea un objeto User
            if (userData != null) {
                val user = User(
                    userId = userData["userId"].toString().toInt() as? Int ?: 0,
                    name = userData["name"] as? String ?: "",
                    password = userData["password"] as? String ?: "",
                    phone = userData["phone"] as? String ?: "",
                    farms = userData["farms"] as? MutableList<Farm> ?: mutableListOf()
                )
                list.add(Pair(userKey, user))
            }
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
                    val farmData: Map<String, Any>? =
                        snapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

                    if (farmData != null) {
                        val farm = Farm(
                            nameFarm = farmData["nameFarm"] as? String ?: "",
                            hectares = farmData["hectares"].toString().toDouble() as? Double ?: 0.0,
                            numCows = farmData["numCows"].toString().toInt() as? Int ?: 0,
                            address = farmData["address"] as? String ?: "",
                            cattles = farmData["cattles"] as? MutableList<Cattle>
                                ?: mutableListOf(),
                            receipts = farmData["receipts"] as? MutableList<Receipt>
                                ?: mutableListOf()
                        )
                        callback(farm)
                    }

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

                val cowsKeys = mutableListOf<String>()
                val cows = mutableListOf<Cattle>()

                for (cowSnapshot in snapshot.children) {
                    // Obtiene cada finca y la convierte a la clase Farm
                    val key = cowSnapshot.key.toString()
                    val cowData: Map<String, Any>? =
                        cowSnapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

                    if (cowData != null) {
                        val cow = Cattle(
                            cattleId = cowData["cattleId"].toString().toInt() as? Int ?: 0,
                            marking = cowData["marking"] as? String ?: "",
                            birthdate = cowData["birthdate"] as? String ?: "",
                            weight = cowData["weight"].toString().toInt() as? Int ?: 0,
                            age = cowData["age"].toString().toInt() as? Int ?: 0,
                            breed = cowData["breed"] as? String ?: "",
                            state = cowData["state"] as? String ?: "",
                            gender = cowData["gender"] as? String ?: "",
                            type = cowData["type"] as? String ?: "",
                            motherMark = cowData["motherMark"] as? String ?: "",
                            fatherMark = cowData["fatherMark"] as? String ?: "",
                            cost = cowData["cost"].toString().toDouble() as? Double ?: 0.0,
                            castrated = cowData["castrated"].toString().toBoolean() as? Boolean ?: false
                        )
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

    fun registerCow(cow: Cattle, userKey: String?, farm: String?) {
        val userReference = myRef.child(userKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingUser = snapshot.getValue(User::class.java)
                    val farmKey = farm.toString().toInt()

                    val isMarkingUnique = existingUser?.farms?.get(farmKey)?.cattles
                        ?.none { it.marking == cow.marking } ?: true

                    if (isMarkingUnique) {
                        // Agregar la nueva vaca solo si la marcación es única
                        existingUser?.farms?.get(farmKey)?.cattles?.add(cow)
                        userReference.setValue(existingUser)
                    } else {
                        Log.i("Marcación duplicada", "Ya existe una vaca con la misma marcación")
                    }
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
            userReference.setValue(null)
        }
    }

    fun deleteCow(key: String?, farmKey: String?, cowKey: String?) {
        if (key != null && farmKey != null && cowKey != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        val cattleRemove =
                            existingUser?.farms?.get(farmKey.toString().toInt())?.cattles?.get(
                                cowKey.toString().toInt()
                            )
                        existingUser?.farms?.get(farmKey.toString().toInt())?.cattles?.remove(
                            cattleRemove
                        )

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })

        }

    }

    fun deleteVaccine(key: String?, farmKey: String?, cowKey: String?, vaccineKey: String?) {
        if (key != null && farmKey != null && cowKey != null && vaccineKey != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        val vaccineRemove =
                            existingUser?.farms?.get(farmKey.toString().toInt())?.cattles?.get(
                                cowKey.toString().toInt()
                            )?.vaccines?.get(vaccineKey.toString().toInt())
                        existingUser?.farms?.get(
                            farmKey.toString().toInt()
                        )?.cattles?.get(cowKey.toString().toInt())?.vaccines?.remove(vaccineRemove)

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })

        }

    }

    fun editDeath(death:DeathDetails, key: String?, farmKey: String?, cowKey: String?){
        if (key != null && farmKey != null && cowKey != null) {
            val userReference =
                myRef.child(key).child("farms").child(farmKey).child("cattles").child(cowKey)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(Cattle::class.java)

                        existingUser?.apply {
                            this.state = "Death"
                            this.death = death
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
                            this.castrated = cow.castrated
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

    fun editVaccine(
        vaccine: Vaccine,
        key: String?,
        farmKey: String?,
        cowKey: String?,
        vaccineKey: String?
    ) {
        if (key != null && farmKey != null && cowKey != null && vaccineKey != null) {
            val userReference =
                myRef.child(key)
                    .child("farms")
                    .child(farmKey)
                    .child("cattles")
                    .child(cowKey)
                    .child("vaccines")
                    .child(vaccineKey)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingVaccine = snapshot.getValue(Vaccine::class.java)

                        existingVaccine?.apply {
                            this.idVaccine = vaccine.idVaccine
                            this.vacineName = vaccine.vacineName
                            this.vaccineCost = vaccine.vaccineCost
                            this.date = vaccine.date
                            this.supplier = vaccine.supplier
                        }

                        userReference.setValue(existingVaccine)
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
                var cowData: Map<String, Any>? =
                    snapshot.getValue(object : GenericTypeIndicator<Map<String, Any>>() {})

                if (cowData != null) {
                    val cow = Cattle(
                        cattleId = cowData["cattleId"].toString().toInt() as? Int ?: 0,
                        marking = cowData["marking"] as? String ?: "",
                        birthdate = cowData["birthdate"] as? String ?: "",
                        weight = cowData["weight"].toString().toInt() as? Int ?: 0,
                        age = cowData["age"].toString().toInt() as? Int ?: 0,
                        breed = cowData["breed"] as? String ?: "",
                        state = cowData["state"] as? String ?: "",
                        gender = cowData["gender"] as? String ?: "",
                        type = cowData["type"] as? String ?: "",
                        motherMark = cowData["motherMark"] as? String ?: "",
                        fatherMark = cowData["fatherMark"] as? String ?: "",
                        cost = cowData["cost"].toString().toDouble() as? Double ?: 0.0,
                        castrated = cowData["castrated"].toString().toBoolean() as? Boolean ?: false
                    )

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

    fun getReceipt(
        user: String?,
        farmKey: String?,
        callback: (List<Receipt>?, List<String>?) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("receipts")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val receipts = mutableListOf<Receipt>()
                val receiptKey = mutableListOf<String>()

                for (receiptSnapshot in snapshot.children) {

                    val key = receiptSnapshot.key.toString()
                    val receipt = receiptSnapshot.getValue(Receipt::class.java)

                    if (receipt != null && key != null) {
                        receipts.add(receipt)
                        receiptKey.add(key)
                    }
                }
                callback(receipts, receiptKey)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(null, null)
            }
        })
    }

    fun getReceiptDetails(
        user: String?,
        farmKey: String?,
        receiptKey: String?,
        callback: (Receipt) -> Unit
    ) {
        val userReference = myRef.child(user.orEmpty()).child("farms")
            .child(farmKey.orEmpty())
            .child("receipts")
            .child(receiptKey.orEmpty())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val receipt = snapshot.getValue(Receipt::class.java)
                    callback(receipt ?: Receipt())
                } else {
                    callback(Receipt())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error en la lectura de datos: ${error.details}")
                callback(Receipt())
            }
        })
    }

    fun deleteReceipt(key: String?, farmKey: String?, receiptKey: String?) {
        if (key != null && farmKey != null && receiptKey != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        val receiptRemove =
                            existingUser?.farms?.get(farmKey.toString().toInt())?.receipts?.get(
                                receiptKey.toString().toInt()
                            )
                        existingUser?.farms?.get(farmKey.toString().toInt())?.receipts?.remove(
                            receiptRemove
                        )

                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })

        }
    }

    fun editReceipt(receipt: Receipt, key: String?, farmKey: String?, receiptKey: String?) {
        if (key != null && farmKey != null && receiptKey != null) {
            val userReference =
                myRef.child(key).child("farms").child(farmKey).child("receipts").child(receiptKey)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(Receipt::class.java)

                        existingUser?.apply {
                            this.idReceipt = receipt.idReceipt
                            this.nameReceipt = receipt.nameReceipt
                            this.amountPaid = receipt.amountPaid
                            this.date = receipt.date
                            this.receiptType = receipt.receiptType
                            this.name = receipt.name
                            this.tel = receipt.tel
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

    fun deleteReceiptAndCowByCommonName(key: String?, farmKey: String?, commonName: String?) {
        if (key != null && farmKey != null && commonName != null) {
            val userReference = myRef.child(key)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val existingUser = snapshot.getValue(User::class.java)

                        // Buscar y eliminar el recibo por nombre común
                        existingUser?.farms?.get(
                            farmKey.toString().toInt()
                        )?.receipts?.removeAll { it.nameReceipt == commonName }

                        // Buscar y eliminar la vaca por nombre común
                        val farm = existingUser?.farms?.get(farmKey.toString().toInt())
                        farm?.cattles?.removeAll { it.marking == commonName }

                        // Actualizar la referencia en la base de datos
                        userReference.setValue(existingUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Algo fallo", error.details)
                }
            })
        }
    }

    fun registerVaccine(vaccine: Vaccine, user: String?, farmKey: String?, cowKey: String?) {
        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingCattle = snapshot.getValue(Cattle::class.java)
                    val vaccineList = existingCattle?.vaccines
                    vaccineList?.add(vaccine)
                    existingCattle?.apply {
                        if (vaccineList != null) {
                            this.vaccines = vaccineList
                        }
                    }
                    userReference.setValue(existingCattle)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    fun getVaccineDetails(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        vaccineKey: String?,
        callback: (Vaccine) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("vaccines")
            .child(vaccineKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var vaccineData = snapshot.getValue(Vaccine::class.java)

                if (vaccineData != null) {
                    callback(vaccineData)
                } else {
                    callback(Vaccine())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(Vaccine())
            }
        })
    }

    fun getCowVaccines(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        callback: (List<Vaccine>?, List<String>?) -> Unit
    ) {

        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("vaccines")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val vaccineKeys = mutableListOf<String>()
                val vaccines = mutableListOf<Vaccine>()

                for (vaccineSnapshot in snapshot.children) {

                    val key = vaccineSnapshot.key.toString()
                    val vaccineData = vaccineSnapshot.getValue(Vaccine::class.java)

                    if (vaccineData != null) {
                        vaccines.add(vaccineData)
                        vaccineKeys.add(key)
                    }

                }
                callback(vaccines, vaccineKeys)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(null, null)
            }
        })
    }

    fun getTotalVaccineCostForAllCows(
        user: String?,
        farmKey: String?,
        callback: (Double) -> Unit
    ) {
        val userReference =
            myRef.child(user.toString())
                .child("farms")
                .child(farmKey.toString())
                .child("cattles")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var totalVaccineCost = 0.0
                Log.d("Debug", "Iterando sobre vacas")

                for (cowSnapshot in snapshot.children) {
                    Log.d("Debug", "Iterando sobre vaca")

                    // Cambia snapshot a cowSnapshot en el bucle interno
                    for (vaccineSnapshot in cowSnapshot.child("vaccines").children) {
                        Log.d("Debug", "Iterando sobre vacuna")
                        val vaccineData = vaccineSnapshot.getValue(Vaccine::class.java)
                        if (vaccineData != null) {
                            Log.d("Debug", "Costo de vacuna: ${vaccineData.vaccineCost}")
                            totalVaccineCost += vaccineData.vaccineCost
                        }
                    }
                }

                Log.d("Debug", "Total del costo de todas las vacunas: $totalVaccineCost")
                callback(totalVaccineCost)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(0.0)
            }
        })
    }

    fun getSumOfAmountPaidByReceiptType(
        user: String?,
        farmKey: String?,
        callback: (Double, Double) -> Unit
    ) {
        val receiptsReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())


        receiptsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalAmountPaidCompra = 0.0
                var totalAmountPaidVenta = 0.0
                Log.d("Debug", "Iterando sobre recibos")
                for (snapshot in snapshot.child("receipts").children) {
                    Log.d("Debug", "Entra al ciclo")
                    val receipt = snapshot.getValue(Receipt::class.java)
                    if (receipt != null) {
                        // Sumar al total correspondiente según el tipo del recibo
                        if (receipt.receiptType == "Compra ganado" || receipt.receiptType == "Compra producto") {
                            totalAmountPaidCompra += receipt.amountPaid
                        } else if (receipt.receiptType == "Venta ganado") {
                            totalAmountPaidVenta += receipt.amountPaid
                        }
                    }
                }
                callback(totalAmountPaidCompra, totalAmountPaidVenta)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error en la lectura de datos: ${error.details}")
                callback(0.0, 0.0)
            }
        })
    }

    fun registerNewsBreeding(
        news: BreedingPerformance,
        user: String?,
        farmKey: String?,
        cowKey: String?
    ) {
        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingCattle = snapshot.getValue(Cattle::class.java)
                    val newsList = existingCattle?.PBreeding
                    newsList?.add(news)
                    existingCattle?.apply {
                        if (newsList != null) {
                            this.PBreeding = newsList
                        }
                    }
                    userReference.setValue(existingCattle)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    fun registerNewsLifting(
        news: LiftingPerformance,
        user: String?,
        farmKey: String?,
        cowKey: String?
    ) {
        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingCattle = snapshot.getValue(Cattle::class.java)
                    val newsList = existingCattle?.PLifting
                    newsList?.add(news)
                    existingCattle?.apply {
                        if (newsList != null) {
                            this.PLifting = newsList
                        }
                    }
                    userReference.setValue(existingCattle)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
            }
        })
    }

    fun getCowNewsLifting(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        callback: (List<LiftingPerformance>?, List<String>?) -> Unit
    ) {

        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("plifting")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val keys = mutableListOf<String>()
                val NewsList = mutableListOf<LiftingPerformance>()

                for (pliftingSnapshot in snapshot.children) {

                    val key = pliftingSnapshot.key.toString()
                    val NewsData = pliftingSnapshot.getValue(LiftingPerformance::class.java)

                    if (NewsData != null) {
                        NewsList.add(NewsData)
                        keys.add(key)
                    }

                }
                callback(NewsList, keys)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(null, null)
            }
        })
    }

    fun getCowNewsBreeding(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        callback: (List<BreedingPerformance>?, List<String>?) -> Unit
    ) {

        val userReference = myRef.child(user.toString())
            .child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("pbreeding")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val keys = mutableListOf<String>()
                val NewsList = mutableListOf<BreedingPerformance>()

                for (pbreedingSnapshot in snapshot.children) {

                    val key = pbreedingSnapshot.key.toString()
                    val NewsData = pbreedingSnapshot.getValue(BreedingPerformance::class.java)

                    if (NewsData != null) {
                        NewsList.add(NewsData)
                        keys.add(key)
                    }

                }
                callback(NewsList, keys)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(null, null)
            }
        })
    }

    fun getNewsBreedingDetails(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        keyBreeding: String?,
        callback: (BreedingPerformance) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("pbreeding")
            .child(keyBreeding.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var NewsData = snapshot.getValue(BreedingPerformance::class.java)

                if (NewsData != null) {
                    callback(NewsData)
                } else {
                    callback(BreedingPerformance())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(BreedingPerformance())
            }
        })
    }

    fun getNewsLiftingDetails(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        keyLifting: String?,
        callback: (LiftingPerformance) -> Unit
    ) {

        val userReference = myRef.child(user.toString()).child("farms")
            .child(farmKey.toString())
            .child("cattles")
            .child(cowKey.toString())
            .child("plifting")
            .child(keyLifting.toString())

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var NewsData = snapshot.getValue(LiftingPerformance::class.java)

                if (NewsData != null) {
                    callback(NewsData)
                } else {
                    callback(LiftingPerformance())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo", error.details)
                callback(LiftingPerformance())
            }
        })
    }

}