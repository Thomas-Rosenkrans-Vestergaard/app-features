# Kotlin Generics

## Definitions

Through the text i will refer to both generic types and parameterized types. These words have different meanings in generic programming. Generic types refers to types that take paramererized types. Considering the type `List<E>`, `List` is the generic type and `E` is the paramererized type.

## General

Generics are a features of many programming languages, that allow for declarations of classes, functions or similar constructions without knowing the type of values stored, returned or accepted by methods of the type. Generics therefore allow for types that can act on many different types, while preserving behaviour.

```kotlin
class Box<T>(t: T) {
    var value = t
}

val number = 5
val box    = Box<Int>(number)
```
In the above example, the `Box` class does not known the type of the constructor parameters `t` or the field `value`. This type information is declared when we create an instance of the `Box` class, using `<Int>`. Most of the time, it is not necessary to specify the type explicitly, since Kotlin can infer the type from the type of the passed variable `number`. This feature is called type inference.

```kotlin
val number = 5
val box    = Box(number)
```

Generics are great for creating reusable code, such that a class or function can act upon many different types. A great example of this is the Kotlin Collections API located in the `kotlin.collections` package. Using generics for collections are great, since the type of the values stored, does not change the behavior of the collection. Other examples of generics usage is sorting code, and the functional interfaces defined in Java.

## Extension functions and generics

Extension functions can be defined on generic types. In the [random.kts](random.kts) file, i have implemented an extension function on the `List` interface, that can retrieve a random element.

```kotlin
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
```

Here we first declare that our extension function uses the parameterized type `T`. We next declare that the extension function is called on a `List<T>`. The extention function can therefor be applied to all `List` instances, regardless of the type of `T`.

## Constraints

Constraints are a way of constraining the types that can be provided to a parameterized type. These are often necessary when the generic construct needs to perform operations upon or using the contained values.

To illustrate the use of constraints i have implemented the `quicksort` sorting algorithm. The implementation is not important, but can be found in the [sorting.kts](sorting.kts) file.


```kotlin
fun <T : Comparable<T>> sort(elements: Array<T>);
fun <T : Comparable<T>> quicksort(elements: Array<T>, left: Int, right: Int);
```

Here, the type parameter `T` is constrained to be a subtype of `Comparable<T>`. This means that any type that implements `Comparable<T>` can be sorted. The `compareTo` method on the `Comparable` type can now be used to compare to values of that type. Had we not constrained the type `T`, we would only have the methods present on `Any`, and could therefore not call `compareTo`.

```kotlin
// from the quicksort function
...
while (elements[l].compareTo(pivot) < 0) l++
while (elements[r].compareTo(pivot) > 0) r--
...
```

We have used constraints to guarantee that the provided type is `Comparable`. We can now sort arrays of all `Comparable` types. These include `Int`, `Double` or our own custom types, that implement `Comparable`.

```kotlin

val integers = arrayOf(34, 12, 45, 89, 1)
sort(integers)

val doubles = arrayOf(12.1, 45.1, 23.9, 12.9, 99.88)
sort(doubles)
```

## Variance

Variance are the rules governing how subtyping affects generic types. There are three main types of variance. Considering the generic type `Box<E>` the following statements are true:

- When `E` is invariant, both `Box<Child>` and `Box<Parent>` are __not__ subtypes of each other.
- When `E` is marked covariant, `Box<Child>` is a subtype of `Box<Parent>`.
- When `E` is marked contravariant, `Box<Parent>` is a subtype of `Box<Child>`.

Type parameters can be marked covariant using the `out` keyword, and contravariant using the `in` keyword. Type parameters can both be used when declaring the generic type on (type parameter), or when instantiating the generic type (on type arguments). Within the Kotlin documentation these variations are called [use-site]() and [declaration-site](https://kotlinlang.org/docs/reference/generics.html#declaration-site-variance).

```kotlin
// During declaration (declaration-site)
interface Box<E>{}     // Invariant (default)
interface Box<out E>{} // Covariant
interface Box<in E>{}  // Contravariant

// During instantiating (use-site)
val invariant = new ArrayList<Number>()        // Invariant (default)
val covariant = new ArrayList<out Number>()    // Covariant
val contravariant = new ArrayList<in Number>() // Contravariant
```

Generics in Kotlin are invariant by default. Meaning that the below code will not pass the Kotlin type-checker, since `Box<Integer>` is not a subtype of `Box<Number>` even though `Integer` is a subtype of `Number`.

```kotlin
class Box<T>;

val number  = Box<Number>()
val integer = Box<Integer>()

number = integer // error: type mismatch: inferred type is Variance.Box<Integer> but Variance.Box<Number> was expected
```

Invariance is important because it limits the operations that can safely be performed on a generic type with a parameterized type. When a parameterized type is covariant it can be _retrieved_ from the generic type. When a parameterized type is contravatiant it can only be _given_ to the generic type.

```kotlin
fun both(elements: MutableList<String>) { // Invariant
    elements.add() // Takes String
    elements.get() // Returns String?
}

fun retrieve(elements: MutableList<out String>) { // Covariant
    elements.add() // Takes Nothing
    elements.get() // Returns String
}

fun insert(elements: MutableList<in String>) { // Contravariant
    elements.add() // Takes String
    elements.get() // Returns Any?
}
```

The class `Nothing` cannot be instantiated, and the methods can therefore not be called. You can view `Nothing` as the inverse of `Any`. All objects inherit from `Any`, but none inherit from `Nothing`.

### Covariance example

In the below example, it can be seen that calling `getNamesInvariant` with an argument of type `MutableList<Dog>`, does not pass the type-checker. This can be fixed with the `getNamesCovariant` function. Although `getNamesCovariant` does lose the ability to insert `Animal` instances into the list.

```kotlin
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
```

The first function `getNamesInvariant` works for `MutableList<Animal>`, but not for `MutableList<Dog>` or `MutableList<Cat>`. 

### Contravariance example

In the below example, i have implemented the `move` function, that can move the contents of the provided `source` into the provided `destination`. Here the type of `destination` is marked contravariant using the `in` keyword. This allows us to call the `move` function with a list of `Person` even though `Teacher` and `Student` was provided.

If `destination` has been invariant, we would only be able to move `List<Student>` into `List<Student>` and `List<Teacher>` into `List<Teacher>`.

```kotlin
open class Person(val name: String)
open class Student(name: String) : Person(name)
open class Teacher(name: String) : Person(name)

fun <T> move(source: List<T>, destination: MutableList<in T>) {
    for (value in source)
        destination.add(value)
}

val students = mutableListOf(Student("Thomas"), Student("Jonas"))
val teachers = mutableListOf(Teacher("Anders"))
val fieldtrip = mutableListOf<Person>()

move<Student>(students, fieldtrip)
move<Teacher>(teachers, fieldtrip)
print(fieldtrip) // [Contravariance$Student@9ef9c02, Contravariance$Student@76e13af5, Contravariance$Teacher@78c6917]
```

## Nullabililty

Nullability of course still work when using parameterized types. Using the `?` token, we can tell Kotlin that the type is nullable. If we attempt to pass a nullable type into a non-nullable parameterized types.

```kotlin
class Box<T> (value : T)
val nullable = Box<Int?>(null)
val normal = Box<Int>(null) // error: null can not be a value of a non-null type Int
```

In the above example, the type `T` was made nullable using the use-site declaration. We can also mark the type nullable when declaring our methods.

```kotlin
class Example<T>(private val v: T) {

    public fun returnIfTrue(condition: Boolean): T? =
        if (condition) v else null
}

val example = Example(5)
println(example.returnIfTrue(true))  // 5
println(example.returnIfTrue(false)) // null
``` 

## Type Erasure

Type Erasure means that the compiler will remove all parameterized types during compilation. The parameterized types are replaced by their actual runtime types. Because of this, it is impossible to find the type of a parameterized type during runtime.

```kotlin
fun 
```