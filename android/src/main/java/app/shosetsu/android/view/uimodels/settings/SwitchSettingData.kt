package app.shosetsu.android.view.uimodels.settings

import androidx.core.view.isVisible
import app.shosetsu.android.view.uimodels.settings.base.ToggleableStateSettingData
import com.github.doomsdayrs.apps.shosetsu.databinding.SettingsItemBinding

/*
 * This file is part of shosetsu.
 *
 * shosetsu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * shosetsu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with shosetsu.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * shosetsu
 * 25 / 06 / 2020
 */
class SwitchSettingData(id: Int) : ToggleableStateSettingData(id) {
	override fun bindBinding(holder: SettingsItemBinding, payloads: List<Any>) {
		super.bindBinding(holder, payloads)
		with(holder) {
			switchView.isVisible = true
			switchView.isChecked = isChecked
			switchView.setOnCheckedChangeListener(onCheckedListener)
		}
	}

	override fun unbindBinding(holder: SettingsItemBinding) {
		super.unbindBinding(holder)
		with(holder) {
			switchView.setOnCheckedChangeListener(null)
		}
	}
}
