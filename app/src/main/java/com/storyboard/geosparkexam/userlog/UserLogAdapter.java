package com.storyboard.geosparkexam.userlog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class UserLogAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<GeosparkLog> mGeoLog = new ArrayList<>();

    UserLogAdapter(Context context) {
        this.mContext = context;
    }

    void addAllItem(List<GeosparkLog> lst) {
        mGeoLog.clear();
        mGeoLog.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.logs_activity_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeosparkLog geosparkLog = mGeoLog.get(position);
        ((ItemHolder) holder).mTxtName.setText(geosparkLog.getmName());
        ((ItemHolder) holder).mTxtComments.setText(geosparkLog.getmComments());
        ((ItemHolder) holder).mTxtComments.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", ((ItemHolder) holder).mTxtComments.getText().toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGeoLog.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private TextView mTxtComments;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTxtComments = itemView.findViewById(R.id.txt_comments);
        }
    }
}
