# kotlin-fun

You know, writing code in Kotlin is a lot of `fun`. 

Here you may find some useful stuff written while playing around. Contributions are welcome.

## Self-reference

Kotlin has no ability to reference a variable which is not initialized inside its constructor.
But sometimes it is necessary, e.g. for callbacks.

There is a workaround with the following usage:

    val c: MyClass = selfReference { MyClass(someParams) { println(self.someField); } }
    
Here, `self` is a *magic* reference to a value which is not constructed yet.

## Sequence chaining

* `modifyPrefix` takes the original sequence and applies an operator to its prefix, leaving the
 tail unchanged, if any;

* `chain` transforms the sequence by chaining the results of the operators, each called on what's left of the sequence  
 after the previous one;
 
* `lazyChain` is the same but concatenation is done lazily and no sequence objects are created until their items item are
 requested;
  
* operator for concatenating a sequence with lambda that provides a sequence.

### Examples

    val seq = (1..5).asSequence()
    
    val prefixModified = seq.modifyPrefix { drop(1).take(2).map { -it } } // -2, -3, 4, 5 
    
    val chained = seq.chain(
        { sequenceOf(-1, 0) }
        { take(2).map { it + 100 } }
        { map { it * 100 } }
    )
    // -1, 0, 101, 102, 300, 400, 500
    
    fun primes(): Sequence<Int> {
        fun primesFilter(from: Sequence<Int>): Sequence<Int> = from.iterator().let {
            val current = it.next()
            sequenceOf(current) + { primesFilter(it.asSequence().filter { it % current != 0 }) }
        }
        return primesFilter((2..Int.MAX_VALUE).asSequence())
    }    
    
## Field property delegate
    
Provides property delegates which behave as if there was a backing field. Useful for extension properties.
    
Use `[Synchronized][Nullable]FieldProperty` for properties with different nullability and thread-safety. 
Default initializer for nullable version produces `null`, for not-null -- throws `IllegalStateException`.

    var MyClass.tag: String by FieldProperty { it.name }
    
    fun main(args: Array<String>) {
       val c = MyClass("some name")
       println(c.tag) // some name
       c.tag = "some tag"
       println(c.tag) // some tag
    }
    
Can be used to create inner mapping distinct for instances of enclosing class:

    val enclosingMyClassSharedTag = FieldProperty<MyClass, String> { it.name.reversed() }

    class Enclosing {    
        var MyClass.innerTag: String by FieldProperty { it.name }
        var MyClass.sharedTag: String by enclosingMyClassSharedTag
        
        // ...
    }    