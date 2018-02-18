package ca326.com.activities;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

public class CustomVideoView extends TextureView implements TextureView.SurfaceTextureListener {
    private MediaPlayer mMediaPlayer;
    private Uri mSource;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private boolean isLooping = false;
    private Context context;


    public CustomVideoView(Context context) {
        this(context, null, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSurfaceTextureListener(this);
    }

    public void setSource(Uri source) {
        mSource = source;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mCompletionListener = listener;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    @Override
    protected void onDetachedFromWindow() {
        // release resources on detach
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
//            mMediaPlayer.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener) this);
//            mMediaPlayer.setOnErrorListener((MediaPlayer.OnErrorListener) this);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setDataSource(getContext(), mSource);
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.prepare();

            mMediaPlayer.start();

            mMediaPlayer.stop();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mMediaPlayer.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
}
