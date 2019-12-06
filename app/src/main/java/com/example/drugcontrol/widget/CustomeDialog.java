package com.wanxun.maker.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wanxun.maker.BuildConfig;
import com.wanxun.maker.R;
import com.wanxun.maker.activity.BaseActivity;
import com.wanxun.maker.interfaces.OnPermissionListener;

import java.util.List;

/**
 * 自定义的dialog
 */
public class CustomeDialog {

    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        TextView tv = v.findViewById(R.id.tipTextView);
        ProgressBar progressBar = v.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        if (TextUtils.isEmpty(msg)) {   //如果是空则使用默认的字符串
            tv.setText(context.getString(R.string.dialog_loading));
        } else {
            tv.setText(msg);
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 创建选择相片的对话框
     *
     * @param context           上下文
     * @param requestCodeCamera 拍照的请求码 默认传0，默认处理的code就是Constant.REQ_CODE_CAMEIA，否则就传自定义的code，然后处理的就是自己传进来的code
     * @param requestCodePick   选择图库的请求码 默认传0，默认处理的code就是Constant.REQ_CODE_PICK_IMAGE，否则就传自定义的code，然后处理的就是自己传进来的code
     * @param fragment          startActivityForResult   onActivityForResult响应的是fragment 还是 activity
     * @return
     */
    public static Dialog createSelectPicDialog(final Context context, final int requestCodeCamera, final int requestCodePick, final Fragment fragment) {
        return createSelectPicDialog(context, requestCodeCamera, requestCodePick, fragment, true);
    }

    /**
     * 创建选择相片的对话框
     *
     * @param context           上下文
     * @param requestCodeCamera 拍照的请求码 默认传0，默认处理的code就是Constant.REQ_CODE_CAMEIA，否则就传自定义的code，然后处理的就是自己传进来的code
     * @param requestCodePick   选择图库的请求码 默认传0，默认处理的code就是Constant.REQ_CODE_PICK_IMAGE，否则就传自定义的code，然后处理的就是自己传进来的code
     * @param fragment          startActivityForResult   onActivityForResult响应的是fragment 还是 activity
     * @param isDismiss         操作完不主动取消   default: ture
     * @return
     */
    public static Dialog createSelectPicDialog(final Context context, final int requestCodeCamera, final int requestCodePick, final Fragment fragment, final boolean isDismiss) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.photo_choose_dialog, null);
        TextView button1 = view.findViewById(R.id.bt_camara);
        TextView button2 = view.findViewById(R.id.bt_photo);
        TextView button3 = view.findViewById(R.id.bt_cancle);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.requestRunTimePermission(new String[]{Manifest.permission.CAMERA}, new OnPermissionListener() {
                        @Override
                        public void onGranted() {
                        //照相机
                        String state = Environment.getExternalStorageState();
                        if (!state.equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            getImageByCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, PathManager.getThumbImageFile(context));
                        } else {
                            imageUri = Uri.fromFile(PathManager.getThumbImageFile(context));
                        }
                        //指定照片存储路径 可以去PathManager.getThumbImageFile(context) 拿到file去拿Uri 注：原图 onActivityResult data.getData()拿到的是压缩过后略缩图
                        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        if (requestCodeCamera != 0) {   //传进来的code
                            if (fragment == null) {
                                ((Activity) context).startActivityForResult(getImageByCamera, requestCodeCamera);
                            } else {
                                fragment.startActivityForResult(getImageByCamera, requestCodeCamera);
                            }
                        } else {    //默认的code
                            if (fragment == null) {
                                ((Activity) context).startActivityForResult(getImageByCamera, Constant.REQ_CODE_CAMEIA);
                            } else {
                                fragment.startActivityForResult(getImageByCamera, Constant.REQ_CODE_CAMEIA);
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissionList, List<String> neverAskPermissionList) {
                        if (!neverAskPermissionList.isEmpty()) {
                            Toast.makeText(context, "请手动打开相机权限后再执行操作哦~", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "权限已被拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (isDismiss) {
                    dialog.dismiss();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                if (requestCodePick != 0) { //用户传进来的code
                    if (fragment == null) {
                        ((Activity) context).startActivityForResult(intent, requestCodePick);
                    } else {
                        fragment.startActivityForResult(intent, requestCodePick);
                    }
                } else {    //默认的code
                    if (fragment == null) {
                        ((Activity) context).startActivityForResult(intent, Constant.REQ_CODE_PICK_IMAGE);
                    } else {
                        fragment.startActivityForResult(intent, Constant.REQ_CODE_PICK_IMAGE);
                    }
                }
                if (isDismiss) {
                    dialog.dismiss();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog createMutilSelectPicDialog(final Activity activity, int maxSelectNum, List<LocalMedia> selectList) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.photo_choose_dialog, null);
        TextView button1 = view.findViewById(R.id.bt_camara);
        TextView button2 = view.findViewById(R.id.bt_photo);
        TextView button3 = view.findViewById(R.id.bt_cancle);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 单独拍照   BUG未查出
//                PictureSelector.create(activity)
//                        .openCamera(PictureMimeType.ofImage())
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
                BaseActivity.requestRunTimePermission(new String[]{Manifest.permission.CAMERA}, new OnPermissionListener() {
                    @Override
                    public void onGranted() {
                        //照相机
                        String state = Environment.getExternalStorageState();
                        if (!state.equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(activity, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            getImageByCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID, PathManager.getThumbImageFile(activity));
                        } else {
                            imageUri = Uri.fromFile(PathManager.getThumbImageFile(activity));
                        }
                        //指定照片存储路径 可以去PathManager.getThumbImageFile(context) 拿到file去拿Uri 注：原图 onActivityResult data.getData()拿到的是压缩过后略缩图
                        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        activity.startActivityForResult(getImageByCamera, Constant.REQ_CODE_CAMEIA);
                        dialog.dismiss();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissionList, List<String> neverAskPermissionList) {
                        if (!neverAskPermissionList.isEmpty()) {
                            Toast.makeText(activity, "请手动打开相机权限后再执行操作哦~", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "权限已被拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(activity)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.pictureseleted)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .isCamera(false)// 是否显示拍照按钮
                        .compress(true)// 是否压缩
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .selectionMedia(selectList)// 是否传入已选图片
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                dialog.dismiss();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }


    //创建带ok和cancel两个按钮的dialog
    public static Dialog showOkCancelAlertDialog(boolean canCancel, String title, String content, String okText, String cancelText,
                                                 final View.OnClickListener okListener, final View.OnClickListener cancelListener) {
        Context context = AppStackUtils.getInstance().getContext();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setCancelable(canCancel);
        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelListener == null) {
                    return;
                }
                cancelListener.onClick(null);
            }
        });
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (okListener == null) {
                    return;
                }
                okListener.onClick(null);
            }
        });
        AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.item_tv_color_03));
        return dialog;
    }

    //创建只有OK按钮的dialog
    public static Dialog showOkAlertDialog(boolean canCancel, String title, String content, String okText, final View.OnClickListener onClickListener) {
        Context context = AppStackUtils.getInstance().getContext();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setCancelable(canCancel);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClickListener == null) {
                    return;
                }
                onClickListener.onClick(null);
            }
        });
        AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        return dialog;
    }

    public static Dialog showCompetitionJoinDialog(View.OnClickListener okListener) {
        Context context = AppStackUtils.getInstance().getContext();
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_competition_join, null);
        TextView tvTitle = contentView.findViewById(R.id.tvWebSite);
        tvTitle.setText(((BaseActivity) context).getSharedFileUtils().getHostUrl());
        View submit = contentView.findViewById(R.id.sure_btn_tv);
        submit.setOnClickListener(okListener);
        Dialog okCancelAlertDialog = new Dialog(context, R.style.Dialog);
        Window win = okCancelAlertDialog.getWindow();
        win.setContentView(contentView);
        okCancelAlertDialog.setCancelable(false);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = okCancelAlertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics())); //设置宽度
        okCancelAlertDialog.getWindow().setAttributes(lp);
        return okCancelAlertDialog;
    }
}
