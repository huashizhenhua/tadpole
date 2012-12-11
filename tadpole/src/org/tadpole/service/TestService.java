package org.tadpole.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.tadpole.aidl.TestServiceConnect;
import org.tadpole.app.PluginActivity;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class TestService extends Service {
	
	
	private static final String TAG = "TestService";
	
	@Override
	public IBinder onBind(Intent arg0) {
			
		 return mBinder;
	}
	
	private TestServiceConnect.Stub mBinder = new TestServiceConnect.Stub() {
		@Override
		public void test() throws RemoteException {
			System.out.println("Test in TestServiceConnect.Stub");
			CountDownLatch cdl = new CountDownLatch(1);
			Log.i(TAG, "wait 5 second to verify alixpay's implement method");
			try {
				cdl.await(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Intent intent = new Intent();
			intent.setClass(TestService.this, PluginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
			
			Log.i(TAG, "wait end ...");
		}
	};
	
}
