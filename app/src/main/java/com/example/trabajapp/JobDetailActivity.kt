package com.example.trabajapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.example.trabajapp.databinding.ActivityJobDetailBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class JobDetailActivity : AppCompatActivity() {

    // constantes globales para evitar errores
    companion object {
        const val BUNDLE_KEY = "bundle_key"
        const val ID_KEY = "id_key"
        const val TITLE_KEY = "title_key"
        const val PUBDATE_KEY = "published_at_key"
        const val DESC_KEY = "desc_key"
        const val FUNCTIONS_KEY = "functions_key"
        const val DESIRABLE_KEY = "desirable_key"
        const val REMOTE_KEY = "remote_key"
        const val LANG_KEY = "lang_key"
        const val CATEGORY_KEY = "category_name_key"
        const val APPLICATIONS_KEY = "applications_count_key"

        const val MODALITY_KEY = "modality_key"
        const val COMPANY_KEY = "company_key"
        const val COMP_LOGO_KEY = "comp_logo_key"
        const val SENIORITY_KEY = "seniority_key"

        const val PUBLIC_LINK_KEY = "public_link_key"
    }

    private lateinit var binding: ActivityJobDetailBinding

    private val simpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
    private lateinit var bundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        bundle = intent.extras?.getBundle(BUNDLE_KEY)!!

        if (bundle != null) {
            createUI(bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            // User chose the "Favorite" action, mark the current item as a favorite...
            Toast.makeText(this, "Guardar", Toast.LENGTH_SHORT).show()
            true
        }

        R.id.open_in_browser -> {
            navigateToWebpage(bundle.getString(PUBLIC_LINK_KEY, "www.google.com"))
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun createUI(bundle : Bundle) {

        binding.tvCompanyName.text = bundle.getString(COMPANY_KEY)
        Picasso.get().load(bundle.getString(COMP_LOGO_KEY)).into(binding.ivLogo)

        binding.tvJobTitle.text = bundle.getString(TITLE_KEY)

        binding.tvDescription.text = HtmlCompat.fromHtml(bundle.getString(DESC_KEY).orEmpty(), 0)
        binding.tvFunctions.text = HtmlCompat.fromHtml(bundle.getString(FUNCTIONS_KEY).orEmpty(), 0)
        binding.tvDesirable.text = HtmlCompat.fromHtml(bundle.getString(DESIRABLE_KEY).orEmpty(), 0)

        if (binding.tvDescription.text.isEmpty()) binding.tvDescription.isVisible = false
        if (binding.tvFunctions.text.isEmpty()) binding.tvFunctions.isVisible = false
        if (binding.tvDesirable.text.isEmpty()) binding.tvDesirable.isVisible = false

        binding.tvPubDate.text = getDateString(bundle.getLong(PUBDATE_KEY))
        binding.chipApplicants.text = bundle.getInt(APPLICATIONS_KEY).toString()

        if (bundle.getBoolean(REMOTE_KEY)) {
            binding.tvRemote.text = "Remoto"
        } else {
            binding.tvRemote.text = "Presencial"
        }

        binding.tvModality.text = bundle.getString(MODALITY_KEY)
        binding.tvSeniority.text = bundle.getString(SENIORITY_KEY)
    }

    private fun getDateString(time: Long) : String = simpleDateFormat.format(time * 1000L)

    private fun navigateToWebpage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error en el Intent", Toast.LENGTH_LONG).show()
        }
    }
}