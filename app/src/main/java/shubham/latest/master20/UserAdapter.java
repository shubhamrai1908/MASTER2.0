package shubham.latest.master20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private List<Ground1> list;

    public UserAdapter(Context context, List<Ground1> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View rootview= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        Ground1 ground=list.get(position);
        holder.textView.setText(ground.name+"  |  "+ground.phone);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,ground.uid,Toast.LENGTH_SHORT).show();
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=ground.uid;
                Task<Void> voidTask = Utils.removeUser(uid);
                Toast.makeText(context, ground.name+" Deleted from ground database", Toast.LENGTH_LONG).show();

            }
        });
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String uid=ground.uid;
                Task<Void> voidTask = Utils.removeUser(uid);

                voidTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "User removed from database...", Toast.LENGTH_SHORT).show();

                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageButton button;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.ground_list);
            button=itemView.findViewById(R.id.img_button);

        }
    }
}
