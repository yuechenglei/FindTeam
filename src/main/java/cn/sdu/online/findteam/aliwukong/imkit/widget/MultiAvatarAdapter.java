package cn.sdu.online.findteam.aliwukong.imkit.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.alibaba.doraemon.image.ImageDecoder;
import com.alibaba.doraemon.image.ImageMagician;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.aliwukong.avatar.AvatarImageDecoder;
import cn.sdu.online.findteam.aliwukong.avatar.AvatarMagicianImpl;
import cn.sdu.online.findteam.net.NetCore;
import cn.sdu.online.findteam.share.MyApplication;
import cn.sdu.online.findteam.util.AndTools;
import cn.sdu.online.findteam.view.RoundImageView;

/**
 * Created by wn on 2015/8/14.
 */
public class MultiAvatarAdapter extends CustomGridAdapter<String> {
    private AbsListView parent;
    private AbsListView mListView;
    private ImageDecoder mImageDecoder;
    private ImageMagician mImageMagician;
    private Bitmap mDefaultAvatar;
    //    private static final int KEY_ID = 2014124145;
    private static final int KEY_URL = 2015011319;
    private static final String VALUE_NIL = "NIL";
    private static final String TAG = "AvatarMagician";

    String imgPath = "";
    ImageView item;

    public MultiAvatarAdapter(Context context, AbsListView parent) {
        super(context);
        this.parent = parent;

//		setBlockSize(AndTools.dp2px(context, 21), AndTools.dp2px(context, 21));
        setSpace(AndTools.dp2px(mContext, 1), AndTools.dp2px(mContext, 1));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setImageDecoder(ImageDecoder imageDecoder) {
        this.mImageDecoder = imageDecoder;
    }

    public void setDefaultDrawable(Bitmap defaultAvatar) {
        this.mDefaultAvatar = defaultAvatar;
    }

    public void setImageMagician(ImageMagician imageMagician) {
        this.mImageMagician = imageMagician;
    }

    public void setListView(AbsListView listView) {
        mListView = listView;
    }

    @Override
    public void setColumnNum(int num) {
        super.setColumnNum(num);
        if (1 == num) {
            setBlockSize(AndTools.dp2px(mContext, 50), AndTools.dp2px(mContext, 50));
        } else if (2 == num) {
            setBlockSize(AndTools.dp2px(mContext, 24), AndTools.dp2px(mContext, 24));
        } else {
            setBlockSize(AndTools.dp2px(mContext, 16), AndTools.dp2px(mContext, 16));
        }
    }

    @Override
    public void setList(List<String> itemlist) {
        if (itemlist == null) {
            return;
        }
        if (mList != null) {
            mList.clear();
        } else {
            mList = new ArrayList<String>();
        }

        mList.addAll(itemlist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item = (ImageView) convertView;
        if (item == null) {
            item = new ImageView(mContext);

            if (mImageDecoder != null) {
                item.setTag(AvatarMagicianImpl.KEY_TAG, TAG);//用于在url2key中区别其他地方的view
                // TODO WKNEW
                item.setTag(AvatarImageDecoder.SELFDECODERTAG, mImageDecoder);
            }
        }

        item.setImageResource(R.drawable.head_moren);
        imgPath = getItem(position);
/*        String urlTag = (String) item.getTag(KEY_URL);*/
        if (!imgPath.equals("-1")) {
            ImageLoader imageLoader = new ImageLoader(MyApplication.getQueues(), MyApplication.bitmapCache);
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(item, R.drawable.head_moren,
                    R.drawable.head_moren);
            imageLoader.get(imgPath, imageListener);
        }
        return item;
    }
}
