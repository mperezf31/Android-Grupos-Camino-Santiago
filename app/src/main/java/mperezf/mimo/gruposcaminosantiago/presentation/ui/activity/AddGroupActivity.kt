package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.AddGroupFragment

class AddGroupActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_group_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.create_group)

        if (savedInstanceState == null) {
            showFragment(AddGroupFragment.newInstance())
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
