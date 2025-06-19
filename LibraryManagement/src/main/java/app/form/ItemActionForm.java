package app.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ItemActionForm {
    /** 廃棄・紛失対象の個別資料ID */
    @NotNull(message="対象アイテムを選択してください")
    private Long itemId;

    /** アクション種別: "DISCARD" or "LOST" */
    @NotNull(message="処理種別を選択してください")
    private String actionType;

    /** 理由・備考 */
    @Size(max=200, message="備考は200文字以内で入力してください")
    private String note;

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}