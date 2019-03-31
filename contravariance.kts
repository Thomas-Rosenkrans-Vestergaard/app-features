open class Person(val name: String)
open class Student(name: String) : Person(name)
open class Teacher(name: String) : Person(name)

fun <T> move(source: List<T>, destination: MutableList<in T>) { // try to remove 'in'
    for (value in source)
        destination.add(value)
}

val students = mutableListOf(Student("Thomas"), Student("Jonas"))
val teachers = mutableListOf(Teacher("Anders"))
val fieldtrip = mutableListOf<Person>()

move<Student>(students, fieldtrip)
move<Teacher>(teachers, fieldtrip)
print(fieldtrip) // [Contravariance$Student@9ef9c02, Contravariance$Student@76e13af5, Contravariance$Teacher@78c6917]