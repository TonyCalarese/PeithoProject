package com.example.peithoproject.recyclerassets;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.peithoproject.R;

public class ChartsActivity extends AppCompatActivity {

    /*
    //RecyclerView
    private RecyclerView mDataFrame;
    private DataFrameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        /*
        mDataFrame = (RecyclerView) v.findViewById(R.id.dataRecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mDataFrame.setLayoutManager(mLayoutManager);
        mAdapter = new DataFrameAdapter();
        mDataFrame.setAdapter(mAdapter);
         */
    }
    /*
    private void UpdateUI() {
        if (mAdapter == null) {
            mAdapter = new DataFrameAdapter();
            mDataFrame.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }
    */

    /*
    //Adapter
    public class DataFrameAdapter extends RecyclerView.Adapter<DataHolder> {
        public DataFrameAdapter() {
        }

        @Override
        public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DataHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DataHolder holder, int position) {
            holder.bindPosition(mUserEmoData.getIndexEmotion(position), position);
        }

        @Override
        public int getItemCount() {
            return UserEmoData.getEmotionDataSize();
        }

        //public void appendEmotion(float emotion) { UserEmoData.add(emotion); }

    }
*/


}
