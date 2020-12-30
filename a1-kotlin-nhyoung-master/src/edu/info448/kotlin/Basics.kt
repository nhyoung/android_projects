package edu.info448.kotlin

import kotlin.math.E
import kotlin.math.pow

/* Define an _immutable_ variable `name` whose value is your name 
   (first and last) */
val name: String = "Nick Young"


/* Define a function called `feels()` that takes in two String as arguments: 
   one representing a topic (e.g., "Kotlin") and one representing an emotion 
   (e.g., "happy"). The function should print out a string with the format: 
   "<topic> makes me feel <emotion>!", replacing <topic> and <emotion> with the 
   parameter values.
   
   Use an inline string template!
*/
fun feels(topic: String, emotion: String) {
    val sentence = "$topic makes me feel $emotion!"
    println(sentence)
}


/* In the main function at the bottom of the file, call your `feels()` function 
   and let us know how "Kotlin" makes you feel! */


/* Define a function `compoundInterest()` that takes three _named_ arguments: 
     1. `principle` for an initial bank balance (in whole dollars, as an Int)
     2. `rate` for an annual interest rate (as a Double). It should have a
        _default_ value of 0.01 
     3. `duration` for a number of years (as an Int). It should have a _default_ 
         value of 1.0
   The function should calculate the continuous compound interest
     (https://en.wikipedia.org/wiki/Compound_interest#Continuous_compounding) 
   and *return* the resulting total balance after that many number of years

   You can call the function from the main method below and print the result to 
   check your work. Compare to
     http://www.mathwarehouse.com/calculators/continuous-compound-interest-calculator.php
*/
fun compoundInterest(principle: Int, rate: Double = 0.01, duration: Int = 1): Double {
    return principle * E.pow(rate * duration)
}



/* Define a function `fizzBuzz()` that takes in a single number (an Int) as an 
   argument. The function should *return* a _list_ of Strings from 1 to the 
   argument (e.g., with the number 1 represented by the string "1"). But for 
   multiples of three, the list should contain "Fizz" instead of the number. For multiples of five, the list should contain "Buzz" instead of the number. For 
   numbers which are multiples of both three and five, the list should contain "FizzBuzz" instead of the number.
   The returned list should be empty for arguments less than 1. 

   You can call the function from the main method below and print the result to 
   check your work.

   Optional challenge: try to do this using `map()` instead of a loop!
*/
fun fizzBuzz(num: Int): List<String> {
    val result: MutableList<String> = mutableListOf()

    for (i in 1..num) {
        if (i % 3 == 0 && i % 5 == 0) {
            result.add("FizzBuzz")
        } else if (i % 3 == 0) {
            result.add("Fizz")
        } else if (i % 5 == 0) {
            result.add("Buzz")
        } else {
            result.add(i.toString())
        }
    }

    return result
}


/* Challenge: declare an _extension function_ for the String class that 
   overloads the `times()` operator function. The function should take in an Int,
   allowing allowing the String to be multiplied by a number--e.g., `"spam" * 3`. 
   Your function should return a new String that is the initial string repeated 
   a number of times. For example, `"spam" * 3` should return the String 
   "spamspamspam". Remmber to declare your function as an `operator` function!

  Tip: use a range and the `fold()` functions to repeat and aggregate the String
*/
operator fun String.times(num: Int): String {
    var result: String = "";
    for (i in 1..num) {
        result = result.plus(this)
    }
    return result
}

//A main() method in Kotlin
fun main(args: Array<String>) {
    feels("Kotlin", "excited")
    println(compoundInterest(100, 0.04, 6))
    println(fizzBuzz(15))
    val curr = "spam"
    println(curr.times(5))
}
