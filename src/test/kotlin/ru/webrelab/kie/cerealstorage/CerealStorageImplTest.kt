package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(2f, 1f)
        }
    }

    @Test
    fun `addCereal should throw if adding negative amount of cereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -1f)
        }
    }

    @Test
    fun `addCereal should throw if there is no storage capacity for new container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, 10f)
            storage.addCereal(Cereal.BULGUR, 5f)
            storage.addCereal(Cereal.PEAS, 5f)
        }
    }

    @Test
    fun `addCereal should return overflow if containerCapacity is exceeded if using new container`() {
        val remaining = storage.addCereal(Cereal.BUCKWHEAT, 30f)

        assertEquals(20f, remaining, 0.01f)
    }

    @Test
    fun `addCereal should return overflow if containerCapacity is exceeded if using existing container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 6f)
        val remaining = storage.addCereal(Cereal.BUCKWHEAT, 10f)

        assertEquals(6f, remaining, 0.01f)
    }

    @Test
    fun `getCereal should throw if requesting negative amount of cereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.PEAS, -1f)
        }
    }

    @Test
    fun `getCereal should return amount of received cereal if requested less than available`() {
        storage.addCereal(Cereal.BULGUR, 10f)

        val receivedCereal = storage.getCereal(Cereal.BULGUR, 5f)

        assertEquals(5f, receivedCereal, 0.01f)
    }

    @Test
    fun `getCereal should return amount of unreceived cereal if requested more than available`() {
        storage.addCereal(Cereal.MILLET, 5f)

        val receivedCereal = storage.getCereal(Cereal.MILLET, 15f)

        assertEquals(10f, receivedCereal, 0.01f)
    }

    @Test
    fun `getCereal should return amount of received cereal if requested amount equals to available`() {
        storage.addCereal(Cereal.MILLET, 5f)

        val receivedCereal = storage.getCereal(Cereal.MILLET, 5f)

        assertEquals(5f, receivedCereal)
    }

    @Test
    fun `removeContainer should remove empty container`() {
        storage.addCereal(Cereal.MILLET, 5f)
        storage.getCereal(Cereal.MILLET, 6f)

        assertTrue(storage.removeContainer(Cereal.MILLET))
        assertEquals(0f, storage.getAmount(Cereal.MILLET))
    }

    @Test
    fun `removeContainer should not remove full container`() {
        storage.addCereal(Cereal.PEAS, 2f)

        assertFalse(storage.removeContainer(Cereal.PEAS))
    }

    @Test
    fun `getAmount should return amount of stored cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 2f)

        val amount = storage.getAmount(Cereal.BUCKWHEAT)

        assertEquals(2f, amount)
    }

    @Test
    fun `getAmount return 0 if there is no requested cereal`() {
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `getSpace should return empty space in the requested container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 2f)

        val space = storage.getSpace(Cereal.BUCKWHEAT)

        assertEquals(8f, space, 0.01f)
    }

    @Test
    fun `getSpace return 0 if there is no requested cereal`() {
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `toString should return storage info`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        storage.addCereal(Cereal.PEAS, 1f)
        val storageDescription = storage.toString()

        assertEquals(
            "Вместимость хранилища=20.0,\n" +
                    "Вместимость контейнера=10.0,\n" +
                    "Крупа - Гречка, Количество - 10.0\n" +
                    "Крупа - Горох, Количество - 1.0\n", storageDescription
        )
    }
}