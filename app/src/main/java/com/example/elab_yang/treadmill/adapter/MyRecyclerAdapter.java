package com.example.elab_yang.treadmill.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elab_yang.treadmill.R;
import com.example.elab_yang.treadmill.model.CardItem;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements OnLongClickListener {
    Context mContext;
    private final List<CardItem> mDataList;

    // Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public MyRecyclerAdapter(List<CardItem> dataList) {
        mDataList = dataList;
    }

    // ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    // 뷰 홀더를 생성하는 부분. 레이아웃을 만드는 부분
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    // RecyclerView의 Row 하나하나를 구현하기위해 Bind(묶이다) 될 때
    // 뷰 홀더에 데이터를 설정하는 부분
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardItem item = mDataList.get(position);

        CardView cardview = holder.cardview;
        TextView date = holder.date;
        TextView time = holder.time;
        TextView ei = holder.ei;
        TextView speed = holder.speed;
        TextView distance = holder.distance;
        TextView bpm = holder.bpm;
        TextView kcal = holder.kcal;

        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.ei.setText(item.getEi());
        holder.speed.setText(item.getSpeed());
        holder.distance.setText(item.getDistance());
        holder.bpm.setText(item.getBpm());
        holder.kcal.setText(item.getKcal());
        // 톡 건드림

        holder.cardview.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                removeAt(position);

                return false;
            }
        });
    }

    // 아이템의 수
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
    }

    public void removeAt(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    // 각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
    // 반드시 RecyclerView.ViewHolder를 상속해야 함
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        TextView date, time, ei, speed, distance, bpm, kcal;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            ei = (TextView) itemView.findViewById(R.id.ei);
            speed = (TextView) itemView.findViewById(R.id.speed);
            distance = (TextView) itemView.findViewById(R.id.distance);
            bpm = (TextView) itemView.findViewById(R.id.bpm);
            kcal = (TextView) itemView.findViewById(R.id.kcal);

            cardview.setOnClickListener(v -> {

            });
            itemView.setOnClickListener(v -> {
//                    Toast.makeText(mContext, "머요ㅡㅡ", Toast.LENGTH_LONG).show();
            });
        }
    }
}