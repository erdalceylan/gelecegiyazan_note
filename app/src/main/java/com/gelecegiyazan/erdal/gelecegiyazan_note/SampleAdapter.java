/*
 * Copyright (C) 2015 Thomas Robert Altstidl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gelecegiyazan.erdal.gelecegiyazan_note;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelecegiyazan.erdal.gelecegiyazan_note.detay.Detay;
import com.tr4android.recyclerviewslideitem.SwipeAdapter;
import com.tr4android.recyclerviewslideitem.SwipeConfiguration;

import java.util.ArrayList;

public class SampleAdapter extends SwipeAdapter implements View.OnClickListener {
    ArrayList<String> mDataset;

    private Context mContext;
    private RecyclerView mRecyclerView;

    public SampleAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        // create dummy dataset
        mDataset = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            mDataset.add("person" + String.valueOf(i + 1) + "@sample.com");
        }
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout contentView;
        View avatarView;
        TextView textView;

        public SampleViewHolder(View view) {
            super(view);
            contentView = (LinearLayout) view.findViewById(R.id.contentView);
            avatarView = view.findViewById(R.id.avatarView);
            textView = (TextView) view.findViewById(R.id.textView);
            contentView.setOnClickListener(SampleAdapter.this);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample, parent, true);
        return new SampleViewHolder(v);
    }

    @Override
    public void onBindSwipeViewHolder(RecyclerView.ViewHolder swipeViewHolder, int i) {
        SampleViewHolder sampleViewHolder = (SampleViewHolder) swipeViewHolder;
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        sampleViewHolder.avatarView.setBackgroundDrawable(drawable);
        sampleViewHolder.textView.setText(mDataset.get(i));
    }

    @Override
    public SwipeConfiguration onCreateSwipeConfiguration(Context context, int position) {
        return new SwipeConfiguration.Builder(context)
                .setLeftBackgroundColorResource(R.color.red_error)
                .setRightBackgroundColorResource(R.color.green_complete)
                .setLeftUndoable(true)
                .setLeftUndoDescription("Siliniyor")
                .setDescriptionTextColorResource(android.R.color.white)
                .setLeftSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.NORMAL_SWIPE)
                .setRightSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.RESTRICTED_SWIPE)
                .build();
    }

    @Override
    public void onSwipe(int position, int direction) {
        if (direction == SWIPE_LEFT) {
            mDataset.remove(position);
            notifyItemRemoved(position);
            Toast toast = Toast.makeText(mContext, "Deleted item at position " + position, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(mContext, "Marked item as read at position " + position, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
        // We need to get the parent of the parent to actually have the proper view
//        int position = mRecyclerView.getChildAdapterPosition((View) view.getParent().getParent());
//        Toast toast = Toast.makeText(mContext, "Clicked item at position " + position, Toast.LENGTH_SHORT);
//        toast.show();

        Intent intent = new Intent(view.getContext(), Detay.class);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}