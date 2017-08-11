package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-08-11.
 */

public interface ObjectType<E extends Enum<E> & ObjectType<E>> {

    int getGlobalTypeSerial();

    Class<? extends ObjectType>[] REGISTRY = RegistryHelper.getAll();

    enum RegistryHelper {

        ;

        public static Class<? extends ObjectType>[] getAll() {
            final Class<?>[] registry = {
                    ArticleType.class,
                    DocumentComponentType.class,
            };
            return (Class<? extends ObjectType>[]) registry;
        }
    }
}
