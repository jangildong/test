package com.example.alphacar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacar.Announce_detail_Activity;
import com.example.alphacar.DTOS.NotiftDTO;
import com.example.alphacar.R;

import java.util.ArrayList;

public class NotifyAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<NotiftDTO> notiftDTOS;
    LayoutInflater inflater;
    NotiftDTO notiftDTO;

    public NotifyAdapter(Context mContext, ArrayList<NotiftDTO> notiftDTOS) {
        this.mContext = mContext;
        this.notiftDTOS = notiftDTOS;
               this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notiftDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotiViewHolder viewHolder;


        if (convertView == null){
            // 화면을 붙인다.
            convertView = inflater.inflate(R.layout.notify_item, null);

            viewHolder = new NotiViewHolder();
            // 붙인 화면과 아래에 생성한 뷰홀더를 연결한다.
            viewHolder.title = convertView.findViewById(R.id.noti_title);
            viewHolder.content = convertView.findViewById(R.id.noti_content);
            viewHolder.imageView = convertView.findViewById(R.id.noti_detail_photo);
     //       viewHolder.customer_email = convertView.findViewById(R.id.noti_customer_email);
     //       viewHolder.noti_id = convertView.findViewById(R.id.noti_id);

            convertView.setTag(viewHolder);
        }else {  // 캐시된 뷰가 있을경우 이미 생성된 뷰홀더를 사용한다.
            viewHolder = (NotiViewHolder) convertView.getTag();
        }

        NotiftDTO dto = notiftDTOS.get(position);
    //    String id = String.valueOf(dto.getNotice_id());
        String title = dto.getNotice_title();
        String content = dto.getNotice_writedate();
   //     String customer_email = dto.getCustomer_email();

    //    viewHolder.noti_id.setText(id);

        viewHolder.title.setText(title);
        viewHolder.content.setText(content);
   //     viewHolder.customer_email.setText(customer_email);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  //              Toast.makeText(mContext, id , Toast.LENGTH_SHORT).show();
                /* 공지사항 클릭시 상세정보 */

               Intent intent = new Intent(mContext, Announce_detail_Activity.class);
               intent.putExtra("notice_id",dto.getNotice_id());
               intent.putExtra("customer_email",dto.getCustomer_email());
               intent.putExtra("notice_title",dto.getNotice_title());
               intent.putExtra("notice_content",dto.getNotice_content());
               intent.putExtra("notice_file", dto.getNotice_filepath());
               mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    public class NotiViewHolder{
        public TextView title, content, customer_email, noti_id;
        public ImageView imageView;
    }
}
