package me.june.restaurant

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

class RestaurantApplicationTests {

    @Test
    fun copy_test() {
        val timestamp = (System.currentTimeMillis() / 1000).toString()

        val input = ByteArray(16)
        val input2 = Arrays.copyOf(timestamp.toByteArray(), timestamp.toByteArray().size)
        System.arraycopy(timestamp.toByteArray(), 0, input, 0, timestamp.toByteArray().size)

        System.out.println(Arrays.toString(input));
        System.out.println(Arrays.toString(input2));
    }

}
