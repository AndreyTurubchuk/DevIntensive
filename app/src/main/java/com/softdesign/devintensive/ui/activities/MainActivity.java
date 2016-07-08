package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private  static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private ImageView mPhone, mEmail, mVk, mGit;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private DataManager mDataManager;
    private int mCurrentEditMode = 0;
    private FloatingActionButton mFab;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private ImageView mProfileImage;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;
    private RelativeLayout mProfilePlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mDataManager = DataManager.getInstance();

        mPhone = (ImageView)findViewById(R.id.phone_view);
        mEmail = (ImageView)findViewById(R.id.email_view);
        mVk = (ImageView)findViewById(R.id.Vk_view);
        mGit = (ImageView)findViewById(R.id.git_view);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

        mUserPhone = (EditText)findViewById(R.id.phone_et);
        mUserMail = (EditText)findViewById(R.id.email_et);
        mUserVk = (EditText)findViewById(R.id.VK_et);
        mUserGit = (EditText)findViewById(R.id.git_et);
        mUserBio = (EditText)findViewById(R.id.bio_et);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mPhone.setOnClickListener(this);
        mEmail.setOnClickListener(this);
        mVk.setOnClickListener(this);
        mGit.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.userfoto) //TO DO  07.07.2016 сделать плейсхолдеры и transform
                .into(mProfileImage);

        if (savedInstanceState == null){//Активити запускается впервые
          } else { //Активити уже создавалось
           mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
           changeEditMode(mCurrentEditMode);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }


  @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.phone_view:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mUserPhone.getText().toString()));
                startActivity(intent);
                break;

            case R.id.email_view:
                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.setType("plain/text");
                intent2.putExtra(Intent.EXTRA_EMAIL, mUserMail.getText().toString());
                startActivity(Intent.createChooser(intent2, "Выберите email-клиент"));
                break;

            case R.id.Vk_view:
                Uri uriVk3 = Uri.parse("http://" + mUserVk.getText().toString());
               Intent vkIntent3 = new Intent(Intent.ACTION_VIEW, uriVk3);
                startActivity(vkIntent3);
                break;

            case R.id.git_view:
                Uri uriVk4 = Uri.parse("http://" + mUserGit.getText().toString());
                Intent vkIntent4 = new Intent(Intent.ACTION_VIEW, uriVk4);
                startActivity(vkIntent4);
                break;
        }
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void changeEditMode(int mode) {
        if(mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
            userValue.setEnabled(true);
            userValue.setFocusable(true);
            userValue.setFocusableInTouchMode(true);

            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            }
        }
        else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
            userValue.setEnabled(false);
            userValue.setFocusable(false);
            userValue.setFocusableInTouchMode(false);
            hideProfilePlaceholder();
            unlockToolbar();
            saveUserInfoValue();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            }
        }
    }
    private void loadUserInfoValue(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }
      private void setupDrawer(){
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                                showSnackbar(item.getTitle().toString());
                                item.setChecked(true);
                                mNavigationDrawer.closeDrawer(GravityCompat.START);
                                return false;
                            }
                    });
      }
   private void showSnackbar(String message){
       Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
   }

    //получение результатов из другой Activity (фото из камеры или фотогалереи)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
                        case ConstantManager.REQUEST_GALLERY_PICTURE:
                                if (resultCode == RESULT_OK && data != null) {
                                        mSelectedImage = data.getData();
                                        insertProfileImage(mSelectedImage);
                                }
                                break;

                        case ConstantManager.REQUEST_CAMERA_PICTURE:
                                if (resultCode == RESULT_OK && mPhotoFile != null) {
                                        mSelectedImage = Uri.fromFile(mPhotoFile);
                                        insertProfileImage(mSelectedImage);
                                }
        }

    }


    private void loadPhotoFromGalery(){
        Intent takeIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);


    }

    private void loadPhotoFromCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

          Snackbar.make(mCoordinatorLayout, "Для корректной работы приложения необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                  .setAction("Разрешить", new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          openApplicationSettings();
                      }
                  }) .show();

        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
           if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
               //TODO 07.07.2016 обрабатываем разрешение (разрешение получено)
               // например вывести сообщение или обработать какой-то логикой
           }

           if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
               //TODO 07.07.2016 обрабатываем разрешение (разрешение получено)
               // например вывести сообщение или обработать какой-то логикой
           }
       }

    }

    private void hideProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.GONE);

    }

    private void showProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.VISIBLE);

    }

    private void lockToolbar(){
        mAppBarLayout.setExpanded(true,true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    private void unlockToolbar(){
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case ConstantManager.LOAD_PROFILE_PHOTO:
             String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int choiceItem) {
                                switch (choiceItem){
                                    case 0:
                                        //Загрузить из галереи
                                        loadPhotoFromGalery();
                                      //  showSnackbar("Загрузить из галереи");
                                        break;
                                    case 1:
                                        //Загрузить из камеры
                                        loadPhotoFromCamera();
                                       // showSnackbar("Загрузить из камеры");
                                        break;
                                    case 2:
                                        //отменить
                                        dialog.cancel();
                                      //  showSnackbar("Отмена");
                                        break;
                                }
                            }
                });
             return builder.create();
        default: return null;
        }
    }

    private File createImageFile()  throws IOException{
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;

    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        //TO DO  07.07.2016 сделать плейсхолдеры и transform
              //.placeholder(R.drawable. .profile_image);
                //
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);

    }

    public void openApplicationSettings(){
        Intent appSettingsIntent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

}
