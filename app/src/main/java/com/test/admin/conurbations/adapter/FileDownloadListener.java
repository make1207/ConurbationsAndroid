package com.test.admin.conurbations.adapter;


import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.TasksManager;

/**
 * Author   : D22434
 * version  : 2018/1/25
 * function :
 */

public class FileDownloadListener extends FileDownloadSampleListener {

    private static final String TAG = "FileDownloadListener";

    private TaskItemAdapter.TaskItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
        final TaskItemAdapter.TaskItemViewHolder tag = (TaskItemAdapter.TaskItemViewHolder) task.getTag();
        if (tag != null && tag.id != task.getId()) {
            return null;
        }
        return tag;
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        super.pending(task, soFarBytes, totalBytes);
        LogUtil.d(TAG, "pending:" + task.getId());
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }

        tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                , totalBytes);
        tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
    }

    @Override
    protected void started(BaseDownloadTask task) {
        super.started(task);
        LogUtil.d(TAG, "started:" + task.getId());
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }
        tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
    }

    @Override
    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
        LogUtil.d(TAG, "connected:" + task.getId() + "-" + soFarBytes + "-" + totalBytes);
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }

        tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                , totalBytes);
        tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        super.progress(task, soFarBytes, totalBytes);
        LogUtil.d(TAG, "progress:" + task.getId() + "-" + soFarBytes + "-" + totalBytes);
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }
        tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                , totalBytes);
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        super.error(task, e);
        LogUtil.d(TAG, "error:" + task.getId() + "-" + e.getMessage());
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }

        tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                , task.getLargeFileTotalBytes());
        TasksManager.getInstance().removeTaskForViewHolder(task.getId());
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        super.paused(task, soFarBytes, totalBytes);
        LogUtil.d(TAG, "paused:" + task.getId() + "-" + soFarBytes + "-" + totalBytes);
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }

        tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
        tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
        TasksManager.getInstance().removeTaskForViewHolder(task.getId());
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        super.completed(task);
        LogUtil.d(TAG, "completed:" + task.getId() + "-" + task.getStatus());
        ToastUtils.getInstance().showToast(task.getFilename() + " 下载完成");
        final TaskItemAdapter.TaskItemViewHolder tag = checkCurrentHolder(task);
        if (tag == null) {
            return;
        }
        tag.updateDownloaded();
        TasksManager.getInstance().removeTaskForViewHolder(task.getId());
    }

}
