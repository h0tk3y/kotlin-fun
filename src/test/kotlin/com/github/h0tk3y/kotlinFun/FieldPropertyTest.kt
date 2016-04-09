package com.github.h0tk3y.kotlinFun

import org.junit.Assert.*
import org.junit.Test

class MyClass(val name: String)

var MyClass.uninitializedTag: String by FieldProperty()

val propertyHolderMyClassSharedTag = FieldProperty<MyClass, String> { it.name.reversed() }

class PropertyHolder() {
    var MyClass.innerTag: String by FieldProperty { it.name }
    var MyClass.sharedTag: String by propertyHolderMyClassSharedTag

    fun getInner(c: MyClass) = c.innerTag
    fun setInner(c: MyClass, value: String) {
        c.innerTag = value
    }

    fun getShared(c: MyClass) = c.sharedTag
    fun setShared(c: MyClass, value: String) {
        c.sharedTag = value
    }
}

class FieldPropertyTest {

    @Test
    fun testUninitialized() {
        val c = MyClass("name")
        try {
            println(c.uninitializedTag)
            fail()
        } catch (e: IllegalStateException) {
        }

        c.uninitializedTag = c.name
        assertEquals(c.name, c.uninitializedTag)
    }

    @Test
    fun differentObjects() {
        val c1 = MyClass("name1")
        val c2 = MyClass("name2")
        c1.uninitializedTag = c1.name
        c2.uninitializedTag = c2.name
        assertEquals(c1.name, c1.uninitializedTag)
        assertEquals(c2.name, c2.uninitializedTag)
        c1.uninitializedTag = c2.uninitializedTag
        assertEquals(c2.uninitializedTag, c1.uninitializedTag)
    }

    var MyClass.nullableTag: String? by NullableFieldProperty { it.name }

    @Test
    fun testNullableReassignment() {
        val c = MyClass("name")
        assertEquals(c.name, c.nullableTag)
        c.nullableTag = null
        assertNull(c.nullableTag)
    }


    @Test
    fun innerVsShared() {
        val p1 = PropertyHolder()
        val p2 = PropertyHolder()

        val c1 = MyClass("c1")

        p1.setInner(c1, "i1")
        assertEquals("i1", p1.getInner(c1))
        assertEquals(c1.name, p2.getInner(c1))

        p1.setShared(c1, "s1")
        assertEquals("s1", p1.getShared(c1))
        assertEquals("s1", p2.getShared(c1))
    }
}

