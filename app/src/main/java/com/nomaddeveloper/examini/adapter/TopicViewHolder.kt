package com.nomaddeveloper.examini.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.database.RealmCRUD
import com.nomaddeveloper.examini.model.topic.LessonTopic
import com.nomaddeveloper.examini.ui.fragment.PrepareToQuestionFragment

class TopicViewHolder(itemView: View) : ViewHolder(itemView) {
    private val topicNameTV: TextView = itemView.findViewById(R.id.tv_topic_name)
    private val topicFrequencyTV: TextView = itemView.findViewById(R.id.tv_topic_frequency)
    private val layoutAllTopic: LinearLayout = itemView.findViewById(R.id.ll_topic_item)
    private val cell1: TextView = itemView.findViewById(R.id.cell1)
    private val cell2: TextView = itemView.findViewById(R.id.cell2)
    private val cell3: TextView = itemView.findViewById(R.id.cell3)
    private val cell4: TextView = itemView.findViewById(R.id.cell4)
    private val cell5: TextView = itemView.findViewById(R.id.cell5)
    private val cell6: TextView = itemView.findViewById(R.id.cell6)
    private val backgroundDrawable =
        getDrawable(itemView.context, R.drawable.background_topic_item) as GradientDrawable

    fun bindTo(
        topic: LessonTopic,
        fragmentManager: FragmentManager,
        realmCRUD: RealmCRUD,
        googleId: String,
    ) {
        topicNameTV.text = topic.topic
        topicFrequencyTV.text = "Son 3 senede ortalama ${topic.frequency} soru"
        layoutAllTopic.setOnClickListener {
            val prepareToQuestionFragment =
                PrepareToQuestionFragment.newInstance(topic.lesson, topic.topic)
            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_left
                )
                .replace(
                    R.id.lesson_container_fragment,
                    prepareToQuestionFragment,
                    PrepareToQuestionFragment::class.java.name
                )
                .addToBackStack(null)
                .commit()
        }
        populateLast6Points(topic.lesson, topic.topic, realmCRUD, googleId)
    }

    private fun populateLast6Points(
        lesson: String,
        topic: String,
        realmCRUD: RealmCRUD,
        googleId: String,
    ) {
        val points = realmCRUD.getLast6Points(googleId, lesson, topic)

        val cells = listOf(cell1, cell2, cell3, cell4, cell5, cell6)
        cells.forEachIndexed { index, textView ->
            if (index < points.size) {
                textView.text = points[index].value.toString()
            } else {
                textView.text = ""
            }
        }

        val averagePoints = if (points.isNotEmpty()) {
            points.map { it.value }.average()
        } else {
            0.0
        }
        backgroundDrawable.setColor(getColorForAverage(averagePoints))
        layoutAllTopic.background = backgroundDrawable
    }

    private fun getColorForAverage(average: Double): Int {
        return when {
            average >= 4.5 -> {
                // Pale green
                Color.rgb(144, 238, 144) // Light green
            }

            average >= 3.5 -> {
                // Pale yellow
                Color.rgb(255, 255, 224) // Light yellow
            }

            average >= 2.5 -> {
                // Pale orange
                Color.rgb(255, 228, 196) // Light orange
            }

            average >= 1.5 -> {
                // Pale red
                Color.rgb(255, 182, 193) // Light pink
            }

            else -> {
                // Very pale red
                Color.rgb(255, 105, 97) // Coral
            }
        }
    }
}