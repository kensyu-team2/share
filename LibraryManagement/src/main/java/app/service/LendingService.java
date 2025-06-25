package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Item;
import app.entity.Lending;
import app.entity.Member;
import app.entity.Reservation;
import app.entity.Status;
import app.form.LendingForm;
import app.repository.ItemRepository;
import app.repository.LendingRepository;
import app.repository.MemberRepository;
import app.repository.ReservationRepository;
import app.repository.StatusRepository;

@Service
public class LendingService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final LendingRepository lendingRepository;
    private final StatusRepository statusRepository;
    private final ReservationRepository reservationRepository;

    private static final int LENDING_PERIOD_WEEKS = 2;
    private static final int LENDING_LIMIT = 10;

    public LendingService(MemberRepository memberRepository, ItemRepository itemRepository,
                          LendingRepository lendingRepository, StatusRepository statusRepository,
                          ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.lendingRepository = lendingRepository;
        this.statusRepository = statusRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void executeLending(LendingForm form) {

        Set<String> uniqueItemIds = new HashSet<>(form.getItemIds());
        if (uniqueItemIds.size() != form.getItemIds().size()) {
            throw new RuntimeException("同一資料IDが入力されています。");
        }

        Integer memberId = Integer.parseInt(form.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException(" 存在しない会員IDが入力されています。"));
        if (member.getRetireDate() != null) {
            throw new RuntimeException(" この会員は退会済みです。");
        }

        long currentLoans = lendingRepository.countByMemberAndReturnDateIsNull(member);
        if (currentLoans + form.getItemIds().size() > LENDING_LIMIT) {
            throw new RuntimeException(" 貸出上限(" + LENDING_LIMIT + "冊)を超えています。");
        }

        Status statusLent = statusRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("「貸出中」ステータス(ID:2)がDBに存在しません。"));
        Status statusInStock = statusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("「在庫あり」ステータス(ID:1)がDBに存在しません。"));
        Status statusReserved = statusRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("「予約確保中」ステータス(ID:3)がDBに存在しません。"));

        List<Item> itemsToLend = new ArrayList<>();
        for (String itemIdStr : form.getItemIds()) {
            Integer itemId = Integer.parseInt(itemIdStr);
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException(" 存在しない資料IDです (" + itemId + ")"));

            if (item.getStatus().equals(statusInStock)) {
                // --- 【ケースA】ステータスが「在庫あり」の場合 ---
                // お客様のルール「棚にある資料に予約は入っていない」に基づき、無条件で貸出OK。
                // 予約のチェックは一切行わない。

            } else if (item.getStatus().equals(statusReserved)) {
                // --- 【ケースB】ステータスが「予約確保中」の場合 ---
                // この時だけ、予約者本人かを確認します。

                List<Reservation> reservations = reservationRepository.findByBookOrderByReserveDateAsc(item.getBook());

                if (reservations.isEmpty()) {
                    throw new IllegalStateException(" 資料(ID:" + itemId + ")は予約確保中ですが、予約情報が見つかりません。");
                }

                Member highestPriorityMember = reservations.get(0).getMember();

                if (highestPriorityMember.getMemberId().equals(member.getMemberId())) {
                    // 本人確認が取れたので、予約を消化します。
                    reservationRepository.delete(reservations.get(0));
                } else {
                    // 本人ではない場合、予約者リストにいるか否かでメッセージを分ける
                    boolean isBorrowerInList = reservations.stream()
                        .anyMatch(r -> r.getMember().getMemberId().equals(member.getMemberId()));

                    if (isBorrowerInList) {
                        // 予約者だが優先度が低い場合
                        throw new RuntimeException(" この資料は他の会員(ID:" + highestPriorityMember.getMemberId() + ")が先に予約しています。");
                    } else {
                        // そもそも予約していない場合
                        throw new RuntimeException(" この資料は他の会員によって予約されています。");
                    }
                }

            } else {
                // --- 【ケースC】上記以外のすべてのステータス（貸出中、廃棄済など）の場合 ---
                // 貸出NGとし、現在の正しいステータス名を返します。
                throw new RuntimeException(" 資料(ID:" + itemId + ")は貸出できません。現在の状態: " + item.getStatus().getStatusName());
            }

            // すべてのチェックを通過した資料だけがリストに追加されます
            itemsToLend.add(item);
            System.out.println("--- デバッグ終了：資料ID " + itemId + " は貸出OK ---");
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusWeeks(LENDING_PERIOD_WEEKS);

        for (Item item : itemsToLend) {
            Lending newLending = new Lending();
            newLending.setMember(member);
            newLending.setItem(item);
            newLending.setLendDate(today);
            newLending.setDueDate(dueDate);
            lendingRepository.save(newLending);

            // ループの外で宣言した変数を利用する
            item.setStatus(statusLent);
            itemRepository.save(item);
        }
    }

    public Member findMemberById(Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会員が見つかりません: " + memberId));
    }

    public List<Item> findItemsByIds(List<String> itemIdStrings) {
        List<Integer> itemIds = itemIdStrings.stream()
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());
        return itemRepository.findAllById(itemIds);
    }



    /**
     * すべての貸出履歴を、ページネーションとソートを適用して取得します。
     */
    public Page<Lending> findAllLendingHistory(Pageable pageable) {
        return lendingRepository.findAll(pageable);
    } // ★★★ 閉じ括弧をここに移動 ★★★

    /**
     * 特定の書籍(bookId)に関する全貸出履歴を取得します。
     * @param bookId 検索対象の書籍ID
     * @return 貸出エンティティのリスト
     */
    public List<Lending> findLendingHistoryByBookId(Integer bookId) {
        return lendingRepository.findByItem_Book_BookIdOrderByLendDateDesc(bookId);
    }

    public List<Lending> getOverdueLendings() {
        return lendingRepository.findOverdueLendings(LocalDate.now());
    }

}
