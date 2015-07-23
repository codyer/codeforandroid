package com.pao123.common;

import java.util.List;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SdkAsyncCaller<T>
{
    private static final String TAG = "SdkAsyncCaller";
    
    private static class SdkObjectHandler<T> extends Handler {
        
        private CallbackObject<T> callback;
        public SdkObjectHandler(CallbackObject<T> callback) {
            this.callback = callback;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message m) {
            Log.d(TAG, "handle msg: " + m.what);
            switch (m.what) {
                case 0:
                    this.callback.callback((T) m.obj);
                    break;
                default:
                    break;
            }
        }
    };
    
    private static class SdkArrayHandler<T> extends Handler {
        
        private CallbackArray<T> callback;
        public SdkArrayHandler(CallbackArray<T> callback) {
            this.callback = callback;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message m) {
            Log.d(TAG, "handle msg: " + m.what);
            switch (m.what) {
                case 0:
                    this.callback.callback((List<T>) m.obj);
                    break;
                default:
                    break;
            }
        }
    };
    
    private boolean useHandler = true;
    public void noHandler()
    {
        this.useHandler = false;
    }
 
    public void postObject(final PaoSdk sdk, final Class<T> clazz, final CallbackObject<T> callback)
    {
        final SdkObjectHandler<T> objectHandler = new SdkObjectHandler<T>(callback);
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                T obj = sdk.postObject(clazz);
                if (useHandler) {
                    objectHandler.obtainMessage(0, obj).sendToTarget();
                } else {
                    callback.callback(obj);
                }
            }
            
        }, "SDK Thread").start();
    }
    
    public void postArray(final PaoSdk sdk, final Class<T> clazz, final CallbackArray<T> callback)
    {
        final SdkArrayHandler<T> arrayHandler = new SdkArrayHandler<T>(callback);
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                List<T> list = sdk.postArray(clazz);
                if (useHandler) {
                    arrayHandler.obtainMessage(0, list).sendToTarget();
                } else {
                    callback.callback(list);
                }
            }
            
        }, "SDK Thread").start();
    }
}
