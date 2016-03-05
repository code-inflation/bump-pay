package me.sixhackathon.bumppay.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ch.uepaa.p2pkit.P2PKitClient;
import ch.uepaa.p2pkit.discovery.entity.Peer;
import ch.uepaa.p2pkit.internal.messaging.MessageTooLargeException;
import me.sixhackathon.bumppay.MainActivity;

public class AsyncMessageRunner extends AsyncTask {

    private Peer peer;
    private Context appContext;

    public AsyncMessageRunner(Peer peer, Context appContext) {
        super();
        this.peer = peer;
        this.appContext = appContext;
    }

    @Override
    protected Void doInBackground(Object[] params) {
        // send message to all connected peers
        try {
            boolean forwarded = P2PKitClient.getInstance(appContext).getMessageServices().sendMessage(peer.getNodeId(), "text/plain", "Hello!".getBytes());
            if (forwarded){
                Log.i(MainActivity.class.toString(), "Message sent to " + peer.getNodeId());
            }
        } catch (MessageTooLargeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
