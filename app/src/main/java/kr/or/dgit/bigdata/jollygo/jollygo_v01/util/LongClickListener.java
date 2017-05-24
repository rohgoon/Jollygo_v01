package kr.or.dgit.bigdata.jollygo.jollygo_v01.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;

/**
 * Created by NCG on 2017-05-24.
 */

public class LongClickListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {
        ClipData.Item item = new ClipData.Item(
                (CharSequence) view.getTag());

        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        ClipData data = new ClipData(view.getTag().toString(),
                mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                view);

        view.startDrag(data, // data to be dragged
                shadowBuilder, // drag shadow
                view, // target  Vew
                0 // no need flag
        );

        view.setVisibility(View.INVISIBLE);
        return true;

    }
}
