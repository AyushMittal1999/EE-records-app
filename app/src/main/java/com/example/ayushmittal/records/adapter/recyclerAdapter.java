package com.example.ayushmittal.records.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayushmittal.records.R;
import com.example.ayushmittal.records.objectclass.equipment;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.myViewHolder> {
    @NonNull
private ArrayList<equipment> equipmentArrayList;
    public recyclerAdapter(ArrayList<equipment> arrayList){
        equipmentArrayList=arrayList;
    }

    @Override
    public myViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);

        myViewHolder vh=new myViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.myViewHolder holder, int position) {

        equipment object=equipmentArrayList.get(position);
        holder.name.setText(object.getName());
        holder.sno.setText("\n"+object.getSerialStart()+" to "+object.getSerialEnd());
        holder.dop.setText(object.getDop());
        holder.desc.setText("Checked By : "+object.getCheckedby()+"   \nConsignor : "+object.getConsignor()+"\n"+object.getDescription());
        holder.lab.setText("LAB: "+object.getLab());
        holder.price.setText("Rs: "+String.valueOf( object.getCost()));
        holder.bno.setText("bill number: "+object.getBillnumber());
        holder.itemno.setText(String.valueOf(object.getQtyStock()));

    }

    @Override
    public int getItemCount() {
        return equipmentArrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{


        TextView uid,name,desc,price,lab,dop,itemno,sno,bno;

        public myViewHolder(View itemView) {
            super(itemView);
             uid=(TextView)itemView.findViewById(R.id.equipid);
             name=(TextView)itemView.findViewById(R.id.equipname);
             desc=(TextView)itemView.findViewById(R.id.desc);
             price=(TextView)itemView.findViewById(R.id.equipprice);
             lab=(TextView)itemView.findViewById(R.id.lab);
             dop=(TextView)itemView.findViewById(R.id.dop);
             itemno=(TextView) itemView.findViewById(R.id.itemno);
             sno=(TextView)itemView.findViewById(R.id.equipid);
             bno=(TextView)itemView.findViewById(R.id.billnumber);


        }
    }


}
