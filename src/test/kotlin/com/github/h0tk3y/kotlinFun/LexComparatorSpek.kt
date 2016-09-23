package com.github.h0tk3y.kotlinFun

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.naturalOrder

class LexComparatorSpek : Spek({ // @formatter:off
    describe("transformation of a comparator to a lexicographical comparator") { // @formatter:on
        fun <T> transform(comparator: Comparator<in T>) = comparator.lexicographically()

        on("transforming an all-equal comparator") {
            val lexComparator = transform(compareBy { 0 })

            it("should consider empty lists equal") {
                assertEquals(0, lexComparator.compare(emptyList<String>(), emptyList<String>()))
            }

            it("should compare lists by their length") {
                val a = listOf("a")
                val ab = listOf("a", "b")
                val abc = listOf("a", "b", "c")

                lexComparator.withOperators {
                    assertTrue(a < ab)
                    assertTrue(ab < abc)
                    assertTrue(a < abc)
                }
            }
        }

        on("transforming natural order comparator") {
            val stringNaturalComparator = naturalOrder<String>()
            val lexComparator = transform(stringNaturalComparator)

            it("should compare lists by single item if both have only one") {
                val item1 = "a"
                val item2 = "b"
                val list1 = listOf(item1)
                val list2 = listOf(item2)

                assertEquals(stringNaturalComparator.compare(item1, item2), lexComparator.compare(list1, list2))
                assertEquals(stringNaturalComparator.compare(item2, item1), lexComparator.compare(list2, list1))
            }

            it("should compare lists by their first item when it is different") {
                val a = listOf("a", "b")
                val b = listOf("b", "a")
                lexComparator.withOperators {
                    assertTrue(a < b)
                }
            }

            it("should consider the shortest list to be less when it is prefix of the other") {
                val a = listOf("a", "b", "c")
                val b = a + "d" + "e"

                lexComparator.withOperators {
                    assertTrue(a < b)
                    assertTrue(b > a)
                }
            }

            it("should consider equal lists to be equal") {
                val a = listOf("a", "b", "c")

                val result = lexComparator.compare(a.toList(), a.toList())
                assertEquals(0, result)
            }

            it("should perform lexicographical comparison") {
                val a = listOf("a", "b", "c")
                val b = listOf("a", "c", "d", "e")

                lexComparator.withOperators {
                    assertTrue(a < b)
                    assertTrue(b > a)
                }
            }
        }
    } // @formatter:off
}) // @formatter:on
