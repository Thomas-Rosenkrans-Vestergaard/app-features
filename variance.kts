class Box<T>();

val number = Box<Number>()
val integer = Box<Integer>()

// number = integer

fun both(elements: MutableList<String>) {
    elements.add() // Takes String
    elements.get() // Returns String
}

fun retrieve(elements: MutableList<out String>) {
    elements.add() // Takes Nothing
    elements.get() // Returns String
}

fun insert(elements: MutableList<in String>) {
    elements.add() // Takes String
    elements.get() // Returns Any?
}

val covariant = mutableListOf<out String>()