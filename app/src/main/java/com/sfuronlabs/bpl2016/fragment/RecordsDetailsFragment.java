package com.sfuronlabs.bpl2016.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.model.RecordDetailsModel1;
import com.sfuronlabs.bpl2016.model.RecordDetailsModel2;
import com.sfuronlabs.bpl2016.model.RecordDetailsModel3;
import com.sfuronlabs.bpl2016.util.ViewHolder;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class RecordsDetailsFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    public void setUpSixElements(final ArrayList<RecordDetailsModel1> recordDetailsModel1s) {
        recyclerView.setAdapter(new BasicListAdapter<RecordDetailsModel1,SixElementViewHolder>(recordDetailsModel1s) {
            @Override
            public SixElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_six_elements,parent,false);
                return new SixElementViewHolder(view);
            }

            @Override
            public void onBindViewHolder(SixElementViewHolder holder, int position) {
                holder.tv1.setText(recordDetailsModel1s.get(position).getString1());
                holder.tv2.setText(recordDetailsModel1s.get(position).getString2());
                holder.tv3.setText(recordDetailsModel1s.get(position).getString3());
                holder.tv4.setText(recordDetailsModel1s.get(position).getString4());
                holder.tv5.setText(recordDetailsModel1s.get(position).getString5());
                holder.tv6.setText(recordDetailsModel1s.get(position).getString6());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setUpFourElements(final ArrayList<RecordDetailsModel2> recordDetailsModel2s) {
        recyclerView.setAdapter(new BasicListAdapter<RecordDetailsModel2,FourElementViewHolder>(recordDetailsModel2s) {
            @Override
            public FourElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_four_element,parent,false);
                return new FourElementViewHolder(view);
            }

            @Override
            public void onBindViewHolder(FourElementViewHolder holder, int position) {
                holder.tv1.setText(recordDetailsModel2s.get(position).getString1());
                holder.tv2.setText(recordDetailsModel2s.get(position).getString2());
                holder.tv3.setText(recordDetailsModel2s.get(position).getString3());
                holder.tv4.setText(recordDetailsModel2s.get(position).getString4());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setUpThreeElements(final ArrayList<RecordDetailsModel3> recordDetailsModel3s) {
        recyclerView.setAdapter(new BasicListAdapter<RecordDetailsModel3,ThreeElementViewHolder>(recordDetailsModel3s) {
            @Override
            public ThreeElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_three_elements,parent,false);
                return new ThreeElementViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ThreeElementViewHolder holder, int position) {
                holder.tv1.setText(recordDetailsModel3s.get(position).getString1());
                holder.tv2.setText(recordDetailsModel3s.get(position).getString2());
                holder.tv3.setText(recordDetailsModel3s.get(position).getString3());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class SixElementViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv1,tv2,tv3,tv4,tv5,tv6;

        public SixElementViewHolder(View itemView) {
            super(itemView);
            tv1 = ViewHolder.get(itemView,R.id.tv_str1);
            tv2 = ViewHolder.get(itemView,R.id.tv_str2);
            tv3 = ViewHolder.get(itemView,R.id.tv_str3);
            tv4 = ViewHolder.get(itemView,R.id.tv_str4);
            tv5 = ViewHolder.get(itemView,R.id.tv_str5);
            tv6 = ViewHolder.get(itemView,R.id.tv_str6);

        }
    }

    private static class FourElementViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv1,tv2,tv3,tv4;

        public FourElementViewHolder(View itemView) {
            super(itemView);
            tv1 = ViewHolder.get(itemView,R.id.tv_str1);
            tv2 = ViewHolder.get(itemView,R.id.tv_str2);
            tv3 = ViewHolder.get(itemView,R.id.tv_str3);
            tv4 = ViewHolder.get(itemView,R.id.tv_str4);

        }
    }

    private static class ThreeElementViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv1,tv2,tv3;

        public ThreeElementViewHolder(View itemView) {
            super(itemView);
            tv1 = ViewHolder.get(itemView,R.id.tv_str1);
            tv2 = ViewHolder.get(itemView,R.id.tv_str2);
            tv3 = ViewHolder.get(itemView,R.id.tv_str3);

        }
    }

}
