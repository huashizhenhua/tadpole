/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: G:\\Users\\Administrator\\workspace\\Tadpole\\src\\org\\tadpole\\aidl\\TestServiceConnect.aidl
 */
package org.tadpole.aidl;
public interface TestServiceConnect extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.tadpole.aidl.TestServiceConnect
{
private static final java.lang.String DESCRIPTOR = "org.tadpole.aidl.TestServiceConnect";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.tadpole.aidl.TestServiceConnect interface,
 * generating a proxy if needed.
 */
public static org.tadpole.aidl.TestServiceConnect asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.tadpole.aidl.TestServiceConnect))) {
return ((org.tadpole.aidl.TestServiceConnect)iin);
}
return new org.tadpole.aidl.TestServiceConnect.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_test:
{
data.enforceInterface(DESCRIPTOR);
this.test();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.tadpole.aidl.TestServiceConnect
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void test() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_test, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_test = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void test() throws android.os.RemoteException;
}
