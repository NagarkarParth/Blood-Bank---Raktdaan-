package com.example.bloodbanksystem;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DonationCertificateActivity extends AppCompatActivity {

    private EditText etDonorName, etDonationDate;
    private Button btnDownloadPDF;
    private View certificateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donationcertificate);

        etDonorName = findViewById(R.id.etDonorName);
        etDonationDate = findViewById(R.id.etDonationDate);
        btnDownloadPDF = findViewById(R.id.btnDownloadPDF);
        certificateLayout = findViewById(R.id.certificateLayout); // Root layout for the certificate

        // Get donor details from intent
        String donorName = getIntent().getStringExtra("donor_name");
        String donationDate = getIntent().getStringExtra("donation_date");

        if (donorName != null) {
            etDonorName.setText(donorName);
        }
        if (donationDate != null) {
            etDonationDate.setText(donationDate);
        }

        btnDownloadPDF.setOnClickListener(v -> generateAndSavePDF());
    }

    private void generateAndSavePDF() {
        // Hide the download button before capturing the layout
        btnDownloadPDF.setVisibility(View.GONE);

        // Convert Layout to Bitmap
        Bitmap bitmap = Bitmap.createBitmap(certificateLayout.getWidth(), certificateLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        certificateLayout.draw(canvas);

        // Show the download button again after capturing the layout
        btnDownloadPDF.setVisibility(View.VISIBLE);

        // Create PDF
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas pdfCanvas = page.getCanvas();
        pdfCanvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // Save PDF
        savePDF(document);
    }

    private void savePDF(PdfDocument document) {
        String fileName = "Blood_Donation_Certificate.pdf";
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");

        Uri pdfUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            pdfUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        } else {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                document.writeTo(fos);
                Toast.makeText(this, "PDF saved to Downloads!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving PDF!", Toast.LENGTH_SHORT).show();
            }
            document.close();
            return;
        }

        // Save PDF in MediaStore
        if (pdfUri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(pdfUri)) {
                document.writeTo(outputStream);
                Toast.makeText(this, "PDF saved in Downloads!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving PDF!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to create file!", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}