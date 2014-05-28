package com.sky.expense.DataBase;

import android.content.Context;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class BackUp {

    private String SHARED_PREFS;
    private String DATABASES;
    private String APP_PATH;
    private Context mContext;
    private String BACKUP_PATH;
    private String BACKUP_DATABASES;
    private String BACKUP_SHARED_PREFS;

    public BackUp(Context context) {
        mContext = context;
        String packageName = context.getPackageName();
        APP_PATH = new StringBuilder("/data/data/").append(packageName)
                .toString();
        SHARED_PREFS = APP_PATH + "/shared_prefs";
        DATABASES = APP_PATH + "/databases";
        BACKUP_PATH = "/sdcard/sky/backup/";
        BACKUP_PATH += packageName;
        BACKUP_DATABASES = BACKUP_PATH + "/database";
        BACKUP_SHARED_PREFS = BACKUP_PATH + "/shared_prefs";
    }

    /**
     * 备份文件 
     *
     * @return 当且仅当数据库及配置文件都备份成功时返回true。 
     */
    public boolean doBackup() {
        return backupDB();
    }

    private boolean backupDB() {
        return copyDir(DATABASES, BACKUP_DATABASES);
    }

    private boolean backupSharePrefs() {
        return copyDir(DATABASES, BACKUP_DATABASES);
    }

    /**
     * 恢复 
     *
     * @return 当且仅当数据库及配置文件都恢复成功时返回true。 
     */
    public boolean doRestore() {
        return restoreDB();
    }

    private boolean restoreDB() {
        return copyDir(BACKUP_DATABASES, DATABASES);
    }

    private boolean restoreSharePrefs() {
        return copyDir(BACKUP_SHARED_PREFS, SHARED_PREFS);
    }

    /**
     * 复制目录 
     *
     * @param srcDir
     *            源目录 
     * @param destDir
     *            目标目录 
     * @return 当复制成功时返回true, 否则返回false。
     */
    private final boolean copyDir(String srcDir, String destDir) {
        try {
            FileUtils.copyDirectory(new File(srcDir), new File(destDir));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}  