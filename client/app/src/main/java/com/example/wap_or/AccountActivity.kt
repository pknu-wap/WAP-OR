package com.example.wap_or
import AccountLog
import AccountLogAdapter
import VerticalSpaceItemDecoration
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.graphics.Color
class AccountActivity : BaseActivity() {
    private lateinit var btnAll: TextView
    private lateinit var btnIncome: TextView
    private lateinit var btnExpense: TextView
    private lateinit var underlineAll: View
    private lateinit var underlineIncome: View
    private lateinit var underlineExpense: View
    private lateinit var withdrawalButton: Button
    private lateinit var depositButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var accountLogAdapter: AccountLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_main)
        setupBottomNavigation()

        btnAll = findViewById(R.id.btn_all)
        btnIncome = findViewById(R.id.btn_income)
        btnExpense = findViewById(R.id.btn_expense)

        underlineAll = findViewById(R.id.underline_all)
        underlineIncome = findViewById(R.id.underline_income)
        underlineExpense = findViewById(R.id.underline_expense)

        recyclerView = findViewById(R.id.recycler_view)
        withdrawalButton = findViewById(R.id.withdrawal_button)
        depositButton = findViewById(R.id.deposit_button)
        // 초기 설정
        setSelectedButton(btnAll, underlineAll)

        btnAll.setOnClickListener { setSelectedButton(btnAll, underlineAll) }
        btnIncome.setOnClickListener { setSelectedButton(btnIncome, underlineIncome) }
        btnExpense.setOnClickListener { setSelectedButton(btnExpense, underlineExpense) }

        // RecyclerView 초기화
        setupRecyclerView()
        depositButton.setOnClickListener {
            val customDialogView = layoutInflater.inflate(R.layout.custom_popup_deposit, null)
            val customDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(customDialogView)
                .create()
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val confirmButton = customDialogView.findViewById<TextView>(R.id.confirm_button)
            confirmButton.setOnClickListener {
                customDialog.dismiss()
                navigateToDeposit()
            }

            val cancelButton = customDialogView.findViewById<ImageButton>(R.id.cancel_button)
            cancelButton.setOnClickListener {
                customDialog.dismiss()
            }

            customDialog.show()

        }
        withdrawalButton.setOnClickListener { navigateToWrite() }
    }

    private fun setSelectedButton(selectedButton: TextView, selectedUnderline: View) {
        // 모든 밑줄 숨기기
        underlineAll.visibility = View.GONE
        underlineIncome.visibility = View.GONE
        underlineExpense.visibility = View.GONE

        // 선택한 밑줄 보이기
        selectedUnderline.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        // 더미 데이터 생성
        val sampleData = mutableListOf(
            AccountLog("11.25", "#식사", "-5,800원", "26,700원"),
            AccountLog("11.24", "#카페", "-12,900원", "32,350원"),
            AccountLog("11.23", "#의류", "-54,750원", "45,250원")
        )
        // 어댑터 초기화
        accountLogAdapter = AccountLogAdapter(sampleData)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = accountLogAdapter
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing_13dp)
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))
    }
    private fun navigateToDeposit() {
        // DepositActivity로 이동
        val intent = Intent(this, DepositActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun navigateToWrite() {
        // WriteActivity로 이동
        val intent = Intent(this, WriteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}