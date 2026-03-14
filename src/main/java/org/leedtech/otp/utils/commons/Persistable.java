package org.leedtech.otp.utils.commons;

import java.io.Serializable;

public interface Persistable<T extends Serializable> extends Identifiable {

    T getId();
}
