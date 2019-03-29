# Kotlin Generics

## Definitions

Through the text i will refer to both generic types and parameterized types. These words have different meanings in generic programming. Generic types refers to types that take paramererized types. Considering the type `List<E>`, `List` is the generic type and `E` is the paramererized type.

## General

Generics are a features of many programming languages, that allow for declarations of classes, functions or similar constructions without knowing the types of the values acted upon by the declaration. 

```kotlin
class Box<T>(t: T) { // The type T is a generic type.
    var value = t
}

val number = 5
val box    = Box<Int>(number)
```

The term generic refers to the declaration using a type paramter. In the above example `Box` is a generic type, while `T` is a parameterized type.

In the above example, the `Box` class does not known the type of the constructor parameters `t` or the field `value`. This type information is declared when we create an instance of the `Box` class, using `<Int>`. Most of the time, it is not necessary to specify the type explicitly, since Kotlin can infer the type from the type of the passed variable `number`. This feature is called type inference.

```kotlin
class Box<T>(t: T) { // The type T is a generic type.
    var value = t
}

val number = 5
val box    = Box(number)
```

Note that the syntax for type inference differs from Java.
```java 
Box box = new Box<>(5) // Java, in Kotlin you can omit the <>
```

## Usage

Generics are great for creating reusable code, such that a class or function can act upon many different types. A great example of this is the Kotlin Collections API located in the `kotlin.collections` package. Using generics for collections are great, since the type of the values stored, does not change the behavior of the collection. 

### Random

Extension functions can be defined on generic types. In the [random.kts](random.kts) file, i have implemented an extension function on the `List` interface, that can retrieve a random element from that `List`.

```kotlin
import java.util.Random

fun <T> List<T>.random() : T? {
    val size = this.size
    val rnd = Random().nextInt(size)
    return this[rnd]
}

val numbers = listOf(1, 2, 3, 4, 5)
val random = numbers.random()
```

## Constraints

Constraints are a way of constraining the types that can be provided to a parameterized type. These are often necessary when the generic construct needs to perform operations upon or using the contained values.

To illustrate the use of constraints i have implemented the `quicksort` sorting algorithm. The implementation is not important, but can be found in the [sorting.kts](sorting.kts) file.


```kotlin
fun <T : Comparable<T>> sort(elements: Array<T>);
fun <T : Comparable<T>> quicksort(elements: Array<T>, left: Int, right: Int);
```

Here, the type parameter `T` is constrained to be a subtype of `Comparable<T>`. This means that any type that implements `Comparable<T>` can be sorted. The `compareTo` method on the `Comparable` type can then be used to compare to values of that type. Had we not constrained the type `T`, we would only have the methods present on `Any`, and could therefore not call `compareTo`.

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

Variance decides on how subtyping affects generic classes. There are three main types of variance. Given a type `Box<E>` the following is true.

- When `E` is covariant, `Box<Child>` is a subtype of `Box<Parent>`.
- When `E` is contravariant, `Box<Parent>` is a subtype of `Box<Child>`.
- When `E` is invariant, both `Box<Child>` and `Box<Parent>` are __not__ subtypes of each other.

Type parameters can be marked covariant using the `out` keyword, and contravariant using the `in` keyword. Type parameters can both be used when declaring the type on (type parameter), or when using the type (on type arguments).

```kotlin
// During declaration
interface Box<E>     // Invariant (default)
interface Box<out E> // Covariant
interface Box<in E>  // Contravariant

// During invocation
val invariant = new ArrayList<Number>()
val covariant = new ArrayList<out Number>()
val contravariant = new ArrayList<in Number>()
```

Generics in Kotlin are invariant by default. Meaning that the below code will not compile, since `Box<Integer>` is not a subtype of `Box<Number>` even though `Integer` is a subtype of `Number`.

```kotlin
class Box<T>;

val number  = Box<Number>()
val integer = Box<Integer>()

number = integer // error: type mismatch: inferred type is Variance.Box<Integer> but Variance.Box<Number> was expected
```

Invariance is important because it limits the operations that can safely be performed on a generic type with a parameterized type. When a parameterized type is covariant it can be __retrieved__ from the generic type. When a parameterized type is contravatiant it can only be __inserted__ into the generic type.

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

The class `Nothing` cannot be instantiated, and the methods can therefore not be called.

## Type Erasure

## Nullabililty