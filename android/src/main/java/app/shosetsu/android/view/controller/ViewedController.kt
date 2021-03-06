package app.shosetsu.android.view.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import app.shosetsu.android.common.ext.*
import app.shosetsu.common.dto.HResult
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.github.doomsdayrs.apps.shosetsu.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

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
 * ====================================================================
 */

/**
 * shosetsu
 * 23 / 02 / 2020
 *
 * @author github.com/doomsdayrs
 */
abstract class ViewedController<VB : ViewBinding> : LifecycleController, KodeinAware {
	/** Title of this view, Applies to the app system */
	@StringRes
	open val viewTitleRes: Int = -1

	/** String from [viewTitleRes] else just Shosetsu */
	open val viewTitle: String by lazy {
		if (viewTitleRes != -1)
			getString(viewTitleRes)
		else getString(R.string.app_name)
	}
	override val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }

	/**
	 * Should this be attached to root
	 */
	open val attachToRoot: Boolean = false

	lateinit var binding: VB

	init {
		addLifecycleListener(object : LifecycleListener() {
			override fun postCreateView(controller: Controller, view: View) {
				onViewCreated(view)
			}

			override fun preCreateView(controller: Controller) {
				Log.d(logID(), "Create view for ${controller.instance()}")
			}

			override fun preAttach(controller: Controller, view: View) {
				Log.d(logID(), "Attach view for ${controller.instance()}")
			}

			override fun preDetach(controller: Controller, view: View) {
				Log.d(logID(), "Detach view for ${controller.instance()}")
			}

			override fun preDestroyView(controller: Controller, view: View) {
				Log.d(logID(), "Destroy view for ${controller.instance()}")
			}
		})
	}

	constructor()
	constructor(args: Bundle) : super(args)

	private fun Controller.instance(): String {
		return "${javaClass.simpleName}@${Integer.toHexString(hashCode())}"
	}

	@CallSuper
	override fun onDestroy() {
		Log.d(logID(), "Destroying Controller")
		super.onDestroy()
	}

	@CallSuper
	override fun onDetach(view: View) {
		Log.d(logID(), "Detaching View")
		super.onDetach(view)
	}

	@CallSuper
	override fun onAttach(view: View) {
		Log.d(logID(), "Attaching View")
		super.onAttach(view)
	}

	open fun setViewTitle(viewTitle: String = this.viewTitle) {
		Log.i(logID(), "Activity title $viewTitle")
		activity?.title = viewTitle
	}

	/**
	 * The main creation method
	 */
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup,
		savedViewState: Bundle?,
	): View {
		setViewTitle()
		binding = bindView(inflater)
		onViewCreated(binding.root)
		return binding.root
	}

	/**
	 * What to do once the view is created
	 */
	abstract fun onViewCreated(view: View)

	/**
	 * Creates the viewBinding
	 */
	abstract fun bindView(inflater: LayoutInflater): VB

	/**
	 * Show an error on screen
	 */
	open fun handleErrorResult(e: HResult.Error) {
		toast { e.message }
	}

	/** @see [toast] */
	fun toast(
		length: Int = Toast.LENGTH_SHORT,
		message: () -> String,
	) {
		launchUI {
			applicationContext?.toast(message(), length)
		}
	}

	/** @see [toast] */
	fun toast(
		@StringRes message: Int,
		length: Int = Toast.LENGTH_SHORT,
	) {
		launchUI {
			applicationContext?.toast(message, length)
		}
	}


	/**
	 * Convenience method to observe [LiveData]
	 */
	fun <T> LiveData<T>.observe(observer: (T) -> Unit) =
		observe(this@ViewedController, observer)

	/**
	 * Convenience method to simplify [handleObserve] with self
	 */
	inline fun <T : HResult<D>, reified D> LiveData<T>.handleObserve(
		crossinline onLoading: () -> Unit = {},
		crossinline onEmpty: () -> Unit = {},
		crossinline onError: (HResult.Error) -> Unit = {},
		crossinline onSuccess: (D) -> Unit
	) = handleObserve(this@ViewedController, onLoading, onEmpty, onError, onSuccess)
}