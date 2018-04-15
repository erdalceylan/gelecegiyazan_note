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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelecegiyazan.erdal.gelecegiyazan_note.detay.Detay;
import com.gelecegiyazan.erdal.gelecegiyazan_note.detay.Note;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tr4android.recyclerviewslideitem.SwipeAdapter;
import com.tr4android.recyclerviewslideitem.SwipeConfiguration;


import java.util.ArrayList;

public class SampleAdapter extends SwipeAdapter implements View.OnClickListener {
    ArrayList<Note> mDataset;
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;


    private String TAG = "rc tag";

    private RecyclerView mRecyclerView;

    public SampleAdapter(Context context, RecyclerView recyclerView, DatabaseReference dbRef) {
        mContext = context;
        mRecyclerView = recyclerView;
        // create dummy dataset
        mDataset = new ArrayList<>();
        mDatabaseReference = dbRef;
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Note comment = dataSnapshot.getValue(Note.class);
                comment.id = dataSnapshot.getKey();

                // [START_EXCLUDE]
                mDataset.add(comment);
                notifyItemInserted(mDataset.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Note newComment = dataSnapshot.getValue(Note.class);
                String commentKey = dataSnapshot.getKey();

                System.out.println(newComment);
                notifyDataSetChanged();
                // [START_EXCLUDE]
//                int commentIndex = mCommentIds.indexOf(commentKey);
//                if (commentIndex > -1) {
//                    // Replace with the new data
//                    mComments.set(commentIndex, newComment);
//
//                    // Update the RecyclerView
//                    notifyItemChanged(commentIndex);
//                } else {
//                    Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
//                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                notifyDataSetChanged();

                // [START_EXCLUDE]
//                int commentIndex = mCommentIds.indexOf(commentKey);
//                if (commentIndex > -1) {
//                    // Remove data from the list
//                    mCommentIds.remove(commentIndex);
//                    mComments.remove(commentIndex);
//
//                    // Update the RecyclerView
//                    notifyItemRemoved(commentIndex);
//                } else {
//                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
//                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                notifyDataSetChanged();
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
//                Comment movedComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        dbRef.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
//        for (int i = 0; i < 25; i++) {
//            mDataset.add("person" + String.valueOf(i + 1) + "@sample.com");
//        }
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout contentView;
        View avatarView;
        TextView item_title;
        TextView item_content;
        TextView item_date;

        public SampleViewHolder(View view) {
            super(view);
            contentView = (LinearLayout) view.findViewById(R.id.contentView);
            avatarView = view.findViewById(R.id.avatarView);
            item_title = (TextView) view.findViewById(R.id.item_title);
            item_content = (TextView) view.findViewById(R.id.item_content);
            item_date = (TextView) view.findViewById(R.id.item_date);
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
        sampleViewHolder.item_title.setText(mDataset.get(i).title);
        sampleViewHolder.item_content.setText(mDataset.get(i).content);
        sampleViewHolder.item_date.setText(mDataset.get(i).date);
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
//            mDataset.remove(position);
//            notifyItemRemoved(position);
//            Toast toast = Toast.makeText(mContext, "Deleted item at position " + position, Toast.LENGTH_SHORT);
//            toast.show();

            System.out.println(mDataset.get(position).id);
            mDatabaseReference
                    .child(mDataset.get(position).id).removeValue();
            mDataset.remove(position);
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

        int position = mRecyclerView.getChildAdapterPosition((View) view.getParent().getParent());

        Intent intent = new Intent(view.getContext(), Detay.class);
        intent.putExtra("id",mDataset.get(position).id);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}