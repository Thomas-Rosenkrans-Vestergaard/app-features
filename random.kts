import java.util.Random

/**
 * Retrieves a random element from the list.
 * @returns The random element from the list, {@code null} when the list is empty.
 */
fun <T> List<T>.random() : T? {
    if(this.size == 0)
        return null

    val rnd = Random().nextInt(this.size)
    return this[rnd]
}

val numbers = listOf(1, 2, 3, 4, 5)
val random = numbers.random()