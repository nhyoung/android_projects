package edu.info448.kotlin

/* Step 0: Define a _data class_ called `City` that will represent the location 
   of a city. A City should have three properties: a `name` (String), `latitude`
   (Double), and `longitude` (Double).
*/
data class City(val name: String, val latitude: Double, val longitude: Double)

/* Step 1: Define a function called `listCities()` that returns a `list` of
   City objects representing the following U.S. cities:
    {name: "Atlanta", latitude: 33.7490, longitude: -84.3880}
    {name: "Bothell", latitude: 47.7601, longitude: -122.2054},
    {name: "New York", latitude: 40.7128, longitude: -74.0060}
    {name: "San Francisco", latitude: 37.7749, longitude: -122.4194},
    {name: "Tacoma", latitude: 47.2529, longitude: -122.4443},

   Call your function from inside the provided `main()` method, saving the result
   to a local variable called `cityLocations`. Print this out to check your work.
*/
fun listCities(): List<City> {
    return listOf(
            City("Atlanta", 33.7490, -84.3880),
            City("Bothell", 47.7601, -122.2054),
            City("New York",40.7128, -74.0060),
            City("San Francisco", 37.7749, -122.4194),
            City("Tacoma", 47.2529, -122.4443))
}


/* Step 2: Define a function `distanceFromSeattle()` that takes as a param a 
   City object and return the distance in kilometers (Double) between that city 
   and Seattle, WA (latitude: 47.6062, longitude: -122.3321).

   You can use the `haversine()` function provided below to calculate the 
   distance in km between two points on the globe.

   Call your function from inside the provided `main()` method and print out the
   distance between Seattle and Tacoma (should be about 40km)
*/
fun distanceFromSeattle(city: City): Double {
    return haversine(city.latitude, city.longitude, 47.6062, -122.3321)
}



//Calculates the distance in km between two points on a globe
//Implementation from http://rosettacode.org/wiki/Haversine_formula
fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6372.8 // in kilometers
    val l1 = Math.toRadians(lat1)
    val l2 = Math.toRadians(lat2)
    val dl = Math.toRadians(lat2 - lat1)
    val dr = Math.toRadians(lon2 - lon1)
    return 2 * R * Math.asin(Math.sqrt(Math.pow(Math.sin(dl / 2), 2.0) + Math.pow(Math.sin(dr / 2), 2.0) * Math.cos(l1) * Math.cos(l2)))
}

/* Step 3: In the provided `main()` method, call the `map()` method of the
   `cityLocations` list, passing it your `distanceFromSeattle()` function in
   order to get a list of distance between the cities and Seattle. Print
   the resulting list.
   Note that you will need use `::` to specify your function (e.g., `::foo`
   refers to the `foo()` function in the current file).
*/


/* Step 4: In the provided `main()` method, call the `filter()` method of the
   `cityLocations` list, passing it an _anonymous function_ used to filter the
   list. This function should return `true` if the given City is "far" from 
   Seattle (further away than 50km). Print out the resulting list.
*/


/* Step 5: In the provided `main()` method, use the `map()` and `filter() 
   methods to get a list of the `names` of cities that are in the "West",  
   meaning west of the Mississippi river. For our purposes, the Mississippi is 
   at longitude -89.97803 E (so anything "less than" that is to its west).
   Use two _lambda expressions_ to do this; utilize the implicit `it` parameter.
   Print out the resulting list. 

   Hint: filter for western cities, and then map the results to a list of names
   (Strings).
*/


/* Step 6: In the provided `main()` method, use the `maxByOrNull()` method of
   the `cityLocations` list to find which city is the furthest from Seattle.
   Since the last (and only) parameter to the `maxBy()` method is a function,
   you should omit the parentheses from the method call.
   Print the resulting city.

   Hint: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/max-by-or-null.html
*/


/* Step 7: In the provided `main()` method, use the `fold()` method to produce a
   variable called `citiesByName` that is a `Map<String, City>` object, where 
   each key is the name of a city (String) and the value is the City object.
   Print the resulting map.

   Hint: You will need to create an empty `MutableMap<String, City>` as an 
   initial value.

   Note: The `groupBy()` method is a cleaner way of achieving this same result,
   but you must use the `fold()` method for practice.
*/


//The main method. Add the code described above in here. Please indicate each
//step with a comment!
fun main(args: Array<String>) {
    // Step 1
    val cityLocations: List<City>  = listCities()
    println(cityLocations)

    // Step 2
    println(distanceFromSeattle(cityLocations[4]))

    // Step 3
    val calculateDistance = ::distanceFromSeattle
    val distances: List<Double> = cityLocations.map { calculateDistance(it) }
    println(distances)

    // Step 4
    val farCities: List<City> = cityLocations.filter {distanceFromSeattle(it) > 50.00 }
    println(farCities)

    // Step 5
    val westCities: List<City> = cityLocations.filter {it.longitude < -89.97803 }
    val westCityNames: List<String> = westCities.map { it.name }
    println(westCityNames)

    // Step 6
    val furthestFromSeattle: City? = cityLocations.maxByOrNull { distanceFromSeattle(it) }
    println(furthestFromSeattle)

    // Step 7
    var initial: MutableMap<String, City> = mutableMapOf()
    val citiesByName: Map<String,City> = cityLocations.fold(initial) {
        acc: MutableMap<String, City>, city: City ->
        acc[city.name] = city
        acc
    }
    println(citiesByName)
}
