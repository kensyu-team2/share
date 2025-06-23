package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // SpringのTransactionalが推奨されます

import app.entity.Item;
import app.entity.Lending;
import app.entity.Member;
import app.entity.Status;
import app.form.LendingForm;
import app.repository.ItemRepository;
import app.repository.LendingRepository;
import app.repository.MemberRepository;
import app.repository.StatusRepository;

@Service
public class LendingService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final LendingRepository lendingRepository;
    private final StatusRepository statusRepository;

    private static final int LENDING_PERIOD_WEEKS = 2;
    private static final int LENDING_LIMIT = 10;

    public LendingService(MemberRepository memberRepository, ItemRepository itemRepository,
                          LendingRepository lendingRepository, StatusRepository statusRepository) {
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.lendingRepository = lendingRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional
    public void executeLending(LendingForm form) {

        Set<String> uniqueItemIds = new HashSet<>(form.getItemIds());
        if (uniqueItemIds.size() != form.getItemIds().size()) {
            throw new RuntimeException("同一の資料IDが入力されています。");
        }

        Integer memberId = Integer.parseInt(form.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("エラー: 存在しない会員IDです。"));
        if (member.getRetireDate() != null) {
            throw new RuntimeException("エラー: この会員は退会済みです。");
        }

        long currentLoans = lendingRepository.countByMemberAndReturnDateIsNull(member);
        if (currentLoans + form.getItemIds().size() > LENDING_LIMIT) {
            throw new RuntimeException("エラー: 貸出上限(" + LENDING_LIMIT + "冊)を超えています。");
        }

        Status statusLent = statusRepository.findById(2).orElseThrow(() -> new RuntimeException("「貸出中」ステータスがDBに存在しません。"));
        Status statusInStock = statusRepository.findById(1).orElseThrow(() -> new RuntimeException("「在庫あり」ステータスがDBに存在しません。"));

        List<Item> itemsToLend = new ArrayList<>();
        for (String itemIdStr : form.getItemIds()) {
            // ★★★ ここが修正点です ★★★
            // Long.parseLong() から Integer.parseInt() に修正
            Integer itemId = Integer.parseInt(itemIdStr);
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("エラー: 存在しない資料IDです (" + itemId + ")"));

            if (!item.getStatus().equals(statusInStock)) {
                 throw new RuntimeException("エラー: 資料(ID:" + itemId + ")は貸出できません。現在の状態: " + item.getStatus().getStatusName());
            }
            itemsToLend.add(item);
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

            item.setStatus(statusLent);
            itemRepository.save(item);
        }
    }
}