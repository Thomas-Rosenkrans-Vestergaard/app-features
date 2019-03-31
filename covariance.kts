open class Animal(val name: String)
open class Cat(name: String) : Animal(name)
open class Dog(name: String) : Animal(name)

val dogs    = arrayListOf(Dog("Fido"), Dog("Pluto"), Dog("Max"))
val animals = arrayListOf(Dog("Fido"), Cat("Charles"))

fun getNamesInvariant(animals: MutableList<Animal>): List<String> {
    val result = mutableListOf<String>()
    for (animal in animals)
        result.add(animal.name)
    return result
}

fun getNamesCovariant(animals: MutableList<out Animal>): List<String> {
    val result = mutableListOf<String>()
    for (animal in animals)
        result.add(animal.name)
    return result
}

getNamesInvariant(animals)
getNamesInvariant(dogs) // error: type mismatch: inferred type is MutableList<Covariance.Dog> but MutableList<Covariance.Animal> was expected

getNamesCovariant(animals)
getNamesCovariant(dogs)