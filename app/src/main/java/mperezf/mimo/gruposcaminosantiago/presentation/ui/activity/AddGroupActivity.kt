package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.AddGroupFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupListFragment

class AddGroupActivity : BaseActivity(), AddGroupListener {

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

    override fun groupAdded(group: Group) {
        val output = Intent()
        output.putExtra(GroupListFragment.RELOAD_LIST, true)
        setResult(Activity.RESULT_OK, output)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}

interface AddGroupListener {

    fun groupAdded(group: Group)
}
