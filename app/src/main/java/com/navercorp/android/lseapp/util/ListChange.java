package com.navercorp.android.lseapp.util;

/**
 * Created by NAVER on 2017-07-27.
 */

public interface ListChange {

    public static enum Type {
        INSERT,
        DELETE,
        REPLACE,
        MOVE,
        @Deprecated
        UNUSED;
    }

    public Type type();

    public static class Insert implements ListChange {

        public final int index;

        public Insert(int index) {
            this.index = index;
        }

        @Override
        public Type type() {
            return Type.INSERT;
        }
    }

    public static class Delete implements ListChange {

        public final int index;

        public Delete(int index) {
            this.index = index;
        }

        @Override
        public Type type() {
            return Type.DELETE;
        }
    }

    public static class Replace implements ListChange {

        public final int index;

        public Replace(int index) {
            this.index = index;
        }

        @Override
        public Type type() {
            return Type.REPLACE;
        }
    }

    public static class Move implements ListChange {

        public final int fromIndex;
        public final int toIndex;

        public Move(int fromIndex, int toIndex) {
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        public Type type() {
            return Type.MOVE;
        }
    }
}
