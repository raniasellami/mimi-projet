package com.example.project.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.project.R
import com.example.project.adaptar.VideosAdapter
import com.example.project.models.VideoItem


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videosViewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPagerVideos)
        val videoItems: MutableList<VideoItem> = ArrayList()
        val item = VideoItem()
        item.videoURL = "https://firebasestorage.googleapis.com/v0/b/project-60585.appspot.com/o/videos%2FTop%20DIY%20Rainbow%20Pop%20It%20Fidget%20Toys%20Video%20Compilation.mp4?alt=media"
        item.videoTitle = "Women In Tech"
        item.videoDesc = "International Women's Day 2019"
        videoItems.add(item)
        val item2 = VideoItem()
        item2.videoURL = "https://firebasestorage.googleapis.com/v0/b/project-60585.appspot.com/o/videos%2FDIY%20Nails%20Video%20-%20DIY%20Nail%20Art%20Compilation%20-%20Nail%20Tutorial%20%23167.mp4?alt=media&token=f71df350-e3b7-46f2-8140-a12814ad5299"
        item2.videoTitle = "Sasha Solomon"
        item2.videoDesc = "How Sasha Solomon Became a Software Developer at Twitter"
        videoItems.add(item2)
        val item3 = VideoItem()
        item3.videoURL = "https://firebasestorage.googleapis.com/v0/b/project-60585.appspot.com/o/videos%2F2021_12_07_14_48_16?alt=media"
        item3.videoTitle = "Happy Hour Wednesday"
        item3.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item3)
        videosViewPager.adapter = VideosAdapter(videoItems)
    }
}


/*



 */

