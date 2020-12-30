package edu.info448.kotlin

val MINIMUM_WAGE: Double = 15.0
/* See assignment instructions on Canvas */
open class Employee(val name: String, val hourlyWage: Double) {
    private var hoursWorked: Int = 0
        set(value) {
            if (field > 168) {
                field = 168
            } else if (field < 0) {
                field = 0
            } else {
                field = value
            }
        }

    init {
        println("Hiring $name")
    }

    fun calculatePay(): Double {
        return this.hoursWorked * this.hourlyWage
    }

    fun showInvoice() {
        val pay = calculatePay()
        println("Pay $name \$$pay for ${this.hoursWorked} hours work.")
    }

    open fun work(jobCallback: () -> Int) {
        this.hoursWorked += jobCallback()
    }
}

class UnionEmployee(name: String) : Employee(name, MINIMUM_WAGE) {
    var onStrike: Boolean = false

    fun picket() {
        println("On strike! Fair wages for all!")
    }

    override fun work(jobCallback: () -> Int) {
        if (this.onStrike) {
            picket()
        } else {
            super.work(jobCallback)
        }
    }
}

//A main() method in Kotlin
fun main(args: Array<String>) {
    val employees: Array<UnionEmployee> = arrayOf(
            UnionEmployee("Tom",),
            UnionEmployee("Sarah"),
            UnionEmployee("Bob"))
    for (i in 0..(employees.size - 1)) {
        employees[i].onStrike = true
        employees[i].work {
            println("${employees[i].name} working for the man")
            10
        }
        employees[i].showInvoice()
    }
}