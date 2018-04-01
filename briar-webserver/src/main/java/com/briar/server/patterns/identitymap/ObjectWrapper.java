package com.briar.server.patterns.identitymap;

import com.briar.server.constants.Constants;

import static java.lang.Thread.yield;

public class ObjectWrapper<Payload> {

        private Payload payload;
        private int nbObjectReading;
        private boolean isThreadWriting;
        private boolean isToBeDeleted;

        public ObjectWrapper(Payload payload) {
            this.payload = payload;
            this.nbObjectReading = 0;
            this.isThreadWriting = false;
            this.isToBeDeleted = false;
        }

        private Payload getPayload() {
            return this.payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }

        public boolean isPayloadToBeDeleted() {
            return this.isToBeDeleted;
        }

        private Payload startPayloadDeleting() {
            this.isToBeDeleted = true;
            return getPayload();
        }

        public synchronized Payload startReadWriteDeleteAction(Constants.Lock lock) {
            if(isPayloadToBeDeleted()) {
                return null;
            }
            switch(lock) {
                case reading:
                    return startReading();

                case writing:
                    return startWriting();
                case deleting:
                    return startPayloadDeleting();
                default:
                    return null;
            }
        }

        private synchronized Payload startReading() {
            while(this.isThreadWriting) {
                yield();
            }
            ++this.nbObjectReading;
            return getPayload();
        }

        private synchronized Payload startWriting() {
            while(this.nbObjectReading != 0 || this.isThreadWriting) {
                yield();
            }
            this.isThreadWriting = true;
            return getPayload();
        }

        public synchronized void stopReading() {
            if (this.nbObjectReading > 0) {
                --this.nbObjectReading;
            }
        }

        public synchronized void stopWriting() {
            this.isThreadWriting = false;
        }

}
