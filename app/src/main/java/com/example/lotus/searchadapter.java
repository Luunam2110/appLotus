package com.example.lotus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class searchadapter extends RecyclerView.Adapter<searchadapter.ViewHolder> {
    private Context context;
    Boolean request;
    private List<User> muser;
    public searchadapter(Context mcontext, List<User> muser,Boolean request){
        this.context= mcontext;
        this.muser=muser;
        this.request=request;
    }

    @NonNull
    @Override
    public searchadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new searchadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchadapter.ViewHolder holder, int position) {
        final User user =muser.get(position);
        holder.username.setText(user.username);
        if (user.getPicture().equals("default")){
            holder.profile_image.setImageResource(R.drawable.defaul_avt);
        }
        else {
            Picasso.get().load(user.getPicture()).into(holder.profile_image);
        }
        if (!user.getStatus().equals("Online")){
            holder.icon_online.setVisibility(TextView.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("id",user.id);
                bundle.putBoolean("resultSearch",true);
                if (request){
                    Log.d("adapter","đúng");
                    bundle.putBoolean("myRequest",true);
                }

                else {bundle.putBoolean("myRequest",false);}
                intent.putExtra("info",bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return muser.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public TextView icon_online;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username =itemView.findViewById(R.id.user_name);
            icon_online = itemView.findViewById(R.id.online);
            profile_image=itemView.findViewById(R.id.Profile_image);
        }
    }
}
