package app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Item;
import app.entity.Status;
import app.form.ItemActionForm;
import app.repository.ItemRepository;
import app.repository.StatusRepository;
// import app.form.ItemActionForm; // 次のステップで使用

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StatusRepository statusRepository;

    /**
     * IDをキーにItemを1件取得する
     * @param itemId
     * @return 該当するItemエンティティ
     */
    public Item findById(Integer itemId) {
        // findByIdはOptional<T>を返すので、見つからなかった場合の例外処理を追加
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("該当する個別資料が見つかりません。 Item ID: " + itemId));
    }

    /**
     * 廃棄・紛失処理を実行する
     * @param form 画面から入力された情報
     * @return 更新されたItemエンティティ
     */
    public Item processItemDisposalOrLoss(ItemActionForm form) {
        // 1. 対象のItemを取得
        Item item = findById(form.getItemId());

        // 2. ビジネスルールチェック（貸出中のものは廃棄・紛失できない）
        if (item.getStatus().getStatusId() == 2) { // 2:貸出中
            throw new IllegalStateException("この資料は現在貸出中のため、処理できません。");
        }

        // 3. フォームで選択された処理タイプに応じて、ステータスと日付を更新
        String actionType = form.getActionType();
        LocalDate actionDate = form.getActionDate();

        if ("dispose".equals(actionType)) {
            // 廃棄処理
            Status disposedStatus = statusRepository.findById(6) // 6:廃棄済
                    .orElseThrow(() -> new IllegalStateException("「廃棄済」ステータスが見つかりません。"));
            item.setStatus(disposedStatus);
            item.setDeleteDate(actionDate);

        } else if ("lose".equals(actionType)) {
            // 紛失処理
            Status lostStatus = statusRepository.findById(5) // 5:紛失
                    .orElseThrow(() -> new IllegalStateException("「紛失」ステータスが見つかりません。"));
            item.setStatus(lostStatus);
            item.setLostDate(actionDate);
        } else {
            throw new IllegalArgumentException("無効な処理タイプです。");
        }

        // 4. 更新したItemを保存し、返す
        return itemRepository.save(item);
    }
}