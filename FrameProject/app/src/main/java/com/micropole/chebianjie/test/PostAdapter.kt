package com.micropole.chebianjie.test

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.micropole.chebianjie.R
import com.micropole.librarybase.utils.ImageLoader

/**
 * @ClassName       PostAdapter
 * @Model           todo
 * @Description     帖子适配器
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/10/15 14:00
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class PostAdapter(data: List<PostEntity>?) :
    BaseQuickAdapter<PostEntity, BaseViewHolder>(R.layout.item_my_post, data) {

    override fun convert(helper: BaseViewHolder?, item: PostEntity?) {
        helper!!.setText(R.id.tv_title, item!!.title)
        helper.setText(R.id.tv_date, item.create_time)
        helper.setText(R.id.tv_name, item.article_to_user.name)
        if (item.image.indexOf(",") != -1) {
            ImageLoader.loadToUrl(mContext, helper.getView(R.id.iv_pic), "")
        } else
            ImageLoader.loadToUrl(mContext, helper.getView(R.id.iv_pic), "")
        ImageLoader.loadAvatar(mContext, helper.getView(R.id.civ_avatar), "")
        if (item.is_vip == "1") {
            //Vip观看
//            helper.getView<SlantedTextView>(R.id.stv).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.tv_bound).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.tv_bound).text = item.article_gold + "币"
        } else {
//            helper.getView<SlantedTextView>(R.id.stv).visibility = View.GONE
            helper.getView<TextView>(R.id.tv_bound).visibility = View.GONE
        }

        if (item.is_top == "1") {
            //置顶
            helper.getView<TextView>(R.id.tv_top).visibility = View.VISIBLE
        } else
            helper.getView<TextView>(R.id.tv_top).visibility = View.GONE
    }
}