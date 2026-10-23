package com.example.m1.admin

object UserManager {

    private val usersList = mutableListOf(
        UserModel("U-101", "Aarav Patel", "aarav.patel@Gmail.com", "ADMIN", "Active"),
        UserModel("U-102", "Raj Sharma", "rajsharma@gmail.com", "USER", "Active"),
        UserModel("U-103", "Karan patel", "karanpatel@gmail.co", "USER", "Unactive"),
        UserModel("U-104", "Priya Dave", "priya.dave@gmail.com", "USER", "Active"),
        UserModel("U-105", "Rohan Verma", "rohan.v@gmail.com", "USER", "Active"),
        UserModel("U-106", "Sneha Joshi", "sneha.j@gmail.com", "ADMIN", "Active"),
        UserModel("U-107", "Vikram Solanki", "vikram.s@gmail.com", "USER", "Unactive"),
        UserModel("U-108", "Ananya Desai", "ananya.d@gmail.com", "USER", "Active"),
        UserModel("U-109", "Manish Shah", "manish.shah@gmail.com", "USER", "Active")
    )

    fun getUsers(page: Int = 1, pageSize: Int = 3, query: String = "", roleFilter: String = "ALL"): List<UserModel> {
        val filtered = usersList.filter {
            val matchesQuery = query.isEmpty() ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true)
            val matchesRole = when (roleFilter) {
                "ADMIN" -> it.role.equals("ADMIN", ignoreCase = true)
                "USER" -> it.role.equals("USER", ignoreCase = true)
                "ACTIVE" -> it.status.equals("Active", ignoreCase = true)
                "UNACTIVE" -> it.status.equals("Unactive", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesRole
        }

        val startIndex = (page - 1) * pageSize
        if (startIndex >= filtered.size) return emptyList()
        val endIndex = (startIndex + pageSize).coerceAtMost(filtered.size)
        return filtered.subList(startIndex, endIndex)
    }

    fun getTotalPages(pageSize: Int = 3, query: String = "", roleFilter: String = "ALL"): Int {
        val filtered = usersList.filter {
            val matchesQuery = query.isEmpty() ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true)
            val matchesRole = when (roleFilter) {
                "ADMIN" -> it.role.equals("ADMIN", ignoreCase = true)
                "USER" -> it.role.equals("USER", ignoreCase = true)
                "ACTIVE" -> it.status.equals("Active", ignoreCase = true)
                "UNACTIVE" -> it.status.equals("Unactive", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesRole
        }
        return if (filtered.isEmpty()) 1 else ((filtered.size - 1) / pageSize) + 1
    }

    fun addUser(user: UserModel) {
        usersList.add(0, user)
    }

    fun updateUser(id: String, name: String, email: String, role: String, status: String) {
        usersList.find { it.id == id }?.apply {
            this.name = name
            this.email = email
            this.role = role
            this.status = status
        }
    }

    fun deleteUser(id: String) {
        usersList.removeAll { it.id == id }
    }
}
