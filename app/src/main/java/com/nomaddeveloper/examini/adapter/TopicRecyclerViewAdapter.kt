package com.nomaddeveloper.examini.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.database.RealmCRUD
import com.nomaddeveloper.examini.model.topic.LessonTopic

class TopicRecyclerViewAdapter(
    topics: List<LessonTopic>,
    fragmentManager: FragmentManager,
    realmCRUD: RealmCRUD,
    googleId: String
) :
    RecyclerView.Adapter<TopicViewHolder>() {
    private val _topics = topics
    private val _fragmentManager = fragmentManager
    private val _realmCRUD = realmCRUD
    private val _googleId = googleId
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.topic_recycler_view_item, parent, false)
        return TopicViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return _topics.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bindTo(_topics[position], _fragmentManager, _realmCRUD, _googleId)
    }
}