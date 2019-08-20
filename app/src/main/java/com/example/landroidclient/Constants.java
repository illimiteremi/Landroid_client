package com.example.landroidclient;

public class Constants {

    public enum COMMANDE {

        COMMANDE_UNKNOW      ( -2, "UNKNOW"),
        DIRECTION_CENTER     (-1, "DIRECTION CENTER"),
        DIRECTION_LEFT       ( 0, "DIRECTION LEFT"),
        DIRECTION_LEFT_UP    ( 1, "DIRECTION LEFT UP"),
        DIRECTION_UP         ( 2, "DIRECTION UP"),
        DIRECTION_UP_RIGHT   ( 3, "DIRECTION UP RIGHT"),
        DIRECTION_RIGHT      ( 4, "DIRECTION RIGHT"),
        DIRECTION_RIGHT_DOWN ( 5, "DIRECTION RIGHT DOWN"),
        DIRECTION_DOWN       ( 6, "DIRECTION DOWN"),
        DIRECTION_DOWN_LEFT  ( 7, "DIRECTION DOWN LEFT"),
        BRUSH_START          ( 8, "BRUSH START"),
        BRUSH_STOP           ( 9, "BRUSH STOP"),
        START_ALONE_MODE     ( 10, "MODE ALONE START"),
        STOP_ALONE_MODE      ( 11, "MODE ALONE STOP"),
        TEST_LANDROID        ( 12, "TEST LANDROID SERVER"),
        HALT_SYSTEM          ( 13, "HALT LANDROID SERVER");

        private byte    code;
        private String  message;

        /**
         * COMMANDE constructor
         * @param _code
         * @param _message
         */
        COMMANDE(int _code, String _message) {
            code       = (byte) _code;
            message   = _message;
        }

        /**
         * getCode
         * @return
         */
        public byte getCode() { return code; }

        /**
         * getLibelle
         * @return
         */
        public String getMessage() {
            return message;
        }

        /**
         * getValue
         * @param code
         * @return
         */
        public static COMMANDE getValue(byte code) {
            for(COMMANDE e: COMMANDE.values()) {
                if(e.code == code) {
                    return e;
                }
            }
            return COMMANDE_UNKNOW; // not found
        }
    }

    public enum RESPONSE {

        COMMANDE_UNKNOW      ( -1, "UNKNOW"),
        SUCCESS              (  1,   "SUCCESS"),
        FAILL                (  0,  "FAILLED");

        private byte    code;
        private String  message;

        /**
         * RESPONSE constructor
         * @param _code
         * @param _message
         */
        RESPONSE(int _code, String _message) {
            code       = (byte) _code;
            message   = _message;
        }

        /**
         * getCode
         * @return
         */
        public byte getCode() { return code; }

        /**
         * getLibelle
         * @return
         */
        public String getMessage() {
            return message;
        }

        /**
         * getValue
         * @param code
         * @return
         */
        public static RESPONSE getValue(byte code) {
            for(RESPONSE e: RESPONSE.values()) {
                if(e.code == code) {
                    return e;
                }
            }
            return COMMANDE_UNKNOW; // not found
        }
    }

}