# kotlin-fun

You know, writing code in Kotlin is a lot of fun.

So, here I'm playing around with Kotlin and implementing interesting stuff.

Contributions are welcome.

## Self-reference

Kotlin has no ability to reference a variable which is not initialized inside its constructor.
But sometimes it is necessary, e.g. for callbacks.

There is a workaround with the following usage:

    val c: MyClass = selfReference { MyClass(someParams) { println(self.someField); } }
    
Here, `self` is a *magic* reference to a value which is not constructed yet.
