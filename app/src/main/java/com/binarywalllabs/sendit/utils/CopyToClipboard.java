package com.binarywalllabs.sendit.utils;

/**
 * Created by BANE on 3/8/2015.
 */
import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public final class CopyToClipboard extends IntentService {

    private final static String EXTRA_LABEL = "label";

    public static Intent createIntent(Context context, String text, String label) {
        Intent intent = new Intent(context, CopyToClipboard.class);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(EXTRA_LABEL, label);
        return intent;
    }

    public CopyToClipboard() {
        this(CopyToClipboard.class.getSimpleName());
    }

    public CopyToClipboard(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            copyToClipboard(
                    text,
                    intent.getStringExtra(EXTRA_LABEL)
            );
            show(getApplicationContext(),"Copied");
        }
        stopSelf();
    }

    private static void show(final Context context, final String message) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void copyToClipboard(String text, String label) {
        ClipData.Item item = new ClipData.Item(text);
        String[] mimeType = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData clipData = new ClipData(new ClipDescription(label, mimeType), item);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clipData);
    }
}
