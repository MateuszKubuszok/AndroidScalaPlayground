package com.talkie.client.app.activities.settings

import java.util.List

import android.annotation.TargetApi
import android.content.res.Configuration.{ SCREENLAYOUT_SIZE_XLARGE, SCREENLAYOUT_SIZE_MASK }
import android.content.{ Context, Intent }
import android.media.RingtoneManager
import android.net.Uri
import android.os.{ Build, Bundle }
import android.preference.{ ListPreference, Preference, PreferenceActivity, PreferenceFragment, PreferenceManager, RingtonePreference }
import android.text.TextUtils
import android.view.MenuItem
import com.talkie.client.R

object SettingsActivity {

  private def isXLargeTablet(context: Context) =
    (context.getResources.getConfiguration.screenLayout & SCREENLAYOUT_SIZE_MASK) >= SCREENLAYOUT_SIZE_XLARGE

  private lazy val bindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

    def onPreferenceChange(preference: Preference, value: AnyRef) = {
      val stringValue = value.toString

      val name = preference match {
        case listPreference: ListPreference =>
          val index = listPreference.findIndexOfValue(stringValue)
          if (index >= 0) {
            val entries = listPreference.getEntries
            entries(index)
          } else null
        case _: RingtonePreference =>
          if (TextUtils.isEmpty(stringValue))
            preference.getContext.getString(R.string.pref_ringtone_silent)
          else
            Option {
              RingtoneManager.getRingtone(preference.getContext, Uri.parse(stringValue))
            } map { ringtone =>
              ringtone.getTitle(preference.getContext)
            } orNull
        case _ => stringValue
      }

      preference.setSummary(name)

      true
    }
  }

  private def bindPreferenceSummaryToValue(preference: Preference) {
    preference.setOnPreferenceChangeListener(bindPreferenceSummaryToValueListener)
    bindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext).getString(preference.getKey, ""))
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  class GeneralPreferenceFragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.pref_general)
      setHasOptionsMenu(true)
      bindPreferenceSummaryToValue(findPreference("example_text"))
      bindPreferenceSummaryToValue(findPreference("example_list"))
    }

    override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
      case R.id.home =>
        startActivity(new Intent(getActivity, classOf[SettingsActivity]))
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  class NotificationPreferenceFragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.pref_notification)
      setHasOptionsMenu(true)
      bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"))
    }

    override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
      case R.id.home =>
        startActivity(new Intent(getActivity, classOf[SettingsActivity]))
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  class DataSyncPreferenceFragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.pref_data_sync)
      setHasOptionsMenu(true)
      bindPreferenceSummaryToValue(findPreference("sync_frequency"))
    }

    override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
      case R.id.home =>
        startActivity(new Intent(getActivity, classOf[SettingsActivity]))
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

}

class SettingsActivity extends AppCompatPreferenceActivity {

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setupActionBar()
  }

  private def setupActionBar() =
    Option(getSupportActionBar) foreach (_.setDisplayHomeAsUpEnabled(true))

  override def onIsMultiPane = SettingsActivity.isXLargeTablet(this)

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  override def onBuildHeaders(target: List[PreferenceActivity.Header]) =
    loadHeadersFromResource(R.xml.pref_headers, target)

  private lazy val validFragmentNames = Set(
    classOf[PreferenceFragment].getName,
    classOf[SettingsActivity.GeneralPreferenceFragment].getName,
    classOf[SettingsActivity.DataSyncPreferenceFragment].getName,
    classOf[SettingsActivity.NotificationPreferenceFragment].getName
  )
  override protected def isValidFragment(fragmentName: String) =
    validFragmentNames.contains(fragmentName)
}
