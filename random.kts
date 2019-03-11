import java.util.Random

fun <T> List<T>.random() : T {
    val size = this.size
    val rnd = Random().nextInt(size)
    return this[rnd]
}

val numbers = listOf(1, 2, 3, 4, 5)
println(numbers.random())
println(numbers.random())
println(numbers.random())