package shubham.latest.master20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Data> datalist;

    public CustomAdapter(Context context, ArrayList<Data> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView id,sender,requested,operator,ground,message,time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view, null);

            holder.id = row.findViewById(R.id.id);
            holder.sender = row.findViewById(R.id.sender);
            holder.requested = row.findViewById(R.id.requested);
            holder.operator = row.findViewById(R.id.operator);
            holder.ground = row.findViewById(R.id.ground);
            holder.message = row.findViewById(R.id.message);
            holder.time = row.findViewById(R.id.time);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();

        }
        Data data=datalist.get(position);
        holder.id.setText(data.getid());
        holder.sender.setText(data.getSender());
        holder.requested.setText(data.getRequested());
        holder.operator.setText(data.getOperator());
        holder.ground.setText(data.getGround());
        holder.message.setText(data.getMessage());
        holder.time.setText(data.gettime());

        return row;
    }
}
