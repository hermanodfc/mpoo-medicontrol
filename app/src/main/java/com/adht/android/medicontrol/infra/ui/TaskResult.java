package com.adht.android.medicontrol.infra.ui;

import com.adht.android.medicontrol.R;

public class TaskResult {
    public static final TaskResult SUCCESS = new TaskResult(TaskResultType.SUCCESS, R.string.msg_sucesso);
    public static final TaskResult FAIL = new TaskResult(TaskResultType.FAIL, R.string.msg_falha);
    public static final TaskResult CANCEL = new TaskResult(TaskResultType.CANCEL, R.string.msg_cancelado);

    private Integer resId;
    private String msg;
    private TaskResultType type;

    public TaskResult(TaskResultType type, int resId) {
        this.type = type;
        this.resId = resId;
    }

    public TaskResult(TaskResultType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public TaskResultType getType() {
        return type;
    }

    public String getMsg() {
        if (this.msg == null && this.resId != null) {
            this.msg = MediControlApp.getContext().getString(this.resId);
        }
        return this.msg;
    }
}
