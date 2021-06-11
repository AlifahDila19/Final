package com.coroutinesaac.example.view.activity

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import com.coroutinesaac.example
import com.coroutinesaac.example.model.TProduct
import com.coroutinesaac.example.util.bottomSheetConfirmationDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_product.*
import java.math.BigDecimal

class AddProductActivity : BaseActivity() {

    private lateinit var product: TProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        initComponent()
    }

    private fun initComponent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        product = intent.getParcelableExtra("object")

        with(product) {
            val btnDelete
            if (id == 0) btnDelete.gone()

            etProductName.setText(productName)
            etPrice.setText(if (price == BigDecimal.ZERO) "" else price.toPlainString())
        }

        initListener()
    }

    private fun initListener() {
        val btnSave
        btnSave.setOnClickListener { view ->
            val errorMsg = validateFields()

            if (errorMsg != "") {
                Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG).show()
            } else {
                product.apply {
                    productName = etProductName.value()
                    price = BigDecimal(etPrice.value())
                }

                Intent().apply {
                    putExtra("object", product)
                    setResult(RESULT_OK, this)
                }
                finish()
            }
        }

        val btnDelete
        btnDelete.setOnClickListener {
            bottomSheetConfirmationDialog(getString(R.string.delete_question)) {
                val intent = Intent()
                intent.putExtra("object", product)
                intent.putExtra("delete", true)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun validateFields(): String {
        val etProductName
        return if (etProductName.value() == "") {
            getString(R.string.product_name_blank)
        } else if (etPrice.value() == "") {
            getString(R.string.price_blank)
        } else if (etPrice.value().toInt() <= 0) {
            getString(R.string.price_zero)
        } else {
            ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()
        }

        return true
    }

}