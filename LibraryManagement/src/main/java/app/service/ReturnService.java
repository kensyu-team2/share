package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Item;
import app.entity.Lending;
import app.entity.Status;
import app.form.ReturnForm; // 返却用のForm
import app.repository.ItemRepository;
import app.repository.LendingRepository;
import app.repository.StatusRepository;

@Service
public class ReturnService {

    private final ItemRepository itemRepository;
    private final LendingRepository lendingRepository;
    private final StatusRepository statusRepository;

    public ReturnService(ItemRepository itemRepository,
                         LendingRepository lendingRepository,
                         StatusRepository statusRepository) {
        this.itemRepository = itemRepository;
        this.lendingRepository = lendingRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional
    public List<Lending> executeReturn(ReturnForm form) {
        List<String> nonEmptyItemIds = form.getItemIds().stream()
            .filter(id -> id != null && !id.trim().isEmpty())
            .collect(Collectors.toList());

        if (nonEmptyItemIds.isEmpty()) {
            throw new RuntimeException("返却する資料が指定されていません。");
        }

        Status statusInStock = statusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("「在庫あり」ステータス(ID:1)がDBに存在しません。"));

        // ★★★ここから追加★★★
        // 処理した貸出記録を格納するためのリストを用意
        List<Lending> processedLendings = new ArrayList<>();
        // ★★★ここまで追加★★★

        for (String itemIdStr : nonEmptyItemIds) {
            Integer itemId = Integer.parseInt(itemIdStr);
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("エラー: 存在しない資料IDです (" + itemId + ")"));

            Lending lendingRecord = lendingRepository.findByItemAndReturnDateIsNull(item)
                    .orElseThrow(() -> new RuntimeException("エラー: この資料(ID:" + itemId + ")は貸出中ではありません。"));

            lendingRecord.setReturnDate(LocalDate.now());
            lendingRepository.save(lendingRecord);

            item.setStatus(statusInStock);
            itemRepository.save(item);

            // ★★★ここから追加★★★
            // 処理が完了した貸出記録をリストに追加
            processedLendings.add(lendingRecord);
            // ★★★ここまで追加★★★
        }

        // ★★★ここから追加★★★
        // 処理した貸出記録のリストをControllerに返す
        return processedLendings;
        // ★★★ここまで追加★★★
    }

    public List<Lending> prepareReturnConfirmation(ReturnForm form) {
        // フォームから空のIDを取り除く
        List<String> nonEmptyItemIds = form.getItemIds().stream()
            .filter(id -> id != null && !id.trim().isEmpty())
            .collect(Collectors.toList());

        // ★★★ ここからが修正箇所です ★★★
        // 1. 重複を除外するために、リストをSetに変換する
        Set<String> uniqueItemIds = new HashSet<>(nonEmptyItemIds);
        // ★★★ ここまで ★★★

        // 重複を除いた結果、IDが1つもなければ空のリストを返す
        if (uniqueItemIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Lending> lendingList = new ArrayList<>();
        for (String itemIdStr : nonEmptyItemIds) {
            Integer itemId = Integer.parseInt(itemIdStr);
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("エラー: 存在しない資料IDです (" + itemId + ")"));

            // その資料の、現在貸出中の記録を探す
            Lending lendingRecord = lendingRepository.findByItemAndReturnDateIsNull(item)
                    .orElseThrow(() -> new RuntimeException("エラー: この資料(ID:" + itemId + ")は貸出中ではありません。"));

            lendingList.add(lendingRecord);
        }

        return lendingList;
    }
}