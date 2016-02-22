package me.khrystal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import me.khrystal.sample.R;

/**
 * Created by kHRYSTAL on 2015/11/3.
 */
public class WavePtrHeader extends FrameLayout implements PtrUIHandler {
    WaveView waveView;
    int i;
    public WavePtrHeader(Context context) {
        super(context);
        init();
    }

    public WavePtrHeader(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public WavePtrHeader(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.wave_ptr_header, this);
        waveView = (WaveView) view.findViewById(R.id.wave_view);
        waveView.setOriginalImage(R.mipmap.ic_launcher);
        waveView.setUltimateColor(android.R.color.holo_red_dark);
    }

    @Override
    public void onUIReset(PtrFrameLayout ptrFrameLayout) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        waveView.startRefreshing();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
        waveView.stopRefreshing();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b1, PtrIndicator ptrIndicator) {
    }


}
