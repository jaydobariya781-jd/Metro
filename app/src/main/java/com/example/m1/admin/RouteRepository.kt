package com.example.m1.admin

import android.content.Context

/**
 * Route repository for route lookups, corridor traversal, and transfer points
 */
class RouteRepository private constructor(context: Context) {

    private val routes = listOf(
        AdminRouteModel("RT-EW-01", "Vastral Gam", "Thaltej Gam", 17, 34, 30, "East-West Corridor"),
        AdminRouteModel("RT-NS-01", "APMC", "Motera Stadium", 15, 30, 25, "North-South Corridor"),
        AdminRouteModel("RT-EW-02", "Apparel Park", "Old High Court", 7, 14, 15, "East-West Corridor"),
        AdminRouteModel("RT-NS-02", "Paldi", "Old High Court", 4, 8, 10, "North-South Corridor"),
        AdminRouteModel("RT-INT-01", "Vastral Gam", "Motera Stadium", 22, 45, 35, "Cross-Corridor (Via Old High Court)")
    )

    fun getAllRoutes(): List<AdminRouteModel> = routes

    fun getRouteByCode(code: String): AdminRouteModel? {
        return routes.find { it.routeCode.equals(code, ignoreCase = true) }
    }

    fun getRoutesForCorridor(corridor: String): List<AdminRouteModel> {
        return routes.filter { it.corridorType.contains(corridor, ignoreCase = true) }
    }

    fun calculateFareEstimate(source: String, destination: String): Int {
        if (source.equals(destination, ignoreCase = true)) return 5
        val exactRoute = routes.find {
            (it.sourceStation.equals(source, ignoreCase = true) && it.destinationStation.equals(destination, ignoreCase = true)) ||
                    (it.sourceStation.equals(destination, ignoreCase = true) && it.destinationStation.equals(source, ignoreCase = true))
        }
        return exactRoute?.standardFare ?: 20
    }

    companion object {
        @Volatile
        private var instance: RouteRepository? = null

        fun getInstance(context: Context): RouteRepository {
            return instance ?: synchronized(this) {
                instance ?: RouteRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}
