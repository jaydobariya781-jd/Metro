package com.example.m1.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.m1.R
import com.google.android.material.button.MaterialButton

/**
 * User Management Activity for Admin Panel
 * Allows full CRUD operations, live search filtering, pagination, and role management.
 */
class AdminUsersActivity : AppCompatActivity() {

    private lateinit var etSearchUsers: EditText
    private lateinit var btnFilterUsers: ImageView
    private lateinit var btnAddNewUser: MaterialButton
    private lateinit var llUsersContainer: LinearLayout

    // Pagination
    private lateinit var btnPagePrev: TextView
    private lateinit var btnPage1: TextView
    private lateinit var btnPage2: TextView
    private lateinit var btnPage3: TextView
    private lateinit var btnPageNext: TextView

    private var currentPage = 1
    private var currentQuery = ""
    private var currentFilter = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        initViews()
        setupHeader()
        setupSearchAndFilter()
        setupAddUser()
        setupPagination()
        setupBottomNav()
        loadUsers()
    }

    private fun initViews() {
        etSearchUsers = findViewById(R.id.et_search_users)
        btnFilterUsers = findViewById(R.id.btn_filter_users)
        btnAddNewUser = findViewById(R.id.btn_add_new_user)
        llUsersContainer = findViewById(R.id.ll_users_container)

        btnPagePrev = findViewById(R.id.btn_page_prev)
        btnPage1 = findViewById(R.id.btn_page_1)
        btnPage2 = findViewById(R.id.btn_page_2)
        btnPage3 = findViewById(R.id.btn_page_3)
        btnPageNext = findViewById(R.id.btn_page_next)
    }

    private fun setupHeader() {
        findViewById<TextView>(R.id.tv_admin_brand)?.setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.iv_admin_bell)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro: No new user alerts", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.iv_admin_profile)?.setOnClickListener {
            Toast.makeText(this, "Admin Session: Full Access Granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearchAndFilter() {
        etSearchUsers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                currentQuery = s?.toString()?.trim() ?: ""
                currentPage = 1
                loadUsers()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnFilterUsers.setOnClickListener {
            val filterOptions = arrayOf("All Users", "Active Only", "Unactive Only", "Admins Only", "Standard Users")
            AlertDialog.Builder(this)
                .setTitle("Filter Application Users")
                .setItems(filterOptions) { _, which ->
                    currentFilter = when (which) {
                        1 -> "ACTIVE"
                        2 -> "UNACTIVE"
                        3 -> "ADMIN"
                        4 -> "USER"
                        else -> "ALL"
                    }
                    currentPage = 1
                    Toast.makeText(this, "Filtering: ${filterOptions[which]}", Toast.LENGTH_SHORT).show()
                    loadUsers()
                }
                .show()
        }
    }

    private fun setupAddUser() {
        btnAddNewUser.setOnClickListener {
            showUserDialog(null)
        }
    }

    private fun setupPagination() {
        btnPage1.setOnClickListener {
            currentPage = 1
            loadUsers()
        }
        btnPage2.setOnClickListener {
            currentPage = 2
            loadUsers()
        }
        btnPage3.setOnClickListener {
            currentPage = 3
            loadUsers()
        }

        btnPagePrev.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                loadUsers()
            } else {
                Toast.makeText(this, "Already on first page", Toast.LENGTH_SHORT).show()
            }
        }

        btnPageNext.setOnClickListener {
            val totalPages = UserManager.getTotalPages(3, currentQuery, currentFilter)
            if (currentPage < totalPages) {
                currentPage++
                loadUsers()
            } else {
                Toast.makeText(this, "Already on last page", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePaginationUI() {
        val totalPages = UserManager.getTotalPages(3, currentQuery, currentFilter)

        val pageButtons = listOf(btnPage1, btnPage2, btnPage3)
        for (i in pageButtons.indices) {
            val pageNum = i + 1
            val btn = pageButtons[i]

            if (pageNum <= totalPages) {
                btn.visibility = View.VISIBLE
                if (pageNum == currentPage) {
                    btn.background = ContextCompat.getDrawable(this, R.drawable.bg_page_active_blue)
                    btn.setTextColor(Color.WHITE)
                } else {
                    btn.background = ContextCompat.getDrawable(this, R.drawable.bg_page_inactive_white)
                    btn.setTextColor(Color.parseColor("#334155"))
                }
            } else {
                btn.visibility = View.GONE
            }
        }
    }

    private fun loadUsers() {
        llUsersContainer.removeAllViews()
        val users = UserManager.getUsers(currentPage, 3, currentQuery, currentFilter)
        val inflater = LayoutInflater.from(this)

        if (users.isEmpty()) {
            val emptyTv = TextView(this).apply {
                text = "No users found matching '$currentQuery'"
                textSize = 14f
                setTextColor(Color.parseColor("#64748B"))
                setPadding(20, 40, 20, 40)
                gravity = android.view.Gravity.CENTER
            }
            llUsersContainer.addView(emptyTv)
        } else {
            for (user in users) {
                val cardView = inflater.inflate(R.layout.item_admin_user_card, llUsersContainer, false)

                cardView.findViewById<TextView>(R.id.tv_user_name).text = user.name
                cardView.findViewById<TextView>(R.id.tv_user_email).text = user.email

                // Role badge
                val tvRole = cardView.findViewById<TextView>(R.id.tv_user_role_badge)
                tvRole.text = user.role.uppercase()
                if (user.role.equals("ADMIN", ignoreCase = true)) {
                    tvRole.background = ContextCompat.getDrawable(this, R.drawable.bg_role_admin_pill)
                    tvRole.setTextColor(Color.parseColor("#1D4ED8"))
                } else {
                    tvRole.background = ContextCompat.getDrawable(this, R.drawable.bg_role_user_pill)
                    tvRole.setTextColor(Color.parseColor("#7C3AED"))
                }

                // Status dot & label
                val viewDot = cardView.findViewById<View>(R.id.view_status_dot)
                val tvStatus = cardView.findViewById<TextView>(R.id.tv_user_status_label)
                tvStatus.text = user.status

                if (user.status.equals("Active", ignoreCase = true)) {
                    viewDot.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#16A34A"))
                    tvStatus.setTextColor(Color.parseColor("#0F172A"))
                } else {
                    viewDot.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#EF4444"))
                    tvStatus.setTextColor(Color.parseColor("#EF4444"))
                }

                // Edit Button Click
                cardView.findViewById<ImageView>(R.id.btn_edit_user).setOnClickListener {
                    showUserDialog(user)
                }

                // Delete Button Click
                cardView.findViewById<ImageView>(R.id.btn_delete_user).setOnClickListener {
                    showDeleteConfirmDialog(user)
                }

                // Card Click: Profile details
                cardView.setOnClickListener {
                    Toast.makeText(this, "${user.name} • ${user.role} • ${user.status}", Toast.LENGTH_SHORT).show()
                }

                llUsersContainer.addView(cardView)
            }
        }

        updatePaginationUI()
    }

    private fun showUserDialog(existingUser: UserModel?) {
        val isEdit = existingUser != null
        val title = if (isEdit) "Edit User Profile" else "Add New Metro User"

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 20)
        }

        val etName = EditText(this).apply {
            hint = "Full Name"
            setText(existingUser?.name ?: "")
        }

        val etEmail = EditText(this).apply {
            hint = "Email Address"
            setText(existingUser?.email ?: "")
        }

        // Role selection
        val tvRoleLabel = TextView(this).apply {
            text = "Select Role:"
            setPadding(0, 20, 0, 10)
            textSize = 14f
            setTextColor(Color.parseColor("#0F172A"))
        }

        val rgRole = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
        }
        val rbUser = RadioButton(this).apply { text = "USER"; id = View.generateViewId() }
        val rbAdmin = RadioButton(this).apply { text = "ADMIN"; id = View.generateViewId() }
        rgRole.addView(rbUser)
        rgRole.addView(rbAdmin)

        if (existingUser?.role.equals("ADMIN", ignoreCase = true)) {
            rbAdmin.isChecked = true
        } else {
            rbUser.isChecked = true
        }

        // Status selection
        val tvStatusLabel = TextView(this).apply {
            text = "Select Status:"
            setPadding(0, 20, 0, 10)
            textSize = 14f
            setTextColor(Color.parseColor("#0F172A"))
        }

        val rgStatus = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
        }
        val rbActive = RadioButton(this).apply { text = "Active"; id = View.generateViewId() }
        val rbUnactive = RadioButton(this).apply { text = "Unactive"; id = View.generateViewId() }
        rgStatus.addView(rbActive)
        rgStatus.addView(rbUnactive)

        if (existingUser?.status.equals("Unactive", ignoreCase = true)) {
            rbUnactive.isChecked = true
        } else {
            rbActive.isChecked = true
        }

        layout.addView(etName)
        layout.addView(etEmail)
        layout.addView(tvRoleLabel)
        layout.addView(rgRole)
        layout.addView(tvStatusLabel)
        layout.addView(rgStatus)

        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(layout)
            .setPositiveButton(if (isEdit) "Save Changes" else "Add User", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val role = if (rbAdmin.isChecked) "ADMIN" else "USER"
            val status = if (rbActive.isChecked) "Active" else "Unactive"

            if (name.isEmpty()) {
                etName.error = "Please enter user's full name"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Please enter email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Please enter a valid email address (e.g. name@metro.com)"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (isEdit && existingUser != null) {
                UserManager.updateUser(existingUser.id, name, email, role, status)
                Toast.makeText(this, "User '$name' updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                val newUser = UserModel(
                    id = "U-${System.currentTimeMillis() % 10000}",
                    name = name,
                    email = email,
                    role = role,
                    status = status
                )
                UserManager.addUser(newUser)
                Toast.makeText(this, "User '$name' added successfully!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
            loadUsers()
        }
    }

    private fun showDeleteConfirmDialog(user: UserModel) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete ${user.name} (${user.email})? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                UserManager.deleteUser(user.id)
                Toast.makeText(this, "User ${user.name} deleted", Toast.LENGTH_SHORT).show()
                loadUsers()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBottomNav() {
        // Tab 1: Home -> open AdminActivity
        findViewById<LinearLayout>(R.id.nav_admin_home)?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Users -> already here
        findViewById<LinearLayout>(R.id.nav_admin_users)?.setOnClickListener {
            // Already here
        }

        // Tab 3: Settings
        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            val intent = Intent(this, AdminSettingsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
