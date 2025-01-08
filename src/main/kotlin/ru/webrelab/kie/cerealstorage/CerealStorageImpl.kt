package ru.webrelab.kie.cerealstorage

import kotlin.math.abs

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()
    override fun addCereal(cereal: Cereal, amount: Float): Float {
        checkIfAmountIsNegative(amount)

        if ((storageCapacity - storage.size * containerCapacity) < containerCapacity) {
            throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
        }

        if (storage.containsKey(cereal)) {
            storage[cereal] = storage[cereal]!! + amount

            if (storage[cereal]!! > containerCapacity) {
                val remaining = storage[cereal]!! - containerCapacity
                storage[cereal] = containerCapacity
                return remaining
            }

            return 0f

        } else {
            return if (amount > containerCapacity) {
                storage[cereal] = containerCapacity

                amount - containerCapacity
            } else {
                storage[cereal] = amount

                0f
            }
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        checkIfAmountIsNegative(amount)

        return if (storage.containsKey(cereal)) {
            storage[cereal] = storage[cereal]!! - amount

            if (storage[cereal]!! >= 0) {
                amount
            } else {
                val remaining = abs(storage[cereal]!!)

                storage[cereal] = 0f

                remaining
            }
        } else {
            0f
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        return if (storage[cereal] == 0f) {
            storage.remove(cereal)

            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return if (storage.containsKey(cereal)) {
            storage[cereal]!!
        } else {
            0f
        }
    }

    override fun getSpace(cereal: Cereal): Float {
        return if (storage.containsKey(cereal)) {
            containerCapacity - storage[cereal]!!
        } else {
            0f
        }
    }

    override fun toString(): String {
        var storageDescription = "Вместимость хранилища=$storageCapacity,\n" +
                "Вместимость контейнера=$containerCapacity,\n"

        for ((cereal, amount) in storage) {
            storageDescription += "Крупа - ${cereal.local}, Количество - $amount\n"
        }

        return storageDescription
    }

    private fun checkIfAmountIsNegative(amount: Float) {
        if (amount < 0) throw IllegalArgumentException("Передано отрицательное значение крупы")
    }
}