package com.micropole.chebianjie.test;

import com.xx.baseuilibrary.mvp.lcec.BaseMvpLcecView;
import com.xx.baseutilslibrary.entity.BaseResponseEntity;
import io.reactivex.Observable;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName HomePostContract
 * @Model todo
 * @Description 首页帖子
 * @Author chen qi hao
 * @Sign 沉迷学习不能自拔
 * @Date 2018/10/15 14:00
 * @Email 371232886@qq.com
 * @Copyright Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */

public class HomePostContract {

    public interface View extends BaseMvpLcecView<List<PostEntity>> {
        boolean isDefault();
        String getSort();
        String getOrder();
        String getClassifyId();
    }

    public interface Presenter {
        void loadData(boolean isRefresh, int page);
    }

    public interface Model {
        Observable<BaseResponseEntity<List<PostEntity>>> loadData(HashMap<String, Object> map);
    }
}
