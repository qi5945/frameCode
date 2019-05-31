package com.micropole.chebianjie.gridSelected;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.micropole.chebianjie.R;
import com.micropole.librarybase.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {

  private List<String> paths = new ArrayList<>();
  private Context mContext;
  private LayoutInflater mLayoutInflater;


  public PhotoPagerAdapter(Context mContext, List<String> paths) {
    this.mContext = mContext;
    this.paths = paths;
    mLayoutInflater = LayoutInflater.from(mContext);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {

    View itemView = mLayoutInflater.inflate(R.layout.item_pager, container, false);

    ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

//    final String path = paths.get(position);
//    final Uri uri;
//    if (path.startsWith("http")) {
//      uri = Uri.parse(path);
//    } else {
//      uri = Uri.fromFile(new File(path));
//    }
//    RequestOptions options = new RequestOptions();
//    options.placeholder(R.mipmap.course_dk);
//    options.error(R.mipmap.course_dk);
//
//    Glide.with(mContext)
//        .load(paths.get(position))
//        .into(imageView);
    ImageLoader.loadToUrl2(mContext,imageView,paths.get(position));
//    GlideImgManager.glideLoader(mContext, paths.get(position), imageView);

    container.addView(itemView);

    return itemView;
  }


  @Override
  public int getCount() {
    return paths.size();
  }


  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
