package biz.nellemann.libsensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Sensor {


    /**
     * Various Configurable Options
     */

    public int doAverageOver = 10;


    /**
     * Private Fields
     */

    private int avgIntCounter;
    private int[] avgIntArray;


    /**
     *  Telegram Handler Setup
     */
    protected TelegramHandler telegramHandler;

    public void setTelegramHandler(TelegramHandler telegramHandler) {
        this.telegramHandler = telegramHandler;
    }

    public TelegramHandler getTelegramHandler() {
        return telegramHandler;
    }




    /**
     * Event Listener Configuration
     */

    protected List<TelegramListener> eventListeners = new ArrayList<>();

    public synchronized void addEventListener( TelegramListener l ) {
        eventListeners.add( l );
    }

    public synchronized void removeEventListener( TelegramListener l ) {
        eventListeners.remove( l );
    }




    /**
     * Measurement Handling
     */


    protected void clear() {
        avgIntCounter = 0;
        avgIntArray = new int[this.doAverageOver];
    }


    protected void onMeasurement(int measurement) {

        if(measurement < 99) {
            sendError(measurement);
            return;
        }

        if(doAverageOver > 0) {
            //log.info("Adding to avg. buffer: " + measurement);
            avgIntArray[avgIntCounter] = measurement;
            avgIntCounter++;
        } else {
            //log.info("Send event: " + measurement);
            sendEvent(measurement);
        }

        if(doAverageOver > 0 && avgIntCounter >= avgIntArray.length) {
            avgIntCounter = 0;
            Double avg = Arrays.stream(avgIntArray).average().orElse(Double.NaN);
            //log.info("Send event: " + avg.intValue());
            sendEvent(avg.intValue());
        }

    }


    private synchronized void sendEvent(int measurement) {
        TelegramResultEvent event = new TelegramResultEvent( this, measurement );
        Iterator listeners = eventListeners.iterator();
        while( listeners.hasNext() ) {
            ( (TelegramListener) listeners.next() ).onTelegramResultEvent( event );
        }
    }


    private synchronized void sendError(int errorCode) {

        TelegramError error;
        switch (errorCode) {
            case 0:
                error = TelegramError.TARGET_0;
                break;
            case 1:
                error = TelegramError.TARGET_1;
                break;
            case 2:
                error = TelegramError.TARGET_2;
                break;
            case 4:
                error = TelegramError.LIGHT_4;
                break;
            case 5:
                error = TelegramError.LIGHT_5;
                break;
            case 6:
                error = TelegramError.LIGHT_6;
                break;
            default:
                error = TelegramError.UNKNOWN;
                break;
        }

        TelegramErrorEvent event = new TelegramErrorEvent( this, error );
        Iterator listeners = eventListeners.iterator();
        while( listeners.hasNext() ) {
            ( (TelegramListener) listeners.next() ).onTelegramErrorEvent( event );
        }
    }

}
