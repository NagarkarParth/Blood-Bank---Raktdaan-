package com.example.bloodbanksystem;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class UserActivity extends AppCompatActivity {
    private Button Bloodbankbtn, btnDonor, btnConsent, btnCamp;
    private TextView aboutus, contactus;
    private LottieAnimationView lottieAnimationView;
    private ImageView sidebarMenu;
    private RelativeLayout sidebarOverlay;
    private LinearLayout sidebarMenuLayout;
    private Button btnCertificate, btnSidebarAbout, btnSidebarContact, btnLogout;

    // New Buttons
    private Button btnShare, btnHelpUs, btnRateUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Initialize Lottie Animation
        lottieAnimationView = findViewById(R.id.lottieAnimation);
        lottieAnimationView.playAnimation();

        // Sidebar Components
        sidebarMenu = findViewById(R.id.iv_sidebar);
        sidebarOverlay = findViewById(R.id.sidebar_overlay);
        sidebarMenuLayout = findViewById(R.id.sidebar_menu);

        btnCertificate = findViewById(R.id.sidebar_certificate);
        btnSidebarAbout = findViewById(R.id.sidebar_about_us);
        btnSidebarContact = findViewById(R.id.sidebar_contact_us);
        btnLogout = findViewById(R.id.sidebar_logout);

        // New Sidebar Buttons
        btnShare = findViewById(R.id.sidebar_share);
        btnHelpUs = findViewById(R.id.sidebar_help_us);
        btnRateUs = findViewById(R.id.sidebar_rate_us);

        // Toggle Sidebar Menu
        sidebarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidebarMenuLayout.setVisibility(View.VISIBLE);
                sidebarOverlay.setVisibility(View.VISIBLE);
            }
        });

        // Close Sidebar when Clicking Outside
        sidebarOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidebarMenuLayout.setVisibility(View.GONE);
                sidebarOverlay.setVisibility(View.GONE);
            }
        });

        // Sidebar Menu Actions
        btnCertificate.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, DonationCertificateActivity.class)));

        btnSidebarAbout.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, aboutus_Activity.class)));

        btnSidebarContact.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, contactus_Activity.class)));

        btnLogout.setOnClickListener(v -> finish());

        // Blood Bank Button
        Bloodbankbtn = findViewById(R.id.btn_blood_bank);
        Bloodbankbtn.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, SearchbloodbankActivity.class)));

        // Blood Donor Button
        btnDonor = findViewById(R.id.btn_blood_donor);
        btnDonor.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, donorActivity.class)));

        // About Us Section
        aboutus = findViewById(R.id.tv_about_us);
        aboutus.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, aboutus_Activity.class)));

        // Contact Us Section
        contactus = findViewById(R.id.tv_contact_us);
        contactus.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, contactus_Activity.class)));

        // Consent Form Button
        btnConsent = findViewById(R.id.btn_consent_form);
        btnConsent.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, BloodConsentActivity.class)));

        // Blood Donation Camp Button
        btnCamp = findViewById(R.id.btn_blood_camp);
        btnCamp.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, BloodCampListActivity.class)));

        // ===== NEW FUNCTIONALITY FOR SHARE, HELP US, RATE US =====

        // Share Button
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Blood Bank App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download the Blood Bank App from: https://yourappurl.com");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // Help Us Button
        btnHelpUs.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@bloodbank.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help Us - Feedback/Support");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi team,\n\nI would like to...");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

        // Rate Us Button
        btnRateUs = findViewById(R.id.sidebar_rate_us);
        btnRateUs.setOnClickListener(v ->
                startActivity(new Intent(UserActivity.this, RateUsActivity.class)));

    };
}