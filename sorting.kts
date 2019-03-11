import java.util.Arrays

fun <T : Comparable<T>> sort(elements: Array<T>) {
    if (elements.size > 1)
        quicksort(elements, 0, elements.size - 1)
}

fun <T : Comparable<T>> quicksort(elements: Array<T>, left: Int, right: Int) {
    var l = left
    var r = right
    var pivot = elements[(left + right) / 2]

    while (l < r) {
        while (elements[l].compareTo(pivot) < 0) l++
        while (elements[r].compareTo(pivot) > 0) r--
        if (l <= r)
            swap(elements, l++, r--)
    }

    if (left < r)
        quicksort(elements, left, r)
    if(right > l)
        quicksort(elements, l, right)
}

fun <T> swap(array: Array<T>, a: Int, b: Int) {
    val temp = array[a]
    array[a] = array[b]
    array[b] = temp
}

val integers = arrayOf(34, 12, 45, 89, 1)
sort(integers)
println("Sort integers")
println(Arrays.toString(integers))

val doubles = arrayOf(12.1, 45.1, 23.9, 12.9, 99.88)
sort(doubles)
println("Sort doubles")
println(Arrays.toString(doubles))

class SortableBox<T: Comparable<T>> (val t : T) : Comparable<SortableBox<T>> {

    override fun compareTo(other: SortableBox<T>): Int {
        return t.compareTo(other.t) // Delegate to t
    }

    override fun toString(): String {
        return t.toString()
    }
}

val strings = arrayOf(SortableBox("a"), SortableBox("c"), SortableBox("b"))
sort(strings)
println("Sort boxes of strings")
println(Arrays.toString(strings))