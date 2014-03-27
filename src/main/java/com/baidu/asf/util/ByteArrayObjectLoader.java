package com.baidu.asf.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * Constructs object instance from byte stream
 */
public class ByteArrayObjectLoader<T> implements ObjectLazyLoader<T> {

    private final byte[] objBytes;

    private T obj;

    public ByteArrayObjectLoader(byte[] objBytes) {
        this.objBytes = objBytes;
    }

    @Override
    public T getObject() {

        if (objBytes == null) {
            return null;
        }

        if (obj == null) {
            synchronized (this) {
                if (obj == null) {
                    ByteArrayInputStream bin = new ByteArrayInputStream((byte[]) objBytes);
                    try {
                        ObjectInput objectInput = new ObjectInputStream(bin);
                        obj = (T) objectInput.readObject();
                    } catch (IOException e) {
                        throw new ObjectConstructException(e);
                    } catch (ClassNotFoundException e) {
                        throw new ObjectConstructException(e);
                    }
                }
            }
        }
        return obj;
    }
}
