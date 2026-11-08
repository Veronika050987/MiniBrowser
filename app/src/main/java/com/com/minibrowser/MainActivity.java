package com.com.minibrowser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import android.os.SystemClock;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;

public class MainActivity extends AppCompatActivity {

    EditText urlInput;
    Button goButton;
    WebView webView;

    long backPressedTime = 0;
    private static final int EXIT_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlInput);
        goButton = findViewById(R.id.goButton);
        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //метод настройки WebView, который разрешает веб-страницам,
        // загруженным в вашем приложении, использовать DOM Storage API
        // (включая localStorage и sessionStorage)

        webView.setWebViewClient(new WebViewClient());

        goButton.setOnClickListener(v -> loadUrlFromInput());

        urlInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrlFromInput();
                return true;
            }
            return false;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                long now = SystemClock.elapsedRealtime();
                //возвращает время с момента включения телефона
                if (now - backPressedTime<EXIT_DELAY){
                    finish();
                }
                else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
    private void loadUrlFromInput() {
        String input = urlInput.getText().toString().trim();

        //android.widget.Toast.makeText(this, "Кнопка нажата! Введено: " + input, android.widget.Toast.LENGTH_SHORT).show();

        if (input.isEmpty()) return;

        String url = (!input.startsWith("http://") && !input.startsWith("https://"))
                ? "https://" + input
                : input;

        webView.loadUrl(url);
    }
}