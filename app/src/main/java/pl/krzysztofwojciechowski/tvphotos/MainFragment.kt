/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

@file:Suppress("DEPRECATION")

package pl.krzysztofwojciechowski.tvphotos

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseFragment
import androidx.leanback.widget.*

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        loadRows()
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(activity, R.color.fastlane_background)
    }


    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        val names = resources.getStringArray(R.array.image_names)
        val ids = resources.obtainTypedArray(R.array.image_ids)


        val images = buildImages(names, ids)

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for (image in images) {
            listRowAdapter.add(image)
        }
        val header = HeaderItem(0, getString(R.string.category_name))
        rowsAdapter.add(ListRow(header, listRowAdapter))

        adapter = rowsAdapter
    }

    private fun buildImages(names: Array<String>, drawables: TypedArray): List<Image> {
        val imageData = mutableListOf<Image>()
        for (i in 0 until names.size) {
            imageData.add(Image(i, names[i], drawables.getResourceId(i, -1)))
        }
        return imageData.toList()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.IMAGE, item as Image)

            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                (itemViewHolder.view as ImageCardView).mainImageView,
                DetailsActivity.SHARED_ELEMENT_NAME
            )
                .toBundle()
            activity.startActivity(intent, bundle)
        }
    }
}
