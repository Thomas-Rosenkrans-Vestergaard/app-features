class Box<T>(@Suppress("UNUSED_PARAMETER") value: T)

val nullable = Box<Int?>(null)
// val normal = Box<Int>(null) // error: null can not be a value of a non-null type Int

class Example<T>(private val v: T) {

    public fun returnIfTrue(condition: Boolean): T? =
        if (condition) v else null
}

val example = Example(5)
println(example.returnIfTrue(true))
println(example.returnIfTrue(false))