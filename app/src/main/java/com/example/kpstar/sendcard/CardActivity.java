package com.example.kpstar.sendcard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.grantland.widget.AutofitTextView;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mImgProfile  = null;
    Button mSendBtn = null;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int CAMERA_REQUEST = 2;
    TextView mTemp = null;
    public SharedPreferences sharedPreferences;
    public Uri selectedImage = null;
    public String imageUrl ;
    int mWidth;
    float mHeight,mSize;
    private byte[] dataimage;
    Intent i;

    LinearLayout mLayout  = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        mHeight = (float)mWidth/30.0f;
        Float mHe = (float)mWidth/25.0f;

        mImgProfile = (ImageView)findViewById(R.id.imageProfile);
        mImgProfile.setMaxWidth(mWidth/2);
        mImgProfile.setMinimumWidth(mWidth/2);
        Bitmap bitmap = null;

        mSendBtn = (Button)findViewById(R.id.sendBtn);
        mSendBtn.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);

        String input = sharedPreferences.getString("image", "");
        if (input.isEmpty()) {
            mImgProfile.setImageDrawable(getDrawable(R.drawable.noimage));
        } else {
            bitmap = decodeBase64(input);
            mImgProfile.setImageBitmap(bitmap);
        }

        mImgProfile.setOnClickListener(this);

        AutofitTextView mTemps = null;
        mTemps = (AutofitTextView) findViewById(R.id.edtName);
        mTemps.setText(sharedPreferences.getString("fullname", ""));

        mTemp = (TextView)findViewById(R.id.edtJob);
        mTemp.setText(sharedPreferences.getString("jobtitle", ""));
        mTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight);

        mTemps = (AutofitTextView) findViewById(R.id.edtCompany);
        mTemps.setText(sharedPreferences.getString("companyname", ""));

        mTemp = (TextView)findViewById(R.id.edtPhone);
        mTemp.setText(sharedPreferences.getString("phone", ""));
        mTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight);

        mTemps = (AutofitTextView)findViewById(R.id.edtEmail);
        String email = sharedPreferences.getString("email", "");
        mTemps.setText(email);
        float emailWidth;

        mTemp = (TextView)findViewById(R.id.edtAddress);
        email = sharedPreferences.getString("address", "");
        emailWidth = mHeight;
        mTemp.setText(email);
        mTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX, emailWidth);

        mTemp = (TextView)findViewById(R.id.edtWebsite);
        email = sharedPreferences.getString("website", "");
        emailWidth = mHeight;
        if (email.length() > 20) {
            emailWidth = (float) (mWidth/((email.length()-5)*2.0f));
        }
        mTemp.setText(email);
        mTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX, emailWidth);

        mLayout = (LinearLayout)findViewById(R.id.cardLayout);
        RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
        lparams.height = mWidth/2;
        mLayout.setLayoutParams(lparams);

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageProfile:
                PopupMenu popup = new PopupMenu(this, mImgProfile);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Gallery:
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(i, RESULT_LOAD_IMAGE);
                                break;
                            case R.id.takePhoto:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                        Toast.makeText(CardActivity.this, "Error occured while creating image file", Toast.LENGTH_SHORT).show();
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(CardActivity.this,
                                                "com.example.kpstar.pictures",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.sendBtn:

                mLayout = (LinearLayout)findViewById(R.id.cardLayout);
                Bitmap bitmap = getBitmapFromView(mLayout);
                selectedImage = getImageUrl(getApplicationContext(), bitmap);

                popup = new PopupMenu(this, mSendBtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.email:
                                sendEmail();
                                break;
                            case R.id.sms:
                                sendSMS();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
        }
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/image");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Business Card");
        intent.putExtra(Intent.EXTRA_TEXT, getTextBody());
        intent.putExtra(Intent.EXTRA_STREAM, selectedImage);
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private String getTextBody() {
        //return "Please Share this image on the Social Media platform you choose. Your share & referral will help bring in my next sale. Thank You";
        return "Please Share this image on the Social Media platform of your choice.\n" + "Your share & referral will help bring in my next customer.\n" +
                "Thank You\n";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            imageUrl = getRealPathFromURI(selectedImage, this);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
//            dataimage = getImageUri(this,(Bitmap)(BitmapFactory.decodeFile(picturePath)));
            Bitmap bitmap = (Bitmap)BitmapFactory.decodeFile(picturePath);
            try {
                ExifInterface ei = new ExifInterface(picturePath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                encodeTobase64(rotatedBitmap);
                mImgProfile.setImageBitmap(rotatedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            int targetW = mImgProfile.getWidth();
            int targetH = mImgProfile.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            try {
                ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                encodeTobase64(rotatedBitmap);
                mImgProfile.setImageBitmap(rotatedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageEncoded);
        editor.commit();

        //return imageEncoded;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Uri getImageUrl(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Uri url = null;
        try {
            url = Uri.parse(path);
        } catch (Exception e) {
            this.finish();
        }
        return url;
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    private void sendSMS() {
        i = new Intent(Intent.ACTION_SEND, Uri.fromParts("sms", "", null));
        i.putExtra("sms_body", getTextBody());
        i.putExtra(Intent.EXTRA_STREAM, selectedImage);
        i.setType("image/png");
        startActivity(Intent.createChooser(i, "Send message"));
    }
}
