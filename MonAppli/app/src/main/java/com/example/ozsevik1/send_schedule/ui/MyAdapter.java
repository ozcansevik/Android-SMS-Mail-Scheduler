package com.example.ozsevik1.send_schedule.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.model.ModelSms;

import java.util.List;

/**
 * Created by ozcan on 27/03/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<ModelSms> listSMS;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text_sms, text_num, text_nom, text_date, text_time;

        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View view) {
            super(view);
            text_sms = (TextView) view.findViewById(R.id.text_sms_row);
            text_num = (TextView) view.findViewById(R.id.text_num_row);
            text_nom = (TextView) view.findViewById(R.id.text_nom_row);
            text_date = (TextView) view.findViewById(R.id.text_date_row);
            text_time = (TextView) view.findViewById(R.id.text_time_row);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ModelSms> list_SMS) {
        listSMS = list_SMS;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sms_row, parent, false);


        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelSms modelSms = listSMS.get(position);

        holder.text_sms.setText(modelSms.getTexte());
        holder.text_num.setText(modelSms.getNumeroTelephone());
        holder.text_nom.setText(modelSms.getNomContact());
        holder.text_date.setText(modelSms.getDateText());
        holder.text_time.setText(modelSms.getTempsText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listSMS.size();
    }
}


