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
import app.entity.Reservation; // ★★★ Reservationエンティティをインポート
import app.entity.Status;
import app.form.ReturnForm;
import app.repository.ItemRepository;
import app.repository.LendingRepository;
import app.repository.ReservationRepository; // ★★★ ReservationRepositoryをインポート
import app.repository.StatusRepository;

@Service
public class ReturnService {

	private final ItemRepository itemRepository;
	private final LendingRepository lendingRepository;
	private final StatusRepository statusRepository;
	private final ReservationRepository reservationRepository; // ★★★ ReservationRepositoryのフィールドを追加

	// コンストラクタを修正して、ReservationRepositoryを受け取るようにします
	public ReturnService(ItemRepository itemRepository,
			LendingRepository lendingRepository,
			StatusRepository statusRepository,
			ReservationRepository reservationRepository) { // ★★★ 引数を追加
		this.itemRepository = itemRepository;
		this.lendingRepository = lendingRepository;
		this.statusRepository = statusRepository;
		this.reservationRepository = reservationRepository; // ★★★ 追加
	}

	@Transactional
	public List<Lending> executeReturn(ReturnForm form) {
		// --- 重複IDを除外する処理（変更なし） ---
		Set<String> uniqueItemIds = new HashSet<>(form.getItemIds().stream()
				.filter(id -> id != null && !id.trim().isEmpty())
				.collect(Collectors.toList()));

		if (uniqueItemIds.isEmpty()) {
			throw new RuntimeException("返却する資料が指定されていません。");
		}

		// --- ステータスオブジェクトを先に取得 ---
		Status statusInStock = statusRepository.findById(1)
				.orElseThrow(() -> new RuntimeException("「在庫あり」ステータス(ID:1)がDBに存在しません。"));
		Status statusReserved = statusRepository.findById(3) // ★★★「予約確保中」ステータスも取得
				.orElseThrow(() -> new RuntimeException("「予約確保中」ステータス(ID:3)がDBに存在しません。"));

		List<Lending> processedLendings = new ArrayList<>();

		for (String itemIdStr : uniqueItemIds) { // 重複を除外したリストでループ
			Integer itemId = Integer.parseInt(itemIdStr);
			Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new RuntimeException("エラー: 存在しない資料IDです (" + itemId + ")"));

			// --- 貸出記録を更新（変更なし） ---
			Lending lendingRecord = lendingRepository.findByItemAndReturnDateIsNull(item)
					.orElseThrow(() -> new RuntimeException("エラー: この資料(ID:" + itemId + ")は貸出中ではありません。"));
			lendingRecord.setReturnDate(LocalDate.now());
			lendingRepository.save(lendingRecord);

			// ★★★ ここからが、お客様のロジックを実装する部分です ★★★

			// 1. この資料の書籍(Book)に紐づく予約をすべて取得します
			List<Reservation> reservations = reservationRepository.findByBookOrderByReserveDateAsc(item.getBook());

			// 2. この書籍の、すでに「予約確保中」になっている他の資料の数を数えます
			// ※このためには、ItemRepositoryに新しいメソッドが必要です
			long numItemsAlreadySetAside = itemRepository.countByBookBookIdAndStatusStatusId(item.getBook().getBookId(),
					3);

			// 3. 「予約の総数」が「すでに確保済みの数」を上回っているか？
			if (reservations.size() > numItemsAlreadySetAside) {
				// 上回っている場合、この返却された資料を次の予約者のために確保します
				item.setStatus(statusReserved); // ステータスを「予約確保中」に設定
			} else {
				// 予約がない、または全ての予約者に資料が確保済みの場合、フリーな在庫に戻します
				item.setStatus(statusInStock); // ステータスを「在庫あり」に設定
			}

			itemRepository.save(item); // 変更したステータスを保存
			processedLendings.add(lendingRecord);
		}

		return processedLendings;
	}

	// --- prepareReturnConfirmation メソッドは、この修正に合わせて調整が必要です ---
	public List<Lending> prepareReturnConfirmation(ReturnForm form) {
		Set<String> uniqueItemIds = new HashSet<>(form.getItemIds().stream()
				.filter(id -> id != null && !id.trim().isEmpty())
				.collect(Collectors.toList()));

		if (uniqueItemIds.isEmpty()) {
			return new ArrayList<>();
		}

		List<Lending> lendingList = new ArrayList<>();
		for (String itemIdStr : uniqueItemIds) { // 重複を除外したIDでループ
			Integer itemId = Integer.parseInt(itemIdStr);
			Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new RuntimeException("エラー: 存在しない資料IDです (" + itemId + ")"));
			Lending lendingRecord = lendingRepository.findByItemAndReturnDateIsNull(item)
					.orElseThrow(() -> new RuntimeException("エラー: この資料(ID:" + itemId + ")は貸出中ではありません。"));
			lendingList.add(lendingRecord);
		}
		return lendingList;
	}
}
