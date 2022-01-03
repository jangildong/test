package com.example.alphacar;

import static com.example.alphacar.LoginPageActivity.loginDTO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alphacar.ATask.ReviewInsert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ReviewActivity extends AppCompatActivity {
    File imgFile = null;
    String imgFilePath = "", state = "";
    // 이미지처리가 정상적으로 되었을때 onActivityResult 에서 데이터를 받기 위한 코드
    public int reqPicCode = 1004;
    ReviewInsert reviewInsert ;
    Button btnBack;
    Button btnRegister;
    EditText editReview;
    EditText editTitle;
    ImageView addPhoto;
    RatingBar ratingBar;
    private final int PICK_IMAGE = 1111;
    private String profile = null;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    int store_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_register);

        Intent intent = getIntent();
        store_number = intent.getIntExtra("store_number", 0);

        btnBack = findViewById(R.id.btnBack);
        btnRegister = findViewById(R.id.btnRegister);
        editReview = findViewById(R.id.editReview);
        editTitle = findViewById(R.id.editTitle);
        addPhoto = findViewById(R.id.addPhoto);
        ratingBar = findViewById(R.id.ratingBar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 이미지뷰를 클릭하면 사진을 찍어서 데이터를 저장한다
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());
                String reviewTitle = editTitle.getText().toString();
                String reviewContent = editReview.getText().toString();
                String email = loginDTO.getCustomer_email();

                String imgPath = "";
                if (imgFilePath!="") {
                    imgPath = imgFilePath;
                }

                if(reviewTitle.length() == 0){
                    Toast.makeText(ReviewActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    editTitle.requestFocus();
                    return;
                }

                if(reviewContent.length() == 0){
                    Toast.makeText(ReviewActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    editReview.requestFocus();
                    return;
                }


                // 이정보를 비동기 Task 로 넘겨 서버에게 전달한다
                reviewInsert = new ReviewInsert(store_number,email, rating, reviewTitle, reviewContent, imgPath);
                try {
                    state = reviewInsert.execute().get().trim();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (state.equals("1")){
                    Toast.makeText(ReviewActivity.this, "리뷰가 정상적으로 등록되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(ReviewActivity.this, DetailActivity.class);
                    intent1.putExtra("store_number", store_number);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent1);
                }else {
                    Toast.makeText(ReviewActivity.this, "리뷰 등록에 실패하였습니다, 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // option
    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(ReviewActivity.this);

        alt_bld.setTitle("사진 업로드").setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        // 사진 촬영 클릭
                        takePicture();
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        //앨범에서 선택
                        pickFromGallery();
                    }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }


    private void pickFromGallery(){
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);




        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        addPhoto = findViewById(R.id.addPhoto);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                imgFilePath = selectedImageUri.toString();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    addPhoto.setImageURI(selectedImageUri);
                }
            }
        }
    }*/



    private void takePicture(){
        int permissionCheck = ContextCompat.checkSelfPermission(ReviewActivity.this, Manifest.permission.CAMERA);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            // 권한없음
            ActivityCompat.requestPermissions(ReviewActivity.this, new String[]{Manifest.permission.CAMERA},0);
        }else{
            // 암묵적인텐트 : 사진찍기(카메라를 불러옴)
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 일단 이 인텐트가 사용가능한지 체크
            if(picIntent.resolveActivity(getPackageManager()) != null){
                imgFile = null;
                // createFile 매소드를 이용하여 임시파일을 만듬
                imgFile = createFile();

                if(imgFile != null){
                    // API24 이상부터는 FileProvider 를 제공해야함
                    Uri imgUri = FileProvider.getUriForFile(getApplicationContext(),
                            getApplicationContext().getPackageName()+".fileprovider",
                            imgFile);
                    // 만약에 API24 이상이면
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){ // API24
                        picIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    }else {
                        picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                    }

                    startActivityForResult(picIntent, reqPicCode);
                }

            }
        }

    }
    private File createFile() {
        // 파일 이름을 만들기 위해 시간값을 생성함
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "My" + timestamp;
        // 사진파일을 저장하기 위한 경로
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File curFile = null;
        try {
            // 임시파일을 생성함(전체경로),  2번째 suffix 확장자:파일확장자(jpg)
            curFile = File.createTempFile(imageFileName, ".jpg"
                    , storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 스트링타입으로 임시파일이 있는 곳의 절대경로를 저장함
        imgFilePath = curFile.getAbsolutePath();

        return curFile;
    }

    // 사진찍은 후 데이터를 받는곳
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == reqPicCode && resultCode == RESULT_OK){
            // 저장처리를 함
            setPic();
        }

    }

    // 사진을 저장처리 하는 곳
    private void setPic() {
       // addPhoto = findViewById(R.id.addPhoto);
        // 이미지뷰의 크기 알아오기
        int targetW = addPhoto.getWidth();
        int targetH = addPhoto.getHeight();

        // 사진의 크기 가져오기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int photoW = options.outWidth;
        int photoH = options.outHeight;

        // 이미지 크기를 맟출비율을 결정
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // 이미지뷰의 크기에 맞게 이미지크기를 조절
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        // 비트맵 이미지를 생성
        Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
        // 이미지를 갤러리에 저장하기
        galleryAddPic(bitmap);
        // Glide.with(this).load("http://www.selphone.co.kr/homepage/img/team/3.jpg").into(imageView);
        Glide.with(this).load(bitmap).circleCrop().into(addPhoto);

        //imageView.setImageBitmap(bitmap);

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
        imageView.setImageBitmap(bitmap);*/

    }

    // 이미지를 갤러리에 저장하기
    private void galleryAddPic(Bitmap bitmap) {
        FileOutputStream fos;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ // API29
            ContentResolver resolver = getContentResolver();

            // 맵구조를 가진 ContentValues : 파일정보를 저장함
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,
                    "Image_" + "jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,
                    "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");

            Uri imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues );
            try {
                fos = (FileOutputStream) resolver
                        .openOutputStream(Objects.requireNonNull(imageUri));
                       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

            }catch (Exception e){

            }

        }else {
            // 이미지 파일을 스캔해서 갤러리에 저장하기 위한 인텐트
            Intent msIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            // 처음에 CreateFile 에서 생성해둔 이미지경로(imgFilePath)를 이용하여 파일객체를 만든다
            File f = new File(imgFilePath);
            Uri contentUri = Uri.fromFile(f);
            msIntent.setData(contentUri);
            // sendBroadcast 를 이용하여 저장
            this.sendBroadcast(msIntent);
        }
    }

}