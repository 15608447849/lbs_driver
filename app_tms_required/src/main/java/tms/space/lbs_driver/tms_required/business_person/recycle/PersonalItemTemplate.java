package tms.space.lbs_driver.tms_required.business_person.recycle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;

import tms.space.lbs_driver.tms_base.recycler.RecyclerViewHolderAbs;
import tms.space.lbs_driver.tms_required.R;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 * 列表每行的数据
 */
@RidClass(R.id.class)
public class PersonalItemTemplate extends RecyclerViewHolderAbs<PersonalItemDataBean>{

    @RidName("rec_item_person_iv")
    public ImageView imageView;

    @RidName("rec_item_person_tv")
    public TextView textView;

    public PersonalItemTemplate(View itemView) {
       super(itemView);
    }

    @Override
    public void bindData(PersonalItemDataBean d) {
        textView.setText(d.getTitle());
        if (d.getImageId()>0){
            imageView.setImageResource(d.getImageId());
        }
    }
}
